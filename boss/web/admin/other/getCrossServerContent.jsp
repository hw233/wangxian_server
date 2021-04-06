<%@page import="com.xuanzhi.tools.text.StringUtil"%><%@page import="java.util.HashMap"%><%@page import="java.net.URL"%><%@page import="java.util.List"%><%@page import="com.xuanzhi.tools.servlet.HttpUtils"%><%@page import="com.fy.boss.gm.XmlServerManager"%><%@page import="com.fy.boss.gm.XmlServer"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
XmlServerManager xsm = XmlServerManager.getInstance();
List<XmlServer> xs =  xsm.getServers();
String fbId = request.getParameter("fbId");
String servername = request.getParameter("servername");
if(fbId!=null&&fbId.trim().length()>0&&servername!=null&&servername.trim().length()>0){
	String args = "authorize.username=lvfei&authorize.password=lvfei321&fbId="+fbId;//+"&authorize.username=huangqing&authorize.password=Huangqing9007";
	long now = System.currentTimeMillis();
	for(int i=0;i<xs.size();i++){
//	 	out.print("--"+i+"--"+xs.get(i).getDescription()+"<br>");
		if(servername!=null&&(xs.get(i).getDescription()).equals(servername)){
			try{
				byte[] b = HttpUtils.webPost(new URL(xs.get(i).getUri()+"/getContent.jsp"), args.getBytes(), new HashMap(), 60000, 60000);
				if(b.length>0){
					out.print(new String(b));
				}
			}catch(Exception e){
				e.printStackTrace();
				out.print(xs.get(i).getDescription()+"</br>"+StringUtil.getStackTrace(e));
			}
		}
	}
	out.print("servername:"+servername+"getcrossserver耗时："+(System.currentTimeMillis()-now));
}


%>