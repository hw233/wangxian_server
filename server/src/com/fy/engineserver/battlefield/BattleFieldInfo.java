package com.fy.engineserver.battlefield;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 战场配置信息
 *
 */
public class BattleFieldInfo {

	public static final String BATTLE_CATEGORY_阿拉希 = Translate.text_1743;
	
	public static final String BATTLE_CATEGORY_5v5互杀 = Translate.text_1744;
	
	public static final String BATTLE_CATEGORY_10v10互杀 = Translate.text_1745;
	
	public static final String BATTLE_CATEGORY_大逃杀 = Translate.text_1746;
	
	public static final String BATTLE_CATEGORY_DOTA = "DOTA";
	
	public static final String BATTLE_CATEGORY_互杀帮战 = Translate.text_1747;
	
	public static final String BATTLE_CATEGORY_仙武大会1 = Translate.text_1748;
	
	public static final String BATTLE_CATEGORY_仙武大会2 = Translate.text_1749;
	
	public static final String BATTLE_CATEGORY_仙武大会3 = Translate.text_1750;
	
	public static final String BATTLE_CATEGORY_仙武大会4 = Translate.text_1751;
	
	public static final String BATTLE_CATEGORY_仙武大会5 = Translate.text_1752;
	
	public static final String BATTLE_CATEGORY_仙武大会6 = Translate.text_1753;
	
	public static final String[] 仙武大会战场数组 = new String[]{Translate.text_1748,Translate.text_1749,Translate.text_1750,Translate.text_1751,Translate.text_1752,Translate.text_1753};
	public static final String[] 仙武大会战场地图数组 = new String[]{Translate.仙武大会1,Translate.仙武大会2,Translate.仙武大会3,Translate.仙武大会4,Translate.仙武大会5,Translate.仙武大会6};
	public static final int[][] 仙武大会战场出生点数组 = new int[][]{{3093,934,842,2164},{3093,934,842,2164},{3093,934,842,2164},{3093,934,842,2164},{3093,934,842,2164},{3093,934,842,2164}};
	public static final int[][] 仙武大会战场战斗点数组 = new int[][]{{2571,1423,1368,1423},{2571,1423,1368,1423},{2571,1423,1368,1423},{2571,1423,1368,1423},{2571,1423,1368,1423},{2571,1423,1368,1423}};
	/**
	 * 战场的类型
	 */
	protected String battleCategory;
	
	
	//战场的名字
	protected String name;
	
	//战场对应的地图名字
	protected String mapName;
	
	//战场最大持续的时间
	protected long maxLifeTime;
	
	//战场开启后，多长时间内进入战斗模式
	protected long startFightingTime;
	
	//玩家进入的最小等级
	protected int minPlayerLevel;
	
	//玩家进入的最大等级
	protected int maxPlayerLevel;

	//战场的战斗模式
	protected int battleFightingType;

	//一方至少有多少人
	protected int minPlayerNumForStartOnOneSide;
	
	//当战场的某一方少于最少人数时，战场多长时间后关闭
	protected long lastingTimeForNotEnoughPlayers;

	protected int maxPlayerNumOnOneSide;

	//战场奖励系数
	protected int battleRewardParam = 80;
	
	//节日奖励系数
	protected int holidyRewardParam = 1;
	
	protected String description = "";
	
	protected String forbidBuffName = "";
	
	//是否为帮战
	protected boolean bangpaiFlag = false;
	
	//帮战的A方
	protected String gangNameForA ;
	
	//帮战的B方
	protected String gangNameForB;
	
	public String getForbidBuffName() {
		return forbidBuffName;
	}

	public void setForbidBuffName(String forbidBuffName) {
		this.forbidBuffName = forbidBuffName;
	}

	public String getBattleCategory() {
		return battleCategory;
	}

	public void setBattleCategory(String battleCategory) {
		this.battleCategory = battleCategory;
	}

	public int getBattleRewardParam() {
		return battleRewardParam;
	}

	public void setBattleRewardParam(int battleRewardParam) {
		this.battleRewardParam = battleRewardParam;
	}

	public int getHolidyRewardParam() {
		return holidyRewardParam;
	}

	public void setHolidyRewardParam(int holidyRewardParam) {
		this.holidyRewardParam = holidyRewardParam;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public long getMaxLifeTime() {
		return maxLifeTime;
	}

	public void setMaxLifeTime(long maxLifeTime) {
		this.maxLifeTime = maxLifeTime;
	}

	public long getStartFightingTime() {
		return startFightingTime;
	}

	public void setStartFightingTime(long startFightingTime) {
		this.startFightingTime = startFightingTime;
	}

	public int getMinPlayerLevel() {
		return minPlayerLevel;
	}

	public void setMinPlayerLevel(int minPlayerLevel) {
		this.minPlayerLevel = minPlayerLevel;
	}

	public int getMaxPlayerLevel() {
		return maxPlayerLevel;
	}

	public void setMaxPlayerLevel(int maxPlayerLevel) {
		this.maxPlayerLevel = maxPlayerLevel;
	}

	public int getBattleFightingType() {
		return battleFightingType;
	}

	public void setBattleFightingType(int battleFightingType) {
		this.battleFightingType = battleFightingType;
	}

	public int getMinPlayerNumForStartOnOneSide() {
		return minPlayerNumForStartOnOneSide;
	}

	public void setMinPlayerNumForStartOnOneSide(int minPlayerNumForStartOnOneSide) {
		this.minPlayerNumForStartOnOneSide = minPlayerNumForStartOnOneSide;
	}

	public long getLastingTimeForNotEnoughPlayers() {
		return lastingTimeForNotEnoughPlayers;
	}

	public void setLastingTimeForNotEnoughPlayers(
			long lastingTimeForNotEnoughPlayers) {
		this.lastingTimeForNotEnoughPlayers = lastingTimeForNotEnoughPlayers;
	}

	public int getMaxPlayerNumOnOneSide() {
		return maxPlayerNumOnOneSide;
	}

	public void setMaxPlayerNumOnOneSide(int maxPlayerNumOnOneSide) {
		this.maxPlayerNumOnOneSide = maxPlayerNumOnOneSide;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isBangpaiFlag() {
		return bangpaiFlag;
	}

	public void setBangpaiFlag(boolean bangpaiFlag) {
		this.bangpaiFlag = bangpaiFlag;
	}

	public String getGangNameForA() {
		return gangNameForA;
	}

	public void setGangNameForA(String gangNameForA) {
		this.gangNameForA = gangNameForA;
	}

	public String getGangNameForB() {
		return gangNameForB;
	}

	public void setGangNameForB(String gangNameForB) {
		this.gangNameForB = gangNameForB;
	}


	
	
	
	
}
