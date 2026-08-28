package somdudewillson.redsparklib.common.utils;

import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceLocationUtilsTest {

    @Test
    public void testResourceLocationUtils() {
        ResourceLocationUtils utils = new ResourceLocationUtils("testmod");

        ResourceLocation path = utils.modPath("test_path");
        assertEquals("testmod", path.getNamespace());
        assertEquals("test_path", path.getPath());

        ResourceLocation tex = utils.modTex("gui/widget.png");
        assertEquals("testmod", tex.getNamespace());
        assertEquals("textures/gui/widget.png", tex.getPath());

        ResourceLocation entityTex = utils.modEntityTex("zombie.png");
        assertEquals("testmod", entityTex.getNamespace());
        assertEquals("textures/entity/zombie.png", entityTex.getPath());

        ResourceLocation mc = ResourceLocationUtils.mcPath("air");
        assertEquals("minecraft", mc.getNamespace());
        assertEquals("air", mc.getPath());
    }
}
