package com.fy.engineserver.soulpith.instance;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.soulpith.property.PropertyModule;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 灵髓
 * 
 * @date on create 2016年3月16日 下午3:00:27
 */
@SimpleEntity
public class SoulPithEntity {
	@SimpleId
	private long id;			//playerId
	@SimpleVersion
	private int version;
	/** 玩家灵髓镶嵌列表（本尊元神区分） */
	private List<PlayerSoulpithInfo> pithInfos = new ArrayList<PlayerSoulpithInfo>();
	/** 之前灵髓加的属性 */
	private transient PropertyModule oldAddProps;
	
	public PlayerSoulpithInfo getPlayerSoulpithInfo(int soulType) {
		for (int i=0; i<pithInfos.size(); i++) {
			if (pithInfos.get(i).getSoulType() == soulType) {
				return pithInfos.get(i);
			}
		}
		return null;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public PropertyModule getOldAddProps() {
		return oldAddProps;
	}
	public void setOldAddProps(PropertyModule oldAddProps) {
		this.oldAddProps = oldAddProps;
	}
	public List<PlayerSoulpithInfo> getPithInfos() {
		return pithInfos;
	}
	public void setPithInfos(List<PlayerSoulpithInfo> pithInfos) {
		this.pithInfos = pithInfos;
	}
}
