package com.fy.engineserver.activity.disaster.instance;

import com.fy.engineserver.activity.disaster.DisasterConstant;
import com.fy.engineserver.activity.disaster.DisasterManager;
import com.fy.engineserver.activity.disaster.module.DisasterRewardModule;
import com.fy.engineserver.activity.treasure.model.BaseArticleModel;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 金猴天灾玩家游戏数据
 */
public class DisasterPlayer implements Comparable<DisasterPlayer>{
	/** 角色id */
	private long playerId;
	/** 死亡次数 */
	private int deadTimes;
	/** 剩余血量 */
	private int leftHp;
	/** 是否已离开游戏地图(在发奖励时，如果离开地图都按最低奖励发放) */
	private boolean leaveGame = false;
	/** 最后一次死亡时间 */
	private long lastDeadTime;
	/** 上次复活时间 */
	private long lastReliveTime;
	/** 玩家传送进来时所在地图 */
	private String gameMap;
	/** 传送前玩家所在地图坐标 */
	private int[] point;
	private String playerNames;
	/** 是否已经结束离开 */
	private boolean end;
	/** 玩家当前所处状态 默认0  进入过1  用以判断多次进入 */
	private int enterType = 0;
	
	@Override
	public String toString() {
		return "DisasterPlayer [playerId=" + playerId + ", deadTimes=" + deadTimes + ", leftHp=" + leftHp + ", leaveGame=" + leaveGame + ", lastDeadTime=" + lastDeadTime + "]";
	}
	
	public DisasterPlayer() {
		super();
	}


	public DisasterPlayer(long playerId, int leftHp, String game, int[] point) {
		super();
		this.playerId = playerId;
		this.leftHp = leftHp;
		this.gameMap = game;
		this.point = point;
		try {
			playerNames = GamePlayerManager.getInstance().getPlayer(playerId).getName();
		} catch (Exception e) {
			DisasterManager.logger.warn("[获取玩家] [异常] [" + playerId + "]", e);
		}
	}
	public long[] reward(int apartSize, int rank) {
		return reward(apartSize, rank, false);
	}
	
	public long[] reward(int apartSize, int rank, boolean temp) {
		long[] result = new long[2];
		try {
			Player player = GamePlayerManager.getInstance().getPlayer(playerId);
			int tempRank = rank;
			if (this.isLeaveGame() || this.isEnd()) {
				tempRank = DisasterConstant.MAX_NUM;
			}
			DisasterRewardModule module = DisasterManager.getInst().getDisasterRewardModule(player, tempRank);
			long exp = module.getRewardExpRate();
//			long exp = player.getNextLevelExp() * module.getRewardExpRate() / 10000;
			if (apartSize == 1) {
				if (this.getDeadTimes() >= DisasterConstant.ONLY_ONE_DEADTIMES) {
					exp /= 2;
				}
			} else if (this.getDeadTimes() < DisasterConstant.MULITY_DEADTIMES) {			//多人参加死亡次数低于20次才能得到物品奖励
				//发放物品奖励
				if (module.getRewardArticles() != null && module.getRewardArticles().size() > 0) {
					Knapsack bag = player.getKnapsack_common();
					for (BaseArticleModel b : module.getRewardArticles()) {
						try {
							Article a = ArticleManager.getInstance().getArticleByCNname(b.getArticleCNNName());
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, b.isBind(), ArticleEntityManager.金猴天灾活动, player, 
									b.getColorType(), b.getArticleNum(), true);
							for (int i=0; i<b.getArticleNum(); i++) {
								if (!bag.put(ae, "金猴天灾活动")) {
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] {ae}, new int[] { 1 }, Translate.金猴天灾物品邮件标题, Translate.金猴天灾物品邮件内容, 0L, 0L, 0L, "金猴天灾奖励");
								}
							}
							result[1] = ae.getId();
							DisasterManager.logger.warn("[发放结束物品奖励] [成功] [" + player.getLogString() + "] [" + b + "]");
						} catch (Exception e) {
							DisasterManager.logger.warn("[发放结束物品奖励] [异常] [" + player.getLogString() + "] [" + b + "]", e);
						}
						
					}
				}
			}
			player.addExp(exp, ExperienceManager.金猴天灾活动);
			try {
				if (!temp) {
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[0], new int[0], Translate.空岛提前退出, String.format(Translate.空岛经验奖励, exp+""), 0L, 0L, 0L, "空岛大冒险");
				}
			} catch (Exception e) {
				
			}
			result[0] = exp;
			DisasterManager.logger.warn("[发放结束经验奖励] [成功] [" + player.getLogString() + "] [增加经验:" + exp + "] [死亡次数:" + this.getDeadTimes() + "] [排名:" + rank + "] [apartSize:" + apartSize + "]");
		} catch (Exception e) {
			DisasterManager.logger.warn("[发放奖励] [异常] [" + this + "]", e); 
		}
		return result;
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getDeadTimes() {
		return deadTimes;
	}
	public void setDeadTimes(int deadTimes) {
		this.deadTimes = deadTimes;
	}
	public int getLeftHp() {
		return leftHp;
	}
	public void setLeftHp(int leftHp) {
		this.leftHp = leftHp;
	}
	public boolean isLeaveGame() {
		return leaveGame;
	}
	public void setLeaveGame(boolean leaveGame) {
		this.leaveGame = leaveGame;
	}
	public long getLastDeadTime() {
		return lastDeadTime;
	}
	public void setLastDeadTime(long lastDeadTime) {
		this.lastDeadTime = lastDeadTime;
	}
	@Override
	public int compareTo(DisasterPlayer o) {
		// TODO Auto-generated method stub
		if (this.getDeadTimes() > o.getDeadTimes()) {
			return 1;
		}
		if (this.getDeadTimes() == o.getDeadTimes() && this.getLastDeadTime() < o.getLastDeadTime()) {
			return 1;
		}
		if (this.getDeadTimes() == o.getDeadTimes() && this.getLastDeadTime() == o.getLastDeadTime()) {
			return 0;
		}
		return -1;
	}

	public int[] getPoint() {
		return point;
	}

	public void setPoint(int[] point) {
		this.point = point;
	}

	public String getGameMap() {
		return gameMap;
	}

	public void setGameMap(String gameMap) {
		this.gameMap = gameMap;
	}

	public String getPlayerNames() {
		return playerNames;
	}

	public void setPlayerNames(String playerNames) {
		this.playerNames = playerNames;
	}

	public long getLastReliveTime() {
		return lastReliveTime;
	}

	public void setLastReliveTime(long lastReliveTime) {
		this.lastReliveTime = lastReliveTime;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public int getEnterType() {
		return enterType;
	}

	public void setEnterType(int enterType) {
		this.enterType = enterType;
	}

}
