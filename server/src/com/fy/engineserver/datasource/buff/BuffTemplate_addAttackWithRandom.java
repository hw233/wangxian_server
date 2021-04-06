package com.fy.engineserver.datasource.buff;



public class BuffTemplate_addAttackWithRandom extends BuffTemplate{
	/** 法攻下限 */
	protected int[] minMagicAttackYs;
	/** 法攻上限 */
	protected int[] maxMagicAttackYs;
	/** 物攻下限 */
	protected int[] minPhyAttackYs;
	/** 物攻上限 */
	protected int[] maxPhyAttackYs;
	
	public BuffTemplate_addAttackWithRandom() {
		minMagicAttackYs = new int[]{1,2,3,4,5,6,7,8,9,10};
		maxMagicAttackYs = new int[]{10,20,30,40,50,60,70,80,90,100};
		minPhyAttackYs = new int[]{1,2,3,4,5,6,7,8,9,10};
		maxPhyAttackYs = new int[]{10,20,30,40,50,60,70,80,90,100};
	}

	@Override
	public Buff createBuff(int level) {
		// TODO Auto-generated method stub
		Buff_AddAttackWithRandom buff = new Buff_AddAttackWithRandom();
		buff.setTemplate(this);
		buff.setGroupId(this.getGroupId());
		buff.setTemplateName(this.getName());
		buff.setLevel(level);
		buff.setAdvantageous(isAdvantageous());
		buff.setFightStop(this.isFightStop());
		buff.setCanUseType(this.getCanUseType());
		buff.setSyncWithClient(this.isSyncWithClient());
		buff.setIconId(iconId);
		buff.setDescription("");
		return buff;
	}

	public int[] getMinMagicAttackYs() {
		return minMagicAttackYs;
	}

	public void setMinMagicAttackYs(int[] minMagicAttackYs) {
		this.minMagicAttackYs = minMagicAttackYs;
	}

	public int[] getMaxMagicAttackYs() {
		return maxMagicAttackYs;
	}

	public void setMaxMagicAttackYs(int[] maxMagicAttackYs) {
		this.maxMagicAttackYs = maxMagicAttackYs;
	}

	public int[] getMinPhyAttackYs() {
		return minPhyAttackYs;
	}

	public void setMinPhyAttackYs(int[] minPhyAttackYs) {
		this.minPhyAttackYs = minPhyAttackYs;
	}

	public int[] getMaxPhyAttackYs() {
		return maxPhyAttackYs;
	}

	public void setMaxPhyAttackYs(int[] maxPhyAttackYs) {
		this.maxPhyAttackYs = maxPhyAttackYs;
	}
	
	
	
}
