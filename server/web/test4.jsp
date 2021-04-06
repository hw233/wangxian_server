<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.PlayerManager"%><%@page import="com.fy.engineserver.sprite.horse.Horse"%><%@page import="java.util.Arrays"%><%@page import="java.util.HashMap"%><%String l="古董"; %><%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.xuanzhi.tools.servlet.HttpUtils"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="java.util.List"%>
<%@page import="cn.uc.g.sdk.cp.model.gamedata.GameData"%>
<%@page import="java.util.ArrayList"%>
<%@page import="cn.uc.g.sdk.cp.model.gamedata.LoginGameRole"%>
<%@page import="cn.uc.g.sdk.cp.constant.GameDataCategory"%>
<%@page import="cn.uc.g.sdk.cp.service.SDKServerService"%>
<%@page import="cn.uc.g.sdk.cp.model.SDKException"%><html xmlns="http://www.w3.org/1999/xhtml"><%!
String queryUrl = "http://collect.sdkyy.9game.cn:8080/ng/cpserver/gamedata/ucid.game.gameData";

public void sendPlayerInfoToUC(Player p , int type){
	if(!p.getUsername().startsWith("UCSDKUSER_")){
		return;
	}
	String uid = p.getUsername().split("UCSDKUSER_")[1];
	if(uid == null || uid.isEmpty()){
		return;
	}

    String accountId = uid;
    
    //最终拼接的游戏数据对象
    List<GameData> gameDataList = new ArrayList<GameData>();
    long startTime = SystemTime.currentTimeMillis();
/**
* 必接功能<br>
* 提交游戏扩展数据功能，游戏SDK要求游戏在运行过程中，提交一些用于运营需要的扩展数据，这些数据通过扩展数据提交方法进行提交。
* 登录游戏角色成功后调用，及角色等级变化后调用
* 游戏内如果没有相应的字段：int传-1，string传"不存在"
*/
    //玩家的游戏数据
    LoginGameRole loginGameRole = new LoginGameRole();
    loginGameRole.setRoleId(p.getId()+"");
    loginGameRole.setRoleLevel(p.getLevel()+"");
    loginGameRole.setRoleName(p.getName()+"");
    loginGameRole.setZoneId("不存在");
    loginGameRole.setZoneName(GameConstants.getInstance().getServerName());
    loginGameRole.setRoleCTime(startTime/1000);
    loginGameRole.setOs("android");
    loginGameRole.setRoleLevelMTime(startTime/1000);

    //构造玩家的游戏数据对象,构建数据后需要调SDKServerService.gameData接口提交数据,详见文档最后
    GameData roleData = new GameData(GameDataCategory.LOGIN_GAME_ROLE, loginGameRole);
    gameDataList.add(roleData);
    
/**
* ===========================
* 构造完数据后，需要调用SDKServerService.gameData(accountId, gameDataList)方法，才会将数据上传到服务器
* ===========================
*/
    try {
        boolean result = SDKServerService.gameData(accountId, gameDataList);
        System.out.println("result:"+result);
    } catch (SDKException e) {
        System.err.println(e.getErrorCode() + "--" + e.getMessage());
    }
	
}

%>
<%
Player p = PlayerManager.getInstance().getPlayer("哦哦");
sendPlayerInfoToUC(p,1);
%>
