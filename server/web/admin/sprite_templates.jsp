<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*"%><%! 
	
	String aaa(String s,int len){
		StringBuffer sb = new StringBuffer();
		char chars[] = s.toCharArray();
		int c = 0;
		for(int i = 0 ; i < chars.length ; i++){
			sb.append(chars[i]);
			c++;
			if( c >= len && (chars[i] == ',' || chars[i] == '{' || chars[i] == '}' || chars[i] == ':')){
				sb.append("<br/>");
				c = 0;
			}
		}
		return sb.toString();
	}
%><% 
	
MonsterManager sm = MemoryMonsterManager.getMonsterManager();
MemoryMonsterManager.MonsterTempalte sts[] = ((MemoryMonsterManager)sm).getMonsterTemaplates();
	%>
<%@include file="IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../css/common.css" />
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<h2>怪模板数据</h2><br><a href="./sprites.jsp">查看所有的精灵</a><br><br>
<table>
	<tr class="titlecolor">
	<td>Java类</td>
	<td>类型编号</td>
	<td>怪物名</td>
	<td>称号</td>
	<td>等级</td>
	<td>血量</td>
	<td>近战攻击强度</td>
	<td>法术攻击强度</td>
	</tr>
	<%
	//为了整合统一类型的技能设置的List
	List<Monster> tempSkills = new ArrayList<Monster>();
	
	for(MemoryMonsterManager.MonsterTempalte s : sts){
		tempSkills.add(s.newMonster());
	}
	List<List<Monster>> lists = new ArrayList<List<Monster>>();
	List<Monster> list = new ArrayList<Monster>();
	List<Integer> indexList = new ArrayList<Integer>();
	while(true){
		for(int i = 0; i < tempSkills.size(); i++){
			Monster sprite = tempSkills.get(i);
			if(list.isEmpty()){
				list.add(sprite);
				indexList.add(i);
				continue;
			}
			if(list.get(0).getClass().getSimpleName().equals(sprite.getClass().getSimpleName())){
				list.add(sprite);
				indexList.add(i);
			}
			
		}
		lists.add(list);
		Integer[] ints = indexList.toArray(new Integer[0]);
		Arrays.sort(ints,new Comparator<Integer>(){
			public int compare(Integer o1, Integer o2){
				return o2.compareTo(o1);
			}
		});
		for(int index : ints){
			tempSkills.remove(index);
		}
		indexList = new ArrayList<Integer>();
		list = new ArrayList<Monster>();
		if(tempSkills.isEmpty()){
			break;
		}
	}
	
	Map<String,String> map = new HashMap<String,String>();
	map.put("StupidMonster","傻瓜型怪");
	map.put("Monster","怪物");
	map.put("","");
	if(!lists.isEmpty()){
		int listsSize = lists.size();
		for (int i = 0; i < listsSize; i++) {
			List<Monster> l = lists.get(i);
			if(!l.isEmpty()){
				%>
				<tr>
					<td rowspan="<%=l.size() %>"><%= map.get(l.get(0).getClass().getSimpleName()) %></td>
					<td><a href="spritetemplatebyid.jsp?spriteCategoryId=<%=l.get(0).getSpriteCategoryId() %>"><%= l.get(0).getSpriteCategoryId() %></a></td>
					<td><%= l.get(0).getName() %></td>
					<td><%= l.get(0).getTitle() %></td>
					<td><%= l.get(0).getLevel()%></td>
					<td><%= l.get(0).getMaxHP() %></td>
					<td><%= l.get(0).getPhyAttack() %></td>
					<td><%= l.get(0).getMagicAttack()%></td>
				</tr>
				<%
				for(int j = 1; j < l.size(); j++){
					Monster sprite = null;
					sprite = l.get(j);
			
				%>
					<tr>
						<td><a href="spritetemplatebyid.jsp?spriteCategoryId=<%= sprite.getSpriteCategoryId() %>"><%= sprite.getSpriteCategoryId() %></a></td>
						<td><%= sprite.getName() %></td>
						<td><%= sprite.getTitle() %></td>
						<td><%= sprite.getLevel()%></td>
						<td><%= sprite.getMaxHP() %></td>
						<td><%= sprite.getPhyAttack() %></td>
						<td><%= sprite.getMagicAttack()%></td>
					</tr>
				<%
				}
			}
		}
	}
%>
</table>	
</BODY></html>
