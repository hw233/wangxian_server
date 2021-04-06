package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_QuSan extends BuffTemplate{

	public BuffTemplate_QuSan(){
		setName(Translate.text_3332);
		setDescription(Translate.text_3333);
	}
	
	public Buff createBuff(int level) {
		Buff_QuSan buff = new Buff_QuSan();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3333);
		buff.setIconId(iconId);
		return buff;
	}

}
