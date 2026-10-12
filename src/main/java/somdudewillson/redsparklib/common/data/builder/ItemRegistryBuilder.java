package somdudewillson.redsparklib.common.data.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import somdudewillson.redsparklib.common.data.task.DatagenTaskBuilder.ItemDatagenTaskBuilder;
import somdudewillson.redsparklib.common.data.RedSparkLibSimpleDatagenManager;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class ItemRegistryBuilder<T extends Item, R> implements ItemRegistryBuilderPropertiesStep<T,R>, ItemRegistryBuilderModelStep<T,R>, ItemRegistryBuilderFinishedStep<T, R> {
	@NonNull
	private final String modid;
	@NonNull
	private final DeferredRegister<Item> itemRegistry;
	@NonNull
	private final String registryName;
	@NonNull
	private final Function<Properties, T> itemFactory;
    private Properties properties = null;
	private RegistryObject</*? neoforge {*//*Item, *//*?}*/Item> itemRegistryObject = null;
	
    protected final BiFunction<ItemRegistryBuilder<T,R>, RegistryObject</*? neoforge {*//*Item, *//*?}*/Item>, R> finisher;
	
	private final List<SimpleDatagenTask<?,?>> datagenTasks = new ArrayList<>();
	private final List<Consumer<RegistryObject</*? neoforge {*//*Item, *//*?}*/? extends Item>>> tabRegistrars = new ArrayList<>();
	
	@RequiredArgsConstructor
	public static class ItemRegistryBuilderFactory {
		@NonNull
		private final String modid;
		@NonNull
		private final DeferredRegister<Item> itemRegistry;

		public <B extends Item> ItemRegistryBuilderPropertiesStep<B,RegistryObject</*? neoforge {*//*Item, *//*?}*/Item>> registerItem(String registryName, Function<Properties, B> itemFactory) {
			return new ItemRegistryBuilder<>(modid, itemRegistry, registryName, itemFactory, ItemRegistryBuilder::identityFinisher);
		}
		public ItemRegistryBuilderPropertiesStep<Item, RegistryObject</*? neoforge {*//*Item, *//*?}*/Item>> registerSimpleItem(String registryName) {
			return new ItemRegistryBuilder<>(modid, itemRegistry, registryName, Item::new, ItemRegistryBuilder::identityFinisher);
		}
		public <B extends Item> ItemRegistryBuilderModelStep<B,RegistryObject</*? neoforge {*//*Item, *//*?}*/Item>> registerItem(String registryName, Supplier<B> itemSupplier) {
			return new ItemRegistryBuilder<>(modid, itemRegistry, registryName, p->itemSupplier.get(), ItemRegistryBuilder::identityFinisher);
		}
	}
	
	protected static <Ti extends Item> RegistryObject</*? neoforge {*//*Item, *//*?}*/Item> identityFinisher(ItemRegistryBuilder<Ti, RegistryObject</*? neoforge {*//*Item, *//*?}*/Item>> builder, RegistryObject</*? neoforge {*//*Item, *//*?}*/Item> itemResult) {
		return itemResult;
	}
	
	protected ItemRegistryBuilder(@NonNull String modid, @NonNull DeferredRegister<Item> itemRegistry, @NonNull String registryName, 
			@NonNull Function<Properties, T> itemFactory, @NonNull BiFunction<ItemRegistryBuilder<T,R>, RegistryObject</*? neoforge {*//*Item, *//*?}*/Item>, R> finisher) {
		this.modid = modid;
		this.itemRegistry = itemRegistry;
		this.registryName = registryName;
		this.finisher = finisher;
		this.itemFactory = itemFactory;
	}
	

	protected ItemRegistryBuilder<T,R> addDatagenTasks(Collection<SimpleDatagenTask<?,?>> newTasks) {
		datagenTasks.addAll(newTasks);
		return this;
	}
	protected ItemRegistryBuilder<T,R> addDatagenTask(SimpleDatagenTask<?,?> newTask) {
		return addDatagenTasks(List.of(newTask));
	}
	
	protected ItemRegistryBuilderFinishedStep<T,R> handleItemDataBuild(
			ItemDatagenTaskBuilder<ItemRegistryBuilderFinishedStep<T, R>, T> builder, 
			List<SimpleDatagenTask<?,?>> newTasks) {
		addDatagenTasks(newTasks);
		return this;
	}

	@Override
	public ItemRegistryBuilderModelStep<T, R> properties(@NonNull Properties properties) {
		this.properties = properties;
		return this;
	}
	
	protected void performItemRegistry() {
		itemRegistryObject = itemRegistry.register(registryName, ()->itemFactory.apply(properties));
	}

	@SuppressWarnings("unchecked")
	@Override
	public ItemDatagenTaskBuilder<ItemRegistryBuilderFinishedStep<T, R>, T> itemModelBuilder() {
		performItemRegistry();
		return ItemDatagenTaskBuilder.chained((RegistryObject</*? neoforge {*//*Item, *//*?}*/T>) itemRegistryObject, this::handleItemDataBuild);
	}
	@Override
	public ItemRegistryBuilderFinishedStep<T, R> simpleItem() {
		return itemModelBuilder()
				.build();
	}


	@Override
	public ItemRegistryBuilderFinishedStep<T, R> inCreativeTab(Consumer<RegistryObject</*? neoforge {*//*Item, *//*?}*/? extends Item>> tabRegistrar) {
		tabRegistrars.add(tabRegistrar);
		return this;
	}
	@Override
	public R build() {
		tabRegistrars.forEach(r->r.accept(itemRegistryObject));
		RedSparkLibSimpleDatagenManager.queueDatagen(modid, datagenTasks);
		return finisher.apply(this, itemRegistryObject);
	}

}
