<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.boss.gm.record.*"%>
<%@ include file="../header.jsp"   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
<head>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
<title>添加问题记录</title>
<link rel="stylesheet" href="../css/style.css"/>
<script type="text/javascript">
   function $(tag){
      return document.getElementById(tag);
   }
   function sub(){
	var username=$("username").value;
	var servername=$("servername").value;
	var name =$("name").value;
	var playname=$("playname").value;
	var createDate=$("createDate").value;
	var recorder=$("recorder").value;
	var question=$("question").value;
	var answer = $("answer").value;
	var questype = $("questype").value;
	var type = document.getElementsByName("type");
	var tr = false;
	for(i=0;i<type.length;i++){
	  if(type[i].checked==true)
	    tr = true;
	}
    if(name&&username&&servername&&playname&&createDate&&recorder&&question&&answer&&tr&&questype){
      $("f1").action='?action=add';
      $("f1").submit();
   }else{
     alert("请填写正确的信息！");
   }
   }


</script>
</head>
<body bgcolor="#FFFFFF" >
	<h1 align="d">添加问题记录</h1>
	<%
	         try{
	         ActionManager amanager = ActionManager.getInstance();
	         TelRecordManager tmanager = TelRecordManager.getInstance(); 
	         String action = request.getParameter("action");
	         if(action!=null&&action.trim().length()>0){
	            if("add".equals(action.trim())){
	                TelRecord tr = new TelRecord();
	     
					String username=request.getParameter("username") ;
					tr.setUname(username.trim());
					String name = request.getParameter("name");
					tr.setName(name.trim());
					String servername=request.getParameter("servername") ;
					tr.setServername(servername.trim());
					String questtype = request.getParameter("questype");
					tr.setQuesttype(questtype.trim());
					String playname=request.getParameter("playname") ;
					tr.setPlayername(playname.trim());
					String recorder = request.getParameter("recorder");
					tr.setRecorder(recorder.trim());
					String createDate=request.getParameter("createDate") ;
					tr.setCreatedate(createDate.trim());  
					String question = request.getParameter("question");
					tr.setQuestion(question.trim());
					String answer = request.getParameter("answer");
					tr.setAnswer(answer.trim());
					String type=request.getParameter("type") ;
					tr.setType(type.trim()); 
					if(username!=null&&name!=null){  
		                tmanager.addone(tr);
		                amanager.save(recorder,"add one telrecord");
	                }
	                out.print("<font color='red' >添加成功！！！</font>");
	            }
	         }
	         out.print("<input type='button' value='刷新' onclick='window.location.replace(\"telrecord_add.jsp\")' />");
	         out.print("<form action='' method='post' id='f1'>");
		     out.print("<table align='center' width='80%' ><caption>添加问题记录判定</caption>");
		     out.print("<th>游戏账号：</th><td class='top'><input type='text' value='' name='username' id='username' /></td></tr>");
		     out.print("<tr><th  >称呼：</th><td><input type='text' value='' name='name' id='name' /></td>");
		     
		     out.print("<th>问题类型：</th><td><select id='questype' name='questype' /><option>--</option>");
		     out.print("<option value='角色恢复' >角色恢复</option>");
		     out.print("<option value='物品丢失' >物品丢失</option>");
		     out.print("<option value='无法登录' >无法登录</option>");
		     out.print("<option value='无法解决' >无法解决</option>");
		     out.print("<option value='任务' >任务</option>");
		     out.print("<option value='外挂' >外挂</option>");
		     out.print("<option value='建议' >建议</option>");
		     out.print("<option value='充值' >充值</option>");
		     out.print("<option value='其他' >其他</option></select></td></tr>");
		     XmlServerManager smanager = XmlServerManager.getInstance();
		     List<XmlServer> servers = smanager.getServers();
		     out.print("<tr><th>服务器：</th><td><select id='servername' name='servername' /><option>--</option>");
		     if(servers == null || servers.size() == 0) {
		     out.print("<option value='无' >无</option>");
		     } else {
		     	for(XmlServer server : servers) {
				     out.print("<option value='" + server.getDescription() + "'>" +server.getDescription()+ "</option>");
		     	}
		     }
			  out.print("</select></td>");
		     
		     
		     out.print("<th>人物昵称：</th><td><input type='text' value='' id='playname' name='playname' /></td></tr> ");
		     out.print("<tr><th>咨询问题内容：</th><td colspan='3'>");
		     out.print("<textarea id='question' name='question' style='width:80%' rows='4' ></textarea></td></tr>");
		     out.print("<tr><th>回答内容：</th><td colspan='3'>");
		     out.print("<textarea id='answer' name='answer' style='width:80%' rows='4' ></textarea></td></tr>");
		     String now = DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm");
		     out.print("<tr><th>填写时间:</th><td colspan='3'>"+now+"<input type='hidden' value='"+now+"' name='createDate' id='createDate' /></td></tr>");
		     out.print("<tr><th>录入人：</th><td><input type='text' value='' id='recorder' name='recorder' /></td></tr> ");  
		     out.print("<tr><td colspan='4' >类型： <input type='radio' value='直接解答'  name='type' />直接解答  <input type='radio' value='已提交'  name='type' />已提交           <input type='radio' value='已解决'  name='type' />已解决</td></tr>    ");
		     out.print("<tr><td colspan='4' ><input type='button' value='提交' onclick='sub();' /><input type='reset' value='重置' /></td></tr></table>");
		     out.print("</form>");
		     
		    }catch(Exception e){
		    out.print(StringUtil.getStackTrace(e));
		    }
	%>
  
</body>
</html>
