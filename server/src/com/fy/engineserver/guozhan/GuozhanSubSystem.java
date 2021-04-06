package com.fy.engineserver.guozhan;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.country.data.Country;
import com.fy.engineserver.country.manager.CountryManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.guozhan.exception.GuozhanAlreadyEndException;
import com.fy.engineserver.guozhan.exception.NoLeftTimeException;
import com.fy.engineserver.guozhan.exception.OperationNotPermitException;
import com.fy.engineserver.guozhan.exception.ResourceNotEnoughException;
import com.fy.engineserver.message.GUOZHAN_ADD_ORDER_REQ;
import com.fy.engineserver.message.GUOZHAN_ADD_ORDER_RES;
import com.fy.engineserver.message.GUOZHAN_CONTROL_PANNEL_NEW_REQ;
import com.fy.engineserver.message.GUOZHAN_CONTROL_PANNEL_NEW_RES;
import com.fy.engineserver.message.GUOZHAN_CONTROL_PANNEL_REQ;
import com.fy.engineserver.message.GUOZHAN_CONTROL_PANNEL_RES;
import com.fy.engineserver.message.GUOZHAN_CURE_BOSS_REQ;
import com.fy.engineserver.message.GUOZHAN_CURE_BOSS_RES;
import com.fy.engineserver.message.GUOZHAN_DEALY_REQ;
import com.fy.engineserver.message.GUOZHAN_DEALY_RES;
import com.fy.engineserver.message.GUOZHAN_DETAIL_REQ;
import com.fy.engineserver.message.GUOZHAN_DETAIL_RES;
import com.fy.engineserver.message.GUOZHAN_HISTORY_REQ;
import com.fy.engineserver.message.GUOZHAN_HISTORY_RES;
import com.fy.engineserver.message.GUOZHAN_LIST_REQ;
import com.fy.engineserver.message.GUOZHAN_LIST_RES;
import com.fy.engineserver.message.GUOZHAN_MAKE_ORDER_REQ;
import com.fy.engineserver.message.GUOZHAN_MAP_ORDER_REQ;
import com.fy.engineserver.message.GUOZHAN_MAP_ORDER_RES;
import com.fy.engineserver.message.GUOZHAN_MAP_POINT_REQ;
import com.fy.engineserver.message.GUOZHAN_MAP_POINT_RES;
import com.fy.engineserver.message.GUOZHAN_ORDERS_REQ;
import com.fy.engineserver.message.GUOZHAN_ORDERS_RES;
import com.fy.engineserver.message.GUOZHAN_PULL_PLAYER_REQ;
import com.fy.engineserver.message.GUOZHAN_REMOVE_ORDER_REQ;
import com.fy.engineserver.message.GUOZHAN_REMOVE_ORDER_RES;
import com.fy.engineserver.message.GUOZHAN_RETURN_BACK_REQ;
import com.fy.engineserver.message.GUOZHAN_STAT_REQ;
import com.fy.engineserver.message.GUOZHAN_STAT_RES;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.HINT_REQ;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.engineserver.sprite.npc.GuozhanNPC;
import com.fy.engineserver.util.ServiceStartRecord;
import com.xuanzhi.tools.text.DateUtil;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

/**
 * 协议处理子系统
 * 
 * @version 创建时间：Mar 22, 2011 11:34:17 AM
 * 
 */
public class GuozhanSubSystem extends SubSystemAdapter {

	public	static Logger logger = LoggerFactory.getLogger(GuozhanSubSystem.class.getName());

	private static GuozhanSubSystem self;

	public static GuozhanSubSystem getInstance() {
		return self;
	}

	private PlayerManager playerManager;
	private GuozhanOrganizer guozhanOrganizer;

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public GuozhanOrganizer getGuozhanOrganizer() {
		return guozhanOrganizer;
	}

	public void setGuozhanOrganizer(GuozhanOrganizer guozhanOrganizer) {
		this.guozhanOrganizer = guozhanOrganizer;
	}

	public String[] getInterestedMessageTypes() {
		return new String[] { "GUOZHAN_ORDERS_REQ", "GUOZHAN_ADD_ORDER_REQ", "GUOZHAN_REMOVE_ORDER_REQ" ,
				"GUOZHAN_CONTROL_PANNEL_REQ", "GUOZHAN_MAKE_ORDER_REQ", "GUOZHAN_CURE_BOSS_REQ", 
				"GUOZHAN_DEALY_REQ", "GUOZHAN_PULL_PLAYER_REQ", "GUOZHAN_HISTORY_REQ", "GUOZHAN_LIST_REQ",
				"GUOZHAN_STAT_REQ", "GUOZHAN_DETAIL_REQ", "GUOZHAN_MAP_POINT_REQ", "GUOZHAN_MAP_ORDER_REQ", 
				"GUOZHAN_MAP_ORDER_BROADCAST_REQ","GUOZHAN_RETURN_BACK_REQ", "GUOZHAN_CONTROL_PANNEL_NEW_REQ"};
	}

	public String getName() {
		return "GuozhanSubSystem";
	}

	public void init() throws Exception {
		
		self = this;
		ServiceStartRecord.startLog(this);
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
	 * 获得所有国战命令
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GUOZHAN_ORDERS_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_ORDERS_REQ req = (GUOZHAN_ORDERS_REQ) message;
		List<FastMessage> mlist = guozhanOrganizer.getFastMessages(player);
		String icons[] = new String[mlist.size()];
		String names[] = new String[icons.length];
		String messages[] = new String[icons.length];
		boolean removables[] = new boolean[icons.length];
		for(int i=0; i<mlist.size(); i++) {
			FastMessage fm = mlist.get(i);
			icons[i] = fm.getIcon();
			names[i] = fm.getName();
			messages[i] = fm.getMessage();
			if(i < 3) {
				removables[i] = false;
			} else {
				removables[i] = true;
			}
		}
		GUOZHAN_ORDERS_RES res = new GUOZHAN_ORDERS_RES(message.getSequnceNum(), icons, names, messages, removables);
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [len:"+icons.length+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return res;
	}
	
	/**
	 * 增加一条命令
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GUOZHAN_ADD_ORDER_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_ADD_ORDER_REQ req = (GUOZHAN_ADD_ORDER_REQ) message;
		String name = req.getName();
		String mes = req.getMessage();
		try {
			guozhanOrganizer.addFastMessage(player, name, mes);
		} catch (OperationNotPermitException e) {
			logger.error("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
		}
		
		List<FastMessage> mlist = guozhanOrganizer.getFastMessages(player);
		String icons[] = new String[mlist.size()];
		String names[] = new String[icons.length];
		String messages[] = new String[icons.length];
		boolean removables[] = new boolean[icons.length];
		for(int i=0; i<mlist.size(); i++) {
			FastMessage fm = mlist.get(i);
			icons[i] = fm.getIcon();
			names[i] = fm.getName();
			messages[i] = fm.getMessage();
			if(i < 3) {
				removables[i] = false;
			} else {
				removables[i] = true;
			}
		}
		GUOZHAN_ADD_ORDER_RES res = new GUOZHAN_ADD_ORDER_RES(message.getSequnceNum(), icons, names, messages, removables);
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [name:"+name+"] [message:"+message+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return res;
	}
	
	/**
	 * 删除一条命令
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GUOZHAN_REMOVE_ORDER_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_REMOVE_ORDER_REQ req = (GUOZHAN_REMOVE_ORDER_REQ) message;
		int index = req.getIndex();
		if(index >= 3) {
			try {
				int realIndex = index - 3;
				guozhanOrganizer.removeFastMessage(player, realIndex);
			} catch (OperationNotPermitException e) {
				logger.error("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
			}
		}
		List<FastMessage> mlist = guozhanOrganizer.getFastMessages(player);
		String icons[] = new String[mlist.size()];
		String names[] = new String[icons.length];
		String messages[] = new String[icons.length];
		boolean removables[] = new boolean[icons.length];
		for(int i=0; i<mlist.size(); i++) {
			FastMessage fm = mlist.get(i);
			icons[i] = fm.getIcon();
			names[i] = fm.getName();
			messages[i] = fm.getMessage();
			if(i < 3) {
				removables[i] = false;
			} else {
				removables[i] = true;
			}
		}
		GUOZHAN_REMOVE_ORDER_RES res = new GUOZHAN_REMOVE_ORDER_RES(message.getSequnceNum(), icons, names, messages, removables);
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [index:"+index+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return res;
	}
	
	public ResponseMessage handle_GUOZHAN_CONTROL_PANNEL_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_CONTROL_PANNEL_REQ req = (GUOZHAN_CONTROL_PANNEL_REQ) message;
		if(player.isIsGuozhan()) {
			Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
			if(guozhan != null) {
				String description = guozhanOrganizer.getConstants().控制面板说明;
				GuozhanNPC npcs[] = guozhan.getBoss();
				String bossName[] = new String[npcs.length];
				String bossIcon[] = new String[npcs.length];
				int bossHP[] = new int[npcs.length];
				boolean bossCanAddHP[] = new boolean[npcs.length];
				int bossAddHPLeftTimes[] = new int[npcs.length];
				int bossAddHPTotalTimes[] = new int[npcs.length];
				for(int i=0; i<npcs.length; i++) {
					bossName[i] = npcs[i].getName();
					bossIcon[i] = npcs[i].getIcon();
					int hp = npcs[i].getHp();
					int maxhp = npcs[i].getMaxHP();
					bossHP[i] = (int)((new Long(hp)*100)/new Long(maxhp));
					if(bossHP[i] == 0 && hp > 0) {
						bossHP[i] = 1;
					}
					if(guozhan.canCureBoss(player) && (npcs[i] == guozhan.get龙象卫长() || npcs[i] == guozhan.get龙象释帝())) {
						bossCanAddHP[i] = true;
					}
					if(player.getCountry() == guozhan.getDefender().getCountryId()) {
						bossAddHPLeftTimes[i] = guozhan.getCureCount();
						bossAddHPTotalTimes[i] = guozhanOrganizer.getConstants().国战加血次数;
					}
				}
				
				long endTime = guozhan.getEndTime();
				int leftTime = (int)(endTime-start)/1000;
				if(leftTime < 0) {
					leftTime = 0;
				}
				boolean canDelayTime = false;
				int delayLeftTimes = 0;
				int delayTotalTimes = 0;
				if(player.getCountry() == guozhan.getAttacker().getCountryId()) {
					delayLeftTimes = guozhan.getDelayedCount();
					delayTotalTimes = guozhanOrganizer.getConstants().延长国战时间次数;
				}
				if(guozhan.canDelayTime(player)) {
					canDelayTime = true;
				}
				boolean canPullPlayer = false;
				int pullLeftTimes = 0;
				int pullTotalTimes = 0;
				if(player.getCountry() == guozhan.getAttacker().getCountryId()) {
					pullLeftTimes = guozhan.getAttackerPullCount();
					pullTotalTimes = guozhanOrganizer.getConstants().国战拉人次数;
				} else {
					pullLeftTimes = guozhan.getDefenderPullCount();
					pullTotalTimes = guozhanOrganizer.getConstants().国战拉人次数;
				}
				if(guozhan.canPullPlayer(player)) {
					canPullPlayer = true;
				}
				GuozhanBornPoint bps[] = guozhan.getAttackerGuozhanBornPoint();
				String bornPointName[] = new String[bps.length];
				String bornPointBelongs[] = new String[bps.length];
				for(int i=0; i<bps.length; i++) {
					bornPointName[i] = bps[i].getName();
					bornPointBelongs[i] = CountryManager.得到国家名(bps[i].getBelongCountryId());
				}
				String orderIcons[] = new String[0];
				String orderNames[] = new String[0];
				String pubOrderNames[] = new String[0];
				String pubOrderMessage[] = new String[0];
				if(guozhan.playerCanMakeOrder(player)) {
					List<FastMessage> mlist = guozhanOrganizer.getFastMessages(player);
					orderIcons = new String[mlist.size()];
					orderNames = new String[mlist.size()];
					for(int i=0; i<mlist.size(); i++) {
						FastMessage fm = mlist.get(i);
						orderIcons[i] = fm.getIcon();
						orderNames[i] = fm.getName();
					}
				} else {
					List<FastMessage> mlist = guozhan.getPubMessages(player);
					int len = mlist.size();
					len = len>20?20:len;
					pubOrderNames = new String[len];
					pubOrderMessage = new String[len];
					int index = 0;
					for(int i=mlist.size()-1; i>=0 && index < len;  i--) {
						pubOrderNames[index] = mlist.get(i).getName();
						pubOrderMessage[index] = mlist.get(i).getMessage();
						index++;
					}
				}
				long guozhanResource = 0;
				if(player.getCountry() == guozhan.getAttacker().getCountryId()) {
					guozhanResource = guozhan.getAttacker().getGuozhanResource();
				} else {
					guozhanResource = guozhan.getDefender().getGuozhanResource();
				}
				GUOZHAN_CONTROL_PANNEL_RES res = new GUOZHAN_CONTROL_PANNEL_RES(message.getSequnceNum(), description,bossName,bossIcon,
						bossHP,bossCanAddHP, bossAddHPLeftTimes,bossAddHPTotalTimes,leftTime, canDelayTime, delayLeftTimes, delayTotalTimes, canPullPlayer, pullLeftTimes, pullTotalTimes, bornPointName, bornPointBelongs, orderIcons, orderNames,
						pubOrderNames, pubOrderMessage, guozhanResource);
				if(logger.isInfoEnabled()) 
					logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
				return res;
			}
		}
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议错误:玩家不在国战中] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	public ResponseMessage handle_GUOZHAN_CONTROL_PANNEL_NEW_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_CONTROL_PANNEL_NEW_REQ req = (GUOZHAN_CONTROL_PANNEL_NEW_REQ) message;
		if(player.isIsGuozhan()) {
			Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
			if(guozhan != null) {
				boolean attacker = true;
				if(player.getCountry() == guozhan.getDefender().getCountryId()) {
					attacker = false;
				}
				String description = guozhanOrganizer.getConstants().控制面板说明;
				description = description.replaceAll("@longxiangshidi@", guozhan.get龙象释帝().getName());
				GuozhanNPC npcs[] = guozhan.getBoss();
				String bossName[] = new String[npcs.length];
				String bossIcon[] = new String[npcs.length];
				int bossHP[] = new int[npcs.length];
				int bossStatus[] = new int[npcs.length];
				boolean bossCanAddHP[] = new boolean[npcs.length];
				int bossAddHPLeftTimes[] = new int[npcs.length];
				int bossAddHPTotalTimes[] = new int[npcs.length];
				for(int i=0; i<npcs.length; i++) {
					bossName[i] = npcs[i].getName();
					bossIcon[i] = npcs[i].getIcon();
					int hp = npcs[i].getHp();
					int maxhp = npcs[i].getMaxHP();
					bossHP[i] = (int)((new Long(hp)*100)/new Long(maxhp));
					if(bossHP[i] == 0 && hp > 0) {
						bossHP[i] = 1;
					}
					if(npcs[i] == guozhan.get龙象释帝()) {
						if(guozhan.get龙象释帝().isAlive()) {
							bossStatus[i] = 2;
						} else {
							bossStatus[i] = 3;
						}
					} else if(npcs[i] == guozhan.get龙象卫长()) {
						if(guozhan.get龙象卫长().isAlive()) {
							bossStatus[i] = 2;
						} else {
							bossStatus[i] = 3;
						}
					} else if(npcs[i] == guozhan.get王城戌卫官()) {
						if(attacker) {
							bossStatus[i] = 0;
						} else {
							bossStatus[i] = 1;
						}
					} else if(npcs[i] == guozhan.get王城戌攻官()) {
						if(attacker) {
							bossStatus[i] = 1;
						} else {
							bossStatus[i] = 0;
						}
					} else if(npcs[i] == guozhan.get昆仑要塞边城守将()) {
						if(attacker) {
							bossStatus[i] = 0;
						} else {
							bossStatus[i] = 1;
						}
					} else if(npcs[i] == guozhan.get昆仑要塞边城攻将()) {
						if(attacker) {
							bossStatus[i] = 1;
						} else {
							bossStatus[i] = 0;
						}
					} else if(npcs[i] == guozhan.get龙牙关边城守将()) {
						if(attacker) {
							bossStatus[i] = 0;
						} else {
							bossStatus[i] = 1;
						}
					} else if(npcs[i] == guozhan.get龙牙关边城攻将()) {
						if(attacker) {
							bossStatus[i] = 1;
						} else {
							bossStatus[i] = 0;
						}
					}
					if(guozhan.canCureBoss(player)) {
						bossCanAddHP[i] = true;
					}
					if(player.getCountry() == guozhan.getDefender().getCountryId()) {
						bossAddHPLeftTimes[i] = guozhan.getCureCount();
						bossAddHPTotalTimes[i] = guozhanOrganizer.getConstants().国战加血次数;
					}
				}
				
				long endTime = guozhan.getEndTime();
				int leftTime = (int)(endTime-start)/1000;
				if(leftTime < 0) {
					leftTime = 0;
				}
				boolean canDelayTime = false;
				int delayLeftTimes = 0;
				int delayTotalTimes = 0;
				if(player.getCountry() == guozhan.getAttacker().getCountryId()) {
					delayLeftTimes = guozhan.getDelayedCount();
					delayTotalTimes = guozhanOrganizer.getConstants().延长国战时间次数;
				}
				if(guozhan.canDelayTime(player)) {
					canDelayTime = true;
				}
				boolean canPullPlayer = false;
				int pullLeftTimes = 0;
				int pullTotalTimes = 0;
				if(player.getCountry() == guozhan.getAttacker().getCountryId()) {
					pullLeftTimes = guozhan.getAttackerPullCount();
					pullTotalTimes = guozhanOrganizer.getConstants().国战拉人次数;
				} else {
					pullLeftTimes = guozhan.getDefenderPullCount();
					pullTotalTimes = guozhanOrganizer.getConstants().国战拉人次数;
				}
				if(guozhan.canPullPlayer(player)) {
					canPullPlayer = true;
				}
				GuozhanBornPoint bps[] = guozhan.getAttackerGuozhanBornPoint();
				String bornPointName[] = new String[bps.length];
				String bornPointBelongs[] = new String[bps.length];
				for(int i=0; i<bps.length; i++) {
					bornPointName[i] = bps[i].getName();
					bornPointBelongs[i] = CountryManager.得到国家名(bps[i].getBelongCountryId());
				}
				String orderIcons[] = new String[0];
				String orderNames[] = new String[0];
				String pubOrderNames[] = new String[0];
				String pubOrderMessage[] = new String[0];
				if(guozhan.playerCanMakeOrder(player)) {
					List<FastMessage> mlist = guozhanOrganizer.getFastMessages(player);
					orderIcons = new String[mlist.size()];
					orderNames = new String[mlist.size()];
					for(int i=0; i<mlist.size(); i++) {
						FastMessage fm = mlist.get(i);
						orderIcons[i] = fm.getIcon();
						orderNames[i] = fm.getName();
					}
				} else {
					List<FastMessage> mlist = guozhan.getPubMessages(player);
					int len = mlist.size();
					len = len>20?20:len;
					pubOrderNames = new String[len];
					pubOrderMessage = new String[len];
					int index = 0;
					for(int i=mlist.size()-1; i>=0 && index < len;  i--) {
						pubOrderNames[index] = mlist.get(i).getName();
						pubOrderMessage[index] = mlist.get(i).getMessage();
						index++;
					}
				}
				long guozhanResource = 0;
				if(player.getCountry() == guozhan.getAttacker().getCountryId()) {
					guozhanResource = guozhan.getAttacker().getGuozhanResource();
				} else {
					guozhanResource = guozhan.getDefender().getGuozhanResource();
				}
				GUOZHAN_CONTROL_PANNEL_NEW_RES res = new GUOZHAN_CONTROL_PANNEL_NEW_RES(message.getSequnceNum(), description,bossName,bossIcon,
						bossHP,bossStatus, bossCanAddHP, bossAddHPLeftTimes,bossAddHPTotalTimes,leftTime, canDelayTime, delayLeftTimes, delayTotalTimes, canPullPlayer, pullLeftTimes, pullTotalTimes, bornPointName, bornPointBelongs, orderIcons, orderNames,
						pubOrderNames, pubOrderMessage, guozhanResource);
				if(logger.isInfoEnabled()) 
					logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
				return res;
			}
		}
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议错误:玩家不在国战中] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	public ResponseMessage handle_GUOZHAN_MAKE_ORDER_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_MAKE_ORDER_REQ req = (GUOZHAN_MAKE_ORDER_REQ) message;
		int index = req.getIndex();
		String error = null;
		if(player.isIsGuozhan()) {
			Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
			if(guozhan != null) {
				try {
					guozhan.playerMakeOrder(player, index);
					if(logger.isInfoEnabled()) 
						logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [order:"+index+"] ["+(System.currentTimeMillis()-start)+"ms]");
				} catch (OperationNotPermitException e) {
					// TODO Auto-generated catch block
					error = Translate.您没有权利发布命令;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				}
			} else {
				error = Translate.您不在国战中;
			}
		} else {
			error = Translate.您不在国战中;
		}
		if(error != null) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, error);
			player.addMessageToRightBag(hintreq);
		}
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议错误：玩家不在国战中] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [order:"+index+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	public ResponseMessage handle_GUOZHAN_CURE_BOSS_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_CURE_BOSS_REQ req = (GUOZHAN_CURE_BOSS_REQ) message;
		int index = req.getIndex();
		String error = null;
		if(player.isIsGuozhan()) {
			Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
			if(guozhan != null) {
				try {
					guozhan.cureBoss(player, index);
					GuozhanNPC npc = guozhan.getBoss()[index];
					int hp = npc.getHp();
					int maxhp = npc.getMaxHP();
					int hpPerc = (hp*100)/maxhp;
					if(hpPerc == 0 && hp > 0) {
						hpPerc = 1;
					}
					GUOZHAN_CURE_BOSS_RES res = new GUOZHAN_CURE_BOSS_RES(message.getSequnceNum(), hpPerc);
					if(logger.isInfoEnabled()) 
						logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [index:"+index+"] ["+(System.currentTimeMillis()-start)+"ms]");
					return res;
				} catch (GuozhanAlreadyEndException e) {
					// TODO Auto-generated catch block
					error = Translate.国战已结束;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (OperationNotPermitException e) {
					// TODO Auto-generated catch block
					error = Translate.您没有权利治疗;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (NoLeftTimeException e) {
					// TODO Auto-generated catch block
					error = Translate.治疗次数已用完;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (ResourceNotEnoughException e) {
					// TODO Auto-generated catch block
					error = Translate.国战资源不足;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				}
			} else {
				error = Translate.您不在国战中;
			}
		} else {
			error = Translate.您不在国战中;
		}
		if(error != null) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, error);
			player.addMessageToRightBag(hintreq);
		}
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议错误:玩家不在国战中] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	

	
	public ResponseMessage handle_GUOZHAN_DEALY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_DEALY_REQ req = (GUOZHAN_DEALY_REQ) message;
		String error = null;
		if(player.isIsGuozhan()) {
			Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
			if(guozhan != null) {
				try {
					guozhan.delayEndTime(player);
					long endTime = guozhan.getEndTime();
					int leftTime = (int)(endTime-start)/1000;
					if(leftTime < 0) {
						leftTime = 0;
					}
					GUOZHAN_DEALY_RES res = new GUOZHAN_DEALY_RES(message.getSequnceNum(), leftTime);
					if(logger.isInfoEnabled()) 
						logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
					return res;
				} catch (OperationNotPermitException e) {
					// TODO Auto-generated catch block
					error = Translate.您没有延时的权利;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (GuozhanAlreadyEndException e) {
					// TODO Auto-generated catch block
					error = Translate.国战已结束;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (ResourceNotEnoughException e) {
					// TODO Auto-generated catch block
					error = Translate.国战资源不足;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (NoLeftTimeException e) {
					// TODO Auto-generated catch block
					error = Translate.延时次数已用完;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				}
			} else {
				error = Translate.您不在国战中;
			}
		} else {
			error = Translate.您不在国战中;
		}
		if(error != null) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, error);
			player.addMessageToRightBag(hintreq);
		}
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议错误:玩家不在国战中] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	public ResponseMessage handle_GUOZHAN_PULL_PLAYER_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_PULL_PLAYER_REQ req = (GUOZHAN_PULL_PLAYER_REQ) message;
		String error = null;
		if(player.isIsGuozhan()) {
			Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
			if(guozhan != null) {
				try {
					guozhan.pullCountryCitizen(player, false);
					if(logger.isInfoEnabled()) 
						logger.info("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
					return null;
				} catch (GuozhanAlreadyEndException e) {
					// TODO Auto-generated catch block
					error = Translate.国战已结束;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (OperationNotPermitException e) {
					// TODO Auto-generated catch block
					error = Translate.您没有拉人权限;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (NoLeftTimeException e) {
					// TODO Auto-generated catch block
					error = Translate.拉人次数已用完您可以用道具行使额外拉人权利;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				} catch (ResourceNotEnoughException e) {
					// TODO Auto-generated catch block
					error = Translate.国战资源不足无法再拉人;
					logger.info("[处理协议异常] ["+req.getTypeDescription()+"] ["+player.getLogString()+"]", e);
				}
			} else {
				error = Translate.您不在国战中;
			}
		} else {
			error = Translate.您不在国战中;
		}
		if(error != null) {
			HINT_REQ hintreq = new HINT_REQ(GameMessageFactory.nextSequnceNum(), (byte) 5, error);
			player.addMessageToRightBag(hintreq);
		}
		if(logger.isInfoEnabled()) 
			logger.info("[处理协议错误:玩家不在国战中] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	/**
	 * 处理获得国战历史
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GUOZHAN_HISTORY_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_HISTORY_REQ req = (GUOZHAN_HISTORY_REQ) message;
		List<GuozhanHistory> hlist = guozhanOrganizer.getGuozhanHistoryList();
		int len = hlist.size();
		if(len > 50) {
			len = 50;
		}
		long ids[] = new long[len];
		String times[] = new String[len];
		String attackers[] = new String[len];
		String defenders[] = new String[len];
		String results[] = new String[len];
		int index = 0;
		for(int i=hlist.size()-1; i>=0 && index < 50; i--) {
			GuozhanHistory h = hlist.get(i);
			ids[index] = h.getId();
			times[index] = DateUtil.formatDate(new Date(h.getOpenTime()), Translate.text_年月日);
			attackers[index] = h.getAttackCountryName() + " " + h.getAttackKing();
			defenders[index] = h.getDefendCountryName() + " " + h.getDefendKing();
			results[index] = h.getResultDesp();
			index++;
		}
		GUOZHAN_HISTORY_RES res = new GUOZHAN_HISTORY_RES(message.getSequnceNum(), ids, times, attackers, defenders, results);
		if(logger.isDebugEnabled()) 
			logger.debug("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [hlen:"+hlist.size()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return res;
	}
	
	/**
	 * 国战查询
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GUOZHAN_LIST_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_LIST_REQ req = (GUOZHAN_LIST_REQ) message;
		List<DeclareWar> declareList = guozhanOrganizer.getDeclareList();
		String times[] = new String[declareList.size()];
		String attackers[] = new String[times.length];
		String defenders[] = new String[times.length];
		for(int i=0;  i<times.length; i++) {
			DeclareWar dw = declareList.get(i);
			times[i] = DateUtil.formatDate(new Date(dw.getStartFightTime()), Translate.text_年月日时分);
			Country c = CountryManager.getInstance().getCountryByCountryId(dw.getDeclareCountryId());
			Player p = null;
			try {
				p = PlayerManager.getInstance().getPlayer(dw.getDeclarePlayerId());
			} catch(Exception e) {}
			attackers[i] = c.getName() + (p!=null?(" "+p.getName()):"") ;
			c = CountryManager.getInstance().getCountryByCountryId(dw.getEnemyCountryId());
			defenders[i] = c.getName();
		}
		GUOZHAN_LIST_RES res = new GUOZHAN_LIST_RES(message.getSequnceNum(), times, attackers, defenders);
		if(logger.isDebugEnabled()) 
			logger.debug("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [dlist:"+declareList.size()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return res;
	}
	
	/**
	 * 国战统计
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_GUOZHAN_STAT_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_STAT_REQ req = (GUOZHAN_STAT_REQ) message;
		List<GuozhanStat> statList = guozhanOrganizer.getSortedGuozhanList();
		String countrys[] = new String[statList.size()];
		String kings[] = new String[countrys.length];
		int winNums[] = new int[countrys.length];
		int loseNums[] = new int[countrys.length];
		for(int i=0;  i<countrys.length; i++) {
			GuozhanStat stat = statList.get(i);
			countrys[i] = CountryManager.得到国家名(stat.getCountryId());
			kings[i] = "";
			try {
				kings[i] = PlayerManager.getInstance().getPlayer(CountryManager.getInstance().getCountryByCountryId(stat.getCountryId()).getKingId()).getName();
			} catch(Exception e) {
				kings[i] = Translate.暂无;
			}
			winNums[i] = stat.getWinNum();
			loseNums[i] = stat.getLoseNum();
		}
		GUOZHAN_STAT_RES res = new GUOZHAN_STAT_RES(message.getSequnceNum(), countrys, kings, winNums, loseNums);
		if(logger.isDebugEnabled()) 
			logger.debug("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [slist:"+statList.size()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return res;
	}
	
	public ResponseMessage handle_GUOZHAN_DETAIL_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_DETAIL_REQ req = (GUOZHAN_DETAIL_REQ) message;
		long id = req.getHistoryId();
		GuozhanHistory hist = guozhanOrganizer.getGuozhanHistory(id);
		if(hist != null) {
			String attackNames[] = hist.getAttackTopPlayers();
			int attackKillNums[] = hist.getAttackTopKillNums();
			String defendNames[] = hist.getDefendTopPlayers();
			int defendKillNums[] = hist.getDefendTopKillNums();
			String anames[] = new String[attackNames.length>10?10:attackNames.length];
			System.arraycopy(attackNames, 0,anames, 0, anames.length);
			int akills[] = new int[anames.length];
			System.arraycopy(attackKillNums, 0, akills, 0, anames.length);
			String dnames[] = new String[defendNames.length>10?10:defendNames.length];
			System.arraycopy(defendNames, 0, dnames, 0, dnames.length);
			int dkills[] = new int[dnames.length];
			System.arraycopy(defendKillNums, 0, dkills, 0, dnames.length);
			GUOZHAN_DETAIL_RES res = new GUOZHAN_DETAIL_RES(message.getSequnceNum(), hist.getResult()==0?hist.getAttackCountryName():hist.getDefendCountryName(), hist.getAttackCountryName() ,
					anames, akills, hist.getDefendCountryName(), dnames, dkills);
			if(logger.isDebugEnabled()) 
				logger.debug("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [id:"+id+"] ["+(System.currentTimeMillis()-start)+"ms]");
			return res;		
		}
		if(logger.isDebugEnabled()) 
			logger.debug("[处理协议错误，国战历史找不到] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] [id:"+id+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	public ResponseMessage handle_GUOZHAN_MAP_POINT_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_MAP_POINT_REQ req = (GUOZHAN_MAP_POINT_REQ) message;
		Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
		if(guozhan != null) {
			byte guozhanType = guozhan.getPlayerGuozhanType(player);
			LinkedHashMap<Integer,GuozhanMapPoint> pointMap = guozhan.getMapPointMap();
			GuozhanMapPoint points[] = pointMap.values().toArray(new GuozhanMapPoint[0]);
			GUOZHAN_MAP_POINT_RES res = new GUOZHAN_MAP_POINT_RES(message.getSequnceNum(), guozhanType, guozhan.playerCanMakeOrder(player), points, Guozhan.国战线路[0], Guozhan.国战线路[1], guozhanType==0?guozhan.getCurrentAttackerMapAimPoint():guozhan.getCurrentDefenderMapAimPoint());
			if(logger.isDebugEnabled()) 
				logger.debug("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
			return res;
		}
		if(logger.isDebugEnabled()) 
			logger.debug("[处理协议错误，玩家不在国战中] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	public ResponseMessage handle_GUOZHAN_MAP_ORDER_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_MAP_ORDER_REQ req = (GUOZHAN_MAP_ORDER_REQ) message;
		Guozhan guozhan = guozhanOrganizer.getPlayerGuozhan(player);
		if(guozhan != null) {
			int aimId = req.getAimId();
			boolean orderValid = guozhan.isPlayerOrderValid(player, aimId);
			if(orderValid) {
				guozhan.makeMapOrder(player, aimId);
				GUOZHAN_MAP_ORDER_RES res = new GUOZHAN_MAP_ORDER_RES(message.getSequnceNum(), (byte)0);
				if(logger.isDebugEnabled()) 
					logger.debug("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
				return res;
			} 
		}
		if(logger.isDebugEnabled()) 
			logger.debug("[处理协议错误，命令不合法或者其他] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		GUOZHAN_MAP_ORDER_RES res = new GUOZHAN_MAP_ORDER_RES(message.getSequnceNum(), (byte)1);
		return res;
	}
	
	public ResponseMessage handle_GUOZHAN_RETURN_BACK_REQ(Connection conn, RequestMessage message, Player player) {
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		GUOZHAN_RETURN_BACK_REQ req = (GUOZHAN_RETURN_BACK_REQ) message;
		guozhanOrganizer.playerReturnBack(player);
		if(logger.isDebugEnabled()) 
			logger.debug("[处理协议] ["+req.getTypeDescription()+"] ["+player.getLogString()+"] ["+(System.currentTimeMillis()-start)+"ms]");
		return null;
	}
	
	public boolean handleResponseMessage(Connection conn, RequestMessage req, ResponseMessage message) throws ConnectionException, Exception {
		return false;
	}
}
