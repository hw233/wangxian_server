package com.fy.engineserver.menu.loverday;

import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.MARRIAGE_BEQ_FLOWER_RES;
import com.fy.engineserver.playerTitles.PlayerTitlesManager;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

/**
 * 查看好友
 * 
 * 
 */
public class Option_Friend extends Option {
	private long friendId;
	private String friendName;
	private Option_SendGift option_SendGift;

	public Option_Friend(long friendId, String friendName, Option_SendGift option_SendGift) {
		super();
		this.friendId = friendId;
		this.friendName = friendName;
		this.option_SendGift = option_SendGift;
	}

	@Override
	public void doSelect(Game game, Player player) {
		List<Long> friendlist = SocialManager.getInstance().getFriendlist(player);
		if (friendlist == null || !friendlist.contains(friendId)) {
			player.sendError(Translate.translateString(Translate.不是好友, new String[][]{{Translate.STRING_1,friendName}}));
			return;
		}
		ArticleEntity ae = player.getArticleEntity(option_SendGift.getArticleName());
		if (ae == null) {
			player.sendError(Translate.translateString(Translate.你没有所需道具, new String[][]{{Translate.STRING_1,option_SendGift.getArticleName()}}));
			return;
		}
		if (!GamePlayerManager.getInstance().isOnline(friendId)) {
			player.sendError(Translate.translateString(Translate.不在线啊, new String[][]{{Translate.STRING_1,friendName}}));
			return;
		}
		Player friend = null;
		try {
			friend = GamePlayerManager.getInstance().getPlayer(friendId);
		} catch (Exception e) {
			e.printStackTrace();
			player.sendError(Translate.translateString(Translate.不在线啊, new String[][]{{Translate.STRING_1,friendName}}));
			return;
		}
		if (friend.getSex() == player.getSex()) {
			player.sendError(Translate.同性不能赠送);
			return;
		}
		ArticleEntity removeFromKnapsacks = player.removeFromKnapsacks(ae, "情人节活动", true);
		if (removeFromKnapsacks == null) {
			player.sendError("你没有所需的物品:" + option_SendGift.getArticleName());
			return;
		}
		player.sendNotice("您使用了爱情信物:" + option_SendGift.getArticleName());
		// 扣除成功.加称号
		boolean isMale = player.getSex() == 0; // 0男 1女
		String nameColor = "";
		String wordColor = "";
		byte flowType = -1;
		if (option_SendGift.getArticleName().equals("纯爱感言")) {
			flowType = 0;
		} else if (option_SendGift.getArticleName().equals("真爱宣言")) {
			flowType = 1;
		} else if (option_SendGift.getArticleName().equals("挚爱箴言")) {
			flowType = 2;
		}
		if (isMale) {
			String otherTitle = null;
			boolean femaleSucc = PlayerTitlesManager.getInstance().addTitle(friend, option_SendGift.getTitleForFemale(), true);
			if (femaleSucc) {
				otherTitle = PlayerTitlesManager.getInstance().getValueByType(option_SendGift.getTitleForFemale());
				if (otherTitle != null) {
					friend.sendError(player.getName() + "送给你" + option_SendGift.getArticleName() + ".你获得称号:" + otherTitle);
				}
			}
			boolean maleSucc = PlayerTitlesManager.getInstance().addTitle(player, option_SendGift.getTitleForMale(), true);
			if (maleSucc) {
				String titleName = PlayerTitlesManager.getInstance().getValueByType(option_SendGift.getTitleForMale());
				if (titleName != null) {
					player.sendError("恭喜您获得称号:" + titleName + ",你的Ta " + friend.getName() + " 获得称号:" + otherTitle);
				}
			}

			ActivitySubSystem.logger.warn("[情人节]" + player.getLogString() + "[称号:" + option_SendGift.getTitleForMale() + "] [对方:" + friendId + "/" + friendName + "] [称号:" + option_SendGift.getTitleForFemale() + "]");
		} else {
			String otherTitle = null;
			boolean maleSucc = PlayerTitlesManager.getInstance().addTitle(friend, option_SendGift.getTitleForMale(), true);
			if (maleSucc) {
				otherTitle = PlayerTitlesManager.getInstance().getValueByType(option_SendGift.getTitleForMale());
				if (otherTitle != null) {
					friend.sendError(player.getName() + "送给你" + option_SendGift.getArticleName() + ".你获得称号:" + otherTitle);
				}
			}
			boolean femaleSucc = PlayerTitlesManager.getInstance().addTitle(player, option_SendGift.getTitleForFemale(), true);
			if (femaleSucc) {
				String titleName = PlayerTitlesManager.getInstance().getValueByType(option_SendGift.getTitleForFemale());
				if (titleName != null) {
					player.sendError("恭喜您获得称号:" + titleName + ",你的Ta " + friend.getName() + " 获得称号:" + otherTitle);
				}
			}
			ActivitySubSystem.logger.warn("[情人节]" + player.getLogString() + "[称号:" + option_SendGift.getTitleForFemale() + "] [对方:" + friendId + "/" + friendName + "] [称号:" + option_SendGift.getTitleForMale() + "]");
		}
		if (flowType != 0) {
			Player[] onlinePlayers = GamePlayerManager.getInstance().getOnlinePlayers();
			for (Player p : onlinePlayers) {
				MARRIAGE_BEQ_FLOWER_RES res = new MARRIAGE_BEQ_FLOWER_RES(GameMessageFactory.nextSequnceNum(), isMale ? (byte) 0 : (byte) 1, flowType);
				p.addMessageToRightBag(res);
			}
		}
		String notice = "";
		switch (flowType) {
		case 0:
			notice = Translate.纯爱感言提示;
			break;
		case 1:
			notice = Translate.真爱宣言提示;
			break;
		case 2:
			notice = Translate.挚爱箴言提示;
			break;

		default:
			break;
		}
		ChatMessage msg = new ChatMessage();
		msg.setMessageText(Translate.translateString(notice, new String[][] { { Translate.STRING_1, player.getName() }, { Translate.STRING_2, friend.getName() } }));
		try {
			ChatMessageService.getInstance().sendMessageToSystem(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public long getFriendId() {
		return friendId;
	}

	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}

	public String getFriendName() {
		return friendName;
	}

	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}

	public Option_SendGift getOption_SendGift() {
		return option_SendGift;
	}

	public void setOption_SendGift(Option_SendGift option_SendGift) {
		this.option_SendGift = option_SendGift;
	}

}
