package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 双防命中
 * 
 * 
 *
 */
public class BuffTemplate_ShuangFangMingZhong extends BuffTemplate{

	/**
	 * 双防的百分比
	 */
	protected int[] shuangFang;

	/**
	 * 命中率的百分比
	 */
	protected int[] attackRating;

	public int[] getAttackRating() {
		return attackRating;
	}

	public void setAttackRating(int[] attackRating) {
		this.attackRating = attackRating;
	}

	public int[] getShuangFang() {
		return shuangFang;
	}

	public void setShuangFang(int[] shuangFang) {
		this.shuangFang = shuangFang;
	}

	public BuffTemplate_ShuangFangMingZhong(){
		setName(Translate.增加双防攻强);
		setDescription(Translate.增加双防攻强);
		shuangFang = new int[]{1,3,5,7,9,11,13,15,17,19};
		attackRating = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ShuangFangMingZhong buff = new Buff_ShuangFangMingZhong();
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
		if(shuangFang != null && shuangFang.length > level){
			if(shuangFang[level] > 0)
				sb.append(Translate.translateString(Translate.增加双防百分比详细, new String[][]{{Translate.STRING_1,shuangFang[level]+"%"}}));
			else if(shuangFang[level] < 0)
				sb.append(Translate.translateString(Translate.降低双防百分比详细, new String[][]{{Translate.STRING_1,-shuangFang[level]+"%"}}));
		}
		if(attackRating != null && attackRating.length > level){
			if(attackRating[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.增加命中百分比详细, new String[][]{{Translate.STRING_1,attackRating[level]+"%"}}));
			}else if(attackRating[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				sb.append(Translate.translateString(Translate.降低命中百分比详细, new String[][]{{Translate.STRING_1,(-attackRating[level])+"%"}}));
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
