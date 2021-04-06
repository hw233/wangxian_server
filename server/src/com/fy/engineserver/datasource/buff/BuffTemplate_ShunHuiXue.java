package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ShunHuiXue extends BuffTemplate{

	public BuffTemplate_ShunHuiXue(){
		setName(Translate.text_3347);
		setDescription(Translate.text_3347);
	}
	
	public Buff createBuff(int level) {
		Buff_ShunHuiXue buff = new Buff_ShunHuiXue();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(Translate.text_3348);
		return buff;
	}

}
