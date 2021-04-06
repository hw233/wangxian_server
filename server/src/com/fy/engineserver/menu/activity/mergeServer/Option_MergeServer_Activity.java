package com.fy.engineserver.menu.activity.mergeServer;

import java.util.Calendar;

import com.fy.engineserver.activity.loginActivity.ActivityManagers;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.menu.NeedCheckPurview;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.TimeTool;
import com.xuanzhi.boss.game.GameConstants;

/**
 * 合服活动
 * 
 *
 */
public class Option_MergeServer_Activity extends Option implements NeedCheckPurview{
	
	
	public byte getOType() {
		return Option.OPTION_TYPE_SERVER_FUNCTION;
	} 
	
	@Override
	public void doSelect(Game game, Player player) {
		String articelName = "";
		boolean dataRecord = false;
		
		Long lasttime = (Long)ActivityManagers.getInstance().getDdc().get(player.getId()+"合服活动奖励20130609");
		if(lasttime!=null){
			ActivityManagers.getInstance().getDdc().put(player.getId()+"合服活动奖励20130609", 521);
		}
		lasttime = (Long)ActivityManagers.getInstance().getDdc().get(player.getId()+"合服活动奖励20130609");
		if(lasttime!=null){
			if(!isSameDay(lasttime.longValue(),System.currentTimeMillis())){
				String servername = GameConstants.getInstance().getServerName();
				if("游云惊龙".equals(servername)||"金风玉露".equals(servername)){
					articelName = servername+"锦囊";
					if(!"".equals(articelName)){
						Article a = ArticleManager.getInstance().getArticle(articelName);
						if(a==null){
							player.sendError(articelName+"不存在，请联系GM去解决！");
							return;
						}
						
						ArticleEntity ae = null;
						try {
							ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_ACHIEVEMENT, player, a.getColorType(), 1, true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							ActivityManagers.logger.warn("[合服活动奖励] [异常] ["+player.getLogString()+"] ["+e+"]");
						}
						
						if(ae!=null){
							if(player.getKnapsack_common().isFull()){
								try {
									MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[]{ae}, "每日合服登陆领取礼包", "71级包括以上玩家即可参与合服登陆领取礼,帮您经验补偿!", 0, 0, 0, "合服活动奖励");
									dataRecord = true;
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									ActivityManagers.logger.warn("[合服活动奖励] [异常] ["+player.getLogString()+"] ["+e+"]");
								}
							}else{
								player.putToKnapsacks(ae, "合服活动奖励");
								dataRecord = true;
							}
						}
						
						if(dataRecord){
							ActivityManagers.getInstance().getDdc().put(player.getId()+"合服活动奖励20130609", System.currentTimeMillis());
						}
					}
				}  
			}else{
				player.sendError("您今天已经领取过了！");
			}
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

	@Override
	public boolean canSee(Player player) {
		// TODO Auto-generated method stub
		String servername = GameConstants.getInstance().getServerName();
		long now = System.currentTimeMillis();
		if("游云惊龙".equals(servername)||"金风玉露".equals(servername)){
			if(TimeTool.formatter.varChar19.parse("2013-06-13 00:00:00") < now && now < TimeTool.formatter.varChar19.parse("2013-06-19 23:59:59")){
				return true;	
			}
		}
		return false;
	}

}
