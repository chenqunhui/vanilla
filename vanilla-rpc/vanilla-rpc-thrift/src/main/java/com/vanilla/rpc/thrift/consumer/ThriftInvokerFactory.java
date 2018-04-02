package com.vanilla.rpc.thrift.consumer;

import java.util.ArrayList;
import java.util.List;

import com.vanilla.rpc.Invoker;
import com.vanilla.rpc.InvokerFactory;
import com.vanilla.rpc.thrift.ThriftInvoker;

public class ThriftInvokerFactory implements InvokerFactory<ThriftInvoker> {

	private List<ThriftInvoker> invokers = new ArrayList<ThriftInvoker>();
	
	@Override
	public Invoker<ThriftInvoker> getInvoker() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Invoker<ThriftInvoker>> existsInvoker() {
		// TODO Auto-generated method stub
		return null;
	}

}
