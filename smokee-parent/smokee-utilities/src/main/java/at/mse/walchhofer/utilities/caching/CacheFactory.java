package at.mse.walchhofer.utilities.caching;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CacheFactory {

	@SuppressWarnings("rawtypes")
	private static TimeFramedCache instance;
	private static boolean conigured = false;
	private static Long ttl;
	
	public static void setup(Long timeToLife) {
		CacheFactory.ttl = timeToLife;
		CacheFactory.conigured = true;
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V extends ITimeCacheElement<K>> TimeFramedCache<K,V> getInstance() throws IllegalStateException {
		if(CacheFactory.conigured && CacheFactory.instance != null) {
			return CacheFactory.instance;
		} else if(CacheFactory.instance == null){
			CacheFactory.instance = new TimeFramedCache<K,V>(CacheFactory.ttl);
			return CacheFactory.instance;
		} else {
			throw new IllegalStateException("Cache must have been initialized by calling setup(1337)!");
		}		
	}
}
