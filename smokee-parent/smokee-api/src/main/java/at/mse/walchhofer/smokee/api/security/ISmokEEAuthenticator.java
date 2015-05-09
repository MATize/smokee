package at.mse.walchhofer.smokee.api.security;

import javax.security.auth.login.LoginException;

/**
 * Interface fuer das Sicherheitssystem von SmokEE <br />
 * Dieses Interface kann verwendet werden um ein alternatives Sicherheitssystem fuer SmokEE zu realisieren.
 * 
 * Eine neue Implementierung muss lediglich diese Interface implementieren und mit {@link javax.enterprise.inject.Alternative} annotiert werden. 
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
public interface ISmokEEAuthenticator {

	/**
	 * 
	 * Diese Methode ueberprueft ob der gegeben Benutzername dem gegebenen API-Schluessel zugeordnet ist.<br />
	 * Ist der uebergebene Schluessel und das Benutzername korrekt, wird das uebergeben Passwort fuer den uebergebenen Benutzer verifiziert.
	 * Ist der API-Schluessel, der Benutzername und das Passwort korrekt, wird ein AUTH_TOKEN generiert ({@link java.util.UUID}), gespeichert und zurueckgegeben
	 * 
	 * @param apiKey Der API-Schluessel der dem uebergebenen Benutzer zugeordnet ist.
	 * @param user Der Benutzername
	 * @param pass Das Passwort
	 * @return den generierten AUTH_TOKEN der fuer die Authentfizierung ohne Benutzer und Passwort verwendet wird. 
	 * @throws LoginException
	 */
	public String login(String apiKey, String user, char[] pass) throws LoginException;
	
	/**
	 * Prueft ob der uebergeben apiKey bekannt ist.
	 * 
	 * @param apiKey
	 * @return {@code TRUE} wenn der apiKey bekannt ist<br />
	 * 		   {@code FALSE} wenn der apiKey nicht bekannt ist
	 */
	public boolean isValidApiKey(String apiKey);
	
	/**
	 * Prueft ob der uebergeben apiKey bekannt ist.<br />
	 * Prueft ob der uebergebene authToken bekannt ist <br />
	 * Ist der apiKey und der authToken bekannt, wird der Benutzer fuer den apiKey und den authToken ermittelt.<br />
	 * Stimmen der Benutzer des apiKeys und des authToken ueberein wird {@code TRUE} retourniert, sonst {@code FALSE}.
	 * 
	 * @param authToken der AUTH-Schluessel
	 * @param apiKey der API-Schluessel
	 * @return {@code TRUE} wenn authToken und apiKey gueltig und auf selben Benutzer referenzieren. <br />
	 * {@code FALSE} in allen anderen Faellen
	 */
	public boolean isValidAuthToken(String authToken, String apiKey);
}
