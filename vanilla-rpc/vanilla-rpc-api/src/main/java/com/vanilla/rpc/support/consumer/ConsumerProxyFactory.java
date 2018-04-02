package com.vanilla.rpc.support.consumer;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.alibaba.fastjson.JSON;
import com.vanilla.common.URL;
import com.vanilla.common.utils.StringUtils;
import com.vanilla.register.NotifyListener;
import com.vanilla.register.Registry;
import com.vanilla.register.RegistryFactory;
import com.vanilla.rpc.Constant;
import com.vanilla.rpc.InvokerFactory;
import com.vanilla.rpc.MidMan;
import com.vanilla.rpc.exception.RpcException;
import com.vanilla.rpc.hyxtrix.HystrixContext;
import com.vanilla.rpc.loadbalance.GetFirstLoadBalance;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 消费者端的单例工厂
 * 
 * @author chenqunhui
 *
 * @param <T>
 */
@Slf4j
@Data
public abstract class ConsumerProxyFactory implements FactoryBean, InitializingBean, Closeable,NotifyListener,ApplicationContextAware{

	private ApplicationContext applicationContext;
	
	private Object target;
	private Class<?> clazz;
	private Registry registry;
	private URL subscribeUrl;
	private List<URL> urls;
	
	
	private String serviceName;
	private int port;
	private String version;
	private int retry;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object getObject() throws Exception {
		return target;
	}

	@Override
	public Class<?> getObjectType() {
		if (null != clazz) {
			return clazz;
		}
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	protected abstract URL buildSubscribeUrl();
	

	@Override
	public void afterPropertiesSet() throws Exception {
		if(StringUtils.isEmpty(serviceName)){
			throw new IllegalArgumentException("Rpc factory bean's property with name 'serviceName' has not been set.");
		}
		ClassLoader loader = this.getClass().getClassLoader();
		clazz = loader.loadClass(serviceName+"$Iface");
		subscribeUrl = buildSubscribeUrl();
		registry();
		registry.subscribe(subscribeUrl, this);
	    InvokerFactory invokerFactory = null;
	    try{
	    	if(applicationContext.containsBean(Constant.DEFAULT_INVOKER_FACTORY_BEAN_NAME)){
	    		invokerFactory = (InvokerFactory)applicationContext.getBean(Constant.DEFAULT_INVOKER_FACTORY_BEAN_NAME);
	    	}
	    }catch(Exception e){
	    	
	    }
	    if(null == invokerFactory){
	    	invokerFactory = applicationContext.getBean(InvokerFactory.class);
	    }
	    if(null == invokerFactory){
	    	throw new RpcException("no invoker factory Bean can be find,please set it");
	    }
		MidMan midMan = new MidMan(registry, subscribeUrl, clazz, new GetFirstLoadBalance(),invokerFactory);
		target = Proxy.newProxyInstance(loader, new Class[]{clazz}, new ConsumerProxy(midMan, new HystrixContext()));
	}

	@Override
	public synchronized void notify(List<URL> urls) {
		if(log.isDebugEnabled()){
			log.debug("notify url is ["+JSON.toJSONString(urls)+"]");
		}
		List<URL> caches = new ArrayList<URL>();
		if(null != urls && !urls.isEmpty()){
			caches.addAll(urls);
		}
		this.urls = caches;
	}
	
	public void registry() throws RpcException{
		try{
			registry = (Registry)applicationContext.getBean(Constant.DEFAULT_REGISTRY_BEAN_NAME);
		}catch(NoSuchBeanDefinitionException e){
			//ignor
		}
		if(null != registry){
			try{
				registry = (Registry)applicationContext.getBean(Registry.class);
			}catch(Exception e){
				//
			}
			
		}
		if(null == registry){
			RegistryFactory registryFactory = null;
			try{
				registryFactory = (RegistryFactory)applicationContext.getBean(Constant.DEFAULT_REGISTRY_FACTORY_BEAN_NAME);
			}catch(NoSuchBeanDefinitionException e){
				//ignor
			}
			if(null == registryFactory){
				try{
					registryFactory = (RegistryFactory)applicationContext.getBean(RegistryFactory.class);
				}catch(Exception e){
					
				}
			}
			if(null != registryFactory){
				registry = registryFactory.getRegistry(new URL("zookeeper","127.0.0.1",2181));
			}
		}
		if(null == registry){
			throw new RpcException("no registry and factory Bean can be find,please set it");
		}
		
		
		
		
	}
}
