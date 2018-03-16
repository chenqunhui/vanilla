package com.vanilla.monitor.support;

import java.util.HashSet;
import java.util.Set;


import com.vanilla.common.URL;
import com.vanilla.monitor.Client;
import com.vanilla.remoting.channel.ChannelManager;

public abstract class AbstractClient implements Client{
	private Set<URL> urls =  new HashSet<URL>();
	private ChannelManager channelManager;
	
	
	public AbstractClient(ChannelManager channelManager){
		this.channelManager = channelManager;
	}

	private boolean isShutdown = false;
	
	public boolean isShutdown(){
		return isShutdown;
	}
	
	public void shutdown(){
		isShutdown = true;
	}
	
	@Override
	public Set<URL> getServerURLs() {
		return urls;
	}
	
	@Override
	public void setServerURLs(Set<URL> urls) {
		this.urls = urls;
	}
	
	public ChannelManager getChannelManager() {
		return channelManager;
	}

	public void setChannelManager(ChannelManager channelManager) {
		this.channelManager = channelManager;
	}

}
