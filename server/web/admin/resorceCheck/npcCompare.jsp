<%@page import="com.fy.engineserver.activity.peoplesearch.PeopleTemplet"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.activity.peoplesearch.CountryNpc"%>
<%@page import="com.fy.engineserver.activity.peoplesearch.PeopleSearchManager"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
</script>
</head>
<body>
	<form method="post" action="?isstart=true"> 
		<%
			String act = request.getParameter("isstart");	
			if(act!=null && act.equals("true")){
				PeopleSearchManager pm = PeopleSearchManager.getInstance();
				List<CountryNpc> list = pm.getMessageProviders();
				MemoryNPCManager mnm =  (MemoryNPCManager)GameManager.getInstance().getNpcManager();
				int rightcount = 0;
				int errorcount = 0;
				for(CountryNpc cn:list){
					if(mnm.getNPCTempalteByCategoryName(cn.getName())==null){
						out.print("消息提供者：----地图："+cn.getMapName()+"--NPC：<font color='red'>"+cn.getName()+"</font><br>");
						errorcount++;
					}else{
						rightcount++;
					}
				}
				out.print("消息提供者对比完毕：NPC正确存在数量："+rightcount+"--NPC错误存在数量："+errorcount+"<br>");
				errorcount=0;
				rightcount=0;
				Hashtable<Integer, PeopleTemplet> map =  pm.getAllTempletNpc();
				Iterator it = map.keySet().iterator();
				while(it!=null && it.hasNext()){
					PeopleTemplet p= map.get(it.next());
					CountryNpc npc = p.getTarget();
					if(mnm.getNPCTempalteByCategoryName(npc.getName())==null){
						out.print("id:"+p.getId()+"--目标NPC：----地图："+npc.getMapName()+"--NPC：<font color='red'>"+npc.getName()+"</font><br>");
						errorcount++;
					}else{
						rightcount++;
					}
				}
				out.print("目标NPC对比完毕：NPC正确存在数量："+rightcount+"--NPC错误存在数量："+errorcount);
			}		
		%>
		<input type='submit' value='开始对比'>
	</form>
</body>
</html>
