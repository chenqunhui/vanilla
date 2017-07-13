package com.vanilla.server.transport;


import com.vanilla.protocol.MessageType;
import com.vanilla.protocol.protobuf.PushMessageProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerTranferHandler extends ChannelInboundHandlerAdapter{

	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof PushMessageProto.PushMessage){
			PushMessageProto.PushMessage pushMsg = (PushMessageProto.PushMessage)msg;
			if(pushMsg.getType() ==  MessageType.NOMARL.getCode()){
				String content = pushMsg.getBody();
				
				System.out.println(content);
				
				
			}

		}
		
		
		
		
        ctx.fireChannelRead(msg);
    }
	
	
	
	
	
	
	
	
	
}
