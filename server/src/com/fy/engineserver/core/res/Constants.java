package com.fy.engineserver.core.res;

import com.fy.engineserver.datasource.language.Translate;


public interface Constants {
	//玩家出生地图
	public static final String mapNameBorn = "linshuangcun";
	
	public static final String [] bornName = {"","linshuangcun","taoyuancun","jiaoshacun"};
	
	public static final String mapNameBorn4Shoukui = "luoyueshangu";
	//出生点区域的名
	public static final String mapAreaBorn[] = {Translate.出生点,Translate.出生点1,Translate.出生点2,Translate.出生点3};
	
	//国家
	public static final String[] countrys = new String[]{"昆仑","九州","万法"};
	
	//种族定义
	public static final String race_human = "ZJ";
	public static final String race_human_new = "XZJ";
	
	public static final int CAREER_DOULUO = 1;
	public static final int CAREER_GUISHA = 2;
	public static final int CAREER_LINGZUN = 3;
	public static final int CAREER_WUHUANG = 4;
	public static final int CAREER_SHOUKUI = 5;
	
	public static final String sex_career[] = {"xiuluo","yingmei","xianxin","jiuli", "shoukui"};
	public static final String race_yao = "yao";
	public static final String races[] = new String[]{race_human,race_yao};
	
	//性别定义
	public static final String sex_male = "nan";
	public static final String sex_female = "nv";
	
	public static final String sexs[] = new String[]{sex_male,sex_female};
	
		
	//状态定义
	public static final String state_dizzy = "晕";
	public static final String state_slow = "减速";
	public static final String state_sleep = "睡眠";
	public static final String state_seal = "封印";
	public static final String state_dingshen = "定身";
	public static final String statenames[] = new String[]{ state_dizzy,state_slow,state_sleep,state_seal,state_dingshen,};
	
	//动作定义	
	
	public static final String stand="站";
	public static final String dazuo="打坐";
	public static final String run="跑";
	public static final String attack1h="攻击1";
	public static final String attack2h="攻击2";
	public static final String attack3h="攻击3";
	public static final String attack4h="攻击4";
	public static final String attack5h="攻击5";
	public static final String attack6h="攻击6";
	public static final String death="死亡";
	public static final String talk="交谈";
	public static final String fly="飞行";
	public static final String bomb="爆炸";
	public static final String cast_prepair="施法聚气";
	public static final String cast_fire="施法";
	public static final String use_item="使用道具";
	
//	public static final String stand="站";
//	public static final String dazuo="打坐";
//	public static final String run="跑";
//	public static final String attack1h="攻击1";
//	public static final String attack2h="攻击2";
//	public static final String death="死亡";	
//	
//	public static final String fly="飞行";
//	public static final String bomb="爆炸";
//	
//	public static final String cast_prepair="施法聚气";
//	public static final String cast_fire="施法";
//	public static final String use_item="使用道具";
	
	//动作定义

	public static final String actions[]   =    {stand,dazuo,run,attack1h,attack2h,attack3h,attack4h,attack5h,attack6h,death,
		/*fly*/attack1h ,/*bomb*/attack1h,
		cast_prepair,cast_fire,use_item,talk,death
	};

	
//	public static final String actions[] = new String[] {stand,dazuo,run,attack1h,attack2h,death,			
//		fly,bomb,
//		cast_prepair,cast_fire,use_item,
//	};
//	
	public static final byte STATE_STAND = 0;
	public static final byte STATE_DAZUO = 1;
	public static final byte STATE_MOVE = 2;
	public static final byte STATE_ATK1H = 3;
	public static final byte STATE_ATK2H = 4;
	public static final byte STATE_ATK3H = 5;
	public static final byte STATE_ATK4H = 6;
	public static final byte STATE_ATK5H = 7;
	public static final byte STATE_ATK6H = 8;
	public static final byte STATE_DEAD = 9;

	public static final byte STATE_FLY = 10;
	public static final byte STATE_EXPLODE = 11;

	public static final byte STATE_CAST_PREPAIR = 12;
	public static final byte STATE_CAST_FIRE = 5;//9;
	public static final byte STATE_USE_ITEM = 13;
	public static final byte STATE_TALK = 14;
	public static final byte STATE_XUANYUN = 15;
	
//	public static final byte STATE_STAND = 0;
//	public static final byte STATE_DAZUO = 1;
//	public static final byte STATE_MOVE = 2;
//	public static final byte STATE_ATK1H = 3;
//	public static final byte STATE_ATK2H = 4;
//	public static final byte STATE_DEAD = 5;
//	
//	public static final byte STATE_FLY = 6;
//	public static final byte STATE_BOMB = 7;
//	
//	public static final byte STATE_CAST_PREPAIR = 8;
//	public static final byte STATE_CAST_FIRE = 9;
//	public static final byte STATE_USE_ITEM = 10;
	
	
	//方向定义
	public static final String UP_NAME = "上";
	public static final String DOWN_NAME = "下";
	public static final String LEFT_NAME = "左";
	public static final String RIGHT_NAME = "RIGHT";

	
	public static final String directions[] = new String[] {UP_NAME,DOWN_NAME,LEFT_NAME,LEFT_NAME};
	public static final byte UP = 0;
	public static final byte DOWN = 1;
	public static final byte LEFT = 2;
	public static final byte RIGHT = 3;
	public static final byte LEFT_UP = 4;
	public static final byte LEFT_DOWN = 5;
	public static final byte RIGHT_UP = 6;
	public static final byte RIGHT_DOWN = 7;
	
	public static final byte NONE = -1;
	
	//部件类型定义
	public static final byte TYPE_BODY = 0;
	public static final byte TYPE_WEAPON = 1;
	public static final byte TYPE_CLOTH = 2;
	public static final byte TYPE_SHOULDER = 3;	
	public static final byte TYPE_HEAD = 4;
	public static final byte TYPE_HORSE = 5;	
	public static final byte TYPE_STATUS = 6;		
	public static final byte TYPE_LIGHT_EFFECT = 7;	
	public static final byte TYPE_SKILL_EFFECT = 8;
	public static final byte TYPE_BUILDING = 9;
	public static final byte TYPE_UI = 10;
	public static final byte TYPE_SHADE = 11;	
	public static final byte TYPE_OTHER = 12;
	public static final byte TYPE_WEAPON_GX = 13;
	public static final String[] partTypes = new String[]{"裸体","武器","衣服","肩膀","头","坐骑","状态","光效","技能后效","地物","UI","阴影","其他","武器光效"};
	
	//部件绘制顺序
	public static final int sortByDraw[][] = new int[][]{
			{TYPE_SHADE, TYPE_HORSE, TYPE_WEAPON,TYPE_WEAPON_GX,TYPE_BODY, TYPE_CLOTH,TYPE_SHOULDER,TYPE_HEAD,TYPE_STATUS,TYPE_LIGHT_EFFECT,TYPE_OTHER},//上
			{TYPE_SHADE,TYPE_BODY,TYPE_CLOTH,TYPE_SHOULDER,TYPE_HEAD,TYPE_WEAPON,TYPE_WEAPON_GX,TYPE_HORSE,TYPE_STATUS,TYPE_LIGHT_EFFECT,TYPE_OTHER},//下
			{TYPE_SHADE,TYPE_HORSE,TYPE_BODY,TYPE_CLOTH,TYPE_HEAD,TYPE_SHOULDER,TYPE_WEAPON,TYPE_WEAPON_GX,TYPE_STATUS,TYPE_LIGHT_EFFECT,TYPE_OTHER},//左
	};
	
	// 生物的类型
	public static final byte PLAYER = 0;
	public static final byte SPRITE = 1;
	public static final byte ENTITY_SUMMONED = 2;
	public static final byte EFFECT_SUMMONED = 3;
	
	// 团队标记，无团队标记
	public static final byte TEAM_MARK_NONE = 0;

	// 团队标记，是团队普通成员
	public static final byte TEAM_MARK_MEMBER = 1;

	// 团队标记，是团队的队长
	public static final byte TEAM_MARK_CAPTAIN = 2;
	/**
	 * 战斗方的类型，分敌方，中立方，友方
	 * 
	 * @return
	 */
//	public static final int FIGHTING_TYPE_ENEMY = 0;
//	public static final int FIGHTING_TYPE_NEUTRAL = 1;
//	public static final int FIGHTING_TYPE_FRIEND = 2;

	/**
	 * 伤害类型
	 */
//	public static final int DAMAGETYPE_PHYSICAL = 0;
//	public static final int DAMAGETYPE_SPELL = 1;
//	public static final int DAMAGETYPE_PHYSICAL_CRITICAL = 2;
//	public static final int DAMAGETYPE_SPELL_CRITICAL = 3;
//	public static final int DAMAGETYPE_DODGE = 4;
//	public static final int DAMAGETYPE_ZHAONGDU = 5;
//	public static final int DAMAGETYPE_FANSHANG = 6;

	// 练功模式
	public static final int PLAYING_MODE_PVE = 0;
	// PK模式
	public static final int PLAYING_MODE_PVP = 1;
	
	public static final int EQUIP8_EFFECT = 1;
	public static final int EQUIP12_EFFECT = 2;
	public static final int EQUIP16_EFFECT = 3;
	
	
	//资源类型 1-图片切片，2-部件素材，3-形象素材,4-地图,5-纯图片
	public static final byte RES_TYPE_PNGMTL = 1;
	public static final byte RES_TYPE_PART = 2;
	public static final byte RES_TYPE_APPEAR = 3;
	public static final byte RES_TYPE_GAMEMAP = 4;
	public static final byte RES_TYPE_PNGRES = 5;	
	public static final int RES_TYPE_NUM = 5;
	/** 兽魁噬魂效果，中此效果对应的狂怒伤害增加  咆哮会传播新buff */
	public static final byte shihunFlag = 1;
	
	public static long 变身CD = 5000;
}
