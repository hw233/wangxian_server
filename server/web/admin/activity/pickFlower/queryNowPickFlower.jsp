<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerManager"%>
<%@page import="com.fy.engineserver.activity.pickFlower.PickFlowerEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.activity.pickFlower.FlushPoint"%>
<%@page import="com.fy.engineserver.activity.pickFlower.Flower"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.activity.pickFlower.FlowerNpc"%><html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>查看本次采花的情况</title>
</head>

<body>
	
	
	
	<%
	
		String name = request.getParameter("name");
		if(name != null && !name.equals("") ){
			
			PickFlowerEntity pfe =  PickFlowerManager.getInstance().pickFlowerEntity;
			if(pfe != null && pfe.isEffect()){
				
				out.print("同意采花的人数:"+pfe.agreePickFlowerPlayer.size()+"<br/>");
				out.print("不同意采花的人数:"+pfe.disAgreePickFlowerPlayer.size()+"<br/>");
				out.print("最大花数:"+pfe.游戏中最大花个数+"<br/>");
				out.print("现有花数:"+pfe.现在游戏中花个数+"<br/>");
				out.print("***********************************************************<br/>");
				
				Map<FlushPoint, Flower> map = pfe.map;
				int i = 0;
				for(Map.Entry<FlushPoint,Flower> en : map.entrySet()){
					Flower f = en.getValue();
					if(f != null ){
						if(!f.isDisappear()){
							++i;
							FlowerNpc npc = f.npc;
							if(npc != null){
								out.print("序号: "+i+"name:"+npc.getName()+"位置:"+en.getKey().gameMap +" x:"+en.getKey().x+"y:"+en.getKey().y+"<br/>");
							}else{
								out.print("npc null"+"<br/>");
							}
						}
					}
				}
				out.print("地上一共有花"+i+"<br/>");
			}else{
				out.print("采花活动还没开始");
			}
			return;
		}
		
	%>
	
	<form action="">

		查看:<input type="text" name="name"/>
		<input type="submit" value="submit">
	
	</form>
</body>
</html>
