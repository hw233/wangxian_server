package com.fy.engineserver.society;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;

public class SocialManager {

	public	static Logger logger = LoggerFactory.getLogger(SocialManager.class);
	
	public static String 在线一重礼  = Translate.在线一重礼;
	public static String 在线二重礼  = Translate.在线二重礼;
	public static String 在线三重礼  = Translate.在线三重礼;
	public static String 在线四重礼  = Translate.在线四重礼;
	public static String qq在线活动标题  = Translate.qq在线活动标题;
	
	public static String 回归有礼礼包  = Translate.回归有礼礼包;
	
	
	public static SimpleEntityManager<Relation> em;
	
	//建立聊天分组的等级限制
	public static int GROUP_MAX_LEVEL = 80;
	//聊天分组的人数限制
	public static int GROUP_MAX_NUM = 60;
	
	// 最多可以添加的好友数
	public static final int MAX_FRIEND_NUM = 40;
	
	// 最多可以添加的黑名单数
	public static final int MAX_BLACK_NUM = 40;
	//最多40个敌人
	public static final int MAX_CHOUREN_NUM = 40;
	
	public static final long QUERY_INTERVEL_TIME = 60 * 1000;
	
	public static int FRIENDNUMMAX = 100;
	
	private PlayerManager playerManager;
	
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public LruMapCache cache = new LruMapCache(32*1024*1024, 60*60*1000);
	
	public static SocialManager self;
	
	public static SocialManager getInstance() {
		return self;
	}
	
	public void init() {
		
		
		{
//			临时要删除
			if (false) {
			Article a = ArticleManager.getInstance().getArticle(在线一重礼);
			if(a == null){
				throw new RuntimeException("在线一重礼没有");
			}
			a = ArticleManager.getInstance().getArticle(在线二重礼);
			if(a == null){
				throw new RuntimeException("在线二重礼没有");
			}
			a = ArticleManager.getInstance().getArticle(在线三重礼);
			if(a == null){
				throw new RuntimeException("在线三重礼没有");
			}
			a = ArticleManager.getInstance().getArticle(在线四重礼);
			if(a == null){
				throw new RuntimeException("在线四重礼没有");
			}
			a = ArticleManager.getInstance().getArticle(回归有礼礼包);
			if(a == null){
				throw new RuntimeException("回归有礼礼包没有:" + 回归有礼礼包);
			}
			}
		}
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		self = this;
		em = SimpleEntityManagerFactory.getSimpleEntityManager(Relation.class);
		if(SocialManager.logger.isInfoEnabled())
			SocialManager.logger.info("[初始化社会关系manager] [成功] ["+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()-start)+"ms]");
		
		ServiceStartRecord.startLog(this);
	}
	
	
	/**
	 * 虚拟机关闭时调用
	 */
	public void destroy() {
		
		em.destroy();
	}
	
	public boolean open = true;
	
	/**
	 * 得到玩家的好友关系
	 * @param id
	 * @return
	 */
	public synchronized Relation getRelationById(long id){
		
		Relation relation = (Relation)cache.get(id);
		if(relation == null) {
			try {
				relation = em.find(id);
			} catch (Exception e) {
				SocialManager.logger.error("[取得玩家的关系异常]",e);
			}
			if(relation != null) {
				cache.put(relation.getId(), relation);
			}else{
				relation = new Relation(id);
				em.notifyNewObject(relation);
				cache.put(id, relation);
			}
		}
		return relation;
	}
	
	
	
	/**
	 * 获得玩家的好友列表
	 * @return
	 */
	public List<Long> getFriendlist(Player player) {
		
		return this.getRelationById(player.getId()).getFriendlist();
	}
	
	/**
	 * 是否是好友
	 * @param playerid
	 * @return
	 */
	public boolean isFriend(Player player, long playerid) {
		return getRelationById(player.getId()).checkFriend(playerid);
	}
	
	/**
	 * 是否符合增加好友的条件
	 * @param player
	 * @return
	 */
	public boolean canAddFriend(Player player) {
		List<Long> flist = this.getFriendlist(player);
		return flist.size() < MAX_FRIEND_NUM;
	}
	
	
	/**
	 * 增加好友
	 * @param player
	 * @param playerid 好友id
	 * @return
	 */
	public String addFriend(Player player, Player playerA) {
		
		// 如果黑名单里有 删除黑名  如果临时好友里有 删除临时好友  不同国家人不能添加好友
		
		String result = "";
		try{
		if(player.getCountry() != playerA.getCountry()){
			return Translate.text_不是一个国家不能添加好友;
		}
		
		if(player.getId() == playerA.getId()){
			result = Translate.text_不能操作自己;
		}else {
			Relation r = this.getRelationById(player.getId());
			synchronized(player) {
				if(r.checkFriend(playerA.getId())){
					result = Translate.text_已经是好友;
				}else{
					
					//好友有上限
					if(r.getFriendlist().size() >= FRIENDNUMMAX){
						return Translate.添加好友失败;
					}
					if(r.getBlacklist().contains(playerA.getId())){
						r.removeBlack(playerA.getId());
//						this.removeBlackuser(player, playerA.getId());
//						r.getBlacklist().remove(playerA.getId());
						if(logger.isWarnEnabled()){
							logger.warn("[添加好友] [删除黑名单成功] ["+player.getLogString()+"] ["+playerA.getLogString()+"]");
						}
					}
					
					if(r.getTemporarylist().contains(playerA.getId())){
						r.removeTemporary(playerA.getId());
//						r.getTemporarylist().remove(playerA.getId());
						if(logger.isWarnEnabled()){
							logger.warn("[添加好友] [删除临时好友成功] ["+player.getLogString()+"] ["+playerA.getLogString()+"]");
						}
					}
					
					r.addPlayer(playerA.getId());
					
					//添加上下线通知的人
					Relation relationA = this.getRelationById(playerA.getId());
					List<Long> nociteList = relationA.getNoticePlayerId();
					if(nociteList != null && nociteList.indexOf(player.getId()) < 0){
						nociteList.add(player.getId());
						if(nociteList.size() >= 100){
							long id = nociteList.remove(0);
							logger.warn("[添加好友] [添加对方的上下线关注人成功] [大于100删除第一个] ["+player.getLogString()+"] ["+playerA.getLogString()+"] ["+id+"]");
						}
						relationA.setNoticePlayerId(nociteList);
					}
					if(logger.isWarnEnabled()){
						logger.warn("[添加好友] [添加对方的上下线关注人成功] ["+player.getLogString()+"] ["+playerA.getLogString()+"] []");
						logger.warn("[增加好友成功] ["+player.getLogString()+"]");
					}
				}
			}
		}
		}catch(Exception e){
			logger.error("[添加好友异常] ["+player.getLogString()+"] [对方:"+(playerA != null ?playerA.getLogString():"null")+"]",e);
		}
		return result;
	}

	
	/**
	 * 得到一个好友
	 * @param player
	 * @param friendid
	 * @return
	 */
	public Player getFriend(Player player, long friendid) {
		
		Relation r = this.getRelationById(player.getId());
		Player p = null;
		if(r.checkFriend(friendid)){
			try {
				p = PlayerManager.getInstance().getPlayer(friendid);
			} catch (Exception e) {
				SocialManager.logger.error("[取得一个好友异常] ["+player.getLogString()+"]",e);
			}
		}
		return p;
	}
	
	/**
	 * 移除一个好友
	 * @param playerid
	 */
	public boolean removeFriend(Player player, long playerid) {
		
		if(player.getId() == playerid){
			player.send_HINT_REQ(Translate.text_不能操作自己);
			return false;
		}
		
		Relation r = this.getRelationById(player.getId());
		
		if(r.checkFriend(playerid)){
			r.removePlayer(playerid);
			if(logger.isWarnEnabled()){
				logger.warn("[删除好友成功] ["+player.getLogString()+"] ["+playerid+"] []");
			}
			
			//删除上下线通知的人
			Relation relationA = this.getRelationById(playerid);
			List<Long> noticePlayerId = relationA.getNoticePlayerId();
			if(noticePlayerId.contains(playerid)){
				noticePlayerId.remove(playerid);
			}
			if(logger.isWarnEnabled()){
				logger.warn("[删除好友] [删除对方的上下线关注人成功] ["+player.getLogString()+"] ["+playerid+"]");
			}
			
			player.send_HINT_REQ(Translate.text_删除好友成功);
			return true;
		} else if (r.checkTempFriend(playerid)) {
			r.removeTemporary(playerid);
			if(logger.isWarnEnabled()){
				logger.warn("[删除临时好友成功] ["+player.getLogString()+"] ["+playerid+"] []");
			}
			player.send_HINT_REQ(Translate.text_删除好友成功);
			return true;
		} else{
			if(logger.isWarnEnabled()){
				logger.warn("[删除好友失败] [没有这个好友] ["+player.getLogString()+"] ["+playerid+"] []");
			}
			player.send_HINT_REQ(Translate.text_没有这个好友);
			return false;
		}
		
	}
	
//	/**
//	 * 得到好友度
//	 * @param player
//	 * @param playerId
//	 * @return
//	 */
//	public long getFavorDegree(Player player, long playerId){
//		Friend f = getFriend(player, playerId);
//		if(f == null) {
//			return 0;
//		}
//		return f.favorDegree;
//	}
	
//	/**
//	 * 清除友好度，一般情况下是在删除好友的同时删除
//	 * @param playerId
//	 */
//	public void clearFavorDegree(Player player, long playerId){
//		Friend f = this.getFriend(player, playerId);
//		if(f == null) {
//			return;
//		}
//		f.favorDegree = 0;
//	}

	
	
	/**
	 * 得到用户的黑名单
	 * @return
	 */
	public List<Long> getBlacklist(Player player) {
		
		return this.getRelationById(player.getId()).getBlacklist();
	}
	
	
	/**
	 * 是否是黑名单成员
	 * @param playerid
	 * @return
	 */
	public boolean isBlackuser(Player player, long playerid) {
		return getRelationById(player.getId()).checkBlack(playerid);
	}
	
	/**
	 * 是否能新增黑名单用户
	 * @param player
	 * @return
	 */
	public boolean canAddBlack(Player player) {
		List<Long> flist = this.getBlacklist(player);
		return flist.size() < MAX_BLACK_NUM;
	}
		
	public boolean addBlackuser(Player player, long id) {
		if(player.getId() == id){
			player.send_HINT_REQ(Translate.text_不能操作自己);
			return false;
		}
		
		Relation r = this.getRelationById(player.getId());
		if(r.checkBlack(id)){
			player.send_HINT_REQ(Translate.text_已经是黑名单);
		}else{
			if(r.getBlacklist().size() >= FRIENDNUMMAX){
				player.send_HINT_REQ(Translate.添加黑名单失败);
				return false;
			}
			if(this.isFriend(player,id)){
				this.removeFriend(player, id);
			}
			
			//如果是临时好友删除临时好友
			if(r.getTemporarylist().contains(id)){
				r.removeTemporary(id);
//				r.getTemporarylist().remove(playerA.getId());
				if(logger.isWarnEnabled()){
					logger.warn("[添加黑名单] [删除临时好友成功] ["+player.getLogString()+"] ["+id+"]");
				}
			}
			
			//如果是仇人删除仇人
			if (isChouren(player, id)) {
				removeChouren(player, id);
			}
			
			r.addBlack(id);
			if(logger.isWarnEnabled()){
				logger.warn("[添加黑名单成功] ["+player.getLogString()+"] ["+id+"] []");
			}
			player.send_HINT_REQ(Translate.text_添加黑名单成功);
		}
		return true;
	}
	
	public boolean removeBlackuser(Player player, long playerid) {
		
		if(player.getId() == playerid){
			player.send_HINT_REQ(Translate.text_不能操作自己);
			return false;
		}
		Relation r = this.getRelationById(player.getId());

		if(r.checkBlack(playerid)){
			r.removeBlack(playerid);
			if(logger.isInfoEnabled()){
				logger.info("[删除黑名单成功] ["+player.getLogString()+"] ["+playerid+"] []");
			}
			player.send_HINT_REQ(Translate.text_删除黑名单成功);
			return true;
		}else{
			if(logger.isInfoEnabled()){
				logger.info("[删除黑名单失败] [黑名单没有这个] ["+player.getLogString()+"] ["+playerid+"] []");
			}
			return false;
		}
		
	}
	
	
	/**
	 * 得到用户的仇人单
	 * @return
	 */
	public List<Long> getChourenlist(Player player) {
		return this.getRelationById(player.getId()).getChourenlist();
	}
	/**
	 * 是否是仇人名单成员
	 * @param playerid
	 * @return
	 */
	public boolean isChouren(Player player, long playerid) {
		return getRelationById(player.getId()).checkChouren(playerid);
	}
	
	/**
	 * 是否能新增仇人名单用户
	 * @param player
	 * @return
	 */
	public boolean canAddChouren(Player player) {
		List<Long> flist = this.getChourenlist(player);
		return flist.size() < FRIENDNUMMAX;
	}
	/**
	 * 
	 * @param player 自己
	 * @param playerid  仇人id
	 */
	public void addChouren(Player player, long id) {
		
		Player p = null;
		try {
			p = playerManager.getPlayer(id);
			if(player.getCountry() == p.getCountry()){
				
				if(logger.isInfoEnabled()){
					logger.info("[添加仇人错误] [俩人是一个国家] ["+player.getLogString()+"] ");
				}
				return;
			}
		} catch (Exception e) {
			SocialManager.logger.error("[添加一个仇人异常] ["+player.getLogString()+"]",e);
		}
		if(canAddChouren(player)) {
			
		}else{
			SocialManager.logger.error("[仇人已达到上限] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
			return ;
		}
		Relation r = this.getRelationById(player.getId());
		
		if(r.checkChouren(id)){
//			SocialManager.logger.error("[已经是仇人] [] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
			SocialManager.logger.error("[已经是仇人] [] [{}] [{}] [{}] []", new Object[]{player.getId(),player.getName(),player.getUsername()});
		}else{
			r.addChouren(id);
			AchievementManager.getInstance().record(player,RecordAction.仇人数量);
			if(AchievementManager.logger.isWarnEnabled()){
				AchievementManager.logger.warn("[成就统计获得仇人数量] ["+player.getLogString()+"] ["+id+"]");
			}
			
			if(logger.isWarnEnabled()){
				logger.warn("[添加仇人成功] ["+player.getLogString()+"] [仇人id:"+id+"]");
			}
		}
		
	}
	
	/**
	 * 移除仇人名单
	 * @param playerid
	 */
	public boolean removeChouren(Player player, long playerid){
		
		Relation r = this.getRelationById(player.getId());
		
		if(r.checkChouren(playerid)){
			r.removeChouren(playerid);
			if(logger.isInfoEnabled()){
				logger.info("[删除仇人成功] ["+player.getLogString()+"] ["+playerid+"]");
			}
			player.send_HINT_REQ(Translate.text_删除仇人成功);
			return true;
		}else{
			if(logger.isInfoEnabled()){
				logger.info("[删除仇人失败] [仇人中没有这个] ["+player.getLogString()+"] ["+playerid+"]");
			}
			return false;
		}
		
	}
	
	public void clearTemporaryFriend(Player player) {
		
		this.getRelationById(player.getId()).clearTemporaryFriend();
	}
	
	
	/**
	 * 增加一个临时好友
	 * @param player
	 * @param playerid 好友id
	 * @return
	 */
	public void addTemporaryFriend(Player player, long playerid) {
		
		List<Long> ids = this.getRelationById(player.getId()).getTemporarylist();
		List<Long> friends = this.getRelationById(player.getId()).getFriendlist();
		if(!ids.contains(playerid) && !friends.contains(playerid)){
			
			this.getRelationById(player.getId()).addTemporary(playerid);
			if(logger.isInfoEnabled()){
				logger.info("[添加临时好友成功] ["+player.getLogString()+"] ["+playerid+"]");
			}
			
		}
	}
	
	/**
	 * 得到用户的临时好友
	 * @return
	 */
	public List<Long> getTemporarylist(Player player) {
		return getRelationById(player.getId()).getTemporarylist();

	}
	
	/**
	 * 根据条件查找好友
	 * @param arr  国家，省份，城市，星座 ，职业  ，最小级别  ，最大级别
	 * @return 
	 * @throws Exception
	 */
	public List<Player_RelatinInfo> queryFriend(Player player,String[] arr) throws Exception{
		
		int country;
		int province;
		int city;
		byte star;
		byte career;
		int minLevel;
		int maxLevel;

		String fuzzyName = "";
		
		country = Byte.parseByte(arr[0].trim());
		province = Integer.parseInt(arr[1].trim());
		city = Integer.parseInt(arr[2].trim());
		star = Byte.parseByte(arr[3].trim());
		career = Byte.parseByte(arr[4].trim());
		minLevel = Integer.parseInt(arr[5].trim());
		maxLevel = Integer.parseInt(arr[6].trim());
		
		if(arr.length == 8){
			fuzzyName = arr[7].trim();
		}
		
		if(minLevel > maxLevel && maxLevel != -1){
			if(logger.isInfoEnabled()){
				logger.info("[查找好友错误] [低等级高于高等级] ["+minLevel+"] ["+maxLevel+"]");
			}
			return null;
		}
		if(logger.isInfoEnabled()){
			logger.info("[查找好友参数] ["+player.getLogString()+"] ["+country+"] ["+province+"] ["+city+"] ["+star+"] ["+career+"] ["+minLevel+"] ["+maxLevel+"]");
		}
		Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
		List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
		
		for(Player p: ps){
			if(p.isOnline() && p != player){
				PlayerSimpleInfoManager psm = PlayerSimpleInfoManager.getInstance();
				PlayerSimpleInfo info = psm.getInfoById(p.getId());
				if(info == null){
					continue;
				}
				// 必须是本国人才能加好友
				if(p.getCountry() != player.getCountry()){
					continue;
				}
				if(country != -1){
					if(info.getCountry() != country){
						continue;
					}
				}
				if(province != -1){
					if(info.getProvince() != province){
						continue;
					}
				}
				if(city != -1){
					if(info.getCity() != city){
						continue;
					}
				}
//				国家，省份，城市，星座 ，职业  ，最小级别  ，最大级别
				if(star != -1){
					if(info.getStar() != star){
						continue;
					}
				}
				if(career != -1){
					if(p.getMainCareer() != career){
						continue;
					}
				}
				if(maxLevel != -1 && minLevel != -1){
					if(p.getLevel() < minLevel || p.getLevel() > maxLevel){
						continue;
					}
				}else if(minLevel == -1 && maxLevel == -1){

					
				}else if(maxLevel == -1){
					if(p.getLevel() < minLevel){
						continue;
					}
				}else if(minLevel == -1){
					if(p.getLevel() > minLevel){
						continue;
					}
				}
				//模糊查找
				if(!fuzzyName.equals("") && !p.getName().contains(fuzzyName)){
					continue;
				}
				int relationShip = SocialManager.getInstance().getRelationA2B(player.getId(), p.getId());
				Player_RelatinInfo prinfo = new Player_RelatinInfo((byte)relationShip, p.getId(),p.getMainCareer(), info.getIcon(), p.getName(), p.isOnline(), info.getMood());
				list.add(prinfo);
				
				if(list.size() >= 50){
					return list;
				}
			}
		}
		return list;
	}
	
	/**
	 * 0 好友 1结义 2 师 3徒 4 夫妻  -1 没有关系
	 * @param p
	 * @return
	 */
	public int getRelationA2B(Long idA,Long idB){
//		long idB = playerB.getId();
		Relation relation = this.getRelationById(idA);
		long uniteId = relation.getUniteId();
		if(relation.getMarriageId() > 0){
			MarriageInfo info = MarriageManager.getInstance().getMarriageInfoById(relation.getMarriageId());
			if(info != null && (idB == info.getHoldA() || idB == info.getHoldB()) && (idA == info.getHoldA() || idA == info.getHoldB())){
				return 4;
			}
		}
		MasterPrentice mp = relation.getMasterPrentice();
		if(mp != null){
			if(mp.getMasterId() == idB){
				return 2;
			}else if(mp.getPrentices().contains(idB)){
				return 3;
			}
		}
		if(uniteId != -1){
			Unite u = UniteManager.getInstance().getUnite(uniteId);
			if(u != null){
				if(u.getMemberIds().contains(idB)){
					return 1;
				}
			}
		}
		if(relation.getFriendlist().contains(idB))
			return 0;
		return -1;
	}
	
	public void upOrDownNotice(Player player,boolean upOrdown){

		 //上线通知
		 SocialManager sm = SocialManager.getInstance();
		 Relation relation = sm.getRelationById(player.getId());
		 List<Long> list = relation.getNoticePlayerId();
		 if(list == null || list.size() == 0) {
			 
				 if (SocialManager.logger.isDebugEnabled()) SocialManager.logger.debug("[上下线通知] [没有关注人] ["+player.getLogString()+"]");
			 return;
		 }
		 Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
		 for(long id : list){
			 for(Player p :ps){
				 if(p.getId() == id){
					 Relation relationB = sm.getRelationById(p.getId());
					 relationB.setUpdateRelationKey(true);
					 if(p.isUpLineNotice()){
						 
//						 0 好友 1结义 2 师 3徒 4 夫妻  -1 没有关系
						 int relationShip = SocialManager.getInstance().getRelationA2B(id, player.getId());
						
						 if(relationShip < 0 || relationShip > 4){
							 SocialManager.logger.error("[上下线通知错误] [俩人关系错误] [关系:"+relationShip+"]  [上下线玩家:"+player.getLogString()+"] [通知的人:"+p.getLogString()+"]");
						 }else{
							 
							 String ship = "";
							 switch(relationShip){
								 case 0:ship = Translate.text_好友;break;
								 case 1:ship = Translate.text_结义兄弟;break;
								 case 2:ship = Translate.text_师傅;break;
								 case 3:ship = Translate.text_徒弟;break;
								 case 4:
									 if(p.getSex() == 0){
										 ship = Translate.text_妻子;
									 }else{
										 ship = Translate.text_丈夫;
									 }
									 break;
								default : break;
							 }
							 
							 if(upOrdown){
								 p.send_HINT_REQ(Translate.translateString(Translate.关系xx名字xx上线了, new String[][]{{Translate.STRING_1,ship},{Translate.STRING_2,player.getName()}}));
							 }else{
								 p.send_HINT_REQ(Translate.translateString(Translate.关系xx名字xx下线了, new String[][]{{Translate.STRING_1,ship},{Translate.STRING_2,player.getName()}}));
							 }
							 
						 }
					 }
					 break;
				 }
			 }
		 }
		 if (SocialManager.logger.isInfoEnabled()) {
			 SocialManager.logger.info(" [上下线通知] ["+player.getLogString()+"]");
		 }
	}
	
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		System.err.println("month:"+month);
		System.err.println("day:"+day);
	}

}
