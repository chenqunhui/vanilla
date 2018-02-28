package com.vanilla.remoting.spi.codec.protobuf;


import java.util.List;

import com.vanilla.remoting.spi.codec.protobuf.MessageProto.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

public class ProtobufToMessageCodec extends MessageToMessageCodec<MessageProto.Message,com.vanilla.remoting.spi.Message>{

	@Override
	protected void encode(ChannelHandlerContext ctx, com.vanilla.remoting.spi.Message msg, List<Object> out)
			throws Exception {
		MessageProto.Message.Builder builder = MessageProto.Message.newBuilder();
		builder.setCompleted(msg.isCompleted());
		builder.setData(msg.getData().toString());
		builder.setId(msg.getId());
		builder.setTimestamp(msg.getTimestamp());
		builder.setType(msg.getType());
		out.add(builder.build());
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
		// TODO Auto-generated method stub
		com.vanilla.remoting.spi.Message obj = new com.vanilla.remoting.spi.Message();
		obj.setCompleted(msg.getCompleted());
		obj.setData(new StringBuilder(msg.getData()));
		obj.setId(msg.getId());
		obj.setTimestamp(msg.getTimestamp());
		obj.setType(msg.getType());
		out.add(obj);
	}


}
