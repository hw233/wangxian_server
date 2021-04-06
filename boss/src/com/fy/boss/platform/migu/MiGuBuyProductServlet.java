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

public class MiGuBuyProductServlet extends HttpServlet {
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
		
		String servername =  ParamUtils.getParameter(req, "servername");//URLDecoder.decode( ParamUtils.getParameter(req, "servername"),"utf-8");
		if (servername == null) {
			logger.warn("[米谷通讯] [失败] [传输过来的servername为null] [" + roleid + "] [" + username + "]");
			return ;
		}
		servername = URLDecoder.decode(servername,"utf-8");
		String platform = ParamUtils.getParameter(req, "platform");
		String channel = ParamUtils.getParameter(req, "channel");
		
		String recordid = ParamUtils.getParameter(req, "recordid");
		String miguuid = ParamUtils.getParameter(req, "miguuserid");
		
		String buymiguuid = ParamUtils.getParameter(req, "sellmiguuserid");
		String sellroleid = ParamUtils.getParameter(req, "sellroleid");
		String tradefee = ParamUtils.getParameter(req, "tradefee");
		String mac  = ParamUtils.getParameter(req, "mac");
		String aid  = ParamUtils.getParameter(req, "aid");
		String enterTime = ParamUtils.getParameter(req, "entertime");
		String saleprice = ParamUtils.getParameter(req, "saleprice");			//出售金额
		
		String recordType = ParamUtils.getParameter(req, "recordType");									//订单类型
		String selledservername = ParamUtils.getParameter(req, "selledservername");							//上架角色所在服务器名
		String selledusername = ParamUtils.getParameter(req, "selledusername");								//上架角色的账号
		String selledroleid = ParamUtils.getParameter(req, "selledroleid");								//上架角色的roleid
		if (selledservername != null) {
			selledservername = URLDecoder.decode(selledservername,"utf-8");
		}
		
		String buyerservername = ParamUtils.getParameter(req, "buyerservername");							//购买者所在服务器
		if (buyerservername != null) {
			buyerservername = URLDecoder.decode(buyerservername,"utf-8");
		}
		String buyerusername = ParamUtils.getParameter(req, "buyerusername");								//购买者账号
		String buyerroleid = ParamUtils.getParameter(req, "buyerroleid");								//购买者的roleid
		
		
		
		String devicecode = "";
		

		Map map = new LinkedHashMap();
		map.put("appkey", "wangxian");
		map.put("service", "SYS10009");
		map.put("submittime", System.currentTimeMillis()/1000);
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
			else
			{
				map.put("result", 3);
				map.put("type", -1);
				res.getWriter().write(jacksonManager.toJsonString(map));
				logger.warn("[米谷通讯] [验证合法请求] [失败] [设备号为空] ["+mac+"] ["+aid+"] ["+devicecode+"] ["+username+"] ["+roleid+"] ["+servername+"] ["+map+"]");
				return;
			}
		}
		else if(MiguWorker.CURRENT_VALIDATE_REQ_TYPE ==MiguWorker.VALIDATE_REQUEST_TYPE_ENTERSERVER_TIME)
		{
			if(!StringUtils.isEmpty(enterTime) )
			{
				devicecode = enterTime;
			}
			else
			{
				map.put("result", 3);
				map.put("type", -1);
				res.getWriter().write(jacksonManager.toJsonString(map));
				logger.warn("[米谷通讯] [验证合法请求] [失败] [设备号为空] ["+mac+"] ["+aid+"] ["+devicecode+"] ["+username+"] ["+roleid+"] ["+servername+"] ["+map+"]");
				return;
			}
		}
		else
		{
			map.put("result", 3);
			map.put("type", -1);
			res.getWriter().write(jacksonManager.toJsonString(map));
			logger.warn("[米谷通讯] [验证合法请求] [失败] [设备号为空] ["+mac+"] ["+aid+"] ["+devicecode+"] ["+username+"] ["+roleid+"] ["+servername+"] ["+map+"]");
			return;
		}
		
		
		String mysign = MiguWorker.genSign(req.getParameterMap(), myappkey, secretkey);
		
		
		
		String cacheKey = MiguWorker.buildKey(username, roleid, servername);
		int validateRequestResult = MiguWorker.validateRequest(cacheKey, devicecode);
		
		if(validateRequestResult != 1)
		{
			map.put("result", 3);
			map.put("type", -1);
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
				
				Map<String,String> paramMap = new LinkedHashMap();
				paramMap.put("op", "buyequip");
				paramMap.put("playerId", roleid);
				paramMap.put("sellplayerId", sellroleid);
				paramMap.put("username", username);
				paramMap.put("servername", URLEncoder.encode(servername,"utf-8"));
				paramMap.put("platform", platform);
				paramMap.put("channel", channel);
				paramMap.put("saleid", recordid);
				paramMap.put("tradefee", tradefee);
				paramMap.put("saleprice", saleprice);
				paramMap.put("mac", (StringUtils.isEmpty(mac)?aid:mac));
				if (recordType.equals("3")) {			//角色交易需要新加一些参数
					//验证购买者username
					if (!username.equals(buyerusername) && !username.equals(selledusername)) {
						logger.error("[米谷通讯] [获取服务器] [失败] [fail] [账号不匹配] [username:"+username+"] [buyerusername:"+buyerusername+"] [selledusername:" + selledusername + "]");
						map.put("type", -1);
						res.getWriter().write(jacksonManager.toJsonString(map));
						return ;
					}
					paramMap.put("selledroleid", selledroleid);
					paramMap.put("selledservername", selledservername);
					paramMap.put("selledusername", selledusername);
					paramMap.put("buyerusername", buyerusername);
					paramMap.put("buyerroleid", buyerroleid);
					paramMap.put("recordType", recordType);
					if (!sellroleid.equalsIgnoreCase(selledroleid)) {
						paramMap.put("sellplayerId", selledroleid);
					}
				}
			
				MiguWorker.addVisitGameUser(paramMap);
				String params = MiguWorker.getSignParam(paramMap);
				String returnString = "";
				
				String aimServername = servername;
				if (recordType.equals("3")) {					//如果是角色交易
					if (servername.equals(selledservername) && servername.equals(buyerservername)) {		//上架的角色、操作者、购买者同服
						
					} else if (selledservername.equals(servername)) {				 //订单和传过来的服务器不同服
						
					} else {															//购买者和上架的角色不同服
						aimServername = selledservername;			
						//验证购买者账号合法性（是否存在以及是否封停）
						boolean isvaliduser = true;
						if (!isvaliduser) {
							logger.error("[米谷通讯] [获取服务器] [失败] [fail] [购买者账号不合法] ["+aimServername+"] ["+username+"]");
							map.put("type", -1);
							res.getWriter().write(jacksonManager.toJsonString(map));
							return ;
						}
						
					}
				}
				
				Server server = ServerManager.getInstance().getServer(aimServername);
				
				if(server == null)
				{
					logger.error("[米谷通讯] [获取服务器] [失败] [fail] [未知服务器] ["+aimServername+"] ["+username+"]");
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
					try {
						paramMap.put("op", "querySaleIdInfo");
						String params2 = MiguWorker.getSignParam(paramMap);
						byte bytes[] = HttpUtils.webPost(url,params2.getBytes("utf-8"), headers,60000, 60000);
						String encoding = (String)headers.get(HttpUtils.ENCODING);
						Integer code = (Integer)headers.get(HttpUtils.Response_Code);
						String message = (String)headers.get(HttpUtils.Response_Message);
						returnString = new String(bytes,encoding);
						logger.warn("[米谷通讯] [获取连接信息] [异常] [已重新验证订单状态] [" + server.getSavingNotifyUrl() + "] [" + username + "] [");
					} catch (Exception e2) {
						map.put("result", 4);
						map.put("type", -1);
						map.put("recordList", new ArrayList());
						res.getWriter().write(jacksonManager.toJsonString(map));
						logger.error("[米谷通讯] [获取连接信息] [失败] [fail] [无法连接服务器] ["+server.getSavingNotifyUrl()+"] ["+username+"]",e);
						return;
					}
				}
				
				if(!StringUtils.isEmpty(returnString))
				{
					try
					{
						map.put("result", 1);
						map.put("recordid", returnString);
						if(logger.isInfoEnabled())
							logger.info("[米谷通讯] [购买装备] [成功] [ok] ["+url+"] ["+username+"] ["+jsons+"] ["+returnString+"] ["+servername+"] ["+channel+"] ["+roleid+"]");
					}
					catch(Exception e)
					{
						logger.warn("[米谷通讯] [购买装备] [失败] [出现未知异常] ["+url+"] ["+username+"] ["+jsons+"] ["+returnString+"] ["+servername+"] ["+channel+"] ["+roleid+"]",e);
					}
				}
				else
				{
					map.put("result", 3);
					map.put("type", -1);
					map.put("recordid", "");
					logger.warn("[米谷通讯] [购买装备] [失败] [游戏服返回失败] ["+url+"] ["+username+"] ["+returnString+"] ["+aimServername+"] ["+channel+"] ["+roleid+"]");
				}
				
				res.getWriter().write(jacksonManager.toJsonString(map));
				return;
			}
			else
			{
				map.put("result", 3);
				map.put("type", -1);
				map.put("recordid", "");
				res.getWriter().write(jacksonManager.toJsonString(map));
				logger.warn("[米谷通讯] [购买装备] [失败] [出现非法请求] ["+username+"] ["+servername+"] ["+channel+"] ["+roleid+"]");
				return;
			}
		}
		else
		{
			map.put("result", 2);
			map.put("type", -1);
			map.put("recordid", "");
			res.getWriter().write(jacksonManager.toJsonString(map));
			logger.warn("[米谷通讯] [购买装备] [失败] [验证签名失败] ["+username+"] ["+servername+"] ["+channel+"] ["+roleid+"] ["+sign+"] ["+mysign+"]");
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
