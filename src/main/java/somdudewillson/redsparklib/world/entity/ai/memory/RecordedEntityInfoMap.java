package somdudewillson.redsparklib.world.entity.ai.memory;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import somdudewillson.redsparklib.common.utils.ExtNbtUtils;
import somdudewillson.redsparklib.common.utils.IContextFreeNBTSerializable;
import somdudewillson.redsparklib.serialization.codecs.NbtContextFreeSerializableCodec;

public class RecordedEntityInfoMap implements IContextFreeNBTSerializable<ListTag> {
	public static final Codec<RecordedEntityInfoMap> CODEC = NbtContextFreeSerializableCodec.list(RecordedEntityInfoMap.class, RecordedEntityInfoMap::new);
	
	private final Object2ObjectOpenHashMap<UUID, RecordedEntityInfo> internalDataMap = new Object2ObjectOpenHashMap<>();

	public boolean putDataForEntity(Level level, Entity entity) {
		internalDataMap.compute(entity.getUUID(), (k,v) -> RecordedEntityInfo.updateOrCreateAndReturnIfUseful(v, entity, level.getGameTime()));
		return true;
	}
	public boolean putDataForEntity(Entity entity) {
		Level level = entity.level();
		if (level == null) { return false; }
		
		return putDataForEntity(level, entity);
	}

	public boolean removeDataForEntityUuid(UUID uuid) {
		return internalDataMap.remove(uuid) != null;
	}
	public boolean removeDataForEntity(Entity entity) {
		return removeDataForEntityUuid(entity.getUUID());
	}
	public boolean removeEntityInfo(RecordedEntityInfo entityInfo) {
		return removeDataForEntityUuid(entityInfo.getEntityId());
	}
	
	@Nullable
	public RecordedEntityInfo getDataForEntityUuid(UUID uuid) {
		return internalDataMap.get(uuid);
	}
	public Optional<RecordedEntityInfo> tryGetDataForEntityUuid(UUID uuid) { return Optional.ofNullable(getDataForEntityUuid(uuid)); }
	
	@Nullable
	public RecordedEntityInfo getDataForEntity(Entity entity) {
		return getDataForEntityUuid(entity.getUUID());
	}
	public Optional<RecordedEntityInfo> tryGetDataForEntity(Entity entity) { return Optional.ofNullable(getDataForEntity(entity)); }

	public boolean hasDataForEntityUuid(UUID uuid) {
		return internalDataMap.containsKey(uuid);
	}
	public boolean hasDataForEntity(Entity entity) {
		return hasDataForEntityUuid(entity.getUUID());
	}
	
	public Set<UUID> getAllCurrentlyStoredUuids() {
		return ImmutableSet.copyOf(internalDataMap.keySet());
	}
	public Set<RecordedEntityInfo> getAllCurrentlyStoredEntityInfo() {
		return ImmutableSet.copyOf(internalDataMap.values());
	}
	
	public RecordedEntityInfo updateAndGetDataForEntity(Level level, Entity entity) {
		putDataForEntity(level, entity);
		return getDataForEntity(entity);
	}
	@Nullable
	public RecordedEntityInfo updateAndGetDataForEntity(Entity entity) {
		putDataForEntity(entity);
		return getDataForEntity(entity);
	}
	public Optional<RecordedEntityInfo> tryUpdateAndGetDataForEntity(Entity entity) { return Optional.ofNullable(updateAndGetDataForEntity(entity)); }

	@Override
	public ListTag serializeNBT() {
		//? neoforge {
		/*return ExtNbtUtils.serializeMap(internalDataMap, NbtUtils::createUUID, ExtNbtUtils.makeInlineStubbedNbtSerializer(provider));
		*///?} forge {
		return ExtNbtUtils.serializeMap(internalDataMap, NbtUtils::createUUID, RecordedEntityInfo::serializeNBT);
		//?}
	}

	@Override
	public void deserializeNBT(ListTag nbt) {
		internalDataMap.clear();
		//? neoforge {
		/*ExtNbtUtils.deserializeIntoMap(
				nbt, internalDataMap, 
				NbtUtils::loadUUID, 
				ExtNbtUtils.makeInlineStubbedNbtDeserializer(provider, RecordedEntityInfo::new));
		*///?} forge {
		ExtNbtUtils.deserializeIntoMap(
				nbt, internalDataMap, 
				NbtUtils::loadUUID, 
				RecordedEntityInfo::deserializeNBT, 
				RecordedEntityInfo::new);
		//?}
	}
}
