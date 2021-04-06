package com.fy.engineserver.menu.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.NeedInitialiseOption;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.StringTool;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.engineserver.util.console.MConsoleManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 合服活动配置
 * 
 *
 */
public class Option_Buchang extends Option implements NeedCheckPurview , MConsole,NeedInitialiseOption{
	
	
	@ChangeAble("startTime")
	private String startTime = "2013-08-12 00:00:00";
	@ChangeAble("endTime")
	private String endTime = "2013-09-19 23:59:59";
	@ChangeAble("openplats")
	private String platformstr;
	private Platform platforms[];
	
	@ChangeAble("openServers")
	private String[] openServers;		//服务器名字和奖励一一对应
	private String openserversstr;
	private String articlenamestr;
	private String articlenumstr;
	private String articlecolorstr;
	@ChangeAble("articlenames")
	private String[] articlenames;
	private int[] articlenums;
	private int[] articlecolors;
	
	@ChangeAble("mailtitle")
	private String mailtitle = Translate.text_4931;
	@ChangeAble("mailcount")
	private String mailcount = Translate.text_4931;
	@ChangeAble("背包满提示") 
	private String 背包满提示 = Translate.背包空间不足;
	@ChangeAble("是否是一次奖励")		//true：活动时间内只能领取一次；false：每天都可以领取一次 
	private String isOneTimeRewardstr;
	private boolean isOneTimeReward;
	@ChangeAble("已经领取过提示")	
	private String 已经领取过 = Translate.您已经领取过此奖励;
	@ChangeAble("levellimit")	
	private int levellimit = 70;
	@ChangeAble("activitykey")	
	private String activitykey;		//唯一key
	private String noticemess = Translate.等级不符;
	
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
	
	@Override
	public void doSelect(Game game, Player player) {
		if(player.getLevel()<71){
			player.sendError(noticemess);
			return;
		}
		
		if(isOneTimeReward){
			DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
			Long nowtiem = (Long)ddc.get(player.getId()+activitykey);
			if(nowtiem==null){
				ddc.put(player.getId()+activitykey, new Long(11));
				doPrice(game,player,ddc);
			}else{
				player.sendError(已经领取过);
			}
		}else{
			DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
			Long nowtiem = (Long)ddc.get(player.getId()+activitykey);
			if(nowtiem==null){
				ddc.put(player.getId()+activitykey, new Long(11));
				nowtiem = (Long)ddc.get(player.getId()+activitykey);
			}
			if(nowtiem!=null){
				if(!isSameDay(System.currentTimeMillis(),nowtiem.longValue())){
					doPrice(game,player,ddc);
				}else{
					player.sendError(Translate.今天已经领取);
				}
			}else{
				ActivitySubSystem.logger.warn("["+activitykey+"] [ddc==null] ["+player.getLogString()+"]");
			}
		}
		
	}
	
	public void doPrice(Game game, Player player,DefaultDiskCache ddc){

		GameConstants gc = GameConstants.getInstance();
		String servername = gc.getServerName();
		int index = -1;
		for(int i=0;i<openServers.length;i++){
			if(openServers[i].equals(servername)){
				index = i;
			}
		}
		if(index>=0){
			String articlename = articlenames[index];
			Article a = ArticleManager.getInstance().getArticle(articlename); 
			if(a==null){
				ActivitySubSystem.logger.warn("["+activitykey+"] [物品不存在] [玩家信息："+player.getLogString()+"] ["+articlename+"]");
				return;
			}
			
			ArticleEntity ae = null;
			
			try {
				ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, a.getColorType(), 0, true);
			} catch (Exception e) {
				e.printStackTrace();
				ActivitySubSystem.logger.warn("["+activitykey+"] [创建物品异常] [玩家信息："+player.getLogString()+"] ["+articlename+"]",e);
				return;
			}
			
			if(ae!=null){
				boolean issucc = false;
				if(player.getKnapsack_common().isFull()){
					MailManager mm = MailManager.getInstance();
					try {
						mm.sendMail(player.getId(), new ArticleEntity[]{ae}, mailtitle, mailcount, 0, 0, 0, activitykey);
						ActivitySubSystem.logger.warn("["+activitykey+"] [领取礼包成功] [邮件发送] ["+player.getLogString()+"]");
						issucc = true;
					} catch (Exception e) {
						e.printStackTrace();
						ActivitySubSystem.logger.warn("["+activitykey+"] [通过邮件发送奖励异常] [玩家信息："+player.getLogString()+"] ["+articlename+"]",e);
					}
					player.sendError(背包满提示);
				}else{
					player.putToKnapsacks(ae, activitykey);
					ActivitySubSystem.logger.warn("["+activitykey+"] [领取礼包成功] [放入背包] ["+player.getLogString()+"]");
					issucc = true;
				}
				if(issucc){
					ddc.put(player.getId()+activitykey, new Long(System.currentTimeMillis()));
					player.sendError(Translate.translateString(Translate.恭喜您获得了, new String[][]{{Translate.STRING_1,articlename},{Translate.COUNT_1,String.valueOf(1)}}));
				}
			}
		}
	
	}
	
	@Override
	public boolean canSee(Player player) {
		long now = System.currentTimeMillis();
		
		if(!PlatformManager.getInstance().isPlatformOf(platforms)){
			return false;
		}
		
		if(player.getLevel()<=levellimit){
			return false;
		}
		
		try{
			if(TimeTool.formatter.varChar19.parse(startTime) > now || now > TimeTool.formatter.varChar19.parse(endTime)){
				return false;
			}
		}catch(Exception e){
			ActivitySubSystem.logger.warn("["+activitykey+"] [cansee] [异常] ["+e+"]");
			return false;
		}
		
		
		GameConstants gc = GameConstants.getInstance();
		if(gc==null){
			return false;
		}
		
		if(openServers!=null){
			for(String name:openServers){
				if(name.equals(gc.getServerName())){
					return true;
				}
			}
			return false;
		}
		
		return true;
	}

	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	}

	@Override
	public String getMConsoleName() {
		return "合服活动参数配置";
	}

	@Override
	public String getMConsoleDescription() {
		return "合服活动配置";
	}

	@Override
	public void init() throws Exception {
		MConsoleManager.register(this);
		if(openserversstr==null || openserversstr.trim().length()<=0){
			throw new Exception("合服的服务器配置为空，请检查参数[openserversstr]");
		}
		if(articlenamestr==null || articlenamestr.trim().length()<=0){
			throw new Exception("合服的奖励物品配置为空，请检查参数[articlenamestr]");
		}
		if(startTime==null || startTime.trim().length()<=0){
			throw new Exception("合服的开始时间配置为空，请检查参数[startTime]");
		}
		if(endTime==null || endTime.trim().length()<=0){
			throw new Exception("合服的开始时间配置为空，请检查参数[endTime]");
		}
		if(isOneTimeRewardstr==null || isOneTimeRewardstr.trim().length()<=0){
			throw new Exception("合服的活动类型配置为空，请检查参数[isOneTimeRewardstr]");
		}
		if(platformstr==null || platformstr.trim().length()<=0){
			throw new Exception("合服的平台类型配置为空，请检查参数[platformstr]");
		}
		if(activitykey==null || activitykey.trim().length()<=0){
			throw new Exception("合服的平台类型配置为空，请检查参数[platformstr]");
		}
		
		try{
			openServers = StringTool.string2Array(openserversstr, ",", String.class);
			articlenames = StringTool.string2Array(articlenamestr, ",", String.class);
			if(isOneTimeRewardstr.trim().equals("true")){
				isOneTimeReward = true;
			}else{
				isOneTimeReward = false;
			}
			
			String ps[] = platformstr.split(",");
			if (ps != null && ps.length > 0) {
				List<Platform> pfs = new ArrayList<Platform>();
				for (int i = 0, len = ps.length; i < len; i++) {
					if(ps[i]!=null){
						Platform pf = PlatformManager.getInstance().getPlatForm(getPlatname(ps[i]));
						if(pf!=null){
							pfs.add(pf);
						}
					}
				}
				platforms = pfs.toArray(new Platform[]{});
			}
			ActivitySubSystem.logger.warn("["+activitykey+"] [初始化完成] ["+toString()+"]");
		}catch(Exception e){
			throw new Exception("合服活动初始化异常："+toString()+""+e);
		}
		
	}
	
	public String getPlatname(String name) {
		if (name.equals("官方")) {
			return "sqage";
		} else if (name.equals("腾讯")) {
			return "tencent";
		} else if (name.equals("台湾")) {
			return "taiwan";
		} else if (name.equals("马来")) {
			return "malai";
		} else if (name.equals("韩国")) {
			return "korea";
		}
		return "";
	}

	@Override
	public String toString() {
		return "Option_MergeServer_Config [startTime=" + startTime + ", endTime=" + endTime + ", platformstr=" + platformstr + ", openserversstr=" + openserversstr + ", articlenamestr=" + articlenamestr + ", isOneTimeReward=" + isOneTimeReward + ", levellimit=" + levellimit + ", activitykey=" + activitykey + "]";
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getPlatformstr() {
		return platformstr;
	}

	public void setPlatformstr(String platformstr) {
		this.platformstr = platformstr;
	}

	public Platform[] getPlatforms() {
		return platforms;
	}

	public void setPlatforms(Platform[] platforms) {
		this.platforms = platforms;
	}

	public String[] getOpenServers() {
		return openServers;
	}

	public void setOpenServers(String[] openServers) {
		this.openServers = openServers;
	}

	public String getOpenserversstr() {
		return openserversstr;
	}

	public void setOpenserversstr(String openserversstr) {
		this.openserversstr = openserversstr;
	}

	public String getArticlenamestr() {
		return articlenamestr;
	}

	public void setArticlenamestr(String articlenamestr) {
		this.articlenamestr = articlenamestr;
	}

	public String[] getArticlenames() {
		return articlenames;
	}

	public void setArticlenames(String[] articlenames) {
		this.articlenames = articlenames;
	}

	public String getMailtitle() {
		return mailtitle;
	}

	public void setMailtitle(String mailtitle) {
		this.mailtitle = mailtitle;
	}

	public String getMailcount() {
		return mailcount;
	}

	public void setMailcount(String mailcount) {
		this.mailcount = mailcount;
	}

	public String getIsOneTimeRewardstr() {
		return isOneTimeRewardstr;
	}

	public void setIsOneTimeRewardstr(String isOneTimeRewardstr) {
		this.isOneTimeRewardstr = isOneTimeRewardstr;
	}

	public boolean isOneTimeReward() {
		return isOneTimeReward;
	}

	public void setOneTimeReward(boolean isOneTimeReward) {
		this.isOneTimeReward = isOneTimeReward;
	}

	public int getLevellimit() {
		return levellimit;
	}

	public void setLevellimit(int levellimit) {
		this.levellimit = levellimit;
	}

	public String getActivitykey() {
		return activitykey;
	}

	public void setActivitykey(String activitykey) {
		this.activitykey = activitykey;
	}

	public String getNoticemess() {
		return noticemess;
	}

	public void setNoticemess(String noticemess) {
		this.noticemess = noticemess;
	}
	
	
}


