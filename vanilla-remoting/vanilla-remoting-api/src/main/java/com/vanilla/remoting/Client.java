package com.vanilla.remoting;

import com.vanilla.common.URL;

public interface Client {

	void init();
	
	void connect();
	
	void close();
	
	void actived();
	
	void inActived();
	
	Object ping();
	boolean isPong(Object pong);
	
	URL getSubscribeURL();    //服务端URL
	
}
