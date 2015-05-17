package at.mse.walchhofer.utilities.caching;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TimeFramedCache<K, V extends ITimeCacheElement<K>> {

    private static final Long DEF_TTL = 300000L;

    private Long timeToLive;
    private ConcurrentHashMap<K, TimeFramedElement<V>> store;

    protected TimeFramedCache() {
        this(DEF_TTL); // 5 minutes default cache time
    }

    protected TimeFramedCache(Long timeToLive) {
        this.timeToLive = timeToLive;
        this.store = new ConcurrentHashMap<K, TimeFramedElement<V>>();
    }

    public int size() {
        return this.store.size();
    }

    public Boolean hasElements() {
        return this.store.size() > 0;
    }

    public void put(K key, V value) {
        TimeFramedElement<V> tfelem = new TimeFramedElement<V>();
        value.setValid(true);
        tfelem.setElement(value);
        tfelem.setTimestamp(System.currentTimeMillis());
        this.store.put(key, tfelem);
    }

    public void put(V value) {
        this.put(value.getKey(), value);
    }

    public V get(K key) {
        if (!this.store.containsKey(key)) {
            return null;
        } else {
            TimeFramedElement<V> elem = this.store.get(key);
            return getWithTimeCheck(elem);
        }
    }

    public Collection<V> values() {
        Collection<V> ret = new ArrayList<>();
        for (TimeFramedElement<V> elem : this.store.values()) {
            V value = getWithTimeCheck(elem);
            if (value != null) {
                ret.add(value);
            }
        }
        return ret;
    }

    private V getWithTimeCheck(TimeFramedElement<V> elem) {
        if (elem.getTimestamp() + this.timeToLive > System.currentTimeMillis()) {
            return elem.getElement();
        } else {
            V element = elem.getElement();
            element.setValid(false);
            return element;
        }
    }

    /**
     * @return the timeToLive
     */
    protected Long getTimeToLive() {
        return timeToLive;
    }

    /**
     * @param ttl
     *            the timeToLive to set
     */
    protected void setTimeToLive(Long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void update(V value) {
        TimeFramedElement<V> tfelem = this.store.get(value.getKey());
        value.setValid(true);
        tfelem.setElement(value);
        this.store.put(value.getKey(), tfelem);
    }

}
