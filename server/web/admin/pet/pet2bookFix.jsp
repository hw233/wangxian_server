<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ConcurrentModificationException"%>
<%@page import="com.fy.engineserver.gateway.GameSubSystem"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.gateway.GameNetworkFramework"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetSkillProp"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="com.fy.engineserver.newBillboard.date.pet.PetRankData"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.newBillboard.date.pet.PetALLBillboard"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkill"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@page import="com.fy.engineserver.sprite.pet2.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
<%
final Field f = ArticleEntity.class.getDeclaredField("version2");
f.setAccessible(true);
Runnable r = new Runnable(){
	public void run(){
		while(true){
			if(PetGrade.translation.containsKey("stopThreadFixBook111")){
				Pet2Manager.log.warn("停止修正");
				break;
			}
			try{
				Iterator<ArticleEntity> it = DefaultArticleEntityManager.getaTable().values().iterator();	
				while(it.hasNext()){
					ArticleEntity ae = it.next();
					String aeName = ae.getArticleName();
					Article art = ArticleManager.getInstance().getArticle(aeName);
					if(art instanceof PetSkillProp){
						Integer version = null;
						try{
							version = (Integer)f.get(ae);
						}catch(Exception e){
							Pet2Manager.log.error("修正技能书错误:",e);
							break;
						}
						if(version == 0){
							ArticleEntityManager.getInstance().em.notifyNewObject(ae);
							Pet2Manager.log.warn("修正未存库的技能书 {} {}", ae.getId(), aeName);
						}
					}
				}
			}catch(ConcurrentModificationException mce){
				Pet2Manager.log.warn("并发修改。"+mce);
			}
			try{
				Thread.sleep(10*1000);
			}catch(Exception e){}
		}
	}
};
if(PetGrade.translation.containsKey("pet2bookFix.jsp_started")){
	out.println("线程已经在运行");
}else{
	new Thread(r, "修正宠物技能书抽取").start();
	PetGrade.translation.put("pet2bookFix.jsp_started","1");
}
///////////////////
/**
Field mf = GameNetworkFramework.class.getDeclaredField("message2SubSysMap");
mf.setAccessible(true);
HashMap<String, GameSubSystem[]> message2SubSysMap = (HashMap<String, GameSubSystem[]>)mf.get(GameNetworkFramework.getInstance());
GameSubSystem[] sys = message2SubSysMap.get("PET2_LIAN_DAN_REQ");
out.println(sys[0].getClass().getSimpleName());
*/
%>
<br/>完成
</BODY></html>
