package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 攻击强度百分比血上限百分比转换
 * @author Administrator
 *
 */
public class BuffTemplate_GongQiangXueShangXianZhuanHuanPercent extends BuffTemplate{

	/**
	 * 攻击强度百分比
	 */
	protected int[] gongQiang;

	/**
	 * 血上限百分比
	 */
	protected int[] totalHPC;

	public int[] getGongQiang() {
		return gongQiang;
	}

	public void setGongQiang(int[] gongQiangC) {
		this.gongQiang = gongQiangC;
	}

	public int[] getTotalHPC() {
		return totalHPC;
	}

	public void setTotalHPC(int[] totalHPC) {
		this.totalHPC = totalHPC;
	}

	public BuffTemplate_GongQiangXueShangXianZhuanHuanPercent(){
		setName(Translate.text_3189);
		setDescription(Translate.text_3189);
		gongQiang = new int[]{1,3,5,7,9,11,13,15,17,19};
		totalHPC = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_GongQiangXueShangXianZhuanHuanPercent buff = new Buff_GongQiangXueShangXianZhuanHuanPercent();
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
		if(gongQiang != null && gongQiang.length > level){
			if(gongQiang[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3182+gongQiang[level]+"%");
			}else if(gongQiang[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3183+(-gongQiang[level])+"%");
			}
		}
		if(totalHPC != null && totalHPC.length > level){
			if(totalHPC[level] > 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3144+totalHPC[level]+"%");
			}else if(totalHPC[level] < 0){
				if(sb.length() > 0)
					sb.append("，");
				
				sb.append(Translate.text_3145+(-totalHPC[level])+"%");
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
