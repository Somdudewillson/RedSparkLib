package somdudewillson.redsparklib.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

@ExtendWith(MockitoExtension.class)
public class PathingUtilsTest {

    @Mock
    private Path mockPath;

    @Mock
    private Node mockNode0;

    @Mock
    private Node mockNode1;

    @Mock
    private Node mockNode2;

    @Test
    public void testPathNodesStream() {
        when(mockPath.getNodeCount()).thenReturn(3);
        when(mockPath.getNode(0)).thenReturn(mockNode0);
        when(mockPath.getNode(1)).thenReturn(mockNode1);
        when(mockPath.getNode(2)).thenReturn(mockNode2);

        List<Node> collected = PathingUtils.pathNodesStream(mockPath).collect(Collectors.toList());
        assertEquals(3, collected.size());
        assertEquals(mockNode0, collected.get(0));
        assertEquals(mockNode1, collected.get(1));
        assertEquals(mockNode2, collected.get(2));
    }

    @Test
    public void testPathNodesWithIdxStream() {
        when(mockPath.getNodeCount()).thenReturn(2);
        when(mockPath.getNode(0)).thenReturn(mockNode0);
        when(mockPath.getNode(1)).thenReturn(mockNode1);

        List<Pair<Integer, Node>> collected = PathingUtils.pathNodesWithIdxStream(mockPath).collect(Collectors.toList());
        assertEquals(2, collected.size());
        assertEquals(Pair.of(0, mockNode0), collected.get(0));
        assertEquals(Pair.of(1, mockNode1), collected.get(1));
    }

    @Test
    public void testVisitPathNodesBiFunction() {
        when(mockPath.getNodeCount()).thenReturn(3);
        when(mockPath.getNode(0)).thenReturn(mockNode0);
        when(mockPath.getNode(1)).thenReturn(mockNode1);
        when(mockPath.getNode(2)).thenReturn(mockNode2);

        int result = PathingUtils.visitPathNodes(mockPath, (count, node) -> count + 1, 0);
        assertEquals(3, result);
    }

    @Test
    public void testVisitPathNodesTriFunction() {
        when(mockPath.getNodeCount()).thenReturn(3);
        when(mockPath.getNode(0)).thenReturn(mockNode0);
        when(mockPath.getNode(1)).thenReturn(mockNode1);
        when(mockPath.getNode(2)).thenReturn(mockNode2);

        List<String> visits = new ArrayList<>();
        PathingUtils.visitPathNodes(mockPath, (list, prev, cur) -> {
            list.add((prev == null ? "null" : "node") + "->" + (cur == null ? "null" : "node"));
            return list;
        }, false, visits);

        assertEquals(3, visits.size());
        assertEquals("null->node", visits.get(0));
    }

    @Test
    public void testCalculatePathLength() {
        when(mockPath.getNodeCount()).thenReturn(3);
        when(mockPath.getNode(0)).thenReturn(mockNode0);
        when(mockPath.getNode(1)).thenReturn(mockNode1);
        when(mockPath.getNode(2)).thenReturn(mockNode2);

        when(mockNode0.asVec3()).thenReturn(new Vec3(0, 0, 0));
        when(mockNode0.distanceTo(mockNode1)).thenReturn(3.0f);
        when(mockNode1.distanceTo(mockNode2)).thenReturn(4.0f);

        Vec3 start = new Vec3(-1, 0, 0);

        // Total expected = 1.0 + 3.0 + 4.0 = 8.0
        double totalLength = PathingUtils.calculatePathLength(mockPath, start);
        assertEquals(8.0, totalLength, 0.001);
    }

    @Test
    public void testTruncatePathToFirstMatch() {
        when(mockPath.getNodeCount()).thenReturn(3);
        when(mockPath.getNode(0)).thenReturn(mockNode0);
        when(mockPath.getNode(1)).thenReturn(mockNode1);

        // We want to truncate at the first node that matches a condition.
        PathingUtils.truncatePathToFirstMatch(mockPath, node -> node == mockNode1);

        verify(mockPath).truncateNodes(2);
    }
}
