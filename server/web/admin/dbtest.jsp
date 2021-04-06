<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*,com.fy.engineserver.datasource.article.data.articles.*,com.fy.engineserver.datasource.article.manager.*,com.fy.engineserver.sprite.horse.HorseManager,com.xuanzhi.tools.simplejpa.*,com.xuanzhi.tools.simplejpa.impl.*"%>


<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.horse.dateUtil.DateFormat"%>
<%@page import="com.fy.engineserver.chuangong.ChuanGongManager"%>
<%@page import="com.fy.engineserver.gametime.SystemTime"%><html>
<head>
<title>数据库测试</title>
</head>
<body>
<%! 
	int count = 0;
	long nowtime = SystemTime.currentTimeMillis();
	
	int countNum = 10*60*24*10;
	
	Thread thread = null;
	
	public static boolean hasRunning = false;
	
	public void doTest() throws Exception {
		PlayerManager pm = PlayerManager.getInstance();
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		SimpleEntityManager<Player> em = SimpleEntityManagerFactory.getSimpleEntityManager(Player.class);
		long[] ids = em.queryIds(Player.class, " username like 'ROBOT%'", "username desc", 1, 10000);
		for(long id : ids) {
			Player player = pm.getPlayer(id);
			if(player != null) {
				if(player.getKnapsack_common().getCells().length < 80) {
					player.getKnapsack_common().addCells(80);
				}
				int ecount = player.getKnapsack_common().getEmptyNum();
				if(ecount > 0) {
					String aname = "朱雀笔";
					Article a = am.getArticle(aname);
					ArticleEntity ae = aem.createEntity(a, false, ArticleEntityManager.CREATE_REASON_OTHER, player, 6, 1, true);
					player.getKnapsack_common().put(ae, "测试");
				}else{
					ecount = player.getKnapsack_common().getCells().length;
					for(int i = 0 ; i < ecount ; i++){
						player.getKnapsack_common().clearCell(i,"测试",false);
					}
				}
				em.notifyFieldChange(player, "knapsacks_common");
				
				List<Long> nextCanAcceptTasks = player.getNextCanAcceptTasks();
				if(nextCanAcceptTasks.size() < 100) {
					nextCanAcceptTasks.add(new java.util.Random().nextLong());
				}
				em.notifyFieldChange(player, "nextCanAcceptTasks");
				em.notifyFieldChange(player, "highWaterOfSilver");
				em.notifyFieldChange(player, "totalChongzhiRMB");
				em.notifyFieldChange(player, "lastRequestTime");
				em.notifyFieldChange(player, "caveId");
				em.notifyFieldChange(player, "lastExchangeForluckFriteTime");
				em.notifyFieldChange(player, "lastExchangeSilkwormTime");
				em.notifyFieldChange(player, "currentExchangeSilkwormTimes");
				em.notifyFieldChange(player, "lastUpdateFavorDegreesTime");
				em.notifyFieldChange(player, "durationOnline");
			}
		}
	}
%><%
	String action = request.getParameter("action");

	if(action != null && action.equals("start")){

	thread = new Thread(new Runnable(){
			
			public void run(){
				hasRunning =  true;
				while(hasRunning && count < countNum){
					
					try{
						Thread.sleep(100*1000);
						doTest();
						count++;
					}catch(Exception e){
						e.printStackTrace();
					}
					if(hasRunning == false)
						break;
					
				}
				hasRunning = false;
				thread = null;
			}
		});
	
		thread.setName("数据库测试");
		thread.start();
	}else if(action != null && action.equals("stop")){
		hasRunning = false;
		thread.interrupt();
	}	
	%>
<%
	if(thread == null){
		out.println("线程还没有启动，<a href='./dbtest.jsp?action=start'>点击这里</a>启动线程! <a href='./dbtest.jsp'>点击这里</a>刷新");
	}else{
		out.println("线程真正运行，已执行"+count+"次循环，<a href='./dbtest.jsp?action=stop'>点击这里</a>停止线程! <a href='./dbtest.jsp'>点击这里</a>刷新");
	}
%>	
</body>

</html>
