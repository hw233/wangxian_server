package com.fy.engineserver.activity.loginActivity;

import static com.fy.engineserver.datasource.language.Translate.STRING_1;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.Logger;


import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.LOGIN_ACTIVITY_MESS_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.xuanzhi.boss.game.GameConstants;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;

/**
 * 连续登录活动
 * 
 *
 */
public class ContinueLoginActivity extends Activity{
		
	private long startTime;
	
	private long endTime;
	
	public Platform[] platforms;
	
	/**
	 * 某些不开此活动的服务器，比如新服啦
	 */
	private Set<String> notOpenServers;
	
	private Set<String> openServers;
	
	private boolean isOpen = true;
	
	public String ddckey;
	
	public List<LoginActivityArticle> articles; 
	
	public DefaultDiskCache ddcKeys;
	
	public DefaultDiskCache ddc;
	
	public int 已领取 = 0;
	
	public int 领取 = 1;
	
	public int 不可领取 = 2;
	
	public ContinueLoginActivity(long startTime,long endTime,Platform[] platforms,Set<String> notOpenServers,String ddckey,List<LoginActivityArticle> articles,DefaultDiskCache ddc,DefaultDiskCache ddcKeys,Set<String> openServers){
		this.startTime = startTime;
		this.endTime = endTime;
		this.platforms = platforms;
		this.notOpenServers = notOpenServers;
		this.ddckey = ddckey;
		this.articles = articles;
		this.ddc = ddc;
		this.ddcKeys = ddcKeys;
		this.openServers = openServers;
	}
	
	/**
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameDay(long time1,long time2){
		Calendar ca=Calendar.getInstance();
		ca.setTimeInMillis(time1);
		int year1=ca.get(Calendar.YEAR);
		int month1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_MONTH);
//		int hour1 = ca.get(Calendar.HOUR_OF_DAY);///
//		int minute1 = ca.get(Calendar.MINUTE);///

		ca.setTimeInMillis(time2);
		int year2=ca.get(Calendar.YEAR);
		int month2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_MONTH);
//		int hour2 = ca.get(Calendar.HOUR_OF_DAY);///
//		int minute2 = ca.get(Calendar.MINUTE);///
//		return year1==year2&&month1==month2&&day1==day2&&hour1==hour2&&minute1==minute2;///
		return year1==year2&&month1==month2&&day1==day2;//&&hour1==hour2&&minute1==minute2;///
	}
	/**
	 * @param start
	 * @param end
	 * @return
	 */
    public static long getContinueLoginDays(long start,long end){
    	Calendar ca=Calendar.getInstance();
    	ca.setTimeInMillis(start);
    	ca.set(Calendar.HOUR_OF_DAY, 0);
    	ca.set(Calendar.MINUTE, 0);
    	ca.set(Calendar.SECOND, 0);
    	ca.set(Calendar.MILLISECOND, 0);
    	long st=ca.getTimeInMillis();
    	
    	ca.clear();
    	ca.setTimeInMillis(end);
    	ca.set(Calendar.HOUR_OF_DAY, 0);
    	ca.set(Calendar.MINUTE, 0);
    	ca.set(Calendar.SECOND, 0);
    	ca.set(Calendar.MILLISECOND, 0);
    	long et=ca.getTimeInMillis();
    	
    	long days=Math.abs((et-st)/(24*60*60*1000));///
//    	long days=Math.abs((et-st)/(1*60*1000));
    	return days;
    }
	
    
    /**
     * 更新玩家状态
     * @param player
     */
	 public void updateLoginData(Player player){
	    	long now = System.currentTimeMillis();
	    	Logger logger = ActivityManagers.logger;
	    	if(now >= endTime){
	    		//end activity
	    		return;
	    	}
	    	try{
	        	if(ddc.get(player.getId()+getName())!=null){
	        		LoginStatDate lsdd = (LoginStatDate)ddc.get(player.getId()+getName());
	        		long oldtime = lsdd.getLasttime();
	        		if(!isSameDay(oldtime, now)){
	        			long days=getContinueLoginDays(oldtime, now);
	        			if(days==1){
		        			LoginStatDate lsd = (LoginStatDate) ddc.get(player.getId()+getName());
		        			int old = lsd.getValue();
			        		lsd.setLasttime(System.currentTimeMillis());
			        		lsd.setValue(old+1);
			        		List<String> list = lsd.getHashGets();
			        		if(old>=7){
			        			list.set(6, 领取+"");
			        		}else{
			        			list.set(lsd.getValue()-1, 领取+"");
			        		}
			        		lsd.setHashGets(list);
			        		ddc.put(player.getId()+getName(),lsd);
		        			if (logger.isInfoEnabled()) {
		        				logger.info("[更新玩家连续登录] [成功] ["+player.getLogString()+"] [oldValue：" +old+ "] [newValue：" + lsd.getValue() + "] [statue:"+lsd.getHashGets().toString()+"]");
		        			}
		        		}else if(days>1){
		        			LoginStatDate lsd = (LoginStatDate) ddc.get(player.getId()+getName());
		        			int old = lsd.getValue();
		        			long ot = lsd.getLasttime();
			        		lsd.setLasttime(System.currentTimeMillis());
			        		lsd.setValue(1);
			        		List<String> list = new ArrayList<String>();
			        		for(int i=0;i<7;i++){
			        			if(i==0){
			        				list.add(领取+"");
			        			}else{
			        				list.add(不可领取+"");
			        			}
			        		}
			        		lsd.setHashGets(list);
			        		ddc.put(player.getId()+getName(),lsd);
		        			if (logger.isInfoEnabled()) {
		        				logger.info("[重置玩家隔天连续登录] [成功] ["+player.getLogString()+"] [oldValue：" +old+ "] [newValue：" + lsd.getValue() + "] [NOW："+new Date(now)+"] [最后更新："+new Date(ot)+"] [statue:"+lsd.getHashGets().toString()+"]");
		        			}
		        		}
	        			LOGIN_ACTIVITY_MESS_RES res = new LOGIN_ACTIVITY_MESS_RES(GameMessageFactory.nextSequnceNum(), Translate.translateString(Translate.连续登录, new String[][] { { STRING_1, lsdd.getValue()+"" } }),Translate.达到7日连登后,lsdd.getValue(), lsdd.getHashGets().toArray(new String[]{}), articles.toArray(new LoginActivityArticle[]{}));
			        	player.addMessageToRightBag(res);
	        		}
	        	}else{
	        		LoginStatDate lsd = new LoginStatDate();
	        		List<String> hashGets = new ArrayList<String>();
	        		for(int i=0;i<7;i++){
	        			hashGets.add(不可领取+"");
	        		}
	        		lsd.setLasttime(System.currentTimeMillis());
	        		lsd.setValue(1);
	        		hashGets.set(lsd.getValue()-1, 领取+"");
	        		lsd.setHashGets(hashGets);
	        		ddc.put(player.getId()+getName(),lsd);
	        		
		        	LOGIN_ACTIVITY_MESS_RES res = new LOGIN_ACTIVITY_MESS_RES(GameMessageFactory.nextSequnceNum(), Translate.translateString(Translate.连续登录, new String[][] { { STRING_1, lsd.getValue()+"" } }), Translate.达到7日连登后,lsd.getValue(), lsd.getHashGets().toArray(new String[]{}), articles.toArray(new LoginActivityArticle[]{}));
		        	player.addMessageToRightBag(res);
	    		
	        		if (logger.isInfoEnabled()) {
	    				logger.info("[连续登录活动] [添加统计数据] [成功] ["+player.getLogString()+"] [statue:"+lsd.getHashGets().toString()+"]");
	    			}
	        		
	        	}
	        	
	    	}catch (Exception e) {
	    		e.printStackTrace();
	    		ActivityManagers.logger.error("[更新玩家连续登录] [异常] ["+player.getLogString()+"] ["+toString()+"]",e);
			}
	    }
	 
	@Override
	public boolean isEffective() {
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

	/**
	 * 弹窗玩家领取奖励
	 * type：第几天
	 */
	@Override
	public void doPrizes(Player player,int index) {
		long now = System.currentTimeMillis();
		try{
			int indexlimit = 0;
			indexlimit = index;
			if(index>=6){
				indexlimit = 6;
			}
			if(index<0){
				indexlimit = 0;
			}
			LoginActivityArticle laa = articles.get(indexlimit);
			LoginStatDate lsd = (LoginStatDate) ddc.get(player.getId()+getName());
			if(lsd == null || laa.getArticleName() == null){
				ActivityManagers.logger.warn("[弹窗玩家领取奖励] [发生奖励] [错误] [LoginStatDate_is_null_or_article_is_null] ["+player.getLogString()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
			}
			List<String> list = lsd.getHashGets();
			String getsStr = list.get(indexlimit);
			if(getsStr.equals(领取+"") == false){
				ActivityManagers.logger.warn("[弹窗玩家领取奖励] [发生奖励] [错误] [is_not_"+领取+"] ["+player.getLogString()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
				return;
			}
			
			Article a = ArticleManager.getInstance().getArticle(laa.getArticleName());
			if (a != null) {
				ArticleEntity ae = null;
				if(laa.getArticleName().equals(Translate.银块)){
					ae = ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_huodong_libao, player, laa.getRewardColor(),laa.getRewardNum(), true);
				}else{
					ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, laa.getRewardColor(),laa.getRewardNum(), true);
				}
				if(ae!=null){
					boolean send = false;
					//Knapsackfullall
					if(player.getKnapsack_common().isFull()){
						player.sendError(Translate.背包已满emile);
						MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, new int[]{laa.getRewardNum()}, Translate.连登活动奖励, Translate.活动期间, 0, 0, 0, "连登活动");
						send = true;
					}else if(player.putToKnapsacks(ae, laa.getRewardNum(), "连续登录活动")){
						player.sendError(laa.getRewardMess());
						ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, 
								ArticleStatManager.YINZI, laa.getRewardNum(), "连登获得", null);
						send = true;
					}
					
					if(send){
		        		list.set(indexlimit, 已领取+"");
		        		lsd.setHashGets(list);
		        		ddc.put(player.getId()+getName(),lsd);
						ddcKeys.put("4月连续登录活动", player.getId()+"####"+(indexlimit+1)+"####"+laa.getArticleName());
						LOGIN_ACTIVITY_MESS_RES res = new LOGIN_ACTIVITY_MESS_RES(GameMessageFactory.nextSequnceNum(), Translate.translateString(Translate.连续登录, new String[][] { { STRING_1, lsd.getValue()+"" } }), Translate.达到7日连登后,lsd.getValue(), lsd.getHashGets().toArray(new String[]{}),articles.toArray(new LoginActivityArticle[]{}));
						player.addMessageToRightBag(res);
						ActivityManagers.logger.warn("[弹窗玩家领取奖励] [发生奖励] [成功] [index:"+index+"] [物品名:"+laa.getArticleName()+"] [数量："+laa.getRewardNum()+"] [颜色："+laa.getRewardColor()+"] ["+player.getLogString()+"]  [statue:"+lsd.getHashGets().toString()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
						/**额外的奖励**/
						List<PrizeMore> morereward = ActivityManagers.getInstance().getMorerewards();
						if(morereward!=null && morereward.size()>0){
							for(PrizeMore mr:morereward){
								if(mr.isEffective()){
									String articlenames[] = mr.getArticlenames();
									if(articlenames!=null && articlenames.length>0){
										ArticleEntity aes [] = new ArticleEntity[articlenames.length]; 
										for(int i=0;i<articlenames.length;i++){
											a = ArticleManager.getInstance().getArticle(articlenames[i]);	
											ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_huodong_libao, player, a.getColorType(),mr.getArticlenums()[i], true);
											aes[i] = ae;
										}
										MailManager.getInstance().sendMail(player.getId(), aes,Translate.恭喜您获得了奖励+":"+a.getName(), Translate.恭喜您获得了奖励+":"+a.getName()+"，"+Translate.请收藏好, 0, 0, 0, "61连登活动");
										player.sendError(Translate.恭喜您获得了奖励+":"+a.getName());
									}
								}
							}
						}
					}
				}
			}else{
				ActivityManagers.logger.warn("[弹窗玩家领取奖励] [发生奖励] [错误] [article_is_null] ["+player.getLogString()+"] [耗时："+(System.currentTimeMillis()-now)+"]");
			}

		}catch(Exception e){
			e.printStackTrace();
			ActivityManagers.logger.warn("[弹窗玩家领取奖励] [发生奖励] [异常] ["+player.getLogString()+"] [耗时："+(System.currentTimeMillis()-now)+"]",e);
		}
		
	}

	@Override
	public String getName() {
		return this.getClass().getSimpleName()+ddckey;
	}
	
	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "startTime:"+sdf.format(startTime)+"endTime:"+sdf.format(endTime);
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

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getDdckey() {
		return ddckey;
	}

	public void setDdckey(String ddckey) {
		this.ddckey = ddckey;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	public List<LoginActivityArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<LoginActivityArticle> articles) {
		this.articles = articles;
	}
	public Set<String> getOpenServers() {
		return openServers;
	}
	public void setOpenServers(Set<String> openServers) {
		this.openServers = openServers;
	}

}
