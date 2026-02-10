package somdudewillson.redsparklib.common.data.task;

import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.minecraftforge.registries.RegistryObject;

@RequiredArgsConstructor
public abstract class SimpleDatagenTask<T, P> {
	@RequiredArgsConstructor
	@FieldDefaults(makeFinal=true, level=AccessLevel.PRIVATE)
	@Getter
	public static enum DatagenTaskType {
		ITEM_MODEL,
		BLOCK_MODEL,
		ITEM_TAGS,
		BLOCK_TAG,
		BLOCK_LOOT;
	}
	@Getter
	protected final RegistryObject<T> targetRegistry;
	
	public abstract <L extends P> void buildData(L dataProvider);
	
	public abstract DatagenTaskType getTaskType();
	public abstract Class<P> getDataProviderClass();
	
	public <L> boolean compatibleWith(Class<L> dataProviderClass) {
		return getDataProviderClass().isAssignableFrom(dataProviderClass);
	}
	public static <L> Predicate<SimpleDatagenTask<?,?>> classCompatibleFilter(Class<L> dataProviderClass) {
		return t -> t.compatibleWith(dataProviderClass);
	}
}
