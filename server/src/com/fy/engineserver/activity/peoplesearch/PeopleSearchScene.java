package com.fy.engineserver.activity.peoplesearch;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.monster.BossMonster;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;

/**
 * 寻人BOSS场景
 * 
 */
public class PeopleSearchScene {
	/** 副本所有者 */
	private Player owner;
	/** 地图 */
	private Game game;
	/** 主人是否进来过 */
	private boolean ownerEnter = false;
	/** 角色最后一次在场景时间 */
	private long playerLastExistTime;

	private long createTime;
	/** BOSS是否已经出生了.只置一次 */
	private boolean bossBourn = false;

	private boolean valid = true;

	private boolean hasAddBoss = false;

	public PeopleSearchScene(Player player) {
		//
		this.owner = player;
		Game gameTemplet = GameManager.getInstance().getGameByName(PeopleSearchManager.sceneName, CountryManager.国家A);
		if (gameTemplet == null) {
			gameTemplet = GameManager.getInstance().getGameByName(PeopleSearchManager.sceneName, CountryManager.中立);
		}
		if (gameTemplet == null) {
			ActivitySubSystem.logger.error(player.getLogString() + "[斩妖降魔] [寻人boss地图不存在:" + PeopleSearchManager.sceneName + "]");
			throw new IllegalStateException("[寻人boss地图不存在:" + PeopleSearchManager.sceneName + "]");
		}
		Game newGame = new Game(GameManager.getInstance(), gameTemplet.getGameInfo());
		this.game = newGame;
		try {
			this.game.init();
		} catch (Exception e) {
			ActivitySubSystem.logger.error(player.getLogString() + "[斩妖降魔] [地图init异常]", e);
		}
		this.game.gi.禁止使用召唤玩家道具 = true;
		long now = SystemTime.currentTimeMillis();
		playerLastExistTime = now;
		createTime = now;
		valid = true;
	}

	/**
	 * 心跳
	 * 如何解决判断进入通过了,但是真正进入的时候被scene心跳移除了.(除了使用设置最后进入时间)
	 * @param now
	 * @return返回true表示要移除
	 */
	public void heartbeat(long now) {
		game.heartbeat();
		boolean invalid = false;// 是否已经无效了 超时或者boss死了,均认为是无效的了
		{
			boolean bossAlive = false; // boss是否活着
			List<Player> remove = new ArrayList<Player>();// 要移除的角色
			for (LivingObject lo : game.getLivingObjects()) {
				if (lo instanceof Player) {
					if (((Player) lo) == owner) {
						playerLastExistTime = now;
					} else {
						remove.add((Player) lo);
					}
				}
				if (lo instanceof BossMonster) {
					bossBourn = true;
					bossAlive = true;
				}
			}
			// 主人不在,且超过20分钟了,直接移除
			if (now - playerLastExistTime > PeopleSearchManager.removeTime) {
				invalid = true;
			} else {
				if (bossBourn && !bossAlive) {// boss已经死了
					if (ActivitySubSystem.logger.isWarnEnabled()) {
						ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [boss刷了.并且死了]");
					}
					invalid = true;
					PeopleSearch ps = owner.getPeopleSearch();
					if (ps == null) {
						ActivitySubSystem.logger.error(owner.getLogString() + "[斩妖降魔] [异常] [boss死了] [角色的寻人不存在了] [bossBourn:" + bossBourn + "] [bossAlive:" + bossAlive + "]");
					} else {
						ps.onDeilver();
						owner.setPeopleSearch(null);
						if (ActivitySubSystem.logger.isWarnEnabled()) {
							ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [成功] [boss死了] [--]");
						}
					}
				}
			}
			if (invalid && bossBourn) { // 无效状态且boss刷过了
				{
					if (now - createTime > 10000) {// 小于十秒不改变状态
						valid = false;// 改变场景状态
						remove.add(owner);
					}
					{
						// 将所有该移除的人都移除
						for (Player p : remove) {
							game.transferGame(p, new TransportData(0, 0, 0, 0, p.getResurrectionMapName(), p.getResurrectionX(), p.getResurrectionY()));
							if (ActivitySubSystem.logger.isWarnEnabled()) {
								ActivitySubSystem.logger.warn(p.getLogString() + "[斩妖降魔] [成功] [将玩家传出副本] [副本主人:" + owner.getLogString() + "]");
							}
						}
					}
				}
			}
		}
		if (!bossBourn && !hasAddBoss) {
			// 如果BOSS还没刷,刷出boss
			int bossId = PeopleSearchManager.getInstance().getBossId()[PeopleSearchManager.random.nextInt(PeopleSearchManager.getInstance().getBossId().length)];
			Monster monster = MemoryMonsterManager.getMonsterManager().createMonster(bossId);
			if (monster != null && monster instanceof BossMonster) {
				BossMonster bm = (BossMonster) monster;
				bm.setLevel(owner.getLevel());
				((MemoryMonsterManager) MemoryMonsterManager.getMonsterManager()).设置sprite属性值(bm);
				bm.setHp(bm.getMaxHP());
				bm.setX(PeopleSearchManager.getInstance().getBossBournPoint().getX());
				bm.setY(PeopleSearchManager.getInstance().getBossBournPoint().getY());
				bm.setBornPoint(PeopleSearchManager.getInstance().getBossBournPoint());
				game.addSprite(bm);
				if (ActivitySubSystem.logger.isWarnEnabled()) {
					ActivitySubSystem.logger.warn(owner.getLogString() + "[斩妖降魔] [刷出了boss] [bossId:" + bossId + "] [bossName:" + bm.getName() + "] [位置:" + bm.getBornPoint().toString() + "]");
				}
			} else {
				ActivitySubSystem.logger.error(owner.getLogString() + "[斩妖降魔] [boss不存在] [bossId:" + bossId + "]");
			}
			hasAddBoss = true;
		}
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public boolean isOwnerEnter() {
		return ownerEnter;
	}

	public void setOwnerEnter(boolean ownerEnter) {
		this.ownerEnter = ownerEnter;
	}

	public long getPlayerLastExistTime() {
		return playerLastExistTime;
	}

	public void setPlayerLastExistTime(long playerLastExistTime) {
		this.playerLastExistTime = playerLastExistTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public boolean isBossBourn() {
		return bossBourn;
	}

	public void setBossBourn(boolean bossBourn) {
		this.bossBourn = bossBourn;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}