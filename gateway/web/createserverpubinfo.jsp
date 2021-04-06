<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.net.URL"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtil"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.gamegateway.mieshi.server.pub.ServerPubRecord"%>
<%@page import="com.fy.gamegateway.mieshi.server.pub.ServerPubRecordManager"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DataBlock"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.finance.service.PlatformSavingCenter"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.fy.boss.platform.uc.UCSDKSavingManager"%>
<%@page import="com.fy.boss.finance.dao.impl.ExceptionOrderFormDAOImpl"%>
<%@page import="com.fy.boss.finance.model.ExceptionOrderForm"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.finance.service.OrderFormManager"%>
<%@page import="com.fy.boss.finance.model.OrderForm"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
	static Map<Integer,Long> fengyinTimeInfo = new HashMap<Integer,Long>();

	static
	{
		fengyinTimeInfo.put(70, 7*24*3600*1000l);
		fengyinTimeInfo.put(110, 30*24*3600*1000l);
		fengyinTimeInfo.put(150, 90*24*3600*1000l);
		fengyinTimeInfo.put(190, 180*24*3600*1000l);
		fengyinTimeInfo.put(220, 180*24*3600*1000l);
	}

	static	int[] SEAL_LEVELS = new int[]{70,110,150,190,220};


%>    

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2>服务器发布信息查看</h2>
	<table border="2px">
	<tr>
		<td>
			服务器名
		</td>
		<td>
			服务器类型
		</td>
		<td>
			开服时间
		</td>
			
		<td>
			70级解封时间
		</td>
		<td>
			110级解封时间
		</td>
		<td>
			150级解封时间
		</td>
		<td>
			190级解封时间
		</td>
		<td>
			220级解封时间
		</td>
	</tr>
    
<%
	//
	

		

	String serverpubs = request.getParameter("serverpubs");
	String serverinfos[] = null;
	if(serverpubs != null)
	{
		serverpubs.trim();
		serverinfos = serverpubs.split("\r*\n");
	}
	boolean isDo = ParamUtils.getBooleanParameter(request, "isDo");
	if(serverinfos != null && serverinfos.length > 0)
	{
	
		
		
		if(isDo)
		{
	
			
		
			
			for(String serverinfo : serverinfos)
			{
				String ss[] = serverinfo.split("[=]");
				if(ss != null && ss.length == 2)
				{
					String serverName = ss[0];
					if(serverName == null)
					{
						serverName = "";
					}
					else
					{
						serverName = serverName.trim();
					}
					
					String openServerTime = ss[1];
					if(openServerTime == null)
					{
						openServerTime = "";
					}
					else
					{
						openServerTime = openServerTime.trim();
					}
					
					
					//获取server类型
					MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();			
					MieshiServerInfo si = mm.getServerInfoByName(serverName);
					
					int st = si.getServerType();
					String serverType = MieshiServerInfo.SERVERTYPE_NAMES[st];
					String serverHost = "http://"+si.getIp()+":"+si.getHttpPort()+"/seal_info";
					out.println("servername:"+ serverName +" serverHost:"+ serverHost+"<br/>");
					
					Map<Integer,Long> fengyinMap = new HashMap<Integer,Long>();
					
					URL url = new URL(serverHost);
					Map headers = new HashMap();
					String content = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p";
					byte[] bytes =  HttpUtils.webPost(url, content.getBytes(), headers, 60000, 60000);
					
					String fengyininfo = new String(bytes);
					
					Map<String,Map> fengyininfoMap = JsonUtil.objectFromJson(fengyininfo,Map.class);
					
					Iterator<String> it = fengyininfoMap.keySet().iterator();
					while(it.hasNext())
					{
						String key = it.next();
						Map map = fengyininfoMap.get(key);
						fengyinMap.put(Integer.decode(key), (Long)map.get("sealCanOpenTime"));
					}
					
					ServerPubRecord pubRecord = new ServerPubRecord();
					pubRecord.setCreateTime(System.currentTimeMillis());
					pubRecord.setFengyinTimeInfo(fengyinMap);
					pubRecord.setOpenServerTime(openServerTime);
					pubRecord.setServerName(serverName);
					pubRecord.setServerType(serverType);
					ServerPubRecordManager.getInstance().saveOrUpdate(pubRecord);
					
					
				}
			}
			
			
		}
	}
		
	
	
	

%>
<%
	//查询信息
	ServerPubRecordManager serverPubRecordManager =  ServerPubRecordManager.getInstance();

	List<ServerPubRecord> serverPubs = serverPubRecordManager.queryForWhere(" id > ? ", new Object[]{1});
	
	if(serverPubs != null)
	{
		Iterator<ServerPubRecord> iterator = serverPubs.iterator();
		
		while(iterator.hasNext())
		{
			ServerPubRecord record = iterator.next();	
			
			MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();			
			MieshiServerInfo si = mm.getServerInfoByName(record.getServerName());
			//http://116.213.193.70:8001/seal_info?authorize.username=innerapp&authorize.password=innerapp123
			///seal_info
			String serverHost = "http://"+si.getIp()+":"+si.getHttpPort()+"/seal_info";
					
	
			
%>
		<tr>
			<td>
				<%=record.getServerName() %>
			</td>
			<td>
				<%=record.getServerType() %>
			</td>
			<td>
				
				<%=record.getOpenServerTime() %>
			</td>
			
			<% 
				Map<Integer,Long> fengyinMap = record.getFengyinTimeInfo();
			
				if(fengyinMap != null)
				{
					//判断找到map中存在的最大等级和解封时间
					Integer[] levelObjects = fengyinMap.keySet().toArray(new Integer[0]);
					int levels[] = new int[levelObjects.length];
					for(int i=0; i<levelObjects.length; i++)
					{
						levels[i] = levelObjects[i].intValue();
					}
					
					Arrays.sort(levels);
					
					long jiangeshijian = fengyinTimeInfo.get(levels[levels.length-1]);
					long maxlevelsealTime = fengyinMap.get(levels[levels.length-1]);
					if(maxlevelsealTime + jiangeshijian >= System.currentTimeMillis())
					{
						//去游戏服获取解封时间 更新对象 并重新拿fengyinMap
						
						
						URL url = new URL(serverHost);
						Map headers = new HashMap();
						String content = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p";
						byte[] bytes =  HttpUtils.webPost(url, content.getBytes(), headers, 60000, 60000);
						
						String fengyininfo = new String(bytes);
						
						Map<String,Map> fengyininfoMap = JsonUtil.objectFromJson(fengyininfo,Map.class);
						
						Iterator<String> it = fengyininfoMap.keySet().iterator();
						while(it.hasNext())
						{
							String key = it.next();
							Map map = fengyininfoMap.get(key);
							fengyinMap.put(Integer.decode(key), (Long)map.get("sealCanOpenTime"));
						}
						
						record.setFengyinTimeInfo(fengyinMap);
						
						ServerPubRecordManager.getInstance().saveOrUpdate(record);
						
					}
					
					
			%>
				
				<% 	
				for(int i=0; i<SEAL_LEVELS.length; i++)
				{
					 	Long fengyinOpenTime = fengyinMap.get(SEAL_LEVELS[i]);
					 	if(fengyinOpenTime != null)
					 	{
			%>
			<td>
						<%=DateUtil.formatDate(new Date(fengyinOpenTime), "yyyy-MM-dd HH:mm:ss") %>
			</td>
			<%  		
					 	}
					 	else
					 	{
			%>
			<td>
			</td>
			<% 		 	
					 	}
			%>
					
			<% 	 	}
				}
			
			
			%>	
				
		</tr>
<% 	
		}
		
	}

%>	


	</table><br/>
	<form method="post">
	<input type="hidden" name="isDo" value="true" />
		服务器和开服时间:<textarea rows="100" cols="100" name="serverpubs" value="<%=serverpubs%>"></textarea>[格式:服务器名=2013-09-19 16:00:00]<br>
		<input type="submit" value="提交">
	</form>
</body>
</html>
