package at.mse.walchhofer.smokee.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.inject.Vetoed;

import at.mse.walchhofer.smokee.api.security.ITokenStorage;

@Vetoed
public class InMemoryTokenStorage implements ITokenStorage {

	Map<String, String> apiKeyStore = new ConcurrentHashMap<>();
	Map<String, char[]> userPassStore = new ConcurrentHashMap<>();
	Map<String, String> authTokeStore = new ConcurrentHashMap<>();

	public InMemoryTokenStorage() {
		apiKeyStore.put("652121f9-416a-4584-90ba-a4962d885160", "smoker");
		apiKeyStore.put("00793dfe-00d4-4aac-0013-00a8dcba2200", "arquillian");
		userPassStore.put("smoker", "smoker".toCharArray());
		userPassStore.put("arquillian", "arquillian".toCharArray());
		authTokeStore.put("652121f9-416a-4584-90ba-a4962d885160","smoker");
	}

	@Override
	public Map<String, String> getAPIKeyStorage() {
		return apiKeyStore;
	}

	@Override
	public Map<String, char[]> getUserStorate() {
		return userPassStore;
	}

	@Override
	public Map<String, String> getAuthTokenStorage() {
		return authTokeStore;
	}

}
