package com.xuanzhi.tools.cache;

import java.io.*;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class DiskCacheTestCase {

	public static void main(String args[]) throws Exception{
		
		Properties props = new Properties();
		props.setProperty("log4j.rootLogger","DEBUG,A");
		props.setProperty("log4j.appender.A","org.apache.log4j.ConsoleAppender");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		PropertyConfigurator.configure(props);
		
		File file = new File(System.getProperty("user.dir")+"/data/mieshi_player_info.ddc");
		File indexedFile = new File(System.getProperty("user.dir")+"/data/mieshi_player_info.idx");
		DefaultDiskCache cache = new DefaultDiskCache(file,indexedFile,"PLAYERINFO_CACHE",3600000000L);
		
		for(int i = 0 ; i < 100 ; i++){
			System.out.print(".");
			Thread.sleep(100);
		}
		
		System.out.println("=====cache ============");
		
		cache.destory();
	}
}
