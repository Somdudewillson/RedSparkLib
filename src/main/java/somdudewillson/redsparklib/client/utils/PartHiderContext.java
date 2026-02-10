package somdudewillson.redsparklib.client.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import net.minecraft.client.model.geom.ModelPart;

public class PartHiderContext implements AutoCloseable {
	private static final Predicate<ModelPart> IS_LEAF_PREDICATE = getLeafPredicate();
	private final AtomicBoolean closed = new AtomicBoolean(false);
	private final ModelPart[] parts;
	private final boolean[] partOriginalVisibility;
	
	protected PartHiderContext(ModelPart[] partsToHide) {
		parts = partsToHide;
		partOriginalVisibility = new boolean[parts.length];
		hideParts();
	}
	
	private static Predicate<ModelPart> getLeafPredicate() {try {
        Field childrenField = ModelPart.class.getDeclaredField("children");
        childrenField.setAccessible(true);

        return (ModelPart part) -> {
            try {
                Map<?, ?> childrenMap = (Map<?, ?>) childrenField.get(part);
                return childrenMap == null || childrenMap.isEmpty();
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access 'children' field during predicate execution", e);
            }
        };
        
    } catch (NoSuchFieldException e) {
        throw new RuntimeException("Could not find field 'children' in ModelPart class", e);
    }
	}

	private void hideParts() {
		for (int i=0; i<parts.length; i++) {
			partOriginalVisibility[i] = parts[i].visible;
			parts[i].visible = false;
		}
	}

	public static PartHiderContext fromArray(ModelPart[] partArray) {
		return new PartHiderContext(partArray.clone());
	}
	public static PartHiderContext fromStream(Stream<ModelPart> partStream) {
		return new PartHiderContext(partStream.toArray(ModelPart[]::new));
	}
	public static PartHiderContext fromIterable(Iterable<ModelPart> partIterable) {
		return PartHiderContext.fromStream(StreamSupport.stream(partIterable.spliterator(), false));
	}
	public static PartHiderContext fromRootPart(ModelPart rootPart) {
		return PartHiderContext.fromStream(rootPart.getAllParts());
	}
	public static PartHiderContext fromModelWithRoot(IRootPartProvider rootPartProvider) {
		return PartHiderContext.fromRootPart(rootPartProvider.getRoot());
	}

	public static PartHiderContext fromStreamFiltered(Stream<ModelPart> partStream, Predicate<ModelPart> filter) {
		return fromStream(partStream.filter(filter));
	}
	public static PartHiderContext fromRootPartFiltered(ModelPart rootPart, Predicate<ModelPart> filter) {
		return PartHiderContext.fromStreamFiltered(rootPart.getAllParts(), filter);
	}
	public static PartHiderContext fromModelWithRootFiltered(IRootPartProvider rootPartProvider, Predicate<ModelPart> filter) {
		return PartHiderContext.fromRootPartFiltered(rootPartProvider.getRoot(), filter);
	}

	public static PartHiderContext fromRootPartExcluding(ModelPart rootPart, Set<ModelPart> excluded) {
		return PartHiderContext.fromStreamFiltered(rootPart.getAllParts(), IS_LEAF_PREDICATE.and(Predicate.not(excluded::contains)));
	}
	public static PartHiderContext fromRootPartExcluding(ModelPart rootPart, Stream<ModelPart> excluded) {
		return PartHiderContext.fromRootPartExcluding(rootPart, excluded.collect(Collectors.toUnmodifiableSet()));
	}
	public static PartHiderContext fromRootPartExcluding(ModelPart rootPart, ModelPart excluded) {
		return PartHiderContext.fromRootPartExcluding(rootPart, excluded.getAllParts().filter(IS_LEAF_PREDICATE));
	}
	public static PartHiderContext fromRootPartExcluding(ModelPart rootPart, ModelPart[] excluded) {
		return PartHiderContext.fromRootPartExcluding(rootPart, Arrays.stream(excluded).flatMap(ModelPart::getAllParts).filter(IS_LEAF_PREDICATE));
	}
	public static PartHiderContext fromModelWithRootFiltered(IRootPartProvider rootPartProvider, Set<ModelPart> excluded) {
		return PartHiderContext.fromRootPartExcluding(rootPartProvider.getRoot(), excluded);
	}
	public static PartHiderContext fromModelWithRootFiltered(IRootPartProvider rootPartProvider, Stream<ModelPart> excluded) {
		return PartHiderContext.fromRootPartExcluding(rootPartProvider.getRoot(), excluded);
	}
	public static PartHiderContext fromModelWithRootFiltered(IRootPartProvider rootPartProvider, ModelPart excluded) {
		return PartHiderContext.fromRootPartExcluding(rootPartProvider.getRoot(), excluded);
	}
	public static PartHiderContext fromModelWithRootFiltered(IRootPartProvider rootPartProvider, ModelPart[] excluded) {
		return PartHiderContext.fromRootPartExcluding(rootPartProvider.getRoot(), excluded);
	}
	
	public void wrap(Runnable r) {
		if (closed.get()) { throw new IllegalStateException("This context object has already been closed!"); }
		r.run();
		close();
	}

	@Override
	public void close() {
		if (closed.getAndSet(true)) { return; }
		for (int i=0; i<parts.length; i++) {
			parts[i].visible = partOriginalVisibility[i];
		}
	}

}
