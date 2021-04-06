<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>

<%@page import="com.fy.engineserver.sprite.pet.*"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%>
<%@page import="com.fy.engineserver.playerTitles.*"%>
<%@page import="com.fy.engineserver.sprite.concrete.*"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitle"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.sprite.horse.dateUtil.DateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修改到期时间</title>
</head>
<body>

	<%
	
		HorseManager hm = HorseManager.getInstance();
		List<Horse> list = 	hm.em.query(Horse.class,"","",1,5000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for(Horse h : list){
			out.print(h.getHorseName());
			if(h.isLimitTime()){
				h.setDueTime(h.get到期时间());
				out.print(h.get到期时间());
				out.print(sdf.format(new Date(h.get到期时间()))+"<br/>");
			}
		}
	
	
	%>

</body>

</html>
