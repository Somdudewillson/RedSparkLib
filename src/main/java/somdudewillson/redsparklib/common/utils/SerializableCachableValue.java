package somdudewillson.redsparklib.common.utils;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

@RequiredArgsConstructor
public class SerializableCachableValue<T extends Tag, E> {
	private final AtomicReference<T> serializedValue = new AtomicReference<>();
	private final AtomicReference<E> cachedValue = new AtomicReference<>();
	
	private final BiFunction<HolderLookup.Provider, E,T> serializer;
	private final BiFunction<HolderLookup.Provider, T,E> deserializer;
	
	public SerializableCachableValue(Function<E,T> serializer, Function<T,E> deserializer, E value) {
		this((p, v)->serializer.apply(v), (p, t)->deserializer.apply(t), value);
	}
	public SerializableCachableValue(Function<E,T> serializer, Function<T,E> deserializer, T tag) {
		this((p, v)->serializer.apply(v), (p, t)->deserializer.apply(t), tag);
	}
	
	public SerializableCachableValue(BiFunction<HolderLookup.Provider,E,T> serializer, BiFunction<HolderLookup.Provider,T,E> deserializer, E value) {
		this(serializer, deserializer);
		cachedValue.set(value);
	}
	public SerializableCachableValue(BiFunction<HolderLookup.Provider,E,T> serializer, BiFunction<HolderLookup.Provider,T,E> deserializer, T tag) {
		this(serializer, deserializer);
		serializedValue.set(tag);
	}
	
	public static <S extends Tag, V extends INBTSerializable<S>> SerializableCachableValue<S, V> fromSerializableValue(V serializableValue, Supplier<V> instanceSupplier) {
		return new SerializableCachableValue<S,V>(
				(p, v)->v.serializeNBT(/*? neoforge {*//*p*//*?}*/), 
				(p, t)->ExtNbtUtils.inlineNbtDeserialize(/*? neoforge {*//*p, *//*?}*/t, instanceSupplier),
				serializableValue);
	}
	public static <S extends Tag, V extends INBTSerializable<S>> SerializableCachableValue<S, V> fromSerializable(Supplier<V> instanceSupplier) {
		return new SerializableCachableValue<S,V>(
				(p, v)->v.serializeNBT(/*? neoforge {*//*p*//*?}*/), 
				(p, t)->ExtNbtUtils.inlineNbtDeserialize(/*? neoforge {*//*p, *//*?}*/t, instanceSupplier));
	}
	
	@Locked.Write
	public void setValue(@Nullable E newValue) {
		cachedValue.set(newValue);
		serializedValue.set(null);
	}
	@Locked.Write
	public void setSerialized(@Nullable T newSerialized) {
		serializedValue.set(newSerialized);
		cachedValue.set(null);
	}
	@SuppressWarnings("unchecked")
	public void setSerializedFromTag(@Nullable Tag tag) throws ClassCastException {
		setSerialized((T)tag);
	}
	@Locked.Write
	public void clear() {
		serializedValue.set(null);
		cachedValue.set(null);
	}

	public void setValueFromSerialized(HolderLookup.Provider provider, @Nullable T serialized) {
		if (serialized != null) {
			setValue(deserializer.apply(provider, serialized));
		} else {
			clear();
		}
	}
	@SuppressWarnings("unchecked")
	public void setValueFromTag(HolderLookup.Provider provider, @Nullable Tag tag) throws ClassCastException {
		setValueFromSerialized(provider, (T)tag);
	}
	public void setSerializedFromValue(HolderLookup.Provider provider, @Nullable E value) {
		if (value != null) {
			setSerialized(serializer.apply(provider, value));
		} else {
			clear();
		}
	}

	@Locked.Read
	public boolean hasValue() {
		return serializedValue.get() != null || cachedValue.get() != null;
	}

	@Locked.Read
	public Optional<E> tryGetValue(HolderLookup.Provider provider) {
		return Optional.ofNullable(cachedValue.updateAndGet(v->{
			if (v!=null) { return v; }
			val serialized = serializedValue.get();
			if (serialized==null) { return null; }
			return deserializer.apply(provider, serialized);
		}));
	}

	@Locked.Read
	public Optional<T> tryGetSerialized(HolderLookup.Provider provider) {
		return Optional.ofNullable(serializedValue.updateAndGet(v->{
			if (v!=null) { return v; }
			val cached = cachedValue.get();
			if (cached==null) { return null; }
			return serializer.apply(provider, cached);
		}));
	}
}