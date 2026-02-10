package somdudewillson.redsparklib.serialization.codecs;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import com.mojang.serialization.Codec;

import lombok.val;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class GraphCodec<A extends Graph<N>, N> extends BaseGraphCodec<A, N> {
	
    public GraphCodec(Codec<N> elementCodec) {
		super(elementCodec);
	}

	public static <T extends INBTSerializable<K>, G extends Graph<T>, K extends Tag> GraphCodec<G, T> forINbtSerializable(Class<K> tagClass, Class<T> valueClass, Supplier<T> instanceSupplier) {
    	return new GraphCodec<G, T>(new NbtSerializableCodec<>(tagClass, valueClass, instanceSupplier));
    }
	
	public static <T extends INBTSerializable<CompoundTag>, G extends Graph<T>> GraphCodec<G, T> compound(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(CompoundTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ListTag>, G extends Graph<T>> GraphCodec<G, T> list(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(ListTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ByteArrayTag>, G extends Graph<T>> GraphCodec<G, T> byteArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(ByteArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<IntArrayTag>, G extends Graph<T>> GraphCodec<G, T> intArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(IntArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<LongArrayTag>, G extends Graph<T>> GraphCodec<G, T> longArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(LongArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<StringTag>, G extends Graph<T>> GraphCodec<G, T> string(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(StringTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ByteTag>, G extends Graph<T>> GraphCodec<G, T> byteT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(ByteTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ShortTag>, G extends Graph<T>> GraphCodec<G, T> shortT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(ShortTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<IntTag>, G extends Graph<T>> GraphCodec<G, T> intT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(IntTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<LongTag>, G extends Graph<T>> GraphCodec<G, T> longT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(LongTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<FloatTag>, G extends Graph<T>> GraphCodec<G, T> floatT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(FloatTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<DoubleTag>, G extends Graph<T>> GraphCodec<G, T> doubleT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return GraphCodec.forINbtSerializable(DoubleTag.class, targetClass, instanceSupplier);
	}

	@Override
	protected boolean isMutable(A value) {
		return !(value instanceof ImmutableGraph<?>);
	}

	@Override
	protected boolean isDirected(A value) { return value.isDirected(); }
	@Override
	protected Set<N> getNodes(A value) { return value.nodes(); }
	@Override
	protected Set<EndpointPair<N>> getEdges(A value) { return value.edges(); }

	@Override
	@Nullable
	protected Tag encodeExtraToTag(A value, Map<N, Integer> nodeIdMap, Map<EndpointPair<N>, Integer> edgeIdMap) { return null; }

	@SuppressWarnings("unchecked")
	@Override
	protected A finishDecodingGraph(Tag extraNbt, boolean mutable, boolean directed, List<N> nodeList,
			List<EndpointPair<N>> edgeList, Map<Integer, N> nodeIdMap, Map<Integer, EndpointPair<N>> edgeIdMap) {
		GraphBuilder<Object> resultBuilder;
		if (directed) {
			resultBuilder = GraphBuilder.directed();
		} else {
			resultBuilder = GraphBuilder.undirected();
		}
		
		A result;
		if (mutable) {
			val mutableResult = (MutableGraph<N>)resultBuilder.build();
			nodeList.forEach(mutableResult::addNode);
			edgeList.forEach(e->mutableResult.putEdge(e));
			result = (A) mutableResult;
		} else {
			val immutableBuilder = (ImmutableGraph.Builder<N>)resultBuilder.immutable();
			nodeList.forEach(immutableBuilder::addNode);
			edgeList.forEach(e->immutableBuilder.putEdge(e));
			result = (A) immutableBuilder.build();
		}
		return result;
	}

    @Override
    public String toString() {
        return "GraphCodec[" + elementCodec + ']';
    }
}
