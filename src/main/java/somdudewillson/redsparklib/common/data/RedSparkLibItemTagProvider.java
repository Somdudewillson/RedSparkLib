package somdudewillson.redsparklib.common.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class RedSparkLibItemTagProvider extends ItemTagsProvider {
    protected static final Logger LOGGER = LogUtils.getLogger();
	private final RedSparkLibSimpleDatagenManager manager;

	public RedSparkLibItemTagProvider(RedSparkLibSimpleDatagenManager manager, 
			PackOutput output, CompletableFuture<Provider> lookupProvider, 
			CompletableFuture<TagsProvider.TagLookup<Block>> pBlockTags,
			String modid, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, pBlockTags, modid, existingFileHelper);
		this.manager = manager;
	}

	@Override
	protected void addTags(Provider pProvider) {
		LOGGER.debug("Generating simple item tags for {}...", manager.getModid());
		List<SimpleDatagenTask<Object, RedSparkLibItemTagProvider>> datagenTasks = manager.getFilteredDatagenTasks(RedSparkLibItemTagProvider.class);
		for (SimpleDatagenTask<?, RedSparkLibItemTagProvider> genTask : datagenTasks) {
			genTask.buildData(this, LOGGER);
		}
		LOGGER.debug("Generated {} simple item tags for {}.", datagenTasks.size(), manager.getModid());
		manager.markDatagenRun();
	}
	
	@Override
	public @NotNull String getName() {
        return String.format("Tags for %s mod id %s via RedSparkLib", this.registryKey.location(), this.modId);
	}

}
