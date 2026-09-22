package somdudewillson.redsparklib.serialization.codecs;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.graph.EndpointPair;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.mojang.serialization.Codec;

import lombok.val;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import somdudewillson.redsparklib.common.utils.ExtNbtUtils;
import somdudewillson.redsparklib.common.utils.IContextFreeNBTSerializable;
import somdudewillson.redsparklib.platform.PlatformSpecificUtils;

public class ValueGraphCodec<A extends ValueGraph<N, W>, N, W> extends BaseGraphCodec<A, N> {
    protected final Codec<W> edgeValueCodec;
	
    public ValueGraphCodec(Codec<N> elementCodec, Codec<W> edgeValueCodec) {
		super(elementCodec);
		this.edgeValueCodec = edgeValueCodec;
	}

	public static <T extends IContextFreeNBTSerializable<K>, T2 extends IContextFreeNBTSerializable<K2>, K extends Tag, K2 extends Tag, G extends ValueGraph<T, T2>> ValueGraphCodec<G, T, T2> forINbtSerializable(Class<K> elementTagClass, Class<T> elementClass, Supplier<T> elementInstanceSupplier, Class<K2> edgeValueTagClass, Class<T2> edgeValueClass, Supplier<T2> edgeValueInstanceSupplier) {
    	return new ValueGraphCodec<G, T, T2>(
    			new NbtContextFreeSerializableCodec<>(elementTagClass, elementClass, elementInstanceSupplier), 
    			new NbtContextFreeSerializableCodec<>(edgeValueTagClass, edgeValueClass, edgeValueInstanceSupplier));
    }
	public static <T extends IContextFreeNBTSerializable<K>, T2, K extends Tag, G extends ValueGraph<T, T2>> ValueGraphCodec<G, T, T2> forINbtSerializableNodes(Class<K> elementTagClass, Class<T> elementClass, Supplier<T> elementInstanceSupplier, Codec<T2> edgeValueCodec) {
    	return new ValueGraphCodec<G, T, T2>(
    			new NbtContextFreeSerializableCodec<>(elementTagClass, elementClass, elementInstanceSupplier), 
    			edgeValueCodec);
    }
	public static <T, T2 extends IContextFreeNBTSerializable<K2>, K2 extends Tag, G extends ValueGraph<T, T2>> ValueGraphCodec<G, T, T2> forINbtSerializableEdgeValues(Codec<T> elementCodec, Class<K2> edgeValueTagClass, Class<T2> valueClass, Supplier<T2> instanceSupplier) {
    	return new ValueGraphCodec<G, T, T2>(
    			elementCodec, 
    			new NbtContextFreeSerializableCodec<>(edgeValueTagClass, valueClass, instanceSupplier));
    }

	@Override
	protected boolean isMutable(A value) {
		return !(value instanceof ImmutableValueGraph<?,?>);
	}

	@Override
	protected boolean isDirected(A value) { return value.isDirected(); }
	@Override
	protected Set<N> getNodes(A value) { return value.nodes(); }
	@Override
	protected Set<EndpointPair<N>> getEdges(A value) { return value.edges(); }

	@Override
	protected Tag encodeExtraToTag(A value, Map<N, Integer> nodeIdMap, Map<EndpointPair<N>, Integer> edgeIdMap) {
		Stream<CompoundTag> edgeValueStream = value.edges().stream()
				.map(e->Pair.of(e,value.edgeValue(e)))
				.filter(p->p.getRight().isEmpty())
				.map(p->Pair.of(p.getLeft(),p.getRight().get()))
				.map(p->{
					CompoundTag root = new CompoundTag();
					
					root.putInt("edgeId", edgeIdMap.get(p.getLeft()));
					root.put("value", PlatformSpecificUtils.getOrThrowDataResult(edgeValueCodec.encodeStart(NbtOps.INSTANCE, p.getRight()), "Error serializing edge value for edge "+edgeIdMap.get(p.getLeft())+": ", LOGGER::error));
					
					return root;
				});
		return ExtNbtUtils.serializeTagStream(edgeValueStream);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected A finishDecodingGraph(Tag extraNbt, boolean mutable, boolean directed, List<N> nodeList,
			List<EndpointPair<N>> edgeList, Map<Integer, N> nodeIdMap, Map<Integer, EndpointPair<N>> edgeIdMap) {
		ValueGraphBuilder<Object, Object> resultBuilder;
		if (directed) {
			resultBuilder = ValueGraphBuilder.directed();
		} else {
			resultBuilder = ValueGraphBuilder.undirected();
		}
		
		Map<EndpointPair<N>, W> edgeValueMap = ExtNbtUtils.deserializeToStream((ListTag) extraNbt, t->(CompoundTag)t)
				.collect(Collectors.toMap(
						t->edgeIdMap.get(t.getInt("edgeId")), 
						t->PlatformSpecificUtils.getOrThrowDataResult(edgeValueCodec.decode(NbtOps.INSTANCE, t.get("value")), "Error deserializing edge value for edge "+t.getInt("edgeId")+": ", LOGGER::error).getFirst()));
		
		A result;
		if (mutable) {
			val mutableResult = (MutableValueGraph<N, W>)resultBuilder.build();
			nodeList.forEach(mutableResult::addNode);
			edgeList.forEach(e->mutableResult.putEdgeValue(e, edgeValueMap.get(e)));
			result = (A) mutableResult;
		} else {
			val immutableBuilder = (ImmutableValueGraph.Builder<N, W>)resultBuilder.immutable();
			nodeList.forEach(immutableBuilder::addNode);
			edgeList.forEach(e->immutableBuilder.putEdgeValue(e, edgeValueMap.get(e)));
			result = (A) immutableBuilder.build();
		}
		return result;
	}

    @Override
    public String toString() {
        return "GraphCodec[" + elementCodec + ']';
    }
}
