package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.activity.xianling.XianLingManager;

public class BuffTemplate_MeetMonster extends BuffTemplate {

	private int[] buffIds; // 从1开始

	public BuffTemplate_MeetMonster() {
		setName("");
		setDescription("");
	}

	public Buff createBuff(int level) {
		Buff_MeetMonster buff = new Buff_MeetMonster();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String des = XianLingManager.instance.meetMonsterRateMap.get(level+1).getName();
		buff.setDescription(des);
		return buff;
	}

	public int[] getBuffIds() {
		return buffIds;
	}

	public void setBuffIds(int[] buffIds) {
		this.buffIds = buffIds;
	}

}
