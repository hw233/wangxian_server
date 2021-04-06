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
<%@page
	import="com.fy.engineserver.newBillboard.monitorLog.LogRecordManager"%>
<%@page
	import="com.fy.engineserver.newBillboard.monitorLog.LogRecord"%>
<%@page import="java.util.Map.Entry"%>
<%@page
	import="com.fy.engineserver.activity.activeness.PlayerActivenessInfo"%>
<%@page
	import="com.fy.engineserver.activity.activeness.ActivenessManager"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.util.TimeTool.TimeDistance"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.message.CAN_GET_RES"%>
<%@page import="com.fy.engineserver.activity.activeness.ActivityForActiveness"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.activity.dig.DigManager"%>
<%@page import="com.fy.engineserver.activity.dig.DigTemplate"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>查看玩家活跃度</title>
</head>
<body>

<%
	String playerName = request.getParameter("playerName");
	if (playerName != null && !"".equals(playerName)) {
		Player player = PlayerManager.getInstance().getPlayer(playerName);
		PlayerActivenessInfo pai = player.getActivenessInfo();
		ActivenessManager am = ActivenessManager.getInstance();
		if(pai!=null){
			out.print("玩家签到信息:"+Arrays.toString(pai.getHasSign())+"当月天数："+pai.getHasSign().length+"<br/>");
			out.print("玩家领取签到奖励信息:"+Arrays.toString(pai.getHasGotSign())+"<br/>");
			out.print("玩家单日活跃度:"+pai.getDayActiveness()+"<br/>");
			out.print("玩家总活跃度:"+pai.getTotalActiveness()+"<br/>");
			out.print("领奖情况:"+Arrays.toString(pai.getGotten())+"<br/>");
			out.print("已抽奖次数:"+pai.getHasLottery()+"<br/>");
			out.print("可抽奖次数:"+pai.getCanLottery()+"<br/>");
			Map<Integer,Integer>  doneNum = pai.getDoneNum();
			Set<Integer> cids = doneNum.keySet();
			Iterator<Integer> iter = cids.iterator();
			out.print("任务--今日完成次数--满足条件次数<br/>");
			while(iter.hasNext()){
				int id = iter.next();
				for(ActivityForActiveness afa:am.getActivityForActivenesses()){
					if(id == afa.getId()){
						out.print(afa.getName()+"--"+doneNum.get(id)+"--"+afa.getTimesLimit()+"<br/>");
					}
				}
			}
		} else {
			out.print("该玩家活跃度信息不存在!");
		}
		out.print("--------------玩家挖宝图信息----------------<br/>");
		Map<Long,DigTemplate> digInfo = player.getDigInfo();
		if(digInfo!=null){
			Set<Long> ids = digInfo.keySet();
			Iterator<Long> iter = ids.iterator();
			out.print("挖宝图id--地图名--坐标<br/>");
			while(iter.hasNext()){
				long id = iter.next();
				out.print(id+"--"+digInfo.get(id).getMapName()+"--("+digInfo.get(id).getPoints().x+","+digInfo.get(id).getPoints().y+")<br/>");
			}
		}
	} else {
		out.print("该玩家不存在!");
	}
%>

<form action="">
	角色名：<input type="text" name="playerName" />
	 <input	type="submit" value="submit" /></form>
</body>

</html>
