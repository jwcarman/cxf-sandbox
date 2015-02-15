package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;

public class JaxrsServer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULT_ADDRESS = "http://0.0.0.0:7777/cxf";

//----------------------------------------------------------------------------------------------------------------------
// main() method
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        final Class<?> resourceClass = Class.forName(System.getProperty("resourceClass"));
        sf.setResourceClasses(resourceClass);
        sf.setResourceProvider(resourceClass, new SingletonResourceProvider(resourceClass.newInstance(), true));
        final String address = System.getProperty("address", DEFAULT_ADDRESS);
        sf.setAddress(address);
        System.out.printf(">>> Starting %s JAX-RS server listening at %s, press any key to stop...%n", resourceClass.getSimpleName(), address);
        Server server = sf.create();
        System.in.read();
        System.out.printf(">>> Stopping %s server...%n", resourceClass.getSimpleName());
        server.stop();
        server.destroy();
        System.exit(0);
    }
}
