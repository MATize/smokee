package at.mse.walchhofer.smokee.security.boundary;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.security.auth.login.LoginException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.mse.walchhofer.smokee.api.security.Constants;
import at.mse.walchhofer.smokee.api.security.ISmokEEAuthenticator;

@RequestScoped
@Produces({ MediaType.APPLICATION_JSON })
@Path("/login")
public class LoginResource {

	@Inject
	ISmokEEAuthenticator authenticator;

	@POST
	public Response login(@Context HttpHeaders headers,
			@FormParam("user") String user, @FormParam("pass") String pass) {
		List<String> apiKeys = headers
				.getRequestHeader(Constants.HTTP_HEADER_API_KEY);
		if (apiKeys.size() != 1) {
			return Response
					.status(Status.BAD_REQUEST)
					.header(Constants.HTTP_HEADER_SMOKER_ERROR,
							"Header " + Constants.HTTP_HEADER_API_KEY
									+ " missing or invalid!").build();
		} else {
			String apiKey = apiKeys.get(0);
			try {
				String token = authenticator.login(apiKey, user,
						pass.toCharArray());
				JsonObject obj = Json.createObjectBuilder()
						.add("auth_token", token).build();
				return Response.status(Status.OK).entity(obj).build();
			} catch (LoginException ex) {
				return Response
						.status(Status.UNAUTHORIZED)
						.entity(Json
								.createObjectBuilder()
								.add("message",
										"Login failed! Check your apikey, username and password!")
								.build()).build();
			}
		}
	}
}
