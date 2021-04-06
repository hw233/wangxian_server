package com.fy.engineserver.sprite.pet2;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 玩家获得过的宠物的集合。
 * 
 * create on 2013年8月15日
 */
@SimpleEntity
public class PetsOfPlayer implements Cacheable{
	@SimpleId
	public long pid;
	@SimpleVersion
	private int version;
	/**
	 * 宠物名称连接起来的字符串。
	 * 注意数据库字段的长度，目前（2013年8月23日12:03:14有157种宠物）
	 */
	@SimpleColumn(length=3000)
	public String pets;
	/**
	 * 血脉
	 */
	public long xueMai;
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public String getPets() {
		return pets;
	}
	public void setPets(String pets) {
		this.pets = pets;
		Pet2Manager.getInst().petsOfPlayerBeanEm.notifyFieldChange(this, "pets");
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public long getXueMai() {
		return xueMai;
	}
	public void setXueMai(long xueMai) {
		this.xueMai = xueMai;
		Pet2Manager.getInst().petsOfPlayerBeanEm.notifyFieldChange(this, "xueMai");
	}
	@Override
	public int getSize() {
		return pets == null ? 4 : pets.length();
	}
}
