<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnConf"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%
//修正骨印，妖印的描述。
SkEnConf c = SkEnConf.conf[4 - 1][0];
out.println(ArrayUtils.toString(c.desc));
c.desc[0] = "人阶10重:获得永久提升自身3%内防";
c.desc[1] = "地阶10重:获得永久提升自身4%内防";
c.desc[2] = "天阶10重:获得永久提升自身5%内防";
/*
*/

out.println("<br/>");

c = SkEnConf.conf[4 - 1][3];
out.println(ArrayUtils.toString(c.desc));
c.desc[0] = "人阶10重:获得永久提升自身1%法攻";
c.desc[1] = "地阶10重:获得永久提升自身2%法攻";
c.desc[2] = "天阶10重:获得永久提升自身3%法攻";
/*
*/
%>