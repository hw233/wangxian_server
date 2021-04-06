package com.fy.engineserver.calculate;

import java.util.Random;

import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.sprite.Player;

public class NumericalCalculator {

	static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	
	
	public static final int[][] 属性点职业分配方案 = new int[][]{
		//普通,修罗,影魅,仙心,九黎
		 {3, 6, 3,0,0}, //力量
		 {1, 2, 6,2,3}, //敏捷
		 {3, 4, 3,3,5}, //耐力
		 {1, 0, 0,7,4}, //智力
		 
	};
	
	public static final int[][] 属性点职业初始值 = new int[][]{
		//普通,修罗,影魅,仙心,九黎
		 {15, 26, 23,0,0}, //力量
		 {10, 2, 6,2,3}, //敏捷
		 {15,24,23,23,25}, //耐力
		 { 5,0,0, 27,24}, //智力
		 
	};
	public static final int 力量兑换外功 = 0;
	public static final int 灵力兑换内法 = 1;
	public static final int 耐力兑换HP = 2;
	public static final int 身法兑换命中 = 3;
	public static final int 身法兑换闪躲 = 4;
	public static final int 身法兑换会心一击 = 5;
	/**
	 * 1力量=2外功AP
	 * 1灵力=2内法AP
	 * 1耐力=10HP
	 * 1身法=2命中、6闪躲、2会心一击
	 */
	public static final int[] 一级属性点对应换算二级属性值 = new int[]{
		//普通,修罗,影魅,仙心,九黎
		  2,2,10,2,6,2
	};
	
	
	//暴击
	public static final int 暴击率等级差基线 = -3;
	public static final int 暴击率等级差数据表[] = new int[]{1,2,3,4,5,6,10};
	
	//命中
	public static final int 命中率等级差基线 = -12;
	public static final int 命中率等级差数据表[] = new int[]{15,20,30,40,50,65,75,80,83,85,88,89,90,91,93,94};
	
	//闪避
	public static final int 闪避率等级差基线 = -3;
	public static final int 闪避率等级差数据表[] = new int[]{0,1,2,3,4,5,6,7,8,9};
	
	
	public static final int[] 生命值修正参数R = new int[]{10,0,0,3,3};
	public static final int[] 生命值修正参数T = new int[]{2,0,0,1,1};
	
	
	public static final int[] 法力值修正参数R = new int[]{0,20,20,0,15};
	public static final int[] 法力值修正参数T = new int[]{0,2,3,0,1};
	
	public static final int 少林 = 0;
	public static final int 武当 = 1;
	public static final int 峨嵋 = 2;
	public static final int 明教 = 3;
	public static final int 五毒 = 4;
	
	
	public static final int 力量 = 0;
	public static final int 敏捷 = 1;
	public static final int 耐力 = 2;
	public static final int 智力 = 3;
	
	public static final int 生命值上限 = 4;
	public static final int 法力值上限 = 5;
	public static final int 物理攻击上限 = 6;
	public static final int 物理攻击下线 = 7;
	public static final int 物理防御值 = 8;
	public static final int 法术防御值 = 9;
	public static final int 命中值 = 10;
	public static final int 闪避值 = 11;
	public static final int 暴击值 = 12;
	public static final int 韧性值 = 13;
	public static final int 近战攻击强度 = 14;
	public static final int 法术攻击强度 = 15;
	
	public static final int 基础5秒回血 = 16;
	public static final int 基础5秒回蓝 = 17;
	
	public static final int 基础内力值 = 18;
	
	public static final int 杀死怪物经验基线 = -5;
	public static final int 杀死怪物经验参数1[] = new int[]{30,30,30,30,30,20,20,20,20,20,10,10};
	public static final int 杀死怪物经验参数2[] = new int[]{150,140,130,120,110,100,90,80,70,60,50,30};
	
	
	//下标为装备的颜色，对应的是"白","绿","蓝","紫","橙"                                            
	public static final int 装备颜色出售价格参数1[] = new int[]{3,5,8,7,7};
	
	public static final int 装备颜色修理价格参数1[] = new int[]{6,14,17,18,20};
	
	//下标为 布甲，皮甲，板甲，通用材质，武器
	public static final double 装备类型系数[] = new double[]{ 1, 1.5 , 2 , 1 ,3};
	
	//"武器","头盔","护肩","衣服","护手","鞋子","首饰","项链","戒指"
	//此下标顺序必须和EquipmentType定义一直
	public static final double 装备部件系数[] = new double[]{1.5,1,1,1,0.8,1,0.8,0.8,0.8};
	
	//  下标为 等级差/5
	public static final int 怪打人基础减伤比例值[] = new int[]{0,12,24,36,48,60,72,84,96,100};
	
	public static final int 怪打人基础减伤对应的等级差[] = new int[]{0,6,11,16,21,26,31,36,41,46};
	
	
	public static int 计算怪打人基础减伤百分比(int monsterLevel,int playerLevel){
		if(monsterLevel >= playerLevel) return 0;
		int k = (playerLevel - monsterLevel)/5;
		if(k >= 怪打人基础减伤比例值.length) 
			k = 怪打人基础减伤比例值.length - 1;
		return 怪打人基础减伤比例值[k];
	}
	
	public static int 计算装备的出售价格(int equipmentLimitLevel,int colorType,int equipmentType,int materialType){
		double ddd = equipmentLimitLevel * 装备颜色出售价格参数1[colorType] * 装备部件系数[equipmentType];
		if(equipmentType == EquipmentColumn.EQUIPMENT_TYPE_weapon){
			ddd = ddd * 装备类型系数[4];
		}else{
			ddd = ddd * 装备类型系数[materialType];
		}
		return (int)ddd+1;
	}
	
	public static int 计算装备的修理价格(int equipmentLimitLevel,int colorType,int equipmentType,int materialType){
		double ddd = equipmentLimitLevel * 装备颜色修理价格参数1[colorType] * 装备部件系数[equipmentType]/10;
		if(equipmentType == EquipmentColumn.EQUIPMENT_TYPE_weapon){
			ddd = ddd * 装备类型系数[4];
		}else{
			ddd = ddd * 装备类型系数[materialType];
		}
		return (int)ddd+1;
	}
	
	/**
	 * 声望打折后强化装备的价格
	 * 漠不关心：10折
		同道之情：9折
		君子之交：8折
		刎颈之交：7折
		生死至交：5折
	 * @param player
	 * @param price
	 * @return
	 */
	public static int 声望打折后强化装备的价格(Player player, int price){
		
		return 0;
	}
	
	public static int 计算杀死怪物得到的经验值(int playerLevel,int monsterLevel,int teamMemberNum,int teamMemberLevel,boolean societyFlag){
		int 每日任务活动时间规划 = 6;
		int 每日杀怪时间规划 = 18;
		int 击杀怪物所需时间 = 5;
		int 同时击杀数量 = 7;
		double 本原升级时间 = 1d*monsterLevel*monsterLevel*monsterLevel*600/(180*180*180) - 1d*(monsterLevel - 1)*(monsterLevel - 1)*(monsterLevel - 1)*600/(180*180*180);
		double 秒产出经验公式 = (1d*monsterLevel*monsterLevel*monsterLevel*monsterLevel+1d*monsterLevel)/(1d*本原升级时间*3600);
		int exp = (int)((每日任务活动时间规划*1d*3600*秒产出经验公式*击杀怪物所需时间/(1d*每日杀怪时间规划*3600*同时击杀数量)+1)/2);
		return exp/teamMemberNum ;
	}
	
	public static int 计算杀死怪物经验值(int playerLevel,int monsterLevel,int teamMemberNum,int teamMemberLevel,boolean societyFlag){
		int a = (playerLevel - monsterLevel) - 杀死怪物经验基线;
		if(a < 0) a = 0;
		if(a >= 杀死怪物经验参数2.length) a = 杀死怪物经验参数2.length -1;
		int b = (45 + monsterLevel * 12 + 杀死怪物经验参数1[a]) * 杀死怪物经验参数2[a]/100;
		if(!societyFlag)
			return b * playerLevel/teamMemberLevel + b * (teamMemberNum - 1) * 10/100;
		else{
			int 每日任务活动时间规划 = 6;
			int 每日杀怪时间规划 = 18;
			int 击杀怪物所需时间 = 5;
			int 同时击杀数量 = 7;
			long 本原升级时间 = monsterLevel*monsterLevel*monsterLevel*600/(180*180*180) - (monsterLevel - 1)*600/(180*180*180);
			double 秒产出经验公式 = ExperienceManager.maxExpOfLevel[playerLevel]*1d/(本原升级时间*3600);
			int exp = (int)((每日任务活动时间规划*1d*3600*秒产出经验公式*击杀怪物所需时间/(每日杀怪时间规划*3600*同时击杀数量)+1)/2);
			return exp/teamMemberLevel ;
		}
	}
	
	public static int 计算杀死精英怪物经验值(int playerLevel,int monsterLevel,int teamMemberNum,int teamMemberLevel,boolean societyFlag){
		int a = (playerLevel - monsterLevel) - 杀死怪物经验基线;
		if(a < 0) a = 0;
		if(a >= 杀死怪物经验参数2.length) a = 杀死怪物经验参数2.length -1;
		int b = (135 + monsterLevel * 36 + 杀死怪物经验参数1[a]) * 杀死怪物经验参数2[a]/100;
		if(!societyFlag)
			return b * playerLevel/teamMemberLevel + b * (teamMemberNum - 1) * 10/100;
		else
			return b * playerLevel/teamMemberLevel ;
	}
	
	public static int 计算杀死BOSS怪物经验值(int playerLevel,int monsterLevel,int teamMemberNum,int teamMemberLevel){
		int a = (playerLevel - monsterLevel) - 杀死怪物经验基线;
		if(a < 0) a = 0;
		if(a >= 杀死怪物经验参数2.length) a = 杀死怪物经验参数2.length -1;
		int b = (1125 + monsterLevel * 125 + 杀死怪物经验参数1[a]) * 杀死怪物经验参数2[a]/100;
		return  b * playerLevel/teamMemberLevel + b * (teamMemberNum - 1) * 10/100;
	}
	
	public static int 计算杀死副本精英怪物经验值(int playerLevel,int monsterLevel,int teamMemberNum,int teamMemberLevel){
		int a = (playerLevel - monsterLevel) - 杀死怪物经验基线;
		if(a < 0) a = 0;
		if(a >= 杀死怪物经验参数2.length) a = 杀死怪物经验参数2.length -1;
		int b = (225 + monsterLevel * 25 + 杀死怪物经验参数1[a]) * 杀死怪物经验参数2[a]/100;
		return b * playerLevel/teamMemberLevel + b * (teamMemberNum - 1) * 10/100;
	}
	
	public static int 计算杀死怪物掉落的金币(int monsterLevel){
		double ddd = (monsterLevel * 1);
		ddd = ddd * ddd * ddd;
		ddd = Math.sqrt(ddd) + 15;
		
		int money = (int)(ddd * (0.75 +random.nextDouble() * 0.25)) + 1;
		
		return money;
	}
	
	public static int 计算杀死副本精英掉落的金币(int monsterLevel){
		double ddd = (monsterLevel * 1.3);
		ddd = ddd * ddd * ddd;
		ddd = Math.sqrt(ddd) + 1;
		
		int money = (int)((ddd * (0.75 +random.nextDouble() * 0.25)) + 1) * 2;
		
		return money;
	}
	
	public static int 计算杀死BOSS怪物掉落的金币(int monsterLevel){
		double ddd = (monsterLevel * 1.3);
		ddd = ddd * ddd * ddd;
		ddd = Math.sqrt(ddd) + 1;
		
		int money = (int)((ddd * (0.75 +random.nextDouble() * 0.25)) + 1) * 9;
		
		return money;
	}
	
	public static int 计算任务奖励的经验值(int taskLevel,int taskType){
		
		if(taskLevel <= 5){
			return (45 + taskLevel * 5) * (10 + 2 * taskLevel);
		}else if(taskLevel <= 10){
			return (45 + taskLevel * 5) * (30 + 2 * taskLevel);
		}else if(taskLevel <= 15){
			return (45 + taskLevel * 5) * (50 + 2 * taskLevel);
		}else{
			return (45 + taskLevel * 5) * 100;
		}
			

	}
	
	/**
	 * 计算一级属性
	 * @param career
	 * @param level
	 * @param propertyIndex
	 * @return
	 */
	public static int 根据等级计算一级属性基本值(int career,int level,int propertyIndex){
		return 属性点职业初始值[propertyIndex][career] + 属性点职业分配方案[propertyIndex][career] * (level-1);
	}

	/**
	 * 计算一级属性
	 * @param career
	 * @param level
	 * @param propertyIndex
	 * @return
	 */
	public static int 根据一级属性计算二级属性基本值(int value,int propertyIndex){
		return value*一级属性点对应换算二级属性值[propertyIndex];
	}
	
	/**
	 * 计算一级属性
	 * @param career
	 * @param level
	 * @param propertyIndex
	 * @return
	 */
//	public static int 根据一级属性计算二级属性基本值(
//			int strength,int spellpower,int constitution,int dexterity,
//			int career,int level,int propertyIndex){
//		switch(propertyIndex){
//		case 生命值上限:
//			if(level <= 29){
//				return constitution * 10 + 生命值修正参数R[career] + 生命值修正参数T[career] * level;
//			}else if(level <= 40){
//				return constitution * 10 + 生命值修正参数R[career] + (生命值修正参数T[career]+1) * level;
//			}else{
//				return constitution * 10 + 生命值修正参数R[career] + (生命值修正参数T[career]+2) * level;
//			}
//		case 法力值上限:
//			//if(career == 法师 || career == 牧师 || career == 术士){
//				if(level <= 29){
//					return spellpower * 10 + 法力值修正参数R[career] + 法力值修正参数T[career] * level;
//				}else if(level <= 40){
//					return spellpower * 10 + 法力值修正参数R[career] + (法力值修正参数T[career]+1) * level;
//				}else{
//					return spellpower * 10 + 法力值修正参数R[career] + (法力值修正参数T[career]+2) * level;
//				}
//			//}else if(career == 战士){
//			//	return 100;
//			//}else {
//			//	return 150;
//			//}
//		case 基础内力值:
//			if(level <= 29){
//				return spellpower * 10 + 法力值修正参数R[career] + 法力值修正参数T[career] * level;
//			}else if(level <= 40){
//				return spellpower * 10 + 法力值修正参数R[career] + (法力值修正参数T[career]+1) * level;
//			}else{
//				return spellpower * 10 + 法力值修正参数R[career] + (法力值修正参数T[career]+2) * level;
//			}
//		case 近战攻击强度:
//			if(career == 少林){
//				return  (level * 2 + strength * 2 - 9);
//			}else if(career == 明教){
//				return  (level * 2 + dexterity * 2  + strength - 9);
//			}else if(career == 五毒){
//				return  (level * 2 + strength * 1 + spellpower * 1 + dexterity );
//			}else{
//				return  (level * 2 + strength * 1);
//			}
//		case 物理防御值:
//			return 10 + (level -1) * 15;
//		case 法术攻击强度:
//			if(career == 少林){
//				return 0;
//			}else if(career == 明教){
//				return 0;
//			}else if(career == 五毒){
//				return  0;
//			}else if(career == 武当){
//				return  (int)(level * 2 + spellpower * 1.4 );
//			}else{
//				return  (int)(level * 2 + spellpower * 1.4 );
//			}
//		case 法术防御值:
//			return 0;
//		case 命中值:
//			return 0;
//		case 闪避值:
//			return dexterity/4;
//		case 暴击值:
//			if(career == 少林){
//				return dexterity/4;
//			}else if(career == 明教){
//				return dexterity/4;
//			}else if(career == 五毒){
//				return dexterity/4;
//			}else if(career == 武当){
//				return  0;
//			}else{
//				return  0;
//			}
//		case 韧性值:
//			return 0;
//		case 基础5秒回血:
//			return level/15+1;
//		case 基础5秒回蓝:
//			return level/10+1;
//		default:
//		}
//		return 0;
//
//	}
}
