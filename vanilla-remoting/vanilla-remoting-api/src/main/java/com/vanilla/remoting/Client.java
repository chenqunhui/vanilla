package com.vanilla.remoting;


public interface Client {

	void init();
	
	void connect();
	
	void close();
	
	void actived();
	
	void inActived();
	
	Object ping();
	boolean isPong(Object pong);
	
}
