package somdudewillson.redsparklib.platform;

import java.util.function.Supplier;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
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
import net.minecraftforge.registries.holdersets.HolderSetType;
//? neoforge {
/*import com.mojang.serialization.MapCodec;
import com.google.common.base.Suppliers;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
*///?} forge {
import com.mojang.serialization.Codec;

import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BannerPattern;
//?}

public class PlatformRegistryInstances {
	//? neoforge {
	/*// Game objects
	public static final Registry<Block> BLOCKS = BuiltInRegistries.BLOCK;
	public static final Registry<Fluid> FLUIDS = BuiltInRegistries.FLUID;
	public static final Registry<Item> ITEMS = BuiltInRegistries.ITEM;
	public static final Registry<MobEffect> MOB_EFFECTS = BuiltInRegistries.MOB_EFFECT;
	public static final Registry<SoundEvent> SOUND_EVENTS = BuiltInRegistries.SOUND_EVENT;
	public static final Registry<Potion> POTIONS = BuiltInRegistries.POTION;
	public static final Registry<EntityType<?>> ENTITY_TYPES = BuiltInRegistries.ENTITY_TYPE;
	public static final Registry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = BuiltInRegistries.BLOCK_ENTITY_TYPE;
	public static final Registry<ParticleType<?>> PARTICLE_TYPES = BuiltInRegistries.PARTICLE_TYPE;
	public static final Registry<MenuType<?>> MENU_TYPES = BuiltInRegistries.MENU;
	public static final Registry<RecipeType<?>> RECIPE_TYPES = BuiltInRegistries.RECIPE_TYPE;
	public static final Registry<RecipeSerializer<?>> RECIPE_SERIALIZERS = BuiltInRegistries.RECIPE_SERIALIZER;
	public static final Registry<Attribute> ATTRIBUTES = BuiltInRegistries.ATTRIBUTE;
	public static final Registry<StatType<?>> STAT_TYPES = BuiltInRegistries.STAT_TYPE;
	public static final Registry<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = BuiltInRegistries.COMMAND_ARGUMENT_TYPE;

	// Villages
	public static final Registry<VillagerProfession> VILLAGER_PROFESSIONS = BuiltInRegistries.VILLAGER_PROFESSION;
	public static final Registry<PoiType> POI_TYPES = BuiltInRegistries.POINT_OF_INTEREST_TYPE;
	public static final Registry<MemoryModuleType<?>> MEMORY_MODULE_TYPES = BuiltInRegistries.MEMORY_MODULE_TYPE;
	public static final Registry<SensorType<?>> SENSOR_TYPES = BuiltInRegistries.SENSOR_TYPE;
	public static final Registry<Schedule> SCHEDULES = BuiltInRegistries.SCHEDULE;
	public static final Registry<Activity> ACTIVITIES = BuiltInRegistries.ACTIVITY;

	// Worldgen
	public static final Registry<WorldCarver<?>> WORLD_CARVERS = BuiltInRegistries.CARVER;
	public static final Registry<Feature<?>> FEATURES = BuiltInRegistries.FEATURE;
	public static final Registry<ChunkStatus> CHUNK_STATUS = BuiltInRegistries.CHUNK_STATUS;
	public static final Registry<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES = BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE;
	public static final Registry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = BuiltInRegistries.FOLIAGE_PLACER_TYPE;
	public static final Registry<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = BuiltInRegistries.TREE_DECORATOR_TYPE;

	// Dynamic/Data driven

	// Custom registries
	public static final Supplier<Registry<EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = Suppliers.ofInstance(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS);
	public static final Supplier<Registry<MapCodec<? extends IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = Suppliers.ofInstance(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS);
	public static final Supplier<Registry<MapCodec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZERS = Suppliers.ofInstance(NeoForgeRegistries.BIOME_MODIFIER_SERIALIZERS);
	public static final Supplier<Registry<MapCodec<? extends StructureModifier>>> STRUCTURE_MODIFIER_SERIALIZERS = Suppliers.ofInstance(NeoForgeRegistries.STRUCTURE_MODIFIER_SERIALIZERS);
	public static final Supplier<Registry<FluidType>> FLUID_TYPES = Suppliers.ofInstance(NeoForgeRegistries.FLUID_TYPES);
	public static final Supplier<Registry<HolderSetType>> HOLDER_SET_TYPES = Suppliers.ofInstance(NeoForgeRegistries.HOLDER_SET_TYPES);
	*///?} forge {
	// Game objects
	public static final IForgeRegistry<Block> BLOCKS = ForgeRegistries.BLOCKS;
	public static final IForgeRegistry<Fluid> FLUIDS = ForgeRegistries.FLUIDS;
	public static final IForgeRegistry<Item> ITEMS = ForgeRegistries.ITEMS;
	public static final IForgeRegistry<MobEffect> MOB_EFFECTS = ForgeRegistries.MOB_EFFECTS;
	public static final IForgeRegistry<SoundEvent> SOUND_EVENTS = ForgeRegistries.SOUND_EVENTS;
	public static final IForgeRegistry<Potion> POTIONS = ForgeRegistries.POTIONS;
	public static final IForgeRegistry<Enchantment> ENCHANTMENTS = ForgeRegistries.ENCHANTMENTS;
	public static final IForgeRegistry<EntityType<?>> ENTITY_TYPES = ForgeRegistries.ENTITY_TYPES;
	public static final IForgeRegistry<BlockEntityType<?>> BLOCK_ENTITY_TYPES = ForgeRegistries.BLOCK_ENTITY_TYPES;
	public static final IForgeRegistry<ParticleType<?>> PARTICLE_TYPES = ForgeRegistries.PARTICLE_TYPES;
	public static final IForgeRegistry<MenuType<?>> MENU_TYPES = ForgeRegistries.MENU_TYPES;
	public static final IForgeRegistry<PaintingVariant> PAINTING_VARIANTS = ForgeRegistries.PAINTING_VARIANTS;
	public static final IForgeRegistry<RecipeType<?>> RECIPE_TYPES = ForgeRegistries.RECIPE_TYPES;
	public static final IForgeRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = ForgeRegistries.RECIPE_SERIALIZERS;
	public static final IForgeRegistry<Attribute> ATTRIBUTES = ForgeRegistries.ATTRIBUTES;
	public static final IForgeRegistry<StatType<?>> STAT_TYPES = ForgeRegistries.STAT_TYPES;
	public static final IForgeRegistry<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = ForgeRegistries.COMMAND_ARGUMENT_TYPES;

	// Villages
	public static final IForgeRegistry<VillagerProfession> VILLAGER_PROFESSIONS = ForgeRegistries.VILLAGER_PROFESSIONS;
	public static final IForgeRegistry<PoiType> POI_TYPES = ForgeRegistries.POI_TYPES;
	public static final IForgeRegistry<MemoryModuleType<?>> MEMORY_MODULE_TYPES = ForgeRegistries.MEMORY_MODULE_TYPES;
	public static final IForgeRegistry<SensorType<?>> SENSOR_TYPES = ForgeRegistries.SENSOR_TYPES;
	public static final IForgeRegistry<Schedule> SCHEDULES = ForgeRegistries.SCHEDULES;
	public static final IForgeRegistry<Activity> ACTIVITIES = ForgeRegistries.ACTIVITIES;

	// Worldgen
	public static final IForgeRegistry<WorldCarver<?>> WORLD_CARVERS = ForgeRegistries.WORLD_CARVERS;
	public static final IForgeRegistry<Feature<?>> FEATURES = ForgeRegistries.FEATURES;
	public static final IForgeRegistry<ChunkStatus> CHUNK_STATUS = ForgeRegistries.CHUNK_STATUS;
	public static final IForgeRegistry<BlockStateProviderType<?>> BLOCK_STATE_PROVIDER_TYPES = ForgeRegistries.BLOCK_STATE_PROVIDER_TYPES;
	public static final IForgeRegistry<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = ForgeRegistries.FOLIAGE_PLACER_TYPES;
	public static final IForgeRegistry<TreeDecoratorType<?>> TREE_DECORATOR_TYPES = ForgeRegistries.TREE_DECORATOR_TYPES;

	// Dynamic/Data driven
	public static final IForgeRegistry<Biome> BIOMES = ForgeRegistries.BIOMES;

	// Custom registries
	public static final Supplier<IForgeRegistry<EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = ForgeRegistries.ENTITY_DATA_SERIALIZERS::get;
	public static final Supplier<IForgeRegistry<Codec<? extends IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = ForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS::get;
	public static final Supplier<IForgeRegistry<Codec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZERS = ForgeRegistries.BIOME_MODIFIER_SERIALIZERS::get;
	public static final Supplier<IForgeRegistry<Codec<? extends StructureModifier>>> STRUCTURE_MODIFIER_SERIALIZERS = ForgeRegistries.STRUCTURE_MODIFIER_SERIALIZERS::get;
	public static final Supplier<IForgeRegistry<FluidType>> FLUID_TYPES = ForgeRegistries.FLUID_TYPES::get;
	public static final Supplier<IForgeRegistry<HolderSetType>> HOLDER_SET_TYPES = ForgeRegistries.HOLDER_SET_TYPES::get;
	public static final Supplier<IForgeRegistry<ItemDisplayContext>> DISPLAY_CONTEXTS = ForgeRegistries.DISPLAY_CONTEXTS::get;
	//?}

	// Non-Forge Registries (BuiltInRegistries)
	//? neoforge {
	/*public static final Registry<GameEvent> GAME_EVENT = BuiltInRegistries.GAME_EVENT;
	public static final Registry<ResourceLocation> CUSTOM_STAT = BuiltInRegistries.CUSTOM_STAT;
	public static final Registry<RuleTestType<?>> RULE_TEST = BuiltInRegistries.RULE_TEST;
	public static final Registry<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER;
	public static final Registry<PosRuleTestType<?>> POS_RULE_TEST = BuiltInRegistries.POS_RULE_TEST;
	public static final Registry<PositionSourceType<?>> POSITION_SOURCE_TYPE = BuiltInRegistries.POSITION_SOURCE_TYPE;
	public static final Registry<VillagerType> VILLAGER_TYPE = BuiltInRegistries.VILLAGER_TYPE;
	public static final Registry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE;
	public static final Registry<LootItemFunctionType<?>> LOOT_FUNCTION_TYPE = BuiltInRegistries.LOOT_FUNCTION_TYPE;
	public static final Registry<LootItemConditionType> LOOT_CONDITION_TYPE = BuiltInRegistries.LOOT_CONDITION_TYPE;
	public static final Registry<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE;
	public static final Registry<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE;
	public static final Registry<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE;
	public static final Registry<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = BuiltInRegistries.FLOAT_PROVIDER_TYPE;
	public static final Registry<IntProviderType<?>> INT_PROVIDER_TYPE = BuiltInRegistries.INT_PROVIDER_TYPE;
	public static final Registry<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = BuiltInRegistries.HEIGHT_PROVIDER_TYPE;
	public static final Registry<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = BuiltInRegistries.BLOCK_PREDICATE_TYPE;
	public static final Registry<StructurePlacementType<?>> STRUCTURE_PLACEMENT = BuiltInRegistries.STRUCTURE_PLACEMENT;
	public static final Registry<StructurePieceType> STRUCTURE_PIECE = BuiltInRegistries.STRUCTURE_PIECE;
	public static final Registry<StructureType<?>> STRUCTURE_TYPE = BuiltInRegistries.STRUCTURE_TYPE;
	public static final Registry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = BuiltInRegistries.PLACEMENT_MODIFIER_TYPE;
	public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = BuiltInRegistries.TRUNK_PLACER_TYPE;
	public static final Registry<RootPlacerType<?>> ROOT_PLACER_TYPE = BuiltInRegistries.ROOT_PLACER_TYPE;
	public static final Registry<FeatureSizeType<?>> FEATURE_SIZE_TYPE = BuiltInRegistries.FEATURE_SIZE_TYPE;
	public static final Registry<MapCodec<? extends BiomeSource>> BIOME_SOURCE = BuiltInRegistries.BIOME_SOURCE;
	public static final Registry<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATOR = BuiltInRegistries.CHUNK_GENERATOR;
	public static final Registry<MapCodec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = BuiltInRegistries.MATERIAL_CONDITION;
	public static final Registry<MapCodec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = BuiltInRegistries.MATERIAL_RULE;
	public static final Registry<MapCodec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = BuiltInRegistries.DENSITY_FUNCTION_TYPE;
	public static final Registry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = BuiltInRegistries.STRUCTURE_PROCESSOR;
	public static final Registry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = BuiltInRegistries.STRUCTURE_POOL_ELEMENT;
	public static final Registry<CatVariant> CAT_VARIANT = BuiltInRegistries.CAT_VARIANT;
	public static final Registry<FrogVariant> FROG_VARIANT = BuiltInRegistries.FROG_VARIANT;
	public static final Registry<Instrument> INSTRUMENT = BuiltInRegistries.INSTRUMENT;
	public static final Registry<DecoratedPotPattern> DECORATED_POT_PATTERNS = BuiltInRegistries.DECORATED_POT_PATTERN;
	public static final Registry<CreativeModeTab> CREATIVE_MODE_TAB = BuiltInRegistries.CREATIVE_MODE_TAB;
	*///?} forge {
	public static final Registry<GameEvent> GAME_EVENT = BuiltInRegistries.GAME_EVENT;
	public static final Registry<ResourceLocation> CUSTOM_STAT = BuiltInRegistries.CUSTOM_STAT;
	public static final Registry<RuleTestType<?>> RULE_TEST = BuiltInRegistries.RULE_TEST;
	public static final Registry<RuleBlockEntityModifierType<?>> RULE_BLOCK_ENTITY_MODIFIER = BuiltInRegistries.RULE_BLOCK_ENTITY_MODIFIER;
	public static final Registry<PosRuleTestType<?>> POS_RULE_TEST = BuiltInRegistries.POS_RULE_TEST;
	public static final Registry<PositionSourceType<?>> POSITION_SOURCE_TYPE = BuiltInRegistries.POSITION_SOURCE_TYPE;
	public static final Registry<VillagerType> VILLAGER_TYPE = BuiltInRegistries.VILLAGER_TYPE;
	public static final Registry<LootPoolEntryType> LOOT_POOL_ENTRY_TYPE = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE;
	public static final Registry<LootItemFunctionType> LOOT_FUNCTION_TYPE = BuiltInRegistries.LOOT_FUNCTION_TYPE;
	public static final Registry<LootItemConditionType> LOOT_CONDITION_TYPE = BuiltInRegistries.LOOT_CONDITION_TYPE;
	public static final Registry<LootNumberProviderType> LOOT_NUMBER_PROVIDER_TYPE = BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE;
	public static final Registry<LootNbtProviderType> LOOT_NBT_PROVIDER_TYPE = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE;
	public static final Registry<LootScoreProviderType> LOOT_SCORE_PROVIDER_TYPE = BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE;
	public static final Registry<FloatProviderType<?>> FLOAT_PROVIDER_TYPE = BuiltInRegistries.FLOAT_PROVIDER_TYPE;
	public static final Registry<IntProviderType<?>> INT_PROVIDER_TYPE = BuiltInRegistries.INT_PROVIDER_TYPE;
	public static final Registry<HeightProviderType<?>> HEIGHT_PROVIDER_TYPE = BuiltInRegistries.HEIGHT_PROVIDER_TYPE;
	public static final Registry<BlockPredicateType<?>> BLOCK_PREDICATE_TYPE = BuiltInRegistries.BLOCK_PREDICATE_TYPE;
	public static final Registry<StructurePlacementType<?>> STRUCTURE_PLACEMENT = BuiltInRegistries.STRUCTURE_PLACEMENT;
	public static final Registry<StructurePieceType> STRUCTURE_PIECE = BuiltInRegistries.STRUCTURE_PIECE;
	public static final Registry<StructureType<?>> STRUCTURE_TYPE = BuiltInRegistries.STRUCTURE_TYPE;
	public static final Registry<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPE = BuiltInRegistries.PLACEMENT_MODIFIER_TYPE;
	public static final Registry<TrunkPlacerType<?>> TRUNK_PLACER_TYPE = BuiltInRegistries.TRUNK_PLACER_TYPE;
	public static final Registry<RootPlacerType<?>> ROOT_PLACER_TYPE = BuiltInRegistries.ROOT_PLACER_TYPE;
	public static final Registry<FeatureSizeType<?>> FEATURE_SIZE_TYPE = BuiltInRegistries.FEATURE_SIZE_TYPE;
	public static final Registry<Codec<? extends BiomeSource>> BIOME_SOURCE = BuiltInRegistries.BIOME_SOURCE;
	public static final Registry<Codec<? extends ChunkGenerator>> CHUNK_GENERATOR = BuiltInRegistries.CHUNK_GENERATOR;
	public static final Registry<Codec<? extends SurfaceRules.ConditionSource>> MATERIAL_CONDITION = BuiltInRegistries.MATERIAL_CONDITION;
	public static final Registry<Codec<? extends SurfaceRules.RuleSource>> MATERIAL_RULE = BuiltInRegistries.MATERIAL_RULE;
	public static final Registry<Codec<? extends DensityFunction>> DENSITY_FUNCTION_TYPE = BuiltInRegistries.DENSITY_FUNCTION_TYPE;
	public static final Registry<StructureProcessorType<?>> STRUCTURE_PROCESSOR = BuiltInRegistries.STRUCTURE_PROCESSOR;
	public static final Registry<StructurePoolElementType<?>> STRUCTURE_POOL_ELEMENT = BuiltInRegistries.STRUCTURE_POOL_ELEMENT;
	public static final Registry<CatVariant> CAT_VARIANT = BuiltInRegistries.CAT_VARIANT;
	public static final Registry<FrogVariant> FROG_VARIANT = BuiltInRegistries.FROG_VARIANT;
	public static final Registry<BannerPattern> BANNER_PATTERN = BuiltInRegistries.BANNER_PATTERN;
	public static final Registry<Instrument> INSTRUMENT = BuiltInRegistries.INSTRUMENT;
	public static final Registry<String> DECORATED_POT_PATTERNS = BuiltInRegistries.DECORATED_POT_PATTERNS;
	public static final Registry<CreativeModeTab> CREATIVE_MODE_TAB = BuiltInRegistries.CREATIVE_MODE_TAB;
	//?}
}
