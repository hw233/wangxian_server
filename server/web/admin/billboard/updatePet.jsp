<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>



<%@page import="com.fy.engineserver.sprite.pet.*"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>test</title>
</head>
<body>

	<%
		
		PetManager pm = PetManager.getInstance();
	
		long num = pm.em.count();
		
		if(num >= 4000){
			int max = (int)(num/4000) +1;
			
			for(int j = 1;j <= max;j++){
				long[] ids = pm.em.queryIds(Pet.class,"","",((j-1)*4000)+1,4000*j+1);
				for(long id:ids){
					Pet pet = pm.getPet(id);
					if(pet != null){
						try{
							long pid = pet.getOwnerId();
							SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
							Player  p = em.find(pid);
							out.print(pet.getCountry()+"<br/>");
							pet.setCountry(p.getCountry());
							int temp = 0;
							// 设置宠物品质得分  排行榜
							//pet.setQualityScore(temp);
							pet.setPetScore();
						}catch(Exception e){
							pet.setDelete(true);
							out.print(e);
						}
					}
				}
			}
			
		}else{
			long[] ids = pm.em.queryIds(Pet.class,"","",1,4000);
			for(long id:ids){
				Pet pet = pm.getPet(id);
				if(pet != null){
					try{
						long pid = pet.getOwnerId();
						SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
						Player  p = em.find(pid);
						out.print(pet.getCountry()+"<br/>");
						pet.setCountry(p.getCountry());
						int temp = 0;
						
						// 设置宠物品质得分  排行榜
					//	pet.setQualityScore(temp);
						pet.setPetScore();
					}catch(Exception e){
						pet.setDelete(true);
						out.print(e);
					}
				}
			}
		}
		out.print("完成");
	%>


</body>

</html>
