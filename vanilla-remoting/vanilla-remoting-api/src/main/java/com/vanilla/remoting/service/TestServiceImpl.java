package com.vanilla.remoting.service;

public class TestServiceImpl implements TestService {
	
	public String todo(String args){
		return "now execute todo["+args+"]";
	}
}
