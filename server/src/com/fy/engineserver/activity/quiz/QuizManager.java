package com.fy.engineserver.activity.quiz;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;
import static com.fy.engineserver.datasource.language.Translate.translateString;
import static com.fy.engineserver.datasource.language.Translate.分别获得宝石袋奖励;
import static com.fy.engineserver.datasource.language.Translate.恭贺答题前三甲;
import static com.fy.engineserver.datasource.language.Translate.探花;
import static com.fy.engineserver.datasource.language.Translate.放大器最多只能使用xx;
import static com.fy.engineserver.datasource.language.Translate.本次答题结束;
import static com.fy.engineserver.datasource.language.Translate.本题答题时间结束;
import static com.fy.engineserver.datasource.language.Translate.榜眼;
import static com.fy.engineserver.datasource.language.Translate.求助最多只能使用xx;
import static com.fy.engineserver.datasource.language.Translate.状元;
import static com.fy.engineserver.datasource.language.Translate.福星宝石袋1级;
import static com.fy.engineserver.datasource.language.Translate.福星宝石袋2级;
import static com.fy.engineserver.datasource.language.Translate.福星宝石袋3级;
import static com.fy.engineserver.datasource.language.Translate.第一名不能使用求助;
import static com.fy.engineserver.datasource.language.Translate.答题活动已经完成;
import static com.fy.engineserver.datasource.language.Translate.答题等待描述;
import static com.fy.engineserver.datasource.language.Translate.获得文采值xx;
import static com.fy.engineserver.datasource.language.Translate.首轮不能使用求助;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.boss.authorize.model.Passport;
import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.AllActivityManager;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.extraquiz.RewardArticles;
import com.fy.engineserver.activity.notice.ActivityNoticeManager;
import com.fy.engineserver.activity.notice.ActivityNoticeManager.Activity;
import com.fy.engineserver.activity.quiz.AnswerRecord.RecordComparator;
import com.fy.engineserver.activity.shop.ActivityProp;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.hotspot.Hotspot;
import com.fy.engineserver.hotspot.HotspotManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.quizActivity.Option_QuizActivity_Agree;
import com.fy.engineserver.menu.quizActivity.Option_QuizActivity_DisAgree;
import com.fy.engineserver.message.ANSWER_QUIZ_CANCEL_RES;
import com.fy.engineserver.message.ANSWER_QUIZ_FINISH_RES;
import com.fy.engineserver.message.ANSWER_USE_PROPS_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.POP_WAIT_TIME_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.CompoundReturn;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.Utils;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.AcceptHuoDonginfoFlow;
import com.sqage.stat.model.FinishHuoDonginfoFlow;
import com.xuanzhi.tools.transport.ResponseMessage;

public class QuizManager implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(QuizManager.class.getName());
	public static RecordComparator comparator = new AnswerRecord.RecordComparator();
	public final static int LV = 15;
	public static int beginHour1 = 10;
	public static int beginMinute1 = 0;
	public static int beginHour2 = 17;
	public static int beginMinute2 = 0;
	public final static int SUBJECTNUM = 20;
	public static int MAXSCORE = 20;
	public final static int BRANCHNUM = 4;
	public final static int ANSWER_INTERVAL = 25 * 1000;// 答题间隔
	public final static int ANSWER_INTERVAL_second = 25;// 答题间隔
	public final static int WAIT_CHECK_INTERVAL = 5 * 1000;// 答题间隔
	public final static int WAIT_CHECK_INTERVAL_second = 5;// 答题间隔
	public final static long 多长时间后开始 = 60 * 1000;
	public final static int 多长时间后开始分钟 = 60;
	// public final static int 多长时间后开始分钟 = 5;
	public final static int TOP20 = 20;
	
	public int[] quizExp = null;
	public String configFile = "";
	public String configFileExp = "";
	public Random random = new Random();

	private PlayerManager playerManager = PlayerManager.getInstance();
	private transient Quiz qz;
	private static QuizManager quizManager = null;

	public static String WAIT_DES = 答题等待描述;

	public int 今天的第几次答题 = 0;
	// 同步同意(不)答題的人
	public Object syn = new Object();
	
	public int getSubjectNum() {
		if (qz == null || qz.getQuizNum() <= 0) {
			return SUBJECTNUM;
		}
		return qz.getQuizNum();
	}
	public int getMaxScore() {
		if (qz == null || qz.getQuizNum() <= 0) {
			return MAXSCORE;
		}
		return qz.getQuizNum();
	}

	public void init() throws Exception {
		

		loadExp();

		Quiz qz = null;

		if (qz != null) {
			this.clear();
		}
		if (qz != null && qz.getQs() != QuizState.完成) {
			// 判断qz的时间
			if (qz.getEndTime() >= SystemTime.currentTimeMillis()) {
				qz = null;
			}
		} else {
			qz = null;
		}
		quizManager = this;
//		Thread t = new Thread(this, "QuizManager");
//		t.start();
		if (logger.isInfoEnabled()) logger.info("quizManager 初始化成功");
		ServiceStartRecord.startLog(this);
	}

	private void loadExp() throws Exception {
		File file = new File(configFileExp);
		HSSFWorkbook workbook = null;

		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		workbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = null;
		sheet = workbook.getSheetAt(0);
		if (sheet == null) return;
		int rows = sheet.getPhysicalNumberOfRows();
		HSSFRow row = null;
		HSSFCell cell = null;
		quizExp = new int[rows];
		for (int i = 1; i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {

				cell = row.getCell(0);
				int level = (int) cell.getNumericCellValue();
				cell = row.getCell(1);
				int exp = (int) cell.getNumericCellValue();
				quizExp[level] = exp;
			}

		}
		if (QuizManager.logger.isWarnEnabled()) {
			QuizManager.logger.warn("答题经验生成成功");
		}

	}

	public static QuizManager getInstance() {
		return quizManager;
	}

	public void destroy() {
		Quiz qu = null;
		try {
			// qz = quizDAO.getEntity();
		} catch (Exception e) {
			if (logger.isWarnEnabled()) logger.warn("初始化quizmanager", e);
		}
		if (qu != null) {
			this.clear();
		}
		if (qz != null && qz.getQs() != QuizState.完成) {
			// 保存
			// quizDAO.saveEntity(qz);
		}
	}

	// 删除库中的数据
	private void clear() {
		// quizDAO.removeEntity();
	}

	// 设置今天开不开答题
	public boolean open = true;

	public void run() {

		while (open) {

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (!com.fy.engineserver.util.ContextLoadFinishedListener.isLoadFinished()) {
				continue;
			}
			Calendar currentCalender = Calendar.getInstance();
			try {
				if (qz == null) {
					try {
						Thread.sleep(2 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					int currentHour = currentCalender.get(Calendar.HOUR_OF_DAY);

					if (currentHour == beginHour1) {
						int currentMinute = currentCalender.get(Calendar.MINUTE);
						if (currentMinute - beginMinute1 <= 2 && currentMinute - beginMinute1 >= 0) {
							// 生成题，通知在线玩家
							qz = this.load();
							qz.setQs(QuizState.开始);
							qz.setRunningTime(SystemTime.currentTimeMillis() + 多长时间后开始);
							qz.setEndTime(SystemTime.currentTimeMillis() + 多长时间后开始 + (ANSWER_INTERVAL + WAIT_CHECK_INTERVAL) * 20);
							qz.setQuizNum(-1);			//正常答题没有额外奖励，题目数量也不改变
							qz.setExtraRewards(null);
							qz.setExtraNeedScore(-1);

							qz.setBeginTime(currentCalender.get(Calendar.YEAR) + Translate.年 + (currentCalender.get(Calendar.MONTH) + 1) + Translate.月 + currentCalender.get(Calendar.DAY_OF_MONTH) + Translate.日 + currentHour + Translate.时 + currentMinute + Translate.分);
							今天的第几次答题 = 0;
							logger.warn("[答题活动开始:] [" + beginHour1 + "小时] [" + beginMinute1 + "分钟] [现在:] [" + currentHour + "小时] [" + currentMinute + "分钟]");

						}
					} else if (currentHour == beginHour2) {
						int currentMinute = currentCalender.get(Calendar.MINUTE);
						if (currentMinute - beginMinute2 <= 2 && currentMinute - beginMinute2 >= 0) {
							qz = this.load();
							qz.setQs(QuizState.开始);
							qz.setRunningTime(SystemTime.currentTimeMillis() + 多长时间后开始);
							qz.setEndTime(SystemTime.currentTimeMillis() + 多长时间后开始 + (ANSWER_INTERVAL + WAIT_CHECK_INTERVAL) * 20);

							qz.setBeginTime(currentCalender.get(Calendar.YEAR) + Translate.年 + (currentCalender.get(Calendar.MONTH) + 1) + Translate.月 + currentCalender.get(Calendar.DAY_OF_MONTH) + Translate.日 + currentHour + Translate.时 + currentMinute + Translate.分);
							qz.setQuizNum(-1);			//正常答题没有额外奖励，题目数量也不改变
							qz.setExtraRewards(null);
							qz.setExtraNeedScore(-1);

							今天的第几次答题 = 1;
							logger.warn("[答题活动开始:] [" + beginHour2 + "小时] [" + beginMinute2 + "分钟] [现在:] [" + currentHour + "小时] [" + currentMinute + "分钟]");

						}
					} else {				//2015年1月15日10:10:23    答题额外开启活动
						try {
							int currentMinute = currentCalender.get(Calendar.MINUTE);
							CompoundReturn cr = AllActivityManager.instance.notifySthHappened(AllActivityManager.datiActivity, currentHour, currentMinute);
							if (cr != null && cr.getBooleanValue()) {
								qz = this.load();
								qz.setQs(QuizState.开始);
								qz.setRunningTime(SystemTime.currentTimeMillis() + 多长时间后开始);
								qz.setEndTime(SystemTime.currentTimeMillis() + 多长时间后开始 + (ANSWER_INTERVAL + WAIT_CHECK_INTERVAL) * 20);
	
								qz.setBeginTime(currentCalender.get(Calendar.YEAR) + Translate.年 + (currentCalender.get(Calendar.MONTH) + 1) + Translate.月 + currentCalender.get(Calendar.DAY_OF_MONTH) + Translate.日 + currentHour + Translate.时 + currentMinute + Translate.分);
								qz.setQuizNum(cr.getIntValue());			//正常答题没有额外奖励，题目数量也不改变
								qz.setExtraRewards((RewardArticles) cr.getObjValue());
								qz.setExtraNeedScore((int) cr.getLongValue());
	
								今天的第几次答题 = 2;
								logger.warn("[额外答题活动开启] [题目数量:" + qz.getQuizNum() + "] [额外奖励需要积分:" + qz.getExtraNeedScore() + "] [额外奖励:" + qz.getExtraRewards() + "]");
							}
						} catch (Exception e) {
							ActivitySubSystem.logger.warn("[额外答题活动]", e);
						}
					}
				} else {
					if (qz.getQs() == QuizState.开始) {
						// 通知玩家
						qz.setQs(QuizState.通知);
						quizBeginNotice();

					} else if (qz.getQs() == QuizState.通知) {
						// 隔多长时间开始
						long now = SystemTime.currentTimeMillis();
						if (now >= qz.getRunningTime()) {
							qz.setLastUpdateTime(now);
							qz.setQs(QuizState.运行);
							List<Subject> list = qz.getSubjectList();
							if (list != null) {
								Subject subject = list.get(0);
								qz.returnSubject(subject);
							}
							if (logger.isDebugEnabled()) {
								logger.debug("通知状态");
							}
						}
					} else if (qz.getQs() == QuizState.运行 || qz.getQs() == QuizState.答题后) {
						// 运行
						qz.heartBeat();
					} else if (qz.getQs() == QuizState.完成) {
						// 奖励
						try {
							settleAll();
							ActivityNoticeManager.getInstance().activityEnd(Activity.答题, GamePlayerManager.getInstance().getOnlinePlayers());

						} catch (Exception e) {
							logger.error("[答题活动最后结算异常]", e);
						}
						qz = null;
					}
				}
			} catch (Throwable t) {
				logger.error("[答题心跳异常]", t);
			}
		}

	}

	/**
	 * 答题时间到通知多久后开始答题
	 */
	private void quizBeginNotice() {
		Player[] ps = playerManager.getOnlinePlayers();
		ResponseMessage res = createMessage(多长时间后开始分钟, false);
		List<Player> players = new ArrayList<Player>();
		for (Player player : ps) {
			synchronized (syn) {
				if (player.getLevel() >= LV) {
					if (!qz.getAgreeQuizPlayers().contains(player.getId())) {
						player.addMessageToRightBag(res);
						players.add(player);
						if (logger.isWarnEnabled()) {
							logger.warn("[活动开始发送答题通知] [" + player.getLogString() + "]");
						}
					}
				} else {
					if (!qz.disAgreeQuizPlayers.contains(player.getId())) {
						qz.disAgreeQuizPlayers.add(player.getId());
						if (logger.isWarnEnabled()) {
							logger.warn("[活动开始发送答题通知] [答题等级不够] [" + player.getLogString() + "]");
						}
					}
				}
			}
		}
		ActivityNoticeManager.getInstance().activityStart(Activity.答题, players.toArray(new Player[0]));
	}

	public ResponseMessage createMessage(int beginTime, boolean minuteOrsecond) {

		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60 * 60);

		mw.setTitle(Translate.text_答题活动);
		String st = null;
		if (minuteOrsecond) {
			st = Translate.translateString(Translate.text_你是否接受答题活动, new String[][] { { Translate.STRING_1, String.valueOf(beginTime) } });
		} else {
			st = Translate.translateString(Translate.text_你是否接受答题活动秒开始, new String[][] { { Translate.STRING_1, String.valueOf(beginTime) } });
		}
		mw.setDescriptionInUUB(st);

		Option_QuizActivity_Agree agree = new Option_QuizActivity_Agree();
		agree.setText(Translate.text_答题接受);
		agree.setColor(0xffffff);

		Option_QuizActivity_DisAgree disagree = new Option_QuizActivity_DisAgree();
		disagree.setText(Translate.text_答题拒绝);
		disagree.setColor(0xffffff);

		mw.setOptions(new Option[] { agree, disagree });

		QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());

		return res;
	}

	/**
	 * 玩家同意答题
	 * @param player
	 */
	public void agreeQuiz(Player player) {
		if (qz != null) {
			synchronized (syn) {

				if (qz.getQs().type == QuizState.完成.type) {
					player.send_HINT_REQ(答题活动已经完成);
					return;
				}

				List<Long> list = qz.getAgreeQuizPlayers();
				if (!list.contains(player.getId())) {
					list.add(player.getId());

					if (qz.getQuizRecord().get(player.getId()) == null) {
						AnswerRecord ar = new AnswerRecord();
						ar.setPlayerId(player.getId());
						qz.getQuizRecord().put(player.getId(), ar);
					}
					HotspotManager.getInstance().overHotspot(player, Hotspot.OVERTYPE_DATI, "答题");
					int time = (int) ((qz.getRunningTime() - SystemTime.currentTimeMillis()) / 1000);
					if (logger.isWarnEnabled()) {
						logger.warn("[同意答题时间] [" + player.getLogString() + "] [时间:" + time + "]");
					}
					if (time > 1) {
						POP_WAIT_TIME_RES res = new POP_WAIT_TIME_RES(GameMessageFactory.nextSequnceNum(), time, QuizManager.WAIT_DES);
						player.addMessageToRightBag(res);

					} else if (time > 0) {
						POP_WAIT_TIME_RES res = new POP_WAIT_TIME_RES(GameMessageFactory.nextSequnceNum(), 1, QuizManager.WAIT_DES);
						player.addMessageToRightBag(res);
					} else {

						int tempTime = (int) ((SystemTime.currentTimeMillis() - qz.getRunningTime()) / 1000);
						if (tempTime > 1) {
							// 0开始
							int num = qz.getNextNum();
							int timeInterval = (num + 1) * (ANSWER_INTERVAL_second + WAIT_CHECK_INTERVAL_second);

							if (timeInterval - tempTime > 1) {
								POP_WAIT_TIME_RES res = new POP_WAIT_TIME_RES(GameMessageFactory.nextSequnceNum(), timeInterval - tempTime, QuizManager.WAIT_DES);
								player.addMessageToRightBag(res);
							} else {
								POP_WAIT_TIME_RES res = new POP_WAIT_TIME_RES(GameMessageFactory.nextSequnceNum(), 1, QuizManager.WAIT_DES);
								player.addMessageToRightBag(res);
							}

						} else {
							POP_WAIT_TIME_RES res = new POP_WAIT_TIME_RES(GameMessageFactory.nextSequnceNum(), 1, QuizManager.WAIT_DES);
							player.addMessageToRightBag(res);
						}
						if (logger.isWarnEnabled()) {
							logger.warn("[同意答题时间] [" + player.getLogString() + "] [tempTime:" + tempTime + "]");
						}

					}

					// 活跃度统计
					ActivenessManager.getInstance().record(player, ActivenessType.答题);
					player.send_HINT_REQ(Translate.你同意了此次答题活动);

					// 设置玩家答题记录
					long now = SystemTime.currentTimeMillis();
					long lastAnaswerTime = player.getLastAnswerQuizTime();
					player.setLastAnswerQuizTime(lastAnaswerTime);
					boolean same = Utils.checkSameDay(now, lastAnaswerTime);
					try {
						boolean[] states = new boolean[] { false, false, false };
						if (same) {
							// 判断时间在那个区间内
							states = player.getTodayAnswerQuizState();
							if (states == null) {
								states = new boolean[] { false, false, false };
							}
						}
						if (states.length == 2) {
							states = Arrays.copyOf(states, states.length + 1);
							states[2] = false;
						}
						states[今天的第几次答题] = true;
						player.setTodayAnswerQuizState(states);
						QuizManager.logger.warn("[设置今天答题状态] [" + player.getLogString() + "]");
					} catch (Exception e) {
						
					}

					// // ///////////接受活动 start/////////////////////////////
					StatClientService statClientService = StatClientService.getInstance();
					AcceptHuoDonginfoFlow acceptHuoDonginfoFlow = new AcceptHuoDonginfoFlow();

					Passport pp = player.getPassport();
					if (pp != null) {
						acceptHuoDonginfoFlow.setJixing(pp.getRegisterMobileOs());
					}

					acceptHuoDonginfoFlow.setCreateDate(System.currentTimeMillis());
					acceptHuoDonginfoFlow.setFenQu(com.xuanzhi.boss.game.GameConstants.getInstance().getServerName());
					acceptHuoDonginfoFlow.setGameLevel("" + player.getLevel());
					acceptHuoDonginfoFlow.setId(12345L);

					acceptHuoDonginfoFlow.setTaskName("答题");
					acceptHuoDonginfoFlow.setTaskType("答题");
					acceptHuoDonginfoFlow.setUserName(player.getUsername());
					if (!TestServerConfigManager.isTestServer()) {
						statClientService.sendAcceptHuoDonginfoFlow("", acceptHuoDonginfoFlow);
					}

					// if(logger.isDebugEnabled()){
					// logger.debug("[同意答题] ["+player.getLogString()+"] []");
					// }
				}
			}
		} else {
			player.sendError(Translate.text_答题活动已经结束);
			if (logger.isWarnEnabled()) logger.warn("[同意答题错误] [还没开始] [" + player.getLogString() + "]");
		}
	}

	/**
	 * 玩家不同意答题
	 * @param player
	 */
	public void disAgreeQuiz(Player player) {
		if (qz != null) {
			synchronized (syn) {
				List<Long> list = qz.getAgreeQuizPlayers();
				if (list.contains(player.getId())) {
					list.remove(player.getId());
					this.getQz().disAgreeQuizPlayers.add(player.getId());
				} else {
					this.getQz().disAgreeQuizPlayers.add(player.getId());
				}
				player.addMessageToRightBag(new ANSWER_QUIZ_CANCEL_RES(GameMessageFactory.nextSequnceNum()));
				player.send_HINT_REQ(Translate.你拒绝了此次答题活动);
				if (logger.isWarnEnabled()) {
					logger.warn("[不同意答题] [" + player.getLogString() + "] []");
				}
			}
		} else {
			player.addMessageToRightBag(new ANSWER_QUIZ_CANCEL_RES(GameMessageFactory.nextSequnceNum()));
			if (logger.isWarnEnabled()) logger.warn("[不同意答题错误] [还没开始] [" + player.getLogString() + "]");

		}
	}

	public boolean answerQuiz(byte answerType, byte answerKey, Player player) {

		long now = SystemTime.currentTimeMillis();
		if (qz == null) {
			player.send_HINT_REQ(本次答题结束);

			// 没有答题关闭答题框
			ANSWER_QUIZ_FINISH_RES res = new ANSWER_QUIZ_FINISH_RES(GameMessageFactory.nextSequnceNum());
			player.addMessageToRightBag(res);
			if (logger.isWarnEnabled()) {
				logger.warn("[答题错误] [已经结束] [" + player.getLogString() + "]");
			}
			return false;
		}

		if (now - qz.getLastUpdateTime() >= (ANSWER_INTERVAL - 500)) {
			player.send_HINT_REQ(本题答题时间结束);
			return false;
		}

		int num = this.qz.getNextNum();
		if (logger.isWarnEnabled()) {
			logger.warn("[答题类型] [" + answerType + "] [num:" + qz.getNextNum() + "]");
		}
		if (answerType == 0) {
			// 答题
			return this.answer(num, answerKey, player);
		} else if (answerType == 1) {
			return this.helpAnswer(num, player);
			// help
		} else if (answerType == 2) {
			//
			return this.amplifierAnswer(num, player);
		}
		return false;
	}

	// 最后结算，给文采值，给经验值
	public void settleAll() {
		if (qz != null) {
			Player player = null;

			synchronized (syn) {
				qz.getSubjectList().clear();
				Map<Long, AnswerRecord> map = qz.getQuizRecord();

				for (Entry<Long, AnswerRecord> en : map.entrySet()) {

					try {
						player = playerManager.getPlayer(en.getKey());
						if (en.getValue().getScore() > 0) {
							long addExp = quizExp[player.getLevel()] * en.getValue().getScore();
							addExp *= ActivityManager.getInstance().getDatiExpRateFormActivity();
							player.addExp(addExp, ExperienceManager.ADDEXP_REASON_QUIZACTIVITY);
							try {
								if (qz.getExtraNeedScore() > 0 && en.getValue().getScore() >= qz.getExtraNeedScore()) {
									if (qz.getExtraRewards() != null && qz.getExtraRewards().getArticleCNNames() != null) {
										List<Player> players = new ArrayList<Player>();
										players.add(player);
										ActivityProp[] props = new ActivityProp[qz.getExtraRewards().getArticleCNNames().length];
										for (int kk=0; kk<qz.getExtraRewards().getArticleCNNames().length; kk++) {
											Article a = ArticleManager.getInstance().getArticleByCNname(qz.getExtraRewards().getArticleCNNames()[kk]);
											props[kk] = new ActivityProp(qz.getExtraRewards().getArticleCNNames()[kk], a.getColorType(), qz.getExtraRewards().getArticleNums()[kk], 
													true);
										}
										ActivityManager.sendMailForActivity(players, props,Translate.text_答题奖励, String.format(Translate.恭喜你获得答题额外奖励, en.getValue().getScore() + ""), "额外答题活动");
										if (logger.isWarnEnabled()) {
											logger.warn("[额外答题活动发奖励] [成功] [" + player.getLogString() + "] [" + qz.getExtraRewards() + "]");
										}	
									}
								}
							} catch (Exception e) {
								ActivitySubSystem.logger.warn("[额外答题活动奖励] [" + player.getLogString() + "] [得分:" + en.getValue().getScore() + "]", e);
							}
						}
						if (player.isOnline()) {
							// player.send_HINT_REQ("获得文采值"+en.getValue().getScore());
							player.send_HINT_REQ(translateString(获得文采值xx, new String[][] { { STRING_1, en.getValue().getScore() + "" } }));

//							AchievementManager.getInstance().record(player, RecordAction.完成答题次数);

							
							if (en.getValue().getScore() == getMaxScore()) {
//							if (en.getValue().getScore() == MAXSCORE) {
								AchievementManager.getInstance().record(player, RecordAction.答题全部正确次数);
							}
							if (AchievementManager.logger.isWarnEnabled()) {
								AchievementManager.logger.warn("[答题完成统计成就] [" + player.getLogString() + "]");
							}

						}

						StatClientService statClientService = StatClientService.getInstance();
						// /////////////////完成活动 start//////////////////////
						FinishHuoDonginfoFlow finishHuoDonginfoFlow = new FinishHuoDonginfoFlow();
						Passport pp = player.getPassport();
						if (pp != null) {
							finishHuoDonginfoFlow.setJixing(pp.getRegisterMobileOs());
						}
						finishHuoDonginfoFlow.setDate(System.currentTimeMillis());
						finishHuoDonginfoFlow.setFenQu(com.xuanzhi.boss.game.GameConstants.getInstance().getServerName());
						finishHuoDonginfoFlow.setGetDaoJu(0);
						finishHuoDonginfoFlow.setGetWuPin(0);
						finishHuoDonginfoFlow.setGetYOuXiBi(0);

						finishHuoDonginfoFlow.setId(12345L);
						finishHuoDonginfoFlow.setStatus("完成");
						finishHuoDonginfoFlow.setTaskName("答题");
						finishHuoDonginfoFlow.setUserName(player.getUsername());
						finishHuoDonginfoFlow.setAward(""); // / 如果有多个，用“+”分割 ，如: +南海玄晶+南海玄晶+南海玄晶+南海玄晶+南海玄晶+无字天书
						if (!TestServerConfigManager.isTestServer()) {
							statClientService.sendFinishHuoDonginfoFlow("", finishHuoDonginfoFlow);
						}
						// /////////////////完成活动 end//////////////////////

					} catch (Exception e) {
						logger.error("[答题最后结算] [id:" + en.getKey() + "]", e);
					}
				}
				qz.getQuizRecord().clear();

				List<Long> listAgree = qz.getAgreeQuizPlayers();
				for (long id : listAgree) {
					try {
						ANSWER_QUIZ_FINISH_RES res = new ANSWER_QUIZ_FINISH_RES(GameMessageFactory.nextSequnceNum());
						Player p = playerManager.getPlayer(id);
						if (p.isOnline()) {
							p.addMessageToRightBag(res);
						}
						if (logger.isWarnEnabled()) {
							logger.error("[答题最后结算发送关闭请求] [" + p.getLogString() + "]");
						}
					} catch (Exception e) {
						logger.error("[答题最后结算异常] [id:" + id + "]", e);
					}
				}
				qz.getAgreeQuizPlayers().clear();
			}
			boolean isExtraQuiz = qz.getExtraRewards() != null;

			if (QuizManager.logger.isWarnEnabled()) QuizManager.logger.warn("[本次答题完成] [是否为活动额外开启答题:" + isExtraQuiz + "]");

//			if (isExtraQuiz) {
//				return ;
//			}
			List<Entry<Long, AnswerRecord>> lastScoreList = qz.lastTopList;
			int i = 0;
			if (lastScoreList != null) {
				// 发送广播世界
				ChatMessageService cms = ChatMessageService.getInstance();

				ChatMessage msg = new ChatMessage();
				StringBuffer sb1 = new StringBuffer();
				String prizeSt = "<f color='" + ArticleManager.quiz_prize_color[0] + "'> " + 恭贺答题前三甲 + "</f>";
				sb1.append(prizeSt);
				String prizeS = 分别获得宝石袋奖励;
				for (Entry<Long, AnswerRecord> en : lastScoreList) {
					boolean b = playerManager.isOnline(en.getKey());
					// if(b){
					try {
						Player answer = playerManager.getPlayer(en.getKey());
						if (i < 3) {
							sb1.append(answer.getName());
							sb1.append("，");
							try {
								Article a = ArticleManager.getInstance().getArticle(答题三甲奖品[i]);
								if (a != null) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, false, 0, answer, a.getColorType(), 1, true);
									if (ae != null) {
										String mailContent = Translate.translateString(Translate.恭喜你在xx答题中获得的xx名次得到xx, new String[][] { { Translate.STRING_1, qz.getBeginTime() }, { Translate.COUNT_1, i + 1 + "" }, { Translate.ARTICLE_NAME_1, 答题三甲奖品[i] } });
										MailManager.getInstance().sendMail(en.getKey(), new ArticleEntity[] { ae }, Translate.text_答题奖励, mailContent, 0, 0, 0, "答题");
										if (logger.isWarnEnabled()) {
											logger.warn("[答题完成发奖励] [" + answer.getLogString() + "] [" + 答题三甲奖品[i] + "]");
										}
									} else {
										logger.error("[发送答题奖励错误] [" + answer.getLogString() + "] [奖励物品 null]");
									}
								} else {
									logger.error("[发送答题奖励错误] [" + answer.getLogString() + "] [没有奖励物品] [" + 答题三甲奖品[i] + "]");
								}
							} catch (Exception e) {
								logger.error("[发送答题奖励异常] [" + answer.getLogString() + "]", e);
							}

						} else {
							break;
						}
					} catch (Exception e) {
						logger.error("[答题完成发送前三名] [id:" + en.getKey() + "]", e);
					}
					++i;
					// }
				}

				sb1.append(prizeS);
				msg.setMessageText(sb1.toString());
				try {

					cms.sendMessageToSystem(msg);
					if (logger.isInfoEnabled()) {
						logger.info("[答题完成发送世界广播] [" + sb1.toString() + "]");
					}
					Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
					for (Player p : ps) {
						p.send_HINT_REQ(sb1.toString());
					}
					if (logger.isInfoEnabled()) {
						logger.info("[答题完成发送个人广播] [" + sb1.toString() + "]");
					}
				} catch (Exception e) {
					logger.error("[答题完成发送世界广播错误] [" + sb1.toString() + "]", e);
				}

				int num = 1;
				for (Entry<Long, AnswerRecord> en : lastScoreList) {
					if (en != null) {
						boolean b = playerManager.isOnline(en.getKey());
						if (b) {
							try {
								Player answer = playerManager.getPlayer(en.getKey());
								String des = Translate.translateString(Translate.本次答题结束您获得了第xx名, new String[][] { { Translate.COUNT_1, num + "" } });
								cms.sendMessageToPersonal(answer, des);
								if (logger.isDebugEnabled()) {
									logger.debug("[答题完成发送个人广播] [" + answer.getLogString() + "] [" + des + "] []");
								}
							} catch (Exception e) {
								logger.error("[答题完成发送个人广播异常] [id:" + en.getKey() + "] []", e);
							}

						}
					}
					num++;
				}

			} else {
				logger.warn("[答题完成发送奖励错误] [lastScoreList null]");
			}
		}

	}

	public static final String[] 答题三甲 = { 状元, 榜眼, 探花 };
	public static String[] 答题三甲奖品 = { 福星宝石袋3级, 福星宝石袋2级, 福星宝石袋1级 };

	public void enterNotice(Player player) {

		if (qz != null) {
			if (true) {
				return;
			}

			synchronized (syn) {

				if (player.getLevel() < LV) {
					if (!getQz().disAgreeQuizPlayers.contains(player.getId())) {
						getQz().disAgreeQuizPlayers.add(player.getId());
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[玩家登陆答题通知] [等级不够] [" + player.getLogString() + "]");
					}
					return;
				}

				ResponseMessage res = null;

				if (qz.getQs() == QuizState.通知) {
					if (!qz.getAgreeQuizPlayers().contains(player.getId()) && !qz.disAgreeQuizPlayers.contains(player.getId())) {
						// 通知
						int times = (int) ((qz.getRunningTime() - SystemTime.currentTimeMillis()) / 1000);
						if (times > 60) {
							times = times / 60;
							res = createMessage(times, true);
							if (logger.isWarnEnabled()) {
								logger.warn("[玩家登陆答题通知] [" + player.getLogString() + "] [通知] [true] [" + times + "]");
							}
						} else {
							res = createMessage(times, false);
							if (logger.isWarnEnabled()) {
								logger.warn("[玩家登陆答题通知] [" + player.getLogString() + "] [通知] [false] [" + times + "]");
							}
						}
					}
				} else if (qz.getQs() == QuizState.运行) {
					if (!qz.getAgreeQuizPlayers().contains(player.getId()) && !qz.disAgreeQuizPlayers.contains(player.getId())) {

						MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60);
						mw.setTitle(Translate.text_答题活动);
						mw.setDescriptionInUUB(Translate.text_你是否接受答题活动正在进行);

						Option_QuizActivity_Agree agree = new Option_QuizActivity_Agree();
						agree.setText(Translate.text_答题接受);
						agree.setColor(0xffffff);

						Option_QuizActivity_DisAgree disagree = new Option_QuizActivity_DisAgree();
						disagree.setText(Translate.text_答题拒绝);
						disagree.setColor(0xffffff);

						mw.setOptions(new Option[] { agree, disagree });
						res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());

						if (logger.isWarnEnabled()) {
							logger.warn("[玩家登陆答题通知] [" + player.getLogString() + "] [运行]");
						}
					}
				}
				if (res != null) {
					player.addMessageToRightBag(res);
				}
				if (logger.isWarnEnabled()) {
					logger.warn("[玩家登陆答题通知] [" + player.getLogString() + "]");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("[答题通知] [还不到答题时间] [" + player.getLogString() + "]");
			}
		}
	}

	/**
	 * 回答问题
	 * @param num
	 * @param qa
	 */
	public boolean answer(int num, byte result, Player player) {

		if (num > getSubjectNum()) {
//		if (num > SUBJECTNUM) {
			if (logger.isWarnEnabled()) logger.warn("[答题错误] [大于最大题目数] [" + num + "]");
		} else if (result > BRANCHNUM) {
			if (logger.isWarnEnabled()) logger.warn("[答题错误] [答案不符] [" + result + "]");
		} else if (qz != null) {
			List<Subject> list = qz.getSubjectList();
			if (list != null) {
				if (list.size() >= num) {

					Map<Byte, List<Long>> oneQuizMap = qz.getOneQuizRecord();
					List<Long> Playerlist = oneQuizMap.get(result);
					if (Playerlist != null) {
						if (!Playerlist.contains(player.getId())) {
							Playerlist.add(player.getId());
						} else {
							if (logger.isWarnEnabled()) logger.warn("[重复答题] [" + player.getLogString() + "] []");
							return false;
						}
					} else {
						Playerlist = new ArrayList<Long>();
						Playerlist.add(player.getId());
						oneQuizMap.put(result, Playerlist);
					}

					long cost = SystemTime.currentTimeMillis() - qz.getLastUpdateTime();
					Map<Long, AnswerRecord> map = qz.getQuizRecord();
					if (map.get(player.getId()) != null) {
						map.get(player.getId()).addTempRecord(result, cost);
					} else {
						AnswerRecord ar = new AnswerRecord();
						ar.setPlayerId(player.getId());
						ar.addTempRecord(result, cost);
						map.put(player.getId(), ar);
					}
					if (logger.isWarnEnabled()) {
						logger.warn("[答题完成] [" + player.getLogString() + "] []");
					}
					return true;

				} else {
					if (logger.isWarnEnabled()) logger.warn("[答题错误] [生成的题库错误] [" + list.size() + "]");
				}
			} else {
				if (logger.isWarnEnabled()) logger.warn("[答题错误] [生成的题库为null] []");
			}
		}
		return false;
	}

	/**
	 * 求助 跟1选择一样
	 * @param num
	 */
	public boolean helpAnswer(int num, Player player) {

		if (qz != null) {
			if (num == 0) {
				player.sendError(首轮不能使用求助);
				return false;
			}

			List<Player> useHelp = qz.getUseHelp();
			if (useHelp.contains(player)) {
				if (logger.isWarnEnabled()) {
					logger.warn("[答题使用求助] [一个题不能重复使用求助] [" + player.getLogString() + "]");
				}
				return false;
			}

			List<AnswerRecord> list = qz.getTopList();
			if (list != null) {
				if (list.get(0) != null) {
					AnswerRecord ar = list.get(0);
					if (ar.getPlayerId() == player.getId()) {
						player.sendError(第一名不能使用求助);
						if (logger.isWarnEnabled()) {
							logger.warn("[答题使用求助] [第一名不能使用求助] [" + player.getLogString() + "]");
						}
						return false;
					}
				}
			}

			Map<Long, AnswerRecord> mapRecord = qz.getQuizRecord();
			if (mapRecord != null) {
				AnswerRecord ar = mapRecord.get(player.getId());
				if (ar == null) {
					ar = new AnswerRecord();
					ar.setPlayerId(player.getId());
					ar.setLastCostTime(SystemTime.currentTimeMillis() - qz.getLastUpdateTime());
					mapRecord.put(player.getId(), ar);
					ar.addHelp();
					qz.getUseHelp().add(player);
					ANSWER_USE_PROPS_RES res = new ANSWER_USE_PROPS_RES(GameMessageFactory.nextSequnceNum(), (byte) 1);
					player.addMessageToRightBag(res);
					if (logger.isWarnEnabled()) {
						logger.warn("[答题使用求助1] [" + player.getLogString() + "]");
					}
					return true;
				} else {
					if (ar.isAddHelp()) {
						ar.setLastCostTime(SystemTime.currentTimeMillis() - qz.getLastUpdateTime());
						ar.addHelp();
						qz.getUseHelp().add(player);
						ANSWER_USE_PROPS_RES res = new ANSWER_USE_PROPS_RES(GameMessageFactory.nextSequnceNum(), (byte) 1);
						player.addMessageToRightBag(res);
						if (logger.isWarnEnabled()) {
							logger.warn("[答题使用求助2] [" + player.getLogString() + "]");
						}
						return true;
					} else {
						// player.send_HINT_REQ("求助最多只能使用"+AnswerRecord.helpNumLimit+"次");
						player.send_HINT_REQ(translateString(求助最多只能使用xx, new String[][] { { STRING_1, AnswerRecord.helpNumLimit + "" } }));
					}
				}
			}
		}
		return false;

	}

	/**
	 * 放大器 跟最多答案一样
	 * @param num
	 */
	public boolean amplifierAnswer(int num, Player player) {

		if (qz != null) {

			List<Player> useAmplifier = qz.getUseAmplifier();
			if (useAmplifier.contains(player)) {
				if (logger.isWarnEnabled()) {
					logger.warn("[答题使用放大镜] [一个题不能重复使用放大镜] [" + player.getLogString() + "]");
				}
				return false;
			}

			Map<Long, AnswerRecord> mapRecord = qz.getQuizRecord();

			if (mapRecord != null) {
				AnswerRecord ar = mapRecord.get(player.getId());
				if (ar == null) {
					ar = new AnswerRecord();
					ar.setPlayerId(player.getId());
					ar.setLastCostTime(SystemTime.currentTimeMillis() - qz.getLastUpdateTime());
					mapRecord.put(player.getId(), ar);
					ar.addAmplifier();
					qz.getUseAmplifier().add(player);
					ANSWER_USE_PROPS_RES res = new ANSWER_USE_PROPS_RES(GameMessageFactory.nextSequnceNum(), (byte) 2);
					player.addMessageToRightBag(res);
					if (logger.isWarnEnabled()) {
						logger.warn("[答题使用放大镜1] [" + player.getLogString() + "]");
					}
					return true;
				} else {
					if (ar.isAddAmplifier()) {
						ar.setLastCostTime(SystemTime.currentTimeMillis() - qz.getLastUpdateTime());
						ar.addAmplifier();
						qz.getUseAmplifier().add(player);

						ANSWER_USE_PROPS_RES res = new ANSWER_USE_PROPS_RES(GameMessageFactory.nextSequnceNum(), (byte) 2);
						player.addMessageToRightBag(res);
						if (logger.isWarnEnabled()) {
							logger.warn("[答题使用放大镜2] [" + player.getLogString() + "]");
						}
						return true;
					} else {
						// player.send_HINT_REQ("放大器最多只能使用"+AnswerRecord.amplifierNumLimit+"次");
						player.send_HINT_REQ(translateString(放大器最多只能使用xx, new String[][] { { STRING_1, AnswerRecord.amplifierNumLimit + "" } }));
						return false;
					}
				}
			}
		}
		return false;

	}

	public boolean confirmClose(Player player) {
		Quiz qz = this.getQz();
		if (qz != null) {
			synchronized (syn) {
				List<Long> list = qz.getAgreeQuizPlayers();
				if (list.contains(player.getId())) {
					list.remove(player.getId());
					qz.disAgreeQuizPlayers.add(player.getId());
					if (logger.isWarnEnabled()) {
						logger.warn("[玩家关闭答题活动成功] [" + player.getLogString() + "] []");
					}
					return true;
				} else {
					qz.disAgreeQuizPlayers.add(player.getId());
					if (logger.isWarnEnabled()) logger.warn("[关闭答题活动错误] [同意答题活动的人里没有玩家] [" + player.getLogString() + "]");
				}
			}
		} else {
			if (logger.isWarnEnabled()) logger.warn("[关闭答题活动错误] [没有活动] [" + player.getLogString() + "]");
		}
		return true;
	}

	public Quiz load() {

		int 题库id = 0;
		int 题干 = 1;
		int 答案1 = 2;
		int 答案2 = 3;
		int 答案3 = 4;
		int 答案4 = 5;
		int 正确答案 = 6;

		String configFile = QuizManager.getInstance().configFile;
		Random random = QuizManager.getInstance().random;
		File file = new File(configFile);
		HSSFWorkbook workbook = null;
		Quiz quiz = new Quiz();
		try {
			InputStream is = new FileInputStream(file);
			POIFSFileSystem pss = new POIFSFileSystem(is);
			workbook = new HSSFWorkbook(pss);
			HSSFSheet sheet = null;
			sheet = workbook.getSheetAt(0);
			if (sheet == null) return null;
			int rows = sheet.getPhysicalNumberOfRows();
			HSSFRow row = null;
			HSSFCell cell = null;
//			for (int i = 0; i < QuizManager.SUBJECTNUM;) {
			for (int i = 0; i < QuizManager.getInstance().getSubjectNum();) {
				int rowNum = random.nextInt(rows);
				if (rowNum == 0) continue;
				boolean repeat = false;
				if (quiz != null && quiz.getSubjectList().size() > 0) {
					for (Subject s : quiz.getSubjectList()) {
						if (s.getSubjectId() == rowNum) {
							repeat = true;
							break;
						}
					}
				}
				if (repeat) {
					continue;
				}
				row = sheet.getRow(rowNum);
				if (row != null) {
					Subject subject = new Subject();

					cell = row.getCell(题库id);
					try {
						subject.setSubjectId((int) cell.getNumericCellValue());
					} catch (Exception ex) {
						try {
							subject.setSubjectId(Integer.parseInt(cell.getStringCellValue().trim()));
						} catch (Exception e) {
							// throw e;
						}
					}
					cell = row.getCell(题干);
					subject.setTrunk((cell.getStringCellValue()));
					cell = row.getCell(答案1);
					String b1 = "";
					try {
						b1 = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						b1 = String.valueOf((int) cell.getNumericCellValue());
					}
					subject.setBranchA(b1);

					cell = row.getCell(答案2);
					String b2 = "";
					try {
						b2 = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						b2 = (String.valueOf((int) cell.getNumericCellValue()));
					}
					subject.setBranchB(b2);

					cell = row.getCell(答案3);
					String b3 = "";
					try {
						b3 = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						b3 = String.valueOf((int) cell.getNumericCellValue());
					}
					subject.setBranchC(b3);
					cell = row.getCell(答案4);
					String b4 = "";
					try {
						b4 = (cell.getStringCellValue().trim());
					} catch (Exception ex) {
						b4 = (String.valueOf((int) cell.getNumericCellValue()));
					}
					subject.setBranchD(b4);
					cell = row.getCell(正确答案);

					try {
						subject.setRightAnswer((int) cell.getNumericCellValue());
					} catch (Exception ex) {
						try {
							subject.setRightAnswer(Integer.parseInt(cell.getStringCellValue().trim()));
						} catch (Exception e) {
							// throw e;
						}
					}
					quiz.getSubjectList().add(subject);
					++i;
				} else {
					if (QuizManager.logger.isWarnEnabled()) QuizManager.logger.warn("解析题库错误");
					return null;
				}
			}
			if (QuizManager.logger.isDebugEnabled()) {
				QuizManager.logger.debug("生成题库成功");
			}
			return quiz;
		} catch (Exception e) {
			if (QuizManager.logger.isWarnEnabled()) QuizManager.logger.warn("解析题库错误", e);
		}
		return null;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public Quiz getQz() {
		return qz;
	}

	public void setQz(Quiz qz) {
		this.qz = qz;
	}

	public String getConfigFileExp() {
		return configFileExp;
	}

	public void setConfigFileExp(String configFileExp) {
		this.configFileExp = configFileExp;
	}

	// 得到玩家今天的答题状态 true 完成 false 没完成
	public boolean[] getTodayAnswerQuizState(Player player) {
		boolean[] states = new boolean[] { false, false };
		long now = SystemTime.currentTimeMillis();
		boolean same = Utils.checkSameDay(now, player.getLastAnswerQuizTime());
		if (same) {
			if (player.getTodayAnswerQuizState() != null) {
				states = player.getTodayAnswerQuizState();
			}
		}
		if (QuizManager.logger.isWarnEnabled()) {
			QuizManager.logger.warn("[查看当天的答题状态] [" + player.getLogString() + "] [" + states[0] + "] [" + states[1] + "]");
		}
		return states;
	}

}
