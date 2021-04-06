package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_DingShengFengMo extends BuffTemplate{

	public BuffTemplate_DingShengFengMo(){
		setName(Translate.text_3170);
		setDescription(Translate.text_3170);
	}
	
	public Buff createBuff(int level) {
		Buff_DingShengFengMo buff = new Buff_DingShengFengMo();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.定身且封魔);
		buff.setIconId(iconId);
		return buff;
	}

}
