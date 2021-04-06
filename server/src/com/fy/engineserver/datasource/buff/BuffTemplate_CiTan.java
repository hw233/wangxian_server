package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_CiTan extends BuffTemplate{

	public BuffTemplate_CiTan(){
		setName(Translate.text_3306);
		setDescription(Translate.text_3307);
	}
	
	public Buff createBuff(int level) {
		Buff_CiTan buff = new Buff_CiTan();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(Translate.刺探);
		return buff;
	}

}
