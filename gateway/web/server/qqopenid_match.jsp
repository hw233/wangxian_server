<%@ page contentType="text/html;charset=utf-8" import="java.util.*,
com.xuanzhi.tools.text.*,
com.xuanzhi.tools.transport.*,java.nio.channels.*,org.apache.log4j.Logger,
com.fy.gamegateway.stat.*,com.fy.gamegateway.mieshi.server.*,com.fy.gamegateway.tools.*"%><%

	long now = System.currentTimeMillis();

	String action = request.getParameter("action");
	if(action == null) action = "";
	int total = 0;
	int count = 0;
	MieshiPlayerInfoManager mieshiPlayerInfoManager = MieshiPlayerInfoManager.getInstance();
	if(action.equals("match")){
	
		
		String data = request.getParameter("data");
		
		String items[] = data.split("\r*\n");
		total = items.length;
	
		for(int i = 0 ; i < items.length ; i++){
			String s = items[i].trim();
			if(s.length() == 0) continue;
			String ss[] = s.split(" ");
			
			if(ss.length == 2)
			{
				MieshiPlayerInfo[] ms = mieshiPlayerInfoManager.getMieshiPlayerInfoByUsername("QQUSER_"+ss[0]);
				
				for(MieshiPlayerInfo m : ms)
				{
					if(m != null)
					{
						mieshiPlayerInfoManager.updateMieshiPlayerInfo("QQUSER_"+ss[1],m.getServerRealName(),m.getLevel(),m.getCareer(),m.getPlayerName(),1);
						count++;
					}
				}
				
			}
			
		}
	}
%><html>
<head>
</head>
<body>
<center>
<h2>QQOpenId比对工具</h2>
已处理完uid改为openid个数为<%=count %>个
<h3>请将合作方提供的UDID或者MAC地址拷贝在输入框中</h3>
<form method=post><input type='hidden' name='action' value='match'>
<textarea rows="50" cols="30" name='data'></textarea><br>
<input type='submit' value='提交'>
</form>
</center>
</body></html>