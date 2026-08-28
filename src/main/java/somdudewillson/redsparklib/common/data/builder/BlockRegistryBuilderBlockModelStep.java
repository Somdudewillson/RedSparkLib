package somdudewillson.redsparklib.common.data.builder;

import net.minecraft.world.level.block.Block;
import somdudewillson.redsparklib.common.data.task.DatagenTaskBuilder;

public interface BlockRegistryBuilderBlockModelStep<T extends Block> {
	public DatagenTaskBuilder.BlockDatagenTaskBuilder.BlockDatagenTaskBuilderNoItem<BlockRegistryBuilderFinishedStep<T>, BlockRegistryBuilderItemStep<T>, T> blockDataBuilder();
	public BlockRegistryBuilderFinishedStep<T> simpleBlockNoItem();
	public BlockRegistryBuilderItemStep<T> simpleBlockWithBlockItem();
	public BlockRegistryBuilderItemStep<T> simpleBlockWithFlatItem();
}
