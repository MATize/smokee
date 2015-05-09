package at.mse.walchhofer.smokee.service;

import java.io.File;
import java.net.URL;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import at.mse.walchhofer.smokee.utils.FileUtils;

@RequestScoped
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "application/js","text/css" })
@Path("/resources")
public class ResourcesResource {

	private static final String BASE_DIR = "web";

	@GET
	@Path("/{type}/{file}")
	public Response getJsFile(@PathParam("file") String filename, @PathParam("type") String type) {
		URL resource = this.getClass().getClassLoader().getResource(BASE_DIR+"/"+type+"/"+filename);
		if(resource != null) {
			File file = FileUtils.getFileForUrl(resource);
			if(file.isFile() && file.exists()) {
				String mediaType="text/";
				if(type.equals("js")) {
					mediaType += "javascript";
				} else {
					mediaType += "css";
				}
				mediaType += "; charset=UTF-8";
				return Response.ok().entity(file).type(mediaType).build();
			}
		}
		return Response.status(Status.NOT_FOUND).type(MediaType.APPLICATION_JSON).build();
	}

}
