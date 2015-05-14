package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloAsync extends AbstractHelloResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/{id}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void sayHello(@PathParam("id") final String id, @Suspended final AsyncResponse response) {
        executeAsync(response, () -> helloMessage(id));
    }
}
