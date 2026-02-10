package somdudewillson.redsparklib.common.utils;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.collect.EvictingQueue;

import lombok.val;
import net.minecraft.client.renderer.texture.Tickable;

public class MultiTickCalculatedValue<C,S,V> implements Tickable {
	private final AtomicReference<V> latestValue = new AtomicReference<>();
	private final AtomicLong valueAge = new AtomicLong();
	private final AtomicLong valueTimeoutAge = new AtomicLong(20);
	private C stateInitContext = null;
	private S latestState = null;
	private int currentCalcTime = 0;
	private final EvictingQueue<Integer> pastCalcTimes = EvictingQueue.create(10);
	private int calcTimeEst = 0;
	
	private final Function<C, S> stateInitFunc;
	private final Function<S, Optional<V>> valueCalcFunction;
	
	public MultiTickCalculatedValue(@Nullable C stateInitContext, Function<C, S> stateInitFunc, Function<S, Optional<V>> valueCalcFunction, long timeoutAge) {
		this.stateInitContext = stateInitContext;
		this.stateInitFunc = stateInitFunc;
		this.valueCalcFunction = valueCalcFunction;
		
		this.valueTimeoutAge.set(timeoutAge);
	}
	public MultiTickCalculatedValue(@Nullable C stateInitContext, Function<C, S> stateInitFunc, Function<S, Optional<V>> valueCalcFunction) {
		this(stateInitContext, stateInitFunc, valueCalcFunction, 20);
	}
	
	
	public boolean hasValue() {
		return latestValue.get() != null;
	}
	
	@Nullable
	public V getValue() {
		return latestValue.get();
	}
	public Optional<V> tryGetValue() {
		return Optional.ofNullable(getValue());
	}
	
	public void setTimeoutAge(long timeout) {
		valueTimeoutAge.set(timeout);
	}

	public void setStateInitContext(@Nullable C stateInitContext, BiPredicate<C,C> equalityTest, boolean invalidateCurrentValue) {
		this.stateInitContext = stateInitContext;
		if (equalityTest.test(this.stateInitContext, stateInitContext)) { return; }
		
		if (invalidateCurrentValue) {
			latestState = null;
			latestValue.set(null);
		}
	}
	public void setStateInitContext(C stateInitContext, boolean invalidateCurrentValue) {
		setStateInitContext(stateInitContext, Objects::equals, invalidateCurrentValue);
	}
	@Nullable
	public C getStateInitContext() {
		return this.stateInitContext;
	}

	@Override
	public void tick() {
		valueAge.incrementAndGet();
		if (stateInitContext != null && latestState == null && (latestValue.get()==null || valueAge.get()+calcTimeEst>=valueTimeoutAge.get())) {
			latestState = stateInitFunc.apply(stateInitContext);
			currentCalcTime = 0;
		}
		if (latestState != null) {
			currentCalcTime++;
			val potentialResult = valueCalcFunction.apply(latestState);
			if (potentialResult.isPresent()) {
				latestValue.set(potentialResult.get());
				latestState = null;
				valueAge.set(0);
				
				pastCalcTimes.add(currentCalcTime);
				calcTimeEst = (int) Math.round(pastCalcTimes.stream().mapToInt(Integer::valueOf).average().orElse(0));
			}
		}
	}
}
