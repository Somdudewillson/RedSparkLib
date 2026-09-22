package somdudewillson.redsparklib.common.data.task;

import java.util.function.Predicate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
//? neoforge {
/*import java.util.function.Supplier;
*///?} forge {
import net.minecraftforge.registries.RegistryObject;
//?}
import org.slf4j.Logger;

@RequiredArgsConstructor
public abstract class SimpleDatagenTask<T, P> {
	@Getter
	//? neoforge {
	/*protected final Supplier<T> targetRegistry;
	*///?} forge {
	protected final RegistryObject<T> targetRegistry;
	//?}
	
	public <L extends P> void buildData(L dataProvider, Logger logger) {
		buildData(dataProvider);
	}
	protected abstract <L extends P> void buildData(L dataProvider);
	
	public abstract DatagenTaskType getTaskType();
	public abstract Class<P> getDataProviderClass();
	
	public <L> boolean compatibleWith(Class<L> dataProviderClass) {
		return getDataProviderClass().isAssignableFrom(dataProviderClass);
	}
	public static <L> Predicate<SimpleDatagenTask<?,?>> classCompatibleFilter(Class<L> dataProviderClass) {
		return t -> t.compatibleWith(dataProviderClass);
	}
}
