package somdudewillson.redsparklib.common.effect;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.base.Suppliers;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import somdudewillson.redsparklib.common.RedSparkLibCapabilities;
import somdudewillson.redsparklib.common.capability.IMobEffectDataHolder;

public abstract class CustomDataMobEffect<T> extends MobEffect {
	protected final Supplier<RegistryObject<CustomDataMobEffect<T>>> registryFetcher = Suppliers.memoize(()->{
		return RegistryObject.create(ForgeRegistries.MOB_EFFECTS.getKey(this), ForgeRegistries.MOB_EFFECTS);
	});

	protected CustomDataMobEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}
	
	public abstract T getDefaultDataState();
	
	public abstract CompoundTag serializeData(T data);
	public abstract void deserializeData(T data, CompoundTag tag);
	
	protected MobEffectCustomDataContextManager<T> getCustomDataContextManager(LivingEntity pLivingEntity) {
		return new MobEffectCustomDataContextManager<>(pLivingEntity, registryFetcher.get(), this::getDefaultDataState, this::serializeData, this::deserializeData);
	}
	protected static class MobEffectCustomDataContextManager<T> implements AutoCloseable {
		protected final RegistryObject<? extends CustomDataMobEffect<T>> mobEffectRegistry;
		protected final Function<T, CompoundTag> dataSerializer;
		protected final IMobEffectDataHolder potionDataCap;
		protected final CompoundTag existingDataTag;
		@Getter
		@Setter
		protected T extraData;
		
		public MobEffectCustomDataContextManager(LivingEntity pLivingEntity, RegistryObject<? extends CustomDataMobEffect<T>> pMobEffectRegistry,
				Supplier<T> defaultDataSupplier, Function<T, CompoundTag> pDataSerializer, BiConsumer<T, CompoundTag> dataDeserializer) {	
			mobEffectRegistry = pMobEffectRegistry;
			dataSerializer = pDataSerializer;
					
			potionDataCap = pLivingEntity.getCapability(RedSparkLibCapabilities.MOB_EFFECT_DATA_HOLDER).orElse(null);
			if (potionDataCap == null) { throw new IllegalStateException(String.format("Entity lacks %s capability!", RedSparkLibCapabilities.MOB_EFFECT_DATA_HOLDER.getName())); }
			existingDataTag = potionDataCap.getOrInitDataForMobEffect(mobEffectRegistry, ()->dataSerializer.apply(defaultDataSupplier.get()));
			extraData = defaultDataSupplier.get();
			dataDeserializer.accept(extraData, existingDataTag);
		}

		@Override
		public void close() {
			val newDataTag = dataSerializer.apply(extraData);
			if (!existingDataTag.equals(newDataTag)) {
				potionDataCap.setDataForMobEffect(mobEffectRegistry, newDataTag);
			}
		}
		
	}
	
	@Override
	public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
		try (MobEffectCustomDataContextManager<T> ctx = getCustomDataContextManager(pLivingEntity)) {
			ctx.setExtraData( applyEffectTick(pLivingEntity, pAmplifier, ctx.getExtraData()) );
		}
	}
	public T applyEffectTick(LivingEntity pLivingEntity, int pAmplifier, T extraData) { return extraData; }

	@Override
	public void applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth) {
		try (MobEffectCustomDataContextManager<T> ctx = getCustomDataContextManager(pLivingEntity)) {
			ctx.setExtraData( applyInstantenousEffect(pSource, pIndirectSource, pLivingEntity, pAmplifier, pHealth, ctx.getExtraData()) );
		}
	}
	public T applyInstantenousEffect(@Nullable Entity pSource, @Nullable Entity pIndirectSource, LivingEntity pLivingEntity, int pAmplifier, double pHealth, T extraData) { return extraData; }
	
	
}
