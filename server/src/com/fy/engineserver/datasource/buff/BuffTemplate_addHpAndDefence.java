package com.fy.engineserver.datasource.buff;



/**
 * 修改双防和气血百分比 
 * 
 *
 */
public class BuffTemplate_addHpAndDefence extends BuffTemplate{
	private int[] hpNums;
	
	private int[] attackNum;

	public BuffTemplate_addHpAndDefence(){
		setName("增加角色双攻及气血上限");
		setDescription("增加角色双攻及气血上限");
	}
	
	public Buff createBuff(int level) {
		Buff_addHpAndDefence buff = new Buff_addHpAndDefence();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		String des = "";
		buff.setDescription(des);
		return buff;
	}

	public int[] getHpNums() {
		return hpNums;
	}

	public void setHpNums(int[] hpNums) {
		this.hpNums = hpNums;
	}

	public int[] getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int[] attackNum) {
		this.attackNum = attackNum;
	}
	

}
