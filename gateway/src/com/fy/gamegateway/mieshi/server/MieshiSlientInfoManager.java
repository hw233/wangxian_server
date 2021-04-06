/**
 * 
 */
package com.fy.gamegateway.mieshi.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xuanzhi.tools.cache.diskcache.DiskCache;
import com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache;
import com.xuanzhi.tools.servlet.HttpUtils;


/**
 * 
 *
 */
public class MieshiSlientInfoManager {
	public static DiskCache cache = new DefaultDiskCache(new File("/home/game/resin_gateway/webapps/game_gateway/WEB-INF/diskCacheFileRoot/sliceinfo.ddc"), "沉默日志", 1000L*60*60*24*MieshiGatewaySubSystem.CACHE_DISTIME);

	public static Logger logger = Logger.getLogger(MieshiGatewaySubSystem.class);
	private static MieshiSlientInfoManager self;
	public static MieshiSlientInfoManager getInstance(){
		return self;
	}
	
	
	public void slienceAll(String username,int hour,String reason,int level,String opuser)
	{
		//查询所有的角色和服务器
		MieshiPlayerInfoManager mieshiPlayerInfoManager = MieshiPlayerInfoManager.getInstance();
		MieshiPlayerInfo[] mieshiPlayerInfos = mieshiPlayerInfoManager.getMieshiPlayerInfoByUsername(username);
		
		for(MieshiPlayerInfo mieshiPlayerInfo:mieshiPlayerInfos)
		{
			long playerId = mieshiPlayerInfo.getPlayerId();
			String servername = mieshiPlayerInfo.getServerRealName();
			
			
			ArrayList<MieshiSlientInfo> lst = (ArrayList<MieshiSlientInfo>)cache.get(username);
			if(lst == null)
			{
				lst = new ArrayList<MieshiSlientInfo>();
			}
			
			remoteSlience(servername, mieshiPlayerInfo, hour,reason,level,opuser,lst);
			cache.put(username, lst);
		}
		
	}

	public void remoteSlience(String servername,MieshiPlayerInfo mieshiPlayerInfo,int hour,String reason,int sliencelevel,String opuser,ArrayList<MieshiSlientInfo> slienceList)
	{
		MieshiServerInfo mieshiServerInfo = MieshiServerInfoManager.getInstance().getServerInfoByName(servername);
		if(mieshiServerInfo == null)
		{
			return;
		}
		
		String url = mieshiServerInfo.getServerUrl()+"/admin/yunying/op/silencePlayers.jsp";
		String authusername = "caolei";
		String authpass = "caolei890";
		//TODO:访问对应的游戏服jsp接口
		url+="?authorize.username="+authusername+"&authorize.password="+authpass;
		long playerId =  mieshiPlayerInfo.playerId;
		String content = "playerId="+playerId+"&hour="+hour+"&reason="+reason+"&level="+sliencelevel;
		Map headers = new HashMap();
		
		try {
			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes("utf-8"), headers, 60000, 60000);
			
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			content = new String(bytes,encoding);
			if(logger.isInfoEnabled())
				logger.info("[调用沉默接口] [得到响应内容] [成功] [encoding:"+encoding+"] [code:"+(code == null ?"--":code.intValue())+"] [message:"+message+"] ["+content+"] [username:"+mieshiPlayerInfo.userName+"] [url:"+url+"] ["+mieshiPlayerInfo.serverRealName+"] ["+mieshiPlayerInfo.playerName+"] ["+mieshiPlayerInfo.playerId+"]");
		} catch (Exception e) {
			logger.error("[调用沉默接口] [得到响应内容] [失败] [encoding:--] [code:--] [message:--] ["+content+"] [username:"+mieshiPlayerInfo.userName+"] [url:"+url+"] ["+mieshiPlayerInfo.serverRealName+"] ["+mieshiPlayerInfo.playerName+"] ["+mieshiPlayerInfo.playerId+"]",e);
		}
		
		if(!content.contains("success"))
		{
			logger.warn("[调用沉默接口] [沉默用户] [失败] ["+content+"] [username:"+mieshiPlayerInfo.userName+"] [url:"+url+"] ["+mieshiPlayerInfo.serverRealName+"] ["+mieshiPlayerInfo.playerName+"] ["+mieshiPlayerInfo.playerId+"]");
			return;
		}
		
		MieshiSlientInfo mieshiSlientInfo = new MieshiSlientInfo();
		mieshiSlientInfo.opuser = opuser;
		mieshiSlientInfo.optime = System.currentTimeMillis();
		mieshiSlientInfo.servername = mieshiPlayerInfo.serverRealName;
		mieshiSlientInfo.playername =  mieshiPlayerInfo.playerName;
		mieshiSlientInfo.playerId =  mieshiPlayerInfo.playerId;
		mieshiSlientInfo.slienceHour = hour;
		mieshiSlientInfo.sliencelevel = sliencelevel;
		mieshiSlientInfo.reason = reason;
		mieshiSlientInfo.username =  mieshiPlayerInfo.userName;
		
		slienceList.add(mieshiSlientInfo);
		
		logger.warn("[调用沉默接口] [沉默用户] [成功] ["+content+"] [username:"+mieshiPlayerInfo.userName+"] [url:"+url+"] ["+mieshiPlayerInfo.serverRealName+"] ["+mieshiPlayerInfo.playerName+"] ["+mieshiPlayerInfo.playerId+"]");
	}
	
	
	public void unSlienceAll(String username,int hour,String reason,int level,String opuser)
	{
		//查询所有的角色和服务器
		MieshiPlayerInfoManager mieshiPlayerInfoManager = MieshiPlayerInfoManager.getInstance();
		MieshiPlayerInfo[] mieshiPlayerInfos = mieshiPlayerInfoManager.getMieshiPlayerInfoByUsername(username);
		
		for(MieshiPlayerInfo mieshiPlayerInfo:mieshiPlayerInfos)
		{
			long playerId = mieshiPlayerInfo.getPlayerId();
			String servername = mieshiPlayerInfo.getServerRealName();
			
			ArrayList<MieshiSlientInfo> lst = (ArrayList<MieshiSlientInfo>)cache.get(username);
			
			if(lst != null)
			{
				remoteUnSlience(servername, mieshiPlayerInfo, hour,reason,level,opuser,lst);
				if(lst == null || lst.size() == 0)
				{
					cache.remove(username);
				}
			}
		}
		
	}

	public void remoteUnSlience(String servername,MieshiPlayerInfo mieshiPlayerInfo,int hour,String reason,int sliencelevel,String opuser,ArrayList<MieshiSlientInfo> slienceList)
	{
		MieshiServerInfo mieshiServerInfo = MieshiServerInfoManager.getInstance().getServerInfoByName(servername);
		String url = "http://"+mieshiServerInfo.getIp()+":"+mieshiServerInfo.getHttpPort();
		String authusername = "authorize.username";
		String authpass = "";
		//TODO:访问对应的游戏服jsp接口
		
		for(int i=slienceList.size()-1; i>=0; i--)
		{
			if(slienceList.get(i).playerId == mieshiPlayerInfo.getPlayerId())
				slienceList.remove(i);
		}
	}
	
	
	
	
	public void init(){
		self = this;
	}

}
