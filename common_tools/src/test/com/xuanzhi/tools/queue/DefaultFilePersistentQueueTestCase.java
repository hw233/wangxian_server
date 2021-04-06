package com.xuanzhi.tools.queue;

import junit.framework.*;
import java.io.*;
import java.util.Properties;

import org.apache.log4j.*;

public class DefaultFilePersistentQueueTestCase extends TestCase{
	
	public DefaultFilePersistentQueueTestCase(){
		super("");
		Properties props = new Properties();
		props.setProperty("log4j.rootLogger","INFO,A");
		props.setProperty("log4j.appender.A","org.apache.log4j.ConsoleAppender");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		
		PropertyConfigurator.configure(props);
	}

	public void testA() {
		File dir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "queueA");
		if(dir.exists() == false)
			dir.mkdir();
		
		DefaultFilePersistentQueue pq = new DefaultFilePersistentQueue(dir,16,256);
		
		try{
		
		
		for(int i = 0 ; i < 16 ; i++){
			pq.push(new String("Test-String-"+(i+1)));
		}
		
		Assert.assertTrue(!pq.isEmpty());
		Assert.assertTrue(!pq.isFull());
		
		Assert.assertEquals(0,pq.getStorageSize());
		
		for(int i = 0 ; i < 128 ; i++){
			pq.push(new String("Test-String-"+(16+i+1)));
			Assert.assertEquals("",i+1,pq.getStorageSize());
		}
		
		for(int i = 0 ; i < 64 ; ){
			String s = (String)pq.pop();
			if(s != null){
				Assert.assertEquals("Test-String-"+(i+1),s);
				i++;
			}
		}
		
		
		pq.persistForShutdown();
		
		Assert.assertEquals(0,pq.getMemerySize());
		
		pq = null;
		System.out.println("Sleeping 10s:");
		for(int i = 0 ; i < 10 ; i++){
			try{
				Thread.sleep(1000L);
			}catch(Exception e){}
			System.out.print(".");
		}
		System.out.println("done!");
		
		pq = new DefaultFilePersistentQueue(dir,32,256);
		
		Assert.assertEquals("total items :",80,pq.size());
		
		for(int i = 0 ; i < 64 ; ){
			String s = (String)pq.pop();
			if(s != null){
				Assert.assertEquals("Test-String-"+(64+i+1),s);
				i++;
			}
		}
		
		
		}finally{
			pq.clear();
		}
	}
	
	public void testB() {
		File dir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "queueB");
		if(dir.exists() == false)
			dir.mkdir();
		
		final DefaultFilePersistentQueue pq = new DefaultFilePersistentQueue(dir,4096,1024*256);
		
		try{
			for(int i = 0 ; i < 10000 ; i++){
				pq.push(new String("Test-String-"+(i+1)));
			}
		
			Thread t1 = new Thread(new Runnable(){
				public void run() {
					for(int i = 0 ; i < 100000 ; i++){
						pq.push(new String("Test-String-"+(10000+i+1)));
						try{
							Thread.currentThread().sleep((long)Math.random()*15);
						}catch(Exception e){
							
						}
						if(i % 1000 == 0){
							System.out.println("[push-thread] [i="+(i)+"]");
						}
					}
				}});
			t1.start();
			
			Thread t2 = new Thread(new Runnable(){
				public void run() {
					for(int i = 0 ; i < 110000 ; ){
						String s = (String)pq.pop();
						if(s == null)
						{
						try{
								Thread.currentThread().sleep((long)Math.random()*15);
						}catch(Exception e){
							
						}
						}else{
							Assert.assertEquals("{Test-String-"+(i+1)+"}{"+s+"}","Test-String-"+(i+1),s);
							i++;
						}
						if(i % 1000 == 0){
							System.out.println("[pop-thread] [i="+(i)+"]");
						}
					}
				}});
			t2.start();
		
			t2.join();
			
			Assert.assertEquals(0,pq.size());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			pq.clear();
		}
	}
}
