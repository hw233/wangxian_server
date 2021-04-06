<%@page import="com.fy.engineserver.mail.MailSendType"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%!int enterTimes = 0;%>
<%
String ip = request.getRemoteAddr();
String recorder = "";
Object o = session.getAttribute("authorize.username");
if(o!=null){
	recorder = o.toString();
}
	if (enterTimes != 0) {
		out.print("你访问次数太多了:" + enterTimes);
		return;
	}
	String mailTitle = "马上有红包，天天送吉祥！";
	String mailContent = "马年大吉，充值有礼！马上有红包，春节期间天天返给您！赶快取走红包吧！《飘渺寻仙曲》祝您新春愉快！";

	enterTimes++;
	out.print("<H2>这是你第" + enterTimes + "次访问</H2>");
	//TODO
	//File file = new File("/home/game/resin/webapps/game_server/WEB-INF/game_init_config/sendSilver.txt");
	String  path = request.getRealPath("sendSilver.txt");
	out.print(path + " 是否存在:" + new File(path).exists() + "<HR/>");
	FileInputStream fis = new FileInputStream(path); 
	InputStreamReader isr = new InputStreamReader(fis, "GBK"); 
	BufferedReader bufferedReader = new BufferedReader(isr); 
	String line = bufferedReader.readLine();
	List<String[]> list = new ArrayList<String[]>();
	String thisServerName = GameConstants.getInstance().getServerName();
	try {
		while (line != null && !"".equals(line.trim())) {
			String[] values = line.split("\t");
			String serverName = values[0];
			if (thisServerName.equals(serverName)) {
				list.add(values);
			}
			line = bufferedReader.readLine();
		}
	} catch (Exception e) {
		out.print("解析出异常了,去stderr看看!<BR/>");
		e.printStackTrace();
	} finally {
		if (bufferedReader != null) {
			try {
				bufferedReader.close();
			} catch (Exception ee) {
				out.print("关闭出异常了,去stderr看看!<BR/>");
				ee.printStackTrace();
			}
		}
	}
	//TODO 发送
	for (String[] arr : list) {
		long playerId = Long.valueOf(arr[1]);
		long silver = Long.valueOf(arr[2]);
		MailManager.getInstance().sendMail(playerId, null, null, mailTitle, mailContent, silver, 0, 0, "后台补发",MailSendType.后台发送,"--",ip,recorder);
		out.print(arr[0] + "--" + playerId + "--" + silver + "文<HR/>");
	}
	out.print("总数:" + list.size() + "<BR/>");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>

<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.io.*"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%></html>