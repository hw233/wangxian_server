<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.boss.gm.gmuser.server.TransferQuestionManager"%>
<%@page import="com.fy.boss.gm.gmuser.TransferQuestion"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" rel="stylesheet" href="style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>处理问题</title>
<script type="text/javascript">
	function changeType2(tname){
		var str = "";
		if(tname=="常规咨询"){
			str = "<select name='erjiselect'><option>注册咨询</option><option>充值咨询</option><option>游戏内容咨询</option><option>游戏活动咨询</option><option>其他咨询</option></select>";
		}else if(tname=="玩家建议"){
			str = "<select name='erjiselect'><option>游戏活动建议</option><option>规则建议</option><option>平台建议</option><option>论坛建议</option><option>充值建议</option><option>其他建议</option></select>";
		}else if(tname=="数据异常"){
			str = "<select name='erjiselect'><option>角色异常</option><option>道具异常</option><option>任务异常</option><option>其他异常</option></select>";
		}else if(tname=="充值问题"){
			str = "<select name='erjiselect'><option>充值未到账</option><option>充值系统故障</option><option>充值区组错误</option><option>其他问题</option></select>";
		}else if(tname=="帐号问题"){
			str = "<select name='erjiselect'><option>无法登录</option><option>帐号纠纷</option><option>帐号找回</option><option>其他问题</option></select>";
		}else if(tname=="平台问题"){
			str = "<select name='erjiselect'><option>无法注册</option><option>无法登录</option><option>其他问题</option></select>";
		}else if(tname=="服务器问题"){
			str = "<select name='erjiselect'><option>服务器宕机</option><option>无服务器延迟</option><option>其他问题</option></select>";
		}else if(tname=="游戏BUG"){
			str = "<select name='erjiselect'><option>数据bug</option><option>NPC_bug</option><option>道具bug</option><option>图形bug</option><option>文字bug</option><option>平台bug</option><option>论坛bug</option><option>其他bug</option></select>";
		}else if(tname=="玩家申诉"){
			str = "<select name='erjiselect'><option>禁言申诉</option><option>封停申诉</option><option>其他申诉</option></select>";
		}else if(tname=="玩家举报"){
			str = "<select name='erjiselect'><option>外挂举报</option><option>广告举报</option><option>言论举报</option><option>其他举报</option></select>";
		}else if(tname=="其他问题"){
			str = "<select name='erjiselect'><option>其他问题</option></select>";
		}else if(tname="默认所有"){
			str = "<select name='erjiselect'><option>默认所有</option></select>";
		}
		document.getElementById('erjiwenti').innerHTML = str;		
	}
	
   function displaynone(){
		document.getElementById('serverStat').style.display = "none";   
		document.getElementById('yinc').style.display = "none"; 
		document.getElementById('xianshi').style.display = "";
   }
   
   function displayblock(){
	   document.getElementById('serverStat').style.display = "";  
	   document.getElementById('xianshi').style.display = "none";
	   document.getElementById('yinc').style.display = ""; 
   }
</script>
</head>
<body bgcolor="#c8edcc">
<h1>查询-处理问题</h1>
		<%
			out.print("<img src='images/xianshi.jpg' id='xianshi' width='30' height='30' onclick='displayblock()' style='display:none' title='显示选择菜单'>");
			out.print("<img src='images/yinc.jpg' id='yinc' width='30' height='30' onclick='displaynone()'  title='隐藏选择菜单'>");
		%>
<form method="post">
	<table id='serverStat' >
	<%
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = sdf.format(new Date());	
		XmlServerManager smanager = XmlServerManager.getInstance();
		List<XmlServer> totalservers = smanager.getServers();
		
		List<TransferQuestion> list = TransferQuestionManager.getInstance().getQuestions();
		
	%>
	
	<%!
		enum Type1 {
			常规咨询("注册咨询","充值咨询","游戏内容咨询","游戏活动咨询","其他咨询"),
			玩家建议("游戏活动建议","规则建议","平台建议","论坛建议","充值建议","其他建议"),
			数据异常("角色异常","道具异常","任务异常","其他问题"),
			充值问题 ("充值未到账","充值系统故障","充值区组错误"),
			帐号问题("无法登录","帐号纠纷","帐号找回"),
			平台问题("无法注册","无法登录"),
			服务器问题("服务器宕机","服务器延迟"),
			游戏BUG("数据bug","NPC_bug","道具bug","图形bug","文字bug","平台bug","论坛bug"),
			玩家申诉("禁言申诉","封停申诉"),
			玩家举报 ("外挂举报","广告举报","言论举报"),
			其他问题 ("其他问题");
			private String []typename;
			Type1(String...type2name){
				this.typename = type2name;
			}
		}
	%>
		
		<tr><td colspan="4" bgcolor="#CAE111"><B><font size="4">查询问题</font></B></td></tr>
		<tr><th>姓名：</th><td><input type="text" name="realname" value="" /></td>
		<th>服务器：</th><td><select name='servername'>
		<%
			out.print("<option>默认所有</option>");
			for (XmlServer ss : totalservers) {
				if (ss!=null) {
					out.print("<option>" + ss.getDescription() + "</option>");
				}
			}
		%>
		</select></td></tr>
		<tr><th>游戏名称</th><td><select name='gamename'><option>飘渺寻仙曲</option>
		<option>三国</option><option>潜龙</option><option>西游</option></select></td><th>游戏渠道:</th><td><input type='text' name='qudao' value=''/></td></tr>
		<tr><th>游戏账号：</th><td><input type="text" name="username" value="" /></td><th>角色名：</th><td><input type="text" name="playername" value=""/></td></tr>
		<tr><th>一级问题分类：</th><td>
		<select onchange="changeType2(this.value)" name="yijiselect">
			<%
			out.print("<option>默认所有</option>");
				for(Type1 type:Type1.values()){
					out.print("<option>"+type+"</option>");
				}
			%>		
		</select>
		</td><th>二级问题分类：</th><td id='erjiwenti'>
			<select name='erjiselect'><option>默认所有</option></select>		
		</td></tr>	
		<tr><th>事件状态：</th><td><select name='eventstate'><option>未处理</option>已解决<option></option><option></option></select></td><th>事件编号：</th><td><input type="text" name="eventid" value=""/></td></tr>
		<tr><th>处理人：</th><td><input type="text" name="handler" value="" /></td><th>处理部门：</th><td><select name='handletodoor'><option>默认所有</option><option>运维部</option><option>运营部</option><option>客服部</option></select></td></tr>
		<tr><th>递交部门：</th><td><select name='handledoor'><option>默认所有</option><option>技术部</option><option>运营部</option><option>客服部</option></select></tr>
		<tr><td colspan="4"><input type='button' value='查询' onclick="submit()"/><input type='reset' value='清空' /></td></tr>	
	</table>
	<hr>
	<%
		if(list!=null&&list.size()>0){
	%>		
		<table>
			<tr><th>游戏名称</th><th>一级分类</th><th>事件数</th><th>批处理</th><th>操作</th></tr>
			<%
				Map<String,Map<String,List<TransferQuestion>>> games = new HashMap<String,Map<String,List<TransferQuestion>>>();
				for(TransferQuestion tt:list){
					if (!games.containsKey(tt.getGameName())) {
						games.put(tt.getGameName(), new HashMap<String,List<TransferQuestion>>());
					}
					if (!games.get(tt.getGameName()).containsKey(tt.getQuestionType1())) {
						games.get(tt.getGameName()).put(tt.getQuestionType1(), new ArrayList<TransferQuestion>());
					}
					games.get(tt.getGameName()).get(tt.getQuestionType1()).add(tt);
				}
			
				
			%>
			<%
				for(Iterator<String> itor = games.keySet().iterator();itor.hasNext();) {
					String gameName = itor.next();
					Map<String,List<TransferQuestion>> gameQuestions = games.get(gameName);
					for (Iterator<String> inner = gameQuestions.keySet().iterator();inner.hasNext();) {
						String type1 = inner.next();
					%>
					<tr><td><%=gameName %></td><td><%=type1 %></td><td><%=gameQuestions.get(type1).size() %></td><td><a title="领取所有问题，别人不能继续领取" href="baidu.com">批处理</a></td><td><a title="展开一级问题" href="questionHandle.jsp?questionType1=<%=type1 %>">查看</a></td></tr>
					<%
				}}
			%>
			
			
		</table>
	<%		
		}else{
			out.print("没有记录！");
		}
	%>
	
	
</form>	
</body>
</html>