package com.carmanconsulting.sandbox.cxf.jaxrs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public abstract class AbstractExceptionMapper<E extends Exception> implements ExceptionMapper<E> {
//----------------------------------------------------------------------------------------------------------------------
// ExceptionMapper Implementation
//----------------------------------------------------------------------------------------------------------------------


    @Override
    public Response toResponse(E exception) {
        System.out.println("I'm mapping this exception!");
        new Exception(exception).printStackTrace();
        return Response.serverError().entity(exception.getMessage()).build();
    }
}
