package com.fy.engineserver.activity.fairyRobbery.instance;

import com.fy.engineserver.activity.fairyRobbery.FairyRobberyEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class FairyRobberyEntity {
	@SimpleId
	private long id;			//角色id	
	@SimpleVersion
	private int version;
	
	private int passLevel;		//仙界渡劫 渡劫层数
	
	private long lastSucceTime;	//上次渡劫成功时间
	

	@Override
	public String toString() {
		return "FairyRobberyEntity [id=" + id + ", passLevel=" + passLevel + ", lastSucceTime=" + lastSucceTime + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPassLevel() {
		return passLevel;
	}

	public void setPassLevel(int passLevel) {
		this.passLevel = passLevel;
		FairyRobberyEntityManager.em.notifyFieldChange(this, "passLevel");
	}

	public long getLastSucceTime() {
		return lastSucceTime;
	}

	public void setLastSucceTime(long lastSucceTime) {
		this.lastSucceTime = lastSucceTime;
		FairyRobberyEntityManager.em.notifyFieldChange(this, "lastSucceTime");
	}
	
	
}
