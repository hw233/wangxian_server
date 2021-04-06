package com.fy.engineserver.activity.everylogin;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;
import com.xuanzhi.boss.game.GameConstants;

public class LoginEveryDay extends LoginActivity {

	public long startTime;
	public long endTime;
	public String[] prizeName;
	public int[] prizeNum;
	public int[] prizeColor;
	public Platform[] platforms;
	public String name;

	public Set<String> servers = new HashSet<String>();

	public LoginEveryDay(long startTime, long endTime, String[] prizeName, int[] prizeNum, int[] prizeColor, Platform[] platforms, String name, Set<String> servers) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.prizeName = prizeName;
		this.prizeColor = prizeColor;
		this.prizeNum = prizeNum;
		this.platforms = platforms;
		this.name = name;
		this.servers = servers;
	}

	@Override
	public CompoundReturn doPrize(Player player, int days) {
		days = days - 1;
		if (days >= prizeName.length) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue("时间异常!");
		}
		Article a = ArticleManager.getInstance().getArticle(prizeName[days]);
		if (a == null) {
			ActivitySubSystem.logger.warn(player.getLogString() + " [连续登陆奖励] [连续天数:" + days + "] [物品不存在:" + prizeName[days] + "]");
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue("奖励物品不存在");
		}
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, prizeColor[days], prizeNum[days], true);
			// for (int i = 0; i < prizeNum[days]; i++) {
			// if (player.putToKnapsacks(ae, "连续登录活动")) {
			// ActivitySubSystem.logger.warn("[连续登录活动] [领取奖励] [成功] [" + player.getLogString() + "]");
			// }
			// }
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { prizeNum[days] }, "系统邮件", "恭喜你获得连续登陆奖励", 0L, 0L, 0l, "连续登陆活动");
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
		return name;// "连续登录活动，公告领取20130328";
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
