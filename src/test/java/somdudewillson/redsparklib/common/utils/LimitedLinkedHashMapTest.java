package somdudewillson.redsparklib.common.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class LimitedLinkedHashMapTest {

    @Test
    public void testCapacityEviction() {
        LimitedLinkedHashMap<String, Integer> map = new LimitedLinkedHashMap<>(3);
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        
        assertEquals(3, map.size());
        
        map.put("d", 4);
        assertEquals(3, map.size());
        assertFalse(map.containsKey("a"));
        assertTrue(map.containsKey("b"));
        assertTrue(map.containsKey("c"));
        assertTrue(map.containsKey("d"));
    }

    @Test
    public void testInvalidCapacities() {
        assertThrows(IllegalArgumentException.class, () -> new LimitedLinkedHashMap<>(0));
        assertThrows(IllegalArgumentException.class, () -> new LimitedLinkedHashMap<>(5, 3));
    }

    @Test
    public void testMapConstructor() {
        Map<String, Integer> source = new HashMap<>();
        source.put("a", 1);
        source.put("b", 2);
        
        LimitedLinkedHashMap<String, Integer> map = new LimitedLinkedHashMap<>(source, 3);
        assertEquals(2, map.size());
        assertEquals(3, map.getMaxCapacity());
        
        map.put("c", 3);
        map.put("d", 4);
        assertEquals(3, map.size());
        assertFalse(map.containsKey("a"));
    }

    @Test
    public void testAccessOrderConstructor() {
        LimitedLinkedHashMap<String, Integer> map = new LimitedLinkedHashMap<>(4, 8, 0.75f, true);
        assertEquals(8, map.getMaxCapacity());
        
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", 5);
        map.put("f", 6);
        map.put("g", 7);
        map.put("h", 8);
        
        // Access "a" to move it to the end of the access order
        map.get("a");
        
        // Exceed capacity
        map.put("i", 9);
        
        assertEquals(8, map.size());
        // Since "a" was accessed, "b" should be evicted instead of "a"
        assertFalse(map.containsKey("b"));
        assertTrue(map.containsKey("a"));
    }
}
