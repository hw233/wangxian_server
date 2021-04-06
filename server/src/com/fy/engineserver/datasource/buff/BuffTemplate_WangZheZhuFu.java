package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 *增加双防、物防、闪避以及免暴属性   c值
 */
public class BuffTemplate_WangZheZhuFu extends BuffTemplate{

	/**
	 * 双防的百分比
	 */
	protected int[] shuangFang;
	/** 闪避百分比 */
	protected int[] dodgePercent;
	/** 免暴百分比 */
	protected int[] criticalDefance;

	public int[] getShuangFang() {
		return shuangFang;
	}

	public void setShuangFang(int[] shuangFang) {
		this.shuangFang = shuangFang;
	}


	public BuffTemplate_WangZheZhuFu(){
		setName(Translate.增加双防攻强);
		setDescription(Translate.增加双防攻强);
		shuangFang = new int[]{1,3,5,7,9,11,13,15,17,19};
		dodgePercent = new int[]{1,3,5,7,9,11,13,15,17,19};
		criticalDefance = new int[]{1,3,5,7,9,11,13,15,17,19};
	}
	
	public Buff createBuff(int level) {
		Buff_WangZheZhuFu buff = new Buff_WangZheZhuFu();
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
		if(dodgePercent != null && dodgePercent.length > level && dodgePercent[level] > 0){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(String.format(Translate.增加闪避比例, dodgePercent[level]+"%"));
		}
		if (criticalDefance != null && criticalDefance.length > level) {
			if(sb.length() > 0)
				sb.append("，");
			sb.append(String.format(Translate.增加免暴比例, criticalDefance[level]+"%"));
		}
		buff.setDescription(sb.toString());
		return buff;
	}

	public int[] getDodgePercent() {
		return dodgePercent;
	}

	public void setDodgePercent(int[] dodgePercent) {
		this.dodgePercent = dodgePercent;
	}

	public int[] getCriticalDefance() {
		return criticalDefance;
	}

	public void setCriticalDefance(int[] criticalDefance) {
		this.criticalDefance = criticalDefance;
	}
	
	

}
