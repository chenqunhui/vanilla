package com.vanilla.rpc.thrift.consumer;

import javax.naming.Context;

import org.apache.thrift.TException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.vanilla.rpc.thrift.EchoService;

public class ThriftConsumerProxyFactoryTest {

	public static void main(String[] args){
		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		EchoService.Iface service = (EchoService.Iface)context.getBean("echoService");
		try {
			service.echo("3252532");
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
