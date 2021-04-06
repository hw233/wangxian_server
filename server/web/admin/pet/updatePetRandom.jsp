<%@ page contentType="text/html;charset=utf-8"%><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>

<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.sprite.*"%>

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
	if (true) {
		return;
	}
		
		PetManager pm = PetManager.getInstance();
		long num = pm.em.count();
		out.print("num");
		
		long[] ids = pm.em.queryIds(Pet.class,"delete = 'F' ");
		
		for(long id : ids){
			Pet pet = pm.getPet(id);
			if(pet != null){
				float[] realRandom = new float[10];
				float[] beforeReplaceRandom = new float[10];
				for(int i= 0;i<5;i++){
					realRandom[i] = 75;
					if(pet.isIdentity()){
						beforeReplaceRandom[i] = 75;
					}
				}			
				for(int z =5;z<10;z++){
					realRandom[z] = 100;
					if(pet.isIdentity()){
						beforeReplaceRandom[z] = 100;
					}
					
				}
				pet.setRealRandom(realRandom);
				if(pet.isIdentity()){
					pet.setBeforeReplaceRandom(beforeReplaceRandom);
				}
				out.print(pet.getLogString());
				
				try{
					long pid = pet.getOwnerId();
					SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
					Player  p = em.find(pid);
					pet.setCountry(p.getCountry());
					int temp = 0;
					pet.setPetScore();
				}catch(Exception e){
					pet.setDelete(true);
					out.print(e);
				}
				
			}
		}
		out.print(" flush over");
	%>

</body>

</html>
