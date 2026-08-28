package somdudewillson.redsparklib.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.phys.AABB;

public class ExtNbtUtilsTest {

    enum TestEnum {
        FIRST, SECOND, THIRD
    }

    @Test
    public void testBlockPosSerialization() {
        BlockPos pos = new BlockPos(10, -20, 30);
        LongTag tag = ExtNbtUtils.serializeBlockPos(pos);
        BlockPos decoded = ExtNbtUtils.deserializeBlockPos(tag);
        assertEquals(pos, decoded);
    }

    @Test
    public void testBoundingBoxSerialization() {
        AABB box = new AABB(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        ListTag tag = ExtNbtUtils.serializeBoundingBox(box);
        AABB decoded = ExtNbtUtils.deserializeBoundingBox(tag);
        assertEquals(box, decoded);
    }

    @Test
    public void testEnumSerialization() {
        Tag tag1 = ExtNbtUtils.serializeEnumValue(TestEnum.SECOND, true);
        assertTrue(tag1 instanceof StringTag);
        assertEquals("SECOND", tag1.getAsString());

        Tag tag2 = ExtNbtUtils.serializeEnumValue(TestEnum.THIRD, false);
        assertTrue(tag2 instanceof IntTag);
        assertEquals(2, ((IntTag) tag2).getAsInt());

        assertEquals(TestEnum.SECOND, ExtNbtUtils.deserializeEnumValue(tag1, TestEnum.class));
        assertEquals(TestEnum.THIRD, ExtNbtUtils.deserializeEnumValue(tag2, TestEnum.class));
    }

    @Test
    public void testStreamSerialization() {
        Stream<StringTag> stream = Stream.of(StringTag.valueOf("a"), StringTag.valueOf("b"));
        ListTag listTag = ExtNbtUtils.serializeTagStream(stream);
        assertEquals(2, listTag.size());
        assertEquals("a", listTag.getString(0));
        assertEquals("b", listTag.getString(1));
    }

    @Test
    public void testListSerialization() {
        List<StringTag> list = List.of(StringTag.valueOf("x"), StringTag.valueOf("y"));
        ListTag tag = ExtNbtUtils.serializeTagList(list);
        List<StringTag> decoded = ExtNbtUtils.deserializeIntoList(tag, new ArrayList<>(), t -> (StringTag) t);
        assertEquals(list, decoded);
    }

    @Test
    public void testSetSerialization() {
        Set<StringTag> set = Set.of(StringTag.valueOf("hello"));
        ListTag tag = ExtNbtUtils.serializeTagSet(set);
        Set<StringTag> decoded = ExtNbtUtils.deserializeIntoSet(tag, new HashSet<>(), t -> (StringTag) t);
        assertEquals(set, decoded);
    }

    @Test
    public void testMapSerialization() {
        Map<StringTag, IntTag> map = Map.of(StringTag.valueOf("key"), IntTag.valueOf(42));
        ListTag tag = ExtNbtUtils.serializeMap(map, Function -> Function, Function -> Function);
        Map<StringTag, IntTag> decoded = ExtNbtUtils.deserializeIntoMap(tag, new HashMap<>(), t -> (StringTag) t, t -> (IntTag) t);
        assertEquals(map, decoded);
    }

    @Test
    public void testMultisetSerialization() {
        Multiset<StringTag> multiset = HashMultiset.create();
        multiset.add(StringTag.valueOf("a"), 2);
        multiset.add(StringTag.valueOf("b"), 1);

        ListTag tag = ExtNbtUtils.serializeTagMultiset(multiset);
        Multiset<StringTag> decoded = ExtNbtUtils.deserializeIntoMultiset(tag, HashMultiset.create(), t -> (StringTag) t);
        assertEquals(multiset, decoded);
    }

    @Test
    public void testMultimapSerialization() {
        Multimap<StringTag, IntTag> multimap = HashMultimap.create();
        multimap.put(StringTag.valueOf("key"), IntTag.valueOf(42));
        multimap.put(StringTag.valueOf("key"), IntTag.valueOf(43));
        multimap.put(StringTag.valueOf("other"), IntTag.valueOf(100));

        ListTag tag = ExtNbtUtils.serializeMultimap(multimap, Function -> Function, Function -> Function);
        Multimap<StringTag, IntTag> decoded = ExtNbtUtils.deserializeIntoMultimap(tag, HashMultimap.create(), t -> (StringTag) t, t -> (IntTag) t);
        assertEquals(multimap, decoded);
    }

}
