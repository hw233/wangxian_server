package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JingZhun extends BuffTemplate{

	/**
	 * 精准值
	 */
	protected int[] accurate;

	public int[] getAccurate() {
		return accurate;
	}

	public void setAccurate(int[] accurate) {
		this.accurate = accurate;
	}

	public BuffTemplate_JingZhun(){
		setName(Translate.精准);
		setDescription(Translate.精准);
		accurate = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JingZhun buff = new Buff_JingZhun();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		StringBuffer sb = new StringBuffer();
		if(accurate != null && accurate.length > level){
			if(accurate[level] > 0){
				sb.append(Translate.translateString(Translate.精准详细描述,new String[][]{{Translate.STRING_1,accurate[level]+""}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
