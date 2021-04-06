package com.fy.engineserver.datasource.article.data.magicweapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.language.Translate;

public class MagicWeaponConstant {
	
	public static boolean 宠物协议 = true;
	public static boolean 宠物协议日志 = false;
	
	public static int 宠物协议出错多少次后将玩家踢下线 = 20;
	public static int 宠物协议出错封号等级 = 70;
	public static Map<Long, Integer> 宠物协议出错记录 = new Hashtable<Long, Integer>();
	
	public static int 伤害数值 = 10000000;
	
	public static void main(String[] args) {
		float f = 8f / 100f;
		int d = 100;
		int g = (int) (d * f);
		System.out.println(g);
	}
	
	
	/** 基础技能对应id */
	public static final int hpNum = 1;								//气血
	public static final int physicAttNum = 2;						//外攻
	public static final int magicAttNum = 3;						//内攻
	public static final int hitNum = 4;								//命中
	public static final int dodgeNum = 5;							//闪避
	public static final int physicDefanceNum = 6;					//外防
	public static final int magicDefanceNum = 7;					//内防
	public static final int cirtNum = 8;							//暴击
	public static final int mpNum = 9;								//仙法
	public static final int 打怪经验 = 10;								//打怪经验
	public static final int 掉宝率 = 11;								//掉宝率
	public static final int 法宝吞噬经验 = 12;							//法宝吞噬经验
	public static final int 宠物打怪经验 = 13;							//宠物打怪经验
	public static final int 精准 = 14;								//精准
	public static final int 免爆 = 15;								//免爆
	public static final int 火攻 = 16;								//火攻
	public static final int 冰攻 = 17;								//冰攻,
	public static final int 雷攻 = 18;								//雷攻
	public static final int 风攻 = 19;								//风攻
	public static final int 破甲 = 20;								//破甲
	public static final int 火炕 = 21;								//火抗
	public static final int 冰抗 = 22;								//冰抗
	public static final int 雷抗 = 23;								//雷抗
	public static final int 风抗 = 24;								//风抗
	public static final int 火减抗 = 25;								//火减抗
	public static final int 冰减抗 = 26;								//冰减抗
	public static final int 雷减抗 = 27;								//雷减抗
	public static final int 风减抗 = 28;								//风减抗
	public static final int 祈福冰凤 = 29;								//风减抗
	public static final int 紫色果实 = 30;								//风减抗
	public static final int 橙色果实 = 31;								//风减抗
	public static final int 法宝掉落 = 32;								//法宝掉落
	public static final int 移动速度 = 33;								//法宝掉落
	public static final int 买药打折 = 34;								//法宝掉落
	public static final int 回城复活血量增加 = 35;								//法宝掉落
	public static final int 修理装备打折 = 36;								//法宝掉落
	public static final int 兽魂属性 = 37;								//法宝掉落
	public static final int 法宝基础属性 = 38;								//法宝掉落
	
	public static String[] skillStr = {"","气血","外攻","内攻","命中","闪避","外防","内防","暴击","仙法","打怪经验","掉宝率","法宝吞噬经验","宠物打怪经验"
										,"精准","免爆","火攻","冰攻","雷攻","风攻","破甲","火炕","冰抗","雷抗","风抗","火减抗","冰减抗","雷减抗","风减抗"
										,"风减抗","风减抗"};
	public static Map<Integer, String> mappingKeyValue = new HashMap<Integer, String>();		//属性配表描述映射type--很多地方用。
	public static Map<Integer, String> mappingKeyValue2 = new HashMap<Integer, String>();		//只用来当描述。
	public static Map<String, Integer> mappingValueKey = new HashMap<String, Integer>();		//type映射属性描述----拼泡泡时候用
	
	/** 读表用 */
	public static final int 翻译描述_所在sheet = 4;
	/** 属性增加类型 */
	public static final int addNum = 1;			//增加具体数值			
	public static final int addPercent = 2;			//增加人物属性百分比		
	public static final int addMagicWeaponPercent = 3;	//增加法宝自身属性百分比(在计算基础属性数值时需要将附加技能计算进去)		
	/** 属性类型 */
	public static final int basiceAttr = 1;			//附加属性			
	public static final int addtiveAttr = 2;			//附加属性			
	public static final int hiddenAttr = 3;			//隐藏属性			
	/** 属性增加类型(1：增加具体数值   2：增加人物属性百分比  3：增加法宝基础属性百分比) */
	public static final int add_typeNum = 1;
	public static final int add_typePercent4Person = 2;
	public static final int add_typePercent4MagicWeapon = 3;
	/** 附加属性中只增加法宝属性值的技能 (不用翻译)*//**/
	public static String[] addtiveSkill = new String[]{"气血","外攻","内攻","外防","内防","暴击","闪避"};
	/** 法宝前缀名存储，方便随机法宝属性 */
	public static List<String> pre_nameList = new ArrayList<String>();
	/** 随机比率   万分比 */
	public static final int tenThousand = 10000;
	/** 新的数值公式 */
	public static float[] newColorWeight= new float[]{20, 160, 300, 400, 560, 600};
	public static float[] newLevelWeight= new float[]{20, 40, 80, 160, 300, 440, 640, 700};
	public static float[] newLevelWeight_new= new float[]{20, 40, 80, 160, 300, 440, 640, 700, 1700, 2700 ,3700, 4700, 5700 ,6700, 7700, 8700};
	/** 颜色权重(需要用此值除以100)--根据颜色读取(白、绿、蓝、紫、橙、金) */
	public static float[] colorWeight= new float[]{10, 20, 40, 60, 80, 100};
	public static float[] colorWeight4H= new float[]{33, 50, 66, 75, 83, 100};			//隐藏属性百分比用
	/**  加持权重 (需要用此值除以100)*/
	public static float aidWeight = 200;
	/** 初值权重(需要用此值除以100) */
	public static float initalWeight = 20;
	/** 成长权重(需要用此值除以100)----法宝升级属性加成计算用 */
	public static float growWeight = 80;
	/** 隐藏属性权重(需要用此值除以100) */
	public static float hiddenWeight = 15;
	/** 强化最高级别 */
	public static float starMaxValue = 20;
	/** 最大颜色值 */
	public static float maxColor = 5;
	/** 隐藏属性向下随机百分比、概率 */
	public static final int[] deductHiddenP = new int[]{80,60,40,20,0};
	public static final int[] deductProblem = new int[]{1500,2500,3000,2000,1000};			//万分比
	
	
	public static final double[] strongValues = new double[] { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50,
		60.5, 72, 84.5, 98, 112.5, 128, 144.5, 162, 180.5, 200,
		245, 269, 294 ,320, 347, 376, 429, 490, 561, 643, 739};
	
	public static String [] 品 = {Translate.凡品,Translate.下品,Translate.中品,Translate.上品,Translate.精品,Translate.极品,Translate.珍品,
		Translate.绝品, Translate.奇刃, Translate.凡兵, Translate.大道, Translate.通玄, Translate.鸿钧, Translate.法宝混沌};
	public static String [] 阶 = {Translate.坐骑一阶,Translate.坐骑二阶,Translate.坐骑三阶,Translate.坐骑四阶,Translate.坐骑五阶,Translate.坐骑六阶,
		Translate.坐骑七阶,Translate.坐骑八阶,Translate.坐骑九阶, Translate.坐骑十阶};
	public static int 法宝羽化等级 = 20;
	public static int 法宝羽化等级限制 = 220;
	public static int strongMaterialMaxNumber = 4;
	/**
	 * 总的幸运基数
	 */
	public static int TOTAL_LUCK_VALUE = 1000000;
	
	/**
	 * 白,绿,蓝,紫,橙,金
	 */
	public static int basicEap[] = {100,150,200,250,300,350};
	public static double 等级系数 = 0.05;
	public static double 加持系数 = 0.1;
	public static int 刷新隐藏属性激活所需费用 [] = {100000,300000,600000,1000000,3000000,3000000};
	public static List<String> 刷新隐藏属性所需材料 = new ArrayList<String>();
	static{
		刷新隐藏属性所需材料.add(Translate.重铸符);
		刷新隐藏属性所需材料.add(Translate.天铸符);
		刷新隐藏属性所需材料.add(Translate.神铸符);
	}
	/** 法宝出生位置偏移量 */
	public static int offsetX = 150;
	public static int offsetY = -150;
	
	/** 法宝灵气补充 */
	public static String[] lingqiProps = new String[]{Translate.次品灵石, Translate.中品灵石, Translate.极品灵石};
	public static int[] lingqiNum = new int[]{100, 200, 300};
	/** 法宝掉耐久时间---单位：分钟 */
	public static int LASTWARNTIME = 25;
	
}
