package somdudewillson.redsparklib.common.effect;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.RegistryObject;
//? neoforge {
/*import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.HolderLookup;
*///?}

public abstract class CustomNbtDataMobEffect<T extends INBTSerializable<CompoundTag>> extends CustomDataMobEffect<T> {

	protected CustomNbtDataMobEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}
	
	@Override
	public CompoundTag serializeData(T data/*? neoforge{*//*, HolderLookup.Provider provider*//*?}*/) {
		return data.serializeNBT(/*? neoforge{*//*provider*//*?}*/);
	}
	@Override
	public void deserializeData(T data, /*? neoforge{*//*HolderLookup.Provider provider,*//*?}*/ CompoundTag tag) {
		data.deserializeNBT(/*? neoforge{*//*provider, *//*?}*/tag);
	}
	
	protected static <E extends CustomNbtDataMobEffect<K>, K extends INBTSerializable<CompoundTag>> MobEffectCustomDataContextManager<K> getCustomDataContextManager(
			LivingEntity pLivingEntity, 
			RegistryObject</*? neoforge{*//*MobEffect, *//*?}*/? extends CustomNbtDataMobEffect<K>> mobEffectRegistry, 
			Supplier<K> defaultDataSupplier) {
		return new MobEffectCustomDataContextManager<K>(
				pLivingEntity, 
				mobEffectRegistry, 
				defaultDataSupplier, 
				INBTSerializable::serializeNBT, 
				INBTSerializable::deserializeNBT
				);
	}

}
