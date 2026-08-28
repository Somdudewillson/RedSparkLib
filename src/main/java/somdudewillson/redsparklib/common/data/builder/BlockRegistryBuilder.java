package somdudewillson.redsparklib.common.data.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import somdudewillson.redsparklib.common.data.RedSparkLibSimpleDatagenManager;
import somdudewillson.redsparklib.common.data.task.DatagenTaskBuilder;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

public class BlockRegistryBuilder<T extends Block> 
		implements BlockRegistryBuilderBlockModelStep<T>, BlockRegistryBuilderItemStep<T>, BlockRegistryBuilderFinishedStep<T> {
	@NonNull
	private final String modid;
	@NonNull
	private final DeferredRegister<Block> blockRegistry;
	@NonNull
	private final DeferredRegister<Item> itemRegistry;
	@NonNull
	private final String registryName;
	@NonNull
	private final Supplier<T> blockSupplier;
	private final RegistryObject<Block> blockRegistryObject;
	private RegistryObject<Item> itemRegistryObject = null;
	
	private final List<SimpleDatagenTask<?,?>> datagenTasks = new ArrayList<>();
	private final List<Consumer<RegistryObject<? extends Item>>> tabRegistrars = new ArrayList<>();
	
	@RequiredArgsConstructor
	public static class BlockRegistryBuilderFactory {
		@NonNull
		private final String modid;
		@NonNull
		private final DeferredRegister<Block> blockRegistry;
		@NonNull
		private final DeferredRegister<Item> itemRegistry;
		
		public <B extends Block> BlockRegistryBuilderBlockModelStep<B> registerBlock(String registryName, Supplier<B> blockSupplier) {
			return new BlockRegistryBuilder<B>(modid, blockRegistry, itemRegistry, registryName, blockSupplier);
		}
	}
	
	protected BlockRegistryBuilder(String modid, DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry, String registryName, Supplier<T> blockSupplier) {
		this.modid = Objects.requireNonNull(modid);
		this.blockRegistry = Objects.requireNonNull(blockRegistry);
		this.itemRegistry = Objects.requireNonNull(itemRegistry);
		this.registryName = Objects.requireNonNull(registryName);
		this.blockSupplier = Objects.requireNonNull(blockSupplier);
		
		blockRegistryObject = blockRegistry.register(registryName, blockSupplier);
	}
	

	protected BlockRegistryBuilder<T> addDatagenTasks(Collection<SimpleDatagenTask<?,?>> newTasks) {
		datagenTasks.addAll(datagenTasks);
		return this;
	}
	protected BlockRegistryBuilder<T> addDatagenTask(SimpleDatagenTask<?,?> newTask) {
		return addDatagenTasks(List.of(newTask));
	}
	
	protected BlockRegistryBuilderFinishedStep<T> handleBlockDataBuildNoItem(
			DatagenTaskBuilder.BlockDatagenTaskBuilder.BlockDatagenTaskBuilderNoItem<BlockRegistryBuilderFinishedStep<T>, BlockRegistryBuilderItemStep<T>, T> builder, 
			List<SimpleDatagenTask<?,?>> newTasks) {
		addDatagenTasks(newTasks);
		return this;
	}
	protected BlockRegistryBuilderItemStep<T> handleBlockDataBuildItem(
			DatagenTaskBuilder.BlockDatagenTaskBuilder.BlockDatagenTaskBuilderWithItem<BlockRegistryBuilderFinishedStep<T>, BlockRegistryBuilderItemStep<T>, T> builder, 
			List<SimpleDatagenTask<?,?>> newTasks) {
		addDatagenTasks(newTasks);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public DatagenTaskBuilder.BlockDatagenTaskBuilder.BlockDatagenTaskBuilderNoItem<BlockRegistryBuilderFinishedStep<T>, BlockRegistryBuilderItemStep<T>, T> blockDataBuilder() {
		return DatagenTaskBuilder.BlockDatagenTaskBuilder.chained((RegistryObject<T>) blockRegistryObject, this::handleBlockDataBuildNoItem, this::handleBlockDataBuildItem);
	}

	@Override
	public BlockRegistryBuilderFinishedStep<T> simpleBlockNoItem() {
		return blockDataBuilder().build();
	}
	@Override
	public BlockRegistryBuilderItemStep<T> simpleBlockWithBlockItem() {
		return blockDataBuilder().blockItem().build();
	}
	@Override
	public BlockRegistryBuilderItemStep<T> simpleBlockWithFlatItem() {
		return blockDataBuilder().flatItem().build();
	}

	@Override
	public BlockRegistryBuilderItemStep<T> inCreativeTab(Consumer<RegistryObject<? extends Item>> tabRegistrar) {
		tabRegistrars.add(tabRegistrar);
		return this;
	}
	@Override
	public BlockRegistryBuilderFinishedStep<T> properties(Properties properties) {
		itemRegistryObject = itemRegistry.register(
				registryName, 
				()->new BlockItem(blockRegistryObject.get(), new Item.Properties()));
		tabRegistrars.forEach(r->r.accept(itemRegistryObject));
		return this;
	}
	
	public RegistryObject<Block> build() {
		RedSparkLibSimpleDatagenManager.queueDatagen(modid, datagenTasks);
		return blockRegistryObject;
	}

}
