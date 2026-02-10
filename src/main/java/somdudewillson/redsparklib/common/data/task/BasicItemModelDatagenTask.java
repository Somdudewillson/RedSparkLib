package somdudewillson.redsparklib.common.data.task;

import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.RegistryObject;

public class BasicItemModelDatagenTask<R extends Item> extends ItemModelDatagenTask<R> {

	public BasicItemModelDatagenTask(RegistryObject<R> targetRegistry) {
		super(targetRegistry);
	}

	@Override
	public void buildData(ItemModelProvider itemModelProvider) {
		itemModelProvider.basicItem(targetRegistry.get());
	}
	
}