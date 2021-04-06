package com.fy.engineserver.datasource.article.data.articles;

import com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.horse.Horse;

/**
 *         器灵
 *         战斗属性
 *         气血	外公修为	法术攻击	物理防御	法术防御	庚金攻击	葵水攻击	离火攻击	乙木攻击	庚金抗性	葵水抗性	离火抗性	乙木抗性
 */
public class QiLingArticle extends Article {

	/**
	 * 器灵类型，只有相应类型的器灵才能放入对应的装备器灵槽
	 */
	public byte qilingType;
	
	/**
	 * 装备类型，只有相应的装备类型才能嵌入器灵
	 */
	public byte equipmentType;
	
	//器灵属性倍数
	public int qilingTimes;
	
	public int qilingHunLiangTimes;

	public int getQilingHunLiangTimes() {
		return qilingHunLiangTimes;
	}

	public float getQiLingHunLiangTimesFloat () {
		float a = getQilingHunLiangTimes() / 10000.0f;
		return a;
	}
	
	public void setQilingHunLiangTimes(int qilingHunLiangTimes) {
		this.qilingHunLiangTimes = qilingHunLiangTimes;
	}

	public int getQilingTimes() {
		return qilingTimes;
	}

	public void setQilingTimes(int qilingTimes) {
		this.qilingTimes = qilingTimes;
	}

	public byte getQilingType() {
		return qilingType;
	}

	public void setQilingType(byte qilingType) {
		this.qilingType = qilingType;
	}

	public byte getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(byte equipmentType) {
		this.equipmentType = equipmentType;
	}

	/**
	 * 数组中的顺序为下面所示
	 * MHP血量 AP-外功 AP-内法 AC-外防 AC-内防 DGP闪躲 DCHP会心防御 HITP命中 CHP会心一击 ACTP精准 DAC破甲 FAP火属性攻击 FRT火抗性 DFRT火减抗 IAP冰属性攻击 IRT冰抗性 DIRT冰减抗 WAP风属性攻击 WRT风抗性 DWRT风减抗 TAP雷属性攻击 TRT雷抗性 DTRT雷减抗
	 */
	public static String[] propertysValuesStrings = new String[] { Translate.血量,Translate.物理攻击,Translate.法术攻击,Translate.物理防御,Translate.法术防御,Translate.庚金攻击,Translate.葵水攻击,Translate.离火攻击,Translate.乙木攻击,Translate.庚金抗性,Translate.葵水抗性,Translate.离火抗性,Translate.乙木抗性 };
	protected int[] propertysValues = new int[5];

	public int[] getPropertysValues() {
		return propertysValues;
	}

	public void setPropertysValues(int[] propertysValues) {
		this.propertysValues = propertysValues;
	}
	
	/**
	 * 根据颜色得到器灵属性上限
	 * @param colorType
	 * @return
	 */
	public int getMaxProperty(int colorType) {
		if (colorType < 0 || colorType > getPropertysValues().length) {
			return -1;
		}
		long a = ArticleManager.器灵各个颜色最大属性值[getQilingType()][colorType];
		a = a * getQilingTimes() / 10000;
		return  (int)a ;
	}

	/**
	 * 加载宝石属性 
	 */
	public void addPropertyValue(Player player, int soulType, int color, QiLingArticleEntity qae) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
//		double add = isCurrSoul ? 1 : soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent()+player.getUpgradeNums()*0.01);
		int[] values = propertysValues;
		if (player != null && values != null && values.length > color) {
			int value = qae.propertyValue;
			if (value != 0) {
				//气血	物理攻击	法术攻击	物理防御	法术防御	庚金攻击	葵水攻击	离火攻击	乙木攻击	庚金抗性	葵水抗性	离火抗性	乙木抗性
				switch (qilingType) {
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
						player.setFireAttackB(player.getFireAttackB() + value);
					} else {
						player.setFireAttackX(player.getFireAttackX() + (int) (value * add));
					}
					soul.setFireAttackB(soul.getFireAttackB() + value);
					break;
				case 6:
					if (isCurrSoul) {
						player.setBlizzardAttackB(player.getBlizzardAttackB() + value);
					} else {
						player.setBlizzardAttackX(player.getBlizzardAttackX() + (int) (value * add));
					}
					soul.setBlizzardAttackB(soul.getBlizzardAttackB() + value);
					break;
				case 7:
					if (isCurrSoul) {
						player.setWindAttackB(player.getWindAttackB() + value);
					} else {
						player.setWindAttackX(player.getWindAttackX() + (int) (value * add));
					}
					soul.setWindAttackB(soul.getWindAttackB() + value);
					break;
				case 8:
					if (isCurrSoul) {
						player.setThunderAttackB(player.getThunderAttackB() + value);
					} else {
						player.setThunderAttackX(player.getThunderAttackX() + (int) (value * add));
					}
					soul.setThunderAttackB(soul.getThunderAttackB() + value);
					break;
				case 9:
					if (isCurrSoul) {
						player.setFireDefenceB(player.getFireDefenceB() + value);
					} else {
						player.setFireDefenceX(player.getFireDefenceX() + (int) (value * add));
					}
					soul.setFireDefenceB(soul.getFireDefenceB() + value);
					break;
				case 10:
					if (isCurrSoul) {
						player.setBlizzardDefenceB(player.getBlizzardDefenceB() + value);
					} else {
						player.setBlizzardDefenceX(player.getBlizzardDefenceX() + (int) (value * add));
					}
					soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() + value);
					break;
				case 11:
					if (isCurrSoul) {
						player.setWindDefenceB(player.getWindDefenceB() + value);
					} else {
						player.setWindDefenceX(player.getWindDefenceX() + (int) (value * add));
					}
					soul.setWindDefenceB(soul.getWindDefenceB() + value);
					break;
				case 12:
					if (isCurrSoul) {
						player.setThunderDefenceB(player.getThunderDefenceB() + value);
					} else {
						player.setThunderDefenceX(player.getThunderDefenceX() + (int) (value * add));
					}
					soul.setThunderDefenceB(soul.getThunderDefenceB() + value);
					break;
				}
			}
		}
	}

	/**
	 * 卸载宝石属性
	 */
	public void removePropertyValue(Player player, int soulType, int color, QiLingArticleEntity qae) {
		boolean isCurrSoul = player.isCurrSoul(soulType);
		Soul soul = player.getSoul(soulType);
//		double add = isCurrSoul ? 1 : soul.getAddPercent();
		double add = isCurrSoul ? 1 : (soul.getAddPercent()+player.getUpgradeNums()*0.01);
		int[] values = propertysValues;
		if (player != null && values != null && values.length > color) {
			int value = qae.propertyValue;
			if (value != 0) {
				//气血	物理攻击	法术攻击	物理防御	法术防御	庚金攻击	葵水攻击	离火攻击	乙木攻击	庚金抗性	葵水抗性	离火抗性	乙木抗性
				switch (qilingType) {
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
						player.setFireAttackB(player.getFireAttackB() - value);
					} else {
						player.setFireAttackX(player.getFireAttackX() - (int) (value * add));
					}
					soul.setFireAttackB(soul.getFireAttackB() - value);
					break;
				case 6:
					if (isCurrSoul) {
						player.setBlizzardAttackB(player.getBlizzardAttackB() - value);
					} else {
						player.setBlizzardAttackX(player.getBlizzardAttackX() - (int) (value * add));
					}
					soul.setBlizzardAttackB(soul.getBlizzardAttackB() - value);
					break;
				case 7:
					if (isCurrSoul) {
						player.setWindAttackB(player.getWindAttackB() - value);
					} else {
						player.setWindAttackX(player.getWindAttackX() - (int) (value * add));
					}
					soul.setWindAttackB(soul.getWindAttackB() - value);
					break;
				case 8:
					if (isCurrSoul) {
						player.setThunderAttackB(player.getThunderAttackB() - value);
					} else {
						player.setThunderAttackX(player.getThunderAttackX() - (int) (value * add));
					}
					soul.setThunderAttackB(soul.getThunderAttackB() - value);
					break;
				case 9:
					if (isCurrSoul) {
						player.setFireDefenceB(player.getFireDefenceB() - value);
					} else {
						player.setFireDefenceX(player.getFireDefenceX() - (int) (value * add));
					}
					soul.setFireDefenceB(soul.getFireDefenceB() - value);
					break;
				case 10:
					if (isCurrSoul) {
						player.setBlizzardDefenceB(player.getBlizzardDefenceB() - value);
					} else {
						player.setBlizzardDefenceX(player.getBlizzardDefenceX() - (int) (value * add));
					}
					soul.setBlizzardDefenceB(soul.getBlizzardDefenceB() - value);
					break;
				case 11:
					if (isCurrSoul) {
						player.setWindDefenceB(player.getWindDefenceB() - value);
					} else {
						player.setWindDefenceX(player.getWindDefenceX() - (int) (value * add));
					}
					soul.setWindDefenceB(soul.getWindDefenceB() - value);
					break;
				case 12:
					if (isCurrSoul) {
						player.setThunderDefenceB(player.getThunderDefenceB() - value);
					} else {
						player.setThunderDefenceX(player.getThunderDefenceX() - (int) (value * add));
					}
					soul.setThunderDefenceB(soul.getThunderDefenceB() - value);
					break;
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
					boolean bln = p.isIsUpOrDown();
					if (bln) {
						switch (i) {
						case 0:
							horse.setMaxHPB(horse.getMaxHPB() + value);
							p.setMaxHPB(p.getMaxHPB() + value);
							break;
						case 1:
							horse.setPhyAttackB(horse.getPhyAttackB() + value);
							p.setPhyAttackB(p.getPhyAttackB() + value);
							break;
						case 2:
							horse.setMagicAttackB(horse.getMagicAttackB() + value);
							p.setMagicAttackB(p.getMagicAttackB() + value);
							break;
						case 3:
							horse.setPhyDefenceB(horse.getPhyDefenceB() + value);
							p.setPhyDefenceB(p.getPhyDefenceB() + value);
							break;
						case 4:
							horse.setMagicDefenceB(horse.getMagicDefenceB() + value);
							p.setMagicDefenceB(p.getMagicDefenceB() + value);
							break;
						case 5:
							horse.setDodgeB(horse.getDodgeB() + value);
							p.setDodgeB(p.getDodgeB() + value);
							break;
						case 6:
							horse.setCriticalDefenceB(horse.getCriticalDefenceB() + value);
							p.setCriticalDefenceB(p.getCriticalDefenceB() + value);
							break;
						case 7:
							horse.setHitB(horse.getHitB() + value);
							p.setHitB(p.getHitB() + value);
							break;
						case 8:
							horse.setCriticalHitB(horse.getCriticalHitB() + value);
							p.setCriticalHitB(p.getCriticalHitB() + value);
							break;
						case 9:
							horse.setAccurateB(horse.getAccurateB() + value);
							p.setAccurateB(p.getAccurateB() + value);
							break;
						case 10:
							horse.setBreakDefenceB(horse.getBreakDefenceB() + value);
							p.setBreakDefenceB(p.getBreakDefenceB() + value);
							break;
						case 11:
							horse.setFireAttackB(horse.getFireAttackB() + value);
							p.setFireAttackB(p.getFireAttackB() + value);
							break;
						case 12:
							horse.setFireDefenceB(horse.getFireDefenceB() + value);
							p.setFireDefenceB(p.getFireDefenceB() + value);
							break;
						case 13:
							horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() + value);
							p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() + value);
							break;
						case 14:
							horse.setBlizzardAttackB(horse.getBlizzardAttackB() + value);
							p.setBlizzardAttackB(p.getBlizzardAttackB() + value);
							break;
						case 15:
							horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() + value);
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() + value);
							break;
						case 16:
							horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() + value);
							p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() + value);
							break;
						case 17:
							horse.setWindAttackB(horse.getWindAttackB() + value);
							p.setWindAttackB(p.getWindAttackB() + value);
							break;
						case 18:
							horse.setWindDefenceB(horse.getWindDefenceB() + value);
							p.setWindDefenceB(p.getWindDefenceB() + value);
							break;
						case 19:
							horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() + value);
							p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() + value);
							break;
						case 20:
							horse.setThunderAttackB(horse.getThunderAttackB() + value);
							p.setThunderAttackB(p.getThunderAttackB() + value);
							break;
						case 21:
							horse.setThunderDefenceB(horse.getThunderDefenceB() + value);
							p.setThunderDefenceB(p.getThunderDefenceB() + value);
							break;
						case 22:
							horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() + value);
							p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() + value);
							break;
						}
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
					boolean bln = p.isIsUpOrDown();
					if (bln) {
						switch (i) {
						case 0:
							horse.setMaxHPB(horse.getMaxHPB() - value);
							p.setMaxHPB(p.getMaxHPB() - value);
							break;
						case 1:
							horse.setPhyAttackB(horse.getPhyAttackB() - value);
							p.setPhyAttackB(p.getPhyAttackB() - value);
							break;
						case 2:
							horse.setMagicAttackB(horse.getMagicAttackB() - value);
							p.setMagicAttackB(p.getMagicAttackB() - value);
							break;
						case 3:
							horse.setPhyDefenceB(horse.getPhyDefenceB() - value);
							p.setPhyDefenceB(p.getPhyDefenceB() - value);
							break;
						case 4:
							horse.setMagicDefenceB(horse.getMagicDefenceB() - value);
							p.setMagicDefenceB(p.getMagicDefenceB() - value);
							break;
						case 5:
							horse.setDodgeB(horse.getDodgeB() - value);
							p.setDodgeB(p.getDodgeB() - value);
							break;
						case 6:
							horse.setCriticalDefenceB(horse.getCriticalDefenceB() - value);
							p.setCriticalDefenceB(p.getCriticalDefenceB() - value);
							break;
						case 7:
							horse.setHitB(horse.getHitB() - value);
							p.setHitB(p.getHitB() - value);
							break;
						case 8:
							horse.setCriticalHitB(horse.getCriticalHitB() - value);
							p.setCriticalHitB(p.getCriticalHitB() - value);
							break;
						case 9:
							horse.setAccurateB(horse.getAccurateB() - value);
							p.setAccurateB(p.getAccurateB() - value);
							break;
						case 10:
							horse.setBreakDefenceB(horse.getBreakDefenceB() - value);
							p.setBreakDefenceB(p.getBreakDefenceB() - value);
							break;
						case 11:
							horse.setFireAttackB(horse.getFireAttackB() - value);
							p.setFireAttackB(p.getFireAttackB() - value);
							break;
						case 12:
							horse.setFireDefenceB(horse.getFireDefenceB() - value);
							p.setFireDefenceB(p.getFireDefenceB() - value);
							break;
						case 13:
							horse.setFireIgnoreDefenceB(horse.getFireIgnoreDefenceB() - value);
							p.setFireIgnoreDefenceB(p.getFireIgnoreDefenceB() - value);
							break;
						case 14:
							horse.setBlizzardAttackB(horse.getBlizzardAttackB() - value);
							p.setBlizzardAttackB(p.getBlizzardAttackB() - value);
							break;
						case 15:
							horse.setBlizzardDefenceB(horse.getBlizzardDefenceB() - value);
							p.setBlizzardDefenceB(p.getBlizzardDefenceB() - value);
							break;
						case 16:
							horse.setBlizzardIgnoreDefenceB(horse.getBlizzardIgnoreDefenceB() - value);
							p.setBlizzardIgnoreDefenceB(p.getBlizzardIgnoreDefenceB() - value);
							break;
						case 17:
							horse.setWindAttackB(horse.getWindAttackB() - value);
							p.setWindAttackB(p.getWindAttackB() - value);
							break;
						case 18:
							horse.setWindDefenceB(horse.getWindDefenceB() - value);
							p.setWindDefenceB(p.getWindDefenceB() - value);
							break;
						case 19:
							horse.setWindIgnoreDefenceB(horse.getWindIgnoreDefenceB() - value);
							p.setWindIgnoreDefenceB(p.getWindIgnoreDefenceB() - value);
							break;
						case 20:
							horse.setThunderAttackB(horse.getThunderAttackB() - value);
							p.setThunderAttackB(p.getThunderAttackB() - value);
							break;
						case 21:
							horse.setThunderDefenceB(horse.getThunderDefenceB() - value);
							p.setThunderDefenceB(p.getThunderDefenceB() - value);
							break;
						case 22:
							horse.setThunderIgnoreDefenceB(horse.getThunderIgnoreDefenceB() - value);
							p.setThunderIgnoreDefenceB(p.getThunderIgnoreDefenceB() - value);
							break;
						}
					}
				}
			}
		}
	}

}
