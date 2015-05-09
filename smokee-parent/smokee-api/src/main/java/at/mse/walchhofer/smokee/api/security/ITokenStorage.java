package at.mse.walchhofer.smokee.api.security;

import java.util.Map;

/**
 * Interface fuer die Verwahrung der Sicherheitsschluesseln von SmokEE internen Sicherheitssystem 
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
public interface ITokenStorage {

	/**
	 * Zugriff auf die API-Schluesseln
	 * @return eine Map mit API-KEY als <strong>key</strong> und Benutzername als <strong>value</strong> 
	 */
	public Map<String, String> getAPIKeyStorage();
	
	/**
	 * 
	 * 
	 * @return eine Map mit Benutzername als <strong>key</strong> und Passwort als <strong>value</strong>
	 */
	public Map<String, char[]> getUserStorate();
	
	/**
	 * 
	 * @return eine Map mit AUTH-Token als <strong>key</strong> und Benutzernamen als <strong>value</strong>
	 */
	public Map<String, String> getAuthTokenStorage();
	
}
