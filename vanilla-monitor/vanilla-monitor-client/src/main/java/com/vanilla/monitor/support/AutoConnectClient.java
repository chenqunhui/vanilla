package com.vanilla.monitor.support;

import java.util.HashSet;
import java.util.Set;


import org.apache.log4j.Logger;

import com.vanilla.common.URL;
import com.vanilla.common.utils.UrlUtils;
import com.vanilla.remoting.channel.Channel;
import com.vanilla.remoting.channel.ChannelManager;

/**
 * 自动重连Client
 * 
 * failedChannelMap  失败的链接
 * getServerURLs() 可用URL
 * 
 * 
 * 
 * @author chenqunhui
 *
 */
public abstract class AutoConnectClient extends AbstractClient {

	private Logger logger = Logger.getLogger(AutoConnectClient.class);
	
	
	
	//TODO
		
	// 自动重连
	private  Thread autoReconnectThread = new Thread("Auto-reconnect-thread") {
		public void run() {
			try {
				Thread.sleep(1000L); // 延迟1秒启动
			} catch (InterruptedException e) {
				// ignore
			}
			if(logger.isInfoEnabled()){
				logger.info("auto reconnect thread started! ");
			}
			while (true) {
				if(isShutdown()){
					logger.info("client is shutown,auto reconnect thread exit! ");
					break;
				}
				connect();
				try {
					Thread.sleep(1000L); // check every 100 mill
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	};
	
	public void connect(){
		final Set<URL> urls = new HashSet<URL>();
		urls.addAll(getServerURLs()) ;
		if(urls.isEmpty()){
			logger.warn("no avaiable server url can be used!");
			return;
		}
		Set<String> keySet = new HashSet<String>();
		Set<Channel> channels = this.getChannelManager().existsChannels();
		channels.forEach(t-> keySet.add(UrlUtils.getKey(t.getUrl())));
		for(URL url : urls){
			String key = UrlUtils.getKey(url);
			if(!keySet.contains(key)){
				if(logger.isDebugEnabled()){
					logger.debug("create channel for url "+ url.toFullString());
				}
				this.getChannelManager().createChannel(url);
			}
		}
	}

	public AutoConnectClient(ChannelManager channelManager){
		super(channelManager);
		autoReconnectThread.start();
	}
}
