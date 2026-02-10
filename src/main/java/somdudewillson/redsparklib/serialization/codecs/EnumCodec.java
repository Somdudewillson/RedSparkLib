package somdudewillson.redsparklib.serialization.codecs;

import java.util.HashMap;
import java.util.Map;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

public class EnumCodec<A extends Enum<A>> implements Codec<A> {
	private final Map<String, A> nameMap = new HashMap<>();
	
	public EnumCodec(Class<A> enumClazz) {
		this(enumClazz.getEnumConstants());
	}
	
	public EnumCodec(A[] enumValues) {
		for (A val : enumValues) {
			nameMap.put(val.name(), val);
		}
	}
	
	@Override
	public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
		return PrimitiveCodec.STRING.encode(input.name(), ops, prefix);
	}

	@Override
	public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
		return PrimitiveCodec.STRING.decode(ops, input).map(pair->pair.mapFirst(nameMap::get));
	}

}
