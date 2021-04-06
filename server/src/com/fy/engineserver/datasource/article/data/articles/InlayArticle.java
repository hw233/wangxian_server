package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.horse.Horse;

/**
 *         镶嵌类物品
 *         宝石名称 战斗属性
 *         绿宝石 MHP（血量）
 *         橙宝石 AP(外功)
 *         橙晶石 AP(内法)
 *         黑金石 AC,AC2,DGP,DCHP（外防、内防、闪躲、会心防御）
 *         蓝宝石 HITP,CHP,ACRT,DAC（命中、会心一击、精准、破甲）
 *         红宝石 FAP,FRT,DFRT（火属性攻击、抗性、减抗）
 *         白晶石 IAP,IRT,DIRT（冰属性攻击、抗性、减抗）
 *         黄宝石 WAP,WRT,DWRT（风属性攻击、抗性、减抗）
 *         紫晶石 TAP,TRT,DTRT（雷属性攻击、抗性、减抗）
 */
public class InlayArticle extends ComposeArticle {

	private String showName;
	/**
	 * 镶嵌宝石的颜色编号对应数组下标
	 */
	public static final int Inlay_Color_Green = 0;
	public static final int Inlay_Color_Orange = 1;
	public static final int Inlay_Color_Black = 2;
	public static final int Inlay_Color_Blue = 3;
	public static final int Inlay_Color_Red = 4;
	public static final int Inlay_Color_White = 5;
	public static final int Inlay_Color_Yellow = 6;
	public static final int Inlay_Color_Purple = 7;

	public static final String[] inlayColorStrings = new String[] { Translate.绿, Translate.橙, Translate.黑, Translate.蓝, Translate.红, Translate.白, Translate.黄, Translate.紫 };

	@Override
	public int getKnapsackType() {
		// TODO Auto-generated method stub
		return KNAP_异宝;
	}

	@Override
	public byte getArticleType() {
		// TODO Auto-generated method stub
		return ARTICLE_TYPE_INLAY_ARTICLE;
	}

	/**
	 * 镶嵌宝石颜色，区别于本身的物品颜色，可以是紫色品质的白宝石
	 */
	protected int inlayColorType;
	
	/**
	 * 宝石类型:宝石(1),匣子(2,3,4)
	 */
	protected int inlayType; 

	protected int classLevel;

	public int getInlayColorType() {
		return inlayColorType;
	}

	public void setInlayColorType(int inlayColorType) {
		this.inlayColorType = inlayColorType;
	}

	public int getInlayType() {
		return this.inlayType;
	}

	public void setInlayType(int inlayType) {
		this.inlayType = inlayType;
	}

	public int getClassLevel() {
		return classLevel;
	}

	public void setClassLevel(int classLevel) {
		this.classLevel = classLevel;
	}

	/**
	 * 数组中的顺序为下面所示
	 * MHP血量 AP-外功 AP-内法 AC-外防 AC-内防 DGP闪躲 DCHP会心防御 HITP命中 CHP会心一击 ACTP精准 DAC破甲 FAP火属性攻击 FRT火抗性 DFRT火减抗 IAP冰属性攻击 IRT冰抗性 DIRT冰减抗 WAP风属性攻击 WRT风抗性 DWRT风减抗 TAP雷属性攻击 TRT雷抗性 DTRT雷减抗
	 */
	public static String[] propertysValuesStrings = new String[] { Translate.血量, Translate.物理攻击, Translate.法术攻击, Translate.物理防御, Translate.法术防御, Translate.闪躲, Translate.免暴, Translate.命中, Translate.暴击, Translate.精准, Translate.破甲, Translate.庚金攻击, Translate.庚金抗性, Translate.庚金减抗, Translate.葵水攻击, Translate.葵水抗性, Translate.葵水减抗, Translate.离火攻击, Translate.离火抗性, Translate.离火减抗, Translate.乙木攻击, Translate.乙木抗性, Translate.乙木减抗 , Translate.庚金攻击, Translate.庚金抗性, Translate.庚金减抗, Translate.庚金攻击, Translate.庚金抗性, Translate.庚金减抗, Translate.葵水攻击, Translate.葵水抗性, Translate.葵水减抗, Translate.葵水攻击, Translate.葵水抗性, Translate.葵水减抗, Translate.乙木攻击, Translate.乙木抗性, Translate.乙木减抗 , Translate.乙木攻击, Translate.乙木抗性, Translate.乙木减抗 , Translate.离火攻击, Translate.离火抗性, Translate.离火减抗, Translate.离火攻击, Translate.离火抗性, Translate.离火减抗,};
	protected int[] propertysValues;

	public int[] getPropertysValues() {
		return propertysValues;
	}

	public void setPropertysValues(int[] propertysValues) {
		this.propertysValues = propertysValues;
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	/**
	 * 加载宝石属性
	 */
	public void addPropertyValue(Player player, int soulType) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
		// double add = isCurrSoul ? 1 : soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);
		int[] values = propertysValues;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						if (isCurrSoul) {
							player.setMaxHPB(player.getMaxHPB() + value);
						} else {
							player.setMaxHPX(player.getMaxHPX() + (int) (value * add));
						}
						soul.setMaxHpB(soul.getMaxHpB() + value);
						break;
					case 1:
						if (isCurrSoul) {
							player.setPhyAttackB(player.getPhyAttackB() + value);
						} else {
							player.setPhyAttackX(player.getPhyAttackX() + (int) (value * add));
						}
						soul.setPhyAttackB(soul.getPhyAttackB() + value);
						break;
					case 2:
						if (isCurrSoul) {
							player.setMagicAttackB(player.getMagicAttackB() + value);
						} else {
							player.setMagicAttackX(player.getMagicAttackX() + (int) (value * add));
						}
						soul.setMagicAttackB(soul.getMagicAttackB() + value);
						break;
					case 3:
						if (isCurrSoul) {
							player.setPhyDefenceB(player.getPhyDefenceB() + value);
						} else {
							player.setPhyDefenceX(player.getPhyDefenceX() + (int) (value * add));
						}
						soul.setPhyDefenceB(soul.getPhyDefenceB() + value);
						break;
					case 4:
						if (isCurrSoul) {
							player.setMagicDefenceB(player.getMagicDefenceB() + value);
						} else {
							player.setMagicDefenceX(player.getMagicDefenceX() + (int) (value * add));
						}
						soul.setMagicDefenceB(soul.getMagicDefenceB() + value);
						break;
					case 5:
						if (isCurrSoul) {
							player.setDodgeB(player.getDodgeB() + value);
						} else {
							player.setDodgeX(player.getDodgeX() + (int) (value * add));
						}
						soul.setDodgeB(soul.getDodgeB() + value);
						break;
					case 6:
						if (isCurrSoul) {
							player.setCriticalDefenceB(player.getCriticalDefenceB() + value);
						} else {
							player.setCriticalDefenceX(player.getCriticalDefenceX() + (int) (value * add));
						}
						soul.setCriticalDefenceB(soul.getCriticalDefenceB() + value);
						break;
					case 7:
						if (isCurrSoul) {
							player.setHitB(player.getHitB() + value);
						} else {
							player.setHitX(player.getHitX() + (int) (value * add));
						}
						soul.setHitB(soul.getHitB() + value);
						break;
					case 8:
						if (isCurrSoul) {
							player.setCriticalHitB(player.getCriticalHitB() + value);
						} else {
							player.setCriticalHitX(player.getCriticalHitX() + (int) (value * add));
						}
						soul.setCriticalHitB(soul.getCriticalHitB() + value);
						break;
					case 9:
						if (isCurrSoul) {
							player.setAccurateB(player.getAccurateB() + value);
						} else {
							player.setAccurateX(player.getAccurateX() + (int) (value * add));
						}
						soul.setAccurateB(soul.getAccurateB() + value);
						break;
					case 10:
						if (isCurrSoul) {
							player.setBreakDefenceB(player.getBreakDefenceB() + value);
						} else {
							player.setBreakDefenceX(player.getBreakDefenceX() + (int) (value * add));
						}
						soul.setBreakDefenceB(soul.getBreakDefenceB() + value);
						break;
					case 11:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() + value);
						} else {
							player.setFireAttackX(player.getFireAttackX() + (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() + value);
						break;
					case 23:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() + value);
						} else {
							player.setFireAttackX(player.getFireAttackX() + (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() + value);
						break;
					case 26:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() + value);
						} else {
							player.setFireAttackX(player.getFireAttackX() + (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() + value);
						break;
					case 12:
						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() + value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() + (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() + value);
						break;
					case 24:
						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() + value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() + (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() + value);
						break;
					case 27:

						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() + value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() + (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() + value);
						break;
					case 13:
						if (isCurrSoul) {
							player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + value);
						} else {
							player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() + (int) (value * add));
						}
						soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() + value);
						break;
					case 25:
						if (isCurrSoul) {
							player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + value);
						} else {
							player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() + (int) (value * add));
						}
						soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() + value);
						break;
					case 28:

						if (isCurrSoul) {
							player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + value);
						} else {
							player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() + (int) (value * add));
						}
						soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() + value);
						break;
					case 14:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() + value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() + (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() + value);
						break;
					case 29:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() + value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() + (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() + value);
						break;
					case 32:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() + value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() + (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() + value);
						break;
					case 15:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() + value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() + (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() + value);
						break;
					case 30:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() + value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() + (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() + value);
						break;
					case 33:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() + value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() + (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() + value);
						break;
					case 16:
						if (isCurrSoul) {
							player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + value);
						} else {
							player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() + (int) (value * add));
						}
						soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() + value);
						break;
					case 31:
						if (isCurrSoul) {
							player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + value);
						} else {
							player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() + (int) (value * add));
						}
						soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() + value);
						break;
					case 34:
						if (isCurrSoul) {
							player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + value);
						} else {
							player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() + (int) (value * add));
						}
						soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() + value);
						break;
					case 17:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() + value);
						} else {
							player.setWindAttackX(player.getWindAttackX() + (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() + value);
						break;
					case 41:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() + value);
						} else {
							player.setWindAttackX(player.getWindAttackX() + (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() + value);
						break;
					case 44:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() + value);
						} else {
							player.setWindAttackX(player.getWindAttackX() + (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() + value);
						break;
					case 18:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() + value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() + (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() + value);
						break;
					case 42:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() + value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() + (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() + value);
						break;
					case 45:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() + value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() + (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() + value);
						break;
					case 19:
						if (isCurrSoul) {
							player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + value);
						} else {
							player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() + (int) (value * add));
						}
						soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() + value);
						break;
					case 43:
						if (isCurrSoul) {
							player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + value);
						} else {
							player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() + (int) (value * add));
						}
						soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() + value);
						break;
					case 46:
						if (isCurrSoul) {
							player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + value);
						} else {
							player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() + (int) (value * add));
						}
						soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() + value);
						break;
					case 20:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() + value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() + (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() + value);
						break;
					case 35:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() + value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() + (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() + value);
						break;
					case 38:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() + value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() + (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() + value);
						break;
					case 21:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() + value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() + (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() + value);
						break;
					case 36:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() + value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() + (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() + value);
						break;
					case 39:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() + value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() + (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() + value);
						break;
					case 22:
						if (isCurrSoul) {
							player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + value);
						} else {
							player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() + (int) (value * add));
						}
						soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() + value);
						break;
					case 37:
						if (isCurrSoul) {
							player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + value);
						} else {
							player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() + (int) (value * add));
						}
						soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() + value);
						break;
					case 40:
						if (isCurrSoul) {
							player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + value);
						} else {
							player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() + (int) (value * add));
						}
						soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() + value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 卸载宝石属性
	 */
	public void removePropertyValue(Player player, int soulType) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
		double add = isCurrSoul ? 1 : (soul.getAddPercent() + player.getUpgradeNums() * 0.01);
		int[] values = propertysValues;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						if (isCurrSoul) {
							player.setMaxHPB(player.getMaxHPB() - value);
						} else {
							player.setMaxHPX(player.getMaxHPX() - (int) (value * add));
						}
						soul.setMaxHpB(soul.getMaxHpB() - value);
						break;
					case 1:
						if (isCurrSoul) {
							player.setPhyAttackB(player.getPhyAttackB() - value);
						} else {
							player.setPhyAttackX(player.getPhyAttackX() - (int) (value * add));
						}
						soul.setPhyAttackB(soul.getPhyAttackB() - value);
						break;
					case 2:
						if (isCurrSoul) {
							player.setMagicAttackB(player.getMagicAttackB() - value);
						} else {
							player.setMagicAttackX(player.getMagicAttackX() - (int) (value * add));
						}
						soul.setMagicAttackB(soul.getMagicAttackB() - value);
						break;
					case 3:

						if (isCurrSoul) {
							player.setPhyDefenceB(player.getPhyDefenceB() - value);
						} else {
							player.setPhyDefenceX(player.getPhyDefenceX() - (int) (value * add));
						}
						soul.setPhyDefenceB(soul.getPhyDefenceB() - value);
						break;
					case 4:
						if (isCurrSoul) {
							player.setMagicDefenceB(player.getMagicDefenceB() - value);
						} else {
							player.setMagicDefenceX(player.getMagicDefenceX() - (int) (value * add));
						}
						soul.setMagicDefenceB(soul.getMagicDefenceB() - value);
						break;
					case 5:

						if (isCurrSoul) {
							player.setDodgeB(player.getDodgeB() - value);
						} else {
							player.setDodgeX(player.getDodgeX() - (int) (value * add));
						}
						soul.setDodgeB(soul.getDodgeB() - value);
						break;
					case 6:
						if (isCurrSoul) {
							player.setCriticalDefenceB(player.getCriticalDefenceB() - value);
						} else {
							player.setCriticalDefenceX(player.getCriticalDefenceX() - (int) (value * add));
						}
						soul.setCriticalDefenceB(soul.getCriticalDefenceB() - value);
						break;
					case 7:
						if (isCurrSoul) {
							player.setHitB(player.getHitB() - value);
						} else {
							player.setHitX(player.getHitX() - (int) (value * add));
						}
						soul.setHitB(soul.getHitB() - value);
						break;
					case 8:
						if (isCurrSoul) {
							player.setCriticalHitB(player.getCriticalHitB() - value);
						} else {
							player.setCriticalHitX(player.getCriticalHitX() - (int) (value * add));
						}
						soul.setCriticalHitB(soul.getCriticalHitB() - value);
						break;
					case 9:
						if (isCurrSoul) {
							player.setAccurateB(player.getAccurateB() - value);
						} else {
							player.setAccurateX(player.getAccurateX() - (int) (value * add));
						}
						soul.setAccurateB(soul.getAccurateB() - value);
						break;
					case 10:
						if (isCurrSoul) {
							player.setBreakDefenceB(player.getBreakDefenceB() - value);
						} else {
							player.setBreakDefenceX(player.getBreakDefenceX() - (int) (value * add));
						}
						soul.setBreakDefenceB(soul.getBreakDefenceB() - value);
						break;
					case 11:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() - value);
						} else {
							player.setFireAttackX(player.getFireAttackX() - (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() - value);
						break;
					case 23:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() - value);
						} else {
							player.setFireAttackX(player.getFireAttackX() - (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() - value);
						break;
					case 26:
						if (isCurrSoul) {
							player.setFireAttackB(player.getFireAttackB() - value);
						} else {
							player.setFireAttackX(player.getFireAttackX() - (int) (value * add));
						}
						soul.setFireAttackB(soul.getFireAttackB() - value);
						break;
					case 12:
						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() - value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() - (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() - value);
						break;
					case 24:
						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() - value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() - (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() - value);
						break;
					case 27:
						if (isCurrSoul) {
							player.setFireDefenceB(player.getFireDefenceB() - value);
						} else {
							player.setFireDefenceX(player.getFireDefenceX() - (int) (value * add));
						}
						soul.setFireDefenceB(soul.getFireDefenceB() - value);
						break;
					case 13:
						if (isCurrSoul) {
							player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - value);
						} else {
							player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() - (int) (value * add));
						}
						soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() - value);
						break;
					case 25:
						if (isCurrSoul) {
							player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - value);
						} else {
							player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() - (int) (value * add));
						}
						soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() - value);
						break;
					case 28:
						if (isCurrSoul) {
							player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - value);
						} else {
							player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() - (int) (value * add));
						}
						soul.setFireIgnoreDefenceB(soul.getFireIgnoreDefenceB() - value);
						break;
					case 14:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() - value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() - (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() - value);
						break;
					case 29:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() - value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() - (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() - value);
						break;
					case 32:
						if (isCurrSoul) {
							player.setBlizzardAttackB(player.getBlizzardAttackB() - value);
						} else {
							player.setBlizzardAttackX(player.getBlizzardAttackX() - (int) (value * add));
						}
						soul.setBlizzardAttackB(soul.getBlizzardAttackB() - value);
						break;
					case 15:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() - value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() - (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() - value);
						break;
					case 30:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() - value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() - (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() - value);
						break;
					case 33:
						if (isCurrSoul) {
							player.setBlizzardDefenceB(player.getBlizzardDefenceB() - value);
						} else {
							player.setBlizzardDefenceX(player.getBlizzardDefenceX() - (int) (value * add));
						}
						soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() - value);
						break;
					case 16:
						if (isCurrSoul) {
							player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - value);
						} else {
							player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() - (int) (value * add));
						}
						soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() - value);
						break;
					case 31:
						if (isCurrSoul) {
							player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - value);
						} else {
							player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() - (int) (value * add));
						}
						soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() - value);
						break;
					case 34:
						if (isCurrSoul) {
							player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - value);
						} else {
							player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() - (int) (value * add));
						}
						soul.setBlizzardIgnoreDefenceB(soul.getBlizzardIgnoreDefenceB() - value);
						break;
					case 17:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() - value);
						} else {
							player.setWindAttackX(player.getWindAttackX() - (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() - value);
						break;
					case 41:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() - value);
						} else {
							player.setWindAttackX(player.getWindAttackX() - (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() - value);
						break;
					case 44:
						if (isCurrSoul) {
							player.setWindAttackB(player.getWindAttackB() - value);
						} else {
							player.setWindAttackX(player.getWindAttackX() - (int) (value * add));
						}
						soul.setWindAttackB(soul.getWindAttackB() - value);
						break;
					case 18:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() - value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() - (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() - value);
						break;
					case 42:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() - value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() - (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() - value);
						break;
					case 45:
						if (isCurrSoul) {
							player.setWindDefenceB(player.getWindDefenceB() - value);
						} else {
							player.setWindDefenceX(player.getWindDefenceX() - (int) (value * add));
						}
						soul.setWindDefenceB(soul.getWindDefenceB() - value);
						break;
					case 19:
						if (isCurrSoul) {
							player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - value);
						} else {
							player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() - (int) (value * add));
						}
						soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() - value);
						break;
					case 43:
						if (isCurrSoul) {
							player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - value);
						} else {
							player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() - (int) (value * add));
						}
						soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() - value);
						break;
					case 46:
						if (isCurrSoul) {
							player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - value);
						} else {
							player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() - (int) (value * add));
						}
						soul.setWindIgnoreDefenceB(soul.getWindIgnoreDefenceB() - value);
						break;
					case 20:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() - value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() - (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() - value);
						break;
					case 35:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() - value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() - (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() - value);
						break;
					case 38:
						if (isCurrSoul) {
							player.setThunderAttackB(player.getThunderAttackB() - value);
						} else {
							player.setThunderAttackX(player.getThunderAttackX() - (int) (value * add));
						}
						soul.setThunderAttackB(soul.getThunderAttackB() - value);
						break;
					case 21:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() - value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() - (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() - value);
						break;
					case 36:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() - value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() - (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() - value);
						break;
					case 39:
						if (isCurrSoul) {
							player.setThunderDefenceB(player.getThunderDefenceB() - value);
						} else {
							player.setThunderDefenceX(player.getThunderDefenceX() - (int) (value * add));
						}
						soul.setThunderDefenceB(soul.getThunderDefenceB() - value);
						break;
					case 22:
						if (isCurrSoul) {
							player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - value);
						} else {
							player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() - (int) (value * add));
						}
						soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() - value);
						break;
					case 37:
						if (isCurrSoul) {
							player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - value);
						} else {
							player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() - (int) (value * add));
						}
						soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() - value);
						break;
					case 40:
						if (isCurrSoul) {
							player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - value);
						} else {
							player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() - (int) (value * add));
						}
						soul.setThunderIgnoreDefenceB(soul.getThunderIgnoreDefenceB() - value);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 加载宝石属性,只加给当前元神
	 * @param player
	 */
	public void addPropertyValue(Player player){
		int[] values = propertysValues;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setMaxHPB(player.getMaxHPB() + value);
						break;
					case 1:
						player.setPhyAttackB(player.getPhyAttackB() + value);
						break;
					case 2:
						player.setMagicAttackB(player.getMagicAttackB() + value);
						break;
					case 3:
						player.setPhyDefenceB(player.getPhyDefenceB() + value);
						break;
					case 4:
						player.setMagicDefenceB(player.getMagicDefenceB() + value);
						break;
					case 5:
						player.setDodgeB(player.getDodgeB() + value);
						break;
					case 6:
						player.setCriticalDefenceB(player.getCriticalDefenceB() + value);
						break;
					case 7:
						player.setHitB(player.getHitB() + value);
						break;
					case 8:
						player.setCriticalHitB(player.getCriticalHitB() + value);
						break;
					case 9:
						player.setAccurateB(player.getAccurateB() + value);
						break;
					case 10:
						player.setBreakDefenceB(player.getBreakDefenceB() + value);
						break;
					case 11:
						player.setFireAttackB(player.getFireAttackB() + value);
						break;
					case 12:
						player.setFireDefenceB(player.getFireDefenceB() + value);
						break;
					case 13:
						player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + value);
						break;
					case 14:
						player.setBlizzardAttackB(player.getBlizzardAttackB() + value);
						break;
					case 15:
						player.setBlizzardDefenceB(player.getBlizzardDefenceB() + value);
						break;
					case 16:
						player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + value);
						break;
					case 17:
						player.setWindAttackB(player.getWindAttackB() + value);
						break;
					case 18:
						player.setWindDefenceB(player.getWindDefenceB() + value);
						break;
					case 19:
						player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + value);
						break;
					case 20:
						player.setThunderAttackB(player.getThunderAttackB() + value);
						break;
					case 21:
						player.setThunderDefenceB(player.getThunderDefenceB() + value);
						break;
					case 22:
						player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + value);
						break;
					}
				}
			}
		}
	}
	public void removePropertyValue(Player player){
		int[] values = propertysValues;
		if (player != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {
					switch (i) {
					case 0:
						player.setMaxHPB(player.getMaxHPB() - value);
						break;
					case 1:
						player.setPhyAttackB(player.getPhyAttackB() - value);
						break;
					case 2:
						player.setMagicAttackB(player.getMagicAttackB() - value);
						break;
					case 3:
						player.setPhyDefenceB(player.getPhyDefenceB() - value);
						break;
					case 4:
						player.setMagicDefenceB(player.getMagicDefenceB() - value);
						break;
					case 5:
						player.setDodgeB(player.getDodgeB() - value);
						break;
					case 6:
						player.setCriticalDefenceB(player.getCriticalDefenceB() - value);
						break;
					case 7:
						player.setHitB(player.getHitB() - value);
						break;
					case 8:
						player.setCriticalHitB(player.getCriticalHitB() - value);
						break;
					case 9:
						player.setAccurateB(player.getAccurateB() - value);
						break;
					case 10:
						player.setBreakDefenceB(player.getBreakDefenceB() - value);
						break;
					case 11:
						player.setFireAttackB(player.getFireAttackB() - value);
						break;
					case 12:
						player.setFireDefenceB(player.getFireDefenceB() - value);
						break;
					case 13:
						player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() - value);
						break;
					case 14:
						player.setBlizzardAttackB(player.getBlizzardAttackB() - value);
						break;
					case 15:
						player.setBlizzardDefenceB(player.getBlizzardDefenceB() - value);
						break;
					case 16:
						player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() - value);
						break;
					case 17:
						player.setWindAttackB(player.getWindAttackB() - value);
						break;
					case 18:
						player.setWindDefenceB(player.getWindDefenceB() - value);
						break;
					case 19:
						player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() - value);
						break;
					case 20:
						player.setThunderAttackB(player.getThunderAttackB() - value);
						break;
					case 21:
						player.setThunderDefenceB(player.getThunderDefenceB() - value);
						break;
					case 22:
						player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() - value);
						break;
					}
				}
			}
		}
	}

	/**
	 * 加载宝石属性
	 */
	public void addPropertyValueToHorse(Horse horse) {
		int[] values = propertysValues;
		if (horse != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];
				if (value != 0) {

					// value = value * lastEnergyIndex / 10;
					Player p = null;
					try {
						p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					float index = (float) (horse.getLastEnergyIndex() * 0.1);
					boolean bln = p.isIsUpOrDown() && horse.isInited() && (p.getRidingHorseId() == horse.getHorseId());
					switch (i) {
					case 0:
						horse.setMaxHPB(horse.getMaxHPB() + value);
						if (bln) {
							p.setMaxHPB(p.getMaxHPB() + (int)(value * index));
						}
						break;
					case 1:
						horse.setPhyAttackB(horse.getPhyAttackB() + value);
						if (bln) {
							p.setPhyAttackB(p.getPhyAttackB() + (int)(value * index));
						}
						break;
					case 2:
						horse.setMagicAttackB(horse.getMagicAttackB() + value);
						if (bln) {
							p.setMagicAttackB(p.getMagicAttackB() + (int)(value * index));
						}
						break;
					case 3:
						horse.setPhyDefenceB(horse.getPhyDefenceB() + value);
						if (bln) { 
							p.setPhyDefenceB(p.getPhyDefenceB() + (int)(value * index));
						}
						break;
					case 4:
						horse.setMagicDefenceB(horse.getMagicDefenceB() + value);
						if (bln) {
							p.setMagicDefenceB(p.getMagicDefenceB() + (int)(value * index));
						}
						break;
					case 5:
						horse.setDodgeB(horse.getDodgeB() + value);
						if (bln) {
							p.setDodgeB(p.getDodgeB() + (int)(value * index));
						}
						break;
					case 6:
						horse.setCriticalDefenceB(horse.getCriticalDefenceB() + value);
						if (bln) {
							p.setCriticalDefenceB(p.getCriticalDefenceB() + (int)(value * index));
						}
						break;
					case 7:
						horse.setHitB(horse.getHitB() + value);
						if (bln) {
							p.setHitB(p.getHitB() + (int)(value * index));
						}
						break;
					case 8:
						horse.setCriticalHitB(horse.getCriticalHitB() + value);
						if (bln) {
							p.setCriticalHitB(p.getCriticalHitB() + (int)(value * index));
						}
						break;
					case 9:
						horse.setAccurateB(horse.getAccurateB() + value);
						if (bln) {
							p.setAccurateB(p.getAccurateB() + (int)(value * index));
						}
						break;
					case 10:
						horse.setBreakDefenceB(horse.getBreakDefenceB() + value);
						if (bln) {
							p.setBreakDefenceB(p.getBreakDefenceB() + (int)(value * index));
						}
						break;
					case 11:
					case 23:
					case 26:
						horse.setFireAttackB(horse.getFireAttackB() + value);
						if (bln) {
							p.setFireAttackB(p.getFireAttackB() + (int)(value * index));
						}
						break;
					case 12:
					case 24:
					case 27:
						horse.setFireDefenceB(horse.getFireDefenceB() + value);
						if (bln) {
							p.setFireDefenceB(p.getFireDefenceB() + (int)(value * index));
						}
						break;
					case 13:
					case 25:
					case 28:
						horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + value);
						if (bln) {
							p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + (int)(value * index));
						}
						break;
					case 14:
					case 29:
					case 32:
						horse.setBlizzardAttackB(horse.getBlizzardAttackB() + value);
						if (bln) {
							p.setBlizzardAttackB(p.getBlizzardAttackB() + (int)(value * index));
						}
						break;
					case 15:
					case 30:
					case 33:
						horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() + value);
						if (bln) {
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() + (int)(value * index));
						}
						break;
					case 16:
					case 31:
					case 34:
						horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + value);
						if (bln) {
							p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + (int)(value * index));
						}
						break;
					case 17:
					case 41:
					case 44:
						horse.setWindAttackB(horse.getWindAttackB() + value);
						if (bln) {
							p.setWindAttackB(p.getWindAttackB() + (int)(value * index));
						}
						break;
					case 18:
					case 42:
					case 45:
						horse.setWindDefenceB(horse.getWindDefenceB() + value);
						if (bln) {
							p.setWindDefenceB(p.getWindDefenceB() + (int)(value * index));
						}
						break;
					case 19:
					case 43:
					case 46:
						horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + value);
						if (bln) {
							p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + (int)(value * index));
						}
						break;
					case 20:
					case 35:
					case 38:
						horse.setThunderAttackB(horse.getThunderAttackB() + value);
						if (bln) {
							p.setThunderAttackB(p.getThunderAttackB() + (int)(value * index));
						}
						break;
					case 21:
					case 36:
					case 39:
						horse.setThunderDefenceB(horse.getThunderDefenceB() + value);
						if (bln) {
							p.setThunderDefenceB(p.getThunderDefenceB() + (int)(value * index));
						}
						break;
					case 22:
					case 37:
					case 40:
						horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + value);
						if (bln) {
							p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + (int)(value * index));
						}
						break;
					}
				}
			}
		}
	}

	/**
	 * 卸载宝石属性
	 */
	public void removePropertyValueFromHorse(Horse horse) {
		int[] values = propertysValues;
		if (horse != null && values != null) {
			for (int i = 0; i < values.length; i++) {
				int value = values[i];

				if (value != 0) {
					// value = value * lastEnergyIndex / 10;
					Player p = null;
					try {
						p = PlayerManager.getInstance().getPlayer(horse.getOwnerId());
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					float index = (float) (horse.getLastEnergyIndex() * 0.1);
					boolean bln = p.isIsUpOrDown() && (p.getRidingHorseId() == horse.getHorseId());
					switch (i) {
					case 0:
						horse.setMaxHPB(horse.getMaxHPB() - value);
						if (bln) {
							p.setMaxHPB(p.getMaxHPB() - (int)(value*index));
						}
						break;
					case 1:
						horse.setPhyAttackB(horse.getPhyAttackB() - value);
						if (bln) {
							p.setPhyAttackB(p.getPhyAttackB() - (int)(value*index));
						}
						break;
					case 2:
						horse.setMagicAttackB(horse.getMagicAttackB() - value);
						if (bln) {
							p.setMagicAttackB(p.getMagicAttackB() - (int)(value*index));
						}
						break;
					case 3:
						horse.setPhyDefenceB(horse.getPhyDefenceB() - value);
						if (bln) {
							p.setPhyDefenceB(p.getPhyDefenceB() - (int)(value*index));
						}
						break;
					case 4:
						horse.setMagicDefenceB(horse.getMagicDefenceB() - value);
						if (bln) {
							p.setMagicDefenceB(p.getMagicDefenceB() - (int)(value*index));
						}
						break;
					case 5:
						horse.setDodgeB(horse.getDodgeB() - value);
						if (bln) {
							p.setDodgeB(p.getDodgeB() - (int)(value*index));
						}
						break;
					case 6:
						horse.setCriticalDefenceB(horse.getCriticalDefenceB() - value);
						if (bln) {
							p.setCriticalDefenceB(p.getCriticalDefenceB() - (int)(value*index));
						}
						break;
					case 7:
						horse.setHitB(horse.getHitB() - value);
						if (bln) {
							p.setHitB(p.getHitB() - (int)(value*index));
						}
						break;
					case 8:
						horse.setCriticalHitB(horse.getCriticalHitB() - value);
						if (bln) {
							p.setCriticalHitB(p.getCriticalHitB() - (int)(value*index));
						}
						break;
					case 9:
						horse.setAccurateB(horse.getAccurateB() - value);
						if (bln) {
							p.setAccurateB(p.getAccurateB() - (int)(value*index));
						}
						break;
					case 10:
						horse.setBreakDefenceB(horse.getBreakDefenceB() - value);
						if (bln) {
							p.setBreakDefenceB(p.getBreakDefenceB() - (int)(value*index));
						}
						break;
					case 11:
					case 23:
					case 26:
						horse.setFireAttackB(horse.getFireAttackB() - value);
						if (bln) {
							p.setFireAttackB(p.getFireAttackB() - (int)(value*index));
						}
						break;
					case 12:
					case 24:
					case 27:
						horse.setFireDefenceB(horse.getFireDefenceB() - value);
						if (bln) {
							p.setFireDefenceB(p.getFireDefenceB() - (int)(value*index));
						}
						break;
					case 13:
					case 25:
					case 28:
						horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - value);
						if (bln) {
							p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - (int)(value*index));
						}
						break;
					case 14:
					case 29:
					case 32:
						horse.setBlizzardAttackB(horse.getBlizzardAttackB() - value);
						if (bln) {
							p.setBlizzardAttackB(p.getBlizzardAttackB() - (int)(value*index));
						}
						break;
					case 15:
					case 30:
					case 33:
						horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() - value);
						if (bln) {
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() - (int)(value*index));
						}
						break;
					case 16:
					case 31:
					case 34:
						horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - value);
						if (bln) {
							p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - (int)(value*index));
						}
						break;
					case 17:
					case 41:
					case 44:
						horse.setWindAttackB(horse.getWindAttackB() - value);
						if (bln) {
							p.setWindAttackB(p.getWindAttackB() - (int)(value*index));
						}
						break;
					case 18:
					case 42:
					case 45:
						horse.setWindDefenceB(horse.getWindDefenceB() - value);
						if (bln) {
							p.setWindDefenceB(p.getWindDefenceB() - (int)(value*index));
						}
						break;
					case 19:
					case 43:
					case 46:
						horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - value);
						if (bln) {
							p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - (int)(value*index));
						}
						break;
					case 20:
					case 35:
					case 38:
						horse.setThunderAttackB(horse.getThunderAttackB() - value);
						if (bln) {
							p.setThunderAttackB(p.getThunderAttackB() - (int)(value*index));
						}
						break;
					case 21:
					case 36:
					case 39:
						horse.setThunderDefenceB(horse.getThunderDefenceB() - value);
						if (bln) {
							p.setThunderDefenceB(p.getThunderDefenceB() - (int)(value*index));
						}
						break;
					case 22:
					case 37:
					case 40:
						horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - value);
						if (bln) {
							p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - (int)(value*index));
						}
						break;
					}

				}
			}
		}
	}

}
