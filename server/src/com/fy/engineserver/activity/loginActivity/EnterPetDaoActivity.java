package com.fy.engineserver.activity.loginActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.Activity;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.petdao.PetDao;
import com.fy.engineserver.sprite.petdao.PetDaoManager;
import com.xuanzhi.boss.game.GameConstants;

public class EnterPetDaoActivity extends Activity{
	
	private long startTime;
	
	private long endTime;
	
	public Platform[] platforms;
	
	private Set<String> notOpenServers;
	
	private Set<String> openServers;
	
	private boolean isOpen = true;
	
	static	Map<Integer, String> map = new HashMap<Integer, String>();
	static{
		map.put(new Integer(1), "天录传送符");
		map.put(new Integer(2), "天录传送符");
	}
	
	public EnterPetDaoActivity(){}
	
	public EnterPetDaoActivity(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
	}

	@Override
	public boolean isEffective() {
		if(!isOpen){
			return false;
		}
		ActivitySubSystem.logger.warn("[迷城送礼活动] [1]");
		long now = SystemTime.currentTimeMillis();
		if(startTime > now || endTime < now){
			return false;
		}
		if (!PlatformManager.getInstance().isPlatformOf(getPlatforms())) {
			return false;
		}
		ActivitySubSystem.logger.warn("[迷城送礼活动] [2]");
		GameConstants gc = GameConstants.getInstance();
		if (gc == null || notOpenServers.contains(gc.getServerName())) {
			return false;
		}
		ActivitySubSystem.logger.warn("[迷城送礼活动] [3]");
		if(openServers.contains(gc.getServerName())){
			return true;
		}
		/**开放的服务器为空，该平台都符合**/
		if(openServers.size()==0){
			return true;
		}
		ActivitySubSystem.logger.warn("[迷城送礼活动] [4]");
		return false;
	}

	public void doPrizes(Player player, String mapname,int michengtype) {
		// TODO Auto-generated method stub
		ActivitySubSystem.logger.warn("[迷城送礼活动] [player:"+player.getName()+"] [gameName:"+mapname+"]");
		if (PetDaoManager.getInstance().isPetDao(mapname)) {
			try{
					int type = michengtype;
					String rewardname = map.get(new Integer(type));
					ActivitySubSystem.logger.warn("[迷城送礼活动] [player:"+player.getName()+"] [gameName:"+mapname+"] [type:"+type+"] [rewardname:"+rewardname+"]");
					if(rewardname==null || rewardname.trim().length()==0){
						ActivitySubSystem.logger.warn("[迷城送礼活动] [奖励名字为空："+rewardname+"] ["+player.getLogString()+"] [type:"+type+"] ");
					}
					Article aa = ArticleManager.getInstance().getArticle(rewardname);
					if(aa!=null){
						try {
							int count = 0;
							if(type==1){
								count = 10;
							}else if(type == 2){
								count = 20;
							}
							ActivitySubSystem.logger.warn("[迷城送礼活动] [player:"+player.getName()+"] [gameName:"+mapname+"] [type:"+type+"] [rewardname:"+rewardname+"] [count:"+count+"]");
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(aa, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player,aa.getColorType(),count, true);
							try {
								MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae},new int[]{count},Translate.九宫秘境大乐透, Translate.九宫秘境大乐透, 0, 0, 0, "进入迷城送礼活动");
								ActivitySubSystem.logger.warn("[迷城送礼活动] [OK] ["+player.getLogString()+"] [奖励："+aa.getName()+"] [数量:"+count+"] ");
							} catch (Exception e) {
								e.printStackTrace();
								ActivitySubSystem.logger.warn("[迷城送礼活动]  [错误]",e);
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							ActivitySubSystem.logger.warn("[迷城送礼活动]  [异常] ["+player.getLogString()+"] [异常："+e1+"]");
						}
					}else{
						ActivitySubSystem.logger.warn("[迷城送礼活动] [错误] [物品不存在:"+rewardname+"]");
					}
			}catch(Exception e){
				e.printStackTrace();
				ActivitySubSystem.logger.warn("[迷城送礼活动] ["+player.getLogString()+"] ["+e+"]");
			}
		}
			
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

	@Override
	public void doPrizes(Player player, int index) {
		// TODO Auto-generated method stub
		
	}

}
