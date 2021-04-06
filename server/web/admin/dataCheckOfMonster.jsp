<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@page import="com.fy.engineserver.util.datacheck.DataCheckManager"%>
<%@page import="com.fy.engineserver.util.datacheck.DataCheckHandler"%>
<%@page import="com.fy.engineserver.util.datacheck.SendHtmlToMail"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.sprite.monster.MemoryMonsterManager.MonsterTempalte"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="com.fy.engineserver.core.res.Part"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>怪物npc基本检查</title>
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<script type="text/javascript">

</script>
</head>
<body>
<% 
MemoryMonsterManager mmm = (MemoryMonsterManager)MemoryMonsterManager.getMonsterManager();
LinkedHashMap<Integer, MemoryMonsterManager.MonsterTempalte> templates = mmm.getTemplates();
for(MemoryMonsterManager.MonsterTempalte m : templates.values()){
	if(m.monster.getIcon() == null || m.monster.getIcon().isEmpty()){
		out.print("[怪物:"+m.monster.getName()+"] [icon:"+m.monster.getIcon()+"] [icon不存在]<br>");
	}
}
out.print("===========================================================<br>");
MemoryNPCManager mp = (MemoryNPCManager)MemoryNPCManager.getNPCManager();
LinkedHashMap<String, MemoryNPCManager.NPCTempalte> templatesNameMap = mp.templatesNameMap;
for(MemoryNPCManager.NPCTempalte n : templatesNameMap.values()){
	if(n.npc.getIcon() == null || n.npc.getIcon().isEmpty()){
		out.print("[NPC:"+n.npc.getName()+"] [icon:"+n.npc.getIcon()+"] [icon不存在]<br>");
	}
}
out.print("===========================================================<br>");

for(MemoryNPCManager.NPCTempalte n : templatesNameMap.values()){
	
	
	String partkey = ResourceManager.partHead + n.npc.getAvataRace()+"_"+n.npc.getAvataSex() + ResourceManager.partTail;

	Part p = null;
	if (ResourceManager.getInstance().parts.containsKey(partkey)) {
		p = (Part) ResourceManager.getInstance().parts.get(partkey);
	}
	
	if(p == null){
		out.print("[NPC:"+n.npc.getName()+"] [avata:"+n.npc.getAvata()+"] [partkey:"+partkey+"] [avata不存在]<br>");
	}
}
%>
</body>
</html>
