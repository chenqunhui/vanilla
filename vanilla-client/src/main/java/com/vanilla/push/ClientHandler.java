package com.vanilla.push;

import java.lang.reflect.Method;

import com.vanilla.remoting.exchange.MyRequest;
import com.vanilla.remoting.exchange.Response;
import com.vanilla.remoting.exchange.support.MyResponseFuture;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter{
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//		writer.write(msg.toString());
//		writer.write(System.getProperty("line.separator"));
//		writer.flush();
		System.out.println(msg);
		if(msg instanceof Response){
			MyResponseFuture.received(null, (Response)msg);
		}
    }
}
