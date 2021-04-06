package com.fy.engineserver.util.config;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.expactivity.ServersConfig;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.xuanzhi.boss.game.GameConstants;

public class ServerFit4Activity implements ServerFit,ServersConfig{
	
	private Platform[] platForms ;
	
	private List<String> openServerNames;
	
	private List<String> notOpenServerNames;
	
	/** 服务器名1 */
	private String serverName;
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("开放平台：");
		for(int i=0; i<platForms.length; i++) {
			sb.append(platForms[i]);
			if((i+1) < platForms.length) {
				sb.append("、");
			} else {
				sb.append("<br/>");
			}
		}
		if(openServerNames == null && notOpenServerNames == null) {
			sb.append("所有服务器都开启！");
		} else if(openServerNames != null) {
			sb.append("开放服务器：");
			for(int i=0; i<openServerNames.size(); i++) {
				sb.append(openServerNames.get(i));
				if((i+1) < openServerNames.size()) {
					sb.append("、");
				} else {
					sb.append("<br/>");
				}
			}
		}else {
			sb.append("不开放服务器：");
			for(int i=0; i<openServerNames.size(); i++) {
				sb.append(openServerNames.get(i));
				if((i+1) < openServerNames.size()) {
					sb.append("、");
				} else {
					sb.append("<br/>");
				}
			}
		}
		return sb.toString();
	}
	
	public Platform[] getPlatForms() {
		return platForms;
	}

	public void setPlatForms(Platform[] platForms) {
		this.platForms = platForms;
	}

	public List<String> getOpenServerNames() {
		return openServerNames;
	}

	public void setOpenServerNames(List<String> openServerNames) {
		this.openServerNames = openServerNames;
	}

	public List<String> getNotOpenServerNames() {
		return notOpenServerNames;
	}

	public void setNotOpenServerNames(List<String> notOpenServerNames) {
		this.notOpenServerNames = notOpenServerNames;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public boolean thisServerOpen(){
		boolean result = false;
		if (PlatformManager.getInstance().isPlatformOf(platForms)) {
			if(openServerNames != null){		//开启活动列不为空
				if(openServerNames.contains(serverName)) {
					result = true;
				} 
			}else if(notOpenServerNames != null){		//不开活动服务器列表不为空
				if(!notOpenServerNames.contains(serverName)) {
					result = true;
				}
			} else {									//开启活动服务器和不开启都不填则默认都开
				result = true;
			}
		}
		return result;
	}
	
	private Platform getPlatForm(String pN) throws Exception {
		for(Platform p : Platform.values()) {
			if(p.getPlatformName().equals(pN)) {
				return p;
			}
		}
		throw new Exception("配置了错误的平台名{}" + pN);
	}
	
	public ServerFit4Activity(String platForms, String openServerNames, String notOpenServers) throws Exception {
		try{
			String[] temp1 = platForms.split(",");
			this.platForms = new Platform[temp1.length];
			for(int i=0; i<temp1.length; i++) {		//设置平台
				this.platForms[i] = getPlatForm(temp1[i].trim());
			}
			if(openServerNames != null){
				String[] temp2 = openServerNames.split(",");
				if(!"".equals(openServerNames) && !"ALL_SERVER".equals(openServerNames)) {				//开启活动服务器名
					this.openServerNames = new ArrayList<String>();
					for(int i=0; i<temp2.length; i++) {		//设置服务器
						this.openServerNames.add(temp2[i].trim());
					}
				}
			}
			if(notOpenServers != null){
				String[] temp3 = notOpenServers.split(",");
				if(!"".equals(notOpenServers)) {				//不开启活动服务器名
					this.notOpenServerNames = new ArrayList<String>();
					for(int i=0; i<temp3.length; i++) {		//设置服务器
						this.notOpenServerNames.add(temp3[i].trim());
					}
				}
			}
			serverName = GameConstants.getInstance().getServerName();
		}catch(Exception e) {
			throw e;
		}
	}

	@Override
	public boolean thiserverFit() {
		// TODO Auto-generated method stub
		return thisServerOpen();
	}
}
