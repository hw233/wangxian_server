package com.fy.gamegateway.thirdpartner.ruanlei;

import com.xuanzhi.tools.cache.*;
import com.xuanzhi.tools.queue.DefaultQueue;
import com.xuanzhi.tools.servlet.*;
import org.apache.log4j.*;

public class RuanleiAppstoreMatchService implements Runnable{

	static Logger logger = Logger.getLogger(RuanleiAppstoreMatchService.class);
	
	LruMapCache cache = new LruMapCache(1024*1024,72*3600*1000L,false,"RuanleiAppstoreMatchService");
	
	DefaultQueue queue = new DefaultQueue(102400);
	
	Thread thread = null;
	
	public static class Item implements Cacheable{
		public String macAddress;
		public String appName;
		public String channel;
		
		public int getSize(){
			return 1;
		}
	}
	
	static RuanleiAppstoreMatchService self;
	
	public static RuanleiAppstoreMatchService getInstance(){
		if(self != null) return self;
		synchronized(RuanleiAppstoreMatchService.class){
			if(self != null) return self;
			self = new RuanleiAppstoreMatchService();
			return self;
		}
	}
	
	protected RuanleiAppstoreMatchService(){
		thread = new Thread(this,"RuanleiAppstoreMatchService-Thread");
		thread.start();
	}
	
	/**
	 * 由软猎通过调用我们的接口，通知我们某个用户点击了我们的广告
	 */
	public void notifyAdvertiseClicked(String macAddress,String appName,String channel){
		Item item = new Item();
		item.macAddress = macAddress;
		item.appName = appName;
		item.channel = channel;
		
		if(item.macAddress != null){
			cache.put(item.macAddress.toUpperCase(), item);
		}
		
		logger.info("[点击通知] ["+macAddress+"] ["+appName+"] ["+channel+"] [cache_size:"+cache.getNumElements()+"]");
	}
	
	/**
	 * 
	 * @param macAddress
	 * @param appName
	 * @param channel
	 */
	public void notifyUserActive(String macAddress,String appName,String channel){
		if(macAddress == null || macAddress.length() == 0) return;
		
		Item item = (Item)cache.get(macAddress.toUpperCase());
		if(item == null) return;
		cache.remove(macAddress.toUpperCase());
		
		queue.push(item);
		
		logger.info("[激活通知] ["+macAddress+"] ["+appName+"] ["+channel+"] [match:{"+item.macAddress+","+item.appName+","+item.channel+"}] [cache_size:"+cache.getNumElements()+"] [queue_size:"+queue.size()+"]");
	}
	
	public void sendNotifyToPartner(Item item){
		long ll = System.currentTimeMillis();
		String appId = "778";
		String url = "http://ad2.ruixinonline.com/ad_rest/rest/appInstall/install?mac=";
		if(item != null && item.macAddress != null){
			url+=item.macAddress.toUpperCase()+"&appId="+appId+"&installTime="+System.currentTimeMillis()+"";
			try{
				HttpUtils.webGet(new java.net.URL(url), new java.util.HashMap(), 5000, 5000);
				
				logger.info("[回调第三方接口] [用户激活] [成功] ["+item.macAddress+"] ["+item.appName+"] ["+item.channel+"] [url:"+url+"] [cost:"+(System.currentTimeMillis() - ll)+"ms]");
			}catch(Exception e){
				logger.info("[回调第三方接口] [用户激活] [失败] ["+item.macAddress+"] ["+item.appName+"] ["+item.channel+"] [url:"+url+"] [cost:"+(System.currentTimeMillis() - ll)+"ms]",e);
			}
		}else{
			if(item == null){
				logger.error("[回调第三方接口] [用户激活] [失败] [item为空] [url:"+url+"] [cost:"+(System.currentTimeMillis() - ll)+"ms]");				
			}else{
				logger.error("[回调第三方接口] [用户激活] [失败] ["+item.macAddress+"] ["+item.appName+"] ["+item.channel+"] [url:"+url+"] [cost:"+(System.currentTimeMillis() - ll)+"ms]");	
			}
		}
	}
	
	public void run(){
		
		while(thread != null && thread.isInterrupted() == false){
			try{
				Item item = (Item)queue.pop(1000);
				if(item != null){
					sendNotifyToPartner(item);
				}
			}catch(Exception e){
				logger.error("",e);
			}
		}
	}
}
