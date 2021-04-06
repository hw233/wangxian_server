<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.activity.base.AddRateActivity"%>
<%@page import="com.fy.engineserver.activity.expactivity.DailyfExpActivity"%>
<%@page import="com.fy.engineserver.activity.shop.ArticleActivityOfUseAndGive"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivity"%>
<%@page import="com.fy.engineserver.activity.base.TimesActivityManager"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityOfBuyAndGive"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivity"%>
<%@page
	import="com.fy.engineserver.activity.shop.ShopActivityManager"%>
<%@page
	import="com.fy.engineserver.datasource.skill.activeskills.SkillWithoutTraceAndWithRange"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page
	import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.event.cate.EventWithObjParam"%>
<%@page import="com.fy.engineserver.event.Event"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page
	import="com.fy.engineserver.datasource.skill.SkillInfoHelper"%>
<%@page import="com.fy.engineserver.datasource.skill.ActiveSkill"%>
<%@page import="com.fy.engineserver.datasource.career.Career"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.message.SkEnh_exINFO_RES"%>
<%@page import="com.fy.engineserver.message.SkEnh_exINFO_REQ"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="java.util.Iterator"%>
<%@page
	import="com.fy.engineserver.datasource.skill.master.SkEnConf"%>
<%@page import="com.fy.engineserver.message.SkEnh_Exp2point_RES"%>
<%@page import="com.fy.engineserver.message.SkEnh_Exp2point_REQ"%>
<%@page import="com.fy.engineserver.message.SkEnh_Add_point_RES"%>
<%@page import="com.fy.engineserver.message.SkEnh_Add_point_REQ"%>
<%@page
	import="com.fy.engineserver.datasource.skill.master.ExchangeConf"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.SkEnh_INFO_REQ"%>
<%@page import="com.fy.engineserver.message.SkEnh_INFO_RES"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.sprite.pet2.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		long now = System.currentTimeMillis();
		String color_red = "#CD0000";
		String color_yellow = "#EEC900";
		String color_greew = "#008B00";
		String color_blue = "#000080";
		String color_black = "#000000";
		
		
		String newEndTime = request.getParameter("newEndTime");
		String newStartTime = request.getParameter("newStartTime");
		String password = request.getParameter("password");
		newEndTime = newEndTime == null ? "" : newEndTime;
		newStartTime = newStartTime == null ? "" : newStartTime;
		
		if(!"".equals(newEndTime) || !"".equals(newStartTime)) {
			//if(!"qweasd123".equals(password)) {
			//	out.println("[密码不对，请联系技术人员索取密码！]<br>");			
			//	return ;
			//}
			password = null;
			//out.println("===新开始时间 : " + newStartTime + "<br>");
			//out.println("===新结束时间 : " + newEndTime + "<br>");
			long neTime = TimeTool.formatter.varChar19.parse(newEndTime);
			long sTime = TimeTool.formatter.varChar19.parse(newStartTime);
			int modifyType = Integer.valueOf(request.getParameter("modifyType"));
			int modifyIndex = Integer.valueOf(request.getParameter("modifyIndex"));
			switch(modifyType) {
			case 0:												//商店活动修改
				ShopActivityManager sam1 = ShopActivityManager.getInstance();
				List<ShopActivity> list = sam1.getShopActivities();
				ShopActivity tempS = list.get(modifyIndex);
				if(!"".equals(newEndTime) && neTime > 0) {
					tempS.setEndTime(neTime);
				}
				if(!"".equals(newStartTime) && sTime > 0) {
					tempS.setStartTime(sTime);
				}
				list.set(modifyIndex, tempS);
				sam1.setShopActivities(list);
				out.println("[商店及使用赠送活动] [修正活动结束时间成功] [ 调整后活动开始时间为:" + (new Timestamp(ShopActivityManager.getInstance().getShopActivities().get(modifyIndex).getStartTime())) + "<br>");
				out.println("[商店及使用赠送活动] [修正活动结束时间成功] [ 调整后活动结束时间为:" + (new Timestamp(ShopActivityManager.getInstance().getShopActivities().get(modifyIndex).getEndTime())) + "<br>");
				break;
			case 1:												//酒贴使用次数活动修改
				TimesActivityManager tm = TimesActivityManager.instacen;
				TimesActivity tempT = tm.activitys.get(modifyIndex);
				if(!"".equals(newEndTime) && neTime > 0) {
					tempT.endTime = neTime;
				}
				if(!"".equals(newStartTime) && sTime > 0) {
					tempT.startTime = sTime;
				}
				tm.activitys.set(modifyIndex, tempT);
				out.println("[酒贴祈福次数增加活动] [修正活动结束时间成功] [ 调整后活动开始时间为:" + (new Timestamp(tm.activitys.get(modifyIndex).startTime)) + "<br>");
				out.println("[酒贴祈福次数增加活动] [修正活动结束时间成功] [ 调整后活动结束时间为:" + (new Timestamp(tm.activitys.get(modifyIndex).endTime)) + "<br>");
				break;
			case 2:												//答题经验
				break;
			case 3:												//传功经验
				break;
			case 4:												//屠魔贴经验
				break;
			case 5:												//所有经验
				break;
			}
		}
	%>
	<h2>商店、次数、倍数活动后台</h2>

	<form action="shopActivityShow.jsp" method="post">
		<input type="hidden" name="action" value="showConf" /> <input
			type="submit" value="查看商店、使用赠送活动配置" />
	</form>
	<br />
	<form action="shopActivityShow.jsp" method="post">
		<input type="hidden" name="action" value="showConf2" /> <input
			type="submit" value="查看增加酒、贴次数活动配置" />
	</form>
	<br />
	<form action="shopActivityShow.jsp" method="post">
		<input type="hidden" name="action" value="showConf3" /> <input
			type="submit" value="查看经验等倍数活动配置" />
	</form>
	<br />
	<%
		if ("showConf".equals(action)) {
	%>
		<body>
			<table border=1 align="center" width="90%">
				<tr>
					<th><font color='red'>活动类型</font></th>
					<th>开始时间 </th>
					<th>结束时间</th>
					<th>活动时间修正</th>
					<th>开放平台</th>
					<th>开放服务器</th>
					<th>不开放服务器</th>
					<th><font color='red'>本服是否开放</font></th>
					<th><font color='red'>是否正常开放</font></th>
					<th>商店名</th>
					<th>需要的物品</th>
					<th>赠送的物品</th>
				</tr>
				<%
				ShopActivityManager sam = ShopActivityManager.getInstance();
				List<ShopActivity> list = sam.getShopActivities();
				String color3 = color_black;
				for(int ii =0; ii<list.size(); ii++) {
				//for (ShopActivity s : list) {
					ShopActivity s = list.get(ii);
					ServerFit4Activity fit = (ServerFit4Activity) s.getServerFit();
					String actType = (s instanceof ShopActivityOfBuyAndGive) ? "购买赠送活动" : "使用赠送活动";
					color3 = (s instanceof ShopActivityOfBuyAndGive) ? color_blue : color_black;
					String shopName = "";
					String needArti = "";
					String giveArti = "";
					String platf = "";
					for(Platform p : fit.getPlatForms()) {
						platf += p;
						platf += "、";
					}
					String openServer = "";
					if(fit.getOpenServerNames() != null) {
						for(String ss : fit.getOpenServerNames()) {
							openServer += ss;
							openServer += "、";
						}
					}
					String notopenServer = "";
					if(fit.getNotOpenServerNames() != null){
						for(String ss : fit.getNotOpenServerNames()) {
							notopenServer += ss;
							notopenServer += "、";
						}
					}
					if(s instanceof ShopActivityOfBuyAndGive) {
						shopName = ((ShopActivityOfBuyAndGive)s).getShopName();
						needArti = ((ShopActivityOfBuyAndGive)s).getNeedBuyProp().toString();
						giveArti = ((ShopActivityOfBuyAndGive)s).getGiveProp().toString();
					} else if(s instanceof ArticleActivityOfUseAndGive){
						needArti = ((ArticleActivityOfUseAndGive)s).getNeedBuyProp().toString();
						giveArti = ((ArticleActivityOfUseAndGive)s).getGiveProp().toString();
					}
					String color = color_black;
					String color2 = color_black;
					String result = "";
					if(fit.thisServerOpen()) {
						color = color_red;
						if (now < s.getStartTime() || now > s.getEndTime()) {
							color2 = color_red;
							result = "时间不满足";
						} else {
							color2 = color_greew;
							result = "正常开放";
						}
					}
					%>
					<tr>
						<td style="color:<%=color3%>"><%=actType %></td>
						<td><%=new Timestamp(s.getStartTime()) %></td>
						<td><%=new Timestamp(s.getEndTime()) %></td>
						<td><form action="shopActivityShow.jsp" method="post">
								开始时间:<input type="text" name="newStartTime" value=<%=newStartTime %>></input>
								<br>结束时间:<input type="text" name="newEndTime" value=<%=newEndTime %>></input>
								<input type="hidden" name="modifyIndex" value=<%=ii %>></input>
								<input type="hidden" name="modifyType" value=0></input>
								<input type="submit" value="活动时间修正"></input>
							</form>
						</td>
						<td><%=platf %></td>
						<td><%=openServer %></td>
						<td><%=notopenServer %></td>
						<td style="color:<%=color%>"><%=fit.thisServerOpen() %></td>
						<td style="color:<%=color2%>"><%=result %></td>
						<td><%=shopName %></td>
						<td><%=needArti %></td>
						<td><%=giveArti %></td>
					</tr>
				<% 
				}
				%>
			</table>
			</body>
	<%
		} else if("showConf2".equals(action)) {
	%>
			<table border=1 align="center" width="90%">
			<tr>
				<th><font color='red'>活动类型</font></th>
				<th>开始时间 </th>
				<th>结束时间</th>
				<th>活动时间修正</th>
				<th>开放平台</th>
				<th>开放服务器</th>
				<th>不开放服务器</th>
				<th><font color='red'>本服是否开放(true表示活动已开始)</font></th>
				<th>等级限制</th>
				<th>增加次数</th>
			</tr>
	<%
			TimesActivityManager tm = TimesActivityManager.instacen;
			for(int ii=0; ii<tm.activitys.size(); ii++) {
			//for (TimesActivity t : tm.activitys) {
				TimesActivity t = tm.activitys.get(ii);
				String actType = "";
				String platf = "";
				String openServer = "";
				String notopenServer = "";
				String levelLimit = "";
				String muls = "1";
				for(Platform p : t.getPlatForms()) {
					platf += p;
					platf += "、";
				}
				if(t.getServerNames() != null){
					for(String ss : t.getServerNames()) {
						openServer += ss;
						openServer += "、";
					}
				}
				if(t.getUnServerNames() != null){
					for(String ss : t.getUnServerNames()) {
						notopenServer += ss;
						notopenServer += "、";
					}
				}
				levelLimit = t.getLevellimit() + "";
				muls = t.getAddNum() + "";
				String tColor = color_blue;
				if("喝酒".equals(t.getType())) {
					tColor = color_yellow;
				} else if ("屠魔贴".equals(t.getType())) {
					tColor = color_black;
				}
				String tColor2 = color_red;
				if(t.CanAdd()) {
					tColor2 = color_greew;
				}
		%>
				<tr>
					<td style="color:<%=tColor%>"><%=t.getType() %></td>
					<td><%=new Timestamp(t.startTime) %></td>
					<td><%=new Timestamp(t.endTime) %></td>
					<td><form action="shopActivityShow.jsp" method="post">
							开始时间:<input type="text" name="newStartTime" value=<%=newStartTime %>></input>
							<br>结束时间:<input type="text" name="newEndTime" value=<%=newEndTime %>></input>
							<input type="hidden" name="modifyIndex" value=<%=ii %>></input>
							<input type="hidden" name="modifyType" value=1></input>
							<input type="submit" value="活动时间修正"></input>
						</form>
					</td>
					<td><%=platf %></td>
					<td><%=openServer %></td>
					<td><%=notopenServer %></td>
					<td style="color:<%=tColor2%>"><%=t.CanAdd() %></td>
					<td><%=levelLimit %></td>
					<td><%=muls %></td>
				</tr>
		<% 
				}
		%>
		</table>		
	<%
		} else if("showConf3".equals(action)) {
	%>
		<table border=1 align="center" width="80%">
		<tr>
			<th><font color='red'>活动类型</font></th>
			<th>开始时间 </th>
			<th>结束时间</th>
			<th>活动时间修正</th>
			<th>开放平台</th>
			<th>开放服务器</th>
			<th>不开放服务器</th>
			<th><font color='red'>本服是否开启</font></th>
			<th><font color='red'>是否正常开放</font></th>
			<th>地图限制</th>
			<th>增加比率</th>
		</tr>
		<%
		ActivityManager am = ActivityManager.getInstance();
		String actType = "";
		String platf = "";
		String openServer = "";
		String notopenServer = "";
		String limitMaps = "";
		String muls = "1";
		for(int i=0; i<am.getExpActivityList().size(); i++) {		//所有经验翻倍活动
			actType = "所有经验";
			platf = "";
			DailyfExpActivity dd = (DailyfExpActivity)am.getExpActivityList().get(i);
			ServerFit4Activity fit = (ServerFit4Activity)dd.getServersConfig();
			for(Platform p : fit.getPlatForms()) {
				platf += p;
				platf += "、";
			}
			if(fit.getOpenServerNames() != null){
				for(String ss : fit.getOpenServerNames()) {
					openServer += ss;
					openServer += "、";
				}
			}
			if(fit.getNotOpenServerNames() != null){
				for(String ss : fit.getNotOpenServerNames()) {
					notopenServer += ss;
					notopenServer += "、";
				}
			}
			if(dd.getLimitmaps() != null) {
				for(String sss : dd.getLimitmaps()) {
					limitMaps += sss;
					limitMaps += "、";
				}
			}
			muls = dd.getExpRate() + "";
			String maColor1 = color_red;
			if(fit.thisServerOpen()) {
			}
			%>
			<tr>
				<td style="color:<%=color_black%>"><%=actType %></td>
				<td><%=new Timestamp(dd.getStartTime()) %></td>
				<td><%=new Timestamp(dd.getEndTime()) %></td>
				<td><form action="shopActivityShow.jsp" method="post">
						开始时间:<input type="text" name="newStartTime" value=<%=newStartTime %>></input>
						<br>结束时间:<input type="text" name="newEndTime" value=<%=newEndTime %>></input>
						<input type="hidden" name="modifyIndex" value=<%=i %>></input>
						<input type="hidden" name="modifyType" value=5></input>
						<input type="submit" value="活动时间修正"></input>
					</form>
				</td>
				<td><%=platf %></td>
				<td><%=openServer %></td>
				<td><%=notopenServer %></td>
				<td><%=fit.thisServerOpen() %></td>
				<td><%=fit.thisServerOpen() %></td>
				<td><%=limitMaps %></td>
				<td><%=muls %></td>
			</tr>
			<% 
		}
		for(int i=0; i<am.getTumotieActivityList().size(); i++) {		//所有经验翻倍活动
			actType = "屠魔贴经验";
			platf = "";
			DailyfExpActivity dd = (DailyfExpActivity)am.getExpActivityList().get(i);
			ServerFit4Activity fit = (ServerFit4Activity)dd.getServersConfig();
			for(Platform p : fit.getPlatForms()) {
				platf += p;
				platf += "、";
			}
			if(fit.getOpenServerNames() != null){
				for(String ss : fit.getOpenServerNames()) {
					openServer += ss;
					openServer += "、";
				}
			}
			if(fit.getNotOpenServerNames() != null){
				for(String ss : fit.getNotOpenServerNames()) {
					notopenServer += ss;
					notopenServer += "、";
				}
			}
			if(dd.getLimitmaps() != null) {
				for(String sss : dd.getLimitmaps()) {
					limitMaps += sss;
					limitMaps += "、";
				}
			}
			muls = dd.getExpRate() + "";
			%>
			<tr>
				<td style="color:<%=color_yellow%>"><%=actType %></td>
				<td><%=new Timestamp(dd.getStartTime()) %></td>
				<td><%=new Timestamp(dd.getEndTime()) %></td>
				<td><form action="shopActivityShow.jsp" method="post">
						开始时间:<input type="text" name="newStartTime" value=<%=newStartTime %>></input>
						<br>结束时间:<input type="text" name="newEndTime" value=<%=newEndTime %>></input>
						<input type="hidden" name="modifyIndex" value=<%=i %>></input>
						<input type="hidden" name="modifyType" value=2></input>
						<input type="submit" value="活动时间修正"></input>
					</form>
				</td>
				<td><%=platf %></td>
				<td><%=openServer %></td>
				<td><%=notopenServer %></td>
				<td><%=fit.thisServerOpen() %></td>
				<td><%=limitMaps %></td>
				<td><%=muls %></td>
			</tr>
			<% 
		}
		for(int i=0; i<am.getChuanGongActivity().size(); i++) {		//所有经验翻倍活动
			actType = "传功";
			platf = "";
			AddRateActivity dd = am.getChuanGongActivity().get(i);
			ServerFit4Activity fit = (ServerFit4Activity)dd.getFit();
			for(Platform p : fit.getPlatForms()) {
				platf += p;
				platf += "、";
			}
			if(fit.getOpenServerNames() != null){
				for(String ss : fit.getOpenServerNames()) {
					openServer += ss;
					openServer += "、";
				}
			}
			if(fit.getNotOpenServerNames() != null) {
				for(String ss : fit.getNotOpenServerNames()) {
					notopenServer += ss;
					notopenServer += "、";
				}
			}
			muls = dd.getAddRate() + "";
			%>
			<tr>
				<td style="color:<%=color_black%>"><%=actType %></td>
				<td><%=new Timestamp(dd.getStartTime()) %></td>
				<td><%=new Timestamp(dd.getEndTime()) %></td>
				<td><form action="shopActivityShow.jsp" method="post">
							开始时间:<input type="text" name="newStartTime" value=<%=newStartTime %>></input>
						<br>结束时间:<input type="text" name="newEndTime" value=<%=newEndTime %>></input>
						<input type="hidden" name="modifyIndex" value=<%=i %>></input>
						<input type="hidden" name="modifyType" value=3></input>
						<input type="submit" value="活动时间修正"></input>
					</form>
				</td>
				<td><%=platf %></td>
				<td><%=openServer %></td>
				<td><%=notopenServer %></td>
				<td><%=fit.thisServerOpen() %></td>
				<td><%=limitMaps %></td>
				<td><%=muls %></td>
			</tr>
			<% 
		}
		for(int i=0; i<am.getDatiActivity().size(); i++) {		//所有经验翻倍活动
			actType = "答题";
			platf = "";
			AddRateActivity dd = am.getDatiActivity().get(i);
			ServerFit4Activity fit = (ServerFit4Activity)dd.getFit();
			for(Platform p : fit.getPlatForms()) {
				platf += p;
				platf += "、";
			}
			if(fit.getOpenServerNames() != null){
				for(String ss : fit.getOpenServerNames()) {
					openServer += ss;
					openServer += "、";
				}
			}
			if(fit.getNotOpenServerNames() != null) {
				for(String ss : fit.getNotOpenServerNames()) {
					notopenServer += ss;
					notopenServer += "、";
				}
			}
			muls = dd.getAddRate() + "";
			%>
			<tr>
				<td><%=actType %></td>
				<td><%=new Timestamp(dd.getStartTime()) %></td>
				<td><%=new Timestamp(dd.getEndTime()) %></td>
				<td><form action="shopActivityShow.jsp" method="post">
						开始时间:<input type="text" name="newStartTime" value=<%=newStartTime %>></input>
						<br>结束时间:<input type="text" name="newEndTime" value=<%=newEndTime %>></input>
						<input type="hidden" name="modifyIndex" value=<%=i %>></input>
						<input type="hidden" name="modifyType" value=4></input>
						<input type="submit" value="修正结束时间"></input>
					</form>
				</td>
				<td><%=platf %></td>
				<td><%=openServer %></td>
				<td><%=notopenServer %></td>
				<td><%=fit.thisServerOpen() %></td>
				<td><%=limitMaps %></td>
				<td><%=muls %></td>
			</tr>
			<% 
		}
		%>
	</table>
	<%
		}
	%>
</body>
</html>
