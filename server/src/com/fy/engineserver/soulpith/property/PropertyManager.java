package com.fy.engineserver.soulpith.property;

import org.slf4j.Logger;

import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;

public class PropertyManager {
	
	private static PropertyManager inst = new PropertyManager();
	
	public static PropertyManager getInst() {
		return inst;
	}
	/**
	 * 增加属性
	 * @param addTypes
	 * @param propertys
	 * @param propertyNums
	 */
	public void addAttrs(Player player, PropertyModule module, Logger logger) {
		if (module.getAddTypes().size() != module.getPropertys().size() || module.getPropertys().size() != module.getPropertyNums().size()) {
			try {
				throw new Exception();
			} catch (Exception e) {
				logger.warn("[增加属性] [失败] [参数异常] [" + player.getLogString() + "] [" + module + "]", e);
			}
			return ;
		}
		Soul soul = player.getCurrSoul();
		double addRate = soul.getAddPercent() + player.getUpgradeNums() * 0.01;
		for (int i=0; i<module.getAddTypes().size(); i++) {
			AddPropertyTypes addType = module.getAddTypes().get(i);
			int addNum = module.getPropertyNums().get(i);
			switch (module.getPropertys().get(i)) {
			case HP:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMaxHPB(player.getMaxHPB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMaxHPC(player.getMaxHPC() + ((float)addNum/100f) );
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMaxHPX(player.getMaxHPX() + (int)(addNum * addRate));
				}
				break;
			case PHYATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setPhyAttackB(player.getPhyAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setPhyAttackC(player.getPhyAttackC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setPhyAttackX(player.getPhyAttackX() + (int)(addNum * addRate));
				}
				break;
			case PHYDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setPhyDefenceB(player.getPhyDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setPhyDefenceC(player.getPhyDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setPhyDefenceX(player.getPhyDefenceX() + (int)(addNum * addRate));
				}
				break;
			case BREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBreakDefenceB(player.getBreakDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBreakDefenceC(player.getBreakDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBreakDefenceX(player.getBreakDefenceX() + (int)(addNum * addRate));
				}
				break;
			case DODGE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setDodgeB(player.getDodgeB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setDodgeC(player.getDodgeC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setDodgeX(player.getDodgeX() + (int)(addNum * addRate));
				}
				break;
			case CIRT:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setCriticalHitB(player.getCriticalHitB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setCriticalHitC(player.getCriticalHitC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setCriticalHitX(player.getCriticalHitX() + (int)(addNum * addRate));
				}
				break;
			case MP:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMaxMPB(player.getMaxMPB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMaxMPC((int) (player.getMaxMPC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMaxMPX(player.getMaxMPX() + (int)(addNum * addRate));
				}
				break;
			case MAGICATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMagicAttackB(player.getMagicAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMagicAttackC(player.getMagicAttackC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMagicAttackX(player.getMagicAttackX() + (int)(addNum * addRate));
				}
				break;
			case MAGICDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMagicDefenceB(player.getMagicDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMagicDefenceC(player.getMagicDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMagicDefenceX(player.getMagicDefenceX() + (int)(addNum * addRate));
				}
				break;
			case HIT:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setHitB(player.getHitB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setHitC(player.getHitC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setHitX(player.getHitX() + (int)(addNum * addRate));
				}
				break;
			case ACCURATE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setAccurateB(player.getAccurateB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setAccurateC(player.getAccurateC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setAccurateX(player.getAccurateX() + (int)(addNum * addRate));
				}
				break;
			case CRITICALDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setCriticalDefenceB(player.getCriticalDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setCriticalDefenceC(player.getCriticalDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setCriticalDefenceX(player.getCriticalDefenceX() + (int)(addNum * addRate));
				}
				break;
			case FIREATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setFireAttackB(player.getFireAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setFireAttackC((int) (player.getFireAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setFireAttackX(player.getFireAttackX() + (int)(addNum * addRate));
				}
				break;
			case FIREDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setFireDefenceB(player.getFireDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setFireDefenceC((int) (player.getFireDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setFireDefenceX(player.getFireDefenceX() + (int)(addNum * addRate));
				}
				break;
			case FIREBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setFireIgnoreDefenceC((int) (player.getFireIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;
			case WINDATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setWindAttackB(player.getWindAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setWindAttackC((int) (player.getWindAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setWindAttackX(player.getWindAttackX() + (int)(addNum * addRate));
				}
				break;
			case WINDDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setWindDefenceB(player.getWindDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setWindDefenceC((int) (player.getWindDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setWindDefenceX(player.getWindDefenceX() + (int)(addNum * addRate));
				}
				break;
			case WINDBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setWindIgnoreDefenceC((int) (player.getWindIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;
			case BLIZZATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBlizzardAttackB(player.getBlizzardAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBlizzardAttackC((int) (player.getBlizzardAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBlizzardAttackX(player.getBlizzardAttackX() + (int)(addNum * addRate));
				}
				break;
			case BLIZZDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBlizzardDefenceB(player.getBlizzardDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBlizzardDefenceC((int) (player.getBlizzardDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBlizzardDefenceX(player.getBlizzardDefenceX() + (int)(addNum * addRate));
				}
				break;
			case BLIZZBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBlizzardIgnoreDefenceC((int) (player.getBlizzardIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;
			case THUNDATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setThunderAttackB(player.getThunderAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setThunderAttackC((int) (player.getThunderAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setThunderAttackX(player.getThunderAttackX() + (int)(addNum * addRate));
				}
				break;
			case THUNDDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setThunderDefenceB(player.getThunderDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setThunderDefenceC((int) (player.getThunderDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setThunderDefenceX(player.getThunderDefenceX() + (int)(addNum * addRate));
				}
				break;
			case THUNDERBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setThunderIgnoreDefenceC((int) (player.getThunderIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;

			}
		}
	}
	/**
	 * 减属性
	 * @param player
	 * @param module
	 * @param logger
	 */
	public void deductAttrs(Player player, PropertyModule module, Logger logger) {
		if (module.getAddTypes().size() != module.getPropertys().size() || module.getPropertys().size() != module.getPropertyNums().size()) {
			try {
				throw new Exception();
			} catch (Exception e) {
				logger.warn("[减少属性] [失败] [参数异常] [" + player.getLogString() + "] [" + module + "]", e);
			}
			return ;
		}
		Soul soul = player.getCurrSoul();
		double addRate = soul.getAddPercent() + player.getUpgradeNums() * 0.01;
		for (int i=0; i<module.getAddTypes().size(); i++) {
			AddPropertyTypes addType = module.getAddTypes().get(i);
			int addNum = -module.getPropertyNums().get(i);
			switch (module.getPropertys().get(i)) {
			case HP:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMaxHPB(player.getMaxHPB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMaxHPC(player.getMaxHPC() + ((float)addNum/100f) );
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMaxHPX(player.getMaxHPX() + (int)(addNum * addRate));
				}
				break;
			case PHYATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setPhyAttackB(player.getPhyAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setPhyAttackC(player.getPhyAttackC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setPhyAttackX(player.getPhyAttackX() + (int)(addNum * addRate));
				}
				break;
			case PHYDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setPhyDefenceB(player.getPhyDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setPhyDefenceC(player.getPhyDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setPhyDefenceX(player.getPhyDefenceX() + (int)(addNum * addRate));
				}
				break;
			case BREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBreakDefenceB(player.getBreakDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBreakDefenceC(player.getBreakDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBreakDefenceX(player.getBreakDefenceX() + (int)(addNum * addRate));
				}
				break;
			case DODGE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setDodgeB(player.getDodgeB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setDodgeC(player.getDodgeC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setDodgeX(player.getDodgeX() + (int)(addNum * addRate));
				}
				break;
			case CIRT:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setCriticalHitB(player.getCriticalHitB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setCriticalHitC(player.getCriticalHitC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setCriticalHitX(player.getCriticalHitX() + (int)(addNum * addRate));
				}
				break;
			case MP:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMaxMPB(player.getMaxMPB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMaxMPC((int) (player.getMaxMPC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMaxMPX(player.getMaxMPX() + (int)(addNum * addRate));
				}
				break;
			case MAGICATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMagicAttackB(player.getMagicAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMagicAttackC(player.getMagicAttackC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMagicAttackX(player.getMagicAttackX() + (int)(addNum * addRate));
				}
				break;
			case MAGICDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setMagicDefenceB(player.getMagicDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setMagicDefenceC(player.getMagicDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setMagicDefenceX(player.getMagicDefenceX() + (int)(addNum * addRate));
				}
				break;
			case HIT:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setHitB(player.getHitB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setHitC(player.getHitC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setHitX(player.getHitX() + (int)(addNum * addRate));
				}
				break;
			case ACCURATE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setAccurateB(player.getAccurateB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setAccurateC(player.getAccurateC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setAccurateX(player.getAccurateX() + (int)(addNum * addRate));
				}
				break;
			case CRITICALDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setCriticalDefenceB(player.getCriticalDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setCriticalDefenceC(player.getCriticalDefenceC() + ((float)addNum/100f));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setCriticalDefenceX(player.getCriticalDefenceX() + (int)(addNum * addRate));
				}
				break;
			case FIREATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setFireAttackB(player.getFireAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setFireAttackC((int) (player.getFireAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setFireAttackX(player.getFireAttackX() + (int)(addNum * addRate));
				}
				break;
			case FIREDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setFireDefenceB(player.getFireDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setFireDefenceC((int) (player.getFireDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setFireDefenceX(player.getFireDefenceX() + (int)(addNum * addRate));
				}
				break;
			case FIREBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setFireIgnoreDefenceB(player.getFireIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setFireIgnoreDefenceC((int) (player.getFireIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setFireIgnoreDefenceX(player.getFireIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;
			case WINDATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setWindAttackB(player.getWindAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setWindAttackC((int) (player.getWindAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setWindAttackX(player.getWindAttackX() + (int)(addNum * addRate));
				}
				break;
			case WINDDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setWindDefenceB(player.getWindDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setWindDefenceC((int) (player.getWindDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setWindDefenceX(player.getWindDefenceX() + (int)(addNum * addRate));
				}
				break;
			case WINDBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setWindIgnoreDefenceB(player.getWindIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setWindIgnoreDefenceC((int) (player.getWindIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setWindIgnoreDefenceX(player.getWindIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;
			case BLIZZATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBlizzardAttackB(player.getBlizzardAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBlizzardAttackC((int) (player.getBlizzardAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBlizzardAttackX(player.getBlizzardAttackX() + (int)(addNum * addRate));
				}
				break;
			case BLIZZDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBlizzardDefenceB(player.getBlizzardDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBlizzardDefenceC((int) (player.getBlizzardDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBlizzardDefenceX(player.getBlizzardDefenceX() + (int)(addNum * addRate));
				}
				break;
			case BLIZZBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setBlizzardIgnoreDefenceB(player.getBlizzardIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setBlizzardIgnoreDefenceC((int) (player.getBlizzardIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setBlizzardIgnoreDefenceX(player.getBlizzardIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;
			case THUNDATTACK:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setThunderAttackB(player.getThunderAttackB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setThunderAttackC((int) (player.getThunderAttackC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setThunderAttackX(player.getThunderAttackX() + (int)(addNum * addRate));
				}
				break;
			case THUNDDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setThunderDefenceB(player.getThunderDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setThunderDefenceC((int) (player.getThunderDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setThunderDefenceX(player.getThunderDefenceX() + (int)(addNum * addRate));
				}
				break;
			case THUNDERBREAKDEFANCE:
				if (addType == AddPropertyTypes.ADD_B_NUM) {
					player.setThunderIgnoreDefenceB(player.getThunderIgnoreDefenceB() + addNum);
				} else if (addType == AddPropertyTypes.ADD_C_NUM) {
					player.setThunderIgnoreDefenceC((int) (player.getThunderIgnoreDefenceC() + ((float)addNum/100f)));
				} else if (addType == AddPropertyTypes.ADD_X_NUM) {
					player.setThunderIgnoreDefenceX(player.getThunderIgnoreDefenceX() + (int)(addNum * addRate));
				}
				break;

			}
		}
	}
}
