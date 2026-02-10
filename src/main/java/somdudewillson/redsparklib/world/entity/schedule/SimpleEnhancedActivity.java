package somdudewillson.redsparklib.world.entity.schedule;

import java.util.Collection;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class SimpleEnhancedActivity extends EnhancedActivity<LivingEntity> {
	protected final BiPredicate<Brain<?>, LivingEntity> conditionFunc;
	protected final ImmutableSet<Pair<MemoryModuleType<?>, MemoryStatus>> requiredMemoryStatuses;

	public SimpleEnhancedActivity(String pName, BiPredicate<Brain<?>, LivingEntity> conditionFunc, ImmutableSet<Pair<MemoryModuleType<?>, MemoryStatus>> requiredMemoryStatuses) {
		super(pName);
		this.conditionFunc = conditionFunc;
		this.requiredMemoryStatuses = requiredMemoryStatuses;
	}
	public SimpleEnhancedActivity(String pName, BiPredicate<Brain<?>, LivingEntity> conditionFunc, Collection<Pair<MemoryModuleType<?>, MemoryStatus>> requiredMemoryStatuses) {
		this(pName, conditionFunc, ImmutableSet.copyOf(requiredMemoryStatuses));
	}
	public SimpleEnhancedActivity(String pName, BiPredicate<Brain<?>, LivingEntity> conditionFunc, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus) {
		this(pName, conditionFunc, ImmutableSet.of(requiredMemoryStatus));
	}
	public SimpleEnhancedActivity(String pName, BiPredicate<Brain<?>, LivingEntity> conditionFunc, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus0, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus1) {
		this(pName, conditionFunc, ImmutableSet.of(requiredMemoryStatus0, requiredMemoryStatus1));
	}
	public SimpleEnhancedActivity(String pName, BiPredicate<Brain<?>, LivingEntity> conditionFunc, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus0, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus1, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus2) {
		this(pName, conditionFunc, ImmutableSet.of(requiredMemoryStatus0, requiredMemoryStatus1, requiredMemoryStatus2));
	}
	
	public <T> SimpleEnhancedActivity(String pName, Supplier<MemoryModuleType<T>> memoryModule, @Nullable T requiredValue) {
		this(
				pName,
				requiredValue==null ? (brain, entity)->true : (brain, entity)->brain.getMemory(memoryModule.get()).map(v->requiredValue.equals(v)).orElse(false),
				Pair.of(memoryModule.get(), requiredValue==null ? MemoryStatus.VALUE_ABSENT : MemoryStatus.VALUE_PRESENT)
				);
	}
	

	public static Function<String, SimpleEnhancedActivity> makeFactory(BiPredicate<Brain<?>, LivingEntity> conditionFunc, ImmutableSet<Pair<MemoryModuleType<?>, MemoryStatus>> requiredMemoryStatuses) {
		return (name)->new SimpleEnhancedActivity(name, conditionFunc, requiredMemoryStatuses);
	}
	public static Function<String, SimpleEnhancedActivity> makeFactory(BiPredicate<Brain<?>, LivingEntity> conditionFunc, Collection<Pair<MemoryModuleType<?>, MemoryStatus>> requiredMemoryStatuses) {
		return (name)->new SimpleEnhancedActivity(name, conditionFunc, requiredMemoryStatuses);
	}
	public static Function<String, SimpleEnhancedActivity> makeFactory(BiPredicate<Brain<?>, LivingEntity> conditionFunc, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus) {
		return (name)->new SimpleEnhancedActivity(name, conditionFunc, requiredMemoryStatus);
	}
	public static Function<String, SimpleEnhancedActivity> makeFactory(BiPredicate<Brain<?>, LivingEntity> conditionFunc, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus0, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus1) {
		return (name)->new SimpleEnhancedActivity(name, conditionFunc, requiredMemoryStatus0, requiredMemoryStatus1);
	}
	public static Function<String, SimpleEnhancedActivity> makeFactory(BiPredicate<Brain<?>, LivingEntity> conditionFunc, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus0, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus1, Pair<MemoryModuleType<?>, MemoryStatus> requiredMemoryStatus2) {
		return (name)->new SimpleEnhancedActivity(name, conditionFunc, requiredMemoryStatus0, requiredMemoryStatus1, requiredMemoryStatus2);
	}

	public static <T> Function<String, SimpleEnhancedActivity> makeFactory(Supplier<MemoryModuleType<T>> memoryModule, @Nullable T requiredValue) {
		return (name)->new SimpleEnhancedActivity(name, memoryModule, requiredValue);
	}
	
	
	Set<Pair<MemoryModuleType<?>, MemoryStatus>> getRequiredMemoryStatuses() {
		return ImmutableSet.of();
	}

	@Override
	boolean isValidActivity(Brain<?> brain, LivingEntity entity) {
		return conditionFunc.test(brain, entity);
	}

}
