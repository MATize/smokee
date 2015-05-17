package at.mse.walchhofer.smokee.api.security;

/**
 * 
 * Konstanten die vom SmokEE-Sicherheitssystem im HTTP Header verwendet werden
 * 
 * Alle HTTP Header werden mit X-* gekennzeichnet;<br />
 * Diese HTTP Header sollen niemals zum Standard durch IETF erklaert werden.<br />
 * Daher wird <a
 * href="http://tools.ietf.org/html/rfc6648">http://tools.ietf.org/
 * html/rfc6648</a> ignoriert.
 * 
 * @author <a href="mailto:matize@mount.at">Matthias Walchhofer</a>
 *
 */
public class Constants {

    /**
     * HTTP-HEADER fuer die API-Schluessel.<br />
     * Dieser Schluessel muss manuell erzeugt werden.<br/>
     * Jeder Aufrufer erhaelt einen eigenen API_KEY, ohne dem der Zugriff auf
     * SmokEE unterbinden wird.
     */
    public static final String HTTP_HEADER_API_KEY = "X-SMOKER-API-KEY";

    /**
     * HTTP-HEADER fuer den AUTH-Schluessel.<br/>
     * Dieser Schluessel wird automatisch erzeugt, wenn das Sicherheitssystem
     * mit einem korrekten API_KEY, Benuztername und Passwort aufgerufen wurde.<br />
     * Der AUTH_TOKEN ermoeglicht den Zugriff auf SmokEE zusammen mit API_KEY
     * ohne eine erneute Authentifzierung mit Benutzernamen und Passwort
     * durchfuehren zu muessen
     */
    public static final String HTTP_HEADER_AUTH_TOKEN = "X-SMOKER-AUTH-TOKEN";

    /**
     * HTTP-HEADER in dem Fehler von SmokEE retourniert werden.
     */
    public static final String HTTP_HEADER_SMOKER_ERROR = "X-SMOKER-ERROR";
}
