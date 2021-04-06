<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@include file="../header.jsp"%>
<%@page import="com.fy.engineserver.economic.*"%>
<%@include file="../authority.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>绑定元宝管理</title>
		<link rel="stylesheet" href="../style.css" />
		<style type="text/css">
#a {
	font-size: 0px;
}

#a li {
	font-size: 16px;
	text-align: center;
	width: 110px;
	list-style-type: none;
	display: inline;
	padding: 5px 10px;
}
</style>
		<script type="text/javascript">
	function $(tag) {
		return document.getElementById(tag);
	}
	function querybyname(){
	var pname = $("pname").value;
	if(pname){
	 window.location.replace("bandyuanbao_compensate.jsp?pname="+pname);
	}else{
	 alert("查询的角色名字不能为空！");
	}
	}
	function querybyid(){
	var pid = $("pid").value;
	if(pid){
	 window.location.replace("bandyuanbao_compensate.jsp?pid="+pid);
	}else{
	 alert("查询的角色ID不能为空！");
	}
	}
	function addshi(){
	  $("addcount").value="10";
	  var result = prompt("请输入增加的原因","工资补偿");
	   $("f1").action="?action=add&result="+result;
	  if(window.confirm("你确定添加十元宝吗！")){
	    $("f1").submit();
	  }
	}
	function addbai(){
	   var result = prompt("请输入增加的原因","工资补偿");
	  $("addcount").value="100";
	   $("f1").action="?action=add&result="+result;
	  if(window.confirm("你确定添加一百元宝吗！")){
	    $("f1").submit();
	  }
	}
	function addpointshi(){
	  $("addpointcount").value="10";
	  var result = prompt("请输入增加的原因","补偿");
	   $("f1").action="?action=addpoint&result="+result;
	  if(window.confirm("你确定添加十积分吗！")){
	    $("f1").submit();
	  }
	}
	function addpointbai(){
	   var result = prompt("请输入增加的原因","补偿");
	  $("addpointcount").value="100";
	   $("f1").action="?action=addpoint&result="+result;
	  if(window.confirm("你确定添加一百积分吗！")){
	    $("f1").submit();
	  }
	}
	function addpointqian(){
	   var result = prompt("请输入增加的原因","补偿");
	  $("addpointcount").value="1000";
	   $("f1").action="?action=addpoint&result="+result;
	  if(window.confirm("你确定添加一千积分吗！")){
	    $("f1").submit();
	  }
	}
</script>
	</head>
	<body>
		<%
			try {
			    out.print("<h1>绑定元宝发送</h1>");
				String gmusername = session.getAttribute("username").toString();
				ActionManager amanager = ActionManager.getInstance();
				PlayerManager pmanager = PlayerManager.getInstance();
				BillingCenter bcenter = BillingCenter.getInstance();
				Player p = null;
				String pname = request.getParameter("pname");
				String pid = request.getParameter("pid");
				if(pname!=null&&pname.trim().length()>0){
				 p = pmanager.getPlayer(pname.trim());
				}
				if(pid !=null&&pid.trim().length()>0){
				 p = pmanager.getPlayer(Long.parseLong(pid.trim()));
				}
				String action = request.getParameter("action");
				if(action!=null&&"add".equals(action.trim())&&p!=null){
				   long account = Long.parseLong(request.getParameter("addcount").trim());
				   String result1 = request.getParameter("result");
				   amanager.save(gmusername,"给 ["+p.getUsername()+"]["+p.getName()+"][添加绑定元宝]["+account+"]["+result1+"]");
				   bcenter.playerSaving(p,account, CurrencyType.BIND_YUANBAO,SavingReasonType.COMPENSATE_PLAYER, null);
				}
				if(action!=null&&"addpoint".equals(action.trim())&&p!=null){
				   long account = Long.parseLong(request.getParameter("addpointcount").trim());
				   String result1 = request.getParameter("result");
				   amanager.save(gmusername,"给 ["+p.getUsername()+"]["+p.getName()+"][添加充值积分]["+account+"]["+result1+"]");
				   bcenter.playerSaving(p,account, CurrencyType.CHARGE_POINTS,SavingReasonType.COMPENSATE_PLAYER, null);
				}
				if((pname!=null||pid!=null)&&p==null)
				 out.print("<font color='red' >您所查找的角色不存在，请检查查询信息</font>");
				out.print("<form action='' method='post' id = 'f1'>");
				if(p!=null&&p.getUsername()!=null){
				out.print("<table align='center' width='80%' >");
                out.print("<caption>玩家绑定元宝补偿</caption>");
                out.print("<tr><th>账号名 </th><th>角色名</th><th>角色ID</th><th>剩余绑定元宝数</th><th>充值  </th></tr>");
                out.print("<tr><td>"+p.getUsername()+"</td>");
                out.print("<td>"+p.getName()+"</td>");
                out.print("<td>"+p.getId()+"<input type='hidden' name='pid' value='"+p.getId()+"' /></td>");
                out.print("<td>"+p.getBindyuanbao()+"<input type='hidden' value='' id='addcount' name='addcount' /></td>");
                out.print("<td><input type='button' value='加10' onclick='addshi();' />");
                out.print("<input type='button' value='加100' onclick='addbai();' /></td><tr></table>");
                out.print("<table align='center' width='80%' >");
                out.print("<caption>玩家充值积分补偿</caption>");
                out.print("<tr><th>账号名 </th><th>角色名</th><th>角色ID</th><th>充值积分</th><th>增加充值积分  </th></tr>");
                out.print("<tr><td>"+p.getUsername()+"</td>");
                out.print("<td>"+p.getName()+"</td>");
                out.print("<td>"+p.getId()+"<input type='hidden' name='pid' value='"+p.getId()+"' /></td>");
                out.print("<td>"+p.getChargePoints()+"<input type='hidden' value='' id='addpointcount' name='addpointcount' /></td>");
                out.print("<td><input type='button' value='加10' onclick='addpointshi();' />");
                out.print("<input type='button' value='加100' onclick='addpointbai();' />");
                 out.print("<input type='button' value='加1000' onclick='addpointqian();' />");
                out.print("</td><tr></table>");
                
                out.print("</form>");
                }
                out.print("<br/>角色名：<input type='text' id='pname' name='pname' value=''/>");
                 out.print("<input type='button' value='查询' onclick='querybyname();'");
                out.print("<br/>角色ID：<input type='text' id='pid' name='pid' value=''/>"); 
                 out.print("<input type='button' value='查询' onclick='querybyid();'");
			} catch (Exception e) {
				//e.printStackTrace();
				//out.print(e.getMessage());
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("../gmuser/visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>
	</body>
</html>
