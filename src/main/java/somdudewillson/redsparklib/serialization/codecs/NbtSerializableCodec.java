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
import net.minecraftforge.common.util.INBTSerializable;
import somdudewillson.redsparklib.common.utils.ExtNbtUtils;

public class NbtSerializableCodec<A extends INBTSerializable<K>, K extends Tag> extends NbtWrappingCodec<A, K> {
	private final Class<A> targetClass;
	private final Supplier<A> instanceSupplier;
	
	public NbtSerializableCodec(Class<K> tagClass, Class<A> targetClass, Supplier<A> instanceSupplier) {
		super(tagClass);
		this.targetClass = targetClass;
		this.instanceSupplier = instanceSupplier;
	}
	
	public static <T extends INBTSerializable<CompoundTag>> NbtSerializableCodec<T, CompoundTag> compound(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(CompoundTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ListTag>> NbtSerializableCodec<T, ListTag> list(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(ListTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ByteArrayTag>> NbtSerializableCodec<T, ByteArrayTag> byteArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(ByteArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<IntArrayTag>> NbtSerializableCodec<T, IntArrayTag> intArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(IntArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<LongArrayTag>> NbtSerializableCodec<T, LongArrayTag> longArray(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(LongArrayTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<StringTag>> NbtSerializableCodec<T, StringTag> string(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(StringTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ByteTag>> NbtSerializableCodec<T, ByteTag> byteT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(ByteTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<ShortTag>> NbtSerializableCodec<T, ShortTag> shortT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(ShortTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<IntTag>> NbtSerializableCodec<T, IntTag> intT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(IntTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<LongTag>> NbtSerializableCodec<T, LongTag> longT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(LongTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<FloatTag>> NbtSerializableCodec<T, FloatTag> floatT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(FloatTag.class, targetClass, instanceSupplier);
	}
	public static <T extends INBTSerializable<DoubleTag>> NbtSerializableCodec<T, DoubleTag> doubleT(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<>(DoubleTag.class, targetClass, instanceSupplier);
	}
	
	/**
	 * Creates a new {@link NbtSerializableCodec} by automatically detecting the NBT tag type 
	 * through an initial invocation of the provided {@code instanceSupplier}.
	 * <p>
	 * This method instantiates a dummy object to inspect the class returned by 
	 * {@link INBTSerializable#serializeNBT()}. While convenient, the type-specific 
	 * static factory methods (such as {@link #compound(Class, Supplier)}, {@link #list(Class, Supplier)}, 
	 * etc.) should be preferred over this method to avoid the overhead of the extra 
	 * instantiation and to provide better compile-time type safety.
	 *
	 * @param <T> The type of the serializable object.
	 * @param <L> The type of the NBT tag the object serializes to.
	 * @param targetClass The class of the object to be coded.
	 * @param instanceSupplier A supplier that provides a fresh instance of the object for decoding.
	 * @return A new codec for the specified serializable type.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends INBTSerializable<L>, L extends Tag> NbtSerializableCodec<T, L> auto(Class<T> targetClass, Supplier<T> instanceSupplier) {
		return new NbtSerializableCodec<T,L>((Class<L>)instanceSupplier.get().serializeNBT().getClass(), targetClass, instanceSupplier);
	}

	@Override
	protected K encodeToTag(A value) { return value.serializeNBT(); }

	@Override
	protected A decodeFromTag(K nbt) { return ExtNbtUtils.inlineNbtDeserialize(nbt, instanceSupplier); }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NbtSerializableCodec<?,?> nbtCodec = (NbtSerializableCodec<?,?>) o;
        return Objects.equals(targetClass, nbtCodec.targetClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NbtSerializableCodec.class, targetClass);
    }

    @Override
    public String toString() {
        return "NbtSerializableCodec[" + targetClass.getName() + ']';
    }

}
