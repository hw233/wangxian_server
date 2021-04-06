package com.fy.engineserver.newBillboard;

import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 记录排行榜一些简单数据
 * 
 *
 */
@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"menuname"}),
	@SimpleIndex(members={"submenuname"}),
	@SimpleIndex(members={"keyname"})
})
public class BillboardInfo {
	@SimpleId
	private long id;

	@SimpleVersion
	private int version;
	
	private long pid;
	
	private long starttime;
	
	private long endtiem;
	
	private String menuname;
	
	private String submenuname;
	
	private String keyname;
	
	private long value;

	private int country;
	
	private String playername;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
		setDirty("playername");
	}

	public long getPid() {
		return pid;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
		setDirty("country");
	}

	public void setPid(long pid) {
		this.pid = pid;
		setDirty("pid");
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
		setDirty("starttime");
	}

	public long getEndtiem() {
		return endtiem;
	}

	public void setEndtiem(long endtiem) {
		this.endtiem = endtiem;
		setDirty("endtiem");
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
		setDirty("menuname");
	}

	public String getSubmenuname() {
		return submenuname;
	}

	public void setSubmenuname(String submenuname) {
		this.submenuname = submenuname;
		setDirty("submenuname");
	}

	public String getKeyname() {
		return keyname;
	}

	public void setKeyname(String keyname) {
		this.keyname = keyname;
		setDirty("keyname");
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
		setDirty("value");
	}
	
	public void setDirty(String field) {
		try {
			SimpleEntityManager<BillboardInfo> em = BillboardStatDateManager.em_info;
			if (em != null) {
				em.notifyFieldChange(this, field);
			}
		} catch (java.lang.IllegalArgumentException e) {
			BillboardsManager.logger.error("[排行榜简单信息改变数据保存异常] ["+field+"]",e);
		}
	}

	@Override
	public String toString() {
		return "BillboardInfo [menuname=" + menuname + ", submenuname="
				+ submenuname + ", keyname=" + keyname + ", value=" + value
				+ "]";
	}
	
	
}
