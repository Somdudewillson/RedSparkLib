package somdudewillson.redsparklib.serialization.codecs;

import java.util.function.Function;

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

public class DelegatingNbtWrappingCodec<A, K extends Tag> extends NbtWrappingCodec<A, K> {
	private final Function<A, K> toNbtFunc;
	private final Function<K, A> fromNbtFunc;
	
	public DelegatingNbtWrappingCodec(Class<K> tagClass, Function<A, K> toNbtFunc, Function<K, A> fromNbtFunc) {
		super(tagClass);
		this.toNbtFunc = toNbtFunc;
		this.fromNbtFunc = fromNbtFunc;
	}
	
	public static <T> DelegatingNbtWrappingCodec<T, CompoundTag> compound(Function<T, CompoundTag> toNbtFunc, Function<CompoundTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(CompoundTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, ListTag> list(Function<T, ListTag> toNbtFunc, Function<ListTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(ListTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, ByteArrayTag> byteArray(Function<T, ByteArrayTag> toNbtFunc, Function<ByteArrayTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(ByteArrayTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, IntArrayTag> intArray(Function<T, IntArrayTag> toNbtFunc, Function<IntArrayTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(IntArrayTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, LongArrayTag> longArray(Function<T, LongArrayTag> toNbtFunc, Function<LongArrayTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(LongArrayTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, StringTag> string(Function<T, StringTag> toNbtFunc, Function<StringTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(StringTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, ByteTag> byteT(Function<T, ByteTag> toNbtFunc, Function<ByteTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(ByteTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, ShortTag> shortT(Function<T, ShortTag> toNbtFunc, Function<ShortTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(ShortTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, IntTag> intT(Function<T, IntTag> toNbtFunc, Function<IntTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(IntTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, LongTag> longT(Function<T, LongTag> toNbtFunc, Function<LongTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(LongTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, FloatTag> floatT(Function<T, FloatTag> toNbtFunc, Function<FloatTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(FloatTag.class, toNbtFunc, fromNbtFunc);
	}
	public static <T> DelegatingNbtWrappingCodec<T, DoubleTag> doubleT(Function<T, DoubleTag> toNbtFunc, Function<DoubleTag, T> fromNbtFunc) {
		return new DelegatingNbtWrappingCodec<>(DoubleTag.class, toNbtFunc, fromNbtFunc);
	}

	@Override
	protected K encodeToTag(A value) {
		return toNbtFunc.apply(value);
	}

	@Override
	protected A decodeFromTag(K nbt) {
		return fromNbtFunc.apply(nbt);
	}

}
