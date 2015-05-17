package at.mse.walchhofer.smokee.security;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Vetoed;

import at.mse.walchhofer.smokee.api.security.IAuthTokenValue;
import at.mse.walchhofer.smokee.api.security.ITokenStorage;

@Vetoed
public class InMemoryTokenStorage implements ITokenStorage {

    Map<String, String> apiKeyStore = new ConcurrentHashMap<>();
    Map<String, char[]> userPassStore = new ConcurrentHashMap<>();
    Map<String, IAuthTokenValue> authTokeStore = new ConcurrentHashMap<>();

    public InMemoryTokenStorage() {
        Calendar now = Calendar.getInstance();
        apiKeyStore.put("652121f9-416a-4584-90ba-a4962d885160", "smoker");
        apiKeyStore.put("00793dfe-00d4-4aac-0013-00a8dcba2200", "arquillian");
        userPassStore.put("smoker", "smoker".toCharArray());
        userPassStore.put("arquillian", "arquillian".toCharArray());
        authTokeStore.put("652121f9-416a-4584-90ba-a4962d885160", new AuthTokenValue("smoker", Calendar.getInstance()));
        // abgelaufen
        Calendar vorgestern = now;
        vorgestern.add(Calendar.DAY_OF_YEAR, -2);
        authTokeStore.put("00793dfe-00d4-4aac-0013-00a8dcba2200", new AuthTokenValue("arquillian", vorgestern));

    }

    @Override
    public Map<String, String> getAPIKeyStorage() {
        return apiKeyStore;
    }

    @Override
    public Map<String, char[]> getUserStorage() {
        return userPassStore;
    }

    @Override
    public Map<String, IAuthTokenValue> getAuthTokenStorage() {
        return authTokeStore;
    }

}
