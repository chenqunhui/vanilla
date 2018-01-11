package com.vanilla.remoteing.netty.handler;


import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import com.vanilla.remoteing.netty.NettyClient;
import com.vanilla.remoteing.netty.config.NettyClientConfig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = Logger.getLogger(ClientHeartbeatHandler.class);
	
    private  NettyClientConfig config;  
    
    private NettyClient client;
      
    private AtomicInteger currentCount = new AtomicInteger(0);  
    
    public ClientHeartbeatHandler(NettyClient client){
    	this.config = client.getConfig();
    	this.client = client;
    }
      
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
    	if(logger.isDebugEnabled()){
    		logger.debug("connect is activity !");
    	}
    	client.actived();
        ctx.fireChannelActive();  
    }  
  
    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
		if(logger.isDebugEnabled()){
			logger.debug("connect is inactivity !");
		}
		//channelManager.close();
		client.inActived();
        ctx.fireChannelInactive();
    }  
  
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.ALL_IDLE) {
            	int count =  currentCount.incrementAndGet();
            	if(count > 1){
                	logger.warn("Heartbeat has not get response from server "+ctx.channel().remoteAddress()+" for "+ count +" times!");
                }
                if(count <= config.getMaxDelayCount()){ 
                	if(logger.isDebugEnabled()){
                		logger.debug("Heartbeat send ping to server  "+ctx.channel().remoteAddress()+" : ping !");
                	}
                    ctx.channel().writeAndFlush("ping");  
                }else{
                	//TRY_TIMES次心跳后服务端无响应则关闭链接；
                	logger.error("Heartbeat has not get response from server for "+(count-1)+" times,close it!");
                	try{
                		ctx.channel().close();
                	}catch(Exception e){
                		
                	}
                	try{
                		ctx.close();
                	}catch(Exception e){
                		
                	}
                	return;
                }
            }  
        }  
    }  
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	String message = (String)msg;
    	if(null != message){
    		if (message.equals("pong")) {
    			if(logger.isDebugEnabled()){
    				logger.debug("Heartbeat response from server "+ctx.channel().localAddress()+" : pong !");
    			}
    			currentCount.set(0);
            	ReferenceCountUtil.release(msg);
            }else{
            	ctx.fireChannelRead(msg);
            	return;
            }
    	}
    }  
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
    	System.out.println("catch exception");
        ctx.fireExceptionCaught(cause);
    }
    
}
