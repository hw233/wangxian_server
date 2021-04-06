<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn" %>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.masterAndPrentice.*" %>
<%@page import="com.fy.engineserver.stat.StatData"%>
<%@page import="com.fy.engineserver.society.*" %>
<%@page import="com.fy.engineserver.constants.GameConstant"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*" %>
<%@page import="com.fy.engineserver.honor.Honor"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.*" %>
<%@page import="com.fy.engineserver.core.event.LeaveGameEvent"%>
<%@page import="com.fy.engineserver.gm.record.*" %>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitle" %>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager" %>
<%@include file="../header.jsp"%>
<%-- <%@include file="../authority.jsp"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>潜龙玩家管理</title>
		<link rel="stylesheet" href="../style.css" />
		<style type="text/css">
		#showdiv{
		background-color:#DCE0EB;
		text-align:left;
		margin:10px 0px;
		}
		</style>
		<script type="text/javascript">
	function $(tag) {
		return document.getElementById(tag);
	}
	function change(mid, tag,sid) {
		//将子定义的内容填入回复框
		var instr = document.getElementById("repcontent" + mid);
		if (tag.value != "") {
			instr.value = tag.value;
		}
	}


	function ajaxFunction(playerid,id,sid)
	{
	var xmlHttp;
	
	try
	   {
	  // Firefox, Opera 8.0+, Safari
	   xmlHttp=new XMLHttpRequest();
	   }
	catch (e)
	   {
	
	 // Internet Explorer
	  try
	     {
	     xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
	     }
	  catch (e)
	     {
	
	     try
	        {
	        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
	        }
	     catch (e)
	        {
	        alert("您的浏览器不支持AJAX！");
	        return false;
	        }
	     }
	   }
	
	xmlHttp.onreadystatechange=function()
	{
	if(xmlHttp.readyState==4)
	  {
	   //document.myForm.name.value=xmlHttp.ResponseText;
	   document.getElementById("showdiv").innerHTML = xmlHttp.responseText;
	   setposition(sid);
	  }
	}
	//var str = document.getElementById("articleid").value;
	var url = "/gm/gmuser/ArticlesSelectforajax.jsp?playerid="+playerid+"&articleid="+id;
	xmlHttp.open("GET",convertURL(url),true);
	xmlHttp.send(null);
	
	
	}
	function convertURL(url) {    
        //获取时间戳
        var timstamp = (new Date()).valueOf();    
        //将时间戳信息拼接到url上    
        //url = "AJAXServer"
        if (url.indexOf("?") >= 0) {
            url = url + "&t=" + timstamp;    
        } else {
            url = url + "?t=" + timstamp;    
        }
        return url;
}
	function setposition(id){
	  var obj = document.getElementById("showdiv");
		obj.style.display="";
		obj.style.border="1px solid red";
		var objheight = obj.clientHeight;
		var objwidth = obj.clientWidth;
		var o = document.getElementById(id);
		var lefttopx = o.offsetLeft;
		var lefttopy = o.offsetTop;
		var oparent = o.offsetParent;
		while(oparent.tagName != "BODY"){
			lefttopx = lefttopx + oparent.offsetLeft;
			lefttopy = lefttopy + oparent.offsetTop;
			oparent = oparent.offsetParent;
		}
		
		
		var righttopx = lefttopx+o.offsetWidth;
		//var righttopy = o.offsetTop;
		//var leftbottomx = o.offsetLeft;
		var leftbottomy = lefttopy+o.offsetHeight;
		//var rightbottomx = o.offsetLeft+o.offsetWidth;
		//var rightbottomy = o.offsetTop+o.offsetHeight;
		var scrollTopvalue = document.documentElement.scrollTop;
		var scrollLeftvalue = document.documentElement.scrollLeft;
		var clientHeightvalue = document.documentElement.clientHeight;
		var clientWidthvalue = document.documentElement.clientWidth;
		//如果相对左边横坐标大于浏览器宽度的1/2，那么就把位置设置在这个对象的左边，以此类推就可以比较好的显示
		if((lefttopx-scrollLeftvalue)>(clientWidthvalue/2)){
			if((lefttopx-2)>objwidth){
				obj.style.left = (lefttopx-2-objwidth)+"px";
			}else{
				obj.style.left = (scrollLeftvalue+2)+"px";
			}
		}else{
			obj.style.left=(righttopx+2)+"px";
		}
		if((lefttopy-scrollTopvalue)> (clientHeightvalue/2)){
			if(objheight>clientHeightvalue){
				obj.style.top = (scrollTopvalue+2)+"px";
			}else{
				if((lefttopy-2)>objheight){
					obj.style.top = (lefttopy-2-objheight)+"px";
				}else{
					obj.style.top = (scrollTopvalue+2)+"px";
				}
			}
			
		}else{
			obj.style.top = (leftbottomy+2)+"px";
		}
	}
	function divHidden(){
		  var obj = document.getElementById("showdiv");
		  obj.style.display="none";
	}
	function silent(playid, playername) {
		if (playid) {
			var hour = prompt("请输入禁言的小时数,注意小于等于0时为永久禁言", "0")
			var result = prompt("请输入禁言的原因", "聊天中包含非法词汇");
			window.location.replace("?action=silent&playername=" + playername + "&playerid=" + playid
					+ "&shour=" + hour + "&result=" + result);

		}
	}

	function talk(playid, playername) {
		if (playid) {
			var result = prompt("请输入解禁的原因", "系统错误")
			window.location.replace("?action=talk&playername=" + playername + "&playerid=" + playid
					+ "&result=" + result);

		}
	}

	function kick(playid, playername) {
		alert(11);
		if (playid && window.confirm("你确认踢掉这个人吗！")) {
			window.location.replace("?action=kick&playername=" + playername + "&playid=" + playid
					+ "&kick=true");

		}
	}
</script>
	</head>
	<body>
		
		<div id='d1'>
			<form action='' method='get' id='f1'>
				<table>
					<tr>
						<th>
							用户名
						</th>
						<td class="top">
							<input type='text' id='userid' name='userid'
								value='<%=request.getParameter("userid") == null ? ""
					: request.getParameter("userid")%>' />
							<input type='hidden' id='mid' name='mid'
								value='<%=request.getParameter("mid")%>' />
						</td>
					</tr>
					<tr>
						<th>
							操作
						</th>
						<td>
							<input type='submit' value='查询用户' />
						</td>
					</tr>
				</table>
			</form>
			<%
				try {
					String userName = request.getParameter("userid");
					if(userName!=null&&userName.trim().length()>0){
						String uname="";
						ActionManager acmanager = ActionManager.getInstance();
						SilenceRecord sr = SilenceRecord.getInstance();
						KickManager kcmanager = KickManager.getInstance();
						out.print("<input type='button' value='刷新' onclick='window.location.replace(\"user_action.jsp?a=1");
						if (userName != null && "" != userName)//如果参数不为空的刷新的时候也加上参数
							out.print("&userid=" + userName);
						if (request.getParameter("playername") != null
								&& !"".equals(request.getParameter("playername")))
							out.print("&playername="
									+ request.getParameter("playername"));
						if (request.getParameter("playerid") != null
								&& !"".equals(request.getParameter("playerid")))
							out.print("&playerid=" + request.getParameter("playerid"));
						out.print("\")' />");
						PlayerManager mpm = PlayerManager.getInstance();
						ChatMessageService cms = ChatMessageService.getInstance();
						if (userName != null && !userName.equals("")) {
							try {
//	 							out.print("<input type='button' value='创建角色' onclick='window.location.replace(\"resultbyid.jsp?text="
//	 											+ userName.trim() + "\")' />");
								Player players[] = mpm.getPlayerByUser(userName.trim());
								if(players!=null){
										System.out.print(players.length);
										out.print("<table align='80%' align='center' ><tr><th>Id</th><th>角色名</th><th>性别</th><th>等级</th><th>国家</th><th>宗派</th></tr>");
										for (Player player : players) {//遍历玩家角色列表
											out.println("<tr><td><a href ='user_action.jsp?playerid="
															+ player.getId()
															+ "' >"
															+ player.getId()
															+ "</a></td><td><a href='user_action.jsp?playername="
															+ player.getName()
															+ "' >"
															+ player.getName()
															+ "</a></td><td>"
															+ (player.getSex() == 0 ? "男" : "女")
															+ "</td>");
											String country = "";
											if(player.getCountry()==1){
												country = "昊天";
											}else if(player.getCountry()==2){
												country = "无尘";
											}else if(player.getCountry()==3){
												country = "沧月";
											}
											out.print("<td>"
													+ player.getLevel()
													+ "</td><td>"
													+ country
													+ "</td><td>"
													+ (player.getGangName().equals("") ? "无"
															: player.getGangName())
													+ "</td></tr>");
										}
										out.print("</table>");
								}
							} catch (Exception e) {
							    e.printStackTrace();
								out.print("<p color='red'>请输入正确的用户名"+StringUtil.getStackTrace(e)+"</p>");
							}
						}
					}
					
					}catch(Exception e){
						e.printStackTrace();
						out.print("<p color='red'>异常："+StringUtil.getStackTrace(e)+"</p>");
					}
			%>

		</div>
		<hr />
		
		<%
			try{
				
// 				String userName = request.getParameter("userid");//获取用户名
				String playerName = request.getParameter("playername");
		%>
		<div id='d2'>
			<form action='' method='post' id='f2'>
				<table>
					<caption>
						请输入角色名或者角色ID其中的任意一个
					</caption>
					<tr>
						<th>
							角色名
						</th>
						<td class="top">
							<input type='text' id='playername' name='playername' value='' />
						</td>
					</tr>
					<tr>
						<th>
							角色ID
						</th>
						<td>
							<input type='text' id='playerid' name='playerid' value='' />
						</td>
					</tr>
					<tr>
						<th>
							操作
						</th>
						<td>
							<input type='submit' value='查询角色' />
						</td>
					</tr>
				</table>
			</form>
			<%
			String playername = request.getParameter("playername");
			String playerId = request.getParameter("playerid");
			if(playername!=null || playerId!=null){
				ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
				ActionManager acmanager = ActionManager.getInstance();
				ArticleManager amanager = ArticleManager.getInstance();
				PlayerManager mpm = PlayerManager.getInstance();
				ChatMessageService cms = ChatMessageService.getInstance();
				SilenceRecord sr = SilenceRecord.getInstance();
				KickManager kcmanager = KickManager.getInstance();
				CareerManager cmanager = CareerManager.getInstance();
				FaeryManager fmanager = FaeryManager.getInstance();
				Relation relation =null;
				Player p = null;
				try {
					if (request.getParameter("action") != null) {
						String action = request.getParameter("action");
						try {
							if ("kick".equals(action)) {
								//提某个玩家下线
								long playid = Long.parseLong(request.getParameter(
										"playid").trim());
								String result1 = request.getParameter("kickResult");
								mpm.kickPlayer(playid);
								Kick kick = new Kick();
								kick.setDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));	
								kick.setGmname("0");	
								kick.setUsername(playerName);
								kick.setType("kick");
								kick.setResult(result1);
								kcmanager.addKicks(kick);
										p = mpm.getPlayer(playid);
										if(p != null && p.isOnline()) {
											if(p.getCurrentGame() != null){
												p.getCurrentGame().getQueue().push(new LeaveGameEvent(p));
											}
											p.leaveServer();
											p.getConn().close();
										}
										
									acmanager.save("", "踢掉了一个玩家角色账号为:" + playid
										+ "--原因 [" + result1 + "]");
							}
							if ("silent".equals(action)) {
								//禁言
								long playid = Long.parseLong(request.getParameter(
										"playerid").trim());
								int hour = Integer.parseInt(request.getParameter(
										"shour").trim());
								String result1 = request.getParameter("result");
								cms.banPlayer(playid, hour);
								
								Kick kick = new Kick();
								kick.setDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));	
//									kick.setGmname(uname);	
								kick.setUsername(playerName);
								kick.setType("silent");
								kick.setResult(result1);
								kcmanager.addKicks(kick);
								acmanager.save("", "沉默了一个玩家角色ID为 :" + playid
												+ "时间：" + hour + "---原因： ["
												+ result1 + "]");
								}
								
							if ("talk".equals(action)) {
								long playid = Long.parseLong(request.getParameter(
										"playerid").trim());
								cms.relieveBanedPlayer(playid);
								String result1 = request.getParameter("result");
								
								Kick kick = new Kick();
								kick.setDate(DateUtil.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));	
//									kick.setGmname(uname);	
								kick.setUsername(playerName);
								kick.setType("talk");
								kick.setResult(result1);
								kcmanager.addKicks(kick);
								
								acmanager.save("", "恢复了一个玩家聊天功能角色ID为 :" + playid
										+ "---原因： [" + result1 + "]");
							}
						} catch (Exception e) {
							out.print("<p color='red'>操作所需要的信息不全"+StringUtil.getStackTrace(e)+"</p>");
						}
					}
					
					if (playername != null || playerId != null) {
						
						if (playername != null && !"".equals(playername))
							p = mpm.getPlayer(playername.trim());
						if (playerId != null && !"".equals(playerId))
							p = mpm.getPlayer(Long.parseLong(playerId.trim()));
						
						out.println("<table><tr><th>角色ID</th><td class='top' >"
								+ p.getId());
						if (!cms.isPlayerBaned(p.getId()))
							out.print("<input type='button' value='禁言' onclick='silent(\""
									+ p.getId() + "\",\"" + p.getName() + "\");'  />");
						if (cms.isPlayerBaned(p.getId()))
							out.print("<input type='button' onclick='talk(\""
									+ p.getId() + "\",\"" + p.getName() + "\");' value='解禁' />");
						out.print("<input type='button' value='踢下线' onclick='kick(\""
										+ p.getId()
										+ "\",\""
										+ p.getName()
										+ "\");' />");
						out
								.print("<input type='button' value='查询邮件' onclick='window.location.replace(\"player_mail1.jsp?action=query&pid="
										+ p.getId() + "\")' />");
						out.print("</td></tr>");
									
						String countryName = "";
						if(p.getCountry()==1){
							countryName = "昊天";
						}else if(p.getCountry()==2){
							countryName = "无尘";
						}else if(p.getCountry()==3){
							countryName = "沧月";
						}
						out.print("<tr><th>用户名</th><td>"
								+ p.getUsername()+"</td></tr>");
						out.print("<tr><th>角色名</th><td>"
								+ p.getName()+"</td></tr>");
						out.print("<tr><th>国家</th><td>"
								+ countryName+"</td></tr>");
						out.print("<tr><th>仙府</th><td>");
						if(fmanager.getCave(p)!=null){
							out.print(fmanager.getCave(p).getOwnerName());									
						}
						out.print("</td></tr><tr><th>在线状态</th><td>"
								+ (p.isOnline()==true?"在线":"下线") +"</td></tr>");
						out.print("<tr><th>配偶</th><td>"
								+ (p.getSpouse() == null?"":p.getSpouse()) +"</td></tr>");
						out.print("<tr><th>性别</th><td>"
								+ (p.getSex() == 0 ? "男" : "女") + "</td></tr>");
						out.print("<tr><th>级别</th><td>" + p.getLevel()
								+ "</td></tr>");
						out.print("<tr><th>宗族</th><td>"
								+ p.getZongPaiName()+"</td></tr>");
						out.print("<tr><th>家族</th><td>"
								+ p.getJiazuName()+"</td></tr>");
						out.print("<tr><th>职业</th><td>"+cmanager.careerNames[p.getCareer()]+"</td></tr>");				
						out.print("<tr><th>银子</th><td>"
								+ p.getSilver()+"</td></tr>");
						out.print("<tr><th>绑银</th><td>"
								+ p.getBindSilver()+"</td></tr>");
						out.print("<tr><th>称号</th><td>");
						if(p.getPlayerTitles()!=null){
							List<PlayerTitle> playerTitles = p.getPlayerTitles();
							String ptName ="";
							for(PlayerTitle pt:playerTitles){
								if(pt!=null&&pt.getTitleName()!=null){
									ptName = pt.getTitleName();
									out.print(ptName+"&nbsp;&nbsp");
								}
							}
						}
						
						out.print("</td></tr>");
						out.print("<tr><th>师徒列表</th><td>");
						if(p!=null&&SocialManager.getInstance().getRelationById(p.getId())!=null&&SocialManager.getInstance().getRelationById(p.getId()).getMasterPrentice()!=null){
							relation = SocialManager.getInstance().getRelationById(p.getId());
							MasterPrentice mp = relation.getMasterPrentice();
							if(mp!=null&&mp.getMasterId()!=-1){
								out.print("师傅："+mpm.getPlayer(mp.getMasterId()).getName()+"("+mpm.getPlayer(mp.getMasterId()).getId()+")<br/>");
								if(mp.getPrentices().size()>0){
									for(long s :mp.getPrentices()){
										try{
										if(mpm.getPlayer(s)!=null)
										  	out.print("徒弟列表："+mpm.getPlayer(s).getName()+"("+mpm.getPlayer(s).getId()+")<br/>");								  
										}catch(Exception e){
											out.print("玩家不存在(ID:"+s+")  ");
										}
									}
								}
							}else{
								out.print("没有师傅或者徒弟");
							}
						}
						
						out.print("</td></tr>");
						if(p.getLeaveMasterTime()!=0l)
						out.print("<tr><th>主动离开师傅的时间</th><td>"+DateUtil.formatDate(new Date(p.getLeaveMasterTime()),"yyyy-MM-dd HH:mm")+"</td></tr>");
						if(p.getBanishPrenticeTime()!=0l)
						out.print("<tr><th>驱逐徒弟的时间</th><td>"+DateUtil.formatDate(new Date(p.getBanishPrenticeTime()),"yyyy-MM-dd HH:mm")+"</td></tr>");
						out.print("<tr><th>好友列表</th><td>");
						int c=0;
						List<Long> frs = relation.getFriendlist();
						for(Long fr:frs){
							try{
								out.print(mpm.getPlayer(fr).getName()+"("+fr+")  ");
							}catch(Exception e){
								out.print("玩家不存在(ID:"+fr+")  ");
							}
							c++;
							if(c%5==0){
							  out.print("<br/>");
							}
						}
						out.print("</td></tr>");
						out.print("<tr><th>角色数据</th><td>"+(p.getStatData(StatData.STAT_KILLING_NUM)==null?0:p.getStatData(StatData.STAT_KILLING_NUM).getValue())+"(击杀数)    ");
						out.print((p.getStatData(StatData.STAT_INCOME_FORM_AU_WEEKLY)==null?0:p.getStatData(StatData.STAT_INCOME_FORM_AU_WEEKLY).getValue())+"(官职)");
						out.print((p.getStatData(StatData.STAT_ONLINE_TIME)==null?0:p.getStatData(StatData.STAT_ONLINE_TIME).getValue()/(60000))+"分(体力)<br/>");
						out.print((p.getStatData(StatData.STAT_ONLINE_DAYS)==null?0:p.getStatData(StatData.STAT_ONLINE_DAYS).getValue())+"(元气)");
						out.print((p.getStatData(StatData.STAT_CURRENT_LEVEL_ONLINE_TIME)==null?0:p.getStatData(StatData.STAT_CURRENT_LEVEL_ONLINE_TIME).getValue()/(60000))+"(移动速度)    ");
						out.print((p.getStatData(StatData.STAT_DUEL_TIMES)==null?0:p.getStatData(StatData.STAT_DUEL_TIMES).getValue())+"(境界)");
						out.print((p.getStatData(StatData.STAT_DUEL_WIN_TIMES)==null?0:p.getStatData(StatData.STAT_DUEL_WIN_TIMES).getValue())+"(历练)<br/>");
						out.print((p.getStatData(StatData.STAT_DUEL_LOSE_TIMES)==null?0:p.getStatData(StatData.STAT_DUEL_LOSE_TIMES).getValue())+"(功勋)");
						out.print((p.getStatData(StatData.STAT_CURRENT_DAY_ONLINE_TIME)==null?0:p.getStatData(StatData.STAT_CURRENT_DAY_ONLINE_TIME).getValue()/(60000))+"(文采值)");
						out.print((p.getStatData(StatData.STAT_CURRENT_WEEK_ONLINE_DAYS)==null?0:p.getStatData(StatData.STAT_CURRENT_WEEK_ONLINE_DAYS).getValue())+"(恶名值)");
						out.print((p.getStatData(StatData.STAT_LAST_WEEK_ONLINE_DAYS)==null?0:p.getStatData(StatData.STAT_LAST_WEEK_ONLINE_DAYS).getValue())+"(在线时长)</td></tr>");
						EquipmentColumn equips = p.getEquipmentColumns();
						out.print("<tr><th>身上物品</th><td><table style='border:0px' >");
						int i = 0;
						long ids[] = equips.getEquipmentIds();
						for (long id : ids) {
							if (i == 0)
								out.print("<tr>");
							if (aemanager.getEntity(id) != null) {
								out
										.print("<td style='border:0px'><span id='s"+id+"' onmouseover='ajaxFunction(\""+p.getId()+"\",\""+id+"\",\"s"+id+"\")' onmouseout='divHidden();' >"
												+ aemanager.getEntity(id)
														.getArticleName()
												+ "</span></td>");
								i++;
							}
							if (i % 4 == 0)
								out.print("</tr><tr>");
						}
						if (i % 4 != 0) {
							for (int j = 0; j < (i % 4); j++)
								out.print("<td style='border:0px'></td>");
							out.print("</tr>");
						}
						out.print("</table></td></tr>");
						out.print("<tr><th>背包物品</th><td><table style='border:0px' >");
						Knapsack ks = p.getKnapsack_common();
						Cell[] css = ks.getCells();
						i = 0;
						if(css.length>0){
							for (Cell cs : css) {
								if (cs != null) {
									if (i == 0)
										out.print("<tr>");
									if (aemanager.getEntity(cs.getEntityId()) != null) {
										out.print("<td style='border:0px'><span id='b"+cs.getEntityId()+"' onmouseover='ajaxFunction(\""+p.getId()+"\",\""+cs.getEntityId()+"\",\"b"+cs.getEntityId()+"\")' onmouseout='divHidden();' >"
												+ aemanager.getEntity(
														cs.getEntityId())
														.getArticleName() + "</span>("
												+ cs.getCount() + ")</td>");
										i++;
									}
									if (i % 4 == 0)
										out.print("</tr><tr>");

								}
							}
							if (i % 4 != 0) {
								for (int j = 0; j < (i % 4); j++)
									out.print("<td style='border:0px' ></td>");
								out.print("</tr>");
							}
						}
						out.print("</table></td></tr>");

						
						out.print("<tr><th>防暴背包</th><td><table style='border:0px' >");
						if (p.getKnapsack_fangbao() != null) {
						css = p.getKnapsack_fangbao().getCells();
						i = 0;
						for (Cell cs : css) {
							if (cs != null) {
								if (i == 0)
									out.print("<tr>");
								if (aemanager.getEntity(cs.getEntityId()) != null) {
									out.print("<td style='border:0px'><span id='c"+cs.getEntityId()+"' onmouseover='ajaxFunction(\""+p.getId()+"\",\""+cs.getEntityId()+"\",\"c"+cs.getEntityId()+"\")' onmouseout='divHidden();' >"
											+ aemanager.getEntity(
													cs.getEntityId())
													.getArticleName() + "</span>("
											+ cs.getCount() + ")</td>");
									i++;
								}
								if (i % 4 == 0)
									out.print("</tr><tr>");

							}
						}
						if (i % 4 != 0) {
							for (int j = 0; j < (i % 4); j++)
								out.print("<td style='border:0px' ></td>");
							out.print("</tr>");
						}
							
						}
							out.print("</table></td></tr>");
						if (p.getKnapsacks_cangku() != null) {
							out.print("<tr><th>仓库物品</th><td><table style='border:0px' >");
							css = p.getKnapsacks_cangku().getCells();
							i = 0;
							for (Cell cs : css) {
								if (cs != null) {
									if (i == 0)
										out.print("<tr>");
									if (aemanager.getEntity(cs.getEntityId()) != null) {
										out.print("<td style='border:0px'><span id='c"+cs.getEntityId()+"' onmouseover='ajaxFunction(\""+p.getId()+"\",\""+cs.getEntityId()+"\",\"c"+cs.getEntityId()+"\")' onmouseout='divHidden();' >"
												+ aemanager.getEntity(
														cs.getEntityId())
														.getArticleName() + "</span>("
												+ cs.getCount() + ")</td>");
										i++;
									}
									if (i % 4 == 0)
										out.print("</tr><tr>");

								}
							}
							if (i % 4 != 0) {
								for (int j = 0; j < (i % 4); j++)
									out.print("<td style='border:0px' ></td>");
								out.print("</tr>");
							}
							out.print("</table></td></tr>");
						}

						out.print("<tr><th>地图名</th><td>" + p.getMapName()
								+ "</td></tr></table>");

					}
				} catch (Exception e) {
					e.printStackTrace();
					out.print("<p color='red' >请输入正确的角色名</p>" );
				}
			}
					
				} catch (Exception e) {
					out.print(StringUtil.getStackTrace(e));
				}
			%>
<div id="showdiv" style="position:absolute;  display:none;"></div>
		</div>


	</body>
</html>
