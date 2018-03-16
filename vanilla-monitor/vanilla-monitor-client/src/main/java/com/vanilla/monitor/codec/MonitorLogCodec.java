package com.vanilla.monitor.codec;

import java.util.List;

import com.vanilla.monitor.beans.MonitorLogJava;
import com.vanilla.monitor.beans.proto.MonitorLogProto;
import com.vanilla.monitor.beans.proto.MonitorLogProto.MonitorLog;
import com.vanilla.remoting.spi.codec.protobuf.MessageProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

public class MonitorLogCodec extends MessageToMessageCodec<MonitorLogProto.MonitorLog, MonitorLogJava> {
	{

	}

	@Override
	protected void encode(ChannelHandlerContext ctx, MonitorLogJava log, List<Object> out) throws Exception {
		System.out.println("MonitorLogCodec encode开始处理");
		MonitorLogProto.MonitorLog.Builder builder = MonitorLogProto.MonitorLog.newBuilder();
		builder.setLocalAddress(log.getLocalAddress());
		builder.setLoggerName(log.getLoggerName());
		builder.setMsg(log.getMsg());
		builder.setMsgId(log.getMsgId());
		builder.setThreadName(log.getThreadName());
		builder.setTimestamp(log.getTimestamp());
		builder.setType(log.getType());
		out.add(builder.build());
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, MonitorLog msg, List<Object> out) throws Exception {
		System.out.println("MonitorLogCodec decode开始处理");
		MonitorLogJava log = new MonitorLogJava();
		log.setLocalAddress(msg.getLocalAddress());
		log.setLoggerName(msg.getLoggerName());
		log.setMsg(msg.getMsg());
		log.setMsgId(msg.getMsgId());
		log.setThreadName(msg.getThreadName());
		log.setTimestamp(msg.getTimestamp());
		log.setType(msg.getType());
		out.add(log);
	}
}