package com.vanilla.remoting.spi;

public enum MessageType {

	HEARTBEAT(0),
	HEARTBEAT_RESP(1),
	MONIT_LOG(2),
	NOMARL(3);
	
	MessageType(int code){
		this.code = code;
	}
	
	private int code ;

	public int getCode() {
		return code;
	}
}
