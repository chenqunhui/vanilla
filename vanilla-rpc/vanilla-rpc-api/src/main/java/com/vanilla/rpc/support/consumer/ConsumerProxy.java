package com.vanilla.rpc.support.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.vanilla.rpc.MidMan;
import com.vanilla.rpc.hyxtrix.HystrixContext;
import com.vanilla.rpc.hyxtrix.RpcHystrixCommand;

/**
 * 消费者端代理类
 * @author chenqunhui
 *
 * @param <T>
 */
public class ConsumerProxy<T> implements InvocationHandler{

	private HystrixContext context;
	
	private MidMan<T> midMan;
	
	public ConsumerProxy(MidMan<T> midMan,HystrixContext context){
		this.midMan = midMan;
		this.context = context;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		 if (context != null && context.getFallbackObject() != null) {
	            return new RpcHystrixCommand(proxy, method, args, midMan, context).execute();
	        } else {
	            return midMan.invoke(proxy, method, args);
	        }
	}

}
