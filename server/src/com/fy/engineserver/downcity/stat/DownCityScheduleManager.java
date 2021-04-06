package com.fy.engineserver.downcity.stat;

import java.io.File;
import java.util.ArrayList;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

public class DownCityScheduleManager implements Runnable {
	DefaultDiskCache ddc;
	
	File dataFile;
	static DownCityScheduleManager instance; 
	
	public LruMapCache cache = null;;
	
	public static final String DID = "downcityids";
	
	public static DownCityScheduleManager getInstance(){
		if(instance == null){
			return new DownCityScheduleManager();
		}
		return instance;
	}
	
	public void init() throws Exception{
		if(true){
			return;
		}
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		cache = new LruMapCache(2048,2*3600*1000L);
		
		Thread t = new Thread(this, "DownCityScheduleManager");
		t.start();
		
		instance = this;
		
		ddc = new DefaultDiskCache(dataFile, null,
				Translate.text_4064, 1L * 365 * 24 * 3600 * 1000L);

		ServiceStartRecord.startLog(this);
	}
	
	public DefaultDiskCache getDdc() {
		return ddc;
	}

	public void setDdc(DefaultDiskCache ddc) {
		this.ddc = ddc;
	}
	
	public void destroy() {
		serializeAll();
	}
	
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				Thread.sleep(10000);
				long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				String ids[] = (String[])cache.keySet().toArray(new String[0]);
				for(int i=0; i< ids.length; i++) {
					DownCitySchedule dc = (DownCitySchedule)cache.get(ids[i]);
					if(dc.isDirty() && (now-dc.getLastUpdateTime() > 30*1000)) {
						serializeDownCitySchedule(dc);
					}
				}
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				DownCityManager.logger.error("[副本统计线程发生异常]", e);
			}
		}
	}
	
	private void serializeAll() {
		String ids[] = (String[])cache.keySet().toArray(new String[0]);
		for(int i=0; i< ids.length; i++) {
			DownCitySchedule dc = (DownCitySchedule)cache.get(ids[i]);
			if(dc.isDirty()) {
				serializeDownCitySchedule(dc);
			}
		}
	}
	
	public DownCitySchedule getDownCitySchedule(String downCityId) {
		DownCitySchedule dcs = (DownCitySchedule)cache.get(downCityId);
		if(dcs == null) {
			dcs = (DownCitySchedule)ddc.get(downCityId);
			if(dcs != null){
				cache.put(downCityId, dcs);
			}
		}
		return dcs;
	}
	
	public void createDownCitySchedule(DownCitySchedule dcs) {
		cache.put(dcs.getDownCityId(), dcs);
	}
	
	public void serializeDownCitySchedule(DownCitySchedule dcs) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(ddc.get(dcs.getDownCityId()) == null) {
			ddc.put(dcs.getDownCityId(), dcs);
			ArrayList<String> ids = (ArrayList<String>)ddc.get(DID);
			if(ids == null) {
				ids = new ArrayList<String>();
			}
			ids.add(dcs.getDownCityId());
			ddc.put(DID, ids);
		} else {
			ddc.put(dcs.getDownCityId(), dcs);
		}
		dcs.setDirty(false);
		dcs.setLastUpdateTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		if(DownCityManager.logger.isInfoEnabled()) {
//			DownCityManager.logger.info("[保存副本统计] ["+dcs.getDownCityId()+"] ["+dcs.getName()+"] ["+dcs.getDownCityPlayerInfos().size()+"] ["+dcs.getDownCityOutputInfos().size()+"] ["+dcs.getDownCityConsumeInfos().size()+"] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)+"]");
			if(DownCityManager.logger.isInfoEnabled())
				DownCityManager.logger.info("[保存副本统计] [{}] [{}] [{}] [{}] [{}] [{}]", new Object[]{dcs.getDownCityId(),dcs.getName(),dcs.getDownCityPlayerInfos().size(),dcs.getDownCityOutputInfos().size(),dcs.getDownCityConsumeInfos().size(),(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-now)});
		}
	}

	public File getDataFile() {
		return dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}

}
