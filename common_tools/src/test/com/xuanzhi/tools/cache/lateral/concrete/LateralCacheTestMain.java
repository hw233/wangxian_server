package com.xuanzhi.tools.cache.lateral.concrete;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.PropertyConfigurator;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.lateral.LateralCache;
import com.xuanzhi.tools.text.StringUtil;
import com.xuanzhi.tools.text.XmlUtil;
import com.xuanzhi.tools.transport.DefaultConnectionSelector;

/**
 * 此测试例子运行后，
 * 随机的做一些行为，这些行为包括
 * 		读取一些对象放入到Cache中，30%
 * 		删除一些对象		10%
 * 		修改一些对象		40%
 * 		从cache中取一些对象 20%	
 * 
 * 设计当中，cache的个数可以配置，随机的频率也可以配置。
 * @author <a href='mailto:myzdf.bj@gmail.com'>Yugang Wang</a>
 * 2008-6-26
 */
public class LateralCacheTestMain implements Runnable{

	public static void main(String args[]) throws Exception{
		String host= null;
		int port = 3334;
		int model = 4;
		long step = 1000;
		
		String others = null;
		String loggerLevel = "DEBUG";
		
		for(int i = 0 ; i < args.length ; i++){
			if(args[i].equals("-host") && i < args.length-1){
				host = args[i+1];
				i++;
			}else if(args[i].equals("-port") && i < args.length-1){
				port = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-model") && i < args.length-1){
				model = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-step") && i < args.length-1){
				step = Integer.parseInt(args[i+1]);
				i++;
			}else if(args[i].equals("-others") && i < args.length-1){
				others = args[i+1];
				i++;
			}else if(args[i].equals("-loglevel") && i < args.length-1){
				loggerLevel = args[i+1];
				i++;
			}else if(args[i].equals("-h")){
				System.out.println("Usage java -cp . com.xuanzhi.tools.cache.lateral.concrete.LateralCacheTestMain <-host host> <-port port> <-model model> <-step step> <-others others>");
				System.out.println("Author: myzdf");
				System.out.println("Options:");
				System.out.println("          -host host 服务器绑定的IP");
				System.out.println("          -port port 服务器绑定的端口，默认为3334");
				System.out.println("          -model model LateralCache的模式，1～6分别表示强覆盖，弱覆盖，强引用，弱引用，重装和无为");
				System.out.println("          -step step 自动线程每步操作之间的间隔");
				System.out.println("          -others others 制定其他Lateral者，格式 host1:port1;host2:port2;...");
				System.out.println("          -h 打印此帮助，并退出");
				System.exit(0);
			}
		}
		
		if(host == null || others == null){
			System.out.println("参数不全，请参见-h");
			System.exit(1);
		}

		Properties props = new Properties();
		props.setProperty("log4j.rootLogger",""+loggerLevel+",A");
		props.setProperty("log4j.appender.A","org.apache.log4j.FileAppender");
		props.setProperty("log4j.appender.A.File","./out.log");
		props.setProperty("log4j.appender.A.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.A.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		props.setProperty("log4j.logger.com.xuanzhi.tools.cache.lateral",""+loggerLevel+",B");
		props.setProperty("log4j.additivity.com.xuanzhi.tools.cache.lateral","false");
		props.setProperty("log4j.appender.B","org.apache.log4j.FileAppender");
		props.setProperty("log4j.appender.B.File","./lateral.log");
		props.setProperty("log4j.appender.B.layout","org.apache.log4j.PatternLayout");
		props.setProperty("log4j.appender.B.layout.ConversionPattern","[%p] %-d{yyyy-MM-dd HH:mm:ss} %m%n");
		
		PropertyConfigurator.configure(props);
		
			
		
		StringBuffer xml = new StringBuffer();
		xml.append("<lateral-cache-manager>");
		xml.append("	<identity>"+host+"@"+port+"</identity>");
		xml.append("	<network-props>");
		xml.append("	     <bindHost>"+host+"</bindHost>");
		xml.append("	     <bindPort>"+port+"</bindPort>");
		xml.append("	</network-props>");
		xml.append("	<peers>");
		
		String ss[] = others.split(";");
		for(int i = 0 ; i < ss.length ; i++){
			int k = ss[i].indexOf(":");
			String h = ss[i].substring(0,k);
			int p = Integer.parseInt(ss[i].substring(k+1));
			xml.append("	<peer identity=\""+h+"@"+p+"\" host=\""+h+"\" port=\""+p+"\"/>");
		}
		xml.append("	</peers>");
		xml.append("<caches><cache name=\"aaa\" model=\""+model+"\"/></caches>");
		xml.append("</lateral-cache-manager>");
		
		DefaultLateralCacheManager cm = new DefaultLateralCacheManager();
		cm.configure(XmlUtil.loadStringWithoutTitle(xml.toString()).getDocumentElement());
		
			
		LateralCacheTestMain m = new LateralCacheTestMain();
		m.step = step;
		m.cache = cm.getCache("aaa");
		Thread t = new Thread(m);
		t.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		while(true){
			System.out.print(">");
			String line = reader.readLine();
			if(line.trim().length() == 0) continue;
			line = line.trim();
			if(line.startsWith("put ")){
				String keyvalue[] = line.substring(4).split(" ",2);
				m.cache.put(keyvalue[0],new CacheItem(keyvalue[1]));
				System.out.println(keyvalue[0] + " -> " + m.cache.get(keyvalue[0]));
			}else if(line.startsWith("get ")){
				String key = line.substring(4);
				CacheItem ci = (CacheItem)m.cache.get(key);
				System.out.println(key+" -> " + ci);
			}else if(line.startsWith("modify ")){
				String keyvalue[] = line.substring(7).split(" ",2);
				m.cache.put(keyvalue[0],new CacheItem(keyvalue[1]));
				m.cache.update(keyvalue[0]);
				System.out.println(keyvalue[0] + " -> " + m.cache.get(keyvalue[0]));
			}else if(line.startsWith("remove ")){
				String key = line.substring(7);
				m.cache.remove(key);
				System.out.println(key + " -> " + m.cache.get(key));
			}else if(line.equals("exit")){
				System.exit(0);
			}else if(line.equals("list")){
				DefaultLateralCache dcache = (DefaultLateralCache)m.cache;
				Iterator it = dcache.m_cache.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry me = (Map.Entry)it.next();
					System.out.println("  "+me.getKey()+" = "+me.getValue());
				}
				it = dcache.m_refcache.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry me = (Map.Entry)it.next();
					System.out.println("  "+me.getKey()+" -> "+me.getValue());
				}
			}
		}
	}
	
	
	protected long step;
	protected LateralCache cache;
	
	
	protected ArrayList<String> keys = new ArrayList<String>();
	public Cacheable loadObject(String key){
		return new CacheItem(key);
	}
	
	public void run(){
		Random r = new Random(System.currentTimeMillis());
		while(true){
			try {
				Thread.sleep(step);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int k = r.nextInt(102400) % 10;
			if(k<3){
				String key = StringUtil.randomString(5);
				Cacheable o = loadObject(key);
				cache.put(key,o);
				keys.add(key);
			}else if(k==3 && keys.size() > 10){
				int i = r.nextInt(keys.size());
				cache.remove(keys.get(i));
			}else if(k < 8&& keys.size() > 10){
				int i = r.nextInt(keys.size());
				cache.update(keys.get(i));
			}else if(keys.size() > 10){
				int i = r.nextInt(keys.size());
				cache.get(keys.get(i));
			}
		}
	}
	
	public static class CacheItem implements Cacheable,Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 8064635369071751495L;
		
		String value;
		public CacheItem(String value){
			this.value = value;
		}
		
		public String toString(){
			return "CI:"+value;
		}
		
		public boolean equals(Object o){
			CacheItem c = (CacheItem)o;
			if(c == this || c.value.equals(this.value)) return true;
			return false;
		}
		
		public int getSize() {
			return 1;
		}
		
	}
}
