<%@page
	import="com.fy.engineserver.uniteserver.UnitServerFunctionManager"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page
	import="com.fy.engineserver.menu.peoplesearch.Option_PeopleSearch_Info"%>
<%@page
	import="com.fy.engineserver.menu.peoplesearch.Option_Accept_PeopleSearch"%>
<%@page import="com.fy.engineserver.menu.Option"%>
<%@page import="com.fy.engineserver.menu.MenuWindow"%>
<%@page import="com.fy.engineserver.menu.WindowManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	//UnitServerFunctionManager.cfgs.clear();
	MenuWindow[] mws = WindowManager.getInstance().getWindows();
	LinkedHashMap<Integer, MenuWindow> windowMap = WindowManager.getInstance().getWindowMap();
	MenuWindow mw1 = windowMap.get(51034);
	Option[] option = mw1.getOptions();
	option = Arrays.copyOf(option, option.length + 1);
	Option_Exchange_Activity option1 = new Option_Exchange_Activity();
	option1.setText("破魔伞本尊兑换");
	option1.setActivityName("破魔伞本尊兑换");
	option[option.length - 1] = option1;
	Option_Exchange_Activity option2 = new Option_Exchange_Activity();
	option2.setText("破魔伞元神兑换");
	option2.setActivityName("破魔伞元神兑换");
	option = Arrays.copyOf(option, option.length + 1);
	option[option.length - 1] = option2;
	mw1.setOptions(option);

	for (Integer id : windowMap.keySet()) {
		if (id == 51034) {
			Option[] options = windowMap.get(id).getOptions();
			for (Option op : options) {
				out.print("OId=" + op.getOId() + ",OptionId==" + op.getOptionId() + ",Text==" + op.getText() + "<br>");
			}
		}
	}
%>

<%@page
	import="com.fy.engineserver.menu.activity.exchange.Option_Exchange_Activity"%>
<%@page import="java.util.Arrays"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>