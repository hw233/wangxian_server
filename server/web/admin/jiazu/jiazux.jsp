<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember4Client"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="java.util.Set"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String jiazuName = request.getParameter("jiazuName");
	if (jiazuName != null && !"".equals(jiazuName)) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuName);
		if (jiazu == null) {
			out.print("家族不存在:" + jiazuName + "<BR/>");
		} else {
			SeptStation ss = SeptStationManager.getInstance().getSeptStationBySeptId(jiazu.getJiazuID());
			if (ss == null) {
				out.print("家族:" + jiazuName + "还没有驻地<BR/>");
			} else {
				ss.initInfo();
				jiazu.initMember4Client();
				out.print("[--------------------------]" + "<BR/>");
				out.print("[家族成员]" + JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID()).size() + "<BR/>");
				out.print("[家族成员4client]" + jiazu.getMember4Clients().size() + "<BR/>");
				jiazu.calculateSalary();
				out.print("工资计算完毕");
				{
					Set<JiazuMember> mems = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
					for (JiazuMember jm : mems) {
						//jm.setCurrentWeekContribution(300);
					}
					
				}
				{
					//工资情况
					Set<JiazuMember> memList = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
					for (JiazuMember mem : memList) {
						PlayerSimpleInfo ps = PlayerSimpleInfoManager.getInstance().getInfoById(mem.getPlayerID());
						out.print("[成员:" + mem.getPlayerID() + "] [ 职务:" + mem.getTitle() + "][工资:" + mem.getJiazuSalary() + "]" + ps.getName());
					}
					out.print("<HR>");
					out.print("<HR>");
					jiazu.initMember4Client();
					for (JiazuMember4Client jmc : jiazu.getMember4Clients()) {
						out.print(jmc.toString() + "<BR/>");
					}
					out.print("<HR>");
					out.print("<HR>");
				}
			}
		}
	}
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./jiazux.jsp" method="post">
		家族名字:<input name="jiazuName" value="<%=jiazuName%>">
		<input name="OK" value="计算工资" type="submit">
	</form>
</body>
</html>