package somdudewillson.redsparklib.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import net.minecraft.nbt.CompoundTag;

public class OptNbtUtilsTest {

    @Test
    public void testPutAndGetNumeric() {
        CompoundTag tag = new CompoundTag();
        
        OptNbtUtils.putByte(tag, "byteKey", (byte) 12);
        OptNbtUtils.putShort(tag, "shortKey", (short) 345);
        OptNbtUtils.putInt(tag, "intKey", 6789);
        OptNbtUtils.putLong(tag, "longKey", 9876543210L);
        OptNbtUtils.putFloat(tag, "floatKey", 12.34f);
        OptNbtUtils.putDouble(tag, "doubleKey", 56.78);
        OptNbtUtils.putBoolean(tag, "boolKey", true);

        assertEquals((byte) 12, OptNbtUtils.getByte(tag, "byteKey").orElse((byte) 0));
        assertEquals((short) 345, OptNbtUtils.getShort(tag, "shortKey").orElse((short) 0));
        assertEquals(6789, OptNbtUtils.getInt(tag, "intKey").orElse(0));
        assertEquals(9876543210L, OptNbtUtils.getLong(tag, "longKey").orElse(0L));
        assertEquals(12.34f, OptNbtUtils.getFloat(tag, "floatKey").orElse(0.0f));
        assertEquals(56.78, OptNbtUtils.getDouble(tag, "doubleKey").orElse(0.0));
        assertTrue(OptNbtUtils.getBoolean(tag, "boolKey").orElse(false));
    }

    @Test
    public void testPutAndGetStringAndUUID() {
        CompoundTag tag = new CompoundTag();
        UUID uuid = UUID.randomUUID();
        
        OptNbtUtils.putString(tag, "strKey", "hello");
        OptNbtUtils.putUUID(tag, "uuidKey", uuid);

        assertEquals("hello", OptNbtUtils.getString(tag, "strKey").orElse(""));
        assertEquals(uuid, OptNbtUtils.getUUID(tag, "uuidKey").orElse(null));
    }

    @Test
    public void testGetAbsent() {
        CompoundTag tag = new CompoundTag();
        assertFalse(OptNbtUtils.getByte(tag, "absent").isPresent());
        assertFalse(OptNbtUtils.getShort(tag, "absent").isPresent());
    }
}
