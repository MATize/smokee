package at.mse.walchhofer.smokee.wildfly.caching;

import java.lang.reflect.Method;

import org.infinispan.Cache;

import at.mse.walchhofer.smokee.api.caching.ISmokeTestResult;
import at.mse.walchhofer.smokee.api.caching.ISmokEEJCache;

public class JCacheWrapper implements ISmokEEJCache {

    private Cache<Method, ISmokeTestResult> c;

    public JCacheWrapper(Cache<Method, ISmokeTestResult> cache) {
        c = cache;
    }

    @Override
    public ISmokeTestResult get(Method key) {
        return c.get(key);
    }

    @Override
    public ISmokeTestResult put(Method key, ISmokeTestResult value) {
        return c.put(key, value);
    }

    public void kill() {
        c.stop();
        this.c = null;
    }
}
