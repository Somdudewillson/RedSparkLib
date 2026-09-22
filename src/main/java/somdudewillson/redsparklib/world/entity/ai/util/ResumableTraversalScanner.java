package somdudewillson.redsparklib.world.entity.ai.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.val;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
//? neoforge {
/*import net.minecraft.world.level.pathfinder.PathfindingContext;
*///?}

public class ResumableTraversalScanner {
	
	public static Predicate<Node> traversiblePosPredicate() {
		return n->n.costMalus >= 0;
	}

	public static TraversalScannerState initState(
			Supplier<NodeEvaluator> nodeEvaluatorSupplier, Mob mob, 
			Predicate<Node> testFunc, Predicate<Node> shouldEarlyStop,
			double maxRange) {
		return initState(mob.level(), nodeEvaluatorSupplier, mob, testFunc, shouldEarlyStop, maxRange);
	}
	public static TraversalScannerState initState(
			Level level, Supplier<NodeEvaluator> nodeEvaluatorSupplier, Mob mob, 
			Predicate<Node> testFunc, Predicate<Node> shouldEarlyStop,
			double maxRange) {
		return initState(level, nodeEvaluatorSupplier, mob, testFunc, shouldEarlyStop, mob.blockPosition(), maxRange);
	}
	public static TraversalScannerState initState(
			Level level, Supplier<NodeEvaluator> nodeEvaluatorSupplier, Mob mob, 
			Predicate<Node> testFunc, Predicate<Node> shouldEarlyStop,
			BlockPos startPos, double maxRange) {
		int offset = (int) Math.ceil(maxRange);
		PathNavigationRegion pathnavigationregion = new PathNavigationRegion(level, startPos.offset(-offset, -offset, -offset), startPos.offset(offset, offset, offset));
		return initState(pathnavigationregion, nodeEvaluatorSupplier, mob, testFunc, shouldEarlyStop, startPos, maxRange);
	}
	public static TraversalScannerState initState(
			PathNavigationRegion region, Supplier<NodeEvaluator> nodeEvaluatorSupplier, Mob mob, 
			Predicate<Node> testFunc, Predicate<Node> shouldEarlyStop,
			BlockPos startPos, double maxRange) {
		val startNode = new Node(startPos.getX(), startPos.getY(), startPos.getZ());
		
		val openQueue = new LinkedList<Node>();
		val queuedSet = new HashSet<Integer>();
		openQueue.add(startNode);
		queuedSet.add(startNode.hashCode());
		
		val nodeEvaluator = nodeEvaluatorSupplier.get();
		nodeEvaluator.prepare(region, mob);
		//? neoforge {
		/*startNode.type = nodeEvaluator.getPathType(new PathfindingContext(region, mob), startPos.getX(), startPos.getY(), startPos.getZ());
		*///?} forge {
		startNode.type = nodeEvaluator.getBlockPathType(region, startPos.getX(), startPos.getY(), startPos.getZ());
		//?}
		startNode.costMalus = startNode.type.getMalus();
		
		return new TraversalScannerState(
				nodeEvaluator, region, 
				testFunc, shouldEarlyStop,
				openQueue, queuedSet, new HashSet<Node>(),
				startPos, maxRange, new AtomicBoolean()
				);
	}
	
	public static TraversalScannerState step(TraversalScannerState state, int stepsToTake) {
		val nodeEvaluator = state.nodeEvaluator();
		val openQueue = state.openQueue();
		val queuedSet = state.queuedSet();
		val successSet = state.successSet();
		
		Node[] neighbors = new Node[32];
		for (int s=0;s<stepsToTake;s++) {
			if (state.isFinished()) {
				state.finalize();
				return state;
			}
			Node testNode = openQueue.pollLast();
			if (state.testFunc().test(testNode)) {
				successSet.add(testNode);
				if (state.shouldEarlyStop().test(testNode)) {
					state.openQueue().clear();
					return state;
				}
				
				int neighborCount = nodeEvaluator.getNeighbors(neighbors, testNode);
				for (int i=0;i<neighborCount;i++) {
					val neighbor = neighbors[i];
					if (queuedSet.contains(neighbor.hashCode())) { continue; }
					queuedSet.add(neighbor.hashCode());
					if (!neighbor.asBlockPos().closerThan(state.centerPos(), state.maxRange())) { continue; }
					openQueue.add(neighbor);
				}
			}
		}
		
		return state;
	}
	public static TraversalScannerState step(TraversalScannerState inState) {
		return step(inState, 1);
	}
	
	public static TraversalScannerState finish(TraversalScannerState inState) {
		return step(inState, Integer.MAX_VALUE);
	}
	public static TraversalScannerState run(TraversalScannerState inState) {
		return finish(inState);
	}
	public static TraversalScannerState run(
			PathNavigationRegion region, Supplier<NodeEvaluator> nodeEvaluatorSupplier, Mob mob, 
			Predicate<Node> testFunc, Predicate<Node> shouldEarlyStop,
			BlockPos startPos, double maxRange) {
		return finish(initState(region, nodeEvaluatorSupplier, mob, testFunc, shouldEarlyStop, startPos, maxRange));
	}
	
	public static record TraversalScannerState(
			NodeEvaluator nodeEvaluator, PathNavigationRegion region, 
			Predicate<Node> testFunc, Predicate<Node> shouldEarlyStop,
			LinkedList<Node> openQueue, HashSet<Integer> queuedSet, HashSet<Node> successSet, 
			BlockPos centerPos, double maxRange, AtomicBoolean finalized) {
		public void finalize() {
			if (finalized.getAndSet(true)) { return; }
			nodeEvaluator.done();
		}
		public boolean isFinished() {
			return finalized.get() || openQueue.isEmpty();
		}
	}
}
