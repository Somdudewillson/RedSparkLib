package somdudewillson.redsparklib.network.clientbound;

import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;
import somdudewillson.redsparklib.common.RedSparkLibCapabilities;
import somdudewillson.redsparklib.common.capability.IMobEffectDataHolder;
import somdudewillson.redsparklib.network.AbstractSimplePacket;
import somdudewillson.redsparklib.network.SimplePacketHandler;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MobEffectCustomDataPacket extends AbstractSimplePacket {
	public static final int ID = SimplePacketHandler.registerPacketType(MobEffectCustomDataPacket.class, MobEffectCustomDataPacket::new, NetworkDirection.PLAY_TO_CLIENT);
	
	protected static final String ENTITY_ID_KEY = "entity_id";
	protected static final String MOB_EFFECT_DATA_KEY = "mob_effect_data";
	
	private int entityId;
	private ListTag serializedMobEffectData;
	
	public MobEffectCustomDataPacket(int entityId, IMobEffectDataHolder mobEffectData) {
		this.entityId = entityId;
		serializedMobEffectData = mobEffectData.serializeNBT();
	}

	@Override
	public String getName() {
		return "Custom Mob Effect Data Packet";
	}

	@Override
	public void encodeToByteBuffer(FriendlyByteBuf byteBuffer) {
		val wrapperTag = new CompoundTag();
		
		wrapperTag.putInt(ENTITY_ID_KEY, entityId);
		wrapperTag.put(MOB_EFFECT_DATA_KEY, serializedMobEffectData);
		
		byteBuffer.writeNbt(wrapperTag);
	}

	@Override
	public void decodeFromByteBuffer(FriendlyByteBuf byteBuffer) {
		val wrapperTag = byteBuffer.readNbt();
		
		entityId = wrapperTag.getInt(ENTITY_ID_KEY);
		serializedMobEffectData = (ListTag) wrapperTag.get(MOB_EFFECT_DATA_KEY);
	}

	@Override
	public void handle(Supplier<Context> ctxSupplier) {		
		ctxSupplier.get().enqueueWork(() ->
			// Make sure it's only executed on the physical client
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
				val clientEntity = Minecraft.getInstance().level.getEntity(entityId);
				if (clientEntity == null) { return; }
				val mobEffectDataCap = clientEntity.getCapability(RedSparkLibCapabilities.MOB_EFFECT_DATA_HOLDER).orElse(null);
				if (mobEffectDataCap == null) { return; }
				
				mobEffectDataCap.deserializeNBT(serializedMobEffectData);
			})
		);
		
		ctxSupplier.get().setPacketHandled(true);
	}

}
