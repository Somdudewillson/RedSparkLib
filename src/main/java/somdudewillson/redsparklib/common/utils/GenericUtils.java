package somdudewillson.redsparklib.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import somdudewillson.redsparklib.RedSparkLib;

@SuppressWarnings("deprecation")
public class GenericUtils {
	public static <T> HashMap<T, Integer> arrayToInverseMap(T[] array) {
		HashMap<T, Integer> result = new HashMap<T, Integer>();
		for (int i = 0; i < array.length; i++) {
			result.put(array[i], i);
		}

		return result;
	}

	/**
	 * In-place Durstenfeld shuffle for arrays
	 */
	public static <T> void shuffleArray(T[] array, RandomSource rng) {
		for (int i = array.length - 1; i > 0; i--) {
			int index = rng.nextInt(i + 1);

			T a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}

	public static <T> void shuffleCollection(Collection<T> collection, RandomSource rng) {
		@SuppressWarnings("unchecked")
		T[] collectionAsArray = (T[]) collection.toArray();
		shuffleArray(collectionAsArray, rng);
		collection.clear();
		Arrays.stream(collectionAsArray).forEachOrdered(collection::add);
	}
	
	private static final Collector<?, ?, ?> SHUFFLER = Collectors.collectingAndThen(
	        Collectors.toCollection(ArrayList::new),
	        list -> {
	            Collections.shuffle(list);
	            return list;
	        }
	);

	@SuppressWarnings("unchecked")
	public static <T> Collector<T, ?, List<T>> toShuffledList() {
	    return (Collector<T, ?, List<T>>) SHUFFLER;
	}

	public static int distChebyshev(Vec3i vecA, Vec3i vecB) {
		int dist = Math.abs(vecA.getX() - vecB.getX());
		dist = Math.max(dist, Math.abs(vecA.getY() - vecB.getY()));
		dist = Math.max(dist, Math.abs(vecA.getZ() - vecB.getZ()));
		return dist;
	}

	public static int distChebyshev(ChunkPos vecA, ChunkPos vecB) {
		return Math.max(Math.abs(vecA.x - vecB.x), Math.abs(vecA.z - vecB.z));
	}

	@Getter(value = AccessLevel.PRIVATE, lazy = true)
	private static final Field mobEffectDurationField = ObfuscationReflectionHelper.findField(MobEffectInstance.class, "duration");
	private static void mapAndUpdateDuration(MobEffectInstance effectInstance, Int2IntFunction mapper) {

		Field durationField = GenericUtils.getMobEffectDurationField();
		int mappedDuration = effectInstance.mapDuration(mapper);
		if (durationField.canAccess(effectInstance) || durationField.trySetAccessible()) {
			try {
				durationField.setInt(effectInstance, mappedDuration);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				RedSparkLib.LOGGER.error("Failed to update MobEffectInstanceDuration", e);
			}
		} else {
			RedSparkLib.LOGGER.error("Unable to update MobEffectInstanceDuration");
		}
	}
	public static void mapAndUpdateDuration(LivingEntity entity, MobEffectInstance effectInstance, Int2IntFunction mapper) {
		mapAndUpdateDuration(entity, effectInstance, mapper, true);
	}
	public static void mapAndUpdateDuration(LivingEntity entity, MobEffectInstance effectInstance, Int2IntFunction mapper, boolean propagateToClient) {
		mapAndUpdateDuration(effectInstance, mapper);
		if (!entity.level().isClientSide() && propagateToClient) {
			entity.forceAddEffect(effectInstance, entity);
		}
	}
	
	public static String prettyPrintEnum(Enum<?> value) {
		return WordUtils.capitalizeFully(value.name().replaceAll("_", " "));
	}
	
	public static <E, T extends E> Optional<T> trySafeCast(E obj, Class<T> clazz) {
		return clazz.isInstance(obj) ? Optional.of(clazz.cast(obj)) : Optional.empty();
	}
	public static <E, T extends E> Function<E,Optional<T>> inlineTrySafeCast(Class<T> clazz) {
		return e->trySafeCast(e, clazz);
	}
	
	public static double calculateHealthFromPotionEffect(MobEffectInstance effectInstance) {
		int ticksPerTrigger;
		String descId = effectInstance.getEffect()/*? neoforge {*//*.value()*//*?}*/.getDescriptionId();
		String effectKey = descId.substring(descId.lastIndexOf('.') + 1);
		switch (effectKey) {
		case "instant_damage":
			return -1*3*Math.pow(2,effectInstance.getAmplifier())*effectInstance.getDuration();
		case "instant_health":
			return 1*2*Math.pow(2,effectInstance.getAmplifier())*effectInstance.getDuration();
		case "poison":
			ticksPerTrigger = (int) Math.round( 25*Math.pow(2,-effectInstance.getAmplifier()) );
			ticksPerTrigger = Math.max(1, ticksPerTrigger);
			return -1*0.5*(effectInstance.getDuration()/ticksPerTrigger);
		case "wither":
			ticksPerTrigger = (int) Math.round( 40*Math.pow(2,-effectInstance.getAmplifier()) );
			ticksPerTrigger = Math.max(1, ticksPerTrigger);
			return -1*0.75*(effectInstance.getDuration()/ticksPerTrigger);
		case "regeneration":
			ticksPerTrigger = (int) Math.round( 50*Math.pow(2,-effectInstance.getAmplifier()) );
			ticksPerTrigger = Math.max(1, ticksPerTrigger);
			return 1*0.5*(effectInstance.getDuration()/ticksPerTrigger);
		default:
			return 0;
		}
	}

	public static AABB entityBBoxAtPos(Entity entity, Vec3 pos) {
		AABB bbox = entity.getBoundingBox();
		return bbox.move( pos.subtract(entity.position()) );
	}
	public static AABB entityBBoxAtBlockPos(Entity entity, BlockPos pos) {
		return entityBBoxAtPos(entity, pos.getCenter().subtract(0, 0.5, 0));
	}
	
	public static void spawnParticleFromArea(ClientLevel level, RandomSource rng, ParticleOptions particleType, AABB spawnArea, double initialSpeed, Vec3 velocityOffset) {
		val spawnPos = new Vec3(
				Mth.lerp(rng.nextFloat(), spawnArea.minX, spawnArea.maxX),
				Mth.lerp(rng.nextFloat(), spawnArea.minY, spawnArea.maxY),
				Mth.lerp(rng.nextFloat(), spawnArea.minZ, spawnArea.maxZ)
				);
		val spawnVelocity = Vec3.directionFromRotation(rng.nextFloat()*360, rng.nextFloat()*360)
				.add(velocityOffset)
				.scale(initialSpeed);
		level.addParticle(particleType, spawnPos.x, spawnPos.y, spawnPos.z, spawnVelocity.x, spawnVelocity.y, spawnVelocity.z);
	}
	public static void spawnParticlesFromArea(ClientLevel level, RandomSource rng, ParticleOptions particleType, AABB spawnArea, double initialSpeed, Vec3 velocityOffset, int quantity) {
		for (int i=0;i<quantity;i++) {
			spawnParticleFromArea(level, rng, particleType, spawnArea, initialSpeed, velocityOffset);
		}
	}

	// Non-Public inCombat Getter
	private static final AtomicReference<Predicate<CombatTracker>> IN_COMBAT_PREDICATE = new AtomicReference<>();
	public static boolean isInCombat(CombatTracker combatTracker) {
		val inCombatPredicate = IN_COMBAT_PREDICATE.updateAndGet(f->{
			if (f != null) { return f; }
			Field inCombatField = ObfuscationReflectionHelper.findField(CombatTracker.class, "f_19282_");
			inCombatField.trySetAccessible();
			return (tracker) -> {
				try {
					return inCombatField.getBoolean(tracker);
				} catch (IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				}
				return false;
			};
		});

		return inCombatPredicate.test(combatTracker);
	}
}
