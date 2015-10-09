package com.carmanconsulting.sandbox.cxf.jaxrs;

public class HelloResourceImpl implements HelloResource  {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name + "!";
    }
}
