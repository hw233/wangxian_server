package com.fy.engineserver.time;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.xuanzhi.tools.text.XmlUtil;

public class SystemMaintainManager implements Runnable{
	
	public long maxId = -1;
	
	private static SystemMaintainManager self;
	
	public static boolean running = true;
	
	private File file = new File("D:\\systemMaintain.xml");
	public static final byte 时间类型_绝对型 = 0;
	public static final byte 时间类型_剔除维护型 = 1;
	public static final byte 时间类型_剔除下线型 = 2;
	public static final byte 时间类型_剔除暂停型 = 3;
	public static final byte 时间类型_剔除维护且下线型 = 4;
	public static final byte 时间类型_剔除维护且暂停型 = 5;
	public static final byte 时间类型_剔除下线且暂停型 = 6;
	public static final byte 时间类型_剔除维护且下线且暂停型 = 7;
	public static SystemMaintainManager getInstance(){
		return self;
	}
	
	public static Timer createSystemMaintainByType(byte type){
		Timer timer = null;
		if(type == 时间类型_绝对型){
			timer = new AbsoluteTimer();
		}else if(type == 时间类型_剔除维护型){
			timer = new MaintainTimer();
		}else if(type == 时间类型_剔除下线型){
			timer = new OfflineTimer();
		}else if(type == 时间类型_剔除暂停型){
			timer = new PauseTimer();
		}else if(type == 时间类型_剔除维护且下线型){
			timer = new MaintainAndOfflineTimer();
		}else if(type == 时间类型_剔除维护且暂停型){
			timer = new MaintainAndPauseTimer();
		}else if(type == 时间类型_剔除下线且暂停型){
			timer = new OfflineAndPauseTimer();
		}else if(type == 时间类型_剔除维护且下线且暂停型){
			timer = new MaintainAndOfflineAndPauseTimer();
		}
		if(timer != null){
			timer.open();
		}
		return timer;
	}
	public void init() throws Exception{
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		load(file);
		Thread thread = new Thread(this,"SystemMaintainManager");
		thread.start();
		self = this;
		setSystemStartTime(now);
		System.out.println("[SystemMaintainManager] [初始化] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)+"ms]");
	}
	
	public long getNextId(){
		maxId = maxId + 1;
		return maxId;
	}
	
	public void destroy(){
		if(systemStartTime != 0){
			systemStopTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			save();
		}
	}
	
	/**
	 * 系统开启时间
	 * 此时间应该人为控制，因为系统开启时间一般都不是维护好的时间
	 */
	private long systemStartTime;
	
	/**
	 * 系统关闭时间
	 */
	public long systemStopTime;

	/**
	 * 服务器维护时间表
	 * key为维护数据id，value为维护数据
	 */
	protected Hashtable<Long, SystemMaintain> systemMaintainMap = new Hashtable<Long, SystemMaintain>();
	
	public Hashtable<Long, SystemMaintain> getSystemMaintainMap(){
		return systemMaintainMap;
	}
	
	/**
	 * 每次维护开启时间必须调用此函数，才能保证系统维护正确性
	 * @param systemStartTime
	 */
	public void setSystemStartTime(long systemStartTime) {
		this.systemStartTime = systemStartTime;
		if(systemStopTime <= 0){
			systemStopTime = systemStartTime;
			addSystemMaintain(systemStartTime, systemStartTime);
		}else{
			if(systemStopTime > systemStartTime){
				addSystemMaintain(systemStopTime, systemStopTime);
			}else{
				addSystemMaintain(systemStartTime, systemStopTime);
			}
		}
	}

	public long getSystemStartTime() {
		return systemStartTime;
	}

	private void addSystemMaintain(long start, long stop){
		SystemMaintain sm = new SystemMaintain();
		sm.id = getNextId();
		sm.systemStopTime = stop;
		sm.systemStartTime = start;
		systemMaintainMap.put(sm.id, sm);
		save();
	}
	
	public void load(File file) throws Exception{
		if(file != null && file.isFile() && file.exists()){
			FileInputStream is = null;
			try{
				is = new FileInputStream(file);
				Document dom = XmlUtil.load(is,"utf-8");
				Element root = dom.getDocumentElement();
				long maxId = XmlUtil.getAttributeAsLong(root, "maxId");
				long systemLastStopTime = XmlUtil.getAttributeAsLong(root, "systemStopTime");
				Hashtable<Long, SystemMaintain> systemMaintainMap = new Hashtable<Long, SystemMaintain>();
				Element eles[] = XmlUtil.getChildrenByName(root, "SystemMaintain");
				for(int i = 0; eles != null && i < eles.length; i++){
					Element ele = eles[i];
					if(ele != null){
						SystemMaintain sm = new SystemMaintain();
						long id = XmlUtil.getAttributeAsLong(ele, "id");
						sm.id = id;
						long systemStopTime = XmlUtil.getAttributeAsLong(ele, "stop");
						sm.systemStopTime = systemStopTime;
						long systemStartTime = XmlUtil.getAttributeAsLong(ele, "start");
						sm.systemStartTime = systemStartTime;
						systemMaintainMap.put(sm.id, sm);
					}
				}
				this.maxId = maxId;
				this.systemStopTime = systemLastStopTime;
				this.systemMaintainMap = systemMaintainMap;
			}catch(Exception ex){
				ex.printStackTrace();
				if(is != null){
					is.close();
				}
				System.out.println("[系统维护加载] [异常]");
				throw ex;
			}finally{
				if(is != null){
					is.close();
				}
			}
		}
	}
	
	public void save(){
		try{
			long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
			if(file != null){
				if(systemMaintainMap != null && !systemMaintainMap.isEmpty()){
					StringBuffer sb = new StringBuffer();
					sb.append("<?xml version='1.0' encoding='utf-8' ?>\n");
					sb.append("<systemMaintainMap maxId='"+maxId+"' systemStopTime='"+systemStopTime+"'>\n");
					if(systemMaintainMap.values() != null){
						int size = systemMaintainMap.values().size();
						SystemMaintain[] sms = systemMaintainMap.values().toArray(new SystemMaintain[0]);
						for(int i = 0; i < size; i++){
							SystemMaintain sm = sms[i];
							if(sm != null){
								sb.append("<SystemMaintain id='"+sm.id+"' stop='"+sm.systemStopTime+"' start='"+sm.systemStartTime+"'>\n");					
								sb.append("</SystemMaintain>\n");
							}
						}
					}
					sb.append("</systemMaintainMap>");
					FileOutputStream fos = null;
					try{
					fos = new FileOutputStream(file);
					fos.write(sb.toString().getBytes("utf-8"));
					fos.close();
					}catch(Exception ex){
						if(fos != null){
							try {
								fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						System.out.println("[系统维护存储] [异常]");
						ex.printStackTrace();
					}
				}
			}
			System.out.println("[系统维护存储] [用时:"+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("[系统维护存储] [异常]");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long nowTime = 0;
		while(running){
			try {
				Thread.sleep(60000);
				if(systemStartTime != 0){
					systemStopTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
					if(systemStopTime > nowTime + 10*60*1000){
						nowTime = systemStopTime;
						save();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static void main(String[] args) throws Exception{
		SystemMaintainManager smm = new SystemMaintainManager();
		smm.init();
		smm.setSystemStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	}
}
