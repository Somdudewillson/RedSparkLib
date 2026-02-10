package somdudewillson.redsparklib.world.entity.ai.behavior;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import lombok.val;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

public abstract class ActivitySensitiveBehavior<E extends LivingEntity> extends Behavior<E> {
	protected final ImmutableList<Activity> requirementActivities;

	public ActivitySensitiveBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, List<Activity> requirementActivities) {
		super(pEntryCondition);
		this.requirementActivities = ImmutableList.copyOf(requirementActivities);
	}
	public ActivitySensitiveBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, Activity... requirementActivities) {
		super(pEntryCondition);
		this.requirementActivities = ImmutableList.copyOf(requirementActivities);
	}
	
	public ActivitySensitiveBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pDuration, List<Activity> requirementActivities) {
		super(pEntryCondition, pDuration);
		this.requirementActivities = ImmutableList.copyOf(requirementActivities);
	}
	public ActivitySensitiveBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pDuration, Activity... requirementActivities) {
		super(pEntryCondition, pDuration);
		this.requirementActivities = ImmutableList.copyOf(requirementActivities);
	}
	
	public ActivitySensitiveBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pMinDuration, int pMaxDuration, List<Activity> requirementActivities) {
		super(pEntryCondition, pMinDuration, pMaxDuration);
		this.requirementActivities = ImmutableList.copyOf(requirementActivities);
	}
	public ActivitySensitiveBehavior(Map<MemoryModuleType<?>, MemoryStatus> pEntryCondition, int pMinDuration, int pMaxDuration, Activity... requirementActivities) {
		super(pEntryCondition, pMinDuration, pMaxDuration);
		this.requirementActivities = ImmutableList.copyOf(requirementActivities);
	}

	@Override
	protected boolean canStillUse(ServerLevel pLevel, E pEntity, long pGameTime) {
		val entityBrain = pEntity.getBrain();
		return requirementActivities.stream().anyMatch(a->entityBrain.isActive(a)) && this.checkExtraStartConditions(pLevel, pEntity);
	}

}
