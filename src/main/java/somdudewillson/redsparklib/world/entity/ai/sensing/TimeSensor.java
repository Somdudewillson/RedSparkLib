package somdudewillson.redsparklib.world.entity.ai.sensing;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

@RequiredArgsConstructor
public class TimeSensor extends Sensor<LivingEntity> {
	private final Set<MemoryModuleType<Number>> timers;
	private final boolean increment;

	@Override
	public Set<MemoryModuleType<?>> requires() {
		return ImmutableSet.copyOf(timers);
	}
	
	@SuppressWarnings("unchecked")
	public TimeSensor(MemoryModuleType<? extends Number> timer, boolean increment) {
		this(Set.of((MemoryModuleType<Number>)timer), increment);
	}

	@Override
	protected void doTick(ServerLevel pLevel, LivingEntity pEntity) {
		Brain<?> brain = pEntity.getBrain();
		if (increment) {
			timers.forEach(t->brain.setMemory(t, brain.getMemory(t).map(TimeSensor::increment)));
		} else {
			timers.forEach(t->brain.setMemory(t, brain.getMemory(t).map(TimeSensor::decrement)));
		}
	}
	
	protected static <T extends Number> T increment(T number) {
		return accumulate(number, 1);
	}
	protected static <T extends Number> T decrement(T number) {
		return accumulate(number, -1);
	}
	@SuppressWarnings("unchecked")
	protected static <T extends Number> T accumulate(T number, int amount) {
		if (number instanceof Byte) {
			return (T) (Byte) (byte) (number.byteValue()+amount);
		}
		if (number instanceof Short) {
			return (T) (Short) (short) (number.intValue()+amount);
		}
		if (number instanceof Integer) {
			return (T) (Integer) (number.intValue()+amount);
		}
		if (number instanceof Long) {
			return (T) (Long) (number.longValue()+amount);
		}
		if (number instanceof Float) {
			return (T) (Float) (number.floatValue()+amount);
		}
		if (number instanceof Double) {
			return (T) (Double) (number.doubleValue()+amount);
		}
		throw new UnsupportedOperationException("TimeSensor cannot accumulate number class " + number.getClass().getSimpleName());
	}

}
