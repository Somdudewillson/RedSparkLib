package somdudewillson.redsparklib.common.utils;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;
//? neoforge {
/*import net.minecraft.core.HolderLookup;
import org.jetbrains.annotations.Nullable;
*///?}

/**
 * An interface that marks an {@link INBTSerializable} as safe to call without extra context (i.e. registry access).
 */
public interface IContextFreeNBTSerializable<T extends Tag> extends INBTSerializable<T> {
	//? neoforge {
	/*@Override
	T serializeNBT(@Nullable HolderLookup.Provider provider);
	@Override
	void deserializeNBT(@Nullable HolderLookup.Provider provider, T nbt);
	*///?}
	
    default T serializeNBTContextless() {
    	return serializeNBT(/*? neoforge {*//*null*//*?}*/);
    }
    default void deserializeNBTContextless(T nbt) {
    	deserializeNBT(/*? neoforge {*//*null,*//*?}*/nbt);
    }
    
    public static <T extends Tag> IContextFreeNBTSerializable<T> wrap(INBTSerializable<T> inner) {
    	return new WrappingContextFreeNBTSerializable<>(inner);
    }
    
    @RequiredArgsConstructor(access = AccessLevel.PROTECTED)
    public static class WrappingContextFreeNBTSerializable<T extends Tag> implements IContextFreeNBTSerializable<T> {
    	private final INBTSerializable<T> wrapped;
    	
    	@Override
		public T serializeNBT(/*? neoforge {*//*@Nullable HolderLookup.Provider provider*//*?}*/) {
			return wrapped.serializeNBT(/*? neoforge {*//*provider*//*?}*/);
		}

		@Override
		public void deserializeNBT(T nbt) {
			wrapped.deserializeNBT(/*? neoforge {*//*provider,*//*?}*/nbt);
		}
    	
    }
}
