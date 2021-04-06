package com.fy.engineserver.activity.activeness;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;

import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaInfo;
import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;
import com.fy.engineserver.activity.fairyBuddha.ThankAward;
import com.fy.engineserver.activity.fairyBuddha.Voter;
import com.fy.engineserver.activity.fairyBuddha.WorshipAward;
import com.fy.engineserver.activity.fairylandTreasure.ArticleForDraw;
import com.fy.engineserver.activity.fairylandTreasure.FairylandBox;
import com.fy.engineserver.activity.fairylandTreasure.FairylandTreasureManager;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.activity.xianling.PlayerXianLingData;
import com.fy.engineserver.activity.xianling.XianLingChallengeManager;
import com.fy.engineserver.activity.xianling.XianLingManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.PlayerMessagePair;
import com.fy.engineserver.datasource.article.concrete.ReadEquipmentExcelManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.HunshiEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.gaiming.GaiMingManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.hunshi.Hunshi2Material;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.hunshi.HunshiSuit;
import com.fy.engineserver.hunshi.HunshiXiangQian;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.fairyBuddha.Option_Confirm_SetWorshipAwardLevel;
import com.fy.engineserver.menu.fairyBuddha.Option_Confirm_Thank;
import com.fy.engineserver.menu.hunshi.Option_Confirm_Open;
import com.fy.engineserver.menu.quizActivity.Option_UnConfirm_Close;
import com.fy.engineserver.message.ACTIVENESS_DES_REQ;
import com.fy.engineserver.message.ACTIVENESS_DES_RES;
import com.fy.engineserver.message.ACTIVENESS_LIST_REQ;
import com.fy.engineserver.message.ACTIVENESS_LIST_RES;
import com.fy.engineserver.message.CHANGE_NAME_REQ;
import com.fy.engineserver.message.END_DRAW_REQ;
import com.fy.engineserver.message.FAIRY_DECLARE_REQ;
import com.fy.engineserver.message.FAIRY_DECLARE_RES;
import com.fy.engineserver.message.FAIRY_DOTHANK_REQ;
import com.fy.engineserver.message.FAIRY_DOTHANK_RES;
import com.fy.engineserver.message.FAIRY_REFRESH_REQ;
import com.fy.engineserver.message.FAIRY_SET_AWARDLEVEL_REQ;
import com.fy.engineserver.message.FAIRY_SET_AWARDLEVEL_RES;
import com.fy.engineserver.message.FAIRY_THANK_REQ;
import com.fy.engineserver.message.FAIRY_THANK_RES;
import com.fy.engineserver.message.FAIRY_VOTERECORD_REQ;
import com.fy.engineserver.message.FAIRY_VOTERECORD_RES;
import com.fy.engineserver.message.FAIRY_VOTE_REQ;
import com.fy.engineserver.message.GET_PRIZE_REQ;
import com.fy.engineserver.message.GET_SIGNAWARD_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HELP_REQ;
import com.fy.engineserver.message.HELP_RES;
import com.fy.engineserver.message.HUNSHI2_CELL_BUY_REQ;
import com.fy.engineserver.message.HUNSHI2_INFOSHOW_REQ;
import com.fy.engineserver.message.HUNSHI2_INFOSHOW_RES;
import com.fy.engineserver.message.HUNSHI2_KIND_REQ;
import com.fy.engineserver.message.HUNSHI2_KIND_RES;
import com.fy.engineserver.message.HUNSHI2_OPEN_REQ;
import com.fy.engineserver.message.HUNSHI2_OPEN_RES;
import com.fy.engineserver.message.HUNSHI2_PROP_VIEW_REQ;
import com.fy.engineserver.message.HUNSHI2_PROP_VIEW_RES;
import com.fy.engineserver.message.HUNSHI2_PUTON_OPEN_REQ;
import com.fy.engineserver.message.HUNSHI2_PUTON_OPEN_RES;
import com.fy.engineserver.message.HUNSHI_JIANDING2_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING2_SURE_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING_REQ;
import com.fy.engineserver.message.HUNSHI_JIANDING_SURE_REQ;
import com.fy.engineserver.message.HUNSHI_MERGE_AIM_REQ;
import com.fy.engineserver.message.HUNSHI_MERGE_REQ;
import com.fy.engineserver.message.HUNSHI_PROP_VIEW_REQ;
import com.fy.engineserver.message.HUNSHI_PROP_VIEW_RES;
import com.fy.engineserver.message.HUNSHI_PUTON_OPEN_REQ;
import com.fy.engineserver.message.HUNSHI_PUTON_OPEN_RES;
import com.fy.engineserver.message.HUNSHI_PUTON_REQ;
import com.fy.engineserver.message.HUNSHI_SPECIAL_REQ;
import com.fy.engineserver.message.LOTTERY_FINISH_NEW_REQ;
import com.fy.engineserver.message.LOTTERY_FINISH_NEW_RES;
import com.fy.engineserver.message.LOTTERY_FINISH_REQ;
import com.fy.engineserver.message.LOTTERY_FINISH_RES;
import com.fy.engineserver.message.LOTTERY_REQ;
import com.fy.engineserver.message.LOTTERY_RES;
import com.fy.engineserver.message.NEW_USER_ENTER_SERVER_REQ;
import com.fy.engineserver.message.QUERY_COUNTRY_OFFICER_REQ;
import com.fy.engineserver.message.QUERY_USED_NAME_REQ;
import com.fy.engineserver.message.QUERY_USED_NAME_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.SIGN_REQ;
import com.fy.engineserver.message.SIGN_RES;
import com.fy.engineserver.message.START_DRAW_REQ;
import com.fy.engineserver.message.TEAM_QUERY_RES;
import com.fy.engineserver.message.XIANLING_OPEN_MAIN_REQ;
import com.fy.engineserver.message.XILIAN_EQUIPMENT_SURE_REQ;
import com.fy.engineserver.message.XILIAN_PUTEQUIPMENT_REQ;
import com.fy.engineserver.message.XILIAN_PUTEQUIPMENT_RES;
import com.fy.engineserver.message.XL_ACT_MEETMONSTER_BUFF_REQ;
import com.fy.engineserver.message.XL_BILLBOARD_REQ;
import com.fy.engineserver.message.XL_BUTENERGY_REQ;
import com.fy.engineserver.message.XL_BUTENERGY_SURE_REQ;
import com.fy.engineserver.message.XL_CHALLENGE_REQ;
import com.fy.engineserver.message.XL_CHALLENGE_SURE_REQ;
import com.fy.engineserver.message.XL_CROSSBILLBOARD_REQ;
import com.fy.engineserver.message.XL_EXIT_CHALLENGE_REQ;
import com.fy.engineserver.message.XL_GET_SCORE_PRIZE_REQ;
import com.fy.engineserver.message.XL_NOTICE_REQ;
import com.fy.engineserver.message.XL_OPEN_MEETMONSTER_BUFF_REQ;
import com.fy.engineserver.message.XL_OPEN_SCORE_BUFF_REQ;
import com.fy.engineserver.message.XL_QUERY_SKILL_REQ;
import com.fy.engineserver.message.XL_REFRESH_ENERGY_REQ;
import com.fy.engineserver.message.XL_REFRESH_ENERGY_RES;
import com.fy.engineserver.message.XL_SCORE_PRIZE_REQ;
import com.fy.engineserver.message.XL_TIMEDTASK_PRIZE_REQ;
import com.fy.engineserver.message.XL_TIMEDTASK_REQ;
import com.fy.engineserver.message.XL_USE_SCORE_CARD_REQ;
import com.fy.engineserver.message.XL_USE_SKILL_REQ;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.SpriteSubSystem;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.uniteserver.UnitedServerManager;
import com.fy.engineserver.util.Utils;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 活跃度,仙尊,翅膀,仙界宝箱,角色改名,坐骑魂石,仙灵大会协议处理
 * 
 */
public class ActivenessSubSystem implements GameSubSystem {

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[] { "ACTIVENESS_LIST_REQ", "LOTTERY_REQ", "LOTTERY_FINISH_REQ", "ACTIVENESS_DES_REQ", "GET_PRIZE_REQ", "CAN_GET_REQ", "SIGN_REQ", "GET_SIGNAWARD_REQ", "LOTTERY_FINISH_NEW_REQ", "FAIRY_VOTE_REQ", "FAIRY_DECLARE_REQ", "FAIRY_THANK_REQ", "FAIRY_DOTHANK_REQ", "FAIRY_SET_AWARDLEVEL_REQ", "FAIRY_VOTERECORD_REQ", "FAIRY_REFRESH_REQ", "OPEN_WINGPANEL_REQ", "WINGCLASS_REQ", "EXISTING_WING_REQ", "PUT_ONOFF_REQ", "COLLECT_REQ", "QUICK_INLAY_REQ", "QUICK_INLAY_SURE_REQ", "INLAY_EXCAVATE_REQ", "REGULAR_REQ", "SHOW_KIND_REQ", "WING_DES_REQ", "BRIGHTINLAY_EXCAVATE_REQ", "QUERY_WINGPANEL_STRONG_REQ", "WINGPANEL_STRONG_REQ", "NOTOPEN_INLAY_REQ", "WINGPANEL_NOTICE_REQ", "END_DRAW_REQ", "START_DRAW_REQ", "CHANGE_NAME_REQ", "QUERY_USED_NAME_REQ", "XILIAN_PUTEQUIPMENT_REQ", "XILIAN_EQUIPMENT_SURE_REQ", "HUNSHI_MERGE_AIM_REQ", "HUNSHI_SPECIAL_REQ", "HUNSHI_MERGE_REQ", "HUNSHI2_OPEN_REQ", "HUNSHI2_KIND_REQ", "HUNSHI_PUTON_OPEN_REQ", "HUNSHI_PUTON_REQ", "HUNSHI_PROP_VIEW_REQ", "HUNSHI2_CELL_BUY_REQ", "HUNSHI2_PROP_VIEW_REQ", "HUNSHI2_PUTON_OPEN_REQ", "HUNSHI_JIANDING_REQ", "HUNSHI_JIANDING_SURE_REQ", "HUNSHI_JIANDING2_SURE_REQ", "HUNSHI_JIANDING2_REQ", "HUNSHI2_INFOSHOW_REQ", "QUERY_COUNTRY_OFFICER_REQ", "HELP_REQ", "XL_CHALLENGE_REQ", "XL_CHALLENGE_SURE_REQ", "XL_OPEN_MEETMONSTER_BUFF_REQ", "XL_ACT_MEETMONSTER_BUFF_REQ", "XL_OPEN_SCORE_BUFF_REQ", "XL_USE_SCORE_CARD_REQ", "XL_TIMEDTASK_REQ", "XL_TIMEDTASK_PRIZE_REQ", "XL_SCORE_PRIZE_REQ", "XL_GET_SCORE_PRIZE_REQ", "XL_CROSSBILLBOARD_REQ", "XL_BILLBOARD_REQ", "XL_BUTENERGY_REQ", "XL_BUTENERGY_SURE_REQ", "XIANLING_OPEN_MAIN_REQ", "XL_USE_SKILL_REQ", "XL_QUERY_SKILL_REQ", "XL_EXIT_CHALLENGE_REQ", "XL_REFRESH_ENERGY_REQ", "XL_NOTICE_REQ" };
	}

	@Override
	public String getName() {
		return "ActivenessSubSystem";
	}

	Map<Long, Boolean> tag = new HashMap<Long, Boolean>();

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = (Player) conn.getAttachmentData("Player");
		ActivitySubSystem.logger.warn("[ActivenessSubSystem协议处理] [" + player.getLogString() + "] [" + message.getClass().toString() + "]");

		if (player == null) {
			ActivitySubSystem.logger.error("[ActivenessSubSystem协议处理] [连接上没有账号信息]");
			return null;
		}

		FairyBuddhaManager fbm = FairyBuddhaManager.getInstance();
		/******************************** 仙尊协议处理开始 ********************************************/
		if (message instanceof FAIRY_VOTE_REQ) {// 投票
//			FAIRY_VOTE_REQ req = (FAIRY_VOTE_REQ) message;
//			String result = fbm.doVote(player, req.getElectorId(), player.getCurrSoul().getCareer());
//			FAIRY_VOTE_RES res = new FAIRY_VOTE_RES(message.getSequnceNum(), result);
			// fbm.send_FAIRY_SHOW_ELECTORS_RES(player, player.getCurrSoul().getCareer(), 0);
//			return res;
		} else if (message instanceof FAIRY_DECLARE_REQ) {// 设置宣言
			FAIRY_DECLARE_REQ req = (FAIRY_DECLARE_REQ) message;
			String declaration = req.getDeclaration();
			String result = fbm.changeDeclaration(player.getId(), declaration);
			FAIRY_DECLARE_RES res = new FAIRY_DECLARE_RES(message.getSequnceNum(), result);
			// fbm.send_FAIRY_SHOW_ELECTORS_RES(player, player.getCurrSoul().getCareer(), 0);
			return res;
		} else if (message instanceof FAIRY_VOTERECORD_REQ) {// 查看投票记录
			FAIRY_VOTERECORD_REQ req = (FAIRY_VOTERECORD_REQ) message;
			int cycle = req.getCycle();
			FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [查看投票记录] [查看者职业:" + player.getCurrSoul().getCareer() + "]");
			FairyBuddhaInfo fbi = null;
			if (cycle == -1) {
				String key = fbm.getKey(-1) + fbm.KEY_参选者;
				if (fbm.disk.get(key) != null) {
					Map<Byte, List<FairyBuddhaInfo>> electorMap = (Map<Byte, List<FairyBuddhaInfo>>) fbm.disk.get(key);
					fbi = fbm.getElectorById(player.getId(), player.getCurrSoul().getCareer(), electorMap);
				}
			} else if (cycle == 0) {
				Map<Byte, List<FairyBuddhaInfo>> electorMap = fbm.getCurrentElectorMap(fbm.getKey(0) + fbm.KEY_参选者);
				fbi = fbm.getElectorById(player.getId(), player.getCurrSoul().getCareer(), electorMap);
			}
			if (fbi != null) {
				Vector<Voter> voters = fbi.getVoters();
				if (voters != null && voters.size() > 0) {
					FAIRY_VOTERECORD_RES res = new FAIRY_VOTERECORD_RES(GameMessageFactory.nextSequnceNum(), voters.toArray(new Voter[0]));
					return res;
				} else {
					player.sendError(Translate.查看记录无人投票);
				}
			}
			return null;
		} else if (message instanceof FAIRY_THANK_REQ) {// 打开答谢界面
			FairyBuddhaInfo fbi = fbm.getFariyBuddhaById(player.getCurrSoul().getCareer(), player.getId());
			if (fbi != null) {
				FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [进入仙尊答谢处理]");
				// List<Voter> voterList = fbm.getVoters(fbi.getId(), player.getCurrSoul().getCareer());

				List<Voter> voterList = fbi.getVoters();
				if (voterList != null && voterList.size() > 0) {
					if (!(voterList.size() == 1 && voterList.get(0).getId() == player.getId())) {
						FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [获取答谢列表]");
						int num = voterList.size();
						ThankAward ta = fbm.getRightThankAward(fbm.getThankAwardList(), player.getId());
						if (ta != null) {
							FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [获取答谢奖励]");
							String[] articleCNNames = ta.getArticleCNNames();
							long[] prices = ta.getPrice();
							long[] aeIds = new long[articleCNNames.length];
							for (int i = 0; i < articleCNNames.length; i++) {
								Article a = ArticleManager.getInstance().getArticleByCNname(articleCNNames[i]);
								if (a != null) {
									ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, a.getBindStyle() == 0 ? false : true, ArticleEntityManager.仙尊答谢, player, a.getColorType(), 1, true);
									if (ae != null) {
										aeIds[i] = ae.getId();
									}
								}
							}
							String key = fbm.getKey(0) + fbm.KEY_答谢奖励等级 + "_" + player.getId();
							byte level = -1;
							if (fbm.disk.get(key) != null) {
								level = (Byte) fbm.disk.get(key);
							}
							String des = Translate.translateString(Translate.已有XX人答谢, new String[][] { { Translate.STRING_1, num + "" } });
							FAIRY_THANK_RES res = new FAIRY_THANK_RES(message.getSequnceNum(), des, num, aeIds, prices, level);
							FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [发送协议打开答谢界面]");
							return res;
						}
					} else {
						player.sendError(Translate.只有自己投票);
						return null;
					}
				} else {
					player.sendError(Translate.无人投票);
					return null;
				}
			} else {
				FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [不是仙尊无权答谢]");
				player.sendError(Translate.不是仙尊);
				return null;
			}

		} else if (message instanceof FAIRY_DOTHANK_REQ) {// 一键答谢
			String result = "";
			FairyBuddhaInfo fbi = fbm.getFariyBuddhaById(player.getCurrSoul().getCareer(), player.getId());
			if (fbi != null) {
				String key = fbm.getKey(0) + fbm.KEY_答谢奖励等级 + "_" + player.getId();
				if (fbm.disk.get(key) != null) {
					result = Translate.已答谢过;
				} else {
					FAIRY_DOTHANK_REQ req = (FAIRY_DOTHANK_REQ) message;
					byte level = req.getLevel();
					ThankAward ta = fbm.getRightThankAward(fbm.getThankAwardList(), player.getId());
					if (ta != null) {
						// List<Voter> voterList = fbm.getVoters(player.getId(), player.getCurrSoul().getCareer());
						List<Voter> voterList = fbi.getVoters();
						if (voterList != null && voterList.size() > 0) {
							boolean containSelf = false;
							for (Voter v : voterList) {
								if (v.getId() == player.getId()) {
									containSelf = true;
									FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [判断答谢包含自己]");
								}
							}
							// String articleCNName = ta.getArticleCNNames()[level];
							long price = ta.getPrice()[level];
							long cost = 0;
							int voterLength = 0;
							if (containSelf) {
								cost = (voterList.size() - 1) * price;
								voterLength = voterList.size() - 1;
								FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [答谢包含自己]");

							} else {
								cost = voterList.size() * price;
								voterLength = voterList.size();
								FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [答谢不包含自己]");
							}

							if (cost > 0) {
								// 二次确认告知玩家要花费多少银子,是否答谢
								WindowManager windowManager = WindowManager.getInstance();
								MenuWindow mw = windowManager.createTempMenuWindow(600);
								mw.setDescriptionInUUB(Translate.translateString(Translate.是否答谢, new String[][] { { Translate.STRING_1, voterLength + "" }, { Translate.STRING_2, BillingCenter.得到带单位的银两(cost) } }));
								FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [答谢二次确认:" + Translate.translateString(Translate.是否答谢, new String[][] { { Translate.STRING_1, voterLength + "" }, { Translate.STRING_2, BillingCenter.得到带单位的银两(cost) } }) + "]");
								Option_Confirm_Thank o1 = new Option_Confirm_Thank(ta, voterList, cost, level, message.getSequnceNum());
								o1.setText(Translate.确定);

								Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
								o2.setText(Translate.取消);
								mw.setOptions(new Option[] { o1, o2 });
								player.addMessageToRightBag(new QUERY_WINDOW_RES(message.getSequnceNum(), mw, mw.getOptions()));
							} else {
								player.sendError(Translate.无人投票);
								return null;
							}
						}
					}
				}
			}
			if (!"".equals(result)) {
				FAIRY_DOTHANK_RES res = new FAIRY_DOTHANK_RES(message.getSequnceNum(), result);
				return res;
			} else {
				return null;
			}
		} else if (message instanceof FAIRY_SET_AWARDLEVEL_REQ) {// 设置膜拜奖励等级
			FAIRY_SET_AWARDLEVEL_REQ req = (FAIRY_SET_AWARDLEVEL_REQ) message;
			byte level = req.getLevel();
			String key = fbm.getKey(0) + fbm.KEY_膜拜奖励等级 + "_" + player.getId();
			String result = "";
			int succ = 0;
			WorshipAward wa = fbm.getRightWorshipAward();
			FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [获取当时的膜拜奖励]");
			if (wa != null) {
				Map<Byte, Long> priceMap = wa.getPriceMap();
				if (0 == level && (Byte) fbm.disk.get(key) == 0) {
					player.sendError(Translate.是默认档);
					return null;
				} else if (2 == level && (Byte) fbm.disk.get(key) == 2) {
					player.sendError(Translate.是最高档);
					return null;
				} else {
					if (level > (Byte) fbm.disk.get(key)) {
						long cost = priceMap.get((byte) level) - priceMap.get(fbm.disk.get(key));
						if (cost <= player.getSilver()) {
							WindowManager windowManager = WindowManager.getInstance();
							MenuWindow mw = windowManager.createTempMenuWindow(600);
							mw.setDescriptionInUUB(Translate.translateString(Translate.二次确认, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(cost) }, { Translate.STRING_2, wa.getAwardNameMap().get(level) } }));
							Option_Confirm_SetWorshipAwardLevel o1 = new Option_Confirm_SetWorshipAwardLevel(level, cost, wa.getAwardNameMap().get(level), message.getSequnceNum());
							o1.setText(Translate.确定);

							Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
							o2.setText(Translate.取消);
							mw.setOptions(new Option[] { o1, o2 });
							player.addMessageToRightBag(new QUERY_WINDOW_RES(message.getSequnceNum(), mw, mw.getOptions()));
						} else {
							player.sendError(Translate.余额不足);
							return null;
						}
					} else {
						result = Translate.translateString(Translate.设置低档奖励, new String[][] { { Translate.STRING_1, wa.getAwardNameMap().get((Byte) fbm.disk.get(key)) } });
						succ = 0;
					}
				}
			} else {
				result = Translate.出现异常;
				succ = 0;
			}
			if (!"".equals(result)) {
				FAIRY_SET_AWARDLEVEL_RES res = new FAIRY_SET_AWARDLEVEL_RES(message.getSequnceNum(), result, succ);
				FairyBuddhaManager.logger.warn("[" + player.getLogString() + "] [仙尊] [发送协议FAIRY_SET_AWARDLEVEL_RES]");
				return res;
			} else {
				return null;
			}
		} else if (message instanceof FAIRY_REFRESH_REQ) {// 请求刷新投票榜
			fbm.send_FAIRY_SHOW_ELECTORS_RES(player, player.getCurrSoul().getCareer(), 0);
			/******************************** 仙尊协议处理结束 ********************************************/
		}  
		else if (message instanceof END_DRAW_REQ) {
			/** 仙界宝箱协议处理开始 ********************************************/
			FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
			END_DRAW_REQ req = (END_DRAW_REQ) message;
			long id = req.getId();
			ArticleForDraw afd = ftm.tempDrawMap.get(id);
			ftm.sendToPlayer(player, afd);
		} else if (message instanceof START_DRAW_REQ) {
			FairylandTreasureManager ftm = FairylandTreasureManager.getInstance();
			FairylandBox box = ftm.getFairylandBoxList().get(0);
			ftm.send_START_DRAW_RES(player, box);
			/******************************** 仙界宝箱协议处理结束 ********************************************/
		} else if (message instanceof CHANGE_NAME_REQ) {
			/** 角色改名协议处理开始 ********************************************/
			NEW_USER_ENTER_SERVER_REQ enterReq = (NEW_USER_ENTER_SERVER_REQ) conn.getAttachmentData("NEW_USER_ENTER_SERVER_REQ");
			String[] infos = new String[3];
			infos[0] = "VALIDATE_DEVICE_FOR_CHANGENAME";
			infos[1] = player.getUsername();
			infos[2] = enterReq.getClientId();
//			int validate = GaiMingManager.validateDevice(infos);

			CHANGE_NAME_REQ req = (CHANGE_NAME_REQ) message;
			String oldName = player.getName();
			String newName = req.getNewName();
			if (newName.length() > 10) {
				player.sendError(Translate.名字太长);
				return null;
			}
			boolean canChange = true;
//			if (PlatformManager.getInstance().isPlatformOf(Platform.官方)) {// 只有国服有授权
//				// 判断授权是否超过三天(测试服也可以)
//				if (TestServerConfigManager.isTestServer() || validate == 1) {
//					canChange = true;
//				} else {
//					player.sendError(Translate.不是授权设备或授权不超过三天);
//					return null;
//				}
//			} else {
//				canChange = true;
//			}
			if (canChange) {
				// 判断角色名是否合法
				WordFilter filter = WordFilter.getInstance();
				boolean valid = false;
				if (newName != null && newName.length() > 0) {
					SpriteSubSystem sss = SpriteSubSystem.getInstance();
					// 角色名,对于0级词汇,完全过滤;对于1级词汇,等于过滤
					valid = filter.cvalid(newName, 0) && filter.evalid(newName, 1) && sss.prefixValid(newName) && sss.tagValid(newName) && sss.gmValid(newName);
				}
				if (!valid) {
					player.sendError(Translate.角色名称不合法);
					return null;
				}
				try {
					if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) { // 非国服需要判断是否存在特殊字符导致mysql存储出问题
						if (!Utils.isValidatedUTF8ForMysql(newName)) {
							player.send_HINT_REQ(Translate.角色名称不合法);
							return null;
						}
					}
				} catch (Exception e) {
					GamePlayerManager.logger.error("[角色改名] [非国服渠道判断角色名是否含有非法字符] [异常] [newName:" + newName + "]", e);
				}
				GamePlayerManager gpm = ((GamePlayerManager) GamePlayerManager.getInstance());
				synchronized (gpm.getCreate_lock()) {
					if (gpm.creatingPlayerNameSet.contains(newName)) {
						throw new Exception("角色名正在被其他玩家创建");
					}
					gpm.creatingPlayerNameSet.add(newName);
				}
				try {
					boolean exists = gpm.isExists(newName);
					if (!exists) {
						boolean succ = player.removeArticle(Translate.改名卡, "角色改名");
						if (!succ) {
							player.sendError(Translate.改名失败);
							return null;
						}
						player.setName(newName);
						player.setLastGaiMingTime(System.currentTimeMillis());
						if (player.getUsedNameList() == null) {
							List<String> usedNameList = new ArrayList<String>();
							usedNameList.add(oldName);
							player.setUsedNameList(usedNameList);
						} else {
							player.getUsedNameList().add(oldName);
						}
						gpm.em.flush(player);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						GamePlayerManager.logger.warn("[角色改名] [成功] [" + oldName + ">" + newName + "] [上次改名时间:" + sdf.format(new Date(player.getLastGaiMingTime())) + "]");

						{
							// 改名导致的一些操作修改
							// 1.仙府.
							// 2.各种称号
							UnitedServerManager.getInstance().notifyPlayerChanageName(player, oldName);
							// 3.战队
//							JJCManager.getInstance().changePlayerNameForJJCBillboardData(JJCManager.em_stat, oldName, newName);
//							JJCManager.getInstance().changePLayerNameForCross(GameConstants.getInstance().getServerName(), oldName, newName);
							// 4.仙尊,宠物
							GaiMingManager.getInstance().notifyPlayerChanageName(player, oldName);
							// 5.队伍
							Team team = player.getTeam();
							if (team != null) {
								List<Player> members = team.getMembers();
								for (Player member : members) {
									if (member != null) {
										member.addMessageToRightBag(new TEAM_QUERY_RES(GameMessageFactory.nextSequnceNum(), team.getId(), team.getCaptain().getId(), team.getAssignRule(), members.toArray(new Player[0])));
										GamePlayerManager.logger.warn("[角色改名] [刷新队伍] [" + player.getLogString() + "] [oldName:" + oldName + "] [newName:" + newName + "] [成员:" + member.getLogString() + "]");
									}
								}
							}
							// 镖车
							if (player.getFollowableNPC() != null) {
								player.getFollowableNPC().setTitle("<f color='0x71EAF0'>" + player.getName() + "</f>");
							}
						}
						player.sendError(Translate.改名成功);
						// TODO 通知朋友改名了
						return null;
					} else {
						player.sendError(Translate.角色名已存在);
						GamePlayerManager.logger.warn("[角色改名] [" + player.getLogString() + "] [oldName:" + oldName + "] [newName:" + newName + "]" + "角色名已经被使用");
						return null;
					}

				} catch (Exception e) {
					GamePlayerManager.logger.warn("[角色改名] [" + player.getLogString() + "] [oldName:" + oldName + "] [newName:" + newName + "]", e);
					e.printStackTrace();
				} finally {
					synchronized (gpm.create_lock) {
						gpm.creatingPlayerNameSet.remove(newName);
					}
				}
			}
		} else if (message instanceof QUERY_USED_NAME_REQ) {
			QUERY_USED_NAME_REQ req = (QUERY_USED_NAME_REQ) message;
			long frendId = req.getFirendId();
			Player p = PlayerManager.getInstance().getPlayer(frendId);
			List<String> usedNameList = p.getUsedNameList();
			if (usedNameList == null) {
				return new QUERY_USED_NAME_RES(message.getSequnceNum(), p.getName(), new String[0]);
			}
			if (usedNameList.size() > 3) {
				String[] names = new String[3];
				for (int i = 0; i < names.length; i++) {
					names[i] = usedNameList.get(usedNameList.size() - 1 - i);
				}
				return new QUERY_USED_NAME_RES(message.getSequnceNum(), p.getName(), names);
			}
			return new QUERY_USED_NAME_RES(message.getSequnceNum(), p.getName(), usedNameList.toArray(new String[0]));
			/******************************** 角色改名协议处理结束 ********************************************/
		} else if (message instanceof XILIAN_PUTEQUIPMENT_REQ) {
			/** 装备洗炼协议处理结束 ********************************************/
			long eId = ((XILIAN_PUTEQUIPMENT_REQ) message).getEquipmentId();
			if (eId > 0) {
				ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(eId);
				if (ae != null) {
					if (ae instanceof EquipmentEntity) {
						EquipmentEntity ee = (EquipmentEntity) ae;
						if (ee.getColorType() < 5) {
							ArticleManager.logger.warn(player.getLogString() + "[装备洗炼] [不足橙色] [id:" + ae.getId() + "] [color:" + ee.getColorType() + "]");
							player.sendError(Translate.橙色以下);
							return null;
						}
						int propertyNumbers[] = ReadEquipmentExcelManager.getInstance().equipmentColorAdditionPropertyNumbers[ee.getColorType()];
						if (ee.getAddPropInfoShow().getIntValue() == propertyNumbers[propertyNumbers.length - 1]) {
							player.sendError(Translate.洗炼已满);
						}
						ArticleManager.logger.warn(player.getLogString() + "[装备洗炼查询] [id:" + ae.getId() + "] [propertyNumbers：" + Arrays.toString(propertyNumbers) + "]");
						String propDes = ee.getAddPropInfoShow().getStringValue();
						String xiLianDes = Translate.洗炼说明;
						return new XILIAN_PUTEQUIPMENT_RES(message.getSequnceNum(), eId, propDes, xiLianDes);
					} else {
						player.sendError(Translate.translateString(Translate.查询装备洗炼请求错误回复, new String[][] { { Translate.ARTICLE_NAME_1, ae.getArticleName() } }));
					}
				} else {
					ArticleManager.logger.warn(player.getLogString() + "[装备洗炼] [未获得放入的装备]");
				}
			} else {
				ArticleManager.logger.warn(player.getLogString() + "[装备洗炼] [未放入装备或放入装备错误]");
			}
		} else if (message instanceof XILIAN_EQUIPMENT_SURE_REQ) {
			XILIAN_EQUIPMENT_SURE_REQ req = (XILIAN_EQUIPMENT_SURE_REQ) message;
			long eId = req.getEquipmentId();
			long materialId = req.getMaterialId();
			if (eId > 0 && materialId > 0) {
				ArticleManager.getInstance().doXiLianEquipment(player, eId, materialId);
			} else {
				player.sendError(Translate.未放入装备或者洗炼符);
				ArticleManager.logger.warn(player.getLogString() + "[装备洗炼确认] [未放入装备或" + Translate.洗炼符 + "]");
			}
			/******************************** 装备洗炼协议处理结束 ********************************************/
		} else if (message instanceof HUNSHI_MERGE_AIM_REQ) {
			/** 坐骑魂石协议处理开始 ********************************************/
			HunshiManager.getInstance().handle_HUNSHI_MERGE_AIM_REQ(player, (HUNSHI_MERGE_AIM_REQ) message);
		} else if (message instanceof HUNSHI_SPECIAL_REQ) {
			HunshiManager.getInstance().handle_HUNSHI_SPECIAL_REQ(player, (HUNSHI_SPECIAL_REQ) message);
		} else if (message instanceof HUNSHI_JIANDING_REQ) {
			HunshiManager.getInstance().getJianDingMaterial(player, (HUNSHI_JIANDING_REQ) message);
		} else if (message instanceof HUNSHI_JIANDING2_REQ) {
			HunshiManager.getInstance().getJianDing2Material(player, (HUNSHI_JIANDING2_REQ) message);
		} else if (message instanceof HUNSHI_JIANDING_SURE_REQ) {
			HunshiManager.getInstance().jianDingQuery(player, (HUNSHI_JIANDING_SURE_REQ) message);
		} else if (message instanceof HUNSHI_JIANDING2_SURE_REQ) {
			HunshiManager.getInstance().jianDingQuery2(player, (HUNSHI_JIANDING2_SURE_REQ) message);
		} else if (message instanceof HUNSHI_MERGE_REQ) {
			HunshiManager.getInstance().handle_HUNSHI_MERGE_REQ(player, (HUNSHI_MERGE_REQ) message);
		} else if (message instanceof HUNSHI2_OPEN_REQ) {
			Set<String> kindSet = HunshiManager.getInstance().hunshi2KindMap.keySet();
			return new HUNSHI2_OPEN_RES(message.getSequnceNum(), kindSet.toArray(new String[0]));
		} else if (message instanceof HUNSHI2_KIND_REQ) {
			String kind = ((HUNSHI2_KIND_REQ) message).getKinds();
			String des = Translate.套装石合成描述;
			List<Hunshi2Material> materialList = HunshiManager.getInstance().hunshi2KindMap.get(kind);
			if (materialList != null && materialList.size() > 0) {
				return new HUNSHI2_KIND_RES(GameMessageFactory.nextSequnceNum(), kind, materialList.toArray(new Hunshi2Material[0]), des);
			}
			return null;
		} else if (message instanceof HUNSHI_PUTON_OPEN_REQ) {
			HUNSHI_PUTON_OPEN_REQ req = (HUNSHI_PUTON_OPEN_REQ) message;
			Player p = PlayerManager.getInstance().getPlayer(req.getPlayerId());
			if (req.getPlayerId() == player.getId() && player.getLevel() < HunshiManager.openLevel) {
				player.sendError(Translate.translateString(Translate.N级开启, new String[][] { { Translate.STRING_1, HunshiManager.openLevel + "" } }));
				return null;
			} else if (req.getPlayerId() != player.getId() && p.getLevel() < HunshiManager.openLevel) {
				player.sendError(Translate.对方未开启魂石);
				return null;
			}
			long horseId = req.getHorseId();
			Horse h = HorseManager.getInstance().getHorseById(horseId, p);
			String des = Translate.魂石镶嵌描述;
			return new HUNSHI_PUTON_OPEN_RES(req.getSequnceNum(), req.getPlayerId(), h.getHunshiArray(), des);
		} else if (message instanceof HUNSHI2_PUTON_OPEN_REQ) {
			HUNSHI2_PUTON_OPEN_REQ req = (HUNSHI2_PUTON_OPEN_REQ) message;
			long horseId = req.getHorseId();
			Player p = PlayerManager.getInstance().getPlayer(req.getPlayerId());
			Horse h = HorseManager.getInstance().getHorseById(horseId, p);
			String des = Translate.套装魂石镶嵌描述;
			h.getHunshi2Cells();
			return new HUNSHI2_PUTON_OPEN_RES(req.getSequnceNum(), req.getPlayerId(), h.getHunshi2Cells(), des);
		} else if (message instanceof HUNSHI_PUTON_REQ) {
			HunshiManager.getInstance().handle_HUNSHI_PUTON_REQ(player, (HUNSHI_PUTON_REQ) message);
		} else if (message instanceof HUNSHI_PROP_VIEW_REQ) {
			HUNSHI_PROP_VIEW_REQ req = (HUNSHI_PROP_VIEW_REQ) message;
			long horseId = req.getHorseId();
			Player p = PlayerManager.getInstance().getPlayer(req.getPlayerId());
			Horse h = HorseManager.getInstance().getHorseById(horseId, p);
			int[] propertyValueAll = new int[HunshiManager.propertyInfo.length];
			if (h != null) {
				long[] hunshiArray = h.getHunshiArray();
				if (hunshiArray != null && hunshiArray.length > 0) {
					boolean hasHunshi = false;
					for (Long hunshiId : hunshiArray) {
						if (hunshiId > 0) {
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiId);
							if (ae != null && ae instanceof HunshiEntity) {
								hasHunshi = true;
								HunshiEntity he = (HunshiEntity) ae;
								int[] mainPropValue = he.getMainPropValue();
								int[] extraPropValue = he.getExtraPropValue();
								for (int i = 0; i < mainPropValue.length - 1; i++) {
									propertyValueAll[i] += mainPropValue[i] + extraPropValue[i];
								}
							}
						}
					}
					if (!hasHunshi) {
						player.sendError(Translate.未镶嵌魂石);
						return null;
					}
					HunshiManager.getInstance().logger.warn(player.getLogString() + "[魂石属性预览] [值:" + Arrays.toString(propertyValueAll) + "]");
					return new HUNSHI_PROP_VIEW_RES(req.getSequnceNum(), req.getPlayerId(), HunshiManager.getInstance().propertyInfo, propertyValueAll);
				}
			}
		} else if (message instanceof HUNSHI2_CELL_BUY_REQ) {
			HUNSHI2_CELL_BUY_REQ req = (HUNSHI2_CELL_BUY_REQ) message;
			long horseId = req.getHorseId();
			Horse h = HorseManager.getInstance().getHorseById(horseId, player);
			if (h != null) {
				int index = req.getIndex();
				HunshiXiangQian hxq = HunshiManager.getInstance().openHole2.get(index);
				boolean open[] = h.getHunshi2Open();
				if (open[index] == false && hxq.getNeedSilver() > 0) {
					if (player.getLevel() < hxq.getPlayerLevel()) {
						player.sendError(Translate.等级不足);
						return null;
					}
					// 二次确认告知玩家要花费多少银子,是否答谢
					WindowManager windowManager = WindowManager.getInstance();
					MenuWindow mw = windowManager.createTempMenuWindow(600);
					mw.setDescriptionInUUB(Translate.translateString(Translate.是否付费开启, new String[][] { { Translate.STRING_1, BillingCenter.得到带单位的银两(hxq.getNeedSilver()) } }));
					Option_Confirm_Open o1 = new Option_Confirm_Open(h, index, hxq.getNeedSilver());
					o1.setText(Translate.确定);

					Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
					o2.setText(Translate.取消);
					mw.setOptions(new Option[] { o1, o2 });
					player.addMessageToRightBag(new QUERY_WINDOW_RES(message.getSequnceNum(), mw, mw.getOptions()));
				}
			}

		} else if (message instanceof HUNSHI2_INFOSHOW_REQ) {
			HUNSHI2_INFOSHOW_REQ req = (HUNSHI2_INFOSHOW_REQ) message;
			Player p = PlayerManager.getInstance().getPlayer(req.getPlayerId());
			if (p == null) {
				return null;
			}
			Horse h = HorseManager.getInstance().getHorseById(req.getHorseId(), p);
			if (h == null) {
				return null;
			}
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(req.getHunshiId());
			if (ae != null && ae instanceof HunshiEntity) {
				return new HUNSHI2_INFOSHOW_RES(req.getSequnceNum(), req.getHunshiId(), ((HunshiEntity) ae).getInfoShow(p, h));
			}
		} else if (message instanceof HUNSHI2_PROP_VIEW_REQ) {
			HUNSHI2_PROP_VIEW_REQ req = (HUNSHI2_PROP_VIEW_REQ) message;
			Player p = PlayerManager.getInstance().getPlayer(req.getPlayerId());
			long horseId = req.getHorseId();
			Horse h = HorseManager.getInstance().getHorseById(horseId, p);
			int[] propertyValueAll = new int[HunshiManager.propertyInfo.length];
			try {
				if (h != null) {
					HunshiManager hm = HunshiManager.getInstance();
					long[] hunshi2Array = h.getHunshi2Array();
					boolean hasHunshi = false;
					if (hunshi2Array != null && hunshi2Array.length > 0) {
						List<HunshiSuit> suitList = hm.getHunshiSuitList(hunshi2Array);
						HunshiManager.getInstance().logger.debug(player.getLogString() + "[魂石套装属性预览] [获取套装]");
						String[] kinds = new String[suitList.size()];
						String[] des = new String[suitList.size()];
						if (suitList != null && suitList.size() > 0) {
							for (int i = 0; i < suitList.size(); i++) {
								HunshiSuit suit = suitList.get(i);
								kinds[i] = suit.getSuitName();
								des[i] = suit.getInfoShow();
								for (int j = 0; j < suit.getPropId().length; j++) {
									propertyValueAll[suit.getPropId()[j]] += suit.getPropValue()[j];
								}
							}
						}
						for (Long hunshiId : hunshi2Array) {
							if (hunshiId > 0) {
								hasHunshi = true;
								ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshiId);
								if (ae != null && ae instanceof HunshiEntity) {
									HunshiEntity he = (HunshiEntity) ae;
									int[] mainPropValue = he.getMainPropValue();
									int[] extraPropValue = he.getExtraPropValue();
									for (int i = 0; i < mainPropValue.length - 1; i++) {
										propertyValueAll[i] += mainPropValue[i] + extraPropValue[i];
									}
								}
							}
						}
						if (!hasHunshi) {
							player.sendError(Translate.未镶嵌魂石);
							return null;
						}
						HunshiManager.getInstance().logger.debug(player.getLogString() + "[魂石套装属性预览] [值:" + Arrays.toString(propertyValueAll) + "]");
						return new HUNSHI2_PROP_VIEW_RES(req.getSequnceNum(), req.getPlayerId(), kinds, des, hm.propertyInfo, propertyValueAll);
					}
				}
			} catch (Exception e) {
				HunshiManager.getInstance().logger.error(player.getLogString() + "[魂石套装属性预览异常] [值:" + Arrays.toString(propertyValueAll) + "]", e);
				e.printStackTrace();
			}
		} else if (message instanceof QUERY_COUNTRY_OFFICER_REQ) {
//			CountryManager.getInstance().发送官员名称(player);
//			// 弹框问是否给官员投票
//			if (player.countryVoteFlag) {
//				return null;
//			}
//			if (player.getLevel() <= 40) {
//				return null;
//			}
//			String result = CountryManager.getInstance().投票合法性判断(player, new boolean[9], (byte) 0);
//			if (result == null) {
//				WindowManager windowManager = WindowManager.getInstance();
//				MenuWindow mw = windowManager.createTempMenuWindow(600);
//				mw.setDescriptionInUUB(Translate.投票确认);
//				Option_Confirm_Vote o1 = new Option_Confirm_Vote();
//				o1.setText(Translate.确定);
//
//				Option_UnConfirm_Close o2 = new Option_UnConfirm_Close();
//				o2.setText(Translate.取消);
//				mw.setOptions(new Option[] { o1, o2 });
//				player.countryVoteFlag = true;
//				player.addMessageToRightBag(new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions()));
//			}
		}

		/******************************** 仙灵大会协议处理开始 ********************************************/
		XianLingManager xlm = XianLingManager.instance;
		String helpMString = "<f color='#ffffff' size='36'>1、</f><f color='#ff0000' size='36'>150级</f><f color='#ffffff' size='36'>开放助阵系统，默认开放可以助阵一个宠物，</f><f color='#ff0000' size='36'>220级</f><f color='#ffffff' size='36'>开放第二只宠物助阵；</f>\n<f color='#ffffff' size='36'>2、宠物助阵所加成属性为</f><f color='#ff0000' size='36'>除血量外</f><f color='#ffffff' size='36'>所有属性（按比例加成到人物属性）</f>\n<f color='#ffffff' size='36'>3、所加成属性为绝对值，</f><f color='#ff0000' size='36'>不包含百分比</f>；\n<f color='#ffffff' size='36'>4、</f><f color='#ff0000' size='36'>参战宠物</f><f color='#ffffff' size='36'>不可以助阵；</f>\n<f color='#ffffff' size='36'>5、宠物默认助阵属性加成比例为</f><f color='#ff0000' size='36'>5%</f><f color='#ffffff' size='36'>,220级开放加成比例升级功能（根据人物不同等级可提升加成比例不同）</f>\n";
		if (message instanceof HELP_REQ) {
			HELP_REQ req = (HELP_REQ) message;
			int helpType = req.getHelpType();
			String des = "";
			if (helpType == 0) {
				des = xlm.translateMap.get("仙灵主面板帮助");
			} else if (helpType == 1) {
				des = xlm.translateMap.get("仙灵排行榜帮助");
			} else if (helpType == 2) {
				des = Translate.仙灵技能介绍;
			}else if (helpType == 3) {
				des =helpMString;
			}else if (helpType == 4) {
				des = "<f color='#ffffff' size='36'>1、</f><f color='#ff0000' size='36'>1人物220级开启宠物助战加成升级功能；</f>"+
"<f color='#ffffff' size='36'>2、</f><f color='#ff0000' size='36'>可升级7次，前6次，每次增加2%属性加成，最后一次增加3%属性加成，宠物助战最高属性加成为20%；</f>"+
"<f color='#ffffff' size='36'>3、</f><f color='#ff0000' size='36'>每次升级需要消耗不同数量的经验及八卦石，具体消耗数量以每级提示为主。</f>";
			}else if(helpType == 5){
				des = "<f size='37'>————参与条件————</f>\n<f size='30'>全天24小时，81级以上，所有玩家均可参与</f>\n\n<f size='30'>每周最多进入15次（每天最多5次，所有副本共享次数）</f>\n\n<f size='30'>飞仙卡用户增加5次</f>\n\n<f size='37'>————进入说明————</f>\n<f size='30'>玩家必须要2人以上组队才可以进入副本，进入副本时必须在：</f>\n\n<f size='30'>昆华古城、昆仑圣殿、上州仙境、九州天城、万法遗迹、万法之源、灭魔神域，</f>\n<f size='30'>上述7张地图之一，且不处于战斗状态，否则进入失败。</f>";
			}else if(helpType == 6){
				des = "<f size='37'>————参与条件————</f>\n<f size='30'>全天24小时，81级以上，且有家族的所有玩家均可参与</f>\n<f size='30'>每人每天限制进入1次（所有副本共用次数）</f>\n<f size='30'>70级副本要求家族大于等于1级</f>\n<f size='30'>110级副本要求家族等级大于等于5级</f>\n<f size='30'>150级副本要求家族等级大于等于10级</f>\n<f size='30'>190级副本要求家族等级大于等于15级</f>\n<f size='30'>220级副本要求家族等级大于等级20级</f>\n\n<f size='37'>————进入说明————</f>\n<f size='30'>玩家必须在非组队状态下进入副本</f>\n<f size='30'>进入副本时必须在：</f>\n<f size='30'>昆华古城、昆仑圣殿、上州仙境、九州天城、万法遗迹、万法之源、灭魔神域，</f>\n<f size='30'>上述7张地图之一，</f>\n<f size='30'>且不处于战斗状态，否则进入失败。</f>";
			}
			if (xlm.logger.isDebugEnabled()) xlm.logger.debug("[type=" + helpType + "]");
			return new HELP_RES(req.getSequnceNum(), des);
		} else if (message instanceof XIANLING_OPEN_MAIN_REQ) {
			xlm.handle_OPEN_MAIN_REQ(player);
		} else if (message instanceof XL_CHALLENGE_REQ) {
			return xlm.handle_XL_CHALLENGE_REQ((XL_CHALLENGE_REQ) message, player);
		} else if (message instanceof XL_CHALLENGE_SURE_REQ) {
			xlm.handle_open_guanqia_sure((XL_CHALLENGE_SURE_REQ) message, player);
		} else if (message instanceof XL_TIMEDTASK_REQ) {
			return xlm.handle_XL_TIMEDTASK_REQ((XL_TIMEDTASK_REQ) message, player);
		} else if (message instanceof XL_TIMEDTASK_PRIZE_REQ) {
			return xlm.handle_XL_TIMEDTASK_PRIZE_REQ(player);
		} else if (message instanceof XL_OPEN_MEETMONSTER_BUFF_REQ) {
			return xlm.handle_OPEN_MEETMONSTER_BUFF_REQ(player);
		} else if (message instanceof XL_ACT_MEETMONSTER_BUFF_REQ) {
			xlm.handle_ACT_MEETMONSTER_BUFF_REQ(player);
		} else if (message instanceof XL_OPEN_SCORE_BUFF_REQ) {
			return xlm.handle_OPEN_SCORE_BUFF_REQ(player);
		} else if (message instanceof XL_USE_SCORE_CARD_REQ) {
			xlm.handle_USE_SCORE_CARD_REQ((XL_USE_SCORE_CARD_REQ) message, player);
		} else if (message instanceof XL_BUTENERGY_REQ) {
			return xlm.handle_BUTENERGY_REQ(player);
		} else if (message instanceof XL_BUTENERGY_SURE_REQ) {
			xlm.handle_BUTENERGY_SURE_REQ(player);
		} else if (message instanceof XL_SCORE_PRIZE_REQ) {
			return xlm.handle_SCORE_PRIZE_REQ(player);
		} else if (message instanceof XL_GET_SCORE_PRIZE_REQ) {
			return xlm.handle_GET_SCORE_PRIZE_REQ((XL_GET_SCORE_PRIZE_REQ) message, player);
		} else if (message instanceof XL_CROSSBILLBOARD_REQ) {
			return xlm.handle_CROSSBILLBOARD_REQ(player);
		} else if (message instanceof XL_BILLBOARD_REQ) {
			return xlm.handle_BILLBOARD_REQ(player);
		} else if (message instanceof XL_USE_SKILL_REQ) {
			return XianLingChallengeManager.getInstance().handle_USE_SKILL_REQ((XL_USE_SKILL_REQ) message, player);
		} else if (message instanceof XL_QUERY_SKILL_REQ) {
			return xlm.handle_QUERY_SKILL_REQ(player);
		} else if (message instanceof XL_EXIT_CHALLENGE_REQ) {
			xlm.handle_EXIT_CHALLENGE_REQ(player);
		} else if (message instanceof XL_REFRESH_ENERGY_REQ) {
			PlayerXianLingData xianlingData = player.getXianlingData();
			if (xianlingData != null) {
				return new XL_REFRESH_ENERGY_RES(message.getSequnceNum(), xianlingData.getEnergy(), xianlingData.getNextResumeEnergy());
			}
		} else if (message instanceof XL_NOTICE_REQ) {
			int type = ((XL_NOTICE_REQ) message).getNoticeType();
			xlm.sendNotice(type, player);
		}
		/******************************** 仙灵大会协议处理开始 ********************************************/
		/******************************** 活跃度协议处理开始 ********************************************/
		if (ActivenessManager.open == false) {
			ActivitySubSystem.logger.warn("[活跃度活动未开启]");
			return null;
		}

		ActivenessManager am = ActivenessManager.getInstance();
		// ActivenessConfig aconf = am.conf;
		if (ActivitySubSystem.logger.isDebugEnabled()) {
			ActivitySubSystem.logger.warn("[活跃度活动]玩家请求信息:[{}] [{}]", player.getName(), message.getTypeDescription());
		}
		if (message instanceof ACTIVENESS_LIST_REQ) {
			ACTIVENESS_LIST_RES res = am.getACTIVENESS_LIST_RES(player);
			ActivitySubSystem.logger.error(player.getLogString() + "ACTIVENESS_LIST_REQ is " + res == null ? "null" : "not null");
			return res;
		} else if (message instanceof ACTIVENESS_DES_REQ) {
			ACTIVENESS_DES_REQ req = (ACTIVENESS_DES_REQ) message;
			ActivityForActiveness afa = am.getAfaById(req.getActivityID());
			return new ACTIVENESS_DES_RES(message.getSequnceNum(), req.getActivityID(), afa.getName(), afa.getDetaildes());
		} else if (message instanceof GET_PRIZE_REQ) {
			GET_PRIZE_REQ req = (GET_PRIZE_REQ) message;
			PlayerActivenessInfo pai = player.getActivenessInfo();
			if (player.getActivenessInfo().getDayActiveness() >= req.getActiveness()) {
				/** 因为邮件内容不一样，分情况处理领奖 */
				if (req.getActiveness() == am.getAwardLevel()[0] && !pai.getGotten()[0]) {// 第一档奖励
					String articleName = am.getAwardNameFtr()[0];
					Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(a, a.getColorType());
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getActiveness() } }), Translate.translateString(Translate.第一档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }), 0, 0, 0, "活跃度活动");
							boolean gotten[] = pai.getGotten();
							gotten[0] = true;
							pai.setGotten(gotten);
							ActivityManagers.getInstance().changeActivityNums(player);
							pai.setLastGetSignTime(SystemTime.currentTimeMillis());
							player.sendError(Translate.translateString(Translate.活跃度领奖提示, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
							ActivitySubSystem.logger.error(player.getLogString() + "[活跃度活动] [发送邮件] [" + articleName + "]");
						}
					}
				} else if (req.getActiveness() == am.getAwardLevel()[am.getAwardLevel().length - 2] && !pai.getGotten()[am.getAwardLevel().length - 2]) {// 倒数第二档奖励
					String articleName = am.getAwardNameFtr()[am.getAwardLevel().length - 2];
					Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(a, a.getColorType());
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getActiveness() } }), Translate.translateString(Translate.倒数第二档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() }, { Translate.STRING_4, "" + (am.getAwardLevel()[am.getAwardLevel().length - 1] - req.getActiveness()) } }), 0, 0, 0, "活跃度活动");
							boolean gotten[] = pai.getGotten();
							gotten[am.getAwardLevel().length - 2] = true;
							pai.setGotten(gotten);
							ActivityManagers.getInstance().changeActivityNums(player);
							pai.setLastGetTime(SystemTime.currentTimeMillis());
							player.sendError(Translate.translateString(Translate.活跃度领奖提示, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
							ActivitySubSystem.logger.error(player.getLogString() + "[活跃度活动] [发送邮件] [" + articleName + "]");
						}
					}
				} else if (req.getActiveness() == am.getAwardLevel()[am.getAwardLevel().length - 1] && !pai.getGotten()[am.getAwardLevel().length - 1]) {// 最后一档奖励
					String articleName = am.getAwardNameFtr()[am.getAwardLevel().length - 1];
					Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(a, a.getColorType());
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getActiveness() } }), Translate.translateString(Translate.最后档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }), 0, 0, 0, "活跃度活动");
							boolean gotten[] = pai.getGotten();
							gotten[am.getAwardLevel().length - 1] = true;
							pai.setGotten(gotten);
							ActivityManagers.getInstance().changeActivityNums(player);
							pai.setLastGetTime(SystemTime.currentTimeMillis());
							player.sendError(Translate.translateString(Translate.活跃度领奖提示, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
							ActivitySubSystem.logger.error(player.getLogString() + "[活跃度活动] [发送邮件] [" + articleName + "]");
						}
					}
				} else {
					for (int i = 1; i < am.getAwardLevel().length - 2; i++) {
						if (req.getActiveness() == am.getAwardLevel()[i] && !pai.getGotten()[i]) {
							String articleName = am.getAwardNameFtr()[i];
							Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
							if (a != null) {
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
								if (ae != null) {
									int colorValue = ArticleManager.getColorValue(a, a.getColorType());
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getActiveness() } }), Translate.translateString(Translate.中间档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() }, { Translate.STRING_4, "" + (am.getAwardLevel()[i + 1] - req.getActiveness()) } }), 0, 0, 0, "活跃度活动");
									boolean gotten[] = pai.getGotten();
									gotten[i] = true;
									pai.setGotten(gotten);
									ActivityManagers.getInstance().changeActivityNums(player);
									pai.setLastGetTime(SystemTime.currentTimeMillis());
									player.sendError(Translate.translateString(Translate.活跃度领奖提示, new String[][] { { Translate.STRING_1, "" + req.getActiveness() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
									ActivitySubSystem.logger.error(player.getLogString() + "[活跃度活动] [发送邮件] [" + articleName + "]");
								}
							}
						}
					}
				}
				am.sendBright(player);
			} else {
				player.sendError(Translate.领奖活跃度不足);
				ActivitySubSystem.logger.warn(player.getLogString() + "[活跃度不足] [要领取的奖励档：" + req.getActiveness() + "] [现有活跃度：" + pai.getDayActiveness() + "]");
			}
			return null;
		} else if (message instanceof LOTTERY_REQ) {
			Lottery lottery = am.findRightLottery(System.currentTimeMillis(), GameConstants.getInstance().getServerName());
			if (lottery != null) {
				am.setLotteryNamesFtr(lottery.getLotteryCNNames());
				am.setLotteryColors(lottery.getLotteryColors());
				am.setLotteryNums(lottery.getLotteryNums());
				am.setLotteryProbs(lottery.getLotteryProbs());
			}
			
			long[] lotteryID = new long[am.getLotteryNamesFtr().length];
			for (int i = 0; i < am.getLotteryNamesFtr().length; i++) {
				Article a = ArticleManager.getInstance().getArticleByCNname(am.getLotteryNamesFtr()[i]);
				if (a != null) {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, am.getLotteryColors()[i], 1, true);
					if (ae != null) {
						lotteryID[i] = ae.getId();
					} else {
						ActivitySubSystem.logger.warn(player.getLogString() + "抽奖创建物品失败" + am.getLotteryNamesFtr()[i]);
					}
				}
			}
			
			PlayerActivenessInfo pai = player.getActivenessInfo();
			tag.put(pai.getId(), true);
			if (pai.getDayActiveness() < am.getLotteryLevel()[0]) {
				player.sendError(Translate.抽奖活跃度不足);
				ActivitySubSystem.logger.warn(player.getLogString() + "[活跃度不满足抽奖条件] [今日活跃度：" + pai.getDayActiveness() + "]");
				return new LOTTERY_RES(message.getSequnceNum(), lotteryID, ArrayUtils.toPrimitive(am.getLotteryNums()));
			} else if (pai.getHasLottery() < pai.getCanLottery()) {
				ActivitySubSystem.logger.warn(player.getLogString() + "[发送抽奖协议成功,lotteryID:" + Arrays.toString(lotteryID) + "] [lotteryNames:" + Arrays.toString(am.getLotteryNamesFtr()) + "]");
				return new LOTTERY_RES(message.getSequnceNum(), lotteryID, ArrayUtils.toPrimitive(am.getLotteryNums()));
			} else if (pai.getHasLottery() == am.getLotteryNamesFtr().length) {
				player.sendError(Translate.今日抽奖次数已满);
				ActivitySubSystem.logger.warn(player.getLogString() + "[今日抽奖次数已满] [今日已抽奖次数：" + pai.getHasLottery() + "]");
				return new LOTTERY_RES(message.getSequnceNum(), lotteryID, ArrayUtils.toPrimitive(am.getLotteryNums()));
			} else {
				player.sendError(Translate.当前抽奖次数已满);
				ActivitySubSystem.logger.warn(player.getLogString() + "[当前抽奖次数已满] [今日已抽奖次数：" + pai.getHasLottery() + "]");
				return new LOTTERY_RES(message.getSequnceNum(), lotteryID, ArrayUtils.toPrimitive(am.getLotteryNums()));
			}
		} else if (message instanceof LOTTERY_FINISH_REQ) {
			PlayerActivenessInfo pai = player.getActivenessInfo();
			if (tag.get(pai.getId())) {
				if (pai.getHasLottery() < pai.getCanLottery()) {
					String lotteryName = am.doLottery();
					int articleNum = 0;
					int articleColor = 0;
					long resultID = 0l;
					Article article = ArticleManager.getInstance().getArticleByCNname(lotteryName);
					if (article != null) {
						for (int i = 0; i < am.getLotteryNamesFtr().length; i++) {
							if (lotteryName.equals(am.getLotteryNamesFtr()[i])) {
								articleNum = am.getLotteryNums()[i];
								articleColor = am.getLotteryColors()[i];
							}
						}
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.活动, player, articleColor, 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(article, articleColor);
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { articleNum }, Translate.抽奖邮件标题, Translate.translateString(Translate.抽奖邮件内容, new String[][] { { Translate.STRING_1, "<f color='" + colorValue + "'>" + ae.getArticleName() + "</f>" } }), 0, 0, 0, "活跃度活动");
							pai.setHasLottery(pai.getHasLottery() + 1);
							tag.put(pai.getId(), false);
							pai.setLastLotteryTime(SystemTime.currentTimeMillis());
							ActivitySubSystem.logger.error(player.getLogString() + "[活跃度抽奖成功] [发送邮件] [" + lotteryName + "]");
							resultID = ae.getId();

						}
					}
					ActivitySubSystem.logger.warn(player.getLogString() + "[抽奖结束] [获得物品id:" + resultID + "]");
					am.sendBright(player);
					return new LOTTERY_FINISH_RES(message.getSequnceNum(), resultID);
				} else {
					player.sendError(Translate.当前抽奖次数已满);
					ActivitySubSystem.logger.warn(player.getLogString() + "[玩家可能点多次] [今日已抽奖次数：" + pai.getHasLottery() + "]");
				}
			}
		} else if (message instanceof LOTTERY_FINISH_NEW_REQ) {
			PlayerActivenessInfo pai = player.getActivenessInfo();
			if (tag.get(pai.getId())) {
				if (pai.getHasLottery() < pai.getCanLottery()) {
					String lotteryName = am.doLottery();
					int articleNum = 0;
					int articleColor = 0;
					long resultID = 0l;
					int resultNum = 0;
					Article article = ArticleManager.getInstance().getArticleByCNname(lotteryName);
					if (article != null) {
						for (int i = 0; i < am.getLotteryNamesFtr().length; i++) {
							if (lotteryName.equals(am.getLotteryNamesFtr()[i])) {
								articleNum = am.getLotteryNums()[i];
								articleColor = am.getLotteryColors()[i];
							}
						}
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.活动, player, articleColor, 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(article, articleColor);
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { articleNum }, Translate.抽奖邮件标题, Translate.translateString(Translate.抽奖邮件内容, new String[][] { { Translate.STRING_1, "<f color='" + colorValue + "'>" + ae.getArticleName() + "</f>" } }), 0, 0, 0, "活跃度活动");
							pai.setHasLottery(pai.getHasLottery() + 1);
							tag.put(pai.getId(), false);
							pai.setLastLotteryTime(SystemTime.currentTimeMillis());
							ActivitySubSystem.logger.error(player.getLogString() + "[活跃度抽奖成功] [发送邮件] [" + lotteryName + "]");
							resultID = ae.getId();
							resultNum = articleNum;
						}
					}
					ActivitySubSystem.logger.warn(player.getLogString() + "[抽奖结束] [获得物品id:" + resultID + "]");
					am.sendBright(player);
					return new LOTTERY_FINISH_NEW_RES(message.getSequnceNum(), resultID, articleNum);
				} else {
					player.sendError(Translate.当前抽奖次数已满);
					ActivitySubSystem.logger.warn(player.getLogString() + "[玩家可能点多次] [今日已抽奖次数：" + pai.getHasLottery() + "]");
				}
			}
		} else if (message instanceof SIGN_REQ) {
			int[] caInfo = am.getCalenderInfo(System.currentTimeMillis());
			PlayerActivenessInfo pai = player.getActivenessInfo();
			boolean[] hasSign = pai.getHasSign();
			if (hasSign != null) {
				if (hasSign[caInfo[2] - 1]) {
					player.sendError(Translate.已签过到了);
				} else {
					hasSign[caInfo[2] - 1] = true;
					pai.setHasSign(hasSign);
					// 活跃度统计
					am.record(player, ActivenessType.每日签到);
					ActivitySubSystem.logger.warn(player.getLogString() + "[签到成功] [签到日期:" + caInfo[0] + "-" + caInfo[2] + "]");
					try {
						EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), RecordAction.签到次数, 1L });
						EventRouter.getInst().addEvent(evt);
					} catch (Exception e) {
						PlayerAimManager.logger.error("[目标系统] [统计玩家签到次数异常] [" + player.getLogString() + "]", e);
					}
					return new SIGN_RES(message.getSequnceNum(), caInfo[0], caInfo[2]);
				}
			} else {
				ActivitySubSystem.logger.warn(player.getLogString() + "[玩家的签到信息不存在]");
			}
			return null;
		} else if (message instanceof GET_SIGNAWARD_REQ) {
			GET_SIGNAWARD_REQ req = (GET_SIGNAWARD_REQ) message;
			PlayerActivenessInfo pai = player.getActivenessInfo();
			if (am.getSignDays(player) >= req.getLevel()) {
				/** 因为邮件内容不一样，分情况处理领奖 */
				if (req.getLevel() == am.getSignAwardLevel()[0] && !pai.getHasGotSign()[0]) {// 第一档奖励
					String articleName = am.getSignAwardNameFtr()[0];
					Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(a, a.getColorType());
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.签到领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getLevel() } }), Translate.translateString(Translate.签到第一档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }), 0, 0, 0, "活跃度活动");
							boolean getSign[] = pai.getHasGotSign();
							getSign[0] = true;
							pai.setHasGotSign(getSign);
							pai.setLastGetSignTime(SystemTime.currentTimeMillis());
							player.sendError(Translate.translateString(Translate.签到领奖提示, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
							ActivitySubSystem.logger.error(player.getLogString() + "[签到] [发送邮件] [" + articleName + "]");
						}
					}
				} else if (req.getLevel() == am.getSignAwardLevel()[am.getSignAwardLevel().length - 2] && !pai.getHasGotSign()[am.getSignAwardLevel().length - 2]) {// 倒数第二档奖励
					String articleName = am.getSignAwardNameFtr()[am.getSignAwardLevel().length - 2];
					Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(a, a.getColorType());
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.签到领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getLevel() } }), Translate.translateString(Translate.签到倒数第二档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() }, { Translate.STRING_4, "" + (am.getSignAwardLevel()[am.getSignAwardLevel().length - 1] - req.getLevel()) } }), 0, 0, 0, "活跃度活动");
							boolean getSign[] = pai.getHasGotSign();
							getSign[am.getSignAwardLevel().length - 2] = true;
							pai.setHasGotSign(getSign);
							pai.setLastGetSignTime(SystemTime.currentTimeMillis());
							player.sendError(Translate.translateString(Translate.签到领奖提示, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
							ActivitySubSystem.logger.error(player.getLogString() + "[签到] [发送邮件] [" + articleName + "]");
						}
					}
				} else if (req.getLevel() == am.getSignAwardLevel()[am.getSignAwardLevel().length - 1] && !pai.getHasGotSign()[am.getSignAwardLevel().length - 1]) {// 最后一档奖励
					String articleName = am.getSignAwardNameFtr()[am.getSignAwardLevel().length - 1];
					Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
					if (a != null) {
						ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
						if (ae != null) {
							int colorValue = ArticleManager.getColorValue(a, a.getColorType());
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.签到领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getLevel() } }), Translate.translateString(Translate.签到最后档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }), 0, 0, 0, "活跃度活动");
							boolean getSign[] = pai.getHasGotSign();
							getSign[am.getSignAwardLevel().length - 1] = true;
							pai.setHasGotSign(getSign);
							pai.setLastGetSignTime(SystemTime.currentTimeMillis());
							player.sendError(Translate.translateString(Translate.签到领奖提示, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
							ActivitySubSystem.logger.error(player.getLogString() + "[签到] [发送签到邮件] [" + articleName + "]");
						}
					}
				} else {
					for (int i = 1; i < am.getSignAwardLevel().length - 2; i++) {
						if (req.getLevel() == am.getSignAwardLevel()[i] && !pai.getHasGotSign()[i]) {
							String articleName = am.getSignAwardNameFtr()[i];
							Article a = ArticleManager.getInstance().getArticleByCNname(articleName);
							if (a != null) {
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
								if (ae != null) {
									int colorValue = ArticleManager.getColorValue(a, a.getColorType());
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, Translate.translateString(Translate.签到领奖邮件标题, new String[][] { { Translate.STRING_1, "" + req.getLevel() } }), Translate.translateString(Translate.签到中间档邮件内容, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() }, { Translate.STRING_4, "" + (am.getSignAwardLevel()[i + 1] - req.getLevel()) } }), 0, 0, 0, "活跃度活动");
									boolean getSign[] = pai.getHasGotSign();
									getSign[i] = true;
									pai.setHasGotSign(getSign);
									pai.setLastGetSignTime(SystemTime.currentTimeMillis());
									player.sendError(Translate.translateString(Translate.签到领奖提示, new String[][] { { Translate.STRING_1, "" + req.getLevel() }, { Translate.STRING_2, "" + colorValue }, { Translate.STRING_3, ae.getArticleName() } }));
									ActivitySubSystem.logger.error(player.getLogString() + "[签到] [发送邮件] [" + articleName + "]");
								}
							}
						}
					}
				}
			} else {
				player.sendError(Translate.签到天数不足);
				ActivitySubSystem.logger.warn(player.getLogString() + "[签到天数不足] [要领取的奖励档：" + req.getLevel() + "] [这月已签到天数：" + am.getSignDays(player) + "]");
			}
			return null;
		}
		/******************************** 活跃度协议处理结束 ********************************************/
		return null;
	}

	private void pushMessageToGame(Player player, RequestMessage message, Object attachment) {
		Game game = player.getCurrentGame();
		if (game != null) {
			game.messageQueue.push(new PlayerMessagePair(player, message, attachment));
		} 
	}

	@Override
	public boolean handleResponseMessage(Connection conn, RequestMessage request, ResponseMessage response) throws ConnectionException, Exception {
		return false;
	}

	public Map<Long, Boolean> getTag() {
		return tag;
	}

	public void setTag(Map<Long, Boolean> tag) {
		this.tag = tag;
	}

}
