package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_MianYi extends BuffTemplate{

	public BuffTemplate_MianYi(){
		setName(Translate.text_976);
		setDescription(Translate.text_3319);
	}
	
	public Buff createBuff(int level) {
		Buff_MianYi buff = new Buff_MianYi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3320);
		buff.setIconId(iconId);
		return buff;
	}

}
