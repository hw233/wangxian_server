package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

public class BuffTemplate_ShunJiaXueLan extends BuffTemplate{

	/**
	 * 加血
	 */
	protected int[] hp;
	/**
	 * 加蓝
	 */
	protected int[] mp;
	public int[] getHp() {
		return hp;
	}

	public void setHp(int[] hp) {
		this.hp = hp;
	}

	public int[] getMp() {
		return mp;
	}

	public void setMp(int[] mp) {
		this.mp = mp;
	}

	public BuffTemplate_ShunJiaXueLan(){
		setName(Translate.text_3354);
		setDescription(Translate.text_3354);
	}
	
	public Buff createBuff(int level) {
		Buff_ShunJiaXueLan buff = new Buff_ShunJiaXueLan();
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
		if(hp != null && hp.length > level){
			if(hp[level] > 0){
				sb.append(Translate.text_3350+hp[level]+Translate.text_3287);
			}
			if(hp[level] < 0){
				sb.append(Translate.text_3352+(-hp[level])+Translate.text_3287);
			}
		}
		if(mp != null && mp.length > level){
			if(mp[level] > 0){
				if(sb.length() != 0){
					sb.append(Translate.text_3300+mp[level]+Translate.text_3233);
				}else{
					sb.append(Translate.text_3350+mp[level]+Translate.text_3233);
				}
			}
			if(mp[level] < 0){
				if(sb.length() != 0){
					sb.append(Translate.text_3355+(-mp[level])+Translate.text_3233);
				}else{
					sb.append(Translate.text_3352+(-mp[level])+Translate.text_3233);
				}
			}
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
