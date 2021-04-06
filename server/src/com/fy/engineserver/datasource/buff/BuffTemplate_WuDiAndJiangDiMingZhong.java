package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 无敌且降低命中
 * 
 *
 */
public class BuffTemplate_WuDiAndJiangDiMingZhong extends BuffTemplate{


	/**
	 * 降低命中率的百分比
	 */
	protected int[] attackRating;

	public int[] getAttackRating() {
		return attackRating;
	}

	public void setAttackRating(int[] attackRating) {
		this.attackRating = attackRating;
	}

	public BuffTemplate_WuDiAndJiangDiMingZhong(){
		setName(Translate.无敌且降低命中);
		setDescription(Translate.无敌且降低命中);
	}
	
	public Buff createBuff(int level) {
		Buff_WuDiAndJiangDiMingZhong buff = new Buff_WuDiAndJiangDiMingZhong();
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
		if(level >= 0 && level < attackRating.length){
			sb.append(Translate.translateString(Translate.无敌且降低命中详细,new String[][]{{Translate.STRING_1,attackRating[level]/10+"%"}}));
			buff.setDescription(sb.toString());
		}
		return buff;
	}

}
