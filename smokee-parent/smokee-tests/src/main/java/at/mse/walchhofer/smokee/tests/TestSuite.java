package at.mse.walchhofer.smokee.tests;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;


@Singleton
@Startup
public class TestSuite {

	@PostConstruct
	public void setup() {
		//scan classes
	}
}
