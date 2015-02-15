package com.carmanconsulting.sandbox.cxf.jaxrs.async;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolAsyncResource extends AsyncResource {
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------

    private int corePoolSize = 5;
    private int maxPoolSize = 25;
    private long threadKeepAliveValue = 60;
    private TimeUnit threadKeepAliveUnit = TimeUnit.SECONDS;

//----------------------------------------------------------------------------------------------------------------------
// Getter/Setter Methods
//----------------------------------------------------------------------------------------------------------------------

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public void setThreadKeepAliveUnit(TimeUnit threadKeepAliveUnit) {
        this.threadKeepAliveUnit = threadKeepAliveUnit;
    }

    public void setThreadKeepAliveValue(long threadKeepAliveValue) {
        this.threadKeepAliveValue = threadKeepAliveValue;
    }

//----------------------------------------------------------------------------------------------------------------------
// Other Methods
//----------------------------------------------------------------------------------------------------------------------

    protected Executor createExecutor() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                threadKeepAliveValue,
                threadKeepAliveUnit,
                workQueue(),
                threadFactory());
    }

    protected BlockingQueue<Runnable> workQueue() {
        return new LinkedBlockingDeque<>();
    }
}
