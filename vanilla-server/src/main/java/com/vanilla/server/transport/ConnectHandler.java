package com.vanilla.server.transport;

import java.util.concurrent.ConcurrentHashMap;

import com.vanilla.server.transport.utils.ChannelHanderUtils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConnectHandler extends ChannelInboundHandlerAdapter{

	/**
	 * 存储链接信息
	 */
	public static final ConcurrentHashMap<String,Channel> channel_map = new ConcurrentHashMap<String,Channel>();
	public static final ConcurrentHashMap<String,ChannelHandlerContext> context_map = new ConcurrentHashMap<String,ChannelHandlerContext>();
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
	    //System.out.println("TcpChildHandler.channelActive");
	    String key = ChannelHanderUtils.getConnectKey(ctx);
	    System.out.println("key:"+key);
	    if(!context_map.containsKey(key)){
	    	channel_map.put(key, ctx.channel());
	    	context_map.put(key, ctx);
	    }
        ctx.fireChannelActive();
    }
	
	/*@Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		
        //System.out.println("TcpChildHandler.handlerAdded");
    }*/
	
	
	@Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		 System.out.println("TcpChildHandler.handlerRemoved");
		 removeChannel(ctx);
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
		/*System.out.println("TcpChildHandler.exceptionCaught");
        ctx.fireExceptionCaught(cause);*/
		removeChannel(ctx);
    }
	
	/**
	 * 移除channel,关闭并清空相关资源
	 * @param ctx
	 */
	private void removeChannel(ChannelHandlerContext ctx){
		context_map.remove(ChannelHanderUtils.getConnectKey(ctx));
		channel_map.remove(ChannelHanderUtils.getConnectKey(ctx));
		if(null != ctx.channel()){
			try{
				ctx.channel().close();
			}finally{
				//TODO
			}
			
		}
	}
}
