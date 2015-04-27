package com.carmanconsulting.sandbox.cxf.jaxws;

import org.junit.Test;

public class HelloServiceTest extends AbstractServiceTest<HelloService> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public HelloServiceTest() {
        super(HelloService.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected HelloService createServiceBean() {
        return name -> "Hello, " + name + "!";
    }

    @Test
    public void testCall() {
        HelloService proxy = createClientProxy();
        assertEquals("Hello, CXF!", proxy.sayHello("CXF"));
    }

}
