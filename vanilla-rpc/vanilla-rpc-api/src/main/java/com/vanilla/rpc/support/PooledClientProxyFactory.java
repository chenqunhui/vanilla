package com.vanilla.rpc.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;

import com.vanilla.common.URL;
import com.vanilla.rpc.Invoker;
import com.vanilla.rpc.exception.RpcException;
import com.vanilla.rpc.loadbalance.LoadBalance;

public abstract class PooledClientProxyFactory implements ClientProxyFactory<Invoker>{

	private GenericKeyedObjectPool<String, Invoker> pool;
	
	private LoadBalance loadBalance;
	
	private String methodName;
	
	private URL url;
	
	private List<URL> caches = new ArrayList<URL>();
	

	public Invoker getObject(){
		try {
			return loadBalance.select(null, url);
		} catch (RpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
}
