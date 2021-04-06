<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物品</title>
<%
long nowTime = System.currentTimeMillis();
EquipmentUpgradeMamager eum = EquipmentUpgradeMamager.getInstnace();
int strongCount = 100000;
if(eum != null){
	double[][][] PP = new double[20][][];
	for(int i = 0 ; i < PP.length ; i++){
		PP[i] = eum.calP2(eum.getP(),0.01*i,0);
	}
	double[][] P0 = null;
	int level = 0;
	long count = 0;
//	P0 = PP[0];
//	for(int i = 0; i < strongCount; i++){
//		level = 0;
//		while(level < 16){
//			count += 1;
//			level = eum.simulate_one_step(P0,level);
//		}
//	}
//	out.println("不用符的情况下，强化"+strongCount+"个装备到16的平均次数:"+count/strongCount+"<br>");
	
	level = 0;
	count = 0;
	P0 = PP[10];
	for(int i = 0; i < strongCount; i++){
		level = 0;
		while(level < 16){
			count += 1;
			level = eum.simulate_one_step(P0,level);
		}
	}
	out.println("用幸运符的情况下，强化"+strongCount+"个装备到16的平均次数:"+count/strongCount+"<br>");

	level = 0;
	count = 0;
	P0 = PP[15];
	for(int i = 0; i < strongCount; i++){
		level = 0;
		while(level < 16){
			count += 1;
			level = eum.simulate_one_step(P0,level);
		}
	}
	out.println("用超级幸运符的情况下，强化"+strongCount+"个装备到16的平均次数:"+count/strongCount+"<br>");

	level = 0;
	count = 0;
	P0 = PP[0];
	for(int i = 0; i < strongCount; i++){
		level = 0;
		int c = 0;
		while(level < 16){
			int n = c;
			if(n >= 50){
                n = (n-50)/50;
                if(n + 10 < 16)
                    P0 = PP[10+n];
                else
                    P0 = PP[15];
            }else{
                P0 = PP[10];
            }
			level = eum.simulate_one_step(P0,level);
			c++;
			count += 1;
		}
	}
	out.println("用累加幸运符的情况下，强化"+strongCount+"个装备到16的平均次数:"+count/strongCount+"<br>");

	level = 0;
	count = 0;
	P0 = eum.calP3(eum.getP(),0,0);
	for(int i = 0; i < strongCount; i++){
		level = 0;
		while(level < 16){
			level = eum.simulate_one_step(P0,level);
			count += 1;
		}
	}
	out.println("用保级符的情况下，强化"+strongCount+"个装备到16的平均次数:"+count/strongCount+"<br>");
	out.println("用时:"+(System.currentTimeMillis() - nowTime)/1000+"秒");
}
%>
</head>
<body>
</body>
</html>
