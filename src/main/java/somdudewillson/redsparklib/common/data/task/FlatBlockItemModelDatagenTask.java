package somdudewillson.redsparklib.common.data.task;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;

public class FlatBlockItemModelDatagenTask<R extends Block> extends ItemModelDatagenTask<R> {

	public FlatBlockItemModelDatagenTask(RegistryObject<R> targetRegistry) {
		super(targetRegistry);
	}

	@Override
	public void buildData(ItemModelProvider itemModelProvider) {
		itemModelProvider.getBuilder(targetRegistry.getId().getPath())
        	.parent(new ModelFile.UncheckedModelFile("item/generated"))
        	.texture("layer0", itemModelProvider.modLoc("block/" + targetRegistry.getId().getPath()));
	}
	
}