package somdudewillson.redsparklib.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

public class LimitedLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    @java.io.Serial
    private static final long serialVersionUID = 8960375136291704122L;
    
    @Getter
    private final int maxCapacity;

    /**
     * Constructs an empty insertion-ordered {@code LimitedLinkedHashMap} instance
     * with the specified initial capacity, max capacity, and load factor.
     *
     * @param  initialCapacity the initial capacity
     * @param  initialCapacity the max capacity
     * @param  loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the max capacity is nonpositive or max capacity < initial capacity
     *         or the load factor is nonpositive
     */
    public LimitedLinkedHashMap(int initialCapacity, int maxCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        if (maxCapacity < 1) {
        	throw new IllegalArgumentException("Max capacity must be > 0!");
        }
        if (maxCapacity < initialCapacity) {
        	throw new IllegalArgumentException("Max capacity must be >= initial capacity!");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * Constructs an empty insertion-ordered {@code LimitedLinkedHashMap} instance
     * with the specified initial capacity and max capacity, and a default load factor (0.75).
     *
     * @param  initialCapacity the initial capacity
     * @param  initialCapacity the max capacity
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the max capacity is nonpositive or max capacity < initial capacity
     */
    public LimitedLinkedHashMap(int initialCapacity, int maxCapacity) {
        super(initialCapacity);
        if (maxCapacity < 1) {
        	throw new IllegalArgumentException("Max capacity must be > 0!");
        }
        if (maxCapacity < initialCapacity) {
        	throw new IllegalArgumentException("Max capacity must be >= initial capacity!");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * Constructs an empty insertion-ordered {@code LimitedLinkedHashMap} instance
     * with the specified max capacity, an initial capacity sufficient to fit the max capacity, and the default load factor (0.75).
     * 
     * @param  initialCapacity the max capacity
     * @throws IllegalArgumentException if the max capacity is nonpositive
     */
    public LimitedLinkedHashMap(int maxCapacity) {
        super(maxCapacity);
        if (maxCapacity < 1) {
        	throw new IllegalArgumentException("Max capacity must be > 0!");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * Constructs an insertion-ordered {@code LimitedLinkedHashMap} instance with
     * the same mappings as the specified map.  The {@code LimitedLinkedHashMap}
     * instance is created with a default load factor (0.75), an initial
     * capacity sufficient to hold the mappings in the specified map,
     * and a max capacity equal to the size of the specified map.
     *
     * @param  m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public LimitedLinkedHashMap(Map<? extends K, ? extends V> m) {
        super(m);
        this.maxCapacity = m.size();
    }

    /**
     * Constructs an insertion-ordered {@code LimitedLinkedHashMap} instance with
     * the same mappings as the specified map.  The {@code LimitedLinkedHashMap}
     * instance is created with a default load factor (0.75), an initial
     * capacity sufficient to hold the mappings in the specified map,
     * and the specified max capacity.
     *
     * @param  m the map whose mappings are to be placed in this map
     * @param  initialCapacity the max capacity
     * @throws NullPointerException if the specified map is null
     * @throws IllegalArgumentException if the max capacity is nonpositive or max capacity < source map size
     */
    public LimitedLinkedHashMap(Map<? extends K, ? extends V> m, int maxCapacity) {
        super(m);
        if (maxCapacity < 1) {
        	throw new IllegalArgumentException("Max capacity must be > 0!");
        }
        if (maxCapacity < m.size()) {
        	throw new IllegalArgumentException("Max capacity must be >= source map size!");
        }
        this.maxCapacity = maxCapacity;
    }

    /**
     * Constructs an empty {@code LimitedLinkedHashMap} instance with the
     * specified initial capacity, load factor and ordering mode.
     *
     * @param  initialCapacity the initial capacity
     * @param  initialCapacity the max capacity
     * @param  loadFactor      the load factor
     * @param  accessOrder     the ordering mode - {@code true} for
     *         access-order, {@code false} for insertion-order
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the max capacity is nonpositive or max capacity < initial capacity
     *         or the load factor is nonpositive
     */
    public LimitedLinkedHashMap(int initialCapacity, 
    		int maxCapacity,
    		float loadFactor,
    		boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
        if (maxCapacity < 1) {
        	throw new IllegalArgumentException("Max capacity must be > 0!");
        }
        if (maxCapacity < initialCapacity) {
        	throw new IllegalArgumentException("Max capacity must be >= initial capacity!");
        }
        this.maxCapacity = maxCapacity;
    }    

    @Override protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > maxCapacity;
    }
}
