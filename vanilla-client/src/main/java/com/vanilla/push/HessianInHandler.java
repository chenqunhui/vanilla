package com.vanilla.push;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HessianInHandler extends ChannelInboundHandlerAdapter{

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		//System.out.println(Hessian2Serialize.deserialize((byte[])msg));
        ctx.fireChannelRead(msg);
    }
}
