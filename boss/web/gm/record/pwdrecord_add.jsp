<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.boss.gm.record.AccountRecord"%>
<%@page import="com.fy.boss.gm.record.*"%>
<%@page import="com.fy.boss.game.model.Server" %>
<%@page import="com.fy.boss.gm.record.Recover,com.fy.boss.game.*,com.fy.boss.game.service.*"%>
<%@ include file="../header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<%-- <%@include file="../authority.jsp" %> --%>
<head>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<title>添加账号密码问题</title>
<link rel="stylesheet" href="../css/style.css"/>
<script type="text/javascript">
   function $(tag){
      return document.getElementById(tag);
   }
   function sub(){
      var phone = $("phone").value;
      var name = $("name").value;
      var username = $("username").value;
      var server = $("server").value;
      var player = $("player").value;
      var stoptime = $("stoptime").value;
      var pwd1 = $("pwd1").value;
      var pwd2 = $("pwd2").value;
      var phoneusetime1 = $("phoneusetime1").value;
      var phoneusetime2 = $("phoneusetime2").value;
      var phoneusetype1 = $("phoneusetype1").value;
      var lastlogintime = $("lastlogintime").value;
      var clientid = $("clientid").value;
      var chargemessage = $("chargemessage").value;
      var phoneusetype2 = $("phoneusetype2").value;
      var type = document.getElementsByName("type");
      var tr = false;
	  for(i=0;i<type.length;i++){
		  if(type[i].checked==true)
		    tr = true;
	  }
	  if(phone&&name&&username&&server&&player){
	     $("f1").action = "pwdrecord_add.jsp?action=add"
	  	 $("f1").submit();
	  }else{
	    alert("请输入正确的信息！！！");
	   }
   }


</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">添加账号密码问题</h1>
	<%
	         try{
	         ActionManager amanager = ActionManager.getInstance();
	         RecoverManager rmanager = RecoverManager.getInstance();
	         String action = request.getParameter("action");
	         if(action!=null&&action.trim().length()>0){
	            if("add".equals(action.trim())){
	                String name = request.getParameter("name");
	                String phone = request.getParameter("phone");
	                String username = request.getParameter("username");
	                String server  = request.getParameter("server");
	                String player = request.getParameter("player");
	                String createtime = request.getParameter("createtime");
	                String stoptime = request.getParameter("stoptime");
	                String pwd1 = request.getParameter("pwd1");
	                String pwd2 = request.getParameter("pwd2");
	                String phoneusetime1 = request.getParameter("phoneusetime1");
	                String phoneusetime2 = request.getParameter("phoneusetime2");
	                String phoneusetype1 = request.getParameter("phoneusetype1");
	                String phoneusetype2 = request.getParameter("phoneusetype2");
	                String lastlogintime = request.getParameter("lastlogintime");
	                String clientid = request.getParameter("clientid");
	                String chargemessage = request.getParameter("chargemessage");
	                String type = request.getParameter("type");
	                Recover rr = new Recover();
	                rr.setName(name);
	                rr.setPhone(phone);
	                rr.setUsername(username);
	                rr.setServer(server);
	                rr.setPlayer(player);
	                rr.setCreatetime(createtime);
	                rr.setStoptime(stoptime);
	                rr.setPwd1(pwd1);
	                rr.setPwd2(pwd2);
	                rr.setPhoneusetime1(phoneusetime1);
	                rr.setPhoneusetime2(phoneusetime2);
	                rr.setPhoneusetype1(phoneusetype1);
	                rr.setPhoneusetype2(phoneusetype2);
	                rr.setLastlogintime(lastlogintime);
	                rr.setClientid(clientid);
	                rr.setChargemessage(chargemessage);
	                rr.setType(type);
	                rr = rmanager.addone(rr);
	                if(rr!=null){
	                	out.print("<font color='red' >添加成功！！！</font>");
	                }else{
	                    out.print("<font color='red' >添加失败！！！</font>");
	                }
	                
	            }
	         }
	         out.print("<input type='button' value='刷新' onclick='window.location.replace(\"pwdrecord_add.jsp\")' />");
	         out.print("<form action='' method='post' id='f1'>");
		     out.print("<table align='center' width='80%' ><caption>添加账号恢复</caption>");
		     out.print("<tr><th>称呼</th><td class='top'><input type='text' value='' id='name' name='name'/></td>");
		     out.print("<th>联系电话</th><td class='top'><input type='text' value='' id='phone' name='phone' /></td></tr>");
		     out.print("<tr><th>用户名</th><td><input type='text' id='username' name='username' value='' /></td>");
		     StringBuffer sb = new StringBuffer();
		     ServerManager gsm = ServerManager.getInstance();
		     List<Server> slist = gsm.getServers();
		     for(Server s : slist) {
		    	 sb.append("<option value='"+s.getName()+"' >"+s.getName()+"</option>");
		     }
		     out.print("<th>服务器</th><td><select id='server' name='server' >"+sb.toString()+"</select></td></tr>");
		     out.print("<tr><th>角色名</th><td><input type='text' value='' id='player' name='player' /></td>");
		     String date = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm");
		     out.print("<th>创建时间</th><td>"+date+"<input type='hidden' id='createtime' name='createtime' value='"+date+"' /></td></tr>");
		     out.print("<tr><th>停权时间</th><td><input type='text' value='' name='stoptime' id='stoptime' /></td>");
		     out.print("<th>记录人</th><td>"+""+"</td></tr>");
		     out.print("<tr><th>密保问题1</th><td><input type='text' name='pwd1' id='pwd1' value=''/></td>");
		     out.print("<th>密保问题2</th><td><input type='text'  name='pwd2' id='pwd2' value=''/>  ");
		     out.print("</td></tr>");
		     out.print("<tr><th>使用手机时间段1：</th><td><input type='text' value='' name='phoneusetime1' id='phoneusetime1' /></td>");
		     out.print("<th>手机机型1：</th><td><input type='text' value=''  id='phoneusetype1' name='phoneusetype1'/></td></tr>");
		     out.print("<tr><th>使用手机时间段2：</th><td><input type='text' value='' name='phoneusetime2' id='phoneusetime2' /></td>");
		     out.print("<th>手机机型2：</th><td><input type='text' value=''  id='phoneusetype2' name='phoneusetype2'/></td></tr>");
		     out.print("<tr><th>最后一次登录时间</th><td><input type='text' value='' name='lastlogintime' id='lastlogintime' /></td>");
		     out.print("<th>客户端ID</th><td><input type='text' value='' name='clientid' id='clientid' /></td></tr>");
		     out.print("<tr><th>充值信息</th><td colspan='3' ><textarea id='chargemessage' name='chargemessage' style='width:80%;height:50px;'></textarea></td></tr>");
		     out.print("<tr><th>类型：</th><td colspan='3'><input type='radio' value='已提交'  name='type' />已提交           <input type='radio' value='已解决'  name='type' />已解决<input type='radio' value='已结束'  name='type' />已结束</td></tr>    ");
		     out.print("<tr><td colspan='4' ><input type='button' value='提交' onclick='sub();' /><input type='reset' value='重置' /><input type='button' value='返回列表' onclick='window.location.replace(\"recover_add.jsp\")' /></td></tr></table>");
		     out.print("</form>");
		     
		    }catch(Exception e){
		    out.print(StringUtil.getStackTrace(e));
		    }
	%>
  
</body>
</html>
