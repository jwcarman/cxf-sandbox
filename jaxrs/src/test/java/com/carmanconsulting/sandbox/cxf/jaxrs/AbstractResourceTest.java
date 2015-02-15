package com.carmanconsulting.sandbox.cxf.jaxrs;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractResourceTest<R> extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private Server server;

    private final Class<R> resourceClass;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public AbstractResourceTest(Class<R> resourceClass) {
        this.resourceClass = resourceClass;
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract R createResource();

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected R createClientProxy() {
        return JAXRSClientFactory.create(getAddress(), resourceClass);
    }

    protected WebClient createWebClient() {
        return WebClient.create(getAddress());
    }

    @After
    public void destroy() throws Exception {
        server.stop();
        server.destroy();
    }

    protected String getAddress() {
        return "http://localhost:7777/rest";
    }

    protected List<Object> getProviders() {
        return new ArrayList<Object>();
    }

    @Before
    public void initialize() throws Exception {
        startServer();
    }

    private void startServer() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(resourceClass);
        sf.setFeatures(Arrays.asList(new LoggingFeature()));
        sf.setProviders(getProviders());
        sf.setResourceProvider(resourceClass, new SingletonResourceProvider(createResource(), true));
        sf.setAddress(getAddress());
        server = sf.create();
    }
}