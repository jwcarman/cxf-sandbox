package com.carmanconsulting.sandbox.cxf.benchmark.jaxrs;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxrsServer {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private static final String DEFAULT_ADDRESS = "http://0.0.0.0:7777/cxf";
    private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsServer.class);
//----------------------------------------------------------------------------------------------------------------------
// main() method
//----------------------------------------------------------------------------------------------------------------------

    public static void main(String[] args) throws Exception {

        EmbeddedCassandraServerHelper.startEmbeddedCassandra();
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
        final Cluster cluster = Cluster.builder().addContactPoint("localhost").withPort(9142).build();
        Session session = cluster.newSession();
        final String keyspaceName = "jax_rs_server";
        LOGGER.debug("Creating keyspace {}...", keyspaceName);
        session.execute(String.format("CREATE KEYSPACE IF NOT EXISTS %s WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};",
                keyspaceName));
        LOGGER.debug("Keyspace {} created successfully.", keyspaceName);
        session.close();
        cluster.close();

        JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
        final Class<?> resourceClass = Class.forName(System.getProperty("resourceClass"));
        sf.setResourceClasses(resourceClass);
        sf.setResourceProvider(resourceClass, new SingletonResourceProvider(resourceClass.newInstance(), true));
        final String address = System.getProperty("address", DEFAULT_ADDRESS);
        sf.setAddress(address);
        LOGGER.info("\n\n{}\n\nStarting {} JAX-RS server listening at {}, press any key to stop...\n\n{}\n", separator(), resourceClass.getSimpleName(), address, separator());
        Server server = sf.create();
        System.in.read();
        LOGGER.info("\n\n{}\n\nStopping {} server...\n\n{}\n", separator(), resourceClass.getSimpleName(), separator());
        server.stop();
        server.destroy();
        System.exit(0);
    }

    private static String separator() {
        return StringUtils.repeat('*', 120);
    }
}
