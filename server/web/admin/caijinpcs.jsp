<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,com.fy.engineserver.sprite.npc.*"%>
<% 
NPCManager npcm = MemoryNPCManager.getNPCManager();
%>
<%@include file="IPManager.jsp" %><html>
<head>
<link rel="stylesheet" type="text/css" href="../css/common.css" /> 
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
td {
white-space:nowrap;
}
</style>
<script type="text/javascript">
function selectionNameForMap(obj){
	if(obj.value != ""){
		window.location.href="npcs.jsp?gameName="+obj.value;
	}
}

</script>
</HEAD>
<BODY>
<%String gameName = request.getParameter("gameName");
GameManager gm = GameManager.getInstance();
MemoryNPCManager.NPCTempalte sts[] = ((MemoryNPCManager)npcm).getNPCTemaplates();
Hashtable<String,NPC> caijiNpc = new Hashtable<String,NPC>();
NPC[] allNpcs = npcm.getNPCsByPage(0,npcm.getAmountOfNPCs());
if(allNpcs != null){
	for(NPC npc : allNpcs){
		if(npc != null && npc instanceof CaijiNpc){
			if(caijiNpc.get(npc.getName()) == null){
				caijiNpc.put(npc.getName(),npc);
			}
		}
	}
}

Game[] gms = gm.getGames();
StringBuffer sb = new StringBuffer();
if(!caijiNpc.isEmpty() && gms != null && gms.length != 0){
	for(String str : caijiNpc.keySet()){
		if(str != null){
			NPC npc = caijiNpc.get(str);
			
			sb.append("<tr><td>"+str+"</td><td>");
			for(Game game : gms){
				if(game != null){
					LivingObject[] los = game.getLivingObjects();
					int count = 0;
					if(los != null){
						for(LivingObject lo : los){
							if(lo instanceof CaijiNpc && str.equals(((CaijiNpc)lo).getName())){
								count++;
							}
						}
					}
					if(count != 0){
						sb.append(""+game.getGameInfo().getMapName()+":"+count+" ");
					}
				}
			}
			sb.append("</td></tr>");
		}
	}
}
%>

<table>
<tr class="titlecolor">
<td>CaijiNpc</td>
<td>所在地图</td>
</tr>	
<%=sb %>
</table>
	
</BODY>
</html>
