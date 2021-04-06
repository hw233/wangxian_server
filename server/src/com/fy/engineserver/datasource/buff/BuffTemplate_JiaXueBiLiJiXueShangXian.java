package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加血上限同时增加相应生命值
 * 
 * 
 *
 */
public class BuffTemplate_JiaXueBiLiJiXueShangXian extends BuffTemplate{

	/**
	 * 血上限
	 */
	protected int[] totalHPC;

	public int[] getTotalHPC() {
		return totalHPC;
	}

	public void setTotalHPC(int[] totalHPC) {
		this.totalHPC = totalHPC;
	}

	public BuffTemplate_JiaXueBiLiJiXueShangXian(){
		setName(Translate.text_3288);
		setDescription(Translate.text_3289);
		totalHPC = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_JiaXueBiLiJiXueShangXian buff = new Buff_JiaXueBiLiJiXueShangXian();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(totalHPC != null && totalHPC.length > level){
			if(totalHPC[level] > 0)
				buff.setDescription(Translate.text_3290+totalHPC[level]+Translate.text_3291);
			else if(totalHPC[level] < 0)
				buff.setDescription(Translate.text_3292+(-totalHPC[level])+"%");
		}else{
			buff.setDescription(Translate.text_3293);
		}
		return buff;
	}

}
