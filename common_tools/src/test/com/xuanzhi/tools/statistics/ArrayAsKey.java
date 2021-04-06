package com.xuanzhi.tools.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class ArrayAsKey extends TestCase{

	public ArrayAsKey(){
		super("");
	}
	
	public void testA() {
		HashMap<Long,String> map = new HashMap<Long,String>();
		for(int i=0; i<1000000; i++) {
			map.put(new Long(i), ""+i);
		}
		long start = System.currentTimeMillis();
		Iterator<Long> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			Long key = keys.next();
			String str = map.get(key);
			
			List<String> list = new ArrayList<String>();
		}
		System.out.println("time:" + (System.currentTimeMillis()-start));
	}
	
//	
//	public void testA(){
//		
//		Dimension d1 = new Dimension(""){};
//		d1.granulas.put("operator","cmcc");
//		
//		Dimension d2 = new Dimension(""){};
//		d2.granulas.put("product","abc");
//		
//		Dimension x1 = new Dimension(""){};
//		x1.granulas.put("operator","cmcc");
//		
//		Dimension x2 = new Dimension(""){};
//		x2.granulas.put("product","abc");
//		
//		
//		HashMap<DimensionArray,Object> map = new HashMap<DimensionArray,Object>();
//		Object lock = new Object(){};
//		
//		map.put(new DimensionArray(new Dimension[]{d1,d2}),lock);
//		
//		Object l = map.get(new DimensionArray(new Dimension[]{x1,x2}));
//		
//		this.assertNotNull(l);
//		
//	}
//	
//	public static class DimensionArray{
//		Dimension ds[];
//		public DimensionArray(Dimension ds[]){
//			this.ds = ds;
//		}
//		
//		public int hashCode(){
//			int c = 0;
//			for(int i = 0 ; ds != null && i < ds.length ; i++)
//				c+= ds[i].hashCode();
//			return c;
//		}
//		
//		public boolean equals(Object o){
//			if(o instanceof DimensionArray){
//				DimensionArray d = (DimensionArray)o;
//				if(ds.length != d.ds.length)return false;
//				for(int i = 0 ; i < ds.length ; i++){
//					if(ds[i].equals(d.ds[i]) == false) return false;
//				}
//				return true;
//			}
//			return false;
//		}
//	}
}
