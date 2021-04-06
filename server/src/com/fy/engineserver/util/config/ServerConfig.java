package com.fy.engineserver.util.config;

import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.util.TimeTool;

public class ServerConfig {
	
	public String platname;
	
	public String [] servernames;
	
	public String [] notLoadServerNames;
	
	public String [] needupdateconfigs;
	
	public long startTime;
	
	public long endTime;

	public ServerFit fit;
	
	public ServerConfig(String platname,String [] servernames,String [] notLoadServerNames,String [] needupdateconfigs,String startTime, String endTime){
		this.platname = platname;
		this.servernames = servernames;
		this.notLoadServerNames = notLoadServerNames;
		this.needupdateconfigs = needupdateconfigs;
		this.startTime = TimeTool.formatter.varChar19.parse(startTime);
		this.endTime = TimeTool.formatter.varChar19.parse(endTime);
	}
	
	public ServerConfig (String startTime,String endTime,String [] needupdateconfigs,ServerFit fit) {
		this.startTime = TimeTool.formatter.varChar19.parse(startTime);
		this.endTime = TimeTool.formatter.varChar19.parse(endTime);
		this.needupdateconfigs = needupdateconfigs;
		this.fit = fit;
	}
	
	public boolean thisServerOpen (){
		return this.fit.thisServerOpen();
	}
	/**
	 * 不是有效期，加载默认的资源
	 * @return
	 */
	public boolean isEffective() {
		long now = SystemTime.currentTimeMillis();
		return startTime <= now && now <= endTime;
	}
	
	/**
	 * 是否需要加载新资源
	 * @return
	 */
	public boolean isNeedLoadConfig(String platName,String servername){
		if(!platname.equals(platName)){
			return false;
		}
		
		if(notLoadServerNames!=null && notLoadServerNames.length>0){
			for(String name:notLoadServerNames){
				if(name.equals(servername)){
					return false;
				}
			}
		}
		
		if(platname.equals(platName)){
			if(servernames.length>0){
				for(String name:servernames){
					if(name.equals(servername)){
						return true;
					}
				}
			}else{
				return true;
			}
		}
		return false;
	}
	
	public String getPlatname() {
		return platname;
	}

	public void setPlatname(String platname) {
		this.platname = platname;
	}

	public String[] getServernames() {
		return servernames;
	}

	public void setServernames(String[] servernames) {
		this.servernames = servernames;
	}

	public String[] getNeedupdateconfigs() {
		return needupdateconfigs;
	}

	public void setNeedupdateconfigs(String[] needupdateconfigs) {
		this.needupdateconfigs = needupdateconfigs;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String[] getNotLoadServerNames() {
		return notLoadServerNames;
	}

	public void setNotLoadServerNames(String[] notLoadServerNames) {
		this.notLoadServerNames = notLoadServerNames;
	}

	public ServerFit getFit() {
		return fit;
	}

	public void setFit(ServerFit fit) {
		this.fit = fit;
	}

}
