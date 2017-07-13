package com.vanilla.client.heartbeat;


import org.apache.log4j.Logger;

import com.vanilla.client.conf.ClientConfig;
import com.vanilla.protocol.MessageType;
import com.vanilla.protocol.protobuf.PushMessageProto;
import com.vanilla.client.transport.ClientChannelManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

public class ClientHeartbeatHandler extends ChannelInboundHandlerAdapter {

	private static Logger logger = Logger.getLogger(ClientHeartbeatHandler.class);
	
    private  ClientConfig config;  
      
    private int currentCount = 0;  
    
    private ClientChannelManager channelManager;
    
    public ClientHeartbeatHandler(ClientConfig conf,ClientChannelManager channelManager){
    	this.config = conf;
    	this.channelManager = channelManager;
    }
      
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
    	if(logger.isDebugEnabled()){
    		logger.debug("connect is activity !");
    	}
        ctx.fireChannelActive();  
    }  
  
    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
		if(logger.isDebugEnabled()){
			logger.debug("connect is inactivity !");
		}
		channelManager.close();
        ctx.fireChannelInactive();
    }  
  
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
        if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.ALL_IDLE) {  
                if(currentCount <= config.getMaxDelayCount()){ 
                	if(logger.isDebugEnabled()){
                		logger.debug("Heartbeat send ping to server  "+ctx.channel().remoteAddress()+" : ping !");
                	}
                	currentCount++;  
                    ctx.channel().writeAndFlush(ping(21212l));  
                }else{
                	//TRY_TIMES次心跳后服务端无响应则关闭链接；
                	logger.error("Heartbeat has not get response from server for "+currentCount+" times,close it!");
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
                if(currentCount > 1){
                	logger.warn("Heartbeat has not get response from server "+ctx.channel().remoteAddress()+" for "+ currentCount +" times!");
                }
            }  
        }  
    }  
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	PushMessageProto.PushMessage ms = (PushMessageProto.PushMessage)msg;
    	if(null != msg){
    		if (MessageType.HEARTBEAT_RESP.getCode() == ms.getType()) {
    			if(logger.isDebugEnabled()){
    				logger.debug("Heartbeat response from server "+ctx.channel().localAddress()+ms.getBody()+" : pong !");
    			}
            	currentCount =0; 
            	ReferenceCountUtil.release(msg);
            }else{
            	ctx.fireChannelRead(msg);
            	return;
            }
    	}
    }  
    
    private PushMessageProto.PushMessage ping(long sessionId){
		PushMessageProto.PushMessage.Builder builder = PushMessageProto.PushMessage.newBuilder();
		builder.setBody("ping");
		builder.setType(MessageType.HEARTBEAT.getCode());
		return builder.build();
	}
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
    
}
