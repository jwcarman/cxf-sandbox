package com.carmanconsulting.sandbox.cxf.jaxrs;

import javax.ws.rs.ext.Provider;

@Provider
public class MyExceptionMapper extends AbstractExceptionMapper<MyException> {
}
