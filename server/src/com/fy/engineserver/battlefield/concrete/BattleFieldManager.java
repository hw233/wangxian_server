package com.fy.engineserver.battlefield.concrete;

import java.util.ArrayList;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.battlefield.BattleField;
import com.fy.engineserver.battlefield.BattleFieldBeatHeartThread;
import com.fy.engineserver.battlefield.BattleFieldInfo;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.npc.MemoryNPCManager;
import com.fy.engineserver.sprite.npc.NPC;
import com.fy.engineserver.tournament.manager.TournamentManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.StringUtil;



/**
 * 战场管理类
 *
 */
public class BattleFieldManager {
	
//	static Logger logger = Logger.getLogger(BattleField.class);
public	static Logger logger = LoggerFactory.getLogger(TournamentManager.class.getName());
	
	private static BattleFieldManager self;
	
	public static BattleFieldManager getInstance(){
		return self;
	}
	
	//战场的配置信息，分为几种战场
	//每个战场的名字必须唯一
	//所有战场信息的模板数据
	protected ArrayList<BattleFieldInfo> battleFieldInfos = new ArrayList<BattleFieldInfo>();
	
	//内存中所有的战场信息
	protected Hashtable<String,ArrayList<BattleField>> battleFieldMap = new Hashtable<String,ArrayList<BattleField>>();
	
	boolean running = true;
	int threadNum = 10;//50
	
	BattleFieldBeatHeartThread[] threads;
	
	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}
	/**
	 * 通过名字获得一个战场的信息
	 * @param name
	 * @return
	 */
	public BattleFieldInfo getBattleFieldInfoByName(String name){
		for(BattleFieldInfo bf : battleFieldInfos){
			if(bf.getName().equals(name)){
				return bf;
			}
		}
		return null;
	}
	
	
	/**
	 * 获得所有战场信息
	 * @return
	 */
	public BattleFieldInfo[] getBattleFieldInfos(){
		return battleFieldInfos.toArray(new BattleFieldInfo[0]);
	}
	
	/**
	 * 根据名字，得到内存中存在的所有的战场
	 * @param name
	 * @return
	 */
	public BattleField[] getBattleFieldsByName(String name){
		ArrayList<BattleField> al = battleFieldMap.get(name);
		if(al == null){
			return new BattleField[0];
		}
		return al.toArray(new BattleField[0]);
	}
	
	/**
	 * 判断某个地图是不是一个战场地图
	 * 
	 * @param mapName
	 * @return
	 */
	public boolean isBattleFieldGame(String mapName){
		BattleFieldInfo bis[] = getBattleFieldInfos();
		for(int i = 0 ; i < bis.length ; i++){
			if(bis[i].getMapName().equals(mapName)){
				return true;
			}
		}
		return false;
	}
	public void init() throws Exception{
		
		threads = new BattleFieldBeatHeartThread[threadNum];
		for(int i = 0 ; i < threads.length ; i++){
			threads[i] = new BattleFieldBeatHeartThread();
			threads[i].setName("战场心跳线程"+(i+1));
			threads[i].setBeatheart(5);
			threads[i].start();
		}
		
		BattleFieldInfo bi = null;

		
		bi = new BattleFieldInfo();
		bi.setBattleCategory(BattleFieldInfo.BATTLE_CATEGORY_仙武大会1);
		bi.setBattleFightingType(BattleField.BATTLE_FIGHTING_TYPE_对战);
		bi.setHolidyRewardParam(1);
		bi.setBattleRewardParam(30);
		bi.setLastingTimeForNotEnoughPlayers(30000L);
		bi.setMapName(BattleFieldInfo.仙武大会战场地图数组[0]);
		bi.setMaxLifeTime(270*1000L);
		bi.setMaxPlayerLevel(100);
		bi.setMinPlayerLevel(0);
		bi.setMinPlayerNumForStartOnOneSide(1);
		bi.setMaxPlayerNumOnOneSide(1);
		bi.setName(BattleFieldInfo.仙武大会战场地图数组[0]);
		bi.setStartFightingTime(60 * 1000L);
		bi.setDescription(Translate.text_1900);
		battleFieldInfos.add(bi);
		
		bi = new BattleFieldInfo();
		bi.setBattleCategory(BattleFieldInfo.BATTLE_CATEGORY_仙武大会2);
		bi.setBattleFightingType(BattleField.BATTLE_FIGHTING_TYPE_对战);
		bi.setHolidyRewardParam(1);
		bi.setBattleRewardParam(30);
		bi.setLastingTimeForNotEnoughPlayers(30000L);
		bi.setMapName(BattleFieldInfo.仙武大会战场地图数组[1]);
		bi.setMaxLifeTime(270*1000L);
		bi.setMaxPlayerLevel(100);
		bi.setMinPlayerLevel(0);
		bi.setMinPlayerNumForStartOnOneSide(1);
		bi.setMaxPlayerNumOnOneSide(1);
		bi.setName(BattleFieldInfo.仙武大会战场地图数组[1]);
		bi.setStartFightingTime(60 * 1000L);
		bi.setDescription(Translate.text_1900);
		battleFieldInfos.add(bi);
		
		bi = new BattleFieldInfo();
		bi.setBattleCategory(BattleFieldInfo.BATTLE_CATEGORY_仙武大会3);
		bi.setBattleFightingType(BattleField.BATTLE_FIGHTING_TYPE_对战);
		bi.setHolidyRewardParam(1);
		bi.setBattleRewardParam(30);
		bi.setLastingTimeForNotEnoughPlayers(30000L);
		bi.setMapName(BattleFieldInfo.仙武大会战场地图数组[2]);
		bi.setMaxLifeTime(270*1000L);
		bi.setMaxPlayerLevel(100);
		bi.setMinPlayerLevel(0);
		bi.setMinPlayerNumForStartOnOneSide(1);
		bi.setMaxPlayerNumOnOneSide(1);
		bi.setName(BattleFieldInfo.仙武大会战场地图数组[2]);
		bi.setStartFightingTime(60 * 1000L);
		bi.setDescription(Translate.text_1900);
		battleFieldInfos.add(bi);
		
		bi = new BattleFieldInfo();
		bi.setBattleCategory(BattleFieldInfo.BATTLE_CATEGORY_仙武大会4);
		bi.setBattleFightingType(BattleField.BATTLE_FIGHTING_TYPE_对战);
		bi.setHolidyRewardParam(1);
		bi.setBattleRewardParam(30);
		bi.setLastingTimeForNotEnoughPlayers(30000L);
		bi.setMapName(BattleFieldInfo.仙武大会战场地图数组[3]);
		bi.setMaxLifeTime(270*1000L);
		bi.setMaxPlayerLevel(100);
		bi.setMinPlayerLevel(0);
		bi.setMinPlayerNumForStartOnOneSide(1);
		bi.setMaxPlayerNumOnOneSide(1);
		bi.setName(BattleFieldInfo.仙武大会战场地图数组[3]);
		bi.setStartFightingTime(60 * 1000L);
		bi.setDescription(Translate.text_1900);
		battleFieldInfos.add(bi);
		
		bi = new BattleFieldInfo();
		bi.setBattleCategory(BattleFieldInfo.BATTLE_CATEGORY_仙武大会5);
		bi.setBattleFightingType(BattleField.BATTLE_FIGHTING_TYPE_对战);
		bi.setHolidyRewardParam(1);
		bi.setBattleRewardParam(30);
		bi.setLastingTimeForNotEnoughPlayers(30000L);
		bi.setMapName(BattleFieldInfo.仙武大会战场地图数组[4]);
		bi.setMaxLifeTime(270*1000L);
		bi.setMaxPlayerLevel(100);
		bi.setMinPlayerLevel(0);
		bi.setMinPlayerNumForStartOnOneSide(1);
		bi.setMaxPlayerNumOnOneSide(1);
		bi.setName(BattleFieldInfo.仙武大会战场地图数组[4]);
		bi.setStartFightingTime(60 * 1000L);
		bi.setDescription(Translate.text_1900);
		battleFieldInfos.add(bi);
		
		bi = new BattleFieldInfo();
		bi.setBattleCategory(BattleFieldInfo.BATTLE_CATEGORY_仙武大会6);
		bi.setBattleFightingType(BattleField.BATTLE_FIGHTING_TYPE_对战);
		bi.setHolidyRewardParam(1);
		bi.setBattleRewardParam(30);
		bi.setLastingTimeForNotEnoughPlayers(30000L);
		bi.setMapName(BattleFieldInfo.仙武大会战场地图数组[5]);
		bi.setMaxLifeTime(270*1000L);
		bi.setMaxPlayerLevel(100);
		bi.setMinPlayerLevel(0);
		bi.setMinPlayerNumForStartOnOneSide(1);
		bi.setMaxPlayerNumOnOneSide(1);
		bi.setName(BattleFieldInfo.仙武大会战场地图数组[5]);
		bi.setStartFightingTime(60 * 1000L);
		bi.setDescription(Translate.text_1900);
		battleFieldInfos.add(bi);
		
		self = this;
		ServiceStartRecord.startLog(this);

	}
	
	public synchronized void notidyBattleFieldEnd(BattleField battle){
		ArrayList<BattleField> al = this.battleFieldMap.get(battle.getName());
		if(al != null){
			al.remove(battle);
		}
		for(int i = 0 ; i < threads.length ; i++){
			threads[i].removeBattleField(battle);
		}
		
		MemoryMonsterManager sm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		MemoryNPCManager nm = (MemoryNPCManager)MemoryNPCManager.getNPCManager();
		
		Game game = battle.getGame();
		LivingObject los[] = game.getLivingObjects();
		for(int j = 0 ; j < los.length ; j++){
			if (los[j] instanceof Sprite) {
				Sprite sprite = (Sprite) los[j];
				if(sprite instanceof Monster){
					sm.removeMonster((Monster)sprite);
				}else if(sprite instanceof NPC){
					nm.removeNPC((NPC)sprite);
				}
			}
		}
		

	}

	public synchronized void deleteBangPaiBattle(BattleFieldInfo bi){
		battleFieldInfos.remove(bi);
		battleFieldMap.remove(bi.getName());
	}
	
	public synchronized BattleField createNewBangPaiBattle(BattleFieldInfo bi){
		battleFieldInfos.add(bi);
		return createNewBattleField(bi);
	}
	
//	public synchronized BattleField createNewMatch(BattleFieldInfo bi){
//		battleFieldInfos.add(bi);
//		return createNewBattleField(bi);
//	}
	
//	public synchronized void deleteMatch(BattleFieldInfo bi){
//		battleFieldInfos.remove(bi);
//		battleFieldMap.remove(bi.getName());
//	}
	
	/**
	 * 创建一个临时的战场
	 * @param bi
	 * @return
	 */
	public BattleField createTempBattleField(BattleFieldInfo bi){
		BattleField battle = null;
		
		if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会1)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会2)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会3)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会4)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会5)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会6)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}
		
		return battle;
	}
	/**
	 * 创建一个新的战场
	 * @param bi
	 * @return
	 */
	public synchronized BattleField createNewBattleField(BattleFieldInfo bi){
		BattleField battle = null;
		
		if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会1)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会2)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会3)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会4)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会5)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}else if(bi.getBattleCategory().equals(BattleFieldInfo.BATTLE_CATEGORY_仙武大会6)){
			battle = new TournamentField(bi.getName()+"-"+ StringUtil.randomIntegerString(10), bi);
		}
		
		GameManager gameManager = GameManager.getInstance();
		GameInfo gi = gameManager.getGameInfo(bi.getMapName());
		if(gi == null){
//			logger.warn("[创建战场] [失败] [对应的地图信息不存在] ["+bi.getName()+"] ["+bi.getMapName()+"]");
			if(logger.isWarnEnabled())
				logger.warn("[创建战场] [失败] [对应的地图信息不存在] [{}] [{}]", new Object[]{bi.getName(),bi.getMapName()});
			return null;
		}
		//ArticleManager am = ArticleManager.getInstance();
		
		Game newGame = new Game(gameManager,gi);
		
		try {
			newGame.init();
			battle.setGame(newGame);
			newGame.setBattleField(battle);
			
			battle.init();
			int index = 0;
			int min = 1000;
			for(int i = 0 ; i < threads.length ; i++){
				int n = threads[i].getBattleFields().length;
				if(n < min){
					min = n;
					index = i;
				}
			}
			
			threads[index].addBattleField(battle);
			
		} catch (Exception e) {
			e.printStackTrace();
			if(logger.isWarnEnabled())
				logger.warn("[创建战场] [失败] [出现异常] ["+bi.getName()+"]",e);
			return null;
		}

		if(logger.isWarnEnabled())
			logger.warn("[创建战场] [成功] [{}]", new Object[]{bi.getName()});
		
		ArrayList<BattleField> al = battleFieldMap.get(bi.getName());
		if(al == null){
			al = new ArrayList<BattleField>();
			battleFieldMap.put(bi.getName(), al);
		}
		al.add(battle);
		
		return battle;
	}
	
	public void destroy(){
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		running = false;
		BattleFieldInfo infos[] = getBattleFieldInfos();
		for(int i = 0 ; i < infos.length ; i++){
			BattleField bfs[] = this.getBattleFieldsByName(infos[i].getName());
			for(int j = 0 ; j < bfs.length ; j++){
				bfs[j].notifyBattleFieldEndCauseSystemExit();
			}
		}
		
		System.out.println("[系统正在停止] [战场管理器] [销毁完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
	}
	
}
