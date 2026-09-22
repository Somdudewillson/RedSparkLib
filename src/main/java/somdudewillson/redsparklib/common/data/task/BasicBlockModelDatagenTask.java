package somdudewillson.redsparklib.common.data.task;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.registries.RegistryObject;

public class BasicBlockModelDatagenTask<R extends Block> extends SimpleDatagenTask<R, BlockStateProvider> {

	public BasicBlockModelDatagenTask(RegistryObject</*? neoforge {*//*Block, *//*?}*/R> targetRegistry) {
		super(targetRegistry);
	}

	@Override
	public <L extends BlockStateProvider> void buildData(L dataProvider) {
		dataProvider.simpleBlock(targetRegistry.get());
	}

	@Override
	public DatagenTaskType getTaskType() {
		return DatagenTaskType.BLOCK_MODEL;
	}

	@Override
	public Class<BlockStateProvider> getDataProviderClass() {
		return BlockStateProvider.class;
	}

}
