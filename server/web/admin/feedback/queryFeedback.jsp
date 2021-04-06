<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.gm.feedback.Feedback"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.gm.feedback.service.FeedbackManager"%>
<%@page import="com.xuanzhi.stat.model.PlayerMakeDealFlow"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gm.feedback.FeedBackState"%>
<%@page import="com.fy.engineserver.gm.feedback.GMRecord"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style> 
  
.fade{display: none; position: absolute; top: 0%; left: 0%; width: 100%; height: 100%; z-index:1001;background-color: white}  
.small{display: none; position: absolute; top: 15%; left: 25%; width:500; height:500; padding: 1px;  border: 1px solid black; z-index:1002;}  
  
</style> 
<script type="text/javascript">

	function queryfeedback(){
		 obj = document.getElementById('gmSelect'); 
		 var index = obj.selectedIndex; // 选中索引
		 var str = obj.options[index].text; // 选中文本
		 document.getElementById('gm').value = str;
		 document.queryFeedBack.submit();
	}
	
	function send(){
		 obj = document.getElementById('gmSelect'); 
		 var index = obj.selectedIndex; // 选中索引
		 var str = obj.options[index].text; // 选中文本
		 document.getElementById('gm').value = str;
		 document.getElementById("follow").value = document.getElementById("ajaxfeedbackId").value;
		 document.queryFeedBack.submit();
	}
	
	function close(){
		alert(11);
		 obj = document.getElementById('gmSelect'); 
		 var index = obj.selectedIndex; // 选中索引
		 var str = obj.options[index].text; // 选中文本
		 document.getElementById('gm').value = str;
		 document.getElementById("close").value = document.getElementById("ajaxfeedbackId").value;
		 document.queryFeedBack.submit();
		 alert(22);
	}
	

</script>
<link rel="stylesheet" href="../css/style.css"/>
<style type="text/css">
body {
	background-color: #c8edcc;
}
</style>
 <script type="text/javascript" src="feedback.js"></script>
</head>
<body >
				<%
					int perNum = 100;
					String begin = request.getParameter("beginTime");
					if(begin == null || begin.equals("")){
							begin = "";
					}
					String end = request.getParameter("endTime");
					if(end == null || end.equals("")){
						end = "";
					}
					String[] feedBackType =request.getParameterValues("feedbackType");
					if(feedBackType == null){
						feedBackType = new String[5];
						for(int i=0;i< 5;i++){
							feedBackType[i] = i+"";
						}
					}
					
					String[] feedBackState =request.getParameterValues("feedbackState");
					if(feedBackState == null){
						feedBackState = new String[4];
						for(int i=0;i< 4;i++){
							feedBackState[i] = i+"";
						}
					}
					
					String gm = request.getParameter("gm");
					String queryvip = request.getParameter("queryvip");
					String numSt = request.getParameter("hiddenNum");
					int num = Integer.parseInt(numSt.trim());
					String follow = request.getParameter("follow");
					String close = request.getParameter("close");
					List<Feedback> list = null;
					int maxPage = 1;
					Feedback fff = null;
					if(follow!=null&&follow.trim().length()>0){
						fff = FeedbackManager.getInstance().getFeedBack(Long.parseLong(follow.trim()));
						fff.setFollow(true);
					}
					if(close!=null&&close.trim().length()>0){
						fff = FeedbackManager.getInstance().getFeedBack(Long.parseLong(close.trim()));
						fff.setFollow(false);
					}
					
					try{
						list = FeedbackManager.getInstance().oldqueryFeedbacks(begin,end,feedBackType,feedBackState,gm,num);
						String queryname = request.getParameter("queryname");
						String queryscore = request.getParameter("queryscore");
						String queryfollow1 = request.getParameter("queryfollow1");
						
						List<Feedback> list1 = new ArrayList<Feedback>();
						PlayerManager pm = PlayerManager.getInstance();
						if(queryfollow1!=null&&queryfollow1.trim().length()>0){
							for(Feedback f:list){
								try{
									if(f.isFollow()){
										list1.add(f);
									}
								}catch(Exception e){
									continue;
								}
							}
							list = list1;
						}
						if(queryvip!=null&&queryvip.trim().length()>0){
							int level = 0;
							for(Feedback f:list){
								try{
									level = f.getVipLevel();
									if(level==Integer.parseInt(queryvip.trim())){
										list1.add(f);
									}
								}catch(Exception e){
									continue;
								}
							}
							list = list1;
						}
						
						if(queryname!=null&&queryname.trim().length()>0){
							String name = "";
							for(Feedback f:list){
								try{
									name = pm.getPlayer(f.getPlayerId()).getName();
									if(name.equals(queryname)){
										list1.add(f);
									}
								}catch(Exception e){
									continue;
								}
							}
							list = list1;
						}
						
						if(queryscore!=null&&queryscore.trim().length()>0){
							String[] judges = {"满意","一般","不好"};
							for(Feedback ff:list){
								if(ff.isAlreadyJudge()){
									if(queryscore.equals(judges[ff.getJudge()])){
										list1.add(ff);
									}
								}
							}
							list = list1;
						}
						
						
						maxPage = (int)Math.ceil((1f*list.size())/perNum);
						if(maxPage == 0){
							maxPage = 1;
						}
						if(list.size() > num*perNum){
							int max = list.size();
							if(num*perNum + perNum > list.size()){
							}else{
								max = num*perNum+perNum;
							}
							list = list.subList(num*perNum, max);
							FeedbackManager.logger.warn("[gm查询记录完成] [gmName"+gm+"] [beginNum:"+num*perNum+"] [个数:"+list.size()+"]");
						}else{
							FeedbackManager.logger.warn("[gm查询记录完成没有多余的记录] [gmName"+gm+"] [beginNum:"+num*perNum+"] [个数:"+list.size()+"]");
						}
					}catch(Exception e){
						out.print("输入格式错误，请重新输入"+e);
						FeedbackManager.logger.error("[gm查询记录异常] ["+gm+"] []",e);
						return;
					}
				%>

				<table>
      				<form action="queryFeedback.jsp" name="queryFeedBack">
					<input type="hidden"  id="follow" name="follow"/>
					<input type="hidden"  id="close" name="close"/>
      				<tr><th>开始时间:(2011-01-11)</th><td><input name="beginTime" type="text" value="<%=begin %>"/></td></tr>
      				<tr><th>截止时间:(2011-01-11)</th><td><input name="endTime" type="text" value="<%=end %>"/></td></tr>
      				<tr><th>反馈类型（可多选）</th><input type="hidden" name="hiddenNum" value="0"/>
      					<td><input type="checkbox" name="feedbackType" value ="0" checked="checked">bug
							<input type="checkbox" name="feedbackType" value ="1" checked="checked">建议
							<input type="checkbox" name="feedbackType" value ="2" checked="checked">投诉
							<input type="checkbox" name="feedbackType" value ="3" checked="checked">充值
							<input type="checkbox" name="feedbackType" value ="4" checked="checked">其他
						</td>
					</tr>
      				<tr><th>反馈状态（可多选）：</th>
      					<td>
      						<input type="checkbox" name="feedbackState" value ="0" checked="checked">未处理
							<input type="checkbox" name="feedbackState" value ="1">等待处理
							<input type="checkbox" name="feedbackState" value ="2" checked="checked">新反馈
							<input type="checkbox" name="feedbackState" value ="3">已关闭
      					</td>
      				</tr>
      				<tr><th>gm编号：</th>
      					<td>
      						<input type="hidden" id="gm" name="gm" /> 
						  		 <select name="gm" id="gmSelect">
								   	<%
								   	List<GMRecord> list1 = FeedbackManager.getInstance().getAllGm();
								   	boolean bln = true;
								   	int i= 0;
									out.print("<option value=\""+0+"\" selected=\"selected\">全部</option>");
									++i;
								   	for(GMRecord gr : list1){
								   		if(bln){
								   			out.print("<option value=\""+i+">"+gr.getGmName()+"</option>");
								   			bln = false;
								   		}else{
								   			out.print("<option value=\""+i+"\">"+gr.getGmName()+"</option>");
								   		}
								   		++i;
								   	}
								   	%>
			      				</select>
      					</td>
      				</tr>
      				<tr><th>角色名查询</th><td><input type="text" name="queryname"></td></tr>
	      			<tr><th>评分查询</th><td><input type="text" name="queryscore"></td></tr>
	      			<tr><th>VIP查询</th><td><input type="text" name="queryvip"></td></tr>
	      			<tr><th>追踪查询</th><td><input type="text" name="queryfollow1"></td></tr>
	      			<tr align="center">
	     			<th colspan="2"><input type="button"  onclick="queryfeedback()" value="查询"/></th></tr>
	      			</form>
      			</table>
					
				<table cellspacing="1" cellpadding="2" border="1" >
					<tr>
		  				<td>日期</td>
		  				<td>时间</td>
		  				<td>反馈状态</td>
		  				<td>角色名</td>
		  				<td>反馈类型</td>
		  				<td>反馈内容标题</td>
		  				<td>操作人员</td>
		  				<td>是否发送评价</td>
		  				<td>用户评分</td>
		  				<td>是否追踪</td>
		  				<td>VIP等级</td>
		  			</tr>
				<%
				StringBuffer sb = new StringBuffer();
				String[] states = {"未处理","等待","新","关闭","发送评价"};
				String[] types = {"BUG","建议","投诉","充值","其他"};
				String[] judges = {"满意","一般","不好"};
				String[] colors = {"red","blue","green","black"};
				if(list == null || list.size()== 0){
					out.print("<font color='red'>没有合适的记录!!");
				}else{
					out.print(list.size()+"<font color='red'>条记录!");
					PlayerManager pm = PlayerManager.getInstance();
					for(Feedback f : list){
						
						long time = f.getBeginDate();
						
						SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
						
						String st = format1.format(new Date(time));
						String st2 = format2.format(new Date(time));
						String playerName = "";
						
						try{
							playerName = pm.getPlayer(f.getPlayerId()).getName();
						}catch(Exception e){
							FeedbackManager.logger.error("[gm查询错误] [不存在这个玩家]",e);
							playerName = "账号已经删除，不存在";
						}
						int state = f.getGmState();
						
						int ftype = f.getFeedbackType();
						
						String content = f.getSubject();
						String judge = "无";
						if(f.isAlreadyJudge()){
							judge = judges[f.getJudge()];
						}
						String id="s"+f.getId();
						sb.append(f.getId()+"|");
						String 发送评价 = "没有发送";
						if(f.isSendJudge()){
							发送评价 = "已经发送";
						}
						boolean isfollow = f.isFollow();
						String yes = "";
						if(isfollow){
							yes = "是";
						}else{
							yes = "否";
						}
						
						
						%>
					<tr>
		  				<td><%=st %></td>
		  				<td><%= st2 %></td>
		  				<td id=\"<%=id %>\" style="color:<%=colors[state] %>"><%= states[state] %></td>
		  				<td><%= playerName %></td>
		  				<td><%=types[ftype]  %></td>
		  				<td><a href="javascript:ajax('<%=basePath %>','<%=f.getId()%>')"><%=content%></a></td>
		  				<td><%= f.getLastGmId() %></td>
		  				<td><%= 发送评价%></td>
		  				<td><%= judge %></td>
		  				<td><%= yes%></td>
		  				<td><%=f.getVipLevel() %></td>
		  			</tr>
						<%
					}
				}
				
				%>
				</table>
				
				<input type="hidden" name="update" id="update" value=<%=sb.toString()%> />
				
				<input type="hidden" name="ajaxfeedbackId" id="ajaxfeedbackId" />
	
		<div id="small" class="small"> 
		
		<table width="500" height="350" cellspacing="1" cellpadding="2" border="1" >
			<tr>
				<td width="150" valign="top" id="left">
				
				</td>
				<td width="350" valign="top">
					<div style="width:350px;height:350px;overflow-y:scroll; border:1px solid;" id="right">
				
					</div>
				</td>
			</tr>
		</table>
	
		<form action="reply.jsp" method="get" name="reply">
		
			<textarea cols="60" rows="3" name="reply"></textarea>
			<input type="hidden"  id="feedbackId1" name="feedbackId1"/>
			<input type="button" onclick="replyPlayer()" value="发送"/>
		</form>
	
			<form action="sendJudge.jsp" method="get" name="judge">
				<input type="hidden"  id="feedbackId2" name="feedbackId2"/>
			</form>
			
			<form action="closeFeedback.jsp" method="get" name="closeFeedback">
				<input type="hidden"  id="feedbackId3" name="feedbackId3"/>
			</form>
			
			<table>
			<br>
				<tr>
					<td width="200"><a href="javascript:sendjudge()">发送评分</a></td>
					<td width="200"><a href="javascript:closeFeedback()">关闭当前反馈</a></td>
					<td width="100">
					<a href="javascript:void(0)" onclick="closeWindow(11)">关闭</a></td>
					<%
						
						if(fff==null||!fff.isFollow()){
							out.print("<td width='200'><input type='button' onclick='send()' value='设置追踪'/></td>");
						}else{
							out.print("<td width='200'><input type='button' onclick='close()' value='关闭追踪'/></td>");
						}
							
					%>
							
					
					
					
				</tr>
			</table>
		<br/>
		
	</div> 

	<div id="fade" class="fade"> 
	</div> 
	
					
					
	<br/>
	<% 
	if(num >= 1){
		out.print("<a href =\"javascript:void(0)\" onclick=\"submit(true)\" >上一页</a>");
	}
	out.print("第"+(num+1)+"页，共"+maxPage+"页");
	if(maxPage >1 && (num+1) < maxPage){
		out.print("<a href =\"javascript:void(0)\" onclick=\"submit(false)\">下一页</a>");
	}
	%>
	
</body>



</html>