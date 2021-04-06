package com.xuanzhi.tools.cache.distribute.concrete;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.PropertyConfigurator;
import org.w3c.dom.Document;

import com.xuanzhi.tools.cache.CacheObject;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.distribute.concrete.DefaultDistributeCache;
import com.xuanzhi.tools.cache.distribute.concrete.DefaultDistributeCacheManager;
import com.xuanzhi.tools.cache.distribute.concrete.DistributeCacheGroup;
import com.xuanzhi.tools.cache.distribute.concrete.DistributeCacheStub;
import com.xuanzhi.tools.cache.distribute.DistributeCache;

import com.xuanzhi.tools.text.*;

public class SimpleOne {
	
	public static class MyObject implements Serializable,Cacheable{
		public int x;
		public int y;
		public String r;
		
		public MyObject(int x,int y){
			this.x = x;
			this.y = y;
			this.r = x + " * " + y + " = " + (x * y);
		}
		
		public String toString(){
			return r;
		}

		public int getSize() {
			// TODO Auto-generated method stub
			return 1;
		}
	}
	
	DefaultDistributeCacheManager manager;
	
	public SimpleOne(String ba,int port,int discoverIterval,int heartbit) throws Exception{
		
		String conf =  "<cache-manager class=\"\">"+
		 "<broadcast-address>"+ba+"</broadcast-address>"+
		 "<port>"+port+"</port>"+
		 "<handle-thread-num>3</handle-thread-num>"+
		 "<interval-discover>"+discoverIterval+"</interval-discover>"+
		 "<interval-heartbit>"+heartbit+"</interval-heartbit>"+
		 "<stub-max-item-num>128</stub-max-item-num>"+
		 "<stub-max-life-time>180</stub-max-life-time>"+
		 "<caches>"+
		 " 		<cache name=\"aaa\" max-size=\"256\" max-life-time=\"180\" load-factor=\"0.5\"/>"+
		 " 		<cache name=\"bbb\" max-size=\"256\" max-life-time=\"180\" load-factor=\"0.0\"/>"+
		 " 		<cache name=\"ccc\" max-size=\"256\" max-life-time=\"180\" load-factor=\"1.0\"/>"+
		 "</caches>"+
		 "</cache-manager>";
		
		Document d = XmlUtil.loadStringWithoutTitle(conf);
		
		manager = new DefaultDistributeCacheManager();
		manager.configure(d.getDocumentElement());
		
	}
	
	public void create(String cacheName,int x,int y){
		long startTime = System.currentTimeMillis();
		String key = "("+x+","+y+")";
		
		DistributeCache dc = manager.getDistributeCache(cacheName);
		if(dc == null) throw new RuntimeException("cache ["+cacheName+"] not exists.");
		MyObject o = new MyObject(x,y);
		dc.put(key,o);
		
		System.out.println("[create] [cache:"+cacheName+"] ["+key+"] ["+o+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
	}
	
	public MyObject get(String cacheName,int x,int y){
		long startTime = System.currentTimeMillis();
		String key = "("+x+","+y+")";
		
		DistributeCache dc = manager.getDistributeCache(cacheName);
		if(dc == null) throw new RuntimeException("cache ["+cacheName+"] not exists.");
		
		MyObject o = (MyObject)dc.get(key);
		
		System.out.println("[get] [cache:"+cacheName+"] ["+key+"] ["+o+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
		
		return o;
	}
	
	public void randomGet(String cacheName,int times,long intervals){
		
		DistributeCache dc = manager.getDistributeCache(cacheName);
		if(dc == null) throw new RuntimeException("cache ["+cacheName+"] not exists.");
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		
		for(int i = 0 ; i < times ; i++){
			long startTime = System.currentTimeMillis();
			int x = r.nextInt(100);
			int y = r.nextInt(100);
			String key = "("+x+","+y+")";
			MyObject o = (MyObject)dc.get(key);
			
			System.out.println("\t[random-get] ["+(i+1)+"/"+times+"] [cache:"+cacheName+"] ["+key+"] ["+o+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
			
			try{
				Thread.sleep(intervals);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void randomPut(String cacheName,int times,long intervals){
		
		DistributeCache dc = manager.getDistributeCache(cacheName);
		if(dc == null) throw new RuntimeException("cache ["+cacheName+"] not exists.");
		Random r = new Random();
		r.setSeed(System.currentTimeMillis());
		
		for(int i = 0 ; i < times ; i++){
			long startTime = System.currentTimeMillis();
			int x = r.nextInt(100);
			int y = r.nextInt(100);
			String key = "("+x+","+y+")";
			MyObject o = new MyObject(x,y);
			dc.put(key,o);
			System.out.println("\t[random-put] ["+(i+1)+"/"+times+"] ["+key+"] ["+o+"] ["+(System.currentTimeMillis() - startTime)+"ms]");
			
			try{
				Thread.sleep(intervals);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void show(String cacheName){
		DistributeCacheGroup group = manager.getGroup(cacheName);
		
		if(group == null) throw new RuntimeException("cache ["+cacheName+"] not exists.");
		DefaultDistributeCache cache = (DefaultDistributeCache) group.getCache();
		System.out.println("========= " + cacheName + " =========");
		
		System.out.println("\t ------ local cache ("+cache.getNumElements() + "/"+cache.getMaxSize()+","+cache.getMaxLifeTime()+","+cache.getLoadFactor()+")------");
		
		Iterator it = cache.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry me = (Map.Entry)it.next();
			String key = (String)me.getKey();
			CacheObject co = (CacheObject)me.getValue();
			System.out.println("\t\t" + key + " -> " + co.object);
		}
		
		List list = group.getStubs();
		it = list.iterator();
		while(it.hasNext()){
			DistributeCacheStub stub = (DistributeCacheStub)it.next();
			System.out.println("\t ------ stub cache ("+stub.getInetAddress()+") ------");
			Iterator i = stub.keySet().iterator();
			while(i.hasNext()){
				Object key = i.next();
				System.out.println("\t\t" + key);
			}
		}
	}
	
	public void showA(){
		Iterator it = manager.getGetObjectResponseMessageEntrySet().iterator();
		while(it.hasNext()){
			Map.Entry me = (Map.Entry)it.next();
			String key = (String)me.getKey();
			CacheObject co = (CacheObject)me.getValue();
			System.out.println("\t\t" + key + " -> " + co.object);
		}
	}
	
	public static void main(String args[]) throws Exception{
		
		if(args.length < 4){
			System.out.println("Usage: java -cp . SimpleOne <broadcast-address> <port> <discover-interval> <heartbit-interval> [logoLevel]");
			return;
		}
		String logoLevel = "DEBUG";
		if(args.length > 4){
			logoLevel = args[4];
		}
		
		Properties props = new Properties();
		props.setProperty("log4j.rootLogger",logoLevel +",A");
		props.setProperty("log4j.appender.A","org.apache.log4j.ConsoleAppender");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		
		PropertyConfigurator.configure(props);
		
		SimpleOne so = new SimpleOne(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		String s = null;
		
		while( (s = reader.readLine()) != null){
			if(s.trim().length() == 0) continue;
			try{
			s = s.trim();
			String cols[] = s.split("\\s+");
			
			if(cols[0].toLowerCase().equals("get")){
				if(cols.length != 4){
					System.out.println("format error: get <cachename> <x> <y>");
				}else{
					so.get(cols[1],Integer.parseInt(cols[2]),Integer.parseInt(cols[3]));
				}
			}else if(cols[0].toLowerCase().equals("put")){
				if(cols.length != 4){
					System.out.println("format error: put <cachename> <x> <y>");
				}else{
					so.create(cols[1],Integer.parseInt(cols[2]),Integer.parseInt(cols[3]));
				}
			}else if(cols[0].toLowerCase().equals("randomput")){
				if(cols.length != 4){
					System.out.println("format error: randomput <cachename> <times> <interval>");
				}else{
					so.randomPut(cols[1],Integer.parseInt(cols[2]),Long.parseLong(cols[3]));
				}
			}else if(cols[0].toLowerCase().equals("randomget")){
				if(cols.length != 4){
					System.out.println("format error: randomget <cachename> <times> <interval>");
				}else{
					so.randomGet(cols[1],Integer.parseInt(cols[2]),Long.parseLong(cols[3]));
				}
			}else if(cols[0].toLowerCase().equals("show")){
				if(cols.length != 2){
					System.out.println("format error: show <cachename>");
				}else{
					so.show(cols[1]);
				}
			}else if(cols[0].toLowerCase().equals("showa")){
				so.showA();
			}else if(cols[0].toLowerCase().equals("help")){
				System.out.println("commands:");
				System.out.println("\tget <cachename> <x> <y>");
				System.out.println("\tput <cachename> <x> <y>");
				System.out.println("\trandomput <cachename> <times> <interval>");
				System.out.println("\trandomget <cachename> <times> <interval>");
				System.out.println("\tshow <cachename>");
			}else{
				System.out.println("unrecognized command. please see help.");
			}
			}catch(Exception e){
				e.printStackTrace(System.out);
			}
		}
	}
	
}
