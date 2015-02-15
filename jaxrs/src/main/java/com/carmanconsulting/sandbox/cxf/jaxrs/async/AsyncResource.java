package com.carmanconsulting.sandbox.cxf.jaxrs.async;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public abstract class AsyncResource {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicLong threadCount = new AtomicLong();
    private final Supplier<Executor> executorSupplier;
    private long responseTimeoutValue = 10;
    private TimeUnit responseTimeoutUnit = TimeUnit.SECONDS;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public AsyncResource() {
        this.executorSupplier = Suppliers.memoize(new ExecutorSupplier());
    }

//----------------------------------------------------------------------------------------------------------------------
// Abstract Methods
//----------------------------------------------------------------------------------------------------------------------

    protected abstract Executor createExecutor();

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    protected Logger getLogger() {
        return logger;
    }

    public void setResponseTimeoutUnit(TimeUnit responseTimeoutUnit) {
        this.responseTimeoutUnit = responseTimeoutUnit;
    }

    public void setResponseTimeoutValue(long responseTimeoutValue) {
        this.responseTimeoutValue = responseTimeoutValue;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected <T> void executeAsync(AsyncResponse response, Supplier<T> responseSupplier) {
        try {
            response.setTimeoutHandler(timeoutHandler());
            executorSupplier.get().execute(() -> response.resume(responseSupplier.get()));
            response.setTimeout(responseTimeoutValue, responseTimeoutUnit);
        } catch (RejectedExecutionException e) {
            logger.error("Unable to execute runnable!", e);
            response.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No resources available."));
        }
    }

    protected ThreadFactory threadFactory() {
        return r -> new Thread(r, String.format("%s-%d", AsyncResource.this.getClass().getSimpleName(), threadCount.incrementAndGet()));
    }

    protected TimeoutHandler timeoutHandler() {
        return asyncResponse -> asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Operation timed out.").type(MediaType.TEXT_PLAIN).build());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private class ExecutorSupplier implements Supplier<Executor> {
        @Override
        public Executor get() {
            return createExecutor();
        }
    }
}