package com.fy.engineserver.menu.activity;


import java.util.ArrayList;
import java.util.Arrays;
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
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 只能领一次配置
 * 
 *
 */
public class Option_Get_Reward_Only_One_Time extends Option implements NeedCheckPurview,NeedInitialiseOption{
	
	private String startTimeStr;
	private String endTimeStr;
	private long startTime;
	private long endTime;
	private int playerlevel;
	private String activityname = "活动名称";
	private String limitserverstr;
	private String limitservers [];
	private String openserverstr;
	private String openservers[];
	private String rewardAriclenamestr;
	private String rewardArticleNames [];
	private String colorstr;
	private int[] colors;
	private String countstr;
	private int counts[];
	private String platformstr;
	private Platform platforms[];
	
	private String mailtitle;
	private String mailcount;
	private String 背包满提示 = Translate.背包已满;
	private String 已经领取过 = Translate.您已经领取过此奖励;
	
	@Override
	public void doSelect(Game game, Player player) {
		DefaultDiskCache ddc = ActivityManagers.getInstance().getDdc();
		if(ddc!=null && ddc.get(player.getId()+activityname)==null){
			if(rewardArticleNames!=null){
				ArticleEntity aes [] = new ArticleEntity[rewardArticleNames.length];
				int cts [] = new int[rewardArticleNames.length];
				ArticleEntity ae = null;
				MailManager mm = MailManager.getInstance();
				for(int i=0,len=rewardArticleNames.length;i<len;i++){
					String rewardAticleName = rewardArticleNames[i];
					int color = colors[i];
					int count = counts[i];
					Article a = ArticleManager.getInstance().getArticle(rewardAticleName); 
					if(a==null){
						ActivitySubSystem.logger.warn("["+activityname+"] [物品不存在] [玩家信息："+player.getLogString()+"] ["+this.toString2()+"] [放入背包]");
						return;
					}
					try {
						ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, player, color, count, true);
					} catch (Exception e) {
						e.printStackTrace();
						ActivitySubSystem.logger.warn("["+activityname+"] [创建物品异常] [玩家信息："+player.getLogString()+"] ["+this.toString2()+"] [放入背包]",e);
						return;
					}
					
					if(ae!=null){
						aes[i] = ae;
						cts[i] = count;
					}
				}
				if(aes!=null && aes.length>0){
					List<ArticleEntity> entitys = new ArrayList<ArticleEntity>();
					List<Integer> nums = new ArrayList<Integer>();
					List<String> names = new ArrayList<String>();
					for(int i = 0;i < aes.length; i++ ){
						if(aes[i] != null){
							entitys.add(aes[i]);
							nums.add(cts[i]);
							names.add(aes[i].getArticleName());
							if(entitys.size() == 5 || i == aes.length-1){
								try {
									int ns [] = new int[nums.size()];
									for(int j = 0; j < nums.size(); j++){
										ns[j] = nums.get(j);
									}
									mm.sendMail(player.getId(),entitys.toArray(new ArticleEntity[]{}) ,ns, mailtitle, mailcount, 0, 0, 0, activityname);
									ActivitySubSystem.logger.warn("["+activityname+"] [领取礼包成功] [邮件发送] [物品:"+names+"] [物品对应数量:"+Arrays.toString(ns)+"] ["+player.getLogString()+"]");
									player.sendError(Translate.奖励已通过邮件的形式发送);
								} catch (Exception e) {
									e.printStackTrace();
									ActivitySubSystem.logger.warn("["+activityname+"] [通过邮件发送奖励异常] [玩家信息："+player.getLogString()+"] ["+this.toString2()+"] [放入背包]",e);
								}
								entitys.clear();
								nums.clear();
								names.clear();
							}
						}
					}
					ddc.put(player.getId()+activityname, player.getId());
				}
			}
		}else{
			player.sendError(已经领取过);
			ActivitySubSystem.logger.warn("["+activityname+"] [已经领取过] ["+player.getLogString()+"]");
		}
	}
	
	@Override
	public boolean canSee(Player player) {
		long now = System.currentTimeMillis();
		
		if(!PlatformManager.getInstance().isPlatformOf(platforms)){
			return false;
		}
		
		if(player.getLevel()<playerlevel){
			return false;
		}
		
		if(startTime > now || now > endTime){
			return false;
		}
		
		GameConstants gc = GameConstants.getInstance();
		if(gc==null){
			return false;
		}
		if(limitservers!=null){
			for(String name:limitservers){
				if(name.equals(gc.getServerName())){
					return false;
				}
			}
		}
		
		if(openservers!=null){
			for(String name:openservers){
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
	public void init() throws Exception {
		if (rewardAriclenamestr != null && rewardAriclenamestr.trim().length()>0) {
			if (startTimeStr == null || "".equals(startTimeStr) || endTimeStr == null || "".equals(endTimeStr)) {
				throw new IllegalStateException("[Option_ExchangeArticle] [参数配置错误] [时间配置为空] [" + getText() + "]");
			}
			startTime = TimeTool.formatter.varChar19.parse(startTimeStr);
			endTime = TimeTool.formatter.varChar19.parse(endTimeStr);
			if (limitserverstr != null) {
				if(limitserverstr.trim().length()>0){
					limitservers = StringTool.string2Array(limitserverstr, ",", String.class);
				}
			}else{
				limitservers = new String[0];
			}
			if (openserverstr != null) {
				if(openserverstr.trim().length()>0){
					openservers = StringTool.string2Array(openserverstr, ",", String.class);
				}
			}else{
				openservers = new String[0];
			}
			
			rewardArticleNames = StringTool.string2Array(rewardAriclenamestr, ",", String.class);
			
			if (colorstr != null) {
				if(colorstr.trim().length()>0){
					colors = StringTool.string2IntArr(colorstr, ",");
				}
			}else{
				throw new IllegalStateException("[Option_ExchangeArticle] [参数配置错误] [奖励物品颜色为空] [" + toString() + "]");
			}
			
			if (countstr != null) {
				if(countstr.trim().length()>0){
					counts = StringTool.string2IntArr(countstr, ",");
				}
			}else{
				throw new IllegalStateException("[Option_ExchangeArticle] [参数配置错误] [奖励物品数量为空] [" + toString() + "]");
			}
			
			if(platformstr!=null){
				if(platformstr.trim().length()>0){
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
						if(platforms==null){
							platforms = new Platform[]{};
						}
					}
					
				}
			}
			
			if(rewardArticleNames.length!=colors.length && rewardArticleNames.length!=counts.length){
				throw new Exception("奖励领取窗口加载错误，物品名字，颜色，数量的长度不一致！"+this.getText());
			}
			if(Game.logger.isWarnEnabled()){
				Game.logger.warn("[奖励领取窗口加载] [startTimeStr:"+startTimeStr+"] [endTimeStr:"+endTimeStr+"] [完成] [counts:"+Arrays.toString(counts)+"] [colors:"+Arrays.toString(colors)+"] [rewardArticleNames:"+Arrays.toString(rewardArticleNames)+"] [openservers:"+Arrays.toString(openservers)+"] [limitservers:"+Arrays.toString(limitservers)+"]");
			}
		} else {
			throw new IllegalStateException("[Option_ExchangeArticle] [参数配置错误] [奖励物品为空] [" + toString() + "]");
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
	
	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
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

	public int getPlayerlevel() {
		return playerlevel;
	}

	public void setPlayerlevel(int playerlevel) {
		this.playerlevel = playerlevel;
	}

	public String getActivityname() {
		return activityname;
	}

	public void setActivityname(String activityname) {
		this.activityname = activityname;
	}

	public String getLimitserverstr() {
		return limitserverstr;
	}

	public void setLimitserverstr(String limitserverstr) {
		this.limitserverstr = limitserverstr;
	}

	public String[] getLimitservers() {
		return limitservers;
	}

	public void setLimitservers(String[] limitservers) {
		this.limitservers = limitservers;
	}

	public String getOpenserverstr() {
		return openserverstr;
	}

	public void setOpenserverstr(String openserverstr) {
		this.openserverstr = openserverstr;
	}

	public String[] getOpenservers() {
		return openservers;
	}

	public void setOpenservers(String[] openservers) {
		this.openservers = openservers;
	}

	public String getRewardAriclenamestr() {
		return rewardAriclenamestr;
	}

	public void setRewardAriclenamestr(String rewardAriclenamestr) {
		this.rewardAriclenamestr = rewardAriclenamestr;
	}

	public String[] getRewardArticleNames() {
		return rewardArticleNames;
	}

	public void setRewardArticleNames(String[] rewardArticleNames) {
		this.rewardArticleNames = rewardArticleNames;
	}

	public String getColorstr() {
		return colorstr;
	}

	public void setColorstr(String colorstr) {
		this.colorstr = colorstr;
	}

	public int[] getColors() {
		return colors;
	}

	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public String getCountstr() {
		return countstr;
	}

	public void setCountstr(String countstr) {
		this.countstr = countstr;
	}

	public int[] getCounts() {
		return counts;
	}

	public void setCounts(int[] counts) {
		this.counts = counts;
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

	public String toString2() {
		return "Option_Get_Reward_Only_One_Time [activityname=" + activityname + ", colors=" + Arrays.toString(colors) + ", colorstr=" + colorstr + ", counts=" + Arrays.toString(counts) + ", countstr=" + countstr + ", endTime=" + endTime + ", endTimeStr=" + endTimeStr + ", limitservers=" + Arrays.toString(limitservers) + ", limitserverstr=" + limitserverstr + ", mailcount=" + mailcount + ", mailtitle=" + mailtitle + ", openservers=" + Arrays.toString(openservers) + ", openserverstr=" + openserverstr + ", playerlevel=" + playerlevel + ", rewardAriclenamestr=" + rewardAriclenamestr + ", rewardArticleNames=" + Arrays.toString(rewardArticleNames) + ", startTime=" + startTime + ", startTimeStr=" + startTimeStr+" ,platforms="+(platformstr==null);
	}

	public Platform[] getPf() {
		return platforms;
	}

	public void setPf(Platform[] pf) {
		this.platforms = pf;
	}

	public String get背包满提示() {
		return 背包满提示;
	}

	public void set背包满提示(String 背包满提示) {
		this.背包满提示 = 背包满提示;
	}

	public String get已经领取过() {
		return 已经领取过;
	}

	public void set已经领取过(String 已经领取过) {
		this.已经领取过 = 已经领取过;
	}

	public String getPlatformstr() {
		return platformstr;
	}

	public void setPlatformstr(String platformstr) {
		this.platformstr = platformstr;
	}

	@Override
	public String toString() {
		return "Option_Get_Reward_Only_One_Time [activityname=" + activityname + ", colors=" + Arrays.toString(colors) + ", colorstr=" + colorstr + ", counts=" + Arrays.toString(counts) + ", countstr=" + countstr + ", endTimeStr=" + endTimeStr + ", openservers=" + Arrays.toString(openservers) + ", openserverstr=" + openserverstr + ", playerlevel=" + playerlevel + ", rewardAriclenamestr=" + rewardAriclenamestr + ", rewardArticleNames=" + Arrays.toString(rewardArticleNames) + ", startTimeStr=" + startTimeStr + "]";
	}

	public String pageLog() {
		return "Option_Get_Reward_Only_One_Time [startTimeStr=" + startTimeStr + ", endTimeStr=" + endTimeStr + ", startTime=" + startTime + ", endTime=" + endTime + ", playerlevel=" + playerlevel + ", activityname=" + activityname + ", limitserverstr=" + limitserverstr + ", limitservers=" + Arrays.toString(limitservers) + ", openserverstr=" + openserverstr + ", openservers=" + Arrays.toString(openservers) + ", rewardAriclenamestr=" + rewardAriclenamestr + ", rewardArticleNames=" + Arrays.toString(rewardArticleNames) + ", colorstr=" + colorstr + ", colors=" + Arrays.toString(colors) + ", countstr=" + countstr + ", counts=" + Arrays.toString(counts) + ", platforms=" + Arrays.toString(platforms) + ", mailtitle=" + mailtitle + ", mailcount=" + mailcount + ", 背包满提示=" + 背包满提示 + ", 已经领取过=" + 已经领取过 + "]";
	}

	
}


