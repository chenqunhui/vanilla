package com.vanilla.remoteing.netty;

import java.util.List;

import io.netty.channel.ChannelInboundHandler;

public interface HandlerFactory {

	ChannelInboundHandler getObject();
	
}
