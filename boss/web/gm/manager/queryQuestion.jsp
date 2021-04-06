<%@page import="java.util.Calendar"%>
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
<link type="text/css" rel="stylesheet" href="../css/style.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>处理问题</title>
<script type="text/javascript">
	function changeType2(tname){
		var str = "";
		if(tname=="运行问题"){
			str = "<select name='erjiselect'><option>无法登陆</option><option>运行报错</option><option>其他问题</option></select>";
		}else if(tname=="玩家建议"){
			str = "<select name='erjiselect'><option>游戏活动建议</option><option>规则建议</option><option>平台建议</option><option>论坛建议</option><option>充值建议</option><option>其他建议</option></select>";
		}else if(tname=="数据异常"){
			str = "<select name='erjiselect'><option>功能异常</option><option>人物异常</option><option>宠物异常</option><option>坐骑异常</option><option>装备异常</option><option>活动异常</option><option>任务异常</option><option>交易类异常</option><option>其他异常</option></select>";
		}else if(tname=="充值问题"){
			str = "<select name='erjiselect'><option>充值未到账</option><option>SDK充值问题</option><option>充值异常</option><option>充值活动问题</option><option>其他问题</option></select>";
		}else if(tname=="帐号问题"){
			str = "<select name='erjiselect'><option>密码找回</option><option>帐号纠纷</option><option>帐号找回</option><option>修改密保</option><option>申请封停</option><option>账号禁言</option><option>帐号封停</option></select>";
		}else if(tname=="丢失问题"){
			str = "<select name='erjiselect'><option>丢弃问题</option><option>邮件丢失</option><option>银锭丢失</option><option>物品丢失</option><option>其他丢失</option></select>";
		}else if(tname=="游戏问题"){
			str = "<select name='erjiselect'><option>游戏内容问题</option><option>游戏功能咨询</option><option>游戏道具咨询</option><option>游戏活动咨询</option><option>游戏任务咨询</option><option>其他咨询问题</option></select>";
		}else if(tname=="其他问题"){
			str = "<select name='erjiselect'><option>提交BUG</option><option>提交建议</option><option>玩家举报</option><option>投诉</option><option>其他</option></select>";
		}else if(tname=="--"){
			str = "<select name='erjiselect'><option>--</option></select>";
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
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date());	
		XmlServerManager smanager = XmlServerManager.getInstance();
		List<XmlServer> totalservers = smanager.getServers();
		String username = request.getParameter("username");
		String telephone = request.getParameter("telephone");
		String begintime = request.getParameter("begintime");
		String endtime = request.getParameter("endtime");
		String handletodoor = request.getParameter("handletodoor");
		String commiter = request.getParameter("commiter");
		String eventid = request.getParameter("eventid");
		List<String> times = new ArrayList<String>();
		List<TransferQuestion> list2 = new ArrayList<TransferQuestion>();
		List<TransferQuestion> list = TransferQuestionManager.getInstance().getQuestions();
		if(begintime!=null&&begintime.trim().length()>0&&endtime!=null&&endtime.trim().length()>0){
			int beginDay = Integer.parseInt(begintime.split("-")[2]);
			int endDay = Integer.parseInt(endtime.split("-")[2]);
			for(int i=0;i<(endDay-beginDay);i++){
				String timelist = sdf.format(System.currentTimeMillis() - i*24*60*60*1000);
				times.add(timelist);
			}
			if(username!=null&&username.trim().length()>0){
				list2 = TransferQuestionManager.getInstance().getQuestionsByUsername(username, 1, 1000);
			}else if(telephone!=null&&telephone.trim().length()>0){
				list2 = TransferQuestionManager.getInstance().getQuestionsByTelephone(telephone, 1, 1000);
			}else if(handletodoor!=null&&handletodoor.trim().length()>0&&!handletodoor.equals("--")){
				list2 = TransferQuestionManager.getInstance().getQuestionsByBuMengBetweenTime(handletodoor, begintime, endtime, 1, 1000);
			}else if(commiter!=null&&commiter.trim().length()>0){
				list2 = TransferQuestionManager.getInstance().getNumberByCommiter(commiter, begintime, endtime,1, 2000);
			}else if(eventid!=null&&eventid.trim().length()>0){
				list2 = TransferQuestionManager.getInstance().getQuestionsByEventid(eventid, 1, 1000);
			}
		}
		String begin = sdf.format(System.currentTimeMillis() - 6*24*60*60*1000);
		String end = sdf.format(System.currentTimeMillis()+24*60*60*1000);
		if(begintime==null||endtime==null){
			int beginDay = Integer.parseInt(begin.split("-")[2]);
			int endDay = Integer.parseInt(end.split("-")[2]);
			for(int i=0;i<(endDay-beginDay);i++){
				String timelist = sdf.format(System.currentTimeMillis() - i*24*60*60*1000);
				times.add(timelist);
			}
		}
		
		
	%>
	
	<%!
		enum Type1 {
		运行问题("无法登陆","运行报错","其他问题"),
		玩家建议("游戏活动建议","规则建议","其他建议"),
		数据异常("功能异常","人物异常","宠物异常","坐骑异常","装备异常","活动异常","任务异常","交易类异常","其他异常"),
		充值问题 ("充值未到账","充值异常","渠道充值问题","充值活动问题","其他充值问题"),
		帐号问题("密码找回","帐号纠纷","帐号找回","修改密保","申请封停","申请解封","封停申诉","账号禁言","帐号封停"),
		丢失问题("丢弃问题","邮件丢失","银锭丢失","物品丢失"),
		游戏问题("游戏内容问题","游戏功能咨询","游戏道具咨询","游戏活动咨询","游戏任务咨询","其他咨询问题"),
		其他问题 ("提交BUG","玩家举报","投诉","其他");
			private String []typename;
			Type1(String...type2name){
				this.typename = type2name;
			}
		}
	%>
		
		<tr><td colspan="4" bgcolor="#CAE111"><B><font size="4">查询问题</font></B></td></tr>
		<tr><th>联系电话：</th><td><input type="text" name="telephone" value=""/></td><th>游戏账号：</th><td><input type="text" name="username" value="" /></td></tr>
		<tr><th>递交人：</th><td><input type="text" name="commiter" value=""/></td><th>处理部门：</th><td><select name='handletodoor'><option>--</option><option>技术部门</option><option>页面部门</option><option>呼叫部门</option><option>运营部门</option><option>已处理</option></select>匹配时间段查询</td></tr>
		<tr><th>时间段：</th><td colspan="3"><input type='text' name='begintime' id='begintime' value='<%=begin%>'>--<input type='text' name='endtime' id='endtime' value='<%=end%>'></td></tr><font color='red'></font>
		<tr><th>事件编号：</th><td><input type="text" name="eventid" value=""/></td><td colspan="2"><input type='button' value='查询' onclick="submit()"/><input type='reset' value='清空' /></td></tr>	
	</table>
	
	<hr>
	<%
		if(list!=null&&list.size()>0&&list2.size()==0){
	%>		
		<table>
			<tr><th>游戏名称</th><th>一级分类</th><th>事件数</th><th>操作</th></tr>
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
					<tr><td><%=gameName %></td><td><%=type1 %></td><td><%=gameQuestions.get(type1).size() %></td><td><a title="展开一级问题" href="questionHandle.jsp?questionType1=<%=type1 %>">查看</a></td></tr>
					<%
				}}
			%>
			
			
		</table>
		<table>
			<tr>
				<th><font color='red'><B>问题所在时间</B></font></th>
				<%
					for(int j=1;j<times.size();j++){
						if(times.get(j)!=null){
							out.print("<th>"+times.get(j)+"</th>");
						}						
					}					
				%>
			</tr>
			<tr>
				<th><font color='red'><B>扭转问题数量</B></font></th>
				<%
					for(int k=0;k<times.size();k++){
						if(times.get(k)!=null){
							if(k-1>=0){
								out.print("<td>"+TransferQuestionManager.getInstance().getNumberByDay(times.get(k),times.get(k-1))+"</td>");
							}
						}						
					}					
				%>
			
			</tr>
			
		</table>
		
		
	<%		
		}
	%>
</form>
	<%
		if(list2.size()>0){
			out.print("数量："+list2.size());
			%>
			<table>
				<tr><th>事件编号</th><th>游戏名称</th><th>服务器名</th><th>账号</th><th>VIP用户</th><th>一级分类</th><th>二级分类</th><th>提交时间</th><th>问题所在部门</th><th>当前状态</th><th>操作</th></tr>
				<%
				Iterator it = list2.iterator();
					while(it.hasNext()){
						TransferQuestion qt = (TransferQuestion)it.next();
						if(qt.getBackNum()>0){
						%>
						<tr style="background-color: #d297cc"><td><%=qt.getEventid() %></td><td><%=qt.getGameName() %></td><td><%=qt.getServerName() %></td><td><%=qt.getUsername() %></td><td><%=qt.getViplevel() %></td><td><%=qt.getQuestionType1() %></td><td><%=qt.getQuestionType2() %></td><td><%=qt.getRecordTime()%></td><td><%=qt.getHandlOtherBuMeng() %></td><td><%=qt.getCurrHadler() %></td><td><a title="我来处理" href="questionLastHandle.jsp?id=<%=qt.getId() %>">处理</a>||<a title="查看问题" href="questionLastHandle.jsp?idd=chakan&id=<%=qt.getId() %>">查看</a></td></tr>
						<%
						}else{
							%>
							<tr><td><%=qt.getEventid() %></td><td><%=qt.getGameName() %></td><td><%=qt.getServerName() %></td><td><%=qt.getUsername() %></td><td><%=qt.getViplevel() %></td><td><%=qt.getQuestionType1() %></td><td><%=qt.getQuestionType2() %></td><td><%=qt.getRecordTime()%></td><td><%=qt.getHandlOtherBuMeng() %></td><td><%=qt.getCurrHadler() %></td><td><a title="我来处理" href="questionLastHandle.jsp?id=<%=qt.getId() %>">处理</a>||<a title="查看问题" href="questionLastHandle.jsp?idd=chakan&id=<%=qt.getId() %>">查看</a></td></tr>
							<%
						}
					}
				%>
			</table>
			<%			
		}	
	%>
	
	
	
</body>
</html>