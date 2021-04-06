<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.economic.BillingCenter"%>
<%@page
	import="com.fy.engineserver.septstation.service.SeptStationManager"%>
<%@page import="com.fy.engineserver.septstation.SeptStation"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	JiazuManager jiazuManager = JiazuManager.getInstance();
	Map<Long, Jiazu> jiazuMap = jiazuManager.getJiazuLruCacheByID();
	int[] countryNum = new int[4];
	List<Jiazu> jiazuList = new ArrayList<Jiazu>();
	for (Iterator<Long> itor = jiazuMap.keySet().iterator(); itor.hasNext();) {
		jiazuList.add(jiazuMap.get(itor.next()));
	}
	Collections.sort(jiazuList, order);
%>
<%!Comparator<Jiazu> order = new Comparator<Jiazu>() {
		public int compare(Jiazu o1, Jiazu o2) {
			return o2.getLevel() - o1.getLevel();
		}
	};%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<table style="font-size: 12px; text-align: right;" border="1">
		<tr style="background-color: #81B4E7">
			<td>ID</td>
			<td>名字</td>
			<td>国家</td>
			<td>等级</td>
			<td>族长</td>
			<td>维护费</td>
			<td>家族资金</td>
			<td>家族灵脉</td>
			<td>当前人数/总人数</td>
			<td>繁荣度</td>
		</tr>
		<%
			//			for (Iterator<Long> itor = jiazuMap.keySet().iterator(); itor.hasNext();) {
			StringBuffer sbf = new StringBuffer();
			for (Jiazu jiazu : jiazuList) {
				Long jiazuId = jiazu.getJiazuID();
				countryNum[jiazu.getCountry()] += 1;
				if (jiazu != null) {
					SeptStation septStation = SeptStationManager.getInstance().getSeptStationBySeptId(jiazuId);
					boolean hasSeptstation = septStation != null;
					if (hasSeptstation) {
						if (jiazu.getLevel() != septStation.getMainBuildingLevel() && jiazu.getLevel() > 0) {
							sbf.append("等级不符:").append(jiazu.getName()).append("[家族等级" + jiazu.getLevel() + "][驻地等级:" + septStation.getMainBuildingLevel() + "]").append("</BR>");
							jiazu.setLevel(septStation.getMainBuildingLevel());
						}
						if (septStation.getJiazu() == null) {
							septStation.setJiazu(jiazu);
							sbf.append("[驻地恢复:"+jiazu.getName()+"]");
						}
					}
		%>
		<tr>
			<td><a href="./jiazu.jsp?jiazuId=<%=jiazu.getJiazuID()%>"><%=jiazu.getJiazuID()%></a>
			</td>
			<td><%=jiazu.getName()%></td>
			<td><%=CountryManager.得到国家名(jiazu.getCountry())%></td>
			<td><%=jiazu.getLevel()%></td>
			<td><%=jiazu.getJiazuMaster()%></td>
			<td><%=hasSeptstation ? septStation.getInfo().getCurrMaintai() : "--"%></td>
			<td><%=BillingCenter.得到带单位的银两(jiazu.getJiazuMoney())%></td>
			<td><%=BillingCenter.得到带单位的银两(jiazu.getConstructionConsume())%></td>
			<td><%=jiazu.getMemberNum() + "/" + jiazu.maxMember()%></td>
			<td><%=jiazu.getProsperity()%></td>
		</tr>
		<%
			} else {
					out.print("[存在NULL的家族:" + jiazuId + "]");
				}
			}
			out.print(sbf.toString());
		%>
		<%
			out.print("<br>[系统中家族个数:" + jiazuMap.size() + "]");
			//out.print("[系统中家族个数:" + jiazuMap.size() + "]");
			for (int i = 1; i < countryNum.length; i++) {
				out.print(CountryManager.国家名称[i - 1] + ":" + countryNum[i] + "个.");
			}
		%>
	</table>
</body>
</html>