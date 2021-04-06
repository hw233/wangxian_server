package com.fy.engineserver.activity.doomsday;

import java.util.ArrayList;


public class DoomsdayThread implements Runnable {

	long step = 100;
	
	protected long amountOfBeatheart = 0;

	protected long amountOfOverflow = 0;
	
	private ArrayList<DoomsdayFuLiGame> fuliGames = new ArrayList<DoomsdayFuLiGame>();
	private ArrayList<DoomsdayFuLiGame> removeGames = new ArrayList<DoomsdayFuLiGame>();
	
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
	
	public void addGame(DoomsdayFuLiGame game) {
		synchronized (fuliGames) {
			fuliGames.add(game);
		}
	}
	
	public void removeGame(DoomsdayFuLiGame game) {
		synchronized (removeGames) {
			removeGames.add(game);
		}
	}
	
	public void run() {
		while (running) {
			try{
				long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				
				//心跳具体执行
				if (fuliGames.size() > 0) {
					synchronized (fuliGames) {
						if (removeGames.size() > 0) {
							synchronized (removeGames) {
								fuliGames.removeAll(removeGames);
							}
							removeGames.clear();
						}
					}
					DoomsdayFuLiGame[] games = fuliGames.toArray(new DoomsdayFuLiGame[0]);
					for (int i = 0; i < games.length; i++) {
						try {
							games[i].heatbeat();
						}catch(Exception e) {
							DoomsdayManager.logger.error("", e);
						}
					}
				}else {
					if (removeGames.size() > 0) {
						removeGames.clear();
					}
				}
				
				long costTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime;
				if (costTime < step) {
					try {
						Thread.sleep(step - costTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}else {
					if (DoomsdayManager.logger.isWarnEnabled()) {
						DoomsdayManager.logger.warn("末日福利心跳超时 [{}] [{}] [{}] [{}] [{}]", new Object[]{name, amountOfBeatheart, amountOfOverflow, costTime, fuliGames.size()});
					}
					amountOfOverflow++;
				}
				amountOfBeatheart++;
			}catch(Exception e) {
				DoomsdayManager.getInstance().logger.error("末日福利心跳出错" + name, e);
			}
		}
	}

	public void setFuliGames(ArrayList<DoomsdayFuLiGame> fuliGames) {
		this.fuliGames = fuliGames;
	}

	public ArrayList<DoomsdayFuLiGame> getFuliGames() {
		return fuliGames;
	}

	public void setRemoveGames(ArrayList<DoomsdayFuLiGame> removeGames) {
		this.removeGames = removeGames;
	}

	public ArrayList<DoomsdayFuLiGame> getRemoveGames() {
		return removeGames;
	}
	
}
