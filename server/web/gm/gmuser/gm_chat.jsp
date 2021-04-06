<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.chat.ChatMessage"%>
<%@page import="com.fy.engineserver.message.QUERY_WINDOW_RES"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.menu.Option_Cancel"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.message.HINT_REQ"%>
<%@page import="com.fy.engineserver.gm.custom.Chatborder" %>
<%@include file="../header.jsp"%>
<%-- <%@include file="../authority.jsp" %> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>灭世GM私聊 </title>
		<link rel="stylesheet" href="../style.css" />
		<script type='text/javascript'
			src='/dwr/interface/chatborder.js'></script>
		<script type='text/javascript' src='/dwr/engine.js'></script>
<!-- 		<script type="text/javascript" src="/dwr/interface/DWRManager.js"></script> -->
		<script type="text/javascript" src="/dwr/util.js"></script>
		<script type="text/javascript">
  function $(tag){
    return document.getElementById(tag);
  }
  function change(mid,tag){
	  //将子定义的内容填入回复框
	   var instr = document.getElementById("repcontent"+mid);
	   if(tag.value!=""){
	    instr.value=tag.value;
	   }
  }
  var status = 0;
   var inc;
  function set(){
     status =1;
  }
  function unset(){
     status =0;
  }
  
 
  function chat(){
	  var playname = document.getElementById("playname").value;
	  var playid = document.getElementById("playid").value;
	  if(playname!=""){
	       window.location.replace("?action=talk&playername="+playname);
	    }
	  if(playid!=""){
	      window.location.replace("?action=talk&playerid="+playid);
	  }
  }
  
  function showmessage(gmname){
       chatborder.getMessageByname(gmname,show);
  }
  function show(mes){
      if(mes.length>0){
  	  var tag = document.getElementById("message");
      var m1 = tag.innerHTML;
      var m2 ="";
      for(i=0;i<mes.length;i++){
        var s= mes[i].split("#@#");
        
        if(s[0].indexOf("gm01")==0){
          m2= "<p style=\"color:red\" >"+s[0]+"&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[2]+"</p>" + m2;
        }else{	
          m2=m2+"<p style=\"color:green\" >"+s[0]+"&nbsp;&nbsp;&nbsp;&nbsp;"+s[1]+"<br/>"+s[2]+"</p>" + m2;
        }
      } 
      if(m2!=""){ 
		      var cobj = document.createElement("div");
		      var t1 = tag.scrollTop;
		      cobj.innerHTML= m2;
		      tag.appendChild(cobj);
		      if(status==0){
		      tag.scrollTop = tag.scrollHeight;
		      }
		      if(status==1){
		      tag.scrollTop = t1;
		      }
	   }
   }
  }
  function shuaxin(){
	  window.location.replace("gm_chat.jsp");
  }
  
  function send(playid,gmname){
     var sendms = document.getElementById("sendms").value;
     document.getElementById("sendms").value="";
     if(sendms){
       chatborder.sendMessage(gmname,playid,sendms);
     }
  }
  function tankuang(pid,gmname){
    var message =  prompt("请输入弹框的内容", "");
    if(message){
     //win = window.open("gm_tankuang.jsp?action=addkuang&message="+message+"&playerid="+pid);
    // win.close();
    chatborder.tankuang(pid,message,gmname,function(a){alert(a);});
    }
    else
     alert("弹出内容不能为空！！");
  }

  <%String gmname = "gm01";
			if (request.getParameter("action") != null) {
				out.print("inc=self.setInterval(\"showmessage('" + gmname
						+ "')\",2000)");%>
	
						<%}%>
</script>
	</head>
	<body>
		<%!private Map<String, List<String>> records = new HashMap<String, List<String>>();%>
		<%
			try {
				out
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"gm_chat.jsp\")' />");
				out.print("<br/><input type='button' value='锁定' onclick='set();' />");
                out.print("<input type='button' value='取消' onclick='unset();' /><br/>");
// 				String username = session.getAttribute("username").toString();
				String username = "gm01";
				ActionManager amanager = ActionManager.getInstance();
				ChatMessageService cms = ChatMessageService.getInstance();
				PlayerManager pmanager = PlayerManager.getInstance();
				out.print("gmname:" + gmname);
				if (!records.keySet().contains(gmname))
					records.put(gmname, new ArrayList<String>());
				String action = request.getParameter("action");
				if(action!=null&&"delete".equals(action.trim())){
				  String rename = request.getParameter("rename");
				  if(rename!=null)
				    records.put(rename,new ArrayList<String>());
				}   
				
				String playname = null;
				Player p = null;
				long playid = -1;
				try {
					if (request.getParameter("playername") != null
							|| request.getParameter("playerid") != null) {
						//改变用户角色时
						if (request.getParameter("playername") != null) {
							playname = request.getParameter("playername");
							p = pmanager.getPlayer(playname);
							amanager.save(username,"与"+p.getName()+"私聊 在"+DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd HH:mm:ss"));
						}
						if (request.getParameter("playerid") != null) {
							playid = Long.parseLong(request
									.getParameter("playerid"));
							p = pmanager.getPlayer(playid);
							amanager.save(username,"与"+p.getName()+"私聊 在"+DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd HH:mm:ss"));
							playname = p.getName();
						}
						if (!records.get(gmname).contains(playname))
							records.get(gmname).add(playname);
					}
				} catch (Exception e) {
					out.print("请输入正确的角色名或者ID号");
				}
				if(action!=null&&"addkuang".equals(action)){
				  String message = request.getParameter("message");
				   HINT_REQ re = new HINT_REQ(GameMessageFactory.nextSequnceNum(),(byte)0,message);
					p.addMessageToRightBag(re);
				}  
				out.print("<p></p><p></p><p></p>");
				out
						.print("<table width=80% ><caption align='center'>聊天版</caption><tr><td width=80% class='top'>");
				out
						.print("角色名<input type='text' name='playname' id='playname' value='' />或角色id<input type='text' name='playid' id='playid' value='' />   "
								+ "<input type='button' value='玩家聊天' onclick='chat();' />");
				if (playname != null)
					out.print("与" + playname + "聊天中");
				out
						.println("</td><td rowspan=2 width=20% class='top' style='padding: 0;' valign=\"top\" ><table width='100%'><tr><th>玩家列表</th></tr>");

				for (String name : records.get(gmname)) {
					try {
						out.print("<tr><td><a href='?playerid="
								+ pmanager.getPlayer(name).getId()
								+ "&action=talk' />" + name + "</a>("+(pmanager.getPlayer(name).isOnline()?"在线":"下线")+")</td></tr>");
					} catch (Exception e) {
						out.print("名字查找错误，可能修改过角色名！");
					}
				}
				out.print("</table>");
				out
						.print("<div><input type='button' value='清空' onclick='window.location.replace(\"gm_chat.jsp?action=delete&rename="+gmname+"\")' ");
						
				out.print("<input type='button' value='查看聊天记录' onclick='window.open(\"chat_record.jsp\")' /> </div>");
				out.print("</td></tr><tr><td>");
				out
						.print("<div id='message'  style=\"width:100%; height: 500px; border: solid green 1px; overflow: scroll\" ></div>");
				out
						.print("<br/><input type='text' name='sendms' id ='sendms' value='' size='100' />  ");
				if (p != null&&p.isOnline()){
					out.print("<input type='button' value='发送' onclick ='send(\""
							+ p.getId() + "\",\"" + gmname + "\");' id='fs'/>");
					out.print("<input type='button' value='弹框提示' onclick='tankuang(\""+p.getId()+"\");' />");		
							}
				out.print("</td></tr></table>");

			} catch (Exception e) {
			  out.print(StringUtil.getStackTrace(e));
				//out.print(e.getMessage());
				//RequestDispatcher rdp = request.getRequestDispatcher("visitfobiden.jsp");
			   // rdp.forward(request,response);

			}
		%>
	</body></html>
