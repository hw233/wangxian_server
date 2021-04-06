package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 攻击强度点数暴击点数转换
 * @author Administrator
 *
 */
public class BuffTemplate_GongQiangBaoJiZhuanHuan extends BuffTemplate{

	/**
	 * 攻击强度点数
	 */
	protected int[] gongQiangB;

	/**
	 * 暴击点数
	 */
	protected int[] criticalHitB;

	public int[] getGongQiangB() {
		return gongQiangB;
	}

	public void setGongQiangB(int[] gongQiangB) {
		this.gongQiangB = gongQiangB;
	}

	public int[] getCriticalHitB() {
		return criticalHitB;
	}

	public void setCriticalHitB(int[] criticalHitB) {
		this.criticalHitB = criticalHitB;
	}

	public BuffTemplate_GongQiangBaoJiZhuanHuan(){
		setName(Translate.text_3181);
		setDescription(Translate.text_3181);
		gongQiangB = new int[]{1,3,5,7,9,11,13,15,17,19};
		criticalHitB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GongQiangBaoJiZhuanHuan buff = new Buff_GongQiangBaoJiZhuanHuan();
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
		if(gongQiangB != null && gongQiangB.length > level){
			if(gongQiangB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3182+gongQiangB[level]+Translate.text_1469);
			}else if(gongQiangB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3183+(-gongQiangB[level])+Translate.text_1469);
			}
		}
		if(criticalHitB != null && criticalHitB.length > level){
			if(criticalHitB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3139+criticalHitB[level]+Translate.text_1469);
			}else if(criticalHitB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3146+(-criticalHitB[level])+Translate.text_1469);
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
