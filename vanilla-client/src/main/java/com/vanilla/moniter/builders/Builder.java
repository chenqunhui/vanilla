package com.vanilla.moniter.builders;

import org.apache.log4j.spi.LoggingEvent;

import com.vanilla.moniter.beans.MonitLogJava;
import com.vanilla.utils.IpUtils;

public class Builder {

	static String localIp = IpUtils.getLocalHostIP();
	
	public static MonitLogJava buildJavaLog(LoggingEvent event){
		MonitLogJava log = new MonitLogJava();
		log.setLevel(event.getLevel().toString());
		log.setLocalAddress(localIp);
		log.setLoggerName(event.getLoggerName());
		log.setMsg(event.getMessage().toString());
		log.setMsgId("12131515");
		log.setThreadName(event.getThreadName());
		log.setTimestamp(event.getTimeStamp());
		return log;
	}
}
