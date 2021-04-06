package com.xuanzhi.tools.cache.lateral.concrete;

import junit.framework.*;
import java.io.*;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.log4j.*;
import org.w3c.dom.Document;

import com.xuanzhi.tools.cache.Cacheable;

public class LateralCacheManagerTestCase extends TestCase{
	public LateralCacheManagerTestCase(){
		super("");
		Properties props = new Properties();
		props.setProperty("log4j.rootLogger","INFO,A");
		props.setProperty("log4j.appender.A","org.apache.log4j.ConsoleAppender");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		PropertyConfigurator.configure(props);
	}
}
