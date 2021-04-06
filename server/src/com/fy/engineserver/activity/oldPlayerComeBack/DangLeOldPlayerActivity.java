package com.fy.engineserver.activity.oldPlayerComeBack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.console.ChangeAble;
import com.fy.engineserver.util.console.MConsole;
import com.fy.boss.authorize.model.Passport;
/**
 * 当乐老玩家回归活动
 * 
 *
 */
public class DangLeOldPlayerActivity extends ActivityConfig  implements MConsole{
	
	public String channelname = "DCN"; 
	
	@ChangeAble("lastlogintime")
	public static String lastlogintime = "2013-06-21 00:00:00";
	
	public static String []names = {"回归圣囊","回归仙囊","回归宝囊","回归奇囊","回归神囊","回归幻囊"};
	
	public DangLeOldPlayerActivity(){}
	
	public DangLeOldPlayerActivity(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,Set<String> openServers,int levelLimit[]){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.openServers = openServers;
		this.levelLimit = levelLimit;
	}
	
	public boolean isEffective(Player player) {
		ActivityStat stat = (ActivityStat)OldPlayerComeBackManager.getInstance().getDdc().get(activityKey(player));
		if(stat==null){
			//如果是第一次登陆，第一次登陆的时间要在“2013-06-21 00:00:00"之前
			long time = 0;
			try {
				time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastlogintime).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
				ActivitySubSystem.logger.warn("[当乐老玩家活动] [获取time] [异常] [是否补偿依据：玩家上次登陆时间"+(player.getPlayerLastLoginTime())+"] ["+player.getLogString()+"]",e);
				return false;
			}
			
			if(System.currentTimeMillis()>endTime){
				ActivitySubSystem.logger.warn(player.getLogString()+"System.currentTimeMillis()>endTime");
				return false;
			}
			
			if(time>0){
				if(player.getPlayerLastLoginTime()>time){
					ActivitySubSystem.logger.warn(player.getLogString()+"上次登录时间晚于活动要求时间");
					return false;
				}
			}
			ActivityStat state = new ActivityStat();
			state.setLasttime(player.getPlayerLastLoginTime());
			state.setActivitystat(1);
			//隔天奖励
			OldPlayerComeBackManager.getInstance().getDdc().put(activityKey(player), state);
		}
		
		if(super.isEffective(player)){
			Passport p = player.getPassport();
			if(p==null){
				ActivitySubSystem.logger.warn(player.getLogString()+"passport=null");
				return false;
			}
			String lastloginchannel = p.getLastLoginChannel();
			if(lastloginchannel==null || "".equals(lastloginchannel.trim())){
				ActivitySubSystem.logger.warn(player.getLogString()+"渠道串为空");
				return false;
			}
			if (p.getRegisterChannel() !=  null && (p.getRegisterChannel().matches("DCN\\d*_MIESHI"))) {
				ActivitySubSystem.logger.warn(player.getLogString()+"是当乐用户");
				return true;
			}
		}
		ActivitySubSystem.logger.warn(player.getLogString()+"不满足参与活动条件");
		return false;
	}

	@Override
	public void doPrizes(Player player) {
		ActivityStat stat =  (ActivityStat)OldPlayerComeBackManager.getInstance().getDdc().get(activityKey(player));
		if(stat!=null){
			long lasttime = stat.getLasttime();
			long 两次登陆相隔天数 = OldPlayerComeBackManager.getContinueLoginDays(lasttime , System.currentTimeMillis());
			String 上次领取时间 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lasttime);
			if(两次登陆相隔天数==0){
				player.sendError("您"+上次领取时间+"已经领取过了！");
				ActivitySubSystem.logger.warn(player.getLogString()+"[当乐老玩家活动] [不是当天首次登录]");
				return;
			}else if(两次登陆相隔天数==1){
				long oldvalue = stat.getValue();
				if(sendRewardByMail(oldvalue+1,player)){
					stat.setLasttime(System.currentTimeMillis());
					stat.setValue(oldvalue+1);
					OldPlayerComeBackManager.getInstance().getDdc().put(activityKey(player), stat);
					ActivitySubSystem.logger.warn("[当乐老玩家活动] [发奖成功] [更新统计数据] [连续登陆了:"+stat.getValue()+"天] ["+player.getLogString()+"]");
				}else{
					ActivitySubSystem.logger.warn("[当乐老玩家活动] [发奖异常] [连续登陆了几天:"+stat.getValue()+"] ["+player.getLogString()+"]");
				}
			}else if(两次登陆相隔天数>1){
				long oldvalue = stat.getValue();
				if(oldvalue>=6){
					ActivitySubSystem.logger.warn("[当乐老玩家活动] [发奖成功] [已经连续6天领取奖励]"+player.getLogString()+"]");
				}else{
					if(sendRewardByMail(1,player)){
						stat.setLasttime(System.currentTimeMillis());
						stat.setValue(1);
						OldPlayerComeBackManager.getInstance().getDdc().put(activityKey(player), stat);
						ActivitySubSystem.logger.warn("[当乐老玩家活动] [发奖成功] ["+(oldvalue==0?"新增":"隔天")+"统计数据] [连续登陆了:"+stat.getValue()+"天] ["+player.getLogString()+"]");
					}else{
						ActivitySubSystem.logger.warn("[当乐老玩家活动] [发奖异常] [连续登陆了几天:"+stat.getValue()+"] ["+player.getLogString()+"]");
					}
				}
			}
		}else{
			ActivitySubSystem.logger.warn("[当乐老玩家活动] [stat==null] [是否补偿依据：玩家上次登陆时间"+(player.getPlayerLastLoginTime())+"] ["+player.getLogString()+"]");
		}
	}
	
	/**
	 * type:第几天的奖励
	 * @param type
	 * @return
	 */
	public boolean sendRewardByMail(long days,Player p){
		int index = (int)days - 1;
		if(index<0){
			index = 0;
		}
//		if(index > names.length){
//			index = names.length;
//		}
		if(index>=0 && index<6){
			String rewardname = names[index];
			Article a = ArticleManager.getInstance().getArticle(rewardname);
			if(a==null){
				ActivitySubSystem.logger.warn("[当乐老玩家活动] [发送奖励] [物品:"+rewardname+"不存在] [index:"+index+"] [days:"+days+"]");
				return false;
			}
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.活动, p, a.getColorType(), 1, true);
				MailManager mm = MailManager.getInstance();
				mm.sendMail(p.getId(), new ArticleEntity[]{ae}, "飘渺寻仙曲欢迎您回家", "亲爱的老朋友，飘渺寻仙曲欢迎您回家，在这里有最真挚的朋友，在这里有最激情的岁月，在这里有最纯洁的友谊，在这里有最宁静的港湾，飘渺寻仙曲还为您准备了“"+rewardname+"”，请查收邮件活动期间内每日登录，您将会获得千载难逢宝宝、元气丹、飞行坐骑碎片、橙装、各种酒贴等超值道具。", 0, 0, 0, "当乐玩家回归你来我就送");
				p.sendError("奖励以邮件的形式发放，注意查收邮件哦！");
				ActivitySubSystem.logger.warn(p.getLogString()+"[当乐老玩家活动] [发送奖励成功]");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				ActivitySubSystem.logger.warn(p.getLogString()+"[当乐老玩家活动] [发送奖励] [创建:"+rewardname+"异常] [index:"+index+"] [days:"+days+"]",e);
				return false;
			}
		}
		return false;
	}

	@Override
	public String activityKey(Player player) {
		return player.getId()+"DangLeOldPlayerActivity";
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	@Override
	public String getMConsoleName() {
		return "当乐老用户活动";
	}

	@Override
	public String getMConsoleDescription() {
		return "活动相关数据";
	}

	
	
}
