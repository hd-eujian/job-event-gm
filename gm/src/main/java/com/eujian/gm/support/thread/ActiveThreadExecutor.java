package com.eujian.gm.support.thread;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ActiveThreadExecutor extends ThreadPoolExecutor{
	private final ConcurrentHashMap<Runnable, Boolean> activeTasks = new ConcurrentHashMap<>();
	
	public ActiveThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	public ActiveThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory);
	}
	
	public ActiveThreadExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,threadFactory,rejectedExecutionHandler);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		activeTasks.put(r, true);
		super.beforeExecute(t,r);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		activeTasks.remove(r);
		super.afterExecute(r,t);
	}
	
	//返回所有线程
	public Set<Runnable> getActiveTasks() {
        return activeTasks.keySet();
    }
}
