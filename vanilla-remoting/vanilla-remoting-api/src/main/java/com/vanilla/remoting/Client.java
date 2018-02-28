package com.vanilla.remoting;

import com.vanilla.remoting.exchange.Request;
import com.vanilla.remoting.exchange.ResponseFuture;

public interface Client {

	void init();
	
	void connect();
	
	void close();
	
	void actived();
	
	void inActived();
	
	Object ping();
	boolean isPong(Object pong);
	
	ResponseFuture send(Object msg,Request request);
}
