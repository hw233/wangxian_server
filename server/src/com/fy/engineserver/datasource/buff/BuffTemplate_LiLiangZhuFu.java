package com.fy.engineserver.datasource.buff;

import com.fy.engineserver.datasource.language.Translate;

/**
 *增加法攻、破甲、暴击、命中、精准比例
 */
public class BuffTemplate_LiLiangZhuFu extends BuffTemplate{

	/**
	 * 法攻百分比
	 */
	protected int[] magicAttack;
	/** 破甲百分比 */
	protected int[] breakDefence;
	/** 暴击百分比 */
	protected int[] critical;
	/** 命中百分比 */
	protected int[] hitRate;
	/** 精准百分比 */
	private int[] jingzhun;



	public BuffTemplate_LiLiangZhuFu(){
		setName(Translate.增加双防攻强);
		setDescription(Translate.增加双防攻强);
		magicAttack = new int[]{1,3,5,7,9,11,13,15,17,19};
		breakDefence = new int[]{1,3,5,7,9,11,13,15,17,19};
		critical = new int[]{1,3,5,7,9,11,13,15,17,19};
		hitRate = new int[]{1,3,5,7,9,11,13,15,17,19};
		setJingzhun(new int[]{1,3,5,7,9,11,13,15,17,19});
	}
	
	public Buff createBuff(int level) {
		Buff_LiLiangZhuFu buff = new Buff_LiLiangZhuFu();
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
		if(magicAttack != null && magicAttack.length > level && magicAttack[level] > 0){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(String.format(Translate.增加法攻比例, magicAttack[level]+"%"));
		}
		if(breakDefence != null && breakDefence.length > level){
			if(sb.length() > 0)
				sb.append("，");
			sb.append(String.format(Translate.增加破甲比例, breakDefence[level]+"%"));
		}
		if (critical != null && critical.length > level) {
			if(sb.length() > 0)
				sb.append("，");
			sb.append(String.format(Translate.增加暴击比例, critical[level]+"%"));
		}
		if (hitRate != null && hitRate.length > level) {
			if(sb.length() > 0)
				sb.append("，");
			sb.append(String.format(Translate.增加命中比例, hitRate[level]+"%"));
		}
		if (getJingzhun() != null && getJingzhun().length > level) {
			if(sb.length() > 0)
				sb.append("，");
			sb.append(String.format(Translate.增加精准比例, getJingzhun()[level]+"%"));
		}
		buff.setDescription(sb.toString());
		return buff;
	}

	public int[] getJingzhun() {
		return jingzhun;
	}

	public void setJingzhun(int[] jingzhun) {
		this.jingzhun = jingzhun;
	}

	public int[] getMagicAttack() {
		return magicAttack;
	}

	public void setMagicAttack(int[] magicAttack) {
		this.magicAttack = magicAttack;
	}

	public int[] getBreakDefence() {
		return breakDefence;
	}

	public void setBreakDefence(int[] breakDefence) {
		this.breakDefence = breakDefence;
	}

	public int[] getCritical() {
		return critical;
	}

	public void setCritical(int[] critical) {
		this.critical = critical;
	}

	public int[] getHitRate() {
		return hitRate;
	}

	public void setHitRate(int[] hitRate) {
		this.hitRate = hitRate;
	}
	

}
