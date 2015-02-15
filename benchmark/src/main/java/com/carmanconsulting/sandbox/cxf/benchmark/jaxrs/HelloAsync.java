package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs;

import com.carmanconsulting.sandbox.cxf.jaxrs.async.ThreadPoolAsyncResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

public class HelloAsync extends ThreadPoolAsyncResource {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Path("/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void sayHello(@PathParam("name") final String name, @Suspended final AsyncResponse response) {
        executeAsync(response, () -> {
            if("pokey".equals(name)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    getLogger().error("Somebody interrupted my sleep!", e);
                }
            }
            return "Hello, " + name + "!";
        });
    }
}
