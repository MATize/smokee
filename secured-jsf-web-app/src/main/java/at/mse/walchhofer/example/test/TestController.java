package at.mse.walchhofer.example.test;

import javax.ejb.EJB;

import at.mse.walchhofer.example.services.TestBean;
import at.mse.walchhofer.smokee.api.SmokeValue;
import at.mse.walchhofer.smokee.api.SmokeValue.SmokeValueType;
import at.mse.walchhofer.smokee.api.SmokeTest;

public class TestController {

	@EJB
	TestBean bean;

	@SmokeTest(parameters = {
			@SmokeValue(type = SmokeValueType.STRING, value = "Teststring"),
			@SmokeValue(type = SmokeValueType.BOOLEAN, value = "true") })
	private boolean testBooleanWithParam(String testValue, Boolean testFlag) {
		return testFlag;
	}

	@SmokeTest(expectedResult =
			@SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"),
		parameters = {
			@SmokeValue(type = SmokeValueType.LONG, value = "-1")})
	private boolean loescheBenutzerById(Long id) {
		return bean.loescheBenutzerById(id);
	}

	@SmokeTest()
	// wird erstelleBenutzer_Test benannt
	private void erstelleBenutzer() {
	}

	@SmokeTest(name = "createUserTest")
	// wird createUserTest benannt
	private void erstelleBenutzer2() {
	}

	@SmokeTest(expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"))
	private boolean loescheBenutzer() {
		// Loesch-Logik
		return true;
	}

	@SmokeTest(name = "nestedStateless", expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"), rollback = true, enabled = true)
	private boolean testNestedStateless() {
		try {
			bean.testStatelessNested();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@SmokeTest(name = "testNestedStatelessNewTransaction",
			expectedResult =
				@SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"),
			rollback = true)
	private boolean testNestedStatelessNewTransaction() {
		try {
			bean.testStatelessNestedNew();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	
	@SmokeTest(name = "disabledTest", rollback=true, enabled=false)
	private void disabledTestMethod() {
		throw new RuntimeException("This exception shall never be thrown!");
	}

	@SmokeTest(name = "testNestedStatefullTransaction", expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"), rollback = true, enabled = true)
	private boolean testNestedStatefulTransaction() {
		try {
			bean.testStatefulNested();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@SmokeTest(name = "testNestedStatelessNewTransaction", expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"), rollback = true, enabled = true)
	private boolean testNestedStatefulNewTransaction() {
		try {
			bean.testStatefulNestedNew();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@SmokeTest(name = "testNestedSingletonTransaction", expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"), rollback = true, enabled = true)
	private boolean testNestedSingletonTransaction() {
		try {
			bean.testSingletonNested();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	@SmokeTest(name = "testNestedSingletonNewTransaction", expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"), rollback = true, enabled = true)
	private boolean testNestedSingletonNewTransaction() {
		try {
			bean.testSingletonNestedNew();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
