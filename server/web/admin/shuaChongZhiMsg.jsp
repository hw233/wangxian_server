<%@page import="com.fy.engineserver.economic.charge.SpecialConfig"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.platform.PlatformManager"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.File"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeMode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.platform.PlatformManager.Platform"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.economic.charge.ChargeManager"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiServerConfig"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.activity.chongzhiActivity.ChongZhiActivity"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	
	if (PlatformManager.getInstance().platformOf(Platform.台湾)) {
		Field field = ChargeManager.class.getDeclaredField("channelChargeModes");
		field.setAccessible(true);
		HashMap<String, List<ChargeMode>> channelChargeModes = (HashMap<String, List<ChargeMode>>)field.get(ChargeManager.getInstance());
		File file = new File(ChargeManager.getInstance().getFilePath());
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		HSSFWorkbook workbook = new HSSFWorkbook(pss);
		
		HSSFSheet sheet = workbook.getSheetAt(0);// 所有充值通道
		int maxRow = sheet.getPhysicalNumberOfRows();
		for (int i = 1; i < maxRow; i++) {
			HSSFRow row = sheet.getRow(i);
			String userChannel = row.getCell(0).getStringCellValue().trim();
			List<ChargeMode> modes = channelChargeModes.get(userChannel);
			String footDescription = row.getCell(5).getStringCellValue();
			footDescription += "5月7日至5月10日期間，單筆儲值“滿2.5锭小於4.9锭”可得“梅蘭竹菊錦囊”1個，單筆儲值4.9锭可得道具“四君子特惠錦囊”1個（內含“梅蘭竹菊錦囊”3個），開啟道具可兌換首次現世的稀有千載難逢寵物“魔域狼蛛”哦！";
			for (ChargeMode mode : modes) {
				mode.setFootDescription(footDescription);
			}
		}
	}
	%>
</body>
</html>
