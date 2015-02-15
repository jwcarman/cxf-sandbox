package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloSync {

    @Path("/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello(@PathParam("name") final String name) {
        return "Hello, " + name + "!";
    }
}
