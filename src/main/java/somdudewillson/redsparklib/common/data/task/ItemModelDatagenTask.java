package somdudewillson.redsparklib.common.data.task;

import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.RegistryObject;

public abstract class ItemModelDatagenTask<R> extends SimpleDatagenTask<R, ItemModelProvider> {

	public ItemModelDatagenTask(RegistryObject<R> targetRegistry) {
		super(targetRegistry);
	}

	@Override
	public DatagenTaskType getTaskType() {
		return DatagenTaskType.ITEM_MODEL;
	}

	@Override
	public Class<ItemModelProvider> getDataProviderClass() {
		return ItemModelProvider.class;
	}

}
