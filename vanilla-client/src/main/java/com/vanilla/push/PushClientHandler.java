package com.vanilla.push;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class PushClientHandler extends ChannelInboundHandlerAdapter{

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
        ctx.fireChannelRead(msg);
    }
}
