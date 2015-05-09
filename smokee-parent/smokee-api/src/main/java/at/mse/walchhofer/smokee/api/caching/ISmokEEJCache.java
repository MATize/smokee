package at.mse.walchhofer.smokee.api.caching;

import java.lang.reflect.Method;

/**
 * <p>
 * Interface fuer den von SmokEE verwendeten Test-Cache.
 * Liefert zusammen mit {@link at.mse.walchhofer.smokee.api.SmokeCache} die Testcaching-Implementierung die durch dieses Interface entkoppelt ist.
 * </p>
 * <p>
 * Dieses Interface wird verwendet werden um eine eigene Cache-Implementierung zu realisieren.
 * </p>
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
public interface ISmokEEJCache {
	
	/**
	 * 
	 * Retourniert das Test-Resultat für die Methode oder {@code null} wenn die Methode nicht im Cache gespeichert ist.
	 * 
	 * @param key Die {@link java.lang.reflect.Method} deren gespeichertes Test-Resultat retourniert werden sollen
	 * @return {@link at.mse.walchhofer.smokee.api.caching.ISmokeTestResult}: Das Test-Resultat fuer die gegeben Methode oder {@code null} wenn die Methode nicht im Cache gespeichert ist
	 * @throws NullPointerException wenn die Methode {@code null} ist
	 */
	public ISmokeTestResult get(Method key);
	
	/**
	 * Assoziiert das uebergebene Test-Resultat {@link at.mse.walchhofer.smokee.api.caching.ISmokeTestResult} mit der zugehoertigen Methode {@link java.lang.reflect.Method}
	 * 
	 * @param key {@link java.lang.reflect.Method} Die Methode mit der das uebergebene Test-Resultat {@link at.mse.walchhofer.smokee.api.caching.ISmokeTestResult} assoziiert werden soll
	 * @param value {@link at.mse.walchhofer.smokee.api.caching.ISmokeTestResult} Das Test-Resultat das der uebergegebenen Methode {@link java.lang.reflect.Method} zugeordnet werden soll
	 * @return {@link at.mse.walchhofer.smokee.api.caching.ISmokeTestResult} das soeben uebergebene Testresultat <strong>value</strong>, das mit der Methode <strong>key</strong> assoziiert wurde.
	 */
	public ISmokeTestResult put(Method key, ISmokeTestResult value);

}
