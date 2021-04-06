package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加仇恨比例
 * 
 * 
 *
 */
public class BuffTemplate_CouHen extends BuffTemplate{

	/**
	 * 各个级别抽取的百分比
	 */
	protected int hatridRate[];
	
	

	public int[] getHatridRate() {
		return hatridRate;
	}

	public void setHatridRate(int[] hatridRate) {
		this.hatridRate = hatridRate;
	}

	public BuffTemplate_CouHen(){
		setName(Translate.text_3165);
		setDescription(Translate.text_3166);
		hatridRate = new int[]{10,20,50,70,90,110,130,150,170,190};
	}
	
	public Buff createBuff(int level) {
		Buff_CouHen buff = new Buff_CouHen();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(hatridRate != null && hatridRate.length > level){
			buff.setDescription(Translate.text_3167+hatridRate[level]+"%");
		}else{
			buff.setDescription(Translate.text_3167);
		}
		return buff;
	}

}
