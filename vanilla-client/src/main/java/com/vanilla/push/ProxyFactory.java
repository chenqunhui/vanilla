package com.vanilla.push;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.cglib.proxy.Enhancer;

import com.vanilla.remoting.Client;
import com.vanilla.rpc.RpcInvocationHandler;

public class ProxyFactory {

	public static Object getProxy(Client client,Class<?> clazz) {
//		if (clazz.isInterface()) {
//			InvocationHandler h = new InvocationHandler() {
//				@Override
//				public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//					// TODO Auto-generated method stub
//					return null;
//				}
//			};
//			Class<?>[] interfaces = new Class[] { clazz };
//			return Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, h);
//		} else {
//			
//		}
		Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(clazz); // ① 设置需要被代理的类    
        enhancer.setCallback(new RpcInvocationHandler(client));   
        return enhancer.create();  
	}
}
