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
		fengyinTimeInfo.put(1000, 180*24*3600*1000l);
	}

	static	int[] SEAL_LEVELS = new int[]{70,110,150,190,220,1000};


%>    

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

</head>
<body>
	<h2>服务器发布信息查看</h2>
	<table border="2px" style="font-size: 12px">
	<tr>
		<td width="10%">
			服务器名
		</td>
		<td width="10%">
			分区
		</td>
		<td width="10%">
			服务器类型
		</td>
		<td width="10%">
			开服时间
		</td>
			
		<td width="10%">
			70级解封时间
		</td>
		<td width="10%">
			110级解封时间
		</td>
		<td width="10%">
			150级解封时间
		</td>
		<td width="10%">
			190级解封时间
		</td>
		<td width="10%">
			220级解封时间
		</td>
		<td width="10%">
			1000级解封时间
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
	ServerPubRecordManager serverPubRecordManager =  ServerPubRecordManager.getInstance();
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
					
					
					List<ServerPubRecord> lst =  serverPubRecordManager.queryForWhere(" serverName = ? ", new Object[]{serverName});
					if(lst != null && lst.size() > 0)	
					{
						ServerPubRecord pubRecord = lst.get(0);
						pubRecord.setOpenServerTime(openServerTime);
						ServerPubRecordManager.getInstance().saveOrUpdate(pubRecord);
					}
					
					
				}
			}
			
			
		}
	}
		
	
	
	

%>
<%
	//查询信息


	List<ServerPubRecord> serverPubs = serverPubRecordManager.queryForWhere(" id > ? ", new Object[]{1});
	
	MieshiServerInfoManager mm = MieshiServerInfoManager.getInstance();
	String SERVERTYPE_NAMES[] = MieshiServerInfo.SERVERTYPE_NAMES;
	String categories[] = mm.getServerCategories();
	for(int i = 0 ; i < categories.length ; i++)
	{
		MieshiServerInfo sis[] = mm.getServerInfoSortedListByCategory(categories[i]);
		
		for(int j = 0 ; j < sis.length ; j++)
		{
			MieshiServerInfo si = sis[j];
			//http://116.213.193.70:8001/seal_info?authorize.username=innerapp&authorize.password=innerapp123
			///seal_info
			String serverHost = "http://"+si.getIp()+":"+si.getHttpPort()+"/seal_info";
					
			//查询是否数据库有关于此servername的记录 无则插入
			List<ServerPubRecord> lst =  serverPubRecordManager.queryForWhere(" serverName = ? ", new Object[]{si.getRealname()});
			ServerPubRecord record = null;
			if(lst == null || lst.size() < 1)
			{
			
					record = new ServerPubRecord();
					record.setServerName(si.getRealname());
					record.setCreateTime(System.currentTimeMillis());
					record.setServerType(SERVERTYPE_NAMES[si.getServerType()]);
					try
					{
						//获取封印时间
						URL url = new URL(serverHost);
						Map headers = new HashMap();
						String content = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p";
						byte[] bytes =  HttpUtils.webPost(url, content.getBytes(), headers, 60000, 60000);
						
						String fengyininfo = new String(bytes);
						
						Map<String,Map> fengyininfoMap = JsonUtil.objectFromJson(fengyininfo,Map.class);
						Map<Integer,Long> fengyinMap = new HashMap<Integer,Long>();				
						Iterator<String> it = fengyininfoMap.keySet().iterator();
						while(it.hasNext())
						{
							String key = it.next();
							Map map = fengyininfoMap.get(key);
							fengyinMap.put(Integer.decode(key), (Long)map.get("sealCanOpenTime"));
						}
						
						record.setFengyinTimeInfo(fengyinMap);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					serverPubRecordManager.saveOrUpdate(record);
				
			
			}
			else
			{
				record = lst.get(0);
			}
%>
		<tr>
			<td>
				<%=si.getRealname()%>
			</td>
			<td>
				<%= si.getCategory()%>
			</td>
			<td>
				<%= SERVERTYPE_NAMES[si.getServerType()]%>
			</td>
			<td>
				
			<%if(record != null && record.getOpenServerTime() != null){%><%= record.getOpenServerTime()%><%}else{ %><%} %>
			</td>
			
			<% 
				if(record != null)
				{
					Map<Integer,Long> fengyinMap = record.getFengyinTimeInfo();
				
					if(fengyinMap != null && fengyinMap.size() >= 1)
					{
						//判断找到map中存在的最大等级和解封时间
						Integer[] levelObjects = fengyinMap.keySet().toArray(new Integer[0]);
						int levels[] = new int[levelObjects.length];
						for(int k=0; k<levelObjects.length; k++)
						{
							levels[k] = levelObjects[k].intValue();
						}
						
						Arrays.sort(levels);
						
							
						
						long jiangeshijian = fengyinTimeInfo.get(levels[levels.length-1]);
						long maxlevelsealTime = fengyinMap.get(levels[levels.length-1]);
						if(maxlevelsealTime + jiangeshijian >= System.currentTimeMillis())
						{
							//去游戏服获取解封时间 更新对象 并重新拿fengyinMap
							try
							{
							
								URL url = new URL(serverHost);
								Map headers = new HashMap();
								String content = "authorize.username=serverUser&authorize.password=kj2#($1238!salkhdo978HGm).p";
								byte[] bytes =  HttpUtils.webPost(url, content.getBytes(), headers, 60000, 60000);
								
								String fengyininfo = new String(bytes);
								
								Map<String,Map> fengyininfoMap = JsonUtil.objectFromJson(fengyininfo,Map.class);
								Map<Integer,Long> fengyinMap2 = new HashMap<Integer,Long>();				
								Iterator<String> it = fengyininfoMap.keySet().iterator();
								while(it.hasNext())
								{
									String key = it.next();
									Map map = fengyininfoMap.get(key);
									fengyinMap2.put(Integer.decode(key), (Long)map.get("sealCanOpenTime"));
								}
								if(fengyinMap2 != null)
									record.setFengyinTimeInfo(fengyinMap2);
								serverPubRecordManager.saveOrUpdate(record);
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
							
							
							
						}
						
					}
				
					
					
			%>
				
			<% 	
				for(int k=0; k<SEAL_LEVELS.length; k++)
				{
					 	Long fengyinOpenTime = record.getFengyinTimeInfo().get((SEAL_LEVELS[k]));
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
					
			<% 	 }
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
