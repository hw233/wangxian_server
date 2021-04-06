package com.fy.engineserver.calculate;

import java.util.List;
import java.util.Random;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.props.IntProperty;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.util.ProbabilityUtils;

public class EquipmentStrongFlushCalculator {
	static ScriptEngine engine = null;
    static ScriptEngineManager factory = new ScriptEngineManager();
    static{
    	List<ScriptEngineFactory> ll = factory.getEngineFactories();
    	for (ScriptEngineFactory f : ll){
    		if(f.getClass().getName().equals("com.sun.script.javascript.RhinoScriptEngineFactory")){
    			engine = f.getScriptEngine();
    		}
    	}
    }
    
    static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());

	public static   int 智力 = 0;
	public static   int 耐力 = 1;
	public static   int 力量 = 2;
	public static   int 敏捷 = 3;
	public static   int 生命值 = 4;
	public static   int 内力值 = 5;
	public static   int 法术攻击 = 6;
	public static   int 物理攻击 = 7;
	public static   int 法力回复 = 8;
	public static   int 生命回复 = 9;
	public static   int 反伤几率 = 10;
	public static   int 吸血几率 = 11;
	public static   int 命中等级 = 12;
	public static   int 闪避等级 = 13;
	public static   int 暴击等级 = 14;
	public static   int 移动速度 = 15;
	public static   int 物理防御 = 16;
	public static   int 法术防御 = 17;
	public static   int 外功减伤 = 18;
	public static   int 内法减伤 = 19;

	public static final int BASE_NUM = 20;
	public static  int 强化4 = 0;
	public static  int 强化8 = 1;
	public static  int 强化12 = 2;
	public static  int 强化16 = 3;
	
	public static IntProperty[] ips = new IntProperty[]{
		new IntProperty("spellpowerB",Translate.text_2367),
		new IntProperty("constitutionB",Translate.text_2368),
		new IntProperty("strengthB",Translate.text_2369),
		new IntProperty("dexterityB",Translate.text_2370),
		new IntProperty("totalHpB",Translate.text_2371),
		new IntProperty("totalMpB",Translate.text_2372),
		new IntProperty("spellAttackIntensityB",Translate.text_2373),
		new IntProperty("meleeAttackIntensityB",Translate.text_2374),
		new IntProperty("mpRecoverBaseB",Translate.text_2375),
		new IntProperty("hpRecoverBaseB",Translate.text_2376),
		new IntProperty("ironMaidenPercent",Translate.text_2377),
		new IntProperty("hpStealPercent",Translate.text_2378),
		new IntProperty("attackRatingB",Translate.text_2379),
		new IntProperty("dodgeB",Translate.text_2380),
		new IntProperty("criticalHitB",Translate.text_2381),
		new IntProperty("speedC",Translate.text_2382),
		new IntProperty("defenceB",Translate.text_2383),
		new IntProperty("resistanceB",Translate.text_2384),
		new IntProperty("defenceD",Translate.text_2385),
		new IntProperty("resistanceD",Translate.text_2386)};
	/**
	 * 0代表对于用刀的玩家没有用的属性，1表示属性有用
	 */
	public static int []刀属性标记 = new int[]{0,1,1,0,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	
	/**
	 * 0代表对于用剑双剑的玩家没有用的属性，1表示属性有用
	 */
	public static int []剑双剑属性标记 = new int[]{1,1,0,0,1,1,1,0,1,1,1,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1};
	
	/**
	 * 0代表对于用弓的玩家没有用的属性，1表示属性有用
	 */
	public static int []弓属性标记 = new int[]{0,1,0,1,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	
	/**
	 * 0代表对于用双刀的玩家没有用的属性，1表示属性有用
	 */
	public static int []双刀属性标记 = new int[]{1,1,1,1,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	
	/**
	 * 0代表对于用皮甲的玩家没有用的属性，1表示属性有用
	 */
	public static int []皮甲属性标记 = new int[]{1,1,0,1,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	
	/**
	 * 0代表对于用布甲的玩家没有用的属性，1表示属性有用
	 */
	public static int []布甲属性标记 = new int[]{1,1,0,0,1,1,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
	
	/**
	 * 0代表对于用锁甲的玩家没有用的属性，1表示属性有用
	 */
	public static int []锁甲属性标记 = new int[]{0,1,1,0,1,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};

	public static String[][] 刀公式 = new String[][]{
		{Translate.text_2387,Translate.text_2388,Translate.text_2389,Translate.text_2390},
		{Translate.text_2391,Translate.text_2390,Translate.text_2392,Translate.text_2393},
		{Translate.text_2394,Translate.text_2389,Translate.text_2392,Translate.text_2395},
		{Translate.text_2394,Translate.text_2389,Translate.text_2392,Translate.text_2395},
		{Translate.text_2396,Translate.text_2397,Translate.text_2398,Translate.text_2399},
		{Translate.text_2400,Translate.text_2401,Translate.text_2397,Translate.text_2398},
		{Translate.text_2402,Translate.text_2403,Translate.text_2404,Translate.text_2405},
		{Translate.text_2406,Translate.text_2407,Translate.text_2408,Translate.text_2409},
		{"3","6","20","34"},
		{"4","8","25","40"},
		{"1","2","3","3"},
		{"1","2","3","3"},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2388,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{"1","2","3","3"},
		{Translate.text_2411,Translate.text_2412,Translate.text_2413,Translate.text_2414},
		{Translate.text_2415,Translate.text_2416,Translate.text_2417,Translate.text_2418},
		{"1","2","3","3"},
		{"1","2","3","3"}
	};

	public static String[][] 剑双剑公式 = new String[][]{
		{Translate.text_2391,Translate.text_2419,Translate.text_2410,Translate.text_2393},
		{Translate.text_2394,Translate.text_2419,Translate.text_2410,Translate.text_2395},
		{Translate.text_2394,Translate.text_2420,Translate.text_2390,Translate.text_2395},
		{Translate.text_2394,Translate.text_2420,Translate.text_2390,Translate.text_2395},
		{Translate.text_2400,Translate.text_2401,Translate.text_2421,Translate.text_2422},
		{Translate.text_2396,Translate.text_2397,Translate.text_2398,Translate.text_2399},
		{Translate.text_2402,Translate.text_2403,Translate.text_2404,Translate.text_2405},
		{Translate.text_2406,Translate.text_2407,Translate.text_2408,Translate.text_2409},
		{"3","6","20","34"},
		{"4","8","25","40"},
		{"1","2","3","3"},
		{"1","2","3","3"},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2388,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{"1","2","3","3"},
		{Translate.text_2423,Translate.text_2424,Translate.text_2425,Translate.text_2417},
		{Translate.text_2411,Translate.text_2412,Translate.text_2413,Translate.text_2413},
		{"1","2","3","3"},
		{"1","2","3","3"}
	};
	
	public static String[][] 弓公式 = new String[][]{
		{Translate.text_2387,Translate.text_2388,Translate.text_2389,Translate.text_2390},
		{Translate.text_2391,Translate.text_2390,Translate.text_2426,Translate.text_2395},
		{Translate.text_2394,Translate.text_2389,Translate.text_2426,Translate.text_2395},
		{Translate.text_2394,Translate.text_2389,Translate.text_2392,Translate.text_2393},
		{Translate.text_2400,Translate.text_2401,Translate.text_2397,Translate.text_2398},
		{Translate.text_2400,Translate.text_2401,Translate.text_2397,Translate.text_2398},
		{Translate.text_2402,Translate.text_2403,Translate.text_2404,Translate.text_2405},
		{Translate.text_2406,Translate.text_2407,Translate.text_2408,Translate.text_2409},
		{"3","6","20","34"},
		{"4","8","25","40"},
		{"1","2","3","3"},
		{"1","2","3","3"},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2388,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{"1","2","3","3"},
		{Translate.text_2415,Translate.text_2416,Translate.text_2417,Translate.text_2417},
		{Translate.text_2415,Translate.text_2416,Translate.text_2417,Translate.text_2417},
		{"1","2","3","3"},
		{"1","2","3","3"}
	};
	
	public static String[][] 双刀公式 = new String[][]{
		{Translate.text_2387,Translate.text_2388,Translate.text_2389,Translate.text_2390},
		{Translate.text_2391,Translate.text_2390,Translate.text_2426,Translate.text_2395},
		{Translate.text_2394,Translate.text_2389,Translate.text_2426,Translate.text_2395},
		{Translate.text_2394,Translate.text_2389,Translate.text_2392,Translate.text_2393},
		{Translate.text_2400,Translate.text_2401,Translate.text_2397,Translate.text_2398},
		{Translate.text_2400,Translate.text_2401,Translate.text_2397,Translate.text_2398},
		{Translate.text_2402,Translate.text_2403,Translate.text_2404,Translate.text_2405},
		{Translate.text_2406,Translate.text_2407,Translate.text_2408,Translate.text_2409},
		{"3","6","20","34"},
		{"4","8","25","40"},
		{"1","2","3","3"},
		{"1","2","3","3"},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2388,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{"1","2","3","3"},
		{Translate.text_2415,Translate.text_2416,Translate.text_2417,Translate.text_2413},
		{Translate.text_2415,Translate.text_2416,Translate.text_2417,Translate.text_2413},
		{"1","2","3","3"},
		{"1","2","3","3"}
	};
	
	public static String[][] 皮甲公式 = new String[][]{
		{Translate.text_2427,Translate.text_2428,Translate.text_2429,Translate.text_2430},
		{Translate.text_2431,Translate.text_2432,Translate.text_2433,Translate.text_2434},
		{Translate.text_2431,Translate.text_2432,Translate.text_2433,Translate.text_2430},
		{Translate.text_2431,Translate.text_2432,Translate.text_2430,Translate.text_2435},
		{Translate.text_2436,Translate.text_2437,Translate.text_2438,Translate.text_2439},
		{Translate.text_2440,Translate.text_2441,Translate.text_2438,Translate.text_2442},
		{Translate.text_2443,Translate.text_2444,Translate.text_2445,Translate.text_2446},
		{Translate.text_2447,Translate.text_2448,Translate.text_2449,Translate.text_2450},
		{"3","6","20","34"},
		{"4","8","25","40"},
		{"1","2","3","5"},
		{"1","2","3","5"},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2388,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{"1","2","3","5"},
		{Translate.text_2451,Translate.text_2452,Translate.text_2453,Translate.text_2454},
		{Translate.text_2451,Translate.text_2452,Translate.text_2453,Translate.text_2454},
		{"1","2","3","5"},
		{"1","2","3","5"}
	};
	
	public static String[][] 布甲公式 = new String[][]{
		{Translate.text_2427,Translate.text_2432,Translate.text_2430,Translate.text_2435},
		{Translate.text_2431,Translate.text_2428,Translate.text_2429,Translate.text_2435},
		{Translate.text_2431,Translate.text_2432,Translate.text_2429,Translate.text_2433},
		{Translate.text_2431,Translate.text_2432,Translate.text_2429,Translate.text_2433},
		{Translate.text_2436,Translate.text_2455,Translate.text_2456,Translate.text_2438},
		{Translate.text_2440,Translate.text_2441,Translate.text_2442,Translate.text_2457},
		{Translate.text_2443,Translate.text_2444,Translate.text_2445,Translate.text_2446},
		{Translate.text_2447,Translate.text_2448,Translate.text_2449,Translate.text_2450},
		{"4","8","25","40"},
		{"3","6","20","34"},
		{"1","2","3","5"},
		{"1","2","3","5"},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2388,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{"1","2","3","5"},
		{Translate.text_2458,Translate.text_2459,Translate.text_2460,Translate.text_2453},
		{Translate.text_2461,Translate.text_2462,Translate.text_2454,Translate.text_2454},
		{"1","2","3","5"},
		{"1","2","3","5"}
	};
	
	public static String[][] 锁甲公式 = new String[][]{
		{Translate.text_2427,Translate.text_2428,Translate.text_2429,Translate.text_2430},
		{Translate.text_2431,Translate.text_2432,Translate.text_2434,Translate.text_2463},
		{Translate.text_2431,Translate.text_2432,Translate.text_2430,Translate.text_2435},
		{Translate.text_2431,Translate.text_2432,Translate.text_2430,Translate.text_2435},
		{Translate.text_2436,Translate.text_2437,Translate.text_2438,Translate.text_2439},
		{Translate.text_2440,Translate.text_2441,Translate.text_2438,Translate.text_2442},
		{Translate.text_2443,Translate.text_2444,Translate.text_2445,Translate.text_2446},
		{Translate.text_2447,Translate.text_2448,Translate.text_2449,Translate.text_2450},
		{"3","6","20","34"},
		{"4","8","25","40"},
		{"1","1","3","5"},
		{"1","1","3","5"},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2391,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{Translate.text_2388,Translate.text_2389,Translate.text_2410,Translate.text_2392},
		{"1","1","3","5"},
		{Translate.text_2461,Translate.text_2462,Translate.text_2454,Translate.text_2464},
		{Translate.text_2461,Translate.text_2462,Translate.text_2454,Translate.text_2464},
		{"1","1","3","5"},
		{"1","1","3","5"}
	};

	/**
	 * 返回值 0没用 1有用
	 * @param 类型
	 * @param 属性
	 * @param 强化
	 * @param 公式结果
	 * @param 随机结果
	 * @return
	 */
	public synchronized static int 装备强化有用的属性(int 类型,int 属性位置){
		int[] 属性标记  = null;
		if(类型== 0){
			属性标记 = 刀属性标记;
		}else if(类型== 1){
			属性标记 = 剑双剑属性标记;
		}else if(类型== 2){
			属性标记 = 弓属性标记;
		}else if(类型== 3){
			属性标记 = 双刀属性标记;
		}else if(类型== 4){
			属性标记 = 皮甲属性标记;
		}else if(类型== 5){
			属性标记 = 布甲属性标记;
		}else if(类型== 6){
			属性标记 = 锁甲属性标记;
		}
		return 属性标记[属性位置];
	}
	
	/**
	 * 返回值代表颜色，0白，1绿，2蓝，3紫，4橙
	 * @param 类型
	 * @param 属性
	 * @param 强化
	 * @param 公式结果
	 * @param 随机结果
	 * @return
	 */
	public synchronized static int 装备强化属性颜色(int 类型,int 属性位置,int 强化,int 公式结果,int 随机结果){
		int[] 属性标记  = null;
		if(类型== 0){
			属性标记 = 刀属性标记;
		}else if(类型== 1){
			属性标记 = 剑双剑属性标记;
		}else if(类型== 2){
			属性标记 = 弓属性标记;
		}else if(类型== 3){
			属性标记 = 双刀属性标记;
		}else if(类型== 4){
			属性标记 = 皮甲属性标记;
		}else if(类型== 5){
			属性标记 = 布甲属性标记;
		}else if(类型== 6){
			属性标记 = 锁甲属性标记;
		}
		int color = 属性标记[属性位置];
		if(color == 0){
			return 0;
		}
		int min = 0;
		int max = 0;
		if(强化 == 0){
			max = 2*公式结果;
		}else if(强化 == 1){
			min = (int)(公式结果*50/100);
			max = (int)(公式结果*(50+100)/100);
		}else if(强化 == 2){
			min = (int)(公式结果*70/100);
			max = (int)(公式结果*(30+100)/100);
		}else if(强化 == 3){
			min = (int)(公式结果*70/100);
			max = (int)(公式结果*(30+100)/100);
		}
//		ArticleManager.logger.warn("min:"+min+"  max:"+max+"  随机结果:"+随机结果+"  类型:"+类型+"  属性位置:"+属性位置+"  强化:"+强化+"  公式结果:"+公式结果);
		if(ArticleManager.logger.isWarnEnabled())
			ArticleManager.logger.warn("min:{}  max:{}  随机结果:{}  类型:{}  属性位置:{}  强化:{}  公式结果:{}", new Object[]{min,max,随机结果,类型,属性位置,强化,公式结果});
		if(max == 0 || max <= min){
			return 4;
		}
		if(max - min < 5){
			return 4 - (max - 随机结果);
		}
		float result = ((float)(随机结果 - min))/(max - min);
		if(result >= 0.8){
			return 4;
		}
		if(result >= 0.6){
			return 3;
		}
		if(result >= 0.4){
			return 2;
		}
		if(result >= 0.2){
			return 1;
		}
		if(result >= 0.0){
			return 0;
		}
		return 0;
	}
	
	/**
	 * 类型
		0 = 刀公式;

		1 = 剑双剑公式;

		2 = 弓公式;

		3 = 双刀公式;

		4 = 皮甲公式;

		5 = 布甲公式;

		6 = 锁甲公式;

属性为随机到的属性，对应数值为上面那些文字定义的数值，也就是二维数组的行下标，强化为列下标
	 * @param 类型
	 * @param 属性
	 * @param 强化
	 * @param 物品等级
	 * @param 品质
	 * @param 部件系数
	 * @return
	 * @throws Exception
	 */
	public synchronized static int 装备强化属性刷新公式(int 类型,int 属性位置,int 强化,int 物品等级,int 品质,int 部件系数) throws Exception{
		String 公式[][] = null;
		if(类型== 0){
			公式 = 刀公式;
		}else if(类型== 1){
			公式 = 剑双剑公式;
		}else if(类型== 2){
			公式 = 弓公式;
		}else if(类型== 3){
			公式 = 双刀公式;
		}else if(类型== 4){
			公式 = 皮甲公式;
		}else if(类型== 5){
			公式 = 布甲公式;
		}else if(类型== 6){
			公式 = 锁甲公式;
		}
		
		String formula = 公式[属性位置][强化];
		
		engine.put(Translate.text_2465, 物品等级);
		engine.put(Translate.text_2466, 品质);
		engine.put(Translate.text_2467, 部件系数);
		
		Compilable compile = (Compilable)engine;
		
		CompiledScript cs = null;
		try {
			cs = compile.compile(formula);
			Number num = (Number)cs.eval();
			return num.intValue();
		} catch (Exception e) {
			String errorInfo = Translate.text_2468+formula+"]";
			throw new Exception(errorInfo , e);
		}
	}
	
	/**
	 * 随机出来的颜色，必须大于等于指定的颜色，颜色为0时表示没有颜色限制
	 * 
	 * @param 强化
	 * @param 公式结果
	 * @param 颜色
	 * @return
	 */
	public static int 装备随机数值(int 强化,int 公式结果,int 颜色){
		int min = 0;
		int max = 0;
		if(强化 == 0){
			max = 2*公式结果;
		}else if(强化 == 1){
			min = (int)(公式结果*50/100);
			max = (int)(公式结果*(50+100)/100);
		}else if(强化 == 2){
			min = (int)(公式结果*70/100);
			max = (int)(公式结果*(30+100)/100);
		}else if(强化 == 3){
			min = (int)(公式结果*70/100);
			max = (int)(公式结果*(30+100)/100);
		}
		if(min > max){
			return 0;
		}
		int ss = 0;
		if(颜色 == 0){
			ss = min;	
		} else {
			if(max - min < 5){
				ss = max + 颜色 - 4;
			}else{
				int yushu = (max - min)* 颜色%5;
				ss = (int)(min + (max - min)* 颜色/5.0);
				if(yushu != 0){
					ss += 1;
				}
			}
			if(ss <= 0){
				ss = 1;
			}
			return ss;
		}
		int randomValue = ProbabilityUtils.randomGaussian(random,ss, max);
		if(randomValue <= 0){
			randomValue = 1;
		}
		return randomValue;
	}
	
	/**
	 * 颜色范围从0到4，0表示没有限制
	 * 
	 * @param e
	 * @param ee
	 * @param color
	 * @return
	 * @throws Exception
	 */
	public synchronized static int[] 刷新装备属性(Equipment e,EquipmentEntity ee,int[]颜色要求) throws Exception{
		
		return null;
	}
	
	public static int 得到装备类型(Equipment e){
		
		return 0;
	}
	/**
	 * 品质系数:白10，绿20，蓝30，紫40，橙50
	 * @param e
	 * @return
	 */
	public static int 得到装备品质(Equipment e){
		if(e.getColorType() == 0){
			return 10;
		}else if(e.getColorType() == 1){
			return 20;
		}else if(e.getColorType() == 2){
			return 30;
		}else if(e.getColorType() == 3){
			return 40;
		}else if(e.getColorType() == 4){
			return 50;
		}
		return 0;
	}
	/**
	 * 部件系数:武器15，头12，衣服12，肩膀10，手10，鞋10
	 * @param e
	 * @return
	 */
	public static int 得到装备部件系数(Equipment e){
		//装备类型，0武器,1:头盔,2护肩,3衣服,4护手,5靴子,6首饰,7项链,8戒指。
		if(e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_weapon){
			return 15;
		}else if(e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_head){
			return 12;
		}else if(e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_body){
			return 12;
		}else if(e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_shoulder){
			return 10;
		}else if(e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_hand){
			return 10;
		}else if(e.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_foot){
			return 10;
		}
		return 0;
	}
	
	public static int 得到装备的某种强化属性对应的位置(IntProperty ip){
		for(int i = 0; i < ips.length; i++){
			IntProperty ipss = ips[i];
			if(ipss != null && ipss.getFieldName() != null && ip != null && ipss.getFieldName().equals(ip.getFieldName())){
				return i;
			}
		}
		return 0;
	}

}
