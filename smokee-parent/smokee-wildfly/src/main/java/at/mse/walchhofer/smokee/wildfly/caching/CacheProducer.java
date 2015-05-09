package at.mse.walchhofer.smokee.wildfly.caching;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

import at.mse.walchhofer.smokee.api.SmokeCache;

@Singleton
@Startup
public class CacheProducer {

	public static Long CACHE_TTL = 300 * 1000L;

	private EmbeddedCacheManager defaultCacheManager;
	private JCacheWrapper wrapper;

	@Produces
	@SmokeCache
	public JCacheWrapper getCache() {
		if (defaultCacheManager == null) {
			defaultCacheManager = new DefaultCacheManager(
					new ConfigurationBuilder().expiration().lifespan(CACHE_TTL)
							.build());
		}
		if (wrapper == null) {
			wrapper = new JCacheWrapper(defaultCacheManager.getCache(
					"smokeTestResults", true));
		}
		return wrapper;
	}

	@PostConstruct
	public void init() {
		defaultCacheManager = new DefaultCacheManager(
				new ConfigurationBuilder().expiration().lifespan(CACHE_TTL)
						.build());
		wrapper = new JCacheWrapper(defaultCacheManager.getCache(
				"smokeTestResults", true));
	}

	@PreDestroy
	public void destruct() {
		wrapper.kill();
		defaultCacheManager.stop();
	}
}
