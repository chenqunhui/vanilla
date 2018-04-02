package com.vanilla.rpc.thrift.consumer;

import org.apache.commons.pool.KeyedPoolableObjectFactory;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.TServiceClientFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.vanilla.common.URL;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThriftClientFactory implements KeyedPoolableObjectFactory<URL,TServiceClient>{

	private final TServiceClientFactory<TServiceClient> clientFactory;
	private PoolOperationCallBack callback;
	private int connectTimeout;
	private int socketTimeout;
	private boolean framed = false;
	
	public ThriftClientFactory(TServiceClientFactory<TServiceClient> clientFactory){
		this.clientFactory = clientFactory;
	}

	@Override
	public TServiceClient makeObject(URL key) throws Exception {
		if(null == key){
			throw new NullPointerException("cant make thrift service client by null key");
		}
		if(log.isDebugEnabled()){
			log.debug("connecting to service instance:{} with address:{}", key.getIp()+key.getPort());
		}
        
        TSocket tsocket = new TSocket(key.getIp(), key.getPort());
        tsocket.setConnectTimeout(connectTimeout);
        tsocket.setSocketTimeout(socketTimeout);
        TTransport transport;
        if (framed) {
            transport = new TFramedTransport(tsocket);
        } else {
            transport = tsocket;
        }
        TProtocol protocol = new TBinaryProtocol(transport);
        TServiceClient client = this.clientFactory.getClient(protocol);
        transport.open();
        if (callback != null) {
            try {
                callback.make(client);
            } catch (Exception e) {
                log.warn("makeObject:{}", e);
            }
        }
        return client;
	}

	@Override
	public void destroyObject(URL key, TServiceClient obj) throws Exception {
		if (callback != null) {
            try {
                callback.destroy(obj);
            } catch (Exception e) {
                log.warn("destroyObject:{}", e);
            }
        }
        log.info("destroyObject:key = {},client = {}", key, obj);
        TTransport pin = obj.getInputProtocol().getTransport();
        pin.close();
        TTransport pout = obj.getOutputProtocol().getTransport();
        pout.close();
		
	}

	@Override
	public boolean validateObject(URL key, TServiceClient client) {
		 TTransport pin = client.getInputProtocol().getTransport();
	     TTransport pout = client.getOutputProtocol().getTransport();
	     return pin.isOpen() && pout.isOpen();
	}

	@Override
	public void activateObject(URL key, TServiceClient obj) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(URL key, TServiceClient obj) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	 static interface PoolOperationCallBack {
	        /**
	         * 销毁client之前执行
	         *
	         * @param client thrift service client
	         * @return void
	         */
	        void destroy(TServiceClient client);

	        /**
	         * 创建成功时执行
	         *
	         * @param client thrift service client
	         * @return void
	         */
	        void make(TServiceClient client);
	    }

}
