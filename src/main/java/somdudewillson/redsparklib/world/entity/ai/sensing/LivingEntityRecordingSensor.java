package somdudewillson.redsparklib.world.entity.ai.sensing;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;

import lombok.val;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import somdudewillson.redsparklib.common.RedSparkLibMemoryModules;
import somdudewillson.redsparklib.world.entity.ai.memory.RecordedEntityInfoMap;

public class LivingEntityRecordingSensor extends Sensor<LivingEntity> {

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.of(RedSparkLibMemoryModules.RECORDED_ENTITY_INFO.get());
	}
	
	@Override
	protected void doTick(ServerLevel pLevel, LivingEntity pEntity) {
		Brain<?> brain = pEntity.getBrain();
		if (!brain.hasMemoryValue(RedSparkLibMemoryModules.RECORDED_ENTITY_INFO.get())) {
			brain.setMemory(RedSparkLibMemoryModules.RECORDED_ENTITY_INFO.get(), new RecordedEntityInfoMap());
		}
		val existingEntityInfo = brain.getMemory(RedSparkLibMemoryModules.RECORDED_ENTITY_INFO.get()).orElseThrow();
		
		// Insert/update newly-seen entities
		AABB aabb = pEntity.getBoundingBox().inflate((double)this.radiusXZ(), (double)this.radiusY(), (double)this.radiusXZ());
		List<Entity> list = pLevel.getEntities(pEntity, aabb, EntitySelector.NO_SPECTATORS);
		list.stream()
			.filter(e -> (e instanceof LivingEntity))
			.filter(e -> pEntity.closerThan(pEntity, 4) || pEntity.hasLineOfSight(e))
			.forEach(e -> {
				synchronized(existingEntityInfo) {
					existingEntityInfo.putDataForEntity(pLevel, e);
				}
			});
		
		// Clear away outdated entity memories
		Set<UUID> presentUuids = list.stream()
				.map(Entity::getUUID)
				.collect(Collectors.toUnmodifiableSet());
		existingEntityInfo.getAllCurrentlyStoredUuids().stream()
			.filter(uuid->!presentUuids.contains(uuid))
			.map(existingEntityInfo::tryGetDataForEntityUuid)
			.filter(Optional::isPresent)
			.map(Optional::orElseThrow)
			.filter(entityInfo->entityInfo.getLastSeenTime()>getMinTimeoutAgeByEntityType(entityInfo.tryGetEntityType()))// Don't remove really new memories
			.filter(entityInfo->{// Only remove really old memories, really close memories, or those that are close and in LoS
				if ( entityInfo.getLastSeenTime()>getTimeoutAgeByEntityType(entityInfo.tryGetEntityType()) ) { return true; }
				
				val dist = entityInfo.getLastSeenPos().distanceToSqr(pEntity.position());
				return dist < 8*8 || (dist<16*16 && entityHasLineOfSightToPos(pEntity, entityInfo.getLastSeenPos()));
			})
			.forEach(existingEntityInfo::removeEntityInfo);
	}

	protected int radiusXZ() {
		return 64;
	}

	protected int radiusY() {
		return 32;
	}
	
	protected long getMinTimeoutAgeByEntityType(Optional<EntityType<?>> entityType) {
		return entityType.map(et -> {
			if (et == EntityType.PLAYER) {
				return Long.MAX_VALUE;
			}
			return null;
		}).orElse(100l);
	}
	protected long getTimeoutAgeByEntityType(Optional<EntityType<?>> entityType) {
		return entityType.map(et -> {
			if (et == EntityType.ITEM) {
				return (long) (20 * 60 * 2.5);
			}
			if (et == EntityType.PLAYER) {
				return Long.MAX_VALUE;
			}
			return null;
		}).orElse((long) (20 * 60 * 30));
	}
	
	protected boolean entityHasLineOfSightToPos(LivingEntity selfEntity, Vec3 targetPos) {
        Vec3 selfPos = new Vec3(selfEntity.getX(), selfEntity.getEyeY(), selfEntity.getZ());
        if (targetPos.distanceToSqr(selfPos) > 128.0D*128.0D) {
           return false;
        } else {
           return selfEntity.level().clip(new ClipContext(selfPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, selfEntity)).getType() == HitResult.Type.MISS;
        }
	}
}
