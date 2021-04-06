package com.xuanzhi.tools.cache.diskcache.concrete.testcase;

import java.io.File;
import java.io.Serializable;

import com.xuanzhi.tools.cache.diskcache.RemovedListener;
import com.xuanzhi.tools.cache.diskcache.concrete.DataBlock;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.testcase.DiskCacheTestCase.Data;

import junit.framework.TestCase;

public class DiskCachePresureTestCase extends TestCase{

	public DiskCachePresureTestCase(){
		super("DiskCacheTestCase");
	}
	
	public void setUp() throws Exception{
		
	}
	
	public void tearDown() throws Exception{
		
	}
	
	public void test1000()throws Exception{
		doTest(1000);
	}

	public void test10000()throws Exception{
		doTest(10000);
	}
	
	public void test50000()throws Exception{
		doTest(50000);
	}
	
	

	public void doTest(int n) throws Exception{
		int m = n/100;
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"Presure"+n+".ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		long startTime = System.currentTimeMillis();
		byte bytes[] = new byte[2084];
		for(int i = 0 ; i < n ; i ++){
			ddc.put("Presure"+(i+1),new Data("Presure"+(i+1),bytes));
			if(i % m == 0){
				System.out.print(".");
			}
		}
		System.out.println("    \nPut Done! cost "+((System.currentTimeMillis() - startTime)*0.001)+" seconds. " + toString(ddc));
		
		DefaultDiskCache ddc2 = ddc;
		
		startTime = System.currentTimeMillis();
		ddc = new DefaultDiskCache(dataFile);
		System.out.println("initailze "+n+" disk cache total cost "+((System.currentTimeMillis() - startTime)*0.001)+" seconds." + toString(ddc));

		startTime = System.currentTimeMillis();
		for(int i = 0 ; i < n ; i++){
			if(i % 3 == 0){
				ddc.remove("Presure"+(i+1));
			}
		}
		System.out.println("remove "+(n/3)+" items from "+n+" disk cache every cost "+((System.currentTimeMillis() - startTime)*3.0/n)+" milliseconds." + toString(ddc));

		startTime = System.currentTimeMillis();
		for(int i = 0 ; i < n ; i ++){
			if(i % 3 == 0){
				Data d = (Data)ddc.get("Presure"+(i+1));
				assertNull(d);
			}else{
				Data d = (Data)ddc.get("Presure"+(i+1));
				assertNotNull(d);
				assertEquals("Presure"+(i+1),d.data);
			}
		}
		long costTime = System.currentTimeMillis() - startTime;
		
		System.out.println("get items from "+n+" disk cache, every cost "+(costTime*1.0/n)+" milliseconds." + toString(ddc));

		startTime = System.currentTimeMillis();
		for(int i = 0 ; i < n ; i ++){
			if(i % 3 == 1){
				ddc.remove("Presure"+(i+1));
			}
		}
		
		System.out.println("remove "+(n/3)+" items from "+n+" disk cache every cost "+((System.currentTimeMillis() - startTime)*3.0/n)+" milliseconds." + toString(ddc));

		bytes = new byte[3600];
		
		startTime = System.currentTimeMillis();
		
		for(int i = 0 ; i < n ; i ++){
			if(i % 3 == 2){
				ddc.put("XPresure"+i,new Data("XPresure"+(i+1),bytes));
			}
		}
		costTime = System.currentTimeMillis() - startTime;
		
		System.out.println("put "+(n/3)+" items to "+n+" disk cache with many free block, every cost "+(costTime*3.0/n)+" milliseconds." + toString(ddc));

		for(int i = 0 ; i < n ; i ++){
			if(i % 3 == 2){
				Data d = (Data)ddc.get("XPresure"+i);
				assertEquals("XPresure"+(i+1),d.data);
			}
		}
		
		ddc2.clear();
		ddc.clear();
	}
	
	public String toString(DefaultDiskCache ddc){
		long total = ddc.getCacheHits()+ddc.getCacheMisses();
		if(total == 0) total = 1;
		
		return "{"+ddc.getNumElements()+","+(ddc.getCacheHits()*100/total)+"%,"+(ddc.getCurrentDiskSize()/1024)+"K/"+(ddc.getMaxDiskSize()/(1024*1024))+"M," +
		(ddc.getCurrentMemorySize()/1024)+"K/"+(ddc.getMaxMemorySize()/1024)+"K}";
	}
	
	public static class Data implements Serializable{
		private static final long serialVersionUID = -1485990921032507113L;
		public String data;
		public byte bytes[] = new byte[0];
		public Data(String d){
			this.data = d;
		}
		
		public Data(String d,byte bytes[]){
			this.data = d;
			this.bytes = bytes;
		}
		
		public void remove(int type) {
			String s = ""+type;
			if(type == RemovedListener.IDEL_TIMEOUT) s = "IDEL_TIMEOUT";
			if(type == RemovedListener.LIFE_TIMEOUT) s = "LIFE_TIMEOUT";
			if(type == RemovedListener.MANUAL_REMOVE) s = "MANUAL_REMOVE";
			if(type == RemovedListener.SIZE_OVERFLOW) s = "SIZE_OVERFLOW";
			if(type == RemovedListener.DUPLICATE_PUT) s = "DUPLICATE_PUT";
			//System.out.println("remove Data ["+data+"] by ["+s+"]");
			
		}
	}
}
