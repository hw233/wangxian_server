<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.fy.engineserver.gm.record.*"%>
<%@ include file="../header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="zh-cn">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
			<link rel="stylesheet" href="../../css/style.css" />
			
			<title>封号列表</title>
			
			
 <script type="text/javascript">
			
	function overTag(tag) {
		tag.style.color = "red";
		tag.style.backgroundColor = "lightcyan";
	}

	function outTag(tag) {
		tag.style.color = "black";
		tag.style.backgroundColor = "white";
	}
	function query() {
		var username = document.getElementById("username").value;
		if (username)
			window.location.replace("userAction_list.jsp?username="
					+ username);
		else
			alert("输入框中数据不能为空？？？");
	}
	function jump(){
	  var starty = document.getElementById("starty").value;
	  if(starty)
	   window.location.replace("userAction_list.jsp?starty="+starty);
	  else
	   alery("请输入正确的信息！");
	}
	
	function init(ids,cons,dis){
	   document.getElementById(ids).getElementsByTagName('li')[0].className='active';
	   document.getElementById(cons).innerHTML=document.getElementById(dis+"l1").innerHTML;
	//  document.getElementById(ids).onmouseover=function(){onmousOver(ids,cons,dis);}//鼠标指向激发效果
	   document.getElementById(ids).onclick=function(){onmousOver(ids,cons,dis);}//点击激发效果
	}
	
	function onmousOver(ids,cons,dis){
		o = o || window.event;
		var obj=o.target || o.srcElement;
		if (obj.tagName=='LI'){
			if (obj.className=='active'||obj.id=='more')return;
		 	var o=document.getElementById(ids).getElementsByTagName('li');
		    for (var i=0;i<=o.length-1;i++){o[i].className='default'}
		  
		    obj.className='active';
		    if (obj.className=='active'){document.getElementById(cons).innerHTML=document.getElementById(dis+obj.id).innerHTML;}
	   }
   }
  
</script>
<style>

.tab
{
 padding: 0;
 margin: 5 auto 1.5em auto;
 border-left: 1px solid #C1DAD7;
 border-collapse: collapse;
}
.menu,.menu li
{
 margin:0;
 padding:5 auto 1.5em auto;;
 height:24px;
 list-style:none;
 overflow:auto;
 text-align:left;
}
.menu
{
 border-bottom:5px solid #cccccc;
}
.menu .default
{
 width:104px;
 float:left;
 font-size:10pt;
 line-height:1.5;
 margin-left:1px;
 cursor:pointer; 
 background:url('l.jpg') no-repeat;
}
.menu .active
{
 width:104px;
 float:left;
 font-size:10pt;
 line-height:1.5;
 margin-left:1px;
 cursor:pointer; 
 font-weight:bold;
 color:#FFFFFF;
 background:url('h.jpg') no-repeat;
}

</style>
	</head>
	<body bgcolor="#FFFFFF">
		<h1 align="center">
			挨踢用户列表
		</h1>
		<%
			try {
				out
						.print("<input type='button' value='刷新' onclick='window.location.replace(\"userAction_list.jsp\")' />");

				KickManager kimanager = KickManager.getInstance();
		
				List<String> kicknames = kimanager.getKickName();
				List<Kick> kis = kimanager.getKicks();
				List<Kick> kickList = kimanager.getKickList();
				List<Kick> silentList = kimanager.getSilentList();
				List<Kick> talkList = kimanager.getTalkList();
				
				int totalcount = kis.size();
				int start = 0;
				int size = 20;
				if (request.getParameter("start") != null)
					start = Integer.parseInt(request.getParameter("start")
							.trim());
			    if(request.getParameter("starty")!=null){
			       try{
				       start = (Integer.parseInt(request.getParameter("starty").trim())-1)*20;
				       if(start<0||start>totalcount)
				        start = 0;
			       }catch(Exception e){
			          start = 0;
			       }
			    }
				String username = request.getParameter("username");
				if (username != null && username.trim().length() > 0) {
					if (kicknames.contains(username)) {
						Kick bp1 = kis
								.get(kicknames.indexOf(username));
						out.print("<br/><font color='red' >["
								+ bp1.getUsername() + "][" + bp1.getGmname());
						out.print("][" + bp1.getResult() + "][" + bp1.getDate());
						out
								.print("]</font><a href='passport_search.jsp?username="
										+ bp1.getUsername() + "' >修改</a>]<br/>");
					} else {
						out.print("没有记录在此表中！！");
					}
				}
				out
						.print("用户名：<input type='text' name='username' id='username' value='' /><input type='button' value='查询' onclick='query();' /><br/>");
				if (start > 0)
					out.print("<a href='?start=" + (start - 20) + "' >上一页</a>");
				out.print ((start / 20+1) + "/" + ((totalcount - 1) / 20+1));
				if ((start + 20) < totalcount)
					out.print("<a href='?start=" + (start + 20) + "' >下一页</a>");
				out.print("转到<input type='text' size='6px' value='' id='starty' name='starty' /><a href='javascript:jump();'>页</a><br/>");
				
				//###############
				
				//##############
//				out.print("<table width='80%' ><tr><th width=10% >用户名</th><th width=10% >踢线GM</th><th width='40%'>踢线原因</th><th width='10%'>踢线次数</th><th width='10%'>踢线时间</th><th width=20% >操作</th>");
//				if (kis != null && kis.size() > 0) {
//					for (int i = kis.size()-start-1; i >=0&&i>(kis.size()-start-1 -20); i--) {
//						Kick bp = kis.get(i);
//						out.print("<tr><td>" + bp.getUsername() + "</td><td>"
//								+ bp.getGmname());
//						out.print("<td>" + bp.getResult() + "</td><td>"
//								+ 1 + "</td><td>" + bp.getDate());
//						out
//								.print("</td><td><a href='passport_search.jsp?username="
///										+ bp.getUsername()
//										+ "' >修改</a></td></tr>");
//					}
//				}
//				out.print("</table>");
				
				out.print("<div class='tab'>");
				out.print("<ul id='nav1' class='menu'>");
				out.print("<li id='l1' class='default'>被踢线记录</li>");
				out.print("<li id='l2' class='default'>被解禁记录</li>");
				out.print("<li id='l3' class='default'>被禁言记录</li>");
				out.print("</ul>");
				
				out.print("<div class='con' id='con1'>");
				out.print("</div>");
				out.print("</div>");
				
				out.print("<div style='display:none'>"); 
				//踢线
				out.print("<div id='div1_l1'>");
				out.print("<table width='80%' ><tr><th width=10% >用户名</th><th width=10% >踢线GM</th><th width='40%'>踢线原因</th><th width='10%'>踢线次数</th><th width='10%'>踢线时间</th><th width=20% >操作</th>");
				if (kickList != null && kickList.size() > 0) {
					for (int i = kickList.size()-start-1; i >=0&&i>(kickList.size()-start-1 -20); i--) {
						Kick bp = kickList.get(i);
						out.print("<tr><td>" + bp.getUsername() + "</td><td>"
								+ bp.getGmname());
						out.print("<td>" + bp.getResult() + "</td><td>"
								+ 1 + "</td><td>" + bp.getDate());
						out
								.print("</td><td><a href='passport_search.jsp?username="
										+ bp.getUsername()
										+ "' >修改</a></td></tr>");
					}
				}
				out.print("</table>");
				out.print("</div>");
				
				//解禁
				out.print("<div id='div1_l2'>");
				out.print("<table width='80%' ><tr><th width=10% >用户名</th><th width=10% >解禁GM</th><th width='40%'>解禁原因</th><th width='10%'>解禁次数</th><th width='10%'>解禁时间</th><th width=20% >操作</th>");
				if (talkList != null && talkList.size() > 0) {
					for (int i = talkList.size()-start-1; i >=0&&i>(talkList.size()-start-1 -20); i--) {
						Kick bp = talkList.get(i);
						out.print("<tr><td>" + bp.getUsername() + "</td><td>"
								+ bp.getGmname());
						out.print("<td>" + bp.getResult() + "</td><td>"
								+ 1 + "</td><td>" + bp.getDate());
						out
								.print("</td><td><a href='passport_search.jsp?username="
										+ bp.getUsername()
										+ "' >修改</a></td></tr>");
					}
				}
				out.print("</table>");
				out.print("</div>");
				
				//禁言
				out.print("<div id='div1_l3'>");
				out.print("<table width='80%' ><tr><th width=10% >用户名</th><th width=10% >禁言GM</th><th width='40%'>禁言原因</th><th width='10%'>禁言次数</th><th width='10%'>禁言时间</th><th width=20% >操作</th>");
				if (silentList != null && silentList.size() > 0) {
					for (int i = silentList.size()-start-1; i >=0&&i>(silentList.size()-start-1 -20); i--) {
						Kick bp = silentList.get(i);
						out.print("<tr><td>" + bp.getUsername() + "</td><td>"
								+ bp.getGmname());
						out.print("<td>" + bp.getResult() + "</td><td>"
								+ 1 + "</td><td>" + bp.getDate());
						out
								.print("</td><td><a href='passport_search.jsp?username="
										+ bp.getUsername()
										+ "' >修改</a></td></tr>");
					}
				}
				out.print("</table>");
				out.print("</div>");
				out.print("</div>");
				out.print("<script>");
				out.print("init('nav1','con1','div1_')");
				out.print("</script>");
			

				
			} catch (Exception e) {
				out.print(StringUtil.getStackTrace(e));
			}
		%>

	</body>
</html>
