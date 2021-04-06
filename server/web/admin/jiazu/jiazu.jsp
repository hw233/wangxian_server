<%@page import="com.fy.engineserver.core.JiazuSubSystem"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.jiazu.JiazuTitle"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember4Client"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String jiazuIds = request.getParameter("jiazuId");
	Jiazu jiazu = null;//
	if (jiazuIds == null || !"".equals(jiazuIds)) {
		jiazu = JiazuManager.getInstance().getJiazu(Long.valueOf(jiazuIds));
		if (jiazu == null) {
			out.print("[不存在的家族,ID:" + jiazuIds + "]");
			return;
		}
	}
%>

<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>家族信息</title>
<script type='text/javascript' src='../js/jquery.min.js'></script>
<script type="text/javascript">
$(document).ready(function(){
	  $("#cangku").hide();
	  $("#xianshi").click(function(){
	    $("#cangku").toggle();
	  });
});
</script>
</head>
<body>
	<%
		if (jiazu != null) {
			SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazu.getJiazuID());
			boolean hasSeptstation = septStation != null;
	%>
	<%
		String action = request.getParameter("action");
		if (action != null && action.equals("aaaa")) {
			if (jiazu != null) {
				jiazu.setLastJiazuSilverCartime(0);
			}
		}else if (action != null && action.equals("bbbb")) {
			JiazuSubSystem.LEAVE_JIAZU_PENALTY_TIME = 5*60*1000L;
		}
	%>
	<form>
		<input type="hidden" name="action" value="aaaa">
		<input type="hidden" name="jiazuId" value="<%=jiazu.getJiazuID()%>">
		<input type="submit" value="点击">
	</form>
	<form>
		<input type="hidden" name="action" value="bbbb">
		<input type="submit" value="修改24小时为5">
	</form>
	基本信息,家族创建时间:<%=TimeTool.formatter.varChar19.format(jiazu.getCreateTime()) %>
	<BR />
	<table style="font-size: 14px; text-align: right;" border="1">
		<tr>
			<td style="background-color: #81B4E7">ID</td>
			<td><%=jiazu.getJiazuID()%></td>
			<td style="background-color: #81B4E7">名字</td>
			<td><%=jiazu.getName()%></td>
			<td style="background-color: #81B4E7">国家</td>
			<td><%=jiazu.getCountry()%></td>
			<td style="background-color: #81B4E7">等级</td>
			<td><%=jiazu.getLevel()%></td>
			<td style="background-color: #81B4E7">族长</td>
			<td><%=jiazu.getJiazuMaster()%></td>
			<td style="background-color: #81B4E7">宗派ID/NAME</td>
			<%
				if (jiazu.getZongPaiId() > 0) {
						ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(jiazu.getZongPaiId());
			%>
				<td><%=jiazu.getZongPaiId() +"/" + (zp == null ? "NULL" : zp.getZpname())  %></td>
			<%
				} else {
			%>
			<td>--/--</td>
			<%} %>
			<td style="background-color: #81B4E7">驻地ID</td>
			<td><%=jiazu.getBaseID()%></td>
			<td style="background-color: #81B4E7">上次召唤boss时间</td>
			<td><%=jiazu.getLastCallbossTime()%></td>
			<td style="background-color: #81B4E7">家族个人工资上限</td>
			<td><%=jiazu.getSingleMemberSalaryMax()%></td>
		</tr>
		<tr>
			<td style="background-color: #81B4E7">维护费</td>
			<td><%=hasSeptstation ? septStation.getInfo().getCurrMaintai() : "--"%></td>
			<td style="background-color: #81B4E7">家族资金</td>
			<td><%=BillingCenter.得到带单位的银两(jiazu.getJiazuMoney())%></td>
			<td style="background-color: #81B4E7">家族灵脉</td>
			<td><%=BillingCenter.得到带单位的银两(jiazu.getConstructionConsume())%></td>
			<td style="background-color: #81B4E7">当前人数</td>
			<td><%=jiazu.getMemberNum()%></td>
			<td style="background-color: #81B4E7">人数上限</td>
			<td><%=jiazu.maxMember()%></td>
			<td style="background-color: #81B4E7">繁荣度</td>
			<td><%=jiazu.getProsperity()%></td>
			<td style="background-color: #81B4E7">密码</td>
			<td><%=jiazu.getJiazuPassword()%></td>
			<td style="background-color: #81B4E7">家族剩余工资</td>
			<td><%=jiazu.getSalaryLeft()%></td>
		</tr>
	</table>
	<HR />
	<HR />
	成员列表
	<BR />
	<a href="./septStation.jsp?jiazuId=<%=jiazuIds%>">去驻地看看</a>
	<HR />
	<HR />
	<BR />
	<table style="font-size: 14px; text-align: right;" border="1">
		<tr style="background-color: #81B4E7">
			<td>成员ID</td>
			<td>角色ID</td>
			<td>角色名字</td>
			<td>角色等级</td>
			<td>角色性别</td>
			<td>职务</td>
			<td>职务名字</td>
			<td>上周贡献</td>
			<td>本周贡献</td>
			<td>本周工资</td>
			<td>本周领取工资</td>
			<td>工资是否领取</td>
			<td>是否在线</td>
		</tr>
		<%
			Set<JiazuMember> jiazuMemberSet = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
				ArrayList<JiazuMember4Client> list = jiazu.getMember4Clients();
				for (JiazuMember member : jiazuMemberSet) {
					if (member == null) {
						out.print("[含有空的成员M]");
						continue;
					}
					JiazuMember4Client jmc = null;
					for (JiazuMember4Client c : list) {
						if (c != null) {
							if (c.getPlayerId() == member.getPlayerID()) {
								jmc = c;
								break;
							}
						} else {
							out.print("[含有空的成员C]");
						}
					}
		%>
		<tr style="background-color:<%=getTitleColor(member.getTitle())%>"
			title="<%=getTitleColor(member.getTitle())%>">
			<td><%=member.getJiazuMemID()%></td>
			<td><%=member.getPlayerID()%></td>
			<td><%=jmc == null ? "" : jmc.getPlayerName()%></td>
			<td><%=jmc == null ? "" : jmc.getPlayerLevel()%></td>
			<td><%=jmc == null ? "" : jmc.getSex()%></td>
			<td><%=member.getTitleIndex()%></td>
			<td><%=jiazu.getTitleName(member.getTitle())%></td>
			<td><%=member.getLastWeekContribution()%></td>
			<td><%=member.getCurrentWeekContribution()%></td>
			<td><%=jiazu.getSinglePlayerSalary(member.getPlayerID())%></td>
			<td><%=member.getCurrentSalary()%></td>
			<td><%=member.isPaid()%></td>
			<td><%=jmc == null ? "--" : jmc.isOnLine()%></td>
		</tr>
		<%
			}
		%>
	</table>
	<%
		}
	%>
	<input type="button" value="显示仓库物品" id="xianshi">
	<%
		ArrayList<Cell> wareHouse = jiazu.getWareHouse();
		Iterator<Cell> iterator = wareHouse.iterator();
		boolean found = false;
		out.print("<table id='cangku'>");
		while(iterator.hasNext()){
			Cell cell = iterator.next();
			ArticleEntityManager aem = ArticleEntityManager.getInstance();
			ArticleEntity ae = aem.getEntity(cell.getEntityId());
			if(null == ae){
				out.print("没有该id对应的物品 id:"+cell.getEntityId());
			}else{
				out.print("<tr><td>"+ae.getArticleName()+"("+ae.getReferenceCount()+")"+"</td><td>"+cell.getEntityId()+"("+ae.getColorType()+")</td></tr>");
				found = true;
			}
		}
		out.print("</table>");
		if(!found){
			out.print("家族仓库里没有东西哦!");
		}
	%>
	<%!String[] color = { "#F16623", "#E174ED", "#69ACF5", "#00CC00", "#FFFFFF" };%>
	<%!String getTitleColor(JiazuTitle jiazuTitle) {
		String res = color[color.length - 1];
		switch (jiazuTitle) {
		case master:
			res = color[0];
			break;
		case vice_master:
			res = color[1];
			break;
		case law_elder:
		case fight_elder:
		case research_elder:
			res = color[2];
			break;
		case elite:
			res = color[3];
			break;
		case civilian:
			res = color[4];
			break;
		}
		return res;
	}%>
</body>
</html>