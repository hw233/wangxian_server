package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_DingSheng extends BuffTemplate{

	public BuffTemplate_DingSheng(){
		setName(Translate.text_3170);
		setDescription(Translate.text_3170);
	}
	
	public Buff createBuff(int level) {
		Buff_DingSheng buff = new Buff_DingSheng();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.定身);
		buff.setIconId(iconId);
		return buff;
	}

}
