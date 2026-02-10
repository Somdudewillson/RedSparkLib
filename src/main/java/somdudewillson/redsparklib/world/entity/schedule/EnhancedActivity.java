package somdudewillson.redsparklib.world.entity.schedule;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public abstract class EnhancedActivity<T extends LivingEntity> extends Activity {

	public EnhancedActivity(String pName) {
		super(pName);
	}

	abstract boolean isValidActivity(Brain<?> brain, T entity);
	
	Set<Pair<MemoryModuleType<?>, MemoryStatus>> getRequiredMemoryStatuses() {
		return ImmutableSet.of();
	}
	
	Set<MemoryModuleType<?>> getMemoriesToEraseOnStop() {
		return ImmutableSet.of();
	}
	
	
	public static <E extends LivingEntity> void addEnhancedActivityToBrain(Brain<E> brain, EnhancedActivity<E> activity, 
			ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> pTasks, Set<Pair<MemoryModuleType<?>, MemoryStatus>> pMemorieStatuses, Set<MemoryModuleType<?>> pMemoryTypes) {
		brain.addActivityAndRemoveMemoriesWhenStopped(
				activity, pTasks, 
				Sets.union(activity.getRequiredMemoryStatuses(), pMemorieStatuses), 
				Sets.union(activity.getMemoriesToEraseOnStop(), pMemoryTypes));
	}
	public static <E extends LivingEntity> void addEnhancedActivityToBrain(Brain<E> brain, EnhancedActivity<E> activity, 
			ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> pTasks) {
		addEnhancedActivityToBrain(brain, activity, pTasks, ImmutableSet.of(), ImmutableSet.of());
	}
	public static <E extends LivingEntity> void addEnhancedActivityToBrain(Brain<E> brain, EnhancedActivity<E> activity, 
			int pPriorityStart, ImmutableList<? extends BehaviorControl<? super E>> pTasks, Set<Pair<MemoryModuleType<?>, MemoryStatus>> pMemorieStatuses, Set<MemoryModuleType<?>> pMemoryTypes) {
		addEnhancedActivityToBrain(brain, activity, 
				ImmutableList.copyOf(pTasks.stream().map(t->Pair.of(pPriorityStart,t)).iterator()), 
				pMemorieStatuses, pMemoryTypes);
	}
	public static <E extends LivingEntity> void addEnhancedActivityToBrain(Brain<E> brain, EnhancedActivity<E> activity, 
			int pPriorityStart, ImmutableList<? extends BehaviorControl<? super E>> pTasks) {
		addEnhancedActivityToBrain(brain, activity, pPriorityStart, pTasks, ImmutableSet.of(), ImmutableSet.of());
	}

	@SuppressWarnings("unchecked")
	public static <E extends LivingEntity> void addActivityToBrain(Brain<E> brain, Activity activity, 
			ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> pTasks, Set<Pair<MemoryModuleType<?>, MemoryStatus>> pMemorieStatuses, Set<MemoryModuleType<?>> pMemoryTypes) {
		if (activity instanceof EnhancedActivity) {
			addEnhancedActivityToBrain(brain, (EnhancedActivity<E>)activity, pTasks, pMemorieStatuses, pMemoryTypes);
		} else {
			brain.addActivityAndRemoveMemoriesWhenStopped(activity, pTasks, pMemorieStatuses, pMemoryTypes);
		}
	}
	public static <E extends LivingEntity> void addActivityToBrain(Brain<E> brain, Activity activity, 
			ImmutableList<? extends Pair<Integer, ? extends BehaviorControl<? super E>>> pTasks) {
		addActivityToBrain(brain, activity, pTasks, ImmutableSet.of(), ImmutableSet.of());
	}
	public static <E extends LivingEntity> void addActivityToBrain(Brain<E> brain, Activity activity, 
			int pPriorityStart, ImmutableList<? extends BehaviorControl<? super E>> pTasks, Set<Pair<MemoryModuleType<?>, MemoryStatus>> pMemorieStatuses, Set<MemoryModuleType<?>> pMemoryTypes) {
		addActivityToBrain(brain, activity, 
				ImmutableList.copyOf(pTasks.stream().map(t->Pair.of(pPriorityStart,t)).iterator()), 
				pMemorieStatuses, pMemoryTypes);
	}
	public static <E extends LivingEntity> void addActivityToBrain(Brain<E> brain, Activity activity, 
			int pPriorityStart, ImmutableList<? extends BehaviorControl<? super E>> pTasks) {
		addActivityToBrain(brain, activity, pPriorityStart, pTasks, ImmutableSet.of(), ImmutableSet.of());
	}
	
	protected static <U> Optional<U> tryGetMemory(Brain<?> brain, MemoryModuleType<U> pType) {
		if (brain.checkMemory(pType, MemoryStatus.REGISTERED)) {
			return brain.getMemory(pType);
		}
		return Optional.empty();
	}


	@SuppressWarnings("unchecked")
	public static <E extends LivingEntity> void setActiveActivityToFirstValid(Brain<?> brain, E entity, Stream<Activity> pActivities) {
		brain.setActiveActivityToFirstValid(pActivities
				.filter(a->{
					if (!(a instanceof EnhancedActivity)) { return true; }
					return ((EnhancedActivity<E>)a).isValidActivity(brain, entity);
				})
				.toList());
	}
	public static <T extends LivingEntity> void setActiveActivityToFirstValid(Brain<?> brain, LivingEntity entity, List<Activity> pActivities) {
		setActiveActivityToFirstValid(brain, entity, pActivities.stream());
	}
}
