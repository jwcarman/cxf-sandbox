package com.carmanconsulting.sandbox.cxf.jaxws;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractServiceTest<R> extends Assert {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Server server;

    private final Class<R> serviceInterface;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    protected AbstractServiceTest(Class<R> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @SuppressWarnings("unchecked")
    protected R createClientProxy() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress(getAddress());
        factory.setServiceClass(serviceInterface);
        factory.setFeatures(getClientFeatures());
        return (R) factory.create();
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract R createServiceBean();

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public Logger getLogger() {
        return logger;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @After
    public void destroy() throws Exception {
        server.stop();
        server.destroy();
    }

    protected String getAddress() {
        return String.format("http://localhost:%d/%s", getPort(), serviceInterface.getSimpleName());
    }

    protected List<Feature> getServerFeatures() {
        return Collections.singletonList(new LoggingFeature());
    }

    protected List<Feature> getClientFeatures() {
        return Collections.emptyList();
    }

    protected int getPort() {
        return 8888;
    }

    @Before
    public void initialize() throws Exception {
        startServer();
    }

    private void startServer() throws Exception {
        JaxWsServerFactoryBean sf = new JaxWsServerFactoryBean();
        sf.setBus(createBus());
        sf.setServiceClass(serviceInterface);
        sf.setServiceBean(createServiceBean());
        sf.setFeatures(getServerFeatures());
        sf.setAddress(getAddress());
        server = sf.create();
    }

    private Bus createBus() {
        return BusFactory.getDefaultBus();
    }
}