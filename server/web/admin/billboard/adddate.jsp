<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>



<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>test</title>
</head>
<body>

	<%
	
	SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
	
	long num = em.count();
	
	if(num >= 4000){
		int max = (int)(num/4000) +1;
		
		for(int j=0;j<max;j++){
			
			long[] ids = em.queryIds(Player.class,"","",((j-1)*4000)+1,4000*j+1);
			for(long id :ids){
				try{
					Player p = em.find(id);
					if(p != null){
						BillboardStatDateManager.getInstance().getBillboardStatDate(id);
					}
				}catch(Exception e){
					out.print(e);
				}
			}
			
		}
		
	}else{
	
		long[] ids = em.queryIds(Player.class,"","",1,4000);
		for(long id :ids){
			try{
				Player p = em.find(id);
				if(p != null){
					BillboardStatDateManager.getInstance().getBillboardStatDate(id);
				}
			}catch(Exception e){
				out.print(e);
			}
		}
	}
	
	out.print("over");
	%>


</body>

</html>
