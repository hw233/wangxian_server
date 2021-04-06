package com.fy.boss.platform.migu;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.fy.boss.platform.migu.MiguWorker;
import com.xuanzhi.tools.servlet.HttpUtils;
import com.xuanzhi.tools.text.JsonUtil;
import com.xuanzhi.tools.text.ParamUtils;

public class MiGuQueryEquipByRoleServlet extends HttpServlet {
	public static Logger logger = BossServerService.logger;
	
//TODO 需要限制ip
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException 
	{
		long startTime = System.currentTimeMillis(); 
		req.setCharacterEncoding("utf-8");
		res.setCharacterEncoding("utf-8");
		
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
		map.put("service", "SYS10003");
		map.put("submittime", System.currentTimeMillis()/1000);
		map.put("roleid", roleid);
		
		
		String cacheKey = MiguWorker.buildKey(username, roleid, servername);
		int validateRequestResult = MiguWorker.validateRequest(cacheKey, devicecode);
		
		if(validateRequestResult != 1)
		{
			map.put("result", 3);
			res.getWriter().write(jacksonManager.toJsonString(map));
			logger.warn("[米谷通讯] [验证合法请求] [失败] [不是合法请求] ["+username+"] ["+roleid+"] ["+servername+"] ["+map+"]");
			return;
		}
		
		if(mysign.equalsIgnoreCase(sign))
		{
			if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(roleid) 
					&& !StringUtils.isEmpty(servername) && !StringUtils.isEmpty(platform) && !StringUtils.isEmpty(channel))
			{
				String jsons = "";
				HashMap headers = new HashMap();
				//TODO 这里需要做成能根据服务器名称取得真正通信地址的功能，目前临时用潘多拉星的地址
				/**
				 * String op = req.getParameter("op");
			long playerId = ParamUtils.getLongParameter(req, "playerId", -1l);
			String username = req.getParameter("username");
			String servername = req.getParameter("servername");
			String platform = req.getParameter("platform");
			String channel = req.getParameter("channel");
				 */
				//
				
				Map<String,String> paramMap = new LinkedHashMap();
				paramMap.put("op", "queryeq");
				paramMap.put("playerId", roleid);
				paramMap.put("username", username);
				paramMap.put("servername", URLEncoder.encode(servername,"utf-8"));
				paramMap.put("platform", platform);
				paramMap.put("channel", channel);
				paramMap.put("mac", (StringUtils.isEmpty(mac)?aid:mac));
				MiguWorker.addVisitGameUser(paramMap);
				String params = MiguWorker.getSignParam(paramMap);
				
				Server server = ServerManager.getInstance().getServer(servername);
				
				if(server == null)
				{
					logger.error("[米谷通讯] [获取服务器] [失败] [fail] [未知服务器] ["+servername+"] ["+username+"]");
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
					byte bytes[] = HttpUtils.webPost(url,params.getBytes("utf-8"), headers, 60000, 60000);
					String encoding = (String)headers.get(HttpUtils.ENCODING);
					Integer code = (Integer)headers.get(HttpUtils.Response_Code);
					String message = (String)headers.get(HttpUtils.Response_Message);
					jsons = new String(bytes,encoding);
				} catch (Exception e) {
					map.put("result", 4);
					res.getWriter().write(jacksonManager.toJsonString(map));
					logger.warn("[米谷通讯] [获取角色可售装备] [失败] [fail] [出现异常] ["+url+"] ["+username+"] ["+map+"] ["+servername+"] ["+channel+"] ["+roleid+"]",e);
					return;
				}
				
				map.put("result", 1);
				java.util.List lst = new ArrayList();
				try {
					lst = JsonUtil.objectFromJson(jsons, List.class);
					if(logger.isInfoEnabled())
						logger.info("[米谷通讯] [获取角色可售装备] [成功] [ok] ["+url+"] ["+username+"] ["+jsons+"] ["+servername+"] ["+channel+"] ["+roleid+"]");
				} catch (Exception e) {
					logger.warn("[米谷通讯] [获取角色可售装备] [失败] [出现未知异常] ["+url+"] ["+username+"] ["+jsons+"] ["+servername+"] ["+channel+"] ["+roleid+"]",e);
				}
				map.put("equiplist",lst);
				
				
				res.getWriter().write(jacksonManager.toJsonString(map));
				return;
			}
			else
			{
				map.put("result", 3);
				res.getWriter().write(jacksonManager.toJsonString(map));
				logger.warn("[米谷通讯] [获取角色可售装备] [失败] [fail] [参数不全] ["+username+"] ["+map+"] ["+servername+"] ["+channel+"] ["+roleid+"] ["+req.getParameterMap()+"]");
				return;
			}
		}
		else
		{
			map.put("result", 2);
			res.getWriter().write(jacksonManager.toJsonString(map));
			logger.warn("[米谷通讯] [获取角色可售装备] [失败] [fail] [签名验证失败] ["+username+"] ["+map+"] ["+servername+"] ["+channel+"] ["+roleid+"] ["+sign+"] ["+mysign+"] ["+req.getParameterMap()+"]");
			return;
		}
	}
}
