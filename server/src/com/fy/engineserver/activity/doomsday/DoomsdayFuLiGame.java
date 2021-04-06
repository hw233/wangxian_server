package com.fy.engineserver.activity.doomsday;

import java.util.ArrayList;
import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.core.res.MapArea;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

public class DoomsdayFuLiGame {
	
	public static int STATE_START = 1;					//开始
	public static int STATE_WAIT = 2;					//等待进入
	public static int STATE_OVER = 100;					//结束
	
	public static long lifeTime = 1000 * 60 * 60 * 2;	//2小时的生命周期
	
	public static int MAX_MONSTER_NUM = 8;				//同事存在的怪物数目

	private Game game;								//game
	private long startTime;							//启动时间
	private int state = -1;							//状态
	private int monsterID;							//怪物ID
	private long playerID;							//玩家ID
	private int threadIndex;						//线程
	private ArrayList<Monster> monsters = new ArrayList<Monster>();
	Random random = new Random();
	
	private long noPlayerTime = 0;
	
	private long overTime = 0L;							//结束时间
	
	public DoomsdayFuLiGame(String mapName, int monsterID, long playerID) {
		setMonsterID(monsterID);
		setStartTime(System.currentTimeMillis());
		setState(STATE_WAIT);
		setPlayerID(playerID);
		GameInfo gi = null;
		GameManager gm = GameManager.getInstance();
		gi = gm.getGameInfo(mapName);
		if (gi == null) {
			DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + "[福利地图不存在] [{}]", new Object[]{mapName});
		}
		game = new Game(gm,gi);
		
		try {
			game.init();
		}catch(Exception e) {
			DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + "game.init出错:" + mapName, e);
			game = null;
		}
		
		MapArea area = game.gi.getMapAreaByName("boss");
		MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
		for (int i = 0; i < MAX_MONSTER_NUM; i++) {
			Monster monster = mmm.createMonster(monsterID);
			if (monster != null) {
				int bossX = 300;
				int bossY = 300;
				if (area != null) {
					bossX = area.getX() + random.nextInt(area.getWidth());
					bossY = area.getY() + random.nextInt(area.getHeight());
				}
				monster.setX(bossX);
				monster.setY(bossY);
//				DoomsdayManager.logger.warn("[怪物坐标] [{}] [{}]", new Object[]{bossX, bossY, area.getWidth(), area.getHeight()});
				monster.setHp(monster.getMaxHP());
				monster.setBornPoint(new Point(bossX, bossY));
				game.addSprite(monster);
				monsters.add(monster);
			}else {
				DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [怪物创建失败] " + getLogString());
			}
		}
	}
	
	public void heatbeat() {
		try{
			if (game != null) {
				game.heartbeat();
			}else {
				DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [福利房地图不存在]" + getLogString());
				setState(STATE_OVER);
			}
			if (getState() == STATE_WAIT) {
				long now = System.currentTimeMillis();
				if ((now - startTime) > (1000 * 30)) {
					setState(STATE_START);
				}else {
					LivingObject[] livings = game.getLivingObjects();
					for (int i = 0; i < livings.length ; i++) {
						if (livings[i] instanceof Player) {
							if (livings[i].getId() == getPlayerID()) {
								setState(STATE_START);
							}
						}
					}
				}
			}else if (getState() == STATE_START) {
				if (isTimeOver()) {
					setState(STATE_OVER);
					return;
				}
				//移除死亡的怪物
				for (int i = monsters.size(); --i >= 0;) {
					if (monsters.get(i).isDeath()) {
						monsters.remove(i);
					}
				}
				if (monsters.size() < MAX_MONSTER_NUM){
					//刷已经死亡的怪
					MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
					for (int i = 0; i < MAX_MONSTER_NUM - monsters.size(); i++) {
						Monster monster = mmm.createMonster(monsterID);
						if (monster != null) {
							int bossX = 300;
							int bossY = 300;
							MapArea area = game.gi.getMapAreaByName("boss");
							if (area != null) {
								bossX = area.getX() + random.nextInt(area.getWidth());
								bossY = area.getY() + random.nextInt(area.getHeight());
							}
//							DoomsdayManager.logger.warn("[怪物坐标] [{}] [{}]", new Object[]{bossX, bossY, area.getWidth(), area.getHeight()});
							monster.setX(bossX);
							monster.setY(bossY);
							monster.setBornPoint(new Point(bossX, bossY));
							monster.setHp(monster.getMaxHP());
							game.addSprite(monster);
							monsters.add(monster);
						}else {
							DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [怪物创建失败] " + getLogString());
						}
					}
				}
				LivingObject[] livings = game.getLivingObjects();
				boolean isHavePlayer = false;
				for (int i = 0; i < livings.length ; i++) {
					if (livings[i] instanceof Player) {
						isHavePlayer = true;
						noPlayerTime = 0;
						if (livings[i].getId() == getPlayerID()) {
							Player player = (Player)livings[i];
							Buff buff = player.getBuffByName(DoomsdayManager.guajiBuffName);
							if (buff == null) {
								//buff到期了，出地图。
								DoomsdayManager.logger.warn(DoomsdayManager.doomsdayLoggerHead + " [buff时间到了，出地图] " + player.getLogString() + " " + getLogString());
								setStartTime(0);
							}
						}else {
							//这个地图有其他玩家？
							DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [这个地图有其他玩家] " + getLogString());
							setStartTime(0);
						}
						break;
					}
				}
				if (!isHavePlayer) {
					if (noPlayerTime == 0) {
						noPlayerTime = System.currentTimeMillis();
					}else if (System.currentTimeMillis() - noPlayerTime > 1000 * 20) {
						DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + " [这个地图没有player] " + getLogString());
						setStartTime(0);
					}
				}
			}else if (isOver()) {
				if (isTimeOver()) {
					if (game != null) {
						LivingObject[] livings = game.getLivingObjects();
						for (int i = 0; i < livings.length ; i++) {
							if (livings[i] instanceof Player) {
								Player player = (Player)livings[i];
								Game backGame = GameManager.getInstance().getGameByName(DoomsdayManager.kunlunshengdian, player.getCountry());
								MapArea area = backGame.gi.getMapAreaByName(Translate.出生点);
								int bornX = 300;
								int bornY = 300;
								if (area != null) {
									Random random = new Random();
									bornX = area.getX()+random.nextInt(area.getWidth());
									bornY = area.getY() + random.nextInt(area.getHeight());
								}
								TransportData transportData = new TransportData(0,0,0,0,DoomsdayManager.kunlunshengdian,bornX,bornY);
								player.setTransferGameCountry(player.getCountry());
								player.getCurrentGame().transferGame(player, transportData);
							}
						}
					}
					if (overTime <= 0) {
						overTime = System.currentTimeMillis();
					}
				}
			}
		}catch(Exception e) {
			DoomsdayManager.logger.error(DoomsdayManager.doomsdayLoggerHead + "福利房心跳出错" + getLogString(), e);
		}
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append("[p=").append(getPlayerID()).append("] ");
		if (game != null) {
			sb.append("[game=").append(game.gi.name).append("] ");
		}
		sb.append("[sTime=").append(startTime).append("] ");
		sb.append("[state=").append(state).append("] ");
		sb.append("[monsterID=").append(monsterID).append("]");
		
		return sb.toString();
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

	public void setMonsterID(int monsterID) {
		this.monsterID = monsterID;
	}

	public int getMonsterID() {
		return monsterID;
	}

	public void setMonsters(ArrayList<Monster> monsters) {
		this.monsters = monsters;
	}

	public ArrayList<Monster> getMonsters() {
		return monsters;
	}
	
	public boolean isOver() {
		return getState() == STATE_OVER;
	}
	
	public boolean isTimeOver() {
		long now = System.currentTimeMillis();
		if (now - startTime > lifeTime) {
			return true;
		}
		return false;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public long getPlayerID() {
		return playerID;
	}

	public void setThreadIndex(int threadIndex) {
		this.threadIndex = threadIndex;
	}

	public int getThreadIndex() {
		return threadIndex;
	}

	public void setOverTime(long overTime) {
		this.overTime = overTime;
	}

	public long getOverTime() {
		return overTime;
	}
	
	public boolean isRellOver() {
		if (overTime > 0 && System.currentTimeMillis() - overTime > 1000 * 60) {
			return true;
		}
		return false;
	}
	
}
