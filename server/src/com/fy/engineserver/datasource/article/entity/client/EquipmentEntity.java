package com.fy.engineserver.datasource.article.entity.client;

public class EquipmentEntity extends ArticleEntity {
	/**
	 * 装备的强化等级，从0开始
	 */
	int level = 0;

	public String[] strongParticles = new String[0];
	
	/**
	 * 装备使用级别
	 */
	short levelLimit = 0;

	/**
	 * 装备颜色
	 */
	protected byte colorType;

	/**
	 * 装备的攻防次数，每次攻防(武器攻击或装备防御)attackDefenceCount加1，不用存储
	 */
	int attackDefenceCount;

	/**
	 * 装备类型
	 */
	byte equipmentType;

	/**
	 * 武器类型
	 */
	byte weaponType;

	/**
	 * 装备的耐久值
	 * 
	 * 武器每攻击一次，耐久值就减一
	 * 其他装备每被攻击一次，耐久值就减一
	 * 
	 * 当耐久值为0时，装备就失效了，直到修复
	 */
	protected int durability;

	private boolean durabilityChangeFlag;

	/**
	 * 星级
	 */
	protected byte star;

	/**
	 * 铭刻标记，true铭刻 false未铭刻
	 */
	protected boolean inscriptionFlag;

	/**
	 * 资质
	 */
	protected int endowments;

	/**
	 * 基本属性值
	 * 基本属性包括：最大血量，外功，内法，外防，内防
	 * 顺序为上面所说顺序，在赋值时会按照顺序进行赋值
	 * 
	 */
	protected int[] basicPropertyValue = new int[5];

	/**
	 * 附加属性值
	 * 附加属性包括：法力值 破甲值 命中值 闪躲值 精准值 会心一击 会心防御
	 * 火攻值 冰攻值 风攻值 雷攻值 火抗值 冰抗值 风抗值 雷抗值
	 * 顺序为上面所说顺序，在赋值时会按照顺序进行赋值
	 * 
	 */
	protected int[] additionPropertyValue = new int[15];

	/**
	 * 镶嵌宝石数组，必须于下面的宝石颜色数组对应，数组长度为可镶嵌宝石数量
	 */
	protected long[] inlayArticleIds = new long[0];

	/**
	 * 镶嵌宝石颜色数组，必须于宝石数组对应
	 */
	protected int[] inlayArticleColors = new int[0];

	/**
	 * 制造者
	 */
	protected String createrName = "";

	/**
	 * 炼化经验
	 */
	protected int developEXP;

	/**
	 * 炼化升级经验
	 */
	protected int developUpValue;

	public String[] getStrongParticles() {
		return strongParticles;
	}

	public void setStrongParticles(String[] strongParticles) {
		this.strongParticles = strongParticles;
	}

	/**
	 * 职业限制
	 */
	protected int careerLimit;

	public int getDevelopUpValue() {
		return developUpValue;
	}

	public void setDevelopUpValue(int developUpValue) {
		this.developUpValue = developUpValue;
	}

	public int getDevelopEXP() {
		return developEXP;
	}

	public void setDevelopEXP(int developEXP) {
		this.developEXP = developEXP;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public byte getColorType() {
		return colorType;
	}

	public void setColorType(byte colorType) {
		this.colorType = colorType;
	}

	public int getAttackDefenceCount() {
		return attackDefenceCount;
	}

	public void setAttackDefenceCount(int attackDefenceCount) {
		this.attackDefenceCount = attackDefenceCount;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public boolean isDurabilityChangeFlag() {
		return durabilityChangeFlag;
	}

	public void setDurabilityChangeFlag(boolean durabilityChangeFlag) {
		this.durabilityChangeFlag = durabilityChangeFlag;
	}

	public byte getStar() {
		return star;
	}

	public void setStar(byte star) {
		this.star = star;
	}

	public boolean isInscriptionFlag() {
		return inscriptionFlag;
	}

	public void setInscriptionFlag(boolean inscriptionFlag) {
		this.inscriptionFlag = inscriptionFlag;
	}

	public int getEndowments() {
		return endowments;
	}

	public void setEndowments(int endowments) {
		this.endowments = endowments;
	}

	public int[] getBasicPropertyValue() {
		return basicPropertyValue;
	}

	public void setBasicPropertyValue(int[] basicPropertyValue) {
		this.basicPropertyValue = basicPropertyValue;
	}

	public int[] getAdditionPropertyValue() {
		return additionPropertyValue;
	}

	public void setAdditionPropertyValue(int[] additionPropertyValue) {
		this.additionPropertyValue = additionPropertyValue;
	}

	public long[] getInlayArticleIds() {
		return inlayArticleIds;
	}

	public void setInlayArticleIds(long[] inlayArticleIds) {
		this.inlayArticleIds = inlayArticleIds;
	}

	public int[] getInlayArticleColors() {
		return inlayArticleColors;
	}

	public void setInlayArticleColors(int[] inlayArticleColors) {
		this.inlayArticleColors = inlayArticleColors;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public byte getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(byte weaponType) {
		this.weaponType = weaponType;
	}

	public short getLevelLimit() {
		return levelLimit;
	}

	public void setLevelLimit(short levelLimit) {
		this.levelLimit = levelLimit;
	}

	public byte getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(byte equipmentType) {
		this.equipmentType = equipmentType;
	}

	public int getCareerLimit() {
		return careerLimit;
	}

	public void setCareerLimit(int careerLimit) {
		this.careerLimit = careerLimit;
	}
}
