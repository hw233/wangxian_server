package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加血物理攻击法术攻击物理防御法术防御
 * 
 * 
 *
 */
public class BuffTemplate_XueGongQiangFangYu extends BuffTemplate{

	/**
	 * 各个等级增加血物理攻击法术攻击物理防御法术防御的百分比，10表示增加10%
	 */
	protected int percent[];

	public int[] getPercent() {
		return percent;
	}

	public void setPercent(int[] percent) {
		this.percent = percent;
	}

	public BuffTemplate_XueGongQiangFangYu(){
		setName(Translate.text_3366);
		setDescription(Translate.text_3367);
		percent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_XueGongQiangFangYu buff = new Buff_XueGongQiangFangYu();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(percent != null && percent.length > level){
			if(percent[level] > 0)
				buff.setDescription(Translate.text_3368+percent[level]+Translate.text_3369+percent[level]+Translate.text_3370);
			else if(percent[level] < 0)
				buff.setDescription(Translate.text_3371+percent[level]+Translate.text_3369+percent[level]+Translate.text_3370);
		}else{
			buff.setDescription(Translate.text_3372);
		}
		return buff;
	}

}
