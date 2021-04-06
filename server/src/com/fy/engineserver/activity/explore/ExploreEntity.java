package com.fy.engineserver.activity.explore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.activity.activeness.ActivenessManager;
import com.fy.engineserver.activity.activeness.ActivenessType;
import com.fy.engineserver.articleExchange.ArticleExchangeManager;
import com.fy.engineserver.core.g2d.Point;
import com.fy.engineserver.datasource.article.data.articles.ExchangeArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.ExchangeArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.event.EventRouter;
import com.fy.engineserver.event.cate.EventWithObjParam;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.NOTICE_CLIENT_DELIVER_TASK_REQ;
import com.fy.engineserver.message.USE_EXPLOREPROPS_RES;
import com.fy.engineserver.newtask.TaskEntity;
import com.fy.engineserver.playerAims.manager.PlayerAimManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.util.Utils;

import static com.fy.engineserver.datasource.language.Translate.*;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class ExploreEntity {

	
	
	public ExploreEntity(){
		
	}
	
	private static int secondDistance = 500;
	private static int thirdDistance = 200;
	private static Random random = new Random();
	
	private String mapName;
	private Point point;
	private String mapSegmentName;
	private int range;
	private byte country;
	private String showMap;
	//国家
	private long propsId;
	
	private long resourceMapId;
	
	private long taskId;
	
	public boolean execute(Player player){
		
		if(player.getCurrentGame() == null ){
			return false;
		}
		String currentMap = player.getCurrentGame().getGameInfo().getMapName();
		if(player.getCurrentGame().country != this.country){
			player.sendError(不能使用不在指定国家);
			return false;
		}
		if(ArticleExchangeManager.logger.isDebugEnabled()){
			ArticleExchangeManager.logger.debug("[使用寻宝道具] ["+currentMap+"] []");
		}
		if(currentMap.equals(mapName)){
			int distance = Utils.getDistanceA2B(point.x, point.y, player.getX(), player.getY());
			if(distance > secondDistance){
				USE_EXPLOREPROPS_RES res = new USE_EXPLOREPROPS_RES(GameMessageFactory.nextSequnceNum(), false,ExplorePosition.很远.position);
				player.addMessageToRightBag(res);
				if(ArticleExchangeManager.logger.isDebugEnabled()){
					ArticleExchangeManager.logger.debug("[使用寻宝道具] [很远] ["+player.getLogString()+"]");
				}
			}else if(distance > thirdDistance){
				USE_EXPLOREPROPS_RES res = new USE_EXPLOREPROPS_RES(GameMessageFactory.nextSequnceNum(),false, ExplorePosition.附近.position);
				player.addMessageToRightBag(res);
				if(ArticleExchangeManager.logger.isDebugEnabled()){
					ArticleExchangeManager.logger.debug("[使用寻宝道具] [附近] ["+player.getLogString()+"]");
				}
			}else if(distance > range){

				byte position = 0;
				// 八个方向
				int px = point.getX();
				int py = point.getY();
				int x = player.getX();
				int y = player.getY();
				if(px > x){
					//目标在右边
					if(py > y){
						//目标在上边
						position = ExplorePosition.右下方.position;
					}else if(py < y){
						position = ExplorePosition.右上方.position;
					}else{
						position = ExplorePosition.右边.position;
					}
				}else if(px< x){
					//目标在左边
					if(py > y){
						//目标在上边
						position = ExplorePosition.左下方.position;
					}else if(py < y){
						position = ExplorePosition.左上方.position;
					}else{
						position = ExplorePosition.左边.position;
					}
				}else if(px == x){
					if(py > y){
						//目标在上边
						position = ExplorePosition.正下方.position;
					}else if(py < y){
						position = ExplorePosition.正上方.position;
					}
				}
				USE_EXPLOREPROPS_RES res = new USE_EXPLOREPROPS_RES(GameMessageFactory.nextSequnceNum(), false,position);
				player.addMessageToRightBag(res);
				if(ArticleExchangeManager.logger.isDebugEnabled()){
					ArticleExchangeManager.logger.debug("[使用寻宝道具] ["+position+"] ["+player.getLogString()+"]");
				}
			}else {
				if(this.exploreObtain(player,taskId)){
					
					TaskEntity te = player.getTaskEntity(taskId);
					if(te != null){
						if(te.getStatus() ==TaskEntity.TASK_STATUS_GET){
							te.setStatus(TaskEntity.TASK_STATUS_COMPLETE);
							NOTICE_CLIENT_DELIVER_TASK_REQ res = new NOTICE_CLIENT_DELIVER_TASK_REQ(GameMessageFactory.nextSequnceNum(), taskId);
							player.addMessageToRightBag(res);
							AchievementManager.getInstance().record(player, RecordAction.寻宝次数);
							// 活跃度统计
							ActivenessManager.getInstance().record(player, ActivenessType.寻宝);
							if(ExploreManager.logger.isWarnEnabled()){
								ExploreManager.logger.warn("[寻宝成功] [统计成就] ["+player.getLogString()+"]");
							}
							try {
								RecordAction rac = null;
								if(te.getTaskName().contains(国内寻宝)) {
									rac = RecordAction.国内寻宝次数;
								} else {
									rac = RecordAction.国外寻宝次数;
								}
								if(rac != null) {
									EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.RECORD_PLAYER_OPT, new Object[] { player.getId(), rac, 1L});
									EventRouter.getInst().addEvent(evt);
								}
							} catch (Exception e) {
								PlayerAimManager.logger.error("[目标系统] [统计玩家国内外寻宝次数异常] [" + player.getLogString() + "]", e);
							}
						}else{
							ExploreManager.logger.error("[寻宝完成错误] ["+player.getLogString()+"] [任务id:"+taskId+"] ["+te.getStatus()+"]");
						}
					}else{
						ExploreManager.logger.error("[寻宝完成没有任务] ["+player.getLogString()+"] [任务id:"+taskId+"]");
					}
					player.setExploreEntity(null);
					player.send_HINT_REQ(寻宝成功);
					
					USE_EXPLOREPROPS_RES res = new USE_EXPLOREPROPS_RES(GameMessageFactory.nextSequnceNum(),true, ExplorePosition.附近.position);
					player.addMessageToRightBag(res);
					
					if(ExploreManager.logger.isWarnEnabled()){
						ExploreManager.logger.warn("[寻宝成功] ["+player.getLogString()+"]");
					}
					return true;
				}
			}
			return false;
		}else{
			player.sendError(不在指定地图上);
			return false;
		}
	}
	
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public String getMapSegmentName() {
		return mapSegmentName;
	}
	public void setMapSegmentName(String mapSegmentName) {
		this.mapSegmentName = mapSegmentName;
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public long getPropsId() {
		return propsId;
	}
	public void setPropsId(long propsId) {
		this.propsId = propsId;
	}
	
	private boolean exploreObtain(Player player,long taskId){
		
		ArticleEntity ae1 = ArticleEntityManager.getInstance().getEntity(propsId);
		ArticleEntity ae2 = ArticleEntityManager.getInstance().getEntity(this.resourceMapId);
		
		if(player.removeFromKnapsacks(ae1,"寻宝", true) != null && player.removeFromKnapsacks(ae2,"寻宝", true) != null){
			
			ArticleStatManager.addToArticleStat(player, null, ae1, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "寻宝完成删除", null);
			ArticleStatManager.addToArticleStat(player, null, ae2, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "寻宝完成删除", null);
			
			List<ExchangeArticle> list1 = ArticleManager.getInstance().exchangeArticleList;
			List<ExchangeArticle> list = new ArrayList<ExchangeArticle>();
			if(list1 != null && list1.size()>0){
				for(ExchangeArticle ee : list1){
					if(this.getCountry() == player.getCountry()){
						if(ee.isUse()){
							if(ee.getExchangearticleType() == 0 || ee.getExchangearticleType()  == 1){
								list.add(ee);
							}
						}
					}else{
						if(ee.isUse()){
							if(ee.getExchangearticleType() == 2 || ee.getExchangearticleType()  == 3){
								list.add(ee);
							}
						}
					}
				}
				int i = random.nextInt(list.size());
				
				if(ExploreManager.logger.isWarnEnabled()){
					ExploreManager.logger.warn("[到达目的地使用寻宝道具得到物品] ["+player.getLogString()+"] [种类:"+list.size()+"] [随机数:"+i+"]");
				}
				ExchangeArticle a = list.get(i);
				try{
					ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, true, ArticleEntityManager.CREATE_REASON_explore, player, 1, 1, true);
					if(!(ae instanceof ExchangeArticleEntity)){
						if(ExploreManager.logger.isWarnEnabled())
							ExploreManager.logger.warn("[到达目的地使用寻宝道具得到物品] [不是ExchangeArticleEntity] ["+ae.getClass()+"] ["+player.getLogString()+"] ");
						return false;
					}else{
						ExchangeArticleEntity eae = (ExchangeArticleEntity)ae;
						eae.setTaskId(taskId);
						ArticleEntityManager.getInstance().em.notifyFieldChange(eae, "taskId");
					}
					boolean bln = player.putToKnapsacks(ae,"寻宝");
					if(bln){
//						player.send_HINT_REQ("获得"+a.getName());
						player.send_HINT_REQ(translateString(获得xx, new String[][]{{STRING_1,a.getName()}}));
						if(ExploreManager.logger.isWarnEnabled())
							ExploreManager.logger.warn("[到达目的地使用寻宝道具success] [得到物品] ["+player.getLogString()+"] ");
							ArticleStatManager.addToArticleStat(player, null, ae, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte)0, 1, "寻宝获得", null);
						return true;
					}else{
						if(ExploreManager.logger.isWarnEnabled())
							ExploreManager.logger.warn("[到达目的地使用寻宝道具success] [没有得到物品] ["+player.getLogString()+"] ");
					}
				}catch(Exception ex){
					ExploreManager.logger.warn("[到达目的地使用寻宝道具异常] [没有得到物品] ["+player.getLogString()+"]",ex);
				}
			}else{
				if(ExploreManager.logger.isWarnEnabled())
					ExploreManager.logger.warn("[到达目的地使用寻宝道具] [可交换物品配置错误] ["+player.getLogString()+"] ");
			}
		}else{
			if(ExploreManager.logger.isWarnEnabled())
				ExploreManager.logger.warn("[到达目的地使用寻宝道具] [删除原有物品错误] ["+player.getLogString()+"] ");
		}
		return false;
		
	}

	public byte getCountry() {
		return country;
	}

	public void setCountry(byte country) {
		this.country = country;
	}

	public long getResourceMapId() {
		return resourceMapId;
	}

	public void setResourceMapId(long resourceMapId) {
		this.resourceMapId = resourceMapId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public String getShowMap() {
		return showMap;
	}

	public void setShowMap(String showMap) {
		this.showMap = showMap;
	}
	
	
}
