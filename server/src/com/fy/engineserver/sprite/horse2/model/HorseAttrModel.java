package com.fy.engineserver.sprite.horse2.model;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;

/**
 * 坐骑拥有的属性值model
 * @author Administrator
 *
 */
public class HorseAttrModel {
	/** 性格 */
	private int natural;
	/** 气血 */
	private int hp;
	/** 外攻 */
	private int phyAttack;
	/** 外防 */
	private int phyDefance;
	/** 破甲 */
	private int breakDefance;
	/** 闪避 */
	private int dodge;
	/** 暴击 */
	private int critical;
	/** 仙法 */
	private int mp;
	/** 法攻 */
	private int magicAttack;
	/** 法防 */
	private int magicDefance;
	/** 命中 */
	private int hit;
	/** 精准 */
	private int accurate;
	/** 免暴 */
	private int criticalDefence;
	/** 庚金攻击 */
	private int fireAttack;
	/** 庚金抗性 */
	private int fireDefance;
	/** 庚金减抗 */
	private int fireBreak;
	/** 离火攻击 */
	private int windAttack;
	/** 离火抗性 */
	private int windDefance;
	/** 离火减抗 */
	private int windBreak;
	/** 葵水攻击 */
	private int blizzAttack;
	/** 葵水抗性 */
	private int blizzDefance;
	/** 葵水减抗 */
	private int blizzBreak;
	/** 乙木攻击 */
	private int thunderAttack;
	/** 乙木抗性 */
	private int thunderDefance;
	/** 乙木减抗 */
	private int thunderBreak;
	
	public int getValue(int addType) {
		switch (addType) {
		case MagicWeaponConstant.hpNum:		//血
			return hp;
		case MagicWeaponConstant.physicAttNum: //外攻
			return phyAttack;
		case MagicWeaponConstant.physicDefanceNum: //外防
			return phyDefance;
		case MagicWeaponConstant.破甲: //破甲
			return breakDefance ;
		case MagicWeaponConstant.dodgeNum: //闪避
			return dodge;
		case MagicWeaponConstant.cirtNum: //暴击
			return critical;
		case MagicWeaponConstant.mpNum: //仙法
			return mp;
		case MagicWeaponConstant.magicAttNum: //法攻
			return magicAttack;
		case MagicWeaponConstant.magicDefanceNum: //法防
			return magicDefance;
		case MagicWeaponConstant.hitNum: //命中
			return hit;
		case MagicWeaponConstant.精准: //精准
			return accurate;
		case MagicWeaponConstant.免爆: //免爆
			return criticalDefence;
		case MagicWeaponConstant.火攻: //火攻
			return fireAttack;
		case MagicWeaponConstant.火炕: //火炕
			return fireDefance ;
		case MagicWeaponConstant.火减抗: //外攻
			return fireBreak;
		case MagicWeaponConstant.风攻: //外攻
			return windAttack;
		case MagicWeaponConstant.风抗: //风抗
			return windDefance;
		case MagicWeaponConstant.风减抗: //风减抗
			return windBreak;
		case MagicWeaponConstant.冰攻: //冰攻
			return blizzAttack;
		case MagicWeaponConstant.冰抗: //冰抗
			return blizzDefance;
		case MagicWeaponConstant.冰减抗: //冰减抗
			return blizzBreak;
		case MagicWeaponConstant.雷攻: //雷攻
			return thunderAttack;
		case MagicWeaponConstant.雷抗: //雷抗
			return thunderDefance;
		case MagicWeaponConstant.雷减抗: //雷减抗
			return thunderBreak;
		default:
			break;
		}
		return 0;
	}
	/**
	 * 设置对应值
	 * @param addType
	 * @param value
	 */
	public void setValue(int addType, int value) {
		switch (addType) {
		case MagicWeaponConstant.hpNum:		//血
			this.hp = value;
			break;
		case MagicWeaponConstant.physicAttNum: //外攻
			this.phyAttack = value;
			break;
		case MagicWeaponConstant.physicDefanceNum: //外防
			this.phyDefance = value;
			break;
		case MagicWeaponConstant.破甲: //破甲
			this.breakDefance = value;
			break;
		case MagicWeaponConstant.dodgeNum: //闪避
			this.dodge = value;
			break;
		case MagicWeaponConstant.cirtNum: //暴击
			this.critical = value;
			break;
		case MagicWeaponConstant.mpNum: //仙法
			this.mp = value;
			break;
		case MagicWeaponConstant.magicAttNum: //法攻
			this.magicAttack = value;
			break;
		case MagicWeaponConstant.magicDefanceNum: //法防
			this.magicDefance = value;
			break;
		case MagicWeaponConstant.hitNum: //命中
			this.hit = value;
			break;
		case MagicWeaponConstant.精准: //精准
			this.accurate = value;
			break;
		case MagicWeaponConstant.免爆: //免爆
			this.criticalDefence = value;
			break;
		case MagicWeaponConstant.火攻: //火攻
			this.fireAttack = value;
			break;
		case MagicWeaponConstant.火炕: //火炕
			this.fireDefance = value;
			break;
		case MagicWeaponConstant.火减抗: //外攻
			this.fireBreak = value;
			break;
		case MagicWeaponConstant.风攻: //外攻
			this.windAttack = value;
			break;
		case MagicWeaponConstant.风抗: //风抗
			this.windDefance = value;
			break;
		case MagicWeaponConstant.风减抗: //风减抗
			this.windBreak = value;
			break;
		case MagicWeaponConstant.冰攻: //冰攻
			this.blizzAttack = value;
			break;
		case MagicWeaponConstant.冰抗: //冰抗
			this.blizzDefance = value;
			break;
		case MagicWeaponConstant.冰减抗: //冰减抗
			this.blizzBreak = value;
			break;
		case MagicWeaponConstant.雷攻: //雷攻
			this.thunderAttack = value;
			break;
		case MagicWeaponConstant.雷抗: //雷抗
			this.thunderDefance = value;
			break;
		case MagicWeaponConstant.雷减抗: //雷减抗
			this.thunderBreak = value;
			break;

		default:
			break;
		}
	}
	/**
	 * 得到所有属性的增加类型及具体数值
	 * @return
	 */
	public List<Integer[]> getAttrNumAndType() {
		List<Integer[]> list = new ArrayList<Integer[]>();
		Integer[] hp = new Integer[]{MagicWeaponConstant.hpNum, this.hp};
		list.add(hp);
		Integer[] phyAttack = new Integer[]{MagicWeaponConstant.physicAttNum ,this.phyAttack};
		list.add(phyAttack);
		Integer[] phyDefance = new Integer[]{MagicWeaponConstant.physicDefanceNum ,this.phyDefance};
		list.add(phyDefance);
		Integer[] breakDefance = new Integer[]{MagicWeaponConstant.破甲 ,this.breakDefance};
		list.add(breakDefance);
		Integer[] dodge = new Integer[]{MagicWeaponConstant.dodgeNum ,this.dodge};
		list.add(dodge);
		Integer[] critical = new Integer[]{MagicWeaponConstant.cirtNum ,this.critical};
		list.add(critical);
		Integer[] mp = new Integer[]{MagicWeaponConstant.mpNum ,this.mp};
		list.add(mp);
		Integer[] magicAttack = new Integer[]{MagicWeaponConstant.magicAttNum ,this.magicAttack};
		list.add(magicAttack);
		Integer[] magicDefance = new Integer[]{MagicWeaponConstant.magicDefanceNum ,this.magicDefance};
		list.add(magicDefance);
		Integer[] hit = new Integer[]{MagicWeaponConstant.hitNum ,this.hit};
		list.add(hit);
		Integer[] accurate = new Integer[]{MagicWeaponConstant.精准 ,this.accurate};
		list.add(accurate);
		Integer[] criticalDefence = new Integer[]{MagicWeaponConstant.免爆 ,this.criticalDefence};
		list.add(criticalDefence);
		Integer[] fireAttack = new Integer[]{MagicWeaponConstant.火攻 ,this.fireAttack};
		list.add(fireAttack);
		Integer[] fireDefance = new Integer[]{MagicWeaponConstant.火炕 ,this.fireDefance};
		list.add(fireDefance);
		Integer[] fireBreak = new Integer[]{MagicWeaponConstant.火减抗 ,this.fireBreak};
		list.add(fireBreak);
		Integer[] windAttack = new Integer[]{MagicWeaponConstant.风攻 ,this.windAttack};
		list.add(windAttack);
		Integer[] windDefance = new Integer[]{MagicWeaponConstant.风抗 ,this.windDefance};
		list.add(windDefance);
		Integer[] windBreak = new Integer[]{MagicWeaponConstant.风减抗 ,this.windBreak};
		list.add(windBreak);
		Integer[] blizzAttack = new Integer[]{MagicWeaponConstant.冰攻 ,this.blizzAttack};
		list.add(blizzAttack);
		Integer[] blizzDefance = new Integer[]{MagicWeaponConstant.冰抗 ,this.blizzDefance};
		list.add(blizzDefance);
		Integer[] blizzBreak = new Integer[]{MagicWeaponConstant.冰减抗 ,this.blizzBreak};
		list.add(blizzBreak);
		Integer[] thunderAttack = new Integer[]{MagicWeaponConstant.雷攻 ,this.thunderAttack};
		list.add(thunderAttack);
		Integer[] thunderDefance = new Integer[]{MagicWeaponConstant.雷抗 ,this.thunderDefance};
		list.add(thunderDefance);
		Integer[] thunderBreak = new Integer[]{MagicWeaponConstant.雷减抗 ,this.thunderBreak};
		list.add(thunderBreak);
		return list;
	}
	
	@Override
	public String toString() {
		return "HorseAttrModel [natural=" + natural + ", hp=" + hp + ", phyAttack=" + phyAttack + ", phyDefance=" + phyDefance + ", breakDefance=" + breakDefance + ", dodge=" + dodge + ", critical=" + critical + ", mp=" + mp + ", magicAttack=" + magicAttack + ", magicDefance=" + magicDefance + ", hit=" + hit + ", accurate=" + accurate + ", criticalDefence=" + criticalDefence + ", fireAttack=" + fireAttack + ", fireDefance=" + fireDefance + ", fireBreak=" + fireBreak + ", windAttack=" + windAttack + ", windDefance=" + windDefance + ", windBreak=" + windBreak + ", blizzAttack=" + blizzAttack + ", blizzDefance=" + blizzDefance + ", blizzBreak=" + blizzBreak + ", thunderAttack=" + thunderAttack + ", thunderDefance=" + thunderDefance + ", thunderBreak=" + thunderBreak + "]";
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getPhyAttack() {
		return phyAttack;
	}
	public void setPhyAttack(int phyAttack) {
		this.phyAttack = phyAttack;
	}
	public int getPhyDefance() {
		return phyDefance;
	}
	public void setPhyDefance(int phyDefance) {
		this.phyDefance = phyDefance;
	}
	public int getBreakDefance() {
		return breakDefance;
	}
	public void setBreakDefance(int breakDefance) {
		this.breakDefance = breakDefance;
	}
	public int getDodge() {
		return dodge;
	}
	public void setDodge(int dodge) {
		this.dodge = dodge;
	}
	public int getCritical() {
		return critical;
	}
	public void setCritical(int critical) {
		this.critical = critical;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		this.mp = mp;
	}
	public int getMagicAttack() {
		return magicAttack;
	}
	public void setMagicAttack(int magicAttack) {
		this.magicAttack = magicAttack;
	}
	public int getMagicDefance() {
		return magicDefance;
	}
	public void setMagicDefance(int magicDefance) {
		this.magicDefance = magicDefance;
	}
	public int getHit() {
		return hit;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public int getAccurate() {
		return accurate;
	}
	public void setAccurate(int accurate) {
		this.accurate = accurate;
	}
	public int getCriticalDefence() {
		return criticalDefence;
	}
	public void setCriticalDefence(int criticalDefence) {
		this.criticalDefence = criticalDefence;
	}
	public int getFireAttack() {
		return fireAttack;
	}
	public void setFireAttack(int fireAttack) {
		this.fireAttack = fireAttack;
	}
	public int getFireDefance() {
		return fireDefance;
	}
	public void setFireDefance(int fireDefance) {
		this.fireDefance = fireDefance;
	}
	public int getFireBreak() {
		return fireBreak;
	}
	public void setFireBreak(int fireBreak) {
		this.fireBreak = fireBreak;
	}
	public int getWindAttack() {
		return windAttack;
	}
	public void setWindAttack(int windAttack) {
		this.windAttack = windAttack;
	}
	public int getWindDefance() {
		return windDefance;
	}
	public void setWindDefance(int windDefance) {
		this.windDefance = windDefance;
	}
	public int getWindBreak() {
		return windBreak;
	}
	public void setWindBreak(int windBreak) {
		this.windBreak = windBreak;
	}
	public int getBlizzAttack() {
		return blizzAttack;
	}
	public void setBlizzAttack(int blizzAttack) {
		this.blizzAttack = blizzAttack;
	}
	public int getBlizzDefance() {
		return blizzDefance;
	}
	public void setBlizzDefance(int blizzDefance) {
		this.blizzDefance = blizzDefance;
	}
	public int getBlizzBreak() {
		return blizzBreak;
	}
	public void setBlizzBreak(int blizzBreak) {
		this.blizzBreak = blizzBreak;
	}
	public int getThunderAttack() {
		return thunderAttack;
	}
	public void setThunderAttack(int thunderAttack) {
		this.thunderAttack = thunderAttack;
	}
	public int getThunderDefance() {
		return thunderDefance;
	}
	public void setThunderDefance(int thunderDefance) {
		this.thunderDefance = thunderDefance;
	}
	public int getThunderBreak() {
		return thunderBreak;
	}
	public void setThunderBreak(int thunderBreak) {
		this.thunderBreak = thunderBreak;
	}
	public int getNatural() {
		return natural;
	}
	public void setNatural(int natural) {
		this.natural = natural;
	}
	
	
	
}
