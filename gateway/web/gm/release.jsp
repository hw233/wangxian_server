<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,com.fy.boss.authorize.model.*,com.fy.boss.client.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,com.fy.gamegateway.mieshi.server.*"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.gamegateway.mieshi.waigua.*"%>
<%
MieshiServerInfoManager msm = MieshiServerInfoManager.getInstance();
ArrayList<MieshiServerInfoManager.DenyUser> ulist = msm.getDenyUserList();
MieshiServerInfoManager.DenyUser us[] = ulist.toArray(new MieshiServerInfoManager.DenyUser[0]);
int num = 0;
for(MieshiServerInfoManager.DenyUser du : us) {
	if(du.reason != null && du.reason.equals("您因为打金工作室行为被封禁")) {
		msm.removeDenyUser(du.username);
		out.println("[解封用户] ["+du.username+"] ["+du.reason+"]<br>");
		System.out.println("[解封用户] ["+du.username+"] ["+du.reason+"]");
		num++;
	}
}
out.println("共解封" + num + "个账号");
%>