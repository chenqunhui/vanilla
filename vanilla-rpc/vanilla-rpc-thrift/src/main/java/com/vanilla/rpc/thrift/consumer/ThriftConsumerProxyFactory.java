package com.vanilla.rpc.thrift.consumer;

import java.util.HashMap;
import java.util.Map;

import com.vanilla.common.URL;
import com.vanilla.common.utils.IpUtils;
import com.vanilla.rpc.support.consumer.ConsumerProxyFactory;

public class ThriftConsumerProxyFactory extends  ConsumerProxyFactory{

	private final String protocl ="thrift";
	
	@Override
	protected URL buildSubscribeUrl() {
		String ip = IpUtils.getLocalHostIP();
		Map<String,String> parameter  = new HashMap<String,String>();
		parameter.put("version", getVersion());
		parameter.put("retry", String.valueOf(getRetry()));
		return new URL(protocl,ip,getPort(),parameter);
	}

}
