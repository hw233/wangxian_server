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
	String activityname = request.getParameter("activityname");
	String windowtitle = request.getParameter("windowtitle");
	String mailtitle = request.getParameter("mailtitle");
	String mailcontent = request.getParameter("mailcontent");
	String counts = request.getParameter("counts");
	String colors = request.getParameter("colors");
	String articles = request.getParameter("articles");
	String openplatforms = request.getParameter("openplatforms");
	String openservers = request.getParameter("openservers");
	String limitservers = request.getParameter("limitservers");
	String levellimit = request.getParameter("levellimit");
	String starttime = request.getParameter("starttime");
	String endtime = request.getParameter("endtime");
	String action = request.getParameter("comm");
	String packagefullmess = request.getParameter("packagefullmess");
	String hasgetmess = request.getParameter("hasgetmess");
	String pwd = request.getParameter("pwd");
	String delete = request.getParameter("delete");
	if(delete != null && !delete.isEmpty()){
		WindowManager wm = WindowManager.getInstance();
		MenuWindow[] windowMap = wm.getWindows();
		boolean isalive = false;
		for (MenuWindow menu : windowMap) {
			if (menu != null) {
				if (menu.getId() == 30000) {
					Option[] ops = menu.getOptions();
					List<Option> opss = new ArrayList<Option>();
					Option delOption = null;
					for (int i=0;i<ops.length;i++) {
						Option o = ops[i];
						if(o instanceof Option_Get_Reward_Only_One_Time){
							Option_Get_Reward_Only_One_Time activity = (Option_Get_Reward_Only_One_Time)o;
							if(activity.getActivityname().equals(delete)){
								delOption = o;
								continue;
							}
						}
						opss.add(o);
					}
					int oldlength = ops.length;
					int newlength = opss.size();
					menu.setOptions(opss.toArray(new Option[]{}));
					out.print("删除的菜单:"+delete+"--oldlength:"+oldlength+"--newlength:"+newlength+"<br>");
				}
			}
		}
		
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	if (starttime == null || starttime.trim().length() == 0) {
		starttime = sdf.format(new Date());
	}
	if (endtime == null || endtime.trim().length() == 0) {
		endtime = sdf.format(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
	}

	MenuWindow modifymenu = null;
	WindowManager wm = WindowManager.getInstance();
	MenuWindow[] windowMap = wm.getWindows();
	List<Option> list = new ArrayList<Option>();
	boolean isalive = false;
	for (MenuWindow menu : windowMap) {
		if (menu != null) {
			if (menu.getId() == 30000) {
				modifymenu = menu;
				Option[] ops = menu.getOptions();
				for (Option o : ops) {
					if(o instanceof Option_Get_Reward_Only_One_Time){
						Option_Get_Reward_Only_One_Time activity = (Option_Get_Reward_Only_One_Time)o;
						if(activity.getActivityname().equals(activityname)){
							isalive = true;
						}
					}
					list.add(o);
				}
			}
		}
	}
	if(isalive){
		out.print("<font color='red'>已经刷过</font>");
		return;
	}
	
	if (action != null) {
		if(pwd!=null && pwd.equals("4rfvbgt56yhnmju7")){
			if (counts.trim().length() == 0 || colors.trim().length() == 0 || activityname.trim().length() == 0 || windowtitle.trim().length() == 0 || articles.trim().length() == 0 || mailtitle.trim().length() == 0 || mailcontent.trim().length() == 0 || openplatforms.trim().length() == 0 || starttime.trim().length() == 0 || endtime.trim().length() == 0) {
				out.print("<h1><font color='red'>必填项不能为空！</font></h1>");
			} else {
				try {
					Option_Get_Reward_Only_One_Time option = new Option_Get_Reward_Only_One_Time();
					option.setActivityname(activityname);
					option.setEndTimeStr(endtime);
					option.setStartTimeStr(starttime);
					option.setText(windowtitle);
					option.setMailtitle(mailtitle);
					option.setMailcount(mailcontent);
					option.setOpenserverstr(openservers);
					option.setRewardAriclenamestr(articles);
					option.setLimitserverstr(limitservers);
					option.setPlayerlevel(Integer.parseInt(levellimit));
					option.set已经领取过(hasgetmess);
					option.set背包满提示(packagefullmess);
					option.setColorstr(colors);
					option.setCountstr(counts);
					String ps[] = openplatforms.split(",");

					if (ps != null && ps.length > 0) {
						Platform pfs[] = new Platform[ps.length];
						for (int i = 0, len = ps.length; i < len; i++) {
							Platform pf = PlatformManager.getInstance().getPlatForm(getPlatname(ps[i]));
							pfs[i] = pf;
						}
						option.setPf(pfs);
					}
					option.init();
					list.add(option);
					modifymenu.setOptions(list.toArray(new Option[]{}));
					wm.getWindowMap().put(30000, modifymenu);
					out.print("[页面添加活动] [成功] [" + modifymenu.getTitle() + "] [" + option.pageLog() + "]");
					ActivitySubSystem.logger.warn("[页面添加活动] [成功] [" + modifymenu.getTitle() + "] [" + option.pageLog() + "]");
				} catch (Exception e) {
					out.print("[页面添加活动] [异常] [" + modifymenu.getTitle() + "] [" + e + "]");
					ActivitySubSystem.logger.warn("[页面添加活动] [异常] [" + modifymenu.getTitle() + "] [" + e + "]");
				}
			}
		}else{
			out.print("<B>请找相关人员索要认证码！</B>");
		}
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
			<tr><th>活动名称</th><th>窗口标题</th><th>奖励物品</th><th>奖励颜色</th><th>奖励数量</th><th>邮件标题</th><th>邮件内容</th><th>开放的平台</th><th>开始时间</th><th>结束时间</th><th>开放的服务器</th><th>限制的服务器</th><th>等级限制</th><th>操作</th></tr>
			<%
				for(int i=0,len=list.size();i<len;i++){
					if(list.get(i) instanceof Option_Get_Reward_Only_One_Time){
						Option_Get_Reward_Only_One_Time op = (Option_Get_Reward_Only_One_Time)list.get(i);
			%>		
						<tr><td><%=op.getActivityname() %></td><td><%=op.getText() %></td><td><%=op.getRewardAriclenamestr() %></td><td><%=op.getColorstr() %></td><td><%=op.getCountstr() %></td><td><%=op.getMailtitle() %></td><td><%=op.getMailcount() %></td><td><%=op.getPf().toString()%></td><td><%=op.getStartTimeStr() %></td><td><%=op.getEndTimeStr() %></td><td><%=op.getOpenserverstr() %></td><td><%=op.getLimitserverstr() %></td><td><%=op.getPlayerlevel() %></td><td><input type='button' value='编辑'/><input type="submit" value='删除'><input type="hidden" name='delete' value='<%=op.getActivityname()%>'></td></tr>
			<%	
					}
				}
			%>	
		</table>
	</form>

	<form>
		<table>
			<h1>NPC窗口补偿</h1>
			<tr>
				<th><font color='red'>请输入修改认证码：</font></th>
				<td><input
					type="password"
					name='pwd'
					value='<%=pwd == null ? "****" : "*****"%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>唯一*</font>活动名称：</th>
				<td><input
					type='text'
					name='activityname'
					value='<%=activityname == null ? "xx年xx月xx日补偿" : activityname%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>窗口标题：</th>
				<td><input
					type='text'
					name='windowtitle'
					value='<%=windowtitle == null ? "" : windowtitle%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>奖励物品：</th>
				<td><input
					type='text'
					name='articles'
					value='<%=articles == null ? "" : articles%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>奖励颜色：</th>
				<td><input
					type='text'
					name='colors'
					value='<%=colors == null ? "" : colors%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>奖励数量：</th>
				<td><input
					type='text'
					name='counts'
					value='<%=counts == null ? "" : counts%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>邮件标题：</th>
				<td><input
					type='text'
					name='mailtitle'
					value='<%=mailtitle == null ? "" : mailtitle%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>邮件内容：</th>
				<td><input
					type='text'
					name='mailcontent'
					value='<%=mailcontent == null ? "" : mailcontent%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>开放的平台：</th>
				<td><select name='openplatforms'><option>官方</option>
						<option>腾讯</option>
						<option>台湾</option>
						<option>马来</option>
						<option>韩国</option></select></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>开始时间：</th>
				<td><input
					type='text'
					name='starttime'
					value='<%=starttime == null ? "" : starttime%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>结束时间：</th>
				<td><input
					type='text'
					name='endtime'
					value='<%=endtime == null ? "" : endtime%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>背包满提示：</th>
				<td><input
					type='text'
					name='packagefullmess'
					value='<%=packagefullmess == null ? Translate.背包已满 : packagefullmess%>'
				></td>
			</tr>
			<tr>
				<th><font color='red'>*</font>已领取提示：</th>
				<td><input
					type='text'
					name='hasgetmess'
					value='<%=hasgetmess == null ? Translate.您已经领取过此奖励 : hasgetmess%>'
				></td>
			</tr>
			<tr>
				<th>开放的服务器：</th>
				<td><input
					type='text'
					name='openservers'
					value='<%=openservers == null ? "" : openservers%>'
				></td>
			</tr>
			<tr>
				<th>限制的服务器：</th>
				<td><input
					type='text'
					name='limitservers'
					value='<%=limitservers == null ? "" : limitservers%>'
				></td>
			</tr>
			<tr>
				<th>等级限制：</th>
				<td><input
					type='text'
					name='levellimit'
					value='<%=levellimit == null ? 0 : levellimit%>'
				></td>
			</tr>
			<tr>
				<th><input
					type='submit'
					value='添加'
				></th>
				<input
					type="hidden"
					name='comm'
					value='1'
				>
			</tr>
		</table>
	</form>
</body>
</html>