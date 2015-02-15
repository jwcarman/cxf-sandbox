package com.carmanconsulting.sandbox.cxf.jaxrs;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.TimeoutHandler;
import javax.ws.rs.core.Response;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class AbstractAsyncResource {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AtomicLong threadCount = new AtomicLong();
    private final Supplier<Executor> executorSupplier;
    private int minThreads = 50;
    private int maxThreads = 100;
    private long threadTimeoutValue = 60;
    private long responseTimeoutValue = 10;
    private int queueCapacity = 1000;
    private TimeUnit responseTimeoutUnit = TimeUnit.SECONDS;
    private TimeUnit threadTimeoutUnit = TimeUnit.SECONDS;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public AbstractAsyncResource() {
        this.executorSupplier = Suppliers.memoize(new ExecutorSupplier());
    }

    public AbstractAsyncResource(Executor executor) {
        this.executorSupplier = Suppliers.ofInstance(executor);
    }

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    protected Logger getLogger() {
        return logger;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    public void setMinThreads(int minThreads) {
        this.minThreads = minThreads;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public void setResponseTimeoutUnit(TimeUnit responseTimeoutUnit) {
        this.responseTimeoutUnit = responseTimeoutUnit;
    }

    public void setResponseTimeoutValue(long responseTimeoutValue) {
        this.responseTimeoutValue = responseTimeoutValue;
    }

    public void setThreadTimeoutUnit(TimeUnit threadTimeoutUnit) {
        this.threadTimeoutUnit = threadTimeoutUnit;
    }

    public void setThreadTimeoutValue(long threadTimeoutValue) {
        this.threadTimeoutValue = threadTimeoutValue;
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

    public BlockingQueue<Runnable> executorQueue() {
        return new LinkedBlockingDeque<>(queueCapacity);
    }

    public ThreadFactory threadFactory() {
        return r -> new Thread(r, String.format("%s-%d", AbstractAsyncResource.this.getClass().getSimpleName(), threadCount.incrementAndGet()));
    }

    protected TimeoutHandler timeoutHandler() {
        return asyncResponse -> asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Operation timed out.").build());
    }

//----------------------------------------------------------------------------------------------------------------------
// Inner Classes
//----------------------------------------------------------------------------------------------------------------------

    private class ExecutorSupplier implements Supplier<Executor> {
        @Override
        public Executor get() {
            return new ThreadPoolExecutor(
                    minThreads,
                    maxThreads,
                    threadTimeoutValue,
                    threadTimeoutUnit,
                    executorQueue(),
                    threadFactory());
        }
    }
}