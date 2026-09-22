package somdudewillson.redsparklib.common;

import java.util.function.Supplier;

import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.RegistryObject;
import somdudewillson.redsparklib.RedSparkLib;
import somdudewillson.redsparklib.world.entity.ai.sensing.GlobalPlayerSensor;
import somdudewillson.redsparklib.world.entity.ai.sensing.LivingEntityRecordingSensor;

public class RedSparkLibSensorTypes {
	public static final RegistryObject</*? neoforge {*//*SensorType<?>, *//*?}*/SensorType<GlobalPlayerSensor>> GLOBAL_PLAYER_SENSOR = register("global_player_sensor", GlobalPlayerSensor::new);
	public static final RegistryObject</*? neoforge {*//*SensorType<?>, *//*?}*/SensorType<LivingEntityRecordingSensor>> MEATAKER_ENTITY_SENSOR = register("living_entity_recording_sensor", LivingEntityRecordingSensor::new);

	private static <U extends Sensor<?>> RegistryObject</*? neoforge {*//*SensorType<?>, *//*?}*/SensorType<U>> register(String registryName, Supplier<U> pSensorSupplier) {
		return RedSparkLib.SENSOR_TYPES.register(registryName, ()->new SensorType<>(pSensorSupplier));
	}
}
