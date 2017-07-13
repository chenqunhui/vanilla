package com.vanilla.client.transport;

import com.vanilla.transport.Task;

import io.netty.channel.ChannelFuture;

/**
 * 链接管理接口
 * @author chenqunhui
 *
 */
public interface ClientChannelManager extends Task {

	
	void init();
	
	void connect();
	
	void close();
	
	//ChannelHolder initChannel();
	
	//void closeChannel(ChannelFuture channel);
}
