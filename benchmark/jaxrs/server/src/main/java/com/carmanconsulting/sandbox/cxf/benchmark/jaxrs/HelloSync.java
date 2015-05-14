package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloSync extends AbstractHelloResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/{id}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello(@PathParam("id") final String id) {
        return helloMessage(id);
    }
}
