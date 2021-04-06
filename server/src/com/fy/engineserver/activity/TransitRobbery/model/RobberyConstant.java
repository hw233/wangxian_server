package com.fy.engineserver.activity.TransitRobbery.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.language.Translate;


public class RobberyConstant {
	
	
	public static void main(String[] args) throws Exception{
		int a = 0;
		int b = 20954370;
		int c = 0;
		int result = (int) ((a + b) * (float)((100 + c) / 100));
		System.out.println(result);
		System.out.println(Integer.MAX_VALUE);
	}
	
	public static long[] 需要清除的魂石Id = new long[]{1408000000148722186L, 1408000000156481075L, 1408000000156483394L, 1408000000151537410L, 
		1408000000155010841L,1408000000146006183L,1408000000154917805L};
	
	public static int 玩家可升最高等级 = 300;			//以后记得修改
	
	/** 玩家心跳时判断，玩家进入此地图直接传回王城 */
	public static List<String> 没飞升玩家不可进入的地图 = new ArrayList<String>();
	static{
		没飞升玩家不可进入的地图.add("binglingjidao");
		没飞升玩家不可进入的地图.add("fengguimingdi");
		没飞升玩家不可进入的地图.add("huohuangfenjing");
		没飞升玩家不可进入的地图.add("leidouhuanyu");
		没飞升玩家不可进入的地图.add("lingxiaotiancheng");
		没飞升玩家不可进入的地图.add("lunhuiyiji");
		没飞升玩家不可进入的地图.add("wanhuaxiangu");
		没飞升玩家不可进入的地图.add("xiandangong");
		没飞升玩家不可进入的地图.add("xiandibaoku");
		没飞升玩家不可进入的地图.add("xiandimijing");
		没飞升玩家不可进入的地图.add("zhongshenyiji");
	}
	
	public static String 台服广告显示 = "2014巴西世界盃在熱烈歡呼聲中隱藏著些許遺憾，太多足壇大神無緣世界盃這無疑讓億萬球迷百感交集！沒關係！讓你心中的英雄重回綠茵賽場，揮灑熱血，釋放激情！";
	public static boolean 台服广告开关 = false;
	public static String 台服广告url = "";
	
	public static int[] noTitle = new int[]{1,3,9};
	
	public static int 国家排行显示数 = 100;
	public static int 单个重数显示数  = 20;
	
	public static int MAXEXPCOUNT = 3;			//每天最多获得三次经验观礼
	public static long MAXEXPAWARD = 453234223;			//观礼最多获得经验值--默认现有配备220级的升级经验的百分之5
	/** 火炕数值 */
	public static int[] HUODEFANCE = new int[]{130000,200000,300000,400000};
	public static int BASEDEFANCE = 50;			//达到13W金防抵挡百分之五十伤害
	public static int NEXTDEFANCE = 10;			//达到下一数值额外抵挡伤害
	public static String 火神buff = Translate.四象火神;
	
	public static final String CONFIRM = Translate.离开;
	public static final String CANCLE = Translate.暂留;
	
	/** 渡劫封印 */
	public static int MAXLEVEL = 9;				//代表玩家只能通过四重天劫
	/** 渡劫失败延迟传出时间 */
	public static final int 延迟传出时间 = 2;
	/** 观礼加经验百分比 */
	public static final int expAdd = 5;
	/***
	 * 渡劫开始时间
	 */
//	public static Timestamp OPENTIME = Timestamp.valueOf("2013-09-25 18:30:00");	//测试
	public static Timestamp OPENTIME = Timestamp.valueOf("2013-10-22 00:00:00");	//正式
	
	public static String BUFF定身 = "BUFF/定身";
	
	public static String DUJIEMAP = "jiuzhongyunxiao";
	/** 的渡劫失败惩罚buff */
	public static String FAILBUFF = Translate.渡劫虚弱;
	/** 渡劫专属buff---保存，在渡劫完成后清除 */
	public static String[] dujieBuffs = new String[]{"Buff_YuanSuShangHai", "Buff_GuaiWuShangHai","Buff_RayDamage","BuffTemplate_didangshanghai"};
	/** 渡劫成功心法上线提高 */
	public static int[] diyitaoxinfa = new int[]{0,10,20,30,40,50,70,90,100, 100};		//渡过1-8级时开第二套心法
	public static int[] diertaoxinfa = new int[]{10,40};		//渡过8-9级开第三套心法
	/** 天劫道具表 */
	public static String[] 屏蔽散仙 = new String[]{Translate.护身罡气, Translate.护身真气};
	public static int[] 屏蔽个数 = new int[]{2,10};
	public static String[] 减少劫兽伤害 = new String[]{Translate.光法阵, Translate.幻象镜};
	public static int[] 对应buff级别 = new int[]{0,1};			//减少劫兽伤害数据
	public static String[] 抵挡伤害  = new String[]{Translate.元素精华};		
	public static String[] 提升元素伤害 = new String[]{Translate.元素之魂};
	public static String[] 减少雷击 = new String[]{Translate.强体丹};
	public static String[] 增加防御 = new String[]{Translate.心魔丹};		//配表注释为减少所受到的伤害，实现有问题，改为加玩家防御
	public static String 火神减伤 = Translate.火神玄魄;		//火神太牛了，削弱下，减少对玩家的伤害结果
	public static int decreasePers = 50;		//火神减伤比例
	public static int HUOSHENID = 900009;
	public static String[] qiqingjieBoss = new String[]{Translate.七情喜灵, Translate.七情怒灵, Translate.七情忧灵, Translate.七情惧灵, Translate.七情爱灵, Translate.七情憎灵, Translate.七情欲灵};
	/** 概率--百分比 */
	public static int HUNDRED = 100;
	/** 一分钟的毫秒数 */
	public static long ONE_HOUR_MIl = 60 * 60 * 1000;
	public static long ONE_MINIT_MIl = 60 * 1000;
	public static long ONE_MINIT_SECOND = 60;
	public static int ONE_DAY_SECOND = 24 * 60 * 60;
	/** 怪物类型 */
	public static final int BOSS = 1;	
	public static final int IMMORTAL = 2;		//散仙
	public static final int BEAST = 3;			//劫兽
	/** 数据库更新类型 */
	public static final int UPDATE_START_TIME = 1;
	public static final int UPDATE_END_TIME = 2;
	public static final int UPDATE_DOBBERY_LEVEL = 3;
	public static final int UPDATE_FORCE_PULL_TIME = 4;
	public static final int INIT_FORCE_PULL_TIME = 5;
	public static final int UPDATE_PASSLAYER = 6;
	/** 四相劫中雷击间隔时间 和伤害 */
	public static int[] INTERVAL = new int[]{15, 10, 8};
	public static int[] RAYDAMAGE = new int[]{20,50,40,60};
	public static int[] MAXRAYDAMAGE = new int[]{400000,600000,800000,1200000};
	public static int MINRAYDAMAGE = 350000;			//四相劫雷劫最低伤害值
	/** 元神挑战时元神等级限制 */
	public static int SOUL_LEVEL_LIMIT = 110;
	/** 劫兽和散仙的怪物id */
	public static int IMMORTAL_ID = 900005;
	public static int BEAST_ID = 141002;
	/** 倒计时类型 */
	public static byte COUNTDOWN_FORCETIME = 1;
	public static byte COUNTDOWN_IN_ROBBERY = 2;
	/** 四种任务模型boss  刷新时需要重置名字的   (通用（默认修罗）,修罗、影魅、仙心、九黎 兽魁)*/
	public static int[] BOSS_MODEL_IDS = new int[]{900020,900020,900023, 900021,900022, 20113356};	
	/** 神魂劫刷出的bossId */
	public static int SHENHUN_BOSSID = 900024;
	/** 七情劫怪物属性为玩家属性的倍数 */
	public static float[] MUTI_PROPERTIES = new float[]{1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f, 2.2f};
	//心魔boss血量倍数
	public static float MUTI_HP_XINMO = 1f;
	//七情劫怪物血量倍数
	public static float[] MUTI_HP_PROPERTIES = new float[]{1f, 1f, 1f, 1f, 1f, 1f, 1f};
	/** 幻象劫相关数据 */
	public static int SKILL_ACT_HP = 10;		//血量低于百分之10
	public static int LAST_TIME = 10;				//10s后未死亡恢复
	public static float RECOVER_HP = 0.3f;			//恢复到血量的百分之30
	/** 神魂劫触发   */
	public static final int SUCCEED_HP_PERCENT_SHENHU = 2;
	/** 无相劫触发 */
	public static float SUCCEED_HP_PERCENT_WUXIANG = 0.5f;
	public static int NPCID = 600071;
	/** 飞升后裸装形象 */
	public static String[] douluoAvatar = new String[]{"part/XZJ_douluo.xtl","part/chanzhang01_XZJ_douluo.xtl","part/zhuangbei01_XZJ_douluo.xtl","part/yonghengzhiyi_XZJ_douluo.xtl"};
	public static String[] guishaAvatar = new String[]{"part/XZJ_guisha.xtl", "part/shuangdao01_XZJ_guisha.xtl","part/zhuangbei01_XZJ_guisha.xtl","part/yonghengzhiyi_XZJ_guisha.xtl"};
	public static String[] wuhuangAvatar = new String[]{"part/XZJ_wuhuang.xtl","part/mozhang01_XZJ_wuhuang.xtl","part/zhuangbei01_XZJ_wuhuang.xtl","part/yonghengzhiyi_XZJ_wuhuang.xtl"};
	public static String[] lingzunAvatar = new String[]{"part/XZJ_lingzun.xtl","part/fazhang01_XZJ_lingzun.xtl","part/zhuangbei01_XZJ_lingzun.xtl","part/yonghengzhiyi_XZJ_lingzun.xtl"};
	public static String[] shoukuiAvatar = new String[]{"part/XZJ_shoukui.xtl","part/quanzhang_XZJ_shoukui.xtl","part/zhuangbei01_XZJ_shoukui.xtl","part/yonghengzhiyi_XZJ_shoukui.xtl"};
	public static byte[] avatarType = new byte[]{0, 1, 2, 3};			//3是肩膀，需要判断
	
	public static String[] oldDouluoAvata = new String[]{"/part/ZJ_douluo.xtl","/part/fuzi10_ZJ_douluo.xtl","/part/yifu04_ZJ_douluo.xtl","/part/fuzi_ZJ_douluo.xtl"};
	public static String[] oldguishaAvatar = new String[]{"/part/ZJ_guisha.xtl", "/part/shuangci10_ZJ_guisha.xtl","/part/yifu04_ZJ_guisha.xtl","/part/bishou_ZJ_guisha.xtl"};
	public static String[] oldwuhuangAvatar = new String[]{"/part/ZJ_wuhuang.xtl","/part/fan10_ZJ_wuhuang.xtl","/part/yifu04_ZJ_wuhuang.xtl","/part/gx_ZJ_wuhuang.xtl"};
	public static String[] oldlingzunAvatar = new String[]{"/part/ZJ_lingzun.xtl","/part/baojian10_ZJ_lingzun.xtl","/part/yifu04_ZJ_lingzun.xtl","/part/gx_ZJ_lingzun.xtl"};
	public static String[] oldshoukuiAvatar = new String[]{"/part/ZJ_shoukui.xtl","/part/liandao05_ZJ_shoukui.xtl","/part/yifu04_ZJ_shoukui.xtl","/part/liandao_ZJ_shoukui.xtl"};
	public static byte[] oldavatarType = new byte[]{0, 1, 2, 13};			
	public static Map<Byte, String[]> tempAvatar = new HashMap<Byte, String[]>();
	public static String[] 限制使用拉令的仙界地图集 = {"dongfu", "houtianqiankunjie", "xiantianyinyangjie", "taijihunyuanjie", "wujihundunjie",
		"xiandibaoku","xiandangong","zhongshenyiji", "kongdao","shenghunlingyu","wushenzhidian","wushenyiji","xianlingdahuibuzhuo","xianlingdahuibuzhuo01","xiandimijing"};
//	public static String[] 限制使用拉令的仙界地图集 = {"binglingjidao","fengguimingdi","huohuangfenjing","leidouhuanyu","lingxiaotiancheng","wanhuaxiangu",
//		"xiandibaoku","xiandimijing","dongfu", "houtianqiankunjie", "xiantianyinyangjie", "taijihunyuanjie", "wujihundunjie","zhongshenyiji"
//	};
	public static String[] vip限制拉人地图 = new String[]{"houtianqiankunjie","xiantianyinyangjie","taijihunyuanjie","wujihundunjie","jianyu","qinglongshengtan","zhuqueshengtan","baihushengtan","xuanwushengtan","qilinshengtan"
		,"qinglongmijing","zhuquemijing","baihumijing","xuanwumijing","qilinmijing","jiuzhongyunxiao","lunhuiyiji","zhongshenyiji", "kongdao","shenghunlingyu","wushenzhidian","wushenyiji","xianlingdahuibuzhuo","xianlingdahuibuzhuo01"};
	
	static{
		tempAvatar.put((byte)1, oldDouluoAvata);
		tempAvatar.put((byte)2, oldguishaAvatar);
		tempAvatar.put((byte)3, oldlingzunAvatar);
		tempAvatar.put((byte)4, oldwuhuangAvatar);
		tempAvatar.put((byte)5, oldshoukuiAvatar);
	}
	
	public static String getLevelDes(int level) {
		if (level <= 220) {
			return level+"";
		} else {
			return Translate.仙 + (level-220);
		}
	}
}
