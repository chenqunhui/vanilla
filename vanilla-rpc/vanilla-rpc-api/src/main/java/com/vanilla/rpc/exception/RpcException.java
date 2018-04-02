package com.vanilla.rpc.exception;

public class RpcException extends Exception {
	
	private static final long serialVersionUID = -3431645130597580850L;

	public RpcException(String message){
		super(message);
	}
}
