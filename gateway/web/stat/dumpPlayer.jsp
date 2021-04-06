<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page
	import="java.io.*,java.util.*,com.xuanzhi.tools.text.*,com.xuanzhi.tools.mem.*,com.fy.gamegateway.stat.*,
	com.fy.gamegateway.mieshi.resource.manager.*,
	com.fy.gamegateway.mieshi.server.*,java.sql.*,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.* "%>
<html>
<%! 
	int startNum = 0;
	int size = 50000;
	
	int count = 0;

	FileOutputStream fout = null;
	
	public static boolean hasRunning = false;

	Thread 	thread = null;
	
	int dump(int start,int end) throws Exception{
		if(fout == null){
			fout = new FileOutputStream("/home/game/dump_player.txt",true);
		}
		
		StatManager stat = StatManager.getInstance();
		String sql = " select * from (select T.*,rownum as RR from (select B.clientId,B.channel,B.platform,B.uuid,B.deviceId,A.username,to_char(A.registerTime,'YYYY-MM-DD HH24:MI:SS') from ClientAccount A left join Client B on A.clientId=B.clientId where A.hasSuccessRegister='T' order by A.registerTime ) T) where RR>="+start+" and RR < "+end+"";
		Connection conn = null;
		try{
			String dbType = SimpleEntityManagerFactory.getDbType();

			if(dbType.equals("oracle")){
				SimpleEntityManagerOracleImpl<Client> em =  (SimpleEntityManagerOracleImpl<Client>)stat.em4Client;
				conn = em.getConnection();
			}else{
				SimpleEntityManagerMysqlImpl<Client> em =  (SimpleEntityManagerMysqlImpl<Client>)stat.em4Client;
				conn = em.getConnection();
			}
			Statement stmt = conn.createStatement();
			stmt.setFetchSize(1000);
			ResultSet rs = stmt.executeQuery(sql);
			int r = 0;
			while(rs.next()){
				try {
					r++;
					String clientId = rs.getString(1);
					String channel = rs.getString(2);
					String platform = rs.getString(3);
					String uuid = rs.getString(4);
					String deviceId = rs.getString(5);
					String username = rs.getString(6);
					String registerTime = rs.getString(7);
					
					MieshiPlayerInfoManager mmm = MieshiPlayerInfoManager.getInstance();
					MieshiPlayerInfo infos[] = mmm.getMieshiPlayerInfoByUsername(username);
					
					for(int i = 0 ; i < infos.length ; i++){
						StringBuffer sb = new StringBuffer();
						sb.append(clientId);
						sb.append("#&#");
						sb.append(channel);
						sb.append("#&#");
						sb.append(platform);
						sb.append("#&#");
										
						if(uuid == null) uuid = "";
						if(deviceId == null) deviceId = "";
						
						if(uuid.length() > 0) sb.append(uuid);
						else sb.append(deviceId);
						sb.append("#&#");
						
						sb.append(username);
						sb.append("#&#");
						sb.append(registerTime);
						sb.append("#&#");
						
						sb.append(infos[i]!=null?infos[i].getCareer():"");
						sb.append("#&#");
						
						sb.append(infos[i]!=null?infos[i].getLevel():"");
						sb.append("#&#");
						
						sb.append(infos[i]!=null?infos[i].getServerRealName():"");
						sb.append("#&#");
						
						sb.append(infos[i]!=null?infos[i].getPlayerName():"");					
						
						sb.append("\n");
						
						fout.write(sb.toString().getBytes());
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			rs.close();
			stmt.close();
			return  r;
		}finally{
			conn.close();
		}	
	}
%>
<%

%>
<head>
<title>修改单人多连接BUG</title>
</head>
<body>
<%

	
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){
		thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				while(hasRunning){
					
					try{
						Thread.sleep(1000);
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
					try{
						long startTime = System.currentTimeMillis();
						int r = dump(startNum+1,startNum+1+size);
						System.out.println("["+DateUtil.formatDate(new java.util.Date(),"yyyy-MM-dd HH:mm:ss")+"] [dump_player] [start:"+startNum+"] [size:"+size+"] [cost:"+(System.currentTimeMillis() - startTime)+"ms]");
						startNum += size;
						count++;
						if(r == 0) hasRunning = false;
					}catch(Throwable e){
						e.printStackTrace();
					}
				}
				if(fout != null){
					try{
						fout.close();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				hasRunning = false;
				thread = null;
			}
		});
		thread.setName("DUMP PLAYER");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./dumpPlayer.jsp?action=start'>点击这里</a>启动线程! <a href='./dumpPlayer.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./dumpPlayer.jsp?action=stop'>点击这里</a>停止线程! <a href='./dumpPlayer.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
