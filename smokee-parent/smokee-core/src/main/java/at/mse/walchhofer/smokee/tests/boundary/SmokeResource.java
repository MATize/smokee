package at.mse.walchhofer.smokee.tests.boundary;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.mse.walchhofer.smokee.api.caching.ISmokeTestResult;
import at.mse.walchhofer.smokee.tests.control.ConfigurationException;
import at.mse.walchhofer.smokee.tests.control.NotEnabledException;
import at.mse.walchhofer.utilities.logging.Log;

@RequestScoped
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Path("/tests")
public class SmokeResource {

	@Inject
	SmokeService smokeService;

	@Inject
	@Log
	Logger logger;

	@GET
	public Response getSmokeTests() {
		try {
			List<ISmokeTestResult> results = smokeService.executeTests();
			return Response.status(Status.OK).entity(results)
					.type(MediaType.APPLICATION_JSON).build();
		} catch (ConfigurationException ex) {
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Smoker configuration failed with message "
							+ ex.getMessage()).type(MediaType.TEXT_PLAIN)
					.build());
		} catch (NotEnabledException e) {
			throw new WebApplicationException(Response
					.status(Response.Status.BAD_REQUEST)
					.entity("Smoker not enabled!").type(MediaType.TEXT_PLAIN)
					.build());
		}
	}
}
