package somdudewillson.redsparklib.common.utils;

import net.minecraft.nbt.StringTag;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

public class SerializableCachableValueTest {

    @Test
    public void testCachingAndLazyLoading() {
        AtomicInteger serializeCount = new AtomicInteger(0);
        AtomicInteger deserializeCount = new AtomicInteger(0);

        SerializableCachableValue<StringTag, String> cache = new SerializableCachableValue<>(
            s -> {
                serializeCount.incrementAndGet();
                return StringTag.valueOf(s);
            },
            tag -> {
                deserializeCount.incrementAndGet();
                return tag.getAsString();
            }
        );

        assertFalse(cache.hasValue());

        // Set value directly. Serialized should be null.
        cache.setValue("hello");
        assertTrue(cache.hasValue());
        assertEquals(0, serializeCount.get());
        assertEquals(0, deserializeCount.get());

        // Get value: should return "hello" directly without deserialization.
        Optional<String> val = cache.tryGetValue();
        assertTrue(val.isPresent());
        assertEquals("hello", val.get());
        assertEquals(0, deserializeCount.get());

        // Get serialized: should trigger serializer.
        Optional<StringTag> tag = cache.tryGetSerialized();
        assertTrue(tag.isPresent());
        assertEquals("hello", tag.get().getAsString());
        assertEquals(1, serializeCount.get());

        // Subsequent get serialized: should be cached, no extra serialization.
        cache.tryGetSerialized();
        assertEquals(1, serializeCount.get());

        // Now clear
        cache.clear();
        assertFalse(cache.hasValue());

        // Set serialized directly
        cache.setSerialized(StringTag.valueOf("world"));
        assertTrue(cache.hasValue());

        // Get value: should trigger deserializer.
        Optional<String> val2 = cache.tryGetValue();
        assertTrue(val2.isPresent());
        assertEquals("world", val2.get());
        assertEquals(1, deserializeCount.get());

        // Subsequent get value: should be cached, no extra deserialization.
        cache.tryGetValue();
        assertEquals(1, deserializeCount.get());
    }
}
