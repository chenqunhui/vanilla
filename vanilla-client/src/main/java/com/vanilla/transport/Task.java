package com.vanilla.transport;

public  interface Task extends Runnable {
	
    public String getName();

    public void shutdown();
 }