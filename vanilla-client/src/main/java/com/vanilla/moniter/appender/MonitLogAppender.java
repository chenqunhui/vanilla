package com.vanilla.moniter.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

import com.vanilla.moniter.TcpMonitClient;

public class MonitLogAppender extends AppenderSkeleton {

	TcpMonitClient client;
	
	@Override
	protected void append(LoggingEvent event) {
		client.add(event);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean requiresLayout() {
		return false;
	}

}
