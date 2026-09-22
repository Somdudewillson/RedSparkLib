package somdudewillson.redsparklib.common.utils;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import com.mojang.serialization.Codec;

import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.holdersets.HolderSetType;
import somdudewillson.redsparklib.platform.PlatformRegistryInstances;
//? neoforge {
/*import net.minecraft.core.registries.Registries;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
*///?}

public class RegistryNbtUtils {
	// ---------- Serialization ----------
    // Game objects
	public static CompoundTag serializeRegistryValue(Block registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BLOCKS);
	}
	public static CompoundTag serializeRegistryValue(Fluid registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.FLUIDS);
	}
	public static CompoundTag serializeRegistryValue(Item registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.ITEMS);
	}
	public static CompoundTag serializeRegistryValue(MobEffect registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.MOB_EFFECTS);
	}
	public static CompoundTag serializeRegistryValue(SoundEvent registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.SOUND_EVENTS);
	}
	public static CompoundTag serializeRegistryValue(Potion registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.POTIONS);
	}
	public static Optional<Tag> serializeRegistryValue(Enchantment registeredValue) {
		//? neoforge {
		/*return ExtNbtUtils.trySerializeVanillaRegistryValue(registeredValue, Registries.ENCHANTMENT);
		*///?} forge {
		return Optional.of(ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.ENCHANTMENTS));
		//?}
	}
	public static CompoundTag serializeRegistryValue(EntityType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.ENTITY_TYPES);
	}
	public static CompoundTag serializeRegistryValue(BlockEntityType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BLOCK_ENTITY_TYPES);
	}
	public static CompoundTag serializeRegistryValue(ParticleType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.PARTICLE_TYPES);
	}
	public static CompoundTag serializeRegistryValue(MenuType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.MENU_TYPES);
	}
	public static Optional<Tag> serializeRegistryValue(PaintingVariant registeredValue) {
		//? neoforge {
		/*return ExtNbtUtils.trySerializeVanillaRegistryValue(registeredValue, Registries.PAINTING_VARIANT);
		*///?} forge {
		return Optional.of(ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.PAINTING_VARIANTS));
		//?}
	}
	public static CompoundTag serializeRegistryValue(RecipeType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.RECIPE_TYPES);
	}
	public static CompoundTag serializeRegistryValue(RecipeSerializer<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.RECIPE_SERIALIZERS);
	}
	public static CompoundTag serializeRegistryValue(Attribute registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.ATTRIBUTES);
	}
	public static CompoundTag serializeRegistryValue(StatType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.STAT_TYPES);
	}
	public static CompoundTag serializeRegistryValue(ArgumentTypeInfo<?, ?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.COMMAND_ARGUMENT_TYPES);
	}

    // Villages
	public static CompoundTag serializeRegistryValue(VillagerProfession registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.VILLAGER_PROFESSIONS);
	}
	public static CompoundTag serializeRegistryValue(PoiType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.POI_TYPES);
	}
	public static CompoundTag serializeRegistryValue(MemoryModuleType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.MEMORY_MODULE_TYPES);
	}
	public static CompoundTag serializeRegistryValue(SensorType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.SENSOR_TYPES);
	}
	public static CompoundTag serializeRegistryValue(Schedule registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.SCHEDULES);
	}
	public static CompoundTag serializeRegistryValue(Activity registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.ACTIVITIES);
	}

    // Worldgen
	public static CompoundTag serializeRegistryValue(WorldCarver<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.WORLD_CARVERS);
	}
	public static CompoundTag serializeRegistryValue(Feature<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.FEATURES);
	}
	public static CompoundTag serializeRegistryValue(ChunkStatus registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.CHUNK_STATUS);
	}
	public static CompoundTag serializeRegistryValue(BlockStateProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BLOCK_STATE_PROVIDER_TYPES);
	}
	public static CompoundTag serializeRegistryValue(FoliagePlacerType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.FOLIAGE_PLACER_TYPES);
	}
	public static CompoundTag serializeRegistryValue(TreeDecoratorType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.TREE_DECORATOR_TYPES);
	}

    // Dynamic/Data driven.
	public static Optional<Tag> serializeRegistryValue(Biome registeredValue) {
		//? neoforge {
		/*return ExtNbtUtils.trySerializeVanillaRegistryValue(registeredValue, Registries.BIOME);
		*///?} forge {
		return Optional.of(ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BIOMES));
		//?}
	}
	
    // Custom forge registries
	public static CompoundTag serializeRegistryValue(EntityDataSerializer<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.ENTITY_DATA_SERIALIZERS.get());
	}
	public static CompoundTag serializeGlobalLootModifier(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends IGlobalLootModifier> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.GLOBAL_LOOT_MODIFIER_SERIALIZERS.get());
	}
	public static CompoundTag serializeBiomeModifier(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends BiomeModifier> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BIOME_MODIFIER_SERIALIZERS.get());
	}
	public static CompoundTag serializeStructureModifier(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends StructureModifier> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.STRUCTURE_MODIFIER_SERIALIZERS.get());
	}
	public static CompoundTag serializeRegistryValue(FluidType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.FLUID_TYPES.get());
	}
	public static CompoundTag serializeRegistryValue(HolderSetType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.HOLDER_SET_TYPES.get());
	}
	//? forge {
	public static CompoundTag serializeRegistryValue(ItemDisplayContext registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.DISPLAY_CONTEXTS.get());
	}
	//?}
	
	// Non-Forge Registries
	public static CompoundTag serializeRegistryValue(GameEvent registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.GAME_EVENT);
	}
	public static CompoundTag serializeCustomStat(ResourceLocation registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.CUSTOM_STAT);
	}
	public static CompoundTag serializeRegistryValue(RuleTestType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.RULE_TEST);
	}
	public static CompoundTag serializeRegistryValue(RuleBlockEntityModifierType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.RULE_BLOCK_ENTITY_MODIFIER);
	}
	public static CompoundTag serializeRegistryValue(PosRuleTestType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.POS_RULE_TEST);
	}
	public static CompoundTag serializeRegistryValue(PositionSourceType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.POSITION_SOURCE_TYPE);
	}
	public static CompoundTag serializeRegistryValue(VillagerType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.VILLAGER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootPoolEntryType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.LOOT_POOL_ENTRY_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootItemFunctionType/*? neoforge {*//*<?>*//*?}*/ registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.LOOT_FUNCTION_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootItemConditionType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.LOOT_CONDITION_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootNumberProviderType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.LOOT_NUMBER_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootNbtProviderType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.LOOT_NBT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(LootScoreProviderType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.LOOT_SCORE_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(FloatProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.FLOAT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(IntProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.INT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(HeightProviderType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.HEIGHT_PROVIDER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(BlockPredicateType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BLOCK_PREDICATE_TYPE);
	}
	public static CompoundTag serializeRegistryValue(StructurePlacementType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.STRUCTURE_PLACEMENT);
	}
	public static CompoundTag serializeRegistryValue(StructurePieceType registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.STRUCTURE_PIECE);
	}
	public static CompoundTag serializeRegistryValue(StructureType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.STRUCTURE_TYPE);
	}
	public static CompoundTag serializeRegistryValue(PlacementModifierType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.PLACEMENT_MODIFIER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(TrunkPlacerType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.TRUNK_PLACER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(RootPlacerType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.ROOT_PLACER_TYPE);
	}
	public static CompoundTag serializeRegistryValue(FeatureSizeType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.FEATURE_SIZE_TYPE);
	}
	public static CompoundTag serializeBiomeSource(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends BiomeSource> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BIOME_SOURCE);
	}
	public static CompoundTag serializeChunkGenerator(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends ChunkGenerator> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.CHUNK_GENERATOR);
	}
	public static CompoundTag serializeMaterialCondition(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends SurfaceRules.ConditionSource> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.MATERIAL_CONDITION);
	}
	public static CompoundTag serializeMaterialRule(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends SurfaceRules.RuleSource> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.MATERIAL_RULE);
	}
	public static CompoundTag serializeDensityFunctionType(/*? neoforge {*//*MapCodec*//*?} forge {*/Codec/*?}*/<? extends DensityFunction> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.DENSITY_FUNCTION_TYPE);
	}
	public static CompoundTag serializeRegistryValue(StructureProcessorType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.STRUCTURE_PROCESSOR);
	}
	public static CompoundTag serializeRegistryValue(StructurePoolElementType<?> registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.STRUCTURE_POOL_ELEMENT);
	}
	public static CompoundTag serializeRegistryValue(CatVariant registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.CAT_VARIANT);
	}
	public static CompoundTag serializeRegistryValue(FrogVariant registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.FROG_VARIANT);
	}
	//? forge {
	public static CompoundTag serializeRegistryValue(BannerPattern registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.BANNER_PATTERN);
	}
	//?}
	public static CompoundTag serializeRegistryValue(Instrument registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.INSTRUMENT);
	}
	public static CompoundTag serializeDecoratedPotPattern(/*? neoforge {*//*DecoratedPotPattern*//*?} forge {*/String/*?}*/ registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.DECORATED_POT_PATTERNS);
	}
	public static CompoundTag serializeRegistryValue(CreativeModeTab registeredValue) {
		return ExtNbtUtils.serializeRegistryValue(registeredValue, PlatformRegistryInstances.CREATIVE_MODE_TAB);
	}
	

	// ---------- Deserialization ----------
    // Game objects
	public static Optional<Block> tryDeserializeBlock(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Fluid> tryDeserializeFluid(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Item> tryDeserializeItem(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<MobEffect> tryDeserializeMobEffect(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<SoundEvent> tryDeserializeSoundEvent(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Potion> tryDeserializePotion(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Enchantment> tryDeserializeEnchantment(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<EntityType<?>> tryDeserializeEntityType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<BlockEntityType<?>> tryDeserializeBlockEntityType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<ParticleType<?>> tryDeserializeParticleType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<MenuType<?>> tryDeserializeMenuType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<PaintingVariant> tryDeserializePaintingVariant(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<RecipeType<?>> tryDeserializeRecipeType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<RecipeSerializer<?>> tryDeserializeRecipeSerializer(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Attribute> tryDeserializeAttribute(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<StatType<?>> tryDeserializeStatType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<ArgumentTypeInfo<?, ?>> tryDeserializeArgumentTypeInfo(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}

    // Villages
	public static Optional<VillagerProfession> tryDeserializeVillagerProfession(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<PoiType> tryDeserializePoiType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<MemoryModuleType<?>> tryDeserializeMemoryModuleType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<SensorType<?>> tryDeserializeSensorType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Schedule> tryDeserializeSchedule(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Activity> tryDeserializeActivity(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}

    // Worldgen
	public static Optional<WorldCarver<?>> tryDeserializeWorldCarver(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Feature<?>> tryDeserializeFeature(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<ChunkStatus> tryDeserializeChunkStatus(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<BlockStateProviderType<?>> tryDeserializeBlockStateProviderType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<FoliagePlacerType<?>> tryDeserializeFoliagePlacerType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<TreeDecoratorType<?>> tryDeserializeTreeDecoratorType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}

    // Dynamic/Data driven.
	public static Optional<Biome> tryDeserializeBiome(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	
    // Custom forge registries
	public static Optional<EntityDataSerializer<?>> tryDeserializeEntityDataSerializer(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends IGlobalLootModifier> Optional<Codec<T>> tryDeserializeGlobalLootModifier(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends BiomeModifier> Optional<Codec<T>> tryDeserializeBiomeModifier(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends StructureModifier> Optional<Codec<T>> tryDeserializeStructureModifier(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<FluidType> tryDeserializeFluidType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<HolderSetType> tryDeserializeHolderSetType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<ItemDisplayContext> tryDeserializeItemDisplayContext(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	
	// Non-Forge Registries
	public static Optional<GameEvent> tryDeserializeGameEvent(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<ResourceLocation> tryDeserializeCustomStat(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<RuleTestType<?>> tryDeserializeRuleTestType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<RuleBlockEntityModifierType<?>> tryDeserializeRuleBlockEntityModifierType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<PosRuleTestType<?>> tryDeserializePosRuleTestType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<PositionSourceType<?>> tryDeserializePositionSourceType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<VillagerType> tryDeserializeVillagerType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<LootPoolEntryType> tryDeserializeLootPoolEntryType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<LootItemFunctionType/*? neoforge {*//*<?>*//*?}*/> tryDeserializeLootItemFunctionType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<LootItemConditionType> tryDeserializeLootItemConditionType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<LootNumberProviderType> tryDeserializeLootNumberProviderType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<LootNbtProviderType> tryDeserializeLootNbtProviderType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<LootScoreProviderType> tryDeserializeLootScoreProviderType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<FloatProviderType<?>> tryDeserializeFloatProviderType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<IntProviderType<?>> tryDeserializeIntProviderType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<HeightProviderType<?>> tryDeserializeHeightProviderType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<BlockPredicateType<?>> tryDeserializeBlockPredicateType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<StructurePlacementType<?>> tryDeserializeStructurePlacementType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<StructurePieceType> tryDeserializeStructurePieceType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<StructureType<?>> tryDeserializeStructureType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<PlacementModifierType<?>> tryDeserializePlacementModifierType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<TrunkPlacerType<?>> tryDeserializeTrunkPlacerType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<RootPlacerType<?>> tryDeserializeRootPlacerType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<FeatureSizeType<?>> tryDeserializeFeatureSizeType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends BiomeSource> Optional<Codec<T>> tryDeserializeBiomeSource(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends ChunkGenerator> Optional<Codec<T>> tryDeserializeChunkGenerator(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends SurfaceRules.ConditionSource> Optional<Codec<T>> tryDeserializeMaterialCondition(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends SurfaceRules.RuleSource> Optional<Codec<T>> tryDeserializeMaterialRule(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static <T extends DensityFunction> Optional<Codec<T>> tryDeserializeDensityFunctionType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<StructureProcessorType<?>> tryDeserializeStructureProcessorType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<StructurePoolElementType<?>> tryDeserializeStructurePoolElementType(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<CatVariant> tryDeserializeCatVariant(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<FrogVariant> tryDeserializeFrogVariant(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<BannerPattern> tryDeserializeBannerPattern(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<Instrument> tryDeserializeInstrument(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<String> tryDeserializeDecoratedPotPattern(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	public static Optional<CreativeModeTab> tryDeserializeCreativeModeTab(CompoundTag nbt) {
		return tryUnwrapRegistryObject(ExtNbtUtils.deserializeRegistryObject(nbt));
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	private static <T> Optional<T> tryUnwrapRegistryObject(RegistryObject</*? neoforge {*//* ?, *//*?}*/?> toUnwrap) {
		//? neoforge {
		/*return (Optional<T>) toUnwrap.asOptional();
		*///?} forge {
		return Optional.ofNullable((T) toUnwrap.orElse(null));
		//?}
	}
}
