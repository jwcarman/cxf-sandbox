package com.carmanconsulting.sandbox.cxf.jaxrs;

import java.util.List;

import javax.ws.rs.core.Response;

import com.carmanconsulting.sandbox.cxf.jaxrs.resource.HelloResourceAsync;
import com.google.common.collect.Lists;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.feature.LoggingFeature;
import org.junit.Ignore;
import org.junit.Test;

public class ExceptionMapperTest extends AbstractResourceTest<HelloResourceAsync> {
//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public ExceptionMapperTest() {
        super(HelloResourceAsync.class);
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @Override
    protected HelloResourceAsync createResource() {
        return new HelloResourceAsync();
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------s


    @Override
    protected List<Feature> getFeatures() {
        return Lists.newArrayList(new LoggingFeature());
    }

    @Override
    protected List<Object> getProviders() {
        return Lists.newArrayList(new MyExceptionMapper());
    }

    @Test
    @Ignore
    public void test404() {
        Response response = createWebTarget().path("/hello").path("/foo").path("/bar").request().get();
        assertEquals(404, response.getStatus());
    }
}
