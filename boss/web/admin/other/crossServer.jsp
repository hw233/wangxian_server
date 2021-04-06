<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@page import="java.util.HashMap"%><%@page import="java.net.URL"%><%@page import="java.util.List"%><%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="com.fy.boss.gm.XmlServerManager"%><%@page import="com.fy.boss.gm.XmlServer"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
// XmlServerManager xsm = XmlServerManager.getInstance();
// List<XmlServer> xs =  xsm.getServers();
String gmname = request.getParameter("gmname");
String begintime = request.getParameter("begintime");
String endtime = request.getParameter("endtime");
String adminurl = request.getParameter("adminurl");
String args = "authorize.username=lvfei&authorize.password=lvfei321&gmname="+gmname+"&begintime="+begintime+"&endtime="+endtime;//+"&authorize.username=huangqing&authorize.password=Huangqing9007";
long now = System.currentTimeMillis();
// for(int i=0;i<xs.size();i++){
// 	out.print("--"+i+"--"+xs.get(i).getDescription()+"<br>");
// 	if(!(xs.get(i).getDescription()).equals("巍巍昆仑")&&!(xs.get(i).getDescription()).equals("网关")&&!(xs.get(i).getDescription()).equals("腾讯内测")&&!(xs.get(i).getDescription()).equals("幽恋蝶谷")){
		
		try{
			byte[] b = HttpUtils.webPost(new URL(adminurl), args.getBytes(), new HashMap(), 60000, 60000);
			if(b.length>12){
				out.print(new String(b));
				out.print("--------------"+b.length);
			}else{
				out.print("============"+b.length);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			out.print(adminurl+"</br>"+StringUtil.getStackTrace(e));
		}
// 	}
// }
out.print("crossserver耗时："+(System.currentTimeMillis()-now));

%>