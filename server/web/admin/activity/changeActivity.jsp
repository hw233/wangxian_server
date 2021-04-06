<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.activity.ActivityIntroduce"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	Map<Integer,String> map = new HashMap<Integer,String>();
	map.put(1,"<f color='0xff0000'>【充值返紅包】</f>：2月6日維護後至2月9日期間，儲值499兩、9錠999兩可獲得不同獎勵哦.\n1、<f color='0xFDA700'>首次儲值滿499兩得“飛黃騰達”禮包</f>，內含酒票*5，飛行坐騎禮包（3天）及浮華錦羅仙囊。\n2、<f color='0xFDA700'>單筆儲值滿9錠999兩得“金玉滿堂”禮包</f>，為3級寶石袋，內含隨機3級寶石兩塊。\n<f color='0xff0000'>【獎勵】</f>：<f color='0xff0066FF'>銀子</f>\n<f color='0xff0000'>【參與等級】</f>：個人等級到達10級\n<f color='0xff0000'>【友情提示】</f>:禮包會發送至郵箱，請注意查收。");
	map.put(3,"<f color='#e86db2'>【豪飲賀新春】</f>：2月7日至11日：新春佳節，飘渺寻仙曲與玩家同樂！使用酒或屠魔帖，經驗增加10%（不與其他效果疊加）\n<f color='#e86db2'>【獎勵】</f>：海量經驗\n<f color='#e86db2'>【參與等級】</f>：個人等級達到20級。");
	map.put(5,"<f color='0xff0000'>【新春VIP搶購季】</f>：2月9日至14日：節日期間，部分稀有道具放在<f color='0xff0066FF'>商城及VIP商城（V3以上）</f>，八折銷售，每天限量哦！\n<f color='0xff0000'>【獎勵】</f>：<f color='0xE706EA'>靈獸內丹</f>、<f color='0xE706EA'>煉星符</f>、<f color='0xffffff'>5級寶石</f>\n<f color='0xff0000'>【參與等級】</f>：個人等級到達1級");
	List<ActivityIntroduce> list = ActivityManager.getInstance().getActivityIntroduces();
	for (ActivityIntroduce ai : list) {
		int id = ai.getId();
		if (map.containsKey(id)) {
			if (id == 1) {
				ai.setName("充值返禮包");
				ai.setStartNpc("");
			}
			ai.setDes(map.get(id));
			out.print(id + " : " + ai.getDes());
		}
	}
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>