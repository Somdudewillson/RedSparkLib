package somdudewillson.redsparklib.common.data.task;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;

public class FlatBlockItemModelDatagenTask<R extends Block> extends ItemModelDatagenTask<R> {
	//? neoforge {
	/*private final RegistryObject<Block, R> typedRegistry;
	*///?}

	public FlatBlockItemModelDatagenTask(RegistryObject</*? neoforge {*//*Block, *//*?}*/R> targetRegistry) {
		super(targetRegistry);
		//? neoforge {
		/*this.typedRegistry = targetRegistry;
		*///?}
	}

	@Override
	public void buildData(ItemModelProvider itemModelProvider) {
		//? neoforge {
		/*itemModelProvider.getBuilder(typedRegistry.getId().getPath())
        	.parent(new ModelFile.UncheckedModelFile("item/generated"))
        	.texture("layer0", itemModelProvider.modLoc("block/" + typedRegistry.getId().getPath()));
		*///?} forge {
		itemModelProvider.getBuilder(targetRegistry.getId().getPath())
        	.parent(new ModelFile.UncheckedModelFile("item/generated"))
        	.texture("layer0", itemModelProvider.modLoc("block/" + targetRegistry.getId().getPath()));
		//?}
	}
	
}