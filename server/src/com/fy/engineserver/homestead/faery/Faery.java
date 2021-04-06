package com.fy.engineserver.homestead.faery;

import java.util.concurrent.locks.ReentrantLock;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.cave.CaveField;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

/**
 * 仙境(洞府的集合)
 * 
 * 
 */
@SimpleEntity
public class Faery implements FaeryConfig, Comparable<Faery> {
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	/** 索引 */
	@SimpleColumn(name = "index33")
	private int index;
	/** 名字 */
	@SimpleColumn(length = 30)
	private String name;
	/** 所属国家 */
	private int country;
	/** 地图名字 */
	private String gameName;
	/** 仙府的ID */
	@SimpleColumn(saveInterval = 10)
	private Long[] caveIds;
	/** 仙府 */
	private transient Cave[] caves;

	private transient Game game;
	private transient int size = -1;

	public transient ReentrantLock modifyIdLock = new ReentrantLock(true);

	// /** 是否更改过 */
	// private transient boolean dirty = false;
	// /** 是否是新创建的 */
	// private transient boolean isNew = false;
	/** 内存中的地图名 */
	private transient String memoryMapName;

	public Faery() {

	}

	public Faery(int country, int index) {
		try {
			this.gameName = GAME_NAME;
			this.country = country;
			this.index = index;
			this.name = CountryManager.国家名称[country - 1] + Translate.仙府 + index;
			this.id = FaeryManager.faeryEm.nextId();// PublicIDGenerator.getInstance().nextId(PublicIDGenerator.FAERY_ID);
			this.caveIds = new Long[MAX_CAVE_IN_FAERY];
			this.caves = new Cave[MAX_CAVE_IN_FAERY];
			this.setMemoryMapName(this.getGameName() + "_jy_" + this.getId());
			FaeryManager.faeryEm.notifyNewObject(this);

		} catch (Exception e) {
			FaeryManager.logger.error("[创建仙府的时候] [异常] [country:" + country + "]" + toString(), e);
		}
	}

	/**
	 * 获得某个索引的仙府
	 * @param index
	 * @return
	 */
	public Cave getCave(int index) {
		if (index < 0 || index > getCaveIds().length) {
			throw new IllegalStateException();
		}
		return getCaves()[index];
	}

	/**
	 * 仙境是否满了
	 * @return
	 */
	public boolean isFull() {
		return size() >= MAX_CAVE_IN_FAERY;
	}

	/**
	 * 达到开启下一个仙境的条件
	 * @return
	 */
	public boolean reachOpenNext() {
		return size() >= FAERY_OPEN_NEXT_NUM;
	}

	@Override
	public int compareTo(Faery o) {
		return getIndex() - o.getIndex();
	}

	public int size() {
		if (size == -1) {
			initSize();
		}
		return size;
	}

	public void initSize() {
		size = 0;
		for (int i = 0; i < getCaves().length; i++) {
			if (getCaves()[i] != null) {
				size++;
			}
		}
	}

	public void clearCaveid(long caveId) {
		for (int i = 0; i < caveIds.length; i++) {
			if (caveIds[i] == caveId) {
				caveIds[i] = 0L;
				FaeryManager.faeryEm.notifyFieldChange(this, "caveIds");
			}
		}
	}

	/**
	 * 
	 */
	public void init() {
		setName(CountryManager.国家名称[getCountry() - 1] + Translate.仙境 + getIndex());
		if (getCaves() != null) {
			for (Cave cave : getCaves()) {
				if (cave != null) {
					cave.setFaery(this);
					if (cave.getMainBuilding().getNpc() != null) {
						getGame().addSprite(cave.getMainBuilding().getNpc());
					} else {
						FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { getName(), cave.getOwnerName(), cave.getMainBuilding().getType() });
					}
					if (cave.getStorehouse().getNpc() != null) {
						getGame().addSprite(cave.getStorehouse().getNpc());
					} else {
						FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { getName(), cave.getOwnerName(), cave.getStorehouse().getType() });
					}
					if (cave.getPethouse().getNpc() != null) {
						getGame().addSprite(cave.getPethouse().getNpc());
					} else {
						FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { getName(), cave.getOwnerName(), cave.getPethouse().getType() });
					}
					if (cave.getDoorplate().getNpc() != null) {
						getGame().addSprite(cave.getDoorplate().getNpc());
					} else {
						FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { getName(), cave.getOwnerName(), cave.getDoorplate().getType() });
					}
					if (cave.getFence().getNpc() != null) {
						getGame().addSprite(cave.getFence().getNpc());
					} else {
						FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { getName(), cave.getOwnerName(), cave.getFence().getType() });
					}
					for (CaveField field : cave.getFields()) {
						if (field.getPlantStatus() != null) {
							field.getPlantStatus().initCfg();
						}
						if (field.getNpc() != null) {
							game.addSprite(field.getNpc());
						} else {
							FaeryManager.logger.error("[init仙境异常] [增加了一个空的NPC] [所在仙境:{}] [仙府主人:{}] [建筑类型:{}]", new Object[] { getName(), cave.getOwnerName(), field.getType() });
						}
					}
					cave.initSchedule();
				}
			}
		}
		initSize();
		FaeryManager.setMemoryMapName(this);
	}

	public void initGame() {
		Game templetGame = GameManager.getInstance().getGameByName(this.getGameName(), CountryManager.国家A);
		if (templetGame == null) {
			templetGame = GameManager.getInstance().getGameByName(this.getGameName(), CountryManager.中立);
		}

		if (templetGame == null) {
			FaeryManager.logger.error("[仙境初始化异常][地图不存在:{}]", new Object[] { getGameName() });
			return;
		}
		Game realGame = null;
		try {
			realGame = new Game(GameManager.getInstance(), templetGame.getGameInfo());
			realGame.init();
			realGame.country = (byte) country;
			setGame(realGame);
			setMemoryMapName(getGameName() + "_jy_" + getId());
			getGame().realSceneName = getMemoryMapName();
		} catch (Exception e) {
			FaeryManager.logger.error("[仙境初始化异常] [国家:{}] [仙境ID:{}]", new Object[] { getCountry(), getId() }, e);
			return;
		}
	}

	public void beatHeart() {
		for (int i = 0; i < getCaves().length; i++) {
			Cave cave = getCaves()[i];
			if (cave != null) {
				try {
					cave.heartBeat(this, i);
				} catch (Exception e) {
					CaveSubSystem.logger.error("heartBeatError", e);
				}
			}
		}
		game.heartbeat();
	}

	public synchronized int getOneEmptyIndexAndHold(long caveId) {
		int index = -1;
		for (int j = 0; j < caveIds.length; j++) {
			if (caveIds[j] == null || caveIds[j] == 0) {
				index = j;
				caveIds[j] = caveId;
				FaeryManager.faeryEm.notifyFieldChange(this, "caveIds");
				break;
			}
		}
		return index;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		FaeryManager.faeryEm.notifyFieldChange(this, "index");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCountry() {
		return country;
	}

	public void setCountry(int country) {
		this.country = country;
	}

	public Cave[] getCaves() {
		return caves;
	}

	public void setCaves(Cave[] caves) {
		this.caves = caves;
	}

	// public boolean isDirty() {
	// return dirty;
	// }
	//
	// public void setDirty(boolean dirty) {
	// this.dirty = dirty;
	// }
	//
	// public boolean isNew() {
	// return isNew;
	// }
	//
	// public void setNew(boolean isNew) {
	// this.isNew = isNew;
	// }

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Long[] getCaveIds() {
		return caveIds;
	}

	public void setCaveIds(Long[] caveIds) {
		this.caveIds = caveIds;
	}

	public String getMemoryMapName() {
		return memoryMapName;
	}

	public void setMemoryMapName(String memoryMapName) {
		this.memoryMapName = memoryMapName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "[ID:" + getId() + "][国家:" + getCountry() + "]地图名[" + getGameName() + "]";
	}
}
