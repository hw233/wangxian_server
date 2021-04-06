package com.fy.engineserver.activity.notice;

import static com.fy.engineserver.datasource.language.Translate.您的等级不足;
import static com.fy.engineserver.datasource.language.Translate.活动还未开启;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.quiz.QuizManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.quizActivity.Option_QuizActivity_Agree;
import com.fy.engineserver.menu.quizActivity.Option_QuizActivity_DisAgree;
import com.fy.engineserver.message.CURRENT_ACTIVITY_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

/**
 * 活动提示管理器
 * 
 */
public class ActivityNoticeManager {

	private static ActivityNoticeManager instance;

	public static ActivityNoticeManager getInstance() {
		if (instance == null) {
			synchronized (ActivityNoticeManager.class) {
				if (instance == null) {
					instance = new ActivityNoticeManager();
				}
			}
		}
		return instance;
	}

	public enum Activity {
		答题(1, "答题活动", "hd_wenda", "", Boolean.TRUE) {
			@Override
			boolean noticeSignup(Player player) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60 * 60);

				mw.setTitle(Translate.text_答题活动);
				mw.setDescriptionInUUB(Translate.text_参加答题活动);

				Option_QuizActivity_Agree agree = new Option_QuizActivity_Agree();
				agree.setText(Translate.text_答题接受);
				agree.setColor(0xffffff);

				Option_QuizActivity_DisAgree disagree = new Option_QuizActivity_DisAgree();
				disagree.setText(Translate.text_答题拒绝);
				disagree.setColor(0xffffff);

				mw.setOptions(new Option[] { agree, disagree });

				QUERY_WINDOW_RES res = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
				player.addMessageToRightBag(res);
				return true;
			}

			@Override
			public CompoundReturn canjoin(Player player) {
				if (QuizManager.getInstance().getQz() != null) {
					if (player.getLevel() < QuizManager.LV) {
						return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(您的等级不足);
					}
					return CompoundReturn.createCompoundReturn().setBooleanValue(true);
				}
				return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(活动还未开启);
			}
		},
//		武圣(1, "武圣争夺战", "hd_biwu", "", Boolean.TRUE) {
//
//			@Override
//			boolean noticeSignup(Player player) {
//				if (TournamentManager.getInstance().td == null) {
//					return false;
//				}
//				if (TournamentManager.getInstance().td.currentSignUpList == null) {
//					return false;
//				}
//				WindowManager mm = WindowManager.getInstance();
//				MenuWindow mw = mm.createTempMenuWindow(180);
//				mw.setDescriptionInUUB(Translate.武圣争夺战半小时后一触即发您还没有报名报名参加吗);
//				Option_Battle_BiWuSignUp option = new Option_Battle_BiWuSignUp();
//				option.setText(Translate.确定);
//				Option cancelOption = new Option_UseCancel();
//				cancelOption.setText(Translate.取消);
//				mw.setOptions(new Option[] { option, cancelOption });
//				CONFIRM_WINDOW_REQ req = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
//				player.addMessageToRightBag(req);
//				return false;
//			}
//
//			@Override
//			public CompoundReturn canjoin(Player player) {
//				if (TournamentManager.getInstance().td == null) {
//					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(活动还未开启);
//				}
//				if (TournamentManager.getInstance().td.currentSignUpList == null) {
//					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(活动还未开启);
//				}
//				String result = TournamentManager.getInstance().报名合法性判断(player);
//				return CompoundReturn.createCompoundReturn().setBooleanValue(result == null).setStringValue(result);
//			}
//		},
//		国战(1, "国战", "hd_guozhan", "", Boolean.TRUE) {
//
//			@Override
//			boolean noticeSignup(Player player) {
//				if (GuozhanOrganizer.getInstance().getGuozhanList() == null && GuozhanOrganizer.getInstance().getGuozhanList().size() == 0) {
//					return false;
//				}
//				String selfCountryName = CountryManager.得到国家名(player.getCountry());
//				String attackerCountry = CountryManager.得到国家名(GuozhanOrganizer.getInstance().getGuozhanList().get(0).getAttacker().getCountryId());
//				String defenderCountry = CountryManager.得到国家名(GuozhanOrganizer.getInstance().getGuozhanList().get(0).getDefender().getCountryId());
//				if (selfCountryName.equals(attackerCountry)) {
//					GuozhanOrganizer.getInstance().sendConfirmFightToPlayer(player, defenderCountry);
//				} else {
//					GuozhanOrganizer.getInstance().sendConfirmFightToPlayer(player, attackerCountry);
//				}
//				return true;
//			}
//
//			@Override
//			public CompoundReturn canjoin(Player player) {
//				Guozhan guozhan = GuozhanOrganizer.getInstance().getGuozhan(player.getCountry());
//				if (guozhan == null) {
//					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue("国战没有开启");
//				}
//				guozhan = GuozhanOrganizer.getInstance().getPlayerGuozhan(player);
//				if (guozhan != null) {
//					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue("你已经参加了国战");
//				}
//				return CompoundReturn.createCompoundReturn().setBooleanValue(true);
//			}
//
//		},
		;

		private Activity(int id, String name, String icon, String des, boolean needSignup) {
			this.id = id;
			this.name = name;
			this.icon = icon;
			this.des = des;
			this.needSignup = needSignup;
			this.activityNotice = new ActivityNotice(id, name, icon, needSignup, des);
		}

		private int id;
		private String name;
		private String icon;
		private boolean needSignup;// 是否要报名参加
		private String des;
		private ActivityNotice activityNotice;

		// 通知各个系统报名参加活动.弹出提示框等
		abstract boolean noticeSignup(Player player);

		// 是否能参加活动
		public abstract CompoundReturn canjoin(Player player);

		public CompoundReturn signup(Player player) {
			try {
				if (hasSignup(player)) {
					// 已经报名参加了
					return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(Translate.你已经参加了该活动);
				}
				CompoundReturn join = canjoin(player);
				if (!join.getBooleanValue()) {
					return join;
				}
					if (noticeSignup(player)) {
						ActivitySubSystem.logger.warn("[活动提示系统] [提示玩家:" + player.getName() + "] [参加活动:" + this.toString() + "]");
						return CompoundReturn.createCompoundReturn().setBooleanValue(true);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue("未知错误");
		}

		// 是否已经报名参加了
		boolean hasSignup(Player player) {
			if (ActivityNoticeManager.getInstance().getInActivityPlayers().containsKey(this)) {
				return ActivityNoticeManager.getInstance().getInActivityPlayers().get(this).contains(player.getId());
			}
			return false;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}

		public boolean isNeedSignup() {
			return needSignup;
		}

		public void setNeedSignup(boolean needSignup) {
			this.needSignup = needSignup;
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public ActivityNotice getActivityNotice() {
			return activityNotice;
		}

		public void setActivityNotice(ActivityNotice activityNotice) {
			this.activityNotice = activityNotice;
		}

	}

	private Map<Integer, Activity> activityOpen = new LinkedHashMap<Integer, Activity>();// 正在进行中的活动

	private Map<Activity, List<Long>> inActivityPlayers = new HashMap<ActivityNoticeManager.Activity, List<Long>>();

	public void activityStart(Activity activity, Player[] players) {
		try {
			if (!activityOpen.values().contains(activity)) {
				synchronized (ActivityNoticeManager.class) {
					if (!activityOpen.values().contains(activity)) {
						activityOpen.put(activity.getId(), activity);
						inActivityPlayers.put(activity, new ArrayList<Long>());
						ActivitySubSystem.logger.warn("[活动提示系统] [活动开始:" + activity.toString() + "]");
						for (Player player : players) {
							noticePlayerCurrentActivity(player);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void activityEnd(Activity activity, Player[] players) {
		try {
			activityOpen.remove(activity.getId());
			inActivityPlayers.remove(activity);
			for (Player player : players) {
				noticePlayerCurrentActivity(player);
				ActivitySubSystem.logger.warn("[活动提示系统] [提示玩家:" + player.getName() + "] [活动结束:" + activity.toString() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void playerAgreeActivity(Player player, Activity activity) {
		List<Long> list = inActivityPlayers.get(activity);
		if (list != null) {
			list.add(player.getId());
			ActivitySubSystem.logger.warn("[活动提示系统] [玩家:" + player.getName() + "] [同意参加活动:" + activity.toString() + "]");
		}
	}

	/**
	 * 获得活动id
	 * @param activityId
	 * @return
	 */
	public Activity getActivityById(int activityId) {
		for (Activity activity : Activity.values()) {
			if (activity.getId() == activityId) {
				return activity;
			}
		}
		return null;
	}

	/**
	 * 通知角色当前的活动
	 * @param player
	 */
	public void noticePlayerCurrentActivity(Player player) {
		try {
			ActivityNotice[] activityNotices = getCurrentActivity(player);
			if (activityNotices == null || activityNotices.length == 0) {
				return;
			}
			CURRENT_ACTIVITY_REQ req = new CURRENT_ACTIVITY_REQ(GameMessageFactory.nextSequnceNum(), activityNotices);
			player.addMessageToRightBag(req);
			StringBuffer sbf = new StringBuffer();
			for (ActivityNotice notice : activityNotices) {
				sbf.append(notice.toString());
			}
			ActivitySubSystem.logger.warn("[活动提示系统] [通知玩家:" + player.getName() + "] [当前活数据:" + sbf.toString() + "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 得到角色符合的活动
	 * @param player
	 * @return
	 */
	public ActivityNotice[] getCurrentActivity(Player player) {
		List<ActivityNotice> list = new ArrayList<ActivityNotice>();
		for (Activity activity : activityOpen.values()) {
			if (activity.canjoin(player).getBooleanValue()) {
				list.add(activity.getActivityNotice());
			}
		}
		return list.toArray(new ActivityNotice[0]);
	}

	public Map<Integer, Activity> getActivityOpen() {
		return activityOpen;
	}

	public void setActivityOpen(Map<Integer, Activity> activityOpen) {
		this.activityOpen = activityOpen;
	}

	public Map<Activity, List<Long>> getInActivityPlayers() {
		return inActivityPlayers;
	}

	public void setInActivityPlayers(Map<Activity, List<Long>> inActivityPlayers) {
		this.inActivityPlayers = inActivityPlayers;
	}
}
