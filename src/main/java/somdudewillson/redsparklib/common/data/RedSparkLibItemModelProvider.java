package somdudewillson.redsparklib.common.data;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class RedSparkLibItemModelProvider extends ItemModelProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
	private final RedSparkLibSimpleDatagenManager manager;

	public RedSparkLibItemModelProvider(RedSparkLibSimpleDatagenManager manager,
			PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
		super(output, modid, existingFileHelper);
		this.manager = manager;
	}

	@Override
	protected void registerModels() {
		LOGGER.debug("Generating simple item models for {}...", manager.getModid());
		List<SimpleDatagenTask<Object, RedSparkLibItemModelProvider>> datagenTasks = manager.getFilteredDatagenTasks(RedSparkLibItemModelProvider.class);
		for (SimpleDatagenTask<?, RedSparkLibItemModelProvider> genTask : datagenTasks) {
			genTask.buildData(this, LOGGER);
		}
		LOGGER.debug("Generated {} simple item models for {}.", datagenTasks.size(), manager.getModid());
		manager.markDatagenRun();
	}
	
	@Override
	public @NotNull String getName() {
        return "RedSparkLib Item Models: " + manager.getModid();
	}
}
