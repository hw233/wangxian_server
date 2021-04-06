package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 禁止进入战场
 * 
 * 
 *
 */
public class BuffTemplate_ForbidToGoToZhanChang extends BuffTemplate{

	public BuffTemplate_ForbidToGoToZhanChang(){
		setName(Translate.text_3180);
		setDescription(Translate.text_3180);
	}
	
	public Buff createBuff(int level) {
		Buff_ForbidToGoToZhanChang buff = new Buff_ForbidToGoToZhanChang();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(Translate.text_3180);
		return buff;
	}

}
