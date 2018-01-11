package com.vanilla.remoting;

public interface Client {

	void init();
	
	void connect();
	
	void close();
	
	void send(Object msg);
}
