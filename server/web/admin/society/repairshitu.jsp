<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrentice"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrenticeManager"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrenticeSubSystem"%><html>
<head>
<title>修改师徒关系</title>
</head>
<body>
<%! 
	int count = 0;

	List<Long> list = new ArrayList<Long>();
	
	int countNum = 7*24*60;
	
	Thread thread = null;
	
	public static boolean hasRunning = false;
%>

<%
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){

	thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				while(hasRunning && count < countNum){
					
					try{
						Thread.sleep(300*1000);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
					try{
						count++;
						Player[] ps = PlayerManager.getInstance().getOnlinePlayers();
						Relation r = null;
						
						long begin = SystemTime.currentTimeMillis();
						for(Player player : ps){
							
							if(SystemTime.currentTimeMillis() - begin > 500){
								MasterPrenticeSubSystem.logger.warn("[后台更新师徒列表时间太长]");
								break;
							}
							
							long pid = player.getId();
							
							if(list.contains(pid)){
								continue;
							}
							
							list.add(pid);
							
							r = SocialManager.getInstance().getRelationById(pid);
							
							List<Long> temp = null;
							
							if(r != null){
								MasterPrentice mp = r.getMasterPrentice();
								if(mp != null){
									
									//徒弟列表
									List<Long> list = mp.getPrentices();
									if(list != null && list.size()>0){
										for(long id: list){
											
											Relation r1 = SocialManager.getInstance().getRelationById(id);
											if(r1 != null){
												MasterPrentice mp1 = r1.getMasterPrentice();
												if(mp1 != null){
													if(mp1.getMasterId() != pid){
														if(temp == null){
															temp = new ArrayList<Long>();
														}
														temp.add(id);
													}
												}
											}
										}
										
										if(temp != null){
											for(long tempId : temp){
												list.remove(tempId);
												MasterPrenticeSubSystem.logger.warn("[后台删除徒弟列表成员] ["+player.getLogString()+"] [徒弟id:"+tempId+"]");
											}
											r.setMasterPrentice(mp);
											SocialManager.em.notifyFieldChange(r, "masterPrentice");
										}
									}
									
									//师傅id
									long masterId = mp.getMasterId();
									if(masterId > 0){
										Relation r2 = SocialManager.getInstance().getRelationById(masterId);
										MasterPrentice mp2 = r2.getMasterPrentice();
										if(mp2 != null){
											if(!mp2.getPrentices().contains(pid)){
												mp.setMasterId(-1);
												r.setMasterPrentice(mp);
												SocialManager.em.notifyFieldChange(r, "masterPrentice");
												MasterPrenticeSubSystem.logger.warn("[后台删除师傅id] ["+player.getLogString()+"] [师傅id:"+masterId+"]");
											}
										}
									}
								}else{
									list.add(player.getId());
								}
							}
						}
						MasterPrenticeSubSystem.logger.warn("[后台更新师徒关系] [时间:"+(SystemTime.currentTimeMillis() - begin)+"]");
					}catch(Exception e){
						MasterPrenticeSubSystem.logger.error("[后台修改师徒关系错误]",e);
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("修改师徒关系");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./repairshitu.jsp?action=start'>点击这里</a>启动线程! <a href='./repairshitu.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./repairshitu.jsp?action=stop'>点击这里</a>停止线程! <a href='./repairshitu.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
