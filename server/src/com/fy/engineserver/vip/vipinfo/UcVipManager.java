package com.fy.engineserver.vip.vipinfo;

import com.fy.engineserver.activity.ActivityManager;
import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.xuanzhi.tools.cache.diskcache.DiskCache;

public class UcVipManager {
	public static DiskCache disk = ActivityManager.getInstance().disk;
	//发邮件
	public static void sendUcVipMail4EnterServer(String articleName,String playerId)
	{
		long startTime = System.currentTimeMillis();
		try
		{
			if(disk.get(playerId) == null)
			{
				Article a = ArticleManager.getInstance().getArticle(articleName);
				Player player = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.运营发放活动奖励, player, a.getColorType(), 1, true);
				MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { 1 }, "恭迎贵宾", "尊敬的贵宾仙友，您的到来为整个飘渺寻仙曲仙界增添了光彩，赠予您珍稀[贵宾锦囊]，开启后可收获神力道具，助您仙途如虎添翼！请在附件查收。", 0, 0, 0, "在线奖励");
				player.sendError("您已获得贵宾锦囊。");
				disk.put(playerId, System.currentTimeMillis());
				ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [成功] [ok] [" + articleName + "] ["+playerId+"] ["+player.getName()+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
			}
			else
			{
				Player player = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
				ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [失败] [已经发送过这个礼包了] [" + articleName + "] ["+playerId+"] ["+player.getName()+"] [送礼包的时间:"+disk.get(playerId)+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
			}
		
		}
		
		catch(Exception e)
		{
			ActivitySubSystem.logger.error("[UCVIP进入游戏服礼物赠送] [失败] [出现异常] [" + articleName + "] ["+playerId+"] [--] [cost:"+(System.currentTimeMillis() - startTime)+"ms]",e);
		}
	}
}
