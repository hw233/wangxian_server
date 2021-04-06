<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,java.lang.reflect.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.google.gson.*,com.fy.engineserver.datasource.career.*,com.fy.engineserver.datasource.skill.*,com.xuanzhi.boss.game.*"%>
<%
try{
	Gson gson = new Gson();
	PlayerManager sm = PlayerManager.getInstance();
	TaskManager tm = TaskManager.getInstance();
	String action = request.getParameter("action");
	long playerId = -1;
	String playerName = null;
	Object obj = session.getAttribute("playerid");
	Object obj2 = session.getAttribute("playerName");
	if(obj != null){
		playerId = Long.parseLong(obj.toString());
	}
	if(obj2 != null){
		playerName = obj2.toString();
	}
	Player player = null;
	String errorMessage = null;

	if (action == null) {
		if (errorMessage == null) { 
			if(playerId != -1){
				player = sm.getPlayer(playerId);
				if (player == null) {
					errorMessage = "ID为" + playerId + "的玩家不存在！";
				}
			}else if(playerName != null){
				player = sm.getPlayer(playerName);
				if (player == null) {
					errorMessage = "ID为" + playerId + "的玩家不存在！";
				}
			}
		}
	}else if (action != null && action.equals("select_playerId")) {
		try {
			playerId = Long.parseLong(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}else{
				session.setAttribute("playerid",request.getParameter("playerid"));
				playerName = player.getName();
				session.setAttribute("playerName",playerName);
			}
		}
	}else if (action != null && action.equals("select_playerName")) {
		try {
			playerName = request
					.getParameter("playerName");
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符。";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerName);
			if (player == null) {
				errorMessage = "角色名为" + playerName + "的玩家不存在！";
			}else{
				session.setAttribute("playerName",request.getParameter("playerName"));
				playerId = player.getId();
				session.setAttribute("playerid",playerId);
			}
		}
	} else if ((action != null && action.equals("modify_player"))
			|| (action != null && action.equals("playingMode"))
			|| (action != null && action.equals("modifyScheme"))) {
		try {
			playerId = Long.parseLong(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}
		}
		if (errorMessage == null) {
			session.setAttribute("playerid",request.getParameter("playerid"));
			Enumeration em = request.getParameterNames();
			try {
				while (em.hasMoreElements()) {
					String name = (String) em.nextElement();
					String value = request.getParameter(name);
					if (!name.equals("action")
							&& !name.equals("playerid")
							&& value != null
							&& value.trim().length() > 0
							&& !value.equals("null")) {
						//name = name.substring(5);
						Class cl = Player.class;
						Method m = cl.getMethod("get"
								+ Character.toUpperCase(name.charAt(0))
								+ name.substring(1), new Class[] {});
						Class c = m.getReturnType();
						Object o = null;
						if (c == String.class) {
							o = value;
						} else if (c == Byte.TYPE) {
							o = Byte.parseByte(value);
						} else if (c == Short.TYPE) {
							o = Short.parseShort(value);
						} else if (c == Integer.TYPE) {
							o = Integer.parseInt(value);
						} else if (c == Long.TYPE) {
							o = Long.parseLong(value);
						} else if (c == Integer.TYPE) {
							o = Integer.parseInt(value);
						} else if (c == Double.TYPE) {
							o = Double.parseDouble(value);
						} else if (c == Character.TYPE) {
							o = value.charAt(0);
						} else if (c == Boolean.TYPE) {
							o = value.equalsIgnoreCase("true");
						} else if (c.getName().indexOf("[") >= 0) {
							if (name.equals("skillLevels")) {
								//String[] str = request
								//		.getParameterValues("skillLevels");
								//byte[] sls = player.getSkillLevels();
								//if (str != null) {
								//	for (int i = 0; i < str.length; i++) {
								//		sls[i] = new Byte(str[i])
								//				.byteValue();
								//	}
								//}
								//o = sls;

							} else {
								try{
								o = gson.fromJson(value, c);
								}catch(Exception e){
									throw new Exception("JSON解析出错：类型为"+c+",值为" + value);
								}
							}

						} else {
							Constructor construct = c
									.getConstructor(new Class[] { String.class });
							o = construct
									.newInstance(new Object[] { value });
						}
						Object oldO = m.invoke(player, new Object[] {});
						m = cl.getMethod("set"
								+ Character.toUpperCase(name.charAt(0))
								+ name.substring(1), new Class[] { c });
						if (name.equals("exp")) {
							value = request.getParameter("exp");
							if (value != null
									&& value.trim().length() > 0) {
								//2表示其他途径获得经验
								int addValue = new Integer(value)
										.intValue();
								player.addExp(addValue, 2);
							}

						} else {
							m.invoke(player, new Object[] { o });
						}
						if (errorMessage == null)
							errorMessage = "";
						errorMessage += "玩家[" + player.getName()
								+ "]的属性[" + name + "]值从{" + oldO
								+ "}被设置为{" + o + "}\n";
					}
				}
				sm.updatePlayer(player);
			} catch (Exception e) {
				ByteArrayOutputStream bout = new ByteArrayOutputStream();
				e.printStackTrace(new PrintStream(bout));
				errorMessage = new String(bout.toByteArray());
			}
		}
	} else if (action != null && action.equals("openAuro")) {
		try {
			playerId = Long.parseLong(request
					.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}
		}
		if (errorMessage == null) {
			session.setAttribute("playerid",request.getParameter("playerid"));
			byte auroIndex = Byte.parseByte(request
					.getParameter("openAuro"));
			String str = request.getParameter("flag");
			if ("open".equals(str)) {
				player.openAura(auroIndex);
			}
			if ("close".equals(str)) {
				player.closeAura();
			}
		}

	}else if(action != null && action.equalsIgnoreCase("addTaskByTaskName")){
		try {
			playerId = Long.parseLong(request.getParameter("playerid"));
		} catch (Exception e) {
			errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
		}
		if (errorMessage == null) {
			player = sm.getPlayer(playerId);
			if (player == null) {
				errorMessage = "ID为" + playerId + "的玩家不存在！";
			}
		}
		int taskId = Integer.parseInt(request.getParameter("taskId"));
		if (errorMessage == null) {

		}
	}
%>

<html><head>
<SCRIPT   LANGUAGE="JavaScript"> 
  function operation(flag){
	  if(flag){
	  	document.f3.flag.value ="open";
	  }else{
		document.f3.flag.value ="close";
	  }
  	  document.f3.submit();
  }     
  function modifyScheme(){
	  var obj0 = document.getElementById('scheme0'); //selectid
	  var value0 = obj0.options[obj0.options.selectedIndex].value;
	  //var value0 = obj0.options.value; // 选中值,也可以是  obj.options[index].value
	  var obj1 = document.getElementById('scheme1'); //selectid
	  var value1 = obj1.options[obj1.options.selectedIndex].value;
	 
	  var obj2 = document.getElementById('scheme2'); //selectid
	   var value2 = obj2.options[obj2.options.selectedIndex].value;
	  var obj3 = document.getElementById('scheme3'); //selectid
	   var value3 = obj3.options[obj3.options.selectedIndex].value;
	  var obj4 = document.getElementById('scheme4'); //selectid
	   var value4 = obj4.options[obj4.options.selectedIndex].value;
	  var obj5 = document.getElementById('scheme5'); //selectid
	   var value5 = obj5.options[obj5.options.selectedIndex].value;
	  var obj6 = document.getElementById('scheme6'); //selectid
	   var value6 = obj6.options[obj6.options.selectedIndex].value;
	   var obj7 = document.getElementById('scheme7'); //selectid
	   var value7 = obj6.options[obj6.options.selectedIndex].value;
	    var obj8 = document.getElementById('scheme8'); //selectid
	   var value8 = obj6.options[obj6.options.selectedIndex].value;
	  var schemes = "["+value0+","+value1+","+value2+","+value3+","+value4+","+value5+","+value6+","+value7+","+value8+",0,0,0,0,0,0,0]";
	  document.f21.scheme.value=schemes;
  	  document.f21.submit();
  } 
  //-->   
  </SCRIPT>
  <style type="text/css">
  
  <!--
  .tdsmallwidth{width:120px}
  .tdbigwidth{width:360px}
  
  -->
  
  </style>
</HEAD>
<BODY>

</BODY></html>
