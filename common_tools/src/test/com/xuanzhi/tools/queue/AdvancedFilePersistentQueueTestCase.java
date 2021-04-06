package com.xuanzhi.tools.queue;

import junit.framework.*;

import java.io.*;
import java.util.Properties;

import org.apache.log4j.*;

public class AdvancedFilePersistentQueueTestCase extends TestCase{
	
	public AdvancedFilePersistentQueueTestCase(){
		super("");
		Properties props = new Properties();
		props.setProperty("log4j.rootLogger","WARN,A");
		props.setProperty("log4j.appender.A","org.apache.log4j.ConsoleAppender");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		PropertyConfigurator.configure(props);
	}
	
	public void testA() throws Exception{
		
		File dir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "queueA");
		if(dir.exists() == false)
			dir.mkdir();
		
		AdvancedFilePersistentQueue aq = new AdvancedFilePersistentQueue(dir,5,1024 * 1024);
		
		int n = 1000;
		byte da[] = new byte[4 * 1024];
		
		for(int i = 0 ; i < n ; i++){
			aq.push(new TestItem("Item_"+i,da));
		}
		
		Assert.assertTrue(!aq.isEmpty());
		Assert.assertTrue(!aq.isFull());
		
		for(int i = 0 ; i < n ; i++){
			TestItem ti = (TestItem)aq.pop();
			Assert.assertNotNull(ti);
			
			Assert.assertEquals("Item_"+i,ti.name);
		}
		
		aq.clear();
	}
	
	
	public void testB() throws Exception{
		File dir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "queueB");
		if(dir.exists() == false)
			dir.mkdir();
		
		final AdvancedFilePersistentQueue aq = new AdvancedFilePersistentQueue(dir,10,25 * 1024 * 1024);
		
		try{
			final int n = 1000;
			final byte da[] = new byte[4 * 1024];
			final byte da2[] = new byte[16 * 1024];
			
			for(int i = 0 ; i < n ; i++){
				aq.push(new TestItem("Item_"+i,da));
			}
		
			Thread t1 = new Thread(new Runnable(){
				public void run() {
					for(int i = 0 ; i < 100000 ; i++){
						boolean b = false;
						while(b == false){
							if(i % 2 == 0)
								b = aq.push(new TestItem("Item_"+(n+i),da));
							else
								b = aq.push(new TestItem("Item_"+(n+i),da2));
							
							try{
								if(b == false)
									Thread.currentThread().sleep((long)Math.random()*100);
								else
									Thread.currentThread().sleep((long)Math.random()*20);
							}catch(Exception e){
								
							}
						}
						
						if(i % 1000 == 0){
							System.out.println("[push-thread] [i="+(i)+"] "+aq.getStatusAsString());
						}
					}
				}});
			t1.start();
			
			Thread t2 = new Thread(new Runnable(){
				public void run() {
					for(int i = 0 ; i < 101000 ; ){
						TestItem s = (TestItem)aq.pop();
						if(s == null)
						{
							try{
									Thread.currentThread().sleep((long)Math.random()*100);
							}catch(Exception e){
								
							}
						}else{
							Assert.assertEquals("{Item_"+(i)+"}{"+s.name+"}","Item_"+i,s.name);
							i++;
							try{
								Thread.currentThread().sleep((long)Math.random()*50);
							}catch(Exception e){
							
							}
						}
						
						if(i % 1000 == 0){
							System.out.println("[pop-thread] [i="+(i)+"] "+aq.getStatusAsString());
						}
					}
				}});
			t2.start();
		
			t2.join();
			
			Assert.assertTrue(aq.isEmpty());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			aq.clear();
		}
	}

	public void testTimeout() throws Exception{
		File dir = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "queueC");
		if(dir.exists() == false)
			dir.mkdir();
		final AdvancedFilePersistentQueue aq = new AdvancedFilePersistentQueue(dir,10,25 * 1024 * 1024);
		
		long t = System.currentTimeMillis();
		TestItem ti = (TestItem)aq.pop(5000L);
		t = System.currentTimeMillis() - t;
		
		Assert.assertNull(ti);
		Assert.assertTrue(t - 5000L >= 0);
		Assert.assertTrue(t - 5000L < 10L);
		
		int n = 10;
		Thread ts[] = new Thread[n];
		for(int i = 0 ; i < n ; i++){
			ts[i] = new Thread(new Runnable(){
				public void run() {
					long tt = System.currentTimeMillis();
					TestItem ti = (TestItem)aq.pop(0);
					tt = System.currentTimeMillis() - tt;
					Assert.assertTrue(tt - 5000L >= 0L);
					Assert.assertTrue(tt - 5000L < 100L);
				}
			});
			ts[i].start();
		}
		Thread.sleep(5000L);
		
		for(int i = 0 ; i < n ; i++){
			ti = new TestItem("x"+i,new byte[10]);
			aq.push(ti);
		}
		
		for(int i = 0 ; i < n ; i++){
			ts[i].join();
		}
		Assert.assertTrue(aq.isEmpty());
	}
	
	public static class TestItem implements Serializable{
		public String name ;
		public byte data[];
		
		public TestItem(String n,byte[] d){
			name = n;
			data = d;
		}
	}
}
