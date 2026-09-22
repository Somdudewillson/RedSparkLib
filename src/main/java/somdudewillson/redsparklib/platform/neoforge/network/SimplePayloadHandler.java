package somdudewillson.redsparklib.platform.neoforge.network;

//? neoforge {
/*import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import somdudewillson.redsparklib.RedSparkLib;

@EventBusSubscriber(modid = RedSparkLib.MODID)
public class SimplePayloadHandler {
	private static final String PROTOCOL_VERSION = "1";
	private static final List<Consumer<PayloadRegistrar>> PENDING_REGISTRATIONS = new ArrayList<>();
	private static volatile boolean registered = false;

	@SubscribeEvent
	public static void onRegisterPayloadHandler(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
		synchronized (PENDING_REGISTRATIONS) {
			for (Consumer<PayloadRegistrar> registration : PENDING_REGISTRATIONS) {
				registration.accept(registrar);
			}
			registered = true;
		}
	}

	private static synchronized void addRegistration(Consumer<PayloadRegistrar> registration) {
		if (registered) {
			throw new IllegalStateException("Cannot register payload types after RegisterPayloadHandlersEvent has already fired!");
		}
		PENDING_REGISTRATIONS.add(registration);
	}

	public static <T extends CustomPacketPayload> void registerClientboundPayload(
			CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
			IPayloadHandler<T> handler) {
		addRegistration(registrar -> registrar.playToClient(type, codec, handler));
	}

	public static <T extends CustomPacketPayload> void registerServerboundPayload(
			CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
			IPayloadHandler<T> handler) {
		addRegistration(registrar -> registrar.playToServer(type, codec, handler));
	}

	public static <T extends CustomPacketPayload> void registerBidirectionalPayload(
			CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
			IPayloadHandler<T> handler) {
		addRegistration(registrar -> registrar.playBidirectional(type, codec, handler));
	}

	public static <T extends CustomPacketPayload> void registerBidirectionalPayload(
			CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
			IPayloadHandler<T> clientHandler,
			IPayloadHandler<T> serverHandler) {
		addRegistration(registrar -> registrar.playBidirectional(type, codec, new DirectionalPayloadHandler<>(clientHandler, serverHandler)));
	}

	public static <T extends CustomPacketPayload> void registerPayload(
			CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
			IPayloadHandler<T> handler,
			PacketFlow flow) {
		if (flow == PacketFlow.CLIENTBOUND) {
			registerClientboundPayload(type, codec, handler);
		} else if (flow == PacketFlow.SERVERBOUND) {
			registerServerboundPayload(type, codec, handler);
		} else {
			registerBidirectionalPayload(type, codec, handler);
		}
	}
	public static <T extends CustomPacketPayload> void registerPayload(
			CustomPacketPayload.Type<T> type,
			StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
			IPayloadHandler<T> clientHandler,
			IPayloadHandler<T> serverHandler) {
		registerBidirectionalPayload(type, codec, clientHandler, serverHandler);
	}
}
*///?}
