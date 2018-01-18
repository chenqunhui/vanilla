package com.vanilla.remoting;

import com.vanilla.remoting.exchange.ResponseFuture;

public interface Client {

	void init();
	
	void connect();
	
	void close();
	
	ResponseFuture send(Object msg);
}
