<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.career.*,java.io.*,java.util.ArrayList,com.jspsmart.upload.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.buff.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.sprite.npc.*,com.fy.engineserver.core.*,com.xuanzhi.tools.text.*,com.fy.engineserver.menu.WindowManager,com.fy.engineserver.datasource.repute.*,com.fy.engineserver.shop.*,org.apache.log4j.*,com.xuanzhi.boss.game.*,com.fy.engineserver.downcity.*,com.fy.engineserver.menu.question.*,java.sql.*,java.text.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>查看超时线程日志</title>
</head>
<body>
<%
java.io.File file = new java.io.File("/home/game/resin/log/game_server/thread.log");
if(file.isFile() && file.exists()){
	BufferedReader reader = null;
	try{
		reader = new BufferedReader(new FileReader(file));
		String s = null;
		StringBuffer sb = new StringBuffer();
		sb.append("此日志为线程运行超过1秒的日志<br>绿色代表大于10秒<br>蓝色代表大于20秒<br>橙色代表大于50秒<br>红色代表大于100秒<br>");
		//文件格式：账号,手机号码,....
		int count = 0;
		while( (s = reader.readLine()) != null){
			String ss[] = s.split("cost:");
			if(ss.length > 0){
				try{
				String time = ss[1];
				if(time.trim().length() > 0){
					String[] ts = time.split("ms");
					if(Integer.parseInt(ts[0]) >= 1000){
						if(Integer.parseInt(ts[0]) >= 100000){
							sb.append("<font color='red'>"+s+"</font><br>");
						}else if(Integer.parseInt(ts[0]) >= 50000){
							sb.append("<font color='orange'>"+s+"</font><br>");
						}else if(Integer.parseInt(ts[0]) >= 20000){
							sb.append("<font color='blue'>"+s+"</font><br>");
						}else if(Integer.parseInt(ts[0]) >= 10000){
							sb.append("<font color='green'>"+s+"</font><br>");
						}else{
							sb.append(s+"<br>");
						}
					}
				}
				}catch(Exception ex){
					ex.printStackTrace();
					sb.append("日志有一行格式出问题<br>");
				}
			}
			count++;
			if(count > 100000000){
				out.println("日志已经超过一亿行，为了让jsp能看特跳出操作，有需要请联系技术");
				break;
			}
		}
		out.println(sb.toString());
	}catch(Exception ex){
		out.println("查询日志出现异常");
	}finally{
		if(reader != null){
			reader.close();
		}
	}
}
%>

</body>
</html>
