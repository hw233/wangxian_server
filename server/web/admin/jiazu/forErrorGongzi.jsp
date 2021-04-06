<%@page import="com.fy.engineserver.septstation.SeptStationInfo"%>
<%@page
	import="com.fy.engineserver.septbuilding.templet.SeptBuildingTemplet.BuildingType"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String jiazuName = request.getParameter("jiazuName");
	if (jiazuName != null) {
		Jiazu jiazu = JiazuManager.getInstance().getJiazu(jiazuName);
		if (jiazu != null) {
			Set<JiazuMember> members = JiazuManager.getInstance().getJiazuMember(jiazu.getJiazuID());
			if (members != null) {
				boolean needSend = jiazu.getSingleMemberSalaryMax() < 0;;
				/*for (JiazuMember member : members) {
					if (member.getCurrentSalary() > 0) {
						needSend = false;
						break;
					}
				}*/
				if (needSend) {
					for (JiazuMember member : members) {
						if (member.getCurrentSalary() > 0) {
							needSend = false;
							break;
						}
					}
					//DOSEND
					{
						SeptStation station = SeptStationManager.getInstance().getSeptStationById(jiazu.getBaseID());
						if (station == null) {
							out.print("[家族" + jiazuName + "没有驻地]");
							return;
						}
						int level = station.getBuildingLevel(BuildingType.聚仙殿);
						station.initInfo();
						SeptStationInfo info = station.getInfo();
						/** 家族总工资 */
						long baseSumSalary = 5000000 * 1L * info.getMaxMember() * level / 10;
						out.print("baseSumSalary" + baseSumSalary + "<BR/>");
						/** 建筑等级总和 */
						long sumlevel = station.getAllbuildingTotalLevel();
						out.print("sumlevel" + sumlevel + "<BR/>");
						/** 建筑加成 */
						double buildingAdd = ((double) sumlevel) / 90;
						out.print("buildingAdd" + buildingAdd + "<BR/>");
						/** 实际总工资 */
						int realSumSalary = (int) ((1 + buildingAdd) * baseSumSalary);
						out.print("realSumSalary" + realSumSalary + "<BR/>");
						jiazu.setSalarySum(realSumSalary);
						jiazu.setSalaryLeft(realSumSalary);

						out.print("setSalaryLeft" + realSumSalary + "<BR/>");
						// 个人工资上限
						jiazu.setSingleMemberSalaryMax(jiazu.getSalarySum() / info.getMaxMember());
						out.print("getSingleMemberSalaryMax" + jiazu.getSingleMemberSalaryMax() + "<BR/>");
						out.print("<BR/>");
					}
				}
			}
		} else {
			out.print("家族[" + jiazuName + "]不存在<BR/>");
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="./forErrorGongzi.jsp">
		<input type="text" name="jiazuName" value="<%=jiazuName%>"> <input
			type="submit" value="ok">
	</form>
</body>
</html>