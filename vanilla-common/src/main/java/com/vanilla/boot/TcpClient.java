package com.vanilla.boot;


public interface TcpClient {

	void init();
	
	void connect();
	
	void close();
	
	void send(Object msg);
}
