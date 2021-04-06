package com.fy.engineserver.datasource.buff;


/**
 * 冰冻
 * 
 * 
 *
 */
public class BuffTemplate_BingDong extends BuffTemplate{

	public BuffTemplate_BingDong(){
		setName("冰箱");
		setDescription("冰箱");
	}
	
	public Buff createBuff(int level) {
		Buff_BingDong buff = new Buff_BingDong();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(this.getDescription());
		return buff;
	}

}
