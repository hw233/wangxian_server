package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 血上限点数暴击点数转换
 * @author Administrator
 *
 */
public class BuffTemplate_BaoJiXueShangXianZhuanHuan extends BuffTemplate{

	/**
	 * 血上限点数
	 */
	protected int[] totalHPB;

	/**
	 * 暴击点数
	 */
	protected int[] criticalHitB;

	public int[] getTotalHPB() {
		return totalHPB;
	}

	public void setTotalHPB(int[] totalHPB) {
		this.totalHPB = totalHPB;
	}

	public int[] getCriticalHitB() {
		return criticalHitB;
	}

	public void setCriticalHitB(int[] criticalHitB) {
		this.criticalHitB = criticalHitB;
	}

	public BuffTemplate_BaoJiXueShangXianZhuanHuan(){
		setName(Translate.text_3143);
		setDescription(Translate.text_3143);
		totalHPB = new int[]{1,3,5,7,9,11,13,15,17,19};
		criticalHitB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_BaoJiXueShangXianZhuanHuan buff = new Buff_BaoJiXueShangXianZhuanHuan();
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
		if(totalHPB != null && totalHPB.length > level){
			if(totalHPB[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3144+totalHPB[level]+Translate.text_1469);
			}else if(totalHPB[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3145+(-totalHPB[level])+Translate.text_1469);
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
