package somdudewillson.redsparklib.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.apache.commons.lang3.function.TriFunction;
import org.apache.commons.lang3.tuple.Pair;

import lombok.val;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import somdudewillson.redsparklib.world.entity.ai.util.ResumableTraversalScanner;

public class PathingUtils {

	public static Stream<Node> pathNodesStream(Path path) {
		return IntStream.range(0, path.getNodeCount()).mapToObj(i->path.getNode(i));
	}
	public static Stream<Pair<Integer, Node>> pathNodesWithIdxStream(Path path) {
		return IntStream.range(0, path.getNodeCount()).mapToObj(i->Pair.of(i, path.getNode(i)));
	}

	public static void visitPathNodes(Path path, Consumer<Node> visitor) {
		pathNodesStream(path).forEach(visitor);
	}
	public static void visitPathNodes(Path path, BiConsumer<Node, Node> visitor, boolean skipFirst) {
		Node prevNode = null;
		for (int i=0;i<path.getNodeCount();i++) {
			val curNode = path.getNode(i);
			if (i!=0 || !skipFirst) {
				visitor.accept(prevNode, curNode);
			}
			prevNode = curNode;
		}
	}
	public static <T> T visitPathNodes(Path path, BiFunction<T, Node, T> visitor, @Nullable T initialValue) {
		T value = initialValue;
		for (int i=0;i<path.getNodeCount();i++) {
			visitor.apply(value, path.getNode(i));
		}

		return value;
	}
	public static <T> T visitPathNodes(Path path, BiFunction<T, Node, T> visitor) {
		return visitPathNodes(path, visitor, null);
	}
	public static <T> T visitPathNodes(Path path, TriFunction<T, Node, Node, T> visitor, boolean skipFirst, @Nullable T initialValue) {
		T value = initialValue;
		Node prevNode = null;
		for (int i=0;i<path.getNodeCount();i++) {
			val curNode = path.getNode(i);
			if (i!=0 || !skipFirst) {
				visitor.apply(value, prevNode, curNode);
			}
			prevNode = curNode;
		}

		return value;
	}
	public static <T> T visitPathNodes(Path path, TriFunction<T, Node, Node, T> visitor, boolean skipFirst) {
		return visitPathNodes(path, visitor, skipFirst, null);
	}

	public static double calculatePathLength(Path path, Vec3 startPos) {
		return visitPathNodes(path, (dist, a, b)->dist+a.distanceTo(b), true, 0d) + (startPos.distanceTo(path.getNode(0).asVec3()));
	}
	public static double calculatePathLength(Path path) {
		return calculatePathLength(path, path.getNodeCount()>0 ? path.getNode(0).asVec3() : Vec3.ZERO);
	}

	public static Path truncatePathToFirstMatch(Path path, Predicate<Node> predicate) {
		pathNodesWithIdxStream(path)
			.filter(p->predicate.test(p.getRight()))
			.findFirst()
			.ifPresent(p->path.truncateNodes(p.getLeft()+1));
		return path;
	}
	
	public static Set<BlockPos> getAccesibleViaPathfindingFromPos(Level level, Mob entity, BlockPos startPos, double maxRange, Predicate<Node> traversibleFilter, Predicate<Node> targetFilter) {
		level.getProfiler().push("getAccesibleViaPathfindingFromPos");
		
		int offset = (int) Math.ceil(maxRange);
		PathNavigationRegion pathnavigationregion = new PathNavigationRegion(level, startPos.offset(-offset, -offset, -offset), startPos.offset(offset, offset, offset));
		
		val result = ResumableTraversalScanner.run(
				pathnavigationregion, ()->entity.getNavigation().getNodeEvaluator(), 
				entity, ResumableTraversalScanner.traversiblePosPredicate().and(traversibleFilter), n->false,
				startPos, maxRange);
		
		val posSet = result.successSet()
				.stream()
				.filter(targetFilter)
				.map(Node::asBlockPos)
				.collect(Collectors.toSet());
		level.getProfiler().pop();
		return posSet;
	}
	public static Set<BlockPos> getAccesibleViaPathfindingFromPos(Level level, Mob entity, BlockPos startPos, double maxRange) {
		return getAccesibleViaPathfindingFromPos(level, entity, startPos, maxRange, n->true, n->true);
	}
	public static Set<BlockPos> getAccesibleViaPathfindingFromCurrentPos(Level level, Mob entity, double maxRange, Predicate<Node> traversibleFilter, Predicate<Node> targetFilter) {
		return getAccesibleViaPathfindingFromPos(level, entity, entity.blockPosition(), maxRange, traversibleFilter, targetFilter);
	}
	public static Set<BlockPos> getAccesibleViaPathfindingFromCurrentPos(Level level, Mob entity, double maxRange) {
		return getAccesibleViaPathfindingFromPos(level, entity, entity.blockPosition(), maxRange);
	}
	public static Set<BlockPos> getAccesibleViaPathfindingFromCurrentPos(Mob entity, double maxRange, Predicate<Node> traversibleFilter, Predicate<Node> targetFilter) {
		return getAccesibleViaPathfindingFromPos(entity.level(), entity, entity.blockPosition(), maxRange, traversibleFilter, targetFilter);
	}
	public static Set<BlockPos> getAccesibleViaPathfindingFromCurrentPos(Mob entity, double maxRange) {
		return getAccesibleViaPathfindingFromPos(entity.level(), entity, entity.blockPosition(), maxRange);
	}

	// Non-Public Pathing Accessor
	private static final AtomicReference<PathFunction> PATH_FUNC = new AtomicReference<>();
	private static interface PathFunction {
		@Nullable
		public Path createPath(PathNavigation navigation, Set<BlockPos> pTargets, int pRegionOffset, boolean pOffsetUpward, int pAccuracy, float pFollowRange);
	}
	@Nullable
	public static Path createPath(PathNavigation navigation, Set<BlockPos> pTargets, int pRegionOffset, boolean pOffsetUpward, int pAccuracy, float pFollowRange) {
		val pathFunc = PATH_FUNC.updateAndGet(f->{
			if (f != null) { return f; }
			Method pathMethod = ObfuscationReflectionHelper.findMethod(PathNavigation.class, "m_148222_", Set.class, int.class, boolean.class, int.class, float.class);
			return (nav, targets, regionOffset, offsetUp, acc, range) -> {
				try {
					return (Path)pathMethod.invoke(nav, targets, regionOffset, offsetUp, acc, range);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				return null;
			};
		});

		return pathFunc.createPath(navigation, pTargets, pRegionOffset, pOffsetUpward, pAccuracy, pFollowRange);
	}
}
