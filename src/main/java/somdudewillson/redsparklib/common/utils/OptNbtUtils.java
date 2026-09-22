package somdudewillson.redsparklib.common.utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

//? neoforge {
/*import net.minecraft.core.HolderLookup;
*///?}
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public class OptNbtUtils {
	@Nullable
	public static Tag put(CompoundTag tag, String pKey, @Nullable Tag pValue) {
		if (pValue == null) { return tag.get(pKey); }
		return tag.put(pKey, pValue);
	}
	@Nullable
	public static Tag put(CompoundTag tag, String pKey, Optional<? extends Tag> pValue) { return put(tag, pKey, pValue.orElse(null)); }
	@Nullable
	public static <T> Tag put(CompoundTag tag, String pKey, Optional<T> value, Function<T, ? extends Tag> serializer) { return put(tag, pKey, value.map(serializer).orElse(null)); }
	@Nullable
	public static <T> Tag put(CompoundTag tag, String pKey, @Nullable T value, Function<T, ? extends Tag> serializer) { return put(tag, pKey, Optional.ofNullable(value), serializer); }
	@Nullable
	public static Tag putSupplied(CompoundTag tag, String pKey, Supplier<? extends Tag> valueSupplier) { return put(tag, pKey, Optional.ofNullable(valueSupplier.get())); }
	@Nullable
	public static Tag putSuppliedOptional(CompoundTag tag, String pKey, Supplier<Optional<? extends Tag>> valueSupplier) { return put(tag, pKey, valueSupplier.get()); }
	@Nullable
	public static <T> Tag putSupplied(CompoundTag tag, String pKey, Supplier<T> valueSupplier, Function<T, ? extends Tag> serializer) { return put(tag, pKey, Optional.ofNullable(valueSupplier.get()), serializer); }
	@Nullable
	public static <T> Tag putSuppliedOptional(CompoundTag tag, String pKey, Supplier<Optional<T>> valueSupplier, Function<T, ? extends Tag> serializer) { return put(tag, pKey, valueSupplier.get(), serializer); }
	@Nullable
	public static <T extends INBTSerializable<?>> Tag putSerializable(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			CompoundTag tag, String pKey, Optional<T> value) {
		return put(tag, pKey, value.map(v->v.serializeNBT(/*? neoforge {*//*provider*//*?}*/)).orElse(null));
	}
	@Nullable
	public static <T extends INBTSerializable<?>> Tag putSerializable(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			CompoundTag tag, String pKey, @Nullable T value) {
		return putSerializable(/*? neoforge {*//*provider, *//*?}*/tag, pKey, Optional.ofNullable(value));
	}


	public static void putByte(CompoundTag tag, String pKey, @Nullable Byte pValue) {
		if (pValue == null) { return; }
		tag.putByte(pKey, pValue);
	}
	public static void putByte(CompoundTag tag, String pKey, Optional<Byte> pValue) { putByte(tag, pKey, pValue.orElse(null)); }

	public static void putShort(CompoundTag tag, String pKey, @Nullable Short pValue) {
		if (pValue == null) { return; }
		tag.putShort(pKey, pValue);
	}
	public static void putShort(CompoundTag tag, String pKey, Optional<Short> pValue) { putShort(tag, pKey, pValue.orElse(null)); }

	public static void putInt(CompoundTag tag, String pKey, @Nullable Integer pValue) {
		if (pValue == null) { return; }
		tag.putInt(pKey, pValue);
	}
	public static void putInt(CompoundTag tag, String pKey, Optional<Integer> pValue) { putInt(tag, pKey, pValue.orElse(null)); }

	public static void putLong(CompoundTag tag, String pKey, @Nullable Long pValue) {
		if (pValue == null) { return; }
		tag.putLong(pKey, pValue);
	}
	public static void putLong(CompoundTag tag, String pKey, Optional<Long> pValue) { putLong(tag, pKey, pValue.orElse(null)); }

	public static void putFloat(CompoundTag tag, String pKey, @Nullable Float pValue) {
		if (pValue == null) { return; }
		tag.putFloat(pKey, pValue);
	}
	public static void putFloat(CompoundTag tag, String pKey, Optional<Float> pValue) { putFloat(tag, pKey, pValue.orElse(null)); }

	public static void putDouble(CompoundTag tag, String pKey, @Nullable Double pValue) {
		if (pValue == null) { return; }
		tag.putDouble(pKey, pValue);
	}
	public static void putDouble(CompoundTag tag, String pKey, Optional<Double> pValue) { putDouble(tag, pKey, pValue.orElse(null)); }

	public static void putString(CompoundTag tag, String pKey, @Nullable String pValue) {
		if (pValue == null) { return; }
		tag.putString(pKey, pValue);
	}
	public static void putString(CompoundTag tag, String pKey, Optional<String> pValue) { putString(tag, pKey, pValue.orElse(null)); }

	public static void putByteArray(CompoundTag tag, String pKey, @Nullable byte[] pValue) {
		if (pValue == null) { return; }
		tag.putByteArray(pKey, pValue);
	}
	public static void putByteArray(CompoundTag tag, String pKey, Optional<byte[]> pValue) { putByteArray(tag, pKey, pValue.orElse(null)); }

	public static void putByteArray(CompoundTag tag, String pKey, @Nullable List<Byte> pValue) {
		if (pValue == null) { return; }
		tag.putByteArray(pKey, pValue);
	}
	public static void putByteListAsArray(CompoundTag tag, String pKey, Optional<List<Byte>> pValue) { putByteArray(tag, pKey, pValue.orElse(null)); }

	public static void putIntArray(CompoundTag tag, String pKey, @Nullable int[] pValue) {
		if (pValue == null) { return; }
		tag.putIntArray(pKey, pValue);
	}
	public static void putIntArray(CompoundTag tag, String pKey, Optional<int[]> pValue) { putIntArray(tag, pKey, pValue.orElse(null)); }

	public static void putIntArray(CompoundTag tag, String pKey, @Nullable List<Integer> pValue) {
		if (pValue == null) { return; }
		tag.putIntArray(pKey, pValue);
	}
	public static void putIntListAsArray(CompoundTag tag, String pKey, Optional<List<Integer>> pValue) { putIntArray(tag, pKey, pValue.orElse(null)); }

	public static void putLongArray(CompoundTag tag, String pKey, @Nullable long[] pValue) {
		if (pValue == null) { return; }
		tag.putLongArray(pKey, pValue);
	}
	public static void putLongArray(CompoundTag tag, String pKey, Optional<long[]> pValue) { putLongArray(tag, pKey, pValue.orElse(null)); }

	public static void putLongArray(CompoundTag tag, String pKey, @Nullable List<Long> pValue) {
		if (pValue == null) { return; }
		tag.putLongArray(pKey, pValue);
	}
	public static void putLongListAsArray(CompoundTag tag, String pKey, Optional<List<Long>> pValue) { putLongArray(tag, pKey, pValue.orElse(null)); }

	public static void putBoolean(CompoundTag tag, String pKey, @Nullable Boolean pValue) {
		if (pValue == null) { return; }
		tag.putBoolean(pKey, pValue);
	}
	public static void putBoolean(CompoundTag tag, String pKey, Optional<Boolean> pValue) { putBoolean(tag, pKey, pValue.orElse(null)); }

	public static void putUUID(CompoundTag tag, String pKey, @Nullable UUID pValue) {
		if (pValue == null) { return; }
		tag.putUUID(pKey, pValue);
	}
	public static void putUUID(CompoundTag tag, String pKey, Optional<UUID> pValue) { putUUID(tag, pKey, pValue.orElse(null)); }


	public static Optional<Tag> get(CompoundTag tag, String pKey) {
		return Optional.ofNullable(tag.get(pKey));
	}
	@SuppressWarnings("unchecked")
	public static <T extends INBTSerializable<U>, U extends Tag> Optional<T> getSerializable(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			CompoundTag tag, String pKey, 
			Class<U> tagClass, Supplier<T> valueSupplier) {
		return get(tag, pKey).map(t->{
			T instance = valueSupplier.get();
			if (tagClass.isAssignableFrom(t.getClass())) {
				instance.deserializeNBT(/*? neoforge {*//*provider, *//*?}*/(U)t);
			} else {
				return null;
			}
			return instance;
		});
	}
	public static <T extends INBTSerializable<CompoundTag>> Optional<T> getSerializable(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			CompoundTag tag, String pKey, 
			Supplier<T> valueSupplier) {
		return getSerializable(/*? neoforge {*//*provider, *//*?}*/tag, pKey, CompoundTag.class, valueSupplier);
	}

	public static Optional<Byte> getByte(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_ANY_NUMERIC) ? Optional.of(tag.getByte(pKey)) : Optional.empty();
	}

	public static Optional<Short> getShort(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_ANY_NUMERIC) ? Optional.of(tag.getShort(pKey)) : Optional.empty();
	}

	public static Optional<Integer> getInt(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_ANY_NUMERIC) ? Optional.of(tag.getInt(pKey)) : Optional.empty();
	}

	public static Optional<Long> getLong(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_ANY_NUMERIC) ? Optional.of(tag.getLong(pKey)) : Optional.empty();
	}

	public static Optional<Float> getFloat(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_ANY_NUMERIC) ? Optional.of(tag.getFloat(pKey)) : Optional.empty();
	}

	public static Optional<Double> getDouble(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_ANY_NUMERIC) ? Optional.of(tag.getDouble(pKey)) : Optional.empty();
	}

	public static Optional<String> getString(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_STRING) ? Optional.of(tag.getString(pKey)) : Optional.empty();
	}

	public static Optional<byte[]> getByteArray(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_BYTE_ARRAY) ? Optional.of(tag.getByteArray(pKey)) : Optional.empty();
	}

	public static Optional<int[]> getIntArray(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_INT_ARRAY) ? Optional.of(tag.getIntArray(pKey)) : Optional.empty();
	}

	public static Optional<long[]> getLongArray(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_LONG_ARRAY) ? Optional.of(tag.getLongArray(pKey)) : Optional.empty();
	}

	public static Optional<CompoundTag> getCompound(CompoundTag tag, String pKey) {
		return tag.contains(pKey, Tag.TAG_COMPOUND) ? Optional.of(tag.getCompound(pKey)) : Optional.empty();
	}

	public static Optional<ListTag> getList(CompoundTag tag, String pKey, int pTagType) {
		if (!tag.contains(pKey, Tag.TAG_LIST)) { return Optional.empty(); }
		ListTag listTag = (ListTag)tag.get(pKey);
		return listTag.isEmpty() || listTag.getElementType()==pTagType ? Optional.of(listTag) : Optional.empty();
	}
	public static Optional<ListTag> getList(CompoundTag tag, String pKey) {
		if (!tag.contains(pKey, Tag.TAG_LIST)) { return Optional.empty(); }
		ListTag listTag = (ListTag)tag.get(pKey);
		return Optional.ofNullable(listTag);
	}

	public static Optional<Boolean> getBoolean(CompoundTag tag, String pKey) {
		return getByte(tag, pKey).map(b -> b!=0);
	}

	public static Optional<UUID> getUUID(CompoundTag tag, String pKey) {
		return tag.hasUUID(pKey) ? Optional.of(NbtUtils.loadUUID(tag.get(pKey))) : Optional.empty();
	}
}
