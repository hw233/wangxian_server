<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.economic.thirdpart.migu.MiGuTradeServiceWorker"%>
<%@page import="com.fy.boss.platform.migu.MiguWorker"%>
<%@page import="com.fy.boss.message.appstore.AppStoreKunLunSavingManager"%>
<%@page import="com.xuanzhi.tools.text.ParamUtils"%>
<%@page import="com.fy.boss.message.appstore.AppStoreSavingManager"%>
<%@page import="com.fy.boss.message.appstore.AppStoreGuojiSavingManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="com.fy.engineserver.seal.SealCityBossInfo"%>
<%@page import="com.fy.engineserver.seal.data.Seal"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
int currSealLevel = SealManager.getInstance().getSealLevel();
int currSealStep = SealManager.getInstance().getSeal().sealStep;
out.print("currSealLevel:"+currSealLevel+"<br>");
out.print("currSealStep:"+currSealStep+"<br>");
out.print("解封时间:"+TimeTool.formatter.varChar23.format(SealManager.getInstance().getSeal().getSealCanOpenTime())+"<br>");

long lastStepAllCutTime = 0;
for (SealCityBossInfo info : SealManager.getInstance().infos) {
	if (info.sealLevel == currSealLevel) {
		if(info.sealLayer <= currSealStep){
			lastStepAllCutTime += info.cutSealTime;
		}
	}
}
long currOpenSealTime = System.currentTimeMillis();
out.print("lastStepAllCutTime:"+lastStepAllCutTime+"<br>");
long lastOpenSealTime = 0;
int index = SealManager.getInstance().getIndex(currSealLevel);
index = index - 1;
if (index < 0) {
	index = 0;
}
int lastSealLevel = SealManager.takeSealTaskLevel[index];
Seal seal = SealManager.getInstance().sealMap.get(lastSealLevel);
if (seal != null) {
	lastOpenSealTime = seal.getSealCanOpenTime();
}
long value = (currOpenSealTime - lastOpenSealTime) - (180 * SealManager.一天毫秒数 - lastStepAllCutTime);
out.print("value:"+value/SealManager.一天毫秒数);


out.print("190级解封时候的时间:"+TimeTool.formatter.varChar23.format(1467252013832L)+"<br>");
out.print("当前时间:"+TimeTool.formatter.varChar23.format(currOpenSealTime)+"<br>");
out.print("上次解封时间:"+TimeTool.formatter.varChar23.format(lastOpenSealTime)+"<br>");
//SealManager.getInstance().getSeal().setSealCanOpenTime(newTIme-1);
//SealManager.getInstance().saveSeal();
SealManager.testFalg = true;
out.print(SealManager.testFalg);
%>

</body>
</html>
