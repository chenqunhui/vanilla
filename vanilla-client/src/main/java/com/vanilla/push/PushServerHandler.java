  package com.vanilla.push;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import com.vanilla.remoting.exchange.MyRequest;
import com.vanilla.remoting.exchange.Response;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class PushServerHandler extends ChannelInboundHandlerAdapter{
	
	private BufferedOutputStream bufferOut;
	private FileWriter writer;
	
	public  PushServerHandler(){
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
//		writer.write(msg.toString());
//		writer.write(System.getProperty("line.separator"));
//		writer.flush();
		if(msg instanceof MyRequest){
			MyRequest request =(MyRequest)msg;
			Class<?> clazz = Class.forName(request.getClassName());
			Object obj = clazz.newInstance();
			Class<?>[] clazzs = new Class<?>[request.getArgs().length];
			for(int i=0;i< request.getArgs().length;i++){
				clazzs[i] = request.getArgs()[i].getClass();
			}
			Method method = clazz.getMethod(request.getMethodName(), clazzs);
			Object result = method.invoke(obj, request.getArgs());
			
			
			Response resp = new Response(request.getId());
			resp.setResult(result);
			ctx.writeAndFlush(resp);
		}
        //ctx.fireChannelRead(msg);
    }
	
	
}
