package com.vanilla.push;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class PushClientHandler extends ChannelInboundHandlerAdapter{
	
	private BufferedOutputStream bufferOut;
	private FileWriter writer;
	
	public  PushClientHandler(){
		File log = new File("/Users/chenqunhui/Document/log.txt"); 
		try {
			writer = new FileWriter(log, true);
			//bufferOut = new BufferedOutputStream(new FileOutputStream(log,true),4096*2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		writer.write(msg.toString());
		writer.write(System.getProperty("line.separator"));
		writer.flush();
        ctx.fireChannelRead(msg);
    }
	
	
}
