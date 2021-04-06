<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.util.*,com.fy.engineserver.sprite.*,
				com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.enterlimit.*,com.fy.engineserver.smith.*"%>
<%!
public class ProcessDog implements Runnable {
	private Thread thread;
	
	private boolean running;
	
	private HashSet<String> symbol = new HashSet<String>();
	
	private long kickNum;
	
	private HashSet<Long> kickSet = new HashSet<Long>();
	
	public void start() {
		if(!running) {
			running = true;
			thread = new Thread(this, "ProcessDog");
			thread.start();
		}
	}
	
	public void stop() {
		this.running = false;
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public HashSet<String> getSymbol() {
		return symbol;
	}
	
	public void setSymbol(HashSet<String> symbol) {
		this.symbol = symbol;
	}
	
	public void run() {
		while(ArticleRelationShipManager.getInstance().get不采样充值等级()==161 && running) {
			try {
				PlayerManager pm = PlayerManager.getInstance();
				Player ps[] = pm.getOnlinePlayers();
				for(Player p : ps) {
					Player_Process pp = EnterLimitManager.player_process.get(p.getId());
					if(pp != null && pp.getAndroidProcesss() != null && pp.getAndroidProcesss().length > 0) {
						StringBuffer sb = new StringBuffer();
						String proces[] = pp.getAndroidProcesss();
						for(String pr : proces) {
							sb.append(pr + ";");
						}
						String pc = sb.toString();
						if(pc.length() > 0) {
							pc = pc.substring(0, pc.length()-1);
						}
						if(symbol.contains(pc)) {
							//确定是黑名单进程序列
							pm.kickPlayer(p.getId());
							kickNum++;
							kickSet.add(p.getId());
							EnterLimitManager.logger.warn("[黑名单进程序列] [踢下线] ["+p.getLogString()+"] [kickPlayer:"+kickSet.size()+"] [kickNum:"+kickNum+"] [process:"+pc+"]");
						}
					}
				}
				EnterLimitManager.logger.warn("[检查黑名单进程序列] [kickPlayer:"+kickSet.size()+"] [kickNum:"+kickNum+"]");
				Thread.sleep(10000);
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
		running = false;
	}
}
ProcessDog dog = null;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
	<title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" href="css/style.css" />
	<link rel="stylesheet" href="css/atalk.css" />
	<script type="text/javascript">
	</script>
	</head>

	<body>
		<% 
		HashSet<String> sym = new HashSet<String>();
		sym.add("飘渺寻仙曲,com.koramgame.ggplay.wxtw;软件包访问帮助程序,com.android.defcontainer;Android 系统,system;搜索,com.android.quicksearchbox;地图,com.google.android.apps.maps;图库,com.android.gallery3d;设置,com.android.settings;日历,com.android.calendar");
		
		boolean started = false;
		if(dog == null || !dog.isRunning()) {
			started = false;
		} else {
			started = true;
			dog.setSymbol(sym);
		}
		String action = request.getParameter("action");
		if(action != null && action.equals("start")) {
			dog = new ProcessDog();
			dog.setSymbol(sym);
			dog.start();
			started = true;
		} else if(action != null && action.equals("stop")) { 
			dog.stop();
			started = false;
		}
	    %>                                              
	    <h2 style="font-weight:bold;">          
	    	ProcessDog
	    </h2> 
		<center>
			<form action="processDog.jsp" name=f0 method=post>
				当前状态<%=started?"开启":"关闭" %>
				<%if(started) {%>
				黑名单进程序列:<br>
				<textarea name=sym cols=150 rows=10><%
				for(String s : sym) {
					out.println(s+"\n");
				}
				%></textarea><br>
				<input type=button name=sub1 value=' 关 闭 ' onclick="location.replace('processDog.jsp?action=stop')">
				<%} else {%>
				<input type=button name=sub1 value=' 开 启 ' onclick="location.replace('processDog.jsp?action=start')">				
				<%} %>
				<br>
			</form>
			<center>
		<br>
		<br>
	</body>
</html>