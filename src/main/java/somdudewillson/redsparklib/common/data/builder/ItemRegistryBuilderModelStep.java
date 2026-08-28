package somdudewillson.redsparklib.common.data.builder;

import net.minecraft.world.item.Item;
import somdudewillson.redsparklib.common.data.task.DatagenTaskBuilder;

public interface ItemRegistryBuilderModelStep<T extends Item, R> {
	public DatagenTaskBuilder.ItemDatagenTaskBuilder<ItemRegistryBuilderFinishedStep<T,R>, T> itemModelBuilder();
	public ItemRegistryBuilderFinishedStep<T,R> simpleItem();
}
