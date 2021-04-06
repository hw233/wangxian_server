package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_WuFaHuiXue extends BuffTemplate {

	@Override
	public Buff createBuff(int level) {
		Buff_WuFaHuiXue buff = new Buff_WuFaHuiXue();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String des = Translate.无法回血;
		buff.setDescription(des);
		return buff;
	}
}
