package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 将目标瞬间拉向自己，并使目标晕眩
 * 
 * 
 *
 */
public class BuffTemplate_ZhuaRenAndXuanYun extends BuffTemplate{

	public BuffTemplate_ZhuaRenAndXuanYun(){
		setName(Translate.text_3280);
		setDescription(Translate.text_3281);
	}
	
	public Buff createBuff(int level) {
		Buff_ZhuaRenAndXuanYun buff = new Buff_ZhuaRenAndXuanYun();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription(Translate.将目标瞬间拉向自己并使目标晕眩);
		return buff;
	}

}
