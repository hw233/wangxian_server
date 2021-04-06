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
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 服务器维护，领取物品
 * @author Administrator
 * 
 */
public class Option_Continut_Activity_Reward_Sure extends Option {


	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {

		if (ActivityManagers.getInstance().getDdc().get(player.getId() + "用于连登补偿") == null) {
			String mailtitile = "连登奖励领取补偿";
			String mailcontent = "亲爱的玩家，为补偿连登领取奖励的问题，现赠予15个连登令旗作为补偿，注意查收";
			String mailcontent_green = "亲爱的玩家，为补偿连登领取奖励的问题，现赠予35个银块作为补偿，注意查收。";
			Set<String> set4 = new HashSet<String>();
			set4.add("天下无双");
			set4.add("海纳百川");
			set4.add("琼楼金阙");
			set4.add("飘渺仙道");
			set4.add("万里苍穹");
			set4.add("盛世欢歌");
			set4.add("修罗转生");
			MailManager mm = MailManager.getInstance();
			String servername = GameConstants.getInstance().getServerName();
			Article a = ArticleManager.getInstance().getArticle("银块");
			Article a2 = ArticleManager.getInstance().getArticle("连登令旗");
			ArticleEntity ae = null;
			
			try{
				if(set4.contains(servername)){
					ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 35, true);
					mm.sendMail(player.getId(), new ArticleEntity[]{ae}, new int[]{35}, mailtitile, mailcontent_green, 0, 0, 0, "补偿");
					player.sendError("恭喜您获得了："+ ae.getArticleName() + "*35");
					ActivityManagers.getInstance().getDdc().put(player.getId() + "用于连登补偿", player.getId());
					ActivitySubSystem.logger.warn("[通过按钮领取连登补偿] [成功] ["+player.getLogString()+"] [物品："+ae.getArticleName()+"] [数量：35]");
				}else{
					ae = ArticleEntityManager.getInstance().createEntity(a2, true, ArticleEntityManager.活动, player, a2.getColorType(), 15, true);
					mm.sendMail(player.getId(), new ArticleEntity[]{ae}, new int[]{15}, mailtitile, mailcontent, 0, 0, 0, "补偿");
					player.sendError("恭喜您获得了："+ ae.getArticleName() + "*15");
					ActivityManagers.getInstance().getDdc().put(player.getId() + "用于连登补偿", player.getId());
					ActivitySubSystem.logger.warn("[通过按钮领取连登补偿] [成功] ["+player.getLogString()+"] [物品："+ae.getArticleName()+"] [数量：15]");
				}
			}catch(Exception e){
				ActivitySubSystem.logger.warn("[通过按钮领取连登补偿] [异常] ["+player.getLogString()+"] ["+e+"]");
			}
		}else{
			player.sendError("您已经领取过了.");
		}

	}

}
