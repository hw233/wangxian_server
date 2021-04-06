package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_LiJiDuBi extends BuffTemplate{

	public BuffTemplate_LiJiDuBi(){
		setName(Translate.text_3310);
		setDescription(Translate.text_3311);
	}
	
	public Buff createBuff(int level) {
		Buff_LiJiDuBi buff = new Buff_LiJiDuBi();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(this.getDescription());
		buff.setIconId(iconId);
		return buff;
	}

}
