package com.fy.engineserver.activity.newChongZhiActivity;

import java.util.Arrays;

import com.fy.engineserver.activity.ActivitySubSystem;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.economic.CurrencyType;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.fy.engineserver.util.server.TestServerConfigManager;
import com.fy.boss.authorize.model.Passport;
import com.sqage.stat.client.StatClientService;
import com.sqage.stat.model.GameChongZhiFlow;
import com.xuanzhi.boss.game.GameConstants;


public class XiaoFeiGiveJiFenActivity{

	int xiaofeiType = -1;	//消费类型：0：累计消费，1：一次性消费
	int platform = -1;
	String[] serverNames = null;
	String[] unServerName = null;
	String startTime = "";
	String endTime = "";
	long xiaofeinums = -1;
	double jifennums = -1;
	long stime;
	long etime;
	long givefeinums;
	
	public XiaoFeiGiveJiFenActivity(int xiaofeiType, int platform, String[] serverName, String[] unServerName, String startTime, String endTime, long xiaofeinums, double jifennums,long givefeinums) {
		this.xiaofeiType = xiaofeiType;
		this.platform = platform;
		this.serverNames = serverName;
		this.unServerName = unServerName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.xiaofeinums = xiaofeinums;
		this.jifennums = jifennums;
		this.givefeinums = givefeinums;
		stime = TimeTool.formatter.varChar19.parse(startTime);
		etime = TimeTool.formatter.varChar19.parse(endTime);
	}
	
	/**
	 * 奖励满足条件的玩家
	 */
	public void doPrice(Player p,long costs){
		if(xiaofeiType==0){
			long dnum = costs/xiaofeinums;
			long givechargepoint = 0;
			if(dnum>0){
				givechargepoint = (long)(dnum*givefeinums*jifennums);
			}
			if(givechargepoint>0){
				p.setChargePoints(p.getChargePoints()+givechargepoint);
				
				try{
					StatClientService statClientService = StatClientService.getInstance();
					GameChongZhiFlow gameChongZhiFlow = new GameChongZhiFlow();
					String channel4points = (p.getPassport() != null ? p.getPassport().getLastLoginChannel() : "无");

					gameChongZhiFlow.setAction(0); // 0 充值 ，1 消耗 2 老用户获得积分
					gameChongZhiFlow.setCurrencyType(CurrencyType.getCurrencyDesp(CurrencyType.JIFEN));
					Passport pp = p.getPassport();
					if (pp != null) {
						gameChongZhiFlow.setJixing(pp.getRegisterMobileOs());
					}
					gameChongZhiFlow.setFenQu(GameConstants.getInstance().getServerName());
					gameChongZhiFlow.setGame(CountryManager.得到国家名(p.getCountry()));
					gameChongZhiFlow.setGameLevel("" + p.getLevel());
					gameChongZhiFlow.setMoney(givechargepoint);
					gameChongZhiFlow.setQuDao(channel4points);
					String desc = "消费送积分";
					gameChongZhiFlow.setReasonType(desc);

					gameChongZhiFlow.setTime(System.currentTimeMillis());
					gameChongZhiFlow.setUserName(p.getUsername());
					if (!TestServerConfigManager.isTestServer()) {
						statClientService.sendGameChongZhiFlow("", gameChongZhiFlow);
						ActivitySubSystem.logger.warn("[发统计消耗积分] [成功] ["+p.getId()+"] [积分:"+( givechargepoint)+"] ["+p.getUsername()+"] ["+p.getName()+"]");
					}
				}catch(Exception e){
					ActivitySubSystem.logger.error("[发统计消耗积分出现异常] ["+p.getId()+"] [积分:"+( givechargepoint)+"] ["+p.getUsername()+"] ["+p.getName()+"]",e);
				}
			}
			NewChongZhiActivityManager.logger.warn("[消费积分活动] [costs:"+costs+"] [给玩家的积分："+givechargepoint+"] [消费配置:"+xiaofeinums+"--"+givefeinums+"--"+jifennums+"] ["+p.getLogString()+"]");
		}
	}
	
	public boolean isEffective(){
		long now = System.currentTimeMillis();
		if(now < stime || now > etime){
			return false;
		}
		
		if(xiaofeinums==0 || jifennums==0){
			NewChongZhiActivityManager.logger.warn("[消费积分活动配置错误] [result:{xiaofeinums==0 || jifennums==0}]");
			return false;
		}
		
		if (platform == 0) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.官方)) {
				return false;
			}
		}else if (platform == 1) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.台湾)) {
				return false;
			}
		}else if (platform == 2) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.腾讯)) {
				return false;
			}
		}else if (platform == 3) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.马来)) {
				return false;
			}
		}else if (platform == 4) {
			if (!PlatformManager.getInstance().isPlatformOf(Platform.韩国)) {
				return false;
			}
		}
		String serverName = GameConstants.getInstance().getServerName();
		//如果服务器名字在 unserver名字中，就不参加活动
		for (String s : unServerName) {
			if (s.equals(serverName)) {
				return false;
			}
		}
		//如果是allserver 的  或者是这个活动的就参加
		for (String s : serverNames) {
			if (s.equals("ALL_SERVER")) {
				return true;
			}else if (s.equals(serverName)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "[xiaofeiType=" + xiaofeiType + "] [platform=" + platform + "] [serverName=" +(serverNames==null?"":Arrays.toString(serverNames))+ "] [unServerName=" + (unServerName==null?"":Arrays.toString(unServerName)) + "] [startTime=" + startTime + "] [endTime=" + endTime + "] [xiaofeinums=" + xiaofeinums + "] [jifennums=" + jifennums + "]";
	}
	
}
