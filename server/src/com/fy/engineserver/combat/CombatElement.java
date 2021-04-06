package com.fy.engineserver.combat;

/**
 * 战斗元素，具备一系列战斗属性，玩家，怪物，宠物等具有战斗能力的物体都必须实现此接口
 *
 */
public interface CombatElement {
	
	/**
	 * 得到外功
	 * @return
	 */
	public int getPhyAttack();
	
	/**
	 * 设置外功
	 * @param phyAttack
	 */
	public void setPhyAttack(int phyAttack);
	
	/**
	 * 得到外防
	 * @return
	 */
	public int getPhyDefence();
	
	/**
	 * 设置外防
	 * @param phyDefence
	 * @return
	 */
	public void setPhyDefence(int phyDefence);
	
	/**
	 * 返回非物理防御得到的物理减伤率
	 * @return 0~1000 表示千分率
	 */
	public int getPhyDefenceRateOther();
	
	/**
	 * 设置非其他外防率
	 * @param otherPhyDefenceRate
	 */
	public void setPhyDefenceRateOther(int otherPhyDefenceRate);
	
	/**
	 * 得到内法
	 * @return
	 */
	public int getMagicAttack();
	
	/**
	 * 设置内攻
	 * @param magicAttack
	 * @return
	 */
	public void setMagicAttack(int magicAttack);
	
	/**
	 * 得到内防
	 * @return
	 */
	public int getMagicDefence();
	
	/**
	 * 设置内防
	 * @param magicDefence
	 * @return
	 */
	public void setMagicDefence(int magicDefence);
	
	/**
	 * 返回非法力防御得到的法力减伤率
	 * @return 0~1000 表示千分率
	 */
	public int getMagicDefenceRateOther();
	
	/**
	 * 设置非法力防御得到的法力减伤率
	 * @param otherMagicDefenceRate
	 * @return
	 */
	public void setMagicDefenceRateOther(int otherMagicDefenceRate);
	
	/**
	 * 得到血上限
	 * @return
	 */
	public int getMaxHP();
	
	/**
	 * 设置最大HP
	 * @param maxHP
	 */
	public void setMaxHP(int maxHP);
	
	/**
	 * 得到蓝上限
	 * @return
	 */
	public int getMaxMP();
	
	/**
	 * 设置最大MP
	 * @param maxMP
	 */
	public void setMaxMP(int maxMP);
	
	/**
	 * 得到HP
	 * @return
	 */
	public int getHp();
	
	/**
	 * 设置当前HP
	 * @param hp
	 */
	public void setHp(int hp);
	
	/**
	 * 得到MP
	 * @return
	 */
	public int getMp();
	
	/**
	 * 设置当前MP
	 * @param mp
	 */
	public void setMp(int mp);
	
	/**
	 * 得到会心一击值
	 * @return
	 */
	public int getCriticalHit();
	
	/**
	 * 设置会心一击
	 * @param criticalHit
	 */
	public void setCriticalHit(int criticalHit);
	
	/**
	 * 返回非暴击得到的暴击率
	 * @return 0~1000 表示千分率
	 */
	public int getCriticalHitRateOther();
	
	/**
	 * 设置其他会心一击率
	 * @param otherCriticalHitRate
	 */
	public void setCriticalHitRateOther(int otherCriticalHitRate);
	
	/**
	 * 得到会心防御值
	 * @return
	 */
	public int getCriticalDefence();
	
	/**
	 * 设置会心防御
	 * @param criticalDefence
	 */
	public void setCriticalDefence(int criticalDefence);
	
	/**
	 * 返回非减暴得到的减爆率
	 * @return 0~1000 表示千分率
	 */
	public int getCriticalDefenceRateOther();

	/**
	 * 设置其他会心防御率 0~1000千分率
	 * @param otherCriticalDefenceRate
	 */
	public void setCriticalDefenceRateOther(int otherCriticalDefenceRate);
	
	/**
	 * 得到命中值
	 * @return
	 */
	public int getHit();
	
	/**
	 * 设置命中
	 * @param hit
	 */
	public void setHit(int hit);
	
	/**
	 * 由非命中等级转化的命中率，如被动技能，buff
	 * @return 0~1000 表示千分率
	 */
	public int getHitRateOther();
	
	/**
	 * 设置其他命中率
	 * @param otherHitRate
	 */
	public void setHitRateOther(int otherHitRate);
	
	/**
	 * 得到闪避
	 * @return
	 */
	public int getDodge();
	
	/**
	 * 设置闪避
	 * @param dodge
	 */
	public void setDodge(int dodge);
	
	/**
	 * 由非闪避等级转化的闪避率，如被动技能，buff
	 * @return 0~1000 表示千分率
	 */
	public int getDodgeRateOther();
	
	/**
	 * 设置其他闪避率，千分率
	 * @param otherDodgeRate
	 */
	public void setDodgeRateOther(int otherDodgeRate);
	
	/**
	 * 得到精准值
	 * @return
	 */
	public int getAccurate();
	
	/**
	 * 设置精准
	 * @param accurate
	 */
	public void setAccurate(int accurate);
	
	/**
	 * 由非精准等级转化的精准率，如被动技能，buff
	 * @return 0~1000 表示千分率
	 */
	public int getAccurateRateOther();
	
	/**
	 * 设置其他精准率，千分率
	 * @param otherAccurateRate
	 */
	public void setAccurateRateOther(int otherAccurateRate);
	
	/**
	 * 得到破防值
	 * @return
	 */
	public int getBreakDefence();
	
	/**
	 * 设置破防
	 * @param breakDefence
	 */
	public void setBreakDefence(int breakDefence);
	
	/**
	 * 返回非破防等级得到的破防率
	 * @return 0~1000 表示千分率
	 */
	public int getBreakDefenceRateOther();
	
	/**
	 * 设置其他破防率
	 * @param otherBreakDefenceRate
	 */
	public void setBreakDefenceRateOther(int otherBreakDefenceRate);
	
	/**
	 * 得到火攻
	 * @return
	 */
	public int getFireAttack();
	
	/**
	 * 设置火攻
	 * @param fireAttack
	 */
	public void setFireAttack(int fireAttack);
	
	/**
	 * 得到金防
	 * @return
	 */
	public int getFireDefence();
	
	/**
	 * 设置金防
	 * @param fireDefence
	 */
	public void setFireDefence(int fireDefence);
	
	/**
	 * 返回非金防等级得到的金防率
	 * @return 0~1000 表示千分率
	 */
	public int getFireDefenceRateOther();
	
	/**
	 * 设置其他金防率
	 * @param otherFireDefenceRate
	 */
	public void setFireDefenceRateOther(int otherFireDefenceRate);
	
	/**
	 * 得到火减防
	 * @return
	 */
	public int getFireIgnoreDefence();
	
	/**
	 * 设置火减防
	 * @param fireIgnoreDefence
	 */
	public void setFireIgnoreDefence(int fireIgnoreDefence);
	
	/**
	 * 返回非火减抗等级得到的火减抗率
	 * @return 0~1000 表示千分率
	 */
	public int getFireIgnoreDefenceRateOther();
	
	/**
	 * 设置其他火减防率
	 * @param otherFireIgnoreDefence
	 */
	public void setFireIgnoreDefenceRateOther(int otherFireIgnoreDefence);
	
	/**
	 * 得到冰攻
	 * @return
	 */
	public int getBlizzardAttack();
	
	/**
	 * 设置冰攻
	 * @param blizzardAttack
	 */
	public void setBlizzardAttack(int blizzardAttack);
	
	/**
	 * 得到水防
	 * @return
	 */
	public int getBlizzardDefence();
	
	/**
	 * 设置水防
	 * @param blizzardDefence
	 */
	public void setBlizzardDefence(int blizzardDefence);
	
	/**
	 * 返回非冰抗得到的冰抗率
	 * @return 0~1000 表示千分率
	 */
	public int getBlizzardDefenceRateOther();
	
	/**
	 * 设置其他冰抗率
	 * @param otherBlizzardDefence
	 */
	public void setBlizzardDefenceRateOther(int otherBlizzardDefence);
	
	/**
	 * 得到冰减防
	 * @return
	 */
	public int getBlizzardIgnoreDefence();
	
	/**
	 * 设置冰减防
	 * @param blizzardIgnoreDefence
	 */
	public void setBlizzardIgnoreDefence(int blizzardIgnoreDefence);
	
	/**
	 * 返回非冰减抗得到的冰减抗率
	 * @return 0~1000 表示千分率
	 */
	public int getBlizzardIgnoreDefenceRateOther();
	
	/**
	 * 设置其他冰减抗率
	 * @param otherBlizzardIgnoreDefenceRate
	 */
	public void setBlizzardIgnoreDefenceRateOther(int otherBlizzardIgnoreDefenceRate);
	
	/**
	 * 得到风攻
	 * @return
	 */
	public int getWindAttack();
	
	/**
	 * 设置风攻
	 * @param windAttack
	 */
	public void setWindAttack(int windAttack);
	
	/**
	 * 得到火防
	 * @return
	 */
	public int getWindDefence();
	
	/**
	 * 设置火防
	 * @param windDefence
	 */
	public void setWindDefence(int windDefence);
	
	/**
	 * 返回非风抗得到的风抗率
	 * @return 0~1000 表示千分率
	 */
	public int getWindDefenceRateOther();
	
	/**
	 * 设置其他火防率
	 * @param otherWindDefenceRate
	 */
	public void setWindDefenceRateOther(int otherWindDefenceRate);
	
	/**
	 * 得到风减防
	 * @return
	 */
	public int getWindIgnoreDefence();
	
	/**
	 * 设置风减防
	 * @param windIgnoreDefence
	 */
	public void setWindIgnoreDefence(int windIgnoreDefence);
	
	/**
	 * 返回非风减抗得到的风减抗率
	 * @return 0~1000 表示千分率
	 */
	public int getWindIgnoreDefenceRateOther();
	
	/**
	 * 设置其他风减防率
	 * @param otherWindIgnoreDefenceRate
	 */
	public void setWindIgnoreDefenceRateOther(int otherWindIgnoreDefenceRate);
	
	/**
	 * 得到雷攻
	 * @return
	 */
	public int getThunderAttack();
	
	/**
	 * 设置雷攻
	 * @param thunderAttack
	 */
	public void setThunderAttack(int thunderAttack);
	
	/**
	 * 得到木防
	 * @return
	 */
	public int getThunderDefence();
	
	/**
	 * 设置木防
	 * @param thunderDefence
	 */
	public void setThunderDefence(int thunderDefence);

	/**
	 * 返回非雷抗得到的雷抗率
	 * @return 0~1000 表示千分率
	 */
	public int getThunderDefenceRateOther();
	
	/**
	 * 设置其他木防率
	 * @param otherThunderDefenceRate
	 */
	public void setThunderDefenceRateOther(int otherThunderDefenceRate);
	
	/**
	 * 得到雷减防
	 * @return
	 */
	public int getThunderIgnoreDefence();
	
	/**
	 * 设置雷减防
	 * @param thunderIgnoreDefence
	 */
	public void setThunderIgnoreDefence(int thunderIgnoreDefence);
	
	/**
	 * 返回非雷减抗得到的雷减抗率
	 * @return 0~1000 表示千分率
	 */
	public int getThunderIgnoreDefenceRateOther();
	
	/**
	 * 设置其他雷减防率
	 * @param thunderIgnoreDefenceRate
	 */
	public void setThunderIgnoreDefenceRateOther(int thunderIgnoreDefenceRate);
	
}
