package com.fy.engineserver.activity.everylogin;

import java.util.HashSet;
import java.util.Set;

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
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 绿色服的连续登陆活动
 * 
 */
public class GreenServerLoginActivity extends LoginActivity {

	public long startTime;
	public long endTime;
	public String prizeName;
	public int prizeNum;
	public Platform []platforms;

	public Set<String> servers = new HashSet<String>();

	public GreenServerLoginActivity(long startTime, long endTime, String prizeName, int prizeNum, Set<String> servers,Platform [] platforms) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.prizeName = prizeName;
		this.prizeNum = prizeNum;
		this.servers = servers;
		this.platforms = platforms;
	}
	public GreenServerLoginActivity(String startTimeStr, String endTimeStr, String prizeName, int prizeNum, Set<String> servers,Platform [] platforms) {
		this.startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
		this.endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
		this.prizeName = prizeName;
		this.prizeNum = prizeNum;
		this.servers = servers;
		this.platforms = platforms;
	}

	@Override
	public CompoundReturn doPrize(Player player,int days) {
		Article a = ArticleManager.getInstance().getArticle(prizeName);
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), prizeNum, true);
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { prizeNum }, "登陆领银票", "喜获得登陆奖励，请在附件中提取银块道具，使用后可获得银票250两，请在【背包-银票】道具中查看金额。银票可以购买所有商城物品。4月20日之前，每天登陆都有银票奖励哦！", 0L, 0L, 0L, "连续登陆奖励-" + getName());
		} catch (Exception e) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false).setStringValue(player.getLogString() + "[" + getName() + "]-出现异常");
		}

		return CompoundReturn.createCompoundReturn().setBooleanValue(true).setStringValue("恭喜获得登陆奖励，请在附件中提取银块道具，使用后可获得银票250两，请在【背包-银票】道具中查看金额。银票可以购买所有商城物品。4月20日之前，每天登陆都有银票奖励哦！");
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
		return "绿色服连续登陆3-22";
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
