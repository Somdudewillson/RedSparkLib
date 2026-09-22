package somdudewillson.redsparklib.platform.neoforge.network.clientbound;

//? neoforge {
/*import lombok.Getter;
import lombok.val;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import somdudewillson.redsparklib.RedSparkLib;
import somdudewillson.redsparklib.common.RedSparkLibCapabilities;
import somdudewillson.redsparklib.common.capability.IMobEffectDataHolder;
import somdudewillson.redsparklib.platform.neoforge.network.SimplePayloadHandler;

@Getter
public class MobEffectCustomDataPayload implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<MobEffectCustomDataPayload> TYPE = 
			new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(RedSparkLib.MODID, "mob_effect_custom_data"));
	
    public static final StreamCodec<ByteBuf, MobEffectCustomDataPayload> STREAM_CODEC = StreamCodec.<ByteBuf, MobEffectCustomDataPayload, Integer, Tag>composite(
            ByteBufCodecs.VAR_INT,
            MobEffectCustomDataPayload::getEntityId,
            ByteBufCodecs.TAG,
            MobEffectCustomDataPayload::getSerializedMobEffectData,
            MobEffectCustomDataPayload::new
        );
	
	private int entityId;
	private ListTag serializedMobEffectData;
	
	protected MobEffectCustomDataPayload(int entityId, Tag serializedMobEffectData) {
		this.entityId = entityId;
		this.serializedMobEffectData = (ListTag) serializedMobEffectData;
	}
	
	public MobEffectCustomDataPayload(int entityId, HolderLookup.Provider provider, IMobEffectDataHolder mobEffectData) {
		this.entityId = entityId;
		serializedMobEffectData = mobEffectData.serializeNBT(provider);
	}
	
	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
	
	public static void handle(MobEffectCustomDataPayload payload, IPayloadContext context) {
		val clientLevel = Minecraft.getInstance().level;
		val clientEntity = clientLevel.getEntity(payload.getEntityId());
		if (clientEntity == null) { return; }
		
		val mobEffectDataCap = clientEntity.getCapability(RedSparkLibCapabilities.MOB_EFFECT_DATA_HOLDER);
		if (mobEffectDataCap == null) { return; }
		
		mobEffectDataCap.deserializeNBT(clientLevel.registryAccess(), payload.getSerializedMobEffectData());
	}
	
	static {
		SimplePayloadHandler.registerClientboundPayload(TYPE, STREAM_CODEC, MobEffectCustomDataPayload::handle);
	}

}
*///?}
