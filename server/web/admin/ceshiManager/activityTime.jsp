<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.activity.peoplesearch.PeopleSearchManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="">
斩妖除魔<br>
周几开启：<input type="text" name="openDayOfWeek" value="" /> (*英文逗号间隔，1为周天  7为周六)<br>
开启时间：<input type="text" name="openTime" value="" /> （*小时：分钟11:00）<br>
结束时间：<input type="text" name="endTime" value="" /> （*小时：分钟11:30）<br>
<input type="submit" /><br>
</form>
<form action="">
采花大盗<br>
周几开启：<input type="text" name="周几开启" value="" /> (*英文逗号间隔，1为周天  7为周六)<br>
开启时间：<input type="text" name="开启时间" value="" /> （*小时：分钟11:00）<br>
持续时间：<input type="text" name="每次多长时间" value="" /> （*单位分钟）<br>
<input type="submit" />
</form>
<hr>
<%
	String openDayOfWeek = request.getParameter("openDayOfWeek");
	String openTime = request.getParameter("openTime");
	String endTime = request.getParameter("endTime");
	if(openDayOfWeek != null && !openDayOfWeek.equals("")){
		String tempStr[] = openDayOfWeek.split(",");
		int rempInt[] = new int[tempStr.length];
		for(int i=0;i<tempStr.length;i++){
			rempInt[i] = Integer.valueOf(tempStr[i]);
		}
		PeopleSearchManager.getInstance().openDayOfWeek = rempInt;
		out.print("斩妖除魔周几开启设置完毕<br>");
	}
	if(openTime != null && !openTime.equals("")){
		String tempStr[] = openTime.split(":");
		int rempInt[] = new int[tempStr.length];
		for(int i=0;i<tempStr.length;i++){
			rempInt[i] = Integer.valueOf(tempStr[i]);
		}
		PeopleSearchManager.getInstance().openTime = new int[][]{{rempInt[0],rempInt[1]}};
		out.print("斩妖除魔开启时间设置完毕<br>");
	}
	if(endTime != null && !endTime.equals("")){
		String tempStr[] = endTime.split(":");
		int rempInt[] = new int[tempStr.length];
		for(int i=0;i<tempStr.length;i++){
			rempInt[i] = Integer.valueOf(tempStr[i]);
		}
		PeopleSearchManager.getInstance().endTime = new int[][]{{rempInt[0],rempInt[1]}};
		out.print("斩妖除魔结束时间设置完毕<br>");
	}
	String 周几开启 = request.getParameter("周几开启");
	String 开启时间 = request.getParameter("开启时间");
	String 每次多长时间 = request.getParameter("每次多长时间");
	if(周几开启 != null && !周几开启.equals("")){
		String tempStr[] = 周几开启.split(",");
		int rempInt[] = new int[tempStr.length];
		for(int i=0;i<tempStr.length;i++){
			rempInt[i] = Integer.valueOf(tempStr[i]);
		}
		PickFlowerManager.getInstance().周几开启 = rempInt;
		out.print("采花大盗周几开启设置完毕<br>");
	}
	if(开启时间 != null && !开启时间.equals("")){
		String tempStr[] = 开启时间.split(":");
		int rempInt[] = new int[tempStr.length];
		for(int i=0;i<tempStr.length;i++){
			rempInt[i] = Integer.valueOf(tempStr[i]);
		}
		PickFlowerManager.getInstance().开启时间 = new int[][][]{{{rempInt[0],rempInt[1]}}};
		out.print("采花大盗开启时间设置完毕<br>");
	}
	if(每次多长时间 != null && !每次多长时间.equals("")){
		long tempInt = Long.valueOf(每次多长时间);
		PickFlowerManager.getInstance().每次多长时间 = new long[]{tempInt * 60 * 1000};
		out.print("采花大盗持续时间设置完毕<br>");
	}
%>
</body>
</html>