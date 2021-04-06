package com.fy.engineserver.battlefield.dota;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.BattleFieldClosedListener;
import com.fy.engineserver.battlefield.BattleFieldInfo;
import com.fy.engineserver.battlefield.concrete.BattleFieldManager;
import com.fy.engineserver.battlefield.concrete.BattleFieldStatData;
import com.fy.engineserver.constants.Event;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplate_XueGongQiangFangYu;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.message.PLAYER_DEAD_REQ;
import com.fy.engineserver.message.PLAYER_REVIVED_REQ;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;

/**
 * DOTA 战场
 * 包含如下的元素：
 * 
 * 1. 主将
 * 
 * 2. 防御塔
 * 
 * 3. 兵营
 * 
 * 4. 兵，分为近战兵，远程兵，和投掷兵
 * 
 * 5. 采集NPC，采集任务NPC
 * 
 * 6. 各种商店NPC，辅助型NPC
 * 
 *
 */
public class DotaBattleField implements BattleField{

//	public static Logger logger = Logger.getLogger(BattleField.class);
public	static Logger logger = LoggerFactory.getLogger(BattleField.class);
	
	//
	public static final int BATTLE_TYPE_5v5 = 0;
	
	public static final int BATTLE_TYPE_10v10 = 1;

	public static final int BATTLE_TYPE_15v15 = 2;

	public static final int BATTLE_TYPE_20v20 = 3;
	
	public static final int PLAYER_NUM_LIMIT[] = new int[]{5,10,15,20};
	
	protected int battleType = BATTLE_SIDE_A;
	
	protected Point revivedPointForA = new Point(100,3100);
	
	protected Point revivedPointForB = new Point(3100,100);
	
	/**
	 * 获得一个随机出生点
	 * @param side
	 * @return
	 */
	public Point getRandomBornPoint(int side){
		if(side == BattleField.BATTLE_SIDE_A){
			return revivedPointForA;
		}else{
			return revivedPointForB;
		}
	}
	
	//yangtao 1005051447 start
	private ArrayList<BattleFieldClosedListener> closedListener=new ArrayList<BattleFieldClosedListener>();
	//yangtao 1005051447 end
	
	public int getBattleType(){
		return battleType;
	}
	
	public Point getRevivedPointForA(){
		return revivedPointForA;
	}
	
	public Point getRevivedPointForB(){
		return revivedPointForB;
	}
	
	protected BattleFieldInfo bi;
	
	/**
	 * 对应的地图
	 * 地图上有专门的怪物刷新和NPC刷新
	 * 
	 * DOTA战场上，
	 */
	protected Game game;
	
	protected long startTime = 0;
	
	int state;
	
	public int getState(){
		return state;
	}
	
	public long getStartTime(){
		return startTime;
	}
	
	
	protected long endTime = 0;
	
	
	public long getEndTime(){
		return endTime;
	}
	

	public int getMinPlayerLevel(){
		return bi.getMinPlayerLevel();
	}
	
	
	public int getMaxPlayerLevel(){
		return bi.getMaxPlayerLevel();
	}
	
	protected String id;
	
	protected String name;
	
	public String getName(){
		return bi.getName();
	}

	public DotaBattleField(String id,BattleFieldInfo bi){
		this.id = id;
		this.bi = bi;
	}
	
	/**
	 * 徽章
	 */
	protected Article article;
	
	public Article getArticle(){
		return article;
	}
	
	protected int 最大近程兵刷新数量 = 6;
	protected int 最大远程兵刷新数量 = 2;
	protected int 最大攻城兵刷新数量 = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////
	// 内部数据
	
	protected boolean firstBlood = false;
	
	protected int 近程兵刷新数量 = 3;
	protected int 远程兵刷新数量 = 1;
	protected int 攻城兵刷新数量 = 1;
	
	protected int 小兵等级 = 0;
	
	protected long 近程兵的刷新时间间隔 = 30 * 1000;
	protected long 远程兵的刷新时间间隔 = 30 * 1000;
	protected long 攻城兵的刷新时间间隔 = 270 * 1000;
	
	protected long 小兵升级时间间隔 = 420 * 1000;
	
	protected long 上一次小兵升级的时间 = 0;
	
	protected long 上一次刷新近程兵的时间 = 0;
	protected long 上一次刷新远程兵的时间 = 0;
	protected long 上一次刷新攻城兵的时间 = 0;
	

	protected long 增加近程兵的时间间隔 = (16 * 60 + 30) * 1000;
	protected long 增加远程兵的时间间隔 = (45*60) * 1000;
	protected long 增加攻城兵的时间间隔 = (45*60) * 1000;
	
	
	protected long 上一次增加近程兵的时间 = 0;
	protected long 上一次增加远程兵的时间 = 0;
	protected long 上一次增加攻城兵的时间 = 0;
	
	protected long 战场开始时间= 0;
	
	protected long 上一次温泉补血时间  = 0;
	protected long 温泉补血时间间隔  = 1000;
	protected int 温泉补血血量百分比  = 12;
	
	//战场中双方的各个对象
	protected DotaDefenseTower A方上路前塔 = null;
	protected DotaDefenseTower A方上路中塔 = null;
	protected DotaDefenseTower A方上路后塔 = null;
	
	protected DotaDefenseTower A方中路前塔 = null;
	protected DotaDefenseTower A方中路中塔 = null;
	protected DotaDefenseTower A方中路后塔 = null;
	
	protected DotaDefenseTower A方下路前塔 = null;
	protected DotaDefenseTower A方下路中塔 = null;
	protected DotaDefenseTower A方下路后塔 = null;

	protected DotaDefenseTower A方主将护塔1 = null;
	protected DotaDefenseTower A方主将护塔2 = null;

	protected DotaGeneral A方上路近程军营 = null;
	protected DotaGeneral A方上路远程军营 = null;
	
	protected DotaGeneral A方中路近程军营 = null;
	protected DotaGeneral A方中路远程军营 = null;
	
	protected DotaGeneral A方下路近程军营 = null;
	protected DotaGeneral A方下路远程军营 = null;
	
	protected DotaGeneral A方主将 = null;

	protected DotaDefenseTower A方温泉防御塔 = null;
	
	protected DotaDefenseTower B方上路前塔 = null;
	protected DotaDefenseTower B方上路中塔 = null;
	protected DotaDefenseTower B方上路后塔 = null;
	
	protected DotaDefenseTower B方中路前塔 = null;
	protected DotaDefenseTower B方中路中塔 = null;
	protected DotaDefenseTower B方中路后塔 = null;
	
	protected DotaDefenseTower B方下路前塔 = null;
	protected DotaDefenseTower B方下路中塔 = null;
	protected DotaDefenseTower B方下路后塔 = null;

	protected DotaDefenseTower B方主将护塔1 = null;
	protected DotaDefenseTower B方主将护塔2 = null;
	
	protected DotaGeneral B方上路近程军营 = null;
	protected DotaGeneral B方上路远程军营 = null;
	
	protected DotaGeneral B方中路近程军营 = null;
	protected DotaGeneral B方中路远程军营 = null;
	
	protected DotaGeneral B方下路近程军营 = null;
	protected DotaGeneral B方下路远程军营 = null;
	
	protected DotaGeneral B方主将 = null;
	
	protected DotaDefenseTower B方温泉防御塔 = null;
	
	protected int winSide = BATTLE_SIDE_C;
	
	//结束通知标记
	boolean flag_60000 = false;
	boolean flag_30000 = false;
	boolean flag_10000 = false;
	
	BuffTemplate_XueGongQiangFangYu 战场提升BUFF模板 = null;

	private boolean battleOver = false;
	
	public void reset(long startTime,long endTime){
		this.startTime = startTime;
		this.endTime = endTime;
		
		flag_60000 = false;
		flag_30000 = false;
		flag_10000 =false;
		
		state = BattleField.STATE_OPEN;
	}
	
	/**
	 * 获得战场中所有可能出现的怪或者NPC的类型
	 * @return
	 */
	public byte[] getAllSpriteTypeOnGame(){
		return new byte[] {(byte)149,(byte)150,(byte)151,(byte)152,(byte)153,(byte)154,(byte)155,41,
							94,93,92,90,26,29};
	}
	
	protected String sideNames[] = new String[3];
	/**
	 * 设置战场各方的名称
	 * 
	 * @param names
	 * @return
	 */
	public void setBattleFieldSideNames(String names[]){
		sideNames = names; 
	}
	
	public String[] getBattleFieldSideNames(){
		return sideNames;
	}
	/**
	 * 返回战场的战况描述
	 * @return
	 */
	public String getBattleFieldSituation(){
		return Translate.text_1931;
	}
	
	/**
	 * 战场是否在开放
	 * @return
	 */
	
	public boolean isOpen(){
		long l = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if(l > startTime && (l < endTime || endTime ==0)){
			return true;
		}
		return false;
	}
	/**
	 * 刷新小兵
	 * 
	 * @param soliderType
	 * @param num
	 */
	private void flushSolider(int soliderType,int num){
		DotaSolider soliders[] = null;
		
		//A方上路
		soliders = new DotaSolider[num];
		for(int i = 0 ; i < num ; i++){
			if(soliderType == DotaSolider.SOLIDER_TYPE_近程){
				soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方上路近程军营,Translate.text_1932);				
			}else{
				soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方上路远程军营,Translate.text_1933);				
			}						
			//soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方上路近程军营);
			DotaSolider d = soliders[i];
			
//			d.patrolingX = new int[]{347,393,380,670,2514,2895};
//			d.patrolingY = new int[]{2330,1688,1145,209,353,435};
			d.patrolingX = new int[]{393,380,670,2514,2895};
			d.patrolingY = new int[]{1688,1145,209,353,435};
			
			d.setX((int)(288 + 50*Math.random()));//338
			d.setY((int)(2308 + 100 * Math.random()));//2358
			
			d.setAlive(true);
			
			game.addSprite(d);
		}
		
		//A方中路 794,2620 -> 1241,2183 ->1464,1923 -> 1734,1579 -> 2020,1171 -> 2531,745 -> 2875,349


		soliders = new DotaSolider[num];
		for(int i = 0 ; i < num ; i++){
			if(soliderType == DotaSolider.SOLIDER_TYPE_近程){
				soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方中路近程军营,Translate.text_1934);				
			}else{
				soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方中路远程军营,Translate.text_1935);				
			}
			DotaSolider d = soliders[i];
			
//			d.patrolingX = new int[]{794,1241,1464,1734,2020,2531,2895};
//			d.patrolingY = new int[]{2620,2183,1923,1579,1171,745,435};
			d.patrolingX = new int[]{1241,1464,1734,2020,2531,2895};
			d.patrolingY = new int[]{2183,1923,1579,1171,745,435};
			
			d.setX((int)(732 + 100*Math.random()));//782
			d.setY((int)(2582 + 100 * Math.random()));//2622
			
			d.setAlive(true);
			
			game.addSprite(d);
		}
		
		//A方下路 911,2933 -> 1675,2988 -> 2752,2981 ->2914,2000 -> 2928,1613 -> 2813,917 -> 2875,349


		soliders = new DotaSolider[num];
		for(int i = 0 ; i < num ; i++){
			if(soliderType == DotaSolider.SOLIDER_TYPE_近程){
				soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方下路近程军营,Translate.text_1936);				
			}else{
				soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方下路远程军营,Translate.text_1937);				
			}			
			//soliders[i] = createDotaSolider(BATTLE_SIDE_A,soliderType,B方下路近程军营);
			DotaSolider d = soliders[i];
			
//			d.patrolingX = new int[]{911,1675,2752,2914,2928,2813,2895};
//			d.patrolingY = new int[]{2933,2988,2981,2000,1613,917,435};
			d.patrolingX = new int[]{1675,2752,2914,2928,2813,2895};
			d.patrolingY = new int[]{2988,2981,2000,1613,917,435};			
			
			d.setX((int)(920 + 100*Math.random()));//970 800
			d.setY((int)(2908 + 100 * Math.random()));//2958 2800
			
			d.setAlive(true);
			
			game.addSprite(d);
		}
		
		
		
		//B方上路2514,353 -> 670,246 -> 380,1145 -> 393,1688 -> 347,2330 -> 427,2823


		soliders = new DotaSolider[num];
		for(int i = 0 ; i < num ; i++){
			if(soliderType == DotaSolider.SOLIDER_TYPE_近程){
				soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方上路近程军营,Translate.text_1938);				
			}else{
				soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方上路远程军营,Translate.text_1939);				
			}						
			//soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方上路近程军营);
			DotaSolider d = soliders[i];
			
//			d.patrolingX = new int[]{2514,670,380,393,347,353};
//			d.patrolingY = new int[]{353,246,1145,1688,2330,2814};
			d.patrolingX = new int[]{670,380,393,347,353};
			d.patrolingY = new int[]{246,1145,1688,2330,2814};
			d.setX((int)(2476 + 100*Math.random()));//2526
			d.setY((int)(297+ 100 * Math.random()));//347
			
			d.setAlive(true);
			
			game.addSprite(d);
		}
		
		//B方中路2531,745 -> 2020,1171 -> 1734,1579 ->1464,1923 -> 1241,2183 ->794,2620 -> 427,2823


		soliders = new DotaSolider[num];
		for(int i = 0 ; i < num ; i++){
			if(soliderType == DotaSolider.SOLIDER_TYPE_近程){
				soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方中路近程军营,Translate.text_1940);				
			}else{
				soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方中路远程军营,Translate.text_1941);				
			}			
			//soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方中路近程军营);
			DotaSolider d = soliders[i];
			
//			d.patrolingX = new int[]{2531,2020,1734,1464,1241,794,353};
//			d.patrolingY = new int[]{745,1171,1579,1923,2183,2620,2814};
			d.patrolingX = new int[]{2020,1734,1464,1241,794,353};
			d.patrolingY = new int[]{1171,1579,1923,2183,2620,2814};
			
			d.setX((int)(2483 + 100*Math.random()));//2533
			d.setY((int)(673 + 50 * Math.random()));//723
			
			d.setAlive(true);
			
			game.addSprite(d);
		}
		
		
		//B方下路2813,917 -> 2928,1613 ->2914,2000 -> 2752,2981 -> 1675,2988 -> 911,2933 -> 427,2823
		soliders = new DotaSolider[num];
		for(int i = 0 ; i < num ; i++){
			if(soliderType == DotaSolider.SOLIDER_TYPE_近程){
				soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方中路近程军营,Translate.text_1942);				
			}else{
				soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方中路远程军营,Translate.text_1943);				
			}						
			//soliders[i] = createDotaSolider(BATTLE_SIDE_B,soliderType,A方下路近程军营);
			DotaSolider d = soliders[i];
			
//			d.patrolingX = new int[]{2813,2928,2914,2752,1675,911,353};
//			d.patrolingY = new int[]{917,1613,2000,2981,2988,2933,2814};
			d.patrolingX = new int[]{2928,2914,2752,1675,911,353};
			d.patrolingY = new int[]{1613,2000,2981,2988,2933,2814};
			
			d.setX((int)(2788 + 100*Math.random()));//2828
			d.setY((int)(844 + 50 * Math.random()));//894
			
			d.setAlive(true);
			
			game.addSprite(d);
		}
		
		if(logger.isInfoEnabled()){
//			logger.info("[刷新小兵] ["+name+"] ["+DotaSolider.NAMES[soliderType]+"] [数量："+num+"]");
			if(logger.isInfoEnabled())
				logger.info("[刷新小兵] [{}] [{}] [数量：{}]", new Object[]{name,DotaSolider.NAMES[soliderType],num});
		}
	}
	
	/**
	 * 创建一个小兵，不同的对方，不同的兵种，
	 * 要求设置好基本的属性包括：
	 *      小兵的形象
	 *      小兵的基本数值
	 *      小兵的技能
	 *      
	 * @param battleside
	 * @param soliderType
	 * @return
	 */
	private DotaSolider createDotaSolider(int battleside,int soliderType,DotaGeneral general,String desp){return null;}
	
	/**
	 * 创建一个塔，不同的对方
	 * 要求设置好基本的属性包括：
	 *      塔的形象
	 *      塔的基本数值
	 *      塔的技能
	 *      
	 * @param battleside
	 * @param soliderType
	 * @return
	 */
	private DotaDefenseTower createDotaDefenseTower(int battleside,int towerType){return null;}
	
	
	/**
	 * 创建一个建筑
	 * 要求设置好基本的属性包括：
	 *      建筑的形象
	 *      建筑的基本数值
	 *      建筑的技能
	 *      
	 * @param battleside
	 * @param soliderType
	 * @return
	 */
	private DotaGeneral createDotaGeneral(int battleside, boolean 是否为主将){return null;}
	public void init(){
		
		startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		endTime = startTime + bi.getMaxLifeTime();
		
		
		ArticleManager am = ArticleManager.getInstance();
		
		article = am.getArticle(DotaConstants.战场徽章);
		
		game.gi.setLimitFLY(true);
		game.gi.setLimitMOUNT(true);
		game.gi.setLimitQIECUO(true);
		
		战场提升BUFF模板 = new BuffTemplate_XueGongQiangFangYu();
		战场提升BUFF模板.setAdvantageous(true);
		战场提升BUFF模板.setCanUseType((byte)1);
		战场提升BUFF模板.setGroupId(-1);
		战场提升BUFF模板.setSyncWithClient(true);
		战场提升BUFF模板.setTimeType((byte)1);
		战场提升BUFF模板.setId(23456);
		战场提升BUFF模板.setName(Translate.text_1950);
		
		int percent[] = new int[DotaConstants.英雄的最大级别+1];
		for(int i = 0 ; i < percent.length ; i++){
			percent[i] = 4 * i;
		}
		战场提升BUFF模板.setPercent(percent);
		
		战场开始时间 = startTime;
		上一次刷新近程兵的时间 = startTime + 60*1000;
		上一次刷新远程兵的时间 = startTime + 60*1000;
		上一次刷新攻城兵的时间 = startTime;
		
		上一次增加近程兵的时间 = startTime + 30*1000;
		上一次增加远程兵的时间 = startTime;
		上一次增加攻城兵的时间 = startTime;
		
		上一次小兵升级的时间 = startTime;
		
		上一次温泉补血时间 = startTime;
		
		A方上路前塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_QIAN);
		A方上路前塔.setName(Translate.text_1951);
		A方上路前塔.setX(395);
		A方上路前塔.setY(1320);
		A方上路前塔.setAlive(true);
		game.addSprite(A方上路前塔);
		
		A方上路中塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_ZHONG);
		A方上路中塔.setName(Translate.text_1952);
		A方上路中塔.setX(421);
		A方上路中塔.setY(1794);
		A方上路中塔.setAlive(true);
		A方上路中塔.setInvulnerable(true);
		game.addSprite(A方上路中塔);
		
		A方上路后塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_HOU);
		A方上路后塔.setName(Translate.text_1953);
		A方上路后塔.setX(319);
		A方上路后塔.setY(2311);
		A方上路后塔.setAlive(true);
		game.addSprite(A方上路后塔);
		A方上路后塔.setInvulnerable(true);
		
		//
		A方中路前塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_QIAN);
		A方中路前塔.setName(Translate.text_1954);
		A方中路前塔.setX(1518);
		A方中路前塔.setY(1912);
		A方中路前塔.setAlive(true);
		game.addSprite(A方中路前塔);
		
		A方中路中塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_ZHONG);
		A方中路中塔.setName(Translate.text_1955);
		A方中路中塔.setX(1185);
		A方中路中塔.setY(2219);
		A方中路中塔.setAlive(true);
		game.addSprite(A方中路中塔);
		A方中路中塔.setInvulnerable(true);
		
		A方中路后塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_HOU);
		A方中路后塔.setName(Translate.text_1956);
		A方中路后塔.setX(841);
		A方中路后塔.setY(2583);
		A方中路后塔.setAlive(true);
		game.addSprite(A方中路后塔);
		A方中路后塔.setInvulnerable(true);
	
		A方下路前塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_QIAN);
		A方下路前塔.setName(Translate.text_1957);
		A方下路前塔.setX(2646);
		A方下路前塔.setY(3047);
		A方下路前塔.setAlive(true);
		game.addSprite(A方下路前塔);
		
		A方下路中塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_ZHONG);
		A方下路中塔.setName(Translate.text_1958);
		A方下路中塔.setX(1621);
		A方下路中塔.setY(2969);
		A方下路中塔.setAlive(true);
		game.addSprite(A方下路中塔);
		A方下路中塔.setInvulnerable(true);
		
		A方下路后塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_HOU);
		A方下路后塔.setName(Translate.text_1959);
		A方下路后塔.setX(1050);
		A方下路后塔.setY(3016);
		A方下路后塔.setAlive(true);
		game.addSprite(A方下路后塔);
		A方下路后塔.setInvulnerable(true);
		
		A方主将护塔1 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_JIDI);
		A方主将护塔1.setName(Translate.text_1960);
		A方主将护塔1.setX(311);
		A方主将护塔1.setY(2755);
		A方主将护塔1.setAlive(true);
		game.addSprite(A方主将护塔1);
		A方主将护塔1.setInvulnerable(true);
		
		A方主将护塔2 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_JIDI);
		A方主将护塔2.setName(Translate.text_1961);
		A方主将护塔2.setX(440);
		A方主将护塔2.setY(2831);
		A方主将护塔2.setAlive(true);
		game.addSprite(A方主将护塔2);
		A方主将护塔2.setInvulnerable(true);
		
		A方上路近程军营 = createDotaGeneral(BATTLE_SIDE_A,false);
		A方上路近程军营.setName(Translate.text_1962);
		A方上路近程军营.setX(255);
		A方上路近程军营.setY(2393);
		A方上路近程军营.setAlive(true);
		game.addSprite(A方上路近程军营);
		A方上路近程军营.setInvulnerable(true);
		
		A方上路远程军营 = createDotaGeneral(BATTLE_SIDE_A,false);
		A方上路远程军营.setName(Translate.text_1963);
		A方上路远程军营.setX(423);
		A方上路远程军营.setY(2362);
		A方上路远程军营.setAlive(true);
		game.addSprite(A方上路远程军营);
		A方上路远程军营.setInvulnerable(true);
		
		A方中路近程军营 = createDotaGeneral(BATTLE_SIDE_A,false);
		A方中路近程军营.setName(Translate.text_1964);
		A方中路近程军营.setX(696);
		A方中路近程军营.setY(2592);
		A方中路近程军营.setAlive(true);
		game.addSprite(A方中路近程军营);
		A方中路近程军营.setInvulnerable(true);
		
		A方中路远程军营 = createDotaGeneral(BATTLE_SIDE_A,false);
		A方中路远程军营.setName(Translate.text_1965);
		A方中路远程军营.setX(848);
		A方中路远程军营.setY(2672);
		A方中路远程军营.setAlive(true);
		game.addSprite(A方中路远程军营);
		A方中路远程军营.setInvulnerable(true);
		
		A方下路近程军营 = createDotaGeneral(BATTLE_SIDE_A,false);
		A方下路近程军营.setName(Translate.text_1966);
		A方下路近程军营.setX(864);
		A方下路近程军营.setY(2985);
		A方下路近程军营.setAlive(true);
		game.addSprite(A方下路近程军营);
		A方下路近程军营.setInvulnerable(true);
		
		A方下路远程军营 = createDotaGeneral(BATTLE_SIDE_A,false);
		A方下路远程军营.setName(Translate.text_1967);
		A方下路远程军营.setX(993);
		A方下路远程军营.setY(2896);
		A方下路远程军营.setAlive(true);
		game.addSprite(A方下路远程军营);
		A方下路远程军营.setInvulnerable(true);
		
		A方主将 = createDotaGeneral(BATTLE_SIDE_A,true);
		A方主将.setName(Translate.text_1948);
		A方主将.setX(353);
		A方主将.setY(2814);
		A方主将.setAlive(true);
		game.addSprite(A方主将);
		A方主将.setInvulnerable(true);

	
		A方温泉防御塔 = createDotaDefenseTower(BATTLE_SIDE_A,DotaDefenseTower.TOWERTYPE_WENQUAN);
		A方温泉防御塔.setName(Translate.text_1968);
		A方温泉防御塔.setX(29);
		A方温泉防御塔.setY(3174);
		A方温泉防御塔.setAlive(true);
		game.addSprite(A方温泉防御塔);
		A方温泉防御塔.setInvulnerable(true);
		
		
		//////////////////////////////////////////////////////////////////////
		
		B方上路前塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_QIAN);
		B方上路前塔.setName(Translate.text_1951);
		B方上路前塔.setX(750);
		B方上路前塔.setY(223);
		B方上路前塔.setAlive(true);
		game.addSprite(B方上路前塔);
		
		B方上路中塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_ZHONG);
		B方上路中塔.setName(Translate.text_1952);
		B方上路中塔.setX(1745);
		B方上路中塔.setY(265);
		B方上路中塔.setAlive(true);
		game.addSprite(B方上路中塔);
		B方上路中塔.setInvulnerable(true);
		
		B方上路后塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_HOU);
		B方上路后塔.setName(Translate.text_1953);
		B方上路后塔.setX(2452);
		B方上路后塔.setY(325);
		B方上路后塔.setAlive(true);
		game.addSprite(B方上路后塔);
		B方上路后塔.setInvulnerable(true);
		
		//
		B方中路前塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_QIAN);
		B方中路前塔.setName(Translate.text_1954);
		B方中路前塔.setX(1778);
		B方中路前塔.setY(1546);
		B方中路前塔.setAlive(true);
		game.addSprite(B方中路前塔);
		
		B方中路中塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_ZHONG);
		B方中路中塔.setName(Translate.text_1955);
		B方中路中塔.setX(2052);
		B方中路中塔.setY(1134);
		B方中路中塔.setAlive(true);
		game.addSprite(B方中路中塔);
		B方中路中塔.setInvulnerable(true);
		
		B方中路后塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_HOU);
		B方中路后塔.setName(Translate.text_1956);
		B方中路后塔.setX(2466);
		B方中路后塔.setY(773);
		B方中路后塔.setAlive(true);
		game.addSprite(B方中路后塔);
		B方中路后塔.setInvulnerable(true);
	
		B方下路前塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_QIAN);
		B方下路前塔.setName(Translate.text_1957);
		B方下路前塔.setX(2881);
		B方下路前塔.setY(1977);
		B方下路前塔.setAlive(true);
		game.addSprite(B方下路前塔);
		
		B方下路中塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_ZHONG);
		B方下路中塔.setName(Translate.text_1958);
		B方下路中塔.setX(2913);
		B方下路中塔.setY(1590);
		B方下路中塔.setAlive(true);
		game.addSprite(B方下路中塔);
		B方下路中塔.setInvulnerable(true);
		
		B方下路后塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_HOU);
		B方下路后塔.setName(Translate.text_1959);
		B方下路后塔.setX(2875);
		B方下路后塔.setY(967);
		B方下路后塔.setAlive(true);
		game.addSprite(B方下路后塔);
		B方下路后塔.setInvulnerable(true);
		
		B方主将护塔1 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_JIDI);
		B方主将护塔1.setName(Translate.text_1960);
		B方主将护塔1.setX(2819);
		B方主将护塔1.setY(422);
		B方主将护塔1.setAlive(true);
		game.addSprite(B方主将护塔1);
		B方主将护塔1.setInvulnerable(true);
		
		B方主将护塔2 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_JIDI);
		B方主将护塔2.setName(Translate.text_1961);
		B方主将护塔2.setX(2942);
		B方主将护塔2.setY(493);
		B方主将护塔2.setAlive(true);
		game.addSprite(B方主将护塔2);
		B方主将护塔2.setInvulnerable(true);
		
		B方上路近程军营 = createDotaGeneral(BATTLE_SIDE_B,false);
		B方上路近程军营.setName(Translate.text_1962);
		B方上路近程军营.setX(2600);
		B方上路近程军营.setY(288);
		B方上路近程军营.setAlive(true);
		game.addSprite(B方上路近程军营);
		B方上路近程军营.setInvulnerable(true);
		
		B方上路远程军营 = createDotaGeneral(BATTLE_SIDE_B,false);
		B方上路远程军营.setName(Translate.text_1963);
		B方上路远程军营.setX(2481);
		B方上路远程军营.setY(424);
		B方上路远程军营.setAlive(true);
		game.addSprite(B方上路远程军营);
		B方上路远程军营.setInvulnerable(true);		
		
		B方中路近程军营 = createDotaGeneral(BATTLE_SIDE_B,false);
		B方中路近程军营.setName(Translate.text_1964);
		B方中路近程军营.setX(2504);
		B方中路近程军营.setY(671);
		B方中路近程军营.setAlive(true);
		game.addSprite(B方中路近程军营);
		B方中路近程军营.setInvulnerable(true);
		
		B方中路远程军营 = createDotaGeneral(BATTLE_SIDE_B,false);
		B方中路远程军营.setName(Translate.text_1965);
		B方中路远程军营.setX(2624);
		B方中路远程军营.setY(737);
		B方中路远程军营.setAlive(true);
		game.addSprite(B方中路远程军营);
		B方中路远程军营.setInvulnerable(true);		
		
		B方下路近程军营 = createDotaGeneral(BATTLE_SIDE_B,false);
		B方下路近程军营.setName(Translate.text_1966);
		B方下路近程军营.setX(2743);
		B方下路近程军营.setY(914);
		B方下路近程军营.setAlive(true);
		game.addSprite(B方下路近程军营);
		B方下路近程军营.setInvulnerable(true);
		
		B方下路远程军营 = createDotaGeneral(BATTLE_SIDE_B,false);
		B方下路远程军营.setName(Translate.text_1967);
		B方下路远程军营.setX(2873);
		B方下路远程军营.setY(840);
		B方下路远程军营.setAlive(true);
		game.addSprite(B方下路远程军营);
		B方下路远程军营.setInvulnerable(true);
				
		B方主将 = createDotaGeneral(BATTLE_SIDE_B,true);
		B方主将.setName(Translate.text_1948);
		B方主将.setX(2895);
		B方主将.setY(435);
		B方主将.setAlive(true);
		game.addSprite(B方主将);
		B方主将.setInvulnerable(true);
		
		B方温泉防御塔 = createDotaDefenseTower(BATTLE_SIDE_B,DotaDefenseTower.TOWERTYPE_WENQUAN);
		B方温泉防御塔.setName(Translate.text_1968);
		B方温泉防御塔.setX(3120);
		B方温泉防御塔.setY(129);
		B方温泉防御塔.setAlive(true);
		game.addSprite(B方温泉防御塔);
		B方温泉防御塔.setInvulnerable(true);
		
		this.state = BattleField.STATE_FIGHTING;
		
		if(logger.isInfoEnabled()){
//			logger.info("[战场初始完成] ["+this.getName()+"]");
			if(logger.isInfoEnabled())
				logger.info("[战场初始完成] [{}]", new Object[]{this.getName()});
		}

	}
	
	
	public Game getGame(){
		return game;
	}
	
	public Player[] getPlayers(){
		LivingObject ll[] = game.getLivingObjects();
		ArrayList<Player> al = new ArrayList<Player>();
		for(LivingObject o : ll){
			if(o instanceof Player){
				al.add((Player)o);
			}
		}
		return al.toArray(new Player[0]);
	}
	
	public Player[] getPlayersBySide(int side){
		LivingObject ll[] = game.getLivingObjects();
		ArrayList<Player> al = new ArrayList<Player>();
		for(LivingObject o : ll){
			if(o instanceof Player){
				Player p = (Player)o;
				if(p.getBattleFieldSide() == side){
					al.add((Player)o);
				}
			}
		}
		return al.toArray(new Player[0]);
	}
	
	/**
	 * 心跳
	 */
	public void heartbeat(){
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		if(now < startTime){
			return;
		}
		
		if(now > endTime && endTime > 0){
			Player players[] = getPlayers();
			for(int i = 0 ; i < players.length ; i++){
				TransportData td = new TransportData(0,0,0,0,players[i].getEnterBattleFieldMapName(),players[i].getEnterBattleFieldX(),players[i].getEnterBattleFieldY());
				game.transferGame(players[i], td);
			}
			
			BattleFieldManager m = BattleFieldManager.getInstance();
			m.notidyBattleFieldEnd(this);

			battleFieldClosed(winSide);
			
			state = BattleField.STATE_CLOSE;
			
			return;
		}
		
		if(flag_10000 == false && endTime > 0 && endTime - now < 10000){
			flag_10000 = true;
			Player players[] = getPlayers();
			for(int i = 0 ; i < players.length ; i++){
				players[i].send_HINT_REQ(game.gi.getName()+Translate.text_1969);
			}
		}else if(flag_30000 == false && endTime > 0 && endTime - now < 30000){
			flag_30000 = true;
			Player players[] = getPlayers();
			for(int i = 0 ; i < players.length ; i++){
				players[i].send_HINT_REQ(game.gi.getName()+Translate.text_1970);
			}
		}else if(flag_60000 == false && endTime > 0 && endTime - now < 60000){
			flag_60000 = true;
			Player players[] = getPlayers();
			for(int i = 0 ; i < players.length ; i++){
				players[i].send_HINT_REQ(game.gi.getName()+Translate.text_1971);
			}
		}
		if(battleOver == false && now - 上一次小兵升级的时间 >= 小兵升级时间间隔){
			上一次小兵升级的时间 = now;
			小兵等级++;
		}
		
		if(battleOver == false && now - 上一次增加近程兵的时间 >= 增加近程兵的时间间隔){
			上一次增加近程兵的时间 = now;
			if(近程兵刷新数量 < 最大近程兵刷新数量){
				近程兵刷新数量++;
			}
		}
		
		if(battleOver == false && now - 上一次增加远程兵的时间 >= 增加远程兵的时间间隔){
			上一次增加远程兵的时间 = now;
			if(远程兵刷新数量 < 最大远程兵刷新数量){
				远程兵刷新数量++;
			}
		}
		
		
		if(battleOver == false && now - 上一次增加攻城兵的时间 >= 增加攻城兵的时间间隔){
			上一次增加攻城兵的时间 = now;
			if(攻城兵刷新数量 < 最大攻城兵刷新数量){
				攻城兵刷新数量++;
			}
		}
		
		
		if(battleOver == false && now - 上一次刷新近程兵的时间 >= 近程兵的刷新时间间隔){
			上一次刷新近程兵的时间 = now;
			
			flushSolider(DotaSolider.SOLIDER_TYPE_近程,近程兵刷新数量);
		}
		
		if(battleOver == false && now - 上一次刷新远程兵的时间 >= 远程兵的刷新时间间隔){
			上一次刷新远程兵的时间 = now;
			
			flushSolider(DotaSolider.SOLIDER_TYPE_远程,远程兵刷新数量);
			
		}
		
		if(battleOver == false && now - 上一次刷新攻城兵的时间 >= 攻城兵的刷新时间间隔){
			上一次刷新攻城兵的时间 = now;
			
			flushSolider(DotaSolider.SOLIDER_TYPE_攻城,攻城兵刷新数量);
		}
		
		if(battleOver == false &&  now - 上一次温泉补血时间 >= 温泉补血时间间隔){
			上一次温泉补血时间 = now;
			
			wenQuanBuXue();
		}
		
		game.heartbeat();
	}
	
	/**
	 * 温泉补血
	 */
	private void wenQuanBuXue(){}
	
	
	public static class BattleKillRecord{
		
		public static final int KILL_TYPE_BEKILL = 0;
		
		public static final int KILL_TYPE_KILLHERO = 1;
		
		public Player owner;
		public int type;
		public long time;
		
		public BattleKillRecord(Player owner,int type){
			this.owner = owner;
			this.type = type;
			time = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		}
	}
	
	private Hashtable<Long,ArrayList<BattleKillRecord>> battleRecordListMap = new Hashtable<Long,ArrayList<BattleKillRecord>>();
	
	public synchronized ArrayList<BattleKillRecord> getBattleKillRecordList(Player p){
		ArrayList<BattleKillRecord> al = battleRecordListMap.get(p.getId());
		if(al == null){
			al = new ArrayList<BattleKillRecord>();
			battleRecordListMap.put(p.getId(), al);
		}
		return al;
	}
	
	/**
	 * 通知系统，那个英雄被打死了，
	 * 此方法需要进行系统通知
	 * 此方法只是进行统计工作，不进行复活工作
	 * 
	 * @param killer
	 * @param killed
	 */
	protected void notifyKillingPlayer(Fighter killer,Player killed){}
	
	/**
	 * 杀死一个小兵
	 * @param killer
	 * @param solider
	 */
	protected void notifyKillingSolider(Fighter killer,DotaSolider solider){}
	
	/**
	 * 给玩家创建一个新的BUFF
	 * @param p
	 * @param level
	 * @return
	 */
	private Buff createLevelBuff(Player p,int level){
		
		Buff buff = 战场提升BUFF模板.createBuff(level);
		buff.setCauser(p);
		buff.setTemplate(战场提升BUFF模板);
		buff.setGroupId(战场提升BUFF模板.getGroupId());
		buff.setLevel(level);
		
		buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		buff.setInvalidTime(buff.getStartTime() + 4 * 3600 * 1000L);
		buff.setForover(true);
		
		return buff;
	}
	
	/**
	 * 给一个玩家增加经验值，
	 * 此方法要处理玩家的战场升级
	 * @param p
	 * @param exp
	 */
	private void addPlayerExp(Player p,int exp){
		DotaPlayerInfo pi = this.getDotaPlayerInfo(p);
		pi.setBattleExp(pi.getBattleExp() + exp);
		exp = pi.getBattleExp();
		int level = 0;
		for(int i = 0 ; i < DotaConstants.经验值和基本对应表.length - 1; i++){
			if(DotaConstants.经验值和基本对应表[i] <= exp && exp < DotaConstants.经验值和基本对应表[i+1]){
				level = i+1;
				break;
			}
		}
		if(level > DotaConstants.英雄的最大级别){
			level = DotaConstants.英雄的最大级别;
		}
		
		//玩家升级，发送消息通知玩家
		//给玩家增加新的等级的Buff
		if(pi.getBattleLevel() < level){
			pi.setBattleLevel(level);
			
			Buff buff = createLevelBuff(p,level);
			if(buff != null)
				p.placeBuff(buff);
			
			NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, p.getId(),
					(byte) Event.LEVEL_UPGRADE, level);
			p.addMessageToRightBag(req);

		}
	}
	
	/**
	 * 杀死一个塔
	 * @param killer
	 * @param solider
	 */
	protected void notifyKillingTower(Fighter killer,DotaDefenseTower tower){}
	
	/**
	 * 杀死一个建筑物，包括兵营，主将
	 * @param killer
	 * @param solider
	 */
	protected void notifyKillingGeneral(Fighter killer,DotaGeneral general){}
	
	private void notifyBattleOver(int side){
		this.state = BattleField.STATE_STOPFIGHTING;
		this.battleOver = true;
		String message = Translate.text_2003+DotaConstants.对阵双方的名称[side] +Translate.text_2004;
		notifyOneSidePlayer(message, BATTLE_SIDE_A);
		notifyOneSidePlayer(message, BATTLE_SIDE_B);
		
		LivingObject los[] = game.getLivingObjects();
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof DotaSolider){
				DotaSolider d = (DotaSolider)los[i];
				if(d.getBattleFieldSide() != side){
					d.setAlive(false);
				}
			}else if(los[i] instanceof Player){
				Player p = (Player)los[i];
				if(p.getBattleFieldSide() == side){
					 p.winOneBattleField(bi.getMapName());
				}
			}
		}
		
		
	}
	
	/**
	 * 玩家死亡，需要读秒，之后复活
	 * 
	 * @param player
	 */
	public void playerDead(Player player){
		DotaPlayerInfo pi = this.getDotaPlayerInfo(player);
		int k = pi.getBattleLevel() * 2 + 15;
		PLAYER_DEAD_REQ req = new PLAYER_DEAD_REQ(GameMessageFactory.nextSequnceNum(), player.getId(), k);
		player.addMessageToRightBag(req);
		pi.setBattleLastDeadTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
	}
	
	/**
	 * 收到玩家复活指令
	 * 
	 * @param player
	 * @param req
	 */
	public void playerRevived(Player player,PLAYER_REVIVED_REQ req){}
	
	/**
	 * 通知战场中某一方的玩家信息
	 * @param message
	 * @param side
	 */
	protected void notifyOneSidePlayer(String message,int side){
		LivingObject los[] = game.getLivingObjects();
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof Player){
				Player p = (Player)los[i];
				if( p.getBattleFieldSide() == side){
					p.send_HINT_REQ(message);
				}
			}
		}
	}
	
	/**
	 * 有音效的提示
	 * @param message
	 * @param side
	 */
	protected void notifySoundOneSidePlayer(String message,int side){
		LivingObject los[] = game.getLivingObjects();
		for(int i = 0 ; i < los.length ; i++){
			if(los[i] instanceof Player){
				Player p = (Player)los[i];
				if( p.getBattleFieldSide() == side){
					//p.send_HINT_REQ(message);
					//HINT_REQ req = new HINT_REQ();
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)2, message);
					p.addMessageToRightBag(req);
				}
			}
		}
	}
	
	Vector<Long> basttlePlayers = new Vector<Long>();
	
	/**
	 * 玩家进入战场
	 * @param player
	 * @param mapName 玩家进入战场时所在的地图
	 * @param x 玩家进入战场是的位置
	 * @param y 玩家进入战场是的位置
	 */
	public void notifyPlayerEnter(Player player){
		basttlePlayers.addElement(player.getId());
		getBattleFieldStatDataByPlayer(player);
		
		//使得Around_change通知客户端
		player.setBattleFieldSide(player.getBattleFieldSide());
	}
	
	/**
	 * 玩家从战场离开
	 * @param player
	 */
	public void notifyPlayerLeave(Player player){}
	
	/**
	 * 战场意外结束
	 */
	public void notifyBattleFieldEndCauseSystemExit(){
		for(int i=0;i<basttlePlayers.size();i++){
			long playerId = basttlePlayers.elementAt(i);
			removePlayerItem(playerId);
		}
		battleFieldClosed(winSide);
	}
	
	public void removePlayerItem(long playerId) {}
	
	//yangtao 1005051447 start
	private void battleFieldClosed(int side){
		for(BattleFieldClosedListener listener:this.closedListener){
			listener.battleFieldClosed(this);
		}
	}
	
	public void addBattleFieldClosedListener(BattleFieldClosedListener listener){
		this.closedListener.add(listener);
	}
	//yangtao 1005051447 end
	
	public int getKillNumBySide(int side){
		int killNum = 0;
		LivingObject ll[] = game.getLivingObjects();
		for(LivingObject o : ll){
			if(o instanceof Player){
				Player p = (Player)o;
				if(p.getBattleFieldSide() == side){
					ArrayList<BattleKillRecord> al = getBattleKillRecordList(p);
					for(int i = al.size() -1 ; i >= 0 ; i--){
						BattleKillRecord pb = al.get(i);
						if(pb.type == BattleKillRecord.KILL_TYPE_KILLHERO){
							killNum ++;
						}else{
							break;
						}
					}
					//killNum += p.getBattleKillHeroNum();
				}
			}
		}
		return killNum;
	}

	
	
	public int getBattleFightingType() {
		// TODO Auto-generated method stub
		return BattleField.BATTLE_FIGHTING_TYPE_对战;
	}

	
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	
	public long getLastingTimeForNotEnoughPlayers() {
		
		return bi.getLastingTimeForNotEnoughPlayers();
	}

	
	public int getMaxPlayerNumOnOneSide() {
		
		return bi.getMaxPlayerNumOnOneSide();
	}

	
	public int getMinPlayerNumForStartOnOneSide() {
		// TODO Auto-generated method stub
		return bi.getMinPlayerNumForStartOnOneSide();
	}

	
	public long getStartFightingTime() {
		// TODO Auto-generated method stub
		return startTime + bi.getStartFightingTime();
	}

	
	public Hashtable<Long, BattleFieldStatData> getStatDataMap() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public int getWinnerSide() {
		// TODO Auto-generated method stub
		return this.winSide;
	}

	
	//统计每个玩家
	protected Hashtable<Long,BattleFieldStatData> statDataMap = new Hashtable<Long,BattleFieldStatData>();

	
	private synchronized BattleFieldStatData getBattleFieldStatDataByPlayer(Player p){
		BattleFieldStatData sd = statDataMap.get(p.getId());
		if(sd == null){
			sd = new BattleFieldStatData();
			CareerManager cm = CareerManager.getInstance();
			sd.setCareer(cm.getCareer(p.getCareer()).getName());
			sd.setPlayerId(p.getId());
			sd.setPlayerLevel( p.getLevel());

			sd.setPlayerName(p.getName());
			sd.setBattleSide(p.getBattleFieldSide());
			sd.setDescription("");
			statDataMap.put(p.getId(), sd);
		}
		return sd;
	}

	
	public void notifyCauseDamage(Fighter causter, Fighter target, int damage) {
		if(causter instanceof Player){
			Player p = (Player)causter;
			
			BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
			sd.setTotalDamage(sd.getTotalDamage() + damage);
		}
		
	}

	
	public void notifyEnhenceHp(Fighter causter, Fighter target, int hp) {
		
		if(causter instanceof Player){
			Player p = (Player)causter;
			
			BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
			sd.setTotalEnhenceHp(sd.getTotalEnhenceHp()+ hp);
		}
		
	}

	
	public void notifyKilling(Fighter killer, Fighter killed) {
		
		if(killer instanceof Player && killed instanceof Player){
			Player p = (Player)killer;
			
			BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
			sd.setKillingNum(sd.getKilledNum()+1);
			
			//p.send_HINT_REQ("你击杀了对方阵营的["+killed.getName()+"]");
			
		}
		
		if(killed instanceof Player){
			Player p = (Player)killed;
			
			BattleFieldStatData sd = getBattleFieldStatDataByPlayer(p);
			sd.setKilledNum(sd.getKilledNum()+1) ;
			
			this.notifyKillingPlayer(killer, (Player)killed);
		}
		
	}

	
	public void setGame(Game game) {
		this.game = game;
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////
	
	public static class DotaPlayerInfo{
		//战场的经验值
		protected int battleExp = 0;
		
		//战场里的英雄等级
		protected int battleLevel = 1;
		
		//玩家在这场战争中杀了多少个英雄
		protected int battleKillHeroNum = 0;
		
		protected int battleKillTowerNum = 0;
		
		protected int battleKillSoliderNum = 0;
		
		protected int battleBeKillNum = 0;
		
		protected int battleScore = 0;
		
		protected long battleLastDeadTime = 0;

		public int getBattleExp() {
			return battleExp;
		}

		public void setBattleExp(int battleExp) {
			this.battleExp = battleExp;
		}

		public int getBattleLevel() {
			return battleLevel;
		}

		public void setBattleLevel(int battleLevel) {
			this.battleLevel = battleLevel;
		}

		public int getBattleKillHeroNum() {
			return battleKillHeroNum;
		}

		public void setBattleKillHeroNum(int battleKillHeroNum) {
			this.battleKillHeroNum = battleKillHeroNum;
		}

		public int getBattleKillTowerNum() {
			return battleKillTowerNum;
		}

		public void setBattleKillTowerNum(int battleKillTowerNum) {
			this.battleKillTowerNum = battleKillTowerNum;
		}

		public int getBattleKillSoliderNum() {
			return battleKillSoliderNum;
		}

		public void setBattleKillSoliderNum(int battleKillSoliderNum) {
			this.battleKillSoliderNum = battleKillSoliderNum;
		}

		public int getBattleBeKillNum() {
			return battleBeKillNum;
		}

		public void setBattleBeKillNum(int battleBeKillNum) {
			this.battleBeKillNum = battleBeKillNum;
		}

		public int getBattleScore() {
			return battleScore;
		}

		public void setBattleScore(int battleScore) {
			this.battleScore = battleScore;
		}

		public long getBattleLastDeadTime() {
			return battleLastDeadTime;
		}

		public void setBattleLastDeadTime(long battleLastDeadTime) {
			this.battleLastDeadTime = battleLastDeadTime;
		}
		
		
	}
	
	//统计每个玩家
	protected Hashtable<Long,DotaPlayerInfo> playerInfoMap = new Hashtable<Long,DotaPlayerInfo>();
	
	/**
	 * 获得玩家在战场中的信息
	 * @param p
	 * @return
	 */
	public DotaPlayerInfo getDotaPlayerInfo(Player p){
		synchronized(playerInfoMap){
			DotaPlayerInfo pi = playerInfoMap.get(p.getId());
			if(pi == null){
				pi = new DotaPlayerInfo();
				playerInfoMap.put(p.getId(), pi);
			}
			return pi;
		}
	}

	@Override
	public void notifyCauseDamageOfReal(Fighter causter, int damage) {
		// TODO Auto-generated method stub
		
	}

}
