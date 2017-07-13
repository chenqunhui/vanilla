package com.vanilla.server.config;

public class ServerConfig {

	private int port;   //服务启动端口
	
	private int bossGroup;
	
	private int workerGroup;
	
	private long tickTime;  //服务端心跳时间
	private int maxDelayCount; //心跳超时次数

	public ServerConfig defaultConfig(){
		port=21881;
		bossGroup = 2;
		workerGroup = 2;
		tickTime = 30*1000;
		maxDelayCount = 5;
		return this;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getBossGroup() {
		return bossGroup;
	}

	public void setBossGroup(int bossGroup) {
		this.bossGroup = bossGroup;
	}

	public int getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(int workerGroup) {
		this.workerGroup = workerGroup;
	}


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

}
