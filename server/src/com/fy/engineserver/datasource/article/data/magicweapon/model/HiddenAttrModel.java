package com.fy.engineserver.datasource.article.data.magicweapon.model;

import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;

/**
 * 隐藏属性配置
 * @author Administrator
 *
 */
public class HiddenAttrModel {
	/** 等级 */
	private int level;
	/** 仙法 */
	private int mp;
	/** 精准 */
	private int actp;
	/** 暴击 */
	private int cirt;
	/** 免暴 */
	private int dchp;
	/** 火攻 */
	private int fireAttc;
	/** 冰攻 */
	private int iceAttc;
	/** 风攻 */
	private int windAttc;
	/** 雷攻 */
	private int thundAttc;
	/** 破甲（千分比） */
	private int dac;
	/** 命中（千分比） */
	private int hitP;
	/** 闪避(千分比) */
	private int dodgeP;
	/** 火炕（千分比） */
	private int frt;
	/** 冰炕（千分比） */
	private int irt;
	/** 风炕（千分比） */
	private int wrt;
	/** 雷炕（千分比） */
	private int trt;
	/** 减火炕（千分比） */
	private int dfrt;
	/** 减冰炕（千分比） */
	private int dirt;
	/** 减风炕（千分比） */
	private int dwrt;
	/** 减雷炕（千分比） */
	private int dtrt;
	/** 气血 */
	private int hp;
	/** 攻击（内攻，外攻） */
	private int attact;
	
	@Override
	public String toString() {
		return "HiddenAttrModel [level=" + level + ", mp=" + mp + ", actp=" + actp + ", cirt=" + cirt + ", dchp=" + dchp + ", fireAttc=" + fireAttc + ", iceAttc=" + iceAttc + ", windAttc=" + windAttc + ", thundAttc=" + thundAttc + ", dac=" + dac + ", hitP=" + hitP + ", dodgeP=" + dodgeP + ", frt=" + frt + ", irt=" + irt + ", wrt=" + wrt + ", trt=" + trt + ", dfrt=" + dfrt + ", dirt=" + dirt + ", dwrt=" + dwrt + ", dtrt=" + dtrt + "]";
	}
	
	/**
	 * 获取添加属性加持类型
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static int getAttrAddType(int type) throws Exception {
		int result = -1;
		switch(type) {
		case MagicWeaponConstant.mpNum:	
		case MagicWeaponConstant.精准:
		case MagicWeaponConstant.cirtNum:
		case MagicWeaponConstant.免爆:
		case MagicWeaponConstant.火攻:
		case MagicWeaponConstant.冰攻:
		case MagicWeaponConstant.风攻:
		case MagicWeaponConstant.雷攻:
		case MagicWeaponConstant.physicAttNum:
		case MagicWeaponConstant.magicAttNum:
		case MagicWeaponConstant.hpNum:
			result = MagicWeaponConstant.add_typeNum;
			break;
		case MagicWeaponConstant.破甲:
		case MagicWeaponConstant.hitNum:
		case MagicWeaponConstant.dodgeNum:
		case MagicWeaponConstant.火炕:
		case MagicWeaponConstant.冰抗:
		case MagicWeaponConstant.风抗:
		case MagicWeaponConstant.雷抗:
		case MagicWeaponConstant.火减抗:
		case MagicWeaponConstant.冰减抗:
		case MagicWeaponConstant.风减抗:
		case MagicWeaponConstant.雷减抗:
			result = MagicWeaponConstant.add_typePercent4Person;
			break;
		default :
			throw new Exception("[法宝隐藏属性类型错误] [" + type + "]");	
		}
		return result;
	}
	/**
	 * 根据类型(别直接调用此方法。。。此方法获取数值为配表中数值，还需要进一步计算才能得到法宝数值)
	 * @param type
	 * @return  result[0] = 增加类型    result[1]=具体数值（需要做随机）
	 * @throws Exception 
	 */
	public int[] getAttrNumByType(int type) {
		int[] result = new int[2];
		switch(type) {
		case MagicWeaponConstant.physicAttNum:
		case MagicWeaponConstant.magicAttNum:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getAttact();
			break;
		case MagicWeaponConstant.hpNum:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getHp();
			break;
		case MagicWeaponConstant.mpNum:	
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getMp();
			break;
		case MagicWeaponConstant.精准:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getActp();
			break;
		case MagicWeaponConstant.cirtNum:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getCirt();
			break;
		case MagicWeaponConstant.免爆:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getDchp();
			break;
		case MagicWeaponConstant.火攻:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getFireAttc();
			break;
		case MagicWeaponConstant.冰攻:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getIceAttc();
			break;
		case MagicWeaponConstant.风攻:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getWindAttc();
			break;
		case MagicWeaponConstant.雷攻:
			result[0] = MagicWeaponConstant.add_typeNum;
			result[1] = this.getThundAttc();
			break;
		case MagicWeaponConstant.破甲:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getDac();
			break;
		case MagicWeaponConstant.hitNum:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getHitP();
			break;
		case MagicWeaponConstant.dodgeNum:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getDodgeP();
			break;
		case MagicWeaponConstant.火炕:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getFrt();
			break;
		case MagicWeaponConstant.冰抗:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getIrt();
			break;
		case MagicWeaponConstant.风抗:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getWrt();
			break;
		case MagicWeaponConstant.雷抗:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getTrt();
			break;
		case MagicWeaponConstant.火减抗:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getDfrt();
			break;
		case MagicWeaponConstant.冰减抗:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getDirt();
			break;
		case MagicWeaponConstant.风减抗:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getDwrt();
			break;
		case MagicWeaponConstant.雷减抗:
			result[0] = MagicWeaponConstant.add_typePercent4Person;
			result[1] = this.getDtrt();
			break;
		}
		return result;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getMp() {
		return mp;
	}
	public void setMp(int mp) {
		this.mp = mp;
	}
	public int getActp() {
		return actp;
	}
	public void setActp(int actp) {
		this.actp = actp;
	}
	public int getCirt() {
		return cirt;
	}
	public void setCirt(int cirt) {
		this.cirt = cirt;
	}
	public int getDchp() {
		return dchp;
	}
	public void setDchp(int dchp) {
		this.dchp = dchp;
	}
	public int getFireAttc() {
		return fireAttc;
	}
	public void setFireAttc(int fireAttc) {
		this.fireAttc = fireAttc;
	}
	public int getIceAttc() {
		return iceAttc;
	}
	public void setIceAttc(int iceAttc) {
		this.iceAttc = iceAttc;
	}
	public int getWindAttc() {
		return windAttc;
	}
	public void setWindAttc(int windAttc) {
		this.windAttc = windAttc;
	}
	public int getThundAttc() {
		return thundAttc;
	}
	public void setThundAttc(int thundAttc) {
		this.thundAttc = thundAttc;
	}
	public int getDac() {
		return dac;
	}
	public void setDac(int dac) {
		this.dac = dac;
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
	public int getFrt() {
		return frt;
	}
	public void setFrt(int frt) {
		this.frt = frt;
	}
	public int getIrt() {
		return irt;
	}
	public void setIrt(int irt) {
		this.irt = irt;
	}
	public int getWrt() {
		return wrt;
	}
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttact() {
		return attact;
	}

	public void setAttact(int attact) {
		this.attact = attact;
	}

	public void setWrt(int wrt) {
		this.wrt = wrt;
	}
	public int getTrt() {
		return trt;
	}
	public void setTrt(int trt) {
		this.trt = trt;
	}
	public int getDfrt() {
		return dfrt;
	}
	public void setDfrt(int dfrt) {
		this.dfrt = dfrt;
	}
	public int getDirt() {
		return dirt;
	}
	public void setDirt(int dirt) {
		this.dirt = dirt;
	}
	public int getDwrt() {
		return dwrt;
	}
	public void setDwrt(int dwrt) {
		this.dwrt = dwrt;
	}
	public int getDtrt() {
		return dtrt;
	}
	public void setDtrt(int dtrt) {
		this.dtrt = dtrt;
	}
}
