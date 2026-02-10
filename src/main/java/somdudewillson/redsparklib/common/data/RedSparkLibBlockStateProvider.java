package somdudewillson.redsparklib.common.data;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class RedSparkLibBlockStateProvider extends BlockStateProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
	private final RedSparkLibSimpleDatagenManager manager;

	public RedSparkLibBlockStateProvider(RedSparkLibSimpleDatagenManager manager,
			PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
		super(output, modid, existingFileHelper);
		this.manager = manager;
	}

	@Override
	protected void registerStatesAndModels() {
		LOGGER.debug("Generating simple block states & models for {}...", manager.getModid());
		List<SimpleDatagenTask<Object, RedSparkLibBlockStateProvider>> datagenTasks = manager.getFilteredDatagenTasks(RedSparkLibBlockStateProvider.class);
		for (SimpleDatagenTask<?, RedSparkLibBlockStateProvider> genTask : datagenTasks) {
			genTask.buildData(this);
		}
		LOGGER.debug("Generated {} simple block states & models for {}.", datagenTasks.size(), manager.getModid());
		manager.markDatagenRun();
	}
	
	@Override
	public @NotNull String getName() {
        return "RedSparkLib Block States: " + manager.getModid();
	}
}
