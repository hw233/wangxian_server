package com.xuanzhi.tools.listcache.concrete;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import junit.framework.TestCase;
import com.xuanzhi.tools.listcache.event.*;

public class LruListCacheTestCase extends TestCase {
	
	public LruListCacheTestCase(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
		c = new Comparator(){
			public int compare(Object o1,Object o2){
				String s1 = (String)o1;
				String s2 = (String)o2;
				s1 = s1.substring(1,s1.length()-1);
				s2 = s2.substring(1,s2.length()-1);
				if(s1.indexOf(".") > 0)
					s1 = s1.substring(0,s1.indexOf("."));
				if(s2.indexOf(".") > 0)
					s2 = s2.substring(0,s2.indexOf("."));
				int i1 = Integer.parseInt(s1);
				int i2 = Integer.parseInt(s2);
				if(i1 < i2) return -1;
				if(i1 > i2) return 1;
				return 0;
			}
		};
	}
	
	/**
	 * [0...9] -> [15...19] -> [20...119] -> [120...124]  
	 * -> [125...174] -> [175...184] -> [200...209] -> [220...249]
	 * -> [250...299] -> [300...351] -> [400...499]
	 */
	LruListCache cache = null;
	Comparator c = null;
	String ids[] ;
	int n = 0;
	
	public void testPush(){
		n = 1000;
		ids = new String[n];
		for(int i = 0 ; i < n ; i++){
			ids[i] = String.valueOf("-"+i+"-");
		}

		cache = new LruListCache(c,"value","no-name");
		
		cache.initailize(1024*128,900000L,1024,4,512);
		cache.setTotalSize(n);
		
		assertEquals(0,cache.push(ids,20,20,100));
		assertEquals(0,cache.push(ids,125,125,50));
		assertEquals(0,cache.push(ids,200,200,10));
		assertEquals(0,cache.push(ids,0,0,10));
		assertEquals(0,cache.push(ids,400,400,100));
		assertEquals(0,cache.push(ids,250,250,50));
		assertEquals(0,cache.push(ids,302,302,48));
		
		BlockTreeNode node = cache.avlTree.minimum();
		assertEquals(0,node.start);
		assertEquals(10,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(20,node.start);
		assertEquals(100,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(125,node.start);
		assertEquals(50,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(200,node.start);
		assertEquals(10,node.size);

		node = cache.avlTree.next(node);
		assertEquals(250,node.start);
		assertEquals(50,node.size);

		node = cache.avlTree.next(node);
		assertEquals(302,node.start);
		assertEquals(48,node.size);

		node = cache.avlTree.next(node);
		assertEquals(400,node.start);
		assertEquals(100,node.size);

		node = cache.avlTree.next(node);
		assertNull(node);
		
		assertEquals(0,cache.push(ids,220,220,132));
		
		node = cache.avlTree.minimum();
		assertEquals(0,node.start);
		assertEquals(10,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(20,node.start);
		assertEquals(100,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(125,node.start);
		assertEquals(50,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(200,node.start);
		assertEquals(10,node.size);

		node = cache.avlTree.next(node);
		assertEquals(220,node.start);
		assertEquals(30,node.size);

		
		node = cache.avlTree.next(node);
		assertEquals(250,node.start);
		assertEquals(50,node.size);

		node = cache.avlTree.next(node);
		assertEquals(300,node.start);
		assertEquals(52,node.size);

		node = cache.avlTree.next(node);
		assertEquals(400,node.start);
		assertEquals(100,node.size);

		node = cache.avlTree.next(node);
		assertNull(node);
		
		assertEquals(0,cache.push(ids,15,15,170));
		
		node = cache.avlTree.minimum();
		assertEquals(0,node.start);
		assertEquals(10,node.size);

		node = cache.avlTree.next(node);
		assertEquals(15,node.start);
		assertEquals(5,node.size);

		node = cache.avlTree.next(node);
		assertEquals(20,node.start);
		assertEquals(100,node.size);

		node = cache.avlTree.next(node);
		assertEquals(120,node.start);
		assertEquals(5,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(125,node.start);
		assertEquals(50,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(175,node.start);
		assertEquals(10,node.size);
		
		node = cache.avlTree.next(node);
		assertEquals(200,node.start);
		assertEquals(10,node.size);

		node = cache.avlTree.next(node);
		assertEquals(220,node.start);
		assertEquals(30,node.size);

		
		node = cache.avlTree.next(node);
		assertEquals(250,node.start);
		assertEquals(50,node.size);

		node = cache.avlTree.next(node);
		assertEquals(300,node.start);
		assertEquals(52,node.size);

		node = cache.avlTree.next(node);
		assertEquals(400,node.start);
		assertEquals(100,node.size);

		node = cache.avlTree.next(node);
		assertNull(node);

		cache.add("-"+201.5+"-");
	}
	
	public void testAddObjectsInEsc() throws Exception{
		
		n = 1000;
		ids = new String[n];
		for(int i = 0 ; i < n ; i++){
			ids[i] = String.valueOf("-"+i+"-");
		}
		Comparator c1 = new Comparator(){
			public int compare(Object o1,Object o2){
				throw new RuntimeException("in compare");
			}
		};
		cache = new LruListCache(c1,"value","no-name");
		
		cache.initailize(1024*128,900000L,1024,4,512);
		cache.setTotalSize(n);

		assertEquals(0,cache.push(ids,20,20,100));
		assertEquals(0,cache.push(ids,125,125,50));
		assertEquals(0,cache.push(ids,200,200,10));
		assertEquals(0,cache.push(ids,0,0,10));

		assertEquals("cached size",170,cache.getCachedSize());
		assertEquals("block number",4,cache.getBlockNum());
		
		assertEquals(0,cache.push(ids,15,15,170));
		assertEquals("cached size",190,cache.getCachedSize());
		assertEquals("block number",7,cache.getBlockNum());
		
		assertTrue("not cover [14,35]",!cache.isCovered(14,22));
		assertTrue("cover [16,35]",cache.isCovered(16,20));
		assertTrue("cover [50,150]",cache.isCovered(50,101));
		assertTrue("cover [150,184]",cache.isCovered(150,35));
		assertTrue("not cover [150,185]",!cache.isCovered(150,36));
		
		assertEquals(0,cache.push(ids,400,400,100));
		assertEquals(0,cache.push(ids,250,250,50));
		assertEquals(0,cache.push(ids,302,302,48));
		
		assertEquals("cached size",388,cache.getCachedSize());
		assertEquals("block number",10,cache.getBlockNum());
		
		assertTrue("not cover [270 319]",!cache.isCovered(270,50));
		
		//块向下合并
		assertEquals(0,cache.push(ids,300,300,2));
		assertEquals("cached size",390,cache.getCachedSize());
		assertTrue("cover [270 319]",cache.isCovered(270,50));
		
		//块丢弃
		assertEquals(0,cache.push(ids,215,215,3));
		assertEquals("cached size",390,cache.getCachedSize());
		assertTrue("not cover [215 217]",!cache.isCovered(215,3));
		
		//块向上合并
		assertEquals(0,cache.push(ids,220,220,132));
		assertTrue("cover [300,351]",cache.isCovered(300,52));
		
		Object retIds[] = cache.get(250,102);
		for(int i = 0 ; i < retIds.length ; i ++){
			assertEquals("-"+(250+i)+"-","-"+(250+i)+"-",retIds[i]);
		}
	}


	public void testAddObjectsInDEsc() throws Exception{
		
		n = 1000;
		ids = new String[n];
		for(int i = 0 ; i < n ; i++){
			ids[i] = String.valueOf("-"+(n-1-i)+"-");
		}
		
		Comparator c1 = new Comparator(){
			public int compare(Object o1,Object o2){
				throw new RuntimeException("in compare");
			}
		};
		cache = new LruListCache(c1,"value","no-name");
		
		cache.initailize(1024*128,900000L,1024,4,512);
		cache.setTotalSize(n);

		assertEquals(0,cache.push(ids,20,20,100));
		assertEquals(0,cache.push(ids,125,125,50));
		assertEquals(0,cache.push(ids,200,200,10));
		assertEquals(0,cache.push(ids,0,0,10));

		assertEquals("cached size",170,cache.getCachedSize());
		assertEquals("block number",4,cache.getBlockNum());
		
		//块切割
		assertEquals(0,cache.push(ids,15,15,170));
		assertEquals("cached size",190,cache.getCachedSize());
		assertEquals("block number",7,cache.getBlockNum());
		
		assertTrue("not cover [14,35]",!cache.isCovered(14,22));
		assertTrue("cover [16,35]",cache.isCovered(16,20));
		assertTrue("cover [50,150]",cache.isCovered(50,101));
		assertTrue("cover [150,184]",cache.isCovered(150,35));
		assertTrue("not cover [150,185]",!cache.isCovered(150,36));
		
		assertEquals(0,cache.push(ids,400,400,100));
		assertEquals(0,cache.push(ids,250,250,50));
		assertEquals(0,cache.push(ids,302,302,48));
		
		assertEquals("cached size",388,cache.getCachedSize());
		assertEquals("block number",10,cache.getBlockNum());
		
		assertTrue("not cover [270 319]",!cache.isCovered(270,50));
		
		//块向上合并
		assertEquals(0,cache.push(ids,300,300,2));
		assertEquals("cached size",390,cache.getCachedSize());
		assertTrue("cover [270 319]",cache.isCovered(270,50));
		
		//块向下合并
		assertEquals(0,cache.push(ids,220,220,132));
		
		assertTrue("cover [300,351]",cache.isCovered(300,52));
		
		Object retIds[] = cache.get(250,102);
		for(int i = 0 ; i < retIds.length ; i ++){
			assertEquals("-"+(250+i)+"-","-"+(999-250-i)+"-",retIds[i]);
		}
	}

	public void testRemoveObjects() throws Exception{
		
		n = 1000;
		ArrayList al = new ArrayList();
		
		for(int i = 0 ; i < n ; i++){
			al.add(String.valueOf("-"+i+"-"));
		}
		
		ids = (String[])al.toArray(new String[0]);

		cache = new LruListCache(c,"value","no-name");
		
		cache.initailize(1024*128,900000L,1024,4,512);
		cache.setTotalSize(n);

		assertEquals(0,cache.push(ids,20,20,100));
		assertEquals(0,cache.push(ids,125,125,50));
		assertEquals(0,cache.push(ids,200,200,10));
		assertEquals(0,cache.push(ids,0,0,10));
		assertEquals(0,cache.push(ids,400,400,100));
		assertEquals(0,cache.push(ids,250,250,50));
		assertEquals(0,cache.push(ids,302,302,48));
		assertEquals(0,cache.push(ids,220,220,132));
		assertEquals(0,cache.push(ids,15,15,170));
		assertEquals(11,cache.getBlockNum());
		
		int size = cache.getCachedSize();
		int blockNum = cache.getBlockNum();
		cache.remove("-"+25+"-");
		assertEquals(1,size - cache.getCachedSize());
		al.remove("-"+25+"-");
		
		
		size = cache.getCachedSize();
		cache.remove("-"+600+"-");
		assertEquals(size,cache.getCachedSize());
		al.remove("-"+600+"-");
		
		size = cache.getCachedSize();
		cache.remove("-"+124+"-");
		assertEquals(1,size - cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		al.remove("-"+124+"-");
		
		//块向下合并
		size = cache.getCachedSize();
		cache.remove("-"+120+"-");
		assertEquals(1,size - cache.getCachedSize());
		assertEquals(blockNum-1,cache.getBlockNum());
		al.remove("-"+120+"-");

		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.remove("-"+12+"-");
		assertEquals(0,size - cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		al.remove("-"+12+"-");
		
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.remove("-"+16+"-");
		assertEquals(1,size - cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		al.remove("-"+16+"-");
		
		//块向下合并
		size = cache.getCachedSize();
		cache.remove("-"+15+"-");
		assertEquals(1,size - cache.getCachedSize());
		assertEquals(blockNum-1,cache.getBlockNum());
		al.remove("-"+15+"-");
		
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		for(int i = 203 ; i < 209 ; i++){
			cache.remove("-"+i+"-");
			al.remove("-"+i+"-");
		}
		assertEquals(6,size - cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		
		//块丢弃
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.remove("-"+200+"-");
		assertEquals(4,size - cache.getCachedSize());
		assertEquals(blockNum-1,cache.getBlockNum());
		al.remove("-"+200+"-");
		
		
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		for(int i = 175 ; i < 181 ; i++){
			cache.remove("-"+i+"-");
			al.remove("-"+i+"-");
		}
		assertEquals(6,size - cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		
		//块向上合并
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.remove("-"+181+"-");
		assertEquals(1,size - cache.getCachedSize());
		assertEquals(blockNum-1,cache.getBlockNum());
		al.remove("-"+181+"-");
		
		
		ids = (String[])al.toArray(new String[0]);
		assertEquals(0,cache.push(ids,5,5,500)); //全部校验
	}
	
	public void testAddObjectsForSmallMaxBlockSize() throws Exception{
		
		n = 1000;
		ids = new String[n];
		for(int i = 0 ; i < n ; i++){
			ids[i] = String.valueOf("-"+i+"-");
		}
		
		Comparator c = new Comparator(){
			public int compare(Object o1,Object o2){
				return 0;
			}
		};
		cache = new LruListCache(c,"value","no-name");
		
		cache.initailize(1024*128,900000L,32,4,512);
		cache.setTotalSize(n);
		
		assertEquals(0,cache.push(ids,100,100,300));
		assertEquals(300/32+1,cache.getBlockNum());
		assertEquals(300,cache.getCachedSize());
	}
	
	public void testAddObject() throws Exception{
		n = 1000;
		ArrayList al = new ArrayList();
		
		for(int i = 0 ; i < n ; i++){
			al.add(String.valueOf("-"+(i*3)+"-"));
		}
		
		ids = (String[])al.toArray(new String[0]);
		
		Comparator c = new Comparator(){
			public int compare(Object o1,Object o2){
				String s1 = (String)o1;
				String s2 = (String)o2;
				s1 = s1.substring(1,s1.length()-1);
				s2 = s2.substring(1,s2.length()-1);
				if(s1.indexOf(".") > 0)
					s1 = s1.substring(0,s1.indexOf("."));
				if(s2.indexOf(".") > 0)
					s2 = s2.substring(0,s2.indexOf("."));
				int i1 = Integer.parseInt(s1);
				int i2 = Integer.parseInt(s2);
				if(i1 < i2) return -1;
				if(i1 > i2) return 1;
				return 0;
			}
		};
		cache = new LruListCache(c,"value","no-name");
		
		cache.initailize(1024*128,900000L,128,4,512);
		cache.setTotalSize(n);
		

		assertEquals(0,cache.push(ids,20,20,100));
		assertEquals(0,cache.push(ids,125,125,50));
		assertEquals(0,cache.push(ids,200,200,10));
		assertEquals(0,cache.push(ids,0,0,10));
		assertEquals(0,cache.push(ids,400,400,100));
		assertEquals(0,cache.push(ids,250,250,50));
		assertEquals(0,cache.push(ids,302,302,48));
		assertEquals(0,cache.push(ids,220,220,132));
		assertEquals(0,cache.push(ids,15,15,170));
		
		int size = cache.getCachedSize();
		int blockNum = cache.getBlockNum();
		cache.add("-"+604+"-");
		assertEquals(size+1,cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());

		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.add("-"+60.1+"-");
		assertEquals(size+1,cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.add("-"+60.2+"-");
		assertEquals(size+1,cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());

		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.add("-"+63+"-");
		assertEquals(size,cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.add("-"+1200+"-");
		assertEquals(size,cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		
		
		size = cache.getCachedSize();
		blockNum = cache.getBlockNum();
		cache.add("-"+60+"-");
		assertEquals(size,cache.getCachedSize());
		assertEquals(blockNum,cache.getBlockNum());
		
	}
	
	public void testUpdateObject() throws Exception{
		n = 1000;
		ArrayList al = new ArrayList();
		final HashMap<String,Integer> map = new HashMap<String,Integer>();
		for(int i = 0 ; i < n ; i++){
			al.add(String.valueOf("-"+i+"-"));
			map.put(String.valueOf("-"+i+"-"),new Integer(i));
		}
		
		ids = (String[])al.toArray(new String[0]);
		c = new Comparator(){
			public int compare(Object o1,Object o2){
				Integer i1 = map.get(o1);
				Integer i2 = null;
				if(o2 instanceof OldObject){
					OldObject ov = (OldObject)o2;
					i2 = (Integer)ov.oldValue;
				}else{
					i2 = map.get(o2);
				}
				if(i1.intValue() < i2.intValue()) return -1;
				if(i1.intValue() > i2.intValue()) return 1;
				return 0;
			}
		};
		
		cache = new LruListCache(c,"xxxxxx","no-name");
		
		cache.initailize(1024*128,900000L,1024,4,512);
		cache.setTotalSize(n);

		assertEquals(0,cache.push(ids,20,20,100));
		assertEquals(0,cache.push(ids,125,125,50));
		assertEquals(0,cache.push(ids,200,200,10));
		assertEquals(0,cache.push(ids,0,0,10));
		assertEquals(0,cache.push(ids,400,400,100));
		assertEquals(0,cache.push(ids,250,250,50));
		assertEquals(0,cache.push(ids,302,302,48));
		assertEquals(0,cache.push(ids,220,220,132));
		assertEquals(0,cache.push(ids,15,15,170));
		
		Integer old = map.get("-200-");
		map.put("-200-",new Integer(300));
		ListCacheEventHandler.fire(new PropertyChangeEvent("-200-","xxxxxx",old,"*"));
		
		BlockTreeNode tn = cache.avlTree.find("-250-");
		assertEquals(249,tn.start);
		
		tn = cache.avlTree.find("-200-");
		assertEquals(299,tn.start);
		assertEquals(53,tn.size);
		
		old = map.get("-201-");
		map.put("-201-",new Integer(360));
		ListCacheEventHandler.fire(new PropertyChangeEvent("-201-","xxxxxx",old,"*"));
		
		tn = cache.avlTree.find("-250-");
		assertEquals(248,tn.start);
		
		tn = cache.avlTree.find("-201-");
		assertEquals(400,tn.start);
		assertEquals(100,tn.size);
		

	}

}
