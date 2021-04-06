package com.fy.engineserver.downcity.stat;

import java.io.Serializable;

import com.fy.engineserver.sprite.Player;

/**
 * 副本人员信息
 * 
 *
 */
public class DownCityPlayerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3466074780168805405L;

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 玩家id
	 */
	protected long id;
	
	protected String username;
	/**
	 * 名字
	 */
	protected String name;
	
	/**
	 * 职业
	 */
	protected String career;
	
	/**
	 * 等级
	 */
	protected int level;
	
	/**
	 * 血量
	 */
	protected int totalHp;
	
	/**
	 * 蓝量
	 */
	protected int totalMp;
	
	/**
	 * 物理攻击强度
	 */
	protected int meleeAttackIntensity;
	
	/**
	 * 法术攻击强度
	 */
	protected int spellAttackIntensity;
	
	/**
	 * 闪避
	 */
	protected int dodge;
	
	/**
	 * 暴击
	 */
	protected int criticalHit;
	
	/**
	 * 物理防御
	 */
	protected int defence;
	
	/**
	 * 法术防御
	 */
	protected int resistance;
	
	/**
	 * 韧性
	 */
	protected int toughness;
	
	/**
	 * 伤害输出
	 */
	protected int shanghaiShuChu;
	
	/**
	 * 治疗输出
	 */
	protected int zhiliaoShuChu;
	
	/**
	 * 受到伤害
	 */
	protected int receiveDamage;
	
	/**
	 * 受到治疗
	 */
	protected int receiveZhiliao;
	
	/**
	 * 死亡次数
	 */
	protected int deadCount;
	
	/**
	 * 缓回血量
	 */
	protected int huanhuiHp;
	
	/**
	 * 缓回蓝量
	 */
	protected int huanhuiMp;
	
	/**
	 * 瞬回血量
	 */
	protected int shunhuiHp;
	
	/**
	 * 瞬回蓝量
	 */
	protected int shunhuiMp;
	
	/**
	 * 在副本中的时长
	 */
	protected long lastingTime;
	
	/**
	 * 耐久损耗金钱
	 */
	protected int moneyForNaijiu;

	
	public void updatePlayerInfo(Player p){}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCareer() {
		return career;
	}

	public void setCareer(String career) {
		this.career = career;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getTotalHp() {
		return totalHp;
	}

	public void setTotalHp(int totalHp) {
		this.totalHp = totalHp;
	}

	public int getTotalMp() {
		return totalMp;
	}

	public void setTotalMp(int totalMp) {
		this.totalMp = totalMp;
	}

	public int getMeleeAttackIntensity() {
		return meleeAttackIntensity;
	}

	public void setMeleeAttackIntensity(int meleeAttackIntensity) {
		this.meleeAttackIntensity = meleeAttackIntensity;
	}

	public int getSpellAttackIntensity() {
		return spellAttackIntensity;
	}

	public void setSpellAttackIntensity(int spellAttackIntensity) {
		this.spellAttackIntensity = spellAttackIntensity;
	}

	public int getDodge() {
		return dodge;
	}

	public void setDodge(int dodge) {
		this.dodge = dodge;
	}

	public int getCriticalHit() {
		return criticalHit;
	}

	public void setCriticalHit(int criticalHit) {
		this.criticalHit = criticalHit;
	}

	public int getDefence() {
		return defence;
	}

	public void setDefence(int defence) {
		this.defence = defence;
	}

	public int getResistance() {
		return resistance;
	}

	public void setResistance(int resistance) {
		this.resistance = resistance;
	}

	public int getToughness() {
		return toughness;
	}

	public void setToughness(int toughness) {
		this.toughness = toughness;
	}

	public int getShanghaiShuChu() {
		return shanghaiShuChu;
	}

	public void addShanghaiShuChu(int shanghaiShuChu) {
		this.shanghaiShuChu += shanghaiShuChu;
	}

	public int getZhiliaoShuChu() {
		return zhiliaoShuChu;
	}

	public void addZhiliaoShuChu(int zhiliaoShuChu) {
		this.zhiliaoShuChu += zhiliaoShuChu;
	}

	public int getReceiveDamage() {
		return receiveDamage;
	}

	public void addReceiveDamage(int receiveDamage) {
		this.receiveDamage += receiveDamage;
	}

	public int getReceiveZhiliao() {
		return receiveZhiliao;
	}

	public void addReceiveZhiliao(int receiveZhiliao) {
		this.receiveZhiliao += receiveZhiliao;
	}

	public int getDeadCount() {
		return deadCount;
	}

	public void addDeadCount(int deadCount) {
		this.deadCount += deadCount;
	}

	public int getHuanhuiHp() {
		return huanhuiHp;
	}

	public void addHuanhuiHp(int huanhuiHp) {
		this.huanhuiHp += huanhuiHp;
	}

	public int getHuanhuiMp() {
		return huanhuiMp;
	}

	public void addHuanhuiMp(int huanhuiMp) {
		this.huanhuiMp += huanhuiMp;
	}

	public int getShunhuiHp() {
		return shunhuiHp;
	}

	public void addShunhuiHp(int shunhuiHp) {
		this.shunhuiHp += shunhuiHp;
	}

	public int getShunhuiMp() {
		return shunhuiMp;
	}

	public void addShunhuiMp(int shunhuiMp) {
		this.shunhuiMp += shunhuiMp;
	}

	public long getLastingTime() {
		return lastingTime;
	}

	public void addLastingTime(long lastingTime) {
		this.lastingTime += lastingTime;
	}

	public int getMoneyForNaijiu() {
		return moneyForNaijiu;
	}

	public void addMoneyForNaijiu(int moneyForNaijiu) {
		this.moneyForNaijiu += moneyForNaijiu;
	}

}
