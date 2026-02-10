package somdudewillson.redsparklib.world.entity.ai.util;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import somdudewillson.redsparklib.common.utils.ExtNbtUtils;
import somdudewillson.redsparklib.serialization.codecs.DelegatingNbtWrappingCodec;

public record MinimalNodeData(BlockPos pos, BlockPathTypes pathType, float malus) {
	public static final Codec<MinimalNodeData> CODEC = DelegatingNbtWrappingCodec.compound(MinimalNodeData::serializeNBT, MinimalNodeData::deserializeNBT);
	
	public static MinimalNodeData fromNode(Node node) {
		return new MinimalNodeData(node.asBlockPos(), node.type, node.costMalus);
	}
	
	public static CompoundTag serializeNBT(MinimalNodeData value) {
		CompoundTag root = new CompoundTag();
		
		root.put("pos", ExtNbtUtils.serializeBlockPos(value.pos()));
		root.putString("path_type", value.pathType().name());
		root.putFloat("malus", value.malus());
		
		return root;
	}
	public static MinimalNodeData deserializeNBT(CompoundTag nbt) {
		return new MinimalNodeData(
				ExtNbtUtils.deserializeBlockPos(nbt.get("pos")),
				BlockPathTypes.valueOf(nbt.getString("path_type")),
				nbt.getFloat("malus")
				);
	}
	
}