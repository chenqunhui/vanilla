package com.vanilla.monitor.support;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vanilla.common.URL;
import com.vanilla.monitor.Client;
import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.channel.ChannelManager;
import com.vanilla.remoting.exchange.Request;
import com.vanilla.remoting.exchange.ResponseCallback;
import com.vanilla.remoting.exchange.ResponseFuture;

public abstract class AbstractClient implements Client{
	
	private Logger logger = Logger.getLogger(AbstractClient.class);
	private Set<URL> urls =  new HashSet<URL>();
	private ChannelManager channelManager;
	
	public ChannelManager getChannelManager() {
		return channelManager;
	}

	public void setChannelManager(ChannelManager channelManager) {
		this.channelManager = channelManager;
	}

	private boolean isShutdown = false;
	
	public AbstractClient(ChannelManager channelManager){
		this.channelManager = channelManager;
	}
	
	public boolean isShutdown(){
		return isShutdown;
	}
	
	@Override
	public Set<URL> getServerURLs() {
		return urls;
	}
	
	@Override
	public void setServerURLs(Set<URL> urls) {
		this.urls = urls;
	}

	public Object  send(Object message) throws RemotingException{
		ResponseFuture response = channelManager.send(new Request(message));
		return response.get();
	}
	
	public void send(Object message,ResponseCallback callback) throws RemotingException{
		channelManager.asyncSend(new Request(message), callback);
	}
}
