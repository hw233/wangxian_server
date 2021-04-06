package com.xuanzhi.tools.cache.diskcache.concrete.testcase;

import java.io.*;
import java.util.*;

import org.apache.log4j.PropertyConfigurator;

import com.xuanzhi.tools.cache.diskcache.concrete.AbstractDiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.StrictMemoryDiskCache;
import com.xuanzhi.tools.text.StringUtil;

public class DiskCacheMemeryTest {

	static int dataSize = 1024;
	static int keySize = 32;
	/**
	 * 通过交互式的输入输出，查看DiskCache对内存，磁盘占用的情况
	 * 
	 * @param args
	 */
	public static void main(String args[]) throws Exception{
		Random random = new Random();
		random.setSeed(System.currentTimeMillis());
		
		//数据的大小
		
		
		File dataFile = new File("./memerytest.ddc");
		File indexedFile = null;
		
		long lifeTimeout = 100 * 3600 * 1000L;
		long idleTimeout = 3600 * 1000L;
		int maxElement = 1024 * 1024;
		int maxDataBlockInMem = 1024 * 100;
		int maxFreeBlockInMem = 1024 * 100;
		
		AbstractDiskCache cache = null;
		
		String cacheType = "default";
		for(int i = 0 ; i < args.length ; i++){
			if(args[i].equals("-df")){
				dataFile = new File(args[i+1]);
				i++;
			}else if(args[i].equals("-if")){
				indexedFile = new File(args[i+1]);
				i++;
			}else if(args[i].equals("-lt")){
				lifeTimeout = Long.parseLong(args[i+1]) * 1000L;
				i++;
			}else if(args[i].equals("-it")){
				idleTimeout = Long.parseLong(args[i+1]) * 1000L;
				i++;
			}else if(args[i].equals("-ds")){
				dataSize = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-ks")){
				keySize = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-h")){
				printHelp();
				System.exit(0);
			}else if(args[i].equals("-cache")){
				cacheType = args[i+1];
				i++;
			}else if(args[i].equals("-me")){
				maxElement = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-md")){
				maxDataBlockInMem = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-mf")){
				maxFreeBlockInMem = Integer.parseInt(args[i+1]);
				i++;
			}
		}
		Properties props = new Properties();
		props.setProperty("log4j.rootLogger","DEBUG,A");
		props.setProperty("log4j.appender.A","org.apache.log4j.ConsoleAppender");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		PropertyConfigurator.configure(props);
		if(cacheType.equals("strict")){
			cache = new StrictMemoryDiskCache(dataFile,indexedFile,"",idleTimeout,false);
			((StrictMemoryDiskCache)cache).setDefaultLifeTimeTimeout(lifeTimeout);
			((StrictMemoryDiskCache)cache).setMaxElementNum(maxElement);
			((StrictMemoryDiskCache)cache).setMaxNumOfDataBlockInMemory(maxDataBlockInMem);
			((StrictMemoryDiskCache)cache).setMaxNumOfFreeBlockInMemory(maxFreeBlockInMem);
		}else{
			//cache = new DefaultDiskCache(dataFile,indexedFile,"",idleTimeout,false);
			((DefaultDiskCache)cache).setDefaultLifeTimeTimeout(lifeTimeout);
			((DefaultDiskCache)cache).setMaxElementNum(maxElement);
		}
		
		System.out.println("====================================================");
		System.out.print(">");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String s = null;
		while( (s = reader.readLine()) != null){
			s = s.trim();
			if(s.length() == 0) continue;
			if(s.equalsIgnoreCase("exit")){
				System.exit(0);
			}else if(s.equalsIgnoreCase("help")){
				printCommandHelp();
			}else if(s.equalsIgnoreCase("show")){
				Runtime r = Runtime.getRuntime();
				r.gc();
				//System.out.println("MEM{"+StringUtil.addcommas(r.maxMemory())+"/"+StringUtil.addcommas(r.totalMemory())+"/"+StringUtil.addcommas(r.freeMemory())+"},CACHE" 
				//		+cache.toString());
				System.out.println("Memory max:"+StringUtil.addcommas(r.maxMemory())+"    total:"+StringUtil.addcommas(r.totalMemory())+"  free:"+StringUtil.addcommas(r.freeMemory())+"");
				System.out.println("Cache :"+cache.toString());
			}else if(s.startsWith("put ")){
				doPut(cache,s);
			}else if(s.startsWith("get ")){
				doGet(cache,s);
			}else if(s.startsWith("remove ")){
				doRemove(cache,s);
			}else if(s.startsWith("clear")){
				cache.clear();
			}else if(s.startsWith("destory")){
				cache.destory();
			}else{
				System.out.println("invalid command,please see help!");
			}
			System.out.print(">");
		}
		
	}
	/**
	 * put <A~B>
	 * 往cache中加入标号A到B的数据，左闭右开。
	 * 比如put 1~100，就是加入标号为1，2，。。。。，99的数据，
	 * 如果cache中本来有1～100中的某些标号的数据，这些数据将被覆盖
	 * @param cache
	 * @param command
	 */
	public static void doPut(AbstractDiskCache cache,String command){
		String s[] = command.split(" ",2);
		String ss[] = s[1].split("~");
		try{
			long startTime = System.currentTimeMillis();
			int a = Integer.parseInt(ss[0].trim());
			int b = Integer.parseInt(ss[1].trim());
			for(int i = a ; i < b ; i++){
				TestData  td = new TestData(""+i,dataSize);
				cache.put(i,td);
			}
			System.out.println("put done! cost "+(System.currentTimeMillis() - startTime)+" ms");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * remove <A~B>
	 * cache中加入标号A到B的数据，左闭右开。
	 * 比如put 1~100，就是加入标号为1，2，。。。。，99的数据，
	 * 如果cache中本来有1～100中的某些标号的数据，这些数据将被覆盖
	 * @param cache
	 * @param command
	 */
	public static void doRemove(AbstractDiskCache cache,String command){
		String s[] = command.split(" ",2);
		String ss[] = s[1].split("~");
		try{
			long startTime = System.currentTimeMillis();
			int a = Integer.parseInt(ss[0].trim());
			int b = Integer.parseInt(ss[1].trim());
			for(int i = a ; i < b ; i++){
				cache.remove(i);
			}
			System.out.println("remove done! cost "+(System.currentTimeMillis() - startTime)+" ms");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * get <A~B>
	 * cache中加入标号A到B的数据，左闭右开。
	 * 比如put 1~100，就是加入标号为1，2，。。。。，99的数据，
	 * 如果cache中本来有1～100中的某些标号的数据，这些数据将被覆盖
	 * @param cache
	 * @param command
	 */
	public static void doGet(AbstractDiskCache cache,String command){
		String s[] = command.split(" ",2);
		String ss[] = s[1].split("~");
		try{
			long startTime = System.currentTimeMillis();
			int hit = 0;
			int miss = 0;
			int error = 0;
			int a = Integer.parseInt(ss[0].trim());
			int b = Integer.parseInt(ss[1].trim());
			for(int i = a ; i < b ; i++){
				TestData td = (TestData)cache.get(i);
				if(td != null){
					if(td.id.equals(""+i)) hit++;
					else error++;
				}else{
					miss ++;
				}
			}
			System.out.println("get done! Hit:"+hit+",Miss:"+miss+",Error:"+error+",cost "+(System.currentTimeMillis() - startTime)+" ms");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void printCommandHelp(){
		System.out.println("All command list here:");
		System.out.println("  put numA~numB");
		System.out.println("  get numA~numB");
		System.out.println("  remove numA~numB");
		System.out.println("  clear");
		System.out.println("  destory");
		System.out.println("  show");
		System.out.println("  exit");
		
	}
	public static void printHelp(){
		System.out.println("Usage: java -cp . DiskCacheMemeryTest [-df file] [-if file] [-lt timeout] [-it timeout] [-ds size] [-ks size] [-h]");
		System.out.println("Options:");
		System.out.println("       -df file 数据文件地址，默认为./memerytest.ddc");
		System.out.println("       -if file 索引文件地址，默认为null");
		System.out.println("       -lt timeout 生命周期，单位秒，默认为3600");
		System.out.println("       -it file 最大空闲时间，单位秒，默认为300");
		System.out.println("       -ds file 数据大小，默认为1K");
		System.out.println("       -ks file Key的大小， 默认为32");
		System.out.println("       -me num 元素最大个数，默认为1024 * 1024");
		System.out.println("       -md num 在内存中最大的含数据块个数，默认为1024×100");
		System.out.println("       -mf num 在内存中最大的不含数据块个数，默认为1024×100");
		System.out.println("       -h  打印此帮助");
		
	}
	
	public static class TestData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -9145856999077250854L;
		String id;
		byte data[] = null;
		public TestData(String id,int size){
			this.id = id;
			this.data = new byte[size];
		}
	}
}
