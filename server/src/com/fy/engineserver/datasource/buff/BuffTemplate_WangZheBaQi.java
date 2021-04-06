package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 王八之气
 * 一次减少敌人血量百分比
 * 
 * @author Administrator
 *
 */
public class BuffTemplate_WangZheBaQi extends BuffTemplate {
	int[] hpPercent;

	public int[] getHpPercent() {
		return hpPercent;
	}

	public void setHpPercent(int[] hpPercent) {
		this.hpPercent = hpPercent;
	}

	public BuffTemplate_WangZheBaQi(){
		setName(Translate.王者霸气);
		setDescription(Translate.王者霸气);
		hpPercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	/* (non-Javadoc)
	 * @see com.fy.engineserver.datasource.buff.BuffTemplate#createBuff(int)
	 */
	public Buff createBuff(int level) {
		Buff_WangZheBaQi buff = new Buff_WangZheBaQi();
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
		if(hpPercent != null && hpPercent.length > level){
			if(hpPercent[level] > 0)
				sb.append(Translate.translateString(Translate.王者霸气描述, new String[][]{{Translate.COUNT_1,hpPercent[level]+"%"}}));
		}
		buff.setDescription(sb.toString());
		return buff;
	}
}
