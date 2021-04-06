package com.fy.engineserver.sprite.pet;

import java.util.Map;
import java.util.Random;

import com.fy.engineserver.combat.CombatCaculator;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill2.GenericBuff;
import com.fy.engineserver.sprite.pet.Pet.PetRandomType;
import com.fy.engineserver.sprite.pet2.Pet2PropCalc;
import com.fy.engineserver.util.ProbabilityUtils;

public class PetPropertyUtility {
	
	
	
	public static final int 属性初值最小 = 50;
	public static final int 属性初值最大 = 100;
	public static final int 资质初值最小 = 80;
	public static final int 资质初值最大 = 120;
	
	public static final int 物攻型 = 0;
	public static final int 敏捷型 = 1;
	public static final int 法攻型 = 2;
	public static final int 毒伤型 = 3;
	
	public static final int	MN力 = 0;
	public static final int	MN灵 = 1;
	public static final int	MN耐 = 2;
	public static final int	MN命 = 3;
	public static final int	MN闪 = 4;
	public static final int	MN暴 = 5;
	public static final int	MN定 = 6;
	
//	力量,灵力,耐力,身法,定力
	public static final int 力量 = 0;
	public static final int 灵力 = 1;
	public static final int 耐力 = 2;
	public static final int 身法 = 3;
	public static final int 定力 = 4;
	public static final int 力量资质 = 5;
	public static final int 灵力资质 = 6;
	public static final int 耐力资质 = 7;
	public static final int 身法资质 = 8;
	public static final int 定力资质 = 9;
	
	
//	0力量1灵力2身法3耐力4定力
	public static final int 加点力量 = 0;
	public static final int 加点灵力 = 1;
	public static final int 加点身法 = 2;
	public static final int 加点耐力 = 3;
	public static final int 加点定力 = 4;

	
	public static final int 外功 = 0;
	public static final int 内法 = 1;
	public static final int 血量 = 2;
	public static final int 命中 = 3;
	public static final int 闪躲 = 4;
	public static final int 暴击 = 5;
	public static final int 外防 = 6;
	public static final int 内防 = 7;
	
	/**
	 * 扩大1000
	 */
//	public static final int[] 一代是否变异概率表 = new int[]{1,50,100};//千分率
	public static int[] 一代是否变异概率表 = new int[]{50,100,200,1000};//千分率
	
	/**
	 * 扩大1000
	 */
	public static int[][] 一代变异等级概率表 = new int[][]{

		{200,300,200,150,100},
		{200,300,200,150,100},
		{200,300,200,150,100},
		{200,300,200,150,100}
	};
	/**
	 * 千分率，按携带等级区分
	 */
//	public static final int[]  二代是否变异概率表 = new int[]{50,100,200};//千分率
	public static final int[]  二代是否变异概率表 = new int[]{100,150,250};//千分率
	
	/**
	 * 千分率
	 */
	public static final int[][] 二代变异等级概率表 = new int[][]{
		
		{200,300,200,150,100},
		{200,300,200,150,100},
		{200,300,200,150,100}
	};
	
	static Random random = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	public static boolean 随机获得一代是否变异(int 稀有度) {
		int ran = ProbabilityUtils.randomGaussian(random,0, 1000);
		if(ran < 一代是否变异概率表[稀有度]) {
			return true;
		}
		return false;
	}	
	
	public static boolean 随机获得二代是否变异(int breededTimesA, int breededTimesB) {
		try {
			int ran = ProbabilityUtils.randomGaussian(random,0, 1000);
			int tempA = breededTimesA >= 二代是否变异概率表.length ? (二代是否变异概率表.length-1) : breededTimesA;
			int tempB = breededTimesB >= 二代是否变异概率表.length ? (二代是否变异概率表.length-1) : breededTimesB;
			int A变异率 = 二代是否变异概率表[tempA];
			int B变异率 = 二代是否变异概率表[tempB];
			int 变异率 = (A变异率+ B变异率)/2;
			if(ran < 变异率) {
				return true;
			}
			return false;
		} catch(Exception e) {
			PetManager.logger.error("[随机获得二代是否变异]",e);
		}
		return false;
	}
	
	public static int 随机获得一代变异等级(int 稀有度) {
		int 等级概率[] =  一代变异等级概率表[稀有度];
		int k = 0;
		int ran = ProbabilityUtils.randomGaussian(random,0, 1000);
		for(int i=0; i<等级概率.length; i++) {
			k += 等级概率[i];
			if(ran < k) {
				return i;
			}
		}
		return 0;
	}
	
	public static int 随机获得二代变异等级(int 稀有度) {
		int 等级概率[] =  二代变异等级概率表[稀有度];
		int k = 0;
		int ran = ProbabilityUtils.randomGaussian(random,0, 1000);
		for(int i=0; i<等级概率.length; i++) {
			k += 等级概率[i];
			if(ran < k) {
				return i;
			}
		}
		return 0;
	}
	
	//四个性格
	public static int 随机获得性格() {
		return new java.util.Random().nextInt(4);
	}
	
	
//		忠诚	精明	谨慎	狡诈
//	忠诚	0.5	0.166666667	0.166666667	0.166666667
	
//	0.5	0.166666667	0.166666667	0.166666667
//	0.166666667	0.5	0.166666667	0.166666667
//	0.166666667	0.166666667	0.5	0.166666667
//	0.166666667	0.166666667	0.166666667	0.5

	public static double[][] 技能出现几率 = new double[][]{
		
		{0.5 ,0.166666667 ,0.166666667 ,0.166666667},
		{0.166666667,0.5,0.166666667,0.166666667},
		{0.166666667,0.166666667,0.5,0.166666667},
		{0.166666667,0.166666667,0.166666667,0.5}
	};
	
//	技能栏位置	技能学习成功概率	技能遗忘成功率
//	1	0.50,0.75 
//	2	0.50,0.75 
//	3	0.50,0.75 
//	4	0.50,0.75 
//	5	0.50,0.75 
//	6	0.50,0.75 
//	7	0.50,0.75 
//	8	0.50,0.75 
//	9	0.50,0.75 
//	10	0.50,0.75 
	/**
	 * {技能栏位置，{副宠不丢失概率，主宠不丢失概率}}
	 */
	public static double[][] 技能合体概率 = new double[][]{
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75},
		{0.50,0.75}
	};
	
//	技能栏位置	技能学习成功概率	技能遗忘成功率
//			0.5 	0.75
//			0.47	0.72
//			0.44 	0.69
//			0.41	0.66
//			0.38 	0.63
//			0.34 	0.59
//			0.3 	0.55
//			0.26 	0.51
//			0.22 	0.47
//			0.18 	0.43
	/**
	 * {技能栏位置，{副宠不丢失概率，主宠不丢失概率}}
	 */
	public static double[][] 新服技能合体概率 = new double[][]{
		{0.50,0.75},
		{0.47,0.72},
		{0.44,0.69},
		{0.41,0.66},
		{0.38,0.63},
		{0.34,0.59},
		{0.30,0.55},
		{0.26,0.51},
		{0.22,0.47},
		{0.18,0.43}
	};
	
	
	
	
	
	/*******************************基础属性配置表**************************************************/
	
	public static int 携带等级[] = new int[]{1,45,90,135,180,225};
	
	public static int 获得携带等级索引(int trainLevel) {
		for(int i=0; i<携带等级.length; i++) {
			if(trainLevel == 携带等级[i]) {
				return i;
			}
		}
		return 0;
	}
	
	
	/**
	 * { 类型(力量，敏捷等)
	 *,{职业 (外功，敏捷，内法，毒伤)}
	 * }
	 */
	private static int[][] 得到显示属性初值A = {
		{166,110,27,27},
		{27,27,166,138},
		{55,166,55,83},
		{110,55,83,83},
		{55,55,83,83}
	};
	
	
	/**
	 * {携带等级(1,45){稀有度(随处,百里)}}
	 */
	public static final int[][] 宠物成长初值 = new int[][] {
		{20,30,40,50},
		{30,45,60, 75},
		{40,60,80, 100},
		{50,75,100, 125},
		{60,90,120, 150},
		{70,105,140, 175},
	};
	
	
	public static int 根据宠物得到成长初值(Pet pet){
		
		int 稀有度 = pet.getRarity();
//		int trainLevel = pet.getTrainLevel();
		int trainLevel = pet.getRealTrainLevel();
		int 携带等级Index = 获得携带等级索引(trainLevel);
		
		return 宠物成长初值[携带等级Index][稀有度];
	}
	
	/**
	 * 扩大了100倍
	 */
//	public static int[] 宠物品质稀有度加成 = new int[]{60,80,100};
	public static int[] 宠物品质稀有度加成 = new int[]{60,120,240, 300};
	
	
	/**
	 * 扩大了100倍
	 */
	public static final int[] 几代加成表 = new int[]{0, 50};
	
	/**
	 * 扩大了100倍
	 */
	public static final int[] 变异加成表 = new int[]{0,10,20,30,40,50};
	
	/**
	 * 扩大了100倍
	 */
	public static int[] 成长率加成表 = new int[]{0, 10, 20, 30, 40};
	
	
	/**
	 * 扩大1000倍
	 *,	{属性类型
	 *,		{职业类型(外功，敏捷，内法，毒伤)}
	 *,	}
	 */
	public static final int[][] 属性资质初值表 = new int[][]{
		
		{875,1138,4200,5600},
		{5250,4550,700,1120},
		{225,65,180,160},
		{14063,24375,15000,20000},
		{1930,1195,588,784}
		
	};
	
	/**
	 * 扩大100倍的值
	 * @param pet
	 * @return
	 */
	public static int 获得品阶加成(Pet pet) {
		int lv = pet.getStarClass();
		
		if(lv <= 10){
			return lv*5 /2;
		}else{
			return lv*lv/2 /2;
		}
	
	}
	
	/************************力量资质计算*********************************************/
	
	/**
	 * 扩大100
	 * @param pet  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static long 得到力量初值(Pet pet,boolean real,int maxOrMin){
		
		int career = pet.getCareer();
		long 力量初值;
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		long 力量初值0  = 1l*宠物属性初值[携带等级Index][力量][career];
		
		if(maxOrMin ==0){
			力量初值 = 力量初值0 * 属性初值最大;
		}else if(maxOrMin == 1){
			力量初值 = 力量初值0 * 属性初值最小;
		}else{
			力量初值 = (long) (力量初值0* pet.getRandom(real, PetRandomType.随机力量));
		}
		
		return 力量初值;
	}
	
	public static int 计算力量值A(Pet pet) {

		int 初值  =(int)Math.rint(1.0*得到力量初值(pet,true,2)/100);
		int 等级属性点加成  = (pet.getLevel() -1) * 升级属性点分配表[力量][pet.getCareer()];
		return 初值 + 等级属性点加成;
	}
	

	public static int 计算力量值(Pet pet) {

		int base = 计算力量值A(pet);
		base += Pet2PropCalc.getInst().calcBuff(pet, GenericBuff.ATT_LiLiang, base);
		int ret = base + pet.getStrengthS() + Pet2PropCalc.getInst().calcStr(pet);
		return Math.max(0, ret);
	}
	
	/**
	 * 
	 * @param pet 
	 * @param real
	 * @param maxOrMin  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static int 得到实际力量资质(Pet pet,boolean real,int maxOrMin){
		

		int career = pet.getCareer();
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		int 稀有度 = pet.getRarity();
//		携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
		long 资质初值0 = 0 ;
		if(career == 物攻型){
			资质初值0 = 1l*宠物属性资质初值修罗[携带等级Index][稀有度][力量];
		}else if(career == 敏捷型){
			资质初值0 = 1l*宠物属性资质初值影魅[携带等级Index][稀有度][力量];
		}else if(career == 法攻型){
			资质初值0 = 1l*宠物属性资质初值仙心[携带等级Index][稀有度][力量];
		}else if(career == 毒伤型){
			资质初值0 = 1l*宠物属性资质初值九黎[携带等级Index][稀有度][力量];
		}
		 
		long 资质初值;
		
		if(maxOrMin ==0){
			资质初值 = 资质初值0 * 资质初值最大;
		}else if(maxOrMin == 1){
			资质初值 = 资质初值0 * 资质初值最小;
		}else{
			资质初值 = (long) (资质初值0* pet.getRandom(real, PetRandomType.随机力量资质));
		}
		
		double 力量资质 = 1.0*资质初值/100;
		return (int)Math.rint(力量资质)+Pet2PropCalc.getInst().calcLinLi(pet);
	}
	
	/************************身法资质计算*********************************************/
	
	/**
	 * 扩大100
	 * @param pet  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static long 得到身法初值(Pet pet,boolean real,int maxOrMin){
		
		int career = pet.getCareer();
		long 身法初值;
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		long 身法初值0  = 1l*宠物属性初值[携带等级Index][身法][career];
		
		if(maxOrMin ==0){
			身法初值 = 身法初值0 * 属性初值最大;
		}else if(maxOrMin == 1){
			身法初值 = 身法初值0 * 属性初值最小;
		}else{
			身法初值 = (long) (身法初值0* pet.getRandom(real, PetRandomType.随机身法));
		}
		
		return 身法初值;
	}
	
	public static int 计算身法值A(Pet pet) {

		int 初值  =(int)Math.rint(1.0*得到身法初值(pet,true,2)/100);
		int 等级属性点加成  = (pet.getLevel() -1) * 升级属性点分配表[身法][pet.getCareer()];
		return 初值 + 等级属性点加成;
	}
	

	public static int 计算身法值(Pet pet) {

		int base = 计算身法值A(pet);
		base += Pet2PropCalc.getInst().calcBuff(pet, GenericBuff.ATT_ShenFa, base);
		int ret = base + pet.getDexterityS() + Pet2PropCalc.getInst().calcShenFa(pet);
		return Math.max(0, ret);
	}
	
	/**
	 * 
	 * @param pet 
	 * @param real
	 * @param maxOrMin  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static int 得到实际身法资质(Pet pet,boolean real,int maxOrMin){
		

		int career = pet.getCareer();
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		int 稀有度 = pet.getRarity();
//		携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
		long 资质初值0 = 0 ;
		if(career == 物攻型){
			资质初值0 = 1l*宠物属性资质初值修罗[携带等级Index][稀有度][身法];
		}else if(career == 敏捷型){
			资质初值0 = 1l*宠物属性资质初值影魅[携带等级Index][稀有度][身法];
		}else if(career == 法攻型){
			资质初值0 = 1l*宠物属性资质初值仙心[携带等级Index][稀有度][身法];
		}else if(career == 毒伤型){
			资质初值0 = 1l*宠物属性资质初值九黎[携带等级Index][稀有度][身法];
		}
		 
		long 资质初值;
		
		if(maxOrMin ==0){
			资质初值 = 资质初值0 * 资质初值最大;
		}else if(maxOrMin == 1){
			资质初值 = 资质初值0 * 资质初值最小;
		}else{
			资质初值 = (long) (资质初值0* pet.getRandom(real, PetRandomType.随机身法资质));
		}
		
		double 身法资质 = 1.0*资质初值/100;
		return (int)Math.rint(身法资质) + +Pet2PropCalc.getInst().calcShenFa(pet);
	}

	
	/************************灵力资质计算*********************************************/
	
	/**
	 * 扩大100
	 * @param pet  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static long 得到灵力初值(Pet pet,boolean real,int maxOrMin){
		
		int career = pet.getCareer();
		long 灵力初值;
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		long 灵力初值0  = 1l*宠物属性初值[携带等级Index][灵力][career];
		
		if(maxOrMin ==0){
			灵力初值 = 灵力初值0 * 属性初值最大;
		}else if(maxOrMin == 1){
			灵力初值 = 灵力初值0 * 属性初值最小;
		}else{
			灵力初值 = (long) (灵力初值0* pet.getRandom(real, PetRandomType.随机灵力));
		}
		
		return 灵力初值;
	}
	
	public static int 计算灵力值A(Pet pet) {

		int 初值  =(int)Math.rint(1.0*得到灵力初值(pet,true,2)/100);
		int 等级属性点加成  = (pet.getLevel() -1) * 升级属性点分配表[灵力][pet.getCareer()];
		return 初值 + 等级属性点加成;
	}
	

	public static int 计算灵力值(Pet pet) {

		int base = 计算灵力值A(pet);
		base += Pet2PropCalc.getInst().calcBuff(pet, GenericBuff.ATT_LinLi, base);
		int ret = base + pet.getSpellpowerS() + Pet2PropCalc.getInst().calcLinLi(pet);
		return Math.max(0, ret);
	}
	
	/**
	 * 
	 * @param pet 
	 * @param real
	 * @param maxOrMin  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static int 得到实际灵力资质(Pet pet,boolean real,int maxOrMin){
		

		int career = pet.getCareer();
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		int 稀有度 = pet.getRarity();
//		携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
		long 资质初值0 = 0 ;
		if(career == 物攻型){
			资质初值0 = 1l*宠物属性资质初值修罗[携带等级Index][稀有度][灵力];
		}else if(career == 敏捷型){
			资质初值0 = 1l*宠物属性资质初值影魅[携带等级Index][稀有度][灵力];
		}else if(career == 法攻型){
			资质初值0 = 1l*宠物属性资质初值仙心[携带等级Index][稀有度][灵力];
		}else if(career == 毒伤型){
			资质初值0 = 1l*宠物属性资质初值九黎[携带等级Index][稀有度][灵力];
		}
		 
		long 资质初值;
		
		if(maxOrMin ==0){
			资质初值 = 资质初值0 * 资质初值最大;
		}else if(maxOrMin == 1){
			资质初值 = 资质初值0 * 资质初值最小;
		}else{
			资质初值 = (long) (资质初值0* pet.getRandom(real, PetRandomType.随机灵力资质));
		}
		
		double 灵力资质 = 1.0*资质初值/100;
		return (int)Math.rint(灵力资质)+Pet2PropCalc.getInst().calcLinLi(pet);
	}

	/************************耐力资质计算*********************************************/
	
	
	/**
	 * 扩大100
	 * @param pet  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static long 得到耐力初值(Pet pet,boolean real,int maxOrMin){
		
		int career = pet.getCareer();
		long 耐力初值;
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		long 耐力初值0  = 1l*宠物属性初值[携带等级Index][耐力][career];
		
		if(maxOrMin ==0){
			耐力初值 = 耐力初值0 * 属性初值最大;
		}else if(maxOrMin == 1){
			耐力初值 = 耐力初值0 * 属性初值最小;
		}else{
			耐力初值 = (long) (耐力初值0* pet.getRandom(real, PetRandomType.随机耐力));
		}
		
		return 耐力初值;
	}
	
	public static int 计算耐力值A(Pet pet) {

		int 初值  =(int)Math.rint(1.0*得到耐力初值(pet,true,2)/100);
		int 等级属性点加成  = (pet.getLevel() -1) * 升级属性点分配表[耐力][pet.getCareer()];
		return 初值 + 等级属性点加成;
	}
	

	public static int 计算耐力值(Pet pet) {

		int base = 计算耐力值A(pet);
		base += Pet2PropCalc.getInst().calcBuff(pet, GenericBuff.ATT_NaiLi, base);
		int ret =  base + pet.getConstitutionS() + Pet2PropCalc.getInst().calcNaiLi(pet);
		return Math.max(0, ret);
	}
	
	/**
	 * 
	 * @param pet 
	 * @param real
	 * @param maxOrMin  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static int 得到实际耐力资质(Pet pet,boolean real,int maxOrMin){
		

		int career = pet.getCareer();
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		int 稀有度 = pet.getRarity();
//		携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
		long 资质初值0 = 0 ;
		if(career == 物攻型){
			资质初值0 = 1l*宠物属性资质初值修罗[携带等级Index][稀有度][耐力];
		}else if(career == 敏捷型){
			资质初值0 = 1l*宠物属性资质初值影魅[携带等级Index][稀有度][耐力];
		}else if(career == 法攻型){
			资质初值0 = 1l*宠物属性资质初值仙心[携带等级Index][稀有度][耐力];
		}else if(career == 毒伤型){
			资质初值0 = 1l*宠物属性资质初值九黎[携带等级Index][稀有度][耐力];
		}
		 
		long 资质初值;
		
		if(maxOrMin ==0){
			资质初值 = 资质初值0 * 资质初值最大;
		}else if(maxOrMin == 1){
			资质初值 = 资质初值0 * 资质初值最小;
		}else{
			资质初值 = (long) (资质初值0* pet.getRandom(real, PetRandomType.随机耐力资质));
		}
		
		double 耐力资质 = 1.0*资质初值/100;
		return (int)Math.rint(耐力资质) + +Pet2PropCalc.getInst().calcNaiLi(pet);
	}
	

	/************************定力资质计算*********************************************/
	
	
	/**
	 * 扩大100
	 * @param pet  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static long 得到定力初值(Pet pet,boolean real,int maxOrMin){
		
		int career = pet.getCareer();
		long 定力初值;
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		long 定力初值0  = 1l*宠物属性初值[携带等级Index][定力][career];
		
		if(maxOrMin ==0){
			定力初值 = 定力初值0 * 属性初值最大;
		}else if(maxOrMin == 1){
			定力初值 = 定力初值0 * 属性初值最小;
		}else{
			定力初值 = (long) (定力初值0* pet.getRandom(real, PetRandomType.随机定力));
		}
		
		return 定力初值;
	}
	
	public static int 计算定力值A(Pet pet) {

		int 初值  =(int)Math.rint(1.0*得到定力初值(pet,true,2)/100);
		int 等级属性点加成  = (pet.getLevel() -1) * 升级属性点分配表[定力][pet.getCareer()];
		return 初值 + 等级属性点加成;
	}
	

	public static int 计算定力值(Pet pet) {

		int base = 计算定力值A(pet);
		base += Pet2PropCalc.getInst().calcBuff(pet, GenericBuff.ATT_DingLi, base);
		int ret =  base + pet.getDingliS() + Pet2PropCalc.getInst().calcDingLi(pet);
		return Math.max(0, ret);
	}
	
	/**
	 * 
	 * @param pet 
	 * @param real
	 * @param maxOrMin  real 实际概率  maxOrMin概率满值(0)100Min值(1)实际值(2)
	 * @return
	 */
	public static int 得到实际定力资质(Pet pet,boolean real,int maxOrMin){
		

		int career = pet.getCareer();
		int 携带等级Index = 获得携带等级索引(pet.getRealTrainLevel());
		int 稀有度 = pet.getRarity();
//		携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
		long 资质初值0 = 0 ;
		if(career == 物攻型){
			资质初值0 = 1l*宠物属性资质初值修罗[携带等级Index][稀有度][定力];
		}else if(career == 敏捷型){
			资质初值0 = 1l*宠物属性资质初值影魅[携带等级Index][稀有度][定力];
		}else if(career == 法攻型){
			资质初值0 = 1l*宠物属性资质初值仙心[携带等级Index][稀有度][定力];
		}else if(career == 毒伤型){
			资质初值0 = 1l*宠物属性资质初值九黎[携带等级Index][稀有度][定力];
		}
		 
		long 资质初值;
		
		if(maxOrMin ==0){
			资质初值 = 资质初值0 * 资质初值最大;
		}else if(maxOrMin == 1){
			资质初值 = 资质初值0 * 资质初值最小;
		}else{
			资质初值 = (long) (资质初值0* pet.getRandom(real, PetRandomType.随机定力资质));
		}
		
		double 定力资质 = 1.0*资质初值/100;
		return (int)Math.rint(定力资质)+Pet2PropCalc.getInst().calcDingLi(pet);
	}
	
//	/**
//	 * 扩大100 00 00
//	 * @param pet
//	 * @param real real 实际概率  
//	 * @param maxOrMin maxOrMin概率满值(0)100Min值(1)实际值(2)
//	 * @return
//	 */
//	public static long 得到显示定力初值(Pet pet,boolean real,int maxOrMin){
//		
//		int career = pet.getCareer();
//		int A = 得到显示属性初值A[定力][career];
////		int trainLevel = pet.getTrainLevel();
////		int trainLevelIndex = 获得携带等级索引(trainLevel);
////		int 成长初值 = 成长初值表[trainLevelIndex];
//		int 成长初值 = 根据宠物得到成长初值(pet);
//		int 稀有度 = 宠物品质稀有度加成[pet.getRarity()];
//		int 类型加成  = 类型加成表[pet.getGeneration()];
//		int 变异加成 = 0;
//		if(pet.getVariation() != 0) {
//			变异加成 = 变异加成表[pet.getVariation()-1];
//		}
////		long 显示定力初值 = 1l* A*(成长初值*稀有度 +类型加成*100 + 变异加成 *100)* pet.getRandomNum();
//		
//		long 显示定力初值0 = 1l*A*(成长初值 +类型加成 + 变异加成);
//		long 显示定力初值;
//		
//		int random = 0;
//		if(real){
//			random = pet.getMinRandom();
//		}else{
//			random = pet.getTempMinRandom();
//		}
//		
//		if(maxOrMin == 0){
//			显示定力初值 = 显示定力初值0 * 100;
//		}else if(maxOrMin == 1){
////			显示定力初值 = 显示定力初值0 * random;
//			显示定力初值 = 显示定力初值0 * 85;
//		}else{
//			显示定力初值 = 显示定力初值0* pet.getRandom(real, PetRandomType.定力);
//		}
//		return 显示定力初值;
//	}
//	
//	
//	public static int 得到显示定力资质(Pet pet,boolean real,int maxOrMin){
//		
//		int career = pet.getCareer();
//		int A = 得到显示属性初值A[定力][career];
////		int trainLevel = pet.getTrainLevel();
////		int trainLevelIndex = 获得携带等级索引(trainLevel);
////		int 成长初值 = 成长初值表[trainLevelIndex];
//		int 成长初值 = 根据宠物得到成长初值(pet);
//		int 稀有度 = 宠物品质稀有度加成[pet.getRarity()];
//		int 类型加成  = 类型加成表[pet.getGeneration()];
//		int 变异加成 = 0;
//		if(pet.getVariation() != 0) {
//			变异加成 = 变异加成表[pet.getVariation()-1];
//		}
////		int 成长率加成 = 成长率加成表[pet.getGrowthClass()];
//		int 成长率加成 = 0;
//		if(real){
//			成长率加成 = 成长率加成表[pet.getGrowthClass()];
//		}else{
//			成长率加成 = 成长率加成表[pet.getTempGrowthClass()];
//		}
//		
////		double 显示定力资质 = 1l*得到显示定力初值(pet)*(成长初值 + 稀有度 + 成长率加成 +类型加成 + 变异加成 )* pet.getRandomNum()/1000000000;
//		
//		long 显示定力资质0 = 1l*得到显示定力初值(pet,real,maxOrMin)*(成长初值 + 稀有度 + 成长率加成 +类型加成 + 变异加成 );
//		// 100*100*100*100*10
//		long 显示定力资质1;
//		
//		int random = 0;
//		if(real){
//			random = pet.getMinRandom();
//		}else{
//			random = pet.getTempMinRandom();
//		}
//		
//		if(maxOrMin == 0){
//			显示定力资质1 = 1l*得到显示定力初值(pet,real,maxOrMin)*(成长初值 + 稀有度  +成长率加成表[4]+类型加成 + 变异加成 )* 100;
//		}else if(maxOrMin == 1){
////			显示定力资质1 = 1l*得到显示定力初值(pet,real,maxOrMin)*(成长初值 + 稀有度  +类型加成 + 变异加成 ) * random;
//			显示定力资质1 = 1l*得到显示定力初值(pet,real,maxOrMin)*(成长初值 + 稀有度  +类型加成 + 变异加成 ) * 85;
//		}else{
//			显示定力资质1 = 显示定力资质0 * pet.getRandom(real, PetRandomType.显示定力);
//		}
//		
//		double 显示定力资质 = 显示定力资质1/10000000;
//		
//		return (int)Math.rint(显示定力资质);
//		
//	}
//	
//	
//	/**
//	 * 扩大1000倍的值
//	 * @param pet
//	 * @return
//	 */
//	private static int 获得定力资质初值(Pet pet) {
//		int career = pet.getCareer();
//		return 属性资质初值表[定力][career];
//	}
//	
//	/**
//	 * kuoda 1000*100
//	 * @param pet
//	 * @return
//	 */
//	private static long 获得实际定力资质(Pet pet) {
//		int 初值 = 获得定力资质初值(pet);
//		int 品阶加成 = 获得品阶加成(pet);
//		int 类型加成 = 类型加成表[pet.getGeneration()];
//		int 变异加成 = 0;
//		if(pet.getVariation() != 0) {
//			变异加成 = 变异加成表[pet.getVariation()-1];
//		}
//		return 1l*初值*(100+品阶加成+类型加成+变异加成);
//	}
//
//	public static int 获得定力资质(Pet pet) {
//		return (int)Math.rint(获得实际定力资质(pet)/100000);
//	}
//	
//	public static int 计算定力值A(Pet pet) {
//		int 初值  = (int)Math.rint(1.0*得到显示定力初值(pet,true,2)/10000);
//		int 等级属性点加成  = (pet.getLevel() -1) * 升级属性点分配表[定力][pet.getCareer()];
//		return 初值 + 等级属性点加成;
//	}
//	
//	public static int 计算定力值(Pet pet) {
//
//		return 计算定力值A(pet) + pet.getDingliS();
//	}
	/************************************资质属性计算完***************************************************/
	
	/***************************************计算战斗属性******************************************************/
	
	
	/**
	 * 
	 * 
	 * {
	 *,	{外攻型	敏捷型（外）	内攻型 毒伤型（法）},
	 *,	{外攻型	敏捷型（外）	内攻型  毒伤型（法）}
	 * }
	 */
	public static final double[][] A值表 = new double[][] {
		{},
		{},
		{},
		{1,1,1,1},
		{0.526,1.053,0.526,0.263},
		{0.962,1.379,0.962,0.625},
		{},
		{},
		{},
		{},
		{},
		{}
	};
//	
//	/**
//	 * 扩大了1000倍
//	 * {	外  内 耐 命 闪 暴 防
//	 *,	{外攻型	敏捷型（外）	内攻型	毒伤型（法）},
//	 *,	{外攻型	敏捷型（外）	内攻型	毒伤型（法）}
//	 * }
//	 */
//	public static final int[][] N值表 = new int[][] {
//
//		{38,33,30,40},
//		{38,33,30,40},
//		{402,348,321,429},
//		{3,3,3,3},
//		{2,3,1,1},
//		{3,4,2,2},
//		{28,17,13,17},
//		{},
//		{}
//	};
//	
	
//	/**
//	 * 扩大了10倍
//	 * { 等级索引
//	 * }
//	 */
//	public static final int[] B值表 = new int[] {400,100,200,300,400,500,600,700,800,900,1000,1100};
	public static final int[] B值表 = new int[] {0,0,0,100,350,820,1630,2940,5010,8210,13110};
	
	private static int 得到等级索引(Pet pet){
		
		int lv = pet.getLevel();
		
		float result = 1f*(lv-1)/20;

		return (int) Math.floor(result);
	}
	
	/**
	 * bukuoda
	 * @param pet
	 * @return
	 */
	public static int 获得成长等级(Pet pet) {
		
		int index = 得到等级索引(pet);
		if (index >= B值表.length) {
			index = B值表.length -1;
		}
		return pet.getLevel()*10 + B值表[index];
//		if(index == 0){
//			return (2*pet.getLevel()*10 + B值表[0]);
//		}else{
//			return (2*pet.getLevel()*10 + 100 + (pet.getLevel() - 20)*B值表[index]);
//		}
	}
	
//	/**
//	 * kuoda 100 * 100
//	 * @param pet
//	 * @return
//	 */
//	public static long 获得成长值(Pet pet) {
////		成长值=成长初值*（1+成长率）*随机值/10
////		int trainLevel = pet.getTrainLevel();
////		int trainLevelIndex = 获得携带等级索引(trainLevel);
////		int 成长初值 = 成长初值表[trainLevelIndex];
//		int 成长初值 = 根据宠物得到成长初值(pet);
//		int 稀有度 = 宠物品质稀有度加成[pet.getRarity()];
//		int 成长率加成 = 成长率加成表[pet.getGrowthClass()];
////		return 成长初值*稀有度*(100+成长率加成)*pet.getRandomNum();
//		return 成长初值*(100+成长率加成)*pet.getRandom(true, PetRandomType.成长率);
//	}
	
	
	/**
	 * kuoda 100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际物理攻击(Pet pet,boolean real) {
//		（力量初值+K力*(等级-1)）*力量资质*（1+幻化加成+繁殖加成+变异加成）*M力+成长等级*成长初值*（1+成长率）*N力
		
		int 力量值A = 计算力量值(pet); //（力量初值+K力*(等级-1)）
		
//		if(!real){
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				力量值A += tempPoints[加点力量];
//			}
//		}
		
		int 力量资质 = 得到实际力量资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M力 = M值[MN力][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		
		int N力 = N值[MN力][pet.getCareer()];  // 10000
		
		return (1l*力量值A*力量资质*(100+幻化加成+繁殖加成+变异加成)*M力 +1l*成长等级*成长初值*(100+成长率加成)*N力);
	}

	public static int 计算物理攻击(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际物理攻击(pet,real)/(1l*100*10000));
	}
	
	
	/**
	 * kuoda 100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际法术攻击(Pet pet,boolean real) {
//		（灵力初值+K灵*等级）*灵力资质*（1+幻化加成+繁殖加成+变异加成）*M灵+成长等级*成长初值*（1+成长率）*N灵
		
		int 灵力值A = 计算灵力值(pet); //（力量初值+K力*(等级-1)）
		
//		if(!real){
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				灵力值A += tempPoints[加点灵力];
//			}
//		}
		int 灵力资质 = 得到实际灵力资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M灵 = M值[MN灵][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		int N灵 = N值[MN灵][pet.getCareer()];  // 10000
		
		return (1l*灵力值A*灵力资质*(100+幻化加成+繁殖加成+变异加成)*M灵 +1l*成长等级*成长初值*(100+成长率加成)*N灵);
	}

	public static int 计算法术攻击(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际法术攻击(pet,real)/(1l*100*10000));
	}
	
//	/**
//	 * kuoda 1000*100*100*1000
//	 * @param pet
//	 * @return
//	 */
//	private static long 计算实际法术攻击(Pet pet) {
//		int 灵力值 = 计算灵力值(pet);
//		long 灵力资质 = 获得实际灵力资质(pet);
//		int 成长等级  = 获得成长等级(pet);
//		long 成长值 = 获得成长值(pet);
//		int N值 = N值表[内法][pet.getCareer()];
//		if(PetManager.logger.isDebugEnabled()){
//			PetManager.logger.debug("**灵力值"+灵力值+"**灵力资质"+灵力资质+"**成长等级"+成长等级+"**成长值"+成长值+"**N值"+N值);
//		}
//		return 1l*灵力值*灵力资质*100000 + 1l*成长等级*成长值*N值*100;
//	}
//	
//	public static int 计算法术攻击(Pet pet) {
//		return (int)Math.rint(1.0*计算实际法术攻击(pet)/(1l*1000*100*100*1000));
//	}
	
	
	
	/**
	 * kuoda 100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际最大血量(Pet pet,boolean real) {
//		（耐力初值+K耐*等级）*耐力资质*（1+幻化加成+繁殖加成+变异加成）*M耐+成长等级*成长初值*（1+成长率）*N耐
		
		int 耐力值A = 计算耐力值(pet); //（力量初值+K力*(等级-1)）
		
//		if(!real){
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				耐力值A += tempPoints[加点耐力];
//			}
//		}
		
		int 耐力资质 = 得到实际耐力资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M耐 = M值[MN耐][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		int N耐 = N值[MN耐][pet.getCareer()];  // 10000
		
		return (1l*耐力值A*耐力资质*(100+幻化加成+繁殖加成+变异加成)*M耐 +1l*成长等级*成长初值*(100+成长率加成)*N耐);
	}

	public static int 计算最大血量(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际最大血量(pet,real)/(1l*100*10000));
	}

	
//	/**
//	 * kuoda 1000*100*100*1000
//	 * @param pet
//	 * @return
//	 */
//	public static long 计算实际最大血量(Pet pet) {
//		int 耐力值 = 计算耐力值(pet);
//		long 耐力资质 = 获得实际耐力资质(pet);
//		int 成长等级  = 获得成长等级(pet);
//		long 成长值 = 获得成长值(pet);
//		int N值 = N值表[血量][pet.getCareer()];
//		if(PetManager.logger.isDebugEnabled()){
//			PetManager.logger.debug("**耐力值"+耐力值+"**耐力资质"+耐力资质+"**成长等级"+成长等级+"**成长值"+成长值+"**N值"+N值);
//		}
//		return 1l*耐力值*耐力资质*100000 + 1l*成长等级*成长值*N值*100;
//		
//	}
//	
//	public static int 计算最大血量(Pet pet) {
//		return (int)Math.rint(1.0*计算实际最大血量(pet)/(1l*1000*100*100*1000));
//	}
	

	/**
	 * kuoda 100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际命中(Pet pet,boolean real) {
//		（身法初值+K命*等级）*身法资质*（1+幻化加成+繁殖加成+变异加成）*M命+成长等级*成长初值*（1+成长率）*N命
		
		int 身法值A = 计算身法值(pet); //（力量初值+K力*(等级-1)）
//		if(!real){
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				身法值A += tempPoints[加点身法];
//			}
//		}
		
		
		int 身法资质 = 得到实际身法资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M命 = M值[MN命][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		int N命 = N值[MN命][pet.getCareer()];  // 10000
		
		return (1l*身法值A*身法资质*(100+幻化加成+繁殖加成+变异加成)*M命 +1l*成长等级*成长初值*(100+成长率加成)*N命);
	}

	public static int 计算命中(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际命中(pet,real)/(1l*100*10000));
	}
	
//	/**
//	 * kuoda 1000*100*1000*100
//	 * @param pet
//	 * @return
//	 */
//	public static long 计算实际命中(Pet pet) {
//		
//		int 身法值 = 计算身法值(pet);
//		long 身法资质 = 获得实际身法资质(pet);
//		int 成长等级  = 获得成长等级(pet);
//		long 成长值 = 获得成长值(pet);
//		int N值 = N值表[命中][pet.getCareer()];
//		int A值 = A值表[命中][pet.getCareer()];
//		if(PetManager.logger.isDebugEnabled()){
//			PetManager.logger.debug("**身法值"+身法值+"**身法资质"+身法资质+"**成长等级"+成长等级+"**成长值"+成长值+"**N值"+N值);
//		}
//		return 1l*身法值*身法资质*A值*100 + 1l*成长等级*成长值*N值*100;
//	}
//	
//	
//	public static int 计算命中(Pet pet) {
//		return (int)Math.rint(1.0*计算实际命中(pet)/(1l*1000*100*100*1000));
//		
//	}
	
	
	/**
	 * kuoda 100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际闪躲(Pet pet,boolean real) {
//		（身法初值+K闪*等级）*身法资质*（1+幻化加成+繁殖加成+变异加成）*M闪+成长等级*成长初值*（1+成长率）*N闪
		
		int 身法值A = 计算身法值(pet); //（力量初值+K力*(等级-1)）
		
//		if(!real){
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				身法值A += tempPoints[加点身法];
//			}
//		}
		
		int 身法资质 = 得到实际身法资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M闪 = M值[MN闪][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		int N闪 = N值[MN闪][pet.getCareer()];  // 10000
		
		return (1l*身法值A*身法资质*(100+幻化加成+繁殖加成+变异加成)*M闪 +1l*成长等级*成长初值*(100+成长率加成)*N闪);
	}

	public static int 计算闪躲(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际闪躲(pet,real)/(1l*100*10000));
	}
	
//	/**
//	 * kuoda 1000*100*1000*100
//	 * @param pet
//	 * @return
//	 */
//	private static long 计算实际闪躲(Pet pet) {
//		int 身法值 = 计算身法值(pet);
//		long 身法资质 = 获得实际身法资质(pet);
//		int 成长等级  = 获得成长等级(pet);
//		long 成长值 = 获得成长值(pet);
//		int N值 = N值表[闪躲][pet.getCareer()];
//		int A值 = A值表[闪躲][pet.getCareer()];
//		return 1l*身法值*身法资质*A值 *100 + 1l*成长等级*成长值*N值*100;
//	}
//	public static int 计算闪躲(Pet pet) {
//		
//		return (int)Math.rint(1.0*计算实际闪躲(pet)/(1l*1000*100*100*1000));
//	}
	
	/**
	 * kuoda 100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际暴击(Pet pet,boolean real) {
//		（身法初值+K暴*等级）*身法资质*（1+幻化加成+繁殖加成+变异加成）*M暴+成长等级*成长初值*（1+成长率）*N暴
		
		int 身法值A = 计算身法值(pet); //（力量初值+K力*(等级-1)）
//		if(!real){
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				身法值A += tempPoints[加点身法];
//			}
//		}
		
		
		int 身法资质 = 得到实际身法资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M暴 = M值[MN暴][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		int N暴 = N值[MN暴][pet.getCareer()];  // 10000
		
		return (1l*身法值A*身法资质*(100+幻化加成+繁殖加成+变异加成)*M暴 +1l*成长等级*成长初值*(100+成长率加成)*N暴);
	}

	public static int 计算暴击(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际暴击(pet,real)/(1l*100*10000));
	}
	
	
//	/**
//	 * kuoda 1000*100*1000*100
//	 * @param pet
//	 * @return
//	 */
//	private static long 计算实际暴击(Pet pet) {
//		int 身法值 = 计算身法值(pet);
//		long 身法资质 = 获得实际身法资质(pet);
//		int 成长等级  = 获得成长等级(pet);
//		long 成长值 = 获得成长值(pet);
//		int N值 = N值表[暴击][pet.getCareer()];
//		int A值 = A值表[暴击][pet.getCareer()];
//		return 1l*身法值*身法资质*A值*100  + 1l*成长等级*成长值*N值*100;
//		
//	}
//	
//	public static int 计算暴击(Pet pet) {
//		
//		return (int)Math.rint(1.0*计算实际暴击(pet)/(1l*1000*100*100*1000));
//	}
	
	
	/**
	 * kuoda 100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际物理防御(Pet pet,boolean real) {
//		（定力初值+K定*等级）*定力资质*（1+幻化加成+繁殖加成+变异加成）*M定+成长等级*成长初值*（1+成长率）*N定
		
		int 定力值A = 计算定力值(pet); //（力量初值+K力*(等级-1)）
		
//		if(!real){
//			
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				定力值A += tempPoints[加点定力];
//			}
//			
//		}
		
		int 定力资质 = 得到实际定力资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M定 = M值[MN定][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		int N定 = N值[MN定][pet.getCareer()];  // 10000
		
		return (1l*定力值A*定力资质*(100+幻化加成+繁殖加成+变异加成)*M定 +1l*成长等级*成长初值*(100+成长率加成)*N定);
	}

	public static int 计算物理防御(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际物理防御(pet,real)/(1l*100*10000));
	}
	
//	/**
//	 * kuoda 1000*100*100*1000
//	 * @param pet
//	 * @return
//	 */
//	public static long 计算实际物理防御(Pet pet) {
//		int 定力值 = 计算定力值(pet);
//		long 定力资质 = 获得实际定力资质(pet);
//		int 成长等级  = 获得成长等级(pet);
//		long 成长值 = 获得成长值(pet);
//		int N值 = N值表[外防][pet.getCareer()];
//		if(PetManager.logger.isDebugEnabled()){
//			PetManager.logger.debug("**定力值"+定力值+"**定力资质"+定力资质+"**成长等级"+成长等级+"**成长值"+成长值+"**N值"+N值);
//		}
//		return 1l*定力值*定力资质*100000 + 1l*成长等级*成长值*N值*100;
//	}
//	
//
//	public static int 计算物理防御(Pet pet) {
//		return (int)Math.rint(1.0*计算实际物理防御(pet)/(1l*1000*100*100*1000));
//	}
	
	
	/**
	 * kuoda 100*100*10000
	 * @param pet  
	 * @param real true真实  false 临时  
	 * @return
	 */
	private static long 计算实际法术防御(Pet pet,boolean real) {
//		（定力初值+K定*等级）*定力资质*（1+幻化加成+繁殖加成+变异加成）*M定+成长等级*成长初值*（1+成长率）*N定
		
		int 定力值A = 计算定力值(pet); //（力量初值+K力*(等级-1)
		
//		if(!real){
//			int[] tempPoints = pet.getTempPoints();
//			if(tempPoints != null){
//				定力值A += tempPoints[加点定力];
//			}
//		}
		
		int 定力资质 = 得到实际定力资质(pet,true,2); // kuoda 100
	
		int 幻化加成  = 获得品阶加成(pet);
		int 繁殖加成  = 几代加成表[pet.getGeneration()];
		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		
		int M定 = M值[MN定][pet.getCareer()];  // 10000
		int 成长等级  = 获得成长等级(pet);
		int 成长初值 = 根据宠物得到成长初值(pet);
		int 成长率加成 = 0;
		成长率加成 = 成长率加成表[pet.getGrowthClass()];
		int N定 = N值[MN定][pet.getCareer()];  // 10000
		
		return (1l*定力值A*定力资质*(100+幻化加成+繁殖加成+变异加成)*M定 +1l*成长等级*成长初值*(100+成长率加成)*N定);
	}

	public static int 计算法术防御(Pet pet,boolean real) {
		
		return (int)Math.rint(1.0*计算实际法术防御(pet,real)/(1l*100*10000));
	}
//	/**
//	 * kuoda 1000*100*100*1000
//	 * @param pet
//	 * @return
//	 */
//	public static long 计算实际法术防御(Pet pet) {
//		
//		int 定力值 = 计算定力值(pet);
//		long 定力资质 = 获得实际定力资质(pet);
//		int 成长等级  = 获得成长等级(pet);
//		long 成长值 = 获得成长值(pet);
//		int N值 = N值表[外防][pet.getCareer()];
//		return 1l*定力值*定力资质*100000+ 1l*成长等级*成长值*N值*100;
//		
//	}
//
//	public static int 计算法术防御(Pet pet) {
//		
//		return (int)Math.rint(1.0*计算实际法术防御(pet)/(1l*1000*100*100*1000));
//		
//	}
	
	/**
	 * kuoda 100
	 * @param pet
	 * @return
	 */
	public static int 计算命中率(Pet pet,boolean real) {
		
//		100*10000
		long 实际命中值  = 计算实际命中(pet,real);
//		double 命中A值 = CombatCaculator.A_命中;
		double 命中A值 = A值表[命中][pet.getCareer()];
	
		int 成长等级 = 获得成长等级(pet);
		
//		命中率=命中值/(命中A值*成长等级)+80%
		int 命中率 = (int)Math.rint((1.0*实际命中值)/(1l*命中A值*成长等级*100*10000)+80);
		return 命中率;
		
	}
	
	/**
	 * kuoda 100
	 * @param pet
	 * @return
	 */
	public static int 计算暴击率(Pet pet,boolean real) {
		
//		会心一击率=会心一击值/(会心A值*成长等级)+5%
//		1000*100*1000*100
		long 实际暴击值  = 计算实际暴击(pet,real);
//		double 暴击A值 = CombatCaculator.A_会心一击;
		double 暴击A值 = A值表[暴击][pet.getCareer()];
		//kuoda 10
		int 成长等级 = 获得成长等级(pet);
		
		int 暴击率 = (int)Math.rint((1.0*实际暴击值)/(1l*暴击A值*成长等级*100*10000)+5);
		return 暴击率;
		
	}
	
	/**
	 * kuoda 100
	 * @param pet
	 * @return
	 */
	public static int 计算闪躲率(Pet pet,boolean real) {
		
//		闪躲率=(闪避值*闪避X值)/(闪避值+闪避A值*成长等级)+5%
//		100*10000
		long 实际闪躲值  = 计算实际闪躲(pet,real);
//		double 闪躲A值 = CombatCaculator.A_闪避;
		double 闪躲A值 = A值表[闪躲][pet.getCareer()];
		//kuoda 10
		int 成长等级 = 获得成长等级(pet);

		int 闪躲率 = (int)Math.rint((1.0*实际闪躲值)/(1l*闪躲A值*成长等级*100*10000)+5);
		return 闪躲率;
		
	}
	
	
	public static int 临时减伤A值 = 55;
	
	/**
	 * 扩大100
	 */
	public static int[] 临时减伤X值 = {170,170,170,170,170};

	/**
	 * kuoda 100
	 * @param pet
	 * @return
	 */
	public static int 计算外攻防御减伤率(Pet pet,boolean real) {
		
//		防御减伤率=(防御值*减伤X值)/(防御值+减伤A值*成长等级)
//		100*10000
		long 实际外攻防御值  = 计算实际物理防御(pet,real);
		int 减伤A值 =临时减伤A值;
		int 减伤X值 = CombatCaculator.X_减伤[pet.getCareer()+1];
		//kuoda 10
		int 成长等级 = 获得成长等级(pet);

		int 外攻防御减伤率 =(int)Math.rint((1.0*(1l*实际外攻防御值*减伤X值))/(1l*实际外攻防御值+1l*减伤A值*成长等级*100*10000));
		return 外攻防御减伤率;
		
	}
	
	/**
	 * kuoda 100
	 * @param pet
	 * @return
	 */
	public static int 计算法术防御减伤率(Pet pet,boolean real) {
		
//		防御减伤率=(防御值*减伤X值)/(防御值+减伤A值*成长等级)
//		100*10000
		long 实际法术防御值  = 计算实际法术防御(pet,real);
		int 减伤A值 = 临时减伤A值;
		int 减伤X值 = CombatCaculator.X_减伤[pet.getCareer()+1];
		//kuoda 10
		int 成长等级 = 获得成长等级(pet);

		int 法术防御减伤率 =(int)Math.rint((1.0*(1l*实际法术防御值*减伤X值))/(1l*实际法术防御值+1l*减伤A值*成长等级*100*10000));
		return 法术防御减伤率;
		
	}
	
	
	
	/*************************************战斗属性完****************************************************/
	
	
	
	/**
	 * 千分率
	 * {携带等级
	 *  {成长率等级}
	 *  }
	 */
	public static double[][] 成长率等级概率 = new double[][]{

		{0.200,0.300,0.250,0.150,0.100},
		{0.200,0.300,0.250,0.150,0.100},
		{0.200,0.300,0.250,0.150,0.100},
		{0.200,0.300,0.250,0.150,0.100},
		{0.200,0.300,0.250,0.150,0.100},
		{0.200,0.300,0.250,0.150,0.100}
		
	};
	
	public static int 随机获得成长率等级(int trainLevel) {
		int index =获得携带等级索引(trainLevel);
		if(index < 0 || index >= 成长率等级概率.length){
			return 1;
		}
		return ProbabilityUtils.randomProbability(random,成长率等级概率[index]);
	}
	
	
	/**
	 * 获得的二代宠物成长度为两只一代宠物成长度的平均值，向上取整
	 * @param parentA
	 * @param parentB
	 * @return
	 */
	public static int 获得繁殖子宠物的成长率等级(Pet parentA, Pet parentB) {
		return (int)Math.ceil((new Double(parentA.getGrowthClass())+new Double(parentB.getGrowthClass()))/2);
	}
	
	/**
	 * 扩大了100
	 * @param pet  bln(true 真是成长率，false 临时成长率)
	 * @return
	 */
	public static int 计算宠物品质值(Pet pet,boolean bln) {
		
//		(变异加成+类型加成+成长度+稀有度+同性格技能数量加成+非同性格技能数量加成)*携带等级=宠物品质
//		同类性格技能加成=同类性格技能数量*10%
//		非同类性格技能加成=非同类性格技能数量*3%

		int 变异加成 = 0;
		if(pet.getVariation() != 0) {
			变异加成 = 变异加成表[pet.getVariation()-1];
		}
		int 类型加成 = 几代加成表[pet.getGeneration()];
		int 成长率加成 = 0;
		if(bln){
			成长率加成 = 成长率加成表[pet.getGrowthClass()];
		}else{
			成长率加成 = 成长率加成表[pet.getTempGrowthClass()];
		}
		
		int 稀有度加成 = 宠物品质稀有度加成[pet.getRarity()];
		int[] skills = pet.getSkillIds();
		int match = 0;
		int noMatch = 0;
		if(skills != null && skills.length > 0){
			Map<Integer,PetSkillReleaseProbability> map = PetManager.getInstance().getMap();
			
			for(int id : skills){
				PetSkillReleaseProbability pp = map.get(id);
				if(pp != null){
					if(pp.getCharacter() == pet.getCharacter()){
						++match;
					}else{
						++noMatch;
					}
				}else{
					PetManager.logger.error("[计算宠物品质值错误] [宠物配置没有这个技能] [id:"+id+"]");
				}
			}
			
				if (PetManager.logger.isDebugEnabled()) PetManager.logger.debug("[计算宠物品质值] [计算技能值完成]");
		}
		int 同性格技能数量加成 = match*10;
		int 非同性格技能数量加成 = noMatch *3;
		
		//2013年9月20日9:42:40  康建虎修改为新的计算公式。
		/*
		 int(攻击强度*命中率*（1+暴击率）*（1+符合性格技能个数（符合）*技能加成（符合）+技能个数（不符）*技能加成（不符））)
		 */
//		int 攻击强度 = pet.getPhyAttack() + pet.getMagicAttack();
//		float 命中率 = (pet.getHitRate() + pet.getHitRateOther())*1.0f / 1000;
//		float 暴击率 = (pet.getCriticalHitRate() + pet.getCriticalHitRateOther())*1.0f/1000;
//		float newV = (攻击强度 * 命中率 * (1+暴击率)) * (1+match*0.05f + noMatch * 0.01f);
		return (变异加成+类型加成+成长率加成+稀有度加成+同性格技能数量加成+非同性格技能数量加成)*pet.getRealTrainLevel();
//		int tianShSk = 0;
//		//一代/二代天生技能2W分，一代技能在二代宝宝身上3W分
//		if(pet.getGeneration() == 0){
//			tianShSk += pet.talent1Skill>0 ? 20000 : 0;
//		}else{
//			tianShSk += pet.talent1Skill>0 ? 30000 : 0;
//			tianShSk += pet.talent2Skill>0 ? 20000 : 0;
//		}
//		PetManager.logger.debug("PetPropertyUtility.计算宠物品质值: tianShSk {}, newV {}",tianShSk,newV);
//		return Math.round(newV) + tianShSk;
	}
	
	
	/**
	 * 放大1000倍{携带等级{}}
	 * 
	 */
	public static int 宠物品质鉴定表[][][] = new int[][][]{
//		X≤0.83	0.8＜X≤1.03	1.03＜X≤1.66	1.66＜X≤2.46	2.46＜X≤3.79
//		X≤37.35	37.35＜X≤46.35	46.35＜X≤74.7	74.7＜X≤110.7	110.7＜X≤170.55
//		X≤74.7	74.7＜X≤92.7	92.7＜X≤149.4	149.4＜X≤221.4	221.4＜X≤341.1
//		X≤112.05	112.05＜X≤139.05	139.05＜X≤224.1	224.1＜X≤332.1	332.1＜X≤511.65
//		X≤149.4	149.4＜X≤185.4	185.4＜X≤298.8	298.8＜X≤442.8	442.8＜X≤682.2


		{
			{0,830},
			{830,1030},
			{1030,1660},
			{1660,2460},
//			{2460,3790},
			{2460,999999999},
		},
		{
			{0,37350},
			{37350,46350},
			{46350,74700},
			{74700,110700},
//			{110700,170550},
			{110700,999999999},
		},
		{
			{0,74700},
			{74700,92700},
			{92700,149400},
			{149400,221400},
//			{221400,341100},
			{221400,999999999},
		},
		{
			{0,112050},
			{112050,139050},
			{139050,224100},
			{224100,332100},
//			{332100,511650},
			{332100,999999999},
		},
		{
			{0,149400},
			{149400,185400},
			{185400,298800},
			{298800,442800},
//			{442800,682200},
			{442800,999999999},
		},
		{
			{0,149400},
			{149400,185400},
			{185400,298800},
			{298800,442800},
//			{442800,682200},
			{442800,999999999},
		}
	};
	
	public static int 判定宠物品质(Pet pet,boolean bln) {
		
		int temp = 计算宠物品质值(pet,bln);
		// 设置宠物品质得分  排行榜
//		pet.setQualityScore((int)Math.rint((1f*temp/100)));
		
		int X = temp*10;
		int 携带等级索引 = 获得携带等级索引(pet.getRealTrainLevel());
		int 品质表[][] = 宠物品质鉴定表[携带等级索引];
		for(int i=0; i<品质表.length; i++) {
			if(X > 品质表[i][0] && X <= 品质表[i][1]) {
				return i;
			}
		}
		return 0;
	}
	
	public static int 繁殖次数配置[] = new int[]{50, 100, 150};
	
	public static int 是否增加繁殖次数(int oldLevel, int newLevel) {
		int add = 0;
		for(int i=0; i<繁殖次数配置.length; i++) {
			if(oldLevel < 繁殖次数配置[i] && newLevel >= 繁殖次数配置[i]) {
				add++;
				continue;
			}
		}
		return add;
	}
	
//			0技能概率	1技能概率	2技能概率	3技能概率
//	随处可见	10%	50%	40%	0%
//	百里挑一	0%	40%	55%	5%
//	千载难逢	0%	5%	50%	45%

	/**
	 * 一维 宠物稀有度 二维 对应技能个数的比例
	 */
	public static double[][] 随机技能个数配置= {
		
		{0.1,	0.5,	0.4,	0},
		{0,		0.4,	0.55,	0.05},
		{0,		0.05,	0.5,	0.45},
		{0,		0.05,	0.5,	0.45},
	};
	
	
	public static int randomSkillNum(Pet pet){
		
		int 稀有度  = pet.getRarity();
		return ProbabilityUtils.randomProbability(random,随机技能个数配置[稀有度]);
		
	}
	
	
	//携带等级(1,45,){属性类型(力量,灵力,耐力,身法,定力){职业修罗,鬼杀,令尊,武皇}}
	public static int[][][] 宠物属性初值 = {
		{
			//携带等级1
			{61,41,11,11},
			{11,11,61,41},
			{31,21,21,51},
			{21,61,31,21},
			{31,21,31,31}
		},
		{
			//携带等级45
			{211,141,36,36},
			{36,36,211,141},
			{106,71,71,176},
			{71,211,106,71},
			{106,71,106,106}
		},
		{
			//携带等级90
			{493,329,83,83},
			{83,83,493,329},
			{247,165,165,411},
			{165,493,247,165},
			{247,165,247,247}
		},
		{
			//携带等级135
			{979,653,164,164},
			{164,164,979,653},
			{490,327,327,816},
			{327,979,490,327},
			{490,327,490,490}
		},
		{
			//携带等级180
			{1765,1177,295,295},
			{295,295,1765,1177},
			{883,589,589,1471},
			{589,1765,883,589},
			{883,589,883,883}
		},
		{
			//携带等级225
			{3007,2005,502,502},
			{502,502,3007,2005},
			{1504,1003,1003,2506},
			{1003,2506,1504,1003},
			{1504,1003,1504,1504}
		},
	
	};
	
	
	/**
	 * [属性类别(力量,灵力,耐力,身法,定力)][职业(外功，敏捷，内法，毒伤)]
	 */
	public static final int[][] 升级属性点分配表 = new int[][] {
		{6,4,1,1},
		{1,1,6,4},
		{3,2,2,6},
		{2,6,3,2},
		{3,2,3,2}
//		{150,600,750,300,450 },
	};
	
	
	//携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
	public static int[][][] 宠物属性资质初值修罗 = {
		{
			//携带等级1
			{300,50,150,100,150 },
			{450,75,225,150,225 },
			{600,100,300,200,300}, 
			{750, 	125, 	375, 	250, 	375 } 
		},
		{
			//携带等级45
			{450,75,225,150,225 },
			{675,113,338,225,338}, 
			{900,150,450,300,450 },
			{1125 ,	188 ,	563 ,	375 ,	563}

		},
		{
			//携带等级90

			{600,100,300,200,300}, 
			{900,150,450,300,450 },
			{1200,200,600,400,600} ,
			{1500 ,	250 ,	750 ,	500 ,	750 }
		},
		{
			//携带等级135
			{750,125,375,250,375 },
			{1125,188,563,375,563 },
			{1500,250,750,500,750 },
			{1875, 	313 ,	938 ,	625 ,	938 }
		},
		{
			//携带等级180
			{900,150,450,300,450}, 
			{1350,225,675,450,675}, 
			{1800,300,900,600,900} ,
			{2250, 	375, 	1125 ,	750 ,	1125 }
		},
		{
			//携带等级225
			{1050,175,525,350,525 },
			{1575,263,788,525,788 },
			{2100,350,1050,700,1050} ,
			{2625, 	438, 	1313, 	875, 	1313 }
		},
	
	};
	
	//携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
	public static int[][][] 宠物属性资质初值影魅 = {
		{
			//携带等级1
			{200,50,100 ,300 ,100 },
			{300,75,150	,450 ,150 },
			{400,100,200 ,600 ,200},
			{500, 	125, 	250, 	750 ,	250 }
		},
		{
			//携带等级45
			{300,75,150 ,450 ,150 },
			{450,113,25 ,675 ,225 },
			{600,150,300 ,900 ,300},
			{750 ,	188, 	375 ,	1125, 	375 }
		},
		{
			//携带等级90
			{400 ,100,200 ,600 ,200 },
			{600 ,150 ,300 ,900 ,300 },
			{800 ,200 ,400 ,1200 ,400 },
			{1000, 	250 ,	500 ,	1500 ,	500 }
		},
		{
			//携带等级135
			{500 ,125 ,250 ,750 ,250 },
			{750 ,188 ,375 ,1125 ,375 },
			{1000 ,250 ,500 ,1500,500},
			{1250, 	313 ,	625 ,	1875, 	625 }
		},
		{
			//携带等级180
			{600 ,150,300,900,300 },
			{900 ,225,450,1350,450 },
			{1200 ,300,	600,1800,600 },
			{1500 ,	375 ,	750 ,	2250 ,	750 }
		},
		{//225
			{700,175,350,1050,350 },
			{1050,263,525,1575,525 },
			{1400,350,700,2100,700 },
			{1750, 	438, 	875, 	2625, 	875 }
		}
	
	};
	
	
	//携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
	public static int[][][] 宠物属性资质初值仙心 = {
		{
			//携带等级1
			{50,300,100,150,150 },
			{75,450,150,225,225 },
			{100,600,200,300,300 },
			{125, 	750, 	250, 	375 ,	375 }
		},
		{
			//携带等级45
			{75,450,150,225,225 },
			{113,675,225,338,338 },
			{150,900,300,450,450 },
			{188, 	1125, 	375, 	563, 	563 }
		},
		{
			//携带等级90

			{100,600,200,300,300 },
			{150,900,300,450,450 },
			{200,1200,400,600,600 },
			{250, 	1500 ,	500 ,	750, 	750 }
		},
		{
			//携带等级135
			{125,750,250,375,375 },
			{188,1125,375,563,563 },
			{250,1500,500,750,750 },
			{313 ,	1875 ,	625 ,	938 ,	938 }
		},
		{
			//携带等级180
			{150,900,300,450,450 },
			{225,1350,450,675,675 },
			{300,1800,600,900,900 } ,
			{375, 	2250, 	750, 	1125, 	1125 }
		},
		{//225
			{175,1050,350,525,525 },
			{263,1575,525,788,788 },
			{350,2100,700,1050,1050 },
			{438, 	2625, 	875, 	1313, 	1313 }
		}
	};
	
	
	//携带等级{稀有度{类型(力量资质,灵力资质,耐力资质,身法资质,定力资质)}}
	public static int[][][] 宠物属性资质初值九黎 = {
		{
			//携带等级1
			{50,200,250,100,150},
			{75,300,375,150,225 },
			{100,400,500,200,300 },
			{125 ,	500, 	625, 	250, 	375 }
		},
		{
			//携带等级45
			{75,300,375,150,225 },
			{113,450,563,225,338 },
			{150,600,750,300,450 },
			{188 ,	750 ,	938, 	375 ,	563 }
		},
		{
			//携带等级90
			{100,400,500,200,300 },
			{150,600,750,300,450 },
			{200,800,1000,400,600 },
			{250, 	1000, 	1250 ,	500, 	750 }
		},
		{
			//携带等级135
			{125,500,625,250,375 },
			{188,750,938,375,563 },
			{250,1000,1250,500,750 },
			{313, 	1250, 	1563, 	625, 	938 }
		},
		{
			//携带等级180
			{150,600,750,300,450 },
			{225,900,1125,450,675 },
			{300,1200,1500,600,900 },
			{375, 	1500, 	1875, 	750, 	1125 }
		},
		{//225
			{175,700,875,350,525 },
			{263,1050,1313,525,788 },
			{350,1400,1750,700,1050 },
			{438, 	1750 ,	2188 ,	875 ,	1313 }
		}
	
	};
	
	//{类型(M力 M灵 M耐 M命 M闪 M暴 M定){职业(修罗 影魅 仙心 九黎)}} 扩大10000
	public static int[][] M值 = {
		
		{31,39,163, 188}, 
		{188,156,27,47},
		{625,781,812 ,375 },
		{19,5,11,19 },
		{13,8,6 ,5 },
		{15,6,10,12 },
		{32,29,20,23 }
		
	};
	
	//{类型(M力 M灵 M耐 M命 M闪 M暴 M定){职业(修罗 影魅 仙心 九黎)}} 扩大10000
	public static int[][] N值 = {
		
		{268,223,232,268 },
		{268,223,232,268 },
		{2679,2232,2321,2379 },
		{54,45,46,54 },
		{37,71,24,14 },
		{43,50,45,36 },
		{138,82,85,99 }
		
	};

	//扩大100初级 中级  高级 特级 终极
	public static float[] 排行榜积分 = {(float)0.02,(float)0.03,(float)0.05,(float)0.07,(float)0.1};
	
	
	public static float 根据技能名得到积分(Skill skill){
		int index = 0;
		if(skill.getName().contains(Translate.初级)){
			index = 0;
		}else if(skill.getName().contains(Translate.中级)){
			index = 1;
		}else if(skill.getName().contains(Translate.高级)){
			index = 2;
		}else if(skill.getName().contains(Translate.特级)){
			index = 3;
		}else if(skill.getName().contains(Translate.终级)){
			index = 4;
		}
		return 排行榜积分[index];
	}
	
}
