package somdudewillson.redsparklib.common.data.task;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiFunction;

import lombok.val;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.RegistryObject;
//? neoforge {
/*import java.util.function.Supplier;
*///?}

public abstract class SimpleTagDatagenTask<R, P extends IntrinsicHolderTagsProvider<T>, T> extends SimpleDatagenTask<R, P> {
	protected final BiFunction<P, TagKey<T>, IntrinsicHolderTagsProvider.IntrinsicTagAppender<T>> tagFunc;
	protected final ArrayList<TagKey<T>> tags = new ArrayList<>(); 

	@SuppressWarnings({ "unchecked", "rawtypes" })
	//? neoforge {
	/*protected SimpleTagDatagenTask(Supplier<R> targetRegistry, Collection<TagKey<T>> tags) {
	*///?} forge {
	protected SimpleTagDatagenTask(RegistryObject<R> targetRegistry, Collection<TagKey<T>> tags) {
	//?}
		super(targetRegistry);
		this.tags.addAll(tags);
		
		val tagMethod = ObfuscationReflectionHelper.findMethod(IntrinsicHolderTagsProvider.class, "m_206424_", TagKey.class);
		tagFunc = (tagProvider, tagKey) -> {
			try {
				return (IntrinsicHolderTagsProvider.IntrinsicTagAppender)tagMethod.invoke(tagProvider, tagKey);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { }
			return null;
		};
	}
	
	public static SimpleAlignedTagDatagenTask<Block, BlockTagsProvider> blockTags(RegistryObject</*? neoforge {*//*Block, *//*?}*/Block> targetRegistry, Collection<TagKey<Block>> tags) {
		return new SimpleAlignedTagDatagenTask<>(targetRegistry, DatagenTaskType.BLOCK_TAGS, BlockTagsProvider.class, tags);
	}
	public static SimpleAlignedTagDatagenTask<Item, ItemTagsProvider> itemTags(RegistryObject</*? neoforge {*//*Item, *//*?}*/Item> targetRegistry, Collection<TagKey<Item>> tags) {
		return new SimpleAlignedTagDatagenTask<>(targetRegistry, DatagenTaskType.ITEM_TAGS, ItemTagsProvider.class, tags);
	}
	public static SimpleBlockItemTagDatagenTask blockItemTags(RegistryObject</*? neoforge {*//*Block, *//*?}*/Block> targetRegistry, Collection<TagKey<Item>> tags) {
		return new SimpleBlockItemTagDatagenTask(targetRegistry, tags);
	}
	
	public static class SimpleAlignedTagDatagenTask<T, P extends IntrinsicHolderTagsProvider<T>> extends SimpleTagDatagenTask<T, P, T> {
		protected final DatagenTaskType taskType;
		protected final Class<P> dataProviderClass;
		
		//? neoforge {
		/*protected SimpleAlignedTagDatagenTask(Supplier<T> targetRegistry, DatagenTaskType taskType, Class<P> dataProviderClass, Collection<TagKey<T>> tags) {
		*///?} forge {
		protected SimpleAlignedTagDatagenTask(RegistryObject<T> targetRegistry, DatagenTaskType taskType, Class<P> dataProviderClass, Collection<TagKey<T>> tags) {
		//?}
			super(targetRegistry, tags);
			this.taskType = taskType;
			this.dataProviderClass = dataProviderClass;
		}

		@Override
		protected <L extends P> void buildData(L dataProvider) {
			for (TagKey<T> tag : tags) {
				tagFunc.apply(dataProvider, tag).add(targetRegistry.get());
			}
		}

		@Override
		public DatagenTaskType getTaskType() {
			return taskType;
		}

		@Override
		public Class<P> getDataProviderClass() {
			return dataProviderClass;
		}
	}
	
	public static class SimpleBlockItemTagDatagenTask extends SimpleTagDatagenTask<Block, ItemTagsProvider, Item> {
		protected SimpleBlockItemTagDatagenTask(RegistryObject</*? neoforge {*//*Block, *//*?}*/Block> targetRegistry, Collection<TagKey<Item>> tags) {
			super(targetRegistry, tags);
		}

		@Override
		protected <L extends ItemTagsProvider> void buildData(L dataProvider) {
			for (TagKey<Item> tag : tags) {
				tagFunc.apply(dataProvider, tag).add(targetRegistry.get().asItem());
			}
		}

		@Override
		public DatagenTaskType getTaskType() {
			return DatagenTaskType.ITEM_TAGS;
		}

		@Override
		public Class<ItemTagsProvider> getDataProviderClass() {
			return ItemTagsProvider.class;
		}
	}

}
