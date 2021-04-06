package com.fy.engineserver.society;

import static com.fy.engineserver.datasource.language.Translate.不能添加黑名单有其他关系;
import static com.fy.engineserver.datasource.language.Translate.不能直接删除好友有其他关系;
import static com.fy.engineserver.datasource.language.Translate.不能给自己发;
import static com.fy.engineserver.datasource.language.Translate.发送添加好友请求;
import static com.fy.engineserver.datasource.language.Translate.对方不在线;
import static com.fy.engineserver.datasource.language.Translate.对方拒绝了你的请求;
import static com.fy.engineserver.datasource.language.Translate.已经是好友;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.chat.ChatMessageService;
import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gametime.SystemTime;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.jiazu.Jiazu;
import com.fy.engineserver.jiazu.service.JiazuManager;
import com.fy.engineserver.menu.MenuWindow;
import com.fy.engineserver.menu.Option;
import com.fy.engineserver.menu.WindowManager;
import com.fy.engineserver.menu.society.Option_Interest_Agree;
import com.fy.engineserver.menu.society.Option_Interest_DisAgree;
import com.fy.engineserver.message.GET_TYPEPLAYER_REQ;
import com.fy.engineserver.message.GET_TYPEPLAYER_REQ2;
import com.fy.engineserver.message.GET_TYPEPLAYER_RES;
import com.fy.engineserver.message.GET_TYPEPLAYER_RES2;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_AUTOBACK_RES;
import com.fy.engineserver.message.QUERY_FRIEND2_REQ;
import com.fy.engineserver.message.QUERY_FRIEND2_RES;
import com.fy.engineserver.message.QUERY_FRIEND_REQ;
import com.fy.engineserver.message.QUERY_FRIEND_RES;
import com.fy.engineserver.message.QUERY_PERSONNAL_INFO_RES;
import com.fy.engineserver.message.QUERY_SIMPLE_PLAYER_INFO_REQ;
import com.fy.engineserver.message.QUERY_SIMPLE_PLAYER_INFO_RES;
import com.fy.engineserver.message.QUERY_WINDOW_RES;
import com.fy.engineserver.message.SEND_SOCIETY_OPRATE_REQ;
import com.fy.engineserver.message.SEND_SOCIETY_OPRATE_RES;
import com.fy.engineserver.message.SET_AUTOBACK_REQ;
import com.fy.engineserver.message.SET_AUTOBACK_RES;
import com.fy.engineserver.message.SET_PLAYERINFO_REQ;
import com.fy.engineserver.message.SET_PLAYERINFO_RES;
import com.fy.engineserver.message.SOCIAL_QUERY_PLAYERINFO_REQ;
import com.fy.engineserver.message.SOCIAL_QUERY_PLAYERINFO_RES;
import com.fy.engineserver.message.SOCIAL_REFUSE_FRIEND_REQ;
import com.fy.engineserver.message.SOCIAL_RELATION_MANAGE_REQ;
import com.fy.engineserver.message.SOCIAL_RELATION_MANAGE_RES;
import com.fy.engineserver.platform.PlatformManager;
import com.fy.engineserver.platform.PlatformManager.Platform;
import com.fy.engineserver.playerTitles.PlayerTitle;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.PlayerSimpleInfo;
import com.fy.engineserver.sprite.PlayerSimpleInfoManager;
import com.fy.engineserver.tengxun.TengXunDataManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.fy.engineserver.util.Utils;
import com.fy.engineserver.zongzu.data.ZongPai;
import com.fy.engineserver.zongzu.manager.ZongPaiManager;
import com.xuanzhi.tools.text.WordFilter;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class SocietySubSystem  extends SubSystemAdapter {
	
//	public	static Logger logger = LoggerFactory.getLogger(SocietySubSystem.class);
	public	static Logger logger = SocialManager.logger;
	protected PlayerManager playerManager;
	private ChatMessageService chatMessageService;
	private SocialManager socialManager;
	private PlayerSimpleInfoManager playerSimpleInfoManager;
	
	public PlayerSimpleInfoManager getPlayerSimpleInfoManager() {
		return playerSimpleInfoManager;
	}

	public void init(){
		
		if(logger.isInfoEnabled())
			logger.info("[SocietySubSystem] [初始化成功]");
		ServiceStartRecord.startLog(this);
	}
	public void setPlayerSimpleInfoManager(
			PlayerSimpleInfoManager playerSimpleInfoManager) {
		this.playerSimpleInfoManager = playerSimpleInfoManager;
	}

	public SocialManager getSocialManager() {
		return socialManager;
	}

	public void setSocialManager(SocialManager socialManager) {
		this.socialManager = socialManager;
	}

	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"QUERY_AUTOBACK_REQ","SET_AUTOBACK_REQ", "GET_TYPEPLAYER_REQ", "GET_TYPEPLAYER_REQ2" ,"SOCIAL_RELATION_MANAGE_REQ" ,
				"SOCIAL_QUERY_PLAYERINFO_REQ","SEND_SOCIETY_OPRATE_REQ","QUERY_PERSONNAL_INFO_REQ","SOCIAL_REFUSE_FRIEND_REQ",
				"SET_PLAYERINFO_REQ","QUERY_FRIEND_REQ","QUERY_SIMPLE_PLAYER_INFO_REQ","QUERY_FRIEND2_REQ"};
	}

	@Override
	public String getName() {
		return "SocietySubSystem";
	}

	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		
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

	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}
	
	
	
	public Map<Long, Long> guanzhuCD = new ConcurrentHashMap<Long, Long>();
	public static long CD = 15000;
	
	/**
	 * 处理添加,删除好友 仇人 等请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SOCIAL_RELATION_MANAGE_REQ(Connection conn,RequestMessage message, Player player){
		SOCIAL_RELATION_MANAGE_REQ req = (SOCIAL_RELATION_MANAGE_REQ)message;
		byte op = req.getOperation();
		long id = req.getPlayerId();
		byte ptype = req.getPtype();
		PlayerSimpleInfo simpleInfo = null;
		try {
			simpleInfo = PlayerSimpleInfoManager.getInstance().getInfoById(id);
		} catch (Exception e) {
			logger.warn("[社交操作异常] ["+player.getLogString()+"] ["+id+"] ",e);
			return null;
		}
		//0-好友,1-临时好友 2-黑名单, 3-仇人
		if(op == 1){
			// 删除  好友  黑名单  仇人
			if(ptype == 0){
				int relationShip = socialManager.getRelationA2B(player.getId(), id);
				if(relationShip >0){
					player.send_HINT_REQ(不能直接删除好友有其他关系);
					return null;
				}
				if(this.socialManager.removeFriend(player,id)){
					SOCIAL_RELATION_MANAGE_RES res = new SOCIAL_RELATION_MANAGE_RES(req.getSequnceNum(), "", op, ptype, id);
					return res;
				}
			}else if(ptype == 2){
				if(this.socialManager.removeBlackuser(player, id)){
					SOCIAL_RELATION_MANAGE_RES res = new SOCIAL_RELATION_MANAGE_RES(req.getSequnceNum(), "", op, ptype, id);
					return res;
				}
			} else if(ptype == 3){
				if(this.socialManager.removeChouren(player, id)){
					SOCIAL_RELATION_MANAGE_RES res = new SOCIAL_RELATION_MANAGE_RES(req.getSequnceNum(), "", op, ptype, id);
					return res;
				}
			}else {
				logger.error("[处理社会关系] [失败] [删除] ["+ptype+"] ["+player.getLogString()+"] [对方id:"+id+"]");
			}
			
			
		}else if(op == 0){
			
			Player p1 = null;
			try {
				p1 = this.playerManager.getPlayer(id);
			} catch (Exception e) {
				logger.warn("[社交操作异常] ["+player.getLogString()+"] ["+id+"] ",e);
				return null;
			}
			
			//添加  好友  黑名单
			if(ptype == 0){
				String bln1 = this.socialManager.addFriend(player, p1);
//				String bln2 = this.socialManager.addFriend(p1, player);
				if(bln1.equals("")){
					
					player.sendError(Translate.text_关注好友成功);
					SOCIAL_RELATION_MANAGE_RES res = new SOCIAL_RELATION_MANAGE_RES(req.getSequnceNum(), "", op, ptype, id);
					player.addMessageToRightBag(res);
					
//					Relation relatinP1 = socialManager.getRelationById(p1.getId());
					int relationShip = socialManager.getRelationA2B(p1.getId(),player.getId());
					
					AchievementManager.getInstance().record(player,RecordAction.好友数量);
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[成就统计获得好友数量] ["+player.getLogString()+"] ["+p1.getName()+"]");
					}
					if(relationShip < 0){
						//请求对方是否关注自己
						
						MenuWindow mw = WindowManager.getInstance().createTempMenuWindow(600);
						String descript = Translate.translateString(Translate.text_xx已经关注了你你是否关注他, new String[][] {{Translate.PLAYER_NAME_1,player.getName()}});
						mw.setDescriptionInUUB(descript);
						
						Option_Interest_Agree agree = new Option_Interest_Agree();
						agree.setPlayerA(player.getId());
						agree.setText(Translate.确定);
		
						Option_Interest_DisAgree disAgree = new Option_Interest_DisAgree();
						disAgree.setPlayerA(player.getId());
						disAgree.setText(Translate.取消);
						
						mw.setOptions(new Option[] { agree,disAgree });
						
						QUERY_WINDOW_RES res1 = new QUERY_WINDOW_RES(GameMessageFactory.nextSequnceNum(), mw, mw.getOptions());
						p1.addMessageToRightBag(res1);
						
						if (logger.isWarnEnabled()) {
							logger.warn("[发送关注成功] ["+player.getLogString()+"] ["+id+"]");
						}
					}
				}else{
					player.sendError(bln1);
					if (logger.isWarnEnabled()) {
						logger.warn("[发送关注错误] ["+player.getLogString()+"] ["+id+"] ["+bln1+"]");
					}
				}
				
			}else if(ptype == 2){
				//添加黑名单
				int relationShip = socialManager.getRelationA2B(player.getId(), id);
				if(relationShip >0){
					player.send_HINT_REQ(不能添加黑名单有其他关系);
					return null;
				}
				 boolean bln = this.socialManager.addBlackuser(player, id);
				 if(bln){
					 SOCIAL_RELATION_MANAGE_RES res = new SOCIAL_RELATION_MANAGE_RES(req.getSequnceNum(), "", op, ptype, id);
					 return res;
				 }
			}else {
				logger.error("[处理社会关系] [失败] [添加] ["+ptype+"] ["+player.getLogString()+"] [对方id:"+id+"]");
			}
		}else {
			logger.error("[处理社会关系] [失败] [操作:"+op+"] ["+ptype+"] ["+player.getLogString()+"] [对方id:"+id+"]");
			
		}
		return null;
	}
	
	
	public ResponseMessage handle_GET_TYPEPLAYER_REQ2(Connection conn,RequestMessage message, Player player) throws  Exception {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GET_TYPEPLAYER_REQ2 req = (GET_TYPEPLAYER_REQ2)message;
		int pageNum = req.getPage();
		int numbers = req.getNumbers();
		// 0-好友,1-临时好友 2-黑名单, 3-仇人,4-群组
		byte ptype = req.getPtype();
		
		List<Long> listOnline = new ArrayList<Long>();
		List<Long> listOffline = new ArrayList<Long>();
		List<Long> sumList = null;
		if(ptype == 0){
			Relation relation = this.socialManager.getRelationById(player.getId());
			
			//刷新现在结义成就
			long uniteId = relation.getUniteId();
			if(uniteId > 0){
				Unite u = UniteManager.getInstance().getUnite(uniteId);
				if(u != null){
					int num = u.getMemberIds().size();
					AchievementManager.getInstance().record(player, RecordAction.结义人数,num);
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[成就统计结义人数] ["+player.getLogString()+"] ["+num+"]");
					}
				}
			}
			
			relation.reSortFriendList();
			List<Long> online = new ArrayList<Long>(relation.getOnline());
				
			int onlineNum = online.size();
			List<Long> offline = relation.getOffline();
			online.addAll(offline);
			if(logger.isInfoEnabled()){
				logger.info("[查询好友] ["+player.getLogString()+"] ["+online.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{onlineNum,offline.size(),online.size()});
			}
			if(online.size() == 0){
				GET_TYPEPLAYER_RES2 res = new GET_TYPEPLAYER_RES2(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
			int sumPage = (int)Math.ceil(((1.0*online.size())/numbers));
			if(pageNum < 1 || pageNum > sumPage || sumPage <1)
				return null;
				
			List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
			byte relationShip;
			long id;
			String icon;
			String name;
			boolean isonline;
			String mood;
			for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
				if(online.size() <= j )
					break;
				id = online.get(j);
				try {
					PlayerSimpleInfo info = this.playerSimpleInfoManager.getInfoById(id);
					if(info != null){
						icon = "";
						name = info.getName();
						isonline = false;
						if(j < onlineNum) isonline = true;
						mood = info.getMood();
						relationShip = (byte)socialManager.getRelationA2B(player.getId(), id);
						list.add(new Player_RelatinInfo(relationShip, id, info.getCareer(),icon, name, isonline, mood, TengXunDataManager.instance.getGameLevel(id)));
					}
				} catch (Exception e) {
					logger.error("[查找社会关系"+ptype+"] ["+id+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
				}
				
			}
			if(logger.isInfoEnabled())
				logger.info("[查找社会关系{}] [{}] [{}] [{}] []", new Object[]{ptype,player.getId(),player.getName(),player.getUsername()});
			GET_TYPEPLAYER_RES2 res = new GET_TYPEPLAYER_RES2(req.getSequnceNum(),online.size(),onlineNum, sumPage,pageNum,ptype,list.toArray(new Player_RelatinInfo[0]));
			return res;
			
		}else if(ptype == 1){
			//1-临时好友 2-黑名单, 3-仇人
			Relation relation = this.socialManager.getRelationById(player.getId());
			relation.resorttemporarylist();
			listOnline = relation.getTemporarylistOnline();
			listOffline = relation.getTemporarylistOffline();
			sumList = new ArrayList<Long>(listOnline);
			sumList.addAll(listOffline);
			if(logger.isInfoEnabled()){
				logger.info("[查询临时好友] ["+player.getLogString()+"] ["+sumList.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{listOnline.size(),listOffline.size(),sumList.size()});
			}
			if(sumList.size() == 0){
				GET_TYPEPLAYER_RES2 res = new GET_TYPEPLAYER_RES2(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
			
		}else if(ptype == 2){
			Relation relation = this.socialManager.getRelationById(player.getId());
			relation.resortBlackList();
			listOnline = relation.getBlackNameListOnline();
			listOffline = relation.getBlackNameListOffline();
			sumList = new ArrayList<Long>(listOnline);
			sumList.addAll(listOffline);
			
			if(logger.isInfoEnabled()){
				logger.info("[查询黑名单] ["+player.getLogString()+"] ["+sumList.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{listOnline.size(),listOffline.size(),sumList.size()});
			}
			if(sumList.size() == 0){
				GET_TYPEPLAYER_RES2 res = new GET_TYPEPLAYER_RES2(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
		}else if(ptype == 3){
			Relation relation = this.socialManager.getRelationById(player.getId());
			relation.resortChourenList();
			listOnline = relation.getChourenNameListOnline();
			listOffline = relation.getChourenNameListOffline();
			sumList = new ArrayList<Long>(listOnline);
			sumList.addAll(listOffline);
			
			if(logger.isInfoEnabled()){
				logger.info("[查询仇人] ["+player.getLogString()+"] ["+sumList.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{listOnline.size(),listOffline.size(),sumList.size()});
			}
			if(sumList.size() == 0){
				GET_TYPEPLAYER_RES2 res = new GET_TYPEPLAYER_RES2(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
		}
		if(sumList != null){
			
			int playerNum = sumList.size();
			if(logger.isInfoEnabled())
				logger.info("relationtest sumnum{}", new Object[]{playerNum});
			int sumPage = (int)Math.ceil(((1.0*playerNum)/numbers));
			if(pageNum < 1 || pageNum > sumPage || sumPage < 1)
				return null;
			List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
			byte relationShip;
			long id;
			String icon;
			String name;
			boolean isonline;
			String mood;
			
			for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
				if(playerNum <= j )
					break;
				id = sumList.get(j);
				try {
					PlayerSimpleInfo info = this.playerSimpleInfoManager.getInfoById(id);
					if (info != null) {
						name = info.getName();
						icon = "";
						isonline = false;
						if(j < listOnline.size())
							isonline = true;
						mood = info.getMood();
						relationShip = (byte)(-ptype);
						
						list.add(new Player_RelatinInfo(relationShip, id,info.getCareer(), icon, name, isonline, mood, TengXunDataManager.instance.getGameLevel(id)));
					}
				} catch (Exception e) {
					logger.error("[查找社会关系"+ptype+"异常] ["+id+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
				}
				
			}
//			logger.info("[查找社会关系"+ptype+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
			if(logger.isInfoEnabled())
				logger.info("[查找社会关系{}] [{}] [{}] [{}] []", new Object[]{ptype,player.getId(),player.getName(),player.getUsername()});
			GET_TYPEPLAYER_RES2 res = new GET_TYPEPLAYER_RES2(req.getSequnceNum(),sumList.size(),listOnline.size(),sumPage ,pageNum,ptype,list.toArray(new Player_RelatinInfo[0]));
			return res;
		}
		return null;
	}
	
	/**
	 * 处理查询好友 仇人 等请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GET_TYPEPLAYER_REQ(Connection conn,RequestMessage message, Player player)throws  Exception  {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GET_TYPEPLAYER_REQ req = (GET_TYPEPLAYER_REQ)message;
		int pageNum = req.getPage();
		int numbers = req.getNumbers();
		// 0-好友,1-临时好友 2-黑名单, 3-仇人,4-群组
		byte ptype = req.getPtype();
		
		List<Long> listOnline = new ArrayList<Long>();
		List<Long> listOffline = new ArrayList<Long>();
		List<Long> sumList = null;
		if(ptype == 0){
			Relation relation = this.socialManager.getRelationById(player.getId());
			
			//刷新现在结义成就
			long uniteId = relation.getUniteId();
			if(uniteId > 0){
				Unite u = UniteManager.getInstance().getUnite(uniteId);
				if(u != null){
					int num = u.getMemberIds().size();
					AchievementManager.getInstance().record(player, RecordAction.结义人数,num);
					if(AchievementManager.logger.isWarnEnabled()){
						AchievementManager.logger.warn("[成就统计结义人数] ["+player.getLogString()+"] ["+num+"]");
					}
				}
			}
			
			relation.reSortFriendList();
			List<Long> online = new ArrayList<Long>(relation.getOnline());
				
			int onlineNum = online.size();
			List<Long> offline = relation.getOffline();
			online.addAll(offline);
			if(logger.isInfoEnabled()){
				logger.info("[查询好友] ["+player.getLogString()+"] ["+online.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{onlineNum,offline.size(),online.size()});
			}
			if(online.size() == 0){
				GET_TYPEPLAYER_RES res = new GET_TYPEPLAYER_RES(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
			int sumPage = (int)Math.ceil(((1.0*online.size())/numbers));
			if(pageNum < 1 || pageNum > sumPage || sumPage <1)
				return null;
				
			List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
			byte relationShip;
			long id;
			String icon;
			String name;
			boolean isonline;
			String mood;
			for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
				if(online.size() <= j )
					break;
				id = online.get(j);
				try {
					PlayerSimpleInfo info = this.playerSimpleInfoManager.getInfoById(id);
					if(info != null){
						icon = "";
						name = info.getName();
						isonline = false;
						if(j < onlineNum) isonline = true;
						mood = info.getMood();
						relationShip = (byte)socialManager.getRelationA2B(player.getId(), id);
						list.add(new Player_RelatinInfo(relationShip, id, info.getCareer(),icon, name, isonline, mood));
					}
				} catch (Exception e) {
					logger.error("[查找社会关系"+ptype+"] ["+id+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
				}
				
			}
			if(logger.isInfoEnabled())
				logger.info("[查找社会关系{}] [{}] [{}] [{}] []", new Object[]{ptype,player.getId(),player.getName(),player.getUsername()});
			GET_TYPEPLAYER_RES res = new GET_TYPEPLAYER_RES(req.getSequnceNum(),online.size(),onlineNum, sumPage,pageNum,ptype,list.toArray(new Player_RelatinInfo[0]));
			return res;
			
		}else if(ptype == 1){
			//1-临时好友 2-黑名单, 3-仇人
			Relation relation = this.socialManager.getRelationById(player.getId());
			relation.resorttemporarylist();
			listOnline = relation.getTemporarylistOnline();
			listOffline = relation.getTemporarylistOffline();
			sumList = new ArrayList<Long>(listOnline);
			sumList.addAll(listOffline);
			if(logger.isInfoEnabled()){
				logger.info("[查询临时好友] ["+player.getLogString()+"] ["+sumList.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{listOnline.size(),listOffline.size(),sumList.size()});
			}
			if(sumList.size() == 0){
				GET_TYPEPLAYER_RES res = new GET_TYPEPLAYER_RES(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
			
		}else if(ptype == 2){
			Relation relation = this.socialManager.getRelationById(player.getId());
			relation.resortBlackList();
			listOnline = relation.getBlackNameListOnline();
			listOffline = relation.getBlackNameListOffline();
			sumList = new ArrayList<Long>(listOnline);
			sumList.addAll(listOffline);
			
			if(logger.isInfoEnabled()){
				logger.info("[查询黑名单] ["+player.getLogString()+"] ["+sumList.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{listOnline.size(),listOffline.size(),sumList.size()});
			}
			if(sumList.size() == 0){
				GET_TYPEPLAYER_RES res = new GET_TYPEPLAYER_RES(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
		}else if(ptype == 3){
			Relation relation = this.socialManager.getRelationById(player.getId());
			relation.resortChourenList();
			listOnline = relation.getChourenNameListOnline();
			listOffline = relation.getChourenNameListOffline();
			sumList = new ArrayList<Long>(listOnline);
			sumList.addAll(listOffline);
			
			if(logger.isInfoEnabled()){
				logger.info("[查询仇人] ["+player.getLogString()+"] ["+sumList.size()+"] [] ");
				logger.info("relationtest online {} offline {} sumnum{}", new Object[]{listOnline.size(),listOffline.size(),sumList.size()});
			}
			if(sumList.size() == 0){
				GET_TYPEPLAYER_RES res = new GET_TYPEPLAYER_RES(req.getSequnceNum(),0,0, 0,0,ptype,new Player_RelatinInfo[0]);
				return res;
			}
		}
		if(sumList != null){
			
			int playerNum = sumList.size();
			if(logger.isInfoEnabled())
				logger.info("relationtest sumnum{}", new Object[]{playerNum});
			int sumPage = (int)Math.ceil(((1.0*playerNum)/numbers));
			if(pageNum < 1 || pageNum > sumPage || sumPage < 1)
				return null;
			List<Player_RelatinInfo> list = new ArrayList<Player_RelatinInfo>();
			byte relationShip;
			long id;
			String icon;
			String name;
			boolean isonline;
			String mood;
			
			for(int j = (pageNum -1)*numbers; j< pageNum*numbers;j++){
				if(playerNum <= j )
					break;
				id = sumList.get(j);
				try {
					PlayerSimpleInfo info = this.playerSimpleInfoManager.getInfoById(id);
					if (info != null) {
						name = info.getName();
						icon = "";
						isonline = false;
						if(j < listOnline.size())
							isonline = true;
						mood = info.getMood();
						relationShip = (byte)(-ptype);
						
						list.add(new Player_RelatinInfo(relationShip, id,info.getCareer(), icon, name, isonline, mood));
					}
				} catch (Exception e) {
					logger.error("[查找社会关系"+ptype+"异常] ["+id+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
				}
				
			}
//			logger.info("[查找社会关系"+ptype+"] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []");
			if(logger.isInfoEnabled())
				logger.info("[查找社会关系{}] [{}] [{}] [{}] []", new Object[]{ptype,player.getId(),player.getName(),player.getUsername()});
			GET_TYPEPLAYER_RES res = new GET_TYPEPLAYER_RES(req.getSequnceNum(),sumList.size(),listOnline.size(),sumPage ,pageNum,ptype,list.toArray(new Player_RelatinInfo[0]));
			return res;
		}
		return null;
	}
	
	
	
	
	/**
	 * 处理查询好友等请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	
	public ResponseMessage handle_SOCIAL_QUERY_PLAYERINFO_REQ(Connection conn,RequestMessage message, Player player)throws  Exception {
		SOCIAL_QUERY_PLAYERINFO_REQ req = (SOCIAL_QUERY_PLAYERINFO_REQ)message;
		long id = req.getPlayerId();
		try {

			PlayerSimpleInfo info = this.playerSimpleInfoManager.getInfoById(id);
			
			if(info == null){
				logger.error("[查看玩家资料错误] ["+player.getLogString()+"] [对方:"+id+"] ");
				return null;
			}
			info = info.getinfoForPlayer(player);
			Player[] onlinePlayers = this.playerManager.getOnlinePlayers();
			boolean online = false;
			for(Player p1 : onlinePlayers){
				if(p1.getId() == id){
					online = true;
					break;
				}
			}
			
			String gang = "";
			String jiazuName = "";
			String chenghao = "";
			byte relationShip = 0;
			byte autoBack = 0;
			if(PlayerManager.getInstance().isOnline(id)){
				Player player1 = this.playerManager.getPlayer(id);
				List<PlayerTitle> list = player1.getPlayerTitles();
				
				if(list != null){
					int titleType = player1.getDefaultTitleType();
					for (PlayerTitle pt : list) {
						if (pt.getTitleType() == titleType) {
							chenghao = pt.getTitleName();
							if (SocialManager.logger.isDebugEnabled()) {
								SocialManager.logger.debug("[查询玩家称号] [" + player1.getLogString() + "] [称号:"+chenghao+"]");
							}
						}
					}
				}
				autoBack = player1.autoBack;
			}
			relationShip = (byte)socialManager.getRelationA2B(player.getId(), id);
			long jiazuId = info.getJiazuId();
			if(jiazuId > 0){
				Jiazu jz = JiazuManager.getInstance().getJiazu(jiazuId);
				ZongPai zp = null;
				if(jz != null){
					jiazuName = jz.getName();
					zp = ZongPaiManager.getInstance().getZongPaiById(jz.getZongPaiId());
				}
				if(zp != null){
					gang = zp.getZpname();
				}
				
			}
			if(logger.isInfoEnabled()){
				logger.info("[查询好友详情请求成功] ["+player.getLogString()+"] [好友id:"+id+"] [宗派:"+gang+"] [家族:"+jiazuName+"] [称号:"+chenghao+"]");
			}
			SOCIAL_QUERY_PLAYERINFO_RES res = new SOCIAL_QUERY_PLAYERINFO_RES(req.getSequnceNum(), id, info.getName(), info.getMood(), info.getLevel(), 
					CareerManager.careerNameByType(info.getCareer()), gang, jiazuName, chenghao, relationShip, info.getBrithDay(), info.getStar(), online,autoBack);
			return res;
		} catch (Exception e) {
			logger.error("[处理查询好友详情请求] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
		
		return null;
	}
	
	
	/**
	 * 处理发送添加好友请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	
	public ResponseMessage handle_SEND_SOCIETY_OPRATE_REQ(Connection conn,RequestMessage message, Player player)throws  Exception {
		SEND_SOCIETY_OPRATE_REQ req = (SEND_SOCIETY_OPRATE_REQ)message;
		long id = req.getPlayerId();
		try {
			if(player.getId() == id){
				player.send_HINT_REQ(不能给自己发);
			}
			List<Long> list = socialManager.getFriendlist(player);
			if(list != null){
				if(list.contains(id)){
					player.send_HINT_REQ(已经是好友);
					return null;
				}
			}
			Player p = this.playerManager.getPlayer(id);
			if(p.isOnline()){
				SEND_SOCIETY_OPRATE_RES res = new SEND_SOCIETY_OPRATE_RES(req.getSequnceNum(), player.getId(),player.getName());
				p.addMessageToRightBag(res);
				player.send_HINT_REQ(发送添加好友请求);
			}else{
				player.send_HINT_REQ(对方不在线);
			}
		} catch (Exception e) {
			SocialManager.logger.error("[发送添加好友请求异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}

	
	/**
	 * 处理根据条件查询好友请求 
	 * @param conn  
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_QUERY_FRIEND_REQ(Connection conn,RequestMessage message, Player player)throws  Exception  {
		try {
			QUERY_FRIEND_REQ req =(QUERY_FRIEND_REQ)message;
			String[] key = req.getKey();
			if(key == null || key.length != 7){
				if(logger.isWarnEnabled())
					logger.warn("[查找好友条件不对] ["+player.getLogString()+"] []");
				return null;
			}
			List<Player_RelatinInfo> list = this.socialManager.queryFriend(player ,key);
			if(list != null){
				if(logger.isWarnEnabled()){
					logger.warn("[查找好友成功] ["+player.getLogString()+"] ["+list.size()+"]");
				}
				QUERY_FRIEND_RES res = new QUERY_FRIEND_RES(req.getSequnceNum(),list.toArray(new Player_RelatinInfo[0]));
				return res;
			}
		} catch (Exception e) {
			logger.error("[查找好友异常] ["+player.getName()+"] ["+player.getId()+"] ["+player.getUsername()+"]",e);
		}
		return null;
	}

	
	/**
	 * 处理根据条件查询好友请求 
	 * @param conn  
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_QUERY_FRIEND2_REQ(Connection conn,RequestMessage message, Player player)throws  Exception  {
		try {
			QUERY_FRIEND2_REQ req =(QUERY_FRIEND2_REQ)message;
			String[] key = req.getKey();
			if(key == null || key.length != 8){
				if(logger.isWarnEnabled())
					logger.warn("[查找好友条件不对] ["+player.getLogString()+"] []");
				return null;
			}
			List<Player_RelatinInfo> list = this.socialManager.queryFriend(player ,key);
			if(list != null){
				if(logger.isWarnEnabled()){
					logger.warn("[模糊查找好友成功] ["+player.getLogString()+"] ["+list.size()+"]");
				}
				QUERY_FRIEND2_RES res = new QUERY_FRIEND2_RES(req.getSequnceNum(),list.toArray(new Player_RelatinInfo[0]));
				return res;
			}
		} catch (Exception e) {
			logger.error("[模糊查找好友异常] ["+player.getName()+"] ["+player.getId()+"] ["+player.getUsername()+"]",e);
		}
		return null;
	}
	
	/**
	 * 处理查询个人资料的请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_QUERY_PERSONNAL_INFO_REQ(Connection conn,RequestMessage message, Player player) {
		
		QUERY_PERSONNAL_INFO_RES res = null;
		try {
//			int time = (int) (sdata.getValue()/3600000);
			long enterTime = player.getEnterServerTime();
			long now = SystemTime.currentTimeMillis();
			
			PlayerSimpleInfo info = this.playerSimpleInfoManager.getInfoById(player.getId());
			if(info == null){
				return null;
			}
			int time = (int)((1l+now - enterTime +  info.getDurationOnline())/(60*60*1000));
			res = new QUERY_PERSONNAL_INFO_RES(message.getSequnceNum(), time, info.getSeeState(),
					info.getBrithDay(), info.getAge(), info.getStar(),info.getCountry(),info.getProvince(), info.getCity(), info.getLoving(), info.getMood(), info.getPersonShow());
			if(logger.isWarnEnabled()){
				logger.warn("[处理查询个人资料成功] ["+player.getLogString()+"] [持续时间:"+info.getDurationOnline()+"] [本次增加时间:"+(now - enterTime)+"] ["+time+"]");
			}
		} catch (Exception e) {
			logger.error("[处理查询个人资料的请求] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
		return res;
	}
	
	/**
	 * 处理修改用户资料的请求
	 * @param conn  
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_SET_PLAYERINFO_REQ(Connection conn,RequestMessage message, Player player)throws  Exception  {
		SET_PLAYERINFO_REQ req = (SET_PLAYERINFO_REQ)message;
		
		try {
			String loving = req.getLoving();
			String mood = req.getMood();
			String personShow = req.getPersonShow();
			try {				
				if(!PlatformManager.getInstance().isPlatformOf(Platform.官方)){		//非国服需要判断是否存在特殊字符导致mysql存储出问题
					if (!Utils.isValidatedUTF8ForMysql(loving)) {
						player.send_HINT_REQ(Translate.text_6382);
						return null;
					} else if (!Utils.isValidatedUTF8ForMysql(mood)) {
						player.send_HINT_REQ(Translate.text_6383);
						return null;
					} else if (!Utils.isValidatedUTF8ForMysql(personShow)) {
						player.send_HINT_REQ(Translate.text_6384);
						return null;
					}
				}
			} catch (Exception e) {
				Game.logger.error("[修改用户资料] [非国服渠道判断用户资料是否含有非法字符] [异常] [loving:" + loving + "] [mood:" + mood + "] [personShow:" + personShow + "] [" + player.getLogString() + "]", e);
			}
			WordFilter filter = WordFilter.getInstance();
			if(!filter.cvalid(loving, 0)){
				player.send_HINT_REQ(Translate.text_6382);
				return null;
			}
			if(!filter.cvalid(mood, 0)){
				player.send_HINT_REQ(Translate.text_6383);
				return null;
			}
			if(!filter.cvalid(personShow, 0)){
				player.send_HINT_REQ(Translate.text_6384);
				return null;
			}

			player.setAge(req.getAge());
			player.setBrithDay(req.getBrithDay());
			player.setPlayerCountry(req.getCountry());
			player.setCity(req.getCity());
			player.setLoving(loving);
			player.setMood(mood);
			player.setPersonShow(personShow);
			player.setProvince(req.getProvince());
			player.setSeeState(req.getSeeState());
			player.setStar(req.getStar());
			SET_PLAYERINFO_RES res = new SET_PLAYERINFO_RES(req.getSequnceNum(), true);
			PlayerManager.getInstance().savePlayer(player);
			player.send_HINT_REQ(Translate.text_设置个人资料成功);
			
			if (logger.isWarnEnabled()){
				logger.warn("[设置玩家资料成功] ["+player.getLogString()+"] [生日:"+player.getBrithDay()+"] [国家:"+player.getCountry()+"] [城市:"+player.getCity()+"] [爱好:"+player.getLoving()+"] [个人说明:"+player.getPersonShow()+"] []");
			}
			return res;
		} catch (Exception e) {
			logger.error("[设置玩家资料错误] ["+player.getLogString()+"] []",e);
		}
		SET_PLAYERINFO_RES res = new SET_PLAYERINFO_RES(req.getSequnceNum(), false);
		
		return res;
	}
	
	
	/**
	 * 设置玩家自动回复
	 * @param conn  
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_SET_AUTOBACK_REQ(Connection conn,RequestMessage message, Player player)throws  Exception  {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		
		SET_AUTOBACK_REQ req = (SET_AUTOBACK_REQ)message;
		
		byte auto_back = req.getAuto_back();
		if(auto_back < 0 || auto_back > 4){
//			logger.error("[设置玩家自动回复失败] [参数不对] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"][参数："+auto_back+"] ");
			logger.error("[设置玩家自动回复失败] [参数不对] [{}] [{}] [{}][参数：{}] ", new Object[]{player.getId(),player.getName(),player.getUsername(),auto_back});
		}else{
			player.autoBack = auto_back;
//			logger.info("[设置玩家自动回复成功] [] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"][参数："+auto_back+"] ");
			if(logger.isInfoEnabled())
				logger.info("[设置玩家自动回复成功] [] [{}] [{}] [{}][参数：{}] ", new Object[]{player.getId(),player.getName(),player.getUsername(),auto_back});
			SET_AUTOBACK_RES res = new SET_AUTOBACK_RES(req.getSequnceNum(), "", auto_back);
			return res;
		}
		return null;
	}
	
	
	/**
	 * 设置玩家自动回复
	 * @param conn  
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_QUERY_AUTOBACK_REQ(Connection conn,RequestMessage message, Player player)throws  Exception  {
		
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		logger.info("[查询玩家自动回复] [] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] ");
		if(logger.isInfoEnabled())
			logger.info("[查询玩家自动回复] [] [{}] [{}] [{}] ", new Object[]{player.getId(),player.getName(),player.getUsername()});
		QUERY_AUTOBACK_RES res = new QUERY_AUTOBACK_RES(message.getSequnceNum(), "", player.autoBack);
		return res;
	}
	
	
	/**
	 * 拒绝对方添加好友请求
	 * @param conn  
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_SOCIAL_REFUSE_FRIEND_REQ(Connection conn,RequestMessage message, Player player)throws  Exception  {
		SOCIAL_REFUSE_FRIEND_REQ req = (SOCIAL_REFUSE_FRIEND_REQ)message;
		long id = req.getPlayerId();
		Player p = playerManager.getPlayer(id);
		if(p != null){
			p.send_HINT_REQ(对方拒绝了你的请求);
		}
		return null;
	}
	
	
	/**
	 * 查询玩家简单信息
	 * @param conn  
	 * @param message
	 * @param player
	 * @return  
	 */
	public ResponseMessage handle_QUERY_SIMPLE_PLAYER_INFO_REQ(Connection conn,RequestMessage message, Player player){
	
		QUERY_SIMPLE_PLAYER_INFO_REQ req = (QUERY_SIMPLE_PLAYER_INFO_REQ)message;
		long id = req.getId();
		Player target = null;
		try {
			target = PlayerManager.getInstance().getPlayer(id);
				
			QUERY_SIMPLE_PLAYER_INFO_RES res = new QUERY_SIMPLE_PLAYER_INFO_RES(message.getSequnceNum(), target);
			if (logger.isDebugEnabled()){
				logger.debug("[查询simpleplayer成功] ["+player.getLogString()+"]");
			}
			return res;
		} catch (Exception e) {
			logger.error("[查询simpleplayer异常] ["+player.getLogString()+"]",e);
		}
		
		return null;
	}
	
	public ChatMessageService getChatMessageService() {
		return chatMessageService;
	}

	public void setChatMessageService(ChatMessageService chatMessageService) {
		this.chatMessageService = chatMessageService;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}
}
