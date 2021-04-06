package com.fy.engineserver.marriage;


import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class MarriageDownCity{
	
	public static final int STATE_BEGIN = 0;			//初始化
	public static final int STATE_OVER = -1;			//结束
	
	@SimpleId
	private long id;
	
	@SimpleVersion
	private int versionMDC;
	
	private String mapName;
	
	private long startTime;
	
	private int state;
	
	private transient Game game;
	
	public MarriageDownCity(){}
	
	public MarriageDownCity(long id, String mapName){
		this.id = id;
		this.mapName = mapName;
		startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GameManager gameManager = GameManager.getInstance();
		game = new Game(gameManager, gameManager.getGameInfo(mapName));
		try {
			game.init();
		} catch (Exception e) {
			MarriageManager.logger.error("初始化结婚副本出错: mapName =" + mapName, e);
		}
	}
	
	public void heartbeat(){
		if(game!=null){
			game.heartbeat();
			if(startTime<com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-(2*60*60*1000) || state == STATE_OVER){
				setState(STATE_OVER);
				LivingObject[] living = game.getLivingObjects();
				for (int i = 0 ; i < living.length; i++) {
					if (living[i] instanceof Player) {
						Player player = (Player)living[i];
						player.getCurrentGame().transferGame(player, new TransportData(0, 0, 0, 0, player.getResurrectionMapName(), player.getResurrectionX(), player.getResurrectionY()));
					}
				}
			}
		}else {
			if (startTime<com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-(2*60*60*1000)) {
				setState(STATE_OVER);
			}
		}
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getMapName() {
		return mapName;
	}

	public void setState(int state) {
		this.state = state;
		MarriageManager.getInstance().emCity.notifyFieldChange(this, "state");
	}

	public int getState() {
		return state;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Game getGame() {
		if(game==null){
			GameManager gameManager = GameManager.getInstance();
			game = new Game(gameManager, gameManager.getGameInfo(mapName));
			try {
				game.init();
			} catch (Exception e) {
				MarriageManager.logger.error("初始化结婚副本出错:", e);
			}
		}
		return game;
	}

	public void setVersionMDC(int versionMDC) {
		this.versionMDC = versionMDC;
	}

	public int getVersionMDC() {
		return versionMDC;
	}
}
