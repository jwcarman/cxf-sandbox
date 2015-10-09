package com.carmanconsulting.sandbox.cxf.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface HelloResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String sayHello(@PathParam("name") final String name);
}
