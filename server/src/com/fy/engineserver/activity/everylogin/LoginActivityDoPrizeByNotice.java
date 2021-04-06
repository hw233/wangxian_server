package com.fy.engineserver.activity.everylogin;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.xuanzhi.boss.game.GameConstants;

//连续登录活动，公告领取
public class LoginActivityDoPrizeByNotice extends LoginActivity {

	public long startTime;
	public long endTime;
	public String prizeName;
	public int prizeNum;
	public Platform[] platforms;
	public String name;

	public Set<String> servers = new HashSet<String>();

	public LoginActivityDoPrizeByNotice(long startTime, long endTime, String prizeName, int prizeNum,String name, Set<String> servers, Platform[] platforms) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.prizeName = prizeName;
		this.prizeNum = prizeNum;
		this.servers = servers;
		this.platforms = platforms;
		this.name = name;
	}

	@Override
	public CompoundReturn doPrize(Player player,int days) {
		Article a = ArticleManager.getInstance().getArticle(prizeName);
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), prizeNum, true);
			for (int i = 0; i < prizeNum; i++) {
				if (player.putToKnapsacks(ae, "连续登录活动")) {
					ActivitySubSystem.logger.warn("[连续登录活动] [领取奖励] [成功] [" + player.getLogString() + "]");
				}
			}
		} catch (Exception e) {
			ActivitySubSystem.logger.warn("[连续登录活动] [领取奖励] [异常] [" + player.getLogString() + "]", e);
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue("[连续登录活动] [领取奖励] [成功] " + player.getLogString() + "[" + getName() + "]-出现异常");
		}

		return CompoundReturn.createCompoundReturn().setBooleanValue(true).setStringValue("恭喜获得登陆奖励，请在背包中查看您的奖励，每天登陆都有奖励哦！");
	}

	@Override
	public boolean open() {
		if (!PlatformManager.getInstance().isPlatformOf(getPlatform())) {
			return false;
		}
		if (!timeFit()) {
			return false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc == null) {
			return false;
		}
		if (!servers.contains(gc.getServerName())) {
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return name;//"连续登录活动，公告领取20130328";
	}

	@Override
	public boolean timeFit() {
		long now = SystemTime.currentTimeMillis();
		return startTime <= now && now <= endTime;
	}

	@Override
	public Platform[] getPlatform() {
		return platforms;
	}
}
