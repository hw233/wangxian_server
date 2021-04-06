<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page
	import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.datasource.skill.ActiveSkill"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.jsp.propertyvalue.SkillField"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.jsp.propertyvalue.SkillTemplate"%>
<%@page import="com.fy.engineserver.jsp.propertyvalue.SkillManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	Skill[] skills = CareerManager.getInstance().getSkills();
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table>
		<%
			for (Skill skill : skills) {
				if (skill instanceof ActiveSkill) {
					ActiveSkill ak = (ActiveSkill) skill;
					if (ak.getName().equals("고급 광도")) {
						ak.setBuffName("고급 벌레 그룹");
					}
					String buffName = ak.getBuffName();
					if (!"".equals(buffName)) {
						BuffTemplateManager btm = BuffTemplateManager.getInstance();
						BuffTemplate bt = btm.getBuffTemplateByName(buffName);
						if (bt == null) {
							out.print("技能:[" + ak.getClass().getSimpleName() + "/" + ak.getName() + "] [buff不存在:" + buffName + "]<br/>");
						}
					}
				}
			}
		%>
	</table>
</body>
</html>