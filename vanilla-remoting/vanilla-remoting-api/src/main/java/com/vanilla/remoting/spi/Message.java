package com.vanilla.remoting.spi;

import java.io.Serializable;

import lombok.Data;

/**
 * 传输的消息类型需要实现该接口
 * @author chenqunhui
 *
 */

@Data
public class Message implements Serializable{

	private static final long serialVersionUID = -1830874220384347423L;
	
	private int type;
	
	private String content;
}
