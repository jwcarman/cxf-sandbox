package com.carmanconsulting.sandbox.cxf.jaxws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloService {
//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    @WebMethod
    String sayHello(@WebParam(name = "name") String name);
}
