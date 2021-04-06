<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.Mail"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.util.StringTool,com.fy.engineserver.event.EventRouter"%>
<%@page import="org.apache.poi.hssf.usermodel.*"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.*"%>
<%@page import="org.apache.poi.hssf.model.Workbook"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager,com.mieshi.gamebase.monitor.ServerMonitor"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
out.print("触发事件个数"+EventRouter.getInst().addEventCnt);
out.print("<br/>");
out.print("处理事件个数"+EventRouter.getInst().procEventCnt);
//com.mieshi.gamebase.monitor.ServerMonitor.getInst().dumpServerInfo(out);
%>
</body>
</html>
