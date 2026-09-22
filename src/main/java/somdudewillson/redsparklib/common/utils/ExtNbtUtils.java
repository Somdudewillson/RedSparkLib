package somdudewillson.redsparklib.common.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

import lombok.val;
import net.minecraft.core.BlockPos;
//? neoforge {
/*import net.minecraft.core.HolderLookup;
*///?} forge {
import net.minecraftforge.registries.IForgeRegistry;
//?}
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.Registry;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.RegistryObject;

public class ExtNbtUtils {

	// BlockPos Serialization
	public static LongTag serializeBlockPos(BlockPos pos) {
		return LongTag.valueOf(pos.asLong());
	}
	public static BlockPos deserializeBlockPos(NumericTag nbt) {
		return BlockPos.of(nbt.getAsLong());
	}
	public static BlockPos deserializeBlockPos(Tag nbt) {
		return deserializeBlockPos((NumericTag) nbt);
	}
	public static BlockPos deserializeBlockPos(BlockPos __, Tag nbt) {
		return deserializeBlockPos((NumericTag) nbt);
	}

	// Bounding Box Serialization
	public static ListTag serializeBoundingBox(AABB bbox) {
		ListTag root = new ListTag();
		
		root.add(DoubleTag.valueOf(bbox.minX));
		root.add(DoubleTag.valueOf(bbox.minY));
		root.add(DoubleTag.valueOf(bbox.minZ));
		root.add(DoubleTag.valueOf(bbox.maxX));
		root.add(DoubleTag.valueOf(bbox.maxY));
		root.add(DoubleTag.valueOf(bbox.maxZ));
		
		return root;
	}
	public static AABB deserializeBoundingBox(ListTag nbt) {
		return new AABB(
				nbt.getDouble(0),
				nbt.getDouble(1),
				nbt.getDouble(2),
				nbt.getDouble(3),
				nbt.getDouble(4),
				nbt.getDouble(5)
				);
	}
	public static AABB deserializeBoundingBox(BlockPos __, Tag nbt) {
		return deserializeBoundingBox((ListTag) nbt);
	}
	
	// Stream Serialization
	public static <K extends Tag> ListTag serializeTagStream(Stream<K> stream) {
		return serializeStream(stream, Function.identity());
	}
	public static <T extends Tag, K extends INBTSerializable<T>> ListTag serializeStream(Stream<K> stream/*? neoforge {*//*, HolderLookup.Provider provider*//*?}*/) {
		//? neoforge {
		/*return serializeStream(stream, makeInlineStubbedNbtSerializer(provider));
		*///?} forge {
		return serializeStream(stream, K::serializeNBT);
		//?}
	}
	public static <K, V extends Tag> ListTag serializeStream(Stream<K> stream, Function<K, V> valueSerializer) {
		ListTag listTag = new ListTag();
		stream.map(valueSerializer).forEachOrdered(listTag::add);
	
		return listTag;
	}
	public static <KN extends Tag,K> Stream<K> deserializeToStream(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, 
			Supplier<K> valueFactory, TagDeserializerFunction<K, KN> valueDeserializer) {
		//? neoforge {
		/*return deserializeToStream(nbt, makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeToStream(nbt, makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	@SuppressWarnings("unchecked")
	public static <KN extends Tag,K> Stream<K> deserializeToStream(
			ListTag nbt, 
			Function<KN, K> valueDeserializer) {
		return nbt.stream()
				.map(t->(KN)t)
				.map(valueDeserializer);
	}
	
	// List Serialization
	public static <K extends Tag> ListTag serializeTagList(List<K> list) {
		return serializeList(list, Function.identity());
	}
	public static <T extends Tag, K extends INBTSerializable<T>> ListTag serializeList(List<K> list/*? neoforge {*//*, HolderLookup.Provider provider*//*?}*/) {
		//? neoforge {
		/*return serializeList(list, makeInlineStubbedNbtSerializer(provider));
		*///?} forge {
		return serializeList(list, K::serializeNBT);
		//?}
	}
	public static <K, V extends Tag> ListTag serializeList(List<K> list, Function<K, V> valueSerializer) {
		return serializeStream(list.stream(), valueSerializer);
	}
	public static <KN extends Tag,K extends INBTSerializable<KN>,LN extends List<K>> LN deserializeIntoList(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, LN list, 
			Supplier<K> valueFactory) {
		return deserializeIntoList(/*? neoforge {*//*provider, *//*?}*/nbt, list, valueFactory, K::deserializeNBT);
	}
	public static <KN extends Tag,K,LN extends List<K>> LN deserializeIntoList(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, LN list, 
			Supplier<K> valueFactory, TagDeserializerFunction<K, KN> valueDeserializer) {
		//? neoforge {
		/*return deserializeIntoList(nbt, list, makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeIntoList(nbt, list, makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	public static <KN extends Tag,K,LN extends List<K>> LN deserializeIntoList(ListTag nbt, LN list, 
			Function<KN, K> valueDeserializer) {
		deserializeToStream(nbt, valueDeserializer)
			.forEachOrdered(list::add);
		return list;
	}
	
	// Set Serialization
	public static <K extends Tag> ListTag serializeTagSet(Set<K> set) {
		return serializeSet(set, Function.identity());
	}
	public static <T extends Tag, K extends INBTSerializable<T>> ListTag serializeSet(Set<K> set/*? neoforge {*//*, HolderLookup.Provider provider*//*?}*/) {
		//? neoforge {
		/*return serializeSet(set, makeInlineStubbedNbtSerializer(provider));
		*///?} forge {
		return serializeSet(set, K::serializeNBT);
		//?}
	}
	public static <K, V extends Tag> ListTag serializeSet(Set<K> set, Function<K, V> valueSerializer) {
		return serializeStream(set.stream(), valueSerializer);
	}
	public static <KN extends Tag,K extends INBTSerializable<KN>,SN extends Set<K>> SN deserializeIntoSet(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, SN set, 
			Supplier<K> valueFactory) {
		return deserializeIntoSet(/*? neoforge {*//*provider, *//*?}*/nbt, set, valueFactory, K::deserializeNBT);
	}
	public static <KN extends Tag,K,SN extends Set<K>> SN deserializeIntoSet(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, SN set, 
			Supplier<K> valueFactory, TagDeserializerFunction<K, KN> valueDeserializer) {
		//? neoforge {
		/*return deserializeIntoSet(nbt, set, makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeIntoSet(nbt, set, makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	public static <KN extends Tag,K,SN extends Set<K>> SN deserializeIntoSet(ListTag nbt, SN set, 
			Function<KN, K> valueDeserializer) {
		deserializeToStream(nbt, valueDeserializer)
			.forEachOrdered(set::add);
		return set;
	}
	
	// Multiset Serialization
	public static <K extends Tag> ListTag serializeTagMultiset(Multiset<K> multiset) {
		return serializeMultiset(multiset, Function.identity());
	}
	public static <T extends Tag, K extends INBTSerializable<T>> ListTag serializeMultiset(Multiset<K> multiset/*? neoforge {*//*, HolderLookup.Provider provider*//*?}*/) {
		//? neoforge {
		/*return serializeMultiset(multiset, makeInlineStubbedNbtSerializer(provider));
		*///?} forge {
		return serializeMultiset(multiset, K::serializeNBT);
		//?}
	}
	public static <K, V extends Tag> ListTag serializeMultiset(Multiset<K> multiset, Function<K, V> valueSerializer) {
		return serializeStream(multiset.entrySet().stream(), entry -> {
			CompoundTag entryTag = new CompoundTag();
			entryTag.put("e", valueSerializer.apply(entry.getElement()));
			entryTag.put("c", IntTag.valueOf(entry.getCount()));
			return entryTag;
		});
	}
	public static <KN extends Tag,K extends INBTSerializable<KN>,SN extends Multiset<K>> SN deserializeIntoMultiset(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, SN multiset, 
			Supplier<K> valueFactory) {
		return deserializeIntoMultiset(/*? neoforge {*//*provider, *//*?}*/nbt, multiset, valueFactory, K::deserializeNBT);
	}
	public static <KN extends Tag,K,SN extends Multiset<K>> SN deserializeIntoMultiset(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, SN multiset, 
			Supplier<K> valueFactory, TagDeserializerFunction<K, KN> valueDeserializer) {
		//? neoforge {
		/*return deserializeIntoMultiset(nbt, multiset, makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeIntoMultiset(nbt, multiset, makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	@SuppressWarnings("unchecked")
	public static <KN extends Tag,K,SN extends Multiset<K>> SN deserializeIntoMultiset(ListTag nbt, SN multiset, 
			Function<KN, K> valueDeserializer) {
		deserializeToStream(nbt, e -> (CompoundTag) e)
			.forEachOrdered(e -> {
				K element = valueDeserializer.apply((KN) e.get("e"));
				int count = ((NumericTag) e.get("c")).getAsInt();
				multiset.add(element, count);
			});
		return multiset;
	}
	
	// Map Serialization
	public static <KT extends Tag, K extends INBTSerializable<KT>, VT extends Tag, V extends INBTSerializable<VT>> ListTag serializeMap(
			Map<K,V> map/*? neoforge {*//*,
			HolderLookup.Provider provider*//*?}*/) {
		//? neoforge {
		/*return serializeMap(map, makeInlineStubbedNbtSerializer(provider), makeInlineStubbedNbtSerializer(provider));
		*///?} forge {
		return serializeMap(map, K::serializeNBT, V::serializeNBT);
		//?}
	}
	public static <KT extends Tag, K, VT extends Tag, V> ListTag serializeMap(Map<K,V> map, 
			Function<K, KT> keySerializer, Function<V, VT> valueSerializer) {
		return serializeStream(map.entrySet().stream(), e->{
			CompoundTag entryTag = new CompoundTag();
			
			entryTag.put("k", keySerializer.apply(e.getKey()));
			entryTag.put("v", valueSerializer.apply(e.getValue()));
			
			return entryTag;
		});
	}

	@SuppressWarnings("unchecked")
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Map<K,V>> MN deserializeIntoMap(ListTag nbt, MN map, 
			Function<KN, K> keyDeserializer, Function<VN, V> valueDeserializer) {
		deserializeToStream(nbt, e->(CompoundTag)e)
			.forEachOrdered(e -> {
				map.put(
						keyDeserializer.apply( (KN)e.get("k") ), 
						valueDeserializer.apply( (VN)e.get("v") )
						);
			});
		
		return map;
	}
	
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Map<K,V>> MN deserializeIntoMap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			TagDeserializerFunction<K, KN> keyDeserializer, TagDeserializerFunction<V, VN> valueDeserializer, 
			Supplier<K> keyFactory, Supplier<V> valueFactory) {
		//? neoforge {
		/*return deserializeIntoMap(nbt, map, makeInlineStubbedNbtDeserializer(provider, keyFactory, keyDeserializer), makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeIntoMap(nbt, map, makeInlineNbtDeserializer(keyFactory, keyDeserializer), makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Map<K,V>> MN deserializeIntoMap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			Function<KN, K> keyDeserializer, TagDeserializerFunction<V, VN> valueDeserializer, 
			Supplier<V> valueFactory) {
		//? neoforge {
		/*return deserializeIntoMap(nbt, map, keyDeserializer, makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeIntoMap(nbt, map, keyDeserializer, makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Map<K,V>> MN deserializeIntoMap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			TagDeserializerFunction<K, KN> keyDeserializer, Function<VN, V> valueDeserializer, 
			Supplier<K> keyFactory) {
		//? neoforge {
		/*return deserializeIntoMap(nbt, map, makeInlineStubbedNbtDeserializer(provider, keyFactory, keyDeserializer), valueDeserializer);
		*///?} forge {
		return deserializeIntoMap(nbt, map, makeInlineNbtDeserializer(keyFactory, keyDeserializer), valueDeserializer);
		//?}
	}
	
	public static <KN extends Tag,VN extends Tag,K extends INBTSerializable<KN>,V extends INBTSerializable<VN>,MN extends Map<K,V>> MN deserializeIntoMap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			Supplier<K> keyFactory, Supplier<V> valueFactory) {
		return deserializeIntoMap(/*? neoforge {*//*provider, *//*?}*/nbt, map, K::deserializeNBT, V::deserializeNBT, keyFactory, valueFactory);
	}
	
	// Multimap Serialization
	public static <KT extends Tag, K extends INBTSerializable<KT>, VT extends Tag, V extends INBTSerializable<VT>> ListTag serializeMultimap(
			Multimap<K,V> map/*? neoforge {*//*,
			HolderLookup.Provider provider*//*?}*/) {
		//? neoforge {
		/*return serializeMultimap(map, makeInlineStubbedNbtSerializer(provider), makeInlineStubbedNbtSerializer(provider));
		*///?} forge {
		return serializeMultimap(map, K::serializeNBT, V::serializeNBT);
		//?}
	}
	public static <KT extends Tag, K, KV extends Tag, V> ListTag serializeMultimap(Multimap<K,V> map, 
			Function<K, KT> keySerializer, Function<V, KV> valueSerializer) {
		return serializeStream(map.asMap().entrySet().stream(), e -> {
			CompoundTag entryTag = new CompoundTag();
			
			entryTag.put("k", keySerializer.apply(e.getKey()));
			
			ListTag listTag = new ListTag();
			e.getValue().forEach(v -> listTag.add(valueSerializer.apply(v)));
			entryTag.put("v", listTag);
			
			return entryTag;
		});
	}

	@SuppressWarnings("unchecked")
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Multimap<K,V>> MN deserializeIntoMultimap(ListTag nbt, MN map, 
			Function<KN, K> keyDeserializer, Function<VN, V> valueDeserializer) {
		deserializeToStream(nbt, e->(CompoundTag)e)
			.forEachOrdered(e -> {
				K key = keyDeserializer.apply((KN) e.get("k"));
				ListTag valuesTag = (ListTag) e.get("v");
				valuesTag.forEach(t -> {
					map.put(key, valueDeserializer.apply((VN) t));
				});
			});
		
		return map;
	}
	
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Multimap<K,V>> MN deserializeIntoMultimap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			TagDeserializerFunction<K, KN> keyDeserializer, TagDeserializerFunction<V, VN> valueDeserializer, 
			Supplier<K> keyFactory, Supplier<V> valueFactory) {
		//? neoforge {
		/*return deserializeIntoMultimap(nbt, map, makeInlineStubbedNbtDeserializer(provider, keyFactory, keyDeserializer), makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeIntoMultimap(nbt, map, makeInlineNbtDeserializer(keyFactory, keyDeserializer), makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Multimap<K,V>> MN deserializeIntoMultimap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			Function<KN, K> keyDeserializer, TagDeserializerFunction<V, VN> valueDeserializer, 
			Supplier<V> valueFactory) {
		//? neoforge {
		/*return deserializeIntoMultimap(nbt, map, keyDeserializer, makeInlineStubbedNbtDeserializer(provider, valueFactory, valueDeserializer));
		*///?} forge {
		return deserializeIntoMultimap(nbt, map, keyDeserializer, makeInlineNbtDeserializer(valueFactory, valueDeserializer));
		//?}
	}
	public static <KN extends Tag,VN extends Tag,K,V,MN extends Multimap<K,V>> MN deserializeIntoMultimap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			TagDeserializerFunction<K, KN> keyDeserializer, Function<VN, V> valueDeserializer, 
			Supplier<K> keyFactory) {
		//? neoforge {
		/*return deserializeIntoMultimap(nbt, map, makeInlineStubbedNbtDeserializer(provider, keyFactory, keyDeserializer), valueDeserializer);
		*///?} forge {
		return deserializeIntoMultimap(nbt, map, makeInlineNbtDeserializer(keyFactory, keyDeserializer), valueDeserializer);
		//?}
	}
	
	public static <KN extends Tag,VN extends Tag,K extends INBTSerializable<KN>,V extends INBTSerializable<VN>,MN extends Multimap<K,V>> MN deserializeIntoMultimap(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			ListTag nbt, MN map, 
			Supplier<K> keyFactory, Supplier<V> valueFactory) {
		return deserializeIntoMultimap(/*? neoforge {*//*provider, *//*?}*/nbt, map, K::deserializeNBT, V::deserializeNBT, keyFactory, valueFactory);
	}
	
	// Enum Serialization
	
	public static Tag serializeEnumValue(Enum<?> value, boolean useName) {
		return useName ? StringTag.valueOf(value.name()) : IntTag.valueOf(value.ordinal());
	}
	public static Tag serializeEnumValue(Enum<?> value) { return serializeEnumValue(value, true); }

	public static <T extends Enum<T>> T tryDeserializeEnumValue(@Nullable Tag nbt, Class<T> enumClass, T defaultValue) {
		return tryDeserializeEnumValue(nbt, enumClass).orElse(defaultValue);
	}
	public static <T extends Enum<T>> Optional<T> tryDeserializeEnumValue(@Nullable Tag nbt, Class<T> enumClass) {
		if (nbt == null) { return Optional.empty(); }
		return Optional.of(deserializeEnumValue(nbt, enumClass));
	}
	public static <T extends Enum<T>> T deserializeEnumValue(Tag nbt, Class<T> enumClass) {
		T[] enumConstants = enumClass.getEnumConstants();
		if (nbt.getType() == StringTag.TYPE) {
			final String targetName = nbt.getAsString();
			return Arrays.stream(enumConstants).filter(e->e.name().equals(targetName)).findAny().orElseThrow(()->new NoSuchElementException("No matching enum found for name '"+targetName+"'"));
		}
		int idx = ((NumericTag)nbt).getAsInt();
		if (idx<0 || idx>=enumConstants.length ) {
			throw new NoSuchElementException("No matching enum found for ordinal "+idx);
		}
		return enumConstants[idx];
	}
	
	// Resource Location Serialization
	public static Tag serializeResourceLocation(ResourceLocation resourceLocation) {
		return ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, resourceLocation).getOrThrow(/*? < 1.21 {*/false, s->{}/*?}*/);
	}
	public static ResourceLocation deserializeResourceLocation(Tag tag) {
		return ResourceLocation.CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow(/*? < 1.21 {*/false, s->{}/*?}*/).getFirst();
	}
	
	// Resource Key Serialization
	public static <T> Tag serializeResourceKey(ResourceKey<? extends Registry<T>> registryKey, ResourceKey<T> resourceKey) {
		return ResourceKey.codec(registryKey).encodeStart(NbtOps.INSTANCE, resourceKey).getOrThrow(/*? < 1.21 {*/false, s->{}/*?}*/);
	}
	public static <T> ResourceKey<T> deserializeResourceKey(ResourceKey<? extends Registry<T>> registryKey, Tag tag) {
		return ResourceKey.codec(registryKey).decode(NbtOps.INSTANCE, tag).getOrThrow(/*? < 1.21 {*/false, s->{}/*?}*/).getFirst();
	}
	
	// Registry Value Serialization
	//? forge {
	public static <T> CompoundTag serializeRegistryValue(T registeredValue, IForgeRegistry<T> registry) {
		return serializeRegistryObject(RegistryObject.create(registry.getKey(registeredValue), registry));
	}
	//?}
	public static <T> CompoundTag serializeRegistryValue(T registeredValue, Registry<T> registry) {
		//? neoforge {
		/*return serializeRegistryObject(RegistryObject.create(registry.key(), registry.getKey(registeredValue)));
		*///?} forge {
		return serializeRegistryObject(RegistryObject.create(registry.getKey(registeredValue), registry.key(), "ext_serializer"));
		//?}
	}
	public static <T> CompoundTag serializeRegistryObject(RegistryObject<T/*? neoforge {*//*, ? extends T*//*?}*/> registryObject) {
		val registryKey = registryObject.getKey();
		CompoundTag root = new CompoundTag();
		
		root.put("registry", serializeResourceLocation(registryKey.registry()));
		root.put("name", serializeResourceLocation(registryKey.location()));
		
		return root;
	}
	public static <T, R extends Registry<T>> Optional<Tag> trySerializeVanillaRegistryValue(T registeredValue, ResourceKey<R> registryKey) {
		val registry = VanillaRegistries.createLookup().lookupOrThrow(registryKey);
		val valueKey = registry.listElementIds()
				//? neoforge {
				/*.filter( k->registry.get(k).map(v->v.value().equals(registeredValue)).orElse(false) )
				*///?} forge {
				.filter( k->registry.get(k).map(v->v.get().equals(registeredValue)).orElse(false) )
				//?}
				.findAny();
		
		return valueKey.map(k->serializeResourceKey(registryKey, k));
	}
	
	public static RegistryObject<?/*? neoforge {*//*, ?*//*?}*/> deserializeRegistryObject(CompoundTag nbt) {
		val registryLocation = deserializeResourceLocation(nbt.get("registry"));
		val name = deserializeResourceLocation(nbt.get("name"));
		
		//? neoforge {
		/*return RegistryObject.create(registryLocation, name);
		*///?} forge {
		return RegistryObject.createOptional(name, registryLocation, name.getNamespace());
		//?}
	}
	public static <T, R extends Registry<T>> Optional<T> tryDeserializeVanillaRegistryValue(Tag nbt, ResourceKey<R> registryKey) {
		val registry = VanillaRegistries.createLookup().lookupOrThrow(registryKey);
		//? neoforge {
		/*return registry.get(deserializeResourceKey(registryKey, nbt))
				.filter(Reference::isBound)
				.map(Reference::value);
		*///?} forge {
		return registry.get(deserializeResourceKey(registryKey, nbt)).map(Reference::get);
		//?}
	}

	public static <T extends Tag, U> U inlineNbtDeserialize(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			T tag, 
			Supplier<U> instanceSupplier, 
			TagDeserializerFunction<U,T> deserializerFunction) {
		U instance = instanceSupplier.get();
		deserializerFunction.deserialize(instance, /*? neoforge {*//*provider, *//*?}*/tag);
		return instance;
	}
	public static <T extends Tag, U extends INBTSerializable<T>> U inlineNbtDeserialize(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			T tag, 
			Supplier<U> instanceSupplier) {
		return inlineNbtDeserialize(/*? neoforge {*//*provider, *//*?}*/tag, instanceSupplier, INBTSerializable::deserializeNBT);
	}
	public static <T extends Tag, U extends IContextFreeNBTSerializable<T>> U inlineNbtDeserializeContextFree(
			T tag, 
			Supplier<U> instanceSupplier) {
		return inlineNbtDeserialize(/*? neoforge {*//*null, *//*?}*/tag, instanceSupplier, INBTSerializable::deserializeNBT);
	}
	public static <T extends Tag, U extends INBTSerializable<T>> TagInlineDeserializerFunction<T,U> makeInlineNbtDeserializer(Supplier<U> instanceSupplier) {
		return (/*? neoforge {*//*provider, *//*?}*/tag) -> inlineNbtDeserialize(/*? neoforge {*//*provider, *//*?}*/tag, instanceSupplier);
	}
	public static <T extends Tag, U> TagInlineDeserializerFunction<T,U> makeInlineNbtDeserializer(Supplier<U> instanceSupplier, TagDeserializerFunction<U,T> deserializerFunction) {
		return (/*? neoforge {*//*provider, *//*?}*/tag) -> inlineNbtDeserialize(/*? neoforge {*//*provider, *//*?}*/tag, instanceSupplier, deserializerFunction);
	}
	
	public static <T extends Tag, U> T inlineNbtSerialize(
			U instance,
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			TagSerializerFunction<U, T> serializerFunction) {
		return serializerFunction.serialize(instance/*? neoforge {*//*, provider*//*?}*/);
	}
	public static <T extends Tag, U extends INBTSerializable<T>> T inlineNbtSerialize(
			U instance/*? neoforge {*//*,
			HolderLookup.Provider provider*//*?}*/) {
		return inlineNbtSerialize(instance, /*? neoforge {*//*provider, *//*?}*/INBTSerializable::serializeNBT);
	}
	public static <T extends Tag, U extends INBTSerializable<T>> TagInlineSerializerFunction<T,U> makeInlineNbtSerializer() {
		return (instance/*? neoforge {*//*, provider*//*?}*/) -> inlineNbtSerialize(instance/*? neoforge {*//*, provider*//*?}*/);
	}
	public static <T extends Tag, U> TagInlineSerializerFunction<T,U> makeInlineNbtSerializer(TagSerializerFunction<U,T> serializerFunction) {
		return (instance/*? neoforge {*//*, provider*//*?}*/) -> inlineNbtSerialize(instance, /*? neoforge {*//*provider, *//*?}*/ serializerFunction);
	}
	
	public static <T extends Tag, U extends INBTSerializable<T>> Function<U,T> makeInlineStubbedNbtSerializer(
			/*? neoforge {*//*HolderLookup.Provider provider*//*?}*/) {
		return (value) -> inlineNbtSerialize(value/*? neoforge {*//*, provider*//*?}*/);
	}
	public static <T extends Tag, U> Function<U,T> makeInlineStubbedNbtSerializer(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			TagSerializerFunction<U, T> serializerFunction) {
		return (value) -> inlineNbtSerialize(value, /*? neoforge {*//*provider,*//*?}*/ serializerFunction);
	}
	public static <T extends Tag, U extends INBTSerializable<T>> Function<T,U> makeInlineStubbedNbtDeserializer(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			Supplier<U> instanceSupplier) {
		return (tag) -> inlineNbtDeserialize(/*? neoforge {*//*provider,*//*?}*/ tag, instanceSupplier);
	}
	public static <T extends Tag, U> Function<T,U> makeInlineStubbedNbtDeserializer(
			/*? neoforge {*//*HolderLookup.Provider provider,*//*?}*/
			Supplier<U> instanceSupplier, 
			TagDeserializerFunction<U, T> deserializerFunction) {
		return (tag) -> inlineNbtDeserialize(/*? neoforge {*//*provider,*//*?}*/ tag, instanceSupplier, deserializerFunction);
	}
}
