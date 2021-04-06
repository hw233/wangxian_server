package com.fy.engineserver.activity.disaster;

/**
 * 金猴天灾常量
 */
public class DisasterConstant {
	public static int MAX_NUM = 5; // 最多能有几个玩家同在一个地图
	public static int MIN_NUM = 2; // 最少几个玩家同在一个地图
	/** 出生血量 */
	public static final int BASEHP = 100;
	/** 火圈伤害 */
//	public static final int FIRE_DAMAGE = 10;
	/** 系统自动匹配时间间隔 */
	public static long matchCD = 90000;
	/** 活动开启前多少毫秒通知玩家 */
	public static long[] prenoticeTime = new long[] { 600000, 300000, 60000 };
	/** 不允许进入金猴天灾活动的地图 */
	public static String[] unavaibleMaps = new String[] { "jianyu", "jiuzhongyunxiao", "lunhuiyiji" };
	/** 活动结束后等待传送时间 */
	public static final long END_WAIT_TIME = 20000;
	/** 只有一个人进入游戏时，死亡多少次会得不到物品奖励且只能得到一半经验奖励 */
	public static int ONLY_ONE_DEADTIMES = 20;
	/** 多人参加时，死亡多少次无法得到物品奖励 */
	public static int MULITY_DEADTIMES = 30;
	/** 每次活动开启玩家可进入次数 */
	public static int MAX_ENTER_TIMES = 2;
	/** 玩家死亡后自动复活时间 */
	public static long RELIVE_INTEVAL = 2800;
	/** 刷新宝箱时间间隔 */
	public static long REFRESH_BOX_INTEVAL = 20000;
	/** 每次刷新宝箱个数 */
	public static int REFRESH_BOX_NUM = 2;
	/** 宝箱NPCID */
	public static int BOX_NPCID = 100000;
	/** 宝箱消失时间 */
	public static int BOX_CLEAN_TIME = 15000;
	/** 宝箱内道具 */
	public static String BOX_ARTICLE_CNNNAME = "经验灵矿";
	
	public static int viewWith = 2000;
	public static int viewHeight = 2000;
	/** 角色多久不动身边刷boss */
	public static long refreshBossTime = 5000;
	public static int TEMP_MONSTER_ID2 = 20113397;
	
	public static String BOX_AVATA = "CJkuangshi";
	
	public static int TEMP_MONSTER_ID = 20113395;
	public static int TEMP_NPC_ID = 19860001;
	public static int TEMP_NPC_ID2 = 19860002;
	public static int[] TEMP_NPC_PINTS = new int[]{1134,830};
	/** 采集距离 */
	public static int availiableDistance = 120;

	public static void main(String[] args) {
	}
}
