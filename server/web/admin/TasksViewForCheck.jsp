<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.task.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.monster.*,com.fy.engineserver.sprite.npc.*,java.lang.reflect.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>任务</title>
<% TaskManager tm = TaskManager.getInstance();
String[] dependencys = null;
if(tm != null){
	dependencys = tm.getAllDependencys();
}
String dependency = request.getParameter("dependency");
if(dependency == null){
	dependency = "全部";
}
%>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<style type="text/css">
.tablestyle1{
width:100%;
border:0px solid #69c;
border-top:1px solid #69c;
border-right:1px solid #69c;
border-bottom:1px solid #69c;
border-left:1px solid #69c;
border-collapse: collapse;
}
.tablestyle2{
width:100%;
border:0px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
}
.tdtable{
 padding: 0px;
 border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.width{
width:100px;
}
.align{
text-align: left;
}
.borderbottom{
border-bottom:1px dotted #69c;
}
.lefttd{
border-left:0px solid #69c;
}
.toptd{
border-top:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.bottomtd{
border-bottom:0px solid #69c;
}
</style>
<script type="text/javascript">
function selectdependencys(){
	var selected = document.getElementById("selectdependedcy");
	window.location.href="TasksViewForCheck.jsp?dependency="+selected.value;
}
</script>
</head>
<body>
<a href="TasksView.jsp">刷新此页面</a>
<br>
<br>
<input type="hidden" id="dependency">
<select id="selectdependedcy" onchange="javascript:selectdependencys();">
<option value="全部" <%=(dependency != null? (dependency.equals("全部") ? "selected":""):"") %>><%="全部" %>
<%if(dependencys != null){ 
	for(int i = 0; i < dependencys.length; i++){
		String depStr = dependencys[i];
		if(depStr != null){
			%>
			<option value="<%=depStr %>" <%=(dependency != null? (dependency.equals(depStr) ? "selected":""):"") %>><%=depStr %>
			<%
		}
	}
}
%>
</select>
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>
<%if(dependencys != null){

if(dependency != null){
	Task[] tasks = null;
	if(tm != null){
		if("全部".equals(dependency)){
			tasks = tm.getAllTasks();
		}else{
			tasks = tm.getAllTasksOnDependency(dependency);
		}
	}
	if(tasks != null){
			ArticleManager am = ArticleManager.getInstance();
			MonsterManager mmm = MemoryMonsterManager.getMonsterManager();
			GameManager gm = GameManager.getInstance();
%>
<font color="red"><b><%=dependency %></b></font>中的所有任务<br><br>
<table class="tablestyle1">
<tr align="center">
	<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">ID</td>
	<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">任务名</td>
	<td width="160" align="center" style="word-wrap: break-word;">任务等级</td>
	<td width="160" align="center" style="word-wrap: break-word;">可接等级</td>
	<td width="160" align="center" style="word-wrap: break-word;">归属地</td>
	<td width="160" align="center" style="word-wrap: break-word;">是否帮派任务</td>
	<td width="160" align="center" style="word-wrap: break-word;">任务类型</td>
	<td width="160" align="center" style="word-wrap: break-word;">开始NPC</td>
	<td width="160" align="center" style="word-wrap: break-word;">结束NPC</td>
	<td width="160" align="center" style="word-wrap: break-word;">开始地图</td>
	<td width="160" align="center" style="word-wrap: break-word;">结束地图</td>
	<td width="160" align="center" style="word-wrap: break-word;">声望</td>
	<td width="160" align="center" style="word-wrap: break-word;">领取时间</td>
	<td width="160" align="center" style="word-wrap: break-word;">传递物品</td>
	<td width="160" align="center" style="word-wrap: break-word;">奖励物品</td>
	<td width="160" align="center" style="word-wrap: break-word;">目标</td>
</tr>
<%
		int alSize = tasks.length;
int count = 0;
		for(int jj = 0; jj < alSize; jj++){
  			%>
  			<tr align="center">
  			<%Task task = tasks[jj];
  			if(task.getName().indexOf("(旧)") >= 0 || task.getName().indexOf("（旧）") >= 0 || (task.getDependency() != null && (task.getDependency().indexOf("(旧)") >= 0 || task.getDependency().indexOf("（旧）") >= 0))){count++ ;continue;};
  			%>
  			<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<a href="TaskByTaskId.jsp?taskId=<%=task.getId() %>" ><%=task.getId() %></a>
  				<%} %>
  				</td>
  				<td class="lefttd" width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<a href="TaskByTaskId.jsp?taskId=<%=task.getId() %>" ><%=task.getName() %></a>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ %>
  				<%=task.getTaskLevel() %>
  				<%} %>
  				</td>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ 
  				if((task.getTaskLevel() - task.getMinPlayerLevel()) > 3 || (task.getTaskLevel() - task.getMinPlayerLevel()) < -3){
  					out.println("<h1><font color='red'>"+task.getMinPlayerLevel()+"(与任务等级相差大于3)</font></h1>");
  				}else{
  					out.println(task.getMinPlayerLevel());
  				}
  				%>
  				
  				<%} %>
  				</td>
  				<td><%
  				if(task != null){ %>
  				<%=task.getDependency()%>
  				<%} %></td>
  				<td><%
  				if(task != null){ %>
  				<%=(task.isIsGangTask() ? "是":"不是")%>
  				<%} %></td>
				<td width="160" align="center" style="word-wrap: break-word;">
  				<%
  				if(task != null){ 
  					String taskTypeStr = "";
  					if(task.getTaskType()==Task.TYPE_ONCE){ 
  						taskTypeStr = "一次性任务";
  					}else if(task.getTaskType()==Task.TYPE_DAILY){ 
  						taskTypeStr = "日常任务";
  					}else if(task.getTaskType()==Task.TYPE_LOOP){ 
  						taskTypeStr = "<h1><font color='red'>循环任务</font></h1>";
  					}else if(task.getTaskType()==Task.TYPE_RUNNING){
  						taskTypeStr = "跑环任务";
  					}
  				%>
  				<%=taskTypeStr %>
  				<%} %>
  				</td>
  				<td>
  				<%
  				NPCManager npcm = MemoryNPCManager.getNPCManager();
  				MemoryNPCManager.NPCTempalte sts[] = ((MemoryNPCManager)npcm).getNPCTemaplates();
  				%>
  				<%if(task != null){
  					String camp = "<h1><font color='red'>没有阵营设置</font></h1>";
  					String npcName = task.getStartNPC();
  					if(sts != null){
  						for(MemoryNPCManager.NPCTempalte npcT : sts){
  							if(npcT != null){
  								NPC npc = npcm.createNPC(npcT.NPCCategoryId);
  								if(npc != null){
  									if(npc.getName().equals(npcName)){
  	  									if(npc.getPoliticalCamp() != 0){
  	  	  									if(npc.getPoliticalCamp() != task.getCampLimit()){
  	  	  										camp = "<h1><font color='red'>阵营错误(NPC "+npc.getPoliticalCamp()+")(Task "+task.getCampLimit()+")</font></h1>";
  	  	  									}else{
  	  	  										camp = "";
  	  	  									}
  	  	  								}else{
  	  	  									camp = "";
  	  	  								}
  	  									break;
  	  								}
  	  								npcm.removeNPC(npc);
  								}
  							}
  						}
  					}
  					boolean has = false;
  					if(npcName != null && !npcName.equals("")){
  						String mapName = task.getStartMapName();
  						if(mapName == null || "".equals(mapName)){
  							camp +="<h1><font color='red'>没有设置开始地图</font></h1>";
  						}else{
  							Game game = GameManager.getInstance().getGameByName(mapName);
  							if(game != null){
  								LivingObject[] los = game.getLivingObjects();
  								if(los != null){
  									for(LivingObject lo : los){
  										if(lo instanceof NPC){
  											if(npcName.equals(((NPC)lo).getName())){
  												if(((NPC)lo).getX() == task.getStartX() && ((NPC)lo).getY() == task.getStartY()){
  													
  												}else{
  													camp +="<h1><font color='red'>NPC位置错误("+task.getStartX()+","+task.getStartY()+")("+((NPC)lo).getX()+","+((NPC)lo).getY()+")</font></h1>";
  												}
  												has = true;
  												break;
  											}
  										}
  									}
  								}
  			  					if(!has){
  			  						camp +="<h1><font color='red'>地图上没有NPC</font></h1>";
  			  					}
  							}else{
  								camp +="<h1><font color='red'>没有这张开始地图</font></h1>";
  							}
  						}
  					}
  				%>
  				<%=("".equals(task.getStartNPC())? "" :task.getStartNPC()+camp)%>
  				<%} %>
  				</td>
  				<td>
  				<%if(task != null){
  					String camp = "<h1><font color='red'>没有阵营设置</font></h1>";
  					String npcName = task.getEndNPC();
  					if(sts != null){
  						for(MemoryNPCManager.NPCTempalte npcT : sts){
  							if(npcT != null){
  								NPC npc = npcm.createNPC(npcT.NPCCategoryId);
  								if(npc.getName().equals(npcName)){
  									if(npc.getPoliticalCamp() != 0){
  	  									if(npc.getPoliticalCamp() != task.getCampLimit()){
  	  										camp = "<h1><font color='red'>阵营错误(NPC "+npc.getPoliticalCamp()+")(Task "+task.getCampLimit()+")</font></h1>";
  	  									}else{
  	  										camp = "";
  	  									}
  	  								}else{
  	  									camp = "";
  	  								}
  									break;
  								}
  								npcm.removeNPC(npc);
  							}
  						}
  					}
  					boolean has = false;
  					if(npcName != null && !npcName.equals("")){
  						String mapName = task.getEndMapName();
  						if(mapName == null || "".equals(mapName)){
  							camp +="<h1><font color='red'>没有设置结束地图</font></h1>";
  						}else{
  							Game game = GameManager.getInstance().getGameByName(mapName);
  							if(game != null){
  								LivingObject[] los = game.getLivingObjects();
  								if(los != null){
  									for(LivingObject lo : los){
  										if(lo instanceof NPC){
  											if(npcName.equals(((NPC)lo).getName())){
												if(((NPC)lo).getX() == task.getEndX() && ((NPC)lo).getY() == task.getEndY()){
  													
  												}else{
  													camp +="<h1><font color='red'>NPC位置错误("+task.getEndX()+","+task.getEndY()+")("+((NPC)lo).getX()+","+((NPC)lo).getY()+")</font></h1>";
  												}
  												has = true;
  												break;
  											}
  										}
  									}
  								}
  			  					if(!has){
  			  						camp +="<h1><font color='red'>地图上没有NPC</font></h1>";
  			  					}
  							}else{
  								camp +="<h1><font color='red'>没有这张结束地图</font></h1>";
  							}
  						}
  					}

  				%>
  				<%=("".equals(task.getEndNPC())? "" :task.getEndNPC()+camp)%>
  				<%} %>
  				</td>
				<td><%
  				if(task != null){ %>
  				<%=task.getStartMapName()%>
  				<%} %></td>
				<td>
				<%
  				if(task != null){ %>
  				<%=task.getEndMapName()%>
  				<%} %></td>
  				<td>
				<%
  				if(task != null){
  					StringBuffer sbb = new StringBuffer();
  					String warn = "";
  					if(task.getRewardPrestigeNames() != null){ 
  					for(int i = 0; i < task.getRewardPrestigeNames().length; i++){
  						String str = task.getRewardPrestigeNames()[i];
  						if(str != null){
  							if(task.getCampLimit() == Task.CAMP_LIMIT_NONE && (str.indexOf("日月盟") >= 0 || str.indexOf("紫微宫") >= 0)){
  								warn = "<h1><font color='red'>任务阵营为中立，奖励声望为"+str+"</font></h1>";
  							}
  							if(task.getCampLimit() == Task.CAMP_LIMIT_RIYUEMENG && str.indexOf("紫微宫") >= 0){
  								warn = "<h1><font color='red'>任务阵营为日月盟，奖励声望为"+str+"</font></h1>";
  							}
  							if(task.getCampLimit() == Task.CAMP_LIMIT_ZIWEIGONG && str.indexOf("日月盟") >= 0){
  								warn = "<h1><font color='red'>任务阵营为紫微宫，奖励声望为"+str+"</font></h1>";
  							}
  							sbb.append(str+"("+task.getRewardPrestigeValues()[i]+")");
  							sbb.append(" ");
  						}
  					}
  					}
  					%>
  					<%=sbb.toString() + warn%>
  					<%
  				} %></td>
  				<td>
  				<% 
			TimeSlice ts = task.getValidTimeSlice();
			if(ts == null){
				out.println("无限制");
			}else{
				if(ts instanceof DailyTimeSlice){
					out.println("每天的"+((DailyTimeSlice)ts).getStartHour()+"点到"+((DailyTimeSlice)ts).getEndHour()+"点");
				}else if(ts instanceof WeeklyTimeSlice){

					boolean[] weekBoolean = ((WeeklyTimeSlice)ts).getWeeklyValid();
					StringBuffer sb = new StringBuffer();
					for(int i = 0; i < weekBoolean.length; i++){
						switch(i){
						case 0:
							if(weekBoolean[i]){
								sb.append("星期日 ");
							}
							break;
						case 1:
							if(weekBoolean[i]){
								sb.append("星期一 ");
							}
							break;
						case 2:
							if(weekBoolean[i]){
								sb.append("星期二 ");
							}
							break;
						case 3:
							if(weekBoolean[i]){
								sb.append("星期三 ");
							}
							break;
						case 4:
							if(weekBoolean[i]){
								sb.append("星期四 ");
							}
							break;
						case 5:
							if(weekBoolean[i]){
								sb.append("星期五 ");
							}
							break;
						case 6:
							if(weekBoolean[i]){
								sb.append("星期六 ");
							}
							break;
						}
						
					}
					if(sb.length() == 0){
						out.println("<h1><font color='red'>没有可接日期</font></h1>");
					}else{
						out.println(sb.toString());
					}
				}
			}
			%>
  				</td>
  				<td>
				<%
  				if(task != null){
  					StringBuffer sb = new StringBuffer();
  					String articleName = task.getTransitiveArticleName();
  					if(articleName == null){
  					}else{
						if(am.getArticle(articleName) == null){
							sb.append("&nbsp;<h1><font color='red'>"+articleName+"(服务器上没有)</font></h1>&nbsp;");
						}else{
							sb.append("&nbsp;<a href='ArticleByName.jsp?articleName="+articleName+"'>"+articleName+"</a>&nbsp;"+(am.getArticle(articleName).isSailFlag() ? "":"<h1><font color='red'>此物品可以卖</font></h1>"));
						}
						out.println(sb.toString());
	  				}
  				} %></td>
  				<td>
				<%
  				if(task != null){
  					StringBuffer sb = new StringBuffer();
  					String[] articleNames = task.getRewardArticleNames();
  					String warn = "";
  					if(articleNames != null){
  						for(String articleName : articleNames){
  							if(am.getArticle(articleName) == null){
  								sb.append("&nbsp;<h1><font color='red'>"+articleName+"</font></h1>&nbsp;");
  							}else{
  								if(am.getArticle(articleName) instanceof Weapon){
  									if(articleNames.length < 5){
  										warn = "<h1><font color='red'>武器少于五件</font></h1>";
  									}
  								}else if(am.getArticle(articleName) instanceof Equipment){
  									if(articleNames.length < 3){
  										warn = "<h1><font color='red'>装备少于三件</font></h1>";
  									}
  								}
  								sb.append("&nbsp;<a href='ArticleByName.jsp?articleName="+articleName+"'>"+articleName+"</a>&nbsp;");
  							}
  						}
  					}
  					out.println(sb.toString()+warn);
  				} %></td>
				<td>
				<%
				if(task != null){
					if(task.getGoals() != null){
						%><table><%
						for(int i = 0; i < task.getGoals().length; i++){
							TaskGoal tg = task.getGoals()[i];
							if(tg != null){
					if(i != task.getGoals().length - 1){
						%>
									<tr>
					<td class="tdcolor1 td">任务目标<%=(i+1) %></td>
					<td class="tdcolor2 td righttd tdtable">
					<table class="tablestyle1">
					<tr>
					<td class="tdcolor1 td">目标类型</td>
					<td class="tdcolor1 td">目标的名字</td>
					<td class="tdcolor1 td">数量</td>
					<td class="tdcolor1 td">地理位置</td>
					<td class="tdcolor1 td">坐标</td>
					<td class="tdcolor1 td">任务目标的描述</td>
					<td class="tdcolor1 td righttd">和NPC对话的内容</td>
					</tr>
					<%
					if(tg.getGoalType() != TaskGoal.GOALTYPE_QUESTION){
						if(tg.getGoalType() == TaskGoal.GOALTYPE_KILL_SPRITE){
							%>
							<tr>
							<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType())%></td>
							<td class="tdcolor2 td bottomtd">
							<%if(tg.getGoalName() != null){
								boolean exist = false;
								String monsterLevel = "";
								MemoryMonsterManager.MonsterTempalte[] mts =  ((MemoryMonsterManager)mmm).getMonsterTemaplates();
								if(mts != null){
									
									for(MemoryMonsterManager.MonsterTempalte mt : mts){
										if(mt != null){
											Monster ms = mt.monster;
											if(ms != null){
												if(tg.getGoalName().equals(ms.getName())){
													exist = true;
													if((ms.getLevel() - task.getTaskLevel()) > 2 || (ms.getLevel() - task.getTaskLevel()) < -2){
														monsterLevel = "(等级"+ms.getLevel()+")<h1><font color='red'>与任务等级相差大于2</font></h1>";
													}
													break;
												}
											}
										}
									}
								}
								if(exist){
									out.println(tg.getGoalName()+monsterLevel);
								}else{
									out.println("<h1><font color='red'>设置的怪物不存在</font></h1>");
								}
							}else{
								out.println("<h1><font color='red'>没有设置怪物</font></h1>");
							} %>
							</td>
							<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
							<td class="tdcolor2 td bottomtd">
							<%
							String bornPoint = "<h1><font color='red'>目标点附近(320像素)没有这种怪物</font></h1>";
							if(tg.getMapName() == null){
								out.println("<h1><font color='red'>地图名字为空</font></h1>");
							}else{
								Game game = gm.getGameByName(tg.getMapName());
								if(game == null){
									out.println("<h1><font color='red'>"+tg.getMapName()+"(这个地图名字的game为空)</font></h1>");
								}else{
									out.println(tg.getMapName());
									int monsterCategoryId = -1;
									boolean exist = false;
									MemoryMonsterManager.MonsterTempalte[] mts =  ((MemoryMonsterManager)mmm).getMonsterTemaplates();
									if(mts != null){
										
										for(MemoryMonsterManager.MonsterTempalte mt : mts){
											if(mt != null){
												Monster ms = mt.monster;
												if(ms != null){
													if(tg.getGoalName() != null && tg.getGoalName().equals(ms.getName())){
														exist = true;
														monsterCategoryId = ms.getSpriteCategoryId();
														break;
													}
												}
											}
										}
									}
									if(exist){
										boolean notFar = false;
										MonsterFlushAgent.BornPoint[] bps = game.getSpriteFlushAgent().getBornPoints4SpriteCategoryId(monsterCategoryId);
										if(bps != null){
											for(MonsterFlushAgent.BornPoint bp : bps){
												if(bp != null){
													if((tg.getX()- bp.getX()) < 320 && (tg.getX()- bp.getX()) > -320 && (tg.getY()- bp.getY()) < 320 && (tg.getY()- bp.getY()) > -320){
														notFar = true;
														break;
													}
												}
											}
										}
										if(notFar){
											bornPoint = "";
										}
									}else{
									}
								}
							} %>
							</td>
							<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")"+bornPoint %></td>
							<td class="tdcolor2 td bottomtd tdtable">
							<table class="tablestyle1">
							<tr>
							<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
							<td class="tdcolor2 td bottomtd righttd tdtable">
							<table class="tablestyle1">
							<tr>
							<td class="tdcolor1 td">怪名</td>
							<td class="tdcolor1 td righttd">掉落几率</td>
							</tr>
							<%
							String monsters[] = tg.getMonsterNames();
							int[] flopRates = tg.getFlopRate();
							if(monsters != null && monsters.length != 0){
								%>
								
								<%
							}
							%>
							
							<%
							
							if(monsters != null && flopRates != null){
								for(int j = 0; j < monsters.length && j < flopRates.length; j++){
									if(j == (monsters.length -1)){
										%>
										<tr>
							<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
							<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
							</tr>
										<%
									}
							%>
							
							
							<%} }%>
							
							</table>
							</td>
							</tr>
							</table>
							
							
							</td>
							<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
							</tr>
							<%
							
						}else if(tg.getGoalType() == TaskGoal.GOALTYPE_NEED_ARTICLE || tg.getGoalType() == TaskGoal.GOALTYPE_USE_PROPS){

							%>
							<tr>
							<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
							<td class="tdcolor2 td bottomtd">
							<%if(tg.getGoalName() != null){
								if(am.getArticle(tg.getGoalName()) == null){
									out.println("<h1><font color='red'>设置的物品不存在</font></h1>");
								}else{
									out.println("<a href='ArticleByName.jsp?articleName="+tg.getGoalName()+"'>"+tg.getGoalName()+"</a>");
								}
							}else{
								out.println("<h1><font color='red'>没有设置物品</font></h1>");
							} %>
							</td>
							<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
							<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
							<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
							<td class="tdcolor2 td bottomtd tdtable">
							<table class="tablestyle1">
							<tr>
							<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
							<td class="tdcolor2 td bottomtd righttd tdtable">
							<table class="tablestyle1">
							<tr>
							<td class="tdcolor1 td">怪名</td>
							<td class="tdcolor1 td righttd">掉落几率</td>
							</tr>
							<%
							String monsters[] = tg.getMonsterNames();
							int[] flopRates = tg.getFlopRate();
							if(monsters != null && monsters.length != 0){
								%>
								
								<%
							}
							%>
							
							<%
							
							if(monsters != null && flopRates != null){
								for(int j = 0; j < monsters.length && j < flopRates.length; j++){
									if(j == (monsters.length -1)){
										%>
										<tr>
							<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
							<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
							</tr>
										<%
									}
							%>
							
							
							<%} }%>
							
							</table>
							</td>
							</tr>
							</table>
							
							
							</td>
							<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
							</tr>
							<%
							
						
						}else{
							String camp = "";
							if("探索".equals(tg.goalTypeToString(tg.getGoalType()))){
								Game game = gm.getGameByName(tg.getMapName());
								if(game == null){
									camp += "<h1><font color='red'>地图不存在</font></h1>";
								}else{
									if(game.getGameInfo().getMapAreaByName(tg.getGoalName()) == null){
										camp += "<h1><font color='red'>没有此区域</font></h1>";
									}else{
										if(game.getGameInfo().getMapAreaByPoint(tg.getX(),tg.getY()) == null){
											camp += "<h1><font color='red'>设置的坐标点找不到区域</font></h1>";
										}else{
											if(!tg.getGoalName().equals(game.getGameInfo().getMapAreaByPoint(tg.getX(),tg.getY()).getName())){
												camp += "<h1><font color='red'>设置的坐标点不在区域中</font></h1>";
											}
										}
									}
								}
							}
					%>
					<tr>
					<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) + camp%></td>
					<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
					<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
					<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
					<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
					<td class="tdcolor2 td bottomtd tdtable">
					<table class="tablestyle1">
					<tr>
					<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
					<td class="tdcolor2 td bottomtd righttd tdtable">
					<table class="tablestyle1">
					<tr>
					<td class="tdcolor1 td">怪名</td>
					<td class="tdcolor1 td righttd">掉落几率</td>
					</tr>
					<%
					String monsters[] = tg.getMonsterNames();
					int[] flopRates = tg.getFlopRate();
					if(monsters != null && monsters.length != 0){
						%>
						
						<%
					}
					%>
					
					<%
					
					if(monsters != null && flopRates != null){
						for(int j = 0; j < monsters.length && j < flopRates.length; j++){
							if(j == (monsters.length -1)){
								%>
								<tr>
					<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
					<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
					</tr>
								<%
							}
					%>
					
					
					<%} }%>
					
					</table>
					</td>
					</tr>
					</table>
					
					
					</td>
					<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
					</tr>
					<%}}else{
						%>
						<tr>
						<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
						<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
						<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
						<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
						<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
						<td class="tdcolor2 td bottomtd tdtable">
						<table class="tablestyle1">
						<tr>
						<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
						<td class="tdcolor2 td bottomtd righttd tdtable">
						<%String questionTitles[] = tg.getMonsterNames();
						StringBuffer qsb = new StringBuffer();
						if(questionTitles != null){
							for(int m = 0; m < questionTitles.length; m++){
								String questionTitle = questionTitles[m];
								if(questionTitle != null){
									if(qsb.length() != 0){
										qsb.append("\n");
									}
									qsb.append("题目"+(m+1)+":<a href='question.jsp?titleName="+java.net.URLEncoder.encode(questionTitle)+"'>"+questionTitle+"</a>");
								}
							}
						}
						%>
						</td>
						</tr>
						</table>
						
						
						</td>
						<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
						</tr>
						<%
						
					} %>
					
					</table>
					</td>
					</tr>
						<%
					}else{
								%>
								
								<tr>
					<td class="tdcolor1 td bottomtd">任务目标<%=(i+1) %></td>
					<td class="tdcolor2 td bottomtd righttd tdtable">
					<table class="tablestyle1">
					<tr>
					<td class="tdcolor1 td">目标类型</td>
					<td class="tdcolor1 td">目标的名字</td>
					<td class="tdcolor1 td">数量</td>
					<td class="tdcolor1 td">地理位置</td>
					<td class="tdcolor1 td">坐标</td>
					<td class="tdcolor1 td">任务目标的描述</td>
					<td class="tdcolor1 td righttd">和NPC对话的内容</td>
					</tr>
					<%
					if(tg.getGoalType() != TaskGoal.GOALTYPE_QUESTION){
						if(tg.getGoalType() == TaskGoal.GOALTYPE_KILL_SPRITE){
						%>
						<tr>
						<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
						<td class="tdcolor2 td bottomtd">
						<%if(tg.getGoalName() != null){
							boolean exist = false;
							String monsterLevel = "";
							MemoryMonsterManager.MonsterTempalte[] mts =  ((MemoryMonsterManager)mmm).getMonsterTemaplates();
							if(mts != null){
								
								for(MemoryMonsterManager.MonsterTempalte mt : mts){
									if(mt != null){
										Monster ms = mt.monster;
										if(ms != null){
											if(tg.getGoalName().equals(ms.getName())){
												exist = true;
												if((ms.getLevel() - task.getTaskLevel()) > 2 || (ms.getLevel() - task.getTaskLevel()) < -2){
													monsterLevel = "(等级"+ms.getLevel()+")<h1><font color='red'>与任务等级相差大于2</font></h1>";
												}
												break;
											}
										}
									}
								}
							}
							if(exist){
								out.println(tg.getGoalName()+monsterLevel);
							}else{
								out.println("<h1><font color='red'>设置的怪物不存在</font></h1>");
							}
						}else{
							out.println("<h1><font color='red'>没有设置怪物</font></h1>");
						} %>
						</td>
						<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
						<td>
						<%
							String bornPoint = "<h1><font color='red'>目标点附近(320像素)没有这种怪物</font></h1>";
							if(tg.getMapName() == null){
								out.println("<h1><font color='red'>地图名字为空</font></h1>");
							}else{
								Game game = gm.getGameByName(tg.getMapName());
								if(game == null){
									out.println("<h1><font color='red'>"+tg.getMapName()+"(这个地图名字的game为空)</font></h1>");
								}else{
									out.println(tg.getMapName());
									int monsterCategoryId = -1;
									boolean exist = false;
									MemoryMonsterManager.MonsterTempalte[] mts =  ((MemoryMonsterManager)mmm).getMonsterTemaplates();
									if(mts != null){
										
										for(MemoryMonsterManager.MonsterTempalte mt : mts){
											if(mt != null){
												Monster ms = mt.monster;
												if(ms != null){
													if(tg.getGoalName() != null && tg.getGoalName().equals(ms.getName())){
														exist = true;
														monsterCategoryId = ms.getSpriteCategoryId();
														break;
													}
												}
											}
										}
									}
									if(exist){
										boolean notFar = false;
										MonsterFlushAgent.BornPoint[] bps = game.getSpriteFlushAgent().getBornPoints4SpriteCategoryId(monsterCategoryId);
										if(bps != null){
											for(MonsterFlushAgent.BornPoint bp : bps){
												if(bp != null){
													if((tg.getX()- bp.getX()) < 320 && (tg.getX()- bp.getX()) > -320 && (tg.getY()- bp.getY()) < 320 && (tg.getY()- bp.getY()) > -320){
														notFar = true;
														break;
													}
												}
											}
										}
										if(notFar){
											bornPoint = "";
										}
									}else{
									}
								}
							} %>
							</td>
							<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")"+bornPoint %></td>
						<td class="tdcolor2 td bottomtd tdtable">
						<table class="tablestyle1">
						<tr>
						<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
						<td class="tdcolor2 td bottomtd righttd tdtable">
						<table class="tablestyle1">
						<tr>
						<td class="tdcolor1 td">怪名</td>
						<td class="tdcolor1 td righttd">掉落几率</td>
						</tr>
						<%
						String monsters[] = tg.getMonsterNames();
						int[] flopRates = tg.getFlopRate();
						if(monsters != null && monsters.length != 0){
							%>
							
							<%
						}
						%>
						
						<%
						
						if(monsters != null && flopRates != null){
							for(int j = 0; j < monsters.length && j < flopRates.length; j++){
								if(j == (monsters.length -1)){
									%>
									<tr>
						<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
						<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
						</tr>
									<%
								}
						%>
						
						
						<%} }%>
						
						</table>
						</td>
						</tr>
						</table>
						
						
						</td>
						<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
						</tr>
						<%
						
					}else if(tg.getGoalType() == TaskGoal.GOALTYPE_NEED_ARTICLE || tg.getGoalType() == TaskGoal.GOALTYPE_USE_PROPS){

						%>
						<tr>
						<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
						<td class="tdcolor2 td bottomtd">
						<%if(tg.getGoalName() != null){
							if(am.getArticle(tg.getGoalName()) == null){
								out.println("<h1><font color='red'>设置的物品不存在</font></h1>");
							}else{
								out.println("<a href='ArticleByName.jsp?articleName="+tg.getGoalName()+"'>"+tg.getGoalName()+"</a>");
							}
						}else{
							out.println("<h1><font color='red'>没有设置物品</font></h1>");
						} %>
						</td>
						<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
						<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
						<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
						<td class="tdcolor2 td bottomtd tdtable">
						<table class="tablestyle1">
						<tr>
						<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
						<td class="tdcolor2 td bottomtd righttd tdtable">
						<table class="tablestyle1">
						<tr>
						<td class="tdcolor1 td">怪名</td>
						<td class="tdcolor1 td righttd">掉落几率</td>
						</tr>
						<%
						String monsters[] = tg.getMonsterNames();
						int[] flopRates = tg.getFlopRate();
						if(monsters != null && monsters.length != 0){
							%>
							
							<%
						}
						%>
						
						<%
						
						if(monsters != null && flopRates != null){
							for(int j = 0; j < monsters.length && j < flopRates.length; j++){
								if(j == (monsters.length -1)){
									%>
									<tr>
						<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
						<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
						</tr>
									<%
								}
						%>
						
						
						<%} }%>
						
						</table>
						</td>
						</tr>
						</table>
						
						
						</td>
						<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
						</tr>
						<%
						
					
					}else{
						String camp = "";
						if("探索".equals(tg.goalTypeToString(tg.getGoalType()))){
							Game game = gm.getGameByName(tg.getMapName());
							if(game == null){
								camp += "<h1><font color='red'>地图不存在</font></h1>";
							}else{
								if(game.getGameInfo().getMapAreaByName(tg.getGoalName()) == null){
									camp += "<h1><font color='red'>没有此区域</font></h1>";
								}else{
									if(game.getGameInfo().getMapAreaByPoint(tg.getX(),tg.getY()) == null){
										camp += "<h1><font color='red'>设置的坐标点找不到区域</font></h1>";
									}else{
										if(!tg.getGoalName().equals(game.getGameInfo().getMapAreaByPoint(tg.getX(),tg.getY()).getName())){
											camp += "<h1><font color='red'>设置的坐标点不在区域中</font></h1>";
										}
									}
								}
							}
						}
				%>
				<tr>
				<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) + camp%></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
				<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
				<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
				<td class="tdcolor2 td bottomtd tdtable">
				<table class="tablestyle1">
				<tr>
				<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
				<td class="tdcolor2 td bottomtd righttd tdtable">
				<table class="tablestyle1">
				<tr>
				<td class="tdcolor1 td">怪名</td>
				<td class="tdcolor1 td righttd">掉落几率</td>
				</tr>
				<%
				String monsters[] = tg.getMonsterNames();
				int[] flopRates = tg.getFlopRate();
				if(monsters != null && monsters.length != 0){
					%>
					
					<%
				}
				%>
				
				<%
				
				if(monsters != null && flopRates != null){
					for(int j = 0; j < monsters.length && j < flopRates.length; j++){
						if(j == (monsters.length -1)){
							%>
							<tr>
				<td class="tdcolor2 td bottomtd"><%=monsters[j] %></td>
				<td class="tdcolor2 td bottomtd righttd"><%=flopRates[j] %>%</td>
				</tr>
							<%
						}
				%>
				
				
				<%} }%>
				
				</table>
				</td>
				</tr>
				</table>
				
				
				</td>
				<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
				</tr>
				<%}}else{
						%>
						<tr>
						<td class="tdcolor2 td bottomtd"><%=tg.goalTypeToString(tg.getGoalType()) %></td>
						<td class="tdcolor2 td bottomtd"><%=tg.getGoalName() %></td>
						<td class="tdcolor2 td bottomtd"><%=tg.getGoalAmount() %></td>
						<td class="tdcolor2 td bottomtd"><%=tg.getMapName() %></td>
						<td class="tdcolor2 td bottomtd"><%="("+tg.getX()+","+tg.getY()+")" %></td>
						<td class="tdcolor2 td bottomtd tdtable">
						<table class="tablestyle1">
						<tr>
						<td class="tdcolor2 td bottomtd">描述:<%=tg.getDescription() %></td>
						<td class="tdcolor2 td bottomtd righttd tdtable">
						<%String questionTitles[] = tg.getMonsterNames();
						StringBuffer qsb = new StringBuffer();
						if(questionTitles != null){
							for(int m = 0; m < questionTitles.length; m++){
								String questionTitle = questionTitles[m];
								if(questionTitle != null){
									if(qsb.length() != 0){
										qsb.append("\n");
									}
									qsb.append("题目"+(m+1)+":<a href='question.jsp?titleName="+java.net.URLEncoder.encode(questionTitle)+"'>"+questionTitle+"</a>");
								}
							}
						}
						%>
						</td>
						</tr>
						</table>
						
						
						</td>
						<td class="tdcolor2 bottomtd righttd"><%=tg.getTalkLines() %></td>
						</tr>
						<%
					} %>
					
					</table>
					</td>
					</tr>
								
								<%
							}
							}
						}
					%></table><%
					}
				}
				%>
				</td>
				
			</tr>
		<%}
		
	%>
</table>
<%
out.println("所有的任务数:"+alSize+"个，未显示旧任务:"+count+"个");
	}} %>
<%} %>
</body>
<script type="text/javascript" src="../js/title.js"></script>
</html>
