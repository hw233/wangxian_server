package com.fy.boss.platform.migu;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fy.boss.tools.JacksonManager;
import com.fy.boss.transport.BossServerService;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.ParamUtils;

public class MiGuQueryAllRolesServlet extends HttpServlet {
//TODO 需要限制ip
	//更新相关的订单状态
	public static Logger logger = BossServerService.logger;
	
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		String myappkey = "wangxian";
		String secretkey = "%$wang2014";
		//去网关拿所有角色信息
		String username = ParamUtils.getParameter(req, "useraccount");
		String appkey = ParamUtils.getParameter(req, "appkey");
		String service = ParamUtils.getParameter(req, "service");
		String submittime = ParamUtils.getParameter(req, "submittime");
		String sign = ParamUtils.getParameter(req, "sign");
		String platform = ParamUtils.getParameter(req, "platform");
		String channel = ParamUtils.getParameter(req, "channel");
		
		String mac  = ParamUtils.getParameter(req, "mac");
		String aid  = ParamUtils.getParameter(req, "aid");
		
		String enterTime = ParamUtils.getParameter(req, "entertime");
		String devicecode = "";
		
		if(MiguWorker.CURRENT_VALIDATE_REQ_TYPE ==MiguWorker.VALIDATE_REQUEST_TYPE_DEVICECODE)
		{
			if(!StringUtils.isEmpty(mac) && !"02:00:00:00:00:00".equals(mac))
			{
				devicecode = mac;
			}
			else if(!StringUtils.isEmpty(aid))
			{
				devicecode = aid;
			}
		}
		else if(MiguWorker.CURRENT_VALIDATE_REQ_TYPE ==MiguWorker.VALIDATE_REQUEST_TYPE_ENTERSERVER_TIME)
		{
			if(!StringUtils.isEmpty(enterTime) )
			{
				devicecode = enterTime;
			}
		}
		
		String mysign = MiguWorker.genSign(req.getParameterMap(), myappkey, secretkey);
		JacksonManager jacksonManager = JacksonManager.getInstance();
		Map map = new LinkedHashMap();
		map.put("appkey", "wangxian");
		map.put("service", "SYS10002");
		map.put("submittime", System.currentTimeMillis()/1000);
		
		if(mysign.equalsIgnoreCase(sign))
		{
			if(!StringUtils.isEmpty(username) )
			{
				String jsons = "";
				HashMap headers = new HashMap();
				String tempUrl = "http://116.213.192.216:8882/game_gateway/playerinfo4migu.jsp?username=";
				if (MiguWorker.openRoleExchange) {
					tempUrl = "http://116.213.192.216:8882/game_gateway/playerinfo4miguNew.jsp?username=";
				}
				URL url = new URL(tempUrl + username);
//				URL url = new URL("http://124.248.40.21:8882/game_gateway/playerinfo4migu.jsp?username=" + username);
				
				if(MiguWorker.isDebug)
				{
					url = new URL("http://124.248.40.21:8882/game_gateway/playerinfo4miguNew.jsp?username=" + username);
				}
				
				
			//	URL url = new URL("http://112.25.14.13:8882/game_gateway/playerinfo4website.jsp?username=" + username);
				try {
					byte bytes[] = HttpUtils.webGet(url, headers, 60000, 60000);
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					jsons = new String(bytes,encoding);
				} catch (Exception e) {
					map.put("result", 4);
					res.getWriter().write(jacksonManager.toJsonString(map));
					logger.error("[米谷通讯] [获取玩家所有角色] [失败] [fail] [出现异常] ["+url+"] ["+username+"]", e);
					return;
				}
				
				
				ArrayList<Object> lst = (ArrayList<Object>)jacksonManager.jsonDecodeObject(jsons);
				SimplePlayerInfo[] simplePlayerInfos = new SimplePlayerInfo[lst.size()];
				
				for(int i=0; i<lst.size(); i++)
				{
					SimplePlayerInfo simplePlayerInfo = convertSimlePlayer((Map)lst.get(i));
					if(simplePlayerInfo != null)
					{
						simplePlayerInfos[i] = simplePlayerInfo;
					}
				}
				map.put("result", 1);
				try {
					map.put("rolelist", JsonUtil.jsonFromObject(simplePlayerInfos));
					if(logger.isInfoEnabled())
						logger.info("[米谷通讯] [获取玩家所有角色] [成功] [ok] ["+url+"] ["+username+"] ["+map+"] ["+jsons+"]");
				} catch (Exception e) {
					logger.warn("[米谷通讯] [获取玩家所有角色] [失败] [出现未知异常] ["+url+"] ["+username+"] ["+map+"] ["+jsons+"]",e);
				}
				res.getWriter().write(URLEncoder.encode(jacksonManager.toJsonString(map),"utf-8"));
			
				return;
			}
			else
			{
				map.put("result", 3);
				res.getWriter().write(jacksonManager.toJsonString(map));
				logger.warn("[米谷通讯] [获取玩家所有角色] [失败] [fail] [参数不全] ["+username+"] ["+map+"]");
				return;
			}
		}
		else
		{
			map.put("result", 2);
			res.getWriter().write(jacksonManager.toJsonString(map));
			logger.warn("[米谷通讯] [获取玩家所有角色] [失败] [fail] [签名失败] ["+username+"] ["+map+"] ["+sign+"] ["+mysign+"] ["+appkey+"] ["+secretkey+"]");
			return;
		}
	}
	
	/**
	 * [{"id":1222000000019859218,"version":4,"playerId":1395000000000015087,"serverRealName":"仙魂侠魄","userName":"yuwenbao","level":3,"playerName":"大仙","career":1,"playerRMB":0,"playerVIP":0,"lastAccessTime":1401459750502}]
	 * @param map
	 * @return
	 */
	
	public SimplePlayerInfo convertSimlePlayer(Map map)
	{
		SimplePlayerInfo simplePlayerInfo = new SimplePlayerInfo();
		simplePlayerInfo.roleid = (Long)map.get("playerId")+"";
		simplePlayerInfo.rolename = (String)map.get("name");
		simplePlayerInfo.servername = (String)map.get("servername");
		simplePlayerInfo.rolevocation = (Integer)map.get("career");
		simplePlayerInfo.rolelevel = (Integer)map.get("level");
		simplePlayerInfo.viplevel = (Integer)map.get("vipLevel");
		simplePlayerInfo.country = (Integer)map.get("country");
		return simplePlayerInfo;
	}
	
	public static class SimplePlayerInfo
	{
		public String roleid;
		public String rolename;
		public String servername;
		public int rolevocation;
		public int rolelevel;
		public int viplevel;
		public int country;
	}
	
}
