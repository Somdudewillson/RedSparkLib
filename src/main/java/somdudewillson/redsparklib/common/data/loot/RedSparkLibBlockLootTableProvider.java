package somdudewillson.redsparklib.common.data.loot;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import somdudewillson.redsparklib.common.data.RedSparkLibSimpleDatagenManager;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class RedSparkLibBlockLootTableProvider extends BlockLootSubProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
	private final RedSparkLibSimpleDatagenManager manager;

	protected RedSparkLibBlockLootTableProvider(RedSparkLibSimpleDatagenManager manager) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
		this.manager = manager;
	}

	@Override
	protected void generate() {
		LOGGER.debug("Generating simple block loot tables for {}...", manager.getModid());
		List<SimpleDatagenTask<Object, RedSparkLibBlockLootTableProvider>> datagenTasks = manager.getFilteredDatagenTasks(RedSparkLibBlockLootTableProvider.class);
		for (SimpleDatagenTask<?, RedSparkLibBlockLootTableProvider> genTask : datagenTasks) {
			genTask.buildData(this);
		}
		LOGGER.debug("Generated {} simple block loot tables for {}.", datagenTasks.size(), manager.getModid());
		manager.markDatagenRun();
	}

}
