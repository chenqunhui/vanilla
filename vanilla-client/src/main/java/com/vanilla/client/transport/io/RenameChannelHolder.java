package com.vanilla.client.transport.io;

import io.netty.channel.ChannelFuture;

public class RenameChannelHolder {

	private ChannelFuture channelFuture;

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public void setChannelFuture(ChannelFuture channelFuture) {
		this.channelFuture = channelFuture;
	}
}
