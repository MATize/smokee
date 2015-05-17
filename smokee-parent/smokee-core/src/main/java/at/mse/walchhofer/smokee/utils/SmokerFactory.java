package at.mse.walchhofer.smokee.utils;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.naming.InitialContext;

import at.mse.walchhofer.smokee.api.SmokerStatus;

public class SmokerFactory {

    @Inject
    PropertyUtils propertyUtils;

    @Produces
    @SmokerStatus
    Boolean isSmokerEnabled() {
        try {
            javax.naming.Context ctx = new InitialContext();
            String boolString = (String) ctx.lookup(String.format(
                    "%s/smoker/enabled", propertyUtils.getJndiPrefixFromProperties()));
            return new Boolean(boolString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // @Produces
    // @TestCache
    // TimeFramedCache<Integer, SmokeTestContainer> getCacheInstance() {
    // CacheFactory.setup(10000L);
    // try {
    // return CacheFactory.getInstance();
    // } catch (IllegalStateException e) {
    // return null;
    // }
    // }
}
