<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.newfeedback.NewFeedback"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Date"%>
<%@page import="com.xuanzhi.tools.text.DateUtil"%>
<%@page import="com.fy.boss.gm.record.TelRecord"%>
<%@page import="com.fy.boss.gm.record.TelRecordManager"%>
<%@page import="com.fy.boss.gm.record.ActionManager"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="gm/css/style.css"/>
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<title>选择服务器</title>
</head>
<script language="javascript"> 

	function lookPlayerMess(servername,username,playername,viplevel,serverip,serverport){
		if(username){
			if(servername=="柳暗花明"||servername=="太虚幻境"||servername=="幽冥山谷"||servername=="昆仑圣殿"||servername=="凌霄宝殿"||servername=="霸气乾坤"||servername=="烟雨青山"||servername=="仙山琼阁"||servername=="霸气无双"||servername=="华山之巅"||servername=="神龙摆尾"){
				window.open("http://117.135.128.177:8801/game_gateway/userServerMess.jsp?username="+username+"&servername="+servername+"&playername="+playername+"&viplevel="+viplevel,'newwindow','width=700,height=460,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no');
			}else{
				window.open("http://116.213.192.216:8882/game_gateway/userServerMess.jsp?username="+username+"&servername="+servername+"&playername="+playername+"&viplevel="+viplevel,'newwindow','width=700,height=460,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no');
			}
		}else{
			alert("没有信息可以查询！");
		}
	}
	
	function openDialogue(index){
		if(index){
			var ss = document.getElementById('opendiv'+index).style.display;
			if (ss == "none"){
				document.getElementById('opendiv'+index).style.display = "block";
				var o = {'feedbackid':index};
				$.ajax({
					  type: 'POST',
					  url: "getTalks.jsp",
					  contentType : "application/x-www-form-urlencoded; charset=utf-8",
					  data: o,
					  dataType: "html",
						  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
						  success: function(result)
						  {
	 						  if(result!=null){
	 								mess = result.split("@#$%^");	
									if(mess.length>0){
// 										document.getElementById('record').innerHTML = "";
										for(var i=0;i<mess.length-1;i++){
											talkss = mess[i].split("@@##$$");
											if(document.getElementById(talkss[0])==null){
												$("#record"+index).append(talkss[1]);
											}	
										}
									}
	 						  }
						  }
					});
				document.getElementById('record'+index).scrollTop = document.getElementById('record'+index).scrollHeight;
			} else if (ss == "block"){
				document.getElementById('opendiv'+index).style.display = "none";
			}
		}else{
			alert("打开聊天对话框出错，请找相关人员解决！");
		}
	}
	
</script>

<body bgcolor="#c8edcc">
	<% 
		Object obj = session.getAttribute("authorize.username");
		if(obj!=null){
			String gmid = request.getParameter("gmid");
			out.print(gmid);
			NewFeedbackQueueManager nmanager = NewFeedbackQueueManager.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String starttime = sdf.format(new Date());
			String endtime = sdf.format(System.currentTimeMillis()+24*60*60*1000);
			long startdate = sdf.parse(starttime).getTime();
			long enddate = sdf.parse(endtime).getTime();
			String starttimereq = request.getParameter("starttime");
			String endtimereq = request.getParameter("endtime");
			List<NewFeedback> list = new ArrayList<NewFeedback>();
			if(starttimereq!=null &&starttimereq.trim().length()>0&& endtimereq!=null&&endtimereq.trim().length()>0){
				long startdate2 = sdf.parse(starttimereq).getTime();
				long enddate2 = sdf.parse(endtimereq).getTime();
				list = nmanager.getFeedbacksByHandler(gmid, startdate2, enddate2);
			}else{
				list = nmanager.getFeedbacksByHandler(gmid, startdate, enddate);
			}
	%>	
		<table cellspacing="1" cellpadding="2" border="1" id="msg">
		<caption id='title1'><%=list.size() %></caption>
			<tr><th>GM编号</th><th>服务器</th><th>玩家反馈时间</th><th>账号</th><th>角色名</th><th>标题</th><th>VIP等级</th><th>GM状态</th><th>玩家身上</th><th>玩家状态</th><th>服务评价</th></tr>
			<%
				if(list.size()>0){
					for(NewFeedback fb:list){
						String 玩家状态 = "";
						String 服务评价 = "";
						String GM状态 = "";
						String 玩家身上 = "";
						if(fb.getPlayerState()==0){
							玩家状态 = "等待回复";
						}else if(fb.getPlayerState()==1){
							玩家状态 = "有新回复";
						}else if(fb.getPlayerState()==2){
							玩家状态 = "评分";
						}else{
							玩家状态 = "关闭";
						}
						
						if(fb.getScorestate()==1){
							服务评价 = "满意";
						}else if(fb.getScorestate()==2){
							服务评价 = "一般";
						}else if(fb.getScorestate()==3){
							服务评价 = "不满意";
						}else{
							服务评价 = "未评分";
						}
						
						if(fb.getGmstat()==0){
							GM状态 = "未处理完毕";
						}else if(fb.getGmstat()==1){
							GM状态 = "删除";
						}else if(fb.getGmstat()==2){
							GM状态 = "挂起";
						}
						
						if(fb.getState()==0){
							玩家身上 = "玩家删除";
						}else if(fb.getState()==1){
							玩家身上 = "在身上";
						}else if(fb.getState()==2){
							玩家身上 = "系统删除";
						}
			%>
			<tr><td><%=gmid %></td><td><%=fb.getServername() %></td><td><%=fb.getSendtime() %></td><td><a href="javascript:lookPlayerMess('<%=fb.getServername()%>','<%=fb.getUsername()%>','<%=fb.getPlayername()%>','<%=fb.getViplevel()%>')"><%=fb.getUsername() %></a></td><td><%=fb.getPlayername() %></td><td><a href="javascript:openDialogue('<%=fb.getId()%>')"><%=fb.getTitle() %></a></td><td><%=fb.getViplevel() %></td><td><%=GM状态 %></td><td><%=玩家身上 %></td><td><%=玩家状态 %></td><td><%=服务评价 %></td></tr>	
			<tr><td colspan='11'><div id='opendiv<%=fb.getId() %>' style="display:none"><table style='background-color:#f8f6ff;width:500px;height:204px; border:4px solid #3399cc;'><tr><td><table><tr><div style='width:330px;height:200px;overflow-x:hidden;overflow-y:scroll; border:2px solid #3399cc;' id='record<%=fb.getId()%>'></div></tr></table></td><td width='165px' valign='top'><br>服务器：<%=fb.getServername() %><br>账 号：<%=fb.getUsername() %><br>角色名：<%=fb.getPlayername() %><br>VIP等级：<%=fb.getViplevel() %></td></tr></table></div></td></tr>
			<%								
					}
				}
			%>
		</table>
	 <%  		
	 	}else{
			out.print("请给该页面设置过滤器！");
		}
		
	%>

</body>
</html>

