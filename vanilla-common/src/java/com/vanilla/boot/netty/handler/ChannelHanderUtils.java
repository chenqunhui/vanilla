package com.vanilla.boot.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
public class ChannelHanderUtils {

	public static String getConnectKey(ChannelHandlerContext ctx){
		InetSocketAddress localAddress = (InetSocketAddress)(ctx.channel().localAddress());
		InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
		return localAddress.getHostName()+":"+localAddress.getPort()+"->"+remoteAddress.getHostName()+":"+remoteAddress.getPort();
		//return ctx.channel().localAddress().toString()+"-"+ctx.channel().remoteAddress().toString();
	}
}
