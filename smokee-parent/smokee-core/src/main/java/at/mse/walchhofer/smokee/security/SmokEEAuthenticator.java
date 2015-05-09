package at.mse.walchhofer.smokee.security;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;

import at.mse.walchhofer.smokee.api.security.ISmokEEAuthenticator;
import at.mse.walchhofer.smokee.api.security.ITokenStorage;
import at.mse.walchhofer.utilities.logging.Log;

@ApplicationScoped
public class SmokEEAuthenticator implements ISmokEEAuthenticator {

	@Inject
	@Log
	Logger log;

	@Inject
	ITokenStorage tokenStorage;

	public SmokEEAuthenticator() {
	}

	@Override
	public String login(String apiKey, String user, char[] pass)
			throws LoginException {
		log.info("login request with apiKey=" + apiKey + ", user=" + user);
		if (tokenStorage.getAPIKeyStorage().containsKey(apiKey)) {
			if (tokenStorage.getAPIKeyStorage().get(apiKey).equals(user)) {
				if (tokenStorage.getUserStorate().containsKey(user)) {
					if (Arrays.equals(tokenStorage.getUserStorate().get(user),
							pass)) {
						// purge password
						Arrays.fill(pass, '\0');

						String token = UUID.randomUUID().toString();
						// Kollisionen umgehen
						while (tokenStorage.getAuthTokenStorage().containsKey(
								token)) {
							token = UUID.randomUUID().toString();
						}
						tokenStorage.getAuthTokenStorage().put(token, user);
						return token;
					}
				}
			}
		}
		log.warning("login request with apiKey=" + apiKey + ", user=" + user
				+ " failed!");
		throw new LoginException();
	}

	@Override
	public boolean isValidApiKey(String apiKey) {
		log.info("isValidApiKey request with apiKey=" + apiKey);
		return apiKey != null
				&& tokenStorage.getAPIKeyStorage().containsKey(apiKey);
	}

	@Override
	public boolean isValidAuthToken(String authToken, String apiKey) {
		log.info("isValidAuthToken request with authToken=" + authToken);
		log.info("isValidAuthToken request with apiKey=" + apiKey);
		if (authToken != null && apiKey != null
				&& tokenStorage.getAuthTokenStorage().containsKey(authToken)
				&& tokenStorage.getAPIKeyStorage().containsKey(apiKey)) {
			String apiUser = tokenStorage.getAPIKeyStorage().get(apiKey);
			String tokenUser = tokenStorage.getAuthTokenStorage()
					.get(authToken);
			return apiKey != null && tokenUser != null
					&& apiUser.equals(tokenUser);
		}
		return false;
	}
}
