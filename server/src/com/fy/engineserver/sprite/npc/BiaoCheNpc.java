package com.fy.engineserver.sprite.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.activity.silvercar.SilvercarDropCfg;
import com.fy.engineserver.activity.silvercar.SilvercarManager;
import com.fy.engineserver.activity.silvercar.SilvercarTaskCfg;
import com.fy.engineserver.activity.wafen.manager.WaFenManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.JiazuSubSystem;
import com.fy.engineserver.core.res.ResourceManager;
import com.fy.engineserver.country.data.BiaoJu;
import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.silvercar.Option_SilverCar_Help;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.TRANSIENTENEMY_CHANGE_REQ;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.util.RandomTool;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.RandomTool.RandomType;

public class BiaoCheNpc extends TaskFollowableNPC implements NeedRecordByOption {

	static Random random = new Random();
	/**
	 * 投保状态
	 */
	private boolean toubao;

	/** 上一次求救时间 */
	private long lastCallForhelpTime;
	/** 是不是家族镖车 */
	private boolean jiazuCar;

	/** 是否已经被摧毁 */
	private boolean destroyed = false;

	private SilvercarTaskCfg cfg;

	/** 刷新过的列表:加标列表 最多5次 */
	private List<Long> refreshNPC = Collections.synchronizedList(new ArrayList<Long>(5));
	/** 攻击者列表<PlayerId,最后攻击时间> */
	private Hashtable<Long, Long> attackers = new Hashtable<Long, Long>();
	
	public long 镖车击杀者 = 0;

	private long lastNoticeDamage = 0L;//
	/** 刷新颜色次数 */
	private int refershTimes;
	/** 曾经达到过的最高的颜色 */
	private int maxColor;

	@Override
	public void heartbeat(long heartBeatStartTime, long interval, Game game) {
		super.heartbeat(heartBeatStartTime, interval, game);

	}

	@Override
	public byte getNpcType() {
		return NPC_TYPE_BIAOCHE;
	}

	@Override
	protected void killed(long heartBeatStartTime, long interval, Game game) {
		try {
			super.killed(heartBeatStartTime, interval, game);
			long now = SystemTime.currentTimeMillis();
			int color = getGrade();
			// 根据颜色和模板ID掉落
			// 之后设置等级为-1，设置国家为中立，不能被攻击
			SilvercarManager manager = SilvercarManager.getInstance();
			List<SilvercarDropCfg> list = manager.getDropMap().get(getnPCCategoryId());
			if (list != null) {
				SilvercarDropCfg dropCfg = manager.getDropMap().get(getnPCCategoryId()).get(color);
				if (dropCfg == null) {
					SilvercarManager.logger.error(getOwnerId() + "[的镖车被杀掉] [颜色:{}] [NPC:{}] [掉落配置没找到]", new Object[] { color, getName() });
					return;
				}
				List<Integer> results = RandomTool.getResultIndexs(RandomType.eachRandom, dropCfg.getDropRate(), -1);

				List<Long> ownerIds = new ArrayList<Long>();
				for (Iterator<Long> itor = getAttackers().keySet().iterator(); itor.hasNext();) {
					long playerId = itor.next();
					if ((now - getAttackers().get(playerId) < TimeTool.TimeDistance.MINUTE.getTimeMillis())) {
						ownerIds.add(playerId);
					}
				}

				if (SilvercarManager.logger.isWarnEnabled()) {
					SilvercarManager.logger.warn(getOwnerId() + "[的镖车被杀掉] [颜色:{}] [NPC:{}] [掉落索引:{}]", new Object[] { color, getName(), StringTool.array2String(results.toArray(new Integer[0]), ",") });
				}
				NPCManager nm = MemoryNPCManager.getNPCManager();
				for (int index = 0; index < results.size(); index++) {
					Article article = ArticleManager.getInstance().getArticle(dropCfg.getDropName()[index]);
					if (article != null) {
						try {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, false, ArticleEntityManager.CREATE_REASON_MARRIAGE, null, dropCfg.getDropColor()[index], 1, false);
							FlopCaijiNpc fcn = (FlopCaijiNpc) nm.createNPC(MemoryNPCManager.掉落NPC的templateId);
							fcn.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
							fcn.setEndTime(now + MemoryNPCManager.掉落NPC的存在时间);
							ResourceManager.getInstance().getAvata(fcn);
							fcn.setPlayersList(ownerIds);
							fcn.setAe(ae);
							fcn.setCount(1);
							fcn.setName(ae.getShowName());
							fcn.setNameColor(ArticleManager.color_article[ae.getColorType()]);
							fcn.setX(getX() + random.nextInt(100));
							fcn.setY(getY() + random.nextInt(100));
							getCurrentGame().addSprite(fcn);
							if (SilvercarManager.logger.isWarnEnabled()) {
								SilvercarManager.logger.warn("[掉落:{}] [颜色:{}] [位置:[{},{}]]", new Object[] { dropCfg.getDropName()[index], dropCfg.getDropColor()[index], fcn.getX(), fcn.getY() });
							}
						} catch (Exception ex) {
							SilvercarManager.logger.error("[镖车被杀死] [异常] [主人:" + getOwnerId() + "]", ex);
						}
					} else {
						SilvercarManager.logger.error("[镖车被杀死] [掉落:{}] [不存在]", new Object[] { dropCfg.getDropName()[index] });
					}
				}
			}
			BiaoCheNpc followableNPC = (BiaoCheNpc) MemoryNPCManager.getNPCManager().createNPC(this.getnPCCategoryId());
			followableNPC.setX(this.getX());
			followableNPC.setY(this.getY());
			followableNPC.setCurrentGame(this.getCurrentGame());
			followableNPC.setBornTime(SystemTime.currentTimeMillis());
			followableNPC.setGameNames(this.getCurrentGame().gi);
			followableNPC.setOwnerId(this.getOwnerId());
			followableNPC.setDeadTime(followableNPC.getBornTime() + followableNPC.getLife());

			followableNPC.setTargetMap(this.getTargetMap());
			followableNPC.setTargetX(this.getTargetX());
			followableNPC.setTargetY(this.getTargetY());

			followableNPC.setTaskName(getTaskName());
			followableNPC.setCountry(CountryManager.中立);

			followableNPC.setDestroyed(true);
			followableNPC.setNameColor(ArticleManager.color_article[ArticleManager.equipment_color_白]);
			followableNPC.setObjectScale((short) 500);
			// followableNPC.setObjectColor(ArticleManager.color_article[ArticleManager.equipment_color_白]);/
			followableNPC.setGrade(-1);

			followableNPC.setCfg(getCfg());
			followableNPC.setTitle(getTitle());
			followableNPC.setToubao(isToubao());
			followableNPC.setJiazuCar(isJiazuCar());

			followableNPC.setMaxColor(getMaxColor());
			followableNPC.setRefershTimes(getRefershTimes());
			followableNPC.setRefreshNPC(getRefreshNPC());

			followableNPC.setTitle("[破碎的]" + followableNPC.getTitle());

			Player player = GamePlayerManager.getInstance().getPlayer(getOwnerId());
			player.setFollowableNPC(followableNPC);
			this.getCurrentGame().addSprite(followableNPC);
			
			if (this.isJiazuCar() && 镖车击杀者 > 0) {
				Player 击杀者 = GamePlayerManager.getInstance().getPlayer(镖车击杀者);
				try {
					if (JiazuSubSystem.logger.isDebugEnabled()) {
						JiazuSubSystem.logger.debug("[家族运镖] [发送奖励给击杀镖车的人] [击杀者 : " + 击杀者.getLogString() + "] [运镖人:" + player.getLogString() + "] [attackers:" + attackers.values() + "]");
					}
					if (击杀者.getCountry() != player.getCountry()) {		//只有击杀不同国家的家族镖车才有奖励
						JiazuEntityManager2.instance.reward4KillBiaoche(击杀者);
						for (long pid : attackers.keySet()) {
							if (GamePlayerManager.getInstance().isOnline(pid)) {
								Player plo = GamePlayerManager.getInstance().getPlayer(pid);
								if (击杀者.getJiazuId() > 0 && plo.getJiazuId() == 击杀者.getJiazuId() && 击杀者.getId() != plo.getId()) {
									JiazuEntityManager2.instance.reward4KillBiaoche(plo);
								}
							}
						}
					}
				} catch (Exception e) {
					JiazuSubSystem.logger.error("[家族运镖] [给击杀镖车的人发送奖励] [异常] [击杀者id :" + (player.getFollowableNPC() == null ? 0 : ((BiaoCheNpc)player.getFollowableNPC()).镖车击杀者) + "]", e);
				}
				String jiazuName = "";
				String jiazuName2 = "";
				if (击杀者.getJiazuId() > 0) {
					Jiazu jiazu = JiazuManager.getInstance().getJiazu(击杀者.getJiazuId());
					if (jiazu != null) {
						jiazuName = jiazu.getName();
					}
				}
				if (player.getJiazuId() > 0) {
					Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
					if (jiazu != null) {
						jiazuName2 = jiazu.getName();
					}
				}
				String msg = String.format(Translate.击杀镖车提示, CountryManager.得到国家名(击杀者.getCountry()), jiazuName, 击杀者.getName(), CountryManager.得到国家名(player.getCountry()), jiazuName2);
				WaFenManager.sendWordMsg(msg, (byte)1);
				//还需要给击杀者家族玩家发邮件。。告诉大家谁击杀了某个家族的家族镖车
			}
			
			
		} catch (Exception e) {
			SilvercarManager.logger.error("[镖车NPC被杀死] [异常]", e);
		}
	}

	@Override
	public void causeDamage(Fighter caster, int damage, int hateParam, int damageType) {
		if (getGrade() < 0) {
			SilvercarManager.logger.error("[镖车坏了还被攻击]", new Exception());
			return;
		}
		super.causeDamage(caster, damage, hateParam, damageType);
		long now = SystemTime.currentTimeMillis();
		int HPLimit = (int) (getMaxHP() * SilvercarManager.getInstance().getNoticeHPLimit());
		if (SilvercarManager.logger.isDebugEnabled()) {
			SilvercarManager.logger.debug("[主人:{}] [镖车NPC:{}遭受攻击] [攻击者]", new Object[] { getOwnerId(), getName(), caster.getName() });
		}
		if (caster instanceof Player) {
			getAttackers().put(caster.getId(), now);
			镖车击杀者 = caster.getId();			//最后一个攻击的人就是击杀镖车的人
			try {
				Player master = GamePlayerManager.getInstance().getPlayer(getOwnerId());
				((Player)caster).attackBiaoCheFlag.put(master.getCareer(), System.currentTimeMillis());
			} catch (Exception e) {
				PlayerAimManager.logger.error("[目标系统] [玩家攻击镖车标记] [异常] [" + ((Player)caster).getLogString() + "]", e);
			}
		} else if (caster instanceof Pet) {
			if (((Pet)caster).getOwnerId() > 0) {
				getAttackers().put(((Pet)caster).getOwnerId(), now);
				镖车击杀者 = ((Pet)caster).getOwnerId();
			}
		}
		if (getHp() <= HPLimit) {
			if (SilvercarManager.logger.isDebugEnabled()) {
				SilvercarManager.logger.debug("[主人:{}] [镖车NPC:{}遭受攻击] [血已经低于限制了]", new Object[] { getOwnerId(), getName() });
			}
		} else {
			if (SilvercarManager.logger.isDebugEnabled()) {
				SilvercarManager.logger.debug("[主人:{}] [镖车NPC:{}遭受攻击] [血还没达到限制不通知]", new Object[] { getOwnerId(), getName() });
			}
		}
		try {
			if (SilvercarManager.logger.isDebugEnabled()) {
				SilvercarManager.logger.debug("[主人:{}] [镖车NPC:{}遭受攻击]", new Object[] { getOwnerId(), getName() });
			}
			// 通知家族的人
			Player owner = GamePlayerManager.getInstance().getPlayer(getOwnerId());
			if ((now - lastCallForhelpTime) > SilvercarManager.getInstance().getCarCallForhelpDistance()) {
				lastCallForhelpTime = now;

				// 求救
				if (toubao) {
					// 通知镖局
					Country country = CountryManager.getInstance().countryMap.get(owner.getCountry());
					if (country != null) {
						BiaoJu biaoJu = country.getBiaoju();
						if (biaoJu != null) {
							long jiazuId = biaoJu.getJiazuId();
							Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuId);
							if (jiazu != null) {
								List<Player> players = jiazu.getOnLineMembers();
								for (int i = 0; i < players.size(); i++) {
									Player p = players.get(i);
									if (canSendHelpTo(p, now)) {
										String result = GlobalTool.verifyTransByOther(p.getId());
										if (result == null) {
											callForhelp(p, now, "你运营的镖局的投保人受到攻击,是否前往救援?!");
										}
										if (SilvercarManager.logger.isWarnEnabled()) {
											SilvercarManager.logger.warn("[镖车:{}] [被攻击时主人:{}] [投保了] [通知镖局成员:{}] [result:{}]", new Object[] { getName(), owner.getLogString(), p.getName(), result });
										}
									}
								}
							}
						} else {
							SilvercarManager.logger.error("[镖车:{}] [被攻击时主人:{}] [投保了] [但是投保家族不存在]", new Object[] { getName(), owner.getLogString() });
						}
					}
				}

				if (owner != null) {
					if (owner.getJiazuId() > 0) {
						Jiazu jiazu = JiazuManager.getInstance().getJiazu(owner.getJiazuId());
						if (jiazu != null) {
							String optionText = "你家族[" + owner.getName() + "]的" + ArticleManager.color_article_Strings[getGrade()] + "镖车遭到攻击,快来救援!";
							List<Player> onlines = jiazu.getOnLineMembers();
							for (Player player : onlines) {
								if (player.getId() != owner.getId()) {
									if (canSendHelpTo(player, now)) {
										String result = GlobalTool.verifyTransByOther(player.getId());
										if (result == null) {
											callForhelp(player, now, optionText);
										}
									}
								}
							}
						} else {
							if (SilvercarManager.logger.isDebugEnabled()) {
								SilvercarManager.logger.debug("[镖车:{}被攻击时主人:{}家族没得到,家族ID:{}]", new Object[] { getName(), getOwnerId(), owner.getJiazuId() });
							}
						}
					} else {
						if (SilvercarManager.logger.isDebugEnabled()) {
							SilvercarManager.logger.debug("[镖车:{}被攻击时主人:{}没有家族]", new Object[] { getName(), getOwnerId() });
						}
					}

				} else {
					SilvercarManager.logger.error("[镖车:{}被攻击时主人:{}没拿到]", new Object[] { getName(), getOwnerId() });
				}
			}

			{
				// 提示自己
				if (owner != null) {
					if (now - lastNoticeDamage > 5000) {// 每5秒一次 提示自己
						HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, Translate.你的镖车正在遭受攻击);
						owner.addMessageToRightBag(req);
						lastNoticeDamage = now;
					}
				}
			}

			if (caster instanceof Player || caster instanceof Pet) {
				synchronized (owner.transientEnemyList) {
					if (!owner.transientEnemyList.contains(caster)) {
						owner.transientEnemyList.add(caster);
						TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, (caster instanceof Player ? (byte) 0 : (byte) 1), caster.getId());
						owner.addMessageToRightBag(req);
					}
					owner.transientEnemyPlayerAttackTime.put(caster.getId(), System.currentTimeMillis());
					if (caster instanceof Pet) {
						Player casterOwner = ((Pet) caster).getMaster();
						if (casterOwner != null) {
							if (!owner.transientEnemyList.contains(casterOwner)) {
								owner.transientEnemyList.add(casterOwner);
								TRANSIENTENEMY_CHANGE_REQ req = new TRANSIENTENEMY_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, (casterOwner instanceof Player ? (byte) 0 : (byte) 1), casterOwner.getId());
								owner.addMessageToRightBag(req);
							}
							owner.transientEnemyPlayerAttackTime.put(casterOwner.getId(), System.currentTimeMillis());
						}
					}
				}
			}

		} catch (Exception e) {
			SilvercarManager.logger.error("[镖车:{}被攻击时发生异常]", new Object[] { getName() }, e);
		}

	}

	public boolean isToubao() {
		return toubao;
	}

	public void setToubao(boolean toubao) {
		this.toubao = toubao;
	}

	public List<Long> getRefreshNPC() {
		return refreshNPC;
	}

	public void setRefreshNPC(List<Long> refreshNPC) {
		this.refreshNPC = refreshNPC;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	/**
	 * 是否可以向某个玩家求救
	 * @param player
	 * @param now
	 * @return
	 */
	boolean canSendHelpTo(Player player, long now) {
		if ((now - player.getLastHelpSilvercarTime()) < SilvercarManager.getInstance().getPlayerHelpSilvercarDistance()) {
			if (SilvercarManager.logger.isDebugEnabled()) {
				SilvercarManager.logger.debug(player.getLogString() + "[被NPC求救] [被求救的太频繁了] [NPC:{}] [上次时间:{}] [现在:{}] [间隔:{}]", new Object[] { getName(), player.getLastHelpSilvercarTime(), now, SilvercarManager.getInstance().getPlayerHelpSilvercarDistance() });
			}
			return false;
		}
		if (player.getGame() == null) {// 可能因为过图瞬间导致无GAME
			return false;
		}
		if (player.getGame().equals(this.getGameName())) {
			if (SilvercarManager.logger.isDebugEnabled()) {
				SilvercarManager.logger.debug(player.getLogString() + "[被NPC求救] [在同一地图] [NPC:{}] [地图:{}]", new Object[] { getName(), player.getGame() });
			}
			// 对过近的玩家不发送
			if (isNear(this.getX(), this.getY(), player.getX(), player.getY(), call_radius)) {
				if (SilvercarManager.logger.isDebugEnabled()) {
					SilvercarManager.logger.debug(player.getLogString() + "[被NPC求救] [距离过近,不发送] [NPC[{},{}]] [角色[{},{}]]", new Object[] { getX(), getY(), player.getX(), player.getY() });
				}
				return false;
			}
		}

		if (SilvercarManager.logger.isDebugEnabled()) {
			SilvercarManager.logger.debug(player.getLogString() + "[被NPC:{} 求救] [可以前往]", new Object[] { getName() });
		}
		return true;
	}

	/**
	 * 向某人求救
	 * @param player
	 * @param optionText
	 */
	public void callForhelp(Player player, long now, String optionText) {
		player.setLastHelpSilvercarTime(now);

		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(18000);
		mw.setTitle("999");
		mw.setDescriptionInUUB(optionText);
		Option_UseCancel oc = new Option_UseCancel();
		oc.setText(Translate.text_364);
		oc.setColor(0xffffff);
		List<Option> options = new ArrayList<Option>();
		Option_SilverCar_Help car_Help = new Option_SilverCar_Help(getId());
		car_Help.setText("义不容辞");
		options.add(car_Help);
		options.add(oc);
		mw.setOptions(options.toArray(new Option[0]));
		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(res);
	}

	public SilvercarTaskCfg getCfg() {
		return cfg;
	}

	public void setCfg(SilvercarTaskCfg cfg) {
		this.cfg = cfg;
	}

	public boolean isJiazuCar() {
		return jiazuCar;
	}

	public void setJiazuCar(boolean jiazuCar) {
		this.jiazuCar = jiazuCar;
	}

	public int getRefershTimes() {
		return refershTimes;
	}

	public void setRefershTimes(int refershTimes) {
		this.refershTimes = refershTimes;
	}

	public int getMaxColor() {
		return maxColor;
	}

	public void setMaxColor(int maxColor) {
		this.maxColor = maxColor;
	}

	@Override
	public String toString() {
		return "BiaoCheNpc [toubao=" + toubao + ", lastCallForhelpTime=" + lastCallForhelpTime + ", jiazuCar=" + jiazuCar + ", destroyed=" + destroyed + ", cfg=" + cfg + ", refreshNPC=" + refreshNPC + ", attackers=" + getAttackers() + ", lastNoticeDamage=" + lastNoticeDamage + ", refershTimes=" + refershTimes + ", maxColor=" + maxColor + "]";
	}

	@Override
	public Object clone() {
		BiaoCheNpc p = new BiaoCheNpc();
		p.cloneAllInitNumericalProperty(this);
		p.setRadius(getRadius());
		p.setnPCCategoryId(this.getnPCCategoryId());
		p.windowId = windowId;
		p.setLife(this.getLife());
		return p;
	}

	public Hashtable<Long, Long> getAttackers() {
		return attackers;
	}

	public void setAttackers(Hashtable<Long, Long> attackers) {
		this.attackers = attackers;
	}
}
