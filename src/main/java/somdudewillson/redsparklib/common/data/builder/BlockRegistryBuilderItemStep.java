package somdudewillson.redsparklib.common.data.builder;

import java.util.function.Consumer;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public interface BlockRegistryBuilderItemStep<T extends Block> {
	public BlockRegistryBuilderItemStep<T> inCreativeTab(Consumer<RegistryObject<? extends Item>> tabRegistrar);
	public BlockRegistryBuilderFinishedStep<T> properties(Item.Properties properties);
}
