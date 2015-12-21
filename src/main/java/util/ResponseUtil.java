package main.java.util;

import javax.inject.Named;
import javax.ws.rs.core.Response;

/**
 * A utility class for building restful responses
 * @Author shreyas patil
 */
@Named
public class ResponseUtil {
	public Response buildFailureResponse(String message) {
		return Response.status(401).entity(message).build();
	}

	public Response buildSuccessResponse(Object object) {
		return Response.ok().entity(object).build();
	}
}
