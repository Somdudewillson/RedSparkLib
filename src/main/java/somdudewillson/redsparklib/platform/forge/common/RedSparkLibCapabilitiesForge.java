package somdudewillson.redsparklib.platform.forge.common;

//? forge {
import org.jetbrains.annotations.Nullable;

import lombok.val;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import somdudewillson.redsparklib.RedSparkLib;
import somdudewillson.redsparklib.common.capability.IMobEffectDataHolder;
import somdudewillson.redsparklib.common.capability.MobEffectDataHolderImpl;
import somdudewillson.redsparklib.platform.forge.network.SimplePacketHandler;
import somdudewillson.redsparklib.platform.forge.network.clientbound.MobEffectCustomDataPacket;

public class RedSparkLibCapabilitiesForge {
    public static final Capability<IMobEffectDataHolder> MOB_EFFECT_DATA_HOLDER = CapabilityManager.get(new CapabilityToken<>(){});
    
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        oldPlayer.reviveCaps();

        oldPlayer.getCapability(MOB_EFFECT_DATA_HOLDER).ifPresent(oldData -> {
            newPlayer.getCapability(MOB_EFFECT_DATA_HOLDER).ifPresent(newData -> {
                newData.deserializeNBT(oldData.serializeNBT());
            });
        });
        
        oldPlayer.invalidateCaps();
    }

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof LivingEntity livingEntity && event.getEntity() instanceof ServerPlayer player) {
			val cap = livingEntity.getCapability(MOB_EFFECT_DATA_HOLDER);
			
			cap.ifPresent(data -> {
				SimplePacketHandler.INSTANCE.send(
						PacketDistributor.PLAYER.with(()->player), 
						new MobEffectCustomDataPacket(player.getId(), data));
            });
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			val cap = player.getCapability(MOB_EFFECT_DATA_HOLDER);
			
			cap.ifPresent(data -> {
				SimplePacketHandler.INSTANCE.send(
						PacketDistributor.PLAYER.with(()->player), 
						new MobEffectCustomDataPacket(player.getId(), data));
            });
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			val cap = player.getCapability(MOB_EFFECT_DATA_HOLDER);
			
			cap.ifPresent(data -> {
				SimplePacketHandler.INSTANCE.send(
						PacketDistributor.PLAYER.with(()->player), 
						new MobEffectCustomDataPacket(player.getId(), data));
            });
		}
	}
	
	@SubscribeEvent
	public void onAttachingCapabilities(final AttachCapabilitiesEvent<Entity> event) {
		attachMobEffectDataHolder(event);
	}
	
	public void attachMobEffectDataHolder(final AttachCapabilitiesEvent<Entity> event) {
	    if (!(event.getObject() instanceof LivingEntity)) { return; }

	    MobEffectDataHolderImpl backend = new MobEffectDataHolderImpl((LivingEntity) event.getObject());
	    LazyOptional<IMobEffectDataHolder> optionalStorage = LazyOptional.of(() -> backend);
	    Capability<IMobEffectDataHolder> capability = MOB_EFFECT_DATA_HOLDER;

	    ICapabilityProvider provider = new ICapabilitySerializable<ListTag>() {
	        @Override
	        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction direction) {
	            if (cap == capability) {
	                return optionalStorage.cast();
	            }
	            return LazyOptional.empty();
	        }

	        @Override
	        public ListTag serializeNBT() {
	            return backend.serializeNBT();
	        }

	        @Override
	        public void deserializeNBT(ListTag tag) {
	            backend.deserializeNBT(tag);
	        }
	    };

	    event.addCapability(RedSparkLib.RES_LOC_UTIL.modPath("mob_effect_custom_data"), provider);
	}
}
//?}