package at.mse.walchhofer.smokee.api;

import java.lang.annotation.Documented;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

/**
 * 
 * Qualifier fuer den Smoke-Test-Cache
 * Wird von SmokeService aus dem smokee-core artifact verwendet um die Abhaengigkeit fuer
 * {@link at.mse.walchhofer.smokee.api.caching.ISmokEEJCache} aufzuloesen
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SmokeCache { }
