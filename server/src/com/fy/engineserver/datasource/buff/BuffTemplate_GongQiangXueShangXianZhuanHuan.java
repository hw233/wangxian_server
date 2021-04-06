package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 攻击强度点数血上限点数转换
 * @author Administrator
 *
 */
public class BuffTemplate_GongQiangXueShangXianZhuanHuan extends BuffTemplate{

	/**
	 * 攻击强度点数
	 */
	protected int[] gongQiangB;

	/**
	 * 血上限点数
	 */
	protected int[] totalHPB;

	public int[] getGongQiangB() {
		return gongQiangB;
	}

	public void setGongQiangB(int[] gongQiangB) {
		this.gongQiangB = gongQiangB;
	}

	public int[] getTotalHPB() {
		return totalHPB;
	}

	public void setTotalHPB(int[] totalHPB) {
		this.totalHPB = totalHPB;
	}

	public BuffTemplate_GongQiangXueShangXianZhuanHuan(){
		setName(Translate.text_3188);
		setDescription(Translate.text_3188);
		gongQiangB = new int[]{1,3,5,7,9,11,13,15,17,19};
		totalHPB = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GongQiangXueShangXianZhuanHuan buff = new Buff_GongQiangXueShangXianZhuanHuan();
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
		buff.setDescription(sb.toString());
		return buff;
	}

}
