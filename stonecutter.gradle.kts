plugins {
	alias(libs.plugins.stonecutter)
	alias(libs.plugins.mod.publish.plugin)
	alias(libs.plugins.neoforged.moddev).apply(false)
	alias(libs.plugins.jsonlang.postprocess).apply(false)
	alias(libs.plugins.kotlin.jvm).apply(false)
	alias(libs.plugins.devtools.ksp).apply(false)
	alias(libs.plugins.fletching.table).apply(false)
	alias(libs.plugins.legacyforge.moddev).apply(false)
	id("mod-root")
}

stonecutter active file(".sc_active_version")

stonecutter parameters {
	constants.match(current.project.substringAfterLast('-'), "neoforge", "forge")
	swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
	swaps["mod_id"] = "\"${properties.get<String>("mod.id")}\";"
	swaps["mod_name"] = "\"${properties.get<String>("mod.name")}\";"
	swaps["mod_group"] = "\"${properties.get<String>("mod.group")}\";"
	swaps["minecraft"] = "\"${current.version}\";"
	constants["release"] = properties.get<String>("mod.id") != "modtemplate"
	
	
    val isNeoForge = current.project.endsWith("neoforge")
	
    // Neoforge replacements
    replacements.string(current.parsed >= "1.21") {
        replace("import net.minecraftforge.common.util.INBTSerializable;", "import net.neoforged.neoforge.common.util.INBTSerializable;")
		replace(" serializeNBT()", " serializeNBT(net.minecraft.core.HolderLookup.Provider provider)");
		replace("public void deserializeNBT(", "public void deserializeNBT(net.minecraft.core.HolderLookup.Provider provider, ");
		
		replace("import net.minecraftforge.common.capabilities.ICapabilityProvider;", "import net.neoforged.neoforge.capabilities.ICapabilityProvider;")
		replace("import net.minecraftforge.items.IItemHandler;", "import net.neoforged.neoforge.items.IItemHandler;")
		
		replace("import net.minecraftforge.registries.DeferredRegister;", "import net.neoforged.neoforge.registries.DeferredRegister;")
		replace("import net.minecraftforge.registries.RegistryObject;", "import net.neoforged.neoforge.registries.DeferredHolder;")
		replace("RegistryObject", "DeferredHolder")
		
		replace("import net.minecraftforge.common.data.BlockTagsProvider;", "import net.neoforged.neoforge.common.data.BlockTagsProvider;")
		
		replace("import net.minecraftforge.common.loot.IGlobalLootModifier;", "import net.neoforged.neoforge.common.loot.IGlobalLootModifier;")
		replace("import net.minecraftforge.common.world.BiomeModifier;", "import net.neoforged.neoforge.common.world.BiomeModifier;")
		replace("import net.minecraftforge.common.world.StructureModifier;", "import net.neoforged.neoforge.common.world.StructureModifier;")
		replace("import net.minecraftforge.fluids.FluidType;", "import net.neoforged.neoforge.fluids.FluidType;")
		replace("import net.minecraftforge.registries.holdersets.HolderSetType;", "import net.neoforged.neoforge.registries.holdersets.HolderSetType;")
		
		replace("import net.minecraftforge.fml.util.ObfuscationReflectionHelper;", "import net.neoforged.fml.util.ObfuscationReflectionHelper;")
		
		replace("import net.minecraftforge.fml.common.Mod;", "import net.neoforged.fml.common.Mod;");
		replace("import net.minecraftforge.eventbus.api.SubscribeEvent;", "import net.neoforged.bus.api.SubscribeEvent;")
		replace("import net.minecraftforge.eventbus.api.EventPriority;", "import net.neoforged.bus.api.EventPriority;")
		replace("import net.minecraftforge.eventbus.api.IEventBus;", "import net.neoforged.bus.api.IEventBus;")
		replace("import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;", "import net.neoforged.neoforge.event.entity.living.LivingBreatheEvent;")
		replace("LivingTickEvent", "LivingBreatheEvent")
		
		replace("import net.minecraftforge.data.event.GatherDataEvent;", "import net.neoforged.neoforge.data.event.GatherDataEvent;")
		replace("import net.minecraftforge.common.data.ExistingFileHelper;", "import net.neoforged.neoforge.common.data.ExistingFileHelper;")
		replace("net.minecraftforge.client.model.generators.", "net.neoforged.neoforge.client.model.generators.")
		replace("\"m_206424_\"", "\"tag\"")

		replace("import net.minecraftforge.common.MinecraftForge;", "import net.neoforged.neoforge.common.NeoForge;")
		replace("MinecraftForge.", "NeoForge.")
    }
	// Version replacements
    replacements.string(current.parsed >= "1.21") {
		replace("import net.minecraft.world.level.chunk.ChunkStatus;", "import net.minecraft.world.level.chunk.status.ChunkStatus;")
    }
}
