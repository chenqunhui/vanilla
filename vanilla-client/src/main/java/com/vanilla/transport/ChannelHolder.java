package com.vanilla.transport;

import io.netty.channel.ChannelFuture;

public class ChannelHolder {

	private ChannelFuture channelFuture;

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}
	
	public void writeAndFlush(Object msg){
		channelFuture.channel().writeAndFlush(msg);
	}
}
