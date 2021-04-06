<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_addDamage"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_ZengShu"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_JiangDiZhiLiao"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate_Silence"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	BuffTemplate_Silence bs = new BuffTemplate_Silence();	
	bs.setId(1813);
	bs.setName("沉默说话");
	out.print("添加前，"+BuffTemplateManager.getInstance().getTemplates().size());
	BuffTemplateManager.getInstance().getTemplates().put(bs.getName(), bs);
	
	BuffTemplate_JiangDiZhiLiao bj = new BuffTemplate_JiangDiZhiLiao();
	bj.setTreatment(new int[]{30,40,50,0,0,0,0,0,0,0,0,0});
	bj.setName("降低治疗");
	bj.setId(1800);
	BuffTemplateManager.getInstance().getTemplates().put(bj.getName(), bj);
	
	BuffTemplate_ZengShu bz = new BuffTemplate_ZengShu();
	bz.setSpeed(new int[]{10,20,30,40,50,60,70,80,90,100,21,23,25,27,29,31,33,35,37,39,41,43,45,47});
	bz.setId(1811);
	bz.setName("疾 风");
	BuffTemplateManager.getInstance().getTemplates().put(bz.getName(), bz);
	
	BuffTemplate_addDamage ba = new BuffTemplate_addDamage();
	ba.setDamage(new int[]{5,10,15,20,25,30,35,40,45,50,60,70,80,90,100,21,23,25,27,29,31,33,35,37,39,41,43,45,47,49,51});
	ba.setId(1812);
	ba.setName("伤害提升");
	BuffTemplateManager.getInstance().getTemplates().put(ba.getName(), ba);
	
	out.print("添加后，"+BuffTemplateManager.getInstance().getTemplates().size());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加bufftemplate</title>
</head>
<body>

</body>
</html>