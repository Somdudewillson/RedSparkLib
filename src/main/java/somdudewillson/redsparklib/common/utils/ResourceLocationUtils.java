package somdudewillson.redsparklib.common.utils;

import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;

@RequiredArgsConstructor
public class ResourceLocationUtils {
	private final String modId;
	
	public ResourceLocation modPath(String path) {
		return ResourceLocation.fromNamespaceAndPath(modId, path);
	}
	
	public ResourceLocation modTex(String path) {
		return modPath("textures/"+path);
	}
	
	public ResourceLocation modEntityTex(String path) {
		return modTex("entity/"+path);
	}
	
	public static ResourceLocation mcPath(String path) {
		return ResourceLocation.fromNamespaceAndPath(ResourceLocation.DEFAULT_NAMESPACE, path);
	}
}
