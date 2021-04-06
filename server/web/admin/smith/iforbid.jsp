<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,com.fy.engineserver.sprite.*,com.fy.engineserver.smith.*,com.xuanzhi.tools.text.*"%><%     
MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
ArticleRelationShipManager arm = ArticleRelationShipManager.getInstance();
String id = request.getParameter("playerid");
String name = request.getParameter("playername");
Player p = null;
if(id != null && id.trim().length() > 0) {
	p = PlayerManager.getInstance().getPlayer(Long.valueOf(id));
} else if(name != null && name.trim().length() > 0) {
	p = PlayerManager.getInstance().getPlayer(name);
}
String action = request.getParameter("action");
if(action != null && action.equals("release") && p != null) {
	msm.getForbidPlayers().remove(p.getId());
	arm.getForbidPlayers().remove(p.getId());
}
boolean forbid = false;
if(p != null) {
	if(msm != null && msm.isForbid(p.getId())) {
		forbid = true;
	}
	if(arm != null && arm.isForbid(p.getId())) {
		forbid = true;
	}
}
%><br><h1>查询角色是否被打金工作室封禁</h1>
<form action="iforbid.jsp" name=f1>
	角色名:<input type=text name=playername size=12> 或 ID:<input type=text name=playerid size=12> <input type=submit name=sub1 value=" 提交 ">
</form><br><br>
<%
if(p != null) {
%>
账号：<%=p.getUsername() %>，角色名: <%=p.getName() %>, 角色ID: <%=p.getId() %>，当前银两：<%=p.getSilver()/1000000 %>锭， <font color=red><b><%=forbid?"被封禁":"未封禁" %></b></font>
<%if(forbid) {%>
<input type=button name=bt1 value=" 解封 " onclick="window.location.replace('iforbid.jsp?action=release&playerid=<%=p.getId() %>')">
<%} %>
<%
}
%>