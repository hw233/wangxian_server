package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_PoFang extends BuffTemplate{

	/**
	 * 破防值
	 */
	protected int[] breakDefence;

	public int[] getBreakDefence() {
		return breakDefence;
	}

	public void setBreakDefence(int[] breakDefence) {
		this.breakDefence = breakDefence;
	}

	public BuffTemplate_PoFang(){
		setName(Translate.破防);
		setDescription(Translate.破防);
		breakDefence = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_PoFang buff = new Buff_PoFang();
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
		if(breakDefence != null && breakDefence.length > level){
			if(breakDefence[level] > 0){
				sb.append(Translate.translateString(Translate.破防详细描述,new String[][]{{Translate.STRING_1,breakDefence[level]+""}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
