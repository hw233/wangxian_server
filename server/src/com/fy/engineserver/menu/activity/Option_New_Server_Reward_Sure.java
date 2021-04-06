package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedRecordNPC;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.npc.NPC;

/**
 * 服务器维护，领取物品
 * @author Administrator
 * 
 */
public class Option_New_Server_Reward_Sure extends Option implements NeedRecordNPC {

	private NPC npc;
	private String articleName = "KT 접속 패키지";

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {

		if (ActivityManagers.getInstance().getDdc().get(player.getId() + "用于KT开服登录活动") == null) {
			Article a = ArticleManager.getInstance().getArticle(articleName);
			if (a != null) {
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 1, true);
					if (ae != null) {
						if (player.getKnapsack_common().isFull()) {
							MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, "KT 등록패키지", "《천신》에 오신것을 환영합니다.KT 등록 유저들을 위한 패키지를 받아가세요.즐거운 게임 되세요.", 0, 0, 0, "用于KT开服登录活动");
							ActivitySubSystem.logger.error(player.getLogString() + "[用于KT开服登录活动] [发送邮件] [" + articleName + "]");
						} else {
							player.putToKnapsacks(ae, 1, "用于KT开服登录活动");
							ActivitySubSystem.logger.error(player.getLogString() + "[用于KT开服登录活动] [放入背包] [" + articleName + "]");
						}
						player.sendError("KT 접속 패키지*1획득한것을 축하합니다.");
						ActivityManagers.getInstance().getDdc().put(player.getId() + "用于KT开服登录活动", 1);
					}
				} catch (Throwable e) {
					e.printStackTrace();
					ActivitySubSystem.logger.error(player.getLogString() + "用于KT开服登录活动] [创建物品] [异常] [KT登陆礼包*1] [" + articleName + "]", e);
				}

			} else {
				ActivitySubSystem.logger.error(player.getLogString() + "[用于KT开服登录活动] [物品不存在] [KT登陆礼包*1] [" + articleName + "]");
			}
		} else {
			player.sendError("당신은 이미 수령했습니다.");// 提示已经领取过了
		}
	}

	@Override
	public NPC getNPC() {
		return npc;
	}

	@Override
	public void setNPC(NPC npc) {
		this.npc = npc;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

}
