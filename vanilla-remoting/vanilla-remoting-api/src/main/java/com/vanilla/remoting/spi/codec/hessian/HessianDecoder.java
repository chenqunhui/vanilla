package com.vanilla.remoting.spi.codec.hessian;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HessianDecoder extends ChannelInboundHandlerAdapter{

	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		System.out.println(Hessian2Serialize.deserialize((byte[])msg));
        ctx.fireChannelRead(Hessian2Serialize.deserialize((byte[])msg));
    }
}
