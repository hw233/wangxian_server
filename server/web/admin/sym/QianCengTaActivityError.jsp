<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaActivity_RefDao"%>
<%@page import="com.fy.engineserver.util.StringTool"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.InputStream"%>
<%@page import="com.fy.engineserver.util.config.ConfigServiceManager"%>
<%@page import="java.io.File"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Set"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Dao"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_FlopSet"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_DaoInfo"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Thread"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_TaInfo"%>
<%@page import="com.fy.engineserver.qiancengta.info.QianCengTa_CengInfo"%>
<%@page import="com.fy.engineserver.authority.AuthorityAgent"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTa_Ta"%>
<%@page import="com.fy.engineserver.qiancengta.QianCengTaManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	String p = QianCengTaManager.getInstance().getActivityFile();
	p = p.substring(0, p.lastIndexOf("/")) + "/activity/"+p.substring(p.lastIndexOf("/"));
	File file = new File(p);
	file = new File(ConfigServiceManager.getInstance().getFilePath(file));
	InputStream is = new FileInputStream(file);
	POIFSFileSystem pss = new POIFSFileSystem(is);
	HSSFWorkbook workbook = new HSSFWorkbook(pss);
	HSSFSheet sheet1 = workbook.getSheetAt(0);
	for (int i = 1; i < sheet1.getPhysicalNumberOfRows(); i++) {
		HSSFRow row = sheet1.getRow(i);
		int nandu = StringTool.getCellValue(row.getCell(0), Integer.class);
		String startTime = StringTool.getCellValue(row.getCell(1), String.class);
		String endTime = StringTool.getCellValue(row.getCell(2), String.class);
		int platformType = StringTool.getCellValue(row.getCell(3), Integer.class);
		String[] serverNames = StringTool.getCellValue(row.getCell(4), String.class).split(",");
		String mailTile = StringTool.getCellValue(row.getCell(5), String.class);
		String mailMsg = StringTool.getCellValue(row.getCell(6), String.class);
		String[] rewardNames = StringTool.getCellValue(row.getCell(7), String.class).split(",");
		String[] rewardNums_str = StringTool.getCellValue(row.getCell(8), String.class).split(",");
		int[] rewardNums = new int[rewardNums_str.length];
		for(int j = 0; j < rewardNums_str.length; j++) {
			rewardNums[j] = Integer.parseInt(rewardNums_str[j]);
		}
		String[] rewardColors_str = StringTool.getCellValue(row.getCell(9), String.class).split(",");
		int[] rewardColors = new int[rewardColors_str.length];
		for(int j = 0; j < rewardColors_str.length; j++) {
			rewardColors[j] = Integer.parseInt(rewardColors_str[j]);
		}
		String[] rewardBinds_str = StringTool.getCellValue(row.getCell(10), String.class).split(",");
		boolean[] rewardBinds = new boolean[rewardBinds_str.length];
		for(int j = 0; j < rewardBinds_str.length; j++) {
			rewardBinds[j] = Boolean.parseBoolean(rewardBinds_str[j]);
		}
		QianCengTaActivity_RefDao r1 = new QianCengTaActivity_RefDao(
				TimeTool.formatter.varChar19.parse(startTime),
				TimeTool.formatter.varChar19.parse(endTime),
				nandu, 100000, platformType, serverNames, null, mailTile, mailMsg,
				rewardNames, rewardNums, rewardColors, rewardBinds
				);
		QianCengTaManager.getInstance().refActivitys.add(r1);
	}
	%>
	<br>
	刷在线玩家BUG
	<form>
	<input type="hidden" name="action" value="ref">
	<input type="submit" value="刷数据">
	</form>
	<br>
	查询某个玩家的千层塔详细
	<form>
	<input type="hidden" name="action" value="onePlayer">
	<input type="text" name="pid">
	<input type="submit" value="查询">
	</form>
</body>
</html>