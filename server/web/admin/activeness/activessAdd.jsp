<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">

<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.activity.fateActivity.*"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.sprite.ActivityRecordOnPlayer"%>
<%@page import="com.fy.engineserver.newBillboard.BillboardsManager"%>
<%@page import="com.fy.engineserver.newBillboard.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.*"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="com.xuanzhi.tools.text.StringUtil"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page import="com.fy.engineserver.newBillboard.monitorLog.LogRecord"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.activity.activeness.PlayerActivenessInfo"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.util.TimeTool.TimeDistance"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.message.CAN_GET_RES"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加活跃度</title>
</head>
<body>

<%
String pwd = request.getParameter("pwd");
 if(TestServerConfigManager.isTestServer()||(pwd != null && pwd.equals("xghyd"))){
	 String playerName = request.getParameter("playerName");
		String dayActive = request.getParameter("dayActive");
		String totalActive = request.getParameter("totalActive");
		if (playerName != null && !"".equals(playerName)) {
			Player player = PlayerManager.getInstance().getPlayer(playerName);
			PlayerActivenessInfo pai = player.getActivenessInfo();
			out.print(pai.getDoneNum()+"***<br/>");
			out.print(player.getPlayerLastLoginTime());
			out.print(TimeTool.instance.isSame(player.getPlayerLastLoginTime(), System.currentTimeMillis(), TimeTool.TimeDistance.DAY));
//			PlayerActivenessInfo pai = ActivenessManager.getInstance().getPlayerActivenessInfoFromDB(player);
			if (player != null) { 
				if (pai != null) {
					ActivenessManager am = ActivenessManager.getInstance();
					if (dayActive != null && !"".equals(dayActive)) {
						if ((pai.getDayActiveness() + Integer.valueOf(dayActive)) <= am.getDayActivenessLimit()) {
							pai.setDayActiveness(pai.getDayActiveness() + Integer.valueOf(dayActive));
							ActivitySubSystem.logger.info(player.getLogString() + "[后台增加单日活跃度:" + dayActive + "] [增加后单日活跃度:" + pai.getDayActiveness() + "]");
							//TODO log加在billingCenter里面
							for(int i=0;i<pai.getGotten().length;i++){
								if(pai.getGotten()[i]==false&&pai.getDayActiveness()>=am.getAwardLevel()[i]){
									CAN_GET_RES canGetRes = new CAN_GET_RES();
									canGetRes.setCanGet(true);
									player.addMessageToRightBag(canGetRes);
								}
							}
						}else{
							pai.setDayActiveness(am.getDayActivenessLimit());
							ActivitySubSystem.logger.info(player.getLogString() + "[单日活跃度达到上限] [增加后单日活跃度:" + pai.getDayActiveness() + "]");
						}
						pai.setTotalActiveness(pai.getTotalActiveness() + Integer.valueOf(dayActive));
						ActivitySubSystem.logger.warn(player.getLogString() + "[后台添加单日活跃度:" + dayActive + "]");
//						out.print("添加单日活跃度成功!");
					}
					if (totalActive != null && !"".equals(totalActive)) {
						pai.setTotalActiveness(pai.getTotalActiveness() + Integer.valueOf(totalActive));
						ActivitySubSystem.logger.warn(player.getLogString() + "[后台添加总活跃度:" + totalActive + "]");
//						out.print("添加总活跃度成功!");
					}
					
					int canLottery = 0;
					int n = 0;
					boolean temp = true;
					if(pai.getDayActiveness()<am.getLotteryLevel()[0]){
						ActivitySubSystem.logger.warn(player.getLogString() + "[不满足抽奖]" );
					}else{
						for (int i = 1; i < am.getLotteryLevel().length; i++) {
							if (pai.getDayActiveness() < am.getLotteryLevel()[i]) {
								n = i-1;
								ActivitySubSystem.logger.warn(player.getLogString() + "[n："+n+"]" );
								temp = false;
								break;
							}
						}
						int size = temp ? am.getLotteryTimes().length-1:n;
						canLottery = am.getLotteryTimes()[size];
					}
					ActivitySubSystem.logger.warn(player.getLogString() + "[抽奖次数：]" + Arrays.toString(am.getLotteryTimes()));
					pai.setCanLottery(canLottery);
					ActivitySubSystem.logger.warn(player.getLogString() + "[活跃度更新可抽奖次数："+canLottery+"]" );
					out.print("单日活跃度：" + pai.getDayActiveness() + "<br/>总活跃度：" + pai.getTotalActiveness()+ "<br/>已抽奖次数：" + pai.getHasLottery()+ "<br/>可抽奖次数：" + pai.getCanLottery());
				} else {
					out.print("该玩家活跃度信息不存在!");
				}
			} else {
				out.print("该玩家不存在!");
			}
		}
 }else{
	 out.print("只有测试服才可设置");
 }
	
%>

<form action="">角色名：<input type="text" name="playerName" />
add单日活跃度:<input type="text" name="dayActive" /> add总活跃度：<input
	type="text" name="totalActive" /> <input type="submit" value="submit" /></form>
</body>

</html>
