/**
 * 
 */
package com.fy.boss.platform.migu;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.tools.JacksonManager;
import com.fy.boss.transport.BossServerService;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.StringUtil;

public class MiguWorker {

	public static Logger logger = BossServerService.logger;

	public static boolean isDebug =  false;
	
	public static boolean openRoleExchange = true;
	
	public static String[] applyMiguToken(String[] infos)
	{
		//TODO 加日志
		infos = buildRequestTokenParams(infos);
		
		String appkey = infos[1];
		String service = infos[2];
		String submittime = infos[3];
		String sign = infos[4];
		String useraccount = infos[5];
		String groupid = infos[6];
		String groupname = infos[7];
		String serverid = infos[8];
		String servername = infos[9];
		String roleid = infos[10];
		String rolename = infos[11];
		String rolelevel = infos[12];
		String rolesex = infos[13];
		String rolevocation = infos[14];
		String platform = infos[15];
		String channel = infos[16];
		String mac = infos[17];
		String aid = infos[18];
		String entertime = infos[19];
		String clientid = infos[20];
		
		
		String content="";
		try {
			content = "appkey=" + appkey + "&service=" + service + "&submittime=" + submittime + "&sign=" + sign + "&useraccount=" + useraccount +"&servername="+servername+"&roleid="+roleid+"&rolename="+URLEncoder.encode(rolename,"utf-8")+"&rolelevel="+rolelevel+"&rolesex="+rolesex
					+"&rolevocation="+rolevocation+"&platform="+platform+"&channel="+channel+"&mac="+mac+"&aid="+aid+"&entertime="+entertime + "&clientid="+clientid;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		String url = "http://client.migu99.com/migu/api/v1/wangxian/user/open";
		
		if(isDebug)
		{
			url = "http://58.210.12.90:12587/platform-gamecenter/migu/api/v1/wangxian/user/open";
		}
		
		
		HashMap headers = new HashMap();
		try {

			byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes("utf-8"), headers, 60000, 60000);
			String encoding = (String)headers.get(HttpUtils.ENCODING);
			Integer code = (Integer)headers.get(HttpUtils.Response_Code);
			String message = (String)headers.get(HttpUtils.Response_Message);
			String result = new String(bytes,encoding);
			JacksonManager jm = JacksonManager.getInstance();
			Map dataMap = (Map)jm.jsonDecodeObject(result);
			String[] strs = new String[dataMap.size()];
			if(logger.isInfoEnabled())
			{
				logger.info("[米谷通讯] [获取token] ["+result+"] ["+content+"] ["+code+"] ["+message+"] ["+url+"]");
			}
			strs[0] = (String)dataMap.get("appkey");
			strs[1] = (String)dataMap.get("result");
			if("1".equalsIgnoreCase(strs[1]))
			{
				
				strs[2] = (String)dataMap.get("service");
				strs[3] = (String)dataMap.get("sign");
				strs[4] = (String)dataMap.get("submittime");
				strs[5] = (String)dataMap.get("token");
				if(logger.isInfoEnabled())
				{
					logger.info("[米谷通讯] [获取token] [成功] [ok] ["+result+"] ["+content+"] ["+code+"] ["+message+"] ["+url+"]");
				}
				
				String devicecode = "";
				
				if(!StringUtils.isEmpty(mac) && !"02:00:00:00:00:00".equals(mac))
				{
					devicecode = mac;
				}
				else if(!StringUtils.isEmpty(aid))
				{
					devicecode = aid;
				}
				
				addCache(useraccount, roleid, servername, devicecode, "gettoken",entertime);
			}
			else
			{
				logger.warn("[米谷通讯] [获取token] [失败] [fail] [返回错误结果集] ["+result+"] ["+content+"] ["+code+"] ["+message+"] ["+url+"]");
				strs[2] = (String)dataMap.get("service");
				strs[3] = (String)dataMap.get("sign");
				strs[4] = (String)dataMap.get("submittime");
			}
			
			return strs;
			
		} catch (Exception e) {
				logger.error("[米谷通讯] [获取token] [失败] [fail] ["+url+"] ["+content	+"]",e);
				return new String[0];
			
		}
	}
	
	public static void notifyMiguUpdataUserName(String[] infos) {
		try {
			if(true)return;
			
			infos = buildRequestParams(infos);
			String content="";
			try {
				content = "appkey=" + infos[0] + "&service=" + infos[1] + "&submittime=" + infos[2] + "&sign=" + infos[3] + "&useraccount=" + infos[4]
				+ "&roleid=" + infos[5] + "&rolename=" + URLEncoder.encode(infos[6],"utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String url = "http://client.migu99.com/migu/api/v1/wangxian/user/updateRoleName";
			if(isDebug)
			{
				url = "http://58.210.12.90:12587/platform-gamecenter/migu/api/v1/wangxian/user/updateRoleName";
			}
			HashMap headers = new HashMap();
			try {
	
				byte bytes[] = HttpUtils.webPost(new java.net.URL(url), content.getBytes("utf-8"), headers, 60000, 60000);
	
				String encoding = (String)headers.get(HttpUtils.ENCODING);
				Integer code = (Integer)headers.get(HttpUtils.Response_Code);
				String message = (String)headers.get(HttpUtils.Response_Message);
				String result = new String(bytes,encoding);
				JacksonManager jm = JacksonManager.getInstance();
				Map dataMap = (Map)jm.jsonDecodeObject(result);
//				String[] strs = new String[dataMap.size()];
				if(logger.isInfoEnabled())
				{
					logger.info("[米谷通讯] [获取token] ["+result+"] ["+content+"] ["+code+"] ["+message+"] ["+url+"]");
				}
//				strs[0] = (String)dataMap.get("appkey");
				String rr = (String)dataMap.get("result");
				if("1".equalsIgnoreCase(rr))
				{
					
					if(logger.isInfoEnabled())
					{
						logger.info("[米谷通讯] [更新角色名] [成功] [ok] ["+result+"] ["+content+"] ["+code+"] ["+message+"] ["+url+"]");
					}
					
				}
				else
				{
					logger.warn("[米谷通讯] [更新角色名] [失败] [fail] [返回错误结果集] ["+result+"] ["+content+"] ["+code+"] ["+message+"] ["+url+"]");
				}
				
				
			} catch (Exception e) {
					logger.error("[米谷通讯] [更新角色名] [失败] [fail] ["+url+"] ["+content	+"]",e);
				
			}
		} catch (Exception e) {
			logger.warn("[米谷通讯] [更新角色名] [异常] ", e);
		}
	}
	public static String[] buildRequestParams(String[] infos){
		String secretkey = "%$wang2014";
		Map map = new LinkedHashMap();
		String[] realInfos = new String[7];
		String appkey = "wangxian";
		map.put("appkey", appkey);
		realInfos[0] = appkey;
		String service  = "SYS10013";
		map.put("service", service);
		realInfos[1] = service;
		String submittime = System.currentTimeMillis()/1000+"";
		map.put("submittime", submittime);
		realInfos[2] = submittime;
		String useraccount = infos[1];
		map.put("useraccount", useraccount);
		realInfos[4] = useraccount;
		String roleid = infos[2];
		map.put("roleid", roleid);
		realInfos[5] = roleid;
		String rolename = infos[3];
		map.put("rolename", rolename);
		realInfos[6] = rolename;
		
		String md5str = getSignParam(map)+secretkey;
		realInfos[3] = StringUtil.hash(md5str);
		return realInfos;
		
	}
	
	public static String[] buildRequestTokenParams(String[] infos)
	{
		String secretkey = "%$wang2014";
		
		Map map = new LinkedHashMap();
		
		String[] realInfos = new String[21];
		realInfos[0] = infos[0];
		
		String appkey = "wangxian";
		map.put("appkey", appkey);
		realInfos[1] = appkey;
		
		String service  = "SYS10001";
		map.put("service", service);
		realInfos[2] = service;
		
		String submittime = System.currentTimeMillis()/1000+"";
		map.put("submittime", submittime);
		realInfos[3] = submittime;
		
		String useraccount = infos[1];
		map.put("useraccount", useraccount);
		realInfos[5] = useraccount;
		
		realInfos[6] = "";
		realInfos[7] = "";
		realInfos[8] = "";
		
		String servername = infos[2];
		map.put("servername", servername);
		realInfos[9] = servername;
		
		String roleid = infos[3];
		map.put("roleid", roleid);
		realInfos[10] = roleid;
		
		String rolename = infos[4];
		map.put("rolename", rolename);
		realInfos[11] = rolename;
		
		String rolelevel = infos[5];
		map.put("rolelevel", rolelevel);
		realInfos[12] = rolelevel;
		
		String rolesex = infos[6];
		map.put("rolesex", rolesex);
		realInfos[13] = rolesex;
		
		String rolevocation = infos[7];
		map.put("rolevocation", rolevocation);
		realInfos[14] = rolevocation;
		
		String platform = infos[8];
		map.put("platform", platform);
		realInfos[15] = platform;
		
		String channel = infos[9];
		map.put("channel", channel);
		realInfos[16] = channel;
		
		String mac = infos[10];
		map.put("mac", mac);
		realInfos[17] = mac;
		
		String aid = infos[11];
		map.put("aid", aid);
		realInfos[18] = aid;
		
		String entertime = infos[12];
		map.put("entertime", entertime);
		realInfos[19] = entertime;
		
		String clientId = infos[13];
		map.put("clientid", clientId);
		realInfos[20] = clientId;
		
		
		String md5str = getSignParam(map)+secretkey;
		realInfos[4] = StringUtil.hash(md5str);
		
		return realInfos;
		
	}
	
	public static String genSign(Map<String,String[]> params,String appkey,String secretkey)
	{
		if(logger.isInfoEnabled())
		{
			logger.info("[米谷通讯] [产生签名] ["+params+"] ["+appkey+"] ["+secretkey+"]");
		}
		
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String[] v = params.get(keys[i]);
			if( StringUtils.isEmpty(v[0]) || "sign".equals(keys[i]))continue;
			sb.append(keys[i]+"="+v[0]+"&");
		}
		String md5Str = sb.substring(0,sb.length()-1)+secretkey;
		
		if(logger.isInfoEnabled())
		{
			logger.info("[米谷通讯] [产生签名字符串] ["+params+"] ["+md5Str+"] ["+appkey+"] ["+secretkey+"]");
		}
		
		
		String sign = StringUtil.hash(md5Str);
		return sign;
	}
	
	public static String getSignParam(Map<String,String> params)
	{
		String keys[] = params.keySet().toArray(new String[0]);
		java.util.Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < keys.length ; i++){
			String v = params.get(keys[i]);
			if( StringUtils.isEmpty(v))continue;
			sb.append(keys[i]+"="+v+"&");
		}
		String md5Str = sb.substring(0,sb.length()-1);
		
		if(logger.isInfoEnabled())
		{
			logger.info("[米谷通讯] [产生签名字符串] ["+params+"] ["+md5Str+"]");
		}
		
		return md5Str;
	}
	
	public static String genJson(Map map) throws Exception
	{
		String s = JsonUtil.jsonFromObject(map);
		return s;
	}
	/**
	 * 添加访问游戏服的权限用户名和密码
	 * @return
	 */
	
	public static void addVisitGameUser(Map paramMap)
	{
		paramMap.put("authorize.username", "migu_trade");
		paramMap.put("authorize.password", "akiwWE123)sq");
	}
	
	public static class UrlInfo
	{
		public String url;
		public String port;
	}
	
	public static UrlInfo getUrlInfo(String s)
	{
		Pattern p = Pattern.compile("((http.+):([0-9]+))/saving.+");
		Matcher matcher = p.matcher(s);
		boolean isServerUrl = matcher.find();
		if(isServerUrl)
		{
			String urlStr = matcher.group(2);
			String port = matcher.group(3);
			
			UrlInfo urlInfo = new UrlInfo();
			urlInfo.url = urlStr;
			urlInfo.port = port;
			return urlInfo;
		}
		else
		{
			return null;
		}
	}
	
	public static class MySession
	{
		public String mac;
		public boolean  isValidDevice;
		public String lastStepDesc;
		public String serverName;
		public String roleName;
		public long roleId;
		public long createTime;
		public String username;
		public long enterServertime;
	}
	
	public static volatile boolean isCleanUp = true;
	
	public static Map<String,MySession> userCache = new ConcurrentHashMap<String, MiguWorker.MySession>();
	public static long 缓存条目过期时间 = 60 * 60 * 1000l;
	
	public static volatile boolean isRunning = false;
	
	public static Thread cleanThread  = new Thread(new CleanupTask());
	static
	{
		cleanThread.setName("MIGU_BOSS_CLEAN");
	}
	/**
	 * 根据缓存键值判断是否是合法的用户请求
	 * 0代表不合法 1代表合法
	 * @param cacheKey
	 * @return
	 */
	//验证合法请求的方式
	public static final int VALIDATE_REQUEST_TYPE_DEVICECODE = 1;
	public static final int VALIDATE_REQUEST_TYPE_ENTERSERVER_TIME = 2;
	
	public static int CURRENT_VALIDATE_REQ_TYPE = VALIDATE_REQUEST_TYPE_ENTERSERVER_TIME;
	
	public static int validateRequest(String cacheKey,String mac)
	{
		if(!isRunning)
		{
			cleanThread.start();
			isRunning = true;
			if(logger.isInfoEnabled())
				logger.info("[米谷通讯] [启动缓存清理线程] [成功]");
		}
		
		if(StringUtils.isEmpty(cacheKey))
		{
			logger.warn("[米谷通讯] [验证请求合法性] [失败] [不是合法请求] ["+mac+"] ["+cacheKey+"]");
			return 0;
		}
		
		
		if(StringUtils.isEmpty(mac))
		{
			logger.warn("[米谷通讯] [验证请求合法性] [失败] [不是合法请求] ["+mac+"] ["+cacheKey+"]");
			return 0;
		}
		
		
		
		MySession mySession = userCache.get(cacheKey);
		if(mySession != null)
		{
			if(CURRENT_VALIDATE_REQ_TYPE == VALIDATE_REQUEST_TYPE_DEVICECODE)
			{
				if(!StringUtils.isEmpty(mySession.mac))
				{
					if(mac.equalsIgnoreCase(mySession.mac))
					{
						if(logger.isInfoEnabled())
						{
							logger.info("[米谷通讯] [验证请求合法性] [成功] [是合法请求] ["+mac+"] ["+mySession.mac+"] ["+cacheKey+"]");
						}
						return 1;
					}
					else
					{
						logger.warn("[米谷通讯] [验证请求合法性] [失败] [不是合法请求] ["+mac+"] ["+mySession.mac+"] ["+cacheKey+"]");
						return 0;
					}
				}
				else
				{
					logger.warn("[米谷通讯] [验证请求合法性] [失败] [缓存中没有获取到mac] [当前验证请求的方式:"+CURRENT_VALIDATE_REQ_TYPE+"]  [可能是逻辑出错了] ["+mac+"] ["+mySession.mac+"] ["+cacheKey+"]");
					return 0;
				}
			}
			else
			{
				if(mySession.enterServertime > 0)
				{
					if(mac.equalsIgnoreCase(mySession.enterServertime+""))
					{
						if(logger.isInfoEnabled())
						{
							logger.info("[米谷通讯] [验证请求合法性] [成功] [是合法请求] ["+mac+"] ["+mySession.enterServertime+"] ["+cacheKey+"]");
						}
						return 1;
					}
					else
					{
						logger.warn("[米谷通讯] [验证请求合法性] [失败] [不是合法请求] ["+mac+"] ["+mySession.enterServertime+"] ["+cacheKey+"]");
						return 0;
					}
				}
				else
				{
					logger.warn("[米谷通讯] [验证请求合法性] [失败] [缓存中没有获取到进入服务器的时间] [当前验证请求的方式:"+CURRENT_VALIDATE_REQ_TYPE+"]  [可能是逻辑出错了] ["+mac+"] ["+mySession.enterServertime+"] ["+cacheKey+"]");
					return 0;
				}
			}
		}
		
		logger.warn("[米谷通讯] [验证请求合法性] [失败] [不是合法请求] ["+mac+"] [当前验证请求的方式:"+CURRENT_VALIDATE_REQ_TYPE+"] ["+cacheKey+"]");
		return 0;
	}
	
	public static class CleanupTask implements Runnable
	{
		@Override
		public void run() {
			while(isCleanUp)
			{
				try {
					Thread.sleep(5000l);
					cleanupCacheEntry();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static  void cleanupCacheEntry()
	{
		try
		{
			Iterator<String> iterator = userCache.keySet().iterator();
			
			while(iterator.hasNext())
			{
				String key = iterator.next();
				
				MySession mySession = userCache.get(key);
				
				if(mySession != null)
				{
					if((System.currentTimeMillis()-mySession.createTime) >= 缓存条目过期时间)
					{
						iterator.remove();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void addCache(String username,String roleId,String serverName,String mac,String stepDesc,String entertime)
	{
		try
		{
			String key = buildKey(username, roleId, serverName);
			
			MySession mySession =  new MySession();
			mySession.createTime =System.currentTimeMillis();
			mySession.lastStepDesc = stepDesc;
			mySession.mac = mac;
			mySession.roleId = Long.parseLong(roleId);
			mySession.serverName = serverName;
			mySession.username = username;
			mySession.enterServertime = Long.decode(entertime);
				
			userCache.put(key, mySession);
		}
		catch(Exception e)
		{
			logger.error("[米谷通讯] [添加待验证信息入缓存中] [失败] [出现非法异常] ["+username+"] ["+roleId+"] ["+serverName+"] ["+mac+"] ["+entertime+"] [当前验证方式:"+CURRENT_VALIDATE_REQ_TYPE+"]",e);
		}
	}
	
	public static void opCache(String op,String username,String roleId,String serverName,String roleName,String enterServerTime)
	{
		try
		{
			String key = buildKey(username, roleId, serverName);
			MySession mySession = userCache.get(key);
			if(op.contains("enterserver_query"))
			{
				//对于进入游戏服的用户，我们去判断是否是正在使用米谷的用户，如果是，就更新时间，
				//这样，米谷用户就无法继续操作了，从而达到安全验证请求的目的
				if(mySession != null)
				{
					long oldEnterServerTime = mySession.enterServertime;
					mySession.enterServertime = Long.decode(enterServerTime);
					if(logger.isInfoEnabled())
					{
						logger.info("[米谷通讯] [出现打开米谷同时又进入游戏服的现象] [更新玩家登陆时间] ["+username+"] ["+roleId+"] ["+serverName+"] ["+roleName+"] ["+enterServerTime+"] ["+oldEnterServerTime+"]");
					}
				}
				
			}
		}
		catch(Exception e)
		{
			logger.error("[米谷通讯] [添加待验证信息入缓存中] [失败] [出现非法异常] ["+op+"] ["+username+"] ["+roleId+"] ["+serverName+"] ["+enterServerTime+"] [当前验证方式:"+CURRENT_VALIDATE_REQ_TYPE+"]",e);
		}
	}
	
	public static String buildKey(String username,String roleId,String serverName)
	{
		String key = username+"_"+roleId+"_"+serverName;
		return key;
	}
}
