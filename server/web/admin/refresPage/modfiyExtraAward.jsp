<%@page import="com.fy.engineserver.notice.NoticeManager"%>
<%@page import="com.fy.engineserver.notice.NoticeForever"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager"%>
<%@page import="com.fy.engineserver.activity.ExtraAwardActivity"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%><html>
  <head>
    <title>错误页面</title>
  </head>
  <body>
  	<p>
  		<% 
  		List<ExtraAwardActivity> extraAwaList=ExchangeActivityManager.getInstance().getExtraAwaList();
  		List<ExtraAwardActivity> removeList=new ArrayList<ExtraAwardActivity>();
  		for(ExtraAwardActivity ea:extraAwaList){
  			String name=ea.getName()[0];
  			if(name.equals("偷砖")||name.equals("刺探")||name.equals("情缘")||name.equals("神农-访仙树(10体)")){
  				removeList.add(ea);
  			}
  		}
  		out.print("找到要删除的配置"+removeList.size()+"条<br>");
  		if(removeList.size()>0){
  			for(ExtraAwardActivity ea:removeList){
  				extraAwaList.remove(ea);
  			}
  		}
  		ActivityProp[] props=new ActivityProp[1];
  		props[0]=new ActivityProp("挖宝锦囊", 3, 1, true);
  		ExtraAwardActivity eaa = new ExtraAwardActivity("group", new String[]{"偷砖","刺探"}, new String[]{"偷砖","刺探"}, false, 0, false, "sqage", "", "", "2013-12-06 00:00:00", "2013-12-14 23:59:59", 0, props, "恭喜您获得挖宝大乐透奖励", "请查收附件，领取奖励");
		extraAwaList.add(eaa);
		ExchangeActivityManager.getInstance().setExtraAwaList(extraAwaList);
		out.print("完成");
		
		out.print("平台"+eaa.getPlatForms()+"-----------开放服"+eaa.getOpenServers()+"-----限制服"+eaa.getLimitServers());
		ServerFit4Activity sf4a=new ServerFit4Activity(eaa.getPlatForms(), eaa.getOpenServers(), eaa.getLimitServers());
		out.print(sf4a.thiserverFit());
  		%>
  		
  		
  	</p>
  </body>
</html>
