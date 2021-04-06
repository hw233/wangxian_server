<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="com.sun.btrace.BTraceUtils.Sys"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>测试shell</title>

</head>

<body>

<%

	String logRoot = request.getRealPath("/").replace("webapps","log");
	//具体日志路径
	String logPath = logRoot + "billboardsManager.log";
	//具体sh路径
	String commandFilePath = logRoot + "/testLog.sh";
	
	File commandFile = new File(commandFilePath);
	String command = null;
	FileOutputStream fos = null;
	if(!commandFile.exists()){
		
		command = " /bin/cat " + logPath+ "| grep 更新榜单成功 | cut -d' ' -f 7 | tr -d '[]' | cut -d':' -f 2,5";
		commandFile.createNewFile();
		fos = new FileOutputStream(commandFilePath);
		fos.write(command.getBytes("UTF-8"));
		fos.flush();
		fos.close();
		out.print("不存在命令，创建<br/>");
	}else{
		out.print("命令存在直接执行");
	}
	
	long begin = System.currentTimeMillis();
	Process process = Runtime.getRuntime().exec("/bin/sh " + commandFilePath);
	
	int isError = process.getErrorStream().available();
	if(isError > 0){
		
		BufferedReader errbr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		String errerSt = null;
		while((errerSt = errbr.readLine()) != null){
			out.print(errerSt+"<br/>");
		}
		return;
	}else{
		out.print("错误为空");
	}

	
	int isInput = process.getInputStream().available();
	if(isInput > 0){
		System.out.print(isInput);
		BufferedReader inbr = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String inSt = null;
		while((inSt = inbr.readLine()) != null){
			out.print(inSt+"<br/>");
		}
		return;
	}else{
		out.print("输入为空");
	}
	
	long end = System.currentTimeMillis(); 
	
	out.print("<br/>花费时间:"+(end - begin));
	
%>

</body>
</html>
