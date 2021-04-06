package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_QuShanXuanYun extends BuffTemplate{

	public BuffTemplate_QuShanXuanYun(){
		setName(Translate.text_3337);
		setDescription(Translate.text_3337);
	}
	
	public Buff createBuff(int level) {
		Buff_QuShanXuanYun buff = new Buff_QuShanXuanYun();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.text_3337);
		buff.setIconId(iconId);
		return buff;
	}

}
