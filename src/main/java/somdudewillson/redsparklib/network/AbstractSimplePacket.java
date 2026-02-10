package somdudewillson.redsparklib.network;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public abstract class AbstractSimplePacket {
	public abstract String getName();
	
	public abstract void encodeToByteBuffer(FriendlyByteBuf byteBuffer);
	public abstract void decodeFromByteBuffer(FriendlyByteBuf byteBuffer);
	public abstract void handle(Supplier<NetworkEvent.Context> ctxSupplier);
	
	public static <T extends AbstractSimplePacket> T createFromByteBuffer(Supplier<T> supplier, FriendlyByteBuf byteBuffer) {
		T instance = supplier.get();
		instance.decodeFromByteBuffer(byteBuffer);
		return instance;
	}
	public static <T extends AbstractSimplePacket> Function<FriendlyByteBuf, T> createDecoderFunction(Supplier<T> supplier) {
		return (FriendlyByteBuf byteBuffer) -> AbstractSimplePacket.createFromByteBuffer(supplier, byteBuffer);
	}
}
