package com.vanilla.moniter;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

public class TestJson {

	public static void main(String[] args) {
		
		
		long st = System.currentTimeMillis();
		for(int i=0;i<10000000;i++){
			ByteBuf poolbuf =PooledByteBufAllocator.DEFAULT.directBuffer(10);
			try {
				poolbuf.writeBytes("1221421512421421444444444444444444444444444444444".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long end1 = System.currentTimeMillis();
		for(int i=0;i<10000000;i++){
			ByteBuf unpoolbuf =Unpooled.directBuffer(10);
			try {
				unpoolbuf.writeBytes("1221421512421421444444444444444444444444444444444".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end2 = System.currentTimeMillis();
		
		System.out.println("pooled is "+(end1-st));
		System.out.println("unpooled is "+(end2-end1));
	}

}
