<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.ActivitySubSystem"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="jxl.Cell"%>
<%@page import="jxl.Sheet"%>
<%@page import="jxl.Workbook"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	File file = new File("/data/home/cp_mqq/resin_server/webapps/game_server/WEB-INF/game_init_config/activity/modify_tencent_activityIntroduce.xls");
if (!file.exists()) {
	throw new Exception("配置文件不存在");
}
try {
	List<ActivityIntroduce> res = new ArrayList<ActivityIntroduce>();
	Workbook workbook = Workbook.getWorkbook(file);
	Sheet sheet = workbook.getSheet(0);
	int maxRow = sheet.getRows();

	for (int i = 1; i < maxRow; i++) {
		Cell[] cells = sheet.getRow(i);
		int index = 0;
		int id = Integer.valueOf(StringTool.modifyContent(cells[index++]));
		String icon = StringTool.modifyContent(cells[index++]);
		int showType = Integer.valueOf(StringTool.modifyContent(cells[index++]));
		Integer[] weekDay = StringTool.string2Array(StringTool.modifyContent(cells[index++]), ",", Integer.class);
		String startS = StringTool.modifyContent(cells[index++]);
		String endS = StringTool.modifyContent(cells[index++]);
		Calendar end = Calendar.getInstance();
		Integer[] startTimes = StringTool.string2Array(startS, ":", Integer.class);
		Calendar start = Calendar.getInstance();
		start.set(Calendar.HOUR_OF_DAY, startTimes[0]);
		start.set(Calendar.MINUTE, startTimes[1]);
		Integer[] endTimes = StringTool.string2Array(endS, ":", Integer.class);
		end.set(Calendar.HOUR_OF_DAY, endTimes[0]);
		end.set(Calendar.MINUTE, endTimes[1]);
		String openTimeDistance = startS + "-" + endS;

		String name = (StringTool.modifyContent(cells[index++]));
		String startGameCn = (StringTool.modifyContent(cells[index++]));
		String startGameRes = "";
		String startNpc = (StringTool.modifyContent(cells[index++]));
		startNpc = startNpc == null || "".equals(startNpc) ? "" : startNpc;
		startGameCn = startGameCn == null || "".equals(startGameCn) ? "" : startGameCn;
		int startX = 0;
		int startY = 0;
		CompoundReturn cr = TaskManager.getInstance().getNPCInfoByGameAndName(startGameCn, startNpc);
		if (cr.getBooleanValue()) {
			startX = cr.getIntValues()[0];
			startY = cr.getIntValues()[1];
			startGameRes = GameManager.getInstance().getResName(startGameCn, CountryManager.国家A);
			if (startGameRes == null) {
				startGameRes = GameManager.getInstance().getResName(startGameCn, CountryManager.中立);
			}
		}
		String des = (cells[index++].getContents());
		String value = (StringTool.modifyContent(cells[index++]));
		String[] lables = new String[0];
		if (value == null || "".equals(value)) {

		} else {
			lables = StringTool.string2Array(value, ",", String.class);
		}
		int activityAdd = Integer.valueOf(StringTool.modifyContent(cells[index++]));
		String taskGroupName = StringTool.modifyContent(cells[index++]);
		int dailyNum = 0;
		if (taskGroupName == null || "".equals(taskGroupName)) {
			taskGroupName = "";
			dailyNum = 0;
		} else {
			List<Task> list = TaskManager.getInstance().getTaskGrouopMap().get(taskGroupName);
			if (list != null && list.size() > 0) {
				dailyNum = list.get(0).getDailyTaskMaxNum();
			} else {
				dailyNum = 0;
			}
		}
		int levelLimit = Integer.valueOf(StringTool.modifyContent(cells[index++]));
		String totalStartTime = (cells[index++]).getContents();
		String totalEndTime = (cells[index++]).getContents();
		String isShowAccordTime = StringTool.modifyContent(cells[index++]);
		String[] showservers = new String[0];
		value = (StringTool.modifyContent(cells[index++]));
		if (value == null || "".equals(value)) {

		} else {
			showservers = StringTool.string2Array(value, ",", String.class);
		}
		String[] limitservers = new String[0];
		value = (StringTool.modifyContent(cells[index++]));
		if (value == null || "".equals(value)) {

		} else {
			limitservers = StringTool.string2Array(value, ",", String.class);
		}
		ActivityIntroduce activityIntroduce = new ActivityIntroduce(id, icon, showType, openTimeDistance, name, startGameRes, startGameCn, startNpc, startX, startY, des, activityAdd, lables, levelLimit, taskGroupName, dailyNum, weekDay, start, end, isShowAccordTime, showservers, limitservers,TimeTool.formatter.varChar19.parse(totalStartTime),TimeTool.formatter.varChar19.parse(totalEndTime));
		ActivitySubSystem.logger.warn("[系统初始化] [活动介绍] [" + activityIntroduce.toString() + "]");
		res.add(activityIntroduce);
	}
	ActivityManager.getInstance().setActivityIntroduces(res);
	out.print("[ok]");
} catch (Exception e) {
	throw e;
}
%>