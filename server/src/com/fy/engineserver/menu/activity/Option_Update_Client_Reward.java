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
 * 空袭大回馈
 * 
 *
 */
public class Option_Update_Client_Reward extends Option implements NeedCheckPurview{
	
	/**
	 * 对于这一类点击NPC领取奖励的，可以统一由一个manager去管理
	 * 以后简单配置就能用
	 */
	private String startTime = "2013-06-08 00:00:00";
	private String endTime = "2013-10-17 23:59:59";
	private Platform[] openplats = { Platform.官方 };
	private int playerlevel = 110;
//	private static Set<String> openchannels = new HashSet<String>();
	private Set<String> limitservers = new HashSet<String>();
	private String articlename = "空袭圣宠锦囊";
	private String mailtitle = "空袭渡劫新版回馈";
	private String mailcount = "为了迎接仙界大陆的降临，在下次维护之前，所有仙友都将获得一个空袭圣宠锦囊！";
	private String 背包满提示 = "背包已满!";
	private String 已经领取过 = "已经领取过了!";
	
//	static{
//		openchannels.add("KOREAKT_MIESHI");
//	}
	
	@Override
	public void doSelect(Game game, Player player) {
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		if(ddc!=null && ddc.get(player.getId()+"20130925更端")==null){
			Article a = ArticleManager.getInstance().getArticle(articlename); 
			if(a==null){
				ActivitySubSystem.logger.warn("[20130925更端活动] [物品不存在] [玩家信息："+player.getLogString()+"] ["+articlename+"]");
				return;
			}
			
			ArticleEntity ae = null;
			
			try {
				ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 0, true);
			} catch (Exception e) {
				e.printStackTrace();
				ActivitySubSystem.logger.warn("[20130925更端活动] [创建物品异常] [玩家信息："+player.getLogString()+"] ["+articlename+"]",e);
				return;
			}
			
			if(ae!=null){
				if(player.getKnapsack_common().isFull()){
					MailManager mm = MailManager.getInstance();
					try {
						mm.sendMail(player.getId(), new ArticleEntity[]{ae}, mailtitle, mailcount, 0, 0, 0, "20130925更端活动");
						ActivitySubSystem.logger.warn("[20130925更端活动] [领取礼包成功] [邮件发送] ["+player.getLogString()+"]");
						player.sendError("恭喜您获得了："+articlename+"请查看邮件！");
					} catch (Exception e) {
						e.printStackTrace();
						ActivitySubSystem.logger.warn("[20130925更端活动] [通过邮件发送奖励异常] [玩家信息："+player.getLogString()+"] ["+articlename+"]",e);
					}
					player.sendError(背包满提示);
				}else{
					player.putToKnapsacks(ae, "20130925更端活动");
					ActivitySubSystem.logger.warn("[20130925更端活动] [领取礼包成功] [放入背包] ["+player.getLogString()+"]");
					player.sendError("恭喜您获得了："+articlename);
				}
				ddc.put(player.getId()+"20130925更端", player.getId());
			}
		}else{
			player.sendError(已经领取过);
			ActivitySubSystem.logger.warn("[20130925更端活动] [已经领取过] ["+player.getLogString()+"]");
		}
	}
	
	@Override
	public boolean canSee(Player player) {
		long now = System.currentTimeMillis();
		if(!PlatformManager.getInstance().isPlatformOf(openplats)){
//			ActivitySubSystem.logger.warn("[20130925更端活动] [平台不符]");
			return false;
		}
		
		if(player.getLevel()<playerlevel){
//			ActivitySubSystem.logger.warn("[20130925更端活动] [等级不符]");
			return false;
		}
		
		GameConstants gc = GameConstants.getInstance();
		if(gc==null){
//			ActivitySubSystem.logger.warn("[20130925更端活动] [gc==null]");
			return false;
		}
		
		if(limitservers.contains(gc.getServerName())){
//			ActivitySubSystem.logger.warn("[20130925更端活动] [limitservers==null]");
			return false;
		}
		
		if(TimeTool.formatter.varChar19.parse(startTime) > now || now > TimeTool.formatter.varChar19.parse(endTime)){
//			ActivitySubSystem.logger.warn("[20130925更端活动] [时间不否]");
			return false;
		}
		
		return true;
	}

	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}
	
}


