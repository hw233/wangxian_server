package com.fy.engineserver.menu.activity;

import java.util.HashSet;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * Tstore 登陆用户奖励
 * 
 * 
 */
public class Option_Tstore_Login_Activity extends Option implements NeedCheckPurview {

	/**
	 * 对于这一类点击NPC领取奖励的，可以统一由一个manager去管理
	 * 以后简单配置就能用
	 */
	private String startTime = "2013-07-10 00:00:00";
	private String endTime = "2013-07-16 23:59:59";
	private Platform[] openplats = { Platform.韩国 };
	private static Set<String> openchannels = new HashSet<String>();
	private Set<String> limitservers = new HashSet<String>();
	private String articlename = "티스토어 고객 감사 패키지";
	private String mailtitle = "티스토어 고객 감사 패키지";
	private String mailcount = "티스토어 고객 감사 패키지";
	private String 背包满提示 = "배낭이 가득 찼습니다. 보상은 메일로 지급해 드리오니 확인부탁합니다!";
	private String 已经领取过 = "이미 수령했습니다!";

	static {
		openchannels.add("KOREATSTORE_MIESHI");
	}

	@Override
	public void doSelect(Game game, Player player) {
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		if (ddc != null && ddc.get(player.getId() + "Tstore登陆用户奖励") == null) {
			Article a = ArticleManager.getInstance().getArticle(articlename);
			if (a == null) {
				ActivitySubSystem.logger.warn("[Tstore登陆用户奖励] [物品不存在] [玩家信息：" + player.getLogString() + "] [" + articlename + "]");
				return;
			}

			ArticleEntity ae = null;

			try {
				ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 0, true);
			} catch (Exception e) {
				e.printStackTrace();
				ActivitySubSystem.logger.warn("[Tstore登陆用户奖励] [创建物品异常] [玩家信息：" + player.getLogString() + "] [" + articlename + "]", e);
				return;
			}

			if (ae != null) {
				if (player.getKnapsack_common().isFull()) {
					MailManager mm = MailManager.getInstance();
					try {
						mm.sendMail(player.getId(), new ArticleEntity[] { ae }, mailtitle, mailcount, 0, 0, 0, "Tstore登陆用户奖励");
						ActivitySubSystem.logger.warn("[Tstore登陆用户奖励] [领取礼包成功] [邮件发送] [" + player.getLogString() + "]");
					} catch (Exception e) {
						e.printStackTrace();
						ActivitySubSystem.logger.warn("[Tstore登陆用户奖励] [通过邮件发送奖励异常] [玩家信息：" + player.getLogString() + "] [" + articlename + "]", e);
					}
					player.sendError(背包满提示);
				} else {
					player.putToKnapsacks(ae, "Tstore登陆用户奖励");
					ActivitySubSystem.logger.warn("[Tstore登陆用户奖励] [领取礼包成功] [放入背包] [" + player.getLogString() + "]");
				}
				ddc.put(player.getId() + "Tstore登陆用户奖励", player.getId());
			}
		} else {
			player.sendError(已经领取过);
			ActivitySubSystem.logger.warn("[Tstore登陆用户奖励] [已经领取过] [" + player.getLogString() + "]");
		}
	}

	@Override
	public boolean canSee(Player player) {
		long now = System.currentTimeMillis();
		if (!PlatformManager.getInstance().isPlatformOf(openplats)) {
			return false;
		}

		GameConstants gc = GameConstants.getInstance();
		if (gc == null) {
			return false;
		}

		if (limitservers.contains(gc.getServerName())) {
			return false;
		}

		if (TimeTool.formatter.varChar19.parse(startTime) > now || now > TimeTool.formatter.varChar19.parse(endTime)) {
			return false;
		}

		Passport passport = player.getPassport();
		if (passport != null) {
			String channelName = passport.getLastLoginChannel();
			if (openchannels.contains(channelName)) {
				return true;
			}
		} else {
			ActivityManagers.logger.warn("[KT渠道NPC领取活动] [玩家渠道为空] [请确定玩家渠道查看是否补偿] [玩家信息：" + player.getLogString() + "]");
			return false;
		}

		return false;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
