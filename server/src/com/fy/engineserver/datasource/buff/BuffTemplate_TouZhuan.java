package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_TouZhuan extends BuffTemplate{

	public BuffTemplate_TouZhuan(){
		setName(Translate.text_3306);
		setDescription(Translate.text_3307);
	}
	
	public Buff createBuff(int level) {
		Buff_TouZhuan buff = new Buff_TouZhuan();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(Translate.偷砖);
		return buff;
	}

}
