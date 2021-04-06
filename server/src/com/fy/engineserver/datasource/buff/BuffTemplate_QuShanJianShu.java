package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_QuShanJianShu extends BuffTemplate{

	public BuffTemplate_QuShanJianShu(){
		setName(Translate.text_3336);
		setDescription(Translate.text_3336);
	}
	
	public Buff createBuff(int level) {
		Buff_QuShanJianShu buff = new Buff_QuShanJianShu();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3336);
		buff.setIconId(iconId);
		return buff;
	}

}
