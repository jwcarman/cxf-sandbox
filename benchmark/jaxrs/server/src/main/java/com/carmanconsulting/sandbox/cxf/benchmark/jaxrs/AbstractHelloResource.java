package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs;

import com.carmanconsulting.sandbox.cxf.jaxrs.async.ThreadPoolAsyncResource;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.savoirtech.hecate.cql3.dao.PojoDao;
import com.savoirtech.hecate.cql3.dao.PojoDaoFactory;
import com.savoirtech.hecate.cql3.dao.def.DefaultPojoDaoFactory;

import javax.annotation.PostConstruct;

public abstract class AbstractHelloResource extends ThreadPoolAsyncResource {

    private PojoDao<String, Person> dao;

    @PostConstruct
    public void startup() {
        getLogger().info("Starting up...");
        final Cluster cluster = Cluster.builder().addContactPoint("localhost").withPort(9142).build();
        Session session = cluster.connect("jax_rs_server");
        PojoDaoFactory factory = new DefaultPojoDaoFactory(session);
        dao = factory.createPojoDao(Person.class);
        dao.save(new Person("jcarman", "Jim Carman"));
        dao.save(new Person("duke", "Duke"));
    }

    protected String helloMessage(String id) {
        Person person = dao.findByKey(id);
        if (person == null) {
            return "Not Found!";
        } else {
            return "Hello, " + person.getFullName() + "!";
        }
    }
}
