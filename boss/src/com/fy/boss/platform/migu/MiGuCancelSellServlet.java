package com.fy.boss.platform.migu;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
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

import com.fy.boss.game.model.Server;
import com.fy.boss.game.service.ServerManager;
import com.fy.boss.platform.migu.MiguWorker.UrlInfo;
import com.fy.boss.tools.JacksonManager;
import com.fy.boss.transport.BossServerService;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.ParamUtils;

public class MiGuCancelSellServlet extends HttpServlet {
//TODO 需要限制ip
	//更新相关的订单状态
	public static Logger logger = BossServerService.logger;
	
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		JacksonManager jacksonManager = JacksonManager.getInstance();
		String myappkey = "wangxian";
		String secretkey = "%$wang2014";
		/**
		 * 查询装备列表
		 * 首先获取可出售的装备，需要http通讯
		 * 在这里需要限制ip，验证用户有效性，
		 */
		
		String appkey = ParamUtils.getParameter(req, "appkey");
		String service = ParamUtils.getParameter(req, "service");
		String submittime = ParamUtils.getParameter(req, "submittime");
		String sign = ParamUtils.getParameter(req, "sign");
		String username = ParamUtils.getParameter(req, "useraccount");
		String roleid = ParamUtils.getParameter(req, "roleid");
		String servername =  URLDecoder.decode( ParamUtils.getParameter(req, "servername"),"utf-8");
		String platform = ParamUtils.getParameter(req, "platform");
		String channel = ParamUtils.getParameter(req, "channel");
		
		String recordid = ParamUtils.getParameter(req, "recordid");
		String miguuid = ParamUtils.getParameter(req, "miguuserid");
		String reason = ParamUtils.getParameter(req, "canclereason");
	
		String mac  = ParamUtils.getParameter(req, "mac");
		String aid  = ParamUtils.getParameter(req, "aid");
		String enterTime = ParamUtils.getParameter(req, "entertime");
		String devicecode = "";
		
		String recordservername = ParamUtils.getParameter(req, "recordservername");			//下架订单所在服务器
		String recordtype = ParamUtils.getParameter(req, "recordType");					//下架商品类型   只支持角色交易订单跨服下架
		
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
		
		Map map = new LinkedHashMap();
		map.put("appkey", "wangxian");
		map.put("service", "SYS10010");
		map.put("submittime", System.currentTimeMillis()/1000);
	
		//不是自动的 验证设备
		if(!"2".equals(reason))
		{
			String cacheKey = MiguWorker.buildKey(username, roleid, servername);
			int validateRequestResult = MiguWorker.validateRequest(cacheKey, devicecode);
			
			if(validateRequestResult != 1)
			{
				map.put("result", 3);
				map.put("type", -1);
				map.put("recordid", "");
				res.getWriter().write(jacksonManager.toJsonString(map));
				logger.warn("[米谷通讯] [非自动请求验证合法请求] [失败] [不是合法请求] ["+username+"] ["+roleid+"] ["+servername+"] ["+map+"]");
				return;
			}
		}
//		
		if(mysign.equalsIgnoreCase(sign))
		{
			if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(roleid) 
					&& !StringUtils.isEmpty(servername) && !StringUtils.isEmpty(platform) && !StringUtils.isEmpty(channel))
			{
				String jsons = "";
				HashMap headers = new HashMap();
				//TODO 这里需要做成能根据服务器名称取得真正通信地址的功能，目前临时用潘多拉星的地址
				
				
				Map<String,String> paramMap = new LinkedHashMap();
				paramMap.put("op", "cancelsell");
				paramMap.put("playerId", roleid);
				paramMap.put("username", username);
				paramMap.put("servername", URLEncoder.encode(servername,"utf-8"));
				paramMap.put("platform", platform);
				paramMap.put("channel", channel);
				paramMap.put("saleid", recordid);
				paramMap.put("reason", reason);
				paramMap.put("recordType", recordtype);
				paramMap.put("mac", (StringUtils.isEmpty(mac)?aid:mac));
				MiguWorker.addVisitGameUser(paramMap);
				String params = MiguWorker.getSignParam(paramMap);
				String returnString = "";
				
				String aimserver = servername;
				if ("3".equalsIgnoreCase(recordtype)) {
					aimserver = recordservername;
				}
				
				Server server = ServerManager.getInstance().getServer(aimserver);
				if (logger.isInfoEnabled()) {
					logger.info("[米谷通讯] [取消订单] [" + aimserver + "] [" + recordtype + "] " );
				}
				if(server == null)
				{
					logger.error("[米谷通讯] [获取服务器] [失败] [fail] [未知服务器] ["+aimserver+"] ["+username+"]");
					map.put("result", 3);
					map.put("type", -1);
					res.getWriter().write(jacksonManager.toJsonString(map));
					return;
				}
				
				UrlInfo urlInfo = MiguWorker.getUrlInfo(server.getSavingNotifyUrl());
				
				if(urlInfo == null)
				{
					logger.error("[米谷通讯] [获取连接信息] [失败] [fail] [无法获取服务器连接] ["+server.getSavingNotifyUrl()+"] ["+username+"]");
					map.put("result", 3);
					map.put("type", -1);
					res.getWriter().write(jacksonManager.toJsonString(map));
					return;
				}
				
				URL url = new URL(urlInfo.url+":"+urlInfo.port+"/migu/miguNotify");
				try {
					byte bytes[] = HttpUtils.webPost(url,params.getBytes("utf-8"), headers,60000, 60000);
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					returnString = new String(bytes,encoding);
				} catch (Exception e) {
					logger.error("[米谷通讯] [获取连接信息] [失败] [fail] [无法连接服务器] ["+server.getSavingNotifyUrl()+"] ["+username+"]",e);
					map.put("result", 4);
					map.put("type", -1);
					map.put("recordList", new ArrayList());
					res.getWriter().write(jacksonManager.toJsonString(map));
					return;
				}
				
				if(!StringUtils.isEmpty(returnString))
				{
					try
					{
						map.put("result", 1);
						map.put("recordid", returnString);
						if(logger.isInfoEnabled())
							logger.info("[米谷通讯] [取消出售] [成功] [ok] ["+url+"] ["+username+"] ["+jsons+"] ["+servername+"] ["+channel+"] ["+roleid+"]");
					}
					catch(Exception e)
					{
						logger.warn("[米谷通讯] [取消出售] [失败] [出现未知异常] ["+url+"] ["+username+"] ["+jsons+"] ["+servername+"] ["+channel+"] ["+roleid+"]",e);
					}
				}
				else
				{
					logger.error("[米谷通讯] [获取连接信息] [失败] [fail] [没有返回内容] ["+server.getSavingNotifyUrl()+"] ["+username+"]");
					map.put("result", 3);
					map.put("type", -1);
					map.put("recordid", "");
				}
				
				res.getWriter().write(jacksonManager.toJsonString(map));
				return;
			}
			else
			{
				logger.error("[米谷通讯] [获取连接信息] [失败] [fail] [参数不全] ["+username+"] ["+req.getParameterMap()+"]");
				map.put("result", 3);
				map.put("type", -1);
				map.put("recordid", "");
				res.getWriter().write(jacksonManager.toJsonString(map));
				return;
			}
		}
		else
		{
			map.put("result", 2);
			map.put("type", -1);
			map.put("recordid", "");
			res.getWriter().write(jacksonManager.toJsonString(map));
			logger.warn("[米谷通讯] [取消出售] [失败] [验证签名失败] ["+username+"] ["+servername+"] ["+channel+"] ["+roleid+"]");
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
		simplePlayerInfo.roleid = (String)map.get("playerId");
		simplePlayerInfo.rolename = (String)map.get("playerName");
		simplePlayerInfo.servername = (String)map.get("serverRealName");
		simplePlayerInfo.rolevocation = (Integer)map.get("career");
		simplePlayerInfo.rolelevel = (Integer)map.get("level");
		return simplePlayerInfo;
	}
	
	public static class SimplePlayerInfo
	{
		public String roleid;
		public String rolename;
		public String servername;
		public int rolevocation;
		public int rolelevel;
	}
	
}
