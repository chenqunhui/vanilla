package com.vanilla.remoting.spi;

import java.io.Serializable;

/**
 * 消息体
 * @author cqh
 *
 */

public class Message implements Serializable{

	private static final long serialVersionUID = -1830874220384347423L;
	private long id;
	private int type;
	
	private StringBuilder data;
	
	private long timestamp;
	
	private boolean completed;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public StringBuilder getData() {
		return data;
	}

	public void setData(StringBuilder data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	
}
