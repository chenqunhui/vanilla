package com.vanilla.rpc;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import com.alibaba.fastjson.JSON;
import com.vanilla.remoting.Client;
import com.vanilla.remoting.exchange.MyRequest;
import com.vanilla.remoting.exchange.Request;
import com.vanilla.remoting.exchange.ResponseFuture;
import com.vanilla.remoting.spi.Message;
import com.vanilla.remoting.spi.MessageType;

public class RpcInvocationHandler implements InvocationHandler{

	private Client client;
	private static AtomicLong idLong = new AtomicLong(0);
	
	public RpcInvocationHandler(Client client){
		this.client = client;
	}
	@Override
	public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
		MyRequest request = new MyRequest();
		request.setMethodName(arg1.getName());
		request.setArgs(arg2);
		
		request.setClassName(arg0.getClass().getName().split("[$]")[0]+"Impl");
		
		Message msg = new Message();
		msg.setCompleted(false);
		msg.setData(new StringBuilder(JSON.toJSONString(request)));
		msg.setId(idLong.incrementAndGet());
		msg.setTimestamp(new Date().getTime());
		msg.setType(MessageType.MONIT_LOG.getCode());
		ResponseFuture future = client.send(msg,request);
		return future.get();
	}

}
