package com.vanilla.monitor;

import java.util.Set;

import com.vanilla.common.URL;

public interface Client {

	
	void shutdown();
	
	boolean isShutdown();
	
	void setServerURLs(Set<URL> urls);
	
	Set<URL> getServerURLs();
}
