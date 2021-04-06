package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.economic.ExpenseReasonType;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

public class Option_QingMing_SongYuanBao extends Option {

	public String rewardname = "仙灵意志锦囊";
	public long costExp = 200000;
	
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
		
		if(player.getSilver() < costExp){
			player.sendError(Translate.text_cave_107);
			return;
		}
		
		Article a = ArticleManager.getInstance().getArticle(rewardname);
		if(a==null){
			if(ActivitySubSystem.logger.isInfoEnabled()){
				ActivitySubSystem.logger.info("[清明活动] [送元宝] [物品不存在："+rewardname+"] ["+player.getLogString()+"]");
			}
			return;
		}
		
		try {
			ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_huodong_libao, player, 3, 1, true);
			if(ae==null){
				if(ActivitySubSystem.logger.isInfoEnabled()){
					ActivitySubSystem.logger.info("[清明活动] [送元宝] [创建物品失败："+rewardname+"] ["+player.getLogString()+"]");
				}
				return;
			}
			long oldsiver = player.getSilver();
			BillingCenter.getInstance().playerExpense(player, costExp, CurrencyType.YINZI, ExpenseReasonType.活动, "清明活动送元宝");
			MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, "恭喜您获得清明祭祀奖励", "请查收附件，领取奖励", 0, 0, 0, "清明送元宝");
			player.sendError("恭喜您送元宝成功，请在邮箱内查收奖励");
			ActivitySubSystem.logger.info("[清明活动] [送元宝] [奖励成功] [银子:"+oldsiver+"-->"+player.getSilver()+"] [rewardname:"+rewardname+"] ["+player.getLogString()+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

}
