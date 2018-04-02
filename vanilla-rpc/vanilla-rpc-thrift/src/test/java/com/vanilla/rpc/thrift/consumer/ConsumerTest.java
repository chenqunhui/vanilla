package com.vanilla.rpc.thrift.consumer;

import com.vanilla.common.URL;
import com.vanilla.rpc.thrift.EchoService;

public class ConsumerTest {

	public static void main(String[] args){
		URL url  = new URL("thrift","127.0.0.1",9090);
		ThriftClientProxyFactory factory = new ThriftClientProxyFactory("com.vanilla.rpc.thrift.EchoService",url);
		try {
			EchoService.Iface service = (EchoService.Iface)factory.getObject();
			System.out.println(service.echo("adaaaa"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
