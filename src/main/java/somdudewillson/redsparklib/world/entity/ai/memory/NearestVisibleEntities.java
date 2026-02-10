package somdudewillson.redsparklib.world.entity.ai.memory;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.collect.Iterables;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class NearestVisibleEntities<T extends Entity> {	   
	private final List<T> nearbyEntities;
	private final Predicate<T> lineOfSightTest;

	private NearestVisibleEntities() {
		this.nearbyEntities = List.of();
		this.lineOfSightTest = (targetEntity) -> {
			return false;
		};
	}

	public NearestVisibleEntities(Mob pLivingEntity, List<T> pNearbyEntities) {
		this.nearbyEntities = pNearbyEntities;
		Object2BooleanOpenHashMap<T> object2booleanopenhashmap = new Object2BooleanOpenHashMap<>(pNearbyEntities.size());
		Predicate<Entity> predicate = (targetEntity) -> {
			return pLivingEntity.getSensing().hasLineOfSight(targetEntity);
		};
		this.lineOfSightTest = (targetEntity) -> {
			return object2booleanopenhashmap.computeIfAbsent(targetEntity, predicate);
		};
	}

	public static <U extends Entity> NearestVisibleEntities<U> empty() {
		return new NearestVisibleEntities<U>();
	}

	public Optional<T> findClosest(Predicate<T> pPredicate) {
		for(T targetEntity : this.nearbyEntities) {
			if (pPredicate.test(targetEntity) && this.lineOfSightTest.test(targetEntity)) {
				return Optional.of(targetEntity);
			}
		}

		return Optional.empty();
	}

	public Iterable<T> findAll(Predicate<T> pPredicate) {
		return Iterables.filter(this.nearbyEntities, (targetEntity) -> {
			return pPredicate.test(targetEntity) && this.lineOfSightTest.test(targetEntity);
		});
	}

	public Stream<T> find(Predicate<T> pPredicate) {
		return this.nearbyEntities.stream().filter((targetEntity) -> {
			return pPredicate.test(targetEntity) && this.lineOfSightTest.test(targetEntity);
		});
	}

	public boolean contains(T pEntity) {
		return this.nearbyEntities.contains(pEntity) && this.lineOfSightTest.test(pEntity);
	}

	public boolean contains(Predicate<T> pPredicate) {
		for(T entity : this.nearbyEntities) {
			if (pPredicate.test(entity) && this.lineOfSightTest.test(entity)) {
				return true;
			}
		}

		return false;
	}
}
