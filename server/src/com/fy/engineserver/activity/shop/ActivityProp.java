package com.fy.engineserver.activity.shop;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.sprite.Player;

/**
 * 活动道具
 */
public class ActivityProp {

	private String articleCNName;
	private int articleColor;
	private int articleNum;
	private boolean bind;
	private int goodsId;
	private boolean withOutBind;
	
	 public ActivityProp() {
		// TODO Auto-generated constructor stub
	}

	public ActivityProp(String articleCNName, int articleColor, int articleNum, boolean bind) {
		this.articleCNName = articleCNName;
		this.articleColor = articleColor;
		this.articleNum = articleNum;
		this.bind = bind;
	}

	public void sendMailToPlayer(Player player, String mailTitle, String mailContent, String sendReason) {
		List<Player> players = new ArrayList<Player>();
		players.add(player);
		ActivityProp[] activityProps = new ActivityProp[] { this };
		ActivityManager.sendMailForActivity(players, activityProps, mailTitle, mailContent, sendReason);
	}

	public String getArticleCNName() {
		return articleCNName;
	}

	public void setArticleCNName(String articleCNName) {
		this.articleCNName = articleCNName;
	}

	public int getArticleColor() {
		return articleColor;
	}

	public void setArticleColor(int articleColor) {
		this.articleColor = articleColor;
	}

	public int getArticleNum() {
		return articleNum;
	}

	public void setArticleNum(int articleNum) {
		this.articleNum = articleNum;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}

	@Override
	public String toString() {
		return "ActivityProp [articleName=" + articleCNName + ", articleColor=" + articleColor + ", articleNum=" + articleNum + ", bind=" + (withOutBind?"both":bind) + "]";
	}

	public boolean isWithOutBind() {
		return withOutBind;
	}

	public void setWithOutBind(boolean withOutBind) {
		this.withOutBind = withOutBind;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}
}