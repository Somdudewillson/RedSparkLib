package somdudewillson.redsparklib.common.data.task;

import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.RegistryObject;

public class BasicBlockItemModelDatagenTask<R extends Block> extends ItemModelDatagenTask<R> {
	//? neoforge {
	/*private final RegistryObject<Block, R> typedRegistry;
	*///?}

	public BasicBlockItemModelDatagenTask(RegistryObject</*? neoforge {*//*Block, *//*?}*/R> targetRegistry) {
		super(targetRegistry);
		//? neoforge {
		/*this.typedRegistry = targetRegistry;
		*///?}
	}

	@Override
	public void buildData(ItemModelProvider itemModelProvider) {
		//? neoforge {
		/*itemModelProvider.withExistingParent(typedRegistry.getId().getPath(), ModelLocationUtils.getModelLocation(typedRegistry.get()));
		*///?} forge {
		itemModelProvider.withExistingParent(targetRegistry.getId().getPath(), ModelLocationUtils.getModelLocation(targetRegistry.get()));
		//?}
	}
	
}