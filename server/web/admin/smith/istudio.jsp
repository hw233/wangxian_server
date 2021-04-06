<%@ page contentType="text/html;charset=utf-8"%><%@ page import="java.util.*,com.fy.engineserver.smith.*,com.xuanzhi.tools.text.*"%><%     
MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
SuspectStudio ss = msm.getSuspectStudio();
out.println(JsonUtil.jsonFromObject(ss));
%>