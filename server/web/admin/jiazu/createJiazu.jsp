<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.*"%>
<%@page import="com.fy.engineserver.core.*"%>
<%@page import="com.fy.engineserver.jiazu.dao.*"%>
<%@page import="com.fy.engineserver.message.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
	function funcjs(m) {
		var t = document.getElementById("queryjiazu");
		t.value = m;
		var form1 = document.getElementById("form1");
		form1.submit();
	}
</script>
</head>
<%
	String message = null;
	Jiazu jiazu = null;
	PlayerManager playerManager = PlayerManager.getInstance();
	JiazuSubSystem jiazuSubsystem = JiazuSubSystem.getInstance();

	Player player = null;
	try {
		String playername = request.getParameter("playername");
		if (playername == null || playername.length() == 0)
			playername = "dape";
		JiazuManager manager = JiazuManager.getInstance();

		player = playerManager.getPlayer(playername);
		//player.setMoney(5000L);
		player.setLevel(41);
		String jiazuName = request.getParameter("jiazuname");
		String je = request.getParameter("queryjiazu");
		Long jiazuID = Long.parseLong(request.getParameter("jiazuID") == null ? "0" : request.getParameter("jiazuID"));
		if (jiazuName != null && jiazuName.length() > 0 && (je.equals("3"))) {
			//manager.createJiazu(player, jiazuName);
			jiazu = manager.getJiazu(jiazuName);
			String sloag = request.getParameter("slogan");
			String jiazupassword = request.getParameter("jiazuPassword");
			jiazu.setSlogan(sloag);
			jiazu.setJiazuPassword(jiazupassword);
			player.setJiazuName(jiazuName);
		} else if (jiazuName != null && jiazuName.length() > 0 && (je != null && je.equals("1"))) {
			System.out.println("query one jiazu by name");
			jiazu = manager.getJiazu(jiazuName);
		} else if (jiazuID != null && jiazuID > 0 && (je != null && je.equals("2"))) {
			System.out.println("query one jiazu by id");
			jiazu = manager.getJiazu(jiazuID);
		} else if ((je != null && je.equals("4"))) {
			System.out.println("query one jiazu by id");
			player.setJiazuName(null);
		}

	} catch (Exception e) {
		e.printStackTrace();
		message = e.getMessage();

	}
%>
<body>
	<h1 align="center">创建家族</h1>
	<%
		if (message != null) {
	%>
	<p align="center">
		出错了！原因：<%
		out.println(message);
	%>
	</p>
	<%
		}
	%>
	<form action="" method="post" id="form1">
		<input type="hidden" value="3" name="queryjiazu" id="queryjiazu"></input>
		<table align="center" width="60%" border="1">
			<tr>
				<td width="20%">家族名称:</td>
				<td width="60%"><input type="text" name="jiazuname"
					id="jiazuname"></input>
				</td>
				<td width="10%"><button type="button" onclick="funcjs(3)">创建家族</button>
				</td>
			</tr>
			<tr>
				<td>家族标语:</td>
				<td><input type='text' name='slogan'></input>
				</td>
				<td><button type="button" onclick="funcjs(4)">清除玩家家族</button>
				</td>
			</tr>
			<tr>
				<td>家族密码:</td>
				<td><input type='text' name='jiazuPassword'></input>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>玩家角色名:</td>
				<td><input type='text' name='playername'></input>
				</td>
				<td></td>
			</tr>

		</table>
	</form>
	<hr></hr>
	<h1 align="center">查询家族(1)</h1>

	<form action="" method="post" name="form2">
		<input type="hidden" value="1" name="queryjiazu"></input>
		<table align="center" width="60%" border="1">
			<tr>
				<td width="20%">家族名称:</td>
				<td width="60%"><input name="jiazuname"
					value="<%if (jiazu != null)
				out.println(jiazu.getName());%>"></input>
				</td>
				<td width="10%"><button type="submit" onclick="">查询家族</button>
				</td>
			</tr>
			<tr>
				<td>家族信息:</td>
				<td><textarea rows="15" cols="40">
						<%
							if (jiazu != null)
								out.println(jiazu.toString());
						%>
					</textarea>
				</td>
				<td></td>
			</tr>
		</table>
	</form>
	<hr></hr>
	<h1 align="center">查询家族(2)</h1>
	<form action="" method="post" name="form3">
		<input type="hidden" value="2" name="queryjiazu"></input>
		<table align="center" width="60%" border="1">
			<tr>
				<td width="20%">家族ID:</td>
				<td width="60%"><input name="jiazuID"
					value="<%if (jiazu != null)
				out.println(jiazu.getJiazuID());%>"></input>
				</td>
				<td width="10%"><button type="submit" onclick="">查询家族</button>
				</td>
			</tr>
			<tr>
				<td>家族信息:</td>
				<td><textarea rows="15" cols="40">
						<%
							if (jiazu != null)
								out.println(jiazu.toString());
						%>
					</textarea>
				</td>
				<td></td>
			</tr>
		</table>
	</form>
</body>
</html>
