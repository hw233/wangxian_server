/**
 * 
 */
package com.fy.engineserver.masterAndPrentice;

import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndex;
import com.xuanzhi.tools.simplejpa.annotation.SimpleIndices;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * @author Administrator
 *
 */
@SimpleEntity
@SimpleIndices({
    @SimpleIndex(members={"type","country","registerEndTime"}),
    @SimpleIndex(members={"registerEndTime"})
})
public class MasterPrenticeInfo implements Cloneable, Cacheable, CacheListener {

	private static final long serialVersionUID = -4398245454744133997L;
	
	
	public MasterPrenticeInfo(){
		
	}
	
	@SimpleId
	long id;
	@SimpleVersion
	private int version;
	
	long playerId; // 谁发布的
	
	byte country; //那个国家的 只有本国的才能看见
	
	/**
	 * true师傅信息 false徒弟信息
	 */
	
	boolean type;
	
	long registerEndTime;
	
	long lastUpdateTime = 0;

	public MasterPrenticeInfo(Player player,boolean type ) {
		this.playerId = player.getId();
		this.type = type;
		this.registerEndTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+ MasterPrenticeManager.REGISTER_LAST_TIME;
		this.country = player.getCountry();
	}
	
	public long getId() {
		return id;
	}
	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public long getRegisterEndTime() {
		return registerEndTime;
	}

	public void setRegisterEndTime(long registerEndTime) {
		this.registerEndTime = registerEndTime;
	}

	
	public String getLogString(){
		return "";
	}
	
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public byte getCountry() {
		return country;
	}
	
	public void setCountry(byte country) {
		this.country = country;
	}

	@Override
	public int getSize() {
		
		return 0;
	}

	@Override
	public void remove(int type) {
		
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
