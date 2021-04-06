package com.fy.engineserver.activity.xianling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.GameInfo;
import com.fy.engineserver.core.GameManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.buff.BuffTemplate_ZhongDu;
import com.fy.engineserver.datasource.buff.Buff_ZhongDu;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.event.Event;
import com.fy.engineserver.event.EventProc;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTIFY_EVENT_REQ;
import com.fy.engineserver.message.XL_USE_SKILL_REQ;
import com.fy.engineserver.message.XL_USE_SKILL_RES;
import com.fy.engineserver.sprite.Fighter;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.monster.MemoryMonsterManager;
import com.fy.engineserver.sprite.monster.Monster;
import com.fy.engineserver.sprite.monster.SimpleMonster;
import com.fy.engineserver.util.ServiceStartRecord;

public class XianLingChallengeManager implements EventProc {
	public static Logger logger = LoggerFactory.getLogger(XianLingManager.class);
	public static XianLingChallengeManager instance;
	/** 仙灵挑战线程list */
	public List<XianLingChallengeThread> threads = new ArrayList<XianLingChallengeThread>();
	/** 开启线程个数 */
	public static int threadNum = 5;
	/** 玩家进入出生点 */
	public static int[] bornPoint = new int[] { 1254, 1266 };
	/** boss出生点 */
	public static int[] bornPoint4Boss = new int[] { 1272, 851 };
	/** 仙灵挑战的地图，init时加进来 */
	public static Set<String> mapNames = new HashSet<String>();
	private volatile int currentNum = 0;
	/** 关卡倒计时时间 */
	public static int countDownTime = 4 * 60000; // 毫秒
	/** 捕捉读条时间 */
	public static int barTime = 5000; // 毫秒
	public static int REMOVETIME = 5000; // 毫秒

	public Object lock = new Object();

	public static XianLingChallengeManager getInstance() {
		return instance;
	}

	public static void setInstance(XianLingChallengeManager instance) {
		XianLingChallengeManager.instance = instance;
	}

	public Map<Long,Long> catchMonster = new LinkedHashMap<Long,Long>();
	
	/**
	 * 接收到客户端发来挑战的协议处理
	 * @param player
	 * @param level
	 */
	public synchronized void startChallenge(Player player, int level, long monsterId, int categoryId) {
		XianLingLevelData levelData = XianLingManager.instance.levelDatas.get(level);
		if (levelData != null) {
			// String result = canChallenge(player, levelData);
			// if (result == null) {
			String gameName = levelData.getGameName();
			if (!mapNames.contains(gameName)) {
				mapNames.add(gameName);
			}
			add2Thread(player, levelData, monsterId, categoryId);
			// } else {
			// player.sendError(result);
			// if (logger.isDebugEnabled()) {
			// logger.debug("[仙灵挑战] [不允许挑战] [判定结果:" + result + "] [" + player.getLogString() + "]");
			// }
			// }
		}

	}

	public Monster refreashMonster(Game game, long monsterId, int scale) {
		if (game == null || monsterId <= 0) {
			if (logger.isWarnEnabled()) logger.warn("[仙灵] [刷怪异常] [传入game:" + game + "] [monsterId :" + monsterId + "]");
			return null;
		}
		Monster monster = null;
		try {
			monster = MemoryMonsterManager.getMonsterManager().getMonster(monsterId);
			monster.setX(bornPoint4Boss[0]);
			monster.setY(bornPoint4Boss[1]);
			monster.setBornPoint(new Point(monster.getX(), monster.getY()));
			if (scale > 0) {
				monster.setObjectScale((short) scale);
			}
			game.addSprite(monster);
			if (logger.isDebugEnabled()) logger.debug("[仙灵挑战] [刷怪] [成功] [传入game:" + game + "] [monsterId :" + monsterId + "] [scale:" + scale + "]");

		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [刷出挑战怪异常] [怪物id:" + monsterId + "]", e);
			e.printStackTrace();
		}
		return monster;
	}

	/**
	 * 怪物死亡通知
	 * @param monster
	 */
	public void notifyMonsterKilled(SimpleMonster monster) {
		if (monster != null/* && monster.getOwner() != null */) {
			XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
			for (XianLingChallengeThread ft : list) {
				if (ft != null && ft.isPlayerAtThread(monster.getOwnerId())) {
					ft.notifyMonsterKilled(monster);
					break;
				}
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵挑战] [有怪物被杀死通知,异常][monster:" + monster + "]");

		}
	}

	/**
	 * 处理使用技能协议
	 * @param req
	 * @param player
	 */
	public XL_USE_SKILL_RES handle_USE_SKILL_REQ(XL_USE_SKILL_REQ req, Player player) {
		int skillId = req.getSkillId();
		if (logger.isInfoEnabled()) logger.info("[仙灵] [处理使用技能协议] [XianLingChallengeManager.handle_USE_SKILL_REQ] [" + player.getLogString() + "] [skillId:" + skillId + "]");
		int cdTime = 0;
		int num = 0;
		try {
			XianLingSkill skill = XianLingManager.instance.skillMap.get(skillId);
			if (skill != null) {
				// 判断是否在cd中
				PlayerXianLingData xianLingData = player.getXianlingData();
				if (xianLingData != null) {
					Map<Integer, Long> skillCDMap = xianLingData.getSkillCDMap();
					if (skillCDMap != null) {
						if (skillCDMap.containsKey(skill.getSkillId())) {
							if (skillCDMap.get(skill.getSkillId()) > System.currentTimeMillis()) {
								player.sendError(Translate.技能cd中);
								return null;
							} else {
								cdTime = skill.getCdTime() > skill.getPuclicCDTime() ? skill.getCdTime() : skill.getPuclicCDTime();
								skillCDMap.put(skill.getSkillId(), cdTime + System.currentTimeMillis());
							}
						}
					}
					if (logger.isInfoEnabled()) logger.info("[仙灵] [使用技能] [cd判断结束] [XianLingChallengeManager.handle_USE_SKILL_REQ] [" + player.getLogString() + "] [skillId:" + skill.getSkillId() + "] [skillName:" + skill.getName() + "]");

					XianLingChallenge xc = findXLChallenge(player);
					if (xc != null) {
						Monster monster = MemoryMonsterManager.getMonsterManager().getMonster(xc.getTargetId());
						if (monster != null) {
							boolean removeArticle = false;
							if (skill.getArticleCNName().equalsIgnoreCase("NULL")) {
								removeArticle = true;
							} else {
								if (skill.getSkillId() == 12 && monster.getHp() <= 1) {
									player.send_HINT_REQ(Translate.少于1点血, (byte) 5);
									return null;
								}
								Article a = ArticleManager.getInstance().getArticleByCNname(skill.getArticleCNName());
								if (a != null) {
									num = player.countArticleInKnapsacksByName(a.getName());
									if (num > 0) {
										ArticleEntity ae = player.removeArticle(a.getName(), "仙灵捕捉技能删除", "");
										if (ae != null) {
											removeArticle = true;
											num--;
										}
									} else {
										player.sendError(Translate.translateString(Translate.没有物品, new String[][] { { Translate.STRING_1, a.getName() } }));
										return null;
									}
								}
							}
							if (logger.isDebugEnabled()) logger.debug("[仙灵] [使用技能] [扣除道具:" + removeArticle + "] [XianLingChallengeManager.handle_USE_SKILL_REQ] [" + player.getLogString() + "] [skillId:" + skill.getSkillId() + "] [skillName:" + skill.getName() + "]");
							if (removeArticle) {
								// 技能粒子
								monster.setParticleName("");
								monster.setParticleName(skill.getParticle());
								switch (skill.getSkillId()) {
								case 11:
									xc.setCatchTimes(xc.getCatchTimes() + 1);
									catchMonster(player, monster, xc.getCatchTimes());
									break;
								case 12:
									forceKill(player, monster);
									break;
								case 13:
									regularDamage(player, monster);
									break;
								case 14:
									zhongdu(player, monster, skill.getBuffLastTime());
									break;
								default:
									break;

								}
								XL_USE_SKILL_RES res = new XL_USE_SKILL_RES(req.getSequnceNum(), skillId, cdTime, num);
								if (logger.isErrorEnabled()) logger.error("[仙灵] [发送使用技能] [XianLingChallengeManager.handle_USE_SKILL_REQ] [" + player.getLogString() + "] [skillId:" + skill.getSkillId() + "] [skillName:" + skill.getName() + "]");
								return res;
							}
						}
					}
				} else {
					if (logger.isErrorEnabled()) logger.error("[仙灵] [使用技能] [PlayerXianLingData=null] [XianLingChallengeManager.handle_USE_SKILL_REQ] [" + player.getLogString() + "] [skillId:" + skill.getSkillId() + "] [skillName:" + skill.getName() + "]");
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵] [使用技能] [异常] [XianLingChallengeManager.handle_USE_SKILL_REQ] [skillId:" + skillId + "]" + player.getLogString(), e);
		}
		return null;
	}

	/**
	 * 处理客户端发来的怪物捕捉协议
	 * @param monster
	 */
	public boolean catchMonster(Player player, Monster monster, int catchTimes) {
		if (monster != null/* && monster.getOwner() != null */) {
			try {
				XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
				for (XianLingChallengeThread ft : list) {
					if (ft != null && ft.isPlayerAtThread(player.getId())) {
						ft.notifyMonsterCatched(player, monster, catchTimes);
						return true;
					}
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) logger.error("[仙灵挑战] [捕捉怪物通知,异常][monster:" + monster + "]", e);
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵挑战] [捕捉怪物通知,异常][monster:" + monster + "]");

		}
		return false;
	}

	/**
	 * 使用强杀技能
	 * @param monster
	 */
	public boolean forceKill(Player player, Monster monster) {
		if (monster != null) {
			XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
			for (XianLingChallengeThread ft : list) {
				if (ft != null && ft.isPlayerAtThread(player.getId())) {
					if (monster.getHp() > 1) {
						int damage = monster.getHp() - 1;
						monster.causeDamage(player, damage, 0, Fighter.DAMAGETYPE_PHYSICAL);
						NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, monster.getId(), (byte) 7, damage);
						player.addMessageToRightBag(req);
						if (logger.isInfoEnabled()) logger.info("[仙灵挑战] [使用强杀技能][monster:" + monster.getName() + "] [damage:" + damage + "]");
						return true;
					}
					break;
				}
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵挑战] [使用强杀技能异常][monster:" + monster + "]");
		}
		return false;
	}

	/**
	 * 使用固伤技能
	 * @param monster
	 */
	public boolean regularDamage(Player player, Monster monster) {
		if (monster != null) {
			XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
			for (XianLingChallengeThread ft : list) {
				if (ft != null && ft.isPlayerAtThread(player.getId())) {
					// TODO 公式计算伤害值 int damageValue =
					int damage = monster.getMaxHP() / 5;
					// TODO int hateParam是啥
					monster.causeDamage(player, damage, 0, Fighter.DAMAGETYPE_PHYSICAL);
					NOTIFY_EVENT_REQ req = new NOTIFY_EVENT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, monster.getId(), (byte) 7, damage);
					player.addMessageToRightBag(req);
					if (logger.isInfoEnabled()) logger.info("[仙灵挑战] [使用固伤技能][monster:" + monster.getName() + "] [damage:" + damage + "]");
					return true;
				}
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[仙灵挑战] [使用固伤技能异常][monster:" + monster + "]");
		}
		return false;
	}

	/**
	 * 使用中毒技能
	 * @param player
	 * @param monster
	 * @param lastingTime
	 */
	public boolean zhongdu(Player player, Monster monster, long lastingTime) {
		if (monster != null) {
			try {
				XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
				for (XianLingChallengeThread ft : list) {
					if (ft != null && ft.isPlayerAtThread(player.getId())) {
						BuffTemplate_ZhongDu bb = new BuffTemplate_ZhongDu();
						Buff_ZhongDu buff = bb.createBuff(0);
						buff.setStartTime(SystemTime.currentTimeMillis());
						buff.setInvalidTime(SystemTime.currentTimeMillis() + lastingTime);
						buff.setCauser(player);
						buff.hpFix = (int) Math.ceil((float) monster.getMaxHP() / 100 * 11);
						StringBuffer sb = new StringBuffer();
						sb.append(Translate.text_3391 + (1) + Translate.text_3234 + buff.hpFix + Translate.text_3275);
						buff.setLastingTime(1000); // 1s跳一次
						buff.setDescription(sb.toString());
						buff.setIconId(bb.getIconId() == null ? "" : bb.getIconId());
						monster.placeBuff(buff);
						if (logger.isInfoEnabled()) logger.info("[仙灵挑战] [使用中毒技能][monster:" + monster.getName() + "]");
						return true;
					}
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) logger.error("[仙灵挑战] [使用中毒技能,异常][monster:" + monster.getName() + "]", e);
			}
		} else {
		}
		return false;
	}

	/**
	 * 创建副本添加到Thread
	 * @param player
	 * @param gameName
	 */
	public void add2Thread(Player player, XianLingLevelData levelData, long monsterId, int categoryId) {
		try {
			String gameName = levelData.getGameName();
			Game game = this.createNewGame(player, gameName);
			if (game != null) {
				synchronized (lock) {
					if (currentNum >= threads.size()) {
						currentNum = 0;
					}
					threads.get(currentNum++).notifyStartChallenge(levelData, monsterId, game, player, categoryId);
				}
			}
		} catch (Exception e) {
			if (logger.isErrorEnabled()) logger.error("[仙灵挑战] [添加到Thread]", e);
		}
	}

	public Game findGame(Player player) {
		Game g = null;
		XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
		for (XianLingChallengeThread t : list) {
			g = t.findGame(player);
			if (g != null) {
				return g;
			}
		}
		return null;
	}

	/**
	 * 获得玩家挑战信息
	 * @param player
	 * @return
	 */
	public XianLingChallenge findXLChallenge(Player player) {
		XianLingChallenge xc = null;
		XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
		for (XianLingChallengeThread t : list) {
			xc = t.findXLChallenge(player);
			if (xc != null) {
				return xc;
			}
		}
		return null;
	}

	/**
	 * 当玩家非正常退出副本时调用，将玩家从副本线程中删除
	 * @param player
	 */
	public void deleteXLChallenge(Player player) {
		XianLingChallenge xc = findXLChallenge(player);
		if (xc != null) {
			xc.setResult(XianLingChallenge.TRANSFER);
			if (XianLingManager.logger.isErrorEnabled()) XianLingManager.logger.error("[仙灵] [副本中传送] [" + player.getLogString() + "]");
		}
	}

	/**
	 * 验证可否挑战
	 * @param player
	 * @param levelData
	 * @return
	 */
	public String canChallenge(Player player, XianLingLevelData levelData) {
		String result = null;
		if (player.getLevel() <= XianLingManager.NEEDLEVEL) {
			result = String.format(Translate.等级不够无法打开界面, XianLingManager.NEEDLEVEL);
		}
		XianLing activity = XianLingManager.instance.getCurrentActivity();
		if (activity == null) {
			result = Translate.不在时间段;
		} else {
			result = activity.isThisServerFit();
		}
		String gameName = levelData.getGameName();
		if (gameName == null || gameName.isEmpty()) {
			result = Translate.地图名为空;
		}
		// if (player.getActivePetId() > 0) {
		// result = Translate.pet_fight;
		// }
		if (player.getTeamMark() != Player.TEAM_MARK_NONE) {
			// result = Translate.组队状态不可挑战仙尊;
		}
		PlayerXianLingData xianlingData = player.getXianlingData();
		if (xianlingData != null) {
			if (levelData.getLevel() > (xianlingData.getMaxLevel() + 1)) {
				result = Translate.前面还有未开启的关卡;
			} else if (xianlingData.getMaxLevel() >= levelData.getLevel() && levelData.getType() > 0) {
				result = Translate.高级关卡只能挑战一次;
			}
		}
		if (xianlingData.getEnergy() < 1) {
			result = Translate.真气不足;
		}
		return result;
	}

	/**
	 * 创建关卡场景
	 * @param player
	 * @param mapname
	 * @return
	 */
	public Game createNewGame(Player player, String mapname) {
		try {
			GameManager gameManager = GameManager.getInstance();
			GameInfo gi = gameManager.getGameInfo(mapname);
			if (gi == null) {
				if (logger.isWarnEnabled()) logger.warn("[仙灵挑战] [创建关卡场景失败] [对应的地图信息不存在][玩家:{}][{}][{}]", new Object[] { player.getName(), player.getId(), mapname });
				return null;
			}
			Game newGame = new Game(gameManager, gi);
			try {
				newGame.init();
			} catch (Exception e) {
				logger.error("[仙灵挑战] [初始化场景异常][e:" + e + "]");
				e.printStackTrace();
			}
			return newGame;
		} catch (Exception e) {
			logger.error("[仙灵挑战] [创建关卡场景异常][e:" + e + "]");
		}
		return null;
	}

	public void init() {
		
		instance = this;
		this.doReg();
//		for (int i = 0; i < threadNum; i++) {
//			XianLingChallengeThread xc = new XianLingChallengeThread();
//			xc.setName("仙灵挑战线程-" + i);
//			xc.start();
//			threads.add(xc);
//		}
		List<XianLingLevelData> levelDatas = XianLingManager.instance.levelDatas;
		for (XianLingLevelData data : levelDatas) {
			mapNames.add(data.getGameName());
		}
		ServiceStartRecord.startLog(this);
	}

	public void destory() {
		XianLingManager.logger.error("[仙灵] [XianLingChallengeManager.destroy]");
		XianLingChallengeThread[] list = threads.toArray(new XianLingChallengeThread[threads.size()]);
		for (XianLingChallengeThread t : list) {
			List<XianLingChallenge> gameList = t.getGameList();
			if (gameList.size() > 0) {
				for (XianLingChallenge xc : gameList) {
					try {
						Player player = PlayerManager.getInstance().getPlayer(xc.getPlayerId());
						XianLingManager.logger.error("[" + player.getLogString() + "] [仙灵] [XianLingChallengeManager.destroy] [服务器重启踢出副本]");
						xc.回城(player);
					} catch (Exception e) {
						XianLingManager.logger.error("[仙灵] [XianLingChallengeManager.destroy] [异常]", e);
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void proc(Event evt) {
		Object[] obj = null;
		EventWithObjParam evtWithObj = null;
		switch (evt.id) {
//		case Event.XIANLING_CHALLENGE_RESULT:
//			
//			evtWithObj = (EventWithObjParam) evt;
//			obj = (Object[]) evtWithObj.param;
//			Player player = (Player) obj[0];
//			byte result = (Byte) obj[1];
//			int score = (Integer) obj[2];
//			int categoryId = (Integer) obj[3];
//			
//			break;
		case Event.MONSTER_KILLED_Simple:
			evtWithObj = (EventWithObjParam) evt;
			obj = (Object[]) evtWithObj.param;
			SimpleMonster monster = (SimpleMonster) obj[0];
			if (monster.isBoss()) {
				this.notifyMonsterKilled(monster);
				// SealManager.getInstance().handleBossDead(monster);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void doReg() {
		EventRouter.register(Event.MONSTER_KILLED_Simple, this);
		EventRouter.register(Event.XIANLING_CHALLENGE_RESULT, this);
	}
}
