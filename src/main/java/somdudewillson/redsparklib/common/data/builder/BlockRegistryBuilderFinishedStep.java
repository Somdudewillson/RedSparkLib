package somdudewillson.redsparklib.common.data.builder;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public interface BlockRegistryBuilderFinishedStep<T extends Block> {
	public RegistryObject</*? neoforge {*//*Block, *//*?}*/Block> build();
}
