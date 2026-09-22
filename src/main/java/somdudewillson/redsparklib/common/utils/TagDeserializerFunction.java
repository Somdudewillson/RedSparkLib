package somdudewillson.redsparklib.common.utils;

import java.util.function.Supplier;

//? neoforge {
/*import net.minecraft.core.HolderLookup;
*///?}
import net.minecraft.nbt.Tag;

@FunctionalInterface
public interface TagDeserializerFunction<U, T extends Tag> {
	//? neoforge {
	/*void deserialize(U instance, HolderLookup.Provider provider, T tag);
	*///?} forge {
	void deserialize(U instance, T tag);
	//?}

	default TagInlineDeserializerFunction<T, U> inline(Supplier<U> instanceSupplier) {
		return ExtNbtUtils.makeInlineNbtDeserializer(instanceSupplier, this);
	}
}
