package com.xuanzhi.tools.cache.distribute.concrete;


import junit.framework.*;
import java.io.*;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.log4j.*;
import org.w3c.dom.Document;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.ds.Heap;
import com.xuanzhi.tools.cache.distribute.DistributeCache;
import com.xuanzhi.tools.cache.distribute.concrete.protocol.Message;
import com.xuanzhi.tools.cache.distribute.concrete.protocol.MessageFactory;
import com.xuanzhi.tools.text.XmlUtil;

public class DistributeCacheManagerTestCase extends TestCase{

	public DistributeCacheManagerTestCase(){
		super("");
		Properties props = new Properties();
		props.setProperty("log4j.rootLogger","INFO,A");
		props.setProperty("log4j.appender.A","org.apache.log4j.ConsoleAppender");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		PropertyConfigurator.configure(props);
	}
	DefaultDistributeCacheManager managers[] = null;
	MyMessageTransport ts[] = null;
	int n = 10;
	
	public void setUp() throws Exception{
		ts = new MyMessageTransport[n];
		managers = new DefaultDistributeCacheManager[n];
		
		for(int i = 0 ; i < n  ; i ++){
			managers[i] = new DefaultDistributeCacheManager();
			ts[i] = new MyMessageTransport();
			ts[i].address = InetAddress.getByName("192.168.0."+i);
			managers[i].transport = ts[i];
		}
		
		String conf =  "<cache-manager class=\"\">"+
			 "<broadcast-address>192.168.2.255</broadcast-address>"+
			 "<port>0</port>"+
			 "<handle-thread-num>1</handle-thread-num>"+
			 "<interval-discover>600</interval-discover>"+
			 "<interval-heartbit>50</interval-heartbit>"+
			 "<stub-max-item-num>128</stub-max-item-num>"+
			 "<stub-max-life-time>180</stub-max-life-time>"+
			 "<caches>"+
			 " 		<cache name=\"aaa\" max-size=\"256\" max-life-time=\"180\" load-factor=\"0.5\"/>"+
			 " 		<cache name=\"bbb\" max-size=\"256\" max-life-time=\"180\" load-factor=\"0.0\"/>"+
			 " 		<cache name=\"ccc\" max-size=\"256\" max-life-time=\"180\" load-factor=\"1.0\"/>"+
			 "</caches>"+
			 "</cache-manager>";
			
		Document d = XmlUtil.loadStringWithoutTitle(conf);
		
		for(int i = 0 ; i < n  ; i ++){	
			managers[i].configure(d.getDocumentElement());
		}
			
		
	}
	
	public void tearDown(){
		
	}
	public void testUpdate(){
		
		DistributeCache dc = managers[0].getDistributeCache("aaa");
		Item item = new Item(5,5);
		dc.put(item.getK(),item);
		
		for(int i = 1 ; i < n ; i ++){
			dc = managers[i].getDistributeCache("aaa");
			Item item2 = (Item)dc.get(item.getK());
			Assert.assertEquals(item,item2);
		}
		
		try{
			Thread.currentThread().sleep(2000L);
		}catch(Exception e){
			
		}
		
		dc = managers[0].getDistributeCache("aaa");
		item = new Item(5,5);
		item.r = 125;
		dc.put(item.getK(),item);
		dc.update(item.getK());
		
		try{
			Thread.currentThread().sleep(2000L);
		}catch(Exception e){
			
		}
		
		for(int i = 1 ; i < n ; i ++){
			dc = managers[i].getDistributeCache("aaa");
			Item item2 = (Item)dc.get(item.getK());
			
			Assert.assertEquals(item,item2);
		}
		
	}
	
	public void testGet(){
		int m = 0;
		for(int i = 0 ; i < n ; i ++){
			DistributeCache dc = managers[i].getDistributeCache("aaa");
			
			for(int j = 0 ; j < n ; j++){
				m++;
				Item item = new Item(m,m);
				dc.put(item.getK(),item);
			}
		}
		
		for(int i = 1 ; i <= m ; i++){
			for(int j = 0 ; j < n ; j++){
				DistributeCache dc = managers[j].getDistributeCache("aaa");
				
				Item item = new Item(i,i);
				Item item2 = (Item)dc.get(item.getK());
				
				Assert.assertEquals(item,item2);
			}
		}
	}
	
	public void testMultiThreads(){
		final long m = 30000L;
		
		for(int i = 0 ; i < n ; i ++){
			DefaultDistributeCache dc = (DefaultDistributeCache)managers[i].getDistributeCache("aaa");
			dc.timeout = m;
		}
		
		long startTime = System.currentTimeMillis();
		
		Thread threads[] = new Thread[5];
		for(int i = 0 ; i < threads.length ; i++){
			threads[i] = new Thread(new Runnable(){
				public void run(){
					DistributeCache dc  = managers[0].getDistributeCache("aaa");
					Item item = new Item(10,20);
					dc.get(item.getK());
					
					//System.out.println("["+Thread.currentThread().getName()+"] wake up ...");
				}
			},"Test-Thread-"+i);
			threads[i].start();
		}
		
		Thread oT = new Thread(new Runnable(){
			public void run(){
				try {
					Thread.currentThread().sleep(m/2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				DistributeCache dc  = managers[5].getDistributeCache("aaa");
				Item item = new Item(10,20);
				dc.put(item.getK(),item);
				
				managers[0].getGroup("aaa").findObjectByBroadcast(item.getK(),m);
			}
		},"Thread-oT");
		oT.start();
		
		
		for(int i = 0 ; i < threads.length ; i++){
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		long costTime = System.currentTimeMillis() - startTime;
		
		System.out.println("In TestA, costTime=" + costTime+", timeout="+m);
		
		Assert.assertTrue(costTime < m);
		
		
	}
	
	/**
	 * 实现广播和点播
	 * @param t
	 * @param m
	 */
	public void sendMessage(MyMessageTransport t,Message m){
		m.setFromAddress(t.address);
		if(m.getSendType() == MessageFactory.SEND_TYPE_MULTICAST){
			for(int i = 0 ; i < ts.length ; i++){
				if(ts[i] != t)
					ts[i].putMessage(m);
			}
		}else{
			for(int i = 0 ; i < ts.length ; i++){
				if(ts[i].address.equals(m.getToAddress()))
					ts[i].putMessage(m);
			}
		}
	}
	
	public static class Item implements Cacheable,Serializable{
		
		public int x;
		public int y;
		public int r;
		
		public Item(int x,int y){
			this.x = x;
			this.y = y;
			this.r = x * y;
		}
		
		public int getSize(){
			return 1;
		}
		
		public String getK(){
			return "("+x+","+y+")";
		}
		
		public String toString(){
			return ""+x+" * "+y+" = "+r;
		}
		
		public boolean equals(Object o){
			if(o instanceof Item){
				Item i = (Item)o;
				return (i.x == x && i.y == y && i.r == r);
			}
			return false;
		}
	}
	
	public class MyMessageTransport implements MessageTransport{

		InetAddress address;
		
		Heap heap = null;
		
		public MyMessageTransport(){
			java.util.Comparator c = new java.util.Comparator(){

				public int compare(Object arg0, Object arg1) {
					Message m1 = (Message)arg0;
					Message m2 = (Message)arg1;
					if(m1.getPriority() > m2.getPriority())
						return -1;
					else if(m1.getPriority() < m2.getPriority())
						return 1;
					return 0;
				}};
			heap = new Heap(c);	
		}
		
		public void putMessage(Message m){
			synchronized(heap){
				heap.insert(m);
				heap.notifyAll();
			}
		}
		
		public Message receive(long timeout) {
			synchronized(heap){
				Message m = (Message)heap.extract();
				if(m != null)
					return m;
				
				try{
					if(timeout < 0) timeout = 0;
					heap.wait(timeout);
				}catch(Exception e){
					
				}
				m = (Message)heap.extract();
				if(m != null)
					return m;
			}
			return null;
			
		}

		public void send(Message message) {
			sendMessage(this,message);
		}
		
	}
}
