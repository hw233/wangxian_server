<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>
<%@page import="java.text.*"%>

<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>
<head>
<title>修改所有人的持续在线时间</title>
</head>
<body>

	<%
	
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		
		long num = em.count();
		if(num <= 5000){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd : HH:mm:ss");
			List<Player> list = em.query(Player.class,"","",1,5000);
			long[] ids = em.queryIds(Player.class,"");
			
			for(long id: ids){
				try{
					Player p = PlayerManager.getInstance().getPlayer(id);
					out.print(p.getName()+"    "+p.getDurationOnline()/(60*60*1000)+"小时<br/>");
					p.setDurationOnline(3*60*60*1000);
				}catch(Exception e){
					out.print(StringUtil.getStackTrace(e));
				}
			}
			/*
			for(Player p : list){
				out.print(p.getName()+"  "+sdf.format(new Date(p.getDurationOnline()))+"<br/>");
				p.setDurationOnline(3*60*60*1000);
			}
			*/
			out.print("over");
		}else{
			out.print("num > 5000"+num);
			int max = (int)(num/4000) +1;
			for(int j = 1;j <= max;j++){
				long[] ids = em.queryIds(Player.class,"","",((j-1)*4000)+1,4000*j+1);
				for(long id:ids){
					try{
						Player p = PlayerManager.getInstance().getPlayer(id);
						out.print(p.getName()+"    "+p.getDurationOnline()/(60*60*1000)+"小时<br/>");
						p.setDurationOnline(3*60*60*1000);
					}catch(Exception e){
						out.print(StringUtil.getStackTrace(e));
					}
				}
			}
			out.print("over finish");
		}
	%>

</body>

</html>
