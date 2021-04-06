package com.xuanzhi.tools.monitor;

import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.DateUtil;

import java.util.*;
public class MonitorThread extends Thread{

	public static final int MAX_POINT_NUM = 128 * 4;
	
	MonitorService service;
	LinkedList<MonitorPoint> points = new LinkedList<MonitorPoint>();
	
	public MonitorThread(MonitorService service){
		this.service = service;
	}
	
	public CheckFlag checkFlag = new CheckFlag();
	
	public static class CheckFlag{
		public long startTime = 0;
		public long endTime = 0;
		public String administrator="未知";
		public String information = "无";
		
		protected void setChecking(String admin,String info,long time){
			this.administrator = admin;
			this.information = info;
			this.startTime = System.currentTimeMillis();
			this.endTime = System.currentTimeMillis() + time;
		}
		
		protected void cancelChecking(){
			this.endTime = this.startTime = System.currentTimeMillis();	
		}
		
		public boolean isChecking(){
			long l = System.currentTimeMillis();
			return (l >= startTime && l <= endTime);
		}
	}
	
	public void setChecking(String admin,String info,long time){
		checkFlag.setChecking(admin,info,time);
		if(service instanceof CheckFlagSettingNotify){
			CheckFlagSettingNotify cfsn = (CheckFlagSettingNotify)service;
			cfsn.notify(checkFlag);
		}
	}
	
	protected void handleMonitorPoint(MonitorPoint mp){
		
		int level=0;
		if(points.size() == 0){
			level = service.calculate(null,0,mp.data);
		}else{
			MonitorPoint lastMP = points.getFirst();
			level = service.calculate(lastMP.data,lastMP.level,mp.data);
		}
		mp.setLevel(level);
		if(points.size() >= MAX_POINT_NUM){
			points.removeLast();
		}
		points.addFirst(mp);
		
		MonitorPoint lastNotifyPoint = null;
		Iterator<MonitorPoint> it = points.iterator();
		while(it.hasNext()){
			MonitorPoint p = it.next();
			if(p.isAlerted()){
				lastNotifyPoint = p;
				break;
			}
		}
		
		if(lastNotifyPoint == null){
			if(mp.getLevel() >= service.getMinAlertLevel()){
				mp.setAlerted(true);
				if(checkFlag.isChecking() == false)
					service.alert(mp,0L);
			}
		}else{
			long lastintTime = 0;
			it = points.iterator();
			while(it.hasNext()){
				MonitorPoint p = it.next();
				if(p.getLevel() == mp.getLevel()){
					lastintTime = System.currentTimeMillis() - (p.getCheckTime() + p.getCostTime());
				}else{
					break;
				}
			}
			
			if(mp.getLevel() >= service.getMinAlertLevel()){
				if(System.currentTimeMillis() - lastNotifyPoint.checkTime+lastNotifyPoint.costTime > service.getAlertInterval()){
					mp.setAlerted(true);
					if(checkFlag.isChecking() == false)
						service.alert(mp,lastintTime);
					
				}
			}else if(mp.getLevel() <= service.getMaxNotifyLevel()){
				if(lastNotifyPoint.getLevel() >= service.getMinAlertLevel() && System.currentTimeMillis() - lastNotifyPoint.checkTime+lastNotifyPoint.costTime > service.getAlertInterval()){
					mp.setAlerted(true);
					service.notify(mp,lastintTime);
					if(checkFlag.isChecking() == false){
						checkFlag.cancelChecking();
					}
				}
			}
		}
		
	}
	
	public void run(){
		while(true){
			try{
				long interval = service.getMonitorInterval();
				if(interval < 5000L) interval = 5000L;
				if(interval > 3600000L) interval = 3600000L;
				Thread.sleep(service.getMonitorInterval());
				
				HashMap headers = new HashMap();
				byte bytes[] = null;
				MonitorPoint mp = null;
				long checkTime = System.currentTimeMillis();
				try{
					 bytes = HttpUtils.webGet(service.getDataUrl(),headers,5000,30000);
					 Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					 MonitorData data = new MonitorData(code.intValue(),bytes);
					 mp = new MonitorPoint(data,checkTime,System.currentTimeMillis() - checkTime);
				}catch(Exception e){
					bytes = new String("#no_content").getBytes();
					MonitorData data = new MonitorData(599,bytes);
					mp = new MonitorPoint(data,checkTime,System.currentTimeMillis() - checkTime);
					e.printStackTrace();
				}
				
				handleMonitorPoint(mp);
				
				System.out.println(""+DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ["+service.getName()+"] ["+mp.getLevelAsString()+"] ["+mp.data.getResponseCode()+"]");
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
