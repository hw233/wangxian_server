package com.fy.engineserver.sprite.horse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.HorseFoodArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.props.Cell;
import com.fy.engineserver.datasource.article.data.props.Knapsack;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.FEED_HORSE_REQ;
import com.fy.engineserver.message.FEED_HORSE_RES;
import com.fy.engineserver.message.HORSE_PUTONOROFF_REQ;
import com.fy.engineserver.message.HORSE_PUTONOROFF_RES;
import com.fy.engineserver.message.QUERY_FEED_HORSE_RES;
import com.fy.engineserver.message.QUERY_HORSE_EQUIPMENTS_REQ;
import com.fy.engineserver.message.QUERY_HORSE_EQUIPMENTS_RES;
import com.fy.engineserver.message.QUERY_HORSE_LIST_RES;
import com.fy.engineserver.message.QUERY_PLAYER_HORSE_RES;
import com.fy.engineserver.message.SET_DEFAULT_HORSE_REQ;
import com.fy.engineserver.message.SET_DEFAULT_HORSE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class HorseSubSystem extends SubSystemAdapter  {
	
	
//	public static Logger logger = Logger.getLogger(HorseSubSystem.class);
	public static Logger logger = HorseManager.logger;
	
	public String[] getInterestedMessageTypes() {
//		 "HORSE_RIDE_REQ", 
		return new String[] {"FEED_HORSE_REQ","HORSE_PUTONOROFF_REQ" ,"QUERY_HORSE_LIST",
				"QUERY_HORSE_EQUIPMENTS_REQ","QUERY_FEED_HORSE_REQ","SET_DEFAULT_HORSE_REQ","QUERY_PLAYER_HORSE_REQ"};
	}
	
	
	private HorseManager horseManager;
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "HorseSubSystem";
	}

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * TODO 如果此方法已经处理了请求消息，但是不需要返回响应，那么最后也会return null<br>
	 * 这样一来，调用者就无法区分出消息是否已经被处理了
	 */
	public ResponseMessage handleReqeustMessage(Connection conn, RequestMessage message) throws ConnectionException, Exception {
		Player player = check(conn, message, logger);
		if(useMethodCache){
			return super.handleReqeustMessage(conn, message, player);
		}
		try {
			Class clazz = this.getClass();
			Method m1 = clazz.getDeclaredMethod("handle_" + message.getTypeDescription(), Connection.class, RequestMessage.class, Player.class);
			ResponseMessage res = null;
			m1.setAccessible(true);
			res = (ResponseMessage) m1.invoke(this, conn, message, player);
			return res;
		} catch (InvocationTargetException e) {
			// TODO: handle exception
			e.getTargetException().printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 设置默认坐骑请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SET_DEFAULT_HORSE_REQ(Connection conn,RequestMessage message, Player player) {
		SET_DEFAULT_HORSE_REQ req = (SET_DEFAULT_HORSE_REQ)message;
		
		long horseId = req.getDefaultHorseId();
		boolean result = player.setDefaultHorse(horseId);
		SET_DEFAULT_HORSE_RES res = null;
		if(result){
			if (logger.isWarnEnabled()){
				logger.warn("[设置默认坐骑成功] ["+player.getLogString()+"] ["+horseId+"]");
			}
			res = new SET_DEFAULT_HORSE_RES(message.getSequnceNum(), result, horseId);
		}else{
			logger.error("[设置默认坐骑错误] [没有指定坐骑] ["+player.getLogString()+"] ["+horseId+"]");
			return null;
		}
		return res;
	}
	
	/**
	 * 玩家登陆查询坐骑
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_PLAYER_HORSE_REQ(Connection conn,RequestMessage message, Player player) {
		
		QUERY_PLAYER_HORSE_RES res = null;
		res = new QUERY_PLAYER_HORSE_RES(message.getSequnceNum(), player.getRidingHorseId(), player.getRideHorseId());
		return res;
	}
	
	
	/**
	 * 处理给坐骑穿,脱装备请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_HORSE_PUTONOROFF_REQ(Connection conn,RequestMessage message, Player player) {
		
		HORSE_PUTONOROFF_REQ req = (HORSE_PUTONOROFF_REQ)message;
		long horseId = req.getHorseId();
		long equipmentId = req.getEquipmentId();
		boolean onOroff = req.getOnOroff();
		
		if(!player.getHorseIdList().contains(horseId)){
			logger.error("[玩家给坐骑穿脱装备错误] [失败：用户没有这个坐骑id] [参数:"+horseId+"] ["+player.getLogString()+"]");
			return null;
		}
		
		Horse horse = HorseManager.getInstance().getHorseById(horseId,player);
		if(horse == null){
			logger.error("[玩家给坐骑穿脱装备错误] [horse null] [参数:"+horseId+"] ["+player.getLogString()+"]");
			return null;
		}
		if(onOroff){
			// 穿
			ArticleEntity entity = player.getArticleEntity(equipmentId);
			if(entity != null){
				try {
					horse.putOn((EquipmentEntity)entity,false);
				} catch (Exception e) {
					HorseSubSystem.logger.error("[坐骑:穿装备] [失败：穿装备过程异常] [参数:"+equipmentId+"] ["+player.getLogString()+"]", e);
					return null;
				}
			}else{
				HorseSubSystem.logger.error("[坐骑:穿装备] [失败：请求装备不存在] [equipment:"+equipmentId+"] ["+player.getLogString()+"]");
				return null;
			}
			
		}else{
			//脱
			int i = 0;
			try {
				for(long id :horse.getEquipmentIds()){
					if(id == equipmentId){
						ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(id);
						if(entity != null){
							horse.takeOff(i);
							break;
						}else{
							logger.error("[坐骑脱装备] [失败：请求参数不正确] [equipmentId:"+equipmentId+"] ["+player.getLogString()+"]");
							return null;
						}
					}
					i++;
				}
			} catch (Exception e) {
				HorseSubSystem.logger.error("[坐骑:脱装备异常] [参数:"+equipmentId+"] ["+player.getLogString()+"]");
				return null;
			}
		}
		HORSE_PUTONOROFF_RES res = new HORSE_PUTONOROFF_RES(req.getSequnceNum(),horse);
		return res;
	}

	
	/**
	 * 处理喂养坐骑请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_FEED_HORSE_REQ(Connection conn,RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		HorseSubSystem.logger.error("[坐骑喂养坐骑开始] ["+player.getLogString()+"]");
		
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		FEED_HORSE_REQ req = (FEED_HORSE_REQ)message;
		long id = req.getEntityId();
		long horseId = req.getHorseId();
		ArticleEntity ae = aem.getEntity(id);
		if(ae == null){
			HorseSubSystem.logger.error("[坐骑喂养坐骑] [失败：没有提供的物品] ["+player.getLogString()+"] [物品id:"+id+"]");
			return null;
		}
		Article a = am.getArticle(ae.getArticleName());
		if(a == null || !(a instanceof HorseFoodArticle)){
			HorseSubSystem.logger.error("[坐骑喂养坐骑] [不是坐骑喂养物品] ["+player.getLogString()+"] [name:"+(a != null ? a.getName():"")+"]");
			return null;
		}
		List<Long> list = player.getHorseIdList();
		if(!list.contains(horseId)){
			HorseSubSystem.logger.error("[坐骑喂养坐骑] [失败：没有对应的坐骑]  ["+player.getLogString()+"] [index:"+horseId+"]");
			return null;
		}
		Horse horse = HorseManager.getInstance().getHorseById(horseId,player);
		if(horse == null){
			HorseSubSystem.logger.error("[坐骑喂养坐骑] [horse null]  ["+player.getLogString()+"] [index:"+horseId+"]");
			return null;
		}
		
		if(horse.isFly()){
			player.sendError(Translate.飞行坐骑不能喂养);
			return null;
		}
		if(!horse.isFight()){
			player.sendError(Translate.非战斗坐骑不能喂养);
			return null;
		}
		
		int energyIndex = player.feedHorse(id, horse);
		
		if(energyIndex == -1){
			HorseSubSystem.logger.error("[坐骑喂养坐骑成功] ["+player.getLogString()+"] [index:"+horseId+"] ["+energyIndex+"]");
			FEED_HORSE_RES res = new FEED_HORSE_RES(req.getSequnceNum(),horseId,horse.getEnergy(),energyIndex);
			player.sendError(Translate.喂养坐骑成功);
			return res;
//			player.sendError(Translate.text_喂马一天只能喂养两次);
//			HorseSubSystem.logger.error("[坐骑喂养坐骑] [失败：次数受限]  ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [index:"+horseId+"]", start);
//			return null;
		}else if (energyIndex == -2) {
			player.sendError(Translate.text_马体力值满);
			HorseSubSystem.logger.error("[坐骑喂养坐骑] [失败：体力值满]  ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] [index:"+horseId+"]", start);
			return null;
		}else{
			HorseSubSystem.logger.error("[坐骑喂养坐骑成功] ["+player.getLogString()+"] [index:"+horseId+"] ["+energyIndex+"]");
			FEED_HORSE_RES res = new FEED_HORSE_RES(req.getSequnceNum(),horseId,horse.getEnergy(),energyIndex);
			player.sendError(Translate.喂养坐骑成功);
			return res;
		}
	}
	
	
	/**
	 * 处理查询坐骑请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_HORSE_LIST(Connection conn,RequestMessage message, Player player) {
		
		List<Horse> list = new ArrayList<Horse>();
		QUERY_HORSE_LIST_RES res = null;
		try {
			List<Long> ids = player.getHorseIdList();
		
			for(long id : ids){
				Horse horse = this.horseManager.getHorseById(id,player);
				if(horse != null){
					horse.setHorseLevel(player.getSoulLevel());
					list.add(horse);
				}else{
					logger.error("[查询坐骑失败] [坐骑为null] [坐骑id:"+id+"] ["+player.getLogString()+"]");
				}
			}
//			if(list.size()<1) return null;
			res = new QUERY_HORSE_LIST_RES(message.getSequnceNum(),list.toArray(new Horse[0]));
			if(logger.isWarnEnabled())
				logger.warn("[坐骑] [查询坐骑成功] ["+player.getLogString()+"]");
//			logger.info("[坐骑] [查询坐骑成功] [{}] [{}] [{}] [{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),start});
		
		} catch (Exception e) {
			HorseManager.logger.error("[查询坐骑] ["+player.getLogString()+"]",e);
			return null;
		}
		return res;
	}
	
	/**
	 * 处理查询坐骑请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_HORSE_EQUIPMENTS_REQ(Connection conn,RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		QUERY_HORSE_EQUIPMENTS_REQ req = (QUERY_HORSE_EQUIPMENTS_REQ)message;
		
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		long horseId = req.getHorseId();
		
		List<Long> list = new ArrayList<Long>();
		Horse h = horseManager.getHorseById(horseId,player);
		if(h == null){
//			logger.error("[查询坐骑装备失败] [没有这个坐骑装备] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+horseId+"]", start);
			logger.error("[查询坐骑装备失败] [没有这个坐骑装备] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),horseId,start});
			return null;
		}
		if(!player.getHorseIdList().contains(horseId)){
//			logger.error("[查询坐骑装备失败] [玩家没有这个坐骑] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ["+horseId+"]", start);
			logger.error("[查询坐骑装备失败] [玩家没有这个坐骑] [{}] [{}] [{}] [{}] [{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),horseId,start});
			return null;
		}
		
		Knapsack k = player.getKnapsack_common();
		if(k != null){
			int num = k.getCells().length;
			for(int i = 0;i<num ;i++){
				Cell cell = k.getCell(i);
				if(cell != null){
					long id = cell.getEntityId();
					ArticleEntity ae = aem.getEntity(id);
					if(ae != null){
						Article a = am.getArticle(ae.getArticleName());
						if( a!= null && a instanceof Equipment ){
							Equipment e = (Equipment)a;
							if(e.canUse(h) == null){
								list.add(id);
							}
						}
					}
				}
				
				
			}
		}
		k = player.getKnapsack_fangbao();
		if(k != null){
			int num = k.getCells().length;
			for(int i = 0;i<num ;i++){
				Cell cell = k.getCell(i);
				if(cell != null){
					long id = cell.getEntityId();
					ArticleEntity ae = aem.getEntity(id);
					if(ae != null){
						Article a = am.getArticle(ae.getArticleName());
						if( a!= null && a instanceof Equipment ){
							Equipment e = (Equipment)a;
							if(e.canUse(h) == null){
								list.add(id);
							}
						}
					}
				}
			}
		}
		int i =list.size();
		long[] ids = new long[i];
		for(int j =0 ;j< i ;j++){
			ids[j] = list.get(j);
		}
		
		QUERY_HORSE_EQUIPMENTS_RES res = new QUERY_HORSE_EQUIPMENTS_RES(message.getSequnceNum(),ids);
		if(logger.isWarnEnabled())
			logger.warn("[查询坐骑装备成功] ["+player.getLogString()+"] ["+ids.length+"]");
		return res;
	}
	
	/**
	 * 处理查询坐骑食物请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_FEED_HORSE_REQ(Connection conn,RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		
		Knapsack k = player.getKnapsack_common();
		List<String> list = new ArrayList<String>(0);
		if(k != null){
			int num = k.getCells().length;
			for(int i = 0;i<num ;i++){
				Cell cell = k.getCell(i);
				if(cell != null){
					long id = cell.getEntityId();
					ArticleEntity ae = aem.getEntity(id);
					if(ae != null){
						Article a = am.getArticle(ae.getArticleName());
						if( a!= null && a instanceof HorseFoodArticle){
							list.add(a.getName());
						}
					}
				}
			}
		}
		
		QUERY_FEED_HORSE_RES res = new QUERY_FEED_HORSE_RES(message.getSequnceNum(),list.toArray(new String[0]));
//		logger.info("[坐骑] [查询坐骑成功] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"]", start);
		if(logger.isInfoEnabled())
			logger.info("[坐骑] [查询坐骑成功] [{}] [{}] [{}] [{}]", new Object[]{player.getId(),player.getName(),player.getUsername(),start});
		return res;
	}

	public HorseManager getHorseManager() {
		return horseManager;
	}

	public void setHorseManager(HorseManager horseManager) {
		this.horseManager = horseManager;
	}
	
}
