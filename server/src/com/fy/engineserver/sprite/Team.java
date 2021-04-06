package com.fy.engineserver.sprite;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.achievement.AchievementManager;
import com.fy.engineserver.achievement.RecordAction;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.downcity.DownCity;
import com.fy.engineserver.downcity.DownCityManager;
import com.fy.engineserver.downcity.city.CityAction;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.message.TEAM_CHANGE_CAPTAIN_REQ;
import com.fy.engineserver.message.TEAM_DISSOLVE_REQ;
import com.fy.engineserver.message.TEAM_MEMBER_JOIN_REQ;
import com.fy.engineserver.message.TEAM_MEMBER_LEAVE_REQ;
import com.fy.engineserver.message.TEAM_SET_ASSIGN_RULE_REQ;
import com.xuanzhi.tools.cache.Cacheable;

/**
 * 团队：每个团队有一个队长，同时每个团队的人数有上限 任何一个人只能在一个团队中
 * 
 * 团队存在如下操作：
 * 
 * 一个人发起邀请给另外一个人，另外一个人收到邀请请求，可以同意或者拒绝 一个人可以申请加入一个团队，团队的队长收到申请通知，队长可以同意或者拒绝 队长可以踢人，队长可以设置新的队长，队长可以设置团队分配方案，队长可以解散团队
 * 
 * 分配方案包括： 自由分配：任何人都可以捡任何物品 队伍分配：差物品自由拾取，好物品大家比点 队长分配：差物品自由拾取，好物品队长分配
 * 
 * 
 * 
 */
public class Team implements Cacheable {

	//private static final int MAX_MEMBER_NUM = 5;
	// 自由拾取
	public static final byte ASSIGN_RULE_BY_FREE = 0;

	// 顺序分配
	public static final byte ASSIGN_RULE_BY_TEAM = 1;

//	// 队长分配
	public static final byte ASSIGN_RULE_BY_CAPTAIN = 2;

	public static final String[] RULE_NAMES = new String[] { Translate.text_676, Translate.text_顺序分配};

	private int id;

	private Player captain;

	/**
	 * 分配规则：分配自由拾取，顺序分配
	 */
	private byte assignRule = 0;

	/**
	 * 在队伍分配和队长分配的情况下，用于区分物品的好坏
	 */
	private byte assignColorType = 1;

	// 此列表中包含队长
	List<Player> members = new LinkedList<Player>();

	
	private int maxNum = 5;
	
	private boolean societyFlag = false;
	
	//队伍上的副本信息
	private DownCity dc;
	
	private CityAction city;
	
	public CityAction getCity() {
		return city;
	}

	public void setCity(CityAction city) {
		this.city = city;
	}

	/**
	 * 队长在申请进入打开准备窗口时设置这个变量，当队长点击进入副本，用此变量创建副本
	 */
	public String perpareEnterDownCityName = "";
	/** 1为翅膀副本 */
	public byte prepareEnterCarbonType = -1;
	
	/**
	 * 副本相关,决定副本是什么样的模式进入
	 */
	public byte 元神模式 = 0;
	
	
	
	public void setDownCity(DownCity  dc){
		this.dc = dc;
	}
	
	public DownCity getDownCity(){
		return dc;
	}
	
	public Team(int id, Player captain,int maxNum) {
		this.id = id;
		this.captain = captain;
		this.maxNum = maxNum;
		addMember(captain);
	}

	public byte getAssignRule() {
		if(assignRule != this.getCaptain().getDefaultAssignRule()){
			setAssignRule(this.getCaptain().getDefaultAssignRule(), (byte)2);
		}
		return assignRule;
	}

	public Player getCaptain() {
		return captain;
	}
	
	/**
	 * 队伍中最多能有多少个人
	 * @return
	 */
	public int getMaxNum(){
		return maxNum;
	}
	
	/**
	 * 将队伍标记为团队
	 * 
	 * @param maxNum
	 */
	public void setSociety(boolean sf,int maxNum){
		societyFlag = sf;
		this.maxNum = maxNum;
	}
	
	/**
	 * 判断一个组队是否为团队
	 * @return
	 */
	public boolean isSocietyFlag(){
		return societyFlag;
	}
	
	public boolean isFull() {
		return members.size() >= maxNum;
	}

	/**
	 * 改变队长，并且通知所有玩家
	 * 
	 * @param p
	 */
	public void changeCaptain(Player p) {
		if (this.captain != p && members.contains(p)) {

			if (this.members.contains(this.captain)) {
				this.captain.setTeamMark(Player.TEAM_MARK_MEMBER);
			} else {
				this.captain.setTeamMark(Player.TEAM_MARK_NONE);
			}

			p.setTeamMark(Player.TEAM_MARK_CAPTAIN);
			this.captain = p;

			Iterator<Player> it = members.iterator();
			while (it.hasNext()) {
				Player pp = it.next();
				TEAM_CHANGE_CAPTAIN_REQ req = new TEAM_CHANGE_CAPTAIN_REQ(GameMessageFactory.nextSequnceNum(), captain
						.getId());
				////玩家在跨服服务器上，不通知客户端，以避免本服的队伍和跨服的队伍在客户端产生冲突
				if(pp.isInCrossServer() == false){
					pp.addMessageToRightBag(req);
				}
			}

		}
	}

	/**
	 * 物品分配规则：0标识自由分配，1标识轮流分配 通知所有的玩家
	 * 
	 * @param rule
	 */
	public void setAssignRule(byte rule, byte colorType) {
		assignRule = rule;
		assignColorType = colorType;
		Iterator<Player> it = members.iterator();
		while (it.hasNext()) {
			Player pp = it.next();
			TEAM_SET_ASSIGN_RULE_REQ req = new TEAM_SET_ASSIGN_RULE_REQ(GameMessageFactory.nextSequnceNum(),
					assignRule, assignColorType);
			
			//玩家在跨服服务器上，不通知客户端，以避免本服的队伍和跨服的队伍在客户端产生冲突
			if(pp.isInCrossServer() == false){
				pp.addMessageToRightBag(req);
			}
//			try{
//				if(JJCManager.isJJCBattle(pp.getCurrentGame().gi.name)){
//					return;
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
			
//			HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.translate.text_5933 + Team.RULE_NAMES[rule]+Translate.translate.text_5934+Gem.COLOR_TYPE_NAMES[colorType]+Translate.translate.text_5935);
			HINT_REQ req2 = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)1,Translate.text_5933 + Team.RULE_NAMES[rule]);
			pp.addMessageToRightBag(req2);
		}
	}

	/**
	 * 解散团队，通知团队中所有的玩家，离开团队
	 */
	public void dissolve() {
		Iterator<Player> it = members.iterator();
		while (it.hasNext()) {
			Player pp = it.next();
			TEAM_DISSOLVE_REQ req = new TEAM_DISSOLVE_REQ(GameMessageFactory.nextSequnceNum());
			
			//玩家在跨服服务器上，不通知客户端，以避免本服的队伍和跨服的队伍在客户端产生冲突
			if(pp.isInCrossServer() == false){
				pp.addMessageToRightBag(req);
			}

			pp.setTeamMark(Player.TEAM_MARK_NONE);
			pp.team = null;
			pp.send_HINT_REQ(Translate.队伍解散了);
		}
		captain = null;
		members.clear();
	}

	public Player getMember(long playerId) {
		Iterator<Player> it = members.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p.getId() == playerId)
				return p;
		}
		return null;
	}

	/**
	 * 增加一个队员，并且通知其他所有队员增加了一个队员， 并且通知新进入的队员，他加入了一个团队
	 * 
	 * @param joiner
	 */
	public void addMember(Player joiner) {
		if(members.contains(joiner)) {
			members.remove(joiner);
		}
		members.add(joiner);
		if (joiner == this.captain)
			joiner.setTeamMark(Player.TEAM_MARK_CAPTAIN);
		else
			joiner.setTeamMark(Player.TEAM_MARK_MEMBER);

		joiner.team = this;
		
		AchievementManager.getInstance().record(joiner, RecordAction.组队次数);
		if(AchievementManager.logger.isWarnEnabled()){
			AchievementManager.logger.warn("[成就统计组队次数] ["+joiner.getLogString()+"]");
		}
		
		TEAM_MEMBER_JOIN_REQ req = new TEAM_MEMBER_JOIN_REQ(GameMessageFactory.nextSequnceNum(), id,
				joiner.getId(), joiner.getName());
		Iterator<Player> it = members.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			//玩家在跨服服务器上，不通知客户端，以避免本服的队伍和跨服的队伍在客户端产生冲突
			if(p.isInCrossServer() == false){
				p.addMessageToRightBag(req);
			}
		}
		
		try{
			StringBuffer sb = new StringBuffer();
			sb.append(":");
			Player[] ps = this.getMembers().toArray(new Player[0]);
			for(int i=0;i<ps.length;i++){
				sb.append(ps[i].getLogString()+":");
			}
			TeamSubSystem.logger.error("[队伍变化] [增加队员] [队伍id:"+this.id+"] ["+sb.toString()+"]");
		}catch(Exception e){
			TeamSubSystem.logger.error("[队伍删除玩家异常] ["+joiner.getLogString()+"] [队长"+this.getCaptain().getLogString()+"]",e);
		}
		
		if(DownCityManager.getInstance() != null){
			DownCityManager.getInstance().玩家在开启副本入口界面时进入队伍(joiner, this);
		}
	}

	public boolean contains(Player p){
		return members.contains(p);
	}
	/**
	 * 
	 * @param p
	 * @param type
	 *            0标识主动离开，1标识下线离开，2标识队长踢人,3队伍解散
	 */
	public void removeMember(Player p, int type) {
		
		if (members.remove(p)) {
			p.setTeamMark(Player.TEAM_MARK_NONE);
			p.team = null;

			if (type != 1) {
				TEAM_MEMBER_LEAVE_REQ req = new TEAM_MEMBER_LEAVE_REQ(GameMessageFactory.nextSequnceNum(), p.getId(),
						(byte) type);
				//玩家在跨服服务器上，不通知客户端，以避免本服的队伍和跨服的队伍在客户端产生冲突
				if(p.isInCrossServer() == false){
					p.addMessageToRightBag(req);
				}
			}

			if (members.size() < 2) { // 团队解散
				dissolve();
			} else {
				Iterator<Player> it = members.iterator();
				while (it.hasNext()) {
					Player pp = it.next();
					TEAM_MEMBER_LEAVE_REQ req = new TEAM_MEMBER_LEAVE_REQ(GameMessageFactory.nextSequnceNum(), p
							.getId(), (byte) type);
					//玩家在跨服服务器上，不通知客户端，以避免本服的队伍和跨服的队伍在客户端产生冲突
					if(pp != null && pp.isInCrossServer() == false){
						pp.addMessageToRightBag(req);
					}
				}
				if (p == captain) {
					Player np = members.get(0);
					changeCaptain(np);
				}
			}
		}
		
		try{
			StringBuffer sb = new StringBuffer();
			sb.append(":");
			Player[] ps = this.getMembers().toArray(new Player[0]);
			for(int i=0;i<ps.length;i++){
				sb.append(ps[i].getLogString()+":");
			}
			TeamSubSystem.logger.error("[队伍变化] [删除队员] [队伍id:"+this.id+"] ["+sb.toString()+"]");
		}catch(Exception e){
			TeamSubSystem.logger.error("[队伍删除玩家异常] ["+p.getLogString()+"] [队长"+this.getCaptain().getLogString()+"]",e);
		}
		
		if(DownCityManager.getInstance() != null){
			DownCityManager.getInstance().玩家在开启副本入口界面时退出队伍(p, this);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Player> getMembers() {
		return members;
	}

	public byte getAssignColorType() {
		return assignColorType;
	}

	public void setAssignColorType(byte assignColorType) {
		this.assignColorType = assignColorType;
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return this.getMembers().size();
	}

	//为结义设置的状态
	public transient List<Boolean> uniteStateList;



	public List<Boolean> getUniteStateList() {
		return uniteStateList;
	}

	public void setUniteStateList(List<Boolean> uniteStateList) {
		this.uniteStateList = uniteStateList;
	}

	/**
	 * 顺序拾取 上次顺序拾取的playerId
	 */
	private transient long lastPickUpPlayerId;

	public long getLastPickUpPlayerId() {
		return lastPickUpPlayerId;
	}

	public void setLastPickUpPlayerId(long lastPickUpPlayerId) {
		this.lastPickUpPlayerId = lastPickUpPlayerId;
	}

	
	
	
}
