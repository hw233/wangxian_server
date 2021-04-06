package com.fy.gamegateway.mieshi.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.cache.LruMapCache;
import com.xuanzhi.tools.simplejpa.SimpleEntityManager;
import com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory;
import com.xuanzhi.tools.text.DateUtil;
/**
 * 
 * 
 *
 */
public class MieshiPlayerInfoManager{
	
	//此日志还没有配置
	static Logger logger = Logger.getLogger(MieshiPlayerInfoManager.class);
	
	private static MieshiPlayerInfoManager self;
	public static synchronized MieshiPlayerInfoManager getInstance(){
		return self;
	}
	
	String cacheFile ;
	//DefaultDiskCache cache ;
	
	SimpleEntityManager<MieshiPlayerInfo> em;
	
	//username->playerList
	LruMapCache cache = null;
	
	public static class MieshiPlayerInfoList implements Cacheable{

		public String username;
		public List<MieshiPlayerInfo> playerInfoList = new ArrayList<MieshiPlayerInfo>();
		
		@Override
		public int getSize() {
			// TODO Auto-generated method stub
			return playerInfoList.size();
		}
	}
	
	
	public void init(){
		
		//cache = new DefaultDiskCache(new File(cacheFile),"mieshi-player-info-cache",10L*365*24*3600*1000L);
		
		//cache.setMaxDiskSize(10L*1024*1024*1024);
		//cache.setMaxMemorySize(64*1024*1024);
		//cache.setMaxElementNum(1024*1024*1024);
		
		cache = new LruMapCache(1024*1024, 7200000L,false,"MieshiPlayerInfoManager-Cache");
		em = SimpleEntityManagerFactory.getSimpleEntityManager(MieshiPlayerInfo.class);

		
		self = this;
	}
	
	public void destroy(){
		//cache.destory();
		em.destroy();
	}
	
	
//	public boolean runningForDataFromCacheToDataBase = false;
//	public long totalUserNum = 0;
//	public long currentUserNum = 0;
//	public long handledPlayerNum = 0;
//	public long totalCostTime = 0;
//	public long errorUserNum = 0;
//	public long errorPlayerNum = 0;
//	public int sleepStep = 10;
//	public Thread thread = null;
//	public String errorMessage = null;
//	public long runStartTime = 0;
//	
//	public void runForDataFromCacheToDataBase() throws Exception{
//		if(thread != null) throw new Exception("线程正在运行中.....");
//		runStartTime = System.currentTimeMillis();
//		runningForDataFromCacheToDataBase = true;
//		
//		thread = new Thread(new Runnable(){
//			public void run(){
//				try{
//					DataBlock[] dbs = cache.getDataBlocks();
//					totalUserNum = dbs.length;
//					
//					for(int i = 0 ; runningForDataFromCacheToDataBase && i < dbs.length ; i++){
//						
//						currentUserNum = i+1;
//						
//						errorMessage = "开始处理第【"+currentUserNum+"/"+totalUserNum+"】个的数据块...";
//						
//						DataBlock db = dbs[i];
//						Object data[] = null;
//						long ll = System.currentTimeMillis();
//						try{
//							 data = DiskCacheHelper.readData(cache,db);
//							 
//							 if(data != null){
//									errorMessage = "正在处理【"+data[0]+"】的数据.....";
//									ArrayList<MieshiPlayerInfo> al = (ArrayList<MieshiPlayerInfo>)data[1];
//									for(MieshiPlayerInfo p : al){
//										try{
//											String r = insertIntoDatabase(p);
//											handledPlayerNum++;
//											
//											logger.info("[从Cache中导入数据到数据库] ["+r+"] [totalNum="+totalUserNum+"] [currentNum="+currentUserNum+"] [handledPlayerNum="
//													+handledPlayerNum+"] [errorUserNum="+errorUserNum+"] [errorPlayerNum="+errorPlayerNum+"] [totalCostTime="+(totalCostTime/1000)+"s] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
//													+" ["+p.userName+"] ["+p.serverRealName+"] ["+p.playerId+"] ["+p.playerName+"] [level="+p.level+"] [rmb="+p.playerRMB+"] [vip="+p.playerVIP+"] ["+DateUtil.formatDate(new Date(p.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
//											
//										}catch(Exception e){
//											errorPlayerNum++;
//											logger.error("[从Cache中导入数据到数据库] [导入数据出现异常] [totalNum="+totalUserNum+"] [currentNum="+currentUserNum+"] [handledPlayerNum="
//													+handledPlayerNum+"] [errorUserNum="+errorUserNum+"] [errorPlayerNum="+errorPlayerNum+"] [totalCostTime="+(totalCostTime/1000)+"s] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
//													+" ["+p.userName+"] ["+p.serverRealName+"] ["+p.playerId+"] ["+p.playerName+"] [level="+p.level+"] [rmb="+p.playerRMB+"] [vip="+p.playerVIP+"] ["+DateUtil.formatDate(new Date(p.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]",e);
//											
//											
//										}
//									}
//									errorMessage = "处理【"+data[0]+"】的数据完毕,休息"+sleepStep+"毫秒...";
//							}
//							 
//						}catch(Exception e){
//							//error
//							errorUserNum++;
//							
//							errorMessage = "读取数据出现异常.....";
//							
//							logger.error("[从Cache中导入数据到数据库] [读取数据出现异常] [totalNum="+totalUserNum+"] [currentNum="+currentUserNum+"] [handledPlayerNum="
//									+handledPlayerNum+"] [errorUserNum="+errorUserNum+"] [errorPlayerNum="+errorPlayerNum+"] [totalCostTime="+(totalCostTime/1000)+"s] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
//									+" [offset="+db.offset+"] [length="+db.length+"] [hashcode="+db.hashcode+"] [--] [level=] [rmb=] [vip=] []",e);
//							
//						}
//						
//						
//						totalCostTime += System.currentTimeMillis() - ll;
//						
//						try{
//							Thread.sleep(sleepStep);
//						}catch(Exception e){
//							e.printStackTrace();
//						}
//						
//					}
//				}catch(Exception e){
//					logger.error("[从Cache中导入数据到数据库] [出现未知异常，线程停止]",e);
//				}
//				thread = null;
//			}
//		},"从Cache中导入数据到数据库");
//		thread.start();
//	}
	/**
	 * 插入数据到数据库中，结果用字符串返回。出错抛出异常
	 * @param pi
	 * @return
	 * @throws Exception
	 */
	
//	public String insertIntoDatabase(MieshiPlayerInfo pi)  throws Exception{
//		
//		List<MieshiPlayerInfo> list = em.query(MieshiPlayerInfo.class, "userName=? and serverRealName=? and playerName=?", new Object[]{pi.userName,pi.serverRealName,pi.playerName}, "lastAccessTime desc", 1, 2);
//		if(list.size() > 0){
//			MieshiPlayerInfo p = list.get(0);
//			if (p == pi) {
//				em.notifyFieldChange(p, "level");
//				em.notifyFieldChange(p, "career");
//				em.notifyFieldChange(p, "playerId");
//				em.notifyFieldChange(p, "playerRMB");
//				em.notifyFieldChange(p, "playerVIP");
//				em.notifyFieldChange(p, "lastAccessTime");
//				return "批量更新数据";
//			}else if(p.lastAccessTime < pi.lastAccessTime){
//				if(pi.career != p.career){
//					p.career = pi.career;
//					em.notifyFieldChange(p, "career");
//				}
//				if(pi.level != p.level){
//					p.level = pi.level;
//					em.notifyFieldChange(p, "level");
//				}
//				if(pi.playerId != p.playerId){
//					p.playerId = pi.playerId;
//					em.notifyFieldChange(p, "playerId");
//				}
//				if(pi.playerRMB != p.playerRMB){
//					p.playerRMB = pi.playerRMB;
//					em.notifyFieldChange(p, "playerRMB");
//				}
//				if(pi.playerVIP != p.playerVIP){
//					p.playerVIP = pi.playerVIP;
//					em.notifyFieldChange(p, "playerVIP");
//				}
//				p.lastAccessTime = pi.lastAccessTime;
//				em.notifyFieldChange(p, "lastAccessTime");
//				
//				em.save(p);
//				
//				return "立即更新数据";
//			}else{
//				return "丢弃旧数据";
//			}
//		}else{
//			long id = em.nextId();
//			pi.id = id;
//			pi.version = 0;
//			em.save(pi);
//			return "插入数据";
//		}
//	}
	
	/**
	 * 通过
	 * @param userName
	 * @return
	 */
	public MieshiPlayerInfo[] getMieshiPlayerInfoByUsername(String userName){
		MieshiPlayerInfoList plist = (MieshiPlayerInfoList)cache.get(userName);
		if(plist == null){
			MieshiPlayerInfo ps[] = getMieshiPlayerInfoByUsernameFromDB(userName);
			plist = new MieshiPlayerInfoList();
			plist.username = userName;
			for(MieshiPlayerInfo p : ps){
				plist.playerInfoList.add(p);
				if(logger.isDebugEnabled())
				{
					logger.debug("[通过用户名获得playerInfo] ["+p.serverRealName+"] ["+p.userName+"] ["+p.playerName+"] ["+p.playerId+"] ["+p.level+"]");
				}
			}
			cache.put(userName, plist);
		}
		
		return plist.playerInfoList.toArray(new MieshiPlayerInfo[0]);
	}
	
	public MieshiPlayerInfo[] getMieshiPlayerInfoByUsernameFromDB(String userName){
		List<MieshiPlayerInfo> infos = new ArrayList<MieshiPlayerInfo>();
		try {
			infos = em.query(MieshiPlayerInfo.class, " userName=?", new Object[]{userName},"lastAccessTime desc",1,1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[通过用户名获得playerInfo:发生异常] ["+userName+"]", e);
		}
		return infos.toArray(new MieshiPlayerInfo[0]);
	}
	
	/**
	 * 没有就返回 null
	 * @param userName
	 * @param serverRealName
	 * @return
	 */
	public MieshiPlayerInfo getMieshiPlayerInfoByUsername(String userName,String serverRealName){
		MieshiPlayerInfo pList[] = getMieshiPlayerInfoByUsername(userName);
		for(MieshiPlayerInfo p : pList){
			if(p.serverRealName.equals(serverRealName)){
				if(logger.isDebugEnabled())
				{
					logger.debug("[通过用户名和服务器获得playerInfo] ["+serverRealName+"] ["+p.serverRealName+"] ["+p.userName+"] ["+p.playerName+"] ["+p.playerId+"] ["+p.level+"]");
				}
				return p;
			}
		}
		return null;

	}
	
	/**
	 * 因为合服，会导致同一个服有多个角色，此时需要在同一个服中显示级别最高的角色
	 * @param userName
	 * @param serverRealName
	 * @return
	 */
	public MieshiPlayerInfo[] getMieshiPlayerInfosByUsername(String userName,String serverRealName){
		MieshiPlayerInfo pList[] = getMieshiPlayerInfoByUsername(userName);
		List<MieshiPlayerInfo> mps = new ArrayList<MieshiPlayerInfo>();
		for(MieshiPlayerInfo p : pList){
			if(p == null || p.serverRealName == null){
				if(logger.isInfoEnabled()){
					logger.info("[通过用户名和服务器获得playerInfo] [失败:有角色为null] [数量:"+(pList!=null?pList.length:"0")+"] [userName:"+userName+"] [serverRealName:"+serverRealName+"] [serverRealName:"+(p!=null?p.serverRealName:"null")+"] [playerName:"+(p!=null?p.playerName:"null")+"] [playerId:"+(p!=null?p.playerId:"null")+"] [level:"+(p!=null?p.level:"null")+"]");
				}
				continue;
			}
			if(p.serverRealName.equals(serverRealName)){
				if(logger.isDebugEnabled())
				{
					logger.debug("[通过用户名和服务器获得playerInfo] [成功] ["+serverRealName+"] ["+p.serverRealName+"] ["+p.userName+"] ["+p.playerName+"] ["+p.playerId+"] ["+p.level+"]");
				}
				mps.add(p);
			}
		}
		return mps.toArray(new MieshiPlayerInfo[0]);

	}
	
	
	public MieshiPlayerInfo getMieshiPlayerInfoByUsernameFromDB(String userName,String serverRealName){
		try {
			long ids[] = em.queryIds(MieshiPlayerInfo.class, " userName=? and serverRealName=?", new Object[]{userName, serverRealName},"lastAccessTime desc",1,2);
			if(ids.length > 0) {
				return em.find(ids[0]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[通过用户名和服务器获得playerInfo:发生异常] ["+userName+"] ["+serverRealName+"]", e);
		}
		return null;
	}
	
	/**
	 * 通过角色名反查所有账号
	 * @param playername
	 * @return
	 */
	public List<MieshiPlayerInfo> getMieshiPlayerInfoByPlayernameFromDB(String playername) {
		List<MieshiPlayerInfo> infos = new ArrayList<MieshiPlayerInfo>();
		try {
			long ids[] = em.queryIds(MieshiPlayerInfo.class, " playerName=?", new Object[]{playername});
			for(long id : ids) {
				MieshiPlayerInfo info = em.find(id);
				if(info != null) {
					infos.add(info);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[通过角色名获得playerInfo:发生异常] ["+playername+"]", e);
		}
		return infos;
	}
	
	public List<MieshiPlayerInfo> getMieshiPlayerInfoByPlayernameFromDB(long playerId) {
		List<MieshiPlayerInfo> infos = new ArrayList<MieshiPlayerInfo>();
		try {
			long ids[] = em.queryIds(MieshiPlayerInfo.class, " playerId=?", new Object[]{playerId});
			for(long id : ids) {
				MieshiPlayerInfo info = em.find(id);
				if(info != null) {
					infos.add(info);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[通过角色id获得playerInfo:发生异常] ["+playerId+"]", e);
		}
		return infos;
	}
	
	/**
	 * 通过角色名查询某个server上的player
	 * @param playername
	 * @param serverRealName
	 * @return
	 */
	public MieshiPlayerInfo getMieshiPlayerInfoByPlayernameFromDB(String playername, String serverRealName) {
		try {
			long ids[] = em.queryIds(MieshiPlayerInfo.class, " playerName=? and serverRealName=?", new Object[]{playername, serverRealName});
			if(ids.length > 0) {
				return em.find(ids[0]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("[通过角色名和服务器获得playerInfo:发生异常] ["+playername+"] ["+serverRealName+"]", e);
		}
		return null;
	}
	
	
	public void updateMieshiPlayerInfo(String userName,String serverRealName,int level,
			int career,String playerName,int action){
		
		long ll = System.currentTimeMillis();
		
		MieshiPlayerInfoList plist = (MieshiPlayerInfoList)cache.get(userName);
		if(plist == null){
			MieshiPlayerInfo ps[] = getMieshiPlayerInfoByUsernameFromDB(userName);
			plist = new MieshiPlayerInfoList();
			plist.username = userName;
			for(MieshiPlayerInfo p : ps){
				plist.playerInfoList.add(p);
			}
			cache.put(userName, plist);
		}
		
		boolean found = false;
		MieshiPlayerInfo foundInfo = null;
		for(int i = 0 ; i < plist.playerInfoList.size(); i++){
			MieshiPlayerInfo info = plist.playerInfoList.get(i);
			if(info.serverRealName.equals(serverRealName) && info.userName.equals(userName) && info.playerName.equals(playerName)){
				
				found = true;
				foundInfo = info;
				
				if(action != 4){
					info.serverRealName = serverRealName;
					if(info.career != career){
						info.career = career;
						em.notifyFieldChange(info, "career");
					}
					if(info.level != level){
						info.level = level;
						em.notifyFieldChange(info, "level");
					}
					info.playerName = playerName;
					info.lastAccessTime = System.currentTimeMillis();
					em.notifyFieldChange(info, "lastAccessTime");
					logger.info("[更新玩家数据到数据库] [更新角色数据] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
							+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
					
				}
			}
		}
		
		if (found && foundInfo != null) {
			plist.playerInfoList.remove(foundInfo);
			plist.playerInfoList.add(0, foundInfo);
		}
		
		//插入
		if(found == false && action != 4){
			
			MieshiPlayerInfo info = new MieshiPlayerInfo();
			info.serverRealName = serverRealName;
			info.career = career;
			info.level = level;
			info.playerName = playerName;
			info.userName = userName;
			info.lastAccessTime = System.currentTimeMillis();
			plist.playerInfoList.add(0, info);
		
			try{
				String r = "插入角色数据";
				
				long id = em.nextId();
				info.id = id;
				info.version = 0;
				em.save(info);
				
				logger.info("[更新玩家数据到数据库] ["+r+"] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
				
			
			
			}catch(Exception e){
				logger.error("[更新玩家数据到数据库] [导入数据出现异常] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]",e);
				
				
			}
		}
			
			
		if(foundInfo != null && action == 4){
			MieshiPlayerInfo info = foundInfo;
			plist.playerInfoList.remove(foundInfo);
			
			try{
				String r = "删除角色数据";
				
				em.remove(info);
				
				logger.info("[更新玩家数据到数据库] ["+r+"] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
				
			}catch(Exception e){
				logger.error("[更新玩家数据到数据库] [导入数据出现异常] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]",e);
				
				
			}
			
		}
	}
	
	public void updateMieshiPlayerInfo(String userName, long playerId, String serverRealName,int level,int career,String playerName, int rmb, int vipLevel,int action){
		long ll = System.currentTimeMillis();
		logger.warn("[updateMieshiPlayerInfo] [userName:"+userName+"] [playerId:"+playerId+"] [playerName:"+playerName+"] [server:"+serverRealName+"] [level:"+level+"] [career:"+career+"] [rmb:"+rmb+"] ["+vipLevel+"] ["+action+"]");
		MieshiPlayerInfoList plist = (MieshiPlayerInfoList)cache.get(userName);
		if(plist == null){
			MieshiPlayerInfo ps[] = getMieshiPlayerInfoByUsernameFromDB(userName);
			plist = new MieshiPlayerInfoList();
			plist.username = userName;
			for(MieshiPlayerInfo p : ps){
				plist.playerInfoList.add(p);
			}
			cache.put(userName, plist);
		}
		
		boolean found = false;
		MieshiPlayerInfo foundInfo = null;
		for(int i = 0 ; i < plist.playerInfoList.size(); i++){
			MieshiPlayerInfo info = plist.playerInfoList.get(i);
			if(info.serverRealName.equals(serverRealName) && info.userName.equals(userName) && info.playerName.equals(playerName)){
				
				found = true;
				foundInfo = info;
				
				if(action != 4){
					info.serverRealName = serverRealName;
					if(info.career != career){
						info.career = career;
						em.notifyFieldChange(info, "career");
					}
					if(info.level != level){
						info.level = level;
						em.notifyFieldChange(info, "level");
					}
					if(info.playerId != playerId){
						info.playerId = playerId;
						em.notifyFieldChange(info, "playerId");
					}
					if(info.playerRMB != rmb){
						info.playerRMB = rmb;
						em.notifyFieldChange(info, "playerRMB");
					}
					if(info.playerVIP != vipLevel){
						info.playerVIP = vipLevel;
						em.notifyFieldChange(info, "playerVIP");
					}
					info.playerName = playerName;
					info.lastAccessTime = System.currentTimeMillis();
					em.notifyFieldChange(info, "lastAccessTime");
					logger.info("[更新玩家数据到数据库] [更新角色数据] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
							+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
					break;
				}
			}
		}
		
		if (found && foundInfo != null) {
			plist.playerInfoList.remove(foundInfo);
			plist.playerInfoList.add(0, foundInfo);
		}
		
		//插入
		if(found == false && action != 4){
			
			MieshiPlayerInfo info = new MieshiPlayerInfo();
			info.serverRealName = serverRealName;
			info.career = career;
			info.level = level;
			info.playerName = playerName;
			info.userName = userName;
			info.playerId = playerId;
			info.playerRMB = rmb;
			info.playerVIP = vipLevel;
			info.lastAccessTime = System.currentTimeMillis();
			plist.playerInfoList.add(0, info);
		
			try{
				String r = "插入角色数据";
				
				long id = em.nextId();
				info.id = id;
				info.version = 0;
				em.save(info);
				
				logger.info("[更新玩家数据到数据库] ["+r+"] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
				try
				{
					ArrayList<MieshiSlientInfo> lst = (ArrayList<MieshiSlientInfo>)MieshiSlientInfoManager.cache.get(userName);
					
					if(lst != null && lst.size() != 0)
					{
						MieshiSlientInfo slientInfo = lst.get(lst.size()-1);
						MieshiSlientInfoManager.getInstance().remoteSlience(serverRealName, info, slientInfo.slienceHour, slientInfo.reason, level, "system_auto", lst);
					}
				}
				catch(Exception e)
				{
					MieshiSlientInfoManager.logger.error("[沉默玩家] [沉默新创建的角色] [失败] [出现未知异常] ["+userName+"] ["+serverRealName+"] ["+playerId+"] ["+playerName+"] ["+level+"]",e);
				}
			}catch(Exception e){
				logger.error("[更新玩家数据到数据库] [导入数据出现异常] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]",e);
				
				
			}
			
			
		
		}
			
			
		if(foundInfo != null && action == 4){
			MieshiPlayerInfo info = foundInfo;
			plist.playerInfoList.remove(foundInfo);
			
			try{
				String r = "删除角色数据";
				
				em.remove(info);
				
				logger.info("[更新玩家数据到数据库] ["+r+"] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
				
			}catch(Exception e){
				logger.error("[更新玩家数据到数据库] [导入数据出现异常] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]",e);
				
				
			}
			
		}
	}
	
	public void updatePlayerInfo4Hefu(String username,String oldServerRealName,String newServerRealName,long playerId,String nowPlayerName)
	{
		long ll = System.currentTimeMillis();
		
		MieshiPlayerInfoList plist = (MieshiPlayerInfoList)cache.get(username);
		if(plist == null){
			MieshiPlayerInfo ps[] = getMieshiPlayerInfoByUsernameFromDB(username);
			plist = new MieshiPlayerInfoList();
			plist.username = username;
			for(MieshiPlayerInfo p : ps){
				plist.playerInfoList.add(p);
			}
			cache.put(username, plist);
		}
		
		boolean found = false;
		MieshiPlayerInfo foundInfo = null;
		for(int i = 0 ; i < plist.playerInfoList.size(); i++){
			MieshiPlayerInfo info = plist.playerInfoList.get(i);
			if(info.serverRealName.equals(oldServerRealName) && info.userName.equals(username) && info.playerId == playerId){
				
				found = true;
				foundInfo = info;
				
				String oldPlayerName = info.playerName;
				info.serverRealName = newServerRealName;
				info.playerName = nowPlayerName;
				if(!oldServerRealName.equals(newServerRealName))
				{
					em.notifyFieldChange(info, "serverRealName");
				}
				if(!oldPlayerName.equals(nowPlayerName))
				{
					em.notifyFieldChange(info, "playerName");
				}
				logger.info("[更新玩家数据到数据库为了对应合服新信息] [更新角色数据] [cost:"+(System.currentTimeMillis()-ll)+"ms]"
						+" ["+info.userName+"] ["+oldServerRealName+"] ["+info.serverRealName+"] ["+info.playerId+"] ["+oldPlayerName+"] ["+info.playerName+"] [level="+info.level+"] [rmb="+info.playerRMB+"] [vip="+info.playerVIP+"] ["+DateUtil.formatDate(new java.util.Date(info.lastAccessTime),"yyyy-MM-dd_HH:mm:ss")+"]");
				break;
			}
		}
	}

	public String getCacheFile() {
		return cacheFile;
	}

	public void setCacheFile(String cacheFile) {
		this.cacheFile = cacheFile;
	}

}
