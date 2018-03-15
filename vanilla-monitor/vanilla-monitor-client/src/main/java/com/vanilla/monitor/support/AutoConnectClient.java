package com.vanilla.monitor.support;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import org.apache.log4j.Logger;

import com.vanilla.common.URL;
import com.vanilla.common.utils.UrlUtils;
import com.vanilla.remoting.channel.Channel;
import com.vanilla.remoting.channel.ChannelManager;

public abstract class AutoConnectClient extends AbstractClient {

	private Logger logger = Logger.getLogger(AutoConnectClient.class);
	//key : url key
	protected ConcurrentHashMap<String,Channel> channelMap = new  ConcurrentHashMap<String,Channel>();
		
	// 自动重连
	private  Thread autoReconnectThread = new Thread("Auto-reconnect-thread") {
		public void run() {
			if(logger.isInfoEnabled()){
				logger.info("auto reconnect thread started! ");
			}
			while (true) {
				if(isShutdown()){
					logger.info("client is shutown,auto reconnect thread exit! ");
					break;
				}
				final Set<URL> urls = new HashSet<URL>();
				urls.addAll(getServerURLs()) ;
				if(urls.isEmpty()){
					logger.warn("no avaiable server url can be used!");
					try {
						Thread.sleep(10 * 1000L); // check every 10 seconds
					} catch (InterruptedException e) {
						// ignore
					}
					continue;
				}
				for(URL url : urls){
					String key = UrlUtils.getKey(url);
					if(!channelMap.containsKey(key)){
						Channel channel = AutoConnectClient.this.getChannelManager().createChannel(url);
						try{
							if(channel.doConnect()){
								channelMap.putIfAbsent(key, channel);
							}
						}catch(Exception e){
							//ignor 
						}
					}
				}
				try {
					Thread.sleep(100L); // check every 100 mill
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	};

	public AutoConnectClient(ChannelManager channelManager){
		super(channelManager);
		autoReconnectThread.start();
	}

}
