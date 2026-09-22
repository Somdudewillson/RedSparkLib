package somdudewillson.redsparklib.common.capability;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.registries.RegistryObject;
//? neoforge {
/*import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.core.Holder;
import net.neoforged.neoforge.network.PacketDistributor;
import somdudewillson.redsparklib.platform.neoforge.network.clientbound.MobEffectCustomDataPayload;
*///?} forge {
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.PacketDistributor;
import somdudewillson.redsparklib.platform.forge.network.SimplePacketHandler;
import somdudewillson.redsparklib.platform.forge.network.clientbound.MobEffectCustomDataPacket;
//?}
import somdudewillson.redsparklib.platform.PlatformRegistryInstances;
import somdudewillson.redsparklib.RedSparkLib;
import somdudewillson.redsparklib.common.RedSparkLibCapabilities;

@RequiredArgsConstructor
//? neoforge {
/*@EventBusSubscriber(modid = RedSparkLib.MODID)
*///?} forge {
@Mod.EventBusSubscriber(modid = RedSparkLib.MODID, bus = Bus.FORGE)
//?}
public class MobEffectDataHolderImpl implements IMobEffectDataHolder {
	private final ReentrantReadWriteLock serializationLock = new ReentrantReadWriteLock(true);
	
	protected static final String POTION_ID_KEY = "potion_id";
	protected static final String POTION_DATA_KEY = "potion_data";
	
	protected final ConcurrentHashMap<ResourceLocation, CompoundTag> potionDataMap = new ConcurrentHashMap<>();
	protected final LivingEntity livingEntity;

	@Override
	public CompoundTag getDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect) {
		CompoundTag data = new CompoundTag();
		
		serializationLock.readLock().lock();
		try {
			data = potionDataMap.getOrDefault(mobEffect.getId(), new CompoundTag()).copy();
		} finally {
			serializationLock.readLock().unlock();
		}
		
		return data;
	}

	@Override
	public boolean hasDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect) {
		boolean hasData = false;
		
		serializationLock.readLock().lock();
		try {
			hasData = potionDataMap.containsKey(mobEffect.getId());
		} finally {
			serializationLock.readLock().unlock();
		}
		
		return hasData;
	}

	@Override
	public void setDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect, CompoundTag data) {
		data = data.copy();
		
		serializationLock.readLock().lock();
		try {
			potionDataMap.put(mobEffect.getId(), data);
			
			sendUpdatePacket();
		} finally {
			serializationLock.readLock().unlock();
		}
	}

	@Override
	public CompoundTag getOrInitDataForMobEffect(RegistryObject</*? neoforge {*//*MobEffect, *//*?}*/? extends MobEffect> mobEffect, Supplier<CompoundTag> dataSupplier) {
		CompoundTag data = dataSupplier.get();
		
		serializationLock.readLock().lock();
		try {
			CompoundTag oldValue = potionDataMap.putIfAbsent(mobEffect.getId(), data);
			if (oldValue == null) {
				sendUpdatePacket();
			} else {
				data = oldValue;
			}
			
		} finally {
			serializationLock.readLock().unlock();
		}
		
		return data;
	}

	@Override
	public ListTag serializeNBT() {
		serializationLock.writeLock().lock();
		
		val dataList = new ListTag();
		try {
			for (Entry<ResourceLocation, CompoundTag> entry : potionDataMap.entrySet()) {
				val entryTag = new CompoundTag();
				
				entryTag.putString(POTION_ID_KEY, entry.getKey().toString());
				entryTag.put(POTION_DATA_KEY, entry.getValue().copy());
				
				dataList.add(entryTag);
			}
		} finally {
			serializationLock.writeLock().unlock();
		}
		return dataList;
	}

	@Override
	public void deserializeNBT(ListTag tag) {
		serializationLock.writeLock().lock();
		
		try {
			potionDataMap.clear();
			
			for (int i=0; i<tag.size(); i++) {
				val entryTag = tag.getCompound(i);
				
				val entryPotionId = ResourceLocation.tryParse(entryTag.getString(POTION_ID_KEY));
				val entryData = entryTag.getCompound(POTION_DATA_KEY);
				
				potionDataMap.put(entryPotionId, entryData);
			}
			
			sendUpdatePacket();
		} finally {
			serializationLock.writeLock().unlock();
		}
	}
	
	protected void sendUpdatePacket() {
		if (livingEntity.level().isClientSide) { return; }
		
		//? neoforge {
		/*PacketDistributor.sendToPlayersTrackingEntityAndSelf(livingEntity, new MobEffectCustomDataPayload(livingEntity.getId(), livingEntity.level().registryAccess(), this));
		*///?} forge {
		SimplePacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(()->livingEntity), new MobEffectCustomDataPacket(livingEntity.getId(), this));
		//?}
	}
	
	public void tick() {
		val presentMobEffects = livingEntity.getActiveEffects().stream()
			.map(MobEffectInstance::getEffect)
			/*? neoforge {*//*.map(Holder::value)*//*?}*/
			.map(PlatformRegistryInstances.MOB_EFFECTS::getKey)
			.collect(Collectors.toUnmodifiableSet());
		
		serializationLock.readLock().lock();
		try {
			boolean changed = potionDataMap.keySet().retainAll(presentMobEffects);
			
			if (changed) {
				sendUpdatePacket();
			}
		} finally {
			serializationLock.readLock().unlock();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onLivingTickEvent(LivingTickEvent event) {
		/*? forge {*/if (event.isCanceled()) { return; }/*?}*/
		if ((event.getEntity().level().getGameTime()+event.getEntity().getId() & 7) != 0) { return; }
		val mobEffectDataCap = event.getEntity().getCapability(RedSparkLibCapabilities.MOB_EFFECT_DATA_HOLDER)/*? forge {*/.orElse(null)/*?}*/;
		if (mobEffectDataCap == null) { return; }
		
		mobEffectDataCap.tick();
	}

}
