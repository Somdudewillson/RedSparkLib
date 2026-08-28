package somdudewillson.redsparklib.common.data.builder;

import java.util.function.Consumer;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public interface ItemRegistryBuilderFinishedStep<T extends Item, R> {
	public ItemRegistryBuilderFinishedStep<T,R> inCreativeTab(Consumer<RegistryObject<? extends Item>> tabRegistrar);
	public R build();
}
