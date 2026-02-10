package somdudewillson.redsparklib.common.capability;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.RegistryObject;

@AutoRegisterCapability
public interface IMobEffectDataHolder extends INBTSerializable<ListTag> {
	
	@Nonnull
	CompoundTag getDataForMobEffect(RegistryObject<? extends MobEffect> mobEffect);
	
	boolean hasDataForMobEffect(RegistryObject<? extends MobEffect> mobEffect);
	
	void setDataForMobEffect(RegistryObject<? extends MobEffect> mobEffect, CompoundTag data);

	@Nonnull
	CompoundTag getOrInitDataForMobEffect(RegistryObject<? extends MobEffect> mobEffect, Supplier<CompoundTag> dataSupplier);
	
	void tick();
}
