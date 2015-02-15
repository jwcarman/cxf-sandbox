package com.carmanconsulting.sandbox.cxf.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResourceAsync extends AbstractAsyncResource {

    @Path("/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void sayHello(@PathParam("name") final String name, @Suspended final AsyncResponse response) {
        executeAsync(response, () -> "Hello, " + name + "!");
    }

}
