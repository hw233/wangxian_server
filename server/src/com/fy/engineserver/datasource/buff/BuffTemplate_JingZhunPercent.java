package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_JingZhunPercent extends BuffTemplate{

	/**
	 * 精准值百分比
	 */
	protected int[] accurateC;

	public int[] getAccurateC() {
		return accurateC;
	}

	public void setAccurateC(int[] accurateC) {
		this.accurateC = accurateC;
	}

	public BuffTemplate_JingZhunPercent(){
		setName(Translate.精准);
		setDescription(Translate.精准);
		accurateC = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JingZhunPercent buff = new Buff_JingZhunPercent();
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
		if(accurateC != null && accurateC.length > level){
			if(accurateC[level] > 0){
				sb.append(Translate.translateString(Translate.精准详细描述,new String[][]{{Translate.STRING_1,accurateC[level]+"%"}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
