package com.fy.engineserver.activity.flushmonster;

import java.util.Calendar;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.clifford.manager.CliffordManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.seal.SealManager;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.MonsterManager;
import com.fy.engineserver.util.ProbabilityUtils;
import com.fy.engineserver.util.ServiceStartRecord;

public class FlushMonsterInGuoQingManager implements Runnable{

	private static FlushMonsterInGuoQingManager self;
	
	public static FlushMonsterInGuoQingManager getInstance(){
		return self;
	}
	
	private FlushMonsterInGuoQingManager(){
		
	}
	
	public	static Logger logger = LoggerFactory.getLogger(FlushMonsterInGuoQingManager.class.getName());

	public static boolean running = false;
	
	public void init(){
		
		long now = System.currentTimeMillis();
		self = this;
		//running = true;
		Thread thread = new Thread(this, "FlushMonsterInGuoQingManager");
		thread.start();
		logger.warn("[国庆怪物攻城初始化] [完毕] [开启:"+running+"] [耗时:"+(System.currentTimeMillis() - now)+"ms]");
		ServiceStartRecord.startLog(this);
	}
	
	public static int 开始时间 = 15;//小时
	public static int 刷新时间 = 60000;//毫秒
	
	/*
	 * 三维数组
	 * 第一维 为50个刷新点的数组集合，每个数组50个刷新点
	 * 第二维 为50个刷新点
	 * 第三维 为刷新点坐标
	 */
	public static int[][][] 昆华古城刷新点 = new int[][][]{
		{{895,2107},{1104,2105},{1352,2133},{1511,2120},{1627,2043},{1744,2007},{3219,2214},{3334,2140},{3488,2065},{3588,2013},{3660,1964},{3750,1930},{1830,1799},{4099,1741},{3578,1711},{4112,498},{3982,434},{3897,400},{3800,365},{2855,312},{2683,275},{1679,699},{1142,1078},{2601,146},{2112,56},{2008,47},{1916,76},{2008,122},{1918,132},{1825,57},{1747,60},{1716,155},{1803,219},{1633,253},{1439,236},{1205,171},{1145,94},{998,182},{728,308},{666,403},{671,439},{564,507},{446,640},{362,753},{257,902},{208,1004},{180,1120}},
		{{107,1642},{275,1813},{400,1929},{548,2003},{702,2121},{226,2038},{160,1914},{332,1814},{589,1784},{593,1631},{456,1433},{2725,224},{3930,1355},{3758,1443},{3352,1808},{3258,1848},{3027,1849},{2927,1950},{2768,1993},{2713,2021},{2652,2044},{2569,2002},{2498,1971},{2387,1911},{2251,1844},{2146,1807},{2060,1776},{1968,1742},{1582,1522},{1159,1124},{1438,1095},{1546,1038},{1636,908},{1663,813},{1847,687},{2075,686},{2216,649},{2306,679},{2612,156},{2757,226},{3077,363},{3035,489},{3399,799},{3365,1094},{3674,1083},{4037,555},{1120,586}}
	};
	
	/*
	 * 三维数组
	 * 第一维 为50个刷新点的数组集合，每个数组50个刷新点
	 * 第二维 为50个刷新点
	 * 第三维 为刷新点坐标
	 */
	public static int[][][] 昆仑圣殿刷新点 = new int[][][]{
		{{3323,1099},{3286,1070},{3252,1050},{3220,1020},{2330,805},{2250,835},{2220,850},{2045,756},{2004,789},{1955,824},{1900,842},{1842,869},{1756,899},{1671,933},{1642,967},{1543,1012},{1441,1057},{1409,1193},{1455,1301},{1581,1768},{1598,1828},{1641,1870},{1709,1936},{1783,1931},{1847,1951},{1948,1917},{2607,2024},{2728,2036},{2832,2025},{3133,2121},{3199,2113},{3261,2124},{3154,2141},{3226,2144},{3251,2149},{3551,2091},{3604,2078},{3597,2023},{3554,1965},{3534,2162},{3500,1625},{3431,1525},{3410,980},{3366,942},{2226,677},{2149,698},{2096,725}},
		{{3477,256},{3347,184},{3208,140},{2891,158},{2562,177},{2313,304},{1730,460},{1459,514},{1263,559},{1088,608},{960,759},{847,966},{813,1112},{442,1405},{379,1616},{551,2302},{502,2700},{690,2701},{880,3266},{1018,3450},{1336,3554},{1832,3543},{2309,3300},{3582,3373},{2938,3549},{3120,3617},{3347,3677},{3842,3564},{3941,3410},{4872,3359},{4992,3292},{5160,3205},{5192,2209},{5222,1775},{5194,1529},{5192,708},{5106,591},{5021,497},{4900,386},{4751,285},{4597,192},{4399,254},{4233,370},{4585,408},{4820,431},{4967,593},{3381,472}}
	};
	
	/*
	 * 三维数组
	 * 第一维 为50个刷新点的数组集合，每个数组50个刷新点
	 * 第二维 为50个刷新点
	 * 第三维 为刷新点坐标
	 */
	public static int[][][] 灭魔神域刷新点 = new int[][][]{
		{{1064,64},{926,54},{808,67},{662,364},{584,414},{445,487},{318,552},{220,603},{147,647},{45,570},{57,1332},{205,1433},{383,1523},{563,1606},{743,1700},{932,1812},{418,2186},{171,2480},{80,2668},{803,2379},{2579,2689},{2403,2565},{2202,2466},{2038,2376},{1878,2293},{4380,2568},{4471,2507},{4718,2683},{5023,2263},{4918,2247},{4264,2328},{4821,2023},{4730,1984},{3872,2409},{3731,2482},{3601,2549},{3402,2675},{3255,2610},{3104,2548},{2586,2368},{2392,2268},{2231,2175},{1519,1798},{1607,1721},{1043,1531},{798,1415},{649,1330}},
		{{4544,1711},{4422,1782},{4313,1848},{4038,1421},{3962,1452},{3871,1493},{3764,1597},{3562,2187},{3091,2092},{3200,2029},{3278,1989},{3369,1947},{3459,1926},{3549,1977},{3644,2033},{3765,2091},{2402,1245},{2259,1187},{2143,1137},{1983,1122},{1749,1228},{1640,1309},{1544,1353},{1946,203},{2040,248},{2096,291},{1713,237},{1546,242},{727,623},{630,680},{531,736},{504,891},{399,933},{277,1022},{134,1080},{285,1111},{445,1094},{504,1133},{576,1168},{598,1315},{683,1361},{778,1401},{880,1471},{1016,1538},{1125,1499},{1246,1496},{1378,1492}}
	};
	
	public static int[] 昆华古城刷新怪物id = new int[]{20010349,20010350,20010351,20010352};
	public static double[] 昆华古城刷新怪物几率 = new double[]{0.44,0.44,0.10,0.02};//和id对应
	
	public static int[] 昆仑圣殿刷新怪物id = new int[]{20010353,20010354,20010355,20010356};
	public static double[] 昆仑圣殿刷新怪物几率 = new double[]{0.44,0.44,0.10,0.02};//和id对应
	
	public static int[] 灭魔神域刷新怪物id = new int[]{20010357,20010358,20010359,20010360};
	public static double[] 灭魔神域刷新怪物几率 = new double[]{0.44,0.44,0.10,0.02};//和id对应
	
	Random random = new Random(System.currentTimeMillis());
	public void 刷怪代理(){
		MonsterManager mm = MemoryMonsterManager.getMonsterManager();
		//刷新昆华古城的怪
		
		int[][] 昆华古城刷新 = 昆华古城刷新点[random.nextInt(昆华古城刷新点.length)];
		for(int i = 1; i <= 3; i++){
			Game game = GameManager.getInstance().getGameByName("kunhuagucheng", i);
			if(isFlushMonster(game)){
				for(int j = 0; j < 昆华古城刷新.length; j++){
					int index = ProbabilityUtils.randomProbability(random,昆华古城刷新怪物几率);
					int monsterCategoryId = 昆华古城刷新怪物id[index];
					Monster monster = mm.createMonster(monsterCategoryId);
					if(monster != null){
						monster.setLevel(SealManager.getInstance().getSealLevel());
						monster.setX(昆华古城刷新[j][0]);
						monster.setY(昆华古城刷新[j][1]);
						monster.setBornPoint(new Point(昆华古城刷新[j][0], 昆华古城刷新[j][1]));
						game.addSprite(monster);
					}
				}
			}

		}
		
		int[][] 昆仑圣殿刷新 = 昆仑圣殿刷新点[random.nextInt(昆仑圣殿刷新点.length)];
		for(int i = 1; i <= 3; i++){
			Game game = GameManager.getInstance().getGameByName("kunlunshengdian", i);
			if(isFlushMonster(game)){
				for(int j = 0; j < 昆仑圣殿刷新.length; j++){
					int index = ProbabilityUtils.randomProbability(random,昆仑圣殿刷新怪物几率);
					int monsterCategoryId = 昆仑圣殿刷新怪物id[index];
					Monster monster = mm.createMonster(monsterCategoryId);
					if(monster != null){
						monster.setLevel(SealManager.getInstance().getSealLevel());
						monster.setX(昆仑圣殿刷新[j][0]);
						monster.setY(昆仑圣殿刷新[j][1]);
						monster.setBornPoint(new Point(昆仑圣殿刷新[j][0], 昆仑圣殿刷新[j][1]));
						game.addSprite(monster);
					}
				}
			}
		}
		
		int[][] 灭魔神域刷新 = 灭魔神域刷新点[random.nextInt(灭魔神域刷新点.length)];
		{
			Game game = GameManager.getInstance().getGameByName("miemoshenyu", 0);
			if(isFlushMonster(game)){
				for(int j = 0; j < 灭魔神域刷新.length; j++){
					int index = ProbabilityUtils.randomProbability(random,灭魔神域刷新怪物几率);
					int monsterCategoryId = 灭魔神域刷新怪物id[index];
					Monster monster = mm.createMonster(monsterCategoryId);
					if(monster != null){
						monster.setLevel(SealManager.getInstance().getSealLevel());
						monster.setX(灭魔神域刷新[j][0]);
						monster.setY(灭魔神域刷新[j][1]);
						monster.setBornPoint(new Point(灭魔神域刷新[j][0],灭魔神域刷新[j][1]));
						game.addSprite(monster);
					}
				}
			}
		}
	}
	
	public boolean isFlushMonster(Game game){
		LivingObject[] los = game.getLivingObjects();
		if(los != null){
			int count = 0;
			for(int i = 0; i < los.length; i++){
				if(los[i] instanceof BossMonster){
					count++;
				}
				if(count >= 200){
					return false;
				}
			}
		}
		return true;
	}
	
	public Props p1;
	public Props p2;
	public Props p3;
	public Props p4;
	public Props p5;
	public Props p6;
	public Props p7;
	public Props p8;
	public Props p9;
	public Props p10;
	public Props p11;
	public Props p12;
	public Props p13;
	
	public void run(){
		Calendar calendar = Calendar.getInstance();
		ChatMessageService cms = ChatMessageService.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		long flushTime = 0;
		while(running){
			try{
				Thread.sleep(10000);
				if(!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()){
					continue;
				}
				long now = System.currentTimeMillis();
				calendar.setTimeInMillis(now);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				if(am == null){
					am = ArticleManager.getInstance();
				}
				if(p1 == null || p2 == null || p3 == null || p4 == null || p5 == null || p6 == null || p7 == null || p8 == null || p9 == null || p10 == null || p11 == null || p12 == null || p13 == null){
					p1 = (Props)am.getArticle("封魔录●降魔");
					p2 = (Props)am.getArticle("封魔录●逍遥");
					p3 = (Props)am.getArticle("封魔录●霸者");
					p4 = (Props)am.getArticle("封魔录●朱雀");
					p5 = (Props)am.getArticle("封魔录●水晶");
					p6 = (Props)am.getArticle("封魔录●倚天");
					p7 = (Props)am.getArticle("封魔录●青虹");
					p8 = (Props)am.getArticle("封魔录●赤霄");
					p9 = (Props)am.getArticle("封魔录●震天");
					p10 = (Props)am.getArticle("封魔录●天罡");
					p11 = (Props)am.getArticle("白玉泉");
					p12 = (Props)am.getArticle("金浆醒");
					p13 = (Props)am.getArticle("香桂郢酒");
				}
				if(day == 26 || day == 29 || day == 30 || day == 1 || day == 2 || day == 3 || day == 4 || day == 5 || day == 6 || day == 7){

					//把封魔录使用次数限制改为7
					p1.setMaxUsingTimes(7);
					p2.setMaxUsingTimes(7);
					p3.setMaxUsingTimes(7);
					p4.setMaxUsingTimes(7);
					p5.setMaxUsingTimes(7);
					p6.setMaxUsingTimes(7);
					p7.setMaxUsingTimes(7);
					p8.setMaxUsingTimes(7);
					p9.setMaxUsingTimes(7);
					p10.setMaxUsingTimes(7);
					
					//把酒的使用次数改为7
					p11.setMaxUsingTimes(7);
					p12.setMaxUsingTimes(7);
					p13.setMaxUsingTimes(7);

					//修改祈福基本次数
					CliffordManager.每天可以祈福的次数 = 13;
				}else{

					//把封魔录使用次数限制改为5
					p1.setMaxUsingTimes(5);
					p2.setMaxUsingTimes(5);
					p3.setMaxUsingTimes(5);
					p4.setMaxUsingTimes(5);
					p5.setMaxUsingTimes(5);
					p6.setMaxUsingTimes(5);
					p7.setMaxUsingTimes(5);
					p8.setMaxUsingTimes(5);
					p9.setMaxUsingTimes(5);
					p10.setMaxUsingTimes(5);
					
					//把酒的使用次数改为5
					p11.setMaxUsingTimes(5);
					p12.setMaxUsingTimes(5);
					p13.setMaxUsingTimes(5);
					
					//修改祈福基本次数
					CliffordManager.每天可以祈福的次数 = 8;
				}
				if(day == 27 || day == 1 || day == 2 || day == 3 || day == 4 || day == 5 || day == 6 || day == 7){
					int hour = calendar.get(Calendar.HOUR_OF_DAY);
					if(hour == 开始时间){
						if(now > flushTime + 刷新时间){
							flushTime = now;
							刷怪代理();
							cms.sendMessageToSystem(Translate.怪物攻城活动刷怪);
							logger.warn("[国庆怪物攻城] [刷新怪物] [成功]");
						}
					}else{
						logger.info("[国庆怪物攻城] [刷新怪物] [日期到了分钟未到] [小时:"+hour+"]");
					}
				}else{
					logger.info("[国庆怪物攻城] [刷新怪物] [时间日期未到] [day:"+day+"]");
				}
			}catch(Throwable t){
				logger.error("[国庆怪物攻城] [异常]",t);
			}
		}
	}
}
