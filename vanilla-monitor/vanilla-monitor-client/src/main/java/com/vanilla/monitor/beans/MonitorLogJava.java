package com.vanilla.monitor.beans;

import com.alibaba.fastjson.JSON;

public class MonitorLogJava {

	private String appName="";
	private String msgId="";
	private String level="";
	private String loggerName="";
	private String localAddress="";
	private Long timestamp=0l;
	private String threadName="";
	private String msg="";
	private int type=0;
	
	
	
	
	
	public int getType() {
		return type;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getLoggerName() {
		return loggerName;
	}
	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}
	public String getLocalAddress() {
		return localAddress;
	}
	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
