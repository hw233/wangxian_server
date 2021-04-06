package com.fy.engineserver.minigame;

import com.fy.engineserver.datasource.language.Translate;

public class MinigameConstant {
	/** 小游戏每天可以进行的轮数和每轮的次数  */
	public static int MAX_ROUND = 5;
	public static int PER_ROUNT_FREQUENCE = 10;
	public static int MAX_ONE_DAY_GAMES = 50;
	/** 每日免费生命次数 --等同总生命*/
	public static int DEFAULT_LIFE = 3;
	public static int MAX_BUYLIFE_COUNT = 2;	//非vip最多可购买次数
	/** 小游戏类型 */
	public static final byte JIGSAW = 4;		//拼图
	public static final byte MEMORY = 1;
	public static final byte ELIMINATE = 2;
	public static final byte PIPE = 3;
	/** 通关状态 */
	public static byte EXCELENT = 2;
	public static byte GOOD = 1;
	public static byte FAIL = -1;
	/** 小游戏类型-策划配置 */
	public static String pintu = "拼图";
	public static String jiyi = "记忆";
	public static String duiduipeng = "对对碰";
	public static String guandao = "管道";
	/** 点击npc操作类型 */
	public static String ACCEPTGAME = "领取小游戏";
	public static String BUYLIFE = "买命";
	public static String START_WINDOW = "开始窗口";
	/** 默认固定罐子数据与需要记忆的罐子数量*/
	public static String[] MEMORYLIST = new String[]{"lvbaoshi9", "chengbaoshi9", "chengjingshi9", "heijinshi9", "lanbaoshi9", "hongbaoshi9", "baijingshi9"
		, "huangbaoshi9"};
	public static int MEMORYCOUNTS = 5;
	/** 对对碰数  以及不成对的数量*/
	public static int ELIMENATE_COUNTS = 25;
	public static int SINGLE_COUNTS = 21;
	public static int MAX_DOUBLE_COUNT = ELIMENATE_COUNTS - SINGLE_COUNTS;
	/** 小游戏总积分奖励 */
	public static int GRADE_COUNT = 60;
	public static int AWARD_NUM = 1000;
	/** 拼图游戏地图 */
//	public static String[] JSCONTANT = new String[]{"linglongtabaceng", "linglongtaerceng", "linglongtajiuceng", "linglongtaliuceng", "linglongtaqiceng", "linglongtasanceng", "linglongtashiceng", "linglongtasiceng", "linglongtayiceng", "huanggong", "xiaoyinsi", "xiaoyaojin", "baicaojing", "kunhuagucheng", "tongboshan", "motianya", "wangchengmidao", "yaosai",
//		"fengshentai", "feishengya", "yulongpu", "luoxingyutai", "banruobaocha", "bolangsha", "huishenggu", "canglangshui", "fenglingdu", "yunmengze", "diyumoyuan", "ranshaoshengdian", "lianyujuedi", "yunboguiling", "huawaizhijing"};
	/** 领取小游戏消耗的物品 */
	public static String COSTTYPE = Translate.小游戏参与券;
	/** 买命确认 取消*/
	public static String CONFIRM = Translate.text_125;
	public static String CANCLE = Translate.text_126;
	/** 开始游戏倒计时时间 */
	public static int TEMP_TIME = 4;
	
	public static String DEFAULT_STR = "default_str_eliminate";
	/** 管道提示数量 */
	public static int PIPE_COUNTS = 4;
}
