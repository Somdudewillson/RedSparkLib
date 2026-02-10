package somdudewillson.redsparklib.serialization.codecs;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;

import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;

public abstract class NbtWrappingCodec<A, K extends Tag> implements Codec<A> {
	protected static final LoadingCache<Class<? extends Tag>, Codec<? extends Tag>> CODEC_CACHE = CacheBuilder.newBuilder()
			.weakValues()
			.build(CacheLoader.from(NbtWrappingCodec::tagCodecBuilder));
	protected final Codec<K> codec;
	
	@SuppressWarnings("unchecked")
	public NbtWrappingCodec(Class<K> tagClass) {
		Codec<K> tagCodec = null;
		try {
			tagCodec = (Codec<K>) CODEC_CACHE.get(tagClass);
		} catch (ExecutionException e) {}
		codec = tagCodec;
	}
	
	protected static <L extends Tag> Codec<L> tagCodecBuilder(Class<L> tagClass) {
		return (Codec<L>) Codec.PASSTHROUGH.comapFlatMap((inOps) -> {
			Tag tag = inOps.convert(NbtOps.INSTANCE).getValue();
			return tagClass.isInstance(tag) ? DataResult.success(tagClass.cast(tag)) : DataResult.error(() -> {
				return String.format("Not a %s: %s", tagClass.getSimpleName(), tag);
			});
		}, (inTag) -> {
			return new Dynamic<>(NbtOps.INSTANCE, inTag);
		});
	}

	@Override
	public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
		return codec.encode(encodeToTag(input), ops, prefix);
	}

	@Override
	public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
		return codec.decode(ops, input).map(pair->pair.mapFirst(this::decodeFromTag));
	}
	
	protected abstract K encodeToTag(A value);
	protected abstract A decodeFromTag(K nbt);
}
