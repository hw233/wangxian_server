package com.fy.engineserver.society;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fy.engineserver.marriage.MarriageInfo;
import com.fy.engineserver.marriage.manager.MarriageManager;
import com.fy.engineserver.masterAndPrentice.MasterPrentice;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;
import com.fy.engineserver.unite.Unite;
import com.fy.engineserver.unite.UniteManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class Relation implements Cacheable, CacheListener{
	
	//玩家id一样
	@SimpleId
	private long id;
	@SimpleVersion
	private int version;
	
	public Relation() {
		
	}
	public Relation(long id) {
		this.id = id;
		this.friendlist =  new ArrayList<Long>();
		this.blacklist =  new ArrayList<Long>();
		this.chourenlist =  new ArrayList<Long>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	//最多100人
	@SimpleColumn(length=16000)
	private ArrayList<Long> friendlist;
	
	@SimpleColumn(length=16000)
	private ArrayList<Long> blacklist;
	
	@SimpleColumn(length=16000)
	private ArrayList<Long> chourenlist;
	
//	private ArrayList<Long> chatGrouplist;
	
	private transient LinkedList<Long> temporarylist = new LinkedList<Long>();
	
	private long uniteId = -1;
	private long marriageId = -1;						//结婚info ID
	
	//发布的拜师收徒登记记录id
	
	private long masterPrenticeInfoId = -1;
	public long getMasterPrenticeInfoId() {
		return masterPrenticeInfoId;
	}

	public void setMasterPrenticeInfoId(long masterPrenticeInfoId) {
		this.masterPrenticeInfoId = masterPrenticeInfoId;
		SocialManager.em.notifyFieldChange(this, "masterPrenticeInfoId");
		
	}
	
	private MasterPrentice masterPrentice = new MasterPrentice();
	
	
	public MasterPrentice getMasterPrentice() {
		return masterPrentice;
	}

	public void setMasterPrentice(MasterPrentice masterPrentice) {
		this.masterPrentice = masterPrentice;
		SocialManager.em.notifyFieldChange(this, "masterPrentice");
	}

	// 夫妻name
	@SimpleColumn(length=20)
	private String coupleName = null;

	public String getCoupleName() {
		return coupleName;
	}

	public void setCoupleName(String coupleName) {
		this.coupleName = coupleName;
	}
	
	// 上下线需要通知的人
	@SimpleColumn(length=16000)
	private List<Long> noticePlayerId = new ArrayList<Long>();
	
	private transient List<Long> online ;
	private transient List<Long> offline ;

	private transient List<Long> temporarylistOnline = new ArrayList<Long>();
	private transient List<Long> temporarylistOffline = new ArrayList<Long>();
	private transient List<Long> blackNameListOnline = new ArrayList<Long>();
	private transient List<Long> blackNameListOffline = new ArrayList<Long>();
	private transient List<Long> chourenNameListOnline = new ArrayList<Long>();
	private transient List<Long> chourenNameListOffline = new ArrayList<Long>() ;

	public transient long updateBlackListTime;
	// 添加删除黑名单会变   (true 会重新排列）
	private transient boolean updateBlackListKey;
	public transient long updateChourenListTime;
	// 添加删除仇人会变(true 会重新排列）
	private transient boolean updateChourenListKey;
	public transient long updateTemporarylistTime;
	// 添加临时好友会变(true 会重新排列）
	private transient boolean updateTemporarylistKey;
	/**
	 * 查看黑名单时刷新
	 */
	public void resortBlackList(){
		if(updateBlackListKey || updateBlackListTime == 0 || updateBlackListTime <= com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
			updateBlackListTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + SocialManager.QUERY_INTERVEL_TIME;
			updateBlackListKey = false;
//			Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
			List<Long> tempListOnline = new ArrayList<Long>();
			List<Long> tempListOffline = new ArrayList<Long>();
//			boolean tempExist = false;
			for(Long id : this.blacklist){
				if (GamePlayerManager.getInstance().isOnline(id)) {
					tempListOnline.add(id);
				} else {
					tempListOffline.add(id);
				}
				/*for(Player p : ps){
					if(p.getId() == id){
						tempExist = true;
						break;
					}
				}
				if(tempExist){
					tempExist = false;
					tempListOnline.add(id);
				}else{
					tempListOffline.add(id);
				}*/
			}
			this.blackNameListOnline = tempListOnline;
			this.blackNameListOffline = tempListOffline;
			
		}
	}
	
	/**
	 * 查看仇人时刷新
	 */
	public void resortChourenList(){
		
		if(updateChourenListKey || updateChourenListTime == 0 || updateChourenListTime <= com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
			updateChourenListKey = false;
			updateChourenListTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + SocialManager.QUERY_INTERVEL_TIME;
//			Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
			List<Long> tempListOnline = new ArrayList<Long>();
			List<Long> tempListOffline = new ArrayList<Long>();
//			boolean tempExist = false;
			for(Long id : this.chourenlist){
				if (GamePlayerManager.getInstance().isOnline(id)) {
					tempListOnline.add(id);
				} else {
					tempListOffline.add(id);
				}
				/*for(Player p : ps){
					if(p.getId() == id){
						tempExist = true;
						break;
					}
				}
				if(tempExist){
					tempExist = false;
					tempListOnline.add(id);
				}else{
					tempListOffline.add(id);
				}*/
			}
			this.chourenNameListOnline = tempListOnline;
			this.chourenNameListOffline = tempListOffline;
		}
	}
	
	/**
	 * 查看临时好友时刷新
	 */
	public void resorttemporarylist(){
		
		if(updateTemporarylistKey || updateTemporarylistTime == 0 || updateTemporarylistTime <= com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
			updateTemporarylistKey = false;
			updateTemporarylistTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + SocialManager.QUERY_INTERVEL_TIME;
//			Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
			List<Long> tempListOnline = new ArrayList<Long>();
			List<Long> tempListOffline = new ArrayList<Long>();
//			boolean tempExist = false;
			if(this.temporarylist == null) this.temporarylist = new LinkedList<Long>();
			for(Long id : this.temporarylist){
				if (GamePlayerManager.getInstance().isOnline(id)) {
					tempListOnline.add(id);
				} else {
					tempListOffline.add(id);
				}
				/*for(Player p : ps){
					if(p.getId() == id){
						tempExist = true;
						break;
					}
				}
				if(tempExist){
					tempExist = false;
					tempListOnline.add(id);
				}else{
					tempListOffline.add(id);
				}*/
			}
			this.temporarylistOnline = tempListOnline;
			this.temporarylistOffline = tempListOffline;
		}
		
	}
	
	
	public List<Long> getOnline() {
		return online;
	}

	public void setOnline(List<Long> online) {
		this.online = online;
	}

	public List<Long> getOffline() {
		return offline;
	}

	public void setOffline(List<Long> offline) {
		this.offline = offline;
	}
	private transient long updateFriendListKey;
	/**
	 * 0 好友 1结义 2 师 3徒 4 夫妻  -1 没有关系
	 * @return
	 */
	public void reSortFriendList(){
		if( updateRelationKey || updateFriendListKey == 0 || updateFriendListKey < com.fy.engineserver.gametime.SystemTime.currentTimeMillis()){
			updateFriendListKey = com.fy.engineserver.gametime.SystemTime.currentTimeMillis() + SocialManager.QUERY_INTERVEL_TIME;
			ArrayList<Long> list = new ArrayList<Long>();
			if(updateRelationKey){
				updateRelationKey = false;
				//重新排列好友列表
//				if(marriageId != -1){
//					list.add(marriageId);
//				}
				if(this.getMasterPrentice().getMasterId() != -1)
					if(!list.contains(this.getMasterPrentice().getMasterId()))
						list.add(this.getMasterPrentice().getMasterId());
				for(long id : this.getMasterPrentice().getPrentices()){
					if(!list.contains(id))
						list.add(id);
				}
				if(this.getUniteId() != -1){
					Unite u = UniteManager.getInstance().getUnite(this.getUniteId());
					if(u != null){
						for(long id :u.getMemberIds()){
							if(!list.contains(id) && id != this.id)
								list.add(id);
						}
					}
				}
				for(long fid : friendlist){
					if(!list.contains(fid))
					list.add(fid);
				}
				setFriendlist(list);
			}
			Object[] o = PlayerManager.getInstance().getOnlinePlayers();
			List<Long> onLine = new ArrayList<Long>();
			List<Long> offLine = new ArrayList<Long>();
			
			for(long idd : friendlist){
				boolean exist = false;
				for(Object o1 : o){
					if(((Player)o1).getId() == idd) {
						exist = true;
						break;
					}
				}
				if(!exist){
					try {
//						Player p1 = PlayerManager.getInstance().getPlayer(idd);
						offLine.add(idd);
					} catch (Exception e) {
						SocialManager.logger.error("[查看好友异常] ["+this.getId()+"]",e);
					}
					
				}else{
					onLine.add(idd);
				}
			}
			this.online = onLine;
			this.offline = offLine;
		}
	}	
	/**
	 * 添加好友
	 * @param id
	 */
	public void addPlayer(long id){
		friendlist.add(id);
		setFriendlist(friendlist);
		this.updateRelationKey = true;
	}
	
	/**
	 * 删除好友
	 * @param id
	 */
	public void removePlayer(long id){
		
		// 需要更改通知列表
		Relation other = SocialManager.getInstance().getRelationById(id);
		if(other != null){
			List<Long> list = other.getNoticePlayerId();
			if(list.contains(this.getId())){
				list.remove(this.getId());
				other.setNoticePlayerId(list);
					if (SocialManager.logger.isWarnEnabled()) {
						SocialManager.logger.warn("[删除好友删除对方的上下线通知列表成功] [自己id:"+this.getId()+"] [对方id："+id+"] []");
					}
			}else{
				SocialManager.logger.error("[删除好友删除对方的上下线通知列表错误] [通知列表中不包含自己] [自己id:"+this.getId()+"] [对方id："+id+"] []");
			}
		}
		
		friendlist.remove(id);
		setFriendlist(friendlist);
		this.updateRelationKey = true;
	}
	
	/**
	 * 检查好友关系列表中有无这个玩家
	 * @param id
	 * @return
	 */
	public boolean checkFriend(long id){
		return this.friendlist.contains(id);
	}
	
	public boolean checkTempFriend(long id) {
		return this.temporarylist.contains(id);
	}
	
	/**
	 * 添加黑名单
	 * @param id
	 */
	public void addBlack(long id){
		
		// 需要更改通知列表
		Relation other = SocialManager.getInstance().getRelationById(id);
		if(other != null){
			List<Long> list = other.getNoticePlayerId();
			if(list.contains(this.getId())){
				list.remove(this.getId());
				other.setNoticePlayerId(list);
					if (SocialManager.logger.isWarnEnabled()){
						SocialManager.logger.warn("[添加黑名单删除对方的上下线通知列表成功] [自己id:"+this.getId()+"] [对方id："+id+"] []");
					}
			}else{
				SocialManager.logger.error("[添加黑名单删除对方的上下线通知列表错误] [通知列表中不包含自己] [自己id:"+this.getId()+"] [对方id："+id+"] []");
			}
		}
		
		blacklist.add(id);
		setBlacklist(blacklist);
		this.updateBlackListKey = true;
	}
	
	/**
	 * 删除黑名单
	 * @param id
	 */
	public void removeBlack(long id){
		blacklist.remove(id);
		setBlacklist(blacklist);
		this.updateBlackListKey = true;
	}
	
	/**
	 * 检查黑名单列表中有无这个玩家
	 * @param id
	 * @return
	 */
	public boolean checkBlack(long id){
		return this.blacklist.contains(id);
	}
	
	
	
	/**
	 * 添加仇人
	 * @param id
	 */
	public void addChouren(long id){
		chourenlist.add(id);
		setChourenlist(chourenlist);
		this.updateChourenListKey = true;
	}
	
	/**
	 * 删除仇人
	 * @param id
	 */
	public void removeChouren(long id){
		chourenlist.remove(id);
		setChourenlist(chourenlist);
		this.updateChourenListKey = true;
	}
	
	/**
	 * 检查仇人列表中有无这个玩家
	 * @param id
	 * @return
	 */
	public boolean checkChouren(long id){
		return this.chourenlist.contains(id);
	}
	
	
	
	/**
	 * 添加临时好友
	 * @param id
	 */
	public void addTemporary(long id){
		temporarylist.add(id);
		this.updateTemporarylistKey = true;
	}
	/**
	 * 清空临时好友
	 */
	public void clearTemporaryFriend() {
		temporarylist.clear();
		this.updateTemporarylistKey = true;
	}
	
	
	
	/**
	 * 删除临时好友
	 * @param id
	 */
	public void removeTemporary(long id){
		temporarylist.remove(id);
		this.updateTemporarylistKey = true;
	}
	
	/**
	 * 检查临时好友中有无这个玩家
	 * @param id
	 * @return
	 */
	public boolean checkTemporary(long id){
		return this.temporarylist.contains(id);
	}
	
	
	
//	/**
//	 * 添加群组
//	 * @param id
//	 */
//	public void addChatGroup(long id){
//		chatGrouplist.add(id);
//		this.dirty = true;
//	}
//	
//	/**
//	 * 删除群组
//	 * @param id
//	 */
//	public void removeChatGroup(long id){
//		chatGrouplist.remove(id);
//		this.dirty = true;
//	}
	
	/**
	 * 检查群组列表中有无这个群组
	 * @param id
	 * @return
	 */
//	public boolean checkChatGroup(long id){
//		return this.chatGrouplist.contains(id);
//	}
	
	
	public long getUniteId() {
		return uniteId;
	}

	public void setUniteId(long uniteId) {
		this.uniteId = uniteId;
		SocialManager.em.notifyFieldChange(this, "uniteId");
		this.updateRelationKey = true;
	}
	
	public ArrayList<Long> getFriendlist() {
		return friendlist;
	}

	public void setFriendlist(ArrayList<Long> friendlist) {
		this.friendlist = friendlist;
		SocialManager.em.notifyFieldChange(this, "friendlist");
	}

	public ArrayList<Long> getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(ArrayList<Long> blacklist) {
		this.blacklist = blacklist;
		SocialManager.em.notifyFieldChange(this, "blacklist");
	}

	public ArrayList<Long> getChourenlist() {
		return chourenlist;
	}

	public void setChourenlist(ArrayList<Long> chourenlist) {
		this.chourenlist = chourenlist;
		SocialManager.em.notifyFieldChange(this, "chourenlist");
	}
	
	
	public LinkedList<Long> getTemporarylist() {
		if(temporarylist == null) temporarylist = new LinkedList<Long>();
		return temporarylist;
	}

	public void setTemporarylist(LinkedList<Long> temporarylist) {
		this.temporarylist = temporarylist;
	}

	private long lastUpdateTime = 0;
	
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	
	public String getLogString(){
		return "{id:" + id + "}";
	}
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public void remove(int type) {
		try {
			SocialManager.em.save(this);
		} catch (Exception e) {
			SocialManager.logger.error("[从缓存删除异常]",e);
		}
	}

	public void setMarriageId(long marriageId) {
		try {
			Player player = PlayerManager.getInstance().getPlayer(getId());
			MarriageInfo info = MarriageManager.getInstance().getMarriageInfoById(marriageId);
			if (info != null) {
				if (info.getHoldA() == getId()) {
					Player other = PlayerManager.getInstance().getPlayer(info.getHoldB());
					player.setSpouse(other.getName());
				}else if (info.getHoldB() == getId()) {
					Player other = PlayerManager.getInstance().getPlayer(info.getHoldA());
					player.setSpouse(other.getName());
				}
			}else{
				player.setSpouse("");
			}
		} catch (Exception e) {
			MarriageManager.logger.error("设置Relation的结婚id:", e);
			return;
		}
		this.marriageId = marriageId;
		SocialManager.em.notifyFieldChange(this, "marriageId");
		this.updateRelationKey = true;
	}

	public long getMarriageId() {
		return marriageId;
	}
	
	// true查询好友信息的时候就需要重新排列好友顺序
	private transient boolean updateRelationKey = true;

	public boolean isUpdateRelationKey() {
		return updateRelationKey;
	}

	public void setUpdateRelationKey(boolean updateRelationKey) {
		this.updateRelationKey = updateRelationKey;
	}
	
	
	/**
	 * 背叛师傅
	 */
	public void rebelMaster(){
		
		this.masterPrentice.rebelMaster();
		SocialManager.em.notifyFieldChange(this, "masterPrentice");
		this.setUpdateRelationKey(true);
	}
	
	/**
	 * 背叛师傅后师傅自动删除徒弟
	 * @param id
	 */
	public void autoRemovePrentice(long id){
		this.masterPrentice.autoRemovePrentice(id);
		SocialManager.em.notifyFieldChange(this, "masterPrentice");
		this.setUpdateRelationKey(true);
	}
	
	/**
	 * 逐出徒弟
	 * @param id
	 */
	public void evictPrentice(long id){
		this.masterPrentice.evictPrentice(id);
		SocialManager.em.notifyFieldChange(this, "masterPrentice");
		this.setUpdateRelationKey(true);
	}
	
	/**
	 * 逐出徒弟后徒弟自动删除师傅
	 */
	public void autoRemoveMaster(){
		masterPrentice.autoRemoveMaster();
		SocialManager.em.notifyFieldChange(this, "masterPrentice");
		this.setUpdateRelationKey(true);
	}
	
	
	/**
	 * 收徒
	 * @param id
	 */
	public void takePrentice(long id){
		masterPrentice.takePrentice(id);
		SocialManager.em.notifyFieldChange(this, "masterPrentice");
		this.setUpdateRelationKey(true);
	}
	
	/**
	 * 拜师
	 * @param id
	 */
	public void takeMaster(long id){
		masterPrentice.takeMaster(id);
		SocialManager.em.notifyFieldChange(this, "masterPrentice");
		this.setUpdateRelationKey(true);
	}

	public List<Long> getTemporarylistOnline() {
		return temporarylistOnline;
	}

	public void setTemporarylistOnline(List<Long> temporarylistOnline) {
		this.temporarylistOnline = temporarylistOnline;
	}

	public List<Long> getTemporarylistOffline() {
		return temporarylistOffline;
	}

	public void setTemporarylistOffline(List<Long> temporarylistOffline) {
		this.temporarylistOffline = temporarylistOffline;
	}

	public List<Long> getBlackNameListOnline() {
		return blackNameListOnline;
	}

	public void setBlackNameListOnline(List<Long> blackNameListOnline) {
		this.blackNameListOnline = blackNameListOnline;
	}

	public List<Long> getBlackNameListOffline() {
		return blackNameListOffline;
	}

	public void setBlackNameListOffline(List<Long> blackNameListOffline) {
		this.blackNameListOffline = blackNameListOffline;
	}

	public List<Long> getChourenNameListOnline() {
		return chourenNameListOnline;
	}

	public void setChourenNameListOnline(List<Long> chourenNameListOnline) {
		this.chourenNameListOnline = chourenNameListOnline;
	}

	public List<Long> getChourenNameListOffline() {
		return chourenNameListOffline;
	}

	public void setChourenNameListOffline(List<Long> chourenNameListOffline) {
		this.chourenNameListOffline = chourenNameListOffline;
	}

	public List<Long> getNoticePlayerId() {
		return noticePlayerId;
	}

	public void setNoticePlayerId(List<Long> noticePlayerId) {
		this.noticePlayerId = noticePlayerId;
		SocialManager.em.notifyFieldChange(this, "noticePlayerId");
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
	
}
