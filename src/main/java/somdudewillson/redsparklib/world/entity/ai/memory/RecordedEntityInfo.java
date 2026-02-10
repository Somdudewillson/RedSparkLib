package somdudewillson.redsparklib.world.entity.ai.memory;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.INBTSerializable;
import somdudewillson.redsparklib.common.utils.ExtNbtUtils;
import somdudewillson.redsparklib.common.utils.OptNbtUtils;
import somdudewillson.redsparklib.common.utils.RegistryNbtUtils;
import somdudewillson.redsparklib.serialization.codecs.NbtSerializableCodec;

@NoArgsConstructor
@Data
@Setter(value = AccessLevel.PROTECTED)
public class RecordedEntityInfo implements INBTSerializable<CompoundTag> {
	public static final Codec<RecordedEntityInfo> CODEC = NbtSerializableCodec.compound(RecordedEntityInfo.class, RecordedEntityInfo::new);
	
	@Nullable
	protected Entity entity;
	@Nullable
	protected EntityType<?> entityType;
	@Nullable
	protected AABB entitySize;
	protected UUID entityId;
	protected boolean living;
	protected Vec3 lastSeenPos = Vec3.ZERO;
	protected long lastSeenTime = 0l;
	protected long calculatedDataLastUpdatedTime = 0l;
	
	public RecordedEntityInfo(Entity entity) {
		update(entity, true);
	}
	public RecordedEntityInfo(Entity entity, long currentTime) {
		update(entity, currentTime);
	}
	
	public RecordedEntityInfo update(Entity entity, long currentTime) {
		this.entity = entity;
		this.entityType = entity.getType();
		this.entitySize = entity.getBoundingBox().move(entity.getBoundingBox().getCenter().reverse());
		this.entityId = entity.getUUID();
		this.living = entity instanceof LivingEntity;
		this.lastSeenPos = entity.position();
		this.lastSeenTime = currentTime;
		if (shouldUpdateCalculatedData(entity, currentTime-this.calculatedDataLastUpdatedTime)) {
			updateCalculatedData(entity);
			this.calculatedDataLastUpdatedTime = currentTime;
		}
		
		return this;
	}
	public RecordedEntityInfo update(Entity entity, boolean force) {
		Level level = entity.level();
		if (level == null) {
			return force ? update(entity, lastSeenTime) : this;
		}
		return update(entity, level.getGameTime());
	}
	public RecordedEntityInfo update(Entity entity) { return update(entity, false); }
	
	@Nullable
	public RecordedEntityInfo updateAndReturnIfUseful(Entity entity, long currentTime) {
		update(entity, currentTime);
		return hasUsefulInfo() ? this : null;
	}
	@Nullable
	public RecordedEntityInfo updateAndReturnIfUseful(Entity entity, boolean force) {
		update(entity, force);
		return hasUsefulInfo() ? this : null;
	}
	@Nullable
	public RecordedEntityInfo updateAndReturnIfUseful(Entity entity) {
		return updateAndReturnIfUseful(entity, false);
	}
	
	@Nullable
	public static RecordedEntityInfo createAndReturnIfUseful(Entity entity) {
		RecordedEntityInfo newEntityInfo = new RecordedEntityInfo(entity);
		return newEntityInfo.hasUsefulInfo() ? newEntityInfo : null;
	}
	@Nullable
	public static RecordedEntityInfo createAndReturnIfUseful(Entity entity, long currentTime) {
		RecordedEntityInfo newEntityInfo = new RecordedEntityInfo(entity, currentTime);
		return newEntityInfo.hasUsefulInfo() ? newEntityInfo : null;
	}
	
	@Nullable
	public static RecordedEntityInfo updateOrCreateAndReturnIfUseful(@Nullable RecordedEntityInfo potentialExistingInfo, Entity entity) {
		return potentialExistingInfo!=null ? potentialExistingInfo.updateAndReturnIfUseful(entity) : createAndReturnIfUseful(entity);
	}
	@Nullable
	public static RecordedEntityInfo updateOrCreateAndReturnIfUseful(@Nullable RecordedEntityInfo potentialExistingInfo, Entity entity, long currentTime) {
		return potentialExistingInfo!=null ? potentialExistingInfo.updateAndReturnIfUseful(entity, currentTime) : createAndReturnIfUseful(entity, currentTime);
	}
	
	public boolean isValid() {
		return entityId!=null && lastSeenPos!=null;
	}
	
	public boolean hasUsefulInfo() {
		return true;
	}

	private boolean shouldUpdateCalculatedData(Entity entity, long timeSinceLastUpdate) { return timeSinceLastUpdate>10; }
	protected void updateCalculatedData(Entity entity) { }
	
	public Optional<Entity> tryGetEntity() { return Optional.ofNullable(entity); }
	public Optional<EntityType<?>> tryGetEntityType() { return Optional.ofNullable(entityType); }
	public Optional<AABB> tryGetEntitySize() { return Optional.ofNullable(entitySize); }
	
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag root = new CompoundTag();
		
		root.putUUID("uuid", entityId);
		root.putBoolean("is_living", living);
		OptNbtUtils.put(root, "entity_type", tryGetEntityType().map(RegistryNbtUtils::serializeRegistryValue));
		OptNbtUtils.put(root, "entity_size", tryGetEntitySize().map(ExtNbtUtils::serializeBoundingBox));
		root.put("last_seen", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, lastSeenPos).getOrThrow(false, s->{}));
		root.putLong("last_seen_time", lastSeenTime);
		root.putLong("last_calc_update_time", calculatedDataLastUpdatedTime);
		
		return root;
	}
	
	@Override
	public void deserializeNBT(CompoundTag nbt) {
		entity = null;
		entityId = nbt.getUUID("uuid");
		living = nbt.getBoolean("living");
		entityType = OptNbtUtils.getCompound(nbt, "entity_type").flatMap(RegistryNbtUtils::tryDeserializeEntityType).orElse(null);
		entitySize = OptNbtUtils.getList(nbt, "entity_size", Tag.TAG_DOUBLE).map(ExtNbtUtils::deserializeBoundingBox).orElse(null);
		lastSeenPos = Vec3.CODEC.decode(NbtOps.INSTANCE, nbt.get("last_seen")).getOrThrow(false, s->{}).getFirst();
		lastSeenTime = nbt.getLong("last_seen_time");
		calculatedDataLastUpdatedTime = nbt.getLong("last_calc_update_time");
	}
}
