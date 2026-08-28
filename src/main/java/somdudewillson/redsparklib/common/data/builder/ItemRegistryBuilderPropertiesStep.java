package somdudewillson.redsparklib.common.data.builder;

import lombok.NonNull;
import net.minecraft.world.item.Item;

public interface ItemRegistryBuilderPropertiesStep<T extends Item, R> {
	public ItemRegistryBuilderModelStep<T,R> properties(@NonNull Item.Properties properties);
}
