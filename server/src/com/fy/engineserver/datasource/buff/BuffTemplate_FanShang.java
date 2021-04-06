package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 增加返伤
 * 
 * 
 *
 */
public class BuffTemplate_FanShang extends BuffTemplate{

	/**
	 * 各个级别抽取的百分比
	 */
	protected int ironMaidenPercent[];
	
	

	public int[] getIronMaidenPercent() {
		return ironMaidenPercent;
	}

	public void setIronMaidenPercent(int[] ironMaidenPercent) {
		this.ironMaidenPercent = ironMaidenPercent;
	}

	public BuffTemplate_FanShang(){
		setName(Translate.反伤);
		setDescription(Translate.反伤);
		ironMaidenPercent = new int[]{3,5,7,9,11,13,15,17,19,21};
	}
	
	public Buff createBuff(int level) {
		Buff_FanShang buff = new Buff_FanShang();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		if(ironMaidenPercent != null && ironMaidenPercent.length > level){
			buff.setDescription(Translate.translateString(Translate.对敌人造成反伤, new String[][]{{Translate.COUNT_1,ironMaidenPercent[level]+""}}));
		}
		return buff;
	}

}
