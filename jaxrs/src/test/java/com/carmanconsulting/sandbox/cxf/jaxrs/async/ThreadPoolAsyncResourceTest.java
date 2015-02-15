package com.carmanconsulting.sandbox.cxf.jaxrs.async;

import com.carmanconsulting.sandbox.cxf.jaxrs.AbstractResourceTest;
import com.carmanconsulting.sandbox.cxf.jaxrs.resource.HelloResourceAsync;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.concurrent.TimeUnit;

public class ThreadPoolAsyncResourceTest extends AbstractResourceTest<HelloResourceAsync> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ThreadPoolAsyncResourceTest() {
        super(HelloResourceAsync.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected HelloResourceAsync createResource() {
        final HelloResourceAsync resource = new HelloResourceAsync();
        resource.setResponseTimeoutValue(500);
        resource.setResponseTimeoutUnit(TimeUnit.MILLISECONDS);
        resource.setCorePoolSize(2);
        resource.setMaxPoolSize(5);
        resource.setThreadKeepAliveUnit(TimeUnit.SECONDS);
        resource.setThreadKeepAliveValue(10);
        return resource;
    }

    @Test
    public void testSayHello() {
        assertEquals("Hello, Jim!", createWebTarget().path("hello").path("Jim").request().get(String.class));
    }

    @Test
    public void testTimeout() {
        final Response response = createWebTarget().path("hello").path("pokey").request().get();
        assertEquals("Operation timed out.", response.readEntity(String.class));
        assertEquals(Response.Status.SERVICE_UNAVAILABLE.getStatusCode(), response.getStatus());
    }
}
