package com.fy.engineserver.activity.loginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fy.engineserver.billboard.special.SpecialEquipmentManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

public class OldPlayerActivity extends Activity{
	
	private long startTime;
	
	private long endTime;
	
	public Platform[] platforms;
	
	private Set<String> notOpenServers;
	
	private Set<String> openServers;
	
	private boolean isOpen = true;
	
	public OldPlayerActivity(){}
	
	public OldPlayerActivity(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
	}

	@Override
	public boolean isEffective() {
		// TODO Auto-generated method stub
		if(!isOpen){
			return false;
		}
		
		long now = SystemTime.currentTimeMillis();
		if(startTime > now || endTime < now){
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(getPlatforms())) {
			return false;
		}
		GameConstants gc = GameConstants.getInstance();
		if (gc == null || notOpenServers.contains(gc.getServerName())) {
			return false;
		}
		
		if(openServers.contains(gc.getServerName())){
			return true;
		}
		/**开放的服务器为空，该平台都符合**/
		if(openServers.size()==0){
			return true;
		}
		
		return false;
	}

	@Override
	public void doPrizes(Player player, int index) {
		// TODO Auto-generated method stub
		/**
		List<ArticleEntity> aes = getPlayerEquipmentByLevel(player);
		if(aes!=null && aes.size()>0){
			try {
				List<ArticleEntity> aes1 = aes.subList(0, 5);
				List<ArticleEntity> aes2 = aes.subList(5, 10);
				if(aes1!=null){
					MailManager.getInstance().sendMail(player.getId(), aes1.toArray(new ArticleEntity[]{}), Translate.老玩家回归活动邮件标题, Translate.老玩家回归活动邮件内容, 0, 0, 0, "老玩家回归活动");
					ActivityManagers.logger.warn("[老玩家回归活动] [发装备1] [OK] ["+player.getLogString()+"] ["+aes1.size()+"]");
				}
				if(aes2!=null){
					MailManager.getInstance().sendMail(player.getId(), aes2.toArray(new ArticleEntity[]{}), Translate.老玩家回归活动邮件标题, Translate.老玩家回归活动邮件内容, 0, 0, 0, "老玩家回归活动");
					ActivityManagers.logger.warn("[老玩家回归活动] [发装备2] [OK] ["+player.getLogString()+"] ["+aes2.size()+"]");
				}
			} catch (Exception e) {
				e.printStackTrace();
				ActivityManagers.logger.warn("[老玩家回归活动] [发装备] [错误] ["+aes.size()+"]",e);
			}
		}	
			String jiuname = "";
			if (player.getLevel() < 81) {
				jiuname = Translate.白玉泉;
			} else if (player.getLevel() < 161) {
				jiuname = Translate.金浆醒;
			} else {
				jiuname = Translate.香桂郢酒;
			}
			
			List<ArticleEntity> jius = new ArrayList<ArticleEntity>();
			
			Article aa = ArticleManager.getInstance().getArticle(jiuname);
			if(aa!=null){
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(aa, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, 4,5, true);
					if(ae!=null){
						jius.add(ae);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					ActivityManagers.logger.warn("[老玩家回归活动] [发送酒] [异常] ["+player.getLogString()+"] [异常："+e1+"]");
				}
			}else{
				ActivityManagers.logger.warn("[老玩家回归活动] [发送酒] [错误] [物品不存在:"+jiuname+"]");
			}
			
			if(jius!=null && jius.size()>0){
				try {
					MailManager.getInstance().sendMail(player.getId(), jius.toArray(new ArticleEntity[]{}),new int[]{5},Translate.老玩家回归活动邮件标题, Translate.老玩家回归活动邮件内容, 0, 0, 0, "老玩家回归活动");
					ActivityManagers.logger.warn("[老玩家回归活动] [发送酒] [OK] ["+player.getLogString()+"] ["+aes.size()+"] ["+jiuname+"]");
				} catch (Exception e) {
					e.printStackTrace();
					ActivityManagers.logger.warn("[老玩家回归活动] [发送酒] [错误] ["+aes.size()+"]",e);
				}
			}
			
			player.sendError(Translate.老玩家回归活动邮件内容);
			*/
		
		try{
			Article aa = ArticleManager.getInstance().getArticle("回归老友礼包");
			if(aa!=null){
				try {
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(aa, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, aa.getColorType(),1, true);
					if(ae != null){
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae},Translate.老玩家回归活动邮件标题, Translate.老玩家回归活动邮件内容, 0, 0, 0, "老玩家回归活动");
						ActivityManagers.logger.warn("[老玩家回归活动] [lastlogintime:"+TimeTool.formatter.varChar23.format(player.getLoginServerTime())+"] [成功] ["+player.getLogString()+"]");
					}else{
						ActivityManagers.logger.warn("[老玩家回归活动] [错误] [ae不存在:回归老友礼包]");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					ActivityManagers.logger.warn("[老玩家回归活动] [异常] ["+player.getLogString()+"] [异常："+e1+"]");
				}
			}else{
				ActivityManagers.logger.warn("[老玩家回归活动] [错误] [物品不存在:回归老友礼包]");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private List<ArticleEntity> getPlayerEquipmentByLevel(Player p){
		Article[] allArticles = ArticleManager.getInstance().getAllArticles();
		List<ArticleEntity> list = new ArrayList<ArticleEntity>();
		if(p.getSoulLevel()>=40){
			for(Article a:allArticles){
				if(a instanceof Equipment){
					Equipment e = (Equipment)a;
					if(e.getEquipmentType()<10){
						if(e.getCareerLimit()==p.getCareer()){
							if(e.getSpecial() != SpecialEquipmentManager.伏天古宝 && e.getSpecial() != SpecialEquipmentManager.鸿天帝宝){
								if((e.getPlayerLevelLimit()-1)/20 == (p.getSoulLevel()-1)/20){
									Article aa = ArticleManager.getInstance().getArticle(e.getName());
									if(aa!=null){
										ArticleEntity ae;
										try {
											ae = ArticleEntityManager.getInstance().createEntity(aa, true, ArticleEntityManager.CREATE_REASON_huodong_libao, p, 5,1, true);
											list.add(ae);
										} catch (Exception e1) {
											e1.printStackTrace();
											ActivityManagers.logger.warn("[老玩家回归活动] [获得等级装备] [异常] ["+p.getLogString()+"] [异常："+e1+"]");
										}
									}else{
										ActivityManagers.logger.warn("[老玩家回归活动] [获得等级装备] [错误] [物品不存在"+e.getName()+"]");
									}
								}
							}
						}
					}
				}
			}
		}
		return list;
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName()+"201306031017";
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public Platform[] getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Platform[] platforms) {
		this.platforms = platforms;
	}

	public Set<String> getNotOpenServers() {
		return notOpenServers;
	}

	public void setNotOpenServers(Set<String> notOpenServers) {
		this.notOpenServers = notOpenServers;
	}

	public Set<String> getOpenServers() {
		return openServers;
	}

	public void setOpenServers(Set<String> openServers) {
		this.openServers = openServers;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

}
