package somdudewillson.redsparklib.common.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class RedSparkLibBlockTagProvider extends BlockTagsProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
	private final RedSparkLibSimpleDatagenManager manager;

	public RedSparkLibBlockTagProvider(RedSparkLibSimpleDatagenManager manager, 
			PackOutput output, CompletableFuture<Provider> lookupProvider,
			String modid, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, modid, existingFileHelper);
		this.manager = manager;
	}

	@Override
	protected void addTags(Provider pProvider) {
		LOGGER.debug("Generating simple block tags for {}...", manager.getModid());
		List<SimpleDatagenTask<Object, RedSparkLibBlockTagProvider>> datagenTasks = manager.getFilteredDatagenTasks(RedSparkLibBlockTagProvider.class);
		for (SimpleDatagenTask<?, RedSparkLibBlockTagProvider> genTask : datagenTasks) {
			genTask.buildData(this, LOGGER);
		}
		LOGGER.debug("Generated {} simple block tags for {}.", datagenTasks.size(), manager.getModid());
		manager.markDatagenRun();
	}
	
	@Override
	public @NotNull String getName() {
        return String.format("Tags for %s mod id %s via RedSparkLib", this.registryKey.location(), this.modId);
	}

}
