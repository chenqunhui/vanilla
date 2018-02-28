package com.vanilla.remoting.spi;

import java.io.Serializable;

import lombok.Data;

/**
 * 消息体
 * @author cqh
 *
 */

@Data
public class Message implements Serializable{

	private static final long serialVersionUID = -1830874220384347423L;
	private long id;
	private int type;
	
	private StringBuilder data;
	
	private long timestamp;
	
	private boolean completed;
}
