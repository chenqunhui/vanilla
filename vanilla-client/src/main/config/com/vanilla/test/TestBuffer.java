package com.vanilla.test;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;

public class TestBuffer {

	public static void main(String[] args) {
		System.out.println(PlatformDependent.maxDirectMemory()/1024/1024);
		long st = System.currentTimeMillis();
		try {
			System.out.println("1221421512421421444444444444444444444444444444444".getBytes("UTF-8").length);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<10000000;i++){
			ByteBuf poolbuf =PooledByteBufAllocator.DEFAULT.directBuffer(50);
			try {
				poolbuf.writeBytes("1221421512421421444444444444444444444444444444444".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			poolbuf.release();
		}
		
		long end1 = System.currentTimeMillis();
		for(int i=0;i<10000000;i++){
			ByteBuf unpoolbuf =Unpooled.directBuffer(50);
			try {
				unpoolbuf.writeBytes("1221421512421421444444444444444444444444444444444".getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			unpoolbuf.release();
		}
		long end2 = System.currentTimeMillis();
		
		System.out.println("pooled is "+(end1-st));
		System.out.println("unpooled is "+(end2-end1));
	}

}
