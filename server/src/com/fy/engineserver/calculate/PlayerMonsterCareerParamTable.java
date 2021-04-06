package com.fy.engineserver.calculate;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.fy.engineserver.datasource.language.Translate;

/**
 * 怪物职业参数表
 * 
 *
 */
public class PlayerMonsterCareerParamTable {

	// create a script engine manager
    static ScriptEngineManager factory = new ScriptEngineManager();
    // create a JavaScript engine
    static ScriptEngine engine = factory.getEngineByName("JavaScript");
    
	//职业数据
	public static String monsterCareerNames[] = new String[]{Translate.text_2485,Translate.text_2486,Translate.text_2487,Translate.text_2488,Translate.text_2489};
	
	public static String monsterTypeNames[] = new String[]{Translate.text_2490,Translate.text_2491,Translate.text_2492};
	
	public static String monsterRaceNames[] = new String[]{Translate.text_2493,Translate.text_2494,Translate.text_2495};
	
	
	//参数数据
	public static String monsterParamNames[] = new String[]{
		Translate.text_2496,
		Translate.text_2497,
		Translate.text_2498,
		Translate.text_2499,
		Translate.text_2500,
		Translate.text_2501,
		Translate.text_2502,
		Translate.text_2503,
		Translate.text_2504,
		Translate.text_2505,
		Translate.text_2506,
		Translate.text_2507,
		Translate.text_2508,
		Translate.text_2509,
		Translate.text_2510,
		Translate.text_2511,
		Translate.text_2512,
		Translate.text_2513,
		Translate.text_2514,
		Translate.text_2515,
		Translate.text_2516,
		Translate.text_2517,
		Translate.text_2518,
	};
	
	public static final String 怪物各种属性[] = new String[]{
		Translate.text_2519,
		Translate.text_2520,
		Translate.text_2521,
		Translate.text_2522,
		Translate.text_2523,
		Translate.text_2524,
		Translate.text_2525,
		Translate.text_2476,
		Translate.text_2477,
		Translate.text_2478,
		Translate.text_2479,
		Translate.text_979,
		Translate.text_2526
	};
	
	//职业参数
	public static float monsterCareerParams[][] = new float[][]{
		{6,4,6,5,5},
		{1.6f,1.3f,1.3f,1.5f,1.5f},
		{2.1f,1.8f,1.8f,2f,2f},
		{2.3f,2.6f,2.6f,3f,3f},
		{1.7f,1.4f,1.4f,1.6f,1.6f},
		{2.2f,1.9f,1.9f,2.1f,2.1f},
		{3.4f,2.8f,2.8f,3.2f,3.2f},
		{1.8f,1.5f,1.5f,1.7f,1.7f},
		{2.3f,2f,2f,2.2f,2.2f},
		{3.6f,3f,3f,3.4f,3.4f},
		{3.5f,3,3,2.5f,2.5f},
		{4,3.5f,3.5f,3,3},
		{3.5f,3,3,2.5f,2.5f},
		{4,6,4,5,5},
		{1.5f,1.5f,1.5f,1.5f,1.5f},
		{2f,2f,2f,2f,2f},
		{3,5,3,4,4},
		{5,6,4,5,5},
		{0,1,1,0,0.5f},
		{10,6,6,8,8},
		{12,8,8,10,10},
		{15,10,10,12,12},
		{20,12,12,16,16}
	};
	
	//公式
	public static HashMap<String,HashMap<String,HashMap<String,String>>> monsterFormulaMap = new HashMap<String,HashMap<String,HashMap<String,String>>>();
	
	static {
		
		for(int i = 0 ; i < monsterRaceNames.length ; i++){
			HashMap<String,HashMap<String,String>> map = new HashMap<String,HashMap<String,String>>();
			monsterFormulaMap.put(monsterRaceNames[i], map);
			
			for(int j = 0 ; j < monsterTypeNames.length ; j++){
				LinkedHashMap<String,String> m = new LinkedHashMap<String,String>();
				map.put(monsterTypeNames[j], m);
				
				if(i == 0 && j == 0){
					m.put(Translate.text_2519, Translate.text_2527);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2529);
					m.put(Translate.text_2522,Translate.text_2530);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2506);
					m.put(Translate.text_2476,Translate.text_2531);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
					
				}else if(i == 0 && j == 1){
					
					m.put(Translate.text_2519, Translate.text_2533);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2534);
					m.put(Translate.text_2522,Translate.text_2535);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2506);
					m.put(Translate.text_2476,Translate.text_2536);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}else if(i == 0 && j == 2){
					
					m.put(Translate.text_2519, Translate.text_2537);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2538);
					m.put(Translate.text_2522,Translate.text_2539);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2540);
					m.put(Translate.text_2476,Translate.text_2536);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}if(i == 1 && j == 0){
					
					m.put(Translate.text_2519, Translate.text_2541);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2542);
					m.put(Translate.text_2522,Translate.text_2543);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2507);
					m.put(Translate.text_2476,Translate.text_2531);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}else if(i == 1 && j == 1){
					
					m.put(Translate.text_2519, Translate.text_2544);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2545);
					m.put(Translate.text_2522,Translate.text_2546);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2507);
					m.put(Translate.text_2476,Translate.text_2536);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}else if(i == 1 && j == 2){
					
					m.put(Translate.text_2519, Translate.text_2547);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2548);
					m.put(Translate.text_2522,Translate.text_2549);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2550);
					m.put(Translate.text_2476,Translate.text_2551);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}if(i == 2 && j == 0){
					
					m.put(Translate.text_2519, Translate.text_2552);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2553);
					m.put(Translate.text_2522,Translate.text_2554);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2508);
					m.put(Translate.text_2476,Translate.text_2555);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}else if(i == 2 && j == 1){
					
					m.put(Translate.text_2519, Translate.text_2556);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2557);
					m.put(Translate.text_2522,Translate.text_2558);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2508);
					m.put(Translate.text_2476,Translate.text_2536);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}else if(i == 2 && j == 2){
					m.put(Translate.text_2519, Translate.text_2559);
					m.put(Translate.text_2520,Translate.text_2528);
					m.put(Translate.text_2521,Translate.text_2560);
					m.put(Translate.text_2522,Translate.text_2561);
					m.put(Translate.text_2523,Translate.text_2529);
					m.put(Translate.text_2524,Translate.text_2530);
					m.put(Translate.text_2382, "75");
					m.put(Translate.text_2525,Translate.text_2562);
					m.put(Translate.text_2476,Translate.text_2551);
					m.put(Translate.text_2477,"0");
					m.put(Translate.text_2478,"0");
					m.put(Translate.text_2479,"0");
					m.put(Translate.text_979,"0");
					m.put(Translate.text_2526,Translate.text_2532);
				}
			}
		}
		
	}
	
	
	
	//暴击
	public static final int criticalRateBaseLine = -3;
	public static final int criticalRateTable[] = new int[]{1,2,3,4,5,6,10};
	
	//命中
	public static final int hitRateBaseLine = -7;
	public static final int hitRateTable[] = new int[]{10,30,50,73,93,93,94,95,96,98,99};
	
	//闪避
	public static final int jookRateBaseLine = -3;
	public static final int jookRateTable[] = new int[]{0,1,2,3,4,5,6,7,8,9};
	
	/**
	 * 计算怪物的属性值
	 * @param level 等级
	 * @param monsterCareer 职业
	 * @param monsterType 类型
	 * @param monsterRace 种族
	 * @param fieldName 属性
	 * @return
	 * @throws Exception
	 */
	public static int monsterCalculate(int level,int monsterCareer,int monsterType,int monsterRace,String fieldName) throws Exception{
		engine.put(Translate.text_395, level);
		for(int i = 0 ; i < monsterParamNames.length ; i++){
			float f = monsterCareerParams[i][monsterCareer];
			engine.put(monsterParamNames[i], f);
		}
		
		String formula = monsterFormulaMap.get(monsterRaceNames[monsterRace]).get(monsterTypeNames[monsterType]).get(fieldName);
		if(formula == null){
			throw new Exception("怪物计算公式不存在！种族：" + monsterRaceNames[monsterRace] + "，类型："+monsterTypeNames[monsterType]+"，属性：" + fieldName);
		}
		Compilable compile = (Compilable)engine;
		CompiledScript cs = compile.compile(formula);
		Number num = (Number)cs.eval();
		return num.intValue();
	}
	
}
