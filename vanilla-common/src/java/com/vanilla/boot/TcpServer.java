package com.vanilla.boot;

public interface TcpServer {

	void init();
	boolean isStarted();
	
	void shutdown();
	boolean isShutdown();
	
	void close();
	boolean isClosed();
}
