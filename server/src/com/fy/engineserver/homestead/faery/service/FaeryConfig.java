package com.fy.engineserver.homestead.faery.service;

import com.fy.engineserver.datasource.language.Translate;

public interface FaeryConfig {

	/** 建筑最高等级 */
	int BUILDING_MAX_LEVEL = 20;
	/** 系统默认的仙境个数 */
	int DEFAULT_FAERY_NUM = 2;// 10;
	/** 加速建筑所需VIP等级 */
	int ACCELERATE_SCHEDULE_NEED_VIPLEVEL = 4;
	// 建筑类型
	/** 主建筑 */
	int CAVE_BUILDING_TYPE_MAIN = 0;
	/** 仓库 */
	int CAVE_BUILDING_TYPE_STORE = 1;
	/** 宠物房 */
	int CAVE_BUILDING_TYPE_PETHOUSE = 2;
	/** 门牌 */
	int CAVE_BUILDING_TYPE_DOORPLATE = 3;
	/** 栅栏 */
	int CAVE_BUILDING_TYPE_FENCE = 4;
	/** 田地1 */
	int CAVE_BUILDING_TYPE_FIELD1 = 5;
	/** 田地 2 */
	int CAVE_BUILDING_TYPE_FIELD2 = 6;
	/** 田地 3 */
	int CAVE_BUILDING_TYPE_FIELD3 = 7;
	/** 田地 4 */
	int CAVE_BUILDING_TYPE_FIELD4 = 8;
	/** 田地 5 */
	int CAVE_BUILDING_TYPE_FIELD5 = 9;
	/** 田地 6 */
	int CAVE_BUILDING_TYPE_FIELD6 = 10;

	String[] CAVE_BUILDING_NAMES = Translate.CAVE_BUILDING_NAMES;

	String[] FIELD_INDEX_NAME = Translate.FIELD_INDEX_NAME;
	// 需要时长的操作类型：
	/** 提升食物等级 */
	int OPTION_FOOD_SIZEUP = 0;
	/** 提升木材等级 */
	int OPTION_WOOD_SIZEUP = 1;
	/** 提升石料等级 */
	int OPTION_STONE_SIZEUP = 2;
	/** 建筑升级 */
	int OPTION_LEVEL_UP = 3;
	/** 种植 */
	int OPTION_PLANT = 4;

	// 偷菜时间 主人 非主人 读条时间
	/** 自己收获读条时间 */ 
	int READ_LINE_TIME_SELF = 2;
	/** 其他人收获读条时间 */
	int READ_LINE_TIME_OTHER = 10;
	/** 仙境中最大仙府数量 */
	int MAX_CAVE_IN_FAERY = 24;
	/** 开启下一个仙境其他仙境满足数量条件 */
	int FAERY_OPEN_NEXT_NUM = 10;

	// 其他人每株植物可摘取上限
	int PICK_MAX_NUM = 2;

	// 栅栏状态
	/** 栅栏状态-关闭 */
	int FENCE_STATUS_CLOSE = 0;
	/** 栅栏状态-打开 */
	int FENCE_STATUS_OPEN = 1;

	// 果实资源类型
	/** 资源-食物 */
	int FRUIT_RES_FOOD = 0;
	/** 资源-木材 */
	int FRUIT_RES_WOOD = 1;
	/** 资源-石料 */
	int FRUIT_RES_STONE = 2;
	/** 资源-特殊 */
	int FRUIT_RES_SPECIAL = 3;

	String[] FRUIT_RES_NAMES = { Translate.text_cave_043, Translate.text_cave_044, Translate.text_cave_045, Translate.text_cave_046 };

	// 产出类型
	/** 产出物品 */
	int OUTPUT_TYPE_ARTICLE = 0;
	/** 产出经验 */
	int OUTPUT_TYPE_EXP = 1;
	/** 产出钱（游戏币） */
	int OUTPUT_TYPE_MONEY = 2;

	/** 负责心跳的线程数 */
	int BEATHEART_THREAD_NUM = 10;
	/** 每个线程处理上限 */
	int BEATHEART_MAX_NUM = 20;
	// 配置信息
	/** 主建筑 */
	int FILE_CFG_MAIN = 0;
	/** 仓库 */
	int FILE_CFG_STOREHOUSE = FILE_CFG_MAIN + 1;
	/** 仓库资源等级 */
	int FILE_CFG_STOREHOUSE_RESLV = FILE_CFG_STOREHOUSE + 1;
	/** 宠物仓库 */
	int FILE_CFG_PETHOUSE = FILE_CFG_STOREHOUSE_RESLV + 1;
	/** 仓库兑换道具 */
	int FILE_CFG_CONVERT_ARTICLE = FILE_CFG_PETHOUSE + 1;
	/** 栅栏配置 */
	int FILE_CFG_FENCE = FILE_CFG_CONVERT_ARTICLE + 1;
	/** 田地升级配置 */
	int FILE_CFG_FIELD_LVUP = FILE_CFG_FENCE + 1;
	/** 田地开垦配置 */
	int FILE_CFG_FIELD_ASSART = FILE_CFG_FIELD_LVUP + 1;
	/** 种植列表配置 */
	int FILE_CFG_PLANT_LIST = FILE_CFG_FIELD_ASSART + 1;
	/** 位置配置 */
	int FILE_CFG_POINT = FILE_CFG_PLANT_LIST + 1;
	/** 建筑外形设置 */
	int FILE_CFG_OUTSHOW = FILE_CFG_POINT + 1;
	/** 增加建筑队列道具 */
	int FILE_ADD_QUEUE = FILE_CFG_OUTSHOW + 1;
	/** 各种颜色果实兑换倍率 */
	int FILE_EXCHANGE_RATE = FILE_ADD_QUEUE + 1;
	/** 各等级宠物挂机基础经验 */
	int FILE_PET_HOOKEXP = FILE_EXCHANGE_RATE + 1;
	/** 杂项配置 */
	int FILE_OTHERS = FILE_PET_HOOKEXP + 1;
	/** 炸弹配置 */
	int FILE_BOMB = FILE_OTHERS + 1;

	long MAX_HOOK_TIME = 1000L * 60 * 60 * 24 * 3;

	// 颜色配置
	/** 白 */
	int COLOR_WHITE = 0;
	/** 蓝 */
	int COLOR_BLUE = 1;
	/** 黄 */
	int COLOR_YELLOW = 2;
	/** 绿 */
	int COLOR_GREEN = 3;
	/** 紫 */
	int COLOR_PURPLE = 4;

	String[] COLORS = { "白", "蓝", "黄", "绿", "紫" };

	// 新建驻地中的默认资源
	int DEFAULT_FOOD = 2000;
	int DEFAULT_WOOD = 2000;
	int DEFAULT_STONE = 2000;

	// 洞府状态
	/** 开放状态 */
	int CAVE_STATUS_OPEN = 0;
	/** 封印状态 */
	int CAVE_STATUS_KHATAM = 1;
	/** 删除状态 */
	int CAVE_STATUS_DELETE = 2;
	/** 锁定 */
	int CAVE_STATUS_LOCKED = 3;

	String[] CAVE_STATUS_ARR = { "开放", "封印", "删除" ,"锁定"};
	// 默认NPC
	// int DEFAULT_NPC_MAIN = 123;
	// int DEFAULT_NPC_STOREHOUSE = 123;
	// int DEFAULT_NPC_PETHOUSE = 123;
	// int DEFAULT_NPC_FENCE = 123;
	// int DEFAULT_NPC_DOORPLATE = 123;
	// int DEFAULT_NPC_FIELD = 12;

	// 土地状态
	/** 土地状态-未开垦 */
	int FIELD_STATUS_DESOLATION = 0;
	/** 土地状态-未种植 */
	int FIELD_STATUS_UNPLANTING = 1;
	/** 土地状态-种植中 */
	int FIELD_STATUS_PLANTING = 2;

	// 植物外形状态
	/** 初始状态 */
	int PLANT_AVATA_STATUS_DEFAULT = 0;
	/** 更改后形象 */
	int PLANT_AVATA_STATUS_CHANGED = 1;
	/** 成熟了 */
	int PLANT_AVATA_STATUS_GROWNUP = 2;

	/** 每天偷菜的上限 */
	int DAILY_STEAL_MAXNUM = 100;

	// 建筑升级状态
	/** 正常状态 */
	int CAVE_BUILDING_STATUS_SERVICE = 0;
	/** 升级状态 */
	int CAVE_BUILDING_STATUS_LVUPING = 1;

	String[] CAVE_BUILDING_STATUS_ARR = { "服务中", "升级中" };
	/** 默认的最大工作上限 */
	int DEFAULT_MAX_ACTIVE_SCHEDUL = 4;

	String GAME_NAME = "dongfu";
	
	long INCREASESCHEDULE_TIME_LIMIT = 1000 * 60 *60 *360;//仙府增加队列上限 最大持续时间   
}
