package somdudewillson.redsparklib.serialization.codecs;

import java.util.Objects;
import java.util.function.Supplier;

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
import somdudewillson.redsparklib.common.utils.ExtNbtUtils;
import somdudewillson.redsparklib.common.utils.IContextFreeNBTSerializable;

public class NbtContextFreeSerializableCodec<A extends IContextFreeNBTSerializable<K>, K extends Tag> extends NbtWrappingCodec<A, K> {
	private final Class<A> targetClass;
	private final Supplier<A> instanceSupplier;
	
	public NbtContextFreeSerializableCodec(Class<K> tagClass, Class<A> targetClass, Supplier<A> instanceSupplier) {
		super(tagClass);
		this.targetClass = targetClass;
		this.instanceSupplier = instanceSupplier;
	}
	
	public static <T extends IContextFreeNBTSerializable<CompoundTag>> NbtContextFreeSerializableCodec<T, CompoundTag> compound(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(CompoundTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<ListTag>> NbtContextFreeSerializableCodec<T, ListTag> list(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(ListTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<ByteArrayTag>> NbtContextFreeSerializableCodec<T, ByteArrayTag> byteArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(ByteArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<IntArrayTag>> NbtContextFreeSerializableCodec<T, IntArrayTag> intArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(IntArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<LongArrayTag>> NbtContextFreeSerializableCodec<T, LongArrayTag> longArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(LongArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<StringTag>> NbtContextFreeSerializableCodec<T, StringTag> string(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(StringTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<ByteTag>> NbtContextFreeSerializableCodec<T, ByteTag> byteT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(ByteTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<ShortTag>> NbtContextFreeSerializableCodec<T, ShortTag> shortT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(ShortTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<IntTag>> NbtContextFreeSerializableCodec<T, IntTag> intT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(IntTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<LongTag>> NbtContextFreeSerializableCodec<T, LongTag> longT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(LongTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<FloatTag>> NbtContextFreeSerializableCodec<T, FloatTag> floatT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(FloatTag.class, targetClass, instanceSupplier);
	}
	public static <T extends IContextFreeNBTSerializable<DoubleTag>> NbtContextFreeSerializableCodec<T, DoubleTag> doubleT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<>(DoubleTag.class, targetClass, instanceSupplier);
	}
	
	/**
	 * Creates a new {@link NbtContextFreeSerializableCodec} by automatically detecting the NBT tag type 
	 * through an initial invocation of the provided {@code instanceSupplier}.
	 * <p>
	 * This method instantiates and serializes a dummy object to inspect its serialized class.
	 * While convenient, the type-specific static factory methods 
	 * (such as {@link #compound(Class, Supplier)}, {@link #list(Class, Supplier)}, etc.) 
	 * should be preferred over this method to avoid the overhead of the extra 
	 * instantiation and to provide better compile-time type safety.
	 *
	 * @param <T> The type of the serializable object.
	 * @param <L> The type of the NBT tag the object serializes to.
	 * @param targetClass The class of the object to be coded.
	 * @param instanceSupplier A supplier that provides a fresh instance of the object for decoding.
	 * @return A new codec for the specified serializable type.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IContextFreeNBTSerializable<L>, L extends Tag> NbtContextFreeSerializableCodec<T, L> auto(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtContextFreeSerializableCodec<T,L>((Class<L>)instanceSupplier.get().serializeNBTContextless().getClass(), targetClass, instanceSupplier);
	}

	@Override
	protected K encodeToTag(A value) {
		return value.serializeNBTContextless();
	}

	@Override
	protected A decodeFromTag(K nbt) {
		return ExtNbtUtils.inlineNbtDeserializeContextFree(nbt, instanceSupplier);
	}

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NbtContextFreeSerializableCodec<?,?> nbtCodec = (NbtContextFreeSerializableCodec<?,?>) o;
        return Objects.equals(targetClass, nbtCodec.targetClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NbtContextFreeSerializableCodec.class, targetClass);
    }

    @Override
    public String toString() {
        return "NbtSerializableCodec[" + targetClass.getName() + ']';
    }

}
