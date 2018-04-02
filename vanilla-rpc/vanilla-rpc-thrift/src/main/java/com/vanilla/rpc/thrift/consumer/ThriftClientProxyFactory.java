package com.vanilla.rpc.thrift.consumer;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.beans.factory.FactoryBean;

import com.vanilla.common.URL;
import com.vanilla.rpc.Constant;
import com.vanilla.rpc.thrift.ThriftInvoker;


/**
 *  客户端代理工厂
 * @author chenqunhui
 *
 */
public class ThriftClientProxyFactory implements FactoryBean{

	private URL subscribeUrl;
	private List<URL> urls = new ArrayList<URL>();
	private Class<?> clazz;
	private String serviceFullName;
	private boolean framed = false;
	private ClassLoader classLoader = this.getClass().getClassLoader();
	
	private Object proxyClient;
	
	public ThriftClientProxyFactory(String serviceName,URL url){
		urls.add(url);
		subscribeUrl = url;
		serviceFullName = serviceName;
		try {
			clazz = classLoader.loadClass(serviceFullName+"$Iface");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Object getObject() throws Exception {
		URL url = urls.get(0);
		TSocket tsocket = new TSocket(url.getIp(), url.getPort());
        tsocket.setConnectTimeout(subscribeUrl.getParameter("connectionTimeout", Constant.DEFAULT_CONNECT_TIME_OUT_MIL));
        tsocket.setSocketTimeout(subscribeUrl.getParameter("socketTimeout", Constant.DEFAULT_SOCKET_TIME_OUT_MIL));
        TTransport transport;
        if (framed) {
            transport = new TFramedTransport(tsocket);
        } else {
            transport = tsocket;
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        
        Class<?> thriftClientClass = classLoader.loadClass(serviceFullName + "$Client");
        // 加载Client.Factory类
        Class<TServiceClientFactory<TServiceClient>> fi =
                (Class<TServiceClientFactory<TServiceClient>>)
                        classLoader.loadClass(serviceFullName + "$Client$Factory");
        TServiceClientFactory<TServiceClient> clientFactory = fi.newInstance();
        
        
        TServiceClient client = clientFactory.getClient(protocol);
        transport.open();
		ThriftInvoker invoker = new ThriftInvoker(client);
		return Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new ThriftClientProxy(invoker,null));
		
	}

	@Override
	public Class<?> getObjectType() {
		return clazz;
	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

}
