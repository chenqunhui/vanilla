package com.vanilla.monitor;

import java.util.Set;

import com.vanilla.common.URL;
import com.vanilla.remoting.RemotingException;
import com.vanilla.remoting.exchange.ResponseCallback;

public interface Client {

	//boolean isActivity();
	
	boolean isShutdown();
	
	void setServerURLs(Set<URL> urls);
	
	Set<URL> getServerURLs();
	
	Object send(Object object) throws RemotingException;
	
	void send(Object object,ResponseCallback callback) throws RemotingException;
}
