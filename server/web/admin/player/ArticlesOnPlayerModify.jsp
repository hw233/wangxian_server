<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon"%>
<%@page import="com.fy.engineserver.util.gmstat.EventManager"%>
<%@page import="com.fy.engineserver.util.gmstat.RecordEvent"%>
<%@page import="com.fy.engineserver.util.gmstat.event.ArticleRecordEvent"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.util.datacheck.DataCheckManager"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
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
<%@include file="../IPManager.jsp" %>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page import="com.xuanzhi.tools.authorize.AuthorizedServletFilter"%>
<%@page import="com.fy.engineserver.core.CoreSubSystem"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>人物所持物品</title>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<%!
	public void sendStatMail(RecordEvent event){
		if(!TestServerConfigManager.isTestServer()){
			String [] titles =	{"类型","服务器","账号","角色名","角色id","IP地址","物品集","物品统计集","颜色集","数量集","操作人","操作时间"};
			String title = "后台物品相关操作";
			if(event!=null){
				StringBuffer content = new StringBuffer();
				content.append("<table style='font-size=12px;' border=1><tr bgcolor='greend'>");
				for(String t:titles){
					content.append("<th>");
					content.append(t);
					content.append("</th>");
				}
				content.append("</tr>");
				
				content.append("<tr>"+((ArticleRecordEvent)event).getHtmlStr()+"</tr>");
				content.append("</table>");
				EnterLimitManager.sendMail("[后台物品相关操作] [" + GameConstants.getInstance().getServerName() + "]", content.toString());
			}
		}
	}
%>



<%
if(!TestServerConfigManager.isTestServer()){
	com.xuanzhi.tools.authorize.User user = (com.xuanzhi.tools.authorize.User)session.getAttribute(AuthorizedServletFilter.USER);
	CoreSubSystem.logger.warn("[非法操作-2] [ip:"+request.getRemoteAddr()+"] [un:"+(user != null?user.getName():"null")+"]");
	//return;
}
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
	String articleName_stat = "";
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
			
			articleName_stat = a.getName_stat();
			String countStr = request.getParameter("articleCount");
			String colorType = request.getParameter("articleColor");
			int color = 0;
			if(colorType != null && !colorType.trim().equals("")){
				color = Integer.parseInt(colorType);
			}
			
			if(countStr != null){
				try{
					int count = Integer.parseInt(countStr);
					boolean isadd = false;
					for(int i = 0; i < count; i++){
						ArticleEntity ae = null;
						boolean bindFlag = false;
						if(a.getBindStyle() == Article.BINDTYPE_PICKUP || a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP){
							bindFlag = true;
						}
						if(a instanceof Equipment && color >= 0 && color < ArticleManager.color_equipment_Strings.length){
							ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
						}else if(a instanceof MagicWeapon && color >=0 && color < ArticleManager.color_magicweapon_Strings.length) {
							ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
						}else if(color >= 0 && color < ArticleManager.color_magicweapon_Strings.length){
							ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
						}else{
							return;
						}
						if(!player.putToKnapsacks(ae,"后台")){
							errorMessage = "没有放入物品1";
						}else{
							isadd = true;
						}
					}
					//数据统计
					if(isadd){
						String ip = request.getRemoteAddr();
						Object object = session.getAttribute("authorize.username");
						if(object!=null){
							String recorder = object.toString();
							RecordEvent articleRecordEvent = new ArticleRecordEvent(new String[]{articleName},new String[]{articleName_stat}, new int[]{count},new int[]{color},new int[]{EventManager.getInstance().getArticleType(a)},player.getUsername(), player.getName(),player.getId(), ip,recorder);
							sendStatMail(articleRecordEvent);
						}else{
							String recorder = "";
							RecordEvent articleRecordEvent = new ArticleRecordEvent(new String[]{articleName},new String[]{articleName_stat},new int[]{count},new int[]{color},new int[]{EventManager.getInstance().getArticleType(a)},player.getUsername(),player.getName(),player.getId(),ip,recorder);
							sendStatMail(articleRecordEvent);
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
					}else if(a instanceof MagicWeapon && color >=0 && color < ArticleManager.color_magicweapon_Strings.length) {
						ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
					}else if(color >= 0 && color < ArticleManager.color_article_Strings.length){
						ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
					}else{
						return;
					}
					
					if(!player.putToKnapsacks(ae,"后台")){
						errorMessage = "没有放入物品2";
					}else{
						//数据统计
						String ip = request.getRemoteAddr();
						Object object = session.getAttribute("authorize.username");
						int count = 1;
						if(countStr!=null){
							count = Integer.parseInt(countStr);
						}
						if(object!=null){
							String recorder = object.toString();
							RecordEvent articleRecordEvent = new ArticleRecordEvent(new String[]{articleName},new String[]{articleName_stat}, new int[]{count},new int[]{color},new int[]{EventManager.getInstance().getArticleType(a)},player.getUsername(), player.getName(), player.getId(),ip,recorder);
							sendStatMail(articleRecordEvent);
						}else{
							String recorder = "";
							RecordEvent articleRecordEvent = new ArticleRecordEvent(new String[]{articleName},new String[]{articleName_stat},new int[]{count},new int[]{color},new int[]{EventManager.getInstance().getArticleType(a)},player.getUsername(),player.getName(),player.getId(),ip,recorder);
							sendStatMail(articleRecordEvent);
						}
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
				}else if(a instanceof MagicWeapon && color >=0 && color < ArticleManager.color_magicweapon_Strings.length) {
					ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
				}else if(color >= 0 && color < ArticleManager.color_article_Strings.length){
					ae = aem.createEntity(a,bindFlag,aem.CREATE_REASON_后台,player,color,1,true);
				}else{
					return;
				}
				out.print("=========="+ae.getClass()+"====="+a.getClass());
				if(!player.putToKnapsacks(ae,"后台")){
					errorMessage = "没有放入物品3";
				}else{
					//数据统计
					String ip = request.getRemoteAddr();
					Object object = session.getAttribute("authorize.username");
					int count = 1;
					if(countStr!=null){
						count = Integer.parseInt(countStr);
					}
					if(object!=null){
						String recorder = object.toString();
						RecordEvent articleRecordEvent = new ArticleRecordEvent(new String[]{articleName}, new String[]{articleName_stat},new int[]{count},new int[]{color},new int[]{EventManager.getInstance().getArticleType(a)},player.getUsername(), player.getName(),player.getId(), ip,recorder);
						sendStatMail(articleRecordEvent);
					}else{
						String recorder = "";
						RecordEvent articleRecordEvent = new ArticleRecordEvent(new String[]{articleName},new String[]{articleName_stat}, new int[]{count},new int[]{color},new int[]{EventManager.getInstance().getArticleType(a)},player.getUsername(),player.getName(),player.getId(),ip,recorder);
						sendStatMail(articleRecordEvent);
					}
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
		ArticleEntity ae = player.getKnapsack_common().getArticleEntityByCell(Integer.parseInt(cellIndex));
		player.useItemOfKnapsack(player.getCurrentGame(),0,Integer.parseInt(cellIndex),ae,player.getCurrSoul().getSoulType());

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
		session.setAttribute("playerid",request.getParameter("playerid"));
		MailManager mm = MailManager.getInstance();
		String equipmentid = request.getParameter("givearticleid");
		try {
		ArticleEntity ae = aem.getEntity(Integer.parseInt(equipmentid));


		//if(ae != null){
			//if(mm != null){
				//mm.sendMail(player.getId(), new ArticleEntity[]{ae}, "请保管好您的物品", "请保管好您的物品", 0, 0, 0);
			//}
			//out.println("成功给玩家"+player.getName()+"发送邮件，邮件中包含"+ae.getArticleName());
		//}else{
		//	out.println("没有id为"+equipmentid+"的物品");
		//}
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
var url = "../ArticlesSelectforajax.jsp?playerid="+playerIdStr+"&articleid="+str;
xmlHttp.open("GET",convertURL(url),true);
xmlHttp.send(null);


}
function onClickValueChange1(value,id){
	document.getElementById("articleid").value=value;
	ajaxFunction1(id);
}
function ajaxFunction1(id)
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
   document.getElementById("equipmentDes").innerHTML = xmlHttp.responseText;
  }
}
var str = document.getElementById("articleid").value;
var playerIdStr = document.getElementById("playerIdStr").value;
var url = "../ArticlesSelectforajax.jsp?playerid="+playerIdStr+"&articleid="+str;
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
 text-align: left;
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
width:1000px;
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
<a style="color:#2D20CA" href=ArticlesOnPlayerModify.jsp>刷新此页面</a>
<br><br>
<form id='f1' name='f1' action=""><input type='hidden' name='action' value='select_playerId'>
请输入要用户的ID：<input type='text' name='playerid' id='playerid' value='<%=playerId %>' size='20'><input type='submit' value='提  交'>
</form>
<form id='f01' name='f01' action=""><input type='hidden' name='action' value='select_playerName'>
请输入要角色名：<input type='text' name='playerName' id='playerName' value='<%=playerName %>' size='20'><input type='submit' value='提  交'>
</form>
<input type="hidden" id="articleid" name="articleid">
<form id="useArticle" name="useArticle" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='useArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex" name='cellIndex'>
</form>
<form name="unUseArticle" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='unUseArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="equipmentColumnIndex" name='equipmentColumnIndex'>
</form>
<form name="seeArticle" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='seeArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex1" name='cellIndex1'>
</form>
<form name="disposeArticle" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='disposeArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex2" name='cellIndex2'>
</form>
<form name="change" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='change'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
</form>
<form name="arrange" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='arrange'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
</form>
<form name="addArticle" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='addArticle'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="article" id="article">
<input type="hidden" name="articleCount" id="articleCount">
<input type="hidden" name="articleColor" id="articleColor">
</form>
<form name="giveArticleToPlayerByMail" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='giveArticleToPlayerByMail'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="givearticleid" id="givearticleid">
</form>
<form name="inlayToEquipment" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='inlayToEquipment'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="inlayEquipmentId" id="inlayEquipmentId">
<input type="hidden" name="inlayArticleIndex" id="inlayArticleIndex">
<input type="hidden" name="cellIndex6" id="cellIndex6">
</form>
<form name="fmAddToWareHouse" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='addToWareHouse'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndex3" name='cellIndex3'>
</form>
<form name="fmRemoveFromWareHouse" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='removeFromWareHouse'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type='hidden' id="cellIndexWareHouse" name='cellIndexWareHouse'>
</form>
<form name="fmMail" action=ArticlesOnPlayerModify.jsp>
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
<form name="upgradeMagic" action=ArticlesOnPlayerModify.jsp>
<input type='hidden' name='action' value='upgradeMagic'>
<input type='hidden' name='playerid' value='<%=playerId %>'>
<input type="hidden" name="upgradeMagicValues" id="upgradeMagicValues">
<input type="hidden" name="upgradeMagicChongValues" id="upgradeMagicChongValues">
<input type="hidden" name="cellIndex7" id="cellIndex7">
</form>
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
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){ %>


<%} %>

	<table class="middlerighttableclass" style="background-color: #DCE0EB;">
		<tr >
			<td>添加物品</td><td class="titlecolor" colspan="2">当前装备栏(<%=player.getCurrentSuit() %>)</td>
		</tr>
		<%
		long[] ids = ec.getEquipmentIds();
		for(int i = 0; i < ids.length; i++){
		%>
		<tr >
		<%
			if(i == 0){
				%>
				<td rowspan="<%=ids.length %>">
				<table>
				<tr><td>所有&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:
		<select name="article8" id="article8" style="width:110px" >
		<%for(Article w: articleList){ %>
		<option id="<%=w.getName() %>" ><%=w.getName() %></option>
		<% if(TestServerConfigManager.isTestServer())break;}%>
		</select>
	<input type="button" value="增加" onclick="javascript:onClickAddArticle(8);"></td></tr>
				<tr><td>自己填写&nbsp;&nbsp;:
		<input type="text" name="articlename" id="articlename" style="width:110px">
	<input type="button" value="增加" onclick="javascript:onClickAddArticle(15);"></td></tr>
				<tr><td>增加个数&nbsp;&nbsp;:
	<input type="text" name="count" id="count" value="1" style="width:110px"></td></tr>
				<tr><td>颜色数值&nbsp;&nbsp;:
	<input type="text" name="colorType" id="colorType" value="0" style="width:110px"></td></tr>
				</table>
				</td>
				<td rowspan="<%=ids.length %>">
				<table>
				<tr><td>账号:<%=player.getUsername() %></td></tr>
				<tr><td>职业:<%=CareerManager.careerNames[player.getCareer()] %></td></tr>
				<tr><td>当前元神等级:<%=player.getCurrSoul().getGrade() %></td></tr>
				<tr><td>血量:<%=(player.getHp()+"/"+player.getMaxHP()) %></td></tr>
				<tr><td>物攻强:<%=player.getPhyAttack() %></td></tr>
				<tr><td>法攻强:<%=player.getMagicAttack() %></td></tr>
				<tr><td>速度:<%=player.getSpeed() %></td></tr>
				<tr><td>绑银:<%=BillingCenter.得到带单位的银两(player.getBindSilver()) %></td></tr>
				<tr><td>银子:<%=BillingCenter.得到带单位的银两(player.getSilver()) %></td></tr>
				<tr><td>最高水位银子:<%=BillingCenter.得到带单位的银两(player.getHighWaterOfSilver()) %></td></tr>
				</table>
				</td>
				<%
			}
			%>
			<td >
			<%
			if(ids[i] > 0){
				
				ArticleEntity ee = aem.getEntity(ids[i]);
				if(ee != null){
					String equipmentName = ee.getArticleName();
					if(am.getArticle(equipmentName) instanceof Equipment){
						Equipment equipment = (Equipment)am.getArticle(equipmentName);
						out.println(Equipment.equipmentTypeDesp[equipment.getEquipmentType()]);
						out.println(ee.getId());
						out.println("<BR/>");
						
						%>
						<img id="0<%=i %>" onmouseover="javascript:onClickValueChange1('<%=ee.getId() %>','0<%=i %>');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=equipment.getIconId() %>" ondblclick="javascript:doubleClickUnUse('<%=i %>');">&nbsp;<input type="button" style="width:70px" value="<%=equipment.getName() %>" onclick="javascript:doubleClickUnUse('<%=i %>');">
						<%
					}else if (am.getArticle(equipmentName) instanceof MagicWeapon) {
						MagicWeapon mw = (MagicWeapon)am.getArticle(equipmentName);
						out.println("16法宝");
						out.println("颜色:"+ee.getColorType());
						%>
						<img id="0<%=i %>" onmouseover="javascript:onClickValueChange1('<%=ee.getId() %>','0<%=i %>');" onmouseout="javascript:divHidden();" width="16" height="16" src="/imageServlet?id=<%=mw.getIconId() %>" ondblclick="javascript:doubleClickUnUse('<%=i %>');">&nbsp;<input type="button" style="width:70px" value="<%=mw.getName() %>" onclick="javascript:doubleClickUnUse('<%=i %>');">
						<%
					} else{
						%>
						类型错误，非装备物品放入了装备栏
						<%
					}
				}else{
					out.println(Equipment.equipmentTypeDesp[i]);
					out.println(ids[i]);
				}
			}else{
				out.println(Equipment.equipmentTypeDesp[i]);
			}
			%>
			</td>
			<%
			if(i == 0){
				%>
				<td rowspan="<%=ids.length %>"><div id="equipmentDes">当前装备描述</div></td>
				<%
			}
			%>
		</tr>
		<%
			}
		%>
	
	</table>

<%
Knapsack[] knapsacks = player.getKnapsacks_common();
for(int knapsackIndex = 0; knapsackIndex < knapsacks.length; knapsackIndex++){
	Knapsack knapsack = knapsacks[knapsackIndex];
		if(knapsack != null && knapsack.getCells() != null){
		
		%>
			<table class="middlerighttableclass" style="background-color: #DCE0EB;">
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
							Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
				%>
					<td >
					<input type="radio" value="<%=knapsackIndex+"0"+(i+j) %>" name="articleRadio" id="articleRadio">
					<input type="button" style="width:80px;background-color:<%=a==null?"red":"" %>"  value="<%=ae.getArticleName() + "," + ae.getClass().toString() %><%=cell.getCount()==1?"":"("+cell.getCount()+")" %>" onclick="javascript:doubleClickUseByButton(<%=knapsackIndex+"0"+(i+j) %>);">
					<img width="16" height="16" src="/imageServlet?id=<%=am.getArticle(ae.getArticleName())== null?"":am.getArticle(ae.getArticleName()).getIconId()%>" onmouseup="javascript:onMouseUpFunction(<%=knapsackIndex %>,<%=(i+j) %>);" ondblclick="javascript:doubleClickUse(<%=knapsackIndex+"0"+(i+j) %>);" id="<%=knapsackIndex+"0"+(i+j) %>" onmouseover="javascript:onClickValueChange('<%=ae.getId() %>','<%=knapsackIndex+"0"+(i+j) %>');" onmouseout="javascript:divHidden();"><%=cell.getEntityId() %></td>
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
			<table class="middlerighttableclass" style="background-color: #DCE0EB;">
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
		<table class="middlerighttableclass" style="background-color: #DCE0EB;">
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

<%}
%>

</body>

<%@page import="com.fy.engineserver.enterlimit.EnterLimitManager"%></html>
