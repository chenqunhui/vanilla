package com.vanilla.server.moniter;


import org.apache.log4j.Logger;




import com.vanilla.protocol.MessageType;
import com.vanilla.protocol.protobuf.PushMessageProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TcpReceiver extends ChannelInboundHandlerAdapter{

	private static Logger logger = Logger.getLogger(TcpReceiver.class);
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		PushMessageProto.PushMessage m =(PushMessageProto.PushMessage)msg;
		if(null != m && MessageType.MONIT_LOG.getCode() == m.getType()){
			if(logger.isDebugEnabled()){
				logger.info(m.toString());
			}
			return;
		}
		ctx.fireChannelRead(msg);
    }
	
	
}
