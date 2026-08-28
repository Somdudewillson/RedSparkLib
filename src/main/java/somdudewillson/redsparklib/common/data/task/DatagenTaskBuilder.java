package somdudewillson.redsparklib.common.data.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import javax.annotation.Nonnull;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import somdudewillson.redsparklib.common.data.task.PropertyDependentItemModelDatagenTask.OverridePredicateData;

public abstract class DatagenTaskBuilder<T, S extends DatagenTaskBuilder<T,S>> {
    protected static final Logger LOGGER = LogUtils.getLogger();
    
    protected static <S> List<SimpleDatagenTask<?,?>> identityFinisher(S self, List<SimpleDatagenTask<?,?>> taskList) {
    	return taskList;
    }
    
    protected abstract T finish(List<SimpleDatagenTask<?,?>> taskList);
    
	@Nonnull
	public abstract T build();

	@RequiredArgsConstructor
	public static abstract class DatagenTaskBuilderSingleType<T, S extends DatagenTaskBuilderSingleType<T,S>> extends DatagenTaskBuilder<T,S> {
	    protected final BiFunction<S, List<SimpleDatagenTask<?,?>>, T> finisher;
	    
	    @SuppressWarnings("unchecked")
		protected T finish(List<SimpleDatagenTask<?,?>> taskList) {
	    	return finisher.apply((S)this, taskList);
	    }		
	}

	@Accessors(fluent = true, chain = true)
	@Getter
	@RequiredArgsConstructor
	public static abstract class BlockDatagenTaskBuilder<T, Ta, Tb, R extends Block, S extends BlockDatagenTaskBuilder<T,Ta,Tb,R,S>> extends DatagenTaskBuilder<T, S> {

		protected final RegistryObject<R> block;
		protected final BiFunction<BlockDatagenTaskBuilderNoItem<Ta, Tb, R>, List<SimpleDatagenTask<?,?>>, Ta> finisherA;
		protected final BiFunction<BlockDatagenTaskBuilderWithItem<Ta, Tb, R>, List<SimpleDatagenTask<?,?>>, Tb> finisherB;
		protected final Set<TagKey<Block>> blockTags = new HashSet<>();
		
		public static class BlockDatagenTaskBuilderNoItem<Ta, Tb, R extends Block> extends BlockDatagenTaskBuilder<Ta, Ta, Tb, R, BlockDatagenTaskBuilderNoItem<Ta,Tb,R>> {
			
			protected BlockDatagenTaskBuilderNoItem(
					RegistryObject<R> block,
					BiFunction<BlockDatagenTaskBuilderNoItem<Ta, Tb, R>, List<SimpleDatagenTask<?, ?>>, Ta> finisherA,
					BiFunction<BlockDatagenTaskBuilderWithItem<Ta, Tb, R>, List<SimpleDatagenTask<?, ?>>, Tb> finisherB) {
				super(block, finisherA, finisherB);
			}
			
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> item() {
				return blockItem();
			}
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> blockItem() {
				return new BlockDatagenTaskBuilderWithItem<Ta,Tb,R>(block, finisherA, finisherB)
						.blockTags(blockTags)
						.blockItem();
			}
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> flatItem() {
				return new BlockDatagenTaskBuilderWithItem<Ta,Tb,R>(block, finisherA, finisherB)
						.blockTags(blockTags)
						.flatItem();
			}
			
			@Override
			public BlockDatagenTaskBuilderNoItem<Ta,Tb,R> blockTag(TagKey<Block> tag) {
				blockTags.add(tag);
				return this;
			}
			@Override
			@SuppressWarnings("unchecked")
			public BlockDatagenTaskBuilderNoItem<Ta,Tb,R> blockTags(TagKey<Block>... tags) {
				return blockTags(Arrays.asList(tags));
			}
			@Override
			public BlockDatagenTaskBuilderNoItem<Ta,Tb,R> blockTags(Collection<TagKey<Block>> tags) {
				blockTags.addAll(tags);
				return this;
			}
			
			@Override
			protected Ta finish(List<SimpleDatagenTask<?, ?>> taskList) {
				return finisherA.apply(this, taskList);
			}

			@Override
			public Ta build() {			
				return finish(buildSharedTasks());
			}
		}
		
		public static class BlockDatagenTaskBuilderWithItem<Ta, Tb, R extends Block> extends BlockDatagenTaskBuilder<Tb, Ta, Tb, R, BlockDatagenTaskBuilderWithItem<Ta, Tb, R>> {
			protected static enum BlockItemType {
				BLOCK,
				FLAT;
			}
			protected BlockItemType generateItem = BlockItemType.BLOCK;
			protected final Set<TagKey<Item>> itemTags = new HashSet<>();
			
			protected BlockDatagenTaskBuilderWithItem(
					RegistryObject<R> block,
					BiFunction<BlockDatagenTaskBuilderNoItem<Ta, Tb, R>, List<SimpleDatagenTask<?, ?>>, Ta> finisherA,
					BiFunction<BlockDatagenTaskBuilderWithItem<Ta, Tb, R>, List<SimpleDatagenTask<?, ?>>, Tb> finisherB) {
				super(block, finisherA, finisherB);
			}
			
			public BlockDatagenTaskBuilderNoItem<Ta,Tb,R> noItem() {
				return new BlockDatagenTaskBuilderNoItem<Ta,Tb,R>(block, finisherA, finisherB)
						.blockTags(blockTags);
			}
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> blockItem() {
				generateItem = BlockItemType.BLOCK;
				return this;
			}
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> flatItem() {
				generateItem = BlockItemType.FLAT;
				return this;
			}
			
			@Override
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> blockTag(TagKey<Block> tag) {
				blockTags.add(tag);
				return this;
			}
			@Override
			@SuppressWarnings("unchecked")
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> blockTags(TagKey<Block>... tags) {
				return blockTags(Arrays.asList(tags));
			}
			@Override
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> blockTags(Collection<TagKey<Block>> tags) {
				blockTags.addAll(tags);
				return this;
			}

			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> itemTag(TagKey<Item> tag) {
				itemTags.add(tag);
				return this;
			}
			@SuppressWarnings("unchecked")
			public BlockDatagenTaskBuilderWithItem<Ta,Tb,R> itemTags(TagKey<Item>... tags) {
				itemTags.addAll(Arrays.asList(tags));
				return this;
			}
			
			@Override
			protected Tb finish(List<SimpleDatagenTask<?, ?>> taskList) {
				return finisherB.apply(this, taskList);
			}

			@SuppressWarnings("unchecked")
			@Override
			public Tb build() {
				List<SimpleDatagenTask<?, ?>> tasks = buildSharedTasks();
				
				switch (generateItem) {
				case BLOCK:
					tasks.add(new BasicBlockItemModelDatagenTask<>(block));
					break;
				case FLAT:
					tasks.add(new FlatBlockItemModelDatagenTask<>(block));
					break;
				default:
					break;
				}
				
				if (!itemTags.isEmpty()) {
					tasks.add(SimpleTagDatagenTask.blockItemTags((RegistryObject<Block>) block, itemTags));
				}
				
				return finish(tasks);
			}
		}
		
		public static <iR extends Block> BlockDatagenTaskBuilderNoItem<List<SimpleDatagenTask<?,?>>, List<SimpleDatagenTask<?,?>>, iR> unchained(RegistryObject<iR> block) {
			return new BlockDatagenTaskBuilderNoItem<>(block, DatagenTaskBuilder::identityFinisher, DatagenTaskBuilder::identityFinisher);
		}
		public static <iTa, iTb, iR extends Block> BlockDatagenTaskBuilderNoItem<iTa, iTb, iR> chained(RegistryObject<iR> block, 
				BiFunction<BlockDatagenTaskBuilderNoItem<iTa, iTb, iR>, List<SimpleDatagenTask<?,?>>, iTa> finisherA,
				BiFunction<BlockDatagenTaskBuilderWithItem<iTa, iTb, iR>, List<SimpleDatagenTask<?,?>>, iTb> finisherB) {
			return new BlockDatagenTaskBuilderNoItem<>(block, finisherA, finisherB);
		}
		
		public abstract BlockDatagenTaskBuilder<T,Ta,Tb,R,S> blockTag(TagKey<Block> tag);
		@SuppressWarnings("unchecked")
		public abstract BlockDatagenTaskBuilder<T,Ta,Tb,R,S> blockTags(TagKey<Block>... tags);
		public abstract BlockDatagenTaskBuilder<T,Ta,Tb,R,S> blockTags(Collection<TagKey<Block>> tags);
		
		@SuppressWarnings("unchecked")
		protected List<SimpleDatagenTask<?, ?>> buildSharedTasks() {
			List<SimpleDatagenTask<?, ?>> tasks = new ArrayList<>();
			
			tasks.add(new BasicBlockModelDatagenTask<>(block));
			
			if (!blockTags.isEmpty()) {
				tasks.add(SimpleTagDatagenTask.blockTags((RegistryObject<Block>) block, blockTags));
			}
			
			return tasks;
		}
		
	}
	
	@Accessors(fluent = true, chain = true)
	@Getter
	public static class ItemDatagenTaskBuilder<T, R extends Item> extends DatagenTaskBuilderSingleType<T, ItemDatagenTaskBuilder<T,R>> {
		protected static enum ItemModelType {
			SIMPLE,
			COMPLEX,
			BLOCK,
			BLOCK_FLAT;
		}

		private final RegistryObject<R> item;
		private ItemModelType itemModelType = ItemModelType.SIMPLE;
		private final Set<TagKey<Item>> itemTags = new HashSet<>();
		private final List<OverridePredicateData> overridePredicates = new ArrayList<>();
		
		public ItemDatagenTaskBuilder(RegistryObject<R> item, BiFunction<ItemDatagenTaskBuilder<T,R>, List<SimpleDatagenTask<?, ?>>, T> finisher) {
			super(finisher);
			this.item = item;
		}
		public static <iR extends Item> ItemDatagenTaskBuilder<List<SimpleDatagenTask<?,?>>, iR> unchained(RegistryObject<iR> item) {
			return new ItemDatagenTaskBuilder<>(item, DatagenTaskBuilder::identityFinisher);
		}
		public static <iT, iR extends Item> ItemDatagenTaskBuilder<iT, iR> chained(RegistryObject<iR> item, 
				BiFunction<ItemDatagenTaskBuilder<iT,iR>, List<SimpleDatagenTask<?, ?>>, iT> finisher) {
			return new ItemDatagenTaskBuilder<>(item, finisher);
		}
		
		public ItemDatagenTaskBuilder<T,R> overridePredicate(OverridePredicateData overridePredicate) {
			overridePredicates.add(overridePredicate);
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T build() {
			List<SimpleDatagenTask<?, ?>> tasks = new ArrayList<>();

			if (!itemTags.isEmpty()) {
				tasks.add(SimpleTagDatagenTask.itemTags((RegistryObject<Item>) item, itemTags));
			}
			
			if (overridePredicates.isEmpty()) {
				tasks.add(new BasicItemModelDatagenTask<>(item));
			} else {
				tasks.add(new PropertyDependentItemModelDatagenTask<>(item, overridePredicates.toArray(OverridePredicateData[]::new)));
			}
			
			return finish(tasks);
		}
		
	}
}
