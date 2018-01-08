package com.vanilla.boot.netty.conf;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;


import io.netty.handler.timeout.IdleStateHandler;

public class NettyClientConfig {
	private String host;
	private int port;
	private long tickTime;     //心跳时间 ms
	private int maxDelayCount; //最大允许失败次数，超过时断开链接
	private int reconnectTime;//重连时间  ms
	
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
	
	
	public static NettyClientConfig defaultConfig(){
		NettyClientConfig confg = new NettyClientConfig();
		confg.tickTime = 30000;
		confg.maxDelayCount = 5;
		confg.host = "127.0.0.1";
		confg.reconnectTime = 3000;
		confg.port=21881;
		return confg;
	}
	
	public  IdleStateHandler getHeartbeatConfig(){
		return new IdleStateHandler(0, 0, tickTime,TimeUnit.MILLISECONDS);
	}
	
	
	public InetSocketAddress getServerAddress(){
		return new InetSocketAddress("127.0.0.1",21881);
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setReconnectTime(int reconnectTime) {
		this.reconnectTime = reconnectTime;
	}
}
