package at.mse.walchhofer.smokee;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class StartupHook {

    @PostConstruct
    public void setup() {
        System.out.println("Applikation started!");
    }
}
