package somdudewillson.redsparklib.common.data.task;

import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.RegistryObject;

public class BasicBlockItemModelDatagenTask<R extends Block> extends ItemModelDatagenTask<R> {

	public BasicBlockItemModelDatagenTask(RegistryObject<R> targetRegistry) {
		super(targetRegistry);
	}

	@Override
	public void buildData(ItemModelProvider itemModelProvider) {
		itemModelProvider.withExistingParent(targetRegistry.getId().getPath(), ModelLocationUtils.getModelLocation(targetRegistry.get()));
	}
	
}