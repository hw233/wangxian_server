<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.newBillboard.date.jiazu.JiazuPowerAllBillboard"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="com.fy.engineserver.newBillboard.date.jiazu.JiazuProsperityALLBillboard"%>
<%@page import="com.fy.engineserver.newBillboard.date.jiazu.JiazuProsperityALLBillboard.JiazuCompare"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>

<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.newBillboard.date.jiazu.JiazuProsperityALLBillboard.JiazuCompareRepair"%><html>
<head>
<title>刷新家族更新繁荣度时间</title>
</head>
<body>
	<p> 把时间按现在的顺序设置成i++，重启后还是现在顺序     </p>
	
	
	<%
	
	String repair = request.getParameter("repair");
	
	if(repair != null && repair.equals("xiugai")){
		
		SimpleEntityManager<Jiazu> em = JiazuManager.jiazuEm;
		long[] ids = em.queryIds(Jiazu.class, " ",new Object[]{},"prosperity desc",1,5000);
		out.print("家族个数:"+ids.length+"<br/>");
		
		List<Jiazu> list = new ArrayList<Jiazu>();
		for(int i =0;i<ids.length;i++){
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(ids[i]);
			if(jiazu != null){
				list.add(jiazu);
			}
		}
		
		Class clasz = Class.forName("com.fy.engineserver.newBillboard.date.jiazu.JiazuProsperityALLBillboard$JiazuCompareRepair");
		
		JiazuProsperityALLBillboard.JiazuCompareRepair jc = (JiazuProsperityALLBillboard.JiazuCompareRepair) clasz.newInstance();
		Collections.sort(list,jc);
		int i= 0;
		for(Jiazu jz : list){
			jz.setUpdateProsperityTime(i++);
			out.print("家族名:"+jz.getName()+"等分:"+jz.getProsperity()+"时间:"+jz.getUpdateProsperityTime()+"<br/>");
		}
		return;
	}else if(repair != null && !repair.equals("")){
		
		SimpleEntityManager<Jiazu> em = JiazuManager.jiazuEm;
		long[] ids = em.queryIds(Jiazu.class, " ",new Object[]{},"prosperity desc",1,5000);
		out.print("家族个数:"+ids.length+"<br/>");
		
		List<Jiazu> list = new ArrayList<Jiazu>();
		for(int i =0;i<ids.length;i++){
			Jiazu jiazu = JiazuManager.getInstance().getJiazu(ids[i]);
			if(jiazu != null){
				list.add(jiazu);
			}
		}
		
		
		Class clasz = Class.forName("com.fy.engineserver.newBillboard.date.jiazu.JiazuProsperityALLBillboard$JiazuCompare");
		
		JiazuProsperityALLBillboard.JiazuCompare jc = (JiazuCompare) clasz.newInstance();
		Collections.sort(list,jc);
		
		for(Jiazu jz : list){
			out.print("家族名:"+jz.getName()+"等分:"+jz.getProsperity()+"时间:"+jz.getUpdateProsperityTime()+"<br/>");
		}
		
		out.print("查看完成");
		
		return;
	}
	
	%>
	
	
	<form action="">
		xiugai:<input type="text" name="repair"/>
		<input type="submit" value="submit"/>
	</form>



</body>
</html>