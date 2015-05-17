package at.mse.walchhofer.utilities.logging;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class LoggingFactory {

    @Produces
    @Log
    public Logger createLogger(InjectionPoint injectionPoint) {
        String name = injectionPoint.getMember().getDeclaringClass().getName();
        return Logger.getLogger(name);
    }

}
