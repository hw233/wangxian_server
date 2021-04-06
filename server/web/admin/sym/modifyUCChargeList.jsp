<%@page import="com.fy.engineserver.economic.charge.ChargeMoneyConfigure"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeMode"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.shop.Goods"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.datasource.skill.ActiveSkill"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	Class cl = ChargeManager.class;
	Field f = cl.getDeclaredField("channelChargeModes");
	f.setAccessible(true);
	HashMap<String, List<ChargeMode>> channelChargeModes = (HashMap<String, List<ChargeMode>>)f.get(ChargeManager.getInstance());
	for(String key : channelChargeModes.keySet()){
		if(key.equals("UCNEWSDK_MIESHI")){
			List<ChargeMode> list = channelChargeModes.get(key);
			for(ChargeMode mode : list){
				out.print(Arrays.toString(mode.getMoneyConfigureIds())+"<br>");
				String [] ss = new String[]{"sq5","sq10","sq20","sq30","sq50","sq100","sq200","sq300","sq500","sq1000","sq1500","sq2000"};
				mode.setMoneyConfigureIds(ss);
				for (int ii=0; ii<ss.length; ii++) {
					ChargeMoneyConfigure chargeMoneyConfigure = ChargeManager.getInstance().getChargeMoneyConfigures().get(ss[ii]);
					mode.getMoneyConfigures().add(chargeMoneyConfigure);
					chargeMoneyConfigure.getChargeMode().add(mode);
				}
				out.print(Arrays.toString(mode.getMoneyConfigureIds())+"<br>");
			}
		}
		
	}
	f.set(ChargeManager.getInstance(), channelChargeModes);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>