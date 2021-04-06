<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,com.fy.engineserver.gateway.*"%><%
        PlayerManager sm = PlayerManager.getInstance();
		CareerManager cm = CareerManager.getInstance();


Date date = new Date(org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(application).getStartupDate());

Player sprites[] = sm.getCachedPlayers();
Arrays.sort(sprites,new Comparator<Player>(){
	public int compare(Player p1,Player p2){
		if(p1.getLevel() > p2.getLevel()) return -1;
		if(p1.getLevel() < p2.getLevel()) return 1;
		if(p1.getMeleeAttackIntensity() + p1.getSpellAttackIntensity() > p2.getMeleeAttackIntensity() + p2.getSpellAttackIntensity()) return -1;
		if(p1.getMeleeAttackIntensity() + p1.getSpellAttackIntensity() < p2.getMeleeAttackIntensity() + p2.getSpellAttackIntensity()) return 1;
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
	
	
%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@include file="IPManager.jsp" %><html><head>
</HEAD>
<BODY>
<h2>Game Server，统计人数：<%= sprites.length %>，各个在线玩家的情况</h2><br><a href="./playersproperty.jsp">刷新此页面</a><br><br>
<table border="0" cellpadding="0" cellspacing="1" width="100%" bgcolor="#000000" align="center">
<tr bgcolor="#00FFFF" align="center">
<td>职业</td>
<td>ID</td>
<td>Name</td>
<td>账户名</td>
<td>职业</td>
<td>级别</td>
<td>力</td>
<td>敏</td>
<td>智</td>
<td>耐</td>
<td>血</td>
<td>蓝</td>
<td>武器伤害</td>
<td>攻击强度</td>
<td>法术强度</td>
<td>物理防御</td>
<td>法术防御</td>
<td>暴击率</td>
<td>命中率</td>
<td>闪避率</td>

</tr>
<%
        //int n = sm.getAmountOfPlayers();
		for(int j = 0 ; j < cm.getCareers().length ; j++){
			sprites = players[j];
  			for(int i = 0 ; i < sprites.length ; i++){
                
                %><tr bgcolor="#FFFFFF" align="center">
                
                <%  if(i == 0){
                		out.println("<td rowspan='"+sprites.length+"'>"+cm.getCareers()[j].getName()+"</td>");	
                } %>
                <td><a href='player/mod_player.jsp?action=select_playerId&playerid=<%=sprites[i].getId() %>'><%=sprites[i].getId() %></a></td>
                <td><%= sprites[i].getName()%></td>
                <td><%= sprites[i].getUsername()%></td>
                <td><%= cm.getCareer(sprites[i].getCareer()).getName() %></td>
                <td><%= sprites[i].getLevel() %></td>
                <td><%= sprites[i].getStrength() %></td>
                <td><%= sprites[i].getDexterity() %></td>
                <td><%= sprites[i].getSpellpower() %></td>
                <td><%= sprites[i].getConstitution() %></td>
                <td><%= sprites[i].getTotalHP()  %></td>
                <td><%= sprites[i].getTotalMP()  %></td>
                <td><%= sprites[i].getWeaponDamageLowerLimit() + "~" +sprites[i].getWeaponDamageUpperLimit() %></td>
                <td><%= sprites[i].getMeleeAttackIntensity() %></td>
                <td><%= sprites[i].getSpellAttackIntensity() %></td>
                <td><%= sprites[i].getPhysicalDecrease() %>%</td>
                <td><%= sprites[i].getSpellDecrease() %>%</td>
                <td><%= sprites[i].getCriticalHitPercent() %>%</td>
                <td><%= sprites[i].getAttackRatePercent() %>%</td>
                <td><%= sprites[i].getDodgePercent() %>%</td>
                <% 
        }
		}
    
%>
</table>
</BODY></html>
