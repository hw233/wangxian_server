package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_QuShanDingSheng extends BuffTemplate{

	public BuffTemplate_QuShanDingSheng(){
		setName(Translate.text_3334);
		setDescription(Translate.text_3334);
	}
	
	public Buff createBuff(int level) {
		Buff_QuShanDingSheng buff = new Buff_QuShanDingSheng();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3335);
		buff.setIconId(iconId);
		return buff;
	}

}
