<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.concrete.DefaultDiskCache"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>将大师技能恢复</title>
</head>
<body>
<%
	//循环合服的所有数据库schema 然后将没有被合入的数据 插入到ddc中 然后将ddc上传到被合服的游戏服
	boolean isAdd = ParamUtils.getBooleanParameter(request, "isAdd");
	String mainConf = ParamUtils.getParameter(request, "mainConf", "");
	String otherConfs = ParamUtils.getParameter(request, "otherConfs", "");

	if(isAdd)
	{
		Class destClass =  SkBean.class;
		//获取主数据库的simpleentity
		SimpleEntityManager<SkBean> mainSimpleManager = new  DefaultSimpleEntityManagerFactory(mainConf).getSimpleEntityManager(destClass);
		
		String[] others = otherConfs.split("\r*\n");
		for(String confPath : others)
		{
			DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(confPath);
			SimpleEntityManager<SkBean> em = factory.getSimpleEntityManager(SkBean.class);
			
			long ids[] = em.queryIds(destClass, " point <> 0 ");
			
			for(long id :  ids)
			{
				SkBean mainSkBean = mainSimpleManager.find(id);
				if(mainSkBean != null)
				{
					SkBean skBean =	em.find(id);
					if(skBean == null)
					{
						continue;
					}
					
					if(mainSkBean.getPoint() == 0 || mainSkBean.getPoint() != skBean.getPoint() || mainSkBean.getReturnPointNum() != skBean.getReturnPointNum())
					{
						if(skBean != null)
						{
							mainSkBean.setLevels(skBean.getLevels());
							mainSimpleManager.notifyFieldChange(mainSkBean, "levels");
							mainSkBean.setPoint(skBean.getPoint());
							mainSimpleManager.notifyFieldChange(mainSkBean, "point");
							mainSkBean.setExchangeTimes(skBean.getExchangeTimes());
							mainSimpleManager.notifyFieldChange(mainSkBean, "exchangeTimes");
							mainSkBean.setLastExchangeDay(skBean.getLastExchangeDay());
							mainSimpleManager.notifyFieldChange(mainSkBean, "lastExchangeDay");
							mainSkBean.setSoulLevels(skBean.getSoulLevels());
							mainSimpleManager.notifyFieldChange(mainSkBean, "soulLevels");
							mainSkBean.setReturnPointNum(skBean.getReturnPointNum());
							mainSimpleManager.notifyFieldChange(mainSkBean, "returnPointNum");
							
							mainSimpleManager.save(mainSkBean);
							out.println("[修补大师技能] ["+id+"] ["+mainSkBean.getPoint()+"] ["+skBean.getPoint()+"] ["+mainSkBean.getReturnPointNum()+"] ["+skBean.getReturnPointNum()+"]<br/>");
						}
			/* 			else
						{
							mainSkBean = new SkBean();
							mainSkBean.setLevels(skBean.getLevels());
							mainSimpleManager.notifyFieldChange(mainSkBean, "levels");
							mainSkBean.setPoint(skBean.getPoint());
							mainSimpleManager.notifyFieldChange(mainSkBean, "point");
							mainSkBean.setExchangeTimes(skBean.getExchangeTimes());
							mainSimpleManager.notifyFieldChange(mainSkBean, "exchangeTimes");
							mainSkBean.setLastExchangeDay(skBean.getLastExchangeDay());
							mainSimpleManager.notifyFieldChange(mainSkBean, "lastExchangeDay");
							mainSkBean.setSoulLevels(skBean.getSoulLevels());
							mainSimpleManager.notifyFieldChange(mainSkBean, "soulLevels");
							mainSkBean.setPid(skBean.getPid());
							mainSimpleManager.notifyFieldChange(mainSkBean, "pid");
						}  */
					
					}
					else
					{
						out.println("[修补大师技能] [不用修改] ["+id+"]<br/>");
					}
				}
				else
				{
					out.println("[修补大师技能] [没有找到对应id] ["+id+"]<br/>");	
				}
			}
			
					
		}
	}



%>


<form method="post">
<input type="hidden"  name="isAdd" value="true" /><br/>
主数据库配置文件:<input type="text" name="mainConf" value=<%=mainConf %> /><br/>
副数据库配置文件:<textarea cols="50" rows="50"  name="otherConfs"><%=otherConfs %></textarea>(格式:远程数据库用户名 本地配置的配置文件地址)<br/>
<input type="submit" value="提交" />
</form>
</body>
</html>