<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@page import="com.fy.engineserver.newtask.targets.TaskTargetOfTalkToNPC"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.activity.peoplesearch.PeopleSearchManager"%>
<%@page import="com.fy.engineserver.activity.peoplesearch.CountryNpc"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.newtask.service.TaskConfig.TargetType"%>
<%@page import="com.fy.engineserver.newtask.targets.TaskTarget"%>
<%@page import="com.fy.engineserver.newtask.TaskGivenArticle"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<title>修复任务，商店BUG</title>
</head>
<body>
<form method="post" action="?isstart=true"> 
	<%
		String [] tasknames = null;
		if(PlatformManager.getInstance().isPlatformOf(Platform.台湾)){
			tasknames = new String[]{"國內煮酒(60體)","國外煮酒(80體)"};
		}else{
			tasknames = new String[]{"国内煮酒(60体)","国外煮酒(80体)"};
		}
		for(String name:tasknames){
			Task task = TaskManager.getInstance().getTask(name); 
			TaskGivenArticle givenArticle = task.getGivenArticle();
			String[] names = givenArticle.getNames();
			String [] newNames = new String[names.length];
			for(int i=0;i<names.length;i++){
				if(names[i]!=null){
					out.print("修改前：：任务名字："+name+"--任务道具："+Arrays.toString(task.getGivenArticle().getNames())+"<br>");
					newNames[i] = names[i].trim();
	 				givenArticle.setNames(newNames);
	 				task.setGivenArticle(givenArticle);
					out.print("<font color='red'>修改后：：任务名字："+name+"--任务道具："+Arrays.toString(task.getGivenArticle().getNames())+"</font><br>");
				}
			}
		}
		out.print("=============================任务道具修复完毕=============================<br>");
		
// 		Task task = TaskManager.getInstance().getTask("腾胜天君"); 
// 		Player p = PlayerManager.getInstance().getPlayer("连登");
// 		List<TaskEntity> list = p.getAllTask();
// 		list.clear();
// 		TaskEntity te = new TaskEntity(task,p);
// 		list.add(te);
// 		out.print(list.size());
// 		//新的任务目标
// 		String[] newtargetName = new String[]{"腾胜天君"}; 
// 		String targetMapName[] = new String[]{"落星玉台"};
// 		int[] x = {1534};
// 		int[] y = {2393};
// 		String[] resMapName = {"中立"};
// 		TaskTarget taskTarget = new TaskTargetOfTalkToNPC(targetMapName,newtargetName);
// 		taskTarget.setTargetType(TargetType.TALK_NPC);
// 		taskTarget.setTargetByteType(taskTarget.getTargetType().getIndex());
// 		taskTarget.setTargetName(newtargetName);
// 		taskTarget.setMapName(targetMapName);
// 		taskTarget.setTargetNum(1);
// 		taskTarget.setX(x);
// 		taskTarget.setY(y);
// 		taskTarget.setResMapName(resMapName);
// 		try {
// 			out.print(Arrays.toString(taskTarget.getTargetName())+"--x:"+Arrays.toString(taskTarget.getX())+"--y:"+Arrays.toString(taskTarget.getY()) );
// 			task.setTargets(new TaskTarget[]{taskTarget});
// 		} catch (Exception e) {
// 			e.printStackTrace();
// 			out.print(e);
// 		}
	%>
</form>
</body>
</html>
