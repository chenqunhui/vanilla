package com.vanilla.remoting.spi.codec.hessian;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class HessianEncoder extends ChannelOutboundHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object msg,ChannelPromise promise) throws Exception {
		byte[] bytes = Hessian2Serialize.serialize(msg);
		ctx.write(bytes, promise);
	}
}
