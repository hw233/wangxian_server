<%@page import="com.fy.boss.platform.duoku.DuokuSavingManager"%>
<%
	DuokuSavingManager.getInstance().chargeUrl = "http://duokoo.baidu.com/openapi/client/duokoo_card_recharge";
	out.print(DuokuSavingManager.getInstance().chargeUrl);
%>