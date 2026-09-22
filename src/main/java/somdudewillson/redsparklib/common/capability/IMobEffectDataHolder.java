package somdudewillson.redsparklib.common.capability;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
//? forge {
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
//?}
import net.minecraftforge.registries.RegistryObject;
import somdudewillson.redsparklib.common.utils.IContextFreeNBTSerializable;

/*? forge {*/@AutoRegisterCapability/*?}*/
public interface IMobEffectDataHolder extends IContextFreeNBTSerializable<ListTag> {
	
	@Nonnull
	CompoundTag getDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect);
	
	boolean hasDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect);
	
	void setDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect, CompoundTag data);

	@Nonnull
	CompoundTag getOrInitDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect, Supplier<CompoundTag> dataSupplier);
	
	void tick();
}
