package com.vanilla.remoting.exchange;

import java.lang.reflect.Method;


public class MyRequest extends Request implements java.io.Serializable{

	private String methodName;
	
	private Object[] args;
	
	private String className;
	
	public MyRequest(){
		super();
	}
	

	public String getMethodName() {
		return methodName;
	}



	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}



	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	
	
}
