package com.eujian.gm.support.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FixedThreadPool {
	private final static int CORE_THREAD_COUNT=1000;
	private final static int MAX_THREAD_COUNT=20000;
	//private static int CAPACITY=1000;
	private static int KEEP_ALIVE_TIME=2000;
	
	private LinkedBlockingQueue<Runnable> linkedBlockingQueue=new LinkedBlockingQueue<Runnable>();
	private ActiveThreadExecutor activeThreadPool;
	
	private static FixedThreadPool fixedThreadPool;
	
	FixedThreadPool(){
		this(CORE_THREAD_COUNT,MAX_THREAD_COUNT);
	}
	
	public FixedThreadPool(int corePoolSize, int maximumPoolSize, RejectedExecutionHandler rejectedExecutionHandler){
		linkedBlockingQueue=new LinkedBlockingQueue<Runnable>(maximumPoolSize);
		activeThreadPool=new ActiveThreadExecutor(corePoolSize, maximumPoolSize, KEEP_ALIVE_TIME, TimeUnit.DAYS, linkedBlockingQueue, new FixedThreadFactory(FixedThreadPool.class.getName()),new RejectedStrategy());
	}
	
	public FixedThreadPool(int corePoolSize, int maximumPoolSize){
		activeThreadPool=new ActiveThreadExecutor(corePoolSize, maximumPoolSize, KEEP_ALIVE_TIME, TimeUnit.DAYS, linkedBlockingQueue, new FixedThreadFactory(FixedThreadPool.class.getName()));
	}

	public static FixedThreadPool getInstance() {
		return getInstance(CORE_THREAD_COUNT, MAX_THREAD_COUNT);
	}
	/**
	 * 单例  饿汉模式  加锁保证线程安全
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @return
	 */
	public static FixedThreadPool getInstance(int corePoolSize,int maximumPoolSize) {
		if(fixedThreadPool==null) {
			Lock lock=new ReentrantLock();
			try {
				//开始加锁了
				lock.lock();
				if(fixedThreadPool==null) {
					fixedThreadPool=new FixedThreadPool(corePoolSize, maximumPoolSize);
				}
			} finally {
				//强制释放锁
				lock.unlock();
			}
		}
		return fixedThreadPool;
	}
	
	public boolean execute(Runnable runnable) {
		//判断线程池是否创建了
		if(fixedThreadPool==null) {
			getInstance();
		}
		try {
			fixedThreadPool.activeThreadPool.execute(runnable);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public <T>Future<T> addCallable(Callable<T> callable){
		//判断线程池是否创建了
		if(fixedThreadPool==null) {
			getInstance();
		}
		return fixedThreadPool.activeThreadPool.submit(callable);
	}
	
	/**
	 *获取队列最大数
	 * @return
	 */
	public static int getQueneNum() {
		return fixedThreadPool.activeThreadPool.getQueue().size();
	}
	
	public static boolean shutdown() {
		try {
			fixedThreadPool.activeThreadPool.shutdown();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean stop() {
		try {
			if(fixedThreadPool!=null) {
				fixedThreadPool.activeThreadPool.shutdown();
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 *
	 *创建线程工厂
	 * @author huangliuwen
	 *
	 */
	public class FixedThreadFactory implements ThreadFactory{
		private final AtomicInteger mThreadNum = new AtomicInteger(1);
		private String threadName;
		
		FixedThreadFactory(){
			this("glableName");
		}
		FixedThreadFactory(String threadName){
			this.threadName=threadName;
		}
		@Override
		public Thread newThread(Runnable r) {
			String thdName=threadName+"_"+mThreadNum.getAndIncrement();
			Thread thread=new Thread(r, thdName);
			return thread;
		}
	}
	
}
