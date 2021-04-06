<%@page import="java.net.URL"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfo"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiPlayerInfoManager"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.gamegateway.gmaction.GmActionManager"%>
<%@page import="com.fy.gamegateway.gmaction.GmAction"%>
<%@page import="com.fy.gamegateway.mieshi.server.MieshiServerInfoManager"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%
Map<String, Object> result = new HashMap<String, Object>();
try{
	String userName  = request.getParameter("userName");
	String innerReason = URLDecoder.decode(request.getParameter("innerReason"),"utf-8");
	String reason =  URLDecoder.decode(request.getParameter("reason"),"utf-8");
	String hours = request.getParameter("hours");
	Integer hour = Integer.valueOf(hours);
	String gmUserName =  URLDecoder.decode(request.getParameter("gmUserName"),"utf-8");
	MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
	
	msm.addDenyUser("","",false,false,userName,reason,innerReason,gmUserName,hour==0?true:false,hour/24,hour%24);
	
	
	GmAction ga = new GmAction();
	ga.setActionTime(System.currentTimeMillis());
	ga.setActionType(GmAction.ACTION_FORBID_ACCOUNT);
	ga.setAmount(0);
	ga.setArticleInfo("");
	ga.setOperator(gmUserName);
	ga.setOtherInfos(new String[0]);
	ga.setPetInfo("");
	ga.setPlayerId(0L);
	ga.setPlayerName("");
	ga.setReason(innerReason);
	ga.setServerName("");
	ga.setUserName(userName);
	GmActionManager gmmm = GmActionManager.getInstance();
	if(gmmm != null){
		gmmm.createGmAction(ga);
	}
	
	if(result.get("result") == null)
	{
		result.put("result", "success");
	}
	
	MieshiPlayerInfoManager mieshiPlayerInfoManager = MieshiPlayerInfoManager.getInstance();

	MieshiPlayerInfo[] mieshiPlayerInfos = mieshiPlayerInfoManager.getMieshiPlayerInfoByUsername(userName);
	
	Map<String,String> failKickPlayerMap = new HashMap<String,String>();
	String failKey = "failResults";
	for(MieshiPlayerInfo mieshiPlayerInfo: mieshiPlayerInfos)
	{
		/*
		paramMap.put("authorize.username", "migu_trade");
		paramMap.put("authorize.password", "gmsqagewx");
		*/
		String servername = mieshiPlayerInfo.getServerRealName();
		MieshiServerInfo msi =  msm.getServerInfoByName(servername);
		String visitUrl = msi.getServerUrl()+"/admin/yunying/kickplayer.jsp?authorize.username=wangtianxin&authorize.password=123321&roleid="+mieshiPlayerInfo.getPlayerId();
		String content = "";
		Map headers = new HashMap();
		byte bytes[] = HttpUtils.webGet(new URL(visitUrl), headers, 60000, 60000);

		String encoding = (String)headers.get(HttpUtils.ENCODING);
		Integer code = (Integer)headers.get(HttpUtils.Response_Code);
		String message = (String)headers.get(HttpUtils.Response_Message);
		String re = new String(bytes,encoding);
		if(re == null || !re.contains("success"))
		{
			failKickPlayerMap.put(servername+"_"+mieshiPlayerInfo.getPlayerId(),mieshiPlayerInfo.getPlayerName() );
			result.put(failKey, failKickPlayerMap); 	
		}
	}

	
}catch(Exception e){
	if(result.get("result") == null)
	{
		result.put("result", "fail");
	}
}


	

out.print(JsonUtil.jsonFromObject(result));

%>
