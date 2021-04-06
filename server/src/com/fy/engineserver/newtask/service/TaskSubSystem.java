package com.fy.engineserver.newtask.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementConf;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.bourn.BournManager;
import com.fy.engineserver.core.client.AavilableTaskInfo;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.HorseProps;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.homestead.cave.Cave;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.manager.JiaZuLivenessType;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.JiazuRenwuModel;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_CAN_ACCEPT_TASK_RES;
import com.fy.engineserver.message.TASK_QUERY_ENTITIES_RES;
import com.fy.engineserver.message.TASK_QUERY_TASK_REQ;
import com.fy.engineserver.message.TASK_QUERY_TASK_RES;
import com.fy.engineserver.message.TASK_SEND_ACTION_REQ;
import com.fy.engineserver.message.TASK_SEND_ACTION_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.newtask.actions.TaskActionOfTaskDeliver;
import com.fy.engineserver.newtask.prizes.TaskPrize;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfArticle;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfCareer;
import com.fy.engineserver.newtask.prizes.TaskPrizeOfRandomArticle;
import com.fy.engineserver.newtask.targets.TaskTarget;
import com.fy.engineserver.newtask.targets.TaskTargetOfGetArticleAndDelete;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.TimeTool.TimeDistance;
import com.fy.engineserver.validate.DefaultValidateManager;
import com.fy.engineserver.validate.OtherValidateManager;
import com.fy.engineserver.validate.ValidateAsk;
import com.fy.engineserver.validate.ValidateManager;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class TaskSubSystem extends SubSystemAdapter implements TaskConfig, AchievementConf {

	private static TaskSubSystem instance;

	// public static Logger logger = Logger.getLogger(TaskSubSystem.class);
	public static Logger logger = LoggerFactory.getLogger(TaskSubSystem.class);

	public static ValidateManager validateManager = DefaultValidateManager.getInstance();

	public static TaskSubSystem getInstance() {
		return instance;
	}

	@Override
	public String getName() {
		return "TaskSubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "TASK_QUERY_TASK_REQ", "TASK_QUERY_ENTITIES_REQ", "TASK_SEND_ENTITY_REQ", "TASK_SEND_ACTION_REQ", "OPEN_ACCEPT_TASK_WINDOW_REQ", "QUERY_CAN_ACCEPT_TASK_REQ" };
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if (useMethodCache) {
			return super.handleReqeustMessage(conn, message, player);
		}
		try {
			Class<?> clazz = this.getClass();
			Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
			ResponseMessage res = null;
			res = (ResponseMessage) m1.invoke(this, conn, message, player);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	/**
	 * 玩家推荐任务列表
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_CAN_ACCEPT_TASK_REQ(Connection conn, RequestMessage message, Player player) {

		List<AavilableTaskInfo> infos = new ArrayList<AavilableTaskInfo>();
		List<Long> nextList = player.getNextCanAcceptTasks();
		List<Long> remove = new ArrayList<Long>();
		logger.warn("[handle_QUERY_CAN_ACCEPT_TASK_REQ] [nextList:"+nextList.size()+"] ["+player.getLogString()+"]");
		for (int i = 0; i < nextList.size(); i++) {
			Task task = TaskManager.getInstance().getTask(nextList.get(i));
			if (task != null) {
				CompoundReturn cr = player.takeOneTask(task, false, null);
				if (task.getType() == TASK_TYPE_ONCE && cr.getIntValue() == 12) {
					remove.add(task.getId());
					continue;
				}
				if(task.getCountryLimit() > 0 && task.getCountryLimit() != player.getCountry()){
					remove.add(task.getId());
					continue;
				}
				infos.add(new AavilableTaskInfo(task));
			}
		}
		if (remove.size() > 0) {
			for (int i = 0; i < remove.size(); i++) {
				nextList.remove(remove.get(i));
			}
			player.setDirty(true, "nextCanAcceptTasks");
			if (logger.isWarnEnabled()) {
				logger.warn(player.getLogString() + "[移除错误的任务:]", new Object[] { Arrays.toString(remove.toArray(new Long[0])) });
			}
		}
		logger.warn("[handle_QUERY_CAN_ACCEPT_TASK_REQ] [nextList:"+nextList.size()+"] [remove:"+remove.size()+"] [infos:"+infos.size()+"] ["+player.getLogString()+"]");
		QUERY_CAN_ACCEPT_TASK_RES res = new QUERY_CAN_ACCEPT_TASK_RES(message.getSequnceNum(), infos.toArray(new AavilableTaskInfo[0]));
		return res;
	}

	/**
	 * 玩家正在做的任务
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TASK_QUERY_ENTITIES_REQ(Connection conn, RequestMessage message, Player player) {

		TaskEntity[] entities = player.getTaskEntitysForDisplay();

		int[] taskLevels = new int[entities.length];
		for (int i = 0; i < entities.length; i++) {
			taskLevels[i] = TaskManager.getInstance().getTask(entities[i].getTaskId()).getGrade();
		}
		TASK_QUERY_ENTITIES_RES res = new TASK_QUERY_ENTITIES_RES(message.getSequnceNum(), entities, taskLevels);
		return res;
	}

	/**
	 * 查询某个任务的详细数据
	 * @param player
	 * @param req
	 */
	TaskTarget[] targets = null;
	public ResponseMessage handle_TASK_QUERY_TASK_REQ(Connection conn, RequestMessage message, Player player) {
		TASK_QUERY_TASK_RES res = null;
		try {
			long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();

			TASK_QUERY_TASK_REQ req = (TASK_QUERY_TASK_REQ) message;
			long taskId = req.getTaskId();
			Task task = TaskManager.getInstance().getTask(taskId);
			Task sendTask = null;

			if (task == null) {
				HINT_REQ err = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, Translate.text_task_016);
				player.addMessageToRightBag(err);
				logger.error("玩家[{}] [查询的任务不存在] [taskId:{}]", new Object[] { player.getName(), taskId });
				return null;
			}
			if (task.getCollections() != null && Translate.仙府.equals(task.getCollections())) {
				if (logger.isInfoEnabled()) {
					logger.info(player.getLogString() + "[任务是仙府任务] [{}]", new Object[] { task.getName() });
				}
				sendTask = (Task) task.clone();
				Cave cave = FaeryManager.getInstance().getCave(player);
				if (cave != null) {
					cave.modifyTask(sendTask);
					if (logger.isInfoEnabled()) {
						logger.info(player.getLogString() + "[任务是仙府任务] [{}] [StartNPC:{}] [startX:{}] [startY:{}] [endNPC:{}] [endX:{}] [endY:{}]", new Object[] { task.getName(), sendTask.getStartNpc(), sendTask.getStartX(), sendTask.getStartY(), sendTask.getEndNpc(), sendTask.getEndX(), sendTask.getEndY() });
					}
				}
			} else {
				sendTask = task;
			}
			targets = task.getTargets();
			for(TaskTarget t : targets){
				if(t != null && t.getTargetName() != null && t.getTargetName().length == 1){
					if(t.getTargetName()[0].contains("宝石")){
						Article a = ArticleManager.getInstance().getArticle(t.getTargetName()[0]);
						if(a instanceof InlayArticle){
							t.setTargetName(new String[] { ((InlayArticle)a).getShowName() });
//							System.out.println("[initTargetName22] [targetName:"+(t.getTargetName()!=null?Arrays.toString(t.getTargetName()):"")+"]");
						}
					}
				}
			}
			res = new TASK_QUERY_TASK_RES(req.getSequnceNum(), sendTask, targets, task.getPrizes());
		} catch (Exception e) {
			if (logger.isInfoEnabled()) logger.info("玩家查询任务异常", e);
		}
		return res;
	}

	/**
	 * 任务测试
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	// 玩家任务操作 客户端动作类型，0标识请求任务实体信息，1标识新接了一个任务，2标识交付了一个任务，3标识放弃了一个任务
	public ResponseMessage handle_TASK_SEND_ACTION_REQ(Connection conn, RequestMessage message, Player player) {
		try {
			TASK_SEND_ACTION_REQ req = (TASK_SEND_ACTION_REQ) message;
			byte actionType = req.getActionType();
			long taskId = req.getTaskId();
			// 解析出来的数据selects =req.getSelectedIndex();
			String[] selects = req.getSelectedIndex();
			Task task = TaskManager.getInstance().getTask(taskId);
			TaskEntity entity = null;
			if (task == null) {
				logger.error("[客户端请求任务] [错误] [任务不存在] [玩家:{}] [action:{}] [taskId ={}]", new Object[] { player.getName(), actionType, taskId });
				TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_016);
				return res;
			}

			TaskEventTransactCenter center = TaskEventTransactCenter.getInstance();
			long now = SystemTime.currentTimeMillis();
			switch (actionType) {
			case TASK_ACTION_ACCEPT:// 接任务
				if (task.getId() == 100000) {
					logger.warn(player.getLogString() + " [通过协议接取封印任务] [直接返回]");
					return null;
				}
				String collectionName = task.getCollections();
				if (collectionName != null && Translate.境界.equals(collectionName)) {
					logger.warn(player.getLogString() + "[要接取任务" + task.getName() + "] [是境界任务] [当前人身上的境界任务:" + player.getCurrBournTaskName() + "]");
					if (player.getCurrBournTaskName() != null && !"".equals(player.getCurrBournTaskName()) && !task.getName().equals(player.getCurrBournTaskName())) {
						player.sendError(Translate.text_task_044);
						return null;
					}
					if (TimeTool.instance.isSame(player.getLastDeliverTimeOfBournTask(), now, TimeDistance.DAY)) {
						if (logger.isInfoEnabled()) {
							logger.info(player.getLogString() + "[要接取境界任务:{}] [上次和现在是同一天]", new Object[] { task.getName() });
						}
						if (player.getDeliverBournTaskNum() >= BournManager.getInstance().getMaxBournTaskNum(player)) {
							player.sendError(Translate.text_bourn_020);
							return null;
						}
					}
					player.setLastDeliverTimeOfBournTask(now);
				} else {
					if (task.isServerAutoAccept()) {
						logger.warn(player.getLogString() + " [疑似刷任务] [要接取服务器自动接取的任务:" + task.toString() + "] [直接返回]");
						return null;
					}
				}
				CompoundReturn returns = player.takeOneTask(task, true, null);
				if (returns.getIntValue() == 0) {// 判断通过
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + " [接取任务] [{}]", new Object[] { task.getName() });
					}

					// 验证任务
					try {
						if (task.isTiliTask() || task.needAnswer()) {
							int validateState = OtherValidateManager.getInstance().getValidateState(player, OtherValidateManager.ASK_TYPE_TASK);
							if (validateState == ValidateManager.VALIDATE_STATE_需要答题) {
								ValidateAsk ask = OtherValidateManager.getInstance().sendValidateAsk(player, OtherValidateManager.ASK_TYPE_TASK);
								ask.setAskFormParameters(new Object[] { task });
								return null;
							}
						}
					} catch (Exception e) {
						logger.error(player.getLogString() + " [接取任务] [异常]", e);
					}

					task.doOnServerBeforeAdd(player);
					player.addTask(task);// 给玩家加入一个任务
					task.doOnServerAfterAdd(player);

					entity = player.getTaskEntity(taskId);
					entity.dealOnGet();
					entity.sendEntityChange((byte) 1);
					entity.setStatus(TASK_STATUS_GET);
					TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 0, "");
					center.dealWithTask(Taskoperation.accept, task, player, player.getCurrentGame());
					if (task.isHasCollectionTarget()) {
						player.checkCollectionNPC(player.getCurrentGame());
					}
					return res;
				} else {
					String info = getInfo(returns.getIntValue());
					if (task.getType() == TASK_TYPE_ONCE && returns.getIntValue() == 12) {// 已经做过的单次任务
						if (player.getNextCanAcceptTasks().contains(task.getId())) {
							player.getNextCanAcceptTasks().remove(task.getId());
							player.setDirty(true, "nextCanAcceptTasks");
							List<AavilableTaskInfo> addList = new ArrayList<AavilableTaskInfo>();
							addList.add(new AavilableTaskInfo(task));
							player.noticeClientCanAcceptModify((byte) 1, addList,"接取任务");
							if (logger.isWarnEnabled()) {
								logger.warn(player.getLogString() + "[要接取单次任务:{}] [但是已经完成了] [在可接列表中移除]", new Object[] { task.getName() });
							}
						}
					}
					if (info != null) {
						logger.warn("[截取任务不符] [taskId:"+taskId+"] [taskName:"+task.getName()+"] [country:"+task.getCountryLimit()+"] [info:"+info+"] [pName:"+player.getName()+"] [pCountry:"+player.getCountry()+"]");
						TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, info);
						return res;
					}
				}
				break;
			case TASK_ACTION_DELIVER:// 交付任务
				try {
					if (logger.isDebugEnabled()) {
						logger.debug(player.getLogString() + "[交付任务:{" + taskId + "}]");
					}
					entity = player.getTaskEntity(taskId);
					if (entity == null) {
						if (logger.isWarnEnabled()) {
							logger.warn(player.getLogString() + "[交付任务] [任务不存在或者已交付] [ID:{}]", new Object[] { taskId });
						}
						return null;
					}
					int status = player.getTaskStatus(entity.getTask());
					if (status == TaskConfig.TASK_STATUS_DEILVER) {
						if (logger.isWarnEnabled()) {
							logger.warn(player.getLogString() + "[交付任务] [不是完成状态] [任务:{}] [ID:{}] [状态:{}]", new Object[] { entity.getTask().getName(), entity.getTask().getId(), status });
						}
						return null;
					}
					if (entity != null && entity.taskComplete()) { // 任务达成
						if (task.getDeliverTimeLimit() != null && task.getDeliverTimeLimit().getType() == DEILVER_LIMIT_TYPE_DELIVER) {// 限时且需要在时间内交付
							if ((entity.getLastGetTime() + task.getDeliverTimeLimit().getTime()) < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {// 超时没交任务
								TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_017);
								return res;
							}
						}
						//时间
//						if(!task.check4date()) {
//							if (player.getFunctionNPC(task.getStartMap(), task.getStartNpc()) != null) {
//								player.getFunctionNPC(task.getStartMap(), task.getStartNpc()).addTask2Wait(ModifyType.DAY_CHANGE, task);
//							}
//							TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_017);
//							return res;
//						}
						if (logger.isDebugEnabled()) {
							logger.debug(player.getLogString() + "[交付任务:{" + taskId + "}] [可交付] [开始计算背包] [" + selects + "]");
						}

						if (task.isTiliTask()) {
							EnterLimitManager.setValues(player, PlayerRecordType.完成体力任务次数);
						}
						// 计算包裹是否能放得
						// 奖励索引,选择的奖励索引A,选择的奖励索引B,选择的奖励索引C......
						List<Article> list = new ArrayList<Article>();
						int[][] selectedIndexs = new int[task.getPrizes().length][];

						boolean hasHorseProp = false;

						if (selects.length > 0) { // 确实有选择的数据
							for (int i = 0, j = selects.length; i < j; i++) {
								String[] values = selects[i].split(",");// index,1,3,6某个(第index个)奖励选择的物品
								if (values != null && values.length > 1) {
									int index = Integer.valueOf(values[0]);
									if (!task.getPrizes()[index].isSelectPrize()) {// 不是可选择的项
										TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_018);
										return res;
									}
									selectedIndexs[index] = new int[values.length - 1];
									if (task.getPrizes()[index].getTotalNum() != values.length - 1) {// 任务配置可奖励个数和实际选择数量不匹配
										TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_018);
										return res;
									}
									for (int k = 1; k < values.length; k++) {// 从第一位开始，第0位是奖励索引
										selectedIndexs[index][k - 1] = Integer.valueOf(values[k]);
									}
								}
							}
							// 可以放到上面，写这里比较直观
							for (int i = 0; i < selectedIndexs.length; i++) {
								int[] sel = selectedIndexs[i];// 第i个奖励
								if (sel != null && sel.length > 0) {
									for (int j = 0; j < sel.length; j++) {// 找到任务奖励中和选中的对应的数据
										if (task.getPrizes()[i] instanceof TaskPrizeOfArticle) {
											Article article = ArticleManager.getInstance().getArticle(task.getPrizes()[i].getPrizeName()[sel[j]]);
											if (article != null) {
												list.add(article);
												if (article instanceof HorseProps) {
													hasHorseProp = true;
												}
											}
										}
									}
								}
							}

						}
						if (logger.isDebugEnabled()) {
							logger.debug(player.getLogString() + "[交付任务:{" + taskId + "}] [可交付] [开始计算背包] [" + selects + "] [奖励个数:" + list.size() + "]");
						}
						// 处理非选择类型的奖励
						for (int i = 0; i < task.getPrizes().length; i++) {
							if (task.getPrizes()[i] instanceof TaskPrizeOfArticle) {
								if (task.getPrizes()[i].getPrizeName().length == 1) { // 不是可选奖励
									Article article = ArticleManager.getInstance().getArticle(task.getPrizes()[i].getPrizeName()[0]);
									if (article != null) {
										list.add(article);
										if (article instanceof HorseProps) {
											hasHorseProp = true;
										}
									}
								} else { // 可选的
									if (selectedIndexs[i] == null) {// 没选择
										TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_025);
										return res;
									}
								}
							}
							// 职业奖励,只给装备
							if (task.getPrizes()[i] instanceof TaskPrizeOfCareer) {
								Article article = ArticleManager.getInstance().getArticle(task.getPrizes()[i].getPrizeName()[player.getCareer() - 1]);
								if (article != null) {
									list.add(article);
									if (article instanceof HorseProps) {
										hasHorseProp = true;
									}
								}
							}
						}
						// 如果有要扣除道具的目标,要查看所需的物品时否存在

						List<TaskTarget> delete = task.getTargetByType(TargetType.GET_ARTICLE_AND_DELETE);
						if (delete != null && delete.size() > 0) {
							for (TaskTarget deleteTarget : delete) {
								CompoundReturn cr = ((TaskTargetOfGetArticleAndDelete) deleteTarget).articleInPackage(player);
								if (!cr.getBooleanValue()) {
									TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, cr.getStringValue());
									return res;
								}
							}
							for (TaskTarget deleteTarget : delete) {
								((TaskTargetOfGetArticleAndDelete) deleteTarget).deleteTarget(player);
							}

						}

						// 移除给予道具

						if (task.getGivenArticle() != null) {
							for (String name : task.getGivenArticle().getNames()) {
								ArticleEntity ae = player.getArticleEntity(name);
								// TODO 此处可能有问题,这么做的前提①是物品唯一②任务不存在同名但是不同颜色的物品
								player.removeFromKnapsacks(ae, "任务", true);// .removeArticleEntityFromKnapsackByArticleId(ae.getId());
								if (logger.isWarnEnabled()) {
									logger.warn(player.getLogString() + "[完成任务:{}] [扣除道具:{}] [{}]", new Object[] { task.getName(), name, ae });
								}
							}
						}
						// 能不能放进去
						int[] checkK = getNeedPagLeft(list, task.isHasRandomPrize());
						if (logger.isDebugEnabled()) {
							logger.debug(player.getLogString() + "[完成任务:{}] [是否有随机奖励:{}] [列表长度:{}] [物品占空间列表:{}] [是否有坐骑:{}]", new Object[] { task.getName(), task.isHasRandomPrize(), list.size(), Arrays.toString(checkK), hasHorseProp });
						}
						if (hasHorseProp) {
							if (player.getKnapsack_common().getEmptyNum() < list.size()) {
								if (logger.isInfoEnabled()) {
									logger.info(player.getLogString() + "[完成任务:{}] [是否有随机奖励:{}] [列表长度:{}] [物品占空间列表:{}] [有坐骑] [背包不足不能放进去]", new Object[] { task.getName(), task.isHasRandomPrize(), list.size(), Arrays.toString(checkK) });
								}
								TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_023);
								return res;
							}
						}
						if (player.canAddAll(checkK)) {
							// 包裹足够大 随机奖励问题
							if (logger.isDebugEnabled()) {
								logger.debug(player.getLogString() + "[完成任务:{}] [是否有随机奖励:{}] [列表长度:{}] [物品占空间列表:{}] [背包足够可以放进去]", new Object[] { task.getName(), task.isHasRandomPrize(), list.size(), Arrays.toString(checkK) });
							}
							for (int i = 0; i < task.getPrizes().length; i++) {
								TaskPrize prize = task.getPrizes()[i];
								if (prize instanceof TaskPrizeOfRandomArticle) {
									// 随机奖励 传入是否达成额外的目标
									CompoundReturn cr = ((TaskPrizeOfRandomArticle) prize).doPrize(player, entity.isExcess());
									if (cr.getBooleanValue()) {
										List<ArticleEntity> aelist = (List<ArticleEntity>) cr.getObjValue();
										for (ArticleEntity ae : aelist) {
											player.putToKnapsacks(ae, "任务");
											ArticleStatManager.addToArticleStat(null, player, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "任务获得[" + task.getName_stat() + "]", "");
										}
									}
								} else {
									prize.doPrize(player, selectedIndexs[i], task);
									if (logger.isDebugEnabled()) {
										logger.debug(player.getLogString() + "[完成任务:{}] [是否有随机奖励:{}] [列表长度:{}] [物品占空间列表:{}] [背包足够可以放进去] [发送奖励完毕:{}]", new Object[] { task.getName(), task.isHasRandomPrize(), list.size(), Arrays.toString(checkK), prize.getClass() });
									}
								}
							}
						} else {
							if (logger.isInfoEnabled()) {
								logger.info(player.getLogString() + "[完成任务:{}] [是否有随机奖励:{}] [列表长度:{}] [物品占空间列表:{}] [背包不足不能放进去]", new Object[] { task.getName_stat(), task.isHasRandomPrize(), list.size(), Arrays.toString(checkK) });
							}
							TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_023);
							return res;
						}

						TaskActionOfTaskDeliver actionOfTaskDeliver = TaskActionOfTaskDeliver.createAction(task.getName());
						player.dealWithTaskAction(actionOfTaskDeliver);
						entity.setStatus(TASK_STATUS_DEILVER);

						int taskInstance = task.getDailyTaskCycle() <=0 ? 1 : task.getDailyTaskCycle();
						if (!TimeTool.instance.isSame(entity.getLastDeliverTime(), now, TimeDistance.DAY, taskInstance)) {
							entity.setCycleDeliverTimes(0);
						}
						entity.setLastDeliverTime(now);
						entity.setCycleDeliverTimes(entity.getCycleDeliverTimes() + 1);
						entity.setTotalDeliverTimes(entity.getTotalDeliverTimes() + 1);
						player.transferTask(task, TASK_ACTION_DELIVER);

						entity.sendEntityChange((byte) 2);

						player.checkFunctionNPCModify(ModifyType.TASK_DELIVER);

						if (TaskManager.getInstance().getTaskBigCollections().get(task.getBigCollection()) != null) {
							if (!(Translate.刺探).equals(task.getCollections()) && !(Translate.偷砖).equals(task.getCollections())) {
								player.checkFunctionNPCModify(ModifyType.GET_COLLECTION);
							}
						}
						if (task.isHasCollectionTarget()) {
							player.checkCollectionNPC(player.getCurrentGame());
						}
						if (task.isBournTask() && task.isDailyTask()) {
							player.setDeliverBournTaskNum(1 + player.getDeliverBournTaskNum());
						}
						TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 0, "");

						if (logger.isWarnEnabled()) {
							logger.warn(player.getLogString() + " [交付任务] [OK] [{}]", new Object[] { task.getName() });
						}
						try{
							AllActivityManager.instance.notifySthHappened(AllActivityManager.taskDeliverAct, player, task.getName());
						} catch(Exception e) {
							ActivitySubSystem.logger.error("[完成N次任务活动] [异常2] [" + player.getLogString() + "][" + task.getName_stat() + "]", e);
						}
						try{
							ActivityManagers.getInstance().noticeTaskRewardActivity(player, task.getName());
						}catch(Exception e ){
							ActivitySubSystem.logger.warn("[完成任务额外奖励活动] [异常] [taskname:"+task.getName()+"] ["+player.getLoving()+"] ["+e+"]");
						}
					
						center.dealWithTask(Taskoperation.deliver, task, player, player.getCurrentGame());
						
						{										//兽魁进阶任务
							if (player.getCareer() == 5 && CareerManager.兽魁进阶任务.equals(task.getName_stat())) {
								player.setPlayerRank(1);		//通过任务兽魁升级为二阶
								ActivitySubSystem.logger.warn("[完成兽魁进阶任务] [taskName:" + task.getName_stat() + "] [taskId:" + task.getId() + "] [" + player.getLogString() + "]");
							}
						}
						
						{										//家族任务
							try {
								if (player.getJiazuId() > 0) {
									Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
									if (jiazu != null) {
										JiazuRenwuModel jrm = JiazuManager2.instance.taskMap.get(task.getId());
										if (jrm != null) {
											JiazuManager2.instance.addLiveness(player, JiaZuLivenessType.家族任务);
											StringBuffer sb = new StringBuffer();
											if (jrm.getAddXiulian() > 0) {
												JiazuMember2 jm2 = JiazuEntityManager2.instance.getEntity(player.getId());
												jm2.setPracticeValue(jm2.getPracticeValue() + jrm.getAddXiulian());
												JiazuManager2.logger.warn("[新家族] [完成家族任务] [增加修炼值成功] [任务id:" + task.getId() + "] [增加修炼值:" + JiazuEntityManager2.instance.getParatical(player) + "] [增加后修炼值:" + jrm.getAddXiulian() + "] [" + player.getLogString() + "]");
												sb.append(String.format(Translate.获得修炼值, jrm.getAddXiulian()));
											}
											if (jrm.getAddJiazuZiyuan() > 0) {
												String result = jiazu.addJiazuMoney(jrm.getAddJiazuZiyuan());
												if (result == null) {
													sb.append(",").append(String.format(Translate.获得家族资金, BillingCenter.得到带单位的银两(jrm.getAddJiazuZiyuan())));
													JiazuManager2.logger.warn("[新家族] [完成家族任务] [增加家族自己] [任务id:" + task.getId() + "] [增加资金值:" + jrm.getAddJiazuZiyuan() + "] [" + player.getLogString() + "]");
												} else {
													player.sendError(result);
												}
											}
											if (!sb.toString().isEmpty()) {
												player.sendError(sb.toString());
											}
										}
//										JiazuManager2.instance.refreshTaskList(player, true);
									}
								}
							} catch (Exception e) {
								JiazuManager2.logger.error("[新家族] [完成任务检测是否需要增加修炼值] [异常] [" + player.getLogString() + "]", e);
							}
						}
						
						{
							AchievementManager.getInstance().record(player, RecordAction.完成任务个数);
							PlayerAimManager.instance.notifyCompleteTask(task, player);
							if (task.getType() == TASK_TYPE_DAILY) {
								AchievementManager.getInstance().record(player, RecordAction.完成日常任务次数);
							}
							if (Translate.国运.equals(task.getCollections())) {
								AchievementManager.getInstance().record(player, RecordAction.运镖完成次数);
								ActivenessManager.getInstance().record(player, ActivenessType.个人押镖);
							}
							try {
								RecordAction rAction = null;
								for(int kk=0; kk<PlayerAimManager.任务集合名.length; kk++) {
									if(PlayerAimManager.任务集合名[kk].equals(task.getCollections())) {
										rAction = PlayerAimManager.任务集合action[kk];
										break;
									}
								}
//								if (rAction == null) {
//									for(int kk=0; kk<PlayerAimManager.任务名.length; kk++){
//										if (task.getName().equals(PlayerAimManager.任务名[kk])) {
//											rAction = PlayerAimManager.任务集合action[1];
//											break;
//										}
//									}
//								}
								if(rAction != null) {
									EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), rAction, 1L});
									EventRouter.getInst().addEvent(evt);
								}
							} catch (Exception eex) {
								PlayerAimManager.logger.error("[目标系统] [统计完成任务异常] [" + player.getLogString() + "]", eex);
							}
						}
						return res;
					} else {
						// TODO 提示玩家没接
						TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_022);
						return res;
					}
				} catch (Exception e1) {
					logger.error("交付任务:", e1);
					TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, "error");
					return res;
				}
			case TASK_ACTION_GIVEUP:// 放弃
				if (logger.isInfoEnabled()) {
					logger.info(player.getLogString() + "[放弃任务] [taskName:{}]", new Object[] { task.getName() });
				}
				entity = player.getTaskEntity(taskId);
				if (entity == null) {
					TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_022);
					return res;
				}
				if (entity.getTask().getShowType() == TASK_SHOW_TYPE_MAIN) {
					TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_024);
					return res;
				}
//				if(entity.getTask().getLimitType() > 0) {
//					TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 1, Translate.text_task_046);
//					return res;
//				}
				try {
					if (task.getType() == TASK_TYPE_DAILY) { // 日常任务算完成一次
						int taskInstance = task.getDailyTaskCycle() <= 0 ? 1 : task.getDailyTaskCycle();
						if (!TimeTool.instance.isSame(entity.getLastDeliverTime(), SystemTime.currentTimeMillis(), TimeDistance.DAY, taskInstance)) {
							entity.setCycleDeliverTimes(0);
						}
						entity.setCycleDeliverTimes(entity.getCycleDeliverTimes() + 1);
						entity.setStatus(TASK_STATUS_DEILVER);
						entity.setLastDeliverTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
					}
					player.transferTask(task, TASK_ACTION_GIVEUP);
					player.checkFunctionNPCModify(ModifyType.TASK_GIVEUP);
					if (TaskManager.getInstance().getTaskCollectionsByName(entity.getTask().getCollections()) != null) {
						player.checkFunctionNPCModify(ModifyType.GET_COLLECTION);
					}
					entity.sendEntityChange((byte) 3);
					TASK_SEND_ACTION_RES res = new TASK_SEND_ACTION_RES(req.getSequnceNum(), taskId, actionType, (byte) 0, "");
					center.dealWithTask(Taskoperation.drop, task, player, player.getCurrentGame());
					if (task.isHasCollectionTarget()) {
						player.checkCollectionNPC(player.getCurrentGame());
					}
					if (task.isBournTask() && task.isDailyTask()) {// 境界任务.计算一次次数
						player.setDeliverBournTaskNum(1 + player.getDeliverBournTaskNum());
					}
					// 删除已获得的额外物品
					if (entity.isExcess()) {
						String excessTargetName = task.getExcessTarget();
						ArticleEntity ae = player.getArticleEntity(excessTargetName);
						if (ae != null) {
							player.removeFromKnapsacks(ae, "任务", true);
							if (logger.isDebugEnabled()) {
								logger.debug(player.getLogString() + "[放弃任务] [删除给予道具:{}] [颜色:{}]", new Object[] { ae.getArticleName(), ae.getColorType() });
							}
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug(player.getLogString() + "[放弃任务] [删除给予道具:{}] [不存在了]", new Object[] { excessTargetName });
							}
						}
					}
					if (logger.isWarnEnabled()) {
						logger.warn(player.getLogString() + " [放弃任务] [{}]", new Object[] { task.getName() });
					}
					return res;
				} catch (Exception e) {
					logger.error("任务异常", e);
				}
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("任务动作", e);
		}
		return null;
	}

	public String getInfo(int intValue) {
		String info = null;
		switch (intValue) {
		case 1:
			info = Translate.text_task_001;
			break;
		case 2:
			info = Translate.text_task_002;
			break;
		case 3:
			info = Translate.text_task_003;
			break;
		case 4:
			info = Translate.text_task_004;
			break;
		case 5:
			info = Translate.text_task_005;
			break;
		case 6:
			info = Translate.text_task_006;
			break;
		case 7:
			info = Translate.text_task_007;
			break;
		case 8:
			info = Translate.text_task_008;
			break;
		case 9:
			info = Translate.text_task_009;
			break;
		case 10:
			info = Translate.text_task_010;
			break;
		case 11:
			info = Translate.text_task_011;
			break;
		case 12:
			info = Translate.text_task_012;
			break;
		case 13:
			info = Translate.text_task_013;
			break;
		case 14:
			info = Translate.text_task_014;
			break;
		case 15:
			info = Translate.text_task_015;
			break;
		case 16:
			info = Translate.text_task_021;
			break;
		case 17:
			info = Translate.text_task_026;
			break;
		case 18:
			info = Translate.text_task_027;
			break;
		case 19:
			info = Translate.text_task_028;
			break;
		case 20:
			info = Translate.text_task_029;
			break;
		case 21:
			info = Translate.text_task_036;
			break;
		case 22:
			info = Translate.text_task_037;
			break;
		case 23:
			info = Translate.text_task_042;
			break;
		case 24:
			info = Translate.text_task_042;
			break;
		default:
			break;
		}
		return info;
	}

	/**
	 * 检索任务奖励占包[有随机则要每个包要多一个位置]
	 * @param articles
	 * @return
	 */
	private int[] getNeedPagLeft(List<Article> articles, boolean hasRandom) {
		// <背包类型,要放入的数量>
		// TODO 根据任务奖励类型判断
		int[] check = new int[ArticleManager.knapsackNames.length];
		if (hasRandom) {
			for (int i = 0; i < check.length; i++) {
				check[i] = 1;
			}
		}
		for (int i = 0, j = articles.size(); i < j; i++) {
			check[articles.get(i).getKnapsackType()]++;
		}
		return check;
	}

	public void init() {
		
		instance = this;
		ServiceStartRecord.startLog(this);
	}

	public void destroy() {

	}
}
