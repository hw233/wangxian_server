package com.xuanzhi.tools.listcache.concrete;

import com.xuanzhi.tools.listcache.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import com.xuanzhi.tools.text.StringUtil;

public class TestMemery {
	
	public static void main(String args[]) throws Exception{
		
		long startTime = System.currentTimeMillis();
		
		final HashMap<String,Person> personMap = new HashMap<String,Person>();
		
		Comparator weightC = new Comparator(){
			public int compare(Object o1,Object o2){
				Person p1 = (Person)personMap.get((String)o1);
				int w1 = p1.weight;
				int w2 = 0;
				if(o2 instanceof OldObject){
					w2 = ((Integer)((OldObject)o2).oldValue).intValue();
				}else{
					w2 = ((Person)personMap.get((String)o2)).weight;
				}
				if(w1 < w2) return -1;
				if(w1 > w2) return 1;
				return 0;
			}
		};
		
		Comparator highC = new Comparator(){
			public int compare(Object o1,Object o2){
				Person p1 = (Person)personMap.get((String)o1);
				int w1 = p1.high;
				int w2 = 0;
				if(o2 instanceof OldObject){
					w2 = ((Integer)((OldObject)o2).oldValue).intValue();
				}else{
					w2 = ((Person)personMap.get((String)o2)).high;
				}
				if(w1 < w2) return -1;
				if(w1 > w2) return 1;
				return 0;
			}
		};
		
		Comparator ageC = new Comparator(){
			public int compare(Object o1,Object o2){
				Person p1 = (Person)personMap.get((String)o1);
				int w1 = p1.age;
				int w2 = 0;
				if(o2 instanceof OldObject){
					w2 = ((Integer)((OldObject)o2).oldValue).intValue();
				}else{
					w2 = ((Person)personMap.get((String)o2)).age;
				}
				if(w1 < w2) return -1;
				if(w1 > w2) return 1;
				return 0;
			}
		};
		
		if(args.length != 5){
			System.out.println("Usage: java -cp . TestMemery <total-person> <sub-list-num> <sub-list-min-size> <block-num> <property>");
			System.exit(0);
		}
		
		int n = Integer.parseInt(args[0]);
		int m = Integer.parseInt(args[1]);
		int x = Integer.parseInt(args[2]);
		int y = Integer.parseInt(args[3]);
		boolean property = args[4].equals("true");
		
		
		for(int i = 0 ; i < n ; i++){
			String name = StringUtil.randomString(32);
			while(personMap.containsKey(name)){
				name = StringUtil.randomString(32);
			}
			int weight = 50 + (int)(Math.random() * 100);
			int high = 150 + (int)(Math.random() * 50);
			int age = 20 + (int)(Math.random() * 20);
			
			Person p = new Person(name,weight,high,age);
			personMap.put(name,p);
		}
		String weightPs[] = personMap.keySet().toArray(new String[0]);
		Arrays.sort(weightPs,weightC);
		
		String highPs[] = personMap.keySet().toArray(new String[0]);
		Arrays.sort(highPs,highC);
		
		String agePs[] = personMap.keySet().toArray(new String[0]);
		Arrays.sort(agePs,ageC);
		
		ArrayList al = new ArrayList();
		int xxx = m/100;
		while(al.size() < m){
			String s = StringUtil.randomString(2);
			String subWP[] = search(weightPs,s);
			if(subWP.length > x){
				al.add(s);
				if(al.size() % xxx == 0) System.out.print(".");
			}
		}
		System.out.println("\n[find-sub-string] ["+m+"] cost "+(System.currentTimeMillis() - startTime)+" ms");
		startTime = System.currentTimeMillis();
		
		ArrayList Al = new ArrayList(); // 所有的序列
		for(int i = 0 ; i < al.size() ; i++){
			String s = (String)al.get(i);
			String[][] xx = new String[3][];
			xx[0] = search(weightPs,s);
			xx[1] = search(highPs,s);
			xx[2] = search(agePs,s);
			Al.add(xx);
			if((i+1) % xxx == 0) System.out.print(".");
		}
		System.out.println();
		System.gc();
		long startTotal = Runtime.getRuntime().totalMemory();
		
		System.out.println("[create-sub-arrays] ["+StringUtil.addcommas(Runtime.getRuntime().maxMemory())+"] ["+StringUtil.addcommas(startTotal)+"] ["+StringUtil.addcommas(Runtime.getRuntime().freeMemory())+"] ["+(System.currentTimeMillis() - startTime)+"]");
		
		startTime = System.currentTimeMillis();
		
		int totalSize = 0;
		int blocknum = 0;
		///////////////////////////////////////////////////////////////////
		//create listcache
		HashMap<String,LruListCache> cacheMap = new HashMap<String,LruListCache>();
		for(int i = 0 ; i < al.size() ;i++){
			String s = (String)al.get(i);
			String[][] xx = (String[][])Al.get(i);
			LruListCache llcs[] = new LruListCache[3];
			if(property){
				llcs[0] = new LruListCache(weightC,"weight",s+"-weight");
				llcs[1] = new LruListCache(highC,"high",s+"-high");
				llcs[2] = new LruListCache(ageC,"age",s+"-age");
			}else{
				llcs[0] = new LruListCache(weightC,null,s+"-weight");
				llcs[1] = new LruListCache(highC,null,s+"-high");
				llcs[2] = new LruListCache(ageC,null,s+"-age");
			}
			for(int j = 0 ; j < 3 ; j++){
				llcs[j].initailize(n,1800000L,256,4,1024*128);
				String ids[] = xx[j];
				llcs[j].setTotalSize(ids.length);
				for(int k = 0 ; k < y ; k++){
					int start = (int)(Math.random() * ids.length);
					int size = (int)(Math.random() * (ids.length - start)/y);
					if(start >= 0 && size > 0 && start + size <= ids.length){
						llcs[j].push(ids,start,start,size);
					}
				}
				totalSize += llcs[j].getCachedSize();
				blocknum += llcs[j].getBlockNum();
				cacheMap.put(llcs[j].getName(),llcs[j]);
			}
		}
		
		
		int count = 0;
		Iterator it = cacheMap.values().iterator();
		while(it.hasNext()){
			LruListCache c = (LruListCache)it.next();
			System.out.println("["+c.getName()+"] " + c.getCachedSize() + " " + c.getBlockNum());
			count++;
			if(count > 30) break;
		}
		System.gc();
		long endTotal = Runtime.getRuntime().totalMemory();
		System.out.println("All ListCache ["+cacheMap.size()+"] use "+StringUtil.addcommas(endTotal - startTotal)+" bytes. cache size ["+StringUtil.addcommas(totalSize)+"] and block num ["+StringUtil.addcommas(blocknum)+"], cost "+(System.currentTimeMillis() - startTime)+" ms");
		System.out.println("["+StringUtil.addcommas(Runtime.getRuntime().maxMemory())+"] ["+StringUtil.addcommas(Runtime.getRuntime().totalMemory())+"] ["+StringUtil.addcommas(Runtime.getRuntime().freeMemory())+"]");
		String line = null;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while((line = reader.readLine()) != null){
			line = line.trim();
			if(line.length() == 0 )continue;
			if(line.toLowerCase().equals("exit")){
				System.exit(0);
			}
			
			String ss[] = line.split("\\s+");
			if(ss.length == 3){
				LruListCache c  = cacheMap.get(ss[0]);
				if(c == null){
					System.out.println("cache not exist.");
				}else{
					try{
						int start = Integer.parseInt(ss[1]);
						int size = Integer.parseInt(ss[2]);
						Object ids[] = c.get(start,size);
						for(int i = 0 ; i < ids.length ; i++){
							Person p = personMap.get((String)ids[i]);
							System.out.println("person: " + p.name + " "+p.weight + " " +p.high + " " + p.age );
						}
					}catch(Exception e){
						System.out.println("invalid integer.");
					}
				}
			}else{
				System.out.println("<cache_name> <start> <size>.");
			}
		}
		
		
	}
	
	public static String[] search(String allPersons[],String sub){
		ArrayList al = new ArrayList();
		for(int i = 0 ; i < allPersons.length ; i++){
			if(allPersons[i].indexOf(sub)>=0){
				al.add(allPersons[i]);
			}
		}
		return (String[])al.toArray(new String[0]);
	}
	
	public static class Person{
		
		public String name;
		
		public int weight;
		
		public int high;
		
		public int age;
		
		public Person(String name,int w,int h,int a){
			this.name = name;
			this.weight = w;
			this.high = h;
			this.age = a;
		}
	}
}
