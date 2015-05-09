package at.mse.walchhofer.smokee.security;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import at.mse.walchhofer.smokee.api.security.Constants;
import at.mse.walchhofer.smokee.api.security.ISmokEEAuthenticator;
import at.mse.walchhofer.utilities.logging.Log;

@Provider
@PreMatching
public class SecurityRequestFilter implements ContainerRequestFilter {
	
	@Inject
	@Log
	Logger log;
	
	@Inject
	ISmokEEAuthenticator smokeeAuthenticator;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		if(requestContext.getMethod().equals("OPTIONS")) {
			requestContext.abortWith( Response.status( Response.Status.OK ).build() );
			return;
		}
		
		String apiKey = requestContext.getHeaderString(Constants.HTTP_HEADER_API_KEY );

		if(apiKey == null) {
			apiKey = requestContext.getUriInfo().getQueryParameters().getFirst("apikey");
		}
		if ( !smokeeAuthenticator.isValidApiKey( apiKey ) ) {
			log.warning("given apikey is invalid!");
			// Kick anyone without a valid service key
			requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
			return;
		}
		String path = requestContext.getUriInfo().getPath();
		if(!path.startsWith("/"))
			path = "/"+path;
		if(!path.startsWith("/login")) {
			log.info("resource other than /login requested!");
			String authToken = requestContext.getHeaderString(Constants.HTTP_HEADER_AUTH_TOKEN);
			if(authToken == null || "".equals(authToken)) {
				authToken = requestContext.getUriInfo().getQueryParameters().getFirst("token");
			}
			if(!smokeeAuthenticator.isValidAuthToken(authToken,apiKey)) {
				log.warning("given authtoken is invalid!");
				requestContext.abortWith(Response.status(Status.UNAUTHORIZED).build());
				return;
			}
		}
		
 	}

}
