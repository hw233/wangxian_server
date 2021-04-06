package com.fy.engineserver.activity;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.STRING_2;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.对方不在线;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.chongzhiActivity.ChargePackageActivity;
import com.fy.engineserver.activity.chongzhiActivity.ChargeRecord;
import com.fy.engineserver.activity.chongzhiActivity.MonthCardActivity;
import com.fy.engineserver.activity.chongzhiActivity.MonthCardInfo;
import com.fy.engineserver.activity.chongzhiActivity.MonthCardRecord;
import com.fy.engineserver.activity.farming.FarmingFruit;
import com.fy.engineserver.activity.farming.FarmingManager;
import com.fy.engineserver.activity.fateActivity.FateActivityType;
import com.fy.engineserver.activity.fateActivity.FateManager;
import com.fy.engineserver.activity.fateActivity.base.FateActivity;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.luckfruit.ForLuckFruitManager;
import com.fy.engineserver.activity.notice.ActivityNoticeManager;
import com.fy.engineserver.activity.notice.ActivityNoticeManager.Activity;
import com.fy.engineserver.activity.quiz.Quiz;
import com.fy.engineserver.activity.quiz.QuizManager;
import com.fy.engineserver.activity.treasure.TreasureActivityManager;
import com.fy.engineserver.articleExchange.ArticleExchangeManager;
import com.fy.engineserver.articleExchange.ExchangeDeal;
import com.fy.engineserver.articleExchange.ExchangeInterface;
import com.fy.engineserver.billboard.special.SpecialEquipmentManager;
import com.fy.engineserver.carbon.devilSquare.DevilSquareManager;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.TransportData;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_1EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.Special_2EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.SkillInfoHelper;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.economic.SavingReasonType;
import com.fy.engineserver.economic.charge.ChargeManager;
import com.fy.engineserver.economic.charge.ChargeMode;
import com.fy.engineserver.economic.charge.ChargeMoneyConfigure;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.JiazuFunction;
import com.fy.engineserver.jiazu.JiazuMember;
import com.fy.engineserver.jiazu.JiazuTitle;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Cancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_Charge_Sure;
import com.fy.engineserver.menu.activity.Option_MonthCard_Charge_Sure;
import com.fy.engineserver.menu.activity.explore.Option_Agree_Exchange;
import com.fy.engineserver.menu.activity.explore.Option_DisAgree_Exchange;
import com.fy.engineserver.menu.quizActivity.Option_Confirm_Close;
import com.fy.engineserver.menu.quizActivity.Option_UnConfirm_Close;
import com.fy.engineserver.message.ACTIVITY_FARMING_PLATE_RESPONSE_REQ;
import com.fy.engineserver.message.ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ;
import com.fy.engineserver.message.ACTIVITY_IS_SHOW_REQ;
import com.fy.engineserver.message.ACTIVITY_QUERY_REQ;
import com.fy.engineserver.message.ACTIVITY_QUERY_RES;
import com.fy.engineserver.message.AGREE_ACTIVITY_REQ;
import com.fy.engineserver.message.ANSWER_QUIZ_FINISH_RES;
import com.fy.engineserver.message.ANSWER_QUIZ_REQ;
import com.fy.engineserver.message.CALL_FRIEND_DRINKING_REQ;
import com.fy.engineserver.message.CALL_FRIEND_DRINKING_RES;
import com.fy.engineserver.message.CHARGE_AGRS_RES;
import com.fy.engineserver.message.CLOSE_DEAL_REQ;
import com.fy.engineserver.message.CLOSE_DEAL_RES;
import com.fy.engineserver.message.DISAGREE_ACTIVITY_REQ;
import com.fy.engineserver.message.DRINKING_FRIEND_DO_REQ;
import com.fy.engineserver.message.FUNCTION_SHOW_REQ;
import com.fy.engineserver.message.FUNCTION_SHOW_RES;
import com.fy.engineserver.message.GET_FATEACTIVITY_REQ;
import com.fy.engineserver.message.GET_FATEACTIVITY_RES;
import com.fy.engineserver.message.GIVEUP_ACTIVITY_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HANDOUT_FORLUCK_STORAGE_REQ;
import com.fy.engineserver.message.HANDOUT_FORLUCK_STORAGE_RES;
import com.fy.engineserver.message.HANDOUT_FORLUCK_TREE_REQ;
import com.fy.engineserver.message.HANDOUT_FORLUCK_TREE_RES;
import com.fy.engineserver.message.MONTH_CARD_ACTIVITY_BUY_REQ;
import com.fy.engineserver.message.MONTH_CARD_ACTIVITY_GET_REWARD_REQ;
import com.fy.engineserver.message.MONTH_CARD_ACTIVITY_GET_REWARD_RES;
import com.fy.engineserver.message.MONTH_CARD_ACTIVITY_REQ;
import com.fy.engineserver.message.MONTH_CARD_ACTIVITY_RES;
import com.fy.engineserver.message.QUERY_ONE_SPECIAL_EQUIPMENT_REQ;
import com.fy.engineserver.message.QUERY_ONE_SPECIAL_EQUIPMENT_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.REAL_CHOOSE_ACTIVITY_REQ;
import com.fy.engineserver.message.REAL_CHOOSE_ACTIVITY_RES;
import com.fy.engineserver.message.SEEM_HINT_RES;
import com.fy.engineserver.message.SIGNUP_ACTIVITY_REQ;
import com.fy.engineserver.message.SIGNUP_ACTIVITY_RES;
import com.fy.engineserver.message.SPECIAL_DEAL_ADD_ARTICLE_REQ;
import com.fy.engineserver.message.SPECIAL_DEAL_ADD_ARTICLE_RES;
import com.fy.engineserver.message.SPECIAL_DEAL_DEL_ARTICLE_REQ;
import com.fy.engineserver.message.SPECIAL_DEAL_DEL_ARTICLE_RES;
import com.fy.engineserver.message.SPECIAL_DEAL_FINISH_REQ;
import com.fy.engineserver.message.SPECIAL_DEAL_FINISH_RES;
import com.fy.engineserver.message.SPECIAL_DEAL_REQ;
import com.fy.engineserver.message.TOTLE_ONLINE_REWARD_RES;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.notice.Notice;
import com.fy.engineserver.notice.NoticeManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.npc.ForLuckFruitNPC;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.tool.GlobalTool;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class ActivitySubSystem extends SubSystemAdapter {

	public static Logger logger = LoggerFactory.getLogger(ActivitySubSystem.class);

	@Override
	public String getName() {
		return "ActivitySubSystem";
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "ACTIVITY_FEED_SILKWORM_EXCHANGE_REQ", "HANDOUT_FORLUCK_TREE_REQ", "HANDOUT_FORLUCK_STORAGE_REQ", 
				"ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ", "ACTIVITY_FARMING_PLATE_RESPONSE_REQ", "GET_FATEACTIVITY_REQ", 
				"CHOOSE_ACTIVITY_REQ", "REAL_CHOOSE_ACTIVITY_REQ", "AGREE_ACTIVITY_REQ", "DISAGREE_ACTIVITY_REQ",
				"GIVEUP_ACTIVITY_REQ", "ANSWER_QUIZ_REQ", "ANSWER_QUIZ_CANCEL_REQ", "QUERY_ONE_SPECIAL_EQUIPMENT_REQ", 
				"SPECIAL_DEAL_REQ", "SPECIAL_DEAL_ADD_ARTICLE_REQ", "SPECIAL_DEAL_DEL_ARTICLE_REQ", "SPECIAL_DEAL_FINISH_REQ",
				"CLOSE_DEAL_REQ", "QUERY_EXCHANGE_REQ", "EXCHANGE_REQ", "SIGNUP_ACTIVITY_REQ","CALL_FRIEND_DRINKING_REQ",
				"DRINKING_FRIEND_DO_REQ" ,"MONTH_CARD_ACTIVITY_REQ","MONTH_CARD_ACTIVITY_GET_REWARD_REQ","MONTH_CARD_ACTIVITY_BUY_REQ",
				"CHARGE_GET_PACKAGE_SURE_REQ","SHOW_COST_BUTTON_REQ","COST_BILLBOARD_PAGE_REQ","COST_BILLBOARD_HELP_REQ",
				"TOTLE_ONLINE_REWARD_REQ","GET_TOTLE_ONLINE_REWARD_REQ","ACTIVITY_IS_SHOW_REQ","ACTIVITY_QUERY_REQ","TOTLE_COST_ACTIVITY_REQ","FUNCTION_SHOW_REQ","END_DEAD_GAME_REQ"};
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if (logger.isInfoEnabled()) logger.info("[收到玩家的活动请求]{}:{}", new Object[] { player.getName(), message.getTypeDescription() });

		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}
		Class<?> clazz = this.getClass();
		Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
		m1.setAccessible(true);
		Object obj = m1.invoke(this, conn, message, player);
		return obj == null ? null : (ResponseMessage) obj;

	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	/***********************************************************************************************************************/

	/**
	 * 废弃,什么也不做
	 */
	@Deprecated
	public ResponseMessage handle_ACTIVITY_FARMING_PLATE_RESPONSE_REQ(Connection conn, RequestMessage message, Player player) {
		ACTIVITY_FARMING_PLATE_RESPONSE_REQ req = (ACTIVITY_FARMING_PLATE_RESPONSE_REQ) message;
		int resultId = req.getResultId();
		int taskIndex = req.getTaskIndex();

		if (logger.isWarnEnabled()) {
			logger.warn(player.getJiazuLogString() + "[收到弹出盘子返回] [resultId:{}] [taskIndex:" + taskIndex + "] [记录:{}]", new Object[] { resultId, player.getFarmingPlateResult() });
		}
		if (resultId == -1 || resultId != player.getFarmingPlateResult()) {
			player.sendError(Translate.text_feed_silkworm_002);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[收到弹出盘子返回] [没抽到] [和人身上记录不符] [resultId:{}] [记录:{}]", new Object[] { resultId, player.getFarmingPlateResult() });
			}
			// 什么都不做
			return null;
		}
		player.setFarmingPlateResult(-1);
		FarmingFruit fruit = FarmingManager.getInstance().getFruits().get(resultId);
		if (fruit == null) {
			player.sendError(Translate.text_feed_silkworm_002);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[收到弹出盘子返回] [没抽到] [resultId:{}]", new Object[] { resultId });
			}
			return null;
		} else {
			String[] taskName = fruit.getTaskName();
			if (taskIndex < 0 || taskIndex >= taskName.length) {
				player.sendError(Translate.translateString(Translate.text_feed_silkworm_003, new String[][] { { Translate.STRING_1, String.valueOf(taskIndex) } }));
				logger.error(player.getJiazuLogString() + "[收到弹出盘子返回] [神农活动] [转盘子结果异常] [taskIndex:{}]", new Object[] { taskIndex });
				return null;
			}
			Task task = TaskManager.getInstance().getTask(taskName[taskIndex],player.getCountry());
			if (task != null) {
				// CompoundReturn res = player.addTaskByServer(task);
				if (logger.isWarnEnabled()) {
					logger.warn("[" + player.getJiazuLogString() + "] [收到弹出盘子返回] [神农活动] [转盘子结束,自动接受任务:{}] [taskIndex:{}]", new Object[] { taskIndex, task.getName() });
				}
				// player.sendNotice(Translate.translateString(Translate.text_farming_001, new String[][] { { Translate.ARTICLE_NAME_1, fruit.getName() } }));
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[" + player.getJiazuLogString() + "] [收到弹出盘子返回] [神农活动] [任务不存在] [index:" + taskIndex + "] [" + taskName[taskIndex] + "]");
				}
			}
		}
		return null;
	}
	
	public static final String helpPlayerName = "小助手";
	public Player getHelpPlayer(){
		Player p = null;
		try {
			p = PlayerManager.getInstance().getPlayer(helpPlayerName);
		} catch (Exception e) {
			try {
				p = PlayerManager.getInstance().createPlayer("helpPlayer", helpPlayerName, (byte)0, (byte)0, (byte) 1, (byte) 1, 1);
			} catch (Exception e1) {
				logger.warn("[仙缘论道] [创建小助手] [异常] ["+p.getLogString()+"]");
				e1.printStackTrace();
			}
		}
		return p;
	}
	
	public ResponseMessage handle_TOTLE_COST_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {
		ActivityManager.getInstance().handleTotleCostActivityQuery(player,"客户端请求");
		return null;
	}
	
	public ResponseMessage handle_FUNCTION_SHOW_REQ(Connection conn, RequestMessage message, Player player) {
		FUNCTION_SHOW_REQ req = (FUNCTION_SHOW_REQ)message;
		//1:月卡，2:挖坟，3:寻宝
		int type = req.getQueryType();
		if(type == 1){
//			if(ActivityManager.limitFunctionOfApple){
				return new FUNCTION_SHOW_RES(req.getSequnceNum(),type, true);
//			}
		}else if(type == 2){
			CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.wafenActivity);
			if (cr != null && cr.getBooleanValue() ) {
//				WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
//				if (wp != null) {
//					if(cr.getBooleanValue() && wp.isAllOpened()){
						return new FUNCTION_SHOW_RES(req.getSequnceNum(),type,true);
//					}
//				}
			}
		}else if(type == 3){
			if (TreasureActivityManager.instance.isActivityOpen()) {
				return new FUNCTION_SHOW_RES(req.getSequnceNum(),type,true);
			}
		}
		
		return new FUNCTION_SHOW_RES(req.getSequnceNum(),type,false);
	}
	public ResponseMessage handle_END_DEAD_GAME_REQ(Connection conn, RequestMessage message, Player player) {
		String cityName = TransportData.getMainCityMap(player.getCountry());
		player.leaveServer();
		player.setLastGame(cityName);
		player.setMapName(cityName);
		player.setGame(cityName);
		player.setRealMapName(cityName);
		player.setLastTransferGame(cityName);
		player.setResurrectionMapName(cityName);
		player.getConn().close();
		return null;
	}
	public ResponseMessage handle_ACTIVITY_QUERY_REQ(Connection conn, RequestMessage message, Player player) {
		ACTIVITY_QUERY_REQ req = (ACTIVITY_QUERY_REQ)message;
		//显示最近的10条公告
		int activityId = req.getActivityId();
		Map<Integer,Notice> notices = NoticeManager.getInstance().notices;
		String [] activityNames = new String[notices.size()];
		int [] activityids = new int[notices.size()];
		String acontent = "";
		int [] ids = new int[notices.size()];
		Iterator<Integer> it = notices.keySet().iterator();
		int index = 0;
		boolean havaBindSivler = false;
		int bindSivlerNum = 0;
		while(it.hasNext()){
			Integer key = it.next();
			if(key != null){
				ids[index++] = key;
			}
		}
		Arrays.sort(ids);
		for(int i=0;i<ids.length;i++){
			activityNames[i] = notices.get(ids[i]).getNoticeName();
			activityids[i] = notices.get(ids[i]).getNoticeId();
		}
		
		if(activityId <= 0){
			if(ids.length > 0){
				Notice n = notices.get(ids[ids.length-1]);
				activityId = n.getNoticeId();
				acontent = n.getContent();
				bindSivlerNum = n.getBindSivlerNum();
				havaBindSivler = n.isHavaBindSivler();
			}
		}else{
			Notice n = NoticeManager.getInstance().notices.get(activityId);
			if(n == null){
				try {
					List<Notice> list = NoticeManager.em.query(Notice.class, " noticeId = "+activityId, "noticeId desc", 1, 10);
					if(list != null && list.size() > 0){
						n = list.get(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(n == null){
				String content = Translate.translateString(Translate.查询登录公告失败, new String[][] { { Translate.STRING_1, activityId+"" } });
				player.sendError(content);
				return null;
			}
			acontent = n.getContent();
			bindSivlerNum = n.getBindSivlerNum();
			havaBindSivler = n.isHavaBindSivler();
		}
		ACTIVITY_QUERY_RES res = new ACTIVITY_QUERY_RES(req.getSequnceNum(), activityId, activityNames, activityids, acontent,havaBindSivler,bindSivlerNum);
		player.addMessageToRightBag(res);
		NoticeManager.logger.warn("[查询登录公告] [activityId:"+activityId+"] [activityNames:"+Arrays.toString(activityNames)+"] [activityids:"+Arrays.toString(activityids)+"] [acontent:"+acontent+"] ["+player.getLogString()+"]");
		return null;
	}
	
	String [] rewardNames = {"在线礼包一","在线礼包二","在线礼包三","在线礼包四"};
	long [] totleTimes = {30 * 60 * 1000L, 60 * 60 * 1000L, 90 * 60 * 1000L, 120 * 60 * 1000L};
//	long [] totleTimes = {2 * 60 * 1000L, 4 * 60 * 1000L, 6 * 60 * 1000L, 8 * 60 * 1000L};
	public ResponseMessage handle_TOTLE_ONLINE_REWARD_REQ(Connection conn, RequestMessage message, Player player) {
		long totleOnlineTime = player.getOnlineTimeOfDay();
		int [] rewardState = player.getRewardState();
		
		long [] ids = new long[rewardNames.length];
		long [] onlineTimes = new long[rewardNames.length];
		try {
			for(int i=0;i<rewardNames.length;i++){
				String name = rewardNames[i];
				Article a = ArticleManager.getInstance().getArticle(name);
				ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(name, true, a.getColorType());
				if(ae == null){
					ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.累计在线礼包, player, a.getColorType());
				}
				ids[i] = ae.getId();
				long overTime = totleTimes[i] - totleOnlineTime;
				onlineTimes[i] = overTime < 0 ? 0 : overTime;
				if(rewardState[i] == 0){
					if(overTime <= 0){
						rewardState[i] = 1;
						player.setRewardState(rewardState);
					}
				}
			}
			logger.warn("[请求累计在线奖励] [成功] [totleOnlineTime:{}] [rewardState:{}] [ids:{}] [onlineTimes:{}] [{}]",
					new Object[]{totleOnlineTime,Arrays.toString(rewardState),Arrays.toString(ids),Arrays.toString(onlineTimes),player.getLogString()});
			TOTLE_ONLINE_REWARD_RES res = new TOTLE_ONLINE_REWARD_RES(message.getSequnceNum(), ids, onlineTimes, rewardState);
			return res;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[请求累计在线奖励] [异常] [{}] [{}]",new Object[]{player.getLogString(),e});
		}
		return null;
	}
	
	public ResponseMessage handle_ACTIVITY_IS_SHOW_REQ(Connection conn, RequestMessage message, Player player) {
		ACTIVITY_IS_SHOW_REQ req = (ACTIVITY_IS_SHOW_REQ)message;
		int type = req.getActivityType();
		ActivityManager.getInstance().handPlayerEnter(player, type);
		return null;
	}
	public ResponseMessage handle_GET_TOTLE_ONLINE_REWARD_REQ(Connection conn, RequestMessage message, Player player) {
		int [] rewardState = player.getRewardState();
		try {
			boolean hasReward = false;
			for(int i=0;i<rewardState.length;i++){
				if(rewardState[i] == 1){
					hasReward = true;
					String name = rewardNames[i];
					Article a = ArticleManager.getInstance().getArticle(name);
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.累计在线礼包, player, a.getColorType(), 1, true);
					if(!player.putAll(new ArticleEntity[]{ae}, "累计在线礼包")){
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, "累计在线礼包", "累计在线礼包", 0, 0, 0, "累计在线礼包");
					}
					rewardState[i] = 2;
					player.setLastRewardTime(System.currentTimeMillis());
				}
			}
			if(!hasReward){
				player.sendError(Translate.暂无可领取的奖励);
				return null;
			}
			player.setRewardState(rewardState);
			
			long totleOnlineTime = player.getOnlineTimeOfDay();
			long [] ids = new long[rewardNames.length];
			long [] onlineTimes = new long[rewardNames.length];
			for(int i=0;i<rewardNames.length;i++){
				String name = rewardNames[i];
				Article a = ArticleManager.getInstance().getArticle(name);
				ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(name, true, a.getColorType());
				if(ae == null){
					ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.累计在线礼包, player, a.getColorType());
				}
				ids[i] = ae.getId();
				long overTime = totleTimes[i] - totleOnlineTime;
				onlineTimes[i] = overTime < 0 ? 0 : overTime;
				if(rewardState[i] == 0){
					if(overTime <= 0){
						rewardState[i] = 1;
						player.setRewardState(rewardState);
					}
				}
			}
			logger.warn("[领取累计在线奖励] [成功] [totleOnlineTime:{}] [rewardState:{}] [ids:{}] [onlineTimes:{}] [{}]",
					new Object[]{totleOnlineTime,Arrays.toString(rewardState),Arrays.toString(ids),Arrays.toString(onlineTimes),player.getLogString()});
			TOTLE_ONLINE_REWARD_RES res = new TOTLE_ONLINE_REWARD_RES(message.getSequnceNum(), ids, onlineTimes, rewardState);
			player.addMessageToRightBag(res);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[领取累计在线奖励] [异常] [{}] [{}]",new Object[]{player.getLogString(),e});
		}
		return null;
	}
	
	public ResponseMessage handle_MONTH_CARD_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {
		long now = System.currentTimeMillis();
		MONTH_CARD_ACTIVITY_REQ req = (MONTH_CARD_ACTIVITY_REQ)message;
		
		if(!ActivityManager.isOpenYunYingActivity){
			player.sendError(Translate.活动还没开启);
			return null;
		}
		
		List<MonthCardActivity> monthCardActivity = ActivityManager.getInstance().monthCardActivity;
		if(monthCardActivity == null || monthCardActivity.isEmpty()){
			player.sendError(Translate.活动还没开启);
			return null;
		}
		MonthCardInfo infos [] = new MonthCardInfo[monthCardActivity.size()];
		StringBuffer sb2 = new StringBuffer();
		for(int i = 0;i < monthCardActivity.size();i++){
			MonthCardActivity activity = monthCardActivity.get(i);
			if(activity != null){
				MonthCardInfo info = new MonthCardInfo();
				info.setCardName(activity.getCardName());
				info.setCardId(activity.getCardId());
				info.setChargeMoney(activity.getChargeMoney()+Translate.元);
				if(activity.getDayRewardNames() != null && activity.getChargeRewardNames() != null && activity.getChargeRewardNames().length > 0 && activity.getChargeRewardColors() != null && activity.getChargeRewardColors().length > 0){
					long ids [] = new long[activity.getChargeRewardNames().length];
					long dayIds [] = new long[activity.getDayRewardNames().length];
					for(int j=0;j<activity.getChargeRewardNames().length;j++){
						String name = activity.getChargeRewardNames()[j];
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a == null){
							player.sendError(Translate.赛季奖励配置错误);
							ActivitySubSystem.logger.warn("[月卡活动界面] [出错] [配置的立返奖励不存在] [卡名:{}] [奖励:{}]",new Object[]{activity.getCardName(),name});
							return null;
						}
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(name, true, activity.getChargeRewardColors()[j]);
						if(ae == null){
							try {
								ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.月卡活动立返奖励, player, activity.getChargeRewardColors()[j]);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if(ae == null){
							player.sendError(Translate.赛季奖励配置错误);
							ActivitySubSystem.logger.warn("[月卡活动界面] [出错] [创建临时物品] [卡名:{}] [奖励:{}]",new Object[]{activity.getCardName(),name});
							return null;
						}
						ids[j] = ae.getId();
					}
					for(int j=0;j<activity.getDayRewardNames().length;j++){
						String name = activity.getDayRewardNames()[j];
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a == null){
							player.sendError(Translate.赛季奖励配置错误);
							ActivitySubSystem.logger.warn("[月卡活动界面] [日返奖励出错] [配置的日返奖励不存在] [卡名:{}] [奖励:{}]",new Object[]{activity.getCardName(),name});
							return null;
						}
						ArticleEntity ae = ArticleEntityManager.getInstance().getTempEntity(name, true, activity.getDayRewardColors()[j]);
						if(ae == null){
							try {
								ae = ArticleEntityManager.getInstance().createTempEntity(a, true, ArticleEntityManager.月卡活动立返奖励, player, activity.getDayRewardColors()[j]);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if(ae == null){
							player.sendError(Translate.赛季奖励配置错误);
							ActivitySubSystem.logger.warn("[月卡活动界面] [日返奖励出错] [创建临时物品] [卡名:{}] [奖励:{}]",new Object[]{activity.getCardName(),name});
							return null;
						}
						dayIds[j] = ae.getId();
					}
					info.setDayRewardIds(dayIds);
					info.setDayRewardCounts(activity.getDayRewardCounts());
					info.setChargeRewardIds(ids);
					info.setChargeRewardCounts(activity.getChargeRewardCounts());
					info.setDayRewardInfo(activity.getDayRewardInfo());
					MonthCardRecord record = ActivityManager.getInstance().getMonthCardRecord(player, activity.getCardName());
					if(record != null){
						if(record.getHaveDays() > now){
							sb2.append(activity.getCardName()).append(record.getHaveDays()-System.currentTimeMillis()).append("===");
							info.setHasDayMess((record.getHaveDays()-System.currentTimeMillis()));
							if(!ActivityManagers.isSameDay(record.getLastRewardTime(), System.currentTimeMillis())){
								info.setCanReward(true);
							}
						}
					}
				}
				infos[i] = info;
			}
		}
		MONTH_CARD_ACTIVITY_RES res = new MONTH_CARD_ACTIVITY_RES(req.getSequnceNum(), infos);
		if(logger.isInfoEnabled()){
			logger.info("[月卡活动界面] [请求成功] [infos:{}] [player:{}] [sb2:{}] [耗时:{}ms]",new Object[]{infos.length,player.getLogString(),sb2.toString(), (System.currentTimeMillis() - now)});
		}
		return res;
	}
	
	public ResponseMessage handle_MONTH_CARD_ACTIVITY_GET_REWARD_REQ(Connection conn, RequestMessage message, Player player) {
		long now = System.currentTimeMillis();
		MONTH_CARD_ACTIVITY_GET_REWARD_REQ req = (MONTH_CARD_ACTIVITY_GET_REWARD_REQ)message;
		String cardName = req.getCardName();
		if(cardName == null || cardName.isEmpty()){
			player.sendError(Translate.请选择正确的卡);
			return null;
		}
		
		List<MonthCardActivity> monthCardActivity = ActivityManager.getInstance().monthCardActivity;
		MonthCardActivity mActivity = null;
		for(MonthCardActivity m : monthCardActivity){
			if(m.getCardName().equals(cardName)){
				mActivity = m;
				break;
			}	
		}
		
		if(mActivity == null){
			player.sendError(Translate.活动还没开启);
			return null;
		}
		
		if(!mActivity.isEffectActivity(player)){
			player.sendError(Translate.活动还没开启+"!");
			return null;
		}
		
		MonthCardRecord mc = ActivityManager.getInstance().getMonthCardRecord(player, cardName);
		if(mc == null){
			player.sendError(Translate.您还没购买该卡的服务);
			return null;
		}
		
		if(mc.getHaveDays() <= now){
			player.sendError(Translate.该卡的领取天数为0);
			return null;
		}
		
		if(ActivityManagers.isSameDay(mc.getLastRewardTime(), System.currentTimeMillis())){
			player.sendError(Translate.今天已经领取);
			return null;
		}
		
		String [] dayRewardNames = mActivity.getDayRewardNames();
		if(dayRewardNames == null || dayRewardNames.length <= 0 || mActivity.getDayRewardCounts() == null || mActivity.getDayRewardCounts().length <= 0 ||
				mActivity.getDayRewardColors() == null || mActivity.getDayRewardColors().length <= 0){
			player.sendError(Translate.赛季奖励配置错误);
			return null;
		}
		ArticleEntity aes [] = new ArticleEntity[dayRewardNames.length];
		for(int i=0;i<dayRewardNames.length;i++){
			Article a = ArticleManager.getInstance().getArticle(dayRewardNames[i]);
			if(a != null){
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.月卡活动日返奖励, player, mActivity.getDayRewardColors()[i], mActivity.getDayRewardCounts()[i], true);
					if(ae != null){
						aes[i] = ae;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		boolean result = false;
		if(aes.length > 0){
			try {
				mc.setLastRewardTime(System.currentTimeMillis());
				for(MonthCardRecord d : ActivityManager.getInstance().datas){
					if(d != null && d.getCardName() != null){
						if(d.getPid() == player.getId() && d.getCardName().equals(cardName)){
							d.setLastRewardTime(System.currentTimeMillis());
						}
					}
				}
				
				if(player.putAll(aes, "月卡日返奖励")){
					result = true;
				}else{
					MailManager.getInstance().sendMail(player.getId(), aes, Translate.恭喜您领取了日返奖励, Translate.恭喜您领取了日返奖励, 0, 0, 0, "月卡奖励");
					result = true;
				}
				player.sendError(Translate.恭喜您领取了日返奖励);
				ActivitySubSystem.logger.warn("[月卡活动领取日返奖励] [结果:{}] [cardName:{}] [玩家:{}] [耗时:{}ms]",new Object[]{(result==true?"成功":"失败"),cardName,player.getLogString(), (System.currentTimeMillis()-now)});
				MONTH_CARD_ACTIVITY_GET_REWARD_RES res = new MONTH_CARD_ACTIVITY_GET_REWARD_RES(req.getSequnceNum(), result);
				return res;
			} catch (Exception e) {
				e.printStackTrace();
				ActivitySubSystem.logger.warn("[月卡活动领取日返奖励] [异常:{}] [cardName:{}] [玩家:{}] [耗时:{}ms]",new Object[]{e,cardName,player.getLogString(), (System.currentTimeMillis()-now)});
			}
		}
		return null;
	}
	
	public ResponseMessage handle_CHARGE_GET_PACKAGE_SURE_REQ(Connection conn, RequestMessage message, Player player) {
		ChargeRecord record = ActivityManager.getInstance().getChargeRecord(player);
		int nextDays = record.getDays() + 1;
		ChargePackageActivity activity = null;
		int maxDays = 0;
		for(ChargePackageActivity a : ActivityManager.getInstance().chargePackageActivity){
			if(a.getDays() > maxDays){
				maxDays = a.getDays();
			}
		}
		
		if(nextDays > maxDays){
			nextDays = maxDays;
		}
		
		for(ChargePackageActivity a : ActivityManager.getInstance().chargePackageActivity){
			if(nextDays == a.getDays()){
				activity = a;
			}
		}
		
		if(activity == null){
			ActivitySubSystem.logger.warn("[处理玩家购买充值送礼礼包] [出错:活动配置错误] [nextDays:{}] [maxDays:{}] [玩家:{}]",new Object[]{nextDays,maxDays,player.getLogString()});
			return null;
		}
		
		long money = activity.getMoney();
		String aname = activity.getPackageName();
		
		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		String contentmess = Translate.translateString(Translate.花xx元购买xx, new String[][]{{Translate.COUNT_1,String.valueOf(money)},{Translate.STRING_2,aname}});
		mw.setDescriptionInUUB(contentmess);
		mw.setTitle(Translate.text_419);
		Option_Charge_Sure sure = new Option_Charge_Sure();
		sure.setText(Translate.确定);
		Option_Cancel oc = new Option_Cancel();
		oc.setText(Translate.取消);
		mw.setOptions(new Option[]{sure,oc});
		QUERY_WINDOW_RES req_win = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req_win);
		return null;
	}
	
	public ResponseMessage handle_MONTH_CARD_ACTIVITY_BUY_REQ(Connection conn, RequestMessage message, Player player) {
		MONTH_CARD_ACTIVITY_BUY_REQ req = (MONTH_CARD_ACTIVITY_BUY_REQ)message;
		String cardName = req.getCardName();
		String channleName = req.getChannelName();
		if(cardName == null || cardName.isEmpty()){
			player.sendError(Translate.请选择正确的卡);
			return null;
		}
		
		if(channleName == null || channleName.isEmpty()){
			player.sendError(Translate.请选择正确的卡+".");
			return null;
		}
		
		List<MonthCardActivity> monthCardActivity = ActivityManager.getInstance().monthCardActivity;
		MonthCardActivity mActivity = null;
		for(MonthCardActivity m : monthCardActivity){
			if(m.getCardName().equals(cardName)){
				mActivity = m;
				break;
			}	
		}
		
		if(mActivity == null || 1==1){
			player.sendError(Translate.活动还没开启);
			return null;
		}

		if(!mActivity.isEffectActivity(player)){
			player.sendError(Translate.活动还没开启+"!");
			return null;
		}
		
		List<ChargeMode> list = ChargeManager.getInstance().getChannelChargeModes(channleName);
		if (list == null || list.size() == 0) {
			player.sendError(Translate.无匹的充值信息请联系GM);
			ChargeManager.logger.error(player.getLogString() + "[查询充值列表] [异常] [无匹配的充值列表] [cardName:{}] [渠道:{}] [玩家:{}]",new Object[]{cardName,channleName,player.getLogString()});
			return null;
		}
		
		ChargeMode chargemode = null;
		long money = mActivity.getChargeMoney() * 100;
		String cardId = mActivity.getCardId();
		String chargeId = "";
		String specConfig = "";
		end:for(ChargeMode mode : list){
			if(mode != null && mode.getModeName() != null && mode.getModeName().equals("支付宝")){
				if(mode.getMoneyConfigures() != null){
					for(ChargeMoneyConfigure config : mode.getMoneyConfigures()){
						if(config.getDenomination() == money){
							chargemode = mode;
							chargeId = config.getId();
							specConfig = config.getSpecialConf();
							break end;
						}
					}
				}
			}
		}
		
		if(chargemode == null){
			end:for(ChargeMode mode : list){
				if(mode != null && mode.getMoneyConfigures() != null){
					for(ChargeMoneyConfigure config : mode.getMoneyConfigures()){
						if(config.getDenomination() == money){
							chargemode = mode;
							chargeId = config.getId();
							specConfig = config.getSpecialConf();
							break end;
						}
					}
				}
			}
		}
		
		if(chargemode == null){
			player.sendError(Translate.暂时不能充值联系GM);
			return null;
		}
		
		CHARGE_AGRS_RES res = new CHARGE_AGRS_RES(GameMessageFactory.nextSequnceNum(), chargemode.getModeName(), chargeId, specConfig, cardId, SavingReasonType.充值送月卡活动, money, new String[0]);

		WindowManager windowManager = WindowManager.getInstance();
		MenuWindow mw = windowManager.createTempMenuWindow(600);
		String contentmess = Translate.translateString(Translate.花xx元购买xx, new String[][]{{Translate.COUNT_1,String.valueOf(mActivity.getChargeMoney())},{Translate.STRING_2,cardName}});
		mw.setDescriptionInUUB(contentmess);
		mw.setTitle(Translate.text_419);
		Option_MonthCard_Charge_Sure sure = new Option_MonthCard_Charge_Sure();
		sure.setText(Translate.确定);
		sure.setRes(res);
		Option_Cancel oc = new Option_Cancel();
		oc.setText(Translate.取消);
		mw.setOptions(new Option[]{sure,oc});
		QUERY_WINDOW_RES req_win = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
		player.addMessageToRightBag(req_win);
		
		return null;
	}
	
	/**
	 * 兑换祝福果
	 */
	public ResponseMessage handle_ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ(Connection conn, RequestMessage message, Player player) {
		ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ req = (ACTIVITY_FORLUCK_FRUIT_EXCHANGE_REQ) message;
		long now = SystemTime.currentTimeMillis();
		long articleId = req.getArticleId();
		int articleNum = req.getArticleNum();

		ArticleEntity articleEntity = player.getArticleEntity(articleId);
		if (articleEntity == null) {
			player.sendError(Translate.text_feed_silkworm_005);
			return null;
		}
		int hasNum = player.getArticleEntityNum(articleId);
		if (articleNum != 1) {
			player.sendError(Translate.text_feed_silkworm_007);
			return null;
		}
		if (hasNum < articleNum) {
			player.sendError(Translate.translateString(Translate.text_feed_silkworm_012, new String[][] { { Translate.COUNT_1, String.valueOf(hasNum) } }));
			if (logger.isInfoEnabled()) logger.info(player.getJiazuLogString() + "[兑换祝福果] [物品不足] [要兑换:{}] [持有:{}]", new Object[] { articleNum, hasNum });
			return null;
		}

		ForLuckFruitManager manager = ForLuckFruitManager.getInstance();

		if (!articleEntity.getArticleName().equals(manager.getFruitName())) {
			player.sendError(Translate.text_feed_silkworm_008);
			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[兑换祝福果] [物品不对] [要兑换:{}] [要兑换:{}]", new Object[] { articleEntity.getArticleName(), manager.getFruitName() });
			}
			return null;
		}

		int color = articleEntity.getColorType();
		if (color > manager.getFruitExp().length - 1) {
			logger.error("[养蚕活动] [颜色不存在:{}]", new Object[] { color });
			return null;
		}
		boolean withLastSameDay = TimeTool.instance.isSame(now, player.getLastExchangeForluckFriteTime(), TimeTool.TimeDistance.DAY, 1);
		if (withLastSameDay && manager.getDailyMaxExchangeNum() <= player.getCurrentExchangeForluckFriteTimes()) {
			player.sendError(Translate.text_feed_silkworm_009);
			return null;
		}
		player.removeArticleEntityFromKnapsackByArticleId(articleId, "兑换祝福果", true);
		player.setLastExchangeForluckFriteTime(now);
		player.setCurrentExchangeForluckFriteTimes(withLastSameDay ? player.getCurrentExchangeForluckFriteTimes() + 1 : 1);
		player.setTotalExchangeForluckFriteTimes(player.getTotalExchangeForluckFriteTimes() + 1);
		player.sendError(Translate.text_feed_silkworm_010);
		long addExp = this.获取祝福果增加经验(player) * 祝福丹颜色倍数[articleEntity.getColorType()];
//		player.addExp(manager.getFruitExp()[articleEntity.getColorType()], ExperienceManager.ADDEXP_REASON_EXCHANGE_FORLUCKFRUIT);
		//2014年9月24日    祝福丹获取经验调整为 公式计算
		player.addExp(addExp, ExperienceManager.ADDEXP_REASON_EXCHANGE_FORLUCKFRUIT);
		ArticleStatManager.addToArticleStat(player, null, articleEntity, ArticleStatManager.OPERATION_物品获得和消耗, 0L, (byte) 0, 1L, "兑换祝福果", "");
		return null;
	}
	/**
	 * 获取白丹经验  
	 * @param player
	 * @return
	 */
	public long 获取祝福果增加经验(Player player) {	
		//当前等级^((1-当前等级)/1000)/50
		int level = player.getLevel();
		double times = ((1d-(double)level)/1000d);
		int a = level;
		double c = Math.pow((double)a, times) / 50d;
		long nextLevelExp =  (long) player.getNextLevelExp();
		long exp = (long) (nextLevelExp * c) ;
		return exp;
	}
	/**
	 * 实际经验根据颜色取倍数
	 */
	public static final int[] 祝福丹颜色倍数 = new int[]{1,2,4,8,16};

	/**
	 * 分配家族仓库的果实
	 */
	public ResponseMessage handle_HANDOUT_FORLUCK_STORAGE_REQ(Connection conn, RequestMessage message, Player player) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
		try {
			if (!player.inSelfSeptStation()) {
				return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 1, new int[0], Translate.只能在自己家族驻地操作);
			}
			HANDOUT_FORLUCK_STORAGE_REQ req = (HANDOUT_FORLUCK_STORAGE_REQ) message;
			long playerId = req.getPlayerId();
			int[] nums = req.getNums();

			Player toPlayer;
			try {
				toPlayer = GamePlayerManager.getInstance().getPlayer(playerId);
			} catch (Exception e) {
				e.printStackTrace();
				return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 1, new int[0], Translate.角色不存在);
			}

			JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), player.getJiazuId());

			if (logger.isWarnEnabled()) {
				logger.warn(player.getJiazuLogString() + "[要分配祝福果] [仓库原有{}] [要分配{} ]", new Object[] { Arrays.toString(jiazu.getForLuckFriutNums()), Arrays.toString(nums) });
			}

			if (!JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.release_forluck)) {
				return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 1, jiazu.getForLuckFriutNums(), Translate.你没有权限操作);
			}

			Article article = ArticleManager.getInstance().getArticle(ForLuckFruitManager.getInstance().getFruitName());
			if (article == null) {
				return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 1, jiazu.getForLuckFriutNums(), Translate.物品不存在);
			}
			boolean canSend = true;

			for (int i = 0; i < jiazu.getForLuckFriutNums().length; i++) {
				if (jiazu.getForLuckFriutNums()[i] < nums[i] || nums[i] < 0) {
					logger.warn(player.getJiazuLogString() + "[分配仓库的祝福丹] [异常] [颜色:" + i + "] [要分配数量:" + nums[i] + "] [原有数量:" + jiazu.getForLuckFriutNums()[i] + "]");
					canSend = false;
					break;
				}
			}
			if (canSend) {
				List<ArticleEntity> list = new ArrayList<ArticleEntity>();
				List<Integer> numList = new ArrayList<Integer>();
				for (int i = 0; i < jiazu.getForLuckFriutNums().length; i++) {
					if (nums[i] > 0) {
						jiazu.reduceForLuck(i, nums[i]);
						ArticleEntity articleEntity = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_COLLECTION, toPlayer, i, 1, true);
						list.add(articleEntity);
						numList.add(nums[i]);
					}
				}
				int[] numss = new int[numList.size()];

				for (int i = 0; i < numList.size(); i++) {
					numss[i] = numList.get(i);
				}

				if (logger.isWarnEnabled()) {
					logger.warn(player.getJiazuLogString() + "[要分配祝福果] [分配后资源{}]", new Object[] { Arrays.toString(jiazu.getForLuckFriutNums()) });
				}
				try {
					MailManager.getInstance().sendMail(playerId, list.toArray(new ArticleEntity[0]), numss, Translate.text_feed_silkworm_013, "", 0, 0, 0, Translate.text_feed_silkworm_013);
				} catch (Exception e) {
					logger.error(player.getJiazuLogString() + "[祝福果活动] [分配仓库果实] [发送邮件失败]", e);
					return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 1, jiazu.getForLuckFriutNums(), Translate.发送失败);
				}
				return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 0, jiazu.getForLuckFriutNums(), Translate.操作成功);
			} else {
				return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 1, jiazu.getForLuckFriutNums(), Translate.家族资源不足);
			}
		} catch (Exception e) {
			logger.error("[分配祝福果] [异常]", e);
			return new HANDOUT_FORLUCK_STORAGE_RES(message.getSequnceNum(), (byte) 1, jiazu.getForLuckFriutNums(), Translate.异常);
		}
	}

	/**
	 * 派发树上的祝福果
	 */
	public ResponseMessage handle_HANDOUT_FORLUCK_TREE_REQ(Connection conn, RequestMessage message, Player player) {
		HANDOUT_FORLUCK_TREE_REQ req = (HANDOUT_FORLUCK_TREE_REQ) message;
		long npcId = req.getNpcId();
		long playerId = req.getPlayerId();

		byte result = (byte) 0;
		String failreason = Translate.text_forluck_020;

		if (player.inSelfSeptStation()) {
			// 再次判断权限
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			JiazuMember member = JiazuManager.getInstance().getJiazuMember(player.getId(), jiazu.getJiazuID());
			if (JiazuTitle.hasPermission(member.getTitle(), JiazuFunction.release_forluck)) {
				JiazuMember toMember = JiazuManager.getInstance().getJiazuMember(playerId, jiazu.getJiazuID());
				PlayerSimpleInfo psi = PlayerSimpleInfoManager.getInstance().getInfoById(playerId);
				if (toMember != null && psi != null) {
					ForLuckFruitNPC npc = null;
					for (ForLuckFruitNPC temp : jiazu.getFruitNPCs()) {
						if (temp.getId() == npcId) {
							npc = temp;
							break;
						}
					}
					if (npc != null) {
						if (!npc.canCollection(playerId)) {
							result = (byte) 1;
							failreason = Translate.text_forluck_001;
						} else if (npc.isInService() && npc.getLeftNum() > 0) {
							int color = npc.getOncePickupColor();
							int num = npc.getOncePickupNum();
							if (num > npc.getLeftNum()) {
								result = (byte) 1;
								failreason = Translate.text_forluck_002;
							} else {
								npc.getReapers().put(playerId, SystemTime.currentTimeMillis());
								npc.collection(num);
								Article article = ArticleManager.getInstance().getArticle(ForLuckFruitManager.getInstance().getFruitName());
								if (article != null) {
									List<ArticleEntity> entities = new ArrayList<ArticleEntity>();
									for (int i = 0; i < num; i++) {
										try {
											entities.add(ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_COLLECTION, player, color, 1, true));
										} catch (Exception ex) {
											logger.error(player.getJiazuLogString() + "[分配树上的祝福果] [物品] [异常]", ex);
										}
									}
									try {
										MailManager.getInstance().sendMail(playerId, entities.toArray(new ArticleEntity[0]), Translate.text_feed_silkworm_013, "", 0, 0, 0, Translate.text_feed_silkworm_013);
										{
											// 发送系统消息
											if (color > npc.getArticleColor()) {
												ChatMessageService.getInstance().sendMessageToJiazu(jiazu, Translate.translateString(Translate.text_jiazu_097, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, psi.getName() }, { Translate.STRING_3, String.valueOf(ArticleManager.color_article[color]) }, { Translate.STRING_4, article.getName() } }), "");
											}

										}
										logger.error(player.getJiazuLogString() + "[分配树上的祝福果] [成功] [接受者:{}]", new Object[] { playerId });
									} catch (Exception e) {
										logger.error(player.getJiazuLogString() + "[分配树上的祝福果] [邮件] [异常]", e);
									}
								}
							}
						} else {
							result = (byte) 1;
							failreason = Translate.text_forluck_002;
						}
					} else {
						result = (byte) 1;
						failreason = Translate.text_forluck_002;
					}
				} else {
					result = (byte) 1;
					failreason = Translate.text_forluck_003;
				}
			} else {
				result = (byte) 1;
				failreason = Translate.text_forluck_004;
			}
		} else {
			result = (byte) 1;
			failreason = Translate.text_forluck_005;
		}
		if (logger.isInfoEnabled()) {
			logger.info(player.getJiazuLogString() + "[分配祝福果] [成功] [result:" + result + "] [failreason:" + failreason + "]");
		}
		return new HANDOUT_FORLUCK_TREE_RES(message.getSequnceNum(), result, npcId, playerId, failreason);
	}
	

	/**
	 * 查看活动
	 */
	public ResponseMessage handle_GET_FATEACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {

		GET_FATEACTIVITY_REQ req = (GET_FATEACTIVITY_REQ) message;
		GET_FATEACTIVITY_RES res = null;
		boolean rechoose = req.getRechoose();
		boolean flush = req.getFlush();
		byte activityType = req.getActivityType();
		FateManager fm = FateManager.getInstance();
		// boolean playerRole = true;
		// byte activityState = 0;
		Player[] nodoPlayer = new Player[0];
		Player[] didPlayer = new Player[0];
		long time = 0;

		if (flush) {
			FateActivity fa = fm.getFateActivity(player, activityType);
			if (fa != null) {
				if (FateManager.logger.isWarnEnabled()) {
					FateManager.logger.warn("[活动system] [刷新] [" + player.getLogString() + "] [" + fa.getLastFlushTime() + "] [" + (SystemTime.currentTimeMillis() - fa.getLastFlushTime()) + "] [" + fa.getTemplate().getFlushInterval() * 1000 + "]");
				}
				if ((SystemTime.currentTimeMillis() - fa.getLastFlushTime()) > (fa.getTemplate().getFlushInterval() * 1000)) {
					List<Player> randomUndoPlayer = new ArrayList<Player>();
					List<Player> randomdoPlayer = new ArrayList<Player>();
					List<Long> randomUndo = new ArrayList<Long>();
					if(activityType <= 1){
						player.fateFlushNum++;
					}else{
						player.drinkFlushNum++;
					}
					
					if(player.fateFlushNum == 3){
						player.fateFlushNum = 0;
						Player helpP = getHelpPlayer();
						randomUndoPlayer.add(helpP);
						randomUndo.add(helpP.getId());
						fa.setRandomUndo(randomUndo);
					}else if(player.drinkFlushNum == 3){
						player.drinkFlushNum = 0;
						Player helpP = getHelpPlayer();
						randomUndoPlayer.add(helpP);
						randomUndo.add(helpP.getId());
						fa.setRandomUndo(randomUndo);
					}else{
						fa.setRandomUndo(randomUndo);
						fa.setForceFlush(true);
						fa.flush(player);
						PlayerManager pm = PlayerManager.getInstance();
						for (long id1 : fa.getRandomUndo()) {
							try {
								randomUndoPlayer.add(pm.getPlayer(id1));
							} catch (Exception e) {
								FateManager.logger.error("[查看活动] [没有做过的人] [" + player.getJiazuLogString() + "]", e);
							}
						}
						for (long id1 : fa.getRandomdo()) {
							try {
								randomdoPlayer.add(pm.getPlayer(id1));
							} catch (Exception e) {
								FateManager.logger.error("[查看活动] [做过的人] [" + player.getJiazuLogString() + "]", e);
							}
						}
					}
					res = new GET_FATEACTIVITY_RES(message.getSequnceNum(), activityType, true, (byte) 0, "", 1l, "", (byte) fa.getCountry(), randomUndoPlayer.toArray(new Player[0]), randomdoPlayer.toArray(new Player[0]), fa.getTemplate().getFlushInterval());
					player.addMessageToRightBag(res);
					if (FateManager.logger.isWarnEnabled()) {
						FateManager.logger.warn("[活动system] [刷新成功] [仙缘次数:"+player.fateFlushNum+"] [论道次数:"+player.drinkFlushNum+"] [type:" + activityType + "] [unDo:"+randomUndoPlayer.size()+"] ["+player.getLogString()+"]");
					}
				} else {
					//
					if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[刷新活动错误] [刷新时间不到] [" + player.getJiazuLogString() + "]");
					player.send_HINT_REQ(Translate.刷新时间不到);
				}
			} else {
				if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[刷新活动错误] [" + player.getJiazuLogString() + "]");
			}
			return null;
		}
		if (rechoose) {
			// 重选
			FateActivity fa = fm.getFateActivity(player, activityType);

			if (fa != null && fa.getInviteId() == player.getId()) {

				if (fa.getState() > FateActivity.进行状态) {
					if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[重选活动错误] [" + player.getJiazuLogString() + "] [正在进行]");
					return null;
				}
				time = (SystemTime.currentTimeMillis() - fa.getLastFlushTime() - fa.getTemplate().getFlushInterval() * 1000) / 1000;
				if (time >= 0) {
					time = 0;
				} else {
					time = -time;
				}

				List<Player> randomUndoPlayer = new ArrayList<Player>();
				PlayerManager pm = PlayerManager.getInstance();
				for (long id1 : fa.getRandomUndo()) {
					try {
						if (pm.isOnline(id1)) {
							randomUndoPlayer.add(pm.getPlayer(id1));
						}
					} catch (Exception e) {
						FateManager.logger.error("[查看活动重选] [没有做过的人] [" + player.getJiazuLogString() + "]", e);
					}
				}
				List<Player> randomdoPlayer = new ArrayList<Player>();
				for (long id1 : fa.getRandomdo()) {
					try {
						if (pm.isOnline(id1)) {
							randomdoPlayer.add(pm.getPlayer(id1));
						}
					} catch (Exception e) {
						FateManager.logger.error("[查看活动重选] [做过的人] [" + player.getJiazuLogString() + "]", e);
					}
				}

				res = new GET_FATEACTIVITY_RES(message.getSequnceNum(), activityType, true, (byte) 0, "", 1l, "", (byte) fa.getCountry(), randomUndoPlayer.toArray(new Player[0]), randomdoPlayer.toArray(new Player[0]), time);
				if (FateManager.logger.isWarnEnabled()) {
					FateManager.logger.warn("[活动system] [rechoose重选查看成功] [" + activityType + "] [" + player.getJiazuLogString() + "]");
				}
			}
		} else {
			FateActivity fa = fm.getFateActivity(player, activityType);
			if (fa != null) {
				// 开始状态 = 0;选人状态 = 1;选人成功 = 2;可以进行 = 3;进行状态 = 4;完成状态 = 5;删除状态 = 6;
				byte state = fa.getState();
				long inviteId = fa.getInviteId();
				long invitedId = fa.getInvitedId();
				if (player.getId() == inviteId || player.getId() == invitedId) {

					if (state >= FateActivity.进行状态) {
						// res = new GET_FATEACTIVITY_RES(message.getSequnceNum(), activityType, true, (byte) 1, "活动正在进行中", invitedId, "", nodoPlayer, didPlayer, time);
						// return res;
						SEEM_HINT_RES hintres = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), activityType, Translate.活动正在进行中);
						player.addMessageToRightBag(hintres);
						return null;
					}

					if (player.getId() == inviteId) {
						// 邀请方
						if (state > 1) {
							// 显示重选

							// Player invited;
							PlayerSimpleInfo simpleInfo = null;
							try {
								// invited = PlayerManager.getInstance().getPlayer(invitedId);
								simpleInfo = PlayerSimpleInfoManager.getInstance().getInfoById(invitedId);
								String 显示重选key = "";
								int level = fa.getEnergyLevel();
								if (level > 2 || level < 0) {
									FateManager.logger.error("[level不对] [" + level + "]");
									level = 0;
								}
								String articleName = fa.getTemplate().getArticles();
								String countrySt = CountryManager.getInstance().得到国家名(fa.getCountry());
								if (fa.getTemplate().getType() == FateActivityType.国内仙缘.type || fa.getTemplate().getType() == FateActivityType.国外仙缘.type) {
									// 显示重选key = invited.getName() + "已接受你的邀请，请去凤栖梧桐找她，并使用包裹里的" + articleName + "(用法:双击完成)";
									显示重选key = Translate.translateString(Translate.显示重选key, new String[][] { { Translate.PLAYER_NAME_1, simpleInfo.getName() }, { Translate.STRING_1, countrySt }, { Translate.ARTICLE_NAME_1, articleName } });
								} else {
									// 显示重选key = invited.getName() + "已接受你的邀请，请去凤栖梧桐找他，并使用包裹里的" + articleName + "(用法:双击完成)";
									显示重选key = Translate.translateString(Translate.显示重选key2, new String[][] { { Translate.PLAYER_NAME_1, simpleInfo.getName() }, { Translate.STRING_1, countrySt }, { Translate.ARTICLE_NAME_1, articleName } });
								}

								res = new GET_FATEACTIVITY_RES(message.getSequnceNum(), activityType, true, (byte) 1, 显示重选key, invitedId, simpleInfo.getName(), (byte) fa.getCountry(), nodoPlayer, didPlayer, time);
								if (FateManager.logger.isWarnEnabled()) {
									FateManager.logger.warn("[活动system] [点击按钮重选查看成功] [" + activityType + "] [被邀请id:"+simpleInfo.getId()+"] [被邀请name:"+simpleInfo.getName()+"] [" + player.getJiazuLogString() + "]");
								}

							} catch (Exception e) {

								// FateActivity fa = fm.getFateActivity(player, activityType);

								if (fa != null && fa.getInviteId() == player.getId()) {

									if (fa.getState() > FateActivity.进行状态) {
										if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[重选活动错误] [" + player.getJiazuLogString() + "] [正在进行]");
										return null;
									}
									time = (SystemTime.currentTimeMillis() - fa.getLastFlushTime() - fa.getTemplate().getFlushInterval() * 1000) / 1000;
									if (time >= 0) {
										time = 0;
									} else {
										time = -time;
									}

									List<Player> randomUndoPlayer = new ArrayList<Player>();
									PlayerManager pm = PlayerManager.getInstance();
									for (long id1 : fa.getRandomUndo()) {
										try {
											randomUndoPlayer.add(pm.getPlayer(id1));
										} catch (Exception e1) {
											FateManager.logger.error("[查看活动显示重选] [没有做过的人] [" + player.getJiazuLogString() + "]", e);
										}
									}
									List<Player> randomdoPlayer = new ArrayList<Player>();
									for (long id1 : fa.getRandomdo()) {
										try {
											randomdoPlayer.add(pm.getPlayer(id1));
										} catch (Exception e1) {
											FateManager.logger.error("[查看活动显示重选] [做过的人] [" + player.getJiazuLogString() + "]", e);
										}
									}

									res = new GET_FATEACTIVITY_RES(message.getSequnceNum(), activityType, true, (byte) 0, "", 1l, "", (byte) fa.getCountry(), randomUndoPlayer.toArray(new Player[0]), randomdoPlayer.toArray(new Player[0]), time);
									if (FateManager.logger.isWarnEnabled()) {
										FateManager.logger.warn("[活动system] [邀请方重选查看成功] [" + activityType + "] [" + player.getJiazuLogString() + "]");
									}
								}

								FateManager.logger.error("[活动system] [重选查看异常] [" + activityType + "] [" + player.getJiazuLogString() + "]", e);
							}

						} else {
							// 显示选人页面
							List<Player> randomUndoPlayer = new ArrayList<Player>();
							PlayerManager pm = PlayerManager.getInstance();
							for (long id1 : fa.getRandomUndo()) {
								try {
									if (pm.isOnline(id1)) {
										randomUndoPlayer.add(pm.getPlayer(id1));
									}
									if(pm.getPlayer(id1) != null && pm.getPlayer(id1).getName().equals(helpPlayerName)){
										randomUndoPlayer.add(pm.getPlayer(id1));
									}
								} catch (Exception e) {
									FateManager.logger.error("[查看活动显示选人页面] [没有做过的人] [" + player.getJiazuLogString() + "]", e);
								}
							}
							List<Player> randomdoPlayer = new ArrayList<Player>();
							for (long id1 : fa.getRandomdo()) {
								try {
									if (pm.isOnline(id1)) {
										randomdoPlayer.add(pm.getPlayer(id1));
									}
								} catch (Exception e) {
									FateManager.logger.error("[查看活动显示选人页面] [做过的人] [" + player.getJiazuLogString() + "]", e);
								}
							}
							time = (SystemTime.currentTimeMillis() - fa.getLastFlushTime() - fa.getTemplate().getFlushInterval() * 1000) / 1000;
							if (time >= 0) {
								time = 0;
							} else {
								time = -time;
							}
							res = new GET_FATEACTIVITY_RES(message.getSequnceNum(), activityType, true, (byte) 0, "", 1l, "", (byte) fa.getCountry(), randomUndoPlayer.toArray(new Player[0]), randomdoPlayer.toArray(new Player[0]), time);
							if (FateManager.logger.isWarnEnabled()) {
								FateManager.logger.warn("[活动system] [邀请方重选查看成功1] [" + activityType + "] [" + player.getJiazuLogString() + "]");
							}
						}
					} else {
						// 被邀请方
						if (state > 1) {
							// 显示放弃
							// Player invite;
							PlayerSimpleInfo inviteInfo = null;
							try {
								// invite = PlayerManager.getInstance().getPlayer(inviteId);
								inviteInfo = PlayerSimpleInfoManager.getInstance().getInfoById(inviteId);

								String 显示放弃key = "";
								if (fa.getTemplate().getType() == FateActivityType.国内仙缘.type || fa.getTemplate().getType() == FateActivityType.国外仙缘.type) {
									// 显示放弃key = "请到@STRING_1@国桃源仙境的凤栖梧桐下跟@PLAYER_NAME_1@进行仙缘活动";
									显示放弃key = Translate.translateString(Translate.显示放弃key, new String[][] { { Translate.STRING_1, CountryManager.getInstance().得到国家名(fa.getCountry()) }, { Translate.PLAYER_NAME_1, inviteInfo.getName() } });
								} else {
									// 显示放弃key = "请到@STRING_1@国桃源仙境的凤栖梧桐下跟@PLAYER_NAME_1@进行仙缘活动";
									显示放弃key = Translate.translateString(Translate.显示放弃key2, new String[][] { { Translate.STRING_1, CountryManager.getInstance().得到国家名(fa.getCountry()) }, { Translate.PLAYER_NAME_1, inviteInfo.getName() } });
								}
								res = new GET_FATEACTIVITY_RES(message.getSequnceNum(), activityType, false, (byte) 1, 显示放弃key, inviteInfo.getId(), inviteInfo.getName(), (byte) fa.getCountry(), nodoPlayer, didPlayer, time);
								if (FateManager.logger.isWarnEnabled()) {
									FateManager.logger.warn("[活动system] [被邀请方重选查看成功] [" + activityType + "] [" + player.getJiazuLogString() + "]");
								}

							} catch (Exception e) {
								FateManager.logger.error("[查看活动显示放弃] [" + player.getJiazuLogString() + "]", e);
							}

						} else {
							if (FateManager.logger.isWarnEnabled()) {
								FateManager.logger.warn("[活动system] [活动状态不对] [" + activityType + "] [" + player.getJiazuLogString() + "]");
							}
						}
					}
				} else {
					if (FateManager.logger.isWarnEnabled()) {
						FateManager.logger.warn("[活动system] [活动的玩家不匹配] [" + activityType + "] [" + player.getJiazuLogString() + "]");
					}
				}
			} else {
				if (FateManager.logger.isWarnEnabled()) {
					FateManager.logger.warn("[活动system] [查看个人活动为null] [" + activityType + "] [" + player.getJiazuLogString() + "]");
				}
			}
		}
		return res;
	}

	/**
	 * 邀请玩家活动(没用)
	 */
	public ResponseMessage handle_CHOOSE_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {

		// CHOOSE_ACTIVITY_REQ req = (CHOOSE_ACTIVITY_REQ) message;
		// byte activityType = req.getActivityType(); // 国内仙缘(0),国外仙缘(1),国内论道(2),国外论道(3)
		// long invitedId = req.getPlayerId(); // 被邀请的人的id
		// if (activityType > FateManager.activityNum) {
		// FateManager.logger.warn("[邀请活动参数错误] [活动类型] [" + activityType + "] []");
		// return null;
		// }
		// FateActivity fa = FateManager.getInstance().getFateActivity(player, activityType);
		// if (fa != null && fa.getState() < 4) {
		// PlayerManager pm = PlayerManager.getInstance();
		// try {
		// Player invited = pm.getPlayer(invitedId);
		// if (invited.isOnline()) {
		//
		// CHOOSE_ACTIVITY_RES res = new CHOOSE_ACTIVITY_RES(message.getSequnceNum(), fa.getKeyString(player, invited), fa.getLevelContent(player, invited), activityType);
		// player.addMessageToRightBag(res);
		// if (FateManager.logger.isDebugEnabled()) {
		// FateManager.logger.debug("[活动system] [邀请活动成功] [" + activityType + "]");
		// }
		// } else {
		// player.send_HINT_REQ(player.getName() + "不在线");
		// }
		// } catch (Exception e) {
		// FateManager.logger.warn("[邀请活动参数错误] [] [] []", e);
		// }
		// } else {
		// FateManager.logger.warn("[邀请活动错误] [] [] []");
		// }
		return null;
	}

	/**
	 * 真邀请玩家活动
	 */
	public ResponseMessage handle_REAL_CHOOSE_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {
		REAL_CHOOSE_ACTIVITY_REQ req = (REAL_CHOOSE_ACTIVITY_REQ) message;
		long invitedId = req.getPlayerId(); // 被选择做活动玩家的id
		byte activityType = req.getActivityType();
		if (activityType > FateManager.activityNum || activityType < 0) {
			if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[邀请活动参数错误] [活动类型] [" + activityType + "] []");
			return null;
		}
		int level = 1;
		PlayerManager pm = PlayerManager.getInstance();
		FateManager fm = FateManager.getInstance();
		FateActivity fa = fm.getFateActivity(player, activityType);
		if (fa == null || fa.getState() > 3) {
			if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[邀请活动错误] [玩家没有对应活动] [" + player.getJiazuLogString() + "] []");
			return null;
		}
		try {
			boolean online = pm.isOnline(invitedId);
			if (online) {
				Player invited = pm.getPlayer(invitedId);
				//如果在竞技场 不接受邀请
				{
					if(invited.isInWusheng()) {
						if (FateManager.logger.isWarnEnabled()) {
							FateManager.logger.warn("[邀请活动失败] [被邀请人在比武] [邀请人:" + player.getLogString() + "] [被邀请人:" + invited.getLogString() + "]");
						}
						player.sendError(Translate.text_2585 + invited.getName() + Translate.text_2587);
						return null;
					}
				}
				if (!invited.getActivityRecord(activityType).isCanInvited(fa.getTemplate())) {
					player.send_HINT_REQ(Translate.translateString(Translate.xx正在活动或今日活动次数已满, new String[][] { { Translate.STRING_1, invited.getName() } }));
				} else {
					REAL_CHOOSE_ACTIVITY_RES res = new REAL_CHOOSE_ACTIVITY_RES(message.getSequnceNum(), fa.getId(), activityType, fa.getRealInviteKeyString(player, invited), fa.getSuccessNum(), level);
					invited.addMessageToRightBag(res);

					if (fa.getInvitedId() > 0) {
						player.addMessageToRightBag(new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), activityType, Translate.translateString(Translate.你选择了XX做为你的有缘人将会替换原有有缘人, new String[][] { { Translate.STRING_1, invited.getName() } })));
					} else {
						player.addMessageToRightBag(new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), activityType, Translate.translateString(Translate.你选择了XX做为你的有缘人请等待对方同意, new String[][] { { Translate.STRING_1, invited.getName() } })));
					}
					if (FateManager.logger.isWarnEnabled()) {
						FateManager.logger.warn("[活动system] [真邀请活动成功] [" + activityType + "] [" + player.getJiazuLogString() + "] [" + invited.getLogString() + "]");
					}
				}
			} else {
				Player helpP = pm.getPlayer(invitedId);
				if(helpP != null && helpP.getName().equals(helpPlayerName)){
					boolean result = fa.invitedAgree(helpP, fa, 1, level);
					
					SEEM_HINT_RES res2 = new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), activityType, translateString(Translate.与你进行XX的XX已经到达XX凤栖梧桐, new String[][]{{STRING_1,fa.getActivityName()},{STRING_2,helpP.getName()},{Translate.STRING_3,fa.getCountryName()}}));
					player.addMessageToRightBag(res2);
//					player.addMessageToRightBag(new SEEM_HINT_RES(GameMessageFactory.nextSequnceNum(), activityType, Translate.translateString(Translate.xx同意你仙缘请求去凤栖梧桐去找她2, new String[][] { { Translate.STRING_1, helpP.getName() } })));
					FateManager.logger.warn("[小助手自动同意] [result:"+result+"] ["+player.getLogString()+"] [被邀请:"+helpP.getName()+"]");
				}else{
					player.send_HINT_REQ(Translate.translateString(Translate.xx不在线, new String[][] { { Translate.STRING_1, player.getName() } }));
				}
//				player.sendError(Translate.小助手已经同意);
//				player.sendError(Translate.小助手已经到达);
//				REAL_CHOOSE_ACTIVITY_RES res = new REAL_CHOOSE_ACTIVITY_RES(message.getSequnceNum(), fa.getId(), activityType, fa.getRealInviteKeyString(player, invited), fa.getSuccessNum(), level);
//				invited.addMessageToRightBag(res);
				
			}

		} catch (Exception e) {
			FateManager.logger.error("[真邀请玩家活动] [" + player.getJiazuLogString() + "]", e);
		}
		return null;
	}

	/**
	 * 被邀请玩家同意
	 */
	public ResponseMessage handle_AGREE_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {

		AGREE_ACTIVITY_REQ req = (AGREE_ACTIVITY_REQ) message;
		long activityId = req.getActivityId();
		byte activityType = req.getActivityType();
		int successNum = req.getSuccessNum();
		int level = req.getLevel();
		if (activityType > FateManager.activityNum || activityType < 0) {
			if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[邀请活动参数错误] [活动类型] [" + activityType + "] []");
			return null;
		}
		if (level > 2 || level < 0) {
			if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[邀请活动参数错误] [级别] [" + level + "] []");
			return null;
		}
		FateActivity fa = FateManager.getInstance().getFateActivity(activityId);

		if (fa.getInviteId() > 0) {
			boolean bln = fa.invitedAgree(player, fa, successNum, level);
		} else {
			player.sendError(Translate.对方已经放弃此活动或已完成);
		}
		return null;
	}

	/**
	 * 被邀请玩家不同意
	 */
	public ResponseMessage handle_DISAGREE_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {

		DISAGREE_ACTIVITY_REQ req = (DISAGREE_ACTIVITY_REQ) message;
		long activityId = req.getActivityId();
		FateManager fm = FateManager.getInstance();
		FateActivity fa = fm.getFateActivity(activityId);
		if (fa != null) {
			Player invite;
			try {
				if (PlayerManager.getInstance().isOnline(fa.getInviteId())) {
					invite = PlayerManager.getInstance().getPlayer(fa.getInviteId());
					// player.send_HINT_REQ("你拒绝了" + invite.getName());
					player.send_HINT_REQ(Translate.translateString(Translate.你拒绝了xx, new String[][] { { Translate.STRING_1, invite.getName() } }));
					if (invite.isOnline()) {
						// invite.send_HINT_REQ(player.getName() + "拒绝了你");
						invite.send_HINT_REQ(Translate.translateString(Translate.xx拒绝了你, new String[][] { { Translate.STRING_1, player.getName() } }));
					}
				}else{
					player.sendError(对方不在线);
					return null;
				}
			} catch (Exception e) {
				FateManager.logger.error("[不同意活动] [" + player.getJiazuLogString() + "]", e);
			}
		}
		return null;
	}

	/**
	 * 被邀请者放弃本次活动
	 */
	public ResponseMessage handle_GIVEUP_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {

		GIVEUP_ACTIVITY_REQ req = (GIVEUP_ACTIVITY_REQ) message;
		byte activityType = req.getActivityType();
		FateManager fm = FateManager.getInstance();
		if (activityType > FateManager.activityNum || activityType < 0) {
			if (FateManager.logger.isWarnEnabled()) FateManager.logger.warn("[邀请活动参数错误] [活动类型] [" + activityType + "] []");
			return null;
		}

		FateManager.getInstance().cancleActivity(player, activityType, false);
		return null;
	}

	/**
	 * 玩家答题
	 */
	public ResponseMessage handle_ANSWER_QUIZ_REQ(Connection conn, RequestMessage message, Player player) {

		ANSWER_QUIZ_REQ req = (ANSWER_QUIZ_REQ) message;
		byte answerType = req.getAnswerType(); // 指定答案0，帮助1，放大镜2
		byte answerKey = req.getAnswerKey(); // 答案0,1,2,3

		if (answerType > 2 || answerType < 0) {
			if (QuizManager.logger.isWarnEnabled()) QuizManager.logger.warn("[答题参数错误] [答案类型] [" + answerType + "] []");
			return null;
		}

		if (answerKey > 3 || answerKey < 0) {
			if (QuizManager.logger.isWarnEnabled()) QuizManager.logger.warn("[答题参数错误] [答案] [" + answerKey + "] []");
			return null;
		}
		QuizManager.getInstance().answerQuiz(answerType, answerKey, player);

		return null;
	}

	/**
	 * 玩家关闭答题活动
	 */
	public ResponseMessage handle_ANSWER_QUIZ_CANCEL_REQ(Connection conn, RequestMessage message, Player player) {

		// 弹出选择框
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
		mw.setDescriptionInUUB(Translate.text_关闭答题活动);

		Quiz qz = QuizManager.getInstance().getQz();
		if (qz == null) {
			player.addMessageToRightBag(new ANSWER_QUIZ_FINISH_RES(GameMessageFactory.nextSequnceNum()));
		} else {
			Option_Confirm_Close o1 = new Option_Confirm_Close();
			o1.setText(Translate.确定);

			Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
			o2.setText(Translate.取消);
			mw.setOptions(new Option[] { o1, o2 });
			player.addMessageToRightBag(new QUERY_WINDOW_RES(message.getSequnceNum(), mw, mw.getOptions()));

			if (QuizManager.logger.isWarnEnabled()) {
				QuizManager.logger.warn("[弹出关闭答题活动窗口成功] [" + player.getJiazuLogString() + "]");
			}
		}
		return null;
	}

	/**
	 * 查询指定某一特殊特殊装备
	 */
	public ResponseMessage handle_QUERY_ONE_SPECIAL_EQUIPMENT_REQ(Connection conn, RequestMessage message, Player player) {

		QUERY_ONE_SPECIAL_EQUIPMENT_REQ req = (QUERY_ONE_SPECIAL_EQUIPMENT_REQ) message;

		SpecialEquipmentManager sem = SpecialEquipmentManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		boolean bln = req.getEqupmentType();
		String name = req.getEqupmentName();

		if (bln) {
			// 鸿天帝宝
			Equipment[] equs = ArticleManager.getInstance().allSpecialEquipments1;
			boolean temp = false;
			Equipment ee = null;
			for (Equipment e : equs) {
				if (e.getName().equals(name)) {
					temp = true;
					ee = e;
					break;
				}
			}
			if (!temp) {
				if (SpecialEquipmentManager.logger.isWarnEnabled()) {
					SpecialEquipmentManager.logger.warn("[查询指定特殊装备错误] [没有指定装备] [" + name + "] [" + player.getJiazuLogString() + "]");
				}
			} else {

				QUERY_ONE_SPECIAL_EQUIPMENT_RES res = null;
				List<Long> list = sem.getSpecialEquipmentBillBoard().getSpeicail1(name);
				String icon = ee.getIconId();
				// int level = ee.getArticleLevel();
				int level = ee.getPlayerLevelLimit();
				int classes = ee.getClassLimit();
				String story = ee.getStroy();
				if (story == null) {
					story = "";
				}
				story = story + "\n";
				String skillName = "";
				String skillDes = "";
				String equipmentDes = "";
				int skillId = ee.getSkillId();
				if (list != null && list.size() > 0) {
					ArticleEntity ae = aem.getEntity(list.get(0));
					if (ae != null && ae instanceof Special_1EquipmentEntity) {
						Special_1EquipmentEntity se1 = (Special_1EquipmentEntity) ae;
						long ownerId = se1.getPlayerId();
						Skill skill = CareerManager.getInstance().getSkillById(skillId);
						if (skill == null) {
							SpecialEquipmentManager.logger.error("[查询特殊装备1] [没有技能] [" + player.getJiazuLogString() + "] [" + skillId + "]");
						} else {
							skillName = skill.getName();
							skillDes = SkillInfoHelper.generate(player, skill, false);
						}
						List<PlayerSimpleInfo> specialPlayer = new ArrayList<PlayerSimpleInfo>();
						PlayerSimpleInfo simplePlayer;
						for(long eqId:list){
							ae = ArticleEntityManager.getInstance().getEntity(eqId);
							if(ae instanceof Special_1EquipmentEntity){
								se1 = (Special_1EquipmentEntity)ae;
								long playerId = se1.getPlayerId();
								simplePlayer = PlayerSimpleInfoManager.getInstance().getInfoById(playerId);
								if(simplePlayer != null){
									specialPlayer.add(simplePlayer);
								}
							}
						}
						
						if(specialPlayer.size() > 0){
							
							String[] names = new String[specialPlayer.size()];
							byte[] careers = new byte[specialPlayer.size()];
							byte[] countrys = new byte[specialPlayer.size()];
							int[] levels = new int[specialPlayer.size()];
							short[] classLevel = new short[specialPlayer.size()];

							for (int i = 0; i < specialPlayer.size(); i++) {
								PlayerSimpleInfo ps = specialPlayer.get(i);
								names[i] = ps.getName();
								careers[i] = ps.getCareer();
								countrys[i] = ps.getCountry();
								levels[i] = ps.getLevel();
								classLevel[i] = (short) ps.getClassLevel();
							}
							res = new QUERY_ONE_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), name, icon, level, classes, story, equipmentDes, names, careers, countrys, levels, classLevel);
							if (SpecialEquipmentManager.logger.isWarnEnabled()) {
								SpecialEquipmentManager.logger.warn("[查询指定特殊装备1成功] [" + player.getJiazuLogString() + "] [" + name + "] []");
							}
							player.addMessageToRightBag(res);
						}else{
							//这个装备没有主人
							res = new QUERY_ONE_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), name, icon, level, classes, story, equipmentDes, new String[0], new byte[0], new byte[0], new int[0], new short[0]);
							if (SpecialEquipmentManager.logger.isWarnEnabled()) {
								SpecialEquipmentManager.logger.warn("[查询指定特殊装备1异常] [删除装备] [" + player.getJiazuLogString() + "] [" + name + "] [主人Id:" + ownerId + "]");
							}
							player.addMessageToRightBag(res);
						}
						
						
//						PlayerSimpleInfo simpleInfo = PlayerSimpleInfoManager.getInstance().getInfoById(ownerId);
//						equipmentDes = se1.getInfoShow(player);
//						if (simpleInfo != null) {
//							String[] names = { simpleInfo.getName() };
//							byte[] careers = { simpleInfo.getCareer() };
//							byte[] countrys = { simpleInfo.getCountry() };
//							int[] levels = { simpleInfo.getLevel() };
//							short[] classLevel = { (short) simpleInfo.getClassLevel() };
//							res = new QUERY_ONE_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), name, icon, level, classes, story, equipmentDes, names, careers, countrys, levels, classLevel);
//							if (SpecialEquipmentManager.logger.isWarnEnabled()) {
//								SpecialEquipmentManager.logger.warn("[查询指定特殊装备1成功] [" + player.getJiazuLogString() + "] [" + name + "] []");
//							}
//							player.addMessageToRightBag(res);
//						} else {
//							// 这个装备找不主人 删除
//							// sem.getSpecialEquipmentBillBoard().removeSpecial1(name, list.get(0));
//							res = new QUERY_ONE_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), name, icon, level, classes, story, equipmentDes, new String[0], new byte[0], new byte[0], new int[0], new short[0]);
//							if (SpecialEquipmentManager.logger.isWarnEnabled()) {
//								SpecialEquipmentManager.logger.warn("[查询指定特殊装备1异常] [删除装备] [" + player.getJiazuLogString() + "] [" + name + "] [主人Id:" + ownerId + "]");
//							}
//							player.addMessageToRightBag(res);
//						}
					}
				} else {
					res = new QUERY_ONE_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), name, icon, level, classes, story, equipmentDes, new String[0], new byte[0], new byte[0], new int[0], new short[0]);
					player.addMessageToRightBag(res);

					if (SpecialEquipmentManager.logger.isWarnEnabled()) {
						SpecialEquipmentManager.logger.warn("[查询指定特殊装备错误] [没有生成此装备] [" + name + "] [" + player.getJiazuLogString() + "]");
					}
				}

			}
		} else {
			// 伏天古宝
			Equipment[] equs = ArticleManager.getInstance().allSpecialEquipments2;
			boolean temp = false;
			Equipment ee = null;
			for (Equipment e : equs) {
				if (e.getName().equals(name)) {
					temp = true;
					ee = e;
					break;
				}
			}
			if (!temp) {
				if (SpecialEquipmentManager.logger.isWarnEnabled()) SpecialEquipmentManager.logger.warn("[查询指定特殊装备2错误] [没有指定装备] [" + name + "] []");
			} else {

				// 装备Id
				List<Long> list = sem.getSpecialEquipmentBillBoard().getSpeicail2(name);
				QUERY_ONE_SPECIAL_EQUIPMENT_RES res = null;
				String icon = ee.getIconId();
				int level = ee.getArticleLevel();
				int classes = ee.getClassLimit();
				String story = ee.getStroy();
				if (story == null) {
					story = "";
				}
				story = story + "\n";
				String skillName = "";
				String skillDes = "";
				String equipmentDes = "";
				if (list != null && list.size() > 0) {

					List<PlayerSimpleInfo> listInfos = new ArrayList<PlayerSimpleInfo>();
					int max = list.size();
					List<Long> tempList = new ArrayList<Long>();
					for (int i = 0; i < max; i++) {
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(list.get(i));
						if (ae != null && ae instanceof Special_2EquipmentEntity) {
							Special_2EquipmentEntity se2 = (Special_2EquipmentEntity) ae;
							equipmentDes = se2.getInfoShow(player);
							Long ownerId = se2.getOwnerId();
							PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(ownerId);
							if (info != null) {
								listInfos.add(info);
							} else {
								tempList.add(list.get(i));
							}
						} else {
							tempList.add(list.get(i));
						}
					}

					if (tempList.size() > 0) {
						// 删除特殊装备2
						for (long id : tempList) {
							sem.getSpecialEquipmentBillBoard().removeSpecial2(name, id);
						}
						if (SpecialEquipmentManager.logger.isWarnEnabled()) {
							SpecialEquipmentManager.logger.warn("[查询指定特殊装备2] [没有找到玩家] [删除装备] [" + name + "] [" + player.getJiazuLogString() + "]");
						}
					}
					String[] names = new String[listInfos.size()];
					byte[] careers = new byte[listInfos.size()];
					byte[] countrys = new byte[listInfos.size()];
					int[] levels = new int[listInfos.size()];
					short[] classLevel = new short[listInfos.size()];

					for (int i = 0; i < listInfos.size(); i++) {
						PlayerSimpleInfo ps = listInfos.get(i);
						names[i] = ps.getName();
						careers[i] = ps.getCareer();
						countrys[i] = ps.getCountry();
						levels[i] = ps.getLevel();
						classLevel[i] = (short) ps.getClassLevel();
					}

					res = new QUERY_ONE_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), name, icon, level, classes, story, equipmentDes, names, careers, countrys, levels, classLevel);
					if (SpecialEquipmentManager.logger.isWarnEnabled()) {
						SpecialEquipmentManager.logger.warn("[查询指定特殊装备2成功] [" + player.getJiazuLogString() + "] [" + name + "] [" + names.length + "]");
					}
					player.addMessageToRightBag(res);

				} else {
					res = new QUERY_ONE_SPECIAL_EQUIPMENT_RES(GameMessageFactory.nextSequnceNum(), name, icon, level, classes, story, equipmentDes, new String[0], new byte[0], new byte[0], new int[0], new short[0]);
					player.addMessageToRightBag(res);
					if (SpecialEquipmentManager.logger.isWarnEnabled()) {
						SpecialEquipmentManager.logger.warn("[查询指定特殊装备错误] [指定装备还没出现] [" + name + "] [" + player.getJiazuLogString() + "]");
					}
				}
			}
		}
		return null;
	}

	// SPECIAL_DEAL_REQ SPECIAL_DEAL_ADD_ARTICLE_REQ SPECIAL_DEAL_DEL_ARTICLE_REQ SPECIAL_DEAL_FINISH_REQ CLOSE_DEAL_REQ
	/**
	 * 玩家申请开始交换
	 */
	public ResponseMessage handle_SPECIAL_DEAL_REQ(Connection conn, RequestMessage message, Player player) {

		SPECIAL_DEAL_REQ req = (SPECIAL_DEAL_REQ) message;
		long playerAId = req.getPlayerAId();
		long playerBId = req.getPlayerBId();
		PlayerManager pm = PlayerManager.getInstance();

		Player playerA;
		Player playerB;
		try {
			playerA = pm.getPlayer(playerAId);
			playerB = pm.getPlayer(playerBId);
		} catch (Exception e) {
			FateManager.logger.error("[玩家申请开始交换] " + player.getJiazuLogString() + "]", e);
			return null;
		}
		if (playerA.isFighting()) {
			playerA.sendError(Translate.您正在战斗状态中不能交换);
			return null;
		}
		if (playerB.isFighting()) {
			playerA.sendError(Translate.对方正在战斗状态中不能交换);
			return null;
		}
		if (playerB.hiddenChangePop) {
			playerA.sendError(Translate.该玩家已经屏蔽交换);
			return null;
		}
		ArticleExchangeManager aem = ArticleExchangeManager.getInstance();
		boolean can = aem.playerCanExchange(playerA, playerB);
		if (can) {
			// 弹出框选择框
			MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
			String descript = Translate.translateString(Translate.text_寻宝描述, new String[][] { { Translate.PLAYER_NAME_1, playerA.getName() } });
			mw.setDescriptionInUUB(descript);

			Option_Agree_Exchange o1 = new Option_Agree_Exchange();
			o1.setPlayerA(playerA);
			o1.setText(Translate.确定);

			Option_DisAgree_Exchange o2 = new Option_DisAgree_Exchange();
			o2.setPlayerA(playerA);
			o2.setText(Translate.取消);
			mw.setOptions(new Option[] { o1, o2 });
			playerB.addMessageToRightBag(new QUERY_WINDOW_RES(message.getSequnceNum(), mw, mw.getOptions()));

			if (ArticleExchangeManager.logger.isWarnEnabled()) {
				ArticleExchangeManager.logger.warn("[交换弹出窗口成功] [" + player.getJiazuLogString() + "] []");
			}
		}
		return null;
	}

	/**
	 * 玩家往交换栏加物品
	 */
	public ResponseMessage handle_SPECIAL_DEAL_ADD_ARTICLE_REQ(Connection conn, RequestMessage message, Player player) {

		SPECIAL_DEAL_ADD_ARTICLE_REQ req = (SPECIAL_DEAL_ADD_ARTICLE_REQ) message;
		long dealId = req.getDealId();
		ArticleExchangeManager aem = ArticleExchangeManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		List<ExchangeDeal> list = aem.getList();
		ExchangeDeal ed = null;
		for (ExchangeDeal ed1 : list) {
			if (ed1.getId() == dealId) {
				ed = ed1;
				break;
			}
		}
		// if (ed != null && !ed.isState()) {
		if (ed != null) {

			if (ed.getPlayerAId() != player.getId() && ed.getPlayerBId() != player.getId()) {
				if (ArticleExchangeManager.logger.isWarnEnabled()) {
					ArticleExchangeManager.logger.warn("[交易栏添加物品错误] [没有此交换实体] [" + player.getJiazuLogString() + "] []");
				}
				return null;
			}

			long entityId = req.getEntityId();
			ArticleEntity ae = player.getArticleEntity(entityId);
			if (ae instanceof ExchangeInterface) {
				Player playerB = null;
				if (ed.getPlayerAId() == player.getId()) {
					// a方是本人
					try {
						playerB = pm.getPlayer(ed.getPlayerBId());
						ed.setEntityAId(entityId);

						if (ed.isPlayerAConfirmed()) {
							ed.setPlayerAConfirmed(false);
							SPECIAL_DEAL_FINISH_RES res1 = new SPECIAL_DEAL_FINISH_RES(GameMessageFactory.nextSequnceNum(), false);
							playerB.addMessageToRightBag(res1);
							player.addMessageToRightBag(res1);
							if (ArticleExchangeManager.logger.isWarnEnabled()) {
								ArticleExchangeManager.logger.warn("[玩家再次添加物品] [关闭交易] [" + player.getJiazuLogString() + "] [" + dealId + "]");
							}

						} else {

							SPECIAL_DEAL_ADD_ARTICLE_RES res = new SPECIAL_DEAL_ADD_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), entityId);
							playerB.addMessageToRightBag(res);
							if (ArticleExchangeManager.logger.isWarnEnabled()) {
								ArticleExchangeManager.logger.warn("[玩家首次添加物品] [" + player.getJiazuLogString() + "] [" + dealId + "]");
							}
						}

					} catch (Exception e) {
						ArticleExchangeManager.logger.warn("[玩家首次添加物品异常] [" + player.getJiazuLogString() + "]", e);
						return null;
					}
				} else {
					try {
						// B方是本人
						playerB = pm.getPlayer(ed.getPlayerAId());
						ed.setEntityBId(entityId);

						if (ed.isPlayerBConfirmed()) {
							ed.setPlayerBConfirmed(false);
							SPECIAL_DEAL_FINISH_RES res1 = new SPECIAL_DEAL_FINISH_RES(GameMessageFactory.nextSequnceNum(), false);
							playerB.addMessageToRightBag(res1);
							player.addMessageToRightBag(res1);

							if (ArticleExchangeManager.logger.isWarnEnabled()) {
								ArticleExchangeManager.logger.warn("[玩家再次添加物品] [关闭交易] [" + player.getJiazuLogString() + "] [" + dealId + "]");
							}
						} else {
							SPECIAL_DEAL_ADD_ARTICLE_RES res = new SPECIAL_DEAL_ADD_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), entityId);
							playerB.addMessageToRightBag(res);
							if (ArticleExchangeManager.logger.isWarnEnabled()) {
								ArticleExchangeManager.logger.warn("[玩家首次添加物品] [" + player.getJiazuLogString() + "] [" + dealId + "]");
							}
						}
					} catch (Exception e) {
						FateManager.logger.error("[玩家添加物品] [" + player.getJiazuLogString() + "]", e);
						return null;
					}
				}
			} else {
				if (ArticleExchangeManager.logger.isWarnEnabled()) ArticleExchangeManager.logger.warn("[交易栏添加物品错误] [不能用于交换] [" + player.getJiazuLogString() + "] []");
			}
		} else {
			if (ArticleExchangeManager.logger.isWarnEnabled()) ArticleExchangeManager.logger.warn("[交易栏添加物品错误] [" + player.getJiazuLogString() + "] [" + dealId + "]");
		}

		return null;
	}

	/**
	 * 玩家从交换栏减物品
	 */
	public ResponseMessage handle_SPECIAL_DEAL_DEL_ARTICLE_REQ(Connection conn, RequestMessage message, Player player) {

		SPECIAL_DEAL_DEL_ARTICLE_REQ req = (SPECIAL_DEAL_DEL_ARTICLE_REQ) message;
		long dealId = req.getDealId();
		ArticleExchangeManager aem = ArticleExchangeManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		List<ExchangeDeal> list = aem.getList();
		ExchangeDeal ed = null;
		for (ExchangeDeal ed1 : list) {
			if (ed1.getId() == dealId) {
				ed = ed1;
				break;
			}
		}
		// if (ed != null && !ed.isState()) {
		if (ed != null) {
			if (ed.getPlayerAId() != player.getId() && ed.getPlayerBId() != player.getId()) {
				if (ArticleExchangeManager.logger.isWarnEnabled()) {
					ArticleExchangeManager.logger.warn("[交易栏减少物品错误] [没有此交换实体] [" + player.getJiazuLogString() + "] []");
				}
				return null;
			}
			long entityId = req.getEntityId();
			ArticleEntity ae = player.getArticleEntity(entityId);
			if (ae instanceof ExchangeInterface) {
				Player playerB = null;
				if (ed.getPlayerAId() == player.getId()) {
					// a方为本人
					try {
						playerB = pm.getPlayer(ed.getPlayerBId());
						if (ed.getEntityAId() == entityId) {
							ed.setEntityAId(-1);
							SPECIAL_DEAL_DEL_ARTICLE_RES res = new SPECIAL_DEAL_DEL_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), entityId);
							playerB.addMessageToRightBag(res);
							if (ed.isPlayerAConfirmed()) {
								ed.setPlayerAConfirmed(false);
								SPECIAL_DEAL_FINISH_RES res1 = new SPECIAL_DEAL_FINISH_RES(GameMessageFactory.nextSequnceNum(), false);
								playerB.addMessageToRightBag(res1);
								player.addMessageToRightBag(res1);
							}
							if (ArticleExchangeManager.logger.isWarnEnabled()) {
								ArticleExchangeManager.logger.warn("[玩家减少物品成功] [" + player.getJiazuLogString() + "] [" + dealId + "]");
							}
						} else {
							if (ArticleExchangeManager.logger.isWarnEnabled()) ArticleExchangeManager.logger.warn("[交易栏减少物品错误] [交易栏没有这个id] [" + player.getJiazuLogString() + "] []");
							return null;
						}

					} catch (Exception e) {
						FateManager.logger.error("[玩家减少物品] [没有做过的人] [" + player.getJiazuLogString() + "]", e);
						return null;
					}
				} else {
					try {
						// b方为本人
						playerB = pm.getPlayer(ed.getPlayerAId());
						if (ed.getEntityBId() == entityId) {
							ed.setEntityBId(-1);
							SPECIAL_DEAL_DEL_ARTICLE_RES res = new SPECIAL_DEAL_DEL_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), entityId);
							playerB.addMessageToRightBag(res);
							if (ed.isPlayerBConfirmed()) {
								ed.setPlayerBConfirmed(false);
								SPECIAL_DEAL_FINISH_RES res1 = new SPECIAL_DEAL_FINISH_RES(GameMessageFactory.nextSequnceNum(), false);
								playerB.addMessageToRightBag(res1);
								player.addMessageToRightBag(res1);
							}
							if (ArticleExchangeManager.logger.isWarnEnabled()) {
								ArticleExchangeManager.logger.warn("[玩家减少物品成功] [" + player.getJiazuLogString() + "] [" + dealId + "]");
							}
						} else {
							if (ArticleExchangeManager.logger.isWarnEnabled()) {
								ArticleExchangeManager.logger.warn("[交易栏减少物品错误] [交易栏没有这个id] [" + player.getJiazuLogString() + "] [" + dealId + "]");
							}
							return null;
						}
					} catch (Exception e) {
						ArticleExchangeManager.logger.warn("[交易栏减少物品错误异常] [" + player.getJiazuLogString() + "] []", e);
						return null;
					}
				}
			} else {
				if (ArticleExchangeManager.logger.isWarnEnabled()) ArticleExchangeManager.logger.warn("[交易栏减少物品错误] [不能用于交换] [" + player.getJiazuLogString() + "] [" + dealId + "]");
			}
		} else {
			if (ArticleExchangeManager.logger.isWarnEnabled()) ArticleExchangeManager.logger.warn("[交易栏减少物品错误] [" + player.getJiazuLogString() + "] [" + dealId + "]");
		}

		return null;
	}

	/**
	 * 玩家申请完成
	 */
	public ResponseMessage handle_SPECIAL_DEAL_FINISH_REQ(Connection conn, RequestMessage message, Player player) {

		SPECIAL_DEAL_FINISH_REQ req = (SPECIAL_DEAL_FINISH_REQ) message;
		PlayerManager pm = PlayerManager.getInstance();
		long dealId = req.getDealId();
		long entityId = req.getEntityId();
		ArticleExchangeManager aem = ArticleExchangeManager.getInstance();

		ExchangeDeal ed = aem.getDealById(dealId);
		// if (ed != null && !ed.isState()) {
		if (ed != null) {
			if (ed.getPlayerAId() == player.getId()) {
				if (ed.getEntityAId() == entityId) {
					ed.setPlayerAConfirmed(true);
				} else {
					player.send_HINT_REQ(Translate.还没放上交换物品);
					return null;
				}
			} else {
				if (ed.getEntityBId() == entityId) {
					ed.setPlayerBConfirmed(true);
				} else {
					player.send_HINT_REQ(Translate.还没放上交换物品);
					return null;
				}
			}

			if (ed.isCanExchange()) {
				boolean bln = ed.finishExchange();
				if (bln) {
					// ed.setState(true);
					CLOSE_DEAL_RES res = new CLOSE_DEAL_RES(GameMessageFactory.nextSequnceNum(), dealId);
					player.addMessageToRightBag(res);
					if (ed.getPlayerAId() == player.getId()) {
						try {
							pm.getPlayer(ed.getPlayerBId()).addMessageToRightBag(res);
						} catch (Exception e) {
							FateManager.logger.error("[交换完成] [" + player.getJiazuLogString() + "]", e);
						}
					} else {
						try {
							pm.getPlayer(ed.getPlayerAId()).addMessageToRightBag(res);
						} catch (Exception e) {
							FateManager.logger.error("[交换完成] [" + player.getJiazuLogString() + "]", e);
						}
					}
					if (ArticleExchangeManager.logger.isWarnEnabled()) {
						ArticleExchangeManager.logger.warn("[交换完成] [" + player.getJiazuLogString() + "] [" + dealId + "]");
					}
				} else {
					if (ArticleExchangeManager.logger.isWarnEnabled()) {
						ArticleExchangeManager.logger.warn("[交换失败] [" + player.getJiazuLogString() + "] [" + dealId + "]");
					}
				}

			} else {

				if (ArticleExchangeManager.logger.isWarnEnabled()) {
					ArticleExchangeManager.logger.warn("[完成交换成功] [" + player.getJiazuLogString() + "] [" + dealId + "]");
				}

				SPECIAL_DEAL_FINISH_RES res = new SPECIAL_DEAL_FINISH_RES(dealId, true);
				player.addMessageToRightBag(res);
			}
		} else {
			if (ArticleExchangeManager.logger.isWarnEnabled()) {
				ArticleExchangeManager.logger.warn("[提交交换完成错误] [" + player.getJiazuLogString() + "] [" + dealId + "]");
			}
		}

		return null;
	}

	/**
	 * 玩家关闭交换
	 */
	public ResponseMessage handle_CLOSE_DEAL_REQ(Connection conn, RequestMessage message, Player player) {

		CLOSE_DEAL_REQ req = (CLOSE_DEAL_REQ) message;
		long dealId = req.getDealId();
		ArticleExchangeManager aem = ArticleExchangeManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		ExchangeDeal ed = aem.getDealById(dealId);
		if (ed != null) {
			// ed.setState(true);
			Player playerB = null;
			if (ed.getPlayerAId() == player.getId()) {
				try {
					playerB = pm.getPlayer(ed.getPlayerBId());
					playerB.addMessageToRightBag(new CLOSE_DEAL_RES(GameMessageFactory.nextSequnceNum(), dealId));
					if (ArticleExchangeManager.logger.isWarnEnabled()) {
						ArticleExchangeManager.logger.warn("[关闭交换] [" + player.getJiazuLogString() + "] [" + dealId + "]");
					}
				} catch (Exception e) {
					ArticleExchangeManager.logger.error("[关闭交换异常] [" + player.getJiazuLogString() + "]", e);
				}
			} else {
				try {
					playerB = pm.getPlayer(ed.getPlayerAId());
					playerB.addMessageToRightBag(new CLOSE_DEAL_RES(GameMessageFactory.nextSequnceNum(), dealId));
					if (ArticleExchangeManager.logger.isWarnEnabled()) {
						ArticleExchangeManager.logger.warn("[关闭交换] [" + player.getJiazuLogString() + "] [" + dealId + "]");
					}
				} catch (Exception e) {
					ArticleExchangeManager.logger.error("[关闭交换异常] [" + player.getJiazuLogString() + "] [" + dealId + "]", e);
				}
			}
		}
		return null;
	}

	/**
	 * 被召回人的操作
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_DRINKING_FRIEND_DO_REQ(Connection conn, RequestMessage message, Player player) {
		DRINKING_FRIEND_DO_REQ req = (DRINKING_FRIEND_DO_REQ)message;
		byte type = req.getDoType();
		byte activityType =req.getActivityType();
		FateManager fm = FateManager.getInstance();
		PlayerManager pm = PlayerManager.getInstance();
		FateActivity fa = fm.getFateActivity(player,activityType);
		if(fa!=null){
			Player p2 = null;
			try {
				boolean isonline = GamePlayerManager.getInstance().isOnline(fa.getInvitedId());
				if(!isonline){
					player.sendError(对方不在线);
					return null;
				}
				p2 = pm.getPlayer(fa.getInviteId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(p2!=null){
				switch (type) {
				case 0:
					//不论是国内，国外的仙缘论道，都立即前去召唤人所在国家的凤栖梧桐下,召唤人能用召唤功能
					int country = p2.getTransferGameCountry();
					player.setTransferGameCountry(country);
					Game game = player.getCurrentGame();
					if(game!=null){
						String name = TransportData.getXinShouCunMap(player.getCountry());
						game.transferGame(player, new TransportData(0, 0, 0, 0, name, TransportData.getFateXY(player.getCountry())[0], TransportData.getFateXY(player.getCountry())[1]));
					}
					break;
				case 1:
					player.sendError(Translate.取消召唤);
					if(p2.isOnline()){
						String msg = Translate.translateString(Translate.xx_拒绝了您的召唤, new String[][]{{Translate.STRING_1,player.getName()}});
						p2.sendError(msg);
					}
					break;
				case 2:
					if(p2.isOnline()){
						String msg = Translate.translateString(Translate.请稍等, new String[][]{{Translate.STRING_1,player.getName()}});
						p2.sendError(msg);
					}
					break;	
				}
			}
		}
		
		return null;
	}
	/**
	 * 召唤仙友
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public static String testname = Translate.仙缘论道令;
	
	public ResponseMessage handle_CALL_FRIEND_DRINKING_REQ(Connection conn, RequestMessage message, Player player) {
		
		//是否有消耗品
		if(!player.articleIsExist(testname)){
			String msg = Translate.translateString(Translate.物品不存在提示, new String[][]{{Translate.STRING_1,testname}});
			player.sendError(msg);
			return null;
		}
		CALL_FRIEND_DRINKING_REQ req = (CALL_FRIEND_DRINKING_REQ)message;
		byte activityType = req.getActivityType();
		FateManager fm = FateManager.getInstance();
		FateActivity fa = fm.getFateActivity(player, activityType);
		PlayerManager pm = PlayerManager.getInstance();
		Player p2 = null;
		try {
			boolean isonline = GamePlayerManager.getInstance().isOnline(fa.getInvitedId());
			if(!isonline){
				player.sendError(对方不在线);
				return null;
			}
			p2 = pm.getPlayer(fa.getInvitedId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		if(p2!=null && p2.isInPrison()){
			player.sendError(Translate.仙友在监狱);
			return null;
		}
		
		DownCityManager dcm = DownCityManager.getInstance();
		if(dcm != null && dcm.isDownCityByName(p2.getGame())){
			player.sendError(Translate.仙友在副本);
			return null;
		}
		
		//被召唤的玩家在千层塔内
		String mapname = p2.getCurrentGame().gi.name;
		if(mapname.contains("qiancengta")){
			player.sendError(Translate.仙友在千层塔);
			return null;
		}
		
		if(p2.isInBattleField()){
			player.sendError(Translate.仙友在战场);
			return null;
		}
		
		DevilSquareManager inst = DevilSquareManager.instance;
		if(inst.isPlayerInDevilSquare(p2)) {
			player.sendError(Translate.仙友在副本);
			return null;
		}
		String gr = GlobalTool.verifyTransByOther(p2.getId());
		if(gr != null) {
			player.sendError(gr);
			return null;
		}
		
		int 传送消耗数量 = 1;
		byte fatetype = fa.getTemplate().getType();
		if(fatetype == 1 || fatetype == 3){
			传送消耗数量 = 2;
		}
		
		int 玩家拥有的数量 = player.getKnapsack_common().countArticle(testname);
		if(传送消耗数量>玩家拥有的数量){
			player.sendError(Translate.仙缘论道令数量不足);
			return null;
		}
		
		String targeReginName = fa.getTemplate().getRegionName().trim();
		boolean isInArea = false;
		String Areamap = player.getCurrentMapAreaName();
		if(Areamap != null && Areamap.equals(targeReginName)){
			isInArea = true;
		}
		if(!isInArea){
			String[] areamaps = player.getCurrentMapAreaNames();
			if(areamaps != null){
				for(String st : areamaps){
					if(st.equals(targeReginName)){
						isInArea = true;
						break;
					}
				}
			}
		}
		//召唤人不在凤栖梧桐下
		if(!isInArea){
			player.sendError(Translate.不在凤栖梧桐区域_无法召唤仙友);
			return null;
		}else{
			Game game = player.getCurrentGame();
			int 仙缘论道任务的国家 = fa.getCountry();//0-中立 1昆仑,2九州,3万法
			if (game != null && game.gi != null) {
				if (game.country != 仙缘论道任务的国家) {
					player.sendError(Translate.必须在指定的国家做);
					return null;
				}
			}
		}
		
		{//被召唤人在指定任务的凤栖梧桐下
			try {
				Game game = p2.getCurrentGame();
				if(p2.isOnline()){
					boolean p2IsInArea = false;
					String map2 = p2.getCurrentMapAreaName();
					String[] maps2 = p2.getCurrentMapAreaNames();
					String targeReginName2 = fa.getTemplate().getRegionName().trim();
					if(map2 != null && map2.equals(targeReginName2)){
						p2IsInArea = true;
					}
					if(!p2IsInArea){
						if(maps2 != null){
							for(String st : maps2){
								if(st.equals(targeReginName2)){
									p2IsInArea = true;
									break;
								}
							}
						}
					}
					if(p2IsInArea){
						if(fa.getCountry()==game.country){
							player.sendError(Translate.仙友_在凤栖梧桐区域);
							return null;
						}
					}
					String msg = Translate.translateString(Translate.仙友召唤您, new String[][]{{Translate.STRING_1,player.getName()}});
					for(int i =0;i<传送消耗数量;i++){
						player.removeArticle(testname);
					}
					String mes = Translate.translateString(Translate.您消耗了几个仙缘论道令, new String[][] {{Translate.STRING_1, 传送消耗数量+""}});
					player.sendError(mes);
					CALL_FRIEND_DRINKING_RES res = new CALL_FRIEND_DRINKING_RES(req.getSequnceNum(), msg, activityType);
					p2.addMessageToRightBag(res);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// 报名参加活动
	public ResponseMessage handle_SIGNUP_ACTIVITY_REQ(Connection conn, RequestMessage message, Player player) {
		SIGNUP_ACTIVITY_REQ req = (SIGNUP_ACTIVITY_REQ) message;
		int activityId = req.getActivityId();
		Activity activity = ActivityNoticeManager.getInstance().getActivityById(activityId);
		if (activity == null) {
			return new SIGNUP_ACTIVITY_RES(message.getSequnceNum(), 1, "无效的活动");
		}
		CompoundReturn signup = activity.signup(player);
		return new SIGNUP_ACTIVITY_RES(message.getSequnceNum(), signup.getBooleanValue() ? 0 : 1, signup.getBooleanValue() ? "" : signup.getStringValue());
	}

	/******************************************* 以下是感恩节活动 **********************************************/

	/** 感恩节活动获得奖励的名单<日期,List<获得奖励的名单>> */
	private static Map<String, List<Long>> thinksgivingPrizeMap = new HashMap<String, List<Long>>();

	public static List<ThanksgivingChatActivity> chatActivities = new ArrayList<ThanksgivingChatActivity>();

	static String thinksgivingLog = "[感恩节聊天感恩]";

	static {
		chatActivities.add(new ThanksgivingChatActivity("2012-11-21 12:00:00", "2012-11-21 12:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-21 13:00:00", "2012-11-21 13:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-21 13:40:00", "2012-11-21 13:59:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-21 14:00:00", "2012-11-21 14:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-21 14:30:00", "2012-11-21 14:46:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-21 15:00:00", "2012-11-21 15:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-21 15:30:00", "2012-11-21 15:46:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));

		// chatActivities.add(new ThanksgivingChatActivity("2012-11-22 10:25:00", "2012-11-22 10:40:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉","火鸡肉" }));

		chatActivities.add(new ThanksgivingChatActivity("2012-11-22 12:00:00", "2012-11-22 12:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-23 12:00:00", "2012-11-23 12:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-24 12:00:00", "2012-11-24 12:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-25 12:00:00", "2012-11-25 12:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-26 12:00:00", "2012-11-26 12:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));

		chatActivities.add(new ThanksgivingChatActivity("2012-11-22 20:00:00", "2012-11-22 20:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-23 20:00:00", "2012-11-23 20:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-24 20:00:00", "2012-11-24 20:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-25 20:00:00", "2012-11-25 20:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-26 20:00:00", "2012-11-26 20:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));

		chatActivities.add(new ThanksgivingChatActivity("2012-11-22 22:00:00", "2012-11-22 22:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-23 22:00:00", "2012-11-23 22:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-24 22:00:00", "2012-11-24 22:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-25 22:00:00", "2012-11-25 22:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));
		chatActivities.add(new ThanksgivingChatActivity("2012-11-26 22:00:00", "2012-11-26 22:16:00", new String[] { "感谢", "感恩" }, new String[] { "火鸡肉", "火鸡肉" }));

	}
	public static boolean thinksgivingOpen = true;

	// 感恩节活动
	static class ThanksgivingChatActivity {
		long startTime;
		long endTime;
		String[] chatContents;
		String[] prizes;

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

		public String[] getChatContents() {
			return chatContents;
		}

		public void setChatContents(String[] chatContents) {
			this.chatContents = chatContents;
		}

		public String[] getPrizes() {
			return prizes;
		}

		public void setPrizes(String[] prizes) {
			this.prizes = prizes;
		}

		public ThanksgivingChatActivity(String startTimeStr, String endTimeStr, String[] chatContents, String[] prizes) {
			super();
			this.startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
			this.endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
			this.chatContents = chatContents;
			this.prizes = prizes;
		}

		private boolean timeFit(long now) {
			return startTime <= now && now <= endTime;
		}

		private CompoundReturn contentFilter(String content) {
			CompoundReturn cr = CompoundReturn.createCompoundReturn();
			if (chatContents == null || prizes == null || content == null) {
				return cr;
			}
			for (int i = 0; i < chatContents.length; i++) {
				String chatCString = chatContents[i];
				if (content.contains(chatCString)) {
					return cr.setBooleanValue(true).setIntValue(i);
				}
			}
			return cr;
		}

	}

	/**
	 * 感恩节喊话活动
	 * @param player
	 * @param content
	 */
	public static void thinkgivingChatCheckAndPrize(Player player, String content) {
		long now = SystemTime.currentTimeMillis();
		if (thinksgivingOpen) {
			String time = TimeTool.formatter.varChar10.format(now);
			if (!thinksgivingPrizeMap.containsKey(time)) {
				synchronized (thinksgivingPrizeMap) {
					if (!thinksgivingPrizeMap.containsKey(time)) {
						thinksgivingPrizeMap.put(time, new ArrayList<Long>());
						logger.warn(thinksgivingLog + " [新增一天的数据:" + time + "]");
					}
				}
			}
			List<Long> playerIdList = thinksgivingPrizeMap.get(time);
			if (!playerIdList.contains(player.getId())) {
				// 奖励列表里没有.看看符不符合奖励条件
				for (ThanksgivingChatActivity thanksgivingChatActivity : chatActivities) {
					if (thanksgivingChatActivity.timeFit(now)) {
						CompoundReturn contentFilter = thanksgivingChatActivity.contentFilter(content);
						if (contentFilter.getBooleanValue()) {
							int prizeIndex = contentFilter.getIntValue();
							String prizeName = thanksgivingChatActivity.prizes[prizeIndex];
							Article article = ArticleManager.getInstance().getArticle(prizeName);
							if (article == null) {
								logger.error(thinksgivingLog + " [" + player.getLogString() + "] [触发获得奖励,但是物品不存在:" + prizeName + "]");
								return;
							}
							try {
								if (!playerIdList.contains(player.getId())) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.运营发放活动奖励, player, 4, 2, true);
									if (ae != null) {
										long mailId = MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 2 }, "感恩有礼，飘渺寻仙曲有你更精彩！", "亲爱的玩家，感谢您一路的陪伴和支持。在感恩节到来之际，特赠送感恩节“火鸡肉”道具，请您在附件查收。", 0L, 0L, 0L, "感恩节喊话");
										playerIdList.add(player.getId());
										logger.warn(thinksgivingLog + "[" + player.getLogString() + "] [奖励成功] [获得道具奖励:" + prizeName + "] [邮件Id:" + mailId + "] [喊话:" + content + "]");

									} else {
										logger.error(thinksgivingLog + " [" + player.getLogString() + "] [奖励物品:" + prizeName + "] [创建物品null]");
									}
								}
							} catch (Exception e) {
								logger.error(thinksgivingLog + " [" + player.getLogString() + "] [奖励物品:" + prizeName + "] [异常]", e);
							}
							break;
						} else {
							logger.warn(thinksgivingLog + " [" + player.getLogString() + "] [时间符合,内容不符合:" + content + "]");
						}
					}
				}
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn(thinksgivingLog + " [" + player.getLogString() + "] [失败] [喊话已经领取了奖励]");
				}
			}
		}
	}

	public static void main(String[] args) {
		String date = "2012-11-21 10:00:00";
		System.out.println(TimeTool.formatter.varChar19.parse(date));
	}
}