package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 双防反伤
 * 
 * 
 *
 */
public class BuffTemplate_ShuangFangFanShang extends BuffTemplate{

	/**
	 * 双防的百分比
	 */
	protected int[] shuangFang;

	/**
	 * 攻强的百分比，10表示增加10%
	 */
	protected int ironMaidenPercent[];

	public int[] getShuangFang() {
		return shuangFang;
	}

	public void setShuangFang(int[] shuangFang) {
		this.shuangFang = shuangFang;
	}

	public int[] getIronMaidenPercent() {
		return ironMaidenPercent;
	}

	public void setIronMaidenPercent(int[] ironMaidenPercent) {
		this.ironMaidenPercent = ironMaidenPercent;
	}

	public BuffTemplate_ShuangFangFanShang(){
		setName(Translate.增加双防攻强);
		setDescription(Translate.增加双防攻强);
		shuangFang = new int[]{1,3,5,7,9,11,13,15,17,19};
		ironMaidenPercent = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_ShuangFangFanShang buff = new Buff_ShuangFangFanShang();
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
		if(ironMaidenPercent != null && ironMaidenPercent.length > level){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(Translate.translateString(Translate.对敌人造成反伤, new String[][]{{Translate.COUNT_1,ironMaidenPercent[level]+""}}));
		}
		buff.setDescription(sb.toString());
		return buff;
	}

}
