package com.fy.engineserver.masterAndPrentice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.gateway.GameSubSystem;
import com.fy.engineserver.gateway.SubSystemAdapter;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_MASTERPRENTICEINFO_REQ;
import com.fy.engineserver.message.QUERY_MASTERPRENTICE_RES;
import com.fy.engineserver.message.REBEL_EVICT_REQ;
import com.fy.engineserver.message.REGESTER_CANCLE_REQ;
import com.fy.engineserver.message.TAKE_MASTER_PRNTICE_REQ;
import com.fy.engineserver.society.Relation;
import com.fy.engineserver.society.SocialManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.PlayerManager;
import com.fy.boss.authorize.model.Passport;
import com.xuanzhi.tools.transport.Connection;
import com.xuanzhi.tools.transport.ConnectionException;
import com.xuanzhi.tools.transport.RequestMessage;
import com.xuanzhi.tools.transport.ResponseMessage;

public class MasterPrenticeSubSystem extends SubSystemAdapter {
	
//	public static Logger logger = Logger.getLogger(MasterPrenticeSubSystem.class);
public	static Logger logger = LoggerFactory.getLogger(MasterPrenticeSubSystem.class);
	protected PlayerManager playerManager;
	protected MasterPrenticeManager masterPrenticeManager;
	protected SocialManager socialManager;
	
	public void setSocialManager(SocialManager socialManager) {
		this.socialManager = socialManager;
	}
	
	@Override
	public String[] getInterestedMessageTypes() {
		return new String[]{"TAKE_MASTER_PRNTICE_REQ","REBEL_EVICT_REQ","REGESTER_CANCLE_REQ",
				"QUERY_MASTERPRENTICE_REQ","QUERY_MASTERPRENTICEINFO_REQ",
				
		};
	}

	@Override
	public String getName() {
		return "MasterPrenticeSubSystem";
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
	
	/**
	 * 处理拜师或是收徒请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_TAKE_MASTER_PRNTICE_REQ(Connection conn,RequestMessage message, Player player) {
		
		TAKE_MASTER_PRNTICE_REQ req = (TAKE_MASTER_PRNTICE_REQ)message;
		
		boolean opration = req.getOpration(); // 拜师还是收徒(true)
		boolean ptype = req.getPtype(); // 根据名还是根据id请求,true根据id"
		long id = req.getId();
		String name = req.getName();
		
		if(logger.isDebugEnabled()){
			logger.debug("[拜师还是收徒(true):] ["+opration+"] [根据名还是根据id(true)请求] ["+ptype+"] ["+player.getLogString()+"] []");
		}
		try {
			if(opration){
				// 收徒
				Player prentice = null;
				if(ptype){
					prentice = playerManager.getPlayer(id);					
				}else{
					prentice = playerManager.getPlayer(name);					
				}
				
				if(prentice != null){
					String result = this.masterPrenticeManager.takePrentice(player, prentice);
					
					if(result.equals("")){
						player.sendError(Translate.text_收徒发送成功);
					}else{
						player.sendError(result);
					}
					if(logger.isWarnEnabled()){
						logger.warn("[发送收徒] ["+player.getLogString()+"] [对方:"+prentice.getLogString()+"] [result:"+result+"]");
					}
				}else{
					logger.error("[发送收徒错误] [没有这个玩家] ["+player.getLogString()+"]");
				}
			}else{
				//拜师-
				Player master = null;
				if(ptype){
					master = playerManager.getPlayer(id);
				}else{
					master = playerManager.getPlayer(name);
				}
				if(master != null){
					String result = this.masterPrenticeManager.takeMaster(player, master);
					if(result.equals("")){
						player.sendError(Translate.text_拜师发送成功);
					}else{
						player.sendError(result);
					}
					if(logger.isWarnEnabled()){
						logger.warn("[发送拜师] ["+player.getLogString()+"] [对方:"+master.getLogString()+"] [result:"+result+"]");
					}
				}else{
					logger.error("[发送拜师错误] [没有这个玩家] ["+player.getLogString()+"]");
				}
			}
		} catch (Exception e) {
			logger.error("[拜师或是收徒请求异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	/**
	 * 判师或逐徒请求
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_REBEL_EVICT_REQ(Connection conn,RequestMessage message, Player player) {
		
		REBEL_EVICT_REQ req = (REBEL_EVICT_REQ)message;
		boolean opration = req.getOpration(); // 判师或逐徒 (true)
		long id = req.getId();
		
		if(logger.isDebugEnabled()){
			logger.debug("[判师或逐徒 (true):] ["+opration+"] ["+player.getLogString()+"]");
		}
		try {
			String result = "";
			if(opration){
				//逐徒 
				Player prentice = playerManager.getPlayer(id);
				if(prentice != null){
					result = this.masterPrenticeManager.evictPrentice(player, prentice);
				}
			}else{
				// 判师
				Player master = playerManager.getPlayer(id);
				if(master != null){
					result = this.masterPrenticeManager.rebelMaster(player, master);
				}
			}
			if(!result.equals("")){
				player.sendError(result);
			}
			
			if(logger.isWarnEnabled()){
				logger.warn("[判师或逐徒 (true):] ["+opration+"] ["+player.getLogString()+"] [result:"+result+"]");
			}
		} catch (Exception e) {
			logger.error("[判师或逐徒失败] ["+player.getId()+"] ["+player.getName()+"] ["+player.getUsername()+"] []",e);
		}
		return null;
	}
	
	
	/**
	 * 处理发布(取消发布)拜师，收徒 (true)
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_REGESTER_CANCLE_REQ(Connection conn,RequestMessage message, Player player) {
		try {
			REGESTER_CANCLE_REQ req = (REGESTER_CANCLE_REQ)message;
			boolean ptype = req.getPtype(); // 发布(true)
			boolean opration = req.getOpration(); // 收徒 (true)
			
			if(logger.isDebugEnabled()){
				logger.debug("[取消还是发布(true):] ["+ptype+"] [收徒(true)还是拜师] ["+opration+"] ["+player.getLogString()+"]");
			}
			
			if(ptype){
				// 发布(true)
				String result = this.masterPrenticeManager.registerMasterOrPrentice(player, opration);
				if(result.equals("")){
					player.sendError(Translate.text_发布成功);
					if (logger.isInfoEnabled()) {
						logger.info("[发布成功] [收徒(true)还是拜师] ["+opration+"] ["+player.getLogString()+"]");
					}
				}else{
					player.sendError(result);
					logger.error("[发布失败] [收徒(true)还是拜师] ["+opration+"] ["+player.getLogString()+"] ["+result+"]");
				}
			}else{
				// 取消(true)
				String result = this.masterPrenticeManager.cancleRegister(player, opration);
				if(result.equals("")){
					player.sendError(Translate.text_取消发布成功);
					if (logger.isInfoEnabled()) {
						logger.info("[取消成功] [收徒(true)还是拜师] ["+opration+"] ["+player.getLogString()+"]");
					}
				}else{
					player.sendError(result);
					logger.error("[取消失败] [收徒(true)还是拜师] ["+opration+"] ["+player.getLogString()+"] ["+result+"]");
				}
			}
		} catch (Exception e) {
			logger.error("[发布取消拜师异常] ["+player.getLogString()+"]",e);
		}
		return null;
	}
	
	/**
	 * 查询自己的师傅或徒弟 (true)师傅,徒弟
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_MASTERPRENTICE_REQ(Connection conn,RequestMessage message, Player player) {
		
		Relation relation = socialManager.getRelationById(player.getId());
		MasterPrentice mp = relation.getMasterPrentice();
		Player p = null;
		if(mp != null){
			long masterId = mp.getMasterId();
			if(masterId != -1){
				boolean[] relations = new boolean[1];
				long[] ids = new long[1];
				String[] names = new String[1];
				byte[] careers = new byte[1];
				int[] levels = new int[1];
				if(masterId != -1){
					try {
						p = playerManager.getPlayer(masterId);
						relations[0] = true;
						ids[0] = masterId;
						names[0] = p.getName();
						careers[0] = p.getMainCareer();
						levels[0] = p.getLevel();
						QUERY_MASTERPRENTICE_RES res = new QUERY_MASTERPRENTICE_RES(GameMessageFactory.nextSequnceNum(), relations, ids, names, careers, levels);
						if(logger.isInfoEnabled()){
							logger.info("[查询自己的师徒关系成功] ["+player.getLogString()+"] [师傅id:"+masterId+"]");
						}
						return res;
					} catch (Exception e) {
						mp.setMasterId(-1);
						player.sendError(Translate.师傅不存在);
						logger.error("[查询自己的师徒关系异常] ["+player.getLogString()+"] ["+masterId+"]",e);
						return null;
					}
				}
			}else{
				List<Long> prenticeIds = mp.getPrentices();
				
				if(prenticeIds.size() > 0){
					List<Long> copy = null;
					for(long id : prenticeIds){
						try {
							playerManager.getPlayer(id);
						} catch (Exception e) {
							if(copy == null)  copy = new ArrayList<Long>(prenticeIds);
							copy.remove(id);
							logger.error("[查询徒弟异常] ["+player.getLogString()+"] ["+id+"]",e);
						}
					}
					if(copy != null){
						mp.setPrentices(copy);
						prenticeIds = copy;
					}
				}
				
				int temp = prenticeIds.size();
				boolean[] relations = new boolean[temp];
				long[] ids = new long[temp];
				String[] names = new String[temp];
				byte[] careers = new byte[temp];
				int[] levels = new int[temp];
				int i = 0;
				List<Long> copyList = null;
				for(long id : prenticeIds){
					try {
						p = playerManager.getPlayer(id);
						relations[i] = false;
						ids[i] = id;
						names[i] = p.getName();
						careers[i] = p.getMainCareer();
						levels[i] = p.getLevel();
						i++;
					} catch (Exception e) {
						if(copyList == null)
							copyList = new ArrayList<Long>(prenticeIds);
						ids[i] = -1;
						relations[i] = false;
						names[i] = "";
						careers[i] = 0;
						levels[i] = 0;
						i++;
						copyList.remove(id);
						logger.error("[查询徒弟异常] ["+player.getLogString()+"] ["+id+"]",e);
					}
				}
				if(copyList != null){
					mp.setPrentices(copyList);
				}
				if(logger.isInfoEnabled()){
					logger.info("[查询徒弟成功] ["+player.getLogString()+"]");
				}
				QUERY_MASTERPRENTICE_RES res = new QUERY_MASTERPRENTICE_RES(GameMessageFactory.nextSequnceNum(), relations, ids, names, careers, levels);
				return res;
			}
		}else{
			player.sendError(Translate.text_没有师徒关系);
			if(logger.isWarnEnabled()){
				logger.warn("[查询师傅徒弟错去] ["+player.getLogString()+"] [没有师徒关系]");
			}
		}
		return null;
	}
	
	
	
	/**
	 * 查询所有的登记的师徒信息
	 * @param conn
	 * @param message
	 * @param player
	 * @return
	 */
	public ResponseMessage handle_QUERY_MASTERPRENTICEINFO_REQ(Connection conn,RequestMessage message, Player player) {
		
		QUERY_MASTERPRENTICEINFO_REQ req = (QUERY_MASTERPRENTICEINFO_REQ)message;
		int beginNum = req.getBeginNum();
		boolean ptype = req.getPtype();
		if(logger.isDebugEnabled()){
			logger.debug("[查询所有的登记的师徒信息] [查询拜师还是(true)收徒] ["+ptype+"] ["+beginNum+"] ["+player.getLogString()+"]");
		}
		if(beginNum < 1){
			logger.error("[查询所有的登记的师徒信息错误] [查询拜师还是(true)收徒] ["+ptype+"] ["+beginNum+"] ["+player.getLogString()+"]");
			return null;
		}
		this.masterPrenticeManager.queryMessageAll(player, beginNum, ptype);
		if(logger.isInfoEnabled()){
			logger.info("[查询所有的登记的师徒信息成功] ["+player.getLogString()+"] [查询拜师还是(true)收徒] ["+ptype+"] ["+beginNum+"]");
		}
		return null;
		
	}
	
	
	
	public void setPlayerManager(PlayerManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setMasterPrenticeManager(MasterPrenticeManager masterPrenticeManager) {
		this.masterPrenticeManager = masterPrenticeManager;
	}
}
