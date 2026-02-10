package somdudewillson.redsparklib.common.utils;

import java.util.Optional;

import com.mojang.serialization.Codec;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.StatType;
import net.minecraft.util.valueproviders.FloatProviderType;
import net.minecraft.util.valueproviders.IntProviderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.animal.FrogVariant;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSourceType;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifierType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.HolderSetType;

public class RegistryNbtUtils {
	// ---------- Serialization ----------
    // Game objects
	public static CompoundTag serializeRegistryValue(Block registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.BLOCKS);
	}
	public static CompoundTag serializeRegistryValue(Fluid registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.FLUIDS);
	}
	public static CompoundTag serializeRegistryValue(Item registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.ITEMS);
	}
	public static CompoundTag serializeRegistryValue(MobEffect registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.MOB_EFFECTS);
	}
	public static CompoundTag serializeRegistryValue(SoundEvent registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.SOUND_EVENTS);
	}
	public static CompoundTag serializeRegistryValue(Potion registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.POTIONS);
	}
	public static CompoundTag serializeRegistryValue(Enchantment registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.ENCHANTMENTS);
	}
	public static CompoundTag serializeRegistryValue(EntityType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.ENTITY_TYPES);
	}
	public static CompoundTag serializeRegistryValue(BlockEntityType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.BLOCK_ENTITY_TYPES);
	}
	public static CompoundTag serializeRegistryValue(ParticleType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.PARTICLE_TYPES);
	}
	public static CompoundTag serializeRegistryValue(MenuType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.MENU_TYPES);
	}
	public static CompoundTag serializeRegistryValue(PaintingVariant registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.PAINTING_VARIANTS);
	}
	public static CompoundTag serializeRegistryValue(RecipeType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.RECIPE_TYPES);
	}
	public static CompoundTag serializeRegistryValue(RecipeSerializer<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.RECIPE_SERIALIZERS);
	}
	public static CompoundTag serializeRegistryValue(Attribute registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.ATTRIBUTES);
	}
	public static CompoundTag serializeRegistryValue(StatType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.STAT_TYPES);
	}
	public static CompoundTag serializeRegistryValue(ArgumentTypeInfo<?, ?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.COMMAND_ARGUMENT_TYPES);
	}

    // Villages
	public static CompoundTag serializeRegistryValue(VillagerProfession registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.VILLAGER_PROFESSIONS);
	}
	public static CompoundTag serializeRegistryValue(PoiType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.POI_TYPES);
	}
	public static CompoundTag serializeRegistryValue(MemoryModuleType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.MEMORY_MODULE_TYPES);
	}
	public static CompoundTag serializeRegistryValue(SensorType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.SENSOR_TYPES);
	}
	public static CompoundTag serializeRegistryValue(Schedule registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.SCHEDULES);
	}
	public static CompoundTag serializeRegistryValue(Activity registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.ACTIVITIES);
	}

    // Worldgen
	public static CompoundTag serializeRegistryValue(WorldCarver<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.WORLD_CARVERS);
	}
	public static CompoundTag serializeRegistryValue(Feature<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.FEATURES);
	}
	public static CompoundTag serializeRegistryValue(ChunkStatus registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.CHUNK_STATUS);
	}
	public static CompoundTag serializeRegistryValue(BlockStateProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES);
	}
	public static CompoundTag serializeRegistryValue(FoliagePlacerType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.FOLIAGE_PLACER_TYPES);
	}
	public static CompoundTag serializeRegistryValue(TreeDecoratorType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.TREE_DECORATOR_TYPES);
	}

    // Dynamic/Data driven.
	public static CompoundTag serializeRegistryValue(Biome registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.BIOMES);
	}
	
    // Custom forge registries
	public static CompoundTag serializeRegistryValue(EntityDataSerializer<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.ENTITY_DATA_SERIALIZERS.get());
	}
	public static CompoundTag serializeGlobalLootModifier(Codec<? extends IGlobalLootModifier> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS.get());
	}
	public static CompoundTag serializeBiomeModifier(Codec<? extends BiomeModifier> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.BIOME_MODIFIER_SERIALIZERS.get());
	}
	public static CompoundTag serializeStructureModifier(Codec<? extends StructureModifier> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.STRUCTURE_MODIFIER_SERIALIZERS.get());
	}
	public static CompoundTag serializeRegistryValue(FluidType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.FLUID_TYPES.get());
	}
	public static CompoundTag serializeRegistryValue(HolderSetType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.HOLDER_SET_TYPES.get());
	}
	public static CompoundTag serializeRegistryValue(ItemDisplayContext registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, ForgeRegistries.DISPLAY_CONTEXTS.get());
	}
	
	// Non-Forge Registries
	public static CompoundTag serializeRegistryValue(GameEvent registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.GAME_EVENT);
	}
	public static CompoundTag serializeCustomStat(ResourceLocation registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.CUSTOM_STAT);
	}
	public static CompoundTag serializeRegistryValue(RuleTestType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.RULE_TEST);
	}
	public static CompoundTag serializeRegistryValue(RuleBlockEntityModifierType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER);
	}
	public static CompoundTag serializeRegistryValue(PosRuleTestType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.POS_RULE_TEST);
	}
	public static CompoundTag serializeRegistryValue(PositionSourceType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.POSITION_SOURCE_TYPE);
	}
	public static CompoundTag serializeRegistryValue(VillagerType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.VILLAGER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootPoolEntryType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.LOOT_POOL_ENTRY_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootItemFunctionType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.LOOT_FUNCTION_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootItemConditionType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.LOOT_CONDITION_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootNumberProviderType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootNbtProviderType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootScoreProviderType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(FloatProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.FLOAT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(IntProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.INT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(HeightProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.HEIGHT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(BlockPredicateType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.BLOCK_PREDICATE_TYPE);
	}
	public static CompoundTag serializeRegistryValue(StructurePlacementType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.STRUCTURE_PLACEMENT);
	}
	public static CompoundTag serializeRegistryValue(StructurePieceType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.STRUCTURE_PIECE);
	}
	public static CompoundTag serializeRegistryValue(StructureType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.STRUCTURE_TYPE);
	}
	public static CompoundTag serializeRegistryValue(PlacementModifierType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.PLACEMENT_MODIFIER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(TrunkPlacerType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.TRUNK_PLACER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(RootPlacerType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.ROOT_PLACER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(FeatureSizeType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.FEATURE_SIZE_TYPE);
	}
	public static CompoundTag serializeBiomeSource(Codec<? extends BiomeSource> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.BIOME_SOURCE);
	}
	public static CompoundTag serializeChunkGenerator(Codec<? extends ChunkGenerator> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.CHUNK_GENERATOR);
	}
	public static CompoundTag serializeMaterialCondition(Codec<? extends SurfaceRules.ConditionSource> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.MATERIAL_CONDITION);
	}
	public static CompoundTag serializeMaterialRule(Codec<? extends SurfaceRules.RuleSource> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.MATERIAL_RULE);
	}
	public static CompoundTag serializeDensityFunctionType(Codec<? extends DensityFunction> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.DENSITY_FUNCTION_TYPE);
	}
	public static CompoundTag serializeRegistryValue(StructureProcessorType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.STRUCTURE_PROCESSOR);
	}
	public static CompoundTag serializeRegistryValue(StructurePoolElementType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.STRUCTURE_POOL_ELEMENT);
	}
	public static CompoundTag serializeRegistryValue(CatVariant registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.CAT_VARIANT);
	}
	public static CompoundTag serializeRegistryValue(FrogVariant registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.FROG_VARIANT);
	}
	public static CompoundTag serializeRegistryValue(BannerPattern registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.BANNER_PATTERN);
	}
	public static CompoundTag serializeRegistryValue(Instrument registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.INSTRUMENT);
	}
	public static CompoundTag serializeDecoratedPotPattern(String registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.DECORATED_POT_PATTERNS);
	}
	public static CompoundTag serializeRegistryValue(CreativeModeTab registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, BuiltInRegistries.CREATIVE_MODE_TAB);
	}
	

	// ---------- Deserialization ----------
    // Game objects
	public static Optional<Block> tryDeserializeBlock(CompoundTag nbt) {
		return Optional.ofNullable( (Block) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Fluid> tryDeserializeFluid(CompoundTag nbt) {
		return Optional.ofNullable( (Fluid) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Item> tryDeserializeItem(CompoundTag nbt) {
		return Optional.ofNullable( (Item) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<MobEffect> tryDeserializeMobEffect(CompoundTag nbt) {
		return Optional.ofNullable( (MobEffect) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<SoundEvent> tryDeserializeSoundEvent(CompoundTag nbt) {
		return Optional.ofNullable( (SoundEvent) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Potion> tryDeserializePotion(CompoundTag nbt) {
		return Optional.ofNullable( (Potion) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Enchantment> tryDeserializeEnchantment(CompoundTag nbt) {
		return Optional.ofNullable( (Enchantment) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<EntityType<?>> tryDeserializeEntityType(CompoundTag nbt) {
		return Optional.ofNullable( (EntityType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<BlockEntityType<?>> tryDeserializeBlockEntityType(CompoundTag nbt) {
		return Optional.ofNullable( (BlockEntityType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<ParticleType<?>> tryDeserializeParticleType(CompoundTag nbt) {
		return Optional.ofNullable( (ParticleType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<MenuType<?>> tryDeserializeMenuType(CompoundTag nbt) {
		return Optional.ofNullable( (MenuType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<PaintingVariant> tryDeserializePaintingVariant(CompoundTag nbt) {
		return Optional.ofNullable( (PaintingVariant) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<RecipeType<?>> tryDeserializeRecipeType(CompoundTag nbt) {
		return Optional.ofNullable( (RecipeType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<RecipeSerializer<?>> tryDeserializeRecipeSerializer(CompoundTag nbt) {
		return Optional.ofNullable( (RecipeSerializer<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Attribute> tryDeserializeAttribute(CompoundTag nbt) {
		return Optional.ofNullable( (Attribute) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<StatType<?>> tryDeserializeStatType(CompoundTag nbt) {
		return Optional.ofNullable( (StatType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<ArgumentTypeInfo<?, ?>> tryDeserializeArgumentTypeInfo(CompoundTag nbt) {
		return Optional.ofNullable( (ArgumentTypeInfo<?, ?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}

    // Villages
	public static Optional<VillagerProfession> tryDeserializeVillagerProfession(CompoundTag nbt) {
		return Optional.ofNullable( (VillagerProfession) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<PoiType> tryDeserializePoiType(CompoundTag nbt) {
		return Optional.ofNullable( (PoiType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<MemoryModuleType<?>> tryDeserializeMemoryModuleType(CompoundTag nbt) {
		return Optional.ofNullable( (MemoryModuleType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<SensorType<?>> tryDeserializeSensorType(CompoundTag nbt) {
		return Optional.ofNullable( (SensorType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Schedule> tryDeserializeSchedule(CompoundTag nbt) {
		return Optional.ofNullable( (Schedule) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Activity> tryDeserializeActivity(CompoundTag nbt) {
		return Optional.ofNullable( (Activity) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}

    // Worldgen
	public static Optional<WorldCarver<?>> tryDeserializeWorldCarver(CompoundTag nbt) {
		return Optional.ofNullable( (WorldCarver<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Feature<?>> tryDeserializeFeature(CompoundTag nbt) {
		return Optional.ofNullable( (Feature<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<ChunkStatus> tryDeserializeChunkStatus(CompoundTag nbt) {
		return Optional.ofNullable( (ChunkStatus) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<BlockStateProviderType<?>> tryDeserializeBlockStateProviderType(CompoundTag nbt) {
		return Optional.ofNullable( (BlockStateProviderType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<FoliagePlacerType<?>> tryDeserializeFoliagePlacerType(CompoundTag nbt) {
		return Optional.ofNullable( (FoliagePlacerType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<TreeDecoratorType<?>> tryDeserializeTreeDecoratorType(CompoundTag nbt) {
		return Optional.ofNullable( (TreeDecoratorType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}

    // Dynamic/Data driven.
	public static Optional<Biome> tryDeserializeBiome(CompoundTag nbt) {
		return Optional.ofNullable( (Biome) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	
    // Custom forge registries
	public static Optional<EntityDataSerializer<?>> tryDeserializeEntityDataSerializer(CompoundTag nbt) {
		return Optional.ofNullable( (EntityDataSerializer<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends IGlobalLootModifier> Optional<Codec<T>> tryDeserializeGlobalLootModifier(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends BiomeModifier> Optional<Codec<T>> tryDeserializeBiomeModifier(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends StructureModifier> Optional<Codec<T>> tryDeserializeStructureModifier(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<FluidType> tryDeserializeFluidType(CompoundTag nbt) {
		return Optional.ofNullable( (FluidType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<HolderSetType> tryDeserializeHolderSetType(CompoundTag nbt) {
		return Optional.ofNullable( (HolderSetType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<ItemDisplayContext> tryDeserializeItemDisplayContext(CompoundTag nbt) {
		return Optional.ofNullable( (ItemDisplayContext) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	
	// Non-Forge Registries
	public static Optional<GameEvent> tryDeserializeGameEvent(CompoundTag nbt) {
		return Optional.ofNullable( (GameEvent) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<ResourceLocation> tryDeserializeCustomStat(CompoundTag nbt) {
		return Optional.ofNullable( (ResourceLocation) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<RuleTestType<?>> tryDeserializeRuleTestType(CompoundTag nbt) {
		return Optional.ofNullable( (RuleTestType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<RuleBlockEntityModifierType<?>> tryDeserializeRuleBlockEntityModifierType(CompoundTag nbt) {
		return Optional.ofNullable( (RuleBlockEntityModifierType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<PosRuleTestType<?>> tryDeserializePosRuleTestType(CompoundTag nbt) {
		return Optional.ofNullable( (PosRuleTestType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<PositionSourceType<?>> tryDeserializePositionSourceType(CompoundTag nbt) {
		return Optional.ofNullable( (PositionSourceType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<VillagerType> tryDeserializeVillagerType(CompoundTag nbt) {
		return Optional.ofNullable( (VillagerType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<LootPoolEntryType> tryDeserializeLootPoolEntryType(CompoundTag nbt) {
		return Optional.ofNullable( (LootPoolEntryType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<LootItemFunctionType> tryDeserializeLootItemFunctionType(CompoundTag nbt) {
		return Optional.ofNullable( (LootItemFunctionType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<LootItemConditionType> tryDeserializeLootItemConditionType(CompoundTag nbt) {
		return Optional.ofNullable( (LootItemConditionType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<LootNumberProviderType> tryDeserializeLootNumberProviderType(CompoundTag nbt) {
		return Optional.ofNullable( (LootNumberProviderType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<LootNbtProviderType> tryDeserializeLootNbtProviderType(CompoundTag nbt) {
		return Optional.ofNullable( (LootNbtProviderType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<LootScoreProviderType> tryDeserializeLootScoreProviderType(CompoundTag nbt) {
		return Optional.ofNullable( (LootScoreProviderType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<FloatProviderType<?>> tryDeserializeFloatProviderType(CompoundTag nbt) {
		return Optional.ofNullable( (FloatProviderType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<IntProviderType<?>> tryDeserializeIntProviderType(CompoundTag nbt) {
		return Optional.ofNullable( (IntProviderType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<HeightProviderType<?>> tryDeserializeHeightProviderType(CompoundTag nbt) {
		return Optional.ofNullable( (HeightProviderType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<BlockPredicateType<?>> tryDeserializeBlockPredicateType(CompoundTag nbt) {
		return Optional.ofNullable( (BlockPredicateType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<StructurePlacementType<?>> tryDeserializeStructurePlacementType(CompoundTag nbt) {
		return Optional.ofNullable( (StructurePlacementType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<StructurePieceType> tryDeserializeStructurePieceType(CompoundTag nbt) {
		return Optional.ofNullable( (StructurePieceType) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<StructureType<?>> tryDeserializeStructureType(CompoundTag nbt) {
		return Optional.ofNullable( (StructureType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<PlacementModifierType<?>> tryDeserializePlacementModifierType(CompoundTag nbt) {
		return Optional.ofNullable( (PlacementModifierType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<TrunkPlacerType<?>> tryDeserializeTrunkPlacerType(CompoundTag nbt) {
		return Optional.ofNullable( (TrunkPlacerType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<RootPlacerType<?>> tryDeserializeRootPlacerType(CompoundTag nbt) {
		return Optional.ofNullable( (RootPlacerType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<FeatureSizeType<?>> tryDeserializeFeatureSizeType(CompoundTag nbt) {
		return Optional.ofNullable( (FeatureSizeType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends BiomeSource> Optional<Codec<T>> tryDeserializeBiomeSource(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends ChunkGenerator> Optional<Codec<T>> tryDeserializeChunkGenerator(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends SurfaceRules.ConditionSource> Optional<Codec<T>> tryDeserializeMaterialCondition(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends SurfaceRules.RuleSource> Optional<Codec<T>> tryDeserializeMaterialRule(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	@SuppressWarnings("unchecked")
	public static <T extends DensityFunction> Optional<Codec<T>> tryDeserializeDensityFunctionType(CompoundTag nbt) {
		return Optional.ofNullable( (Codec<T>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<StructureProcessorType<?>> tryDeserializeStructureProcessorType(CompoundTag nbt) {
		return Optional.ofNullable( (StructureProcessorType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<StructurePoolElementType<?>> tryDeserializeStructurePoolElementType(CompoundTag nbt) {
		return Optional.ofNullable( (StructurePoolElementType<?>) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<CatVariant> tryDeserializeCatVariant(CompoundTag nbt) {
		return Optional.ofNullable( (CatVariant) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<FrogVariant> tryDeserializeFrogVariant(CompoundTag nbt) {
		return Optional.ofNullable( (FrogVariant) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<BannerPattern> tryDeserializeBannerPattern(CompoundTag nbt) {
		return Optional.ofNullable( (BannerPattern) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<Instrument> tryDeserializeInstrument(CompoundTag nbt) {
		return Optional.ofNullable( (Instrument) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<String> tryDeserializeDecoratedPotPattern(CompoundTag nbt) {
		return Optional.ofNullable( (String) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
	public static Optional<CreativeModeTab> tryDeserializeCreativeModeTab(CompoundTag nbt) {
		return Optional.ofNullable( (CreativeModeTab) ExtNbtUtils.deserializeRegistryObject(nbt).orElse(null) );
	}
}
