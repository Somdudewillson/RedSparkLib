package somdudewillson.redsparklib.common.utils;

//? neoforge {
/*import net.minecraft.core.HolderLookup;
import java.util.function.BiFunction;
*///?} forge {
import java.util.function.Function;
//?}

import net.minecraft.nbt.Tag;

@FunctionalInterface
public interface TagInlineSerializerFunction<T extends Tag, U> extends /*? neoforge {*//*BiFunction<U, HolderLookup.Provider, T>*//*?} forge {*/Function<U, T>/*?}*/ {

}
