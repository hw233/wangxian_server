<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.zongzu.manager.ZongPaiManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.zongzu.data.ZongPai"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<%
		try{
			String aa = request.getParameter("name");
			String set = request.getParameter("set");
			if(aa != null && !aa.equals("")){
				
				Player p = PlayerManager.getInstance().getPlayer(aa);
				ZongPaiManager zm = ZongPaiManager.getInstance();
				ZongPai zp = zm.getZongPaiByPlayerId(p.getId());
				if(zp == null){
					out.print("宗派 null");
					return;
				}
				Class cl = Class.forName("com.fy.engineserver.zongzu.manager.ZongPaiManager");
				Field f = cl.getDeclaredField("citySeizeMap");
				f.setAccessible(true);
				HashMap<String, Long> map = (HashMap<String, Long>)f.get(zm);
				String mapa = null;
				for(Map.Entry<String, Long> en : map.entrySet()){
					if(en.getValue() == zp.getId()){
						mapa = en.getKey();
					}
					out.print(en.getKey() +"  : "+en.getValue()+"<br/>");
				}
				
				if(zp != null){
					String cityName = zm.getCityNameByZongPai(zp);
					out.print("占领城市"+cityName+"<br/>");
				}else{
					out.print("zp null<br/>");
				}
				
				out.print("本宗派占领国家"+mapa+"<br>");
				if(set == null || set.equals("")){
					zp.setSeizeCity(mapa);
					ZongPaiManager.logger.error("[后台设置宗派占领国家] ["+zp.getLogString()+"] ["+p.getLogString()+"]");
					out.print("后台设置宗派占领国家"+mapa+"<br/>");
				}
				
				out.print("完成");
				return ;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
	%>


		<form action="" >
			随便：<input type="text" name="name" /><br/>
			设置(默认是不设置，为null或空才设置)：<input type="text" name="set" value="set"/><br/>
			<input type="submit" value="提交"/>
		</form>
</body>
</html>