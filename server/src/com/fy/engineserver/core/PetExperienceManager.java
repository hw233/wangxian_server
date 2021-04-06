package com.fy.engineserver.core;

import com.fy.engineserver.datasource.language.Translate;


/**
 * 经验值管理
 * 
 *
 */
public class PetExperienceManager {

	// 打怪获得经验值
	public static final int ADDEXP_REASON_FIGHTING = 0;
	// 购买经验值
	public static final int ADDEXP_REASON_BUYING = 1;
	// 其他
	public static final int ADDEXP_REASON_TASK = 2;

	public static final int ADDEXP_REASON_BATTLE = 3;

	public static final int ADDEXP_REASON_OTHER = 4;
	
	public static final int ADDEXP_REASON_FINISH_APPRENTICESHIP = 5;
	
	public static final int ADDEXP_REASON_QUIZ = 6;
	
	public static final int ADDEXP_REASON_MONEYEXPHONOR_PROPS = 7;
	
	public static final int ADDEXP_REASON_FIRE_NPC = 8;
	
	public static final int ADDEXP_REASON_RETURN_PLAYER = 9;
	
	public static final int ADDEXP_REASON_UPGRADE_PROPS = 10;
	
	public static final int ADDEXP_REASON_SINGLE_PROPS = 11;

	public static final int ADDEXP_REASON_HOOK = 12;
	
	public static final String EXP_REASON_NAMES[] = new String[] { Translate.text_5772, Translate.text_5773, Translate.text_5774, Translate.text_5775,Translate.text_5776,Translate.text_3486,Translate.text_5777,Translate.text_5778,Translate.text_5779,Translate.text_5780,Translate.text_5781,Translate.宠物经验丹 ,Translate.玄兽挂机};
	
	static PetExperienceManager self;
	
	public static PetExperienceManager getInstance(){
		if(self != null) return self;
		synchronized(PetExperienceManager.class){
			if(self != null) return self;
			self = new PetExperienceManager();
			return self;
		}
	}
	
	/**
	 * 最大级别
	 */
	public static int maxLevel = 60;
	public static int feishenMaxLv = 220;
	
	int gangMaxLevel=5;
	
//	public int magicWeaponMaxLevel = 12;
	
	/**
	 * 每一个级别需要的经验值
	 */
	public long maxExpOfLevel[] = null;
	
	/**
	 * 法宝每一个级别需要的经验值
	 */
//	public int maxExpOfMagicWeaponLevel[] = null;
	
	long[] gangExpPerLevel;
	
	public PetExperienceManager(){
			maxLevel = 300;
			maxExpOfLevel = new long[maxLevel+1];
			
			maxExpOfLevel[0] = 1;
			
			for (int i = 1; i <= maxLevel; i++) {
				maxExpOfLevel[i] = (long)((long)(i+1)*(i+1)*(i+1)*(i+1)*0.3);
			}
	}
	
	/**
	 * 计算根据当前级别，当前的经验值，获得的经验值，
	 * 判断是否要升级
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */
	public boolean isUpgradeLevel(int currentLevel,long currentExp,long additionalExp,boolean ownerIsFeishen){
		if(currentLevel >= maxLevel) return false;
		if (!ownerIsFeishen && currentLevel >= feishenMaxLv) {
			return false;
		}
		if( currentExp + additionalExp >= maxExpOfLevel[currentLevel]){
			return true;
		}
		return false;
	}
	
	/**
	 * 计算升级后的级别
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */
	public int calculateLevel(int currentLevel,long currentExp,long additionalExp, boolean ownerIsFeishen){
		if(currentLevel >= maxLevel) return currentLevel;
		long exp = currentExp + additionalExp;
		while( exp >= maxExpOfLevel[currentLevel]){
			exp -= maxExpOfLevel[currentLevel];
			currentLevel++;
			if(currentLevel >= maxLevel) break;
			if (!ownerIsFeishen && currentLevel >= feishenMaxLv) {
				break;
			}
		}
		return currentLevel;
	}
	
	/**
	 * 计算升级后剩余的经验值
	 * @param currentLevel
	 * @param currentExp
	 * @param additionalExp
	 * @return
	 */	
	public long calculateLeftExp(int currentLevel,long currentExp,long additionalExp, boolean ownerIsFeishen){
		if(currentLevel >= maxLevel) return 0;
		long exp = currentExp + additionalExp;
		while( exp >= maxExpOfLevel[currentLevel]){
			exp -= maxExpOfLevel[currentLevel];
			currentLevel++;
			if(currentLevel >= maxLevel) return 0;
			if (!ownerIsFeishen && currentLevel >= feishenMaxLv) {
				break;
			}
		}
		return exp;
	}
	
}
