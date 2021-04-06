package com.fy.engineserver.activity.loginActivity;

import java.util.Calendar;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 任务奖励活动
 * 
 *
 */
public class TaskRewardActivity extends Activity{

	private long startTime;
	
	private long endTime;
	
	public Platform[] platforms;
	
	private Set<String> notOpenServers;
	
	private Set<String> openServers;
	
	private boolean isOpen = true;
	
	private int playerlevel;
	
	private Set<String> tasknames; 
	
	String articlenames [] = {"天录传送符","红玫瑰","软糖"};
	int articlecounts [] = {1,9,9};
	private String mailtitle = "好基友一“被”子奖励";
	private String mailcontent = "光棍节期间您的基友祝您早日脱光！脱光道具，请查收附件！";
	
	public TaskRewardActivity(int playerlevel,long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers,Set<String> tasknames){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		this.tasknames = tasknames;
		this.playerlevel = playerlevel;
	}
	
	@Override
	public boolean isEffective() {
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
	
	public boolean isRightTask(Player p,String taskname){
		try{
			return tasknames.contains(taskname) && p.getLevel()>playerlevel;
		}catch(Exception e){
			return false;
		}
	}

	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);

		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
		return year1==year2&&month1==month2&&day1==day2;
	}
	
	//成长稍后会统一改成配表形式，先这样写了
	@Override
	public void doPrizes(Player player, int index) {
		long now = System.currentTimeMillis();
		ActivitySubSystem.logger.warn("[完成任务额外奖励活动] [tasknames:"+tasknames+"] [pname:"+player.getName()+"] [3]");
		
		TaskRewardStat oldstat = (TaskRewardStat) ActivityManagers.getInstance().getDdc().get(player.getId()+getName());
		if(oldstat==null){
			TaskRewardStat stat = new TaskRewardStat();
			stat.setLastupdatetime(now);
			ActivityManagers.getInstance().getDdc().put(player.getId()+getName(), stat);
			oldstat = (TaskRewardStat) ActivityManagers.getInstance().getDdc().get(player.getId()+getName());
		}
	
		if(oldstat!=null){
			if(!isSameDay(now,oldstat.getLastupdatetime())){
				oldstat.setValue(0);
				oldstat.setLastupdatetime(now);
			}else{
				
				if(oldstat.getValue()<2){
					ArticleEntity aes [] = new ArticleEntity[articlenames.length];
					for(int i=0,len=articlenames.length;i<len;i++){
						Article a = ArticleManager.getInstance().getArticle(articlenames[i]);
						try {
							ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), articlecounts[i], true);
							if(ae!=null){
								aes[i] = ae;
							}
						} catch (Exception e) {
							e.printStackTrace();
							ActivitySubSystem.logger.warn("[完成任务额外奖励活动]  [异常] ["+player.getLogString()+"] ["+e+"]");
						}
					}
					
					if(aes.length>0){
						try {
							MailManager.getInstance().sendMail(player.getId(), aes, articlecounts, mailtitle, mailcontent, 0l, 0l, 0l, "完成任务额外奖励活动");
							oldstat.setValue(oldstat.getValue()+1);
							oldstat.setLastupdatetime(now);
							ActivityManagers.getInstance().getDdc().put(player.getId()+getName(), oldstat);
							ActivitySubSystem.logger.warn("[完成任务额外奖励活动] [成功]  ["+player.getLogString()+"]");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName()+endTime;
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

	public int getPlayerlevel() {
		return playerlevel;
	}

	public void setPlayerlevel(int playerlevel) {
		this.playerlevel = playerlevel;
	}

	public Set<String> getTasknames() {
		return tasknames;
	}

	public void setTasknames(Set<String> tasknames) {
		this.tasknames = tasknames;
	}

	public String[] getArticlenames() {
		return articlenames;
	}

	public void setArticlenames(String[] articlenames) {
		this.articlenames = articlenames;
	}

	public int[] getArticlecounts() {
		return articlecounts;
	}

	public void setArticlecounts(int[] articlecounts) {
		this.articlecounts = articlecounts;
	}

	public String getMailtitle() {
		return mailtitle;
	}

	public void setMailtitle(String mailtitle) {
		this.mailtitle = mailtitle;
	}

	public String getMailcontent() {
		return mailcontent;
	}

	public void setMailcontent(String mailcontent) {
		this.mailcontent = mailcontent;
	}

}
