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
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivityForActiveness"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>修复玩家活跃度信息</title>
</head>
<body>

<%

	String playerName = request.getParameter("playerName");
	if (playerName != null && !"".equals(playerName)) {
		Player player = PlayerManager.getInstance().getPlayer(playerName);
		PlayerActivenessInfo pai = player.getActivenessInfo();
		if (player != null) { 
			String fix = request.getParameter("fix");
			if(fix.equals("")||fix == null){
				
			}else if(fix.equals("donenum")){
				ActivenessManager am = ActivenessManager.getInstance();
				if (pai.getDoneNum() == null && am != null) {
					Map<Integer, Integer> doneNum = new ConcurrentHashMap<Integer, Integer>();
					for (ActivityForActiveness afa : am.getActivityForActivenesses()) {
						doneNum.put(afa.getId(), 0);
					}
					pai.setDoneNum(doneNum);
				}
				out.print(pai.getDoneNum()+"***<br/>");
				out.print(player.getPlayerLastLoginTime());
				out.print(TimeTool.instance.isSame(player.getPlayerLastLoginTime(), System.currentTimeMillis(), TimeTool.TimeDistance.DAY));
			}else if(fix.equals("getsignaward")){
				ActivenessManager am = ActivenessManager.getInstance();
				if (pai.getHasGotSign() != null && am != null) {
					boolean[] getSign = new boolean[am.getSignAwardLevel().length];
					pai.setHasGotSign(getSign);
				}
				out.print(Arrays.toString(pai.getHasGotSign()));
			}else if(fix.equals("sign")){
				ActivenessManager am = ActivenessManager.getInstance();
				if (pai.getHasSign() != null && am != null) {
					boolean[] hasSign = new boolean[am.getCalenderInfo(System.currentTimeMillis())[1]];
					pai.setHasSign(hasSign);
				}
				out.print(Arrays.toString(pai.getHasSign()));
			}
		} else {
			out.print("该玩家不存在！");
		}
	}
%>

<form action="">角色名：<input type="text" name="playerName" /><input type="submit" value="submit" /></form>
</body>

</html>
