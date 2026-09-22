package somdudewillson.redsparklib.common;

//? neoforge {
/*import net.neoforged.neoforge.capabilities.EntityCapability;
import somdudewillson.redsparklib.platform.neoforge.common.RedSparkLibCapabilitiesNeoforge;
*///?} forge {
import net.minecraftforge.common.capabilities.Capability;
import somdudewillson.redsparklib.platform.forge.common.RedSparkLibCapabilitiesForge;
//?}
import somdudewillson.redsparklib.common.capability.IMobEffectDataHolder;

public class RedSparkLibCapabilities {
	//? neoforge {
	/*public static final EntityCapability<IMobEffectDataHolder, Void> MOB_EFFECT_DATA_HOLDER = RedSparkLibCapabilitiesNeoforge.MOB_EFFECT_DATA_HOLDER;
	*///?} forge {
    public static final Capability<IMobEffectDataHolder> MOB_EFFECT_DATA_HOLDER = RedSparkLibCapabilitiesForge.MOB_EFFECT_DATA_HOLDER;
    //?}
}
