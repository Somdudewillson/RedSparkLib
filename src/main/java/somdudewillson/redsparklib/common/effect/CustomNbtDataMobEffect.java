package somdudewillson.redsparklib.common.effect;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.RegistryObject;

public abstract class CustomNbtDataMobEffect<T extends INBTSerializable<CompoundTag>> extends CustomDataMobEffect<T> {

	protected CustomNbtDataMobEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}
	
	@Override
	public CompoundTag serializeData(T data) {
		return data.serializeNBT();
	}
	@Override
	public void deserializeData(T data, CompoundTag tag) {
		data.deserializeNBT(tag);
	}
	
	protected static <K extends INBTSerializable<CompoundTag>> MobEffectCustomDataContextManager<K> getCustomDataContextManager(LivingEntity pLivingEntity, 
			RegistryObject<? extends CustomNbtDataMobEffect<K>> mobEffectRegistry, Supplier<K> defaultDataSupplier) {
		return new MobEffectCustomDataContextManager<K>(
				pLivingEntity, 
				mobEffectRegistry, 
				defaultDataSupplier, 
				INBTSerializable::serializeNBT, 
				INBTSerializable::deserializeNBT
				);
	}

}
