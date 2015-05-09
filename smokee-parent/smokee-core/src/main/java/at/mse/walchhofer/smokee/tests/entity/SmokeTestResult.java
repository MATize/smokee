package at.mse.walchhofer.smokee.tests.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import at.mse.walchhofer.smokee.api.caching.ISmokeTestResult;

@XmlRootElement
public class SmokeTestResult implements ISmokeTestResult {

	private static final long serialVersionUID = 1980182390147260894L;

	private long elapsedTime;
	private String testName;
	private boolean passed;
	private String applicationContext;
	private Date lastRun;
	
	public Long getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(Long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public Boolean getPassed() {
		return passed;
	}
	public void setPassed(Boolean passed) {
		this.passed = passed;
	}
	public void setApplicationContext(String context) {
		this.applicationContext = context;
	}
	
	public String getApplicationContext() {
		return applicationContext;
	}
	/**
	 * @return the lastrun
	 */
	public Date getLastRun() {
		return lastRun;
	}
	/**
	 * @param lastrun the lastrun to set
	 */
	public void setLastRun(Date lastRun) {
		this.lastRun = lastRun;
	}

}
