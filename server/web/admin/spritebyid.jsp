<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,
com.fy.engineserver.datasource.skill.*,java.lang.reflect.*,com.google.gson.*,com.fy.engineserver.sprite.monster.*"%><%! 
	
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
</HEAD>
<BODY>
<h2>怪数据</h2><br>
	<%
	Gson gson = new Gson();
	String id = request.getParameter("sid");
	Monster sprite = sm.getMonster(new Integer(id).intValue());
			
			if(sprite != null){
			Method ms[] = sprite.getClass().getMethods();
			
	  		ArrayList<Method> al = new ArrayList<Method>();
	  		for(int j = 0 ; j < ms.length ; j++){
	  			if(ms[j].getName().length() > 3 && (ms[j].getName().startsWith("get") || ms[j].getName().startsWith("is")) 
	  					&& (ms[j].getModifiers() & Modifier.PUBLIC) != 0
	  					&& ms[j].getParameterTypes().length == 0){
	  				String name = ms[j].getName();
	  				//属性为布尔值时
	  				if(name.indexOf("is") == 0){
	  					name = "set" + name.substring(2);
	  				}else{
	  					name = "s" + name.substring(1);
	  				}
	  				Class c = ms[j].getReturnType();
	  				if(c.isPrimitive() || (c == String.class) || (c.toString().indexOf("class [") >= 0)){
	  					try{
	  						Method m = sprite.getClass().getMethod(name,new Class[]{c});
	  						if(m != null){
	  							al.add(ms[j]);
	  						}
	  					}catch(Exception e){
	  						
	  					}
	  				}
	  			}
	  		}
	%>
		<table cellspacing="1" cellpadding="0" border="0" bgcolor="black">

  		<%
  		int alSize = al.size();
  		for(int j = 0; j < alSize; j+=4){
  			%>
  			<tr bgcolor="#C2CAF5" align="center">
  			<%
  			for(int k = 0; k < 4; k++){
  				if((j+k)>=alSize){
  					for(;k<4;k++){
  						%>
  						<td>&nbsp;</td>
  						<%
  					}
  					break;
  				}
  				Method method = al.get(j+k);
  		  		String name ="";
  		  		if(method.getName().indexOf("is") == 0){
  		  			name = method.getName().substring(2);
  		  		}else{
  		  			name = method.getName().substring(3);
  		  		}
  		  		name = Character.toLowerCase(name.charAt(0)) + name.substring(1);
  		  		%>
  				<td width="160" align="center" style="word-wrap: break-word;">
  				<%= name%>
  				
  				</td>
  			<%
  			}%>
			</tr>
			<tr bgcolor="#FFFFFF" align="center">
			
			<%
	  		for(int k = 0; k < 4; k++){
  				if((j+k)>=alSize){
  					for(;k<4;k++){
  						%>
  						<td>&nbsp;</td>
  						<%
  					}
  					break;
  				}
	  			Method method = al.get(j+k);
				if(method.getReturnType().getName().indexOf("[") == 0){
					%>
					<td width="160" align="center" style="word-wrap: break-word;"><%= gson.toJson(method.invoke(sprite))%></td>
					
					<%
				}else{
				%>
				<td><%= method.invoke(sprite)%></td>
				<% 
				}
	  		}
  			%>
			</tr>
		<%} %>
		</table>
<%
		}else{
			out.println("怪物列表中没有编号为该id的怪物");
		}
%>	
</BODY></html>
