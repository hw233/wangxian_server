<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	<%@include file="../authority.jsp" %>
<%@page import="com.fy.engineserver.datasource.props.Knapsack.Cell"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.gang.service.GangMemberManager"%>
<%@page import="com.fy.engineserver.gang.model.GangTitle"%>
<%@page import="com.fy.engineserver.gang.model.Gang"%>
<%@page import="com.fy.engineserver.gang.service.GangManager"%>
<%@page
	import="com.fy.engineserver.gang.gangwarehouse.GangWareHouseManager"%>
<%@page
	import="com.fy.engineserver.gang.gangwarehouse.GangWareHouse"%>
<%@page
	import="com.fy.engineserver.gang.gangwarehouse.GangWareHouses"%>
<%@page import="com.fy.engineserver.gang.model.GangMember"%>
<%@include file="../header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>潜龙玩家管理 </title>
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
	function query() {
		var str = "gang_message.jsp?a=1";
		var gangname = $("gangname").value;
		if (gangname)
			str = str + "&gangname=" + gangname;
		var gangid = $("gangid").value;
		if (gangid)
			str = str + "&gangid=" + gangid;
		if (gangname || gangid) {
			window.location.replace(str);
		} else {
			alert("请输入正确的信息！！");
		}
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
	var url = "/game_server/gm/gmuser/ArticlesSelectforajax.jsp?playerid="+playerid+"&articleid="+id;
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
</script>
	</head>
	<body>

		<%
			try {
				String uname = session.getAttribute("username").toString();//判断是否登录
				GangManager gamanager = GangManager.getInstance();
				PlayerManager pmanager = PlayerManager.getInstance();
				ArticleEntityManager aemanager = ArticleEntityManager
						.getInstance();
				GangMemberManager gmmanager = GangMemberManager.getInstance();
				GangWareHouseManager gwhmanager = GangWareHouseManager
						.getInstance();
				String gangname = request.getParameter("gangname");
				String gangid = request.getParameter("gangid");

				Gang gang = null;
				if ((gangname != null && gangname.trim().length() > 0)
						|| (gangid != null && gangid.trim().length() > 0)) {
					if (gangname != null)
						gang = gamanager.getGang(gangname);
					if (gangid != null)
						gang = gamanager.getGang(Long.parseLong(gangid.trim()));
				}

				out.print("帮派名：<input type='text' value='"
						+ (gangname == null ? "" : gangname)
						+ "' id='gangname' name='gangname' />");
				out.print("或帮派Id:<input type='text' value='"
						+ (gangid == null ? "" : gangid)
						+ "' id='gangid' name='gangid' />");
				out
						.print("<input type ='button' value='查询' onclick='query();' />");
				if (gang != null) {
					out.print("<table align='center' width='80%'>");
					out.print("<tr><th>帮派名称</th><td class='top'>"
							+ gang.getName() + "(" + gang.getId()
							+ ")</th></tr>");
				    out.print("<tr><th>帮派等级</th><td >"+gang.getGanglevel()+"当前经验("+gang.getGangexp()+")</td></tr>");
				    out.print("<tr><th>帮派资金</th><td>"+gang.getFunds()+"</td></tr>");
					out.print("<tr><th>帮会公告</th><td>" + gang.getNotice()
							+ "</td></tr>");
					out.print("<tr><th>创建日期</th><td>"
							+ DateUtil.formatDate(gang.getCreatedate(),
									"yyyy-MM-dd HH:mm") + "</td></tr>");
					Player creator=null;
					try{
						creator=pmanager.getPlayer(
									Integer.parseInt(gang.getCreator()+""));
					}catch(Exception e){
					
					}
					if(creator!=null){
						out.print("<tr><th>创建人</th><td>"
							+ pmanager.getPlayer(
									Integer.parseInt(gang.getCreator() + ""))
									.getName() + "</td></tr>");					
					}else{
						out.print("<tr><th>创建人</th><td>玩家不存在"
							 +gang.getCreator()+ "</td></tr>");
					}
					
					Player master=null;
					try{
						master=pmanager.getPlayer(
									Integer.parseInt(gang.getBangzhu()+""));
					}catch(Exception e){
					
					}
					if(master!=null){
						out.print("<tr><th>帮主</th><td>"
							+ pmanager.getPlayer(
									Integer.parseInt(gang.getBangzhu() + ""))
									.getName() + "</td></tr>");					
					}else{
						out.print("<tr><th>帮主</th><td>玩家不存在"
							 +gang.getBangzhu()+ "</td></tr>");
					}
					try{
						out.print("<tr><th>帮主仓库</th><td>");
					Knapsack.Cell[] css = null;
					int i = 0;
					if (gwhmanager.getGangWareHouses(gang.getId()) != null) {
						css = gwhmanager.getGangWareHouses(gang.getId())
								.getCells(GangWareHouses.TYPE_MASTER_ONLY);
						i = 0;
						out.print("<table style='border:0px' >");
						for (Knapsack.Cell cs : css) {
							if (cs != null) {
								if (i == 0)
									out.print("<tr>");
								if (aemanager.getEntity(cs.getEntityId()) != null&&creator!=null) {
									
									out.print("<td style='border:0px'> <span id='s"+cs.getEntityId()+"' onmouseover='ajaxFunction(\""+pmanager.getPlayer(
									Integer.parseInt(gang.getCreator()+"")).getId()+"\",\""+cs.getEntityId()+"\",\"s"+cs.getEntityId()+"\")' onmouseout='divHidden();' >"
											+ aemanager.getEntity(
													cs.getEntityId())
													.getArticleName() + "("
											+ cs.getCount() + ")</span></td>");
									i++;
								}
								if (i % 10 == 0)
									out.print("</tr><tr>");

							}
						}
						if (i % 10 != 0) {
							for (int j = 0; j < (i % 10); j++)
								out.print("<td style='border:0px' ></td>");
							out.print("</tr>");
						}
						out.print("</table>");
					}
					out.print("</td></tr>");
					out
							.print("<tr><th>精英仓库</th><td><table style='border:0px' >");
					if (gwhmanager.getGangWareHouses(gang.getId()) != null) {
						css = gwhmanager
								.getGangWareHouses(gang.getId())
								.getCells(GangWareHouses.TYPE_MASTER_VICEMASTER);
						i = 0;
						for (Knapsack.Cell cs : css) {
							if (cs != null) {
								if (i == 0)
									out.print("<tr>");
								if (aemanager.getEntity(cs.getEntityId()) != null&&creator!=null) {
									out.print("<td style='border:0px'><span id='s"+cs.getEntityId()+"' onmouseover='ajaxFunction(\""+pmanager.getPlayer(
									Integer.parseInt(gang.getCreator()+"")).getId()+"\",\""+cs.getEntityId()+"\",\"s"+cs.getEntityId()+"\")' onmouseout='divHidden();' >"
											+ aemanager.getEntity(
													cs.getEntityId())
													.getArticleName() + "("
											+ cs.getCount() + ")</span></td>");
									i++;
								}
								if (i % 10 == 0)
									out.print("</tr><tr>");

							}
						}
						if (i % 10 != 0) {
							for (int j = 0; j < (i % 10); j++)
								out.print("<td style='border:0px' ></td>");
							out.print("</tr>");
						}
						
					}
					out.print("</table>");
					out.print("</td></tr>");
					out
							.print("<tr><th>公共仓库</th><td><table style='border:0px' >");
					if (gwhmanager.getGangWareHouses(gang.getId()) != null) {
						css = gwhmanager.getGangWareHouses(gang.getId())
								.getCells(GangWareHouses.TYPE_PUBLIC);
						i = 0;
						for (Knapsack.Cell cs : css) {
							if (cs != null) {
								if (i == 0)
									out.print("<tr>");
								if (aemanager.getEntity(cs.getEntityId()) != null&&creator!=null) {
									out.print("<td style='border:0px'><span id='s"+cs.getEntityId()+"' onmouseover='ajaxFunction(\""+pmanager.getPlayer(
									Integer.parseInt(gang.getCreator()+"")).getId()+"\",\""+cs.getEntityId()+"\",\"s"+cs.getEntityId()+"\")' onmouseout='divHidden();' >"
											+ aemanager.getEntity(
													cs.getEntityId())
													.getArticleName() + "("
											+ cs.getCount() + ")</span></td>");
									i++;
								}
								if (i % 10 == 0)
									out.print("</tr><tr>");

							}
						}
						if (i % 10 != 0) {
							for (int j = 0; j < (i % 10); j++)
								out.print("<td style='border:0px' ></td>");
							out.print("</tr>");
						}
						
					}
					}catch(Exception e){
					
					}
					
					out.print("</table>");
					out.print("</td></tr>");
					out.print("</table>");
					out
							.print("<table width='80%' align='center' ><caption>帮派成员</caption>");
					List<GangMember> gams = gmmanager.getGangMembers(gang
							.getId());
					out
							.print("<tr><th>Id</th><th>角色名</th><th>帮派地位</th><th>入帮时间</th><th>贡献度</th><th>帮派贡献</th></tr>");
					for (GangMember gam : gams) {
					   Player p = null;
					   try{
							p = pmanager.getPlayer(
										Integer
												.parseInt(gam.getPlayerid()
														+ ""));					   
					   }catch(Exception e){
					   
					   }
					   if(p!=null){
							out.print("<tr><td>"
								+ gam.getId()
								+ "</td><td><a href='user_action.jsp?userid="+p.getUsername()+"&playerid="+p.getId()+"' >"
								+ p.getName()+"("+p.getId()
								+ ")</a>["+p.getLevel()+"]</td><td>");
						out.print(GangTitle.getTitleDesp(Integer.parseInt(gam
								.getTitleid()
								+ ""))
								+ "</td><td>"
								+ DateUtil.formatDate(gam.getCreatedate(),
										"yyyy-MM-dd HH:mm"));
						out.print("</td><td>" + gam.getContributefunds()
								+ "</td><td>" + gam.getGangcontribution()
								+ "</td></tr>");					   
					   }else{
					   		out.print("ID为"+gam.getPlayerid()+"的玩家不存在！");
					   }
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
				//RequestDispatcher rdp = request
				//		.getRequestDispatcher("visitfobiden.jsp");
				//rdp.forward(request, response);
				out.print(StringUtil.getStackTrace(e));
			}
		%>

		</div>
       <div id="showdiv" style="position:absolute;  display:none;"></div>
		</div>

	</body>
