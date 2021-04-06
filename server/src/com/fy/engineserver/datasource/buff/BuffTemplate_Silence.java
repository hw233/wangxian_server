package com.fy.engineserver.datasource.buff;

public class BuffTemplate_Silence extends BuffTemplate{
	
	public BuffTemplate_Silence(){
		setName("");
		setDescription("");
	}
	
	public Buff createBuff(int level) {
		Buff_Silence2 buff = new Buff_Silence2();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setSilenceLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setIconId(iconId);
		buff.setDescription("");
		return buff;
	}

}
