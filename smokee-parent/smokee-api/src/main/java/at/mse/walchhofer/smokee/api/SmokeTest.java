package at.mse.walchhofer.smokee.api;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation um Methode als SmokEE-Test zu markieren.
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD })
public @interface SmokeTest {

	/**
	 * <p>
	 * Dient als Default-Platzhalter für den Testnamen
	 * </p>
	 * @see {@link SmokeTest#name()}
	 */
	public final static String DEF_NAME = "USE_METHOD_NAME";

	/**
	 * <p>
	 * Der Name des Testfall
	 * </p>
	 * <p>
	 * Wird dieses Attribute nicht angegeben, erhaelt <code>name()</code> den
	 * Wert {@value #DEF_NAME} aus dem static Field {@link SmokeTest#DEF_NAME}
	 * uebernommen wird. <br />
	 * Dies bewirkt, dass der Originalname der Testmethode als Testname
	 * uebernommen wird.
	 * </p>
	 * <p>
	 * <strong>Beispiel:</strong><br />
	 * 
	 * <pre>
	 * <code>
	 * {@literal @}SmokeTest(name="testMethod1")
	 * private boolean testMethod() {}
	 * </code>
	 * </pre>
	 * <p>
	 */
	public String name() default DEF_NAME;

	/**
	 * <p>
	 * Eine Liste von Parametern vom Typ {@link SmokeParam}
	 * </p>
	 * <p>
	 * Die Parameter werden beim Durchfuehren des Tests der annotierten Methode
	 * uebergeben. <br />
	 * Hierfuehr muss die Reihenfolge der uebergebenen Parameter sowie die
	 * Datentypen der Parameter mit denen der Methode uebereinstimmen.
	 * </p>
	 * <p>
	 * <strong>Beispiel:</strong><br />
	 * 
	 * <pre>
	 *  <code>
	 *   {@literal @}SmokeTest(name="testMethod1", parameters = {
	 *    {@literal @}SmokeParam(
	 *     type = {@link SmokeParam.SmokeParamType#STRING},
	 *     value = "Teststring"),
	 *    {@literal @}SmokeParam(
	 *     type = {@link SmokeParam.SmokeParamType#BOOLEAN},
	 *     value = "true") })
	 *   private boolean testMethod(String testValue, Boolean testFlag) {}
	 * 	</code>
	 * </pre>
	 * 
	 * </p>
	 */
	public SmokeParam[] parameters() default {};
	
	/**
	 * <p>
	 * Definiert den erwarteten Rueckgabewert des Testaufrufes
	 * </p>
	 * <p>
	 * Hierfuer wird der Parameter vom Typ {@link SmokeParam} verwendet.</br>
	 * Soll eine Methode ohne Rueckgabe-Parameter getestet werden, kann
	 * {@link SmokeParam.SmokeParamType#VOID} verwendet werden.
	 * </p>
	 * <p>
	 * <strong>Beispiel:</strong><br />
	 * 
	 * <pre>
	 *  <code>
	 *   {@literal @}SmokeTest(expectedReturn=
	 *    {@literal @}SmokeParam(
	 *     type = {@link SmokeParam.SmokeParamType#BOOLEAN},
	 *     value = "true"))
	 *   private boolean testMethod() {}
	 *  </code>
	 * </pre>
	 * 
	 * </p>
	 */
	public SmokeParam expectedReturn() default @SmokeParam();

	/**
	 * <p>
	 * Definiert, dass alle von diesem Test betroffenen Transaktionen
	 * zurueckgerollt werden sollen.<br />
	 * Dadurch kann vermieden werden, dass Datenstaende durch den Testdurchlaeuf
	 * veraendert werden.
	 * </p>
	 * <p>
	 * <strong>Beispiel:</strong><br />
	 * 
	 * <pre>
	 *  <code>
	 *   {@literal @}SmokeTest(rollback = true)
	 *   private boolean testMethodTransactional() {}
	 *  </code>
	 * </pre>
	 * 
	 * </p>
	 * 
	 */
	public boolean rollback() default false;

	/**
	 * <p>
	 * Erlaubt das Deaktiveren eines Tests.
	 * </p>
	 * <p>
	 * <strong>Beispiel:</strong>
	 * 
	 * <pre>
	 *  <code>
	 *   {@literal @}SmokeTest(enabled = false)
	 *   private boolean testMethodDeaktiviert() {}
	 *  </code>
	 * </pre>
	 * 
	 * </p>
	 * 
	 */
	public boolean enabled() default true;
}