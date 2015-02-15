package com.carmanconsulting.sandbox.cxf.jaxrs;

import org.junit.Test;

public class AbstractAsyncResourceTest extends AbstractResourceTest<HelloResourceAsync> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public AbstractAsyncResourceTest() {
        super(HelloResourceAsync.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected HelloResourceAsync createResource() {
        return new HelloResourceAsync();
    }

    @Test
    public void testSayHello() {
        assertEquals("Hello, Jim!", createWebTarget().path("hello").path("Jim").request().get(String.class));
    }
}
