package somdudewillson.redsparklib.common.utils;

//? neoforge {
/*import net.minecraft.core.HolderLookup;
*///?}
import net.minecraft.nbt.Tag;

@FunctionalInterface
public interface TagSerializerFunction<U, T extends Tag> {
	//? neoforge {
	/*T serialize(U instance, HolderLookup.Provider provider);
	*///?} forge {
	T serialize(U instance);
	//?}

	default TagInlineSerializerFunction<T, U> inline() {
		return ExtNbtUtils.makeInlineNbtSerializer(this);
	}
}
