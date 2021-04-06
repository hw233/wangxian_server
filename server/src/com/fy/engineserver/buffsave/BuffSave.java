package com.fy.engineserver.buffsave;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"pid"})
})
public class BuffSave {

	@SimpleId
	private long saveID;
	@SimpleVersion
	private int version;
	
	//player id
	private long pid;
	
	//保持buff的剩余可使用时间
	private long savetime;
	
	private String buffname;
	
	private int bufflevel;
	
	//buff失效时间
	private long endtime;
	
	public void notifyFieldChange(String fieldName) {
		BufferSaveManager.bem.notifyFieldChange(this, fieldName);
	}	

	public long getSaveID() {
		return saveID;
	}

	public void setSaveID(long saveID) {
		this.saveID = saveID;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
		notifyFieldChange("pid");
	}

	public long getSavetime() {
		return savetime;
	}

	public void setSavetime(long savetime) {
		this.savetime = savetime;
		notifyFieldChange("savetime");
	}

	public String getBuffname() {
		return buffname;
	}

	public void setBuffname(String buffname) {
		this.buffname = buffname;
		notifyFieldChange("buffname");
	}

	public int getBufflevel() {
		return bufflevel;
	}

	public void setBufflevel(int bufflevel) {
		this.bufflevel = bufflevel;
		notifyFieldChange("bufflevel");
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
		notifyFieldChange("endtime");
	}

	@Override
	public String toString() {
		return "BuffSave [saveID=" + saveID + ", version=" + version + ", pid="
				+ pid + ", savetime=" + savetime + ", buffname=" + buffname
				+ ", bufflevel=" + bufflevel + "]";
	}

}
