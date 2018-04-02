package com.vanilla.rpc.thrift.consumer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.vanilla.cluster.LoadBalance;
import com.vanilla.common.URL;
import com.vanilla.rpc.Invoker;
import com.vanilla.rpc.MidMan;
import com.vanilla.rpc.hyxtrix.HystrixContext;
import com.vanilla.rpc.hyxtrix.RpcHystrixCommand;
import com.vanilla.rpc.thrift.ThriftInvoker;



/**
 * 客户端代理类
 * @author chenqunhui
 *
 */
public class ThriftClientProxy<T> implements InvocationHandler{

	private HystrixContext context;
	
	private MidMan<T> midMan;
	
	
	public ThriftClientProxy(MidMan<T> midMan,HystrixContext context){
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
