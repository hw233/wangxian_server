<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>


<%@page import="com.fy.boss.authorize.model.Passport"%>
<%@page import="com.fy.boss.authorize.service.PassportManager"%>
<%@page import="com.fy.boss.finance.model.OrderForm"%>
<%@page import="com.fy.boss.finance.service.OrderFormManager"%>
<%@page import="com.fy.boss.authorize.dao.PassportDAO"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%><head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
String conform = request.getParameter("confor");
if(conform != null && conform.equals("noblacknotestuser")){
	String usernames [] = {"wtx063","wtx061","wtx062"};
	for(String name : usernames){
		Passport p = getPassport(name);
		if(p == null){
			out.print("账号"+name+"不存在!<br>");
			continue;
		}
		p.setPassWd(StringUtil.hash("123456"));
		em.notifyFieldChange(p, "passWd");
		out.print(name+"<br>");
	}
}else{
	out.print("hello");
}

%>

<%!
PassportDAO passportDAO = PassportManager.getInstance().getPassportDAO();
public Passport getPassport(String username) {
	Passport passport = null;
	try {
		passport = getPassportByUserName(username);
	} catch (Exception e) {
	}
	return passport;
}
SimpleEntityManager<Passport> em = SimpleEntityManagerFactory.getSimpleEntityManager(PassportDAO.pojoClass);
public Passport getPassportByUserName(String userName) throws Exception
{
	Passport p = null;
	try {
		long[] ids = em.queryIds(PassportDAO.pojoClass, " userName = '" + userName + "' or nickName = '" + userName + "'");
		p = em.find(ids[0]);
		return p;
	} catch (Exception e) {
		return null;
	}
}

%>
</body>

