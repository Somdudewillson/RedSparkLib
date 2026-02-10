package somdudewillson.redsparklib.serialization.codecs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import com.google.common.collect.HashBiMap;
import com.google.common.graph.EndpointPair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;

import lombok.val;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import somdudewillson.redsparklib.common.utils.ExtNbtUtils;
import somdudewillson.redsparklib.common.utils.OptNbtUtils;

// This is bad as we do not have proper type bounding
// There isn't really another way to do this though, as the shared superinterface BaseGraph isn't visible to us
public abstract class BaseGraphCodec<A, N> extends NbtWrappingCodec<A, CompoundTag> {
	protected final Logger LOGGER = LogUtils.getLogger();
    
    protected final Codec<N> elementCodec;
    
    public BaseGraphCodec(Codec<N> elementCodec) {
		super(CompoundTag.class);
		this.elementCodec = elementCodec;
	}

	@Override
	protected CompoundTag encodeToTag(A value) {
		CompoundTag root = new CompoundTag();

		root.putBoolean("mutable", isMutable(value));

		root.putBoolean("directed", isDirected(value));
		
		root.put("nodes", 
				ExtNbtUtils.serializeStream( getNodes(value).stream(), n->elementCodec.encodeStart(NbtOps.INSTANCE, n).getOrThrow(false, Util.prefix("Error serializing graph node: ", LOGGER::error)) )
				);
		HashMap<N, Integer> nodeIdMap = new HashMap<>();
		for (N node : getNodes(value)) {
			nodeIdMap.put(node, nodeIdMap.size());
		}
		Map<EndpointPair<N>, Integer> edgeIdMap = getEdges(value)
				.stream()
				.collect(Collectors.toMap(Function.identity(), e->getEdgeId(e, nodeIdMap)));
		root.put("edges", 
				ExtNbtUtils.serializeStream( getEdges(value).stream(), e->new IntArrayTag(new int[] {nodeIdMap.get(e.nodeU()),nodeIdMap.get(e.nodeV())} ) )
				);
		
		OptNbtUtils.put( root, "extra", encodeExtraToTag(value, nodeIdMap, edgeIdMap) );
		return root;
	}
    
    protected abstract boolean isMutable(A value);
    protected abstract boolean isDirected(A value);
    protected abstract Set<N> getNodes(A value);
    protected abstract Set<EndpointPair<N>> getEdges(A value);
    
    @Nullable
    protected abstract Tag encodeExtraToTag(A value, Map<N, Integer> nodeIdMap, Map<EndpointPair<N>, Integer> edgeIdMap);

	@Override
	protected A decodeFromTag(CompoundTag nbt) {
		val directed = nbt.getBoolean("directed");
		
		val nodeList = ExtNbtUtils.deserializeToStream(
				(ListTag)nbt.get("nodes"), 
				t->elementCodec.decode(NbtOps.INSTANCE, t).getOrThrow(false, Util.prefix("Error deserializing graph node: ", LOGGER::error)).getFirst())
			.toList();
		HashBiMap<Integer, N> nodeIdMap = HashBiMap.create();
		for (N node : nodeList) {
			nodeIdMap.put(nodeIdMap.size(),node);
		}
		val edgeList = ExtNbtUtils.deserializeToStream(
				nbt.getList("edges", Tag.TAG_INT_ARRAY), 
				t->((IntArrayTag)t).getAsIntArray())
			.map(a->Pair.of(nodeIdMap.get(a[0]), nodeIdMap.get(a[1])))
			.map(p-> directed?EndpointPair.ordered(p.getLeft(), p.getRight()):EndpointPair.unordered(p.getLeft(), p.getRight()) )
			.toList();
		Map<Integer, EndpointPair<N>> edgeIdMap = edgeList.stream()
				.collect(Collectors.toMap(e->getEdgeId(e, nodeIdMap.inverse()), Function.identity()));
		
		return finishDecodingGraph(nbt.get("extra"), nbt.getBoolean("mutable"), directed, nodeList, edgeList, nodeIdMap, edgeIdMap);
	}

    protected abstract A finishDecodingGraph(Tag extraNbt, boolean mutable, boolean directed, List<N> nodeList, List<EndpointPair<N>> edgeList, Map<Integer, N> nodeIdMap, Map<Integer, EndpointPair<N>> edgeIdMap);
	
	protected int getEdgeId(EndpointPair<?> edge, Map<N, Integer> nodeIdMap) {
		return (nodeIdMap.get(edge.nodeU())*nodeIdMap.size())+nodeIdMap.get(edge.nodeV());
	}
	

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BaseGraphCodec<?,?> othrCodec  = (BaseGraphCodec<?,?>) o;
        return Objects.equals(elementCodec, othrCodec.elementCodec);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), elementCodec);
    }
}
