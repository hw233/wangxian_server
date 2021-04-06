<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackStateManager"%>
<%@page import="com.fy.boss.gm.newfeedback.FeedbackState"%>
<%@page import="com.fy.boss.gm.XmlServer"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.boss.gm.XmlServerManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.boss.gm.newfeedback.GmTalk"%>
<%@page import="com.fy.boss.gm.newfeedback.NewFeedback"%>
<%@page import="com.fy.boss.gm.newfeedback.service.NewFeedbackQueueManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type='text/javascript' src='jquery-1.6.2.js'></script>
<link rel="stylesheet" href="../gm/css/style.css" />
<title>反馈处理</title>
<script type='text/javascript' src='/game_boss/dwr/engine.js'></script>
<script type="text/javascript" src="/game_boss/dwr/util.js"></script>
<script type="text/javascript" src='/game_boss/dwr/interface/newfeedback.js'></script>
</head>
<%!long flag = 0;%>
<script language="javascript">
	window.onload=function(){
		var isshow = document.getElementById('updateid').value;
		var gmnum = document.getElementById('updateid2').value;
		if(isshow>0){
			var o = {'feedbackid':isshow};
			$.ajax({
				  type: 'POST',
				  url: "setGMHandleid.jsp",
				  contentType : "application/x-www-form-urlencoded; charset=utf-8",
				  data: o,
				  dataType: "html",
// 					  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
					  success: function(result)
					  {
// 						  alert(result);
					  }
				});
			self.setInterval("newmess('"+isshow+"','"+gmnum+"')",3000);
		}
	}
	function newmess(isshow,gmnum){
		if(isshow>0){
			var o = {'id':isshow,'gmnum':gmnum};
			$.ajax({
				  type: 'POST',
				  url: "getTalksByid.jsp",
				  contentType : "application/x-www-form-urlencoded; charset=utf-8",
				  data: o,
//					  complete:function(result){alert("completed"+result)},
				  dataType: "html",
// 					  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
					  success: function(result)
					  {
						  if(result!=null){
							  if(result.indexOf("玩家删除")<0){
								  mess = result.split("@#$%^");	
									if(mess.length>0){
										document.getElementById('record').innerHTML = "";
										for(var i=0;i<mess.length-1;i++){
											talkss = mess[i].split("@@##$$");
											if(document.getElementById(talkss[0])==null){
												$("#record").append(talkss[1]);
											}	
										}
									}
							  }else{
								  alert("玩家删除了该反馈，请继续下一个！");
								  window.location.replace("?handle="+gmnum);
							  }
						  }
					  }
				});
		}
	}
	
	function send(id,servername,playername,gmnum,url,sendtime){
		var myDate = new Date();
		var systemtime = myDate.getSeconds();
		var tt = $("#senttimehid").val(); 
		if(systemtime<10){
			systemtime = 10;
		}
		if(systemtime<tt){
			document.getElementById('senttimehid').value = 0;
		}
		if(systemtime-tt>=10){
			document.getElementById('senttimehid').value = systemtime;
			var mess = $("#talkmess").val();
			if(mess&&mess.length<300){
				if(id&&servername&&playername&&gmnum&&url){
					var adminurl = url+"/getTalks.jsp";
					var o = {'content':mess,
							'playername':playername,
							'gmnum':gmnum,
							'id':id,
							'url':adminurl
							};

					$.ajax({
						  type: 'POST',
						  url: "boss_server.jsp",
						  contentType : "application/x-www-form-urlencoded; charset=utf-8",
						  data: o,
						  dataType: "html",
// 							  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
							  success: function(result)
							  {
								  if(result=="yes"){
									  document.getElementById('messid').innerHTML = "<font color='red'>OK</font>";
								  }
							  }
						});
				}else{
					alert("回复出错，请找相关人员解决问题！");
				}
			}else{
				alert("回复玩家的内容不能为空,或字数不能超过300个字！");
			}
		}else{
			alert("10秒内不能第二次回复！");
		}
	}
	
	function leave(gmnum,id){
		alert(gmnum+"--"+id);
		if(id>0){
			if(gmnum){
				
			}else{
				alert("编号为空，错误");
			}
		}else{
			alert("没领取问题，随意暂离。");
		}
	}
	
	function closes(gmnum,id){
		if(id>0){
			var o = {'gmnum':gmnum,
					'id':id,
					};

			$.ajax({
				  type: 'POST',
				  url: "closeAndScore_boss.jsp",
				  contentType : "application/x-www-form-urlencoded; charset=utf-8",
				  data: o,
				  dataType: "html",
// 					  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
					  success: function(result)
					  {
						  window.location.replace("?handle="+gmnum);
							alert("问题处理完毕，稍后玩家会对此次服务进行评分！");
					  }
				});
		}else{
			alert("没有问题可以关闭！");
		}
	}
	 
	function getMess(handler){
		window.location.replace("?getmess=true&handle="+handler+"&lingqu=yes");
	}
	
	function sendServerSucc(id){
		alert(id);
	}
	
	function zhuxiao(gmnum){
		window.location.replace("http://116.213.192.200:8110/game_boss/NewFeedback_gmzhuxiao.jsp?handle="+gmnum);
	}
	
	function lookPlayerMess(servername,username,playername,viplevel,qudao){
		if(username){
			if(servername=="太虚幻境"||servername=="幽冥山谷"||servername=="昆仑圣殿"||servername=="凌霄宝殿"||servername=="霸气乾坤"||servername=="烟雨青山"||servername=="仙山琼阁"||servername=="霸气无双"||servername=="华山之巅"||servername=="神龙摆尾"||servername=="柳暗花明"){
				window.open("http://117.135.128.177:8801/game_gateway/userServerMess.jsp?qudao="+qudao+"&username="+username+"&servername="+servername+"&playername="+playername+"&viplevel="+viplevel,'newwindow','width=700,height=460,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no');
			}else{
				window.open("http://116.213.192.216:8882/game_gateway/userServerMess.jsp?qudao="+qudao+"&username="+username+"&servername="+servername+"&playername="+playername+"&viplevel="+viplevel,'newwindow','width=700,height=460,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no');
			}
		}else{
			alert("没有信息可以查询！");
		}
	}
	
	function addquestion(servername,userName,playerName,vipLevel,handler){
		if(userName){
			window.open('questionAdd.jsp?servername='+servername+'&username='+userName+'&playername='+playerName+'&viplevel='+vipLevel+"&handler="+handler+"&tongji=yes",'newwindow','width=700,height=460,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no,z-look=no');
		}else{
			alert("没有问题可以扭转！");
		}
	}
	
	function doZoom(size) {
	        var zoom = document.all ? document.all['Zoom'] : document.getElementById('Zoom');
	        zoom.style.fontSize = size + 'px';
	 }
	 
	function guaqi(gmnum,id){
// 		var myDate = new Date();
// 		var systemtime = myDate.getMinutes();
// 		var tt = $("#guaqitimehid").val(); 
		//第一次
// 		if(tt==0){
// 			document.getElementById('guaqitimehid').value = systemtime;
// 		}
// 		if(systemtime<tt){
// 			document.getElementById('guaqitimehid').value = 0;
// 		}
// 		if(systemtime-tt>1&& tt>0){
// 		if(systemtime){
// 			document.getElementById('guaqitimehid').value = systemtime;
			if(gmnum&&id>0){
	 			var o = {'handle':gmnum,
	 					'fbid':id,
	 					};
	 			$.ajax({
	 				  type: 'POST',
	 				  url: "guaqi.jsp",
	 				  contentType : "application/x-www-form-urlencoded; charset=utf-8",
	 				  data: o,
	 				  dataType: "html",
// 	 					  error:function(result){alert("error:"+arguments[0]+":"+arguments[1]+":"+arguments[2])},
	 					  success: function(result)
	 					  {
	 						  alert("挂起成功");
	 						  window.location.replace("?handle="+gmnum);
	 					  }
	 				});
	 		}else{
	 			alert("没有问题可以挂起");
	 		}
// 		}else{
// 			alert("请耐心等玩家1分钟再挂起！");
// 		}
	}
	
</script>

<body bgcolor="#c8edcc" style="background-image: url('222.jpg');">
<div id='messid' align="right"></div>
	<table width="750px" height="550px" bgcolor="black">
		<%
			String handler = request.getParameter("handle");
			String startwork = request.getParameter("startwork");
			String lingqu = request.getParameter("lingqu");
			out.print("<h2>"+handler+"，欢迎您来处理问题。<div align='right'><font color='red'>字体大小:</font><a href='javascript:doZoom(10)'>小   </a><a href='javascript:doZoom(14)'>中   </a><a href='javascript:doZoom(18)'>大  </a></div></h2><hr>");
			NewFeedbackQueueManager nfq = NewFeedbackQueueManager.getInstance();
			String isgetmess = request.getParameter("getmess");
			int queussize = nfq.getQueue().size();
			int vipquesusize = nfq.getVipqueue().size();
			int handlernum = nfq.getGmNum();	
			String serverUrl = "";
			String sendtime = "";
			long feedid = 0;
			String username = "";
			String playername = "";
			String qudao = "";
			long viplev = 0;
			String servername = "";
			
			if(handler!=null && startwork!=null && startwork.equals("yes")){
				//
				NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
				SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
				String recordid = sdff.format(new Date());
				if(NewFeedbackStateManager.getInstance().isaddNewState(recordid, handler)){
					FeedbackState stat = new FeedbackState();
					stat.setStartWorkTime(System.currentTimeMillis());
					stat.setStateid(recordid);
					stat.setGmnum(handler);
					statemanager.addNewState(stat);
				}
				//
			}						
			
		%>
		<tr>
			<td colspan='11'>
				<table style='background-color:#f8f6ff;width:480px;height:320px; border:4px solid #3399cc;'>
					<tr>
						<td>
						<div id="Zoom">
							<table>
							<h3>普通在线排队用户：<%=queussize %>，VIP在线排队用户：<%=vipquesusize %></h3>
							<tr><div style='width:340px;height:210px;overflow-x:hidden;overflow-y:scroll; border:2px solid #3399cc;' id='record'>
							<%
							flag = 0;
							if(isgetmess!=null && isgetmess.trim().equals("true")){
								if(nfq.getQueue().size()>0){
									SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									NewFeedback nf = NewFeedbackQueueManager.getInstance().getGuaQiFeedback(handler);
									if(nf==null){
// 										out.print("=====123");
										nf = nfq.getQueue().remove(0);
// 										out.print("=====gmhandler:"+nf.getGmHandler());
									}
									
									if(nf!=null){
										out.print("handler:"+handler+"--nf:"+nf.getGmHandler());
										NewFeedbackQueueManager.getInstance().recordGmWorkNum(handler);
										nf.setIsnewFeedback(1);
										NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(nf, "isnewFeedback");
										if(lingqu!=null && lingqu.equals("yes")){
											//
											NewFeedbackStateManager statemanager = NewFeedbackStateManager.getInstance();
											SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
											String recordid = sdf.format(new Date());
											if(handler!=null && NewFeedbackStateManager.getInstance().isaddNewState(recordid, handler)){
												FeedbackState stat = new FeedbackState();
												stat.setGetFeedbackTime(1);
												stat.setFbid(nf.getId());
												stat.setPlayername(nf.getPlayername());
												stat.setPlayerSendTime(format1.parse(nf.getSendtime()).getTime());
												stat.setServername(nf.getServername());
												stat.setTitle(nf.getTitle());
												stat.setUsername(nf.getUsername());
												stat.setViplevel((int)nf.getViplevel());
												stat.setStateid(recordid);
												stat.setGmnum(handler);
												NewFeedbackQueueManager.getInstance().getLingquCache().add(nf.getId());
												statemanager.addNewState(stat);
											}else{
												if(!NewFeedbackQueueManager.getInstance().getLingquCache().contains(nf.getId())){
													List<FeedbackState> states = statemanager.getStates();
													for(FeedbackState pp:states){
														if(recordid.equals(pp.getStateid()) && handler.equals(pp.getGmnum())){
															pp.setGetFeedbackTime(pp.getGetFeedbackTime()+1);
															NewFeedbackQueueManager.getInstance().getLingquCache().add(nf.getId());
														}
													}
												}
											}
											//
										}
										
										//
										nf.setIsHandleNow(1);
										nf.setGmHandler(handler);
										out.print("<font color='red'>正在加载....</font>");
										NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(nf, "gmHandler");
										NewFeedbackQueueManager.getInstance().sem.notifyFieldChange(nf, "isHandleNow");
										XmlServerManager smanager = XmlServerManager.getInstance();
										List<XmlServer> totalservers = smanager.getServers();
										for(XmlServer ss:totalservers){
											if(ss.getDescription().equals(nf.getServername())){
												serverUrl = ss.getUri();
											}
										}
										flag = nf.getId();
										feedid = nf.getId();
										username= nf.getUsername();
										playername = nf.getPlayername();
										viplev = nf.getViplevel();
										servername = nf.getServername();
										if(username!=null){
											try{
												Passport p = PassportManager.getInstance().getPassport(username);
												qudao = p.getRegisterChannel();
											}catch(Exception e){
												e.printStackTrace();
												out.print("获得通行证出错，可能是QQ的，想查看渠道，请自行去后台查看");
											}
											
										}
										NewFeedbackQueueManager.getInstance().removeQueueIndex(nf);
									%>
									</div></tr>
									<tr><textarea id='talkmess' style='width:340px;height:60px;'></textarea></tr>
									<tr><input type='button' id='click' name='click' onclick='send("<%=nf.getId() %>","<%=nf.getServername() %>","<%=nf.getPlayername() %>","<%=handler %>","<%=serverUrl %>","<%=sendtime %>")' value='回复'/></tr>
									
									<%	
									}else{
										out.print("<font size='6' color='red'><B>领取问题出错</B></font>");
									}
								}else{
									out.print("<font size='6' color='red'><B>没有问题</B></font>");
								}
								
							}else{
								%>
									<a onclick="getMess('<%=handler%>')"><font size="6" color='red'><B>领取问题</B></font></a>
								<%
							}
							%>
							
							</table>
						</td>
						<td width='130px' valign='top'><input type="hidden" id="updateid" name="updateid" value="<%=flag%>"/><input type="hidden" id="updateid2" name="updateid2" value="<%=handler%>"/><input type="hidden" id="senttimehid" name="senttimehid" value="0"/><input type="hidden" id="guaqitimehid" name="guaqitimehid" value="0"/>
						<table style='background-color:#f8f6ff;width:130px;height:320px; border:0px solid #3399cc;'>
							<caption><font size='1px' color='red'>鼠标停留在按钮上，有简单的功能描述</font></caption>
							<tr>
								<td width='130px'><input type='button' id='qiangzhi' onclick="leave('<%=handler%>','<%=feedid%>')" value='暂离' title='上厕所，吃饭，点暂离，并说明暂离原因'/><br></td>
							</tr>
							<tr>
								<td width='130px'><input type='button' id='qiangzhi' onclick='lookPlayerMess("<%=servername%>","<%=username%>","<%=playername%>","<%=viplev%>","<%=qudao%>")' value='查询' title='当想查看更多的玩家信息，点查询'/><br></td>
							</tr>
							<tr>
								<td width='130px'><input type='button' id='qiangzhi' onclick='addquestion("<%=servername%>","<%=username%>","<%=playername%>","<%=viplev%>","<%=handler%>")' value='反馈' title='当这个问题处理不了的时候，点反馈，并告知玩家处理的工作日'/><br></td>
							</tr>
							<tr>
								<td width='130px'><input type='button' id='qiangzhi' onclick="guaqi('<%=handler%>','<%=feedid%>')" value='挂起' title='当您回复玩家后，2分钟之内没有新的回复时，点挂起'/><br></td>
							</tr>
							<tr>
								<td width='130px' bgcolor="#F5DE33"><input type='button' id='qiangzhi' onclick='closes("<%=handler%>","<%=feedid%>")' value='关闭' title='当一个问题完全处理完毕时，点关闭，玩家此时会对这次服务评分'/><br></td>
							</tr>
							<tr>
								<td width='130px'><input type='button' id='qiangzhi' onclick="zhuxiao('<%=handler%>')" value='注销' title="当您一天工作结束时，点注销"/><br></td>
							</tr>
							<tr>
								<td><select>
								<option>运行问题</option>
								<option>玩家建议</option>
								<option>数据异常</option>
								<option>充值问题</option>
								<option>帐号问题</option>
								<option>丢失问题</option>
								<option>游戏咨询</option>
								<option>其他问题</option>
								</select></td>
							</tr>
						</table>
						
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
</body>
</html>

