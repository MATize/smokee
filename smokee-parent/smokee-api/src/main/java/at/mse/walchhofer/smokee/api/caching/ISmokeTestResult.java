package at.mse.walchhofer.smokee.api.caching;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Das Interface um den Zugriff auf die Test-Resultate herzustellen.
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
public interface ISmokeTestResult extends Serializable {
	
	/**
	 * 
	 * @return Die Ausfuehrungszeit des Tests in Millisekunden
	 */
	public Long getElapsedTime();
	
	/**
	 * 
	 * @return Den Namen des Tests
	 */
	public String getTestName();
	
	/**
	 * 
	 * @return {@link java.lang.Boolean}-Wert des Testerfolgs.<br />
	 * {@code TRUE}: wenn Testergebnis dem erwarteten Testresultat entspricht<br />
	 * {@code FALSE}: wenn nicht dem erwarteten Testresultat entspricht
	 */
	public Boolean getPassed();
	
	/**
	 * Der Testcontext<br />
	 * Dieser Wert wird ueber Eigenschaft {@code project.jndi.prefix} die in der die Datei {@code META-INF/smoker.properties} definiert wird, festgelegt. 
	 * @return Der Kontext in dem der Test ausgefuehrt wurde.<br />
	 * 
	 */
	public String getApplicationContext();
	
	/**
	 * Der Testcontext<br />
	 * Dieser Wert wird Standardmaeszig ueber die Eigenschaft {@code project.jndi.prefix} die in der die Datei {@code META-INF/smoker.properties} definiert wird, festgelegt. 
	 * 
	 */
	public void setApplicationContext(String ctx);
	
	/**
	 * 
	 * @return Timestamp wann der Test zuletzt ausgefuehrt wurde
	 */
	public Date getLastRun();
	
	
	/**
	 * Festlegen des Testnamens
	 * @param testName der Testname
	 */
	public void setTestName(String testName);

	/**
	 * Festlegen wann der Test zuletzt ausgefuehrt wurde
	 * @param lastRun Zeitpuntk der letzten Testausfuehrung
	 */
	public void setLastRun(Date lastRun);

	/**
	 * Festlegen der benoetigen Testausfuehrungsdauer
	 * @param elapsedTime die benoetigte Zeit der Testausfuehrung
	 */
	public void setElapsedTime(Long elapsedTime);

	/**
	 * Festlegen des Testerfolgs
	 * 
	 * @param passed Information ob der Test das definierte erwartete Ergebnis retourniert hat 
	 */
	public void setPassed(Boolean passed);
}

