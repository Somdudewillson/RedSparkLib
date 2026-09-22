package somdudewillson.redsparklib.platform;

import java.util.function.Consumer;

import com.mojang.serialization.DataResult;

import lombok.val;
import net.minecraft.Util;

public class PlatformSpecificUtils {
	public static <T> T getOrThrowDataResult(DataResult<T> dataResult, String errorPrefix, Consumer<String> logFunc) {
		val prefixedLogFunc = Util.prefix("Error serializing graph node: ", logFunc);
		//? neoforge {
		/*return dataResult.getOrThrow(s -> {
			prefixedLogFunc.accept(s);
			return new IllegalStateException(s);
		});
		*///?} forge {
		return dataResult.getOrThrow(false, prefixedLogFunc);
		//?}
	}
}
