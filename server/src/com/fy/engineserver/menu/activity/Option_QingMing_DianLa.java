package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivityManager;
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

public class Option_QingMing_DianLa extends Option {

	public String rewardname = "炼器符";
	public long costExp = 500000;
	
	@Override
	public void doSelect(Game game, Player player) {
		if(player==null || game==null){
			player.sendError(Translate.服务器出现错误);
			return;
		}
		
		if(player.getLevel()<10){
			player.sendError(Translate.等级不符);
			return;
		}
		
		if(player.getExp() < costExp){
			player.sendError(Translate.您的经验不足);
			return;
		}
		
		Article a = ArticleManager.getInstance().getArticle(rewardname);
		if(a==null){
			if(ActivitySubSystem.logger.isInfoEnabled()){
				ActivitySubSystem.logger.info("[清明活动] [点蜡] [物品不存在："+rewardname+"] ["+player.getLogString()+"]");
			}
			return;
		}
		
		Long lasttime = (Long)ActivityManagers.getInstance().getDdc().get(player.getId()+"点蜡");
		
		if(lasttime==null){
			ActivityManagers.getInstance().getDdc().put(player.getId()+"点蜡", 11L);
			lasttime = (Long)ActivityManagers.getInstance().getDdc().get(player.getId()+"点蜡");
		}
		
		if(ActivityManagers.isSameDay(lasttime.longValue(), System.currentTimeMillis())){
			player.sendError("您今天已经点过蜡了");
			return;
		}
		
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, 3, 1, true);
			if(ae==null){
				if(ActivitySubSystem.logger.isInfoEnabled()){
					ActivitySubSystem.logger.info("[清明活动] [点蜡] [创建物品失败："+rewardname+"] ["+player.getLogString()+"]");
				}
				return;
			}
			long oldexp = player.getExp();
			player.setExp(oldexp-costExp);
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, "恭喜您获得清明祭祀奖励", "请查收附件，领取奖励", 0, 0, 0, "清明点蜡");
			player.sendError("恭喜您点蜡成功，请在邮箱内查收奖励");
			ActivityManagers.getInstance().getDdc().put(player.getId()+"点蜡", System.currentTimeMillis());
			ActivitySubSystem.logger.info("[清明活动] [点蜡] [奖励成功] [经验:"+oldexp+"-->"+player.getExp()+"] [rewardname:"+rewardname+"] ["+player.getLogString()+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
