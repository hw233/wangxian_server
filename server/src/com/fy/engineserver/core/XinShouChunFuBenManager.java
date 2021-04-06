package com.fy.engineserver.core;

import java.util.ArrayList;
import java.util.Hashtable;

import com.fy.engineserver.core.event.PlayerInOutGameListener;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 新手村副本管理
 * 为的是防止开服的时候，大量用户涌进新手村
 * 
 * 
 * 
 */
public class XinShouChunFuBenManager {

	public final static String 需要多个副本的新手村名称[] = new String[]{Translate.text_2638,Translate.text_2640};
	
	public final static int 新手村最大人数限制 = 50;
	
	public final static int 新手村副本数量 = 20;
	
	private static XinShouChunFuBenManager self;
	
	public static XinShouChunFuBenManager getInstance(){
		return self;
	}
	GameManager gameManager;
	
	/**
	 * 玩家与新手村对应表
	 */
	protected ArrayList<Hashtable<String,Integer>> player2GameMapList = new ArrayList<Hashtable<String,Integer>>();
	
	/**
	 * 副本
	 */
	protected Game games[][];
	
	public Game[][] getGames(){
		return games;
	}
	
	public ArrayList<Hashtable<String,Integer>> getPlayer2GameMapList(){
		return player2GameMapList;
	}
	
	int threadNum = 2;
	
	BeatHeartThread[] threads;
	
	
	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public void init() throws Exception{
		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		threads = new BeatHeartThread[threadNum];
		for(int i = 0 ; i < threads.length ; i++){
			threads[i] = new BeatHeartThread();
			threads[i].setName(Translate.text_3109+(i+1));
			threads[i].setBeatheart(5);
		}
		
		
		games = new Game[需要多个副本的新手村名称.length][];
		
		ArrayList<Game> al = new ArrayList<Game>();
		
		for(int i = 0 ; i < 需要多个副本的新手村名称.length ; i++){
			Hashtable<String,Integer> map = new Hashtable<String,Integer>();
			player2GameMapList.add(map);
			
			games[i] = new Game[新手村副本数量];
			
			Game game = gameManager.getGameByName(需要多个副本的新手村名称[i],0);
			
			if(game == null){
				throw new Exception("配置错误，新手村名称【"+需要多个副本的新手村名称[i]+"】对应的地图不存在");
			}
			for(int j = 0 ; j < games[i].length ; j++){
				Game newGame = new Game(gameManager,game.getGameInfo());
				if(gameManager.getPlayerManager() instanceof PlayerInOutGameListener){
					newGame.addPlayerInOutGameListener((PlayerInOutGameListener)this.gameManager.getPlayerManager());
				}
				newGame.init();
				
				games[i][j] = newGame;
				
				al.add(newGame);
			}
		}
		
		int index = 0;
		for(int i = 0 ; i < al.size() ; i++){
			Game g = al.get(i);
			threads[index].addGame(g);
			index++;
			if(index == threads.length){
				index = 0;
			}
		}
		
		for(int i = 0 ; i < threads.length ; i++){
			threads[i].start();
		}
		
		
		self = this;
		
		System.out.println("[系统初始化] [新手村副本管理器] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
	}
	
	/**
	 * 获得某个新手村 玩家和那个副本对应的关系
	 * 
	 * @param gameName
	 * @return
	 */
	public Hashtable<String,Integer> getPlayer2GameMap(String gameName){
		int k = indexOf(gameName);
		if(k >= 0){
			return player2GameMapList.get(k);
		}
		return null;
	}
	
	public int indexOf(String gameName){
		for(int i = 0 ; i < 需要多个副本的新手村名称.length ; i++){
			if(需要多个副本的新手村名称[i].equals(gameName)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 判断某个地图是否为新手村地图
	 * @param gameName
	 * @return
	 */
	public boolean isXinShouChun(String gameName){
		for(int i = 0 ; i < 需要多个副本的新手村名称.length ; i++){
			if(需要多个副本的新手村名称[i].equals(gameName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获得玩家要进入的新手村副本地图
	 * 如果给出的地图不是新手村，返回null
	 * 如果所有的地图都满了，返回null
	 * @param p
	 * @param gameName
	 * @return
	 */
	public Game getGameByPlayer(Player p,String gameName){
		int k = indexOf(gameName);
		if(k == -1) return null;
		
		Hashtable<String,Integer> map = player2GameMapList.get(k);
		Integer i = map.get(p.getName());
		if(i == null){
			int j = selectGameForNewEnterPlayer(p,k);
			if(j == -1){
				return null;
			}
			map.put(p.getName(), new Integer(j));
			return games[k][j];
		}else{
			
			Game g = games[k][i.intValue()];
			if(g.getNumOfPlayer() < 新手村最大人数限制){
				return g;
			}
			int j = selectGameForNewEnterPlayer(p,k);
			if(j == -1){
				return null;
			}
			map.put(p.getName(), new Integer(j));
			return games[k][j];
		}
	}

	/**
	 * 选择一个副本，如果所有的副本都达到了上限，返回-1
	 * @param p
	 * @param index
	 */
	private int selectGameForNewEnterPlayer(Player p,int index){
		for(int i = 0 ; i < games[index].length ; i++){
			Game g = games[index][i];
			int count = g.getNumOfPlayer();
			if(count < 新手村最大人数限制){
				return i;
			}
		}
		return -1;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}


}
