package com.fy.engineserver.datasource.article.data.magicweapon.model;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 法宝基础属性配表----此数值不可直接用，需要使用公式计算
 * @author Administrator
 *
 */
@SimpleEmbeddable
public class BasicDataModel {
	private int level;
	/** 血量值 */
	private int hpNum;
	/** 攻击（外攻、内攻都使用此值） */
	private int attackNum;
	/** 防御（内防、外防都使用此值） */
	private int defanceNum;
	/** 命中（人物属性千分比） */
	private int hitP;
	/** 闪避 */
	private int dodgeP;
	/** 暴击 */
	private int cirtP;
	
	@Override
	public String toString() {
		return "BasicDataModel [level=" + level + ", hpNum=" + hpNum + ", attackNum=" + attackNum + ", defanceNum=" + defanceNum + ", hitP=" + hitP + ", dodgeP=" + dodgeP + ", cirtP=" + cirtP + "]";
	}
	/**
	 * 获取添加属性加持类型
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static int getAttrAddType(int type) throws Exception {
		return MagicWeaponConstant.add_typeNum;
//		int result = -1;
//		switch (type) {
//		case MagicWeaponConstant.hpNum: 
//			result = MagicWeaponConstant.add_typeNum;
//			break;
//		case MagicWeaponConstant.physicAttNum:
//		case MagicWeaponConstant.magicAttNum: 
//			result = MagicWeaponConstant.add_typeNum;
//			break;
//		case MagicWeaponConstant.physicDefanceNum:
//		case MagicWeaponConstant.magicDefanceNum:
//			result = MagicWeaponConstant.add_typeNum;
//			break;
//		case MagicWeaponConstant.hitNum: 
//			result = MagicWeaponConstant.add_typePercent4Person;
//			break;
//		case MagicWeaponConstant.dodgeNum: 
//			result = MagicWeaponConstant.add_typePercent4Person;
//			break;
//		case MagicWeaponConstant.cirtNum: 
//			result = MagicWeaponConstant.add_typePercent4Person;
//			break;
//		default:
//			throw new Exception("[获取基础数据异常] [不存在的类型:" + type + "]");
//		}
//		return result;
	}
	
	/**
	 * 根据类型。别直接调用此方法(此方法获取数值为配表中数值，还需要进一步计算才能得到法宝数值)
	 * @param type
	 * @return  result[0] = 增加类型    result[1]=具体数值（需要做随机）
	 * @throws Exception 
	 */
	public int[] getAttrNumByType(int type) throws Exception {
		int[] result = new int[2];
		switch (type) {
		case MagicWeaponConstant.hpNum: 
			result[1] = this.getHpNum(); 
			result[0] = MagicWeaponConstant.add_typeNum;
			break;
		case MagicWeaponConstant.physicAttNum:
		case MagicWeaponConstant.magicAttNum: 
			result[1] = this.getAttackNum();
			result[0] = MagicWeaponConstant.add_typeNum;
			break;
		case MagicWeaponConstant.physicDefanceNum:
		case MagicWeaponConstant.magicDefanceNum:
			result[1] = this.getDefanceNum();
			result[0] = MagicWeaponConstant.add_typeNum;
			break;
		case MagicWeaponConstant.hitNum: 
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getHitP();
			break;
		case MagicWeaponConstant.dodgeNum: 
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getDodgeP(); 
			break;
		case MagicWeaponConstant.cirtNum: 
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getCirtP(); 
			break;
		default:
			throw new Exception("[获取基础数据异常] [不存在的类型:" + type + "]");
		}
		result[0] = MagicWeaponConstant.add_typeNum;
		return result;
	}
	
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getHpNum() {
		return hpNum;
	}
	public void setHpNum(int hpNum) {
		this.hpNum = hpNum;
	}
	public int getAttackNum() {
		return attackNum;
	}
	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}
	public int getDefanceNum() {
		return defanceNum;
	}
	public void setDefanceNum(int defanceNum) {
		this.defanceNum = defanceNum;
	}
	public int getHitP() {
		return hitP;
	}
	public void setHitP(int hitP) {
		this.hitP = hitP;
	}
	public int getDodgeP() {
		return dodgeP;
	}
	public void setDodgeP(int dodgeP) {
		this.dodgeP = dodgeP;
	}
	public int getCirtP() {
		return cirtP;
	}
	public void setCirtP(int cirtP) {
		this.cirtP = cirtP;
	}
	
}
