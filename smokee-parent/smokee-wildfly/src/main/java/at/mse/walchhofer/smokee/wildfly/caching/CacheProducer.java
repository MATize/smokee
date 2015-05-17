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

    private EmbeddedCacheManager defCacheManager;
    private JCacheWrapper wrapper;

    @Produces
    @SmokeCache
    public JCacheWrapper getCache() {
        init();
        return wrapper;
    }

    @PostConstruct
    public void init() {
        if (defCacheManager == null) {
            defCacheManager = new DefaultCacheManager(
                    new ConfigurationBuilder().expiration().lifespan(CACHE_TTL)
                            .build());
        }
        if (wrapper == null) {
            wrapper = new JCacheWrapper(defCacheManager.getCache(
                    "smokeTestResults", true));
        }
    }

    @PreDestroy
    public void destruct() {
        wrapper.kill();
        defCacheManager.stop();
    }
}
