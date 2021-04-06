package com.fy.engineserver.sprite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyManager;
import com.fy.engineserver.activity.chestFight.ChestFightManager;
import com.fy.engineserver.activity.fairyBuddha.challenge.FairyChallengeManager;
import com.fy.engineserver.activity.wolf.WolfManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.downcity2.DownCityManager2;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.QUERY_PERSON_TEAM_RES;
import com.fy.engineserver.message.SET_INTEAM_RULE_REQ;
import com.fy.engineserver.message.TEAM_APPLY_JION_REQ;
import com.fy.engineserver.message.TEAM_APPLY_LEAVE_REQ;
import com.fy.engineserver.message.TEAM_APPLY_NOTIFY_REQ;
import com.fy.engineserver.message.TEAM_APPLY_RESULT_REQ;
import com.fy.engineserver.message.TEAM_INVITE_NOTIFY_REQ;
import com.fy.engineserver.message.TEAM_INVITE_REQ;
import com.fy.engineserver.message.TEAM_INVITE_RESULT_REQ;
import com.fy.engineserver.message.TEAM_QUERY_REQ;
import com.fy.engineserver.message.TEAM_QUERY_RES;
import com.fy.engineserver.message.TEAM_SET_ASSIGN_RULE_REQ;
import com.fy.engineserver.message.TEAM_SET_CAPTAIN_REQ;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class TeamSubSystem extends SubSystemAdapter {

//	public static Logger logger = Logger.getLogger(TeamSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(TeamSubSystem.class);
	
	public static int seq_id = 1;
	
	protected PlayerManager playerManager;
	
	private TeamManager teamManager;
	
	public Object teamObjectKey = new Object();
	
	public void init() throws Exception{
		
		self = this;
		ServiceStartRecord.startLog(this);
	}
	
	private static TeamSubSystem self;

	public static TeamSubSystem getInstance() {
		return self;
	}
	
	
	public synchronized static int nextId() {
		return seq_id++;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public String[] getInterestedMessageTypes() {
		return new String[] { "TEAM_QUERY_REQ", "TEAM_INVITE_REQ", "TEAM_INVITE_RESULT_REQ", "TEAM_DISSOLVE_REQ",
				"TEAM_APPLY_JION_REQ", "TEAM_APPLY_RESULT_REQ", "TEAM_APPLY_LEAVE_REQ", "TEAM_SET_CAPTAIN_REQ",
				"TEAM_SET_ASSIGN_RULE_REQ", "TEAM_QUERY_PLAYER_REQ","QUERY_PERSON_TEAM_REQ","SET_INTEAM_RULE_REQ" };
	}


	public String getName() {
		return "TeamSubSystem";
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
	 * 客户端向服务器发送请求，某个玩家邀请另外一个玩家加入团队。(1自己没队，对方没队。2自己是队长，对方没队。 3自己没队，对方有队)
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_INVITE_REQ(Connection conn, RequestMessage message, Player player) {
		
		try{
		TEAM_INVITE_REQ req = (TEAM_INVITE_REQ)message;
		long pid = req.getBeInvitedPlayerId();
		boolean b = playerManager.isOnline(pid);
		Player beInvatedPlayer = null;
		if(b){
			try {
				beInvatedPlayer = playerManager.getPlayer(pid);
			} catch (Exception e) {
				logger.error("[某个玩家邀请另外一个玩家加入团队错误] [没有这个玩家] ["+pid+"]",e);
				return null;
			}
		}
	
		if (beInvatedPlayer == null) {
			
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队失败] [被邀请人不存在或不在线] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_5937));
			return null;
			
		}
		if(beInvatedPlayer == player){
			
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [失败] [邀请人和被邀请人是一个人] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_5945));
			return null;
		}
		if(player.getTeamMark() == Player.TEAM_MARK_MEMBER){
			
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [失败] [邀请人已经组队且不是队长] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_5944));
			return null;
		}
		if(beInvatedPlayer.getCountry() != player.getCountry()){
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [失败] [被邀请人与邀请人不是同一阵营的] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_5938));
			return null;
		}
		if(player.getTeam() != null && player.getTeam().isFull()){
			
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [失败] [邀请人所在的队伍已经达到最大人数限制] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_5946));
			return null;
		}
		if(beInvatedPlayer.isInBattleField()){
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [失败1] [被邀请人在比武赛场1] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.text_5949));
			return null;
		}
		if(beInvatedPlayer.getDuelFieldState()>0){
			
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [失败] [被邀请人在比武赛场] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.text_5949));
			return null;
		}
		if(player.getDuelFieldState()>0){
			
				if(logger.isWarnEnabled()){
					logger.warn("[邀请组队] [失败] [邀请人在比武赛场] ["+player.getLogString()+"]");
				}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.text_5950));
			return null;
		}
		if(SocialManager.getInstance().isBlackuser(beInvatedPlayer, player.getId())){
			
				if(logger.isWarnEnabled()){
					logger.warn("[邀请组队] [失败] [邀请人已被被邀请人加入黑名单] ["+player.getLogString()+"]");
				}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.text_5948));
			return null;
		}
		try {
			if (ChestFightManager.inst.isPlayerInActive(beInvatedPlayer) || ChestFightManager.inst.isPlayerInActive(player)) {
				player.sendError(Translate.宝箱乱斗不可组队);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(SocialManager.getInstance().isBlackuser(player,beInvatedPlayer.getId())){
			
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [失败] [被邀请人已被邀请人加入黑名单] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.你已经把对方加入到黑名单中不能邀请组队));
			return null;
		}
		try {
			if(TransitRobberyEntityManager.getInstance().isPlayerInRobbery(beInvatedPlayer.getId())) {
				player.sendError(Translate.渡劫不允许组队);
				return null;
			}
			if(FairyChallengeManager.getInst().isPlayerChallenging(beInvatedPlayer.getId())) {
				player.sendError(Translate.被邀请者在挑战仙尊副本);
				return null;
			}
		} catch (Exception e) {
			TransitRobberyManager.logger.error("[渡劫] [ 渡劫限制组队异常 ] [邀请人:" + player.getLogString() + "] [被邀请人:" + beInvatedPlayer.getLogString() + "]", e);
		}
		
		try{
			if(WolfManager.getInstance().isWolfGame(player)){
				player.sendError(Translate.副本中不能组队);
				return null;
			}
			if(WolfManager.signList.contains(player.getId())){
				player.sendError(Translate.副本中不能组队);
				return null;
			}
			if(WolfManager.signList.contains(beInvatedPlayer.getId())){
				player.sendError(Translate.小羊活动不允许组队);
				return null;
			}
			if(WolfManager.getInstance().getWolfGame(player) != null){
				player.sendError(Translate.副本中不能组队);
				return null;
			}
			if(WolfManager.getInstance().getWolfGame(beInvatedPlayer) != null){
				player.sendError(Translate.小羊活动不允许组队);
				return null;
			}
			if(WolfManager.getInstance().isWolfGame(beInvatedPlayer)){
				player.sendError(Translate.小羊活动不允许组队);
				return null;
			}
			if(DownCityManager2.instance.inCityGame(player)){
				player.sendError(Translate.副本中不能组队);
				return null;
			}
			if(DownCityManager2.instance.inCityGame(beInvatedPlayer)){
				player.sendError(Translate.副本中不能组队);
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if (beInvatedPlayer.hiddenTeamPop) {
			player.sendError(Translate.该玩家已经屏蔽组队);
			return null;
		}
		if(beInvatedPlayer.getTeamMark() != Player.TEAM_MARK_NONE){
			
			if(player.getTeamMark() != Player.TEAM_MARK_NONE){
				if(logger.isWarnEnabled()){
					logger.warn("[邀请组队] [失败] [自己有队被邀请人也有队] ["+player.getLogString()+"] [被邀请:"+beInvatedPlayer.getLogString()+"]");
				}
				player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, beInvatedPlayer.name + Translate.text_5940));
				return null;
			}else{
				Team team = beInvatedPlayer.getTeam();
				if (team != null) {
					synchronized (teamObjectKey) {
						if (team.isFull()) {
							player.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1,
									beInvatedPlayer.getName() + Translate.text_5941));
						} else {
							
							if(SocialManager.getInstance().isBlackuser(team.getCaptain(), player.getId())){
								if(logger.isWarnEnabled()){
									logger.warn("[申请加入队伍] [失败] [申请人："+player.getLogString()+"] [队长："+team.getCaptain().getLogString()+"] [申请人已被队长加入黑名单]");
								}
								player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.translateString(Translate.队伍队长已经把你加入了黑名单不能申请入队, new String[][]{{Translate.STRING_1,team.getCaptain().getName()}})));
								return null;
							}
							if(SocialManager.getInstance().isBlackuser(player, team.getCaptain().getId())){
								if(logger.isWarnEnabled()){
									logger.warn("[申请加入队伍] [失败] [申请人："+player.getName()+"] [队长："+team.getCaptain().getName()+"] [申请人已把队长加入黑名单]");
								}
								player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.translateString(Translate.你已经把队伍队长xx加入黑名单不能申请入队, new String[][]{{Translate.STRING_1,team.getCaptain().getName()}})));
								return null;
							}
							try{
								if(WolfManager.getInstance().isWolfGame(team.getCaptain())){
									player.sendError(Translate.小羊活动不允许被邀请);
								}
							}catch(Exception e){
								e.printStackTrace();
								if(logger.isWarnEnabled()){
									logger.warn("[申请加入队伍] [失败:正在参加小羊活动2] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"]");
								}
							}
							try {
								if(TransitRobberyEntityManager.getInstance().isPlayerInRobbery(team.getCaptain().getId())) {
									if(logger.isWarnEnabled()){
										logger.warn("[申请加入队伍] [失败] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"] [队长正在渡劫]");
									}
									player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.渡劫不允许组队2 ));
									return null;
								}
								if(FairyChallengeManager.getInst().isPlayerChallenging(team.getCaptain().getId())) {
									player.sendError(Translate.队长正在挑战仙尊);
									if(logger.isWarnEnabled()){
										logger.warn("[申请加入队伍] [失败] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"] [队长正在挑战仙尊]");
									}
									return null;
								}
							}catch (Exception e) {
								if(player != null && team.getCaptain() != null) {
									TransitRobberyManager.logger.error("[渡劫] [组队限制异常] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"]", e);
								} else {
									TransitRobberyManager.logger.error("[渡劫] [组队限制异常] [队长:" + team.getCaptain() + "] [申请人 :" + player + "]", e);
								}
							}
							try{
								if(team.getCaptain().getDuelFieldState() > 0){
									if(logger.isWarnEnabled()){
										logger.warn("[申请加入队伍] [失败] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"] [队长正在战场]");
									}
									player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.队友正在战场 ));
									return null;
								}
							}catch(Exception e){
								e.printStackTrace();
							}
							if (team.getCaptain().hiddenTeamPop) {
								player.sendError(Translate.该玩家已经屏蔽组队);
								return null;
							}
							
							//队长在跨服上
							if(team.getCaptain().isInCrossServer()) {
//									logger.warn("[申请加入队伍] [失败] [申请人："+player.getName()+"] [被申请人："+beInvatedPlayer.getName()+"] [队长在跨服服务器上]");
								if(logger.isWarnEnabled())
									logger.warn("[申请加入队伍] [失败] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"] [队长在跨服服务器上]");
								player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 1, Translate.text_5943));
							}else{
								
								if(team.getCaptain().getInteamRule() == 0){
									//自动进队
									team.addMember(player);
								}else{
									team.getCaptain().addMessageToRightBag(
											new TEAM_APPLY_NOTIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(),
													player.getName()));
									if(logger.isWarnEnabled()){
										logger.warn("[申请入队成功] [向队长发送请求] ["+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"]");
									}
								}
							}
						}
					}
					return null;
				}
			}
		}
		if(player.getTeamMark() == Player.TEAM_MARK_CAPTAIN){
			TEAM_INVITE_NOTIFY_REQ req2 = new TEAM_INVITE_NOTIFY_REQ(GameMessageFactory.nextSequnceNum(),player.getId(),player.getName());
			beInvatedPlayer.addMessageToRightBag(req2);
			if(logger.isWarnEnabled()){
				logger.warn("[邀请组队] [成功] [邀请人是队长] ["+player.getLogString()+"] [被邀请:"+beInvatedPlayer.getLogString()+"]");
			}
		}else{
			TEAM_INVITE_NOTIFY_REQ req2 = new TEAM_INVITE_NOTIFY_REQ(GameMessageFactory.nextSequnceNum(),player.getId(),player.getName());
			beInvatedPlayer.addMessageToRightBag(req2);
			
			if(logger.isWarnEnabled())
				logger.warn("[邀请组队] [成功] [邀请人未组队，要求组成一个新的队伍] ["+player.getLogString()+"] [被邀请:"+beInvatedPlayer.getLogString()+"]");
			
		}
		}catch(Exception e){
			logger.error("[邀请玩家进队异常] ["+player.getLogString()+"]",e);
		}
		return null;
		
	}
	
	
	/**
	 * 邀请通知回复，同意或者拒绝某人的邀请
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_INVITE_RESULT_REQ(Connection conn, RequestMessage message, Player player) {
		
		try{
		TEAM_INVITE_RESULT_REQ req = (TEAM_INVITE_RESULT_REQ)message;
		Player invitePlayer = null;
		if(playerManager.isOnline(req.getInvitePlayer())){
			try {
				invitePlayer = playerManager.getPlayer(req.getInvitePlayer());
			} catch (Exception e) {
				logger.error("[组队邀请回复异常] ["+player.getLogString()+"]",e);
			}
		}
		if(invitePlayer != null && invitePlayer.getCountry() == player.getCountry()){
			if(invitePlayer.getTeamMark() != Player.TEAM_MARK_MEMBER && player.getTeamMark() == Player.TEAM_MARK_NONE){
				if(req.getResult() == 0){
					// 被邀请人同意加入队伍
					synchronized(teamObjectKey) {
						
						if(invitePlayer.getTeam() != null && invitePlayer.getTeam().isFull()){
							
							HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,invitePlayer.getName()+Translate.text_5951);
							player.addMessageToRightBag(req2);
							
						}else if(invitePlayer.getTeamMark() == Player.TEAM_MARK_NONE){
							inviteCreateTeam(invitePlayer, player);
							
							//目地是为了看见队友
							if(player.getAroundNotifyPlayerNum() == 0){
								player.setNewlyEnterGameFlag(true);
							}
							//TODO:去掉原来为了客户端bug所修改的代码
//						AROUND_CHANGE_REQ req1 = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new Player[]{invitePlayer}, new Pet[0], new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0]);
//						player.addMessageToRightBag(req1);
//						AROUND_CHANGE_REQ req2 = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new Player[]{player}, new Pet[0], new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0]);
//						invitePlayer.addMessageToRightBag(req2);
							if(logger.isWarnEnabled())
								logger.warn("[邀请组队回复] [成功] [被邀请人接受邀请人并组成一个新的队伍] ["+player.getLogString()+"] [邀请:"+invitePlayer.getLogString()+"]");
							
							
						}else if(invitePlayer.getTeamMark() == Player.TEAM_MARK_CAPTAIN){
							Team t = invitePlayer.getTeam();
							t.addMember(player);
							
							//目地是为了看见队友
							Player ps[] = t.getMembers().toArray(new Player[0]);
							//TODO:去掉原来为了客户端bug所修改的代码
//						for(int i = 0 ; i < ps.length ; i++){
//							for(int j = 0 ; j < ps.length ; j++){
//								if (ps[i] != ps[j]) {
//									AROUND_CHANGE_REQ req1 = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new Player[]{ps[j]}, new Pet[0], new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0]);
//									ps[i].addMessageToRightBag(req1);
//								}
//							}
//						}
							
							//让客户端清空费队友
							for(int i = 0 ; i < ps.length ; i++){
								if(ps[i].getAroundNotifyPlayerNum() == 0){
									ps[i].setNewlyEnterGameFlag(true);
								}
							}
							if(logger.isWarnEnabled())
								logger.warn("[邀请组队回复] [成功] [被邀请人接受邀请人并加入队伍] ["+player.getLogString()+"] [邀请:"+invitePlayer.getLogString()+"]");
						}
					}
				}else{
					// 被邀请人拒绝加入队伍
					invitePlayer.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, player.name + Translate.text_5952));
					
					if(logger.isWarnEnabled())
						logger.warn("[邀请组队回复] [成功] [被邀请人拒绝邀请人] ["+player.getLogString()+"] [邀请:"+invitePlayer.getLogString()+"]");
				}
			}else{
					if(logger.isWarnEnabled())
						logger.warn("[邀请组队回复] [失败] [邀请人为普通队员或者被邀请人已经组队] ["+player.getLogString()+"] [邀请:"+invitePlayer.getLogString()+"]");
			}
		}else{
			if(invitePlayer != null){
					if(logger.isWarnEnabled())
						logger.warn("[邀请组队回复] [失败] [{}] [{}] [邀请人与被邀请人不是同一阵营]", new Object[]{player.getName(),invitePlayer.getName()});
			}else if(invitePlayer == null){
					if(logger.isWarnEnabled())
						logger.warn("[邀请组队回复] [失败] [{}] [--] [邀请人不存在或者不在线]", new Object[]{player.getName()});
			}
		}
		}catch(Exception e){
			logger.error("[同意或者拒绝某人异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	
	/**
	 * 解散 客户端发送给服务器只有队长才能发起此消息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_DISSOLVE_REQ(Connection conn, RequestMessage message, Player player) {

	
		if (player.getTeamMark() == Player.TEAM_MARK_CAPTAIN) {
			
			try{
//				if(JJCManager.isJJCBattle(player.getCurrentGame().gi.name)){
//					player.sendError(Translate.JJC中不能解散队伍);
//					return null;
//				}
				if(DownCityManager2.instance.inCityGame(player)){
					player.sendError(Translate.副本中不能解散队伍);
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			Team team = player.getTeam();
			
			if (team != null) {
				
				Player ps[] = team.getMembers().toArray(new Player[0]);
				team.dissolve();
				//让客户端清空费队友
				for(int i = 0 ; i < ps.length ; i++){
					if(ps[i].getAroundNotifyPlayerNum() == 0){
						ps[i].setNewlySetAroundNotifyPlayerNum(true);
					}
					
					try{
						if(ps[i] != null && ps[i].getId() != player.getId() && DownCityManager2.instance.inCityGame(ps[i])){
							player.sendError(Translate.队友副本中不能解散队伍);
							return null;
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					
				}
				if(logger.isWarnEnabled()){
					logger.warn("[解散队伍] [成功] [队长解散队伍] ["+player.getLogString()+"]");
				}
			}
			return null;
		} else {
				if(logger.isWarnEnabled())
					logger.warn("[解散队伍] [失败] [解散人不是队长] ["+player.getLogString()+"]");
			return null;
		}
	}
	
	
	
	/**
	 * 申请 玩家向队长提交入队申请 
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_APPLY_JION_REQ(Connection conn, RequestMessage message, Player player) {
		try{
			if(DownCityManager2.instance.inCityGame(player)){
				player.sendError(Translate.副本中不能解散队伍);
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		TEAM_APPLY_JION_REQ req = (TEAM_APPLY_JION_REQ) message;
		if (player.getTeamMark() != Player.TEAM_MARK_NONE) {
			if(logger.isWarnEnabled()){
				logger.warn("[申请加入队伍] [失败] [申请人已经组队] ["+player.getLogString()+"]");
			}
			player.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1, Translate.text_5953));
		} else {
			Player member = null;
			if (playerManager.isOnline(req.getCaptainId())) {
				try {
					member = playerManager.getPlayer(req.getCaptainId());
				} catch (Exception e) {
					logger.error("[组队申请] ["+player.getLogString()+"]",e);
				}
			}
			if (member != null) {
				synchronized (teamObjectKey) {
					Team team = member.getTeam();
					if (team != null) {
						if (team.isFull()) {
							player.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 1,
									member.getName() + Translate.text_5941));
						} else {
							if(SocialManager.getInstance().isBlackuser(team.getCaptain(), player.getId())){
								if(logger.isWarnEnabled()){
									logger.warn("[申请加入队伍] [失败] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"] [申请人已被队长加入黑名单]");
								}
								player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.text_5942));
								return null;
							}
							try{
								if(team.getCaptain().isInBattleField()){
									player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.申请进入队伍失败));
								}
							}catch(Exception e){
								e.printStackTrace();
							}
							try{
								if(WolfManager.getInstance().isWolfGame(team.getCaptain())){
									player.sendError(Translate.小羊活动不允许被邀请);
								}
							}catch(Exception e){
								e.printStackTrace();
								if(logger.isWarnEnabled()){
									logger.warn("[申请加入队伍] [失败:正在参加小羊活动] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"]");
								}
							}
							
							try {
								if(TransitRobberyEntityManager.getInstance().isPlayerInRobbery(team.getCaptain().getId())) {
									if(logger.isWarnEnabled()){
										logger.warn("[申请加入队伍] [失败] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"] [队长正在渡劫]");
									}
									player.addMessageToRightBag(new HINT_REQ(req.getSequnceNum(), (byte) 0, Translate.渡劫不允许组队2 ));
									return null;
								}
								if(FairyChallengeManager.getInst().isPlayerChallenging(team.getCaptain().getId())) {
									player.sendError(Translate.队长正在挑战仙尊);
									if(logger.isWarnEnabled()){
										logger.warn("[申请加入队伍] [失败] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"] [队长正在挑战仙尊]");
									}
									return null;
								}
							}catch (Exception e) {
								if(player != null && team.getCaptain() != null) {
									TransitRobberyManager.logger.error("[渡劫] [组队限制异常] [申请人:"+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"]", e);
								} else {
									TransitRobberyManager.logger.error("[渡劫] [组队限制异常] [队长:" + team.getCaptain() + "] [申请人 :" + player + "]", e);
								}
							}
							if (team.getCaptain().hiddenTeamPop) {
								player.sendError(Translate.该玩家已经屏蔽组队);
								return null;
							}
							
							if(team.getCaptain().getInteamRule() == 0){
								//自动进队
								team.addMember(player);
								//目地是为了看见队友
								Player ps[] = team.getMembers().toArray(new Player[0]);
								//让客户端清空费队友
								for(int i = 0 ; i < ps.length ; i++){
									if(ps[i].getAroundNotifyPlayerNum() == 0){
										ps[i].setNewlyEnterGameFlag(true);
									}
								}
								if(logger.isWarnEnabled()){
									logger.warn("[申请加入，自动入队] [成功] [申请加入队伍，自动入队] ["+player.getLogString()+"]");
								}
							}else{
								team.getCaptain().addMessageToRightBag(
										new TEAM_APPLY_NOTIFY_REQ(GameMessageFactory.nextSequnceNum(), player.getId(),
												player.getName()));
								if(logger.isWarnEnabled()){
									logger.warn("[申请加入] [成功] [给队长发送申请入队] ["+player.getLogString()+"] [队长:"+team.getCaptain().getLogString()+"]");
								}
							}
						}
					}
				}
			} else {
					if(logger.isWarnEnabled())
						logger.warn("[申请加入队伍] [失败] [对方不在线或不存在] ["+player.getLogString()+"] [对方id:"+req.getCaptainId()+"]");
			}
		}
		return null;
	}
	
	/**
	 * 队长同意或者拒绝某个人的申请 
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_APPLY_RESULT_REQ(Connection conn, RequestMessage message, Player player) {
		

		TEAM_APPLY_RESULT_REQ req = (TEAM_APPLY_RESULT_REQ) message;
		Player applyPlayer = null;
		if (playerManager.isOnline(req.getApplyerId())) {
			try {
				applyPlayer = playerManager.getPlayer(req.getApplyerId());
			} catch (Exception e) {
				logger.error("[队长回复] ["+player.getLogString()+"]",e);
			}
		}
		if (applyPlayer == null) {
			if(logger.isWarnEnabled()){
				logger.warn("[申请加入队伍回复] [失败] [申请人不存在或者不在线] ["+player.getLogString()+"] [申请人id:"+req.getApplyerId()+"]");
			}
		} else {
			if (player.getTeamMark() == Player.TEAM_MARK_CAPTAIN) {
				if (req.getResult() == 0) {
					// 队长同意入队申请
					synchronized(teamObjectKey) {
						Team team = player.getTeam();
						if(team.isFull()){
							if(logger.isWarnEnabled()){
								logger.warn("[申请加入队伍回复] [失败] [队伍已经满员] ["+player.getLogString()+"] ["+applyPlayer.getLogString()+"]");
							}
							player.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1, player.name + Translate.text_5941));
						} else if(applyPlayer.getTeamMark() == Player.TEAM_MARK_NONE){
							team.addMember(applyPlayer);
							
							//TODO:去掉原来为了客户端bug所修改的代码
//						//目地是为了看见队友
							Player ps[] = team.getMembers().toArray(new Player[0]);
//						for(int i = 0 ; i < ps.length ; i++){
//							for(int j = 0 ; j < ps.length ; j++){
//								if (ps[i] != ps[j]) {
//									AROUND_CHANGE_REQ req1 = new AROUND_CHANGE_REQ(GameMessageFactory.nextSequnceNum(), new Player[]{ps[j]}, new Pet[0], new Sprite[0], new Player[0], new Pet[0], new Sprite[0], new MoveTrace4Client[0], new FieldChangeEvent[0], new int[0], new Sprite[0]);
//									ps[i].addMessageToRightBag(req1);
//								}
//							}
//						}
							//让客户端清空费队友
							for(int i = 0 ; i < ps.length ; i++){
								if(ps[i].getAroundNotifyPlayerNum() == 0){
									ps[i].setNewlyEnterGameFlag(true);
								}
							}
							if(logger.isWarnEnabled()){
								logger.warn("[申请加入队伍回复] [成功] [队长同意申请人加入队伍] ["+player.getLogString()+"] [申请人:"+applyPlayer.getLogString()+"]");
							}
						}else{
							if(logger.isWarnEnabled()){
								logger.warn("[申请加入队伍回复] [失败] [申请人已经在别的队伍中] ["+player.getLogString()+"] ["+applyPlayer.getLogString()+"]");
							}
							player.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1, Translate.text_5954+applyPlayer.getName()+Translate.text_5955));
						}
					}
				} else {
					// 队长拒绝入队申请
					applyPlayer.addMessageToRightBag(new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1, player.name + Translate.text_5956));
					player.send_HINT_REQ(Translate.translateString(Translate.text_你拒绝了xx加入队伍, new String[][]{{Translate.STRING_1,applyPlayer.getName()}}));
					if(logger.isWarnEnabled()){
						logger.warn("[申请加入队伍回复] [成功] [队长拒绝申请人加入队伍] ["+player.getLogString()+"] [申请人:"+applyPlayer.getLogString()+"]");
					}
				}
			} else {
				if(logger.isWarnEnabled()){
					logger.warn("[申请加入队伍回复] [失败] [同意不是队长] ["+player.getLogString()+"] [申请:"+applyPlayer.getLogString()+"]");
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * 队员离开团队 
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_APPLY_LEAVE_REQ(Connection conn, RequestMessage message, Player player) {
		
		try{
			try{
				if(DownCityManager2.instance.inCityGame(player)){
					player.sendError(Translate.副本中不能解散队伍);
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			TEAM_APPLY_LEAVE_REQ req = (TEAM_APPLY_LEAVE_REQ) message;
			long leavePlayerId = req.getPlayerId();
			if (player.getId() == leavePlayerId) {
				// 编号相同，玩家主动申请离队
				if (player.getTeamMark() != Player.TEAM_MARK_NONE) {
					Team team = player.getTeam();
					if (team != null) {
						Player ps[] = team.getMembers().toArray(new Player[0]);
						team.removeMember(player, 0);
						//让客户端清空费队友
						for(int i = 0 ; i < ps.length ; i++){
							if(ps[i].getAroundNotifyPlayerNum() == 0){
								ps[i].setNewlySetAroundNotifyPlayerNum(true);
							}
						}
						if(logger.isWarnEnabled()){
							logger.warn("[主动离队] [成功] [玩家主动离队] ["+player.getLogString()+"]");
						}
					} else {
						if(logger.isWarnEnabled()){
							logger.warn("[主动离队] [失败] [玩家的队伍对象为null] ["+player.getLogString()+"]");
						}
					}
				} else {
					if(logger.isWarnEnabled()){
						logger.warn("[主动离队] [失败] [玩家已经不再队伍中] ["+player.getLogString()+"]");
					}
				}
			} else {
				// 编号不同，队长踢人
				if (player.getTeamMark() == Player.TEAM_MARK_CAPTAIN) {
					Team team = player.getTeam();
					if (team != null) {
						//if (playerManager.isOnline(leavePlayerId)) {
							Player p = null;
							try {
								p = playerManager.getPlayer(leavePlayerId);
							} catch (Exception e) {
								logger.error("[队员离开团队 错误] [没有这个玩家] ["+leavePlayerId+"]",e);
								return null;
							}
							team.removeMember(p, 2);
							if(logger.isWarnEnabled()){
								logger.warn("[队长踢人] [成功] [踢人成功] ["+player.getLogString()+"]");
							}
					} else {
						if(logger.isWarnEnabled()){
							logger.warn("[队长踢人] [失败] [玩家的队伍对象为null] ["+player.getLogString()+"]");
						}
					}
				} else {
					if(logger.isWarnEnabled()){
						logger.warn("[队长踢人] [失败] [玩家不是队长，无权踢人] ["+player.getLogString()+"]");
					}
				}
			}
		}catch(Exception e){
			logger.error("[队员离开队伍异常] ["+player.getLogString()+"]",e);
		}

		return null;
	}
	
	
	/**
	 * 队长设置某人为新的队长
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_SET_CAPTAIN_REQ(Connection conn, RequestMessage message, Player player) {
		
		try{
			if(DownCityManager2.instance.inCityGame(player)){
				player.sendError(Translate.副本中不能解散队伍);
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		TEAM_SET_CAPTAIN_REQ req = (TEAM_SET_CAPTAIN_REQ) message;
		if (player.getTeamMark() == Player.TEAM_MARK_CAPTAIN) {
			Team team = player.getTeam();
			if (team != null) {
				if(playerManager.isOnline(req.getNewCaptainId())){
					Player p = null;
					try {
						p = playerManager.getPlayer(req.getNewCaptainId());
					} catch (Exception e) {
						logger.error("[设置默认为新队长错误] [没有这个玩家] ["+req.getNewCaptainId()+"]",e);
						return null;
					}
					if(p.getTeam() == team){
						team.changeCaptain(p);
						if(logger.isWarnEnabled()){
							logger.warn("[更换队长] [成功] [已经更换新的队长] ["+player.getLogString()+"] [新:"+p.getLogString()+"]");
						}
						
					}else{
						if(logger.isWarnEnabled()){
							logger.warn("[更换队长] [失败] [新的队长已在别的队伍中] ["+player.getLogString()+"] [新:"+p.getLogString()+"]");
						}
					}
				}else{
					if(logger.isWarnEnabled()){
						logger.warn("[更换队长] [失败] [新的队长不在线] ["+player.getLogString()+"] [新:"+req.getNewCaptainId()+"]");
					}
				}
			}else{
				if(logger.isWarnEnabled()){
					logger.warn("[更换队长] [失败] [玩家的队伍对象为空] ["+player.getLogString()+"]");
				}
			}
		}else{
			if(logger.isWarnEnabled()){
				logger.warn("[更换队长] [失败] [玩家不是队长，无权更换队长] ["+player.getLogString()+"]");
			}
		}
		return null;
	}
	
	
	/**
	 * 队长设置团队物品分配规则 
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_SET_ASSIGN_RULE_REQ(Connection conn, RequestMessage message, Player player) {
		

		TEAM_SET_ASSIGN_RULE_REQ req = (TEAM_SET_ASSIGN_RULE_REQ)message;
		
		byte rule = req.getAssignRule();
		if(rule != 0 && rule != 1){
			if(logger.isWarnEnabled())
				logger.warn("[设置分配规则错误] ["+rule+"] []");
			return null;
		}
		
		if(player.getTeam() != null){
			//有队伍
			if(player.getTeamMark() == Player.TEAM_MARK_CAPTAIN){
				player.getTeam().setAssignRule(req.getAssignRule(),req.getColorType());
				//设置个人的分配规则
				player.setDefaultAssignRule(req.getAssignRule());
				player.send_HINT_REQ(Translate.text_设置队伍分配成功);
				TEAM_SET_ASSIGN_RULE_REQ res = new TEAM_SET_ASSIGN_RULE_REQ(GameMessageFactory.nextSequnceNum(),
						req.getAssignRule(), (byte)2);
				player.addMessageToRightBag(res);
				
				Team team = player.getTeam();
				int assign = team.getAssignRule();
				for(Player member : team.getMembers()){
					if(member != player){
						member.send_HINT_REQ(Translate.translateString(Translate.text_队长把队伍的分配规则设置为xx, new String[][]{{Translate.STRING_1,Team.RULE_NAMES[assign]}}));
					}
				}
				if(logger.isInfoEnabled()){
					logger.info("[设置物品分配规则] [成功] ["+player.getLogString()+"] [规则:"+Team.RULE_NAMES[rule]+"]");
				}
			}else{
				player.sendError(Translate.text_设置队伍分配规则错误);
				if(logger.isWarnEnabled())
					logger.warn("[设置物品分配规则] [失败] [玩家不是队长，不能设置] ["+player.getLogString()+"]");
			}
		}else{
			player.setDefaultAssignRule(req.getAssignRule());
			player.send_HINT_REQ(Translate.text_设置默认队伍分配成功);
			TEAM_SET_ASSIGN_RULE_REQ res = new TEAM_SET_ASSIGN_RULE_REQ(GameMessageFactory.nextSequnceNum(),
					req.getAssignRule(), (byte)2);
			player.addMessageToRightBag(res);
		}
		
		return null;
	}
	
	
	/**
	 * 查询玩家当前所在的团队
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TEAM_QUERY_REQ(Connection conn, RequestMessage message, Player player) {
		
		try{
			TEAM_QUERY_REQ req = (TEAM_QUERY_REQ) message;
			if (player.getTeamMark() != Player.TEAM_MARK_NONE && player.getTeam() != null) {
				
				Team team = player.getTeam();
				List<Player> li = team.getMembers();
				
				if (logger.isDebugEnabled()) {
	//				logger.warn("[查询队伍] [成功] [" + player.getName() + "] [-] [已返回队伍和队长，队友信息]");
					if(logger.isDebugEnabled())
						logger.debug("[查询队伍] [成功] [{}] [-] [已返回队伍和队长，队友信息]", new Object[]{player.getName()});
				}
				return new TEAM_QUERY_RES(req.getSequnceNum(), team.getId(), team.getCaptain().getId(),team
						.getAssignRule(),li.toArray(new Player[0]) );
			} else {
	//				logger.warn("[查询队伍] [成功] [" + player.getName() + "] [-] [玩家不在队伍中]");
					if(logger.isWarnEnabled())
						logger.warn("[查询队伍] [成功] [{}] [-] [玩家不在队伍中]", new Object[]{player.getName()});
				return new TEAM_QUERY_RES(req.getSequnceNum(), -1, -1,(byte)0,new Player[0] );
			}
		}catch(Exception e){
			logger.error("[查询玩家当前队伍异常] ["+player.getLogString()+"]",e);
		}
		return null;
	
	}
	
	
	
	/**
	 * 查询自己进队状态，队伍分配方式，附近队伍
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_PERSON_TEAM_REQ(Connection conn, RequestMessage message, Player player) {
		
		byte assignRule;
		long[] ids;
		String[] dess;
		try {
//			assignRule = TeamManager.DEFAULT_ASSIGN_RULE;
//			if(player.teamMark != Player.TEAM_MARK_NONE){
//				assignRule = player.getTeam().getAssignRule();
//			}
			
			Team team = player.getTeam();
			if(team != null){
				assignRule = team.getAssignRule();
			}else{
				assignRule = player.getDefaultAssignRule();
			}
			
			List<Team> teams = teamManager.getNearbyTeams(player);
			ids = new long[teams.size()];
			dess = new String[teams.size()];
			int i = 0;
			if(teams != null){
				for(Team t : teams){
					StringBuffer des = new StringBuffer();
					Player p = t.getCaptain();
					if(p != null){
						ids[i] = p.getId();
						
						des.append(p.getName());
						des.append("");
						des.append(p.getLevel());
						des.append(Translate.级 + " ");
						List<Player> list = t.getMembers();
						if(list != null){
							des.append("[");
							des.append(list.size());
							des.append("人] ");
							byte career = list.get(0).getCareer();
							des.append(CareerManager.careerNameByType(career));
							int j = 1;
							while(j < list.size()){
								des.append(",");
								career = list.get(j).getCareer();
								des.append(CareerManager.careerNameByType(career));
								j++;
							}
						}
						dess[i] = des.toString();
					}
					
					i++;
				}
			}
			QUERY_PERSON_TEAM_RES res = new QUERY_PERSON_TEAM_RES(message.getSequnceNum(),player.getInteamRule(),assignRule,ids,dess );
//			logger.warn("[查询自己允许进队状态，附近万家] [成功] ["+player.getName()+"] [-] []");
			if(logger.isWarnEnabled())
				logger.warn("[查询自己允许进队状态，附近万家] [成功] [{}] [-] []", new Object[]{player.getName()});
			return res;
		} catch (Exception e) {
			logger.error("[查询周围队伍] ["+player.getLogString()+"]",e);
		}
		
		return null;
	}
	
	/**
	 * 玩家设置进队状态 0 自动进队 1弹提示
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_SET_INTEAM_RULE_REQ(Connection conn, RequestMessage message, Player player) {
		
		SET_INTEAM_RULE_REQ req = (SET_INTEAM_RULE_REQ)message;
		int rule = req.getInteamRule();
		if(rule < 0 || rule > 1){
			logger.error("[设置玩家入队规则] [错误] ["+player.getName()+"] [-] ["+req.getInteamRule()+"]");
			return null;
		}
		String ruleSt= "";
		if(rule == 0){
			ruleSt = Translate.自动进队;
		}else{
			ruleSt = Translate.需要验证;
		}
		player.setInteamRule(req.getInteamRule());
		player.send_HINT_REQ(Translate.translateString(Translate.组队进队方式设置为xx, new String[][]{{Translate.STRING_1,ruleSt}}));
		if(logger.isWarnEnabled())
			logger.warn("[设置玩家入队规则] [成功] ["+player.getName()+"] [-] ["+req.getInteamRule()+"]");
		
		return null;
	}
	
	/**
	 * 通过邀请创建一个团队
	 * 
	 * @param captain
	 * @param invited
	 */
	private void inviteCreateTeam(Player captain, Player invited) {
		Team team = new Team(nextId(), captain,5);
		team.addMember(invited);
		
		//系统默认
		team.setAssignRule(captain.getDefaultAssignRule(), (byte)2);
	}
	
	
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}

	public TeamManager getTeamManager() {
		return teamManager;
	}

	public void setTeamManager(TeamManager teamManager) {
		this.teamManager = teamManager;
	}
}
