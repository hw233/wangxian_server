<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="java.lang.reflect.Field"%>
<%@page
	import="com.fy.engineserver.core.MonsterFlushAgent.BornPoint"%>
<%@page import="com.fy.engineserver.core.MonsterFlushAgent"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String mapName = "mizonglin";
	int[] countrys = new int[] { 1,2,3 };
	int cids[] = new int[] { 20005, 20004 };
	out.print("IN" + Arrays.toString(cids));
	Field x = BornPoint.class.getDeclaredField("x");
	x.setAccessible(true);
	Field y = BornPoint.class.getDeclaredField("y");
	y.setAccessible(true);
	for (int i = 0; i < countrys.length; i++) {
		Game game = GameManager.getInstance().getGameByName(mapName, countrys[i]);
		if (game != null) {
			MonsterFlushAgent mfa = game.getSpriteFlushAgent();
			out.print("<hr>");
			out.print(game + "," + mfa);
			out.print("<hr>");
			if (mfa != null) {
				for (int cid : cids) {
					BornPoint[] bps = mfa.getBornPoints4SpriteCategoryId(cid);
					if (bps != null) {
						for (BornPoint bp : bps) {
							try {
								int bx = bp.getX();
								int by = bp.getX();
								Field f3 = BornPoint.class.getDeclaredField("sprite");
								f3.setAccessible(true);
								Monster monster = (Monster) f3.get(bp);
								out.print("出生点["+countrys[i]+"](" + bx + "," + by + ")<br/>" );
								out.print("原来怪坐标" + monster.getId() + "/" + monster.getName() + ",(" + monster.getX() + "," + monster.getY() + ")<br/>");
								out.print("<BR/>");
								Field f1 = BornPoint.class.getDeclaredField("isAlive");
								f1.setAccessible(true);
								f1.set(bp, false);
								Field f2 = BornPoint.class.getDeclaredField("lastDisappearTime");
								f2.setAccessible(true);
								f2.set(bp, 0L);
							} catch (Exception e) {
								out.print(e.toString());
							}

						}
					} else {
						out.print("bps is null");
					}
				}
			} else {

				out.print(" ag == null");
			}
		} else {
			out.print(" game == null");
		}
	}
	out.print("out");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>刷新错误的怪</title>
</head>
<body>

</body>
</html>