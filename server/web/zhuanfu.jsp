<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.uniteserver.LocalPlayer"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
<%
SimpleEntityManager<Player> em = ((GamePlayerManager)GamePlayerManager.getInstance()).em;
try {
	long[] list = em.queryIds(Player.class, " id > 0");
	for(long id : list){
		Player p = em.find(id);
		p.setName(p.getName()+"@"+"天道之地");
		em.flush(p);
		out.print(p.getName()+"<br>");
	}
} catch (Exception e) {
	out.print("[查询player简单对象] [异常]"+ e);
}
out.print("<hr>");
SimpleEntityManager<Jiazu> jiazuem = JiazuManager.jiazuEm;
long count = jiazuem.count();
List<Jiazu> jiazus = jiazuem.query(Jiazu.class, "", "", 1, count);
for(Jiazu jz : jiazus){
	jz.setName(jz.getName()+"@"+"天道之地");
	jiazuem.flush(jz);
	out.print(jz.getName()+"<br>");
}
out.print("<hr>");
SimpleEntityManager<ZongPai> zpem = ZongPaiManager.em;
long count2 = zpem.count();
List<ZongPai> zongPais = zpem.query(ZongPai.class, "", "", 1, count2);
for(ZongPai zp : zongPais){
	zp.setZpname(zp.getZpname() + "@" + "天道之地");
	out.print(zp.getZpname()+"<br>");
}
%>
