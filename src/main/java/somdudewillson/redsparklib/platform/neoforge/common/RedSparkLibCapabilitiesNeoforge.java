package somdudewillson.redsparklib.platform.neoforge.common;

//? neoforge {
/*import java.util.function.Supplier;

import lombok.val;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import somdudewillson.redsparklib.RedSparkLib;
import somdudewillson.redsparklib.common.capability.IMobEffectDataHolder;
import somdudewillson.redsparklib.common.capability.MobEffectDataHolderImpl;
import somdudewillson.redsparklib.platform.neoforge.network.clientbound.MobEffectCustomDataPayload;

@EventBusSubscriber(modid = RedSparkLib.MODID)
public class RedSparkLibCapabilitiesNeoforge {
	public static final EntityCapability<IMobEffectDataHolder, Void> MOB_EFFECT_DATA_HOLDER = 
			EntityCapability.createVoid(
				ResourceLocation.fromNamespaceAndPath(RedSparkLib.MODID, "mob_effect_data"), 
				IMobEffectDataHolder.class);
	
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
			DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, RedSparkLib.MODID);

	public static final Supplier<AttachmentType<MobEffectDataHolderImpl>> MOB_EFFECT_DATA_ATTACHMENT =
			ATTACHMENT_TYPES.register("mob_effect_data", () -> AttachmentType.serializable(
				(IAttachmentHolder holder) -> {
					if (holder instanceof LivingEntity living) {
						return new MobEffectDataHolderImpl(living);
					}
					throw new IllegalArgumentException("Cannot attach MobEffectDataHolder to non-LivingEntity: " + holder);
				}
			).copyOnDeath().build());

	public static void init(IEventBus modEventBus) {
		ATTACHMENT_TYPES.register(modEventBus);
		modEventBus.addListener(RedSparkLibCapabilitiesNeoforge::onRegisterCapabilities);
	}

	@SuppressWarnings("unchecked")
	public static void onRegisterCapabilities(final RegisterCapabilitiesEvent event) {
		for (EntityType<?> entityType : BuiltInRegistries.ENTITY_TYPE) {
			if (!LivingEntity.class.isAssignableFrom(entityType.getBaseClass())) {
				continue;
			}
			
			attachMobEffectDataHolder(event, (EntityType<? extends LivingEntity>) entityType);
		}
	}
	
	public static <T extends LivingEntity> void attachMobEffectDataHolder(final RegisterCapabilitiesEvent event, EntityType<T> entityType) {
		event.registerEntity(
				MOB_EFFECT_DATA_HOLDER, 
				entityType, 
				(entity, ctx) -> entity.getData(MOB_EFFECT_DATA_ATTACHMENT));
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		if (event.getTarget() instanceof LivingEntity livingEntity && event.getEntity() instanceof ServerPlayer player) {
			val cap = livingEntity.getCapability(MOB_EFFECT_DATA_HOLDER);
			if (cap != null && !livingEntity.level().isClientSide()) {
				PacketDistributor.sendToPlayer(player, new MobEffectCustomDataPayload(livingEntity.getId(), livingEntity.level().registryAccess(), cap));
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			val cap = player.getCapability(MOB_EFFECT_DATA_HOLDER);
			if (cap != null) {
				PacketDistributor.sendToPlayer(player, new MobEffectCustomDataPayload(player.getId(), player.level().registryAccess(), cap));
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			val cap = player.getCapability(MOB_EFFECT_DATA_HOLDER);
			if (cap != null) {
				PacketDistributor.sendToPlayer(player, new MobEffectCustomDataPayload(player.getId(), player.level().registryAccess(), cap));
			}
		}
	}
}
*///?}