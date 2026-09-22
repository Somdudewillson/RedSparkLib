package somdudewillson.redsparklib;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import somdudewillson.redsparklib.common.RedSparkLibMemoryModules;
import somdudewillson.redsparklib.common.RedSparkLibSensorTypes;
import somdudewillson.redsparklib.common.data.RedSparkLibSimpleDatagenManager;
import somdudewillson.redsparklib.common.utils.ResourceLocationUtils;
//? neoforge {
/*import somdudewillson.redsparklib.platform.neoforge.network.SimplePayloadHandler;
import somdudewillson.redsparklib.platform.neoforge.common.RedSparkLibCapabilitiesNeoforge;
*///?} forge {
import somdudewillson.redsparklib.platform.forge.network.SimplePacketHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
//?}

@Mod(RedSparkLib.MODID)
public class RedSparkLib {
    public static final String MODID = "redsparklib";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceLocationUtils RES_LOC_UTIL = new ResourceLocationUtils(MODID);
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, MODID);
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, MODID);

    //? neoforge {
    /*public RedSparkLib(IEventBus modEventBus) {
        init(modEventBus);
    }
    *///? } forge {
    public RedSparkLib(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        init(modEventBus);
    }
    //?}
    
    private void init(IEventBus modEventBus) {
        // Forcibly initialize loader classes
        try {
			Class.forName(RedSparkLibSensorTypes.class.getName());
			Class.forName(RedSparkLibMemoryModules.class.getName());
			
			//? neoforge {
			/*Class.forName(SimplePayloadHandler.class.getName());
			*///?} forge {
			Class.forName(SimplePacketHandler.class.getName());
			//?}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        MEMORY_MODULE_TYPES.register(modEventBus);
        SENSOR_TYPES.register(modEventBus);
        MOB_EFFECTS.register(modEventBus);
        PARTICLE_TYPES.register(modEventBus);
        
        //? neoforge {
        /*RedSparkLibCapabilitiesNeoforge.init(modEventBus);
        modEventBus.addListener((net.neoforged.neoforge.data.event.GatherDataEvent event) -> RedSparkLibSimpleDatagenManager.registerDataGenerators(MODID, event));
        *///?} forge {
        modEventBus.addListener((net.minecraftforge.data.event.GatherDataEvent event) -> RedSparkLibSimpleDatagenManager.registerDataGenerators(MODID, event));
        //?}

        //? forge {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        //?}
    }
}
