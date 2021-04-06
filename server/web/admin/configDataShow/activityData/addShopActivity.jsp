<%@page import="com.fy.engineserver.activity.shop.ShopActivityManager.RepayType"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivity"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityOfBuyAndGive"%>
<%@page import="com.fy.engineserver.util.config.ServerFit4Activity"%>
<%@page import="com.fy.engineserver.util.config.ServerFit"%>
<%@page import="com.fy.engineserver.activity.shop.ActivityProp"%>
<%@page import="com.fy.engineserver.activity.shop.ShopActivityManager"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page
	import="com.fy.engineserver.platform.PlatformManager.Platform"
%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.menu.activity.Option_Get_Reward_Only_One_Time"
%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page
	import="com.fy.engineserver.newBillboard.BillboardStatDateManager"
%>
<%@page import="com.fy.engineserver.newBillboard.BillboardDate"%>
<%@page import="java.util.*"%>
<%@ page
	language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<link
	rel="stylesheet"
	href="../../css/style.css"
/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!public String getPlatname(String name) {
		if (name.equals("官方")) {
			return "sqage";
		} else if (name.equals("腾讯")) {
			return "tencent";
		} else if (name.equals("台湾")) {
			return "taiwan";
		} else if (name.equals("马来")) {
			return "malai";
		} else if (name.equals("韩国")) {
			return "korea";
		}
		return "";
	}%>
<%
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	String platforms = request.getParameter("platforms");
	String openservers = request.getParameter("openservers");
	String unopenservers = request.getParameter("unopenservers");
	String shopname = request.getParameter("shopname");
	String stype = request.getParameter("stype");
	
	String artName = request.getParameter("artName");
	String artColor = request.getParameter("artColor");
	String artBind = request.getParameter("artBind");
	String artNum = request.getParameter("artNum");
	
	String giveName = request.getParameter("giveName");
	String giveColor = request.getParameter("giveColor");
	String giveBind = request.getParameter("giveBind");
	String giveNum = request.getParameter("giveNum");
	
	String mailTitile = request.getParameter("mailTitile");
	String mailContent = request.getParameter("mailContent");
	
	
	String pwd = request.getParameter("pwd");
	
	MenuWindow modifymenu = null;
	WindowManager wm = WindowManager.getInstance();
	MenuWindow[] windowMap = wm.getWindows();
	List<Option> list = new ArrayList<Option>();
	for (MenuWindow menu : windowMap) {
		if (menu != null) {
			if (menu.getId() == 30000) {
				modifymenu = menu;
				Option[] ops = menu.getOptions();
				for (Option o : ops) {
					list.add(o);
				}
			}
		}
	}
	
		if(pwd!=null && pwd.equals("4rfvbgt56yhnmju7")){
			if (startTime.trim().length() == 0 || endTime.trim().length() == 0 || artName.trim().length() == 0 || giveName.trim().length() == 0 || platforms.trim().length() == 0) {
				out.print("<h1><font color='red'>必填项不能为空！</font></h1>");
			} else {
				ShopActivityManager manager = ShopActivityManager.getInstance();
				ActivityProp needBuyProp = new ActivityProp(artName, Integer.parseInt(artColor), Integer.parseInt(artNum), Boolean.parseBoolean(artBind));
				ActivityProp giveProp = new ActivityProp(giveName, Integer.parseInt(giveColor), Integer.parseInt(giveNum), Boolean.parseBoolean(giveBind));
				openservers = openservers == null ? "" : openservers;
				unopenservers = unopenservers == null ? "" : unopenservers;
				ServerFit serverFit = new ServerFit4Activity(platforms, openservers, unopenservers);
				RepayType aaa = null;
				for(RepayType r : RepayType.values()) {
					if(r.type == Integer.parseInt(stype)) {
						aaa = r;
					}
				}
				ShopActivityOfBuyAndGive saob = new ShopActivityOfBuyAndGive(startTime, endTime, aaa, shopname,needBuyProp, giveProp, serverFit, mailTitile, mailContent);
				List<ShopActivity> list1 = manager.getShopActivities();
				list1.add(saob);
				manager.setShopActivities(list1);
			}
		}else{
			out.print("<B>请找相关人员索要认证码！</B>");
		}
%>
<html>
<head>
<meta
	http-equiv="Content-Type"
	content="text/html; charset=UTF-8"
>
<title>NPC窗口补偿</title>
</head>
<body>
	<form>
		<table>
			<h1>商店买送活动</h1>
			<tr>
				<th><font color='red'>请输入修改认证码：</font></th>
				<td><input
					type="password"
					name='pwd'
					value='<%=pwd == null ? "****" : "*****"%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>开启时间*</font>活动名称：</th>
				<td><input
					type='text'
					name='activityname'
					value='<%=startTime == null ? "2013-11-08 00:00:00" : startTime%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>结束时间</th>
				<td><input
					type='text'
					name='windowtitle'
					value='<%=endTime == null ? "2013-11-08 23:59:59" : endTime%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>开放平台</th>
				<td><input
					type='text'
					name='articles'
					value='<%=platforms == null ? "sqage" : platforms%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>开启活动服务器名</th>
				<td><input
					type='text'
					name='colors'
					value='<%=openservers == null ? "国内本地测试" : openservers%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>不开启活动服务器名</th>
				<td><input
					type='text'
					name='colors'
					value='<%=unopenservers == null ? "国内本地测试" : unopenservers%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>商店名</th>
				<td><input
					type='text'
					name='counts'
					value='<%=shopname == null ? "全部道具" : shopname%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>买送类型</th>
				<td><input
					type='text'
					name='mailtitle'
					value='<%=stype == null ? "0" : stype%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>需要购买的物品名</th>
				<td><input
					type='text'
					name='mailcontent'
					value='<%=artName == null ? "" : artName%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>物品颜色</th>
				<td><input
					type='text'
					name='mailcontent'
					value='<%=artColor == null ? "" : artColor%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>是否绑定</th>
				<td><input
					type='text'
					name='starttime'
					value='<%=artBind == null ? "" : artBind%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>数量</th>
				<td><input
					type='text'
					name='endtime'
					value='<%=artNum == null ? "" : artNum%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>赠送的物品名</th>
				<td><input
					type='text'
					name='mailcontent'
					value='<%=giveName == null ? "" : giveName%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>物品颜色</th>
				<td><input
					type='text'
					name='mailcontent'
					value='<%=giveColor == null ? "" : giveColor%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>是否绑定</th>
				<td><input
					type='text'
					name='starttime'
					value='<%=giveBind == null ? "" : giveBind%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>数量</th>
				<td><input
					type='text'
					name='endtime'
					value='<%=giveNum == null ? "" : giveNum%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>邮件标题</th>
				<td><input
					type='text'
					name='endtime'
					value='<%=mailTitile == null ? "" : mailTitile%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>邮件内容</th>
				<td><input
					type='text'
					name='endtime'
					value='<%=mailContent == null ? "" : mailContent%>'
				></td>
			</tr>
		</table>
	</form>
</body>
</html>