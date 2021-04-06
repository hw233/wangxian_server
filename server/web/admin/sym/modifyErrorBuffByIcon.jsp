<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
        pageEncoding="UTF-8"%>
<%
	if(PlatformManager.getInstance().isPlatformOf(Platform.官方)){
		Thread t = new Thread(new ChangeBuff(),"modifybuff");
		t.start();
		out.print("任务以开启，请不用重复开启。");
		Game.logger.warn("[修复buff] [任务以开启，请不用重复开启。]");
	}
%>
<%!
	boolean isstart = true;
	long sleepTime = 60*1000;
	public class ChangeBuff implements Runnable {
		
		@Override
		public void run() {
			while(isstart){
				Game.logger.warn("[修复buff] [1分钟后开启...]");
				try{
					Thread.sleep(sleepTime);
				}catch(Exception e){
					Game.logger.warn("[修复buff] [sleepException] ["+e+"]");
				}
				Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
				for(Player p : ps){
					if(p!=null){
						ArrayList<Buff> buffs = p.getBuffs();
						for(Buff b : buffs){
							if(b.getIconId()==null || b.getIconId().equals("")){
								b.setIconId("buff_xuanyun");
								p.setDirty(true, "buffs");
								Game.logger.warn("[修复buff] [玩家总在线:"+ps.length+"] [玩家:"+p.getName()+"] [玩家buff数:"+buffs.size()+"] [buff:"+b.getTemplateName()+"] [id:"+b.getTemplateId()+"] [bufficon:"+b.getIconId()+"]");
							}
						}
					}
				}
			}
		}
 	}	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改沉默buff</title>
</head>
<body>

</body>
</html>