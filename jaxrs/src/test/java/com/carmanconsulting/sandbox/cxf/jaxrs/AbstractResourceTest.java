package com.carmanconsulting.sandbox.cxf.jaxrs;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractResourceTest<R> extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String WADL_SUFFIX = "?_wadl";
    private final Logger logger = LoggerFactory.getLogger(getClass());
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
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public Logger getLogger() {
        return logger;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected R createClientProxy() {
        return JAXRSClientFactory.create(getAddress(), resourceClass);
    }

    protected WebTarget createWebTarget() {
        return ClientBuilder.newClient().target(getAddress());
    }

    @After
    public void destroy() throws Exception {
        server.stop();
        server.destroy();
    }

    protected String getAddress() {
        return String.format("http://localhost:%d/%s", getPort(), resourceClass.getSimpleName());
    }

    protected int getPort() {
        return 7777;
    }

    protected List<Object> getProviders() {
        return Arrays.asList();
    }

    protected List<Feature> getFeatures() {
        return Arrays.asList();
    }

    @Before
    public void initialize() throws Exception {
        startServer();
        waitForWADL();
    }

    private void startServer() throws Exception {
        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        sf.setResourceClasses(resourceClass);
        sf.setFeatures(getFeatures());
        sf.setProviders(getProviders());
        sf.setResourceProvider(resourceClass, new SingletonResourceProvider(createResource(), true));
        sf.setAddress(getAddress());
        server = sf.create();
    }

    private void waitForWADL() throws Exception {
        Invocation.Builder client = ClientBuilder.newClient().target(getWadlAddress()).request();

        for (int i = 0; i < 20; i++) {
            Thread.sleep(1000);
            Response response = client.get();
            if (response.getStatus() == 200) {
                logger.info("WADL available, proceeding with tests...");
                return;
            } else {
                logger.info("WADL not available yet, waiting 1 more second...");
            }
        }
    }

    private String getWadlAddress() {
        return getAddress() + WADL_SUFFIX;
    }
}