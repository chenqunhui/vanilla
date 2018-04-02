package com.vanilla.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.vanilla.common.URL;
import com.vanilla.register.Registry;
import com.vanilla.register.support.AbstractRegistry;
import com.vanilla.rpc.exception.RpcException;
import com.vanilla.rpc.loadbalance.LoadBalance;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MidMan <T>{

	private Class<?> clazz;
	private URL subscribUrl;
	private LoadBalance loadBalance;
	private List<Invoker<T>> invokers;
	private InvokerFactory<T> invokerFactory;
	private Registry registry;
	
	
	public MidMan(Registry registry,URL subscribUrl,Class<?> clazz, LoadBalance loadBalance,InvokerFactory<T> invokerFactory){
		this.registry = registry;
		this.subscribUrl = subscribUrl;
		this.clazz = clazz;
		this.loadBalance = loadBalance;
		this.invokerFactory = invokerFactory;
	}
	
	public Object invoke(Object proxy,Method method,Object[] args) throws RpcException{
		log.debug("invoking rpc method {}.{}", clazz.getName(), method.getName());
		Invoker<T> invoker = loadBalance.select(invokers, subscribUrl);
        if (invoker == null) {
        	invoker = invokerFactory.getInvoker();
        }
        if(null == invoker){
        	throw new RpcException("there is no provder for service "+clazz.getName()+"."+method.getName()
        							+" for registry "+registry.getUrl().getIp()+":"+registry.getUrl().getIp());
        }
        if (args != null && log.isDebugEnabled()) {
            log.debug("invoking rpc method {}.{} with args:<{}>", clazz.getName(), method.getName(), args);
        }
        return invoker.invoke(proxy, method, args);
        
        

	}
}
