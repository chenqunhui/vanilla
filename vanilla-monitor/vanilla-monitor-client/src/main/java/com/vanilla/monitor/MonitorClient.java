package com.vanilla.monitor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vanilla.common.URL;
import com.vanilla.monitor.support.ChannelManagerFactory;
import com.vanilla.monitor.support.RegisteredClient;
import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.channel.ChannelManager;
import com.vanilla.remoting.exchange.Request;
import com.vanilla.remoting.exchange.ResponseFuture;


public class MonitorClient extends RegisteredClient implements Runnable{
	private Logger logger = Logger.getLogger(MonitorClient.class);
	private final String appName;
	
	private static MonitorClient client;
	
	private boolean isShutdown = false;

	private static BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>(1000);
	
	public static MonitorClient getInstance(String appName,URL registryUrl,String protocol){
		if(client != null){
			return client;
		}
		synchronized(MonitorClient.class){
			if(client != null){
				return client;
			}
			client = new MonitorClient(appName,registryUrl,ChannelManagerFactory.getChannelManager(protocol));
			return client;
		}
	}
	
	
	private MonitorClient(String appName,URL registryUrl,ChannelManager channelManager) {
		super(registryUrl,channelManager);
		this.appName = appName;
		new Thread(this,this.appName+"async-monitor-sender-thread").start();
	}


	
	@Override
	public void run() {
		while (true) {
			try {
				if(isShutdown){
					break;
				}
				Object msg = queue.take();
				if (logger.isDebugEnabled()) {
					logger.debug("start send monitor log...");
				}
				ResponseFuture future = this.send(msg);
				if (logger.isDebugEnabled()) {
					if(future.isDone()){
						logger.debug("send monitor log success...");
					}else{
						logger.debug("send monitor log fail...");
					}
				}
			} catch (Exception e) {
				logger.error("async send monitor error .", e);
			}
		}
		
	}
	private ResponseFuture  send(Object message) throws RemotingException{
		return  this.getChannelManager().send(new Request(message));
	}
	
	public void offer(Object msg) throws InterruptedException{
		if(isShutdown){
			throw new RuntimeException("client is shutdown");
		}
		queue.offer(msg, 300, TimeUnit.MILLISECONDS);
	}
}
