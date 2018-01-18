package com.vanilla.rpc;

import java.lang.reflect.Method;

import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import com.vanilla.remoting.Client;
import com.vanilla.remoting.exchange.MyRequest;
import com.vanilla.remoting.exchange.Request;
import com.vanilla.remoting.exchange.ResponseFuture;

public class RpcInvocationHandler implements InvocationHandler{

	private Client client;
	
	public RpcInvocationHandler(Client client){
		this.client = client;
	}
	@Override
	public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
		MyRequest request = new MyRequest();
		request.setMethodName(arg1.getName());
		request.setArgs(arg2);
		
		request.setClassName(arg0.getClass().getName().split("[$]")[0]+"Impl");
		ResponseFuture future = client.send(request);
		return future.get();
	}

}
