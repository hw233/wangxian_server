package com.xuanzhi.tools.cache.diskcache.concrete.testcase;

import java.io.File;
import java.io.Serializable;

import com.xuanzhi.tools.cache.diskcache.RemovedListener;
import com.xuanzhi.tools.cache.diskcache.concrete.DataBlock;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

import junit.framework.TestCase;

public class DiskCacheTestCase extends TestCase{

	public DiskCacheTestCase(){
		super("DiskCacheTestCase");
	}
	
	public void setUp() throws Exception{
		
	}
	
	public void tearDown() throws Exception{
		
	}
	
	public void testFirstCase() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"abcd.ddc");
		System.out.println(dataFile.getAbsolutePath());
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		int n = 10;
		for(int i = 0 ; i < n ; i ++){
			ddc.put("abc"+i,new Data("abc"+i));
		}
		assertEquals(10,ddc.getNumElements());
		
		Data d = (Data)ddc.get("abc7");
		assertEquals("abc7",d.data);
		
		
		DefaultDiskCache ddc2 = new DefaultDiskCache(dataFile);
		assertEquals(10,ddc2.getNumElements());
		
		d = (Data)ddc2.get("abc7");
		assertEquals("abc7",d.data);
		
		ddc2.clear();
		assertEquals(0,ddc2.getNumElements());
		d = (Data)ddc2.get("abc7");
		assertNull(d);
		
	}
	
	public void testClear() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"abcd2.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		int n = 10;
		for(int i = 0 ; i < n ; i ++){
			ddc.put("xxx"+i,new Data("xxx"+i));
		}
		assertEquals(10,ddc.getNumElements());
		
		Data d = (Data)ddc.get("xxx7");
		assertEquals("xxx7",d.data);
		
		
		
		ddc.clear();
		assertEquals(0,ddc.getNumElements());
		d = (Data)ddc.get("xxx7");
		assertNull(d);
		
		DefaultDiskCache ddc3 = new DefaultDiskCache(dataFile);
		assertEquals(0,ddc3.getNumElements());
		
		d = (Data)ddc3.get("xxx7");
		assertNull(d);
	}
	
	public void testFreeBlock() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"freeblock.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		int n = 10;
		byte bytes[] = new byte[2084];
		for(int i = 0 ; i < n ; i ++){
			for(int j = 0 ; j < bytes.length ; j++){
				bytes[j] = (byte)i;
			}
			ddc.put("yyy"+i,new Data("yyy"+i,bytes));
		}
		assertEquals(10,ddc.getNumElements());
		
		ddc.remove("yyy5");
		assertEquals(10,ddc.getBlockNum());
		
		ddc.remove("yyy8");
		assertEquals(10,ddc.getBlockNum());
		
		ddc.remove("yyy7");
		assertEquals(9,ddc.getBlockNum());

		ddc.put("111111111111111111111111111111111111",new Data("11111111111111111111111111111111111",bytes));
		
		assertEquals(10,ddc.getBlockNum());
		
		ddc.remove("yyy6");
		assertEquals(9,ddc.getBlockNum());
		
		
		DefaultDiskCache ddc2 = new DefaultDiskCache(dataFile);
		Data d = (Data)ddc2.get("111111111111111111111111111111111111");
		assertEquals("11111111111111111111111111111111111",d.data);
		
		assertEquals(bytes.length,d.bytes.length);
		for(int i = 0 ; i < bytes.length ; i++){
			assertEquals(bytes[i],d.bytes[i]);
		}
		
		assertEquals(9,ddc2.getBlockNum());
		ddc2.remove("111111111111111111111111111111111111");
		
		assertEquals(7,ddc2.getBlockNum());
		
		ddc2.clear();
	}
	
	public void testRemove() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"remove.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		
		int n = 4;
		byte bytes[] = new byte[2084];
		for(int i = 0 ; i < n ; i ++){
			ddc.put("Remove"+i,new Data("Remove"+i,bytes));
		}
		assertEquals(4,ddc.getBlockNum());
		ddc.remove("Remove"+2);
		assertEquals(4,ddc.getBlockNum());
		assertNull(ddc.get("Remove"+2));
		
		ddc.clear();
	}
	public void testDuplicatePut() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"DuplicatePut.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		
		int n = 4;
		byte bytes[] = new byte[2084];
		for(int i = 0 ; i < n ; i ++){
			ddc.put("DuplicatePut"+i,new Data("DuplicatePut"+i,bytes));
		}
		
		DataBlock dbs[] = ddc.getDataBlocks();
		for(int i = 0 ;i < dbs.length ;i++){
			System.out.println("[DuplicatePut] [block-"+(i+1)+"] ["+dbs[i].offset+","+dbs[i].length+"] ["+dbs[i].containsData+"] ["+dbs[i].keyLength+"] ["+dbs[i].valueLength+"]");
		}
		
		assertEquals(4,ddc.getBlockNum());
		
		ddc.put("DuplicatePut"+2,new Data("DuplicatePut"+2,bytes));
		
		dbs = ddc.getDataBlocks();
		for(int i = 0 ;i < dbs.length ;i++){
			System.out.println("[DuplicatePut2] [block-"+(i+1)+"] ["+dbs[i].offset+","+dbs[i].length+"] ["+dbs[i].containsData+"] ["+dbs[i].keyLength+"] ["+dbs[i].valueLength+"]");
		}
			
		assertEquals(4,ddc.getBlockNum());
		
		ddc.put("DuplicatePut"+1,new Data("DuplicatePut"+1,bytes));
		
		dbs = ddc.getDataBlocks();
		for(int i = 0 ;i < dbs.length ;i++){
			System.out.println("[DuplicatePut2] [block-"+(i+1)+"] ["+dbs[i].offset+","+dbs[i].length+"] ["+dbs[i].containsData+"] ["+dbs[i].keyLength+"] ["+dbs[i].valueLength+"]");
		}
		
		assertEquals(4,ddc.getBlockNum());
		
		ddc.clear();
	}
	
	public void testIdleTimeout2() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"idletimeout2.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		ddc.setDefaultIdleTimeout(10000L);
		int n = 5000;
		byte bytes[] = new byte[2084];
		for(int i = 0 ; i < n ; i ++){
			for(int j = 0 ; j < bytes.length ; j++){
				bytes[j] = (byte)i;
			}
			ddc.put("zzzz"+i,new Data("zzzz"+i,bytes));
		}
		assertEquals(5000,ddc.getNumElements());
	
		Thread.sleep(15000);
	
		assertEquals(0,ddc.getNumElements());
		
		ddc.clear();
	}
	
	public void testIdleTimeout() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"idletimeout.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		int n = 10;
		byte bytes[] = new byte[2084];
		for(int i = 0 ; i < n ; i ++){
			for(int j = 0 ; j < bytes.length ; j++){
				bytes[j] = (byte)i;
			}
			ddc.put("zzzz"+i,new Data("zzzz"+i,bytes),0L,(i+5)*1000L);
		}
		assertEquals(10,ddc.getNumElements());
	
		Thread.sleep(10000);
		
		ddc.remove("abcd");
		ddc.remove("zzzz9");
		
		Thread.sleep(5000);
		
		assertEquals(0,ddc.getNumElements());
		assertEquals(1,ddc.getBlockNum());
		
		ddc.clear();
	}
	
	
	
	public void testMemorySize() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"memery.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		ddc.setMaxMemorySize(32 * 1024);
		
		int n = 32;
		byte bytes[] = new byte[2084];
		for(int i = 0 ; i < n ; i ++){
			for(int j = 0 ; j < bytes.length ; j++){
				bytes[j] = (byte)i;
			}
			ddc.put("memory"+i,new Data("memory"+i,bytes));
		}
		
		DataBlock dbs[] = ddc.getDataBlocks();
		long keyLength = 0;
		long valueLength = 0;
		for(int i = 0 ;i < dbs.length ;i++){
			System.out.println("[block-"+(i+1)+"] ["+dbs[i].offset+","+dbs[i].length+"] ["+dbs[i].containsData+"] ["+dbs[i].keyLength+"] ["+dbs[i].valueLength+"]");
			if(dbs[i].containsData){
				keyLength += dbs[i].keyLength;
				valueLength += dbs[i].valueLength;
			}
		}
		System.out.println("keyLength="+keyLength+",valueLength="+valueLength+",total="+(keyLength+valueLength));
		
		
		long ms = ddc.getCurrentMemorySize();
		Data d = (Data)ddc.get("abc");
		assertNull(d);
		long ms2 = ddc.getCurrentMemorySize();
		System.out.println("memory size free: ms="+ms+",ms2="+ms2+",max="+ddc.getMaxMemorySize());
		
		
		assertTrue(ms2 < ms);
		assertTrue(ms2 < ddc.getMaxMemorySize());
		
		ddc.clear();
		
	}
	
	public void testDiskSize() throws Exception{
		File dataFile = new File(System.getProperty("user.dir")+File.separator+"memery.ddc");
		DefaultDiskCache ddc = new DefaultDiskCache(dataFile);
		ddc.setMaxDiskSize(32 * 1024);
		
		int n = 32;
		byte bytes[] = new byte[1024];
		for(int i = 0 ; i < n ; i ++){
			for(int j = 0 ; j < bytes.length ; j++){
				bytes[j] = (byte)i;
			}
			ddc.put("disksize"+i,new Data("disksize"+i,bytes));
		}
		System.out.println(ddc);
		ddc.clear();
		
	}
	
	
	public static class Data implements Serializable,RemovedListener{
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
			System.out.println("remove Data ["+data+"] by ["+s+"]");
			
		}
	}
}
