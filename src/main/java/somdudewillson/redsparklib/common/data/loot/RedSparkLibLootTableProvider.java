package somdudewillson.redsparklib.common.data.loot;

import java.util.List;
import java.util.Set;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import somdudewillson.redsparklib.common.data.RedSparkLibSimpleDatagenManager;

public class RedSparkLibLootTableProvider extends LootTableProvider {
	private static final Set<ResourceLocation> REQUIRED_TABLES = Set.of();

	public RedSparkLibLootTableProvider(RedSparkLibSimpleDatagenManager manager, PackOutput packOutput) {
		super(packOutput, REQUIRED_TABLES, 
				List.of(
						new LootTableProvider.SubProviderEntry(()->new RedSparkLibBlockLootTableProvider(manager), LootContextParamSets.BLOCK)
						));
	}

}
