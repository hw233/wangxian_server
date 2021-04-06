package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ChenMo extends BuffTemplate{

	public BuffTemplate_ChenMo(){
		setName(Translate.text_3149);
		setDescription(Translate.text_3150);
	}
	
	public Buff createBuff(int level) {
		Buff_ChenMo buff = new Buff_ChenMo();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3150);
		buff.setIconId(iconId);
		return buff;
	}

}
