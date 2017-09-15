package com.vanilla.boot.netty;

import io.netty.channel.ChannelInboundHandler;

import java.util.List;

public class FilterHandlerFactory {

	protected List<ChannelInboundHandler> getFilters(){
		return null;
	}
}
