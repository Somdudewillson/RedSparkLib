package somdudewillson.redsparklib.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.Getter;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import somdudewillson.redsparklib.common.data.loot.RedSparkLibLootTableProvider;
import somdudewillson.redsparklib.common.data.task.SimpleDatagenTask;

@RequiredArgsConstructor
public class RedSparkLibSimpleDatagenManager {
	private static final LoadingCache<String, RedSparkLibSimpleDatagenManager> MOD_DATA_MANAGERS = CacheBuilder.newBuilder()
			.build(CacheLoader.from(RedSparkLibSimpleDatagenManager::new));
    
	@Getter
	private final String modid;
	private final ReentrantLock queueLock = new ReentrantLock();
	private final List<SimpleDatagenTask<?,?>> queuedDatagen = new ArrayList<>();
	private final AtomicInteger runDatagens = new AtomicInteger();
	
	@Locked("queueLock")
	public boolean queueDatagen(SimpleDatagenTask<?,?> datagenTask) {
		if (runDatagens.get() > 0) {
			throw new IllegalStateException("Attempted to queue a datagen task after datagen has run!");
		}
		return queuedDatagen.add(datagenTask);
	}
	@Locked("queueLock")
	public boolean queueDatagen(Iterable<SimpleDatagenTask<?,?>> datagenTasks) {
		boolean changed = false;
		for (SimpleDatagenTask<?,?> datagenTask : datagenTasks) {
			changed |= queueDatagen(datagenTask);
		}
		
		return changed;
	}
	public static boolean queueDatagen(String modid, SimpleDatagenTask<?,?> datagenTask) {
		try {
			val manager = MOD_DATA_MANAGERS.get(modid);
			return manager.queueDatagen(datagenTask);
		} catch (ExecutionException e) { }
		
		return false;
	}
	public static boolean queueDatagen(String modid, Iterable<SimpleDatagenTask<?,?>> datagenTasks) {
		try {
			val manager = MOD_DATA_MANAGERS.get(modid);
			return manager.queueDatagen(datagenTasks);
		} catch (ExecutionException e) { }
		
		return false;
	}

	@SuppressWarnings("unchecked")
	@Locked("queueLock")
	public <R, P> List<SimpleDatagenTask<R,P>> getFilteredDatagenTasks(Class<P> targetProviderClass) {
		return queuedDatagen.stream()
				.filter(SimpleDatagenTask.classCompatibleFilter(targetProviderClass))
				.map(t -> (SimpleDatagenTask<R,P>)t)
				.toList();
	}
	
	public void markDatagenRun() {
		runDatagens.incrementAndGet();
	}
	
	public void registerDataGenerators(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper fileHelper = evt.getExistingFileHelper();
		RedSparkLibBlockTagProvider blockTagProvider = new RedSparkLibBlockTagProvider(this, output, evt.getLookupProvider(), modid, fileHelper);
		
		generator.addProvider(evt.includeServer(), blockTagProvider);
		
		generator.addProvider(evt.includeServer(), new RedSparkLibItemModelProvider(this, output, modid, fileHelper));
		generator.addProvider(evt.includeServer(), new RedSparkLibBlockStateProvider(this, output, modid, fileHelper));
		
		generator.addProvider(evt.includeServer(), new RedSparkLibLootTableProvider(this, output));
	}
	public static RedSparkLibSimpleDatagenManager registerDataGenerators(String modid, GatherDataEvent evt) {
		RedSparkLibSimpleDatagenManager manager = null;
		try {
			manager = MOD_DATA_MANAGERS.get(modid);
			manager.registerDataGenerators(evt);
		} catch (ExecutionException e) { }
		
		return manager;
	}
}
