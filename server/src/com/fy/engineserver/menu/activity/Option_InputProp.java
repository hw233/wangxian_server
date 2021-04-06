package com.fy.engineserver.menu.activity;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.chat.ChatChannelType;
import com.fy.engineserver.chat.ChatMessage;
import com.fy.engineserver.chat.ChatMessageItem;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.chat.ChatMessageTask;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.OpenInputProp;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.message.CHAT_MESSAGE_REQ;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.MARRIAGE_BEQ_FLOWER_RES;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.xuanzhi.tools.text.WordFilter;

public class Option_InputProp extends Option {

	private OpenInputProp articleCost;// 消耗的道具

	@Override
	public void doInput(Game game, Player player, String inputContent) {
		ArticleManager.logger.warn(player.getLogString() + " [使用道具] [" + articleCost.getName() + "] [输入内容:" + inputContent + "] [验证之前]");
		{
			// TODO 输入是否有效.是否有非法字符
			// WordFilter filter = WordFilter.getInstance();
			// boolean isTooLong = inputContent.getBytes().length > 100;//
			// if (isTooLong) {
			// player.sendError(Translate.输入内容太长);
			// return;
			// }
			// boolean valid = filter.cvalid(inputContent, 0) && filter.evalid(inputContent, 1);
			// if (!valid) {
			// player.sendError(Translate.输入内容非法);
			// return;
			// }
			inputContent = ChatMessageService.getFilteredContent(inputContent, "@#\\$%^\\&\\*");
			inputContent = inputContent.trim();
			ArticleManager.logger.warn(player.getLogString() + " [使用道具] [" + articleCost.getName() + "] [输入内容:" + inputContent + "] [验证文字之后]");
		}
		// ArticleEntity ae = player.getArticleEntity(articleCost.getName());
		// if (ae == null) {
		// ArticleManager.logger.warn(player.getLogString() + " [使用道具] [" + articleCost.getName() + "] [输入内容:" + inputContent + "] [物品不存在]");
		// return;
		// }
		ArticleManager.logger.warn(player.getLogString() + " [使用道具] [" + articleCost.getName() + "] [输入内容:" + inputContent + "] [验证通过]");
		// boolean removeOK = player.removeArticle(articleCost.getName(), "使用");
		// if (removeOK) {
		int sendScope = articleCost.getSendScope();
		int showMessageArea = articleCost.getShowMessageArea();

		// 先确定要发送的玩家列表
		List<Player> playerList = new ArrayList<Player>();
		Player[] onlinePlayers = GamePlayerManager.getInstance().getOnlinePlayers();
		switch (sendScope) {
		case OpenInputProp.send_scope_all:
			for (Player olPlayer : onlinePlayers) {
				playerList.add(olPlayer);
			}
			break;
		case OpenInputProp.send_scope_country:
			for (Player olPlayer : onlinePlayers) {
				if (olPlayer != null && olPlayer.getCountry() == player.getCountry()) {
					playerList.add(olPlayer);
				}
			}
			break;
		case OpenInputProp.send_scope_jiazu:
			for (Player olPlayer : onlinePlayers) {
				if (olPlayer != null && olPlayer.getJiazuId() == player.getJiazuId()) {
					playerList.add(olPlayer);
				}
			}
			break;
		case OpenInputProp.send_scope_zongpai:
			for (Player olPlayer : onlinePlayers) {
				if (olPlayer != null && olPlayer.getZongPaiName() != null && !"".equals(olPlayer.getZongPaiName()) && olPlayer.getZongPaiName().equals(player.getZongPaiName())) {
					playerList.add(olPlayer);
				}
			}
			break;
		case OpenInputProp.send_scope_friend:
			Relation relation = SocialManager.getInstance().getRelationById(player.getId());
			ArrayList<Long> friendlist = relation.getFriendlist();
			for (Long finedId : friendlist) {
				if (GamePlayerManager.getInstance().isOnline(finedId)) {
					Player firend;
					try {
						firend = GamePlayerManager.getInstance().getPlayer(finedId);
						if (firend != null) {
							playerList.add(firend);
						}
					} catch (Exception e) {
						ArticleManager.logger.warn("[使用道具 ] [" + articleCost.getName() + "] [获取好友异常]", e);
					}
				}
			}
			break;
		default:
			break;
		}
		if (playerList.size() > 0) {// 有要发的列表

			inputContent = Translate.玩家 + "[" + player.getName() + "]:" + inputContent;

			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setMessageText(inputContent);

			for (Player p : playerList) {
				ChatMessage message = chatMessage.clone();
				if (inputContent.length() > 0) {
					switch (showMessageArea) {
					case OpenInputProp.message_area_hint:
						p.sendError(inputContent);
						break;
					case OpenInputProp.message_area_sys:
						message.setSort(ChatChannelType.SYSTEM);
						message.setChatType(ChatMessageService.CHAT_TYPE_系统提示消息);
						CHAT_MESSAGE_REQ rr = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
						p.addMessageToRightBag(rr);
						break;
					case OpenInputProp.message_area_colorWorld:
						message.setSort(ChatChannelType.COLOR_WORLD);
						message.setAccessoryItem(new ChatMessageItem());
						message.setAccessoryTask(new ChatMessageTask());
						CHAT_MESSAGE_REQ newReq = new CHAT_MESSAGE_REQ(GameMessageFactory.nextSequnceNum(), message, message.getAccessoryItem(), message.getAccessoryTask());
						p.addMessageToRightBag(newReq);
						break;
					case OpenInputProp.message_area_rool:
						message.setSort(ChatChannelType.SYSTEM);
						message.setChatType(ChatMessageService.CHAT_TYPE_系统滚动消息);
						HINT_REQ hreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 3, message.getMessageText());
						p.addMessageToRightBag(hreq);
						break;
					default:
						break;
					}
				}
				if (articleCost.isNeedSendColorfull()) {
					byte sendAsSex = (byte) (articleCost.getColorfullType() == OpenInputProp.colorfull_type_basesex ? player.getSex() : articleCost.getColorfullType());
					byte flowType = (byte) (articleCost.getColorfullIndex());
					MARRIAGE_BEQ_FLOWER_RES res = new MARRIAGE_BEQ_FLOWER_RES(GameMessageFactory.nextSequnceNum(), sendAsSex, flowType);
					p.addMessageToRightBag(res);
				}
			}
			// }
		}

	}

	@Override
	public void doSelect(Game game, Player player) {
		ArticleManager.logger.warn(player.getLogString() + " [使用道具] [" + articleCost.getName() + "] [doSelect]");
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	public OpenInputProp getArticleCost() {
		return articleCost;
	}

	public void setArticleCost(OpenInputProp articleCost) {
		this.articleCost = articleCost;
	}

}