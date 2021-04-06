<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimManager"%>
<%@page import="java.io.File"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.menu.Option_ExchangeSliver_Salary"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	if (SkEnhanceManager.translation.get("houtaijiancerizhi1") != null) {
		out.println("==========此页面已经刷过！！<br>");
		return ;
	}
	SkEnhanceManager.translation.put("houtaijiancerizhi1", "houtaijiancerizhi"); 
	String path = PlayerAimManager.instance.getFileName();
	String ss[] = path.split(File.separator);
	String path2 = "";
	for (int i=0; i<ss.length; i++) {
		if (ss[i].equals("webapps")) {
			break;
		}
		path2 += (File.separator + ss[i]);
	}
	path2 = File.separator + path2 + File.separator + "log" + File.separator + "game_server" + File.separator + "horseManager.log";
	out.println("文件路径:" + path2 + "<br>");
	FileInputStream f = new FileInputStream(path2); 
	InputStreamReader isr = new InputStreamReader(f, "UTF-8"); 
	BufferedReader reader = new BufferedReader(isr);
	String tempStr = null;
	int count = 0;
	int count2 = 0;
	while ((tempStr = reader.readLine()) != null) {
        // 显示行号
        //if (tempStr.contains("获得宠物错误")) {
        if (tempStr.contains("[坐骑初始化穿装备] [失败]")) {
        	System.out.println(tempStr );
        	String[] strs = tempStr.split("\\[");
        	String playerId = strs[7].split("id")[1].split("\\}")[0].replace(":", "");
        	String articleId = strs[8].split(":")[1].split("\\]")[0];
        	ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(articleId));
        	MailManager.getInstance().sendMail(Long.parseLong(playerId), new ArticleEntity[]{ae}, new int[]{1}, Translate.系统, "由于坐骑系统优化，高阶坐骑原装备使用邮件形式返还给您", 0L, 0L, 0L, "新坐骑系统补发");
        	out.println("[后台页面补发坐骑装备] [成功] [plyaerId:" + playerId + "] [articleId:" + articleId + "]<br>");
        	HorseManager.logger.error("[后台页面补发坐骑装备] [成功] [plyaerId:" + playerId + "] [articleId:" + articleId + "]");
        	out.println("需要补发的角色id[" + playerId + "] [物品id:" + articleId + "]<br>"); 
        	count++;
        }
        if (tempStr.contains("2014-06-13 06")) {
        	out.println("!!!!!!!!!<br>");
        	break;
        }
    }
	reader.close();
	out.println("********************总数:" + count + "<br>");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>坐骑装备丢失补发页面</title>
</head>
<body>

</body>
</html>