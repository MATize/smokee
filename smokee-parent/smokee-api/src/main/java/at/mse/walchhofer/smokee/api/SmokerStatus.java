package at.mse.walchhofer.smokee.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * 
 * Qualifier fuer den Status von SmokEE<br />
 * Der SmokerStatus gibt informiert ob SmokEE aktiviert ist.
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier
public @interface SmokerStatus {
}
