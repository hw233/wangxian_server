<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="com.xuanzhi.tools.transaction.*,
com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*,
com.fy.engineserver.datasource.career.*,
com.fy.engineserver.mail.*,
com.fy.engineserver.mail.service.*,java.sql.Connection,java.sql.*,java.io.*,com.xuanzhi.boss.game.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.datasource.article.data.props.*"%>
<%@page import="com.fy.engineserver.datasource.article.manager.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.*"%>
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所持物品</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<%
int col = 5;
long playerId = -1;
String playerName = null;
Object obj = session.getAttribute("playerid");
Object obj2 = session.getAttribute("playerName");
if(obj != null){
	playerId = Long.parseLong(obj.toString());
}
if(obj2 != null){
	playerName = obj2.toString();
}
String action = request.getParameter("action");
String errorMessage = null;
PlayerManager pm = PlayerManager.getInstance();
Player player = null;
ArticleManager am = ArticleManager.getInstance();
ArticleEntityManager aem = ArticleEntityManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();
CareerManager cm = CareerManager.getInstance();

MailManager mmanager = MailManager.getInstance();
if (action == null) {
	if (errorMessage == null) { 
		if(playerId != -1){
			player = pm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}
		}else if(playerName != null){
			player = pm.getPlayer(playerName);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}
		}
	}
}else if (action != null && action.equals("select_playerId")) {
	try {
		playerId = Long.parseLong(request
				.getParameter("playerid"));
	} catch (Exception e) {
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if (errorMessage == null) {
		player = pm.getPlayer(playerId);
		if (player == null) {
			errorMessage = "ID为" + playerId + "的玩家不存在！";
		}else{
			session.setAttribute("playerid",request.getParameter("playerid"));
			playerName = player.getName();
			session.setAttribute("playerName",playerName);
		}
	}
}else if (action != null && action.equals("select_playerName")) {
	try {
		playerName = request
				.getParameter("playerName");
	} catch (Exception e) {
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if (errorMessage == null) {
		player = pm.getPlayer(playerName);
		if (player == null) {
			errorMessage = "角色名为" + playerName + "的玩家不存在！";
		}else{
			session.setAttribute("playerName",request.getParameter("playerName"));
			playerId = player.getId();
			session.setAttribute("playerid",playerId);
		}
	}
}else if(action != null && action.equals("addArticle")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String articleName = request.getParameter("article");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(articleName == null || articleName.trim().length() == 0){
			errorMessage = "要增加的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));
		Article a = am.getArticle(articleName);
		if(a != null){
			String countStr = request.getParameter("articleCount");
			String colorType = request.getParameter("articleColor");
			int color = 0;
			if(colorType != null && !colorType.trim().equals("")){
				color = Integer.parseInt(colorType);
			}
			if(countStr != null){
				try{
					int count = Integer.parseInt(countStr);
					for(int i = 0; i < count; i++){
						ArticleEntity ae = null;
						boolean bindFlag = false;
						if(a.getBindStyle() == Article.BINDTYPE_PICKUP || a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP){
							bindFlag = true;
						}
						if(a instanceof Equipment && color >= 0 && color < ArticleManager.color_equipment_Strings.length){
							ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
						}else if(color >= 0 && color < ArticleManager.color_article_Strings.length){
							ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
						}else{
							return;
						}
						
						if(!player.putToKnapsacks(ae,"后台")){
							errorMessage = "没有放入物品1";
						}
					}
				}catch(Exception e){
					ArticleEntity ae = null;
					boolean bindFlag = false;
					if(a.getBindStyle() == Article.BINDTYPE_PICKUP || a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP){
						bindFlag = true;
					}
					if(a instanceof Equipment && color >= 0 && color < ArticleManager.color_equipment_Strings.length){
						ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
					}else if(color >= 0 && color < ArticleManager.color_article_Strings.length){
						ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
					}else{
						return;
					}
					
					if(!player.putToKnapsacks(ae,"后台")){
						errorMessage = "没有放入物品2";
					}
				}
			}else{
				ArticleEntity ae = null;
				boolean bindFlag = false;
				if(a.getBindStyle() == Article.BINDTYPE_PICKUP || a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP){
					bindFlag = true;
				}
				if(a instanceof Equipment && color >= 0 && color < ArticleManager.color_equipment_Strings.length){
					ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
				}else if(color >= 0 && color < ArticleManager.color_article_Strings.length){
					ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
				}else{
					return;
				}
				
				if(!player.putToKnapsacks(ae,"后台")){
					errorMessage = "没有放入物品3";
				}
			}
		}
	}
	}
}else if(action != null && action.equals("useArticle")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String cellIndex = request.getParameter("cellIndex");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));


	}
	}
}else if(action != null && action.equals("unUseArticle")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String equipmentColumnIndex = request.getParameter("equipmentColumnIndex");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(equipmentColumnIndex == null || equipmentColumnIndex.trim().length() == 0){
			errorMessage = "要卸载的装备不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));
		player.removeEquipment(player.getCurrentSuit(),new Byte(equipmentColumnIndex));
	}
	}
}else if(action != null && action.equals("change")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));
		if(player.getCurrentSuit() == 0){
			player.switchEquipmentSuit((byte)1);
		}else{
			player.switchEquipmentSuit((byte)0);
		}
	}
	}
}else if(action != null && action.equals("arrange")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
}else if(action != null && action.equals("disposeArticle")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String cellIndex = request.getParameter("cellIndex2");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
}else if(action != null && action.equals("seeArticle")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String cellIndex = request.getParameter("cellIndex1");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
} else if(action != null && action.equals("addToWareHouse")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String cellIndex = request.getParameter("cellIndex3");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要转移的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
}else if(action != null && action.equals("removeFromWareHouse")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String cellIndex = request.getParameter("cellIndexWareHouse");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要转移的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
} else if(action != null && action.equals("addToMail")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	String cellIndex = request.getParameter("cellIndex4");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要邮寄的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
} else if(action != null && action.equals("sendMail")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
}else if(action != null && action.equals("giveArticleToPlayerByMail")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		String ip = request.getRemoteAddr();
		String recorder = "";
		Object o = session.getAttribute("authorize.username");
		if(o!=null){
			recorder = o.toString();
		}
		session.setAttribute("playerid",request.getParameter("playerid"));
		MailManager mm = MailManager.getInstance();
		String equipmentid = request.getParameter("givearticleid");
		try {
		ArticleEntity ae = aem.getEntity(Integer.parseInt(equipmentid));

		if(ae != null){
			if(mm != null){
				mm.sendMail(player.getId(), new ArticleEntity[]{ae}, "请保管好您的物品", "请保管好您的物品", 0, 0, 0, "后台发放",MailSendType.后台发送,player.getName(),ip,recorder);
			}
			out.println("成功给玩家"+player.getName()+"发送邮件，邮件中包含"+ae.getArticleName());
		}else{
			out.println("没有id为"+equipmentid+"的物品");
		}
		} catch (Exception e) {
			e.printStackTrace();
			out.println("邮件发送异常，请查看玩家邮件后再一次进行该操作");
		}
	}
	}
}else if(action != null && action.equals("inlayToEquipment")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));
		MailManager mm = MailManager.getInstance();
		String equipmentid = request.getParameter("inlayEquipmentId");
		String inlayIndex = request.getParameter("inlayArticleIndex");
		String cellIndex = request.getParameter("cellIndex6");
		try {
		ArticleEntity ae = aem.getEntity(Long.parseLong(equipmentid));

		if(ae != null){
			player.inlayEquipment(Long.parseLong(equipmentid),(byte)(Byte.parseByte(inlayIndex) - 1),Short.parseShort(cellIndex));
			out.println("成功给玩家"+player.getName()+"镶嵌物品"+ae.getArticleName());
		}else{
			out.println("没有id为"+equipmentid+"的物品");
		}
		} catch (Exception e) {
			e.printStackTrace();
			out.println("镶嵌异常，请查看玩家装备后再一次进行该操作");
		}
	}
	}
}else if(action != null && action.equals("upgradeMagic")){
	//服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		session.setAttribute("playerid",request.getParameter("playerid"));

	}
	}
}
if(player != null){
	playerId = player.getId();
}

PropsCategory propsCategory[] = am.getAllPropsCategory();

//一般无属性道具
Article[] articleList = am.getAllOtherArticles();
ArrayList<Article> articlesListTemp = new ArrayList<Article>();

if(am.getAllArticles() != null){
	for(Article a : am.getAllArticles()){
		articlesListTemp.add(a);
	}
}

articleList = articlesListTemp.toArray(new Article[0]);



%>
<script type="text/javascript">

function sendMail() {
	<% //服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){%>
    document.getElementById("fmreceiver").value = document.getElementById("receiver").value;
    document.getElementById("fmtitle").value = document.getElementById("title").value;
    document.getElementById("fmcontent").value = document.getElementById("content").value;
    document.getElementById("fmcoins").value = document.getElementById("coins").value;
    document.fmMail.action.value = "sendMail";
	document.fmMail.submit();
	<%}%>
}
function giveArticleToPlayerFromMail(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	
	document.getElementById("givearticleid").value = document.getElementById("equipmentid").value;
	document.giveArticleToPlayerByMail.submit();
	<%}%>
}
function inlayArticleToEquipment(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex6").value = obj[i].value;
        }   
    }
	document.getElementById("inlayEquipmentId").value = document.getElementById("equipmentidid").value;
	document.getElementById("inlayArticleIndex").value = document.getElementById("inlayIndex").value;
	document.inlayToEquipment.submit();
	<%}%>
}
function changeupgradeMagic(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex7").value = obj[i].value;
        }   
    }
	document.getElementById("upgradeMagicValues").value = document.getElementById("upgradeMagicValue").value;
	document.getElementById("upgradeMagicChongValues").value = document.getElementById("upgradeMagicChongValue").value;
	document.upgradeMagic.submit();
	<%}%>
}
function addToMail() {
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
   		if(obj[i].checked){   
    		document.getElementById("cellIndex4").value = obj[i].value;   
    		break;
        }   
    }
    document.getElementById("fmreceiver").value = document.getElementById("receiver").value;
    document.getElementById("fmtitle").value = document.getElementById("title").value;
    document.getElementById("fmcontent").value = document.getElementById("content").value;
    document.getElementById("fmcoins").value = document.getElementById("coins").value;
    document.fmMail.action.value = "addToMail";
	document.fmMail.submit();
	<%}%>
}

function addToWareHouse() {
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
   		if(obj[i].checked){   
    		document.getElementById("cellIndex3").value = obj[i].value;   
    		break;
        }   
    }
	document.fmAddToWareHouse.submit();
	<%}%>
}
function removeFromWareHouse(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
   		if(obj[i].checked){   
    		document.getElementById("cellIndexWareHouse").value = obj[i].value;   
    		break;
        }   
    }
	document.fmRemoveFromWareHouse.submit();
	<%}%>
}
function auction() {
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
   		if(obj[i].checked){   
    		document.getElementById("cellIndex5").value = obj[i].value;   
    		break;
        }   
    }
	document.fmAuction.submit();
	<%}%>
}

function doubleClickUseByButton(index){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	document.getElementById("cellIndex").value = index;
	document.useArticle.submit();
	<%}%>
}
function onMouseUpFunction(knapsackIndex,index){
	alert(knapsackIndex+"+"+index);
}
function seeArticleByRadio(){
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex1").value = obj[i].value;
    	window.open("ArticlesSelect.jsp?action=seeArticle&playerid=<%=playerId %>&cellIndex1="+obj[i].value,"","width=500,height=600,top=60,left=100");
        }   
    }
	//document.seeArticle.submit();
	 
}
function drillArticleByRadio(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex1").value = obj[i].value;
    	window.open("ArticlesStrong.jsp?action=drill&drillCount="+document.getElementById("drillCount").value+"&playerid=<%=playerId %>&cellIndex1="+obj[i].value,"","width=500,height=600,top=60,left=100");
        }   
    }
	//document.seeArticle.submit();
	<%}%>
	 
}
function strongArticleByRadio(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex1").value = obj[i].value;
    	window.open("ArticlesStrong.jsp?action=strongArticle&playerid=<%=playerId %>&cellIndex1="+obj[i].value,"","width=500,height=600,top=60,left=100");
        }   
    }
	//document.seeArticle.submit();
	<%}%>
	 
}
function strong8ArticleByRadio(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex1").value = obj[i].value;
    	window.open("ArticlesStrong.jsp?action=strongArticle&playerid=<%=playerId %>&level="+document.getElementById("equipmentlevel").value+"&cellIndex1="+obj[i].value,"","width=500,height=600,top=60,left=100");
        }   
    }
	//document.seeArticle.submit();
	<%}%>
	 
}
function strong12ArticleByRadio(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex1").value = obj[i].value;
    	window.open("ArticlesStrong.jsp?action=strongArticle&playerid=<%=playerId %>&level=12&cellIndex1="+obj[i].value,"","width=500,height=600,top=60,left=100");
        }   
    }
	//document.seeArticle.submit();
	<%}%>
	 
}
function disposeArticleByRadio(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex2").value = obj[i].value;
        }   
    }
    document.disposeArticle.submit();
    <%}%>
}
function doubleClickUse(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj = document.getElementsByName("articleRadio");   
    for(var i=0;i<obj.length;i++){
    if(obj[i].checked){   
    	document.getElementById("cellIndex").value = obj[i].value;   
        }   
    }
	document.useArticle.submit();
	<%}%>
}
function doubleClickUnUse(index){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
    document.getElementById("equipmentColumnIndex").value = index;
    document.unUseArticle.submit();
    <%}%>
}
function onClickChange(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
    document.change.submit();
    <%}%>
}
function onClickArrange(){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
    document.arrange.submit();
    <%}%>
}
function onClickAddArticle(index){
	<% //服务器类型为0代表为可修改的开发服务器
	if(GameConstants.getInstance().getServerType() == 0){%>
	var obj;
	if(index == 0){
	    obj = document.getElementById('article0'); //selectid
	}else if(index == 1){
		obj = document.getElementById("article1");
	}else if(index == 2){
		obj = document.getElementById("article2");
	}else if(index == 3){
		obj = document.getElementById("article3");
	}else if(index == 4){
		obj = document.getElementById("article4");
	}else if(index == 5){
		obj = document.getElementById("article5");
	}else if(index == 6){
		obj = document.getElementById("article6");
	}else if(index == 7){
		obj = document.getElementById("article7");
	}else if(index == 8){
		obj = document.getElementById("article8");
	}else if(index == 9){
		obj = document.getElementById("article9");
	}else if(index == 10){
		obj = document.getElementById("article10");
	}else if(index == 11){
		obj = document.getElementById("article11");
	}else if(index == 12){
		obj = document.getElementById("article12");
	}else if(index == 13){
		obj = document.getElementById("article13");
	}else if(index == 14){
		obj = document.getElementById("article14");
	}else if(index == 14){
		obj = document.getElementById("article14");
	}
	if(index != 15){
	    var index = obj.selectedIndex; // 选中索引
	    var str = obj.options[index].text; // 选中文本
		document.getElementById("article").value = str;
		document.getElementById("articleCount").value = document.getElementById("count").value;
		document.getElementById("articleColor").value = document.getElementById("colorType").value;
	    document.addArticle.submit();
	}else{
		if(document.getElementById("articlename").value!=""){
			document.getElementById("article").value = document.getElementById("articlename").value;
			document.getElementById("articleCount").value = document.getElementById("count").value;
			document.getElementById("articleColor").value = document.getElementById("colorType").value;
		    document.addArticle.submit();
		}
	}
	<%}%>
}

function loopchangeli(index){
	var myobj1=document.getElementById("li"+index+"1");
	var myobj2=document.getElementById("li"+index+"2");
	var myobj3=document.getElementById("li"+index+"3");
	var maxlength = myobj1.clientHeight;
	var length1 = myobj1.clientHeight;
	var length2 = myobj2.clientHeight;
	var length3 = myobj3.clientHeight;
	if(maxlength < myobj2.clientHeight){
		maxlength = myobj2.clientHeight;
	}
	if(maxlength < myobj3.clientHeight){
		maxlength = myobj3.clientHeight;
	}
	myobj1.style.height=maxlength+"px";
	myobj2.style.height=maxlength+"px";
	myobj3.style.height=maxlength+"px";
}
function heightautochange(index) {
	for(var i = 0; i < index; i++){
	loopchangeli(i);
	}

	
	var myobj1=document.getElementById("topleft");
	var myobj2=document.getElementById("topright");
	var myobj3=document.getElementById("middleleft");
	var myobj4=document.getElementById("middleright");
	var myobj5=document.getElementById("ectableid");
	var myobj6=document.getElementById("knapsacktableid");
	var maxtoplength = myobj1.clientHeight;
	var maxmiddlelength = myobj5.clientHeight;
	var length1 = myobj1.clientHeight;
	var length2 = myobj2.clientHeight;
	var length3 = myobj3.clientHeight;
	var length4 = myobj4.clientHeight;
	var length5 = myobj5.clientHeight;
	var length6 = myobj6.clientHeight;

	if(maxtoplength < length2){
		maxtoplength = length2;
	}
	if(maxmiddlelength < length6){
		maxmiddlelength = length6;
	}
	if(maxmiddlelength < length4){
		maxmiddlelength = length4;
	}
	myobj1.style.height=maxtoplength+"px";
	//myobj1.style.paddingTop = (maxlength-length1)/2+"px";
	//myobj1.style.paddingBottom = (maxlength-length1)/2+"px";
	myobj2.style.height=maxtoplength+"px";
	//myobj2.style.paddingTop = (maxlength-length2)/2+"px";
	//myobj2.style.paddingBottom = (maxlength-length2)/2+"px";
	myobj3.style.height=maxmiddlelength+"px";
	//myobj4.style.height=maxmiddlelength+"px";
	//myobj5.style.height=maxmiddlelength+"px";
	//myobj6.style.height=maxmiddlelength+"px";
	//myobj3.style.paddingTop = (maxlength-length3)/2+"px";
	//myobj3.style.paddingBottom = (maxlength-length3)/2+"px";
	}
//给url地址增加时间戳，不读取缓存    
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

function ajaxFunction(id)
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
   setposition(id);
  }
}
var str = document.getElementById("articleid").value;
var playerIdStr = document.getElementById("playerIdStr").value;
var url = "ArticlesSelectforajax.jsp?playerid="+playerIdStr+"&articleid="+str;
xmlHttp.open("GET",convertURL(url),true);
xmlHttp.send(null);


}
function onClickValueChange(value,id){
		document.getElementById("articleid").value=value;
		ajaxFunction(id);
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
<style type="text/css">
<!--
*{margin: 0px;padding: 0px;list-style: none;}
body{
 margin:0px;
 padding: 0px;
 text-align: center;
 table-layout: fixed;
 word-wrap: break-word;
 list-style: none;
 font-size: 16px;
}
#main{
width:1200px;
margin:10px auto;
border:0px solid #69c;
background-color:#DCE0EB;
}
#topleft{
width:267px;
border:1px solid #69c;
border-top:0px solid #69c;
border-bottom:0px solid #69c;
int:left;
}
#topleft ul{
width:267px;
}
#topleft li{
width:266px;
int:left;
border:1px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
}
#topleft .bottomli{
border-bottom:1px solid #69c;
}
#topleft .textalignleft{
text-align: left;
}
#topleft .bottomrightli{
border-right:1px solid #69c;
}
#topright{
width:734px;
border:1px solid #69c;
border-left:0px solid #69c;
border-bottom:0px solid #69c;
int:left;
}
#topright ul{
width:734px;
}
#topright li{
width:734px;
border:1px solid #69c;
border-top:0px solid #69c;
border-left:0px solid #69c;
border-right:0px solid #69c;
int:left;
}
#topright .bottomli{
border-bottom:0px solid #69c;
}
#middleleft{
width:180px;
border:1px solid #69c;
border-bottom:0px solid #69c;
background-color:#cff;
int:left;
height: 100%;
}
#middleleft ul{
width:180px;
}
#middleleft li{
width:180px;
height:30px;
int:left;
border:1px solid #69c;
border-top:0px solid #69c;
}
#middleright{
width:822px;
border:1px solid #69c;
border-left:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
int:left;
}
#bottom{
width:1003px;
border:1px solid #69c;
border-left:0px solid #69c;
background-color:#DCE0EB;
int:left;
}
#bottom li{
height:30px;
width:166px;
int:left;
border:1px solid #69c;
border-top:0px solid #69c;
border-right:0px solid #69c;
}
#bottom .topli{
width:1002px;
}
#bottom .bottombli{
border-bottom:0px solid #69c;
}
#bottom .lastli{
border-right:1px solid #69c;
}
#topleft li{
border-left:0px solid #69c;
}
.titlecolor{
background-color:#C2CAF5;
}
.fontcolor{
color:#FF0000;
font-family:"Times New Roman",Georgia,Serif;
}
.middlelefttableclass{
width:180px;
height:300px;
border:0px solid #69c;
border-collapse: collapse;
}
.tableclass1td{border:1px solid blue;}
.middlerighttableclass{
width:822px;
border:0px solid #69c;
border-collapse: collapse;
}
td{
border:1px solid #69c;
word-wrap: break-word;
}
#middleleft td{
border:0px solid #69c;
border-bottom:1px solid #69c;
}
#middleleft .bottomtd{

}
#middleright td{
width:20%;
word-wrap: break-word;
text-align: center;
border-left:0px solid #69c;
}
#middleright li{
width:821px;
border:0px solid #69c;
border-right:1px solid #69c;
}
#showdiv{
background-color:#DCE0EB;
text-align:left;
margin:10px 0px;
}
.toptd{
border-top:0px solid #69c;
}
.lefttd{
border-left:0px solid #69c;
}
.righttd{
border-right:0px solid #69c;
}
.td{
border-right:0px solid #69c;
border-bottom:0px solid #69c;
}
.tdlittlewidth{
width:50px;
}
.tdwidth{
width:25%;
}
.tdForBlank{
height:0px;
border-top:0px solid #69c;
border-right:0px solid #69c;
border-bottom:0px solid #69c;
border-left:0px solid #69c;
}
.clear{
clear:both;
}
.clearli{
clear:right;
}
-->
</style>
</head>
<input type="hidden" name ="playerIdStr" id="playerIdStr" value="'<%=playerId %>'">
<body onload="javascript:heightautochange(3);">
<a style="color:#2D20CA" href="ArticlesOnPlayer.jsp">刷新此页面</a>
<br><br>
<form id='f1' name='f1' action=""><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action=""><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<input type="hidden" id="articleid" name="articleid">
<form id="useArticle" name="useArticle" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='useArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex" name='cellIndex'>
</form>
<form name="unUseArticle" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='unUseArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="equipmentColumnIndex" name='equipmentColumnIndex'>
</form>
<form name="seeArticle" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='seeArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex1" name='cellIndex1'>
</form>
<form name="disposeArticle" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='disposeArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex2" name='cellIndex2'>
</form>
<form name="change" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='change'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
</form>
<form name="arrange" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='arrange'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
</form>
<form name="addArticle" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='addArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="article" id="article">
<input type="hidden" name="articleCount" id="articleCount">
<input type="hidden" name="articleColor" id="articleColor">
</form>
<form name="giveArticleToPlayerByMail" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='giveArticleToPlayerByMail'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="givearticleid" id="givearticleid">
</form>
<form name="inlayToEquipment" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='inlayToEquipment'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="inlayEquipmentId" id="inlayEquipmentId">
<input type="hidden" name="inlayArticleIndex" id="inlayArticleIndex">
<input type="hidden" name="cellIndex6" id="cellIndex6">
</form>
<form name="fmAddToWareHouse" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='addToWareHouse'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex3" name='cellIndex3'>
</form>
<form name="fmRemoveFromWareHouse" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='removeFromWareHouse'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndexWareHouse" name='cellIndexWareHouse'>
</form>
<form name="fmMail" action="ArticlesOnPlayer.jsp">
<input type='hidden' id='action' name='action' value='addToMail'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex4" name='cellIndex4'>
<input type='hidden' id="fmreceiver" name='fmreceiver'>
<input type='hidden' id="fmtitle" name='fmtitle'>
<input type='hidden' id="fmcontent" name='fmcontent'>
<input type='hidden' id="fmcoins" name='fmcoins'>
</form>
<form name="fmAuction" action="auction/auction.jsp">
<input type='hidden' name='playerId' value='<%=playerId %>'>
<input type='hidden' id="cellIndex5" name='cellIndex5'>
</form>
<form name="upgradeMagic" action="ArticlesOnPlayer.jsp">
<input type='hidden' name='action' value='upgradeMagic'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="upgradeMagicValues" id="upgradeMagicValues">
<input type="hidden" name="upgradeMagicChongValues" id="upgradeMagicChongValues">
<input type="hidden" name="cellIndex7" id="cellIndex7">
</form>
<div id=pltsTipLayer style="display: none;position: absolute; z-index:10001"></div>
<br>
<%
	if(errorMessage != null){
		out.println("<center><table border='0' cellpadding='0' cellspacing='2' width='100%' bgcolor='#FF0000' align='center'>");
		out.println("<tr bgcolor='#FFFFFF' align='center'><td>");
		out.println("<font color='red'><pre>"+errorMessage+"</pre></font>");
		out.println("</td></tr>");
		out.println("</table></center>");
	}
%>
<%
if(player != null ){
	Player p = player;
	//装备栏
	EquipmentColumn ec = player.getEquipmentColumns();

	//道具栏

%>

<div id="showdiv" style="position:absolute;  display:none;"></div>
<div id="main">
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){ %>
<div id="topleft">
<ul>
<li id="li01">武器&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:

</li>
<li id="li02">
非武器装备:

</li>
<li id="li03">
简单道具&nbsp;&nbsp;:

</li>
<li id="li11">
持续性道具:

</li>
<li id="li12">
回城符道具:

</li>
<li id="li13">
坐骑类道具:

</li>
<li id="li21">
任务类道具:

</li>
<li id="li22">
镶嵌类宝石:

</li>
<li id="li23">
升级类物品:

</li>
<li id="li31">
打孔类物品:

</li>
<li id="li32">
背包扩展&nbsp;&nbsp;:

</li>
<li id="li33">
离线经验&nbsp;&nbsp;:

</li>
<li id="li36">
宝箱&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:

</li>
<li id="li37">
随机宝箱&nbsp;&nbsp;:

</li>
<li id="li34">
所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:
		<select name="article8" id="article8" style="width:110px" >
		<%for(Article w: articleList){ %>
		<option id="<%=w.getName() %>" ><%=w.getName() %></option>
		<% }%>
		</select>
	<input type="button" value="增加" onclick="javascript:onClickAddArticle(8);">
</li>
<li id="li39">自己填写&nbsp;&nbsp;:
		<input type="text" name="articlename" id="articlename" style="width:110px">
	<input type="button" value="增加" onclick="javascript:onClickAddArticle(15);">
</li>
<li id="li38" class="textalignleft">
增加个数&nbsp;&nbsp;:
	<input type="text" name="count" id="count" value="1" style="width:110px">
</li>
<li id="li50" class="bottomli textalignleft">
颜色数值&nbsp;&nbsp;:
	<input type="text" name="colorType" id="colorType" value="0" style="width:110px">
</li>
<li id="li35" style="display:none;">
</li>
</ul>
</div>

<div id="topright">

<ul>
<li class="titlecolor">玩家(<font color="red"><%=p.getName() %></font>)装备栏物品栏</li>
<li>人物状态</li>
<li>力量:<%=p.getStrength() %></li>
<li>内力:<%=p.getSpellpower() %></li>
<li>体制:<%=p.getConstitution() %></li>
<li>灵巧:<%=p.getDexterity() %></li>
<li>HP:<%=p.getHp()%>/<%=p.getMaxHP() %></li>
<li>MP:<%=p.getMp()%>/<%=p.getMaxMP() %></li>
<li>经验:<%=p.getExp()%>/<%=p.getNextLevelExp() %></li>
<li>物理攻击:<%=p.getPhysicalDamageLowerLimit() %>/<%=p.getPhysicalDamageUpperLimit()%></li>
<li>物理防御:<%=p.getPhyDefence() %></li>
<li>道术防御:<%=p.getMagicDefence() %></li>
<li>命中:<%=p.getHit() %></li>
<li>闪避:<%=p.getDodge() %></li>
<li>速度:<%=p.getSpeed() %></li>
<li>物理暴击:<%=p.getCriticalHit() %></li>
<li>门派:<%=cm!= null&&cm.getCareer(p.getCareer())!= null? cm.getCareer(p.getCareer()).getName():"错误" %></li>
</ul>
</div>
<div class="clear"></div>
<%} %>
<div id="middleleft">
<table id="ectableid" class="middlelefttableclass">
			<tr >
				<td class="titlecolor">当前装备栏(<%=player.getCurrentSuit() %>)</td>
			</tr>
			<tr >
				<td >武器:
				<%

					ArticleEntity[] equipmentEntity = ec.getEquipmentArrayByCopy();
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 0){
										equipmentFlag = true;
										
										%>
										<img id="00" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','00');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('0');">&nbsp;<input type="button" style="width:70px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('0');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[0] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[0] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >头盔:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 1){
										equipmentFlag = true;
										
										%>
										<img id="01" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','01');" on onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('1');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('1');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[1] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[1] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >护肩:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 2){
										equipmentFlag = true;
										
										%>
										<img id="02" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','02');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('2');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('2');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[2] %> 
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[2] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >衣服:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 3){
										equipmentFlag = true;
										
										%>
										<img id="03" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','03');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('3');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('3');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[3] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[3] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >护手:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 4){
										equipmentFlag = true;
										
										%>
										<img id="04" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','04');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('4');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('4');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[4] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[4] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >鞋子:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 5){
										equipmentFlag = true;
										
										%>
										<img id="05" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','05');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('5');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('5');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[5] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[5] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >首饰:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 6){
										equipmentFlag = true;
										
										%>
										<img id="06" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','06');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('6');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('6');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[6] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[6] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >项链:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 7){
										equipmentFlag = true;
										
										%>
										<img id="07" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','07');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('7');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('7');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[7] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[7] %>
						<%
					}
				%>
				</td>
			</tr>
			<tr >
				<td >戒指:
				<%
					if(equipmentEntity != null && equipmentEntity.length != 0){
						boolean equipmentFlag = false;
						for(ArticleEntity ee : equipmentEntity){
							if(ee != null){
								String equipmentName = ee.getArticleName();
								if(am.getArticle(equipmentName) instanceof Equipment){
									Equipment equipment = (Equipment)am.getArticle(equipmentName);
									if(equipment.getEquipmentType() == 8){
										equipmentFlag = true;
										
										%>
										<img id="08" onmouseover="javascript:onClickValueChange('<%=ee.getId() %>','08');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('8');">&nbsp;<input type="button" style="width:80px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('8');">
										<%
									}
								}else{
									%>
									类型错误，非装备物品放入了装备栏
									<%
								}
							}
						}
						if(!equipmentFlag){
							%>
							<%=ec.getEquipmentIds()[8] %>
							<%
						}
					}else{
						%>
						<%=ec.getEquipmentIds()[8] %>
						<%
					}
				%>
				</td>
			</tr>
			<!--  <tr>
				<td class="bottomtd"></td>
			</tr>-->
		</table>
		<input type="button" style="width:106px" value="切换到装备栏(<%=player.getCurrentSuit() == 0 ? 1 : 0 %>)" onclick="javascript:onClickChange();">
</div>
<div id="middleright">
<%
Knapsack[] knapsacks = player.getKnapsacks_common();
for(int knapsackIndex = 0; knapsackIndex < knapsacks.length; knapsackIndex++){
	Knapsack knapsack = knapsacks[knapsackIndex];
		if(knapsack != null && knapsack.getCells() != null){
		
		%>
			<table id="knapsacktableid" class="middlerighttableclass">
			<tr ><td colspan="8" class="toptd lefttd titlecolor fontcolor" align="center">背包栏(<%=ArticleManager.得到背包名字(knapsackIndex) %>)</td></tr>
			<%
			int cellCount = knapsack.getCells().length;
			for(int i = 0; i < cellCount; i+=col){ %>
				<tr >
				<%for(int j = 0; j < col; j++){
					if((i+j)>=cellCount){
	  					for(;j<col;j++){
	  						%>
	  						<td>不可用</td>
	  						<%
	  					}
	  					break;
	  				}
					Cell cell = knapsack.getCell(i+j);
					if(cell != null){
						String s = "";
						ArticleEntity ae = aem.getEntity(cell.getEntityId());
						if(ae!=null){
				%>
					<td ><input type="radio" value="<%=knapsackIndex+"0"+(i+j) %>" name="articleRadio" id="articleRadio"><input type="button" style="width:80px" value="<%=ae.getArticleName() %><%=cell.getCount()==1?"":"("+cell.getCount()+")" %>" onclick="javascript:doubleClickUseByButton(<%=knapsackIndex+"0"+(i+j) %>);"><img width="16" height="16" src="/imageServlet?id=<%=am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" onmouseup="javascript:onMouseUpFunction(<%=knapsackIndex %>,<%=(i+j) %>);" ondblclick="javascript:doubleClickUse(<%=knapsackIndex+"0"+(i+j) %>);" id="<%=knapsackIndex+"0"+(i+j) %>" onmouseover="javascript:onClickValueChange('<%=ae.getId() %>','<%=knapsackIndex+"0"+(i+j) %>');" onmouseout="javascript:divHidden();"><%=cell.getEntityId() %></td>
				<%	}else{
					%>
					<td ><%=cell.getEntityId() %></td>
					<%
				}
					}else{
					
					%>
					<!--<td>第<%=i+j %>个格子为NULL</td>-->
					<td >&nbsp;</td>
					<%
					}
					
				} %>
				</tr>
			<%} %>
			<!--  <tr>
				<td colspan="8" align="center">
				
				</td>
			</tr>-->
			</table>
		<%
		}
}
Knapsack knapsack = player.getKnapsacks_fangBao();
		if(knapsack != null && knapsack.getCells() != null){
		
		%>
			<table id="knapsacktableid1" class="middlerighttableclass">
			<tr ><td colspan="8" class="toptd lefttd titlecolor fontcolor" align="center">防爆背包栏</td></tr>
			<%
			int cellCount = knapsack.getCells().length;
			for(int i = 0; i < cellCount; i+=col){ %>
				<tr >
				<%for(int j = 0; j < col; j++){
					if((i+j)>=cellCount){
	  					for(;j<col;j++){
	  						%>
	  						<td>不可用</td>
	  						<%
	  					}
	  					break;
	  				}
					Cell cell = knapsack.getCell(i+j);
					if(cell != null){
						String s = "";
						ArticleEntity ae = aem.getEntity(cell.getEntityId());
						if(ae!=null){
				%>
					<td ><input type="radio" value="<%="0"+(i+j) %>" name="articleRadio" id="articleRadio"><input type="button" style="width:80px" value="<%=ae.getArticleName() %><%=cell.getCount()==1?"":"("+cell.getCount()+")" %>" onclick="javascript:doubleClickUseByButton(<%="0"+(i+j) %>);"><img width="16" height="16" src="/imageServlet?id=<%=am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" onmouseup="javascript:onMouseUpFunction(<%=0 %>,<%=(i+j) %>);" ondblclick="javascript:doubleClickUse(<%="0"+(i+j) %>);" id="<%="0"+(i+j) %>" onmouseover="javascript:onClickValueChange('<%=ae.getId() %>','<%="0"+(i+j) %>');" onmouseout="javascript:divHidden();"><%=cell.getEntityId() %></td>
				<%	}else{
					%>
					<td ><%=cell.getEntityId() %></td>
					<%
				}
					}else{
					
					%>
					<!--<td>第<%=i+j %>个格子为NULL</td>-->
					<td >&nbsp;</td>
					<%
					}
					
				} %>
				</tr>
			<%} %>
			<!--  <tr>
				<td colspan="8" align="center">
				
				</td>
			</tr>-->
			</table>
		<%
}
knapsack = player.getKnapsacks_cangku();
if(knapsack != null && knapsack.getCells() != null){
	
	%>
		<table id="knapsacktableid2" class="middlerighttableclass">
		<tr ><td colspan="8" class="toptd lefttd titlecolor fontcolor" align="center">仓库</td></tr>
		<%
		int cellCount = knapsack.getCells().length;
		for(int i = 0; i < cellCount; i+=col){ %>
			<tr >
			<%for(int j = 0; j < col; j++){
				if((i+j)>=cellCount){
  					for(;j<col;j++){
  						%>
  						<td>不可用</td>
  						<%
  					}
  					break;
  				}
				Cell cell = knapsack.getCell(i+j);
				if(cell != null){
					String s = "";
					ArticleEntity ae = aem.getEntity(cell.getEntityId());
					if(ae!=null){
			%>
				<td ><input type="radio" value="" name="articleRadio" id="articleRadio"><input type="button" style="width:80px" value="<%=ae.getArticleName() %><%=cell.getCount()==1?"":"("+cell.getCount()+")" %>" onclick="javascript:doubleClickUseByButton();"><img width="16" height="16" src="/imageServlet?id=<%=am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" onmouseup="javascript:onMouseUpFunction();" ondblclick="javascript:doubleClickUse();" id="" onmouseover="javascript:onClickValueChange('<%=ae.getId() %>','');" onmouseout="javascript:divHidden();"><%=cell.getEntityId() %></td>
			<%	}else{
				%>
				<td ><%=cell.getEntityId() %></td>
				<%
			}
				}else{
				
				%>
				<!--<td>第<%=i+j %>个格子为NULL</td>-->
				<td >&nbsp;</td>
				<%
				}
				
			} %>
			</tr>
		<%} %>
		<!--  <tr>
			<td colspan="8" align="center">
			
			</td>
		</tr>-->
		</table>
	<%
	}
%>


		<ul><li><input type="button" style="width:100px" value="自动整理道具栏" onclick="javascript:onClickArrange();">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="使用选中物品" onclick="javascript:doubleClickUse();">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="查看选中物品" onclick="javascript:seeArticleByRadio();">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="打孔" onclick="javascript:drillArticleByRadio();"><input type="text" id="drillCount" value="1" style="width:10px">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="强化选中物品" onclick="javascript:strongArticleByRadio();">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="强化装备到" onclick="javascript:strong8ArticleByRadio();"><input type="text" id="equipmentlevel" value="0" style="width:10px">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="强化装备到12" onclick="javascript:strong12ArticleByRadio();">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="销毁选中物品" onclick="javascript:disposeArticleByRadio();">
			<input type="button" style="width:100px" value="物品加入仓库" onclick="javascript:addToWareHouse();">
			<input type="button" style="width:100px" value="取出仓库物品" onclick="javascript:removeFromWareHouse();">
			<input type="button" style="width:100px" value="拍卖选中的物品" onclick="javascript:auction();">
			<input type="button" style="width:100px" value="返还物品给玩家" onclick="javascript:giveArticleToPlayerFromMail();"><input type="text" id="equipmentid" value="0" style="width:100px">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="镶嵌选中宝石到装备上" onclick="javascript:inlayArticleToEquipment();">装备id<input type="text" id="equipmentidid" value="0" style="width:100px">镶嵌位置<input type="text" id="inlayIndex" value="0" style="width:100px">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="升级选中法宝" onclick="javascript:changeupgradeMagic();">升到的级别<input type="text" id="upgradeMagicValue" style="width:100px">升到的重数<input type="text" id="upgradeMagicChongValue" style="width:100px">&nbsp;&nbsp;
			<input type="button" style="width:100px" value="物品加入邮件" onclick="javascript:addToMail();"></li></ul>
</div>
<div class="clear"></div>
<div id="bottom">
</div>
<div class="clear"></div>
<div style="text-align:left;">
<%
Mail mail = (Mail)session.getAttribute("mail");
if(mail == null) {
	mail = new Mail();
}
 %>
<ul><li class="topli titlecolor">新邮件</li></ul>
<ul>
	<li>发件人:<%=player.getName() %><input type="hidden" name=sender id=sender value="<%=player.getName() %>">
	<br>收件人:<input type="text" size=15 name="receiver" id=receiver value="<%=mail.getReceiver()<=0?"":pm.getPlayer(mail.getReceiver()).getName() %>">
	<br>标题:<input type="text" size=45 name="title" id=title value="<%=mail.getTitle()==null?"":mail.getTitle() %>">
	<br>内容:<textarea name="content" id=content cols=45 rows=5><%=mail.getContent()==null?"":mail.getContent() %></textarea>
	<br>金钱:<input type="text" size=15 name="coins" id=coins value="<%=mail.getCoins() %>">(最小单位)</li>
</ul>
<div class="clear"></div>
<ul>
	<li id="articleRadio">以下为附件</li>
</ul>
</div>
<div class="clear"></div>
<div id="bottom">
			<ul>
				<%

				 %>
			</ul>
<div class="clear"></div>
<ul>
	<li class="topli titlecolor"><input type="button" name="bt1" value="发送邮件" onclick="sendMail()"></li>
</ul>
</div>
</div>
<%}
%>

</body>
</html>
