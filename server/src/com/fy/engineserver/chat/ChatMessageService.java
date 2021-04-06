package com.fy.engineserver.chat;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.LivingObject;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.buff.Buff;
import com.fy.engineserver.datasource.buff.BuffTemplateManager;
import com.fy.engineserver.datasource.buff.Buff_Silence2;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.enterlimit.EnterLimitManager;
import com.fy.engineserver.enterlimit.EnterLimitManager.PlayerRecordType;
import com.fy.engineserver.gateway.MieshiGatewayClientService;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.Option_Confirm_Chat_Need_Article;
import com.fy.engineserver.menu.Option_UseCancel;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.message.CHAT_FAILED_REQ;
import com.fy.engineserver.message.CHAT_MESSAGE_REQ;
import com.fy.engineserver.message.CONFIRM_WINDOW_REQ;
import com.fy.engineserver.message.DENY_USER_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.JUBAO_PLAYER_REQ;
import com.fy.engineserver.message.JUBAO_PLAYER_RES;
import com.fy.engineserver.message.REPORT_CHAT_REQ;
import com.fy.engineserver.shop.ShopManager;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.sprite.Team;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Message;
import com.xuanzhi.tools.transport.RequestMessage;

/**
 * 消息传送接口服务
 * 
 */
public class ChatMessageService {

	public static Logger logger = LoggerFactory.getLogger(ChatMessageService.class);
	public static Logger siliaologger = LoggerFactory.getLogger(ChatMessage.class);

	private static ChatMessageService mself;

	public static final int CHAT_TYPE_系统提示消息 = 0;

	public static final int CHAT_TYPE_系统电视消息 = 1;

	public static final int CHAT_TYPE_系统滚动消息 = 2;
	protected PlayerManager playerManager;
	protected ChatChannelManager chatChannelManager;
	protected ShopManager shopManager;
	protected BillingCenter billingCenter;

	public String chatGoodsShopName = "";
	protected boolean worldNeedChatArticle = false;
	protected boolean colorWorldNeedChatArticle = false;
	public static String chatNeedArticleName = Translate.喊话所需物品;
	public static String chatColorWorldNeedArticleName = Translate.彩世喊话所需道具;

	protected Hashtable<String, List<ChatMessage>> gmMessageMap;
	protected List<ChatMessage> worldMessageList = Collections.synchronizedList(new LinkedList<ChatMessage>());
	protected List<ChatMessage> ziweiMessageList = Collections.synchronizedList(new LinkedList<ChatMessage>());
	protected List<ChatMessage> riyueMessageList = Collections.synchronizedList(new LinkedList<ChatMessage>());
	protected File banedMapFile;
	protected DefaultDiskCache banedPlayerMap;

	private SocialManager socialManager;

	public static final byte CHAT_ERROR_检查通过 = 0;
	public static final byte CHAT_ERROR_TYPE_发言频率过快 = 1;
	public static final byte CHAT_ERROR_TYPE_级别不足 = 2;
	public static final byte CHAT_ERROR_TYPE_被禁言 = 3;
	public static final byte CHAT_ERROR_TYPE_vip级别太低 = 4;

	public SocialManager getSocialManager() {
		return socialManager;
	}

	public void setSocialManager(SocialManager socialManager) {
		this.socialManager = socialManager;
	}

	public static ChatMessageService getInstance() {
		return mself;
	}

	public void initialize() {
		
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		gmMessageMap = new Hashtable<String, List<ChatMessage>>();
		serverName = GameConstants.getInstance().getServerName();
		banedPlayerMap = new DefaultDiskCache(banedMapFile, null, "banedPlayerMap", 10 * 365 * 24 * 3600 * 1000L);
		mself = this;
		ServiceStartRecord.startLog(this);
	}

	public void setChatGoodsShopName(String chatGoodsShopName) {
		this.chatGoodsShopName = chatGoodsShopName;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setChatChannelManager(ChatChannelManager chatChannelManager) {
		this.chatChannelManager = chatChannelManager;
	}

	public void setShopManager(ShopManager shopManager) {
		this.shopManager = shopManager;
	}

	public void setBillingCenter(BillingCenter billingCenter) {
		this.billingCenter = billingCenter;
	}

	public void setBanedMapFile(File banedMapFile) {
		this.banedMapFile = banedMapFile;
	}

	/**
	 * 玩家是否被禁言
	 * @param playerId
	 * @return
	 */
	public boolean isPlayerBaned(long playerId) {
		Long endtime = (Long) banedPlayerMap.get(playerId);
		if (endtime != null && endtime >= com.fy.engineserver.gametime.SystemTime.currentTimeMillis()) {
			return true;
		} else if (endtime != null) {
			banedPlayerMap.remove(playerId);
		}
		return false;
	}

	/**
	 * 玩家禁言
	 * @param playerId
	 * @param hour
	 *            禁止发言的小时数，<=0为永久
	 */
	public void banPlayer(long playerId, int hour) {
		long endtime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (hour <= 0) {
			endtime = Long.MAX_VALUE;
		} else {
			endtime += hour * 60 * 60 * 1000;
		}
		banedPlayerMap.put(playerId, endtime);
		if (logger.isInfoEnabled()) {
			logger.info("[禁言] [playerid:{}] [小时:{}]", new Object[] { playerId, hour });
		}
	}

	/**
	 * 解除禁言
	 * @param playerId
	 */
	public void relieveBanedPlayer(long playerId) {
		banedPlayerMap.remove(playerId);
		if (logger.isInfoEnabled()) {
			logger.info("[解除禁言] [playerid:{}]", new Object[] { playerId });
		}
	}

	/**
	 * 确认进行世界频道喊话
	 * @param playerId
	 */
	public void confirmSendWorldChatMessage(Player player, ChatMessage message) {
		if (player == null) {
			logger.error("[发送世界消息] [第二步确认发送] [失败:玩家不存在]");
			return;
		}
		if (message == null) {
			if (logger.isWarnEnabled()) logger.warn("[发送世界消息] [第二步确认发送] [失败:没有找到此用户的喊话消息]");
			return;
		}

		if (player.getLevel() < 10) {
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, "您的等级不够，不能发彩视！");
			player.addMessageToRightBag(req);
			return;
		}

		int type = ChatChannelType.WORLD;
		String needArticleName = chatNeedArticleName;
		if (message.getSort() == ChatChannelType.WORLD) {
			type = ChatChannelType.WORLD;
			needArticleName = chatNeedArticleName;
		} else if (message.getSort() == ChatChannelType.COLOR_WORLD) {
			type = ChatChannelType.COLOR_WORLD;

			needArticleName = chatColorWorldNeedArticleName;
		} else {
			return;
		}
		ChatChannel channel = chatChannelManager.getChatChannel(type);
		Knapsack[] knapsacks = player.getKnapsacks_common();
		boolean succ = false;
		if (knapsacks != null) {
			for (int i = 0; i < knapsacks.length; i++) {
				Knapsack knapsack = knapsacks[i];
				if (knapsack != null) {
					int count = knapsack.countArticle(needArticleName);
					if (count >= 1) {
						ArticleEntity ae = knapsack.remove(knapsack.indexOf(needArticleName), "聊天删除", true);
						if (ae != null) {

							// 统计
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "聊天删除", null);

							succ = true;
							if (ae.getArticleName().equals(needArticleName)) {
								AchievementManager.getInstance().record(player, RecordAction.彩世次数);
								// 活跃度统计
								ActivenessManager.getInstance().record(player, ActivenessType.彩世喊话);
								if (AchievementManager.logger.isWarnEnabled()) {
									AchievementManager.logger.warn("[成就统计获得万里神行符] [" + player.getLogString() + "] [" + needArticleName + "]");
								}
							}
							break;
						}
						{
							// 感恩节聊天活动
							ActivitySubSystem.thinkgivingChatCheckAndPrize(player, message.getMessageText());
							// EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
						}
					}
				}
			}
		}
		if (!succ) {
			Knapsack knapsack = player.getKnapsacks_fangBao();
			if (knapsack != null) {
				int count = knapsack.countArticle(needArticleName);
				if (count >= 1) {
					ArticleEntity ae = knapsack.remove(knapsack.indexOf(needArticleName), "聊天删除", true);
					if (ae != null) {

						// 统计
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, 1, "聊天删除", null);
						succ = true;
						if (ae.getArticleName().equals(needArticleName)) {
							AchievementManager.getInstance().record(player, RecordAction.彩世次数);
							// 活跃度统计
							ActivenessManager.getInstance().record(player, ActivenessType.彩世喊话);
							if (AchievementManager.logger.isWarnEnabled()) {
								AchievementManager.logger.warn("[成就统计获得万里神行符] [" + player.getLogString() + "] [" + needArticleName + "]");
							}
						}
					}
				}
			}
		}

		if (succ) {
			// 给频道增加玩家的一个发送行为
			channel.addPlayerSendAction(player.getId(), message.getChatTime());
			Player players[] = playerManager.getOnlinePlayers();
			for (Player pp : players) {
				if (!pp.isChatChannelOpenning(type) || pp.isBlackuser(player.getId()) || (pp.getCurrentGame() == null && !pp.isInCrossServer()) || pp.getDuelFieldState() > 0) {
					continue;
				}
				ChatMessage msg = message.clone();
				CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
				pp.addMessageToRightBag(newReq);
			}
			if (message.getSort() == ChatChannelType.WORLD) {
				worldMessageList.add(message.clone());
				if (worldMessageList.size() > 1000) {
					worldMessageList.remove(0);
				}
			}

			sendChatReport(Integer.valueOf(message.getSort()), player, message);

			EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);

			if (logger.isWarnEnabled()) logger.warn("[发送世界消息] [第二步确认发送] [成功] [发送人:{}] [接受人数:{}] [{}]", new Object[] { message.getSenderName(), players.length, message.getMessageText() });
		} else {
			String description = Translate.没有喊话道具;
			try {
				description = Translate.translateString(Translate.没有喊话道具描述, new String[][] { { Translate.ARTICLE_NAME_1, needArticleName } });
			} catch (Exception ex) {

			}
			HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, description);
			player.addMessageToRightBag(req);
			if (logger.isInfoEnabled()) logger.info("[发送世界消息] [第二步确认发送] [失败:余额不足] [发送人:{}]", new Object[] { player.getName() });
		}
	}

	/**
	 * 确认进行阵营频道喊话
	 * @param playerId
	 */
	public void confirmSendPolcampChatMessage(Player player) {
	}

	/**
	 * 玩家发送一个聊天信息
	 * @param channelId
	 * @param posterId
	 * @param receiverId
	 * @param content
	 * @param sendTime
	 * @throws Exception
	 */

	public static boolean isJianCha = true;

	public void sendPlayerMessage(ChatMessage message) throws Exception {
		message.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		ChatChannel channel = chatChannelManager.getChatChannel(message.getSort());
		Player player = playerManager.getPlayer(message.getSenderId());
		if (channel == null) {
			if (logger.isInfoEnabled()) logger.info("[发送聊天消息] [频道不存在] [{}]", new Object[] { message.getSort() });
			CHAT_FAILED_REQ req = new CHAT_FAILED_REQ(GameMessageFactory.nextSequnceNum(), message.getSort(), Translate.频道不存在);
			player.addMessageToRightBag(req);
			if (logger.isInfoEnabled()) logger.info("[发送内容包含非法信息] [--] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
			return;
		}
		
		if(ChatMessageService.getInstance().isSilence(player.getId())>0 && ChatMessageService.getInstance().isSilence(player.getId()) < 3){
			sendSilenceMess(player,message,"发送世界消息");
			return;
		}
		
		if (isJianCha) {
			boolean isError = false;
			if (message.getSort() == ChatChannelType.WORLD) {
				if (message.getMessageText().indexOf(Translate.国家A名字) < 0 && message.getMessageText().indexOf(Translate.国家B名字) < 0 && message.getMessageText().indexOf(Translate.国家C名字) < 0) {
					isError = true;
					logger.warn("[世界聊天消息不正确] [{}] [{}]", new Object[] { player.getLogString(), message.getMessageText() });
				}
				if (message.getMessageText().indexOf(player.getName()) < 0) {
					isError = true;
					logger.warn("[世界聊天消息不正确] [{}] [{}]", new Object[] { player.getLogString(), message.getMessageText() });
				}
			}
			if (isError) {
				Integer sendNum = EnterLimitManager.playerSendErrorWorldChat.get(player.getId());
				if (sendNum == null) {
					sendNum = new Integer(0);
				}
				sendNum = sendNum.intValue() + 1;
				EnterLimitManager.playerSendErrorWorldChat.put(player.getId(), sendNum);
				if (sendNum % 2 == 0) {
					DENY_USER_REQ denyreq = new DENY_USER_REQ(GameMessageFactory.nextSequnceNum(), "", player.getUsername(), "修改世界聊天", GameConstants.getInstance().getServerName() + "-修改世界聊天", false, true, false, 0, 24 * (sendNum / 2));
					MieshiGatewayClientService.getInstance().sendMessageToGateway(denyreq);
					player.getConn().close();
				}
				return;
			}
		}
		if (message.getMessageText().indexOf("size") > 0) {
			message.setMessageText(message.getMessageText().replaceAll("size", ""));
			return;
		}
		if (message.getMessageText().indexOf("style") > 0) {
			message.setMessageText(message.getMessageText().replaceAll("style", ""));
			return;
		}
		// 判断发送内容是否干净
		WordFilter filter = WordFilter.getInstance();
		// 聊天内容,只需对0级词汇,完全过滤
		// boolean valid = filter.cvalid(message.getMessageText(), 0);
		// if (!valid) {
		// CHAT_FAILED_REQ req = new CHAT_FAILED_REQ(GameMessageFactory.nextSequnceNum(), message.getSort(), Translate.text_2579);
		// player.addMessageToRightBag(req);
		// if(logger.isInfoEnabled())
		// logger.info("[发送内容包含非法信息] [--] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new
		// Object[]{message.getSenderName(),message.getReceiverName(),ChatChannelType.getTypeDesp(message.getSort()),message.getMessageText(),message.getChatTime()});
		// return;
		// }

		// String messageContent = filter.nvalidAndReplace(message.getMessageText(), 1, "@#\\$%^\\&\\*");
		String messageContent = getFilteredContent(message.getMessageText(), "@#\\$%^\\&\\*");
		if (!判断长度是否允许发送(messageContent)) {
			CHAT_FAILED_REQ req = new CHAT_FAILED_REQ(GameMessageFactory.nextSequnceNum(), message.getSort(), Translate.聊天文字太长);
			player.addMessageToRightBag(req);
			if (logger.isInfoEnabled()) logger.info("[发送被禁止] [聊天文字太长] [发送人:{}] [接受人:{}] [频道类型:{}] [是否频道禁言:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), channel.isPlayerBaned(player.getId()), message.getMessageText(), message.getChatTime() });
			return;
		}
		message.setMessageText(messageContent);

		// 判断玩家是否允许发送
		byte result = channel.isPlayerAllowSend(player.getId(), player.getLevel(), message.getChatTime());
		if (isPlayerBaned(player.getId()) || result != CHAT_ERROR_检查通过) {
			String content = null;
			if (channel.isPlayerBaned(player.getId())) {
				content = Translate.text_2581;
			}
			if (result != CHAT_ERROR_检查通过) {
				if (result == CHAT_ERROR_TYPE_发言频率过快) {
					content = Translate.发言频率过快;
				} else if (result == CHAT_ERROR_TYPE_级别不足) {
					content = Translate.级别不足;
				} else if (result == CHAT_ERROR_TYPE_被禁言) {
					content = Translate.text_2581;
				}else if (result == CHAT_ERROR_TYPE_vip级别太低) {
					content = "vip级别太低";
				}
			}
			if (content == null) {
				content = Translate.发言频率过快;
			}
			CHAT_FAILED_REQ req = new CHAT_FAILED_REQ(GameMessageFactory.nextSequnceNum(), message.getSort(), content);
			player.addMessageToRightBag(req);
			if (logger.isInfoEnabled()) logger.info("[发送被禁止] [--] [发送人:{}] [接受人:{}] [频道类型:{}] [是否频道禁言:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), channel.isPlayerBaned(player.getId()), message.getMessageText(), message.getChatTime() });
			return;
		}
		int channelType = message.getSort();
		if (channelType == ChatChannelType.WORLD) {
			// 世界频道的喊话，不立即转发，先记录喊话内容，给客户端发送是否有喊话道具
			if (worldNeedChatArticle) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60);
				String description = Translate.世界聊天需要道具;
				try {
					description = Translate.translateString(Translate.世界聊天需要道具描述, new String[][] { { Translate.ARTICLE_NAME_1, chatNeedArticleName } });
				} catch (Exception ex) {

				}
				mw.setDescriptionInUUB(description);
				Option_Confirm_Chat_Need_Article ccna = new Option_Confirm_Chat_Need_Article();
				ccna.setChatMessage(message);
				ccna.setText(Translate.确定);

				Option_UseCancel cancel = new Option_UseCancel();
				cancel.setText(Translate.取消);

				mw.setOptions(new Option[] { ccna, cancel });
				CONFIRM_WINDOW_REQ confirm = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
				player.addMessageToRightBag(confirm);
				return;
			} else {
				// 给频道增加玩家的一个发送行为
				channel.addPlayerSendAction(player.getId(), message.getChatTime());
				Player players[] = playerManager.getOnlinePlayers();
				for (Player pp : players) {
					if (!pp.isChatChannelOpenning(ChatChannelType.WORLD) || pp.isBlackuser(player.getId()) || (pp.getCurrentGame() == null && !pp.isInCrossServer()) || pp.getDuelFieldState() > 0) {
						continue;
					}
					if ((pp.getCurrentGame() == null && !pp.isInCrossServer()) || pp.getDuelFieldState() > 0) {
						continue;
					}
					if (pp.hiddenChatMessage) {
						continue;
					}
					ChatMessage msg = message.clone();
					CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
					pp.addMessageToRightBag(newReq);
					{
						// 感恩节聊天活动
						ActivitySubSystem.thinkgivingChatCheckAndPrize(player, msg.getMessageText());
						// EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
					}
					if (logger.isInfoEnabled()) logger.info("[发送世界消息] [无需喊话道具] [成功] [发送人:{}] [接受人:{}] [{}]", new Object[] { message.getSenderName(), pp.getName(), message.getMessageText() });
				}
				worldMessageList.add(message.clone());
				if (worldMessageList.size() > 1000) {
					worldMessageList.remove(0);
				}
				if (logger.isWarnEnabled()) logger.warn("[发送世界消息] [无需喊话道具] [成功] [发送人:{}] [接受人数:{}] [{}]", new Object[] { message.getSenderName(), players.length, message.getMessageText() });
			}
			return;
		}
		if (channelType == ChatChannelType.COLOR_WORLD) {
			// 世界频道的喊话，不立即转发，先记录喊话内容，给客户端发送是否有喊话道具
			if (colorWorldNeedChatArticle) {
				MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(60);
				String description = Translate.彩世聊天需要道具;
				try {
					description = Translate.translateString(Translate.彩世聊天需要道具描述, new String[][] { { Translate.ARTICLE_NAME_1, chatColorWorldNeedArticleName } });
				} catch (Exception ex) {

				}
				mw.setDescriptionInUUB(description);
				Option_Confirm_Chat_Need_Article ccna = new Option_Confirm_Chat_Need_Article();
				ccna.setChatMessage(message);
				ccna.setText(Translate.确定);

				Option_UseCancel cancel = new Option_UseCancel();
				cancel.setText(Translate.取消);

				mw.setOptions(new Option[] { ccna, cancel });
				CONFIRM_WINDOW_REQ confirm = new CONFIRM_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getDescriptionInUUB(), mw.getOptions());
				player.addMessageToRightBag(confirm);
				return;
			} else {
				// 给频道增加玩家的一个发送行为
				channel.addPlayerSendAction(player.getId(), message.getChatTime());
				
				Player players[] = playerManager.getOnlinePlayers();
				for (Player pp : players) {
					if (!pp.isChatChannelOpenning(ChatChannelType.WORLD) || pp.isBlackuser(player.getId()) || (pp.getCurrentGame() == null && !pp.isInCrossServer()) || pp.getDuelFieldState() > 0) {
						continue;
					}
					if ((pp.getCurrentGame() == null && !pp.isInCrossServer()) || pp.getDuelFieldState() > 0) {
						continue;
					}
					ChatMessage msg = message.clone();
					CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
					pp.addMessageToRightBag(newReq);
					if (logger.isInfoEnabled()) logger.info("[发送世界消息] [无需喊话道具] [成功] [发送人:{}] [接受人:{}] [{}]", new Object[] { message.getSenderName(), pp.getName(), message.getMessageText() });
				}
				worldMessageList.add(message.clone());
				if (worldMessageList.size() > 1000) {
					worldMessageList.remove(0);
				}
				if (logger.isWarnEnabled()) logger.warn("[发送世界消息] [无需喊话道具] [成功] [发送人:{}] [接受人数:{}] [{}]", new Object[] { message.getSenderName(), players.length, message.getMessageText() });
			}
			return;
		}
		if (channelType == ChatChannelType.COUNTRY) {
			try {
				Player p = PlayerManager.getInstance().getPlayer(player.getId());
				if(p.getVipLevel() < 11){
					p.sendError("vip级别太低");
					return; 
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			// 给频道增加玩家的一个发送行为
			channel.addPlayerSendAction(player.getId(), message.getChatTime());
			Player players[] = playerManager.getOnlinePlayers();
			for (Player pp : players) {
				if (!pp.isChatChannelOpenning(ChatChannelType.COUNTRY) || pp.getCountry() != player.getCountry() || pp.isBlackuser(player.getId()) || (pp.getCurrentGame() == null && !pp.isInCrossServer()) || pp.getDuelFieldState() > 0) {
					continue;
				}
				if (pp.hiddenChatMessage) {
					continue;
				}
				ChatMessage msg = message.clone();
				CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
				pp.addMessageToRightBag(newReq);
			}
			if (player.getCountry() == 1) {
				ziweiMessageList.add(message.clone());
				if (ziweiMessageList.size() > 1000) {
					ziweiMessageList.remove(0);
				}
			} else {
				riyueMessageList.add(message.clone());
				if (riyueMessageList.size() > 1000) {
					riyueMessageList.remove(0);
				}
			}
			if (logger.isWarnEnabled()) logger.warn("[发送阵营消息] [无需喊话道具] [成功] [发送人:{}] [接受人数:{}] [{}]", new Object[] { message.getSenderName(), players.length, message.getMessageText() });
			// }
			{
				// 感恩节聊天活动
				ActivitySubSystem.thinkgivingChatCheckAndPrize(player, message.getMessageText());
				EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
			}
			return;
		}
		// 给频道增加玩家的一个发送行为
		channel.addPlayerSendAction(player.getId(), message.getChatTime());
		if (channelType == ChatChannelType.TEAM) {
			// 获取所有队伍玩家
			if (player.getTeam() == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您没有队伍);
				player.addMessageToRightBag(hreq);
				return;
			}
			Player players[] = player.getTeamMembers();
			for (Player pp : players) {
				if (pp.isChatChannelOpenning(channel.getType()) && !pp.isBlackuser(player.getId()) && pp.getCurrentGame() != null && pp.getDuelFieldState() == 0) {
					ChatMessage msg = message.clone();
					CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
					pp.addMessageToRightBag(newReq);
					// 测试日志
					if (logger.isInfoEnabled()) logger.info("[发送队伍消息] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { msg.getSenderName(), pp.getName(), ChatChannelType.getTypeDesp(msg.getSort()), msg.getMessageText(), msg.getChatTime() });
				}
			}
			{
				// 感恩节聊天活动
				ActivitySubSystem.thinkgivingChatCheckAndPrize(player, message.getMessageText());
				EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
			}
			return;
		}
		if (channelType == ChatChannelType.JIAZU) {
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(player.getJiazuId());
			if (jiazu == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您没有家族);
				player.addMessageToRightBag(hreq);
				return;
			}
			String mText = message.getMessageText();
			int index = mText.indexOf("</i></f>");
			if (index >= 0) {
				int otherIndex = mText.indexOf("<i ");
				if (otherIndex >= 0) {
					String newText = mText.substring(0, otherIndex);
					newText += "</f>";
					newText += mText.substring(otherIndex, index);
					newText += "</i>";
					newText += mText.substring(index + "</i></f>".length(), mText.length());
					message.setMessageText(newText);
				}
			}
			Player ps[] = playerManager.getOnlinePlayerByJiazu(jiazu);
			for (Player splayer : ps) {
				if (!splayer.isOnline() || !splayer.isChatChannelOpenning(ChatChannelType.JIAZU) || splayer.isBlackuser(player.getId()) || (splayer.getCurrentGame() == null && !splayer.isInCrossServer()) || splayer.getDuelFieldState() > 0) {
					continue;
				}
				if (splayer.hiddenChatMessage) {
					continue;
				}
				ChatMessage msg = message.clone();
				CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
				splayer.addMessageToRightBag(newReq);
				if (logger.isInfoEnabled()) logger.info("[发送帮会消息] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { msg.getSenderName(), splayer.getName(), ChatChannelType.getTypeDesp(msg.getSort()), msg.getMessageText(), msg.getChatTime() });
			}
			{
				// 感恩节聊天活动
				ActivitySubSystem.thinkgivingChatCheckAndPrize(player, message.getMessageText());
				EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
			}
			return;
		}
		if (channelType == ChatChannelType.NEARBY) {
			
			try {
				Player p = PlayerManager.getInstance().getPlayer(player.getId());
				if(p.getVipLevel() < 12){
					p.sendError("vip级别太低");
					return; 
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			// 获取周围的玩家
			Game Ogame = player.getCurrentGame();
			if (Ogame == null) {
				return;
			}
			LivingObject objs[] = Ogame.getLivingObjects();
			for (LivingObject obj : objs) {
				if (obj instanceof Player) {
					Player pp = (Player) obj;
					if (pp != null && pp.isOnline() && !pp.hiddenChatMessage && pp.isChatChannelOpenning(ChatChannelType.NEARBY) && !pp.isBlackuser(player.getId()) && pp.getCurrentGame() != null && pp.getDuelFieldState() == 0) {
						ChatMessage msg = message.clone();
						CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
						pp.addMessageToRightBag(newReq);
						// 测试日志
						if (logger.isInfoEnabled()) logger.info("[发送周围消息] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { msg.getSenderName(), pp.getName(), ChatChannelType.getTypeDesp(msg.getSort()), msg.getMessageText(), msg.getChatTime() });
					}
				}
			}
			{
				// 感恩节聊天活动
				ActivitySubSystem.thinkgivingChatCheckAndPrize(player, message.getMessageText());
				EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
			}
			return;
		}
		if (channelType == ChatChannelType.ZONG) {
			try {
				Player p = PlayerManager.getInstance().getPlayer(player.getId());
				if(p.getVipLevel() < 11){
					p.sendError("vip级别太低");
					return; 
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			ZongPai zp = ZongPaiManager.getInstance().getZongPaiByPlayerId(player.getId());
			if (zp == null) {
				HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.您没有宗族);
				player.addMessageToRightBag(hreq);
				return;
			}
			Player[] ps = playerManager.getOnlineInZongpai(zp.getId());
			ChatMessage msg = message.clone();
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
			if (ps != null) {
				for (Player p : ps) {
					if (p != null && !p.hiddenChatMessage && p.isOnline() == true && p.isChatChannelOpenning(ChatChannelType.ZONG) && p.getCurrentGame() != null && !p.isBlackuser(player.getId())) {
						p.addMessageToRightBag(newReq);
					}
				}
			}

			ZongPaiManager.getInstance().说话增加繁荣度(player);

			if (logger.isInfoEnabled()) logger.info("[发送宗派消息] [发送人:{}] [接受人:] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { player.getName(), ChatChannelType.getTypeDesp(msg.getSort()), msg.getMessageText(), msg.getChatTime() });
			{
				// 感恩节聊天活动
				ActivitySubSystem.thinkgivingChatCheckAndPrize(player, message.getMessageText());
				EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
			}
			return;
		}
		if (channelType == ChatChannelType.PERSONAL) {
			
			// 如果是gm，那么加入gm的消息列表
			String receiver = message.getReceiverName();
			if (receiver.toLowerCase().matches("gm\\d{2}")) {
				List<ChatMessage> gmms = gmMessageMap.get(receiver);
				if (gmms == null) {
					gmms = Collections.synchronizedList(new LinkedList<ChatMessage>());
					gmMessageMap.put(receiver, gmms);
				}
				gmms.add(message.clone());
				// 内存中只保留1000条信息
				if (gmms.size() > 1000) {
					gmms.remove(0);
				}
				if (logger.isInfoEnabled()) {
					logger.info("[接收GM消息] [{}] [{}] [{}]", new Object[] { receiver, message.getSenderName(), message.getMessageText() });
				}
				if (siliaologger.isWarnEnabled()) {
					siliaologger.warn("[接收GM消息] [{}] [{}] [{}]", new Object[] { receiver, message.getSenderName(), message.getMessageText() });
				}
				return;
			}
			
//			Player pp = playerManager.getPlayer(message.getReceiverName());
			Player pp = playerManager.getPlayer(message.getReceiverId());
			// 看看对方是否在线
			if (pp == null || !pp.isOnline()) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2583);
				player.addMessageToRightBag(req);
				if (logger.isInfoEnabled()) logger.info("[发送私聊消息] [失败:目标玩家不在线] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
				return;
			}
			if (pp.isBlackuser(player.getId())) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.对方已把您加入黑名单您不能发出聊天信息);
				player.addMessageToRightBag(req);
				if (logger.isInfoEnabled()) logger.info("[发送私聊消息] [失败:对方已把您加入黑名单] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
				return;
			}
			if (!pp.isChatChannelOpenning(ChatChannelType.PERSONAL)) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2585 + pp.getName() + Translate.text_2586);
				player.addMessageToRightBag(req);
				if (logger.isInfoEnabled()) logger.info("[发送私聊消息] [失败:目标玩家私聊频道关闭] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
				return;
			}
			if (pp.getDuelFieldState() > 0) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2585 + pp.getName() + Translate.text_2587);
				player.addMessageToRightBag(req);
				if (logger.isInfoEnabled()) logger.info("[发送私聊消息] [失败:目标玩家正在比武赛场] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
				return;
			}
			try {
				long start = System.currentTimeMillis();
				// 进行防骗子处理，规则，如果发现发送者不是接收者的好友，并且发现接收者好友列表中有玩家名称和发送者近似，那么认为有骗子嫌疑，对接收者进行提示
				if (!pp.isFriend(message.getSenderId())) {
					List<Long> flist = SocialManager.getInstance().getFriendlist(pp);
					if (flist.size() == 0) {
						if (logger.isDebugEnabled()) {
							logger.debug("[不符合嫌疑要求] [接收方好友列表为空] [sname:" + player.getName() + "] [rname:" + pp.getName() + "]");
						}
					}
					PlayerSimpleInfoManager psm = PlayerSimpleInfoManager.getInstance();
					boolean suspect = false;
					String sname = null;
					for (Long id : flist) {
						PlayerSimpleInfo sp = psm.getInfoById(id);
						if (sp == null) {
							if (logger.isDebugEnabled()) {
								logger.debug("[不符合嫌疑要求] [无法找到简单信息] [sname:" + player.getName() + "] [rname:" + pp.getName() + "] [fid:" + id + "]");
							}
							continue;
						}
						int distance = StringUtils.getLevenshteinDistance(player.getName(), sp.getName());
						double approx = new Double(distance) * 100 / new Double(sp.getName().length());
						if (approx < 35 || (player.getName().length() > 2 && distance <= 2) || (approx <= 50 && player.getName().length() == 2)) {
							suspect = true;
							sname = sp.getName();
							if (logger.isDebugEnabled()) {
								logger.debug("[符合嫌疑要求] [sname:" + player.getName() + "] [fname:" + sp.getName() + "] [distance:" + distance + "] [approx:" + approx + "]");
							}
							break;
						} else {
							if (logger.isDebugEnabled()) {
								logger.debug("[不符合嫌疑要求] [sname:" + player.getName() + "] [fname:" + sp.getName() + "] [distance:" + distance + "] [approx:" + approx + "]");
							}
						}
					}
					if (suspect) {
						ChatMessage cm = message.clone();
						cm.setMessageText("<f color='0xff0000'>友情提示：对方不是您的好友，但是名字和您的好友“" + sname + "”相似，请警惕骗子行为！</f>");
						CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), cm, cm.getAccessoryItem(), cm.getAccessoryTask());
						pp.addMessageToRightBag(newReq);
						logger.warn("[疑似骗子私聊] [发送方:" + player.getLogString() + "] [接收方:" + pp.getLogString() + "] [相似好友:" + sname + "] [" + (System.currentTimeMillis() - start) + "ms]");
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("[不符合嫌疑要求] [双方为好友关系] [sname:" + player.getName() + "] [rname:" + pp.getName() + "]");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
			pp.addMessageToRightBag(newReq);
			socialManager.addTemporaryFriend(pp, player.getId());
			ChatMessage msg = message.clone();

			msg.setReceiverId(pp.getId());
			CHAT_MESSAGE_REQ newReq2 = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
			player.addMessageToRightBag(newReq2);
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[发送私聊消息] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
			if (siliaologger.isWarnEnabled()) {
				siliaologger.warn("[发送私聊消息] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
			}
			EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
			return;
		}
		if (channelType == ChatChannelType.FRIEND) {
//			Player pp = playerManager.getPlayer(message.getReceiverName());
			Player pp = playerManager.getPlayer(message.getReceiverId());
			// 看看对方是否在线
			if (pp == null || !pp.isOnline()) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2583);
				player.addMessageToRightBag(req);
				if (logger.isInfoEnabled()) logger.info("[发送好友聊天消息] [失败:目标玩家不在线] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
				return;
			}
			if (pp.isBlackuser(player.getId())) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2584);
				player.addMessageToRightBag(req);
				if (logger.isInfoEnabled()) logger.info("[发送好友聊天消息] [失败:对方已把您加入黑名单] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
				return;
			}
			if (pp.getDuelFieldState() > 0) {
				HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2585 + pp.getName() + Translate.text_2587);
				player.addMessageToRightBag(req);
				if (logger.isInfoEnabled()) logger.info("[发送好友聊天消息] [失败:目标玩家正在比武赛场] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
				return;
			}
			Player f = this.socialManager.getFriend(pp, player.getId());
			if (f != null) {
				// 好友
				CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
				pp.addMessageToRightBag(newReq);
				CHAT_MESSAGE_REQ newReq2 = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
				player.addMessageToRightBag(newReq2);
				if (pp.autoBack != 0) {
					int autoBack = pp.autoBack;
					if (autoBack <= 0 || autoBack > Player.autoBackString.length) {
						if (logger.isInfoEnabled()) logger.info("[发送好友聊天消息] [失败:自动回复越界] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
						return;
					}
					ChatMessage message1 = message.clone();
					message1.setMessageText(Player.autoBackString[autoBack]);
					CHAT_MESSAGE_REQ newReq3 = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message1, message.getAccessoryItem(), message.getAccessoryTask());
				}

			} else {
				// 私聊
				if (!pp.isChatChannelOpenning(ChatChannelType.PERSONAL)) {
					HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, Translate.text_2585 + pp.getName() + Translate.text_2586);
					player.addMessageToRightBag(req);
					if (logger.isInfoEnabled()) logger.info("[发送私聊消息] [失败:目标玩家私聊频道关闭] [发送人:{}] [接受人:{}] [频道类型:{}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
					return;
				}
				message.setSort(ChatChannelType.PERSONAL);
				CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
				pp.addMessageToRightBag(newReq);
				CHAT_MESSAGE_REQ newReq2 = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
				player.addMessageToRightBag(newReq2);
				EnterLimitManager.setValues(player, PlayerRecordType.聊天次数);
			}
		}

		if (channelType == ChatChannelType.GROUP) {
			// long chatGroupId = message.getGroupId();
			// ChatGroup cg = this.socialManager.getPlayerChatGroup(player, chatGroupId);
			// if(cg == null){
			// HINT_REQ req = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte)0, Translate.translate.text_没有聊天组);
			// player.addMessageToRightBag(req);
			// if(logger.isInfoEnabled())
			// logger.info("[发送好友群组聊天消息] [失败:目标群组不存在] [发送人:"+message.getSenderName()+"] [接受组:"+message.getReceiverName()+"] [频道类型:"+ChatChannelType.getTypeDesp(message.getSort())+"] [内容:"+message.getMessageText()+"] [发送时间:"+message.getChatTime()+"]");
			// return;
			// }
			//
			// List<Long> ids = cg.getMembers();
			// for(long id : ids){
			// Player pp = PlayerManager.getInstance().getPlayer(id);
			// if(pp.isOnline()){
			// if( pp.isBlackuser(player.getId())){
			// return;
			// }
			// CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(),message,message.getAccessoryItem(),message.getAccessoryTask());
			// pp.addMessageToRightBag(newReq);
			// }
			// }
			//
			// CHAT_MESSAGE_REQ newReq2 = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(),message,message.getAccessoryItem(),message.getAccessoryTask());
			// player.addMessageToRightBag(newReq2);;
		}
		// 测试日志
		if (logger.isInfoEnabled()) logger.info("[没有找到消息频道] [发送人:{}] [接受人:{}] [频道类型:{{},{}}] [内容:{}] [发送时间:{}]", new Object[] { message.getSenderName(), message.getReceiverName(), message.getSort(), ChatChannelType.getTypeDesp(message.getSort()), message.getMessageText(), message.getChatTime() });
	}

	public static boolean sendToGateway = true;
	public static String serverName = null;
	public static List<Integer> sendChannelTypeList = new ArrayList<Integer>();
	static {
		sendChannelTypeList.add(ChatChannelType.WORLD);
		sendChannelTypeList.add((int) ChatChannelType.COLOR_WORLD);
	}
	MieshiGatewayClientService mgs;

	/**
	 * 向网关发送聊天信息
	 * @param channelType
	 * @param player
	 * @param message
	 */
	public void sendChatReport(Integer channelType, Player player, ChatMessage message) {
		if (sendToGateway) {
			if (sendChannelTypeList.contains(channelType)) {
				if (mgs == null) {
					mgs = MieshiGatewayClientService.getInstance();
				}
				Message m = new REPORT_CHAT_REQ(GameMessageFactory.nextSequnceNum(), serverName, player.getUsername(), player.getId(), player.getName(), new String[] { message.getMessageText() });
				mgs.sendMessageToGateway(m);
				logger.warn("[discard_message] [MieshiGatewayClientService] [queue_is_full] [" + m.getTypeDescription() + "]");
			}
		}
	}

	/**
	 * 是否超过规定长度80
	 * @return
	 */
	public boolean 判断长度是否允许发送(String msg) {
		String strs[] = msg.split("</[fi]>");
		int len = 0;
		if (strs.length > 1) {
			for (int i = 1; i < strs.length; i++) {
				if (strs[i] != null) {
					if (strs[i].indexOf("onclick") >= 0 || strs[i].indexOf("partName") >= 0 || strs[i].indexOf("imagePath") >= 0) {

					} else {
						len += strs[i].length();
					}
				}
			}
		}
		if (len > 80) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isWorldNeedChatArticle() {
		return worldNeedChatArticle;
	}

	public void setWorldNeedChatArticle(boolean worldNeedChatArticle) {
		this.worldNeedChatArticle = worldNeedChatArticle;
	}

	public boolean isColorWorldNeedChatArticle() {
		return colorWorldNeedChatArticle;
	}

	public void setColorWorldNeedChatArticle(boolean colorWorldNeedChatArticle) {
		this.colorWorldNeedChatArticle = colorWorldNeedChatArticle;
	}

	/**
	 * 系统给队伍发送一个消息
	 * @param teamId
	 * @param content
	 */
	public void sendMessageToTeam(Team team, ChatMessage msg) {
		ChatChannel channel = chatChannelManager.getChatChannel(ChatChannelType.TEAM);
		Player players[] = team.getMembers().toArray(new Player[0]);
		for (Player pp : players) {
			if (pp.isChatChannelOpenning(channel.getType()) && pp.getCurrentGame() != null) {
				ChatMessage message = msg.clone();
				message.setSort(ChatChannelType.TEAM);
				CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
				pp.addMessageToRightBag(newReq);
				// 测试日志
				if (logger.isInfoEnabled()) logger.info("[send_to_team] [{}] [{}] [{}]", new Object[] { message.getSenderName(), pp.getName(), message.getMessageText() });
			}
		}
	}

	/**
	 * 向家族发送消息
	 * @param gang
	 * @param content
	 * @param senderName
	 */
	public void sendMessageToJiazu(Jiazu jiazu, String content, String senderName) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player ps[] = playerManager.getOnlinePlayerByJiazu(jiazu);
		if (ps == null) {
			return;
		}
		for (Player splayer : ps) {
			if (!splayer.isOnline() || !splayer.isChatChannelOpenning(ChatChannelType.JIAZU) || splayer.getCurrentGame() == null) {
				continue;
			}
			ChatMessage mes = new ChatMessage();
			mes.setChatTime(now);
			mes.setMessageText(content);
			mes.setSenderId(-1);
			mes.setSenderName(senderName);
			mes.setSort(ChatChannelType.JIAZU);
			ChatMessageItem item = new ChatMessageItem();
			ChatMessageTask task = new ChatMessageTask();
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), mes, item, task);
			splayer.addMessageToRightBag(newReq);
		}
		if (logger.isWarnEnabled()) logger.warn("[给帮会群发消息] [{}] [{}] [{}ms]", new Object[] { jiazu.getName(), jiazu.getMemberNum(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
	}

	/**
	 * 向家族发送电视消息
	 * @param gang
	 * @param content
	 * @param senderName
	 */
	public void sendMessageToJiazu_dianshi(Jiazu jiazu, String content) {
		if (jiazu == null) {
			return;
		}
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		Player ps[] = playerManager.getOnlinePlayerByJiazu(jiazu);
		if (ps == null) {
			return;
		}
		for (Player splayer : ps) {
			if (!splayer.isOnline() || !splayer.isChatChannelOpenning(ChatChannelType.JIAZU) || splayer.getCurrentGame() == null) {
				continue;
			}
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, content);
			splayer.addMessageToRightBag(hreq);
		}
		if (logger.isWarnEnabled()) logger.warn("[给帮会群发消息] [{}] [{}] [{}ms]", new Object[] { jiazu.getName(), jiazu.getMemberNum(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
	}

	/**
	 * 系统给宗派在线成员发送一个消息
	 * @param zp
	 * @param message
	 */
	public void sendMessageToZong(ZongPai zp, ChatMessage message) {
		long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (zp == null || message == null) {
			return;
		}
		Player ps[] = playerManager.getOnlineInZongpai(zp.getId());
		if (ps == null) {
			return;
		}
		CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
		for (Player player : ps) {
			if (!player.isOnline() || !player.isChatChannelOpenning(ChatChannelType.ZONG) || player.getCurrentGame() == null) {
				continue;
			}
			player.addMessageToRightBag(newReq);
		}
		if (logger.isWarnEnabled()) logger.warn("[系统给宗派群发消息] [{}] [{}ms]", new Object[] { zp.getZpname(), (com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - now) });
	}

	/**
	 * 系统给世界频道发送一个消息
	 * @param content
	 */
	public void sendMessageToWorld(ChatMessage message) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (!pp.isChatChannelOpenning(ChatChannelType.WORLD) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage msg = message.clone();
			msg.setSort(ChatChannelType.WORLD);
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
			pp.addMessageToRightBag(newReq);

			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_world] [{}] [{}] [{}]", new Object[] { msg.getSenderName(), pp.getName(), msg.getMessageText() });
		}
	}

	public void sendMessageToServer(String msg) {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (pp.getCurrentGame() == null) {
				continue;
			}
			pp.send_HINT_REQ(msg, (byte) 6);
		}
	}

	/**
	 * 系统发送一个彩世消息
	 * @param content
	 */
	public void sendColorMessageToWorld(ChatMessage message) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();

		for (Player pp : players) {
			if (!pp.isChatChannelOpenning(ChatChannelType.COLOR_WORLD) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage msg = message.clone();
			msg.setSort(ChatChannelType.COLOR_WORLD);
			// 活跃度统计
			ActivenessManager.getInstance().record(pp, ActivenessType.彩世喊话);
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
			pp.addMessageToRightBag(newReq);

			// 测试日志
			logger.info("[send_to_world] [{}] [{}] [{}]", new Object[] { msg.getSenderName(), pp.getName(), msg.getMessageText() });
		}

	}
	
	public void sendColorMessageToWorld(ChatMessage message,boolean isSendSystem) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();

		for (Player pp : players) {
			if (!pp.isChatChannelOpenning(ChatChannelType.COLOR_WORLD) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage msg = message.clone();
			msg.setSort(ChatChannelType.COLOR_WORLD);
			// 活跃度统计
			ActivenessManager.getInstance().record(pp, ActivenessType.彩世喊话);
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
			pp.addMessageToRightBag(newReq);
			if(isSendSystem){
//				pp.sendError(message.getMessageText());
			}
			// 测试日志
			logger.info("[send_to_world] [{}] [{}] [{}]", new Object[] { msg.getSenderName(), pp.getName(), msg.getMessageText() });
		}

	}

	/**
	 * 系统给国家频道发送一个电视广播消息
	 * @param content
	 */
	public void sendMessageToCountry(int country, ChatMessage message) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (pp.getCountry() != country || !pp.isChatChannelOpenning(ChatChannelType.COUNTRY) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage msg = message.clone();
			msg.setSort(ChatChannelType.SYSTEM);
			msg.setChatType(CHAT_TYPE_系统电视消息);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, msg.getMessageText());
			pp.addMessageToRightBag(hreq);
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_country] [{}] [{}] [{}] [{}]", new Object[] { country, msg.getSenderName(), pp.getName(), msg.getMessageText() });
		}
	}

	/**
	 * 系统给国家频道发送一个广播消息，在屏幕下方的聊天窗口
	 * @param content
	 */
	public void sendMessageToCountry2(int country, ChatMessage message) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (pp.getCountry() != country || !pp.isChatChannelOpenning(ChatChannelType.COUNTRY) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage msg = message.clone();
			msg.setSort(ChatChannelType.SYSTEM);
			msg.setChatType(CHAT_TYPE_系统提示消息);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, msg.getMessageText());
			pp.addMessageToRightBag(hreq);
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_country] [{}] [{}] [{}] [{}]", new Object[] { country, msg.getSenderName(), pp.getName(), msg.getMessageText() });
		}
	}

	/**
	 * 系统频道发送一个电视广播消息
	 * @param content
	 */
	public void sendMessageToSystem(ChatMessage msg) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (!pp.isChatChannelOpenning(ChatChannelType.SYSTEM) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage message = msg.clone();
			message.setSort(ChatChannelType.SYSTEM);
			message.setChatType(CHAT_TYPE_系统电视消息);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, msg.getMessageText());
			pp.addMessageToRightBag(hreq);
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_system] [发送人:{}] [接受人:{}] [{}]", new Object[] { message.getSenderName(), pp.getName(), message.getMessageText() });
		}
	}

	/**
	 * 系统频道发送一个电视广播消息
	 * @param content
	 */
	public void sendMessageToSystem(String msg) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (!pp.isChatChannelOpenning(ChatChannelType.SYSTEM) || pp.getCurrentGame() == null) {
				continue;
			}

			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, msg);
			pp.addMessageToRightBag(hreq);
		}
	}

	/**
	 * 系统给宗派频道发送一个电视广播消息
	 * @param content
	 */
	public void sendMessageToZongPai(ZongPai zp, String des) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		List<Long> jiazuIds = zp.getJiazuIds();
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, des);
		for (Player pp : players) {

			if (!jiazuIds.contains(pp.getJiazuId()) || pp.getCurrentGame() == null) {
				continue;
			}
			// ChatMessage msg = message.clone();
			// msg.setSort(ChatChannelType.SYSTEM);
			// msg.setChatType(CHAT_TYPE_系统电视消息);
			pp.addMessageToRightBag(hreq);
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_zongpai] [" + zp.getZpname() + "] []");
		}
	}

	/**
	 * 系统给宗派频道发送一个弹窗广播消息
	 * @param content
	 */
	public void sendMessageToZongPai5(ZongPai zp, String des) throws Exception {
		if (zp == null) {
			return;
		}
		Player players[] = playerManager.getOnlinePlayers();
		List<Long> jiazuIds = zp.getJiazuIds();
		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, des);
		for (Player pp : players) {

			if (!jiazuIds.contains(pp.getJiazuId()) || pp.getCurrentGame() == null) {
				continue;
			}
			// ChatMessage msg = message.clone();
			// msg.setSort(ChatChannelType.SYSTEM);
			// msg.setChatType(CHAT_TYPE_系统电视消息);
			pp.addMessageToRightBag(hreq);
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_zongpai] [" + zp.getZpname() + "] []");
		}
	}

	/**
	 * 系统给个人发送一个电视广播消息
	 * @param content
	 */
	public void sendMessageToPersonal(Player player, String des) throws Exception {

		HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 2, des);

		// ChatMessage msg = message.clone();
		// msg.setSort(ChatChannelType.SYSTEM);
		// msg.setChatType(CHAT_TYPE_系统电视消息);
		if (player.isOnline()) {
			player.addMessageToRightBag(hreq);
		}
		// 测试日志
		if (logger.isInfoEnabled()) logger.info("[send_to_personal广播] [" + player.getLogString() + "] []");
	}

	/**
	 * 发一般系统消息，聊天框位置的消息
	 * @param msg
	 * @throws Exception
	 */
	public void sendHintMessageToSystem(ChatMessage msg) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (!pp.isChatChannelOpenning(ChatChannelType.SYSTEM) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage message = msg.clone();
			message.setSort(ChatChannelType.SYSTEM);
			message.setChatType(CHAT_TYPE_系统提示消息);
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
			pp.addMessageToRightBag(newReq);
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_system] [发送人:{}] [接受人:{}] [{}]", new Object[] { message.getSenderName(), pp.getName(), message.getMessageText() });
		}
	}

	/**
	 * 发系统滚动消息
	 * @param msg
	 * @throws Exception
	 */
	public void sendRoolMessageToSystem(ChatMessage msg) throws Exception {
		Player players[] = playerManager.getOnlinePlayers();
		for (Player pp : players) {
			if (!pp.isChatChannelOpenning(ChatChannelType.SYSTEM) || pp.getCurrentGame() == null) {
				continue;
			}
			ChatMessage message = msg.clone();
			message.setSort(ChatChannelType.SYSTEM);
			message.setChatType(CHAT_TYPE_系统滚动消息);
			HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 3, msg.getMessageText());
			pp.addMessageToRightBag(hreq);
			pp.sendNotice(msg.getMessageText());
			// 测试日志
			if (logger.isInfoEnabled()) logger.info("[send_to_system] [发送人:{}] [接受人:{}] [{}]", new Object[] { message.getSenderName(), pp.getName(), message.getMessageText() });
		}
	}

	// public void sendBattleMessage(Player player, ChatMessage msg) {
	// try{
	// if (!player.isChatChannelOpenning(ChatChannelType.BATTLE) || player.getCurrentGame() == null) {
	// return;
	// }
	// CHAT_MESSAGE_REQ2 newReq = new CHAT_MESSAGE_REQ2(GameMessageFactory.nextSequnceNum(), msg, msg.getAccessoryItem(), msg.getAccessoryTask());
	// player.addMessageToRightBag(newReq);
	// }catch(Exception e) {
	// logger.error("给玩家发战斗频道信息出错:", e);
	// }
	// }

	/**
	 * 给所有好友发送一个消息 上线 下线
	 * @param content
	 */
	public void sendMessageToFriend(String message, Player p) {

		HINT_REQ hint = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 0, message);
		List<Long> friendList = socialManager.getFriendlist(p);
		for (long id : friendList) {
			Player p1 = null;
			try {
				p1 = playerManager.getPlayer(id);
			} catch (Exception e) {
				logger.error("[给所有好友上线下线] [" + p.getLogString() + "]", e);

			}
			if (p1 != null) {
				p1.addMessageToRightBag(hint);
			}
		}
	}

	/**
	 * 得到世界频道内的消息
	 * 只保留1000条
	 * @return
	 */
	public List<ChatMessage> getWorldMessageList() {
		return worldMessageList;
	}

	/**
	 * 获取某个gm的私聊消息
	 * @param gmname
	 * @return
	 */
	public List<ChatMessage> getGmMessages(String gmname) {
		List<ChatMessage> list = gmMessageMap.get(gmname);
		if (list == null) {
			return new ArrayList<ChatMessage>();
		} else {
			return list;
		}
	}

	/**
	 * 清空某个gm的私聊消息
	 * @param gmname
	 */
	public void clearGmMessages(String gmname) {
		List list = gmMessageMap.get(gmname);
		if (list != null) {
			list.clear();
		}
	}

	/**
	 * gm发送一个私聊信息
	 * @param gmname
	 *            GM角色名
	 * @param playerName
	 *            玩家的角色
	 * @param content
	 *            内容
	 * @return
	 */
	public boolean sendGmMessage(String gmname, long playerId, String content) {
		Player p = null;
		Player gm = null;
		
		boolean isonline = GamePlayerManager.getInstance().isOnline(playerId);
		if(!isonline){
			return false;
		}
		
		try {
			p = playerManager.getPlayer(playerId);
			gm = playerManager.getPlayer(gmname);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("gm发送私聊信息异常:[" + gmname + "] [" + playerId + "]", e);
		}
		if (gm != null && p != null && p.isOnline()) {
			ChatMessage message = new ChatMessage();
			message.setAccessoryItem(new ChatMessageItem());
			message.setAccessoryTask(new ChatMessageTask());
			message.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			message.setMessageText(content);
			message.setSenderId(gm.getId());
			message.setReceiverName(p.getName());
			message.setSenderName(gm.getName());
			message.setSort(ChatChannelType.PERSONAL);
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
			p.addMessageToRightBag(newReq);
			List<ChatMessage> list = gmMessageMap.get(gmname);
			if (list == null) {
				list = Collections.synchronizedList(new LinkedList<ChatMessage>());
				gmMessageMap.put(gmname, list);
			}
			list.add(message);
			// 内存中只保留1000条信息
			if (list.size() > 1000) {
				list.remove(0);
			}
			if (logger.isInfoEnabled()) {
				logger.info("[GM发送私聊信息] [成功] [gm:{}] [player:{}] [{}]", new Object[] { gmname, p.getName(), content });
			}
			return true;
		}
		if (logger.isWarnEnabled()) logger.warn("[GM发送私聊信息] [失败:{}] [gm:{}] [player:{}] [{}]", new Object[] { (p == null ? "玩家不存在" : (!p.isOnline() ? "玩家不在线" : "未知")), gmname, playerId, content });
		return false;
	}

	/**
	 * GM向世界频道发送消息
	 * @param gmname
	 * @param content
	 * @return
	 */
	public boolean sendGmMessageToWorld(String gmname, String content) {
		Player gm = null;
		try {
			gm = playerManager.getPlayer(gmname);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("gm发送世界信息异常:[" + gmname + "]", e);
		}
		if (gm != null) {
			ChatMessage message = new ChatMessage();
			message.setAccessoryItem(new ChatMessageItem());
			message.setAccessoryTask(new ChatMessageTask());
			message.setChatTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
			message.setMessageText(content);
			message.setSenderId(gm.getId());
			message.setSenderName(gm.getName());
			message.setSort(ChatChannelType.WORLD);
			try {
				sendMessageToWorld(message);
				worldMessageList.add(message);
				if (worldMessageList.size() > 1000) {
					worldMessageList.remove(0);
				}
				if (logger.isInfoEnabled()) {
					logger.info("[GM发送世界消息] [成功] [gm:{}] [{}]", new Object[] { gmname, content });
				}
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("[GM发送世界消息异常] [" + gmname + "] [" + content + "]", e);
			}
		}
		if (logger.isWarnEnabled()) logger.warn("[GM发送世界消息] [失败:{}] [gm:{}] [{}]", new Object[] { (gm == null ? "GM不存在" : "未知"), gmname, content });
		return false;
	}

	/**
	 * 过滤文本，凡是<f ...>xxxx</f>格式的，仅替换xxxx
	 * @param content
	 * @param replace
	 * @return
	 */
	public static String getFilteredContent(String content, String replace) {
		String str = new String(content);
		StringBuffer sb = new StringBuffer();
		while (str.indexOf("<f") != -1) {
			sb.append(str.substring(0, str.indexOf("<f")));
			str = str.substring(str.indexOf("<f"));
			String tag = str.substring(0, str.indexOf(">") + 1);
			str = str.substring(str.indexOf(">") + 1);
			sb.append(tag);
			String word = str.substring(0, str.indexOf("</f>"));
			if (tag.indexOf("playerIdXXX") == -1 && tag.indexOf("articleEntityXXX") == -1) {
				word = WordFilter.getInstance().nvalidAndReplace(word, 0, "@#\\$%^\\&\\*");
				// word = word.replaceAll("傻逼", "***");
			}
			sb.append(word);
			sb.append("</f>");
			str = str.substring(str.indexOf("</f>") + 4);
		}
		sb.append(str);
		return sb.toString();
	}
	
	/**
	 * 沉默一个玩家
	 * @param playerId
	 * @param hour
	 */
	public void silencePlayer(long playerId, int hour,String reason,int level) {
		Player p = null;
		try {
			p = PlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(p==null){
			logger.warn("[沉默] [失败：玩家不存在] [playerid:{}] [小时:{}] [原因:{}] [级别：{}]", new Object[] { playerId, hour, reason,level});
			return;
		}

		Buff buff = BuffTemplateManager.getInstance().getBuffTemplateByName(Translate.沉默说话).createBuff(level);	
		if(buff==null){
			logger.warn("[沉默] [失败：获取buff失败] [playerid:{}] [小时:{}] [原因:{}] [级别：{}]", new Object[] { playerId, hour, reason,level});
			return;
		}
		
		if(buff instanceof Buff_Silence2 == false){
			logger.warn("[沉默] [失败：buff类型不符] [playerid:{}] [小时:{}] [原因:{}] [级别：{}]", new Object[] { playerId, hour, reason,level});
			return;
		}
		
		long endtime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		if (hour <= 0) {
			long TIME_LIMIT = 10 * 365 * 24 * 60 * 60 * 1000l;
			endtime += TIME_LIMIT;
		} else {
			endtime += hour * 60 * 60 * 1000;
		}
		
		Buff_Silence2 bs = (Buff_Silence2)buff;
	 	bs.setInvalidTime(endtime);
	 	p.placeBuff(bs);
		if (logger.isWarnEnabled()) {
			logger.warn("[沉默] [成功] [playerid:{}] [小时:{}] [原因:{}] [级别：{}] [endtime:{}]", new Object[] { playerId, hour, reason,level,TimeTool.formatter.varChar23.format(endtime)});
		}
	}

	/**
	 * 取消沉默
	 * @param pid
	 * @return
	 */
	public boolean unSilencePlayer(long pid){
		try {
			Player p = PlayerManager.getInstance().getPlayer(pid);
			if(p!=null){
				Iterator<Buff> it = p.getBuffs().iterator();
				while(it.hasNext()){
					Buff b = it.next();
					if(b!=null && b instanceof Buff_Silence2){
						p.getBuffs().remove(b);
						p.setDirty(true, "buffs");
						if (logger.isWarnEnabled()) {
							logger.warn("[解除沉默] [成功] [playerid:{}] [buff:{}]", new Object[] { pid,b.toString() });
						}
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[解除沉默] [异常] [playerid:"+pid+"] [异常："+e+"]");
		}
		return false;
	} 
	
	/**
	 * 玩家是否被沉默
	 * 
	 *  沉默级别
	 *  0:没有沉默，可以发言
	 *  1:普通沉默，只用于限制玩家发言
	 *  2:2级沉默，用于限制该玩家的交易，拍卖，摆摊，邮件等操作
	 *  3:3级沉默，在2级沉默的基础上开启了说话的权限
	 *
	 * @param playerId
	 * @return
	 */
	public int isSilence(long playerId) {
		try {
			Player p = PlayerManager.getInstance().getPlayer(playerId);
			if(p!=null){
				Iterator<Buff> it = p.getBuffs().iterator();
				while(it.hasNext()){
					Buff b = it.next();
					if(b!=null && b instanceof Buff_Silence2){
						Buff_Silence2 bs = (Buff_Silence2)b;
						if (logger.isWarnEnabled()) {
							logger.warn("[检查玩家是否沉默] [沉默有效] [playerid:{}] [buff:{}]", new Object[] { playerId,bs.toString() });
						}
						return bs.getSilenceLevel();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("[检查玩家是否沉默] [异常] [playerid:"+playerId+"] [异常："+e+"]");
		}
		return 0;
	}
	
	/**
	 * 被沉默的人发消息
	 * @param player
	 * @param message
	 * @param type
	 */
	public void sendSilenceMess(Player player,ChatMessage message,String type){
		if(isSilence(player.getId())>0 && isSilence(player.getId()) < 3){
			if ((player.getCurrentGame() == null && !player.isInCrossServer()) || player.getDuelFieldState() > 0) {
				return;
			}
			if (player.hiddenChatMessage) {
				return;
			}
			CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
			player.addMessageToRightBag(newReq);
			if (logger.isInfoEnabled()) logger.info("["+type+"] [无需喊话道具] [沉默说话] [成功] [发送人:{}] [接受人:{}] [{}]", new Object[] { message.getSenderName(), player.getName(), message.getMessageText() });
			return;
		}
	}
	
	public static String SEND_JUBAO_MESS_URL = "";//"http://w.sqage.com/service_sendInformMsg.jsp";
	public static String JUBAO_TYTE_DESC [] = {"恶意辱骂","恶意刷屏","线下交易","非法宣传","其他"};
	public static String CHAT_TYTE_DESC [] = {"公聊","邮件","私聊"};
	public int MIN_JUBAO_PLAYER_LEVEL = 20;
	
	public JUBAO_PLAYER_RES handle_JUBAO_PLAYER_REQ(RequestMessage message,Player player){
		String resultmess = Translate.举报失败;
		if(message instanceof JUBAO_PLAYER_REQ){
			if(player.getLevel() < MIN_JUBAO_PLAYER_LEVEL){
				player.sendError(Translate.二十级以上的玩家才可以举报);
				return new JUBAO_PLAYER_RES(message.getSequnceNum(), 0, Translate.二十级以上的玩家才可以举报);
			}
			JUBAO_PLAYER_REQ req = (JUBAO_PLAYER_REQ)message;
			long playerId = req.getPlayerId();
			String playerName = req.getPlayerName();
			int jiBaoType = req.getJiBaoType();
			int chatType = req.getChatType();
			String juBaoMess = req.getJuBaoMess();
			long chatTime= req.getChatTime();
			String playerSelfRecordMess = req.getPlayerSelfRecordMess();
			String [] readyMess = req.getReadyMess();
			Player badMan = null;
			try{
				badMan = PlayerManager.getInstance().getPlayer(playerName);
			}catch (Exception e) {
				e.printStackTrace();
				logger.warn("[玩家举报] [通过玩家name获取被举报人] [异常] [举报人账号：{}] [举报人角色：{}] [举报人id：{}] [被举报人id:{}] [被举报人name:{}] [举报类型:{}] [举报内容:{}] [readyMess:{}] {}",new Object[]{player.getUsername(), player.getName(),player.getId(), playerId,playerName,jiBaoType,juBaoMess,(readyMess==null?"nul":Arrays.toString(readyMess)),e });
				try{
					badMan = PlayerManager.getInstance().getPlayer(playerId);
				}catch(Exception e2){
					e2.printStackTrace();
					logger.warn("[玩家举报] [通过玩家id获取被举报人] [异常] [举报人账号：{}] [举报人角色：{}] [举报人id：{}] [被举报人id:{}] [被举报人name:{}] [举报类型:{}] [举报内容:{}] [readyMess:{}] {}",new Object[]{player.getUsername(), player.getName(),player.getId(), playerId,playerName,jiBaoType,juBaoMess,(readyMess==null?"nul":Arrays.toString(readyMess)),e2 });
				}
			}
			if(badMan == null){
				player.sendError(Translate.举报的玩家不存在);
				return null;
			}
			
			try{
				Map headers = new HashMap();
				juBaoMess = URLEncoder.encode(juBaoMess);
				String serverName = GameConstants.getInstance().getServerName();
				LinkedHashMap<String, String> infos = new LinkedHashMap<String, String>();
				infos.put("juBaoUsername",player.getUsername());
				infos.put("juBaoId", String.valueOf(player.getId()));
				infos.put("juBaoPlayerName",player.getName());
				infos.put("beiJuBaoUsername",badMan.getUsername());
				infos.put("beiJuBaoId",String.valueOf(badMan.getId()));
				infos.put("beiJuBaoPlayerName",badMan.getName());
				infos.put("serverName",serverName);
				infos.put("juBaoType",JUBAO_TYTE_DESC[jiBaoType]);
				infos.put("chatType",CHAT_TYTE_DESC[chatType]);
				infos.put("juBaoMess",juBaoMess);
				infos.put("chatTime",String.valueOf(chatTime));
				infos.put("playerSelfRecordMess",playerSelfRecordMess);
				String reqinfos = buildParams(infos);
				
				byte [] bytes = HttpUtils.webPost(new URL(SEND_JUBAO_MESS_URL),reqinfos.getBytes(), headers, 10000, 10000);
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				int code = (Integer)headers.get(HttpUtils.Response_Code);
				String resMessage = (String)headers.get(HttpUtils.Response_Message);
				String	result = new String(bytes,encoding).trim();
				resultmess = result;
				if(result != null && result.equals("举报成功")){
					JUBAO_PLAYER_RES res = new JUBAO_PLAYER_RES(req.getSequnceNum(), 1, Translate.举报客户信息);
					return res;
				}
				player.sendError(result);
				logger.warn("[玩家举报] [发送举报信息] [完成] [结果:"+result+"] [playerSelfRecordMess:"+playerSelfRecordMess+"] [encoding:"+encoding+"] [code:"+code+"] [resMessage:"+resMessage+"] [reqinfos:"+reqinfos+"] [player:"+player.getLogString()+"]");
			}catch(Exception e){
				player.sendError(resultmess);
				e.printStackTrace();
				logger.warn("[玩家举报] [发送举报信息] [异常] [举报人账号：{}] [举报人角色：{}] [举报人id：{}] [被举报人id:{}] [被举报人name:{}] [resultmess:{}] [举报类型:{}] [举报内容:{}] [readyMess:{}] [{}] [{}]",new Object[]{player.getUsername(), player.getName(),player.getId(), playerId,playerName,resultmess,jiBaoType,juBaoMess,(readyMess==null?"nul":Arrays.toString(readyMess)),e});
			}
		}	
		return new JUBAO_PLAYER_RES(message.getSequnceNum(), 0, resultmess);
	}
	
	public boolean limitFuBenVoice(Player p){
		if(WolfManager.getInstance().isWolfGame(p)){
			return true;
		}
		return false;
	}
	
	private String buildParams(Map<String,String> params){
		String[] key = params.keySet().toArray(new String[params.size()]);

		StringBuffer sb = new StringBuffer();
		for (String k : key) {
			sb.append(k);
			sb.append("=");
			sb.append(params.get(k));
			sb.append("&");
		}
		
		return sb.toString().substring(0, sb.length() - 1);
	}
	public static void main(String args[]) {
		String ss = "<f color='2817792'></f><f onclickType='1' color='6075638' playerId='1100000000003489425'onclick='actionPlayer'>战神傻逼传士</f>:<f onclick='articleEntity' onclickType='2' entityId='1100000000455021254' color='-1'>[太乙傻逼散仙]</f><f onclick='articleEntity' onclickType='2' entityId='1100000000455021254' color='-1'>[太乙散仙]</f><f onclick='articleEntity' onclickType='2' entityId='1100000000455021254' color='-1'>[太乙散仙]</f><f color='16766976'>代价来你是大傻逼哈哈</f><a partName='part/bqdonghua.xtl' animName='haixou' width='35' height='32'></a><a partName='part/bqdonghua.xtl' animName='haixou' width='35' height='32'></a><a partName='part/bqdonghua.xtl' animName='haixou' width='35' height='32'></a>";
		System.out.print(getFilteredContent(ss, ""));
	}
}
