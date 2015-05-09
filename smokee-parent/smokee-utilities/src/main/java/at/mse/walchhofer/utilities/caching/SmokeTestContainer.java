package at.mse.walchhofer.utilities.caching;

import java.lang.reflect.Method;

import at.mse.walchhofer.smokee.api.caching.ISmokeTestResult;

public class SmokeTestContainer implements ITimeCacheElement<Integer>{
	
	private ISmokeTestResult result;
	private Method testMethod;
	private Boolean passed;
	private boolean valid;
	
	/**
	 * @return the result
	 */
	public ISmokeTestResult getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(ISmokeTestResult result) {
		this.result = result;
	}
	/**
	 * @return the testMethod
	 */
	public Method getTestMethod() {
		return testMethod;
	}
	/**
	 * @param testMethod the testMethod to set
	 */
	public void setTestMethod(Method testMethod) {
		this.testMethod = testMethod;
	}
	/**
	 * @return the passed
	 */
	public Boolean getPassed() {
		return passed;
	}
	/**
	 * @param passed the passed to set
	 */
	public void setPassed(Boolean passed) {
		this.passed = passed;
	}

	@Override
	public boolean isValid() {
		return this.valid;
	}
	@Override
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	@Override
	public Integer getKey() {
		return testMethod.hashCode();
	}
}
