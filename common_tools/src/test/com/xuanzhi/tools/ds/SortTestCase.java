package com.xuanzhi.tools.ds;

import junit.framework.*;

public class SortTestCase extends TestCase {

	public void testA(){
		String s = "大厦法就流口水$bu了宽带计费";
		
		s = s.replaceAll("\\$bu", "" + 100);
		
		System.out.println(s);
	}
	
	public void testHeapSort(){
		DataComparator dc = new DataComparator();
		int n = 10000;
		Data data[] = new Data[n];
		for(int i = 0 ; i < n ; i++){
			data[i] = new Data((int)(Math.random() * n),(int)(Math.random() * n)); 
		}
		
		long l = System.currentTimeMillis();
		MaxHeap.heapsort(data, data.length, dc);
		l = System.currentTimeMillis() - l;
		System.out.println("heapsort: n = " + n + ",cost "+l+" ms");
		int i = 0;
		try{
			for(i = 0 ; i < n-1 ; i++){
				 assertTrue(dc.compare(data[i+1], data[i])>=0);
			}
		}catch(AssertionFailedError e){
			System.out.println("heapsort error: " + data[i] + " -- " + data[i+1]);
			throw e;
		}
	}
	
	public void testQuickSort(){
		DataComparator dc = new DataComparator();
		int n = 10000;
		int m = 2 * n;
		Data data[] = new Data[n];
		for(int i = 0 ; i < n ; i++){
			data[i] = new Data((int)(Math.random() * n),(int)(Math.random() * n));
		}
		
		long l = System.currentTimeMillis();
		java.util.Arrays.sort(data,dc);
		l = System.currentTimeMillis() - l;
		System.out.println("quicksort: n = " + n + ",cost "+l+" ms");
		int i = 0;
		try{
			for(i = 0 ; i < n-1 ; i++){
				 assertTrue(dc.compare(data[i+1], data[i])>=0);
			}
		}catch(AssertionFailedError e){
					System.out.println("heapsort error: " + data[i] + " -- " + data[i+1]);

			throw e;
		}
	}
	
	public void testQuickSortForModify(){
		DataComparator dc = new DataComparator();
		int n = 10000;
		int m = 2 * n;
		Data data[] = new Data[n];
		for(int i = 0 ; i < n ; i++){
			data[i] = new Data((int)(Math.random() * n),(int)(Math.random() * n));
		}
		
		long l = System.currentTimeMillis();
		java.util.Arrays.sort(data,dc);
		l = System.currentTimeMillis() - l;
		System.out.println("quicksort: n = " + n + ",cost "+l+" ms");
		
		int c = 0;
		for(int i = 0 ; i < n ; i++){
			if(Math.random() > 0.999){
				data[i] = new Data((int)(Math.random() * n),(int)(Math.random() * n));
				c++;
			}
		}
		l = System.currentTimeMillis();
		java.util.Arrays.sort(data,dc);
		l = System.currentTimeMillis() - l;
		System.out.println("quicksort2: n = " + n + ", c = "+c+", cost "+l+" ms");
		
	}
	
	public static class Data{
		int x;
		int y;
		public Data(int x,int y){
			this.x = x;
			this.y = y;
		}
		
		public String toString(){
			return "("+x+","+y+")";
		}
	}
	
	public static class DataComparator implements java.util.Comparator{

		public int compare(Object o1, Object o2) {
			if(o1 == o2) return 0;
			if(((Data)o1).x < ((Data)o2).x) return -1;
			if(((Data)o1).x > ((Data)o2).x) return 1;
			if(((Data)o1).y < ((Data)o2).y) return -1;
			if(((Data)o1).y > ((Data)o2).y) return 1;
			return 0;
		}
		
	}
}
