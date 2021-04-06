package com.fy.engineserver.menu.activity;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;

/**
 * 服务器维护，领取物品
 * @author Administrator
 * 
 */
public class Option_Bug_LSND_Reward_Sure extends Option {

	private String articleNames[] = {"玉液锦囊(绿色)","玉液锦囊(蓝色)"};
	private int articleNums[] = {4,1};

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public void doSelect(Game game, Player player) {
		if (ActivityManagers.getInstance().getDdc().get(player.getId() + "育兽丹bug") == null) {
			ArticleEntity aes [] = new ArticleEntity[articleNames.length];
			int counts [] = new int[articleNames.length];
			try{
				for(int i=0;i<articleNames.length;i++){
					String articlename = articleNames[i];
					int articlenum = articleNums[i];
					Article a = ArticleManager.getInstance().getArticle(articlename);
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), articlenum, true);
					if(ae!=null){
						aes[i]= ae;
						counts[i] = articlenum;
					}
				}
				if(aes.length>0){
					MailManager.getInstance().sendMail(player.getId(), aes, counts, "紧急维护补偿", "尊敬的玩家您好，对于此次维护给您带的不便，我们深表歉意！在此飘渺寻仙曲运营团队，特别为您准备了，玉液锦囊(绿色)*4、玉液锦囊(蓝色)*1作为游戏维护给您带来损失的补偿！\n       飘渺寻仙曲运营团队    2013-9-14", 0, 0, 0, "育兽丹bug");
					player.sendError("恭喜您成功领取了奖励.");
					ActivitySubSystem.logger.warn("[育兽丹bug] [领取成功] ["+player.getLogString()+"]");
					ActivityManagers.getInstance().getDdc().put(player.getId() + "育兽丹bug", 11);
				}
			}catch(Throwable e){
				ActivitySubSystem.logger.warn("[育兽丹bug] [异常] ["+player.getLogString()+"] ["+e+"]");
			}
		}else{
			player.sendError("您已经领取过了！");
		}
		
	}

}
