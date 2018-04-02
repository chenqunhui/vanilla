package com.vanilla.rpc.thrift.provider;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

import com.vanilla.rpc.thrift.EchoService;

public class EchoServiceImpl implements EchoService.Iface {

	@Override
	public String echo(String str) throws TException {
		return str + "1124";
	}

	public static void main(String[] args) {

		TServerTransport serverTransport;
		try {
			serverTransport = new TServerSocket(9090);
			TProcessor tprocessor = new EchoService.Processor<>(new EchoServiceImpl());
			TServer server = new TSimpleServer(new Args(serverTransport).processor(tprocessor));
			server.serve();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
