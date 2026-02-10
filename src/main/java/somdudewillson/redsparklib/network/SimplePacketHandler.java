package somdudewillson.redsparklib.network;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import somdudewillson.redsparklib.RedSparkLib;

public class SimplePacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			RedSparkLib.RES_LOC_UTIL.modPath("main"),
			() -> PROTOCOL_VERSION, 
			PROTOCOL_VERSION::equals, 
			PROTOCOL_VERSION::equals);
	private static AtomicInteger packetsIdxCounter = new AtomicInteger(0);
	
	public static <T extends AbstractSimplePacket> int registerPacketType(Class<T> msgClass, Supplier<T> msgSupplier, NetworkDirection networkDirection) {
		int packetTypeIdx = packetsIdxCounter.getAndIncrement();
		INSTANCE.registerMessage(
				packetTypeIdx, 
				msgClass, 
				AbstractSimplePacket::encodeToByteBuffer, AbstractSimplePacket.createDecoderFunction(msgSupplier), AbstractSimplePacket::handle, 
				Optional.ofNullable(networkDirection));
		
		return packetTypeIdx;
	}
	public static <T extends AbstractSimplePacket> int registerPacketType(Class<T> msgClass, Supplier<T> msgSupplier) {
		return registerPacketType(msgClass, msgSupplier, null);
	}
}
