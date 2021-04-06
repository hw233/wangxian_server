<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*"%><%
        PlayerManager sm = PlayerManager.getInstance();
		CareerManager cm = CareerManager.getInstance();
     
		Player sprites[] = sm.getCachedPlayers();
		
		
		Arrays.sort(sprites,new Comparator<Player>(){
			public int compare(Player p1,Player p2){
				if(p1.getLevel() > p2.getLevel()) return -1;
				if(p1.getLevel() < p2.getLevel()) return 1;
				return 0;
			}
		});

			Player players[][] = new Player[cm.getCareers().length][];
			for(int i = 0 ; i < players.length ; i++){
				ArrayList<Player> al = new ArrayList<Player>();
				for(Player p : sprites){
					if(p.getCareer() == i){
						al.add(p);
					}
				}
				players[i] = al.toArray(new Player[0]);
			}
			
	int levels[] = new int[]{50,45,40,35,30,25,20};
			
%>
<%@page import="com.fy.engineserver.datasource.career.*"%>
<%@page import="com.fy.engineserver.datasource.skill.ActiveSkill"%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>各个职业技能伤害公式</h2><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>职业</td>
<td>路线</td>
<td>主动技能</td>
<td>加点</td>
<td>级别</td>
<td>CD</td>
<td>公式</td>
<%
	for(int i = 0 ;i < levels.length ; i++){
		out.println("<td>"+levels[i]+"级</td>");
	}
%>
</tr>
<%
        //int n = sm.getAmountOfPlayers();
		Career careers[] = cm.getCareers();
		for(int i = 0 ; i < careers.length ; i++){
			CareerThread cts[] = careers[i].getCareerThreads();
			for(int j = 0 ; j < cts.length ; j++){
				ActiveSkill as[] = cts[j].getActiveSkills();
				for(int k = 0 ; k < as.length ; k++){
					out.println("<tr bgcolor='#FFFFFF' align='center'>");
					
					if(j == 0 && k == 0){
						int c = 0;
						for(int x = 0 ; x < cts.length ; x++){
							c+=cts[x].getActiveSkills().length;
						}
						out.println("<td rowspan='"+c+"'>"+careers[i].getName()+"</td>");
					}
					
					if(k == 0){
						out.println("<td rowspan='"+as.length+"'>"+cts[j].getName()+"</td>");
					}
					
					out.println("<td><a href='./skillsbylinkid.jsp?id="+as[k].getId()+"'>"+as[k].getName()+"</a></td>");
					out.println("<td>"+as[k].getNeedCareerThreadPoints()+"</td>");
					out.println("<td>"+as[k].getMaxLevel()+"</td>");
					out.println("<td>"+(as[k].getInterval()/1000f)+"</td>");
					//out.println("<td>武器伤害 * ("+as[k].getParam1() + " + " +as[k].getParam2()+ " * 技能等级"
					//		+") + 攻击强度 * (" + as[k].getParam3() + " + " + as[k].getParam4() +" * 技能等级" 
					//		+") + 法术强度 * (" + as[k].getParam5() + " + " + as[k].getParam6() +" * 技能等级" 
					//		+") + (" +as[k].getParam7() +" + " + as[k].getParam8() + " * 技能等级)</td>");
					//	武器伤害*（1.0+0.4*技能等级） + 攻击强度*0.33 + 10.0 + 10.0*技能等级
					StringBuffer sb = new StringBuffer();
					if(as[k].getParam1() > 0 || as[k].getParam2() > 0){
						sb.append("武器伤害 * ");
						if(as[k].getParam1() > 0 && as[k].getParam2() > 0){
							sb.append("("+as[k].getParam1() + " + " +as[k].getParam2()+ " * 技能等级)");
						}else if(as[k].getParam1() > 0){
							sb.append(""+as[k].getParam1());
						}else {
							sb.append(""+as[k].getParam2()+ " * 技能等级");
						}
					}
					
					if(as[k].getParam3() > 0 || as[k].getParam4() > 0){
						if(sb.length() > 0) sb.append(" + ");
						sb.append("攻击强度 * ");
						if(as[k].getParam3() > 0 && as[k].getParam4() > 0){
							sb.append("("+as[k].getParam3() + " + " +as[k].getParam4()+ " * 技能等级)");
						}else if(as[k].getParam3() > 0){
							sb.append(""+as[k].getParam3());
						}else {
							sb.append(""+as[k].getParam4()+ " * 技能等级");
						}
					}
					
					if(as[k].getParam5() > 0 || as[k].getParam6() > 0){
						if(sb.length() > 0) sb.append(" + ");
						sb.append("法术强度 * ");
						if(as[k].getParam5() > 0 && as[k].getParam6() > 0){
							sb.append("("+as[k].getParam5() + " + " +as[k].getParam6()+ " * 技能等级)");
						}else if(as[k].getParam5() > 0){
							sb.append(""+as[k].getParam5());
						}else {
							sb.append(""+as[k].getParam6()+ " * 技能等级");
						}
					}
					
					if(as[k].getParam7() > 0 || as[k].getParam8() > 0){
						if(sb.length() > 0) sb.append(" + ");
						
						if(as[k].getParam7() > 0 && as[k].getParam8() > 0){
							sb.append("("+as[k].getParam7() + " + " +as[k].getParam8()+ " * 技能等级)");
						}else if(as[k].getParam7() > 0){
							sb.append(""+as[k].getParam7());
						}else {
							sb.append(""+as[k].getParam8()+ " * 技能等级");
						}
					}
					out.println("<td align='left'>"+sb+"</td>");
					

					int damages1[] = new int[levels.length];
					int damages2[] = new int[levels.length];
					for(int x = 0 ; x < levels.length ; x++){
						for(int y = 0 ; y < players[i].length ; y++){
							Player p = players[i][y];
							if(p.getLevel() >= levels[x] -2 && p.getLevel() <= levels[x] + 2){
								int damage = as[k].calDamageOrHpForFighter(p,as[k].getMaxLevel());
								if(damage >= damages1[x]){
									damages1[x] = damage;
								}
								if(damages2[x] == 0 || damage <= damages2[x]){
									damages2[x] = damage;
								}
							}
						}
						out.println("<td>"+damages2[x] + "~" + damages1[x]+"</td>");
					}
					
					out.println("</tr>");
				}
			}
		}
      
%>
</table>
</BODY></html>
