<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String articleName = request.getParameter("article");
	articleName = URLDecoder.decode(articleName,"UTF-8");
	int articleNum = Integer.parseInt(request.getParameter("num"));
	int colorType = Integer.parseInt(request.getParameter("color"));
	boolean isBind = Integer.parseInt(request.getParameter("binded")) == 1 ;
	String txBig = request.getParameter("txBig");		//判断是否为腾讯端口
	String gmUser = request.getParameter("gmUserName");
	gmUser = URLDecoder.decode(gmUser,"UTF-8");
	String reason = request.getParameter("reason");
	reason = URLDecoder.decode(reason,"UTF-8");
	String mailTitle = request.getParameter("mailTitle"); 
	mailTitle = URLDecoder.decode(mailTitle,"UTF-8");
	String mailContent = request.getParameter("mailContent");
	mailContent = URLDecoder.decode(mailContent,"UTF-8");
	Map<String, String> result = new HashMap<String, String>();
	try {
		Player player = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
		} catch (Exception e) {
			
		}
		if (player == null) {
			result.put("result","角色不存在");
		} else {
			Article a = ArticleManager.getInstance().getArticle(articleName);
			if (a == null) {
				result.put("result","物品不存在");
			} else {
				if(!a.isOverlap()){
					ArticleEntity [] as = new ArticleEntity[articleNum];
					StringBuffer ids = new StringBuffer();
					StringBuffer names = new StringBuffer();
					StringBuffer colors = new StringBuffer();
					for(int i=0;i<articleNum;i++){
						ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, isBind, ArticleEntityManager.CREATE_REASON_GMSEND, player, colorType, articleNum, true);
						as[i] = ae;
						ids.append(ae.getId()).append(",");
						names.append(ae.getArticleName()).append(",");
						colors.append(ae.getColorType()).append(",");
					}
					MailManager.getInstance().sendMail(player.getId(), as, new int[] { articleNum }, mailTitle, mailContent, 0L, 0L, 0L, reason);
					result.put("result","success");
					ArticleManager.logger.warn("[gm补发物品] [成功] [gmUse:" + gmUser + "] [玩家:" + player.getLogString() + "] [发送物品id:" + ids + "] [物品名:" + names + "] [颜色:" + colors + "] [个数:" + articleNum + "] [是否绑定:" + isBind + "]");
				}else{
					ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(a, isBind, ArticleEntityManager.CREATE_REASON_GMSEND, player, colorType, articleNum, true);
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] {ae}, new int[] { articleNum }, mailTitle, mailContent, 0L, 0L, 0L, reason);
					result.put("result","success");
					ArticleManager.logger.warn("[gm补发物品] [成功] [gmUse:" + gmUser + "] [玩家:" + player.getLogString() + "] [发送物品id:" + ae.getId() + "] [物品名:" + ae.getArticleName() + "] [颜色:" + ae.getColorType() + "] [个数:" + articleNum + "] [是否绑定:" + isBind + "]");
				}
			}
		}
	} catch (Exception e) {
		ArticleManager.logger.warn("error:[补发物品失败] [playerId:" + playerId + "] [articleName:" + articleName + "] [gmUser:" + gmUser + "]", e);
		//out.print("error:[补发物品失败] [playerId:" + playerId + "] [articleName:" + articleName + "] [gmUser:" + gmUser + "]" + e.getMessage());
		result.put("result","页面出现异常" + e.getMessage());		
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>