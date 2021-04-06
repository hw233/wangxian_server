<%@ page contentType="text/html;charset=utf-8"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Date"%>
<%@page import="java.io.File"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.FileInputStream"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>帐号登录信息（查看3月份之前的文件）</title>
<%
String account=request.getParameter("account");
String startTime=request.getParameter("startTime");
String endTime=request.getParameter("endTime");
Exception error=null;
 %>
</head>
<body>
<form id="f" action="">
输入日期时，建议最多输入30天。
<br>
帐号：<input type="text" id="account" name="account" value="">
<br>
起始日期（实例格式 2010-10-05）：<input type="text" id="startTime" name="startTime" value="">
<br>
截至日期（实例格式 2010-10-05）：<input type="text" id="endTime" name="endTime" value="">
<br>
<input type="submit" value="提交">
</form>
<table width="75%" align="center" border="5" cellspacing="2" cellpadding="2">
<%
if(account!=null&&startTime!=null&&endTime!=null){
	String[] infos=null;
	try{
		infos=getAccountLoginInfo(startTime,endTime,account,error,"gbk");	
	}catch(Exception e){
		e.printStackTrace();
		error=e;
	}
	if(infos!=null){
		out.print("<tr align='center'>");
		out.print("<td></td>");
		out.print("<td>"+account+"</td>");
		out.print("<td></td>");
		out.println("</tr>");
		
		out.print("<tr align='center'>");
		out.print("<td>客户端ID</td>");
		out.print("<td>手机型号</td>");
		out.print("<td>登录次数</td>");
		out.println("</tr>");
		for(String s:infos){
			String[] ss=s.split("%");
			out.print("<tr>");
			for(String st:ss){
				out.print("<td>"+st+"</td>");
			}
			out.println("</tr>");
		}
	}else{
		out.print("<tr><td>输入的时间错误或者日志文件不存在！</td></tr>");
		//if(error!=null){
			out.print("<tr><td>"+error+"</td></tr>");
		//}
	}
}
 %>
 
 <%!
 String[] getAccountLoginInfo(String startTime,String endTime,String account,Exception error,String encoding) throws Exception{
		//try{
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			HashMap<String, Integer> infos=new HashMap<String, Integer>();
			Date startDate=sdf.parse(startTime);
			Date endDate=sdf.parse(endTime);
			Date currentDate=sdf.parse(startTime);
			int index=0;
			while(currentDate.getTime()<=endDate.getTime()){
				File f=new File("/backup/gateway_log/game_gateway_mod/outer.log."+sdf.format(currentDate));
				//File f=new File("/home/game/resin/log/game_gateway_mod/outer.log."+sdf.format(currentDate));
				BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f),encoding));
				String line;
				while((line=br.readLine())!=null){
					if(line.indexOf("[用户登录] [成功]")>=0){
						String[] ss=line.split(" ");
						if(ss.length>=9&&ss[8].equals("["+account+"]")){
							int i1=line.indexOf("[");
							int i2=line.indexOf("]");
							String clientId=line.substring(i1+1, i2);
							String handset="";
							String handsetFlag="jarmt:";
							i1=line.indexOf(handsetFlag);
							if(i1>=0){
								i2=line.indexOf("]", i1);
								handset=line.substring(i1+handsetFlag.length(), i2);
							}
							String key=clientId+"%"+handset;
							if(infos.containsKey(key)){
								int times=infos.get(key);
								times++;
								infos.put(key, times);
							}else{
								infos.put(key, 1);
							}
						}
					}
				}
				Calendar ca=Calendar.getInstance();
				ca.setTime(currentDate);
				ca.add(Calendar.DAY_OF_YEAR, 1);
				currentDate=ca.getTime();
				
				index++;
				if(index>50){
					break;
				}
			}
			ArrayList<String> ar=new ArrayList<String>();
			Iterator<Entry<String, Integer>> it = infos.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, Integer> en =it.next();
				String key=en.getKey();
				int times=en.getValue();
				ar.add(key+"%"+times);
			}
			return ar.toArray(new String[0]);
		//}catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			//error=e;
		//}
		//return null;
	}
  %>
</table>
</body>
</html>
