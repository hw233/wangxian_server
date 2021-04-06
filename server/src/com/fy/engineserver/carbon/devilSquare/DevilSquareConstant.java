package com.fy.engineserver.carbon.devilSquare;

import com.fy.engineserver.datasource.language.Translate;

public class DevilSquareConstant {
	
	public static boolean 是否开启恶魔城堡怪物积分限制 = true;
	
	//副本状态
	public static byte UNSTART = 0;		//未开启
	public static byte PREPARE = 1;		//准备阶段，可以进入副本
	public static byte START = 2;		//已经开启状态，刷怪
	public static byte FINISH = 3;		//已经结束
	//副本开启时间规则
	public static final int DAILY_HOUR = 1;	//每天固定点
	//副本心跳
	public static int DORMANT = 5000;		//副本未开启心跳速度
	public static int NOMAL = 100;			//副本已开启心跳速度
	//下线多久后踢出副本
	public static long kick4NotOnlineTime = 10 * 60 * 1000;
	//各种倒计时    类型，0:入场倒计时  1:准备倒计时   2:刷怪倒计时   3:清怪倒计时 4:刷boss倒计时  5:退出副本倒计时   100:副本整体倒计时
	public static int count_enterCarbon = 0;
	public static int count_ready = 1;
	public static int count_refreashMonster = 2;
	public static int count_cleanMonster = 3;
	public static int count_refreashBoss = 4;
	public static int count_exitCarbon = 5;
	public static int count_reftreashBox = 6;
	public static int count_allTime = 100;
	/** 宝箱id */
	public static int boxCateId = 100000;
	public static String[] boxarticlename = new String[]{Translate.乾坤异宝, Translate.阴阳异宝, Translate.混元异宝, Translate.混沌异宝};
	/** 杀怪积分奖励的物品名 */
	public static String[] scoreArticlename = new String[]{Translate.后天乾坤令, Translate.先天阴阳令, Translate.太极混元令, Translate.无极混沌令};
	/** 杀怪积分兑换物品比率 */
	public static double scoreRate = 0.1;
	public static int boxlasttime = 60;//秒
	
	/** 刷怪物、宝箱提示内容调整 */
	public static String startWith = "<f color='0xFFFF00' size='40'>";
	public static String endWith = "</f>";

	public static String[] colorString = new String[]{"<f color='0xFFFFFF'>","<f color='0x00FF00'>","<f color='0x0000FF'>","<f color='0xE706EA'>","<f color='0xFDA700'>","<f color='0xFFCC33'>"};
	public static int minEnterLevel = 110;
	/** 恶魔广场中可原地复活次数 */
	public static int RELIVE_TIMES = 0;
	
	/** 封印等级    例：110封印解封后果7天开启110级恶魔城堡封印*/
	public static int SEAL_MAX_LEVELS = 220;
	public static int[] SEAL_OPEN_LEVELS_DEVILSQUARE = new int[]{110, 150, 190, 220};				
	public static int[] SEAL_OPEN_DAYS_DEVILSQUARE = new int[]{7, 7, 7, 7};		
	
	public static int PRE_NOTIFY = 2	//城堡门刷新前多久滚屏提示(分钟)
	;
}
