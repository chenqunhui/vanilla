package com.vanilla.remoting;

public interface Server {

	void init();
	boolean isStarted();
	
	void shutdown();
	boolean isShutdown();
	
	void close();
	boolean isClosed();
}
