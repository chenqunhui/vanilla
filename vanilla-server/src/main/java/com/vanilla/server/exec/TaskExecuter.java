package com.vanilla.server.exec;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskExecuter {

	private static ExecutorService es ;
	
	private static AtomicInteger count;
		
	private TaskExecuter(){
		
	}
	
	public static ExecutorService getInstance() {
		if (null != es) {
			return es;
		} else {
			synchronized (TaskExecuter.class) {
				if (null != es) {
					return es;
				}
				es = new ThreadPoolExecutor(30, 50, 30l, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100),
						new ThreadFactory() {
							public Thread newThread(Runnable r) {
								Thread t = new Thread(r, "TaskExecuter-thread" + count.incrementAndGet());
								t.setDaemon(true);
								return t;
							}
						}, 
						new RejectedExecutionHandler() {
							public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
								System.out.println("queue is full .............!");
							}
						});
			}
		}
		return es;
	}
	
	public static void execute(Runnable e){
		TaskExecuter.getInstance().execute(e);
	}
}
