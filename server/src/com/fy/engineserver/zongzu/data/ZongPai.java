package com.fy.engineserver.zongzu.data;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;


@SimpleEntity
@SimpleIndices({
	@SimpleIndex(members={"zpname"})
})

public class ZongPai {

	@SimpleId
	long id;
	@SimpleVersion
	private int version;
	
	public ZongPai(){
		
	}
	
	String zpname;	
	long masterId;
	String zongzhi;
	long createTime;
	String huizhang;
	int maxJiazuCount;
	@SimpleColumn(name="level_")
	int level;
	int fanrongdu;
	@SimpleColumn(name="password1")
	String password;
	String password2;
	String passwordHint;
	String declaration;
	int rongyuzhi;
	
	List<Long> jiazuIds = new ArrayList<Long>();
	
	String seizeCity;
	int point; //报名城战摇骰子点数（没报名是0,报名后没摇是负值,报名后摇出结果是正值）
	
	public String getSeizeCity() {
		return seizeCity;
	}
	public void setSeizeCity(String seizeCity) {
		this.seizeCity = seizeCity;
		ZongPaiManager.em.notifyFieldChange(this, "seizeCity");
	}

	public ZongPai(long id){
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getZpname() {
		return zpname;
	}
	public void setZpname(String zpname) {
		this.zpname = zpname;
		ZongPaiManager.em.notifyFieldChange(this, "zpname");
	}
	public String getZongzhi() {
		return zongzhi;
	}

	public void setZongzhi(String zongzhi) {
		this.zongzhi = zongzhi;
		ZongPaiManager.em.notifyFieldChange(this, "zongzhi");
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
		ZongPaiManager.em.notifyFieldChange(this, "createTime");
	}

	public String getHuizhang() {
		return huizhang;
	}

	public void setHuizhang(String huizhang) {
		this.huizhang = huizhang;
		ZongPaiManager.em.notifyFieldChange(this, "huizhang");
	}

	public int getMaxJiazuCount() {
		return maxJiazuCount;
	}

	public void setMaxJiazuCount(int maxJiazuCount) {
		this.maxJiazuCount = maxJiazuCount;
		ZongPaiManager.em.notifyFieldChange(this, "maxJiazuCount");
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		ZongPaiManager.em.notifyFieldChange(this, "level");
	}

	public int getFanrongdu() {
		return fanrongdu;
	}

	public void setFanrongdu(int fanrongdu) {
		this.fanrongdu = fanrongdu;
		ZongPaiManager.em.notifyFieldChange(this, "fanrongdu");
	}

	public int getRongyuzhi() {
		return rongyuzhi;
	}

	public void setRongyuzhi(int rongyuzhi) {
		this.rongyuzhi = rongyuzhi;
		ZongPaiManager.em.notifyFieldChange(this, "rongyuzhi");
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		ZongPaiManager.em.notifyFieldChange(this, "password");
	}

	public String getPasswordHint() {
		return passwordHint;
	}

	public void setPasswordHint(String passwordHint) {
		this.passwordHint = passwordHint;
		ZongPaiManager.em.notifyFieldChange(this, "passwordHint");
	}

	public List<Long> getJiazuIds() {
		return jiazuIds;
	}

	public void setJiazuIds(List<Long> jiazuIds) {
		this.jiazuIds = jiazuIds;
		ZongPaiManager.em.notifyFieldChange(this, "jiazuIds");
	}
	
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
		ZongPaiManager.em.notifyFieldChange(this, "password2");
	}
	
	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
		ZongPaiManager.em.notifyFieldChange(this, "declaration");
	}
	
	public long getMasterId() {
		return masterId;
	}

	public void setMasterId(long masterId) {
		this.masterId = masterId;
		ZongPaiManager.em.notifyFieldChange(this, "masterId");
	}
	
	
	public String getLogString(){
		return "id:"+id+"name:"+zpname+"master:"+masterId;
	}
	
	/**
	 * 宗派里的家族个数
	 * @param id
	 * @return
	 */
	public int getJiazuNum(){
		return this.jiazuIds.size();
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
	private long lastAddProsperityTime;
	private int dayAddProsperityNum;

	public long getLastAddProsperityTime() {
		return lastAddProsperityTime;
	}
	public void setLastAddProsperityTime(long lastAddProsperityTime) {
		this.lastAddProsperityTime = lastAddProsperityTime;
		ZongPaiManager.em.notifyFieldChange(this, "lastAddProsperityTime");
	}
	public int getDayAddProsperityNum() {
		return dayAddProsperityNum;
	}
	public void setDayAddProsperityNum(int dayAddProsperityNum) {
		this.dayAddProsperityNum = dayAddProsperityNum;
		ZongPaiManager.em.notifyFieldChange(this, "dayAddProsperityNum");
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
		ZongPaiManager.em.notifyFieldChange(this, "point");
	}
	
	
}
