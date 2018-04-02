package com.vanilla.rpc.thrift;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.thrift.TServiceClient;

import com.vanilla.common.URL;
import com.vanilla.rpc.Invoker;
import com.vanilla.rpc.exception.RpcException;
import com.vanilla.rpc.loadbalance.LoadBalance;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author chenqunhui
 *
 * @param <T>
 */
@Slf4j
public  class ThriftInvoker<T> implements Invoker<T>{
	
	private URL url; 
	private Class<T> clazz;
	private TServiceClient client;
	
	
	public ThriftInvoker(TServiceClient client){
		this.client = client;
	}
	
	
	
	@Override
	public Class<T> getInterface() {
		return clazz;
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws RpcException {
		if(log.isDebugEnabled())
			log.debug("invoking thrift method {}.{}", clazz.getName(), method.getName());
		try {
			return method.invoke(client, args);
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new RpcException();
	}

}
