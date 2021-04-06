package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_QuShanXuLuo extends BuffTemplate{

	public BuffTemplate_QuShanXuLuo(){
		setName(Translate.text_3338);
		setDescription(Translate.text_3338);
	}
	
	public Buff createBuff(int level) {
		Buff_QuShanXuLuo buff = new Buff_QuShanXuLuo();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3338);
		buff.setIconId(iconId);
		return buff;
	}

}
