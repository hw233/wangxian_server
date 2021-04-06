package com.fy.engineserver.datasource.buff;

/**
 * 禁止进入战场
 * 
 * 
 *
 */
public class BuffTemplate_HuiMing extends BuffTemplate{

	public BuffTemplate_HuiMing(){
		setName("");
		setDescription("");
	}
	
	public Buff createBuff(int level) {
		Buff_HuiMing buff = new Buff_HuiMing();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription("");
		return buff;
	}

}
