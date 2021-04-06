<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,java.lang.reflect.*,com.fy.engineserver.smith.*"%>
<%
MoneyRelationShipManager msm = MoneyRelationShipManager.getInstance();
msm.set自动封禁充值额度(7000);
Class clazz = ArticleRelationShipManager.class;
Field f = clazz.getDeclaredField("自动封禁充值额度");
f.setAccessible(true);
f.set(ArticleRelationShipManager.getInstance(), 7000);
out.println("money:" + msm.get自动封禁充值额度() + ", article:" + f.getLong(ArticleRelationShipManager.getInstance()));
%>