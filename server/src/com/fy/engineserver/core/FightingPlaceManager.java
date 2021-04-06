package com.fy.engineserver.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

public class FightingPlaceManager implements Runnable{

//	static Logger logger = Logger.getLogger(FightingPlaceManager.class);
public	static Logger logger = LoggerFactory.getLogger(FightingPlaceManager.class);
	
	static FightingPlaceManager self;
	Thread thread = null;
	public static FightingPlaceManager getInstance(){
		return self;
	}
	
	
	ArrayList<FightingPlace> fpList = new ArrayList<FightingPlace>();
	
	GameManager gameManager = null;
	
	
	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void init() throws Exception{
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		thread = new Thread(this,"FightingPlace-Check-Place");
		thread.start();
		
		Game game = gameManager.getGameByName(Translate.text_2983,0);
		
		FightingPlace fp = new FightingPlace(game.gi.name,game,Translate.text_2984,Translate.text_2985,Translate.text_2986,Translate.text_2987,com.fy.engineserver.gametime.SystemTime.currentTimeMillis(),com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+3600000);
		fp.setMinPlayerLevel(25);
		fpList.add(fp);
		
		self = this;
		System.out.println("[系统初始化] [FightingPlaceManager] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now)+"毫秒]");

	}
	
	public FightingPlace[] getFightingPlaces(){
		return fpList.toArray(new FightingPlace[0]);
	}
	
	public boolean isInFightingPlace(Player p){
		FightingPlace[] fp = getFightingPlaces();
		for(int i = 0 ; i < fp.length ; i++){
			if(fp[i].game == p.getCurrentGame()){
				return true;
			}
		}
		return false;
	}
	
	public FightingPlace getFightingPlaceByName(String name){
		FightingPlace[] fp = getFightingPlaces();
		for(int i = 0 ; i < fp.length ; i++){
			if(fp[i].getName().equals(name)){
				return fp[i];
			}
		}
		return null;
	}
	
	public FightingPlace getFightingPlaceByPlayer(Player p){
		FightingPlace[] fp = getFightingPlaces();
		for(int i = 0 ; i < fp.length ; i++){
			if(fp[i].game == p.getCurrentGame()){
				return fp[i];
			}
		}
		return null;
	}
	
	
	public void run(){
		while(true){
			try{
				Thread.sleep(1000);
				
				FightingPlace fp[] = getFightingPlaces();
				for(int i = 0; i < fp.length ; i++){
					try{
						fp[i].heartbeat();
					}catch(Throwable e){
						if(logger.isWarnEnabled())
							logger.warn("",e);
					}
				}
			}catch(Throwable e){
				if(logger.isWarnEnabled())
					logger.warn("",e);
			}
			
		}
	}
}
