package com.vanilla.server.heartbeat;

import java.util.Date;

import org.apache.log4j.Logger;




import com.vanilla.protocol.MessageType;
import com.vanilla.protocol.protobuf.PushMessageProto;
import com.vanilla.server.config.ServerConfig;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 监听客户端心跳
 * @author chenqunhui
 *
 */
public class ServerHeartbeatListener extends ChannelInboundHandlerAdapter {

	private static Logger logger = Logger.getLogger(ServerHeartbeatListener.class);
	
	private int loss_connect_time = 0;
	
	private ServerConfig serverConfig;
	
	public ServerHeartbeatListener(ServerConfig config){
		this.serverConfig = config;
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	    if (evt instanceof IdleStateEvent) {
	      IdleStateEvent event = (IdleStateEvent) evt;  
	      if (event.state() == IdleState.ALL_IDLE) {
	    	  loss_connect_time++; 
	    	  if(logger.isDebugEnabled()){
	    		  logger.debug("Server has not get ping from cliet " + ctx.channel().remoteAddress()+" for "+ loss_connect_time * serverConfig.getTickTime()/1000+"s.");
	    	  }
	    	  if (loss_connect_time > serverConfig.getMaxDelayCount()) { 
	    		  if(logger.isDebugEnabled()){
		    		  logger.debug("Client "+ ctx.channel().remoteAddress()+"  has time out,close it!");
		    	  }
	              ctx.channel().close(); 
	              ctx.close();
	          }   
	        }
	    } else {
	    	super.userEventTriggered(ctx, evt);
	    }
	  }
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		PushMessageProto.PushMessage m =(PushMessageProto.PushMessage)msg;
		if(null != m && MessageType.HEARTBEAT.getCode() == m.getType()){
			if(logger.isDebugEnabled()){
				logger.debug("Heartbeat request from client "+ctx.channel().remoteAddress()+" : ping !");
			}
			if(loss_connect_time>0) loss_connect_time --;
			ctx.writeAndFlush(pong(ctx.hashCode()));
			return;
		}
		ctx.fireChannelRead(msg);
    }
	
	private PushMessageProto.PushMessage pong(long sessionId){
		PushMessageProto.PushMessage.Builder builder = PushMessageProto.PushMessage.newBuilder();
		builder.setBody("pong"+sessionId);
		builder.setType(MessageType.HEARTBEAT_RESP.getCode());
		return builder.build();
	}
}
