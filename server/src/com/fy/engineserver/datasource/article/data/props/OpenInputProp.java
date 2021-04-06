package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.shop.ShopActivityManager;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.activity.Option_InputProp;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.INPUT_WINDOW_REQ;
import com.fy.engineserver.sprite.Player;

/**
 * 触发输入框道具，且可以飘花/糖
 * 
 */
public class OpenInputProp extends Props {

	/** 信息发送范围 */
	public static final int send_scope_all = 0;
	public static final int send_scope_country = 1;
	public static final int send_scope_jiazu = 2;
	public static final int send_scope_zongpai = 3;
	public static final int send_scope_friend = 4;

	/** 信息发送位置 */
	public static final int message_area_hint = 0;// 弹框+系统消息
	public static final int message_area_sys = 1;// 系统消息
	public static final int message_area_colorWorld = 2;// 彩世
	public static final int message_area_rool = 3;// 滚屏sendRoolMessageToSystem

	/** 送花/糖的规则 */
	public static final int colorfull_type_basesex = -1;// 根据性别
	public static final int colorfull_type_flower = 0;// 花
	public static final int colorfull_type_sweet = 1;// 糖

	private int sendScope;
	private int showMessageArea;

	private boolean needSendColorfull;// 是否飘花/糖

	private int colorfullType;// 飘的是花还是糖

	private int colorfullIndex;// 飘花/糖的索引

	private String windowDes;// 窗口描述

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {
		if (!super.use(game, player, ae)) {
			ArticleManager.logger.warn(player.getLogString() + " [使用道具:" + ae.getArticleName() + "] [失败]");
			return false;
		}
		if (!checkSencScope(player, this)) {
			player.sendError(Translate.没有家族或宗派);
//			player.sendError("You can't use this!");
			return false;
		}
		// 弹出窗口。让玩家输入的。
		ArticleManager.logger.warn(player.getLogString() + " [使用道具:" + ae.getArticleName() + "] [弹出提示框]");
		MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(120);
		mw.setDescriptionInUUB(this.windowDes);

		Option_InputProp inputProp = new Option_InputProp();
		inputProp.setText(Translate.确定);
		inputProp.setArticleCost(this);
		//
		// Option_Cancel cancle = new Option_Cancel();
		// cancle.setText(Translate.取消);
		mw.setOptions(new Option[] { inputProp });

		INPUT_WINDOW_REQ res = new INPUT_WINDOW_REQ(GameMessageFactory.nextSequnceNum(), mw.getId(), mw.getTitle(), mw.getDescriptionInUUB(), (byte) 2, (byte) 50, " ", Translate.确定, Translate.取消, new byte[0]);
		player.addMessageToRightBag(res);
		try {
			ArrayList<ArticleEntity> strongMaterialEntitys = new ArrayList<ArticleEntity>();
			strongMaterialEntitys.add(ae);
			ShopActivityManager.getInstance().noticeUseSuccess(player, strongMaterialEntitys);
			ActivitySubSystem.logger.warn("[使用赠送活动][使用触发聊天框道具:" + ae.getArticleName() + "]");
		} catch (Exception e) {
			ActivitySubSystem.logger.error("[使用赠送活动] [使用触发聊天框道具] [" + player.getLogString() + "]", e);
		}

		return true;
	}

	public boolean checkSencScope(Player player, OpenInputProp articleCost) {
		int sendScope = articleCost.getSendScope();
		switch (sendScope) {
		case OpenInputProp.send_scope_all:
		case OpenInputProp.send_scope_country:
		case OpenInputProp.send_scope_friend:
			return true;
		case OpenInputProp.send_scope_jiazu:

			return player.getJiazuId() > 0;
		case OpenInputProp.send_scope_zongpai:

			return player.getZongPaiName() != null && !"".equals(player.getZongPaiName());
		default:
			break;
		}
		return true;
	}

	public int getSendScope() {
		return sendScope;
	}

	public void setSendScope(int sendScope) {
		this.sendScope = sendScope;
	}

	public int getShowMessageArea() {
		return showMessageArea;
	}

	public void setShowMessageArea(int showMessageArea) {
		this.showMessageArea = showMessageArea;
	}

	public boolean isNeedSendColorfull() {
		return needSendColorfull;
	}

	public void setNeedSendColorfull(boolean needSendColorfull) {
		this.needSendColorfull = needSendColorfull;
	}

	public int getColorfullType() {
		return colorfullType;
	}

	public void setColorfullType(int colorfullType) {
		this.colorfullType = colorfullType;
	}

	public int getColorfullIndex() {
		return colorfullIndex;
	}

	public void setColorfullIndex(int colorfullIndex) {
		this.colorfullIndex = colorfullIndex;
	}

	public String getWindowDes() {
		return windowDes;
	}

	public void setWindowDes(String windowDes) {
		this.windowDes = windowDes;
	}
}