package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_XuanYunAndWeak extends BuffTemplate{

	public BuffTemplate_XuanYunAndWeak(){
		setName(Translate.text_3364);
		setDescription(Translate.text_3365);
	}
	
	public Buff createBuff(int level) {
		Buff_XuanYunAndWeak buff = new Buff_XuanYunAndWeak();
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
