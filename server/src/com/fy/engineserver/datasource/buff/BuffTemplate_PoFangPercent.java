package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_PoFangPercent extends BuffTemplate{

	/**
	 * 破防百分比
	 */
	protected int[] breakDefenceC;

	public int[] getBreakDefenceC() {
		return breakDefenceC;
	}

	public void setBreakDefenceC(int[] breakDefenceC) {
		this.breakDefenceC = breakDefenceC;
	}

	public BuffTemplate_PoFangPercent(){
		setName(Translate.破防);
		setDescription(Translate.破防);
		breakDefenceC = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_PoFangPercent buff = new Buff_PoFangPercent();
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
		if(breakDefenceC != null && breakDefenceC.length > level){
			if(breakDefenceC[level] > 0){
				sb.append(Translate.translateString(Translate.破防详细描述,new String[][]{{Translate.STRING_1,breakDefenceC[level]+"%"}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
