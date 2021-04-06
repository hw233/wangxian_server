<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.boss.vip.platform.CustomServicerManager"%>
<%@page import="com.fy.boss.vip.platform.model.CustomServicer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.vip.platform.VipPlayerInfoManager"%>
<%@page import="com.fy.boss.transport.BossServerService"%>
<%@page import="com.fy.boss.vip.platform.model.Descript"%>
<%@page import="com.fy.boss.vip.platform.model.VipPlayerInfoRecord"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="priv.jsp"%>     

    
<% 
	


	List<CustomServicer> lst1 = CustomServicerManager.getInstance().queryForWhere("", new Object[0]);
	String id = ParamUtils.getParameter(request, "id");
	boolean isDone = ParamUtils.getBooleanParameter(request, "isdone");
 	if(id != null)
	{
 		VipPlayerInfoRecord playerInfoRecord = VipPlayerInfoManager.getInstance().find(Long.parseLong(id));
 		
 		if(playerInfoRecord == null)
 		{
 			return;
 		}
 		
 		boolean canOperation = false;
 		
 		if(level != 1)
 		{
 			//判断客服经理是否和当前登陆账号一致  一致才可以继续操作
 			String wheresql = "loginName = ?";
 			List<CustomServicer>  lst = CustomServicerManager.getInstance().queryForWhere(wheresql, new Object[]{curUser});
 			if(lst != null && lst.size() > 0)
 			{
 				CustomServicer customServicer = lst.get(0);
 				
 				if(customServicer.getNickname().equals(playerInfoRecord.getManager4vip()))
 				{
 					canOperation = true;
 				}
 				else
 				{
 					
 					out.println("<script>window.alert('无权限!');</script>");
 					return;
 				}
 			}
 		}
 		
 		
 		if(isDone)
 		{
 			String username = ParamUtils.getParameter(request, "username", "");
 	 		String server = ParamUtils.getParameter(request, "server", "");
 	 		String playerName = ParamUtils.getParameter(request, "playername", "");
 	 		int viplevel = ParamUtils.getIntParameter(request, "viplevel", 0);
 			
 			int sex = ParamUtils.getIntParameter(request, "sex", 1);
 			String birth = ParamUtils.getParameter(request, "birth", "");
 			String realname = ParamUtils.getParameter(request, "realname", "");
 			String qq = ParamUtils.getParameter(request, "qq", "");
 			String phone = ParamUtils.getParameter(request, "phone", "");
 			String province = ParamUtils.getParameter(request, "province", "");
 			String city = ParamUtils.getParameter(request, "city", "");
 			String favor = ParamUtils.getParameter(request, "favor", "");
 			String manager4vip = ParamUtils.getParameter(request, "manager4vip", "");
 			String address = ParamUtils.getParameter(request, "address", "");
 			String desc = ParamUtils.getParameter(request, "desc", "");
 			
 			boolean hasErr = false;
 			
 			if(username == null || username.trim().length() == 0)
 			{
 				out.println("<script>alert(账号不能为空);</script>");
 				hasErr = true;
 			}
 			
 			
 			if(server == null || server.trim().length() == 0)
 			{
 				out.println("<script>alert(服务器名不能为空);</script>");
 				hasErr = true;
 			}
 			
 			
 			if(playerName == null || playerName.trim().length() == 0)
 			{
 				out.println("<script>alert(角色名不能为空);</script>");
 				hasErr = true;
 			}
 			
 			if(viplevel == 0)
 			{
 				out.println("<script>alert(vip等级不能为空);</script>");
 				hasErr = true;
 			}
 			
 			
 			if(realname == null || realname.trim().length() == 0)
 			{
 				out.println("<script>alert(用户姓名不能为空);</script>");
 				hasErr = true;
 			}
 			
 			
 			if(phone == null || phone.trim().length() == 0)
 			{
 				out.println("<script>alert(用户电话不能为空);</script>");
 				hasErr = true;
 			}
 			
 			
 			if(qq == null || qq.trim().length() == 0)
 			{
 				out.println("<script>alert(用户QQ不能为空);</script>");
 				hasErr = true;
 			}
 			
 			if(!hasErr)
 			{
				playerInfoRecord.setUsername(username);
				playerInfoRecord.setServerName(server);
				playerInfoRecord.setPlayerName(playerName);
				playerInfoRecord.setVipLevel(viplevel);
	 			playerInfoRecord.setSex(sex);		
	 			playerInfoRecord.setBirthday(birth);
			//	playerInfoRecord.setBirthtime(DateUtil.parseDate(birth, "yyyy-MM-dd").getTime());
	 			playerInfoRecord.setRealname(realname);		
	 			playerInfoRecord.setQq(qq);		
	 			playerInfoRecord.setPhone(phone);		
	 			playerInfoRecord.setProvince(province);		
	 			playerInfoRecord.setCity(city);		
	 			playerInfoRecord.setFavor(favor);
	 			if(level == 1)
	 			{
	 				playerInfoRecord.setManager4vip(manager4vip);
		 			if(manager4vip != null && manager4vip.trim().length() > 0)
		 			{
		 				playerInfoRecord.setPrivValue(1);///已分配
		 			}
		 			else
		 			{
		 				playerInfoRecord.setPrivValue(0);///未分配
		 			}
	 			}
	 			playerInfoRecord.setAddress(address);	
	 			playerInfoRecord.setUpdateTime(System.currentTimeMillis());
	 			Descript descript = new Descript();
	 			descript.setDescr(desc);
	 			playerInfoRecord.setDesc(descript);	
	 			
	 			VipPlayerInfoManager.getInstance().saveOrUpdate(playerInfoRecord);
				response.sendRedirect("playerinfoList.jsp");
				return;
 			}
 		}
 		
 		
		
		if(playerInfoRecord != null)
		{
%>

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<title></title>
	<link rel='stylesheet' type='text/css' href='../css/jquery-ui.css' />
	<script type='text/javascript' src='../js/jquery-1.9.1.min.js'></script>
	<script type='text/javascript' src='../js/jquery-ui-1.10.3.min.js'></script>
	<script>
	  jQuery(function(){  
		    $.datepicker.regional['zh-CN'] = {  
		      clearText: '清除',  
		      clearStatus: '清除已选日期',  
		      closeText: '关闭',  
		      closeStatus: '不改变当前选择',  
		      prevText: '<上月',  
		      prevStatus: '显示上月',  
		      prevBigText: '<<',  
		      prevBigStatus: '显示上一年',  
		      nextText: '下月>',  
		      nextStatus: '显示下月',  
		      nextBigText: '>>',  
		      nextBigStatus: '显示下一年',  
		      currentText: '今天',  
		      currentStatus: '显示本月',  
		      monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],  
		      monthNamesShort: ['一','二','三','四','五','六', '七','八','九','十','十一','十二'],  
		      monthStatus: '选择月份',  
		      yearStatus: '选择年份',  
		      weekHeader: '周',  
		      weekStatus: '年内周次',  
		      dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  
		      dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  
		      dayNamesMin: ['日','一','二','三','四','五','六'],  
		      dayStatus: '设置 DD 为一周起始',  
		      dateStatus: '选择 m月 d日, DD',  
		      dateFormat: 'yy-mm-dd',  
		      firstDay: 1,  
		      initStatus: '请选择日期',  
		      isRTL: false  
		    };  
		    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);  
		    $('#datepicker').datepicker({changeMonth:true,changeYear:true}); 
		  });
	
	
	$(document).ready(function() 
			{
		 	 $("#birth").datepicker(
		 		      { 
		 		      	dateFormat: "yy-mm-dd"
		 						
		 			  } );
			}
		
			
		);
	</script>
	<style type="text/css">
	<!--
	body,td,th {
		font-size: 12px;
		font-family: Arial, Helvetica, sans-serif;
		font-weight: bold;
	}
	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
	.bai {color: #FFFFFF}
	-->
	</style>
	</head>
	<body>
		<div style="height:20px;clear:both" align="left">
			<h3>修改:</h3>
		</div>
		<div style="text-align: center;">
		<form method="post" action="modifyvipinfo.jsp">
		<input type="hidden" name="isdone" value="true"/>
		<input type="hidden" name="id" value="<%=playerInfoRecord.getId()%>"/>
			<div style="font-size: 15px;font-family: Arial, Helvetica, sans-serif;font-weight: bold; text-align: center;">VIP客户资料库</div>
			<div style="border:1px solid black;height:20px;width:600px;margin-left: auto; margin-right: auto;text-align: left">
				用户资料
			</div>
			账号:<input type="text" name="username" value="<%=playerInfoRecord.getUsername() %>" />&nbsp;&nbsp;&nbsp;
			服务器:<input type="text" name="server" value="<%=playerInfoRecord.getServerName() %>" />&nbsp;&nbsp;&nbsp;
			角色名:<input type="text" name="playername" value="<%=playerInfoRecord.getPlayerName() %>" />&nbsp;&nbsp;&nbsp;
			VIP等级:<input type="text" name="viplevel" value="<%=playerInfoRecord.getVipLevel() %>" />
			<br/>
			
			性别:<input type="radio" name="sex" value="1" <%if(playerInfoRecord.getSex() == 1) {%> checked="checked" <%} %>/> 男 &nbsp;<input type="radio" name="sex" value="2" <%if(playerInfoRecord.getSex() == 2) {%> checked="checked" <%} %>  />女  &nbsp;&nbsp;&nbsp;&nbsp;
			生日:<input type="text" name="birth" id="birth" value="<%=playerInfoRecord.getBirthday() == null ? "":playerInfoRecord.getBirthday() %>" />&nbsp;&nbsp;&nbsp;&nbsp;
			姓名:<input type="text" name="realname"  value="<%=playerInfoRecord.getRealname() == null ? "":playerInfoRecord.getRealname()%>"  /><br/>
			
			联系QQ:<input type="text" name="qq" value="<%=playerInfoRecord.getQq() == null ? "":playerInfoRecord.getQq() %>" />&nbsp;&nbsp;&nbsp;&nbsp;
			联系电话:<input type="text" name="phone" value="<%=playerInfoRecord.getPhone() == null ? "":playerInfoRecord.getPhone() %>" />&nbsp;&nbsp;&nbsp;&nbsp;
			所在省份:<input type="text" name="province"   value="<%=playerInfoRecord.getProvince() == null ? "":playerInfoRecord.getProvince() %>" /><br/>
			
			所在城市:<input type="text" name="city" value="<%=playerInfoRecord.getCity() == null ? "":playerInfoRecord.getCity() %>"   />&nbsp;&nbsp;&nbsp;&nbsp;
			爱好:<input type="text" name="favor"  value="<%=playerInfoRecord.getFavor() == null ? "":playerInfoRecord.getFavor() %>"  />&nbsp;&nbsp;&nbsp;&nbsp;
			客户经理:<%if(level == 1) {%> <select name="manager4vip" >
							<%
				if(lst1 != null && lst1.size() > 0)
				{
					
					for(CustomServicer customServicer : lst1)
					{
						
				%>
					<option  <%if(playerInfoRecord.getManager4vip() != null && playerInfoRecord.getManager4vip().trim().equals(customServicer.getNickname().trim())){ %> selected="selected" <%} %>   value="<%=customServicer.getNickname() %>"><%=customServicer.getNickname() %></option>
				<%	
					}
				}
				
				
				
				
				
				%>
					
			
				</select><br/>
			<%}else{ %><%} %>
			
			
			联系地址:<input type="text" name="address" value="<%=playerInfoRecord.getAddress() == null ? "":playerInfoRecord.getAddress() %>" /><br/>
			<div style="border:1px solid black;height:20px; width:600px;margin-left: auto; margin-right: auto;text-align: left">
				重要事项
			</div>
			<textarea rows="20" cols="30" name="desc" value=""><%=playerInfoRecord.getDesc() == null ? "":playerInfoRecord.getDesc().getDescr() %>  </textarea>
		
			<input type="submit" name="submit" value="修改" />
		</form>
		</div>
	</body>
	</html>

<%
		}
	}
%>