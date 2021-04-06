package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_XuanYun extends BuffTemplate{

	public BuffTemplate_XuanYun(){
		setName(Translate.text_3364);
		setDescription(Translate.text_3365);
	}
	
	public Buff createBuff(int level) {
		Buff_XuanYun buff = new Buff_XuanYun();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setDescription(Translate.眩晕);
		buff.setIconId(iconId);
		return buff;
	}

}
