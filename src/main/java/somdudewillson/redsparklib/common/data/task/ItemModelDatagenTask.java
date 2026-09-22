package somdudewillson.redsparklib.common.data.task;

import net.minecraftforge.client.model.generators.ItemModelProvider;
//? neoforge {
/*import java.util.function.Supplier;
*///?} forge {
import net.minecraftforge.registries.RegistryObject;
//?}

public abstract class ItemModelDatagenTask<R> extends SimpleDatagenTask<R, ItemModelProvider> {

	//? neoforge {
	/*public ItemModelDatagenTask(Supplier<R> targetRegistry) {
		super(targetRegistry);
	}
	*///?} forge {
	public ItemModelDatagenTask(RegistryObject<R> targetRegistry) {
		super(targetRegistry);
	}
	//?}

	@Override
	public DatagenTaskType getTaskType() {
		return DatagenTaskType.ITEM_MODEL;
	}

	@Override
	public Class<ItemModelProvider> getDataProviderClass() {
		return ItemModelProvider.class;
	}

}
