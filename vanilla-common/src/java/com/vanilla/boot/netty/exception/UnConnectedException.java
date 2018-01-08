package com.vanilla.boot.netty.exception;

public class UnConnectedException extends RuntimeException{

	private static final long serialVersionUID = -5965311545964850774L;
	
	
	public UnConnectedException(){
		super();
	}
	
	public UnConnectedException(String message){
		super(message);
	}
	
	public UnConnectedException(String message,Throwable e){
		super(message,e);
	}
	
	public UnConnectedException(Throwable e){
		super(e);
	}
}
