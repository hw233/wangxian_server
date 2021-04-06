package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Sprite;
import com.fy.engineserver.sprite.Team;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.stat.client.StatClientService;
import com.xuanzhi.stat.model.ArticleBornFlow;

/**
 * 掉落方案的实体
 * 
 * 但一个怪物被打死后，会根据怪物身上配置的掉落方案产生一个掉落方案的实体
 * 
 * 每一个实体都属于某个怪，但怪消失后，此实体就消失了。
 * 
 * 实体里包含的物品，但怪被打死的时候，也就是此实体产生的时候，
 * 实体将记录谁能来捡里面的东西。
 * 
 * 其他人都不能捡，甚至其他人都看不见这个怪尸体上的闪光
 * 
 * 
 */
public class FlopSchemeEntity {

	// static Logger logger = Logger.getLogger(FlopSchemeEntity.class);
	public static Logger logger = LoggerFactory.getLogger(FlopSchemeEntity.class);

	/**
	 * 私有物品的掉落
	 * 
	 * 
	 */
	public static class PrivateFlopEntity {

		/**
		 * 私有物品
		 */
		ArticleEntity entity;

		/**
		 * 可以捡的玩家
		 */
		Player player;

		/**
		 * 拾取的标记，true标识已经拾取，false标识还没有拾取
		 */
		boolean pickUpFlag = false;

		public ArticleEntity getEntity() {
			return entity;
		}

		public void setEntity(ArticleEntity entity) {
			this.entity = entity;
		}

		public Player getPlayer() {
			return player;
		}

		public void setPlayer(Player player) {
			this.player = player;
		}

		public boolean isPickUpFlag() {
			return pickUpFlag;
		}

		public void setPickUpFlag(boolean pickUpFlag) {
			this.pickUpFlag = pickUpFlag;
		}

	}

	public static class DiceResult {
		Player player;

		// 0标识放弃，1标识需求，2标识贪婪
		byte diceType;

		int result;
	}

	/**
	 * 共享物品的掉落
	 * 
	 * 
	 */
	public static class ShareFlopEntity {

		public static final byte FREE_PICKUP = 0;
		public static final byte TEAM_PICKUP = 1;
		public static final byte CAPTAIN_PICKUP = 2;

		/**
		 * 共享物品
		 */
		ArticleEntity entity;

		/**
		 * 针对所有玩家的一个标记，标记每一个玩家是可以：
		 * 0 标识 可以捡
		 * 1 标识 比点数大小
		 * 2 标识 队长分配
		 */
		byte assgins[];

		/**
		 * 拾取的标记，true标识已经拾取，false标识还没有拾取
		 */
		boolean pickUpFlag = false;

		/**
		 * 标记是否已经掷骰子了
		 */
		boolean diceFlag = false;
		long diceTime = 0;
		// 多少秒超时
		int timeout = 30;
		ArrayList<DiceResult> drList = new ArrayList<DiceResult>();

		FlopSchemeEntity fse;

		Player dicePlayers[];

		public ShareFlopEntity(FlopSchemeEntity fse) {
			this.fse = fse;
		}

		public ArticleEntity getEntity() {
			return entity;
		}

		public void setEntity(ArticleEntity entity) {
			this.entity = entity;
		}

		public boolean isPickUpFlag() {
			return pickUpFlag;
		}

		public void setPickUpFlag(boolean pickUpFlag) {
			this.pickUpFlag = pickUpFlag;
		}

		public byte[] getAssgins() {
			return assgins;
		}

		public void setAssgins(byte[] assgins) {
			this.assgins = assgins;
		}

		public boolean isDiceFlag() {
			return diceFlag;
		}

		public void setDiceFlag(Player dicePlayers[], boolean diceFlag, int timeout) {
			this.dicePlayers = dicePlayers;
			this.diceFlag = diceFlag;
			if (diceFlag) {
				diceTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
				this.timeout = timeout;
			}
		}

		public void heartbeat(long heartBeatStartTime) {
			if (diceFlag == false) return;
			if (pickUpFlag) return;
			if (drList.size() == 0 && heartBeatStartTime - diceTime < timeout * 1000) return;

			if (drList.size() == 0 && heartBeatStartTime - diceTime > timeout * 1000) {
				for (int j = 0; j < fse.players.length; j++) {
					assgins[j] = ShareFlopEntity.FREE_PICKUP;
				}
				diceFlag = false;
				fse.modifyFlag = true;
				return;
			}

			int count = 0;
			for (int i = 0; i < dicePlayers.length; i++) {
				boolean b = false;
				for (DiceResult d : drList) {
					if (d.player == dicePlayers[i]) {
						b = true;
						break;
					}
				}
				if (b == false) {
					count++;
				}
			}

			if (count == 0 || heartBeatStartTime - diceTime > timeout * 1000) {
				DiceResult dr = null;
				for (DiceResult d : drList) {
					if (d.diceType != 0) {
						if (dr == null) {
							dr = d;
						} else {
							if (dr.diceType == 2 && d.diceType == 1) {
								dr = d;
							} else if (dr.diceType == d.diceType && dr.result < d.result) {
								dr = d;
							}
						}
					}
				}
				if (dr != null) {
					if (dr.player.putToKnapsacks(entity, "拾取")) {

						fse.pickUp(dr.player, new long[] { entity.getId() });
						pickUpFlag = true;

						for (int j = 0; j < fse.players.length; j++) {

							Player pp = fse.getPlayers()[j];
							if (pp == dr.player) {
								pp.send_HINT_REQ(Translate.text_3695, entity);
							} else {
								pp.send_HINT_REQ(dr.player.getName() + Translate.text_3696, entity);
							}

						}

						Article a = ArticleManager.getInstance().getArticle(entity.getArticleName());
						Player p = dr.player;
						if (a != null) {
							// 发送统计日志
							// int sortint[] = ArticleEntityManager.getCategoryOfArticle(a);
							String sort = Translate.text_1736;
							String subsort = Translate.text_1736;
							try {
								// sort = ArticleEntityManager.物品一级分类[sortint[0]];
								// subsort = ArticleEntityManager.物品二级分类[sortint[0]][sortint[1]];
							} catch (Exception e) {
							}
							int alevel = a.getArticleLevel();
							// String color = ArticleEntityManager.getColorOfArticle(a);
							// String material = ArticleEntityManager.getMaterialOfArticle(a);
							ArticleBornFlow flow = new ArticleBornFlow();
							flow.setArticlename(a.getName());
							flow.setBornreason(ArticleEntityManager.CREATE_REASON_DESPS[ArticleEntityManager.CREATE_REASON_CAIJI]);
							flow.setLevel(alevel);
							flow.setSort(sort);
							flow.setSubsort(subsort);
							flow.setCount(1);
							// flow.setColor(color);
							// flow.setMaterial(material);
							flow.setPlayername(p.getName());
							flow.setUsername(p.getUsername());
							flow.setCareer(CareerManager.careerNames[p.getCareer()]);
							flow.setCreatetime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							String map = p.getGame();
							if (map == null || map.length() == 0) {
								map = p.getLastGame();
								if (map == null || map.length() == 0) {
									map = Translate.text_1211;
								}
							}
							flow.setMap(map);
							flow.setNowlevel(p.getLevel());
							// flow.setPolcamp(Player.POLITICALCAMP[p.getPoliticalCamp()]);
							flow.setServer(GameConstants.getInstance().getServerName());
							// flow.setTotalSaving(p.getPassport().getTotalSaving());
							StatClientService.getInstance().sendArticleBornFlow(flow);
						}

					} else {

						for (int j = 0; j < fse.players.length; j++) {

							Player pp = fse.getPlayers()[j];
							if (pp == dr.player) {
								pp.send_HINT_REQ(Translate.text_3697, entity);
								assgins[j] = ShareFlopEntity.FREE_PICKUP;

							} else {
								pp.send_HINT_REQ(dr.player.getName() + Translate.text_3698, entity);
								assgins[j] = 3;
							}
						}
						diceFlag = false;
						fse.modifyFlag = true;

						// logger.warn("[掷骰子拾取-放置] [失败] ["+dr.player.getName()+"] [空格子："+dr.player.getKnapsack().getEmptyNum()+"] ["+entity.getArticleName()+"]");
					}
				} else { // 所有人都放弃了或者超时了
					for (int j = 0; j < fse.players.length; j++) {

						Player pp = fse.getPlayers()[j];
						pp.send_HINT_REQ(Translate.text_3699, entity);
						assgins[j] = ShareFlopEntity.FREE_PICKUP;
					}
					diceFlag = false;
					fse.modifyFlag = true;
				}
			}
		}

		/**
		 * 某个玩家进行掷骰子
		 * @param p
		 * @param diceType
		 * @return
		 */
		public void dice(Player p, byte diceType) {
		}
	}

	public FlopSchemeEntity(Sprite owner) {
		this.owner = owner;
	}

	/**
	 * 此实体属于哪个怪，其实此怪已经死亡了
	 */
	Sprite owner;

	/**
	 * 此实体那些玩家可以看或者捡，
	 * 此变量是针对的掉落的共享物品
	 */
	Player players[];

	/**
	 * 掉落的钱，单位为最小的单位
	 */
	protected int money;

	/**
	 * 共享物品，此物品在允许捡的情况下，任何人都可以捡
	 * 捡完就没有了
	 */
	ShareFlopEntity shareEntities[] = new ShareFlopEntity[0];

	/**
	 * 私有物品的掉落
	 */
	PrivateFlopEntity privateFlopEntities[] = new PrivateFlopEntity[0];

	Team team;

	// ///////////////////////////////////////////////////////////////////////////////
	// 下面几个变量是服务器内部变量，用于控制用，不是数据
	//

	// 此实体是否被部分拾取过
	private boolean modifyFlag = false;

	/**
	 * 判断是否还有东西可以捡
	 * @return
	 */
	public boolean isEmpty() {
		if (money > 0) return false;

		for (int i = 0; i < shareEntities.length; i++) {
			if (!shareEntities[i].isPickUpFlag()) return false;
		}

		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (!privateFlopEntities[i].isPickUpFlag()) return false;
		}

		return true;
	}

	public void heartbeat(long heartBeatStartTime, long interval, Game game) {

		for (int i = 0; i < this.shareEntities.length; i++) {
			shareEntities[i].heartbeat(heartBeatStartTime);
		}

	}

	public boolean isPickupMoneyAvailable(Player p) {
		if (money == 0) return false;
		for (int j = 0; j < players.length; j++) {
			if (players[j] == p) {
				return true;
			}
		}
		return false;
	}

	public void pickUpMoney() {
		if (money > 0) {
			// 平分给所有的人
			BillingCenter economicCenter = BillingCenter.getInstance();

			for (int j = 0; j < players.length; j++) {

				try {
					economicCenter.playerSaving(players[j], money / players.length, CurrencyType.BIND_YINZI, SavingReasonType.MONSTER_FLOP, "");

				} catch (Exception e) {
					if (Game.logger.isWarnEnabled()) Game.logger.warn("[拾取] [金币] [失败] [存储出现异常] [" + players[j].getName() + "] [" + owner.getName() + "]", e);
				}

				HINT_REQ err = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_973 + (money / players.length) + Translate.text_148);
				players[j].addMessageToRightBag(err);
			}

			money = 0;
			this.modifyFlag = true;
		}
	}

	/**
	 * 获得所有可以捡物品的玩家，包括共享物品和私有物品
	 * @return
	 */
	public Player[] getAllPlayers() {
		ArrayList<Player> al = new ArrayList<Player>();
		for (int i = 0; players != null && i < players.length; i++) {
			al.add(players[i]);
		}
		for (int i = 0; privateFlopEntities != null && i < privateFlopEntities.length; i++) {
			if (al.contains(privateFlopEntities[i].getPlayer()) == false) {
				al.add(privateFlopEntities[i].getPlayer());
			}
		}
		return al.toArray(new Player[0]);
	}

	/**
	 * 对于某个玩家来说，拾取是否打开
	 * 也就是说无论是否拾取过，看这个玩家是否有权利拾取
	 * @param p
	 * @return
	 */
	public boolean isPickupEnable(Player p) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) return true;
		}
		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (privateFlopEntities[i] != null && privateFlopEntities[i].player == p) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 对于某个玩家来说，是否可以拾取东西，
	 * 看玩家是否还有东西没有摸。
	 * 
	 * @param p
	 * @return
	 */
	public boolean isPickupAvailable(Player p) {
		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (privateFlopEntities[i] != null && privateFlopEntities[i].player == p && privateFlopEntities[i].pickUpFlag == false) {
				return true;
			}
		}
		boolean b = false;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				b = true;
				break;
			}
		}
		if (b == false) return false;

		for (int i = 0; i < shareEntities.length; i++) {
			if (shareEntities[i].isPickUpFlag() == false) {
				return true;
			}
		}

		if (money > 0) return true;

		return false;
	}

	/**
	 * 获取某个玩家对某个物品的分配规则，如果玩家与对应的物品不存在分配规则，返回-1
	 * @param player
	 * @param entityId
	 * @return
	 */
	public byte getAssignFlag(Player player, long entityId) {
		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (privateFlopEntities[i] != null && privateFlopEntities[i].player == player && privateFlopEntities[i].pickUpFlag == false) {
				if (privateFlopEntities[i].entity != null && privateFlopEntities[i].entity.getId() == entityId) {
					return ShareFlopEntity.FREE_PICKUP;
				}
			}
		}
		int b = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == player) {
				b = i;
				break;
			}
		}
		if (b >= 0) {
			for (int i = 0; i < shareEntities.length; i++) {
				if (shareEntities[i].isPickUpFlag() == false && shareEntities[i].entity.getId() == entityId) {
					return shareEntities[i].assgins[b];
				}
			}
		}
		return -1;
	}

	/**
	 * 对于某个玩家来说，到底能摸出什么物品
	 * 
	 * 注意此方法只是摸出什么物品
	 * @param p
	 * @return
	 */
	public ArticleEntity[] getAvailableArticleEntities(Player p) {
		ArrayList<ArticleEntity> al = new ArrayList<ArticleEntity>();
		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (privateFlopEntities[i] != null && privateFlopEntities[i].player == p && privateFlopEntities[i].pickUpFlag == false) {
				if (privateFlopEntities[i].entity != null) {
					al.add(privateFlopEntities[i].entity);
				}
			}
		}
		int b = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				b = i;
				break;
			}
		}
		if (b >= 0) {
			for (int i = 0; i < shareEntities.length; i++) {
				if (shareEntities[i].isPickUpFlag() == false) {
					al.add(shareEntities[i].entity);
				}
			}
		}

		return al.toArray(new ArticleEntity[0]);
	}

	/**
	 * 对于某个玩家来说，到底能摸出什么物品
	 * 
	 * 注意此方法只是摸出什么物品
	 * @param p
	 * @return
	 */
	public byte[] getAvailableArticleEntitiesAssign(Player p) {
		ArrayList<Byte> al = new ArrayList<Byte>();
		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (privateFlopEntities[i] != null && privateFlopEntities[i].player == p && privateFlopEntities[i].pickUpFlag == false) {
				if (privateFlopEntities[i].entity != null) {
					al.add(ShareFlopEntity.FREE_PICKUP);
				}
			}
		}
		int b = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				b = i;
				break;
			}
		}
		if (b >= 0) {
			for (int i = 0; i < shareEntities.length; i++) {
				if (shareEntities[i].isPickUpFlag() == false) {
					al.add(shareEntities[i].assgins[b]);
				}
			}
		}
		byte[] bb = new byte[al.size()];
		for (int i = 0; i < al.size(); i++) {
			bb[i] = al.get(i).byteValue();
		}
		return bb;
	}

	/**
	 * 模拟拾取，不是正在的拾取，不改变任何属性
	 * @param p
	 * @param entityIds
	 * @return
	 */
	public ArticleEntity[] simulatePickUp(Player p, long entityIds[]) {

		ArrayList<ArticleEntity> al = new ArrayList<ArticleEntity>();
		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (privateFlopEntities[i] != null && privateFlopEntities[i].player == p && privateFlopEntities[i].pickUpFlag == false) {
				if (privateFlopEntities[i].entity != null) {

					for (int j = 0; j < entityIds.length; j++) {
						if (privateFlopEntities[i].entity.getId() == entityIds[j]) {
							al.add(privateFlopEntities[i].entity);
							break;
						}
					}

				}
			}
		}
		int b = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				b = i;
				break;
			}
		}
		if (b >= 0) {
			for (int i = 0; i < shareEntities.length; i++) {
				if (shareEntities[i].isPickUpFlag() == false && shareEntities[i].entity != null) {
					if (shareEntities[i].assgins[b] == ShareFlopEntity.FREE_PICKUP) {
						for (int j = 0; j < entityIds.length; j++) {
							if (shareEntities[i].entity.getId() == entityIds[j]) {
								al.add(shareEntities[i].entity);
								break;
							}
						}
					}
				}
			}
		}

		return al.toArray(new ArticleEntity[0]);
	}

	public ArticleEntity pickUpByCaptainAllocate(Player p, long entityId) {
		int b = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				b = i;
				break;
			}
		}
		if (b >= 0) {
			for (int i = 0; i < shareEntities.length; i++) {
				if (shareEntities[i].isPickUpFlag() == false && shareEntities[i].entity != null) {
					if (shareEntities[i].entity.getId() == entityId) {

						if (logger.isInfoEnabled())
						// logger.info("[pickup] [captain_allocate] ["+p.getUsername()+"] ["+p.getName()+"] ["+shareEntities[i].entity.getArticleName()+"] ["+shareEntities[i].entity.getId()+"]");
						if (logger.isInfoEnabled()) logger.info("[pickup] [captain_allocate] [{}] [{}] [{}] [{}]", new Object[] { p.getUsername(), p.getName(), shareEntities[i].entity.getArticleName(), shareEntities[i].entity.getId() });

						shareEntities[i].setPickUpFlag(true);
						this.modifyFlag = true;
						return shareEntities[i].entity;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 对于某个玩家来说，拾取某些装备，
	 * 
	 * 此方法将把某些物品从此对象中标记位已经拾取
	 * 真正要放入玩家的背包中有其他程序完成，
	 * 
	 * 所以其他程序需要先判断背包中有没有空间，然后才能调用此方法，否则会出现取出来的物品没有地方放置。
	 * 
	 * @param p
	 * @param entityIds
	 * @return
	 */
	public ArticleEntity[] pickUp(Player p, long entityIds[]) {

		ArrayList<ArticleEntity> al = new ArrayList<ArticleEntity>();
		for (int i = 0; i < privateFlopEntities.length; i++) {
			if (privateFlopEntities[i] != null && privateFlopEntities[i].player == p && privateFlopEntities[i].pickUpFlag == false) {
				if (privateFlopEntities[i].entity != null) {

					for (int j = 0; j < entityIds.length; j++) {
						if (privateFlopEntities[i].entity.getId() == entityIds[j]) {
							al.add(privateFlopEntities[i].entity);

							if (logger.isInfoEnabled())
							// logger.info("[pickup] [private] ["+p.getUsername()+"] ["+p.getName()+"] ["+privateFlopEntities[i].entity.getArticleName()+"] ["+privateFlopEntities[i].entity.getId()+"]");
							if (logger.isInfoEnabled()) logger.info("[pickup] [private] [{}] [{}] [{}] [{}]", new Object[] { p.getUsername(), p.getName(), privateFlopEntities[i].entity.getArticleName(), privateFlopEntities[i].entity.getId() });

							privateFlopEntities[i].pickUpFlag = true;
							this.modifyFlag = true;
							break;
						}
					}

				}
			}
		}

		int b = -1;
		for (int i = 0; i < players.length; i++) {
			if (players[i] == p) {
				b = i;
				break;
			}
		}
		if (b >= 0) {
			for (int i = 0; i < shareEntities.length; i++) {
				if (shareEntities[i].isPickUpFlag() == false && shareEntities[i].entity != null) {
					if (shareEntities[i].assgins[b] == ShareFlopEntity.FREE_PICKUP) {
						for (int j = 0; j < entityIds.length; j++) {
							if (shareEntities[i].entity.getId() == entityIds[j]) {
								al.add(shareEntities[i].entity);

								if (logger.isInfoEnabled())
								// logger.info("[pickup] [share] ["+p.getUsername()+"] ["+p.getName()+"] ["+shareEntities[i].entity.getArticleName()+"] ["+shareEntities[i].entity.getId()+"]");
								if (logger.isInfoEnabled()) logger.info("[pickup] [share] [{}] [{}] [{}] [{}]", new Object[] { p.getUsername(), p.getName(), shareEntities[i].entity.getArticleName(), shareEntities[i].entity.getId() });

								shareEntities[i].setPickUpFlag(true);
								this.modifyFlag = true;
								break;
							}
						}
					}
				}
			}
		}

		return al.toArray(new ArticleEntity[0]);
	}

	public boolean getMofifyFlag() {
		return modifyFlag;
	}

	public void clearMofifyFlag() {
		modifyFlag = false;
	}

	public Sprite getOwner() {
		return owner;
	}

	public void setOwner(Sprite owner) {
		this.owner = owner;
	}

	public Player[] getPlayers() {
		return players;
	}

	public void setPlayers(Player[] players) {
		this.players = players;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
		modifyFlag = true;
	}

	public PrivateFlopEntity[] getPrivateFlopEntities() {
		return privateFlopEntities;
	}

	public void setPrivateFlopEntities(PrivateFlopEntity[] privateFlopEntities) {
		this.privateFlopEntities = privateFlopEntities;
		modifyFlag = true;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Translate.text_3707);
		for (int i = 0; i < players.length; i++) {
			if (i < players.length - 1) sb.append(players[i].getName() + ",");
			else sb.append(players[i].getName());

		}
		sb.append(";");
		sb.append(Translate.text_3708 + this.money + ";");
		sb.append(Translate.text_3709);
		for (int i = 0; i < shareEntities.length; i++) {
			sb.append(shareEntities[i].entity.getArticleName() + "-" + shareEntities[i].entity.getId());
			if (shareEntities[i].isPickUpFlag()) sb.append(Translate.text_3710);
			else sb.append(Translate.text_3711);
			if (i < shareEntities.length - 1) {
				sb.append(",");
			}
		}
		sb.append(";");
		sb.append(Translate.text_3712);
		for (int i = 0; i < this.privateFlopEntities.length; i++) {
			sb.append(privateFlopEntities[i].entity.getArticleName() + "-" + privateFlopEntities[i].entity.getId() + "-" + privateFlopEntities[i].player.getName());
			if (privateFlopEntities[i].pickUpFlag) sb.append(Translate.text_3710);
			else sb.append(Translate.text_3711);
			if (i < privateFlopEntities.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public ShareFlopEntity[] getShareEntities() {
		return shareEntities;
	}

	public void setShareEntities(ShareFlopEntity[] shareEntities) {
		this.shareEntities = shareEntities;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

}
