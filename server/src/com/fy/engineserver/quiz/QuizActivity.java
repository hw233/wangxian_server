/**
 * 
 */
package com.fy.engineserver.quiz;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import org.slf4j.Logger;

import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.honor.Honor;
import com.fy.engineserver.honor.HonorManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_AnswerQuiz;
import com.fy.engineserver.menu.Option_QuitQuiz;
import com.fy.engineserver.menu.Option_QuizRewards;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.question.Question;
import com.fy.engineserver.menu.question.QuestionManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.stat.model.PlayerActionFlow;

/**
 * @author Administrator
 * 
 */
public class QuizActivity extends Quiz {

	Random random;

	ArrayList<Question> questions;

	/**
	 * 报名开始时间-小时
	 */
	int enrollStartHour;

	/**
	 * 报名开始时间-分钟
	 */
	int enrollStartMinute;

	/**
	 * 报名截至时间-小时
	 */
	int enrollEndHour;

	/**
	 * 报名截至时间-分钟
	 */
	int enrollEndMinute;

	/**
	 * 答题开始时间-小时
	 */
	int answerStartHour;

	/**
	 * 答题开始时间-分钟
	 */
	int answerStartMinute;

	/**
	 * 每题时间间隔
	 */
	long questionInterval;

	/**
	 * 题目数量
	 */
	int questionsNum;

	/**
	 * 当前的问题
	 */
	Question currentQuestion;

	Logger log;

	/**
	 * 前一题的出题时间
	 */
	long lastQuestionTime;

	/**
	 * 答对时，每题的基础得分
	 */
	int basePoints = 30;

	/**
	 * 答题是否结束
	 */
	boolean isQuizEnd;

	/**
	 * 给前三名的额外奖励
	 */
	String[] specialRewards;

	int questionIndex;

	boolean isNeedSave;

	/**
	 * 
	 */
	public QuizActivity(String name) {
		// TODO Auto-generated constructor stub
		super(name);
		this.random = new Random();
		this.questions = new ArrayList<Question>();
		// QuizManager.getInstance().addActivities(this);
		this.log = QuizManager.log;
	}

	/*
	 * (non-Javadoc)
	 * @see com.fy.engineserver.puzzle.PuzzleHeartbeat#heartbeat()
	 */
	public void heartbeat() throws Exception {
		// TODO Auto-generated method stub
		long ct = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(ct);

		ca.set(Calendar.HOUR_OF_DAY, this.enrollStartHour);
		ca.set(Calendar.MINUTE, this.enrollStartMinute);
		long enrollStartTime = ca.getTimeInMillis();
		ca.set(Calendar.HOUR_OF_DAY, this.enrollEndHour);
		ca.set(Calendar.MINUTE, this.enrollEndMinute);
		long enrollEndTime = ca.getTimeInMillis();

		ca.set(Calendar.HOUR_OF_DAY, this.answerStartHour);
		ca.set(Calendar.MINUTE, this.answerStartMinute);
		long answerStartTime = ca.getTimeInMillis();
		// ca.set(Calendar.HOUR_OF_DAY, QuizActivity.ANSWER_END_TIME_HOUR);
		// ca.set(Calendar.MINUTE, QuizActivity.ANSWER_END_TIME_MINUTE);
		// long answerEndTime=ca.getTimeInMillis();

		if (ct >= enrollStartTime && ct < enrollEndTime) {
			if (this.getStatus() != Quiz.STATUS_ENROLL) {
				this.participants.clear();
				this.isNeedSave = true;
				this.setStatus(Quiz.STATUS_ENROLL);
				ChatMessage cm = new ChatMessage();
				cm.setMessageText(Translate.text_5585);
				ChatMessageService.getInstance().sendMessageToSystem(cm);
				this.isQuizEnd = false;
				if (this.log.isInfoEnabled()) {
					// this.log.info("[问答活动] ["+this.getName()+"] [状态改变] [开始报名]");
					if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [状态改变] [开始报名]", new Object[] { this.getName() });
				}
			}
		} else if (ct >= enrollEndTime && ct < answerStartTime) {
			if (this.getStatus() != Quiz.STATUS_WAITING) {
				this.setStatus(Quiz.STATUS_WAITING);
				if (this.log.isInfoEnabled()) {
					// this.log.info("[问答活动] ["+this.getName()+"] [状态改变] [等待答题]");
					if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [状态改变] [等待答题]", new Object[] { this.getName() });
				}
			}
		} else if (ct >= answerStartTime) {
			if (this.getStatus() == Quiz.STATUS_WAITING && !this.isQuizEnd) {
				this.initQuestions();
				this.setStatus(Quiz.STATUS_START);
				if (this.log.isInfoEnabled()) {
					// this.log.info("[问答活动] ["+this.getName()+"] [状态改变] [开始答题]");
					if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [状态改变] [开始答题]", new Object[] { this.getName() });
				}
			}
		}

		switch (this.getStatus()) {
		case QuizActivity.STATUS_ENROLL:

			break;

		case QuizActivity.STATUS_START:
			this.quizQuestion(ct);
			break;

		case QuizActivity.STATUS_END:

			break;
		}
	}

	private void initQuestions() {
		this.questions.clear();
		this.lastQuestionTime = 0;
		this.questionIndex = 0;
		Question[] qq = QuestionManager.getInstance().getAllQuestion().toArray(new Question[0]);
		// --------------------------------------------------------
		// 教师节特殊处理
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());
		int month = ca.get(Calendar.MONTH);
		int day = ca.get(Calendar.DAY_OF_MONTH);
		Question[] tq = null;
		if (month == 8 && (day == 10)) {
			tq = QuestionManager.getInstance().getAllTeacherDayQuestion().toArray(new Question[0]);
			if (tq != null && tq.length > 0) {
				qq = null;
				qq = tq;
			}
		}
		// --------------------------------------------------------

		// --------------------------------------------------------
		// 亚运会特殊处理
		if ((month == Calendar.NOVEMBER && day >= 12 && day <= 30)) {// 12-30
			tq = QuestionManager.getInstance().getAllAsianGamesQuestion().toArray(new Question[0]);
			if (tq != null && tq.length > 0) {
				qq = null;
				qq = tq;
			}
		}
		// --------------------------------------------------------

		ArrayList<Question> tempList = new ArrayList<Question>();
		for (Question q : qq) {
			tempList.add(q);
		}
		if (this.questionsNum >= tempList.size()) {
			this.questions.addAll(tempList);
		} else {
			for (int i = 0; i < this.questionsNum; i++) {
				int index = Math.abs(this.random.nextInt()) % tempList.size();
				this.questions.add(tempList.get(index));
				tempList.remove(index);
			}
		}
		Enumeration<Long> ids = this.participants.keys();
		while (ids.hasMoreElements()) {
			AnswerRecord ar = this.participants.get(ids.nextElement());
			ar.allQuestions.addAll(this.questions);
		}
		if (this.log.isInfoEnabled()) {
			// this.log.info("[问答活动] ["+this.getName()+"] [问题初始化] [成功] [数量："+this.questions.size()+"]");
			if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [问题初始化] [成功] [数量：{}]", new Object[] { this.getName(), this.questions.size() });
		}
	}

	/**
	 * 提问
	 * 
	 * @param ct
	 */
	private void quizQuestion(long ct) {
		if (ct - this.lastQuestionTime > this.questionInterval) {
			if (this.questions.size() > 0) {
				this.questionIndex++;
				// int lastQuestionId=-1;
				// if(this.currentQuestion!=null){
				// lastQuestionId=this.currentQuestion.getId();
				// }
				this.currentQuestion = this.questions.get(0);
				this.questions.remove(0);
				Enumeration<Long> enu = this.participants.keys();
				while (enu.hasMoreElements()) {
					long id = enu.nextElement();
					AnswerRecord ar = this.participants.get(id);
					Player p = null;
					try {
						p = PlayerManager.getInstance().getPlayer(id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (p != null) {
						// 如果有问题尚未回答，就不发下个问题
						// if(ar.questionStatus.containsValue(0)){
						// continue;
						// }
						Question cq = ar.allQuestions.get(Math.abs(this.random.nextInt()) % ar.allQuestions.size());
						Option_AnswerQuiz[] oa = new Option_AnswerQuiz[cq.getOptions().length];
						for (int i = 0; i < oa.length; i++) {
							oa[i] = new Option_AnswerQuiz(i, this.getName(), cq);
							oa[i].setText(cq.getOptions()[i]);
							oa[i].setColor(0xffffff);
							oa[i].setIconId("147");
						}

						Option[] op = new Option[oa.length + 1];
						System.arraycopy(oa, 0, op, 0, oa.length);
						Option_QuitQuiz oq = new Option_QuitQuiz(this.getName());
						oq.setText(Translate.text_5586);
						oq.setColor(0xffffff);
						oq.setIconId("172");
						op[op.length - 1] = oq;
						MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(3600);
						mw.setNpcId(-1000);
						mw.setTitle(this.getName());
						StringBuffer sb = new StringBuffer();
						sb.append(Translate.text_5587 + this.getPlayerPoints(p.getId()) + '\n');
						sb.append(Translate.text_5588 + this.questionIndex + '\n');
						sb.append(cq.getDescription());
						mw.setDescriptionInUUB(sb.toString());
						mw.setOptions(op);

						QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						p.addMessageToRightBag(res);

						ar.updateQuestionStatus(cq.getId(), 0);
						ar.setCurrentQuestionId(cq.getId());
						ar.allQuestions.remove(cq);
					}

				}
				this.lastQuestionTime = ct;
			} else {
				ArrayList<AnswerRecord> al = new ArrayList<AnswerRecord>();
				al.addAll(this.participants.values());
				Collections.sort(al, new Comparator<AnswerRecord>() {

					public int compare(AnswerRecord o1, AnswerRecord o2) {
						// TODO Auto-generated method stub
						if (o1.getPoints() > o2.getPoints()) {
							return -1;
						} else if (o1.getPoints() < o2.getPoints()) {
							return 1;
						} else {
							if (o1.getSpendTime() < o2.getSpendTime()) {
								return -1;
							} else if (o1.getSpendTime() > o2.getSpendTime()) {
								return 1;
							} else {
								return 0;
							}
						}
					}

				});

				String names = "";

				for (int i = 0; i < al.size(); i++) {
					if (i == 0) {
						names += "[color=" + 0xf8f12c + Translate.text_5589 + al.get(i).getPlayerName() + Translate.text_5590 + al.get(i).getPoints() + "[/color]" + '\n';
					} else if (i == 1) {
						names += "[color=" + 0xf8f12c + Translate.text_5591 + al.get(i).getPlayerName() + Translate.text_5590 + al.get(i).getPoints() + "[/color]" + '\n';
					} else if (i == 2) {
						names += "[color=" + 0xf8f12c + Translate.text_5592 + al.get(i).getPlayerName() + Translate.text_5590 + al.get(i).getPoints() + "[/color]" + '\n';
					} else {
						break;
					}
				}

				PlayerManager pm = PlayerManager.getInstance();

				for (int i = 0; i < al.size(); i++) {
					al.get(i).setRewardExp(this.getRewardExp(i, al.get(i)));
					al.get(i).setRewardMoney(this.getRewardMoney(i, al.get(i)));

					StringBuffer sb = new StringBuffer();
					sb.append(Translate.text_5593 + al.get(i).getPoints() + '\n');
					sb.append(names);
					sb.append(Translate.text_5594 + this.specialRewards.length + Translate.text_5595 + this.specialRewards.length + Translate.text_5596);
					sb.append(Translate.text_5597 + (i + 1) + Translate.text_5598);

					Option_QuizRewards[] oqr = new Option_QuizRewards[2];
					oqr[0] = new Option_QuizRewards(this.getName(), (byte) 1);
					oqr[0].setText(Translate.text_511 + al.get(i).getRewardExp());
					oqr[0].setColor(0xffffff);
					oqr[0].setIconId("149");
					oqr[1] = new Option_QuizRewards(this.getName(), (byte) 0);
					oqr[1].setText(Translate.text_5599 + al.get(i).getRewardMoney());
					oqr[1].setColor(0xffffff);
					oqr[1].setIconId("149");

					MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(3600);
					mw.setNpcId(-1000);
					mw.setTitle(this.getName());
					mw.setDescriptionInUUB(sb.toString());
					mw.setOptions(oqr);

					QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
					try {
						Player player = pm.getPlayer(al.get(i).getPlayerId());
						player.addMessageToRightBag(res);
						// --------------------------------------------------------------------------------
						// 称号处理
						HonorManager hm = HonorManager.getInstance();
						Honor h = hm.getHonor(player, Translate.text_4245);
						if (h != null) {
							hm.loseHonor(player, h, HonorManager.LOSE_REASON_NOT_MEET_STANDARDS);
						}
						h = hm.getHonor(player, Translate.text_5600);
						if (h != null) {
							hm.loseHonor(player, h, HonorManager.LOSE_REASON_NOT_MEET_STANDARDS);
						}
						h = hm.getHonor(player, Translate.text_5601);
						if (h != null) {
							hm.loseHonor(player, h, HonorManager.LOSE_REASON_NOT_MEET_STANDARDS);
						}
						// --------------------------------------------------------------------------------

						if (i < this.specialRewards.length) {
							this.mailPresent(i, player);
							// --------------------------------------------------------------------------------
							// 称号处理
							if (i == 0) {
								hm.gainHonor(player, Translate.text_4245);
							} else if (i == 1) {
								hm.gainHonor(player, Translate.text_5600);
							} else if (i == 2) {
								hm.gainHonor(player, Translate.text_5601);
							}
							// --------------------------------------------------------------------------------
						}
						// log.info("[问答活动] [发奖] [成功] [第"+(i+1)+"名] ["+al.get(i).getPlayerName()+"] ["+al.get(i).getPlayerId()+"] [经验："+al.get(i).getRewardExp()+"] [游戏币："+al.get(i).getRewardMoney()+"]");
						if (log.isInfoEnabled()) log.info("[问答活动] [发奖] [成功] [第{}名] [{}] [{}] [经验：{}] [游戏币：{}]", new Object[] { (i + 1), al.get(i).getPlayerName(), al.get(i).getPlayerId(), al.get(i).getRewardExp(), al.get(i).getRewardMoney() });
					} catch (Exception e) {
						if (log.isWarnEnabled()) log.warn("[问答活动] [发奖] [失败] [第" + (i + 1) + "名] [" + al.get(i).getPlayerName() + "]", e);
					}
				}
				this.setStatus(Quiz.STATUS_END);
				this.isQuizEnd = true;
				if (this.log.isInfoEnabled()) {
					// this.log.info("[问答活动] ["+this.getName()+"] [状态改变] [答题结束]");
					if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [状态改变] [答题结束]", new Object[] { this.getName() });
				}
			}
		}
	}

	@Override
	public boolean answer(long playerId, Question question, int selectionIndex) {
		// TODO Auto-generated method stub
		if (selectionIndex == question.getRightOption()) {
			AnswerRecord ar = this.participants.get(playerId);
			ar.setRightNum(ar.getRightNum() + 1);
			ar.addQuestionId(question.getId());
			ar.setSpendTime(ar.getSpendTime() + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - this.lastQuestionTime));
			int oldValue = ar.getPoints();
			this.addPoints(playerId, question);
			ar.updateQuestionStatus(question.getId(), 1);
			if (this.log.isInfoEnabled()) {
				// this.log.info("[问答活动] ["+this.getName()+"] [玩家回答正确] [玩家："+ar.getPlayerName()+"] [玩家ID："+ar.getPlayerId()+"] [得分："+(ar.getPoints()-oldValue)+"] [分数变化："+oldValue+"-->"+ar.getPoints()+"]");
				if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [玩家回答正确] [玩家：{}] [玩家ID：{}] [得分：{}] [分数变化：{}-->{}]", new Object[] { this.getName(), ar.getPlayerName(), ar.getPlayerId(), (ar.getPoints() - oldValue), oldValue, ar.getPoints() });
			}
			return true;
		} else {
			AnswerRecord ar = this.participants.get(playerId);
			ar.setWrongNum(ar.getWrongNum() + 1);
			ar.addQuestionId(this.currentQuestion.getId());
			ar.setSpendTime(ar.getSpendTime() + (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - this.lastQuestionTime));
			ar.updateQuestionStatus(question.getId(), 2);
			if (this.log.isInfoEnabled()) {
				// this.log.info("[问答活动] ["+this.getName()+"] [玩家回答错误] [玩家："+ar.getPlayerName()+"] [玩家ID："+ar.getPlayerId()+"]");
				if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [玩家回答错误] [玩家：{}] [玩家ID：{}]", new Object[] { this.getName(), ar.getPlayerName(), ar.getPlayerId() });
			}
			return false;
		}
	}

	@Override
	public void enroll(Player player) {
		// TODO Auto-generated method stub
		if (!this.participants.containsKey(player.getId())) {
			this.participants.put(player.getId(), new AnswerRecord(player));
			Player.sendPlayerAction(player, PlayerActionFlow.行为类型_答题, PlayerActionFlow.行为名称_答题_报名, 0, new Date(), true);
			this.isNeedSave = true;
			if (this.log.isInfoEnabled()) {
				// this.log.info("[问答活动] ["+this.getName()+"] [玩家报名] [玩家："+player.getName()+"] [玩家ID："+player.getId()+"]");
				if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [玩家报名] [玩家：{}] [玩家ID：{}]", new Object[] { this.getName(), player.getName(), player.getId() });
			}
		}
	}

	@Override
	public void quit(long playerId) {
		// TODO Auto-generated method stub
		if (this.participants.containsKey(playerId)) {
			this.participants.remove(playerId);
			this.isNeedSave = true;
			if (this.log.isInfoEnabled()) {
				// this.log.info("[问答活动] ["+this.getName()+"] [玩家退出] [玩家ID："+playerId+"]");
				if (this.log.isInfoEnabled()) this.log.info("[问答活动] [{}] [玩家退出] [玩家ID：{}]", new Object[] { this.getName(), playerId });
			}
		}
	}

	@Override
	public boolean isParticipant(long playerId) {
		// TODO Auto-generated method stub
		return this.participants.containsKey(playerId);
	}

	@Override
	public boolean isAnswerTime() {
		// TODO Auto-generated method stub
		return this.getStatus() == Quiz.STATUS_START;
	}

	@Override
	public boolean isEnrollTime() {
		// TODO Auto-generated method stub
		return this.getStatus() == Quiz.STATUS_ENROLL;
	}

	/**
	 * 答对，得分
	 * @param playerId
	 */
	private void addPoints(long playerId, Question question) {
		AnswerRecord ar = this.participants.get(playerId);
		long freeTime = this.questionInterval - (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - this.lastQuestionTime);
		if (freeTime < 0) {
			freeTime = 0;
		}
		if (question.getId() != ar.getCurrentQuestionId()) {
			freeTime = 0;
		}
		int point = (int) (this.basePoints + (freeTime * 2) / 1000);
		ar.setPoints(ar.getPoints() + point);
	}

	private int getPlayerPoints(long playerId) {
		if (this.participants.containsKey(playerId)) {
			return this.participants.get(playerId).getPoints();
		}
		return 0;
	}

	private int getRewardExp(int rank, AnswerRecord ar) {
		float exp = 0;

		exp = ar.getPoints() * (0.9f * ar.getPlayerLevel() * ar.getPlayerLevel() - 25 * ar.getPlayerLevel() + 200);
		int[] n = new int[] { 500, 400, 350, 300, 250, 200, 150, 100, 80, 50 };
		if (rank < n.length) {
			exp += (n[rank] * (1.8f * ar.getPlayerLevel() * ar.getPlayerLevel() - 50 * ar.getPlayerLevel() + 400));
		}
		return (int) exp;
	}

	private int getRewardMoney(int rank, AnswerRecord ar) {
		int money = ar.getPoints() * ar.getPlayerLevel() * 2;
		int[] n = new int[] { 1500000, 1200000, 1000000, 700000, 600000, 500000, 400000, 300000, 200000, 100000 };
		if (rank < n.length) {
			money += n[rank];
		}
		return money;
	}

	/**
	 * 邮寄礼品
	 * @param rank
	 * @param player
	 */
	private void mailPresent(int rank, Player player) {
	}

	public int getEnrollStartHour() {
		return enrollStartHour;
	}

	public void setEnrollStartHour(int enrollStartHour) {
		this.enrollStartHour = enrollStartHour;
	}

	public int getEnrollStartMinute() {
		return enrollStartMinute;
	}

	public void setEnrollStartMinute(int enrollStartMinute) {
		this.enrollStartMinute = enrollStartMinute;
	}

	public int getEnrollEndHour() {
		return enrollEndHour;
	}

	public void setEnrollEndHour(int enrollEndHour) {
		this.enrollEndHour = enrollEndHour;
	}

	public int getEnrollEndMinute() {
		return enrollEndMinute;
	}

	public void setEnrollEndMinute(int enrollEndMinute) {
		this.enrollEndMinute = enrollEndMinute;
	}

	public int getAnswerStartHour() {
		return answerStartHour;
	}

	public void setAnswerStartHour(int answerStartHour) {
		this.answerStartHour = answerStartHour;
	}

	public int getAnswerStartMinute() {
		return answerStartMinute;
	}

	public void setAnswerStartMinute(int answerStartMinute) {
		this.answerStartMinute = answerStartMinute;
	}

	public int getQuestionsNum() {
		return questionsNum;
	}

	public void setQuestionsNum(int questionsNum) {
		this.questionsNum = questionsNum;
	}

	public int getBasePoints() {
		return basePoints;
	}

	public void setBasePoints(int basePoints) {
		this.basePoints = basePoints;
	}

	public long getQuestionInterval() {
		return questionInterval;
	}

	public void setQuestionInterval(long questionInterval) {
		this.questionInterval = questionInterval;
	}

	public String[] getSpecialRewards() {
		return specialRewards;
	}

	public void setSpecialRewards(String[] specialRewards) {
		this.specialRewards = specialRewards;
	}

}
