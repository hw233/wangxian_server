<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="java.util.Map.*"%>

<%@page import="java.io.*"%>

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>


<%@page import="com.fy.boss.authorize.model.Passport"%><html>
<head>
<title>查询日志中的玩家渠道</title>

</head>
<body>

	<%
		String nums = request.getParameter("nums");
		if(nums != null && !nums.equals("")){
	
			String fileName = "/home/dev/mieshi_log/game_server/";
			fileName = fileName+nums;
			fileName = fileName+".txt";
			PlayerManager pm = PlayerManager.getInstance();
			
			BufferedReader br = null;
			try{
				br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"gbk"));
				String ss = br.readLine();
	
				while(ss != null){
					
					String[] arr = ss.split("\\[");
					String st = arr[arr.length -1];
					String real = st.substring(0,st.length()-1);
					
					
					Player player = null;
					Passport pp = null;
					String channel = null;
					if(real != null){
						try{
							player = pm.getPlayer(real);
						}catch(Exception e){
							out.print(ss+"  没有找到,或许不是玩家。");
						}
					}
					if(player != null){
						pp = player.getPassport();
						if(pp != null){
							channel = pp.getRegisterChannel();
						}
					}
					out.print(ss +"  渠道： "+channel + "<br/>");
					ss = br.readLine();
				}
				out.print("over");
				return;
			}catch(Exception e){
				if(br != null){
					try {
						br.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
				out.print("没有指定文件"+fileName);
			}
			return ;
		}
	%>
	
	<form action="">
		那个服务区:<input type="text" name="nums"/>
		<input type="submit" value="submit"/>
	</form>

</body>
</html>
