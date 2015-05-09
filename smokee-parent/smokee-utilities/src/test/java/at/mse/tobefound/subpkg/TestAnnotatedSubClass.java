package at.mse.tobefound.subpkg;

import at.mse.walchhofer.smokee.api.SmokeTest;

public class TestAnnotatedSubClass {
	
	
	public void testA(){
		
	}
	
	@SmokeTest
	void testB() {
		
	}
	
	@SmokeTest
	protected void testC() {
		//no annotation present
	}
	
	@SmokeTest
	protected void testD() {
		//no annotation present
	}
	
	@SmokeTest
	protected void testE() {
		//no annotation present
	}
	
	
	protected void testF() {
		//no annotation present
	}
	
	
	protected void testG() {
		//no annotation present
	}

}
