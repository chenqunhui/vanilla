package com.vanilla.client.conf;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


import io.netty.handler.timeout.IdleStateHandler;

public class ClientConfig {
	private long tickTime;     //心跳时间 ms
	private int maxDelayCount; //最大允许失败次数，超过时断开链接
	private int reconnectTime = 3000;//重连时间  ms
	
	public long getTickTime() {
		return tickTime;
	}
	public void setTickTime(long tickTime) {
		this.tickTime = tickTime;
	}
	public int getMaxDelayCount() {
		return maxDelayCount;
	}
	public void setMaxDelayCount(int maxDelayCount) {
		this.maxDelayCount = maxDelayCount;
	}
	public int getReconnectTime() {
		return reconnectTime;
	}
	
	
	public ClientConfig defaultConfig(){
		tickTime = 30000;
		maxDelayCount = 5;
		return this;
	}
	
	public  IdleStateHandler getHeartbeatConfig(){
		return new IdleStateHandler(0, 0, tickTime,TimeUnit.MILLISECONDS);
	}
	
	
	public InetSocketAddress getServerAddress(){
		return new InetSocketAddress("127.0.0.1",21881);
	}
}
