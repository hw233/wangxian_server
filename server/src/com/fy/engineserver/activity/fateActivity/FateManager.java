package com.fy.engineserver.activity.fateActivity;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.STRING_2;
import static com.fy.engineserver.datasource.language.Translate.XX放弃了xx;
import static com.fy.engineserver.datasource.language.Translate.XX放弃了xx请重新选择有缘人;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.上次xx过期;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.fateActivity.base.FateActivity;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.FateActivityPropsEntity;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.message.BEGIN_ACTIVITY_RES;
import com.fy.engineserver.message.ENTER_GETACTIVITY_RES;
import com.fy.engineserver.message.FINISH_ACTIVITY_RES;
import com.fy.engineserver.message.GET_FATEACTIVITY_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_DELIVER_TASK_REQ;
import com.fy.engineserver.message.SEEM_HINT_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.playerAims.tool.ReadFileTool;
import com.fy.engineserver.sprite.ActivityRecordOnPlayer;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.CacheObject;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class FateManager implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(FateManager.class);
	public static SimpleEntityManager<FateActivity> em;
	private String configFile = "F:\\config\\fateActivitys.xls";

	private Map<Long, FateTemplate> map = new HashMap<Long, FateTemplate>();
	private Map<Integer,Map<Long, FateTemplate>> map2country = new HashMap<Integer, Map<Long,FateTemplate>>();
	private Map<Integer, Long> exp1Map = new HashMap<Integer, Long>();
	private Map<Integer, Long> exp2Map = new HashMap<Integer, Long>();

	public LruMapCache mCache = new LruMapCache(8 * 1024 * 1024, 60 * 60 * 1000);

	private static FateManager self = null;

	public static FateManager getInstance() {
		return self;
	}

	public Map<Integer, Map<Long, FateTemplate>> getMap2country() {
		return map2country;
	}

	public void setMap2country(Map<Integer, Map<Long, FateTemplate>> map2country) {
		this.map2country = map2country;
	}

	public static final int 参加活动的级别 = 40;

	// 活动类型 0 1 2 3
	public final static int activityNum = 3;

	public boolean open = true;
	
	public static long heartBeatTime = 500;

	@Override
	public void run() {

		while (open) {
			try {
				Thread.sleep(heartBeatTime);

				if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
					continue;
				}
				Object os[] = mCache.values().toArray(new Object[0]);
				for (int i = 0; i < os.length; i++) {
					try {
						Object o = os[i];
						FateActivity fa = (FateActivity) ((CacheObject) o).object;
						fa.heartBeat();
						if (fa.getState() == FateActivity.删除状态) {
							mCache.remove(fa.getId());
							fa.setDeleted(true);
						}
					} catch (Exception e) {
						FateManager.logger.error("[仙缘活动心跳异常]", e);
					}
				}
			} catch (Throwable e) {
				logger.error("[仙缘实体心跳线程] [错误]", e);
			}
		}
	}

	public void init() throws Exception {

		

		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

		em = SimpleEntityManagerFactory.getSimpleEntityManager(FateActivity.class);
		this.loadExcel();
		self = this;
		Thread t = new Thread(this, "FateManager");
		t.start();
		loadExcel();

		if (logger.isWarnEnabled()) logger.warn("[初始化fateManager] [成功]");
		ServiceStartRecord.startLog(this);
	}

	public Random random = new Random();

	public void destory() {
		em.destroy();
	}

	/**
	 * 生成一个活动实体
	 * 
	 * @param player
	 * @return
	 */
	public FateActivity createFateActivity(Player player, FateTemplate ft) {
		byte type = ft.getType();
		FateActivity fa = null;

		long[] activityIds = player.getInitActivityId(type);
		ActivityRecordOnPlayer ar = player.getActivityRecord(ft.getType());
		if (activityIds[0] > 0) {
			// 有
			if (logger.isWarnEnabled()) logger.warn("[生成一个活动实体错误] [" + player.getLogString() + "] [已经有] [" + type + "]");
		} else {
			long id;
			try {
				id = em.nextId();
			} catch (Exception e) {
				logger.error("[生成一个活动实体id错误] [" + player.getLogString() + "] [" + type + "]", e);
				return null;
			}
			if (type == FateActivityType.国内仙缘.getType()) {
				fa = new FateActivityInCountry(id);
			} else if (type == FateActivityType.国外仙缘.getType()) {
				fa = new FateActivityAbroad(id);
			} else if (type == FateActivityType.国内论道.getType()) {
				fa = new BeerActivityInCountry(id);
			} else if (type == FateActivityType.国外论道.getType()) {
				fa = new BeerActivityAbroad(id);
			}
			fa.setTemplateId(ft.getId());
			fa.setInviteId(player.getId());
			fa.init();
			fa.flush(player);
			ar.setInitiativeActivityId(fa.getId(), player, type);
			if (type == FateActivityType.国内仙缘.getType()) {
				player.setDirty(true, "fate");
			} else if (type == FateActivityType.国外仙缘.getType()) {
				player.setDirty(true, "abroadFate");
			} else if (type == FateActivityType.国内论道.getType()) {
				player.setDirty(true, "beer");
			} else if (type == FateActivityType.国外论道.getType()) {
				player.setDirty(true, "abroadBeer");
			}
			this.mCache.put(fa.getId(), fa);
			em.notifyNewObject(fa);
			if (logger.isWarnEnabled()) logger.warn("[生成一个活动实体] [" + player.getLogString() + "] [" + fa.getClass() + "] [" + type + "]");
		}
		return fa;
	}

	/**
	 * @param player
	 * @param type
	 *            0 国内仙缘 1国外仙缘 2 国内论道 3国外论道
	 * @return
	 */

	public FateActivity getFateActivity(Player player, byte type) {

		long[] ids = player.getInitActivityId(type);
		FateActivity fa = null;
		if (ids[1] > 0) {
			// 有
			fa = this.getFateActivity(ids[1]);
			return fa;
		} else {
			fa = this.getFateActivity(ids[0]);
			return fa;
		}
	}

	/**
	 * 放弃活动 主动 被动
	 * 
	 * @param player
	 * @param type
	 * @return
	 */
	public boolean cancleActivity(Player player, byte type, boolean active) {
		FateActivity fa = null;
		if (active) {
			long id = player.getInitActivityId(type)[0];
			fa = this.getFateActivity(id);
		} else {
			fa = this.getFateActivity(player, type);
		}
		FateActivityType faType = this.getfateType(type);
		if (fa != null && faType != null) {
			if (fa.havaActive(player)) {
				if (fa.checkActive(player)) {

					if (!active) {
						logger.error("[活动id:" + fa.getId() + "] [玩家放弃活动错误] [被动放弃主动任务] [" + player.getLogString() + "] [活动类型:" + type + "] [主被动:" + active + "]");
						return false;
					}
					// 邀请
					fa.setState(FateActivity.删除状态);
					player.getActivityRecord(type).setInitiativeActivityId(-1, player, type);
					// this.noticePlayerFateActivity(player, faType);
					this.finishNotice(player, faType.type);
					if (fa.getInvitedId() > 0) {
						try {
							Player p2 = PlayerManager.getInstance().getPlayer(fa.getInvitedId());
							p2.getActivityRecord(type).setPassivityActivityId(-1, p2, type);

							// SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), type,player.getName() + 放弃了 + faType.name);
							String result = translateString(XX放弃了xx, new String[][] { { STRING_1, player.getName() }, { STRING_2, faType.name } });
							SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), type, result);
							p2.addMessageToRightBag(res);
							// this.noticePlayerFateActivity(p2, faType);
							this.finishNotice(p2, faType.type);
							if (logger.isWarnEnabled()) {
								logger.warn("[活动id:" + fa.getId() + "] [邀请者放弃活动success] [" + player.getLogString() + "] []");
							}
						} catch (Exception e) {
							FateManager.logger.error("[玩家放弃活动异常] [" + player.getLogString() + "]", e);
						}
					}
					boolean delete = false;
					// 删除道具
					ArticleEntityManager aem = ArticleEntityManager.getInstance();
					ArticleEntity ae = null;
					Knapsack[] ks = new Knapsack[3];
					ks[0] = player.getKnapsack_common();
					ks[1] = player.getKnapsack_fangbao();
					ks[2] = player.getKnapsacks_cangku();
					for (Knapsack ka : ks) {
						for (Cell c : ka.getCells()) {
							if (c.getEntityId() > 0) {
								ae = aem.getEntity(c.getEntityId());
								if (ae != null && ae instanceof FateActivityPropsEntity) {
									FateActivityPropsEntity fae = (FateActivityPropsEntity) ae;
									if (fae.getActivityType() == 0) {
										if (type == 0 || type == 1) {
											// 可以删除
											player.removeArticleEntityFromKnapsackByArticleId(c.getEntityId(), "放弃仙缘", true);
											delete = true;
											break;
										}
									} else {
										if (type == 2 || type == 3) {
											// 可以删除
											player.removeArticleEntityFromKnapsackByArticleId(c.getEntityId(), "放弃论道", true);
											delete = true;
											break;
										}
									}
								}
							}
						}
						if (delete) {
							if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[删除任务删除道具] [" + player.getLogString() + "] []");
							break;
						}
					}
					if (!delete) {
						if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[删除任务删除道具错误] [" + player.getLogString() + "] [没有删除]");
					}
				} else {
					// 被邀请
					if (active) {
						logger.error("[活动id:" + fa.getId() + "] [玩家放弃活动错误] [主动放弃被动任务] [" + player.getLogString() + "] [活动类型:" + type + "] [主被动:" + active + "]");
						return false;
					}

					if (fa.getState() >= FateActivity.进行状态) {
						// fa.setState(FateActivity.完成状态);
						if (fa.getState() == FateActivity.进行状态) {
							player.sendError(Translate.已经开始不能放弃);
							return false;
						}
					} else {
						player.getActivityRecord(type).setPassivityActivityId(-1, player, type);
						fa.setState(FateActivity.选人状态);
						// this.noticePlayerFateActivity(player, faType);
						this.finishNotice(player, faType.type);
					}

					fa.setInvitedId(-1);
					try {
						Player invite = PlayerManager.getInstance().getPlayer(fa.getInviteId());
						// SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), type, player.getName() + 放弃了 + faType.name+请重新选择有缘人);
						String result = translateString(XX放弃了xx请重新选择有缘人, new String[][] { { STRING_1, player.getName() }, { STRING_2, faType.name } });
						SEEM_HINT_RES res = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), type, result);
						invite.addMessageToRightBag(res);
					} catch (Exception e) {
						logger.error("[被邀请者放弃活动异常] [" + player.getLogString() + "]", e);
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[被邀请者放弃活动success] [" + player.getLogString() + "]");
					}
				}
			} else {
				if (logger.isWarnEnabled()) logger.warn("[放弃活动] [指定的活动中没有此人] [" + player.getLogString() + "]");
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[放弃活动] [没有活动] [" + player.getLogString() + "]");
			return false;
		}

		return true;
	}

	public FateActivity beginActivity(Player player, long id) {
		FateTemplate template = FateManager.getInstance().getMap().get((long) id);
		if (template == null) {
			template = FateManager.getInstance().getMap2country().get(player.getCountry()).get((long) id);
			if(template == null){
				if (logger.isInfoEnabled()) logger.info("[生成活动实体失败] [" + player.getLogString() + "] [指定的id为null] [" + id + "] [] ");
				return null;
			}
		}
		FateActivity fa = this.createFateActivity(player, template);
		if (fa != null) {

			GET_FATEACTIVITY_RES res = null;
			List<Player> randomUndoPlayer = new ArrayList<Player>();
			PlayerManager pm = PlayerManager.getInstance();
			for (long id1 : fa.getRandomUndo()) {
				try {
					randomUndoPlayer.add(pm.getPlayer(id1));
				} catch (Exception e) {
					FateManager.logger.error("[玩家生成活动异常] [" + player.getLogString() + "]", e);
				}
			}
			List<Player> randomdoPlayer = new ArrayList<Player>();
			for (long id1 : fa.getRandomdo()) {
				try {
					randomdoPlayer.add(pm.getPlayer(id1));
				} catch (Exception e) {
					FateManager.logger.error("[玩家生成活动异常] [" + player.getLogString() + "]", e);
				}
			}
			try {
				res = new GET_FATEACTIVITY_RES(GameMessageFactory.nextSequnceNum(), fa.getTemplate().getType(), true, (byte) 0, "", 0l, "", (byte) fa.getCountry(), randomUndoPlayer.toArray(new Player[0]), randomdoPlayer.toArray(new Player[0]), fa.getTemplate().getFlushInterval());
				player.addMessageToRightBag(res);
			} catch (Exception e) {
				logger.error("[生成活动实体协议错误] [" + player.getLogString() + "]", e);
			}
			// 返回
			if (logger.isWarnEnabled()) {
				logger.warn("[生成活动实体] [" + player.getLogString() + "] [" + fa.getId() + "]");
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[生成活动实体为null] [" + player.getLogString() + "]");
		}

		return fa;
	}

	public FateActivityType getfateType(byte type) {
		FateActivityType ft = null;
		// byte type = fa.getTemplate().getType();
		if (type == FateActivityType.国内仙缘.type) {
			ft = FateActivityType.国内仙缘;
		} else if (type == FateActivityType.国外仙缘.type) {
			ft = FateActivityType.国外仙缘;
		} else if (type == FateActivityType.国内论道.type) {
			ft = FateActivityType.国内论道;
		} else if (type == FateActivityType.国外论道.type) {
			ft = FateActivityType.国外论道;
		}
		return ft;
	}

	// 玩家放弃(被邀请人放弃，邀请人放弃) 完成活动后(俩人都发)的删除时间 图标等
	public void finishNotice(Player player, byte type) {

		long[] ids = player.getActivityRecord(type).getActivityId();
		if (ids[0] < 0 && ids[1] < 0) {
			FINISH_ACTIVITY_RES res = new FINISH_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(), type);
			player.addMessageToRightBag(res);
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[finishNotice图标不改] [类型:" + type + "] [" + player.getLogString() + "]");
			}
			return;
		}

		if (type == 0) {
			ids = player.getActivityRecord((byte) 1).getActivityId();
			if (ids[0] > 0 || ids[1] > 0) {
				player.addMessageToRightBag(new ENTER_GETACTIVITY_RES(GameMessageFactory.nextSequnceNum(), (byte) 1));
				if (logger.isWarnEnabled()) {
					logger.warn("[finishNotice图标提示] [类型:" + 1 + "] [" + player.getLogString() + "]");
				}
			}
		} else if (type == 1) {
			ids = player.getActivityRecord((byte) 0).getActivityId();
			if (ids[0] > 0 || ids[1] > 0) {
				player.addMessageToRightBag(new ENTER_GETACTIVITY_RES(GameMessageFactory.nextSequnceNum(), (byte) 0));
				if (logger.isWarnEnabled()) {
					logger.warn("[finishNotice图标提示] [类型:" + 0 + "] [" + player.getLogString() + "]");
				}
			}
		} else if (type == 2) {
			ids = player.getActivityRecord((byte) 3).getActivityId();
			if (ids[0] > 0 || ids[1] > 0) {
				player.addMessageToRightBag(new ENTER_GETACTIVITY_RES(GameMessageFactory.nextSequnceNum(), (byte) 3));
				if (logger.isWarnEnabled()) {
					logger.warn("[finishNotice图标提示] [类型:" + 3 + "] [" + player.getLogString() + "]");
				}
			}
		} else if (type == 3) {
			ids = player.getActivityRecord((byte) 2).getActivityId();
			if (ids[0] > 0 || ids[1] > 0) {
				player.addMessageToRightBag(new ENTER_GETACTIVITY_RES(GameMessageFactory.nextSequnceNum(), (byte) 2));
				if (logger.isWarnEnabled()) {
					logger.warn("[finishNotice图标提示] [类型:" + 2 + "] [" + player.getLogString() + "]");
				}
			}
		}
	}

	// 只在登陆的时候判断
	public boolean noticePlayerFateActivity(Player player, FateActivityType ft) {

		long[] ids = null;
		FateActivity fa = null;
		ids = player.getInitActivityId(ft.type);
		player.getActivityRecord(ft.type).enterUpdateNum(ft.type, player);
		if (ids[1] > 0) {
			fa = this.getFateActivity(ids[1]);
			if (fa != null) {
				if (!fa.isFinish()) {
					int time = (int) ((fa.getStartTime() + fa.getTemplate().getDuration() * 1000 - SystemTime.currentTimeMillis()) / 1000);
					if (time > 0) {
						// 还有活动
						Game game = player.getCurrentGame();
						if (game.country == fa.getCountry()) {
							if (game.getGameInfo().getMapName().equals(fa.getMapName())) {
								BEGIN_ACTIVITY_RES res = new BEGIN_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(), ft.type, fa.getX(), fa.getY(), time);
								player.addMessageToRightBag(res);
								if (logger.isWarnEnabled()) {
									logger.warn("[时间提示] [" + player.getLogString() + "]");
								}
							}
						}

					}
					player.addMessageToRightBag(new ENTER_GETACTIVITY_RES(GameMessageFactory.nextSequnceNum(), ft.type));
					if (logger.isWarnEnabled()) {
						logger.warn("[图标提示] [" + player.getLogString() + "]");
					}
					return true;
				} else {
					// player.getActivityRecord(ft.type).setPassivityActivityId(-1);
					Player invite = null;
					try {
						invite = PlayerManager.getInstance().getPlayer(fa.getInviteId());
					} catch (Exception e) {
						FateManager.logger.error("[登陆活动完成] [添加上次完成记录] [被动] [" + player.getLogString() + "]", e);
					}
					player.getActivityRecord(ft.type).addFinishRecord(invite, true, ft.type);

					finishNotice(player, ft.type);
					// player.send_HINT_REQ("上次" + ft.name + "过期");
					player.send_HINT_REQ(translateString(上次xx过期, new String[][] { { STRING_1, ft.name } }));
					fa.setState(FateActivity.删除状态);
					if (logger.isWarnEnabled()) {
						logger.warn("[玩家登陆] [" + ft.name + "活动过期]");
					}
				}
			} else {
				logger.error("[登陆查询仙缘活动错误] [没有活动] [" + ft.name + "] [" + player.getLogString() + "] [活动id:" + ids[1] + "]");
			}
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[登陆查询被动活动] [没有活动] [" + ft.name + "] [" + player.getLogString() + "] [活动id:" + ids[1] + "]");
			}
		}
		if (ids[0] > 0) {
			fa = this.getFateActivity(ids[0]);
			if (fa != null) {
				if (!fa.isFinish()) {
					int time = (int) ((fa.getStartTime() + fa.getTemplate().getDuration() * 1000 - SystemTime.currentTimeMillis()) / 1000);
					if (time > 0) {

						Game game = player.getCurrentGame();
						if (game.country == fa.getCountry()) {
							if (game.getGameInfo().getMapName().equals(fa.getMapName())) {
								BEGIN_ACTIVITY_RES res = new BEGIN_ACTIVITY_RES(GameMessageFactory.nextSequnceNum(), ft.type, fa.getX(), fa.getY(), time);
								player.addMessageToRightBag(res);
								if (logger.isWarnEnabled()) {
									logger.warn("[时间提示] [" + player.getLogString() + "]");
								}
							}
						}
					}
					player.addMessageToRightBag(new ENTER_GETACTIVITY_RES(GameMessageFactory.nextSequnceNum(), ft.type));
					if (logger.isWarnEnabled()) {
						logger.warn("[图标提示] [" + player.getLogString() + "]");
					}
					return true;
				} else {
					fa.setState(FateActivity.删除状态);
					// player.send_HINT_REQ("上次" + ft.name + "过期");
					player.send_HINT_REQ(translateString(上次xx过期, new String[][] { { STRING_1, ft.name } }));
					// player.getActivityRecord(ft.type).setInitiativeActivityId(-1);
					Player invited = null;
					try {
						invited = PlayerManager.getInstance().getPlayer(fa.getInvitedId());
					} catch (Exception e) {
						FateManager.logger.error("[登陆活动完成] [主动] [添加上次完成记录] [" + player.getLogString() + "]", e);
					}
					player.getActivityRecord(ft.type).addFinishRecord(invited, false, ft.type);
					long id = fa.getTemplate().getId();
					Task task = TaskManager.getInstance().getTask(id);
					TaskEntity te = player.getTaskEntity(id);
					if (task != null && te != null) {
						if (te.getStatus() == TaskEntity.TASK_STATUS_GET) {
							te.setStatus(TaskEntity.TASK_STATUS_COMPLETE);
							NOTICE_CLIENT_DELIVER_TASK_REQ req = new NOTICE_CLIENT_DELIVER_TASK_REQ(GameMessageFactory.nextSequnceNum(), id);
							player.addMessageToRightBag(req);

							if (ft.type == FateActivityType.国内仙缘.type || ft.type == FateActivityType.国外仙缘.type) {
								AchievementManager.getInstance().record(player, RecordAction.仙缘完成次数);
								ActivenessManager.getInstance().record(player, ActivenessType.仙缘论道);
								if (ft.type == FateActivityType.国内仙缘.type) {
									AchievementManager.getInstance().record(player, RecordAction.国内仙缘次数);
								} else if (ft.type == FateActivityType.国外仙缘.type) {
									AchievementManager.getInstance().record(player, RecordAction.国外仙缘次数);
								}
							} else if (ft.type == FateActivityType.国内论道.type || ft.type == FateActivityType.国外论道.type) {
								AchievementManager.getInstance().record(player, RecordAction.论道完成次数);
								if(ft.type == FateActivityType.国内论道.type) {
									AchievementManager.getInstance().record(player, RecordAction.国内论道次数);
								} else if (ft.type == FateActivityType.国外论道.type) {
									AchievementManager.getInstance().record(player, RecordAction.国外论道次数);
								}
								ActivenessManager.getInstance().record(player, ActivenessType.仙缘论道);
							}
						}
						if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[登陆完成活动] [统计成就] [" + ft.type + "] [" + fa.getActivityName() + "] [" + player.getLogString() + "]");
					} else {
						FateManager.logger.error("[登陆完成活动错误] [没有指定任务]  [" + player.getLogString() + "] [任务id:" + id + "]");
					}
					if (logger.isDebugEnabled()) {
						logger.debug("[玩家登陆] [" + ft.name + "活动过期]");
					}
				}
			} else {
				logger.error("[登陆查询仙缘活动错误] [没有活动] [" + ft.name + "] [" + player.getLogString() + "] [活动id:" + ids[0] + "]");
			}
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[登陆查询主动活动] [没有活动] [" + ft.name + "] [" + player.getLogString() + "] [活动id:" + ids[0] + "]");
			}
		}
		return false;
	}

	// 玩家登陆
	public void noticePlayerFateActivity(Player player) {
		try {
			if (!noticePlayerFateActivity(player, FateActivityType.国内仙缘)) {
				noticePlayerFateActivity(player, FateActivityType.国外仙缘);
			}
			if (!noticePlayerFateActivity(player, FateActivityType.国内论道)) {
				noticePlayerFateActivity(player, FateActivityType.国外论道);
			}

			if (logger.isWarnEnabled()) logger.warn("[登陆查看仙缘论道任务] [" + player.getLogString() + "] ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("[登陆查看仙缘论道任务异常] [" + player.getLogString() + "]", e);
		}
	}

	public FateActivity getFateActivity(long id) {

		FateActivity fa = (FateActivity) mCache.get(id);
		if (fa == null) {
			try {
				fa = em.find(id);
			} catch (Exception e) {
				logger.error("[查看仙缘论道任务异常]", e);
			}
			if (fa != null) {
				// if (fa.getState() == FateActivity.删除状态)
				// return null;
				fa.init();
				byte type = fa.getTemplate().getType();
				switch (type) {

				case 0:
					fa = (FateActivityInCountry) fa;
					break;
				case 1:
					fa = (FateActivityAbroad) fa;
					break;
				case 2:
					fa = (BeerActivityInCountry) fa;
					break;
				case 3:
					fa = (BeerActivityAbroad) fa;
					break;
				}
				mCache.put(fa.getId(), fa);
			}
		}
		if (fa != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("[通过id获得活动实体] [" + id + "] [类型] [" + fa.getTemplate().getType() + "] [" + (fa == null ? "NULL" : fa.getLogString()) + "]");
			}
		}
		return fa;

	}

	private void loadExcel() throws Exception {

		this.map.clear();

		int idNum = 0;
		int 活动名 = 1;
		int 地图名 = 2;
		int 区域名 = 3;
		int 持续多长时间 = 4;
		int 俩人距离 = 5;
		int 经验变化间隔 = 6;
		int 刷新时间间隔 = 7;
		int 道具名 = 8;
		int 刷新没有跟自己做过的个数 = 9;
		int 刷新跟自己做过的个数 = 10;
		int 一天可以做几次 = 11;
		int 类型 = 12;
		int 物品 = 13;
		int 国家 = 14;

		int 级别 = 0;
		int 经验 = 1;
		File file = new File(configFile);
		HSSFWorkbook workbook = null;
		try {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;

			// 国内经验
			sheet = workbook.getSheetAt(0);
			if (sheet == null) return;
			int rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					HSSFCell cell = row.getCell(级别);
					int level = (int) (cell.getNumericCellValue());
					cell = row.getCell(经验);
					long exp = (long) (cell.getNumericCellValue());

					exp1Map.put(level, exp);
				}
			}
			// 国外经验
			sheet = workbook.getSheetAt(1);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();
			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					HSSFCell cell = row.getCell(级别);
					int level = (int) (cell.getNumericCellValue());
					cell = row.getCell(经验);
					long exp = (long) (cell.getNumericCellValue());

					exp2Map.put(level, exp);
				}
			}

			// 参数配置
			sheet = workbook.getSheetAt(2);
			if (sheet == null) return;
			rows = sheet.getPhysicalNumberOfRows();

			for (int r = 1; r < rows; r++) {
				HSSFRow row = sheet.getRow(r);
				if (row != null) {
					FateTemplate ft = new FateTemplate();
					HSSFCell cell = row.getCell(idNum);
					long id = (long) (cell.getNumericCellValue());
					ft.setId(id);

					cell = row.getCell(活动名);
					String name = (cell.getStringCellValue());
					ft.setActivityName(name);

					cell = row.getCell(地图名);
					String mapName = cell.getStringCellValue();
					ft.setMapName(mapName);

					cell = row.getCell(区域名);
					String region = (cell.getStringCellValue());
					ft.setRegionName(region);

					cell = row.getCell(持续多长时间);
					int duration = (int) (cell.getNumericCellValue());
					ft.setDuration(duration);

					cell = row.getCell(俩人距离);
					int distance = (int) (cell.getNumericCellValue());
					ft.setDistance(distance);

//					cell = row.getCell(经验变化间隔);
					double expInterval = ReadFileTool.getDouble(row, 经验变化间隔, logger);
//					int expInterval = (int) (cell.getNumericCellValue());
					ft.setExpInterval(expInterval);

					cell = row.getCell(刷新时间间隔);
					int flushInterval = (int) (cell.getNumericCellValue());
					ft.setFlushInterval(flushInterval);

					cell = row.getCell(道具名);
					String propsName = (cell.getStringCellValue());
					ft.setPropsName(propsName);

					cell = row.getCell(刷新没有跟自己做过的个数);
					int undoNum = (int) (cell.getNumericCellValue());
					ft.setUndoNum(undoNum);

					cell = row.getCell(刷新跟自己做过的个数);
					int doNum = (int) (cell.getNumericCellValue());
					ft.setDoNum(doNum);

					cell = row.getCell(一天可以做几次);
					int perNum = (int) (cell.getNumericCellValue());
					ft.setPerNum(perNum);

					cell = row.getCell(类型);
					byte type = (byte) (cell.getNumericCellValue());
					ft.setType(type);

					cell = row.getCell(物品);
					String st = (cell.getStringCellValue());
					ft.setArticles(st.trim());

					cell = row.getCell(国家);
					int country = (int) (cell.getNumericCellValue());
					ft.setCountry(country);
					map.put(ft.getId(), ft);
					Map<Long, FateTemplate> fs = map2country.get(country);
					if(fs == null){
						fs = new HashMap<Long, FateTemplate>();
					}
					fs.put(ft.getId(), ft);
					map2country.put(country, fs);
				}
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public Map<Long, FateTemplate> getMap() {
		return map;
	}

	public void setMap(Map<Long, FateTemplate> map) {
		this.map = map;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public Map<Integer, Long> getExp1Map() {
		return exp1Map;
	}

	public void setExp1Map(Map<Integer, Long> exp1Map) {
		this.exp1Map = exp1Map;
	}

	public Map<Integer, Long> getExp2Map() {
		return exp2Map;
	}

	public void setExp2Map(Map<Integer, Long> exp2Map) {
		this.exp2Map = exp2Map;
	}

	public static void main(String[] args) throws Exception {
		FateManager fm = new FateManager();
		fm.init();
		FateTemplate ft = null;
		for (long id : fm.map.keySet()) {
			// ft = fm.map.get(id);
			// System.out.println(id);
		}
		ft = fm.map.get((long) 590);

	}
}
