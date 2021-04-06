package com.fy.engineserver.core;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.REPORT_LONG_HEARTBEAT_REQ;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.queue.DefaultSelectableQueue;
import com.xuanzhi.tools.transport.Message;

public class BeatHeartThread implements Runnable {

public	static Logger logger = LoggerFactory.getLogger(BeatHeartThread.class);
	protected int beatheart = 10;

	protected long amountOfBeatheart = 0;

	protected long amountOfOverflow = 0;

	protected Game games[] = new Game[0];

	protected ArrayList<Game> newGameList = new ArrayList<Game>();
	protected ArrayList<Game> removeGameList = new ArrayList<Game>();
	
	protected boolean running = false;
	protected Thread thread = null;

	protected String name;

	public void start() {
		if (running)
			return;
		running = true;
		thread = new Thread(this, name);
		thread.start();
	}

	public void stop() {
		running = false;
	}

	public Game[] getGames() {
		if (games == null)
			return new Game[0];
		return games;
	}

	public void addGame(Game g) {
		synchronized(newGameList){
			newGameList.add(g);
		}
		gamesNames += g.gi.displayName+",";
	}
	String gamesNames = "";
	private void doAddGame(Game g) {
		if (contains(g))
			return;
		if (games == null)
			games = new Game[0];
		Game gg[] = new Game[games.length + 1];
		System.arraycopy(games, 0, gg, 0, games.length);
		gg[games.length] = g;
		games = gg;
		
		
	}
	
	public void removeGame(Game g){
		synchronized(removeGameList){
			removeGameList.add(g);
		}
	}

	private void doRemoveGame(Game g) {
		int k = indexOf(g);
		if (k == -1)
			return;
		Game gg[] = new Game[games.length - 1];
		System.arraycopy(games, 0, gg, 0, k);
		System.arraycopy(games, k + 1, gg, k, gg.length - k);
		games = gg;
	}

	public int indexOf(Game g) {
		for (int i = 0; games != null && i < games.length; i++) {
			if (games[i].equals(g))
				return i;
		}
		return -1;
	}

	public boolean contains(Game g) {
		return indexOf(g) != -1;
	}

	public int getBeatheart() {
		return beatheart;
	}

	public void setBeatheart(int beatheart) {
		this.beatheart = beatheart;
	}

	public long getAmountOfBeatheart() {
		return amountOfBeatheart;
	}

	public void setAmountOfBeatheart(long amountOfBeatheart) {
		this.amountOfBeatheart = amountOfBeatheart;
	}

	public long getAmountOfOverflow() {
		return amountOfOverflow;
	}

	public void setAmountOfOverflow(long amountOfOverflow) {
		this.amountOfOverflow = amountOfOverflow;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	MieshiGatewayClientService mgs;
	String serverName = "";
	String gameName = "";
	public void run() {
		try {
			long step = 1000 / beatheart;
			int i = 0;
			while (running) {
				
				long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if(newGameList.size() > 0){
					synchronized(newGameList){
						if(newGameList.size() > 0){
							Game gs[] = newGameList.toArray(new Game[0]);
							newGameList.clear();
							for(int j = 0 ; j < gs.length ; j++){
								doAddGame(gs[j]);
							}
						}
					}
				}
				
				if(removeGameList.size() > 0){
					synchronized(removeGameList){
						if(removeGameList.size() > 0){
							Game gs[] = removeGameList.toArray(new Game[0]);
							removeGameList.clear();
							for(int j = 0 ; j < gs.length ; j++){
								doRemoveGame(gs[j]);
							}
						}
					}
				}
				
				int livings = 0;
				for (i = 0; games != null && i < games.length; i++) {
					try {
						games[i].heartbeat();
						livings += games[i].getNumOfPlayer();
					} catch (Throwable e) {
						if(Game.logger.isWarnEnabled())
							Game.logger.warn("heartbeat catch exception on " + games[i].gi.getName(),e);
					}
				}
				long costTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime;

				com.xuanzhi.tools.mem.HeartbeatTrackerService.operationEnd("游戏心跳", gamesNames,costTime);
				
				if (logger.isDebugEnabled()) {
					logger.debug("[{}] [beat-heart]  [overflow:{}] [amount{}] [playerNum:{}] [cost:{}ms]", new Object[]{Thread.currentThread().getName(),amountOfOverflow,amountOfBeatheart,livings,costTime});
				}

				if (costTime < step) {
					try {
						Thread.sleep(step - costTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					if(gameName == null || gameName.equals("")){
						StringBuffer sb = new StringBuffer();
						for(Game g : games) {
							sb.append(g.getGameInfo().getName()+",");
						}
						gameName = sb.toString();
					}
					if(logger.isWarnEnabled())
						logger.warn("[{}] [beatheart-overflow]  [overflow:{}] [amount{}] [games:{}] [playerNum:{}] [cost:{}ms]", new Object[]{Thread.currentThread().getName(),amountOfOverflow,amountOfBeatheart,gameName,livings,costTime});
					amountOfOverflow++;
					step = 1000 / beatheart;
				}
				amountOfBeatheart++;
				
//				if(costTime > 1000){
//					if(mgs == null){
//						mgs = MieshiGatewayClientService.getInstance();
//					}
//					if(serverName == null || serverName.equals("")){
//						serverName = GameConstants.getInstance().getServerName();
//					}
//					
//					if(mgs != null){
//						DefaultSelectableQueue queue = mgs.dq;
//						if(queue != null){
//							Message m = (Message)queue.push(new REPORT_LONG_HEARTBEAT_REQ(GameMessageFactory.nextSequnceNum(), serverName, gameName, costTime));
//							if(m != null){
//								logger.warn("[discard_message] [MieshiGatewayClientService] [queue_is_full] ["+m.getTypeDescription()+"]" );
//							}
//						}else{
//							logger.warn("[conn_hasn't_select_queue] [MieshiGatewayClientService]" );
//						}
//					}
//				}
			}
		} catch(Throwable e){
			if(logger.isWarnEnabled())
				logger.warn("[" + Thread.currentThread().getName()+ "] [beatheart-stop]  [overflow:"+ amountOfOverflow + "] [amount" + amountOfBeatheart+ "]",e);
		}finally {
			running = false;
		}
	}
}
