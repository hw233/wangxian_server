package com.fy.engineserver.battlefield;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.BeatHeartThread;

public class BattleFieldBeatHeartThread implements Runnable {

//	static Logger logger = Logger.getLogger(BeatHeartThread.class);
public	static Logger logger = LoggerFactory.getLogger(BeatHeartThread.class);
	protected int beatheart = 10;

	protected long amountOfBeatheart = 0;

	protected long amountOfOverflow = 0;

	protected BattleField games[] = new BattleField[0];

	protected ArrayList<BattleField> newGameList = new ArrayList<BattleField>();
	protected ArrayList<BattleField> removeGameList = new ArrayList<BattleField>();
	
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

	public BattleField[] getBattleFields() {
		if (games == null)
			return new BattleField[0];
		return games;
	}

	public void addBattleField(BattleField g) {
		synchronized(newGameList){
			newGameList.add(g);
		}
	}
	
	private void doAddBattleField(BattleField g) {
		if (contains(g))
			return;
		if (games == null)
			games = new BattleField[0];
		BattleField gg[] = new BattleField[games.length + 1];
		System.arraycopy(games, 0, gg, 0, games.length);
		gg[games.length] = g;
		games = gg;
	}
	
	public void removeBattleField(BattleField g){
		synchronized(removeGameList){
			removeGameList.add(g);
		}
	}

	private void doRemoveBattleField(BattleField g) {
		int k = indexOf(g);
		if (k == -1)
			return;
		BattleField gg[] = new BattleField[games.length - 1];
		System.arraycopy(games, 0, gg, 0, k);
		System.arraycopy(games, k + 1, gg, k, gg.length - k);
		games = gg;
	}

	public int indexOf(BattleField g) {
		for (int i = 0; games != null && i < games.length; i++) {
			if (games[i].equals(g))
				return i;
		}
		return -1;
	}

	public boolean contains(BattleField g) {
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

	public void run() {
		try {
			long step = 1000 / beatheart;
			int i = 0;
			while (running) {
				
				long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				if(newGameList.size() > 0){
					synchronized(newGameList){
						if(newGameList.size() > 0){
							BattleField gs[] = newGameList.toArray(new BattleField[0]);
							newGameList.clear();
							for(int j = 0 ; j < gs.length ; j++){
								doAddBattleField(gs[j]);
							}
						}
					}
				}
				
				if(removeGameList.size() > 0){
					synchronized(removeGameList){
						if(removeGameList.size() > 0){
							BattleField gs[] = removeGameList.toArray(new BattleField[0]);
							removeGameList.clear();
							for(int j = 0 ; j < gs.length ; j++){
								doRemoveBattleField(gs[j]);
							}
						}
					}
				}
				
				for (i = 0; games != null && i < games.length; i++) {
					try {
						games[i].heartbeat();
					} catch (Exception e) {
						if(logger.isWarnEnabled())
							logger.warn("heartbeat catch exception on " + games[i].getName(),e);
					}
				}
				long costTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime;

				if (logger.isDebugEnabled()) {
//logger.debug("[" + Thread.currentThread().getName()
//+ "] [beat-heart]  [overflow:" + amountOfOverflow
//+ "] [amount" + amountOfBeatheart + "] [cost:"
//+ costTime + "ms]");
					if(logger.isDebugEnabled()){
						logger.debug("[{}] [beat-heart]  [overflow:{}] [amount{}] [cost:{}ms]", new Object[]{Thread.currentThread().getName(),amountOfOverflow,amountOfBeatheart,costTime});
					}

				}

				if (costTime < step) {
					try {
						Thread.sleep(step - costTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
//logger.warn("[" + Thread.currentThread().getName()
//+ "] [beatheart-overflow]  [overflow:"
//+ amountOfOverflow + "] [amount" + amountOfBeatheart
//+ "] [cost:" + costTime + "ms]");
if(logger.isWarnEnabled())
	logger.warn("[{}] [beatheart-overflow]  [overflow:{}] [amount{}] [cost:{}ms]", new Object[]{Thread.currentThread().getName(),amountOfOverflow,amountOfBeatheart,costTime});
					amountOfOverflow++;
				}
				amountOfBeatheart++;
			}
		} finally {
			running = false;
		}
	}
}
