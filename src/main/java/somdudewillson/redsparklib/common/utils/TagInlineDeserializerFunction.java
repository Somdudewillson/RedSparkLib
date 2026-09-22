package somdudewillson.redsparklib.common.utils;

//? neoforge {
/*import net.minecraft.core.HolderLookup;
import java.util.function.BiFunction;
*///?} forge {
import java.util.function.Function;
//?}

import net.minecraft.nbt.Tag;

@FunctionalInterface
public interface TagInlineDeserializerFunction<T extends Tag, U> extends /*? neoforge {*//*BiFunction<HolderLookup.Provider, T, U>*//*?} forge {*/Function<T, U>/*?}*/ {
	
}
