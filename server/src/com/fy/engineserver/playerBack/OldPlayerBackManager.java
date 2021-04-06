package com.fy.engineserver.playerBack;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;

public class OldPlayerBackManager {

	private String fileName;
	DiskCache disk = null;
	
	public static int 老玩家回馈的等级1 = 30;
	public static String 老玩家回馈物品1 = Translate.老友一重礼;
//	public static String 老玩家回馈物品1 = "灵狐蛋";
	public static int 老玩家回馈的等级2 = 70;
	public static String 老玩家回馈物品2 = Translate.老友二重礼;
//	public static String 老玩家回馈物品2 = "狂狼蛋";
	public static int 老玩家回馈的等级3 = 1;
	public static String 老玩家回馈物品3 = Translate.老友三重礼;
//	public static String 老玩家回馈物品3 = "灵狐蛋";
	public static int 老玩家升级回馈级别 = 70;
	
	
	private static OldPlayerBackManager instance;
	public static OldPlayerBackManager getInstance(){
		return instance;
	}
	
	public void init() throws Exception{
		
		
		instance = this;
		File file = new File(fileName);
		disk = new DefaultDiskCache(file,null,"oldPlayer", 100L * 365 * 24 * 3600 * 1000L);
		
		ServiceStartRecord.startLog(this);
	}
	
	
	public void loadPlayer(String serverName)throws Exception{
		try{
			GamePlayerManager gp = (GamePlayerManager)PlayerManager.getInstance();
			SimpleEntityManager<Player> em = gp.em;
			long[] ids = em.queryIds(Player.class, "");
			List<IOldplayerInfo> list = em.queryFields(IOldplayerInfo.class, ids);
			
			if(list != null && list.size() > 0){
				OldPlayerInfo info = null;
				for(IOldplayerInfo Iinfo : list){
					String userName = Iinfo.getUsername();
					if(disk.get(userName) != null){
						info = (OldPlayerInfo) disk.get(Iinfo.getUsername());
						if(info.getLevel() < Iinfo.getLevel()){
							info.setLevel(Iinfo.getLevel());
						}
					}else{
						info = new OldPlayerInfo(Iinfo.getUsername(),Iinfo.getLevel());
					}
					disk.put(info.getUserName(), info);
					SocialManager.logger.warn("[加载某玩家成功] ["+serverName+"] ["+info.getUserName()+"] ["+info.getLevel()+"]");
				}
				
				SocialManager.logger.warn("[loadOldPlayer成功] ["+serverName+"] [这个服:"+list.size()+"]");
			}else{
				SocialManager.logger.warn("[loadOldPlayer失败] ["+(list != null?list.size():"null")+"]");
			}
		}catch(Exception e){
			SocialManager.logger.warn("[loadOldPlayer异常] ["+serverName+"]",e);
			throw e;
		}
	}

	
	//注册老玩家回馈
	public void registerOldPlayerBack(Player player){
		String userName = player.getUsername();
		try{
			if(disk != null){
				if(disk.get(userName) == null){
					SocialManager.logger.warn("[玩家注册不是老玩家] ["+userName+"]");
				}else{
					
					OldPlayerInfo info = (OldPlayerInfo) disk.get(userName);
					
					int level = info.getLevel();
					if(level >= 老玩家回馈的等级1 && level < 老玩家回馈的等级2){
						Article article = ArticleManager.getInstance().getArticle(老玩家回馈物品1);
						if(article != null){
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.老玩家回归, null, article.getColorType(), 1,true);
							player.putToKnapsacks(ae, "老玩家注册回馈");
//							player.sendError("获得一个老玩家回归奖励"+老玩家回馈物品1);
						}else{
							SocialManager.logger.warn("[玩家注册是老玩家回馈失败] [没有对应article] ["+userName+"] ["+老玩家回馈物品1+"]");
						}
					}else if(level >= 老玩家回馈的等级2){
						Article article = ArticleManager.getInstance().getArticle(老玩家回馈物品2);
						if(article != null){
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.老玩家回归, null, article.getColorType(), 1,true);
							player.putToKnapsacks(ae, "老玩家注册回馈");
//							player.sendError("获得一个老玩家回归奖励"+老玩家回馈物品2);
						}else{
							SocialManager.logger.warn("[玩家注册是老玩家回馈失败] [没有对应article] ["+userName+"] ["+老玩家回馈物品2+"]");
						}
					}
					SocialManager.logger.warn("[玩家注册是老玩家回馈成功] ["+userName+"] ["+info.getLevel()+"]");
				}
			}else{
				SocialManager.logger.error("[老玩家回馈错误] [map null] ["+userName+"]");
			}
		}catch(Exception e){
			SocialManager.logger.error("[老玩家回馈异常] ["+userName+"]",e);
		}
		
	}
	
	//升级老玩家回馈
	public void levelUpOldPlayerBack(Player player){
		
		if(player.getLevel() == 老玩家升级回馈级别){
			String userName = player.getUsername();
			try{
				if(disk != null){
					if(disk.get(userName) == null){
						SocialManager.logger.warn("[玩家升级不是老玩家] ["+userName+"]");
					}else{
						OldPlayerInfo info = (OldPlayerInfo) disk.get(userName);
						if(info == null){
							return;
						}
						int level = info.getLevel();
						if(level >= 老玩家回馈的等级3){
							Article article = ArticleManager.getInstance().getArticle(老玩家回馈物品3);
							if(article != null){
								ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.老玩家回归, null, article.getColorType(), 1,true);
//								player.putToKnapsacks(ae, "老玩家升级回馈");
								
								if(ae != null){
									String mailContent = Translate.老玩家回归奖励;
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, mailContent, mailContent, 0, 0, 0, Translate.老玩家回归);
									SocialManager.logger.error("[玩家升级发送奖励成功] ["+player.getLogString()+"] [物品id:"+ae.getId()+"]");
//									player.send_HINT_REQ("获得一个老玩家回归奖励"+老玩家回馈物品3+"，已发送到您的邮箱");
									player.send_HINT_REQ(Translate.translateString(Translate.获得一个老玩家回归奖励xx已发送到您的邮箱, new String[][]{{Translate.STRING_1,老玩家回馈物品3}}));
								}else{
									SocialManager.logger.error("[玩家升级发送奖励错误] ["+player.getLogString()+"]");
								}
							}else{
								SocialManager.logger.warn("[玩家升级是老玩家回馈失败] [没有对应article] ["+userName+"] ["+老玩家回馈物品3+"]");
							}
						}
						SocialManager.logger.warn("[玩家升级是老玩家回馈成功] ["+userName+"] ["+info.getLevel()+"]");
					}
				}else{
					SocialManager.logger.error("[老玩家升级回馈错误] [map null] ["+userName+"]");
				}
			}catch(Exception e){
				SocialManager.logger.error("[老玩家升级回馈异常] ["+userName+"]",e);
			}
		}
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public static class OldPlayerInfo implements Serializable{
		
		private static final long serialVersionUID = 179686190299184201L;
		
		String userName;
		int level;
		public OldPlayerInfo(String userName,int level){
			this.userName = userName;
			this.level = level;
		}
		
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
	}
	
	
}
