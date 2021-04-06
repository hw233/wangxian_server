<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.io.FileReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Map"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.DevilSquareManager"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerOracleImpl"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTask"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.TreeSet"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.service.DeliverTaskManager"%>
<%@page import="com.fy.engineserver.newtask.DeliverTask"%>
<%@page import="com.fy.engineserver.newtask.NewDeliverTaskManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%


/* long totalNum_t1 = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "");
long totalNum_t2 = DeliverTaskManager.getInstance().em.count(DeliverTask.class, "");
long totalNum_t3 = DeliverTaskManager.getInstance().em.count(DeliverTask.class, " deliverTimes>? ", new Object[]{-300});
long totalNum_t4 = TaskEntityManager.em.count(TaskEntity.class, "");
out.println("===目前总数:" + totalNum_t1 + "<br>");
out.println("===目前总数2:" + totalNum_t2 + "<br>");
out.println("===目前总数3:" + totalNum_t3 + "<br>"); 
out.println("===taskentity目前总数4:" + totalNum_t4 + "<br>"); 

 /*  ThreadGroup root= Thread.currentThread().getThreadGroup();
  while (root.getParent() != null) {
     root = root.getParent();
  }
  Thread threads[]= new Thread[root.activeCount()];
  root.enumerate(threads, true);
  for (int i= 0; i < threads.length && threads[i] != null; i++) {
	  out.println("["+(threads[i].getName())+"]<br>");
  	if (threads[i].getName().equals("[后台导玩家任务数据线程]"))
  	{
  		out.println("==================");
  		threads[i].stop();
  		break;
  	}
  } 

long totalNum_111 = DeliverTaskManager.getInstance().em.count(DeliverTask.class, " deliverTimes>? ", new Object[]{-300});
long totalNum_1112 = DeliverTaskManager.getInstance().em.count(DeliverTask.class, "");
out.println("[不可以删除的数:" + totalNum_111 + "][总数:" + totalNum_1112 + "]<br>");  */  

	String password = request.getParameter("password");
	String action = request.getParameter("action");
	password = password == null ? "" : password;
	action = action == null ? "" : action;
	if("qweraszx".equals(password)) {
		if("transfer".equals(action)) {
			Thread thread = new Thread(new Runnable() {
				public void run(){
					testTransData();
				}
			});
			thread.setName("[后台导玩家任务数据线程]");
			thread.start();
			out.println("后台导玩家任务数据线程启动成功");
		} else if("select".equals(action)) {
			option4Jdbc(false);
			out.println("[后台jsp执行查询操作完毕!]<br>");
		} else if("delete".equals(action)) {
			long deleteTime = System.currentTimeMillis();
			option4Jdbc(true);
			out.println("[后台jsp执行删除操作完毕!][消耗时间:"+(System.currentTimeMillis() - deleteTime)+" 毫秒]<br>");
		}
	} else {
		out.println("后台密码错误，请像技术人员索取页面密码！");
	}
%>
	
<%!

public void option4Jdbc(boolean isDelete) throws Exception{
	java.sql.Connection conn = null;
	SimpleEntityManager<DeliverTask> emabc = SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
	SimpleEntityManager<TaskEntity> tkem = SimpleEntityManagerFactory.getSimpleEntityManager(TaskEntity.class);
	SimpleEntityManagerOracleImpl<DeliverTask> emO = (SimpleEntityManagerOracleImpl<DeliverTask>)emabc;
	SimpleEntityManagerOracleImpl<TaskEntity> em1 = (SimpleEntityManagerOracleImpl<TaskEntity>)tkem;
	conn = emO.getConnection();		
	String sql = "";
	String sql2 = "";
	if(!isDelete) {
		sql = "select id,version,playerId,taskName,taskId,lastGetTime,lastDeliverTime,firstGetTime,firstDeliverTime,deliverTimes from DeliverTask where deliverTimes <= -300";
		sql2 = "select id,version,playerId,taskName,taskId,status,showType from TaskEntity where showType=0 and status>=3";
	} else  {
		conn.setAutoCommit(false);
		
		sql = "delete from DeliverTask where deliverTimes <= -300";
		sql2 = "delete from TaskEntity where showType=0 and status>=3";
		//sql2 = "delete from TaskEntity where showType=0 and status>=3 and playerId=1353000000000001025";
	}
	int indexii = 0;
	int indexii2 = 0;
	java.sql.Statement stmt = conn.createStatement();
	java.sql.ResultSet rs = null;
	java.sql.ResultSet rs2 = null;
	
	int sqlCount = 0;
	int sql2Count = 0;
	if(!isDelete)
	{
		rs = stmt.executeQuery(sql);
		rs2 = stmt.executeQuery(sql2);
		
	}
	else
	{
		try
		{
			sqlCount = stmt.executeUpdate(sql);
			sql2Count = stmt.executeUpdate(sql2);
			conn.commit();
		}
		catch(Exception e)
		{
			conn.rollback();
		}
	}
		
	long lastPlayerId = 0;
	if(!isDelete) {
		while(rs.next()) {
			DevilSquareManager.logger.error("[后台页面查delevertask]" + "["+(indexii++)+"]" + "[" + rs.getLong(1) + "][" + rs.getInt(2) + "][" + rs.getLong(3) + "]["+rs.getString(4)+"]["+rs.getLong(5)+"]["+rs.getInt(10)+"]");
		}
		
		rs.close();
	}
	if(!isDelete) {
		while(rs2.next()) {
			DevilSquareManager.logger.error("[后台页面查taskentity]" + "["+(indexii2++)+"]" + "[" + rs2.getLong(1) + "][" + rs2.getInt(2) + "][" + rs2.getLong(3) + "]["+rs2.getString(4)+"]["+rs2.getLong(5)+"]["+rs2.getInt(6)+"]["+rs2.getByte(7)+"]");
		}
		rs2.close();
	}
	if(isDelete) {
		DevilSquareManager.logger.error("[删除语句执行成功][" + sql + "]["+sql2+"] ["+sqlCount+"] ["+sql2Count+"]<br>");
	}

	conn.close();
}


public void testTransData(){
	long startTime = System.currentTimeMillis();	
	Player[] players = GamePlayerManager.getInstance().getOnlinePlayers();
	SocialManager socialManager = SocialManager.getInstance();
	NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
	Relation relation = null;
	Player pp = null;
	int index = 0;
	int index2 = 0;
	Set<Long> tempSet = new HashSet<Long>();
	Set<Long> tempSet2 = new HashSet<Long>();
	
	
	Map<Long, Map<Long, NewDeliverTask>> tempMap1 = new Hashtable<Long, Map<Long, NewDeliverTask>>();//map<playerId,map<taskId,ndt>>
	List<NewDeliverTask> tempNl = new ArrayList<NewDeliverTask>();
	
	//for(int i=0; i<1; i++) {
		List<Long> friendList = new ArrayList<Long>();
		try{
			java.sql.Connection conn = null;
			SimpleEntityManager<DeliverTask> emabc = SimpleEntityManagerFactory.getSimpleEntityManager(DeliverTask.class);
			SimpleEntityManagerOracleImpl<DeliverTask> emO = (SimpleEntityManagerOracleImpl<DeliverTask>)emabc;
			String sql = "select playerId from DeliverTask where deliverTimes > -300 and taskId=1001";
			conn = emO.getConnection();	
			java.sql.Statement stmt = conn.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			Set<Long> tempsss = new HashSet<Long>();
			while(rs.next()) {
				long aaadd = rs.getLong(1);
				if(!friendList.contains(aaadd)){
					friendList.add(aaadd);
				}
			}  
			DeliverTaskManager.logger.error("[**查询库结束**] [耗时:" + (System.currentTimeMillis() - startTime) + "][需要传到数:"+friendList.size()+"]");  
			/*  File f = new File("/home/game/resin_server_3/webapps/game_server/admin/dujie/text123.txt");
			BufferedReader rm = new BufferedReader(new FileReader(f));
			String str = null;
			while((str = rm.readLine()) != null) {
				friendList.add(Long.parseLong(str));
			}
			rm.close();  */
		}catch(Exception e) {
			DeliverTaskManager.logger.error("[**11**] [异常]", e);			
		}
			if(friendList != null && friendList.size() > 0) {
				for(int ii=0; ii<friendList.size(); ii++) {
					if(friendList.get(ii) < 0) {
						continue;
					}
					if(tempSet.contains(friendList.get(ii))) {
						continue;
					}
					if(tempMap1.size() >= 10) {
						tempMap1.clear();
					}
					if(tempNl.size() > 5000) {
						try {
							List<NewDeliverTask> tempNl111 = new ArrayList<NewDeliverTask>();
							for(int is=3000; is<tempNl.size(); is++) {
								tempNl111.add(tempNl.get(is));
							}
							tempNl.clear();
							tempNl.addAll(tempNl111);
						}catch(Exception e){}
					}
					long playerIdd = friendList.get(ii);
					ArrayList<Long> dsCache = new ArrayList<Long>();
					Map<Long, NewDeliverTask> tempPmap = tempMap1.get(playerIdd);
					if(tempPmap == null) {
						tempPmap = new Hashtable<Long, NewDeliverTask>();
					}
					List<DeliverTask> list = null;
					try{
						long totalNum_t = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + playerIdd + " ");
						if(totalNum_t > 0) {
							continue;
						}
						DeliverTaskManager.logger.error("[**后台页面转储任务数据开始**] [playerid:" + playerIdd +"]");
						index ++;
						list = DeliverTaskManager.getInstance().em.query(DeliverTask.class, "playerId = ?", new Object[]{playerIdd},"", 1, 4000);
						//list = DeliverTaskManager.getInstance().em.query(DeliverTask.class, "playerId = ? and deliverTimes > ?", new Object[]{playerIdd, DeliverTaskManager.deleteFlag},"", 1, 4000);
						DeliverTaskManager.logger.error("[delivertask中查找出数据][" + list.size()+"]");
						if(list != null && list.size() > 0) {
							TaskManager tm = TaskManager.getInstance();
							List<DeliverTask> nuFrontG = new ArrayList<DeliverTask>();			//没有前置任务的任务组
							Set<Long> deliveredList = new HashSet<Long>();						//已经完成的任务id
							for(DeliverTask dd : list) {
								Task task = tm.getTask(dd.getTaskId());
								DeliverTaskManager.getInstance().notifyDeleteFromCache(dd);
								DeliverTaskManager.getInstance().tempMap.put(dd.getId(), dd);
								deliveredList.add(dd.getTaskId());
								if(task != null) {
									if(task.getFrontGroupName() == null || task.getFrontGroupName().isEmpty()) {		//挑出已完成任务重没有前置任务的任务列表
										nuFrontG.add(dd);
									}
								}
							}
							Set<Long> wrongData = new TreeSet<Long>(new Comparator<Long>() {
								@Override
								public int compare(Long arg0, Long arg1) {
									// TODO Auto-generated method stub
									if(arg0 > arg1) {
										return 1;
									} else {
										return -1;
									}
								}
							});
							DeliverTaskManager.logger.error("[遍历11delivertask中查找出数据]");
							for(DeliverTask dt : nuFrontG) {									
								try {
									
									NewDeliverTask tempNdt = new NewDeliverTask(playerIdd, dt.getTaskId());
									tempPmap.put(tempNdt.getTaskId(), tempNdt);
									if(!dsCache.contains(tempNdt.getTaskId())) {
										dsCache.add(tempNdt.getTaskId());
									}
									dsCache = notifyNewDeliverTask(tempNdt,tempPmap,dsCache);	//首先将没有前置任务的任务存入新库
									Set<Long> dd = transAllDeliverdTask(playerIdd, dt.getTaskName(), deliveredList, tempPmap,dsCache);
									if(dd != null && dd.size() > 0) {
										for(Long ddd : dd) {
											wrongData.add(ddd);
										}
									} 
								} catch (Exception e) {
									// TODO Auto-generated catch block
								}
							}
						}
					}catch(Exception e) {
					}
					DeliverTaskManager.logger.error("[**后台页面转储任务数据结束**] [playerid:" + playerIdd +"]");
					tempSet.add(playerIdd);
					index2++;
					DeliverTaskManager.logger.error("[后台测试][" + playerIdd + "][ " + dsCache + "]");
					for(Long lds : dsCache) {
						try {
							NewDeliverTask ndtt2 = new NewDeliverTask(playerIdd, lds);
							NewDeliverTaskManager.getInstance().em.notifyNewObject(ndtt2);
							tempNl.add(ndtt2);
							try {
								Thread.sleep(50);
							}catch(Exception e){}
						} catch(Exception e) {
							
						}
					}
					try {
						Thread.sleep(500);
					}catch(Exception e){}
				}
			}
		//}
	//}
	try {
		long totalNum_1 = DeliverTaskManager.getInstance().em.count(DeliverTask.class, " deliverTimes>? ", new Object[]{-300});
		DeliverTaskManager.logger.error("[后台页面导任务数据结束,导入好友以及仇人后delivertask中剩余数据量异常][" + totalNum_1 + "]");
	} catch(Exception e) {
		DeliverTaskManager.logger.error("[导入好友以及仇人结束后delivertask中剩余数据量异常]", e);
	}
	DeliverTaskManager.logger.error("[**后台页面导任务数据结束**] [耗时:" + (System.currentTimeMillis() - startTime) + "]");
	DeliverTaskManager.logger.error("[后台页面导任务数据结束] [总共导入玩家数: " + index2 + "]");
}

public ArrayList<Long> notifyNewDeliverTask(NewDeliverTask dt, Map<Long, NewDeliverTask> temp1Map, ArrayList<Long> dscatch) {
	NewDeliverTask dtn = dt;
	Task tk = TaskManager.getInstance().getTask(dtn.getTaskId());
	if(tk == null) {
		return dscatch;
	}
	boolean flag = false;
	try {
			if(tk.getFrontGroupName() == null || tk.getFrontGroupName().isEmpty()) {		//没有前置任务则插入
				long totalNum111 = NewDeliverTaskManager.getInstance().em.count(NewDeliverTask.class, "playerId=" + dtn.getPlayerId() + " and taskId=" + dtn.getTaskId());
				if(totalNum111 <= 0) {
					if(!dscatch.contains(dtn.getTaskId())) {
						dscatch.add(dtn.getTaskId());
					}
					//NewDeliverTaskManager.getInstance().em.notifyNewObject(dtn);
				}
			} else {													//有前置任务则做更新操作
				List<Task> list = TaskManager.getInstance().getTaskGroupByGroupName(tk.getFrontGroupName());
				for(Task forntTk : list) {
					if(forntTk != null) {
						if(forntTk.getShowType() != tk.getShowType()) {	//有前置任务，判断showtype是否相同，不相同则执行插入操作(主线支线需要区分开,境界与主线相同，单独一条)
							//NewDeliverTaskManager.getInstance().em.notifyNewObject(dtn);
							if(!dscatch.contains(dtn.getTaskId())) {
								dscatch.add(dtn.getTaskId());
							}
							flag = true;
						} else {														//有前置任务，showtype相同执行更新
							NewDeliverTask frontDnt = temp1Map.get(forntTk.getId());
							if(frontDnt != null) {
								for(int kk=0; kk<dscatch.size(); kk++) {
									if(dscatch.get(kk) == frontDnt.getTaskId()) {
										dscatch.remove(frontDnt.getTaskId());
										dscatch.add(tk.getId());
										flag = true;
										break;
									}
								}
								if(flag) {
									break;
								}
							}
						}
					} 
				}
				if(!flag) {
					if(!dscatch.contains(tk.getId())) {
						dscatch.add(tk.getId());
					}
				}
			}

	} catch (Exception e) {
	}
	return dscatch;
}

private Set<Long> transAllDeliverdTask(long playerId, String taskName, Set<Long> deliveredList, Map<Long, NewDeliverTask> al, ArrayList<Long> dsCache) {
	TaskManager tm = TaskManager.getInstance();
	NewDeliverTaskManager ndtm = NewDeliverTaskManager.getInstance();
	Task t = TaskManager.getInstance().getTask(taskName);
	if(t == null) {
		return new TreeSet<Long>();
	}
	List<Task> nextList = tm.getnextTask(t.getGroupName());
	Set<Long> rightData = new HashSet<Long>();
	if(nextList != null && nextList.size() > 0) {
		for(Task nt : nextList) {
			if(deliveredList.contains(nt.getId())) {
				try {
					if(al == null || !(al.containsKey(nt.getId()) && al.get(nt.getId()) != null)) {
						NewDeliverTask temp = new NewDeliverTask(playerId, nt.getId());
						dsCache = notifyNewDeliverTask(temp,al ,dsCache);
						rightData.add(nt.getId());
						al.put(nt.getId(), temp);
					}
					this.transAllDeliverdTask(playerId, nt.getName(), deliveredList, al, dsCache);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
	Set<Long> wrongData = new HashSet<Long>();
	if(deliveredList.size() < rightData.size()) {
		for(Long ll : deliveredList) {
			if(!rightData.contains(ll)) {
				wrongData.add(ll);
			}
		}
	}
	return wrongData;
}

%>
<head>

</head>
<body>
</body>
</html>
