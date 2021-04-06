<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*,
com.fy.engineserver.datasource.skill.*"%><%
        PlayerManager sm = PlayerManager.getInstance();
		CareerManager cm = CareerManager.getInstance();
     
		String playerlevel = request.getParameter("playerlevel");
		if(playerlevel == null){
			playerlevel ="0";
		}else{
			try{
				Integer.parseInt(playerlevel);
			}catch(Exception e){
				playerlevel ="0";
			}
		}
		List<Player> playerList = new ArrayList<Player>();
		int playerCount = sm.getUpperLevelPlayerCount(Integer.parseInt(playerlevel));
		for(int i = 0; i*100 < playerCount; i++){
			Player[] temps = sm.getUpperLevelPlayer(i*100,100,Integer.parseInt(playerlevel));
			if(temps != null){
				for(Player p : temps){
					if(p != null){
						playerList.add(p);
					}
				}
			}
		}
		Player sprites[] = playerList.toArray(new Player[0]);
		
		
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
			
	int levels[] = new int[]{0,1,2,3,4,5};
			
%>
<%@page import="com.fy.engineserver.datasource.career.*"%>
<%@page import="com.fy.engineserver.datasource.skill.ActiveSkill"%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2><%=playerlevel %>级以上人物各个职业技能加点，人数(<%=(sprites != null ? sprites.length : 0) %>)</h2><br>

<form name="f1">
人物级别下限:<input type="text" name="playerlevel" value="<%=playerlevel %>">
<input type="submit" value="提交">
</form>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>职业</td>
<td>路线</td>
<td>技能</td>
<td>所需点数</td>
<td>最大级别</td>
<%
	for(int i = 0 ;i < levels.length ; i++){
		out.println("<td>分配"+levels[i]+"点的人数</td>");
	}
%>
</tr>
<%
        //int n = sm.getAmountOfPlayers();
		Career careers[] = cm.getCareers();
		for(int i = 0 ; i < careers.length ; i++){
			CareerThread cts[] = careers[i].getCareerThreads();
			for(int j = 0 ; j < cts.length ; j++){
				Skill as[] = cts[j].getSkills();
				for(int k = 0 ; k < as.length ; k++){
					out.println("<tr bgcolor='#FFFFFF' align='center'>");
					
					if(j == 0 && k == 0){
						int c = 0;
						for(int x = 0 ; x < cts.length ; x++){
							c+=cts[x].getSkills().length;
						}
						out.println("<td rowspan='"+c+"'>"+careers[i].getName()+"("+players[i].length+")</td>");
					}
					
					if(k == 0){
						out.println("<td rowspan='"+as.length+"'>"+cts[j].getName()+"</td>");
					}
					
					out.println("<td><a href='./skillsbylinkid.jsp?id="+as[k].getId()+"'>"+as[k].getName()+"</a></td>");
					out.println("<td>"+as[k].getNeedCareerThreadPoints()+"</td>");
					out.println("<td>"+as[k].getMaxLevel()+"</td>");
					
					int count[] = new int[levels.length];
					for(int x = 0 ; x < levels.length ; x++){
						count[x] = 0;
						for(int y = 0 ; y < players[i].length ; y++){
							Player p = players[i][y];
							if(p.getSkillLevel(as[k].getId()) == levels[x]){
								count[x] = count[x] + 1;
							}
						}
						if(count[x] == 0){
							if(as[k].getMaxLevel() >= x){
								out.println("<td bgcolor='red'>"+count[x]+"</td>");	
							}else{
								out.println("<td>--</td>");
							}
						}else{
							out.println("<td>"+count[x]+"</td>");
						}
						
					}
					
					out.println("</tr>");
				}
			}
		}
      
%>
</table>
</BODY></html>
