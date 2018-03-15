package com.vanilla.register.support;

public class RegistryException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RegistryException(){
		
	}
	
	public RegistryException(String message){
		super(message);
	}
	
	public RegistryException(String message,Throwable e){
		super(message,e);
	}
}
