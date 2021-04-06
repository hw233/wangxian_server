package com.xuanzhi.tools.ds;
import java.util.HashMap;
import java.util.Random;

import junit.framework.TestCase;

public class Int2IntMapTestCase extends TestCase{

	public void testA(){
		Int2IntMap map = new Int2IntMap();
		int n = 10000;
		for(int i = 0 ; i < n ; i++){
			map.put(i, i*2);
		}
		
		for(int i = 0 ; i < n ; i++){
			int v = map.get(i);
			assertEquals(2*i, v);
		}
		assertEquals(n,map.size());
		
		for(int i = 0 ; i < n ; i++){
			if(i % 2 == 0){
				map.remove(i);
			}
		}
		for(int i = 0 ; i < n ; i++){
			if(i % 2 == 0){
				assertEquals(false,map.containsKey(i));
			}
		}
		map.clear();
		
		assertEquals(0,map.size());
		
		for(int i = 0 ; i < n ; i++){
			assertEquals(false,map.containsKey(i));
		}
	}
	public void testB(){
		long now = System.currentTimeMillis();
		long s5 = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		
		Int2IntMap map = new Int2IntMap(-1,-1,1024*1024*2,true);
		//HashMap<Integer,Integer> map = new HashMap<Integer,Integer>(1024*1024*2);
		Random r = new Random(System.currentTimeMillis());
		
		
		int n = 1500000;
		int key[] = new int[n];
		for(int i = 0 ; i < n ; i++){
			key[i] = Math.abs(r.nextInt(n)) + 2;
			
			while(map.containsKey(key[i])){
				key[i] = Math.abs(r.nextInt(n)) + 2;
			}
			
			map.put(key[i], i*2);
		}
		long st = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		
		System.out.println("put cost "+(System.currentTimeMillis() - now)+"ms,and cost memory "+((st-s5)/1024)+"k");
		now = System.currentTimeMillis();
		for(int i = 0 ; i < n ; i++){
			int v = map.get(key[i]);
			assertEquals(2*i, v);
		}
		
		System.out.println("get cost "+(System.currentTimeMillis() - now)+"ms");
		now = System.currentTimeMillis();
		
		for(int i = 0 ; i < n ; i++){
			map.remove(key[i]);
		}
		
		System.out.println("remove cost "+(System.currentTimeMillis() - now)+"ms");
		now = System.currentTimeMillis();
		
		for(int i = 0 ; i < n ; i++){
			assertEquals(false, map.containsKey(key[i]));
		}
		
		System.out.println("containsKey cost "+(System.currentTimeMillis() - now)+"ms");

	}
	
	public static void main(String args[]){
		Int2IntMapTestCase t = new Int2IntMapTestCase();
		t.testA();
		
		t.testB();
	}
}
