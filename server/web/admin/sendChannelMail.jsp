<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.mail.service.MailManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String pwd = request.getParameter("pwd");
	if(pwd != null && pwd.equals("zxcvbn")){
		
	}else{
		out.print("无权操作");
		return;
	}
	String mess = "1100000000007967745,国内本地测试##1102000000000005930,飘渺王城##1102000000011253897,飘渺王城##1102000000010864910,飘渺王城##1102000000010926319,飘渺王城##1102000000011725094,飘渺王城##1102000000000006812,飘渺王城##1102000000011438531,飘渺王城##1102000000011745657,飘渺王城##1102000000011581804,飘渺王城##1102000000010905862,飘渺王城##1102000000011643076,飘渺王城##1102000000009984727,飘渺王城##1102000000000720691,飘渺王城##1102000000008649359,飘渺王城##1102000000011561479,飘渺王城##1102000000011418035,飘渺王城##1100000000010472889,风雪之巅##1100000000010063772,风雪之巅##1108000000002560255,梦醉江湖##1104000000008294526,梦醉江湖##1104000000000024307,梦醉江湖##1108000000007640070,梦醉江湖##1104000000000450730,梦醉江湖##1104000000000245889,梦醉江湖##1104000000001372775,梦醉江湖##1364000000000001480,剑雨潇湘##1364000000000019858,剑雨潇湘##1369000000000010354,剑雨潇湘##1374000000000004877,剑雨潇湘##1374000000000022958,剑雨潇湘##1364000000000112663,剑雨潇湘##1369000000000004525,剑雨潇湘##1374000000000003156,剑雨潇湘##1374000000000006903,剑雨潇湘##1369000000000011727,剑雨潇湘##1364000000000008993,剑雨潇湘";
	String serverName = GameConstants.getInstance().getServerName();
	String servermess[] = mess.split("##");
	String title = "【交易助手】服务关闭通知";
	String content = "亲爱的仙友，很遗憾部分渠道的【飘渺寻仙曲官方交易助手】将在1月15日关闭挂单功能，1月22日前停止服务，"+
			"请您尽快提现并及时下架物品。若关闭服务后仍有提现问题，请用绑定手机致电400-8281011.";
	if(servermess != null && servermess.length > 0){
		for(String s : servermess){
			if(s.contains(serverName)){
				String ids[] = s.split(",");
				if(ids != null && ids.length == 2){
					String sname = ids[1];
					long pid = Long.parseLong(ids[0]);
					MailManager.getInstance().sendMail(pid, new ArticleEntity[] {}, title, content, 0, 0, 0, "交易助手关闭");
					out.print("[邮件发送成功] [服务器:"+sname+"] [角色id:"+pid+"]<br>");
				}
			}
		}
	}

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>mail</title>
</head>
<body>

</body>
</html>