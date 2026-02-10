package somdudewillson.redsparklib.common;

import java.util.Optional;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;
import somdudewillson.redsparklib.RedSparkLib;
import somdudewillson.redsparklib.world.entity.ai.memory.RecordedEntityInfoMap;

public class RedSparkLibMemoryModules {
	public static final RegistryObject<MemoryModuleType<Player>> NEAREST_PLAYER_GLOBAL = registerMemoryModule("nearest_player_global");
	public static final RegistryObject<MemoryModuleType<RecordedEntityInfoMap>> RECORDED_ENTITY_INFO = registerMemoryModule("recorded_entity_info", RecordedEntityInfoMap.CODEC);
    
    private static <T extends MemoryModuleType<U>, U> RegistryObject<T> registerMemoryModule(String registryName, @Nullable Codec<U> pCodec) {
    	@SuppressWarnings("unchecked")
		RegistryObject<T> memoryTypeRegistry = (RegistryObject<T>) RedSparkLib.MEMORY_MODULE_TYPES.register(registryName, ()->new MemoryModuleType<U>(Optional.ofNullable(pCodec)));
    	return memoryTypeRegistry;
    }
    private static <T extends MemoryModuleType<U>, U> RegistryObject<T> registerMemoryModule(String registryName) {
    	return registerMemoryModule(registryName, null);
    }
}
