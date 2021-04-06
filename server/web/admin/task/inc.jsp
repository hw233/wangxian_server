<%@ page import="com.fy.engineserver.newtask.*"%>
<%@ page import="com.fy.engineserver.newtask.service.*"%>
<%@ page import="com.fy.engineserver.newtask.actions.*"%>
<%@ page import="com.fy.engineserver.newtask.prizes.*"%>
<%@ page import="com.fy.engineserver.newtask.targets.*"%>
<%@ page import="com.fy.engineserver.newtask.timelimit.*"%>
<%@ page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@ page import="com.fy.engineserver.sprite.Player"%>
<%@ page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@ page import=" com.fy.engineserver.newtask.service.TaskConfig.TargetType"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%!
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String timeToString (long time) {
		Date date = new Date();
		date.setTime(time);
		return sdf.format(date);
	}
%>