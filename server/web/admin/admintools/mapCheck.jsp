<%@page import="com.fy.engineserver.core.GameInfo"%>
<%@page
	import="com.fy.engineserver.datasource.language.MultiLanguageTranslateManager"%>
<%@page
	import="com.fy.engineserver.datasource.language.TransferLanguage"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="com.xuanzhi.tools.text.XmlUtil"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.File"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.sprite.Sprite"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.sprite.Team"%>
<%@page import="com.fy.engineserver.sprite.npc.FlopCaijiNpc"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.sprite.monster.Monster"%>
<%@page import="java.util.*"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String mapName = request.getParameter("mapName");
	int country = 1;
	if (mapName != null && !"".equals(mapName)) {
		country = Integer.valueOf(request.getParameter("country"));
		Game game = GameManager.getInstance().getGameByDisplayName(mapName, country);
		if (game == null) {
			out.print("地图不存在");
		} else {
			out.print("<hr>");
			out.print(CountryManager.得到国家名(country) + "-" + mapName);
			out.print("<hr>");
			LivingObject[] los = game.getLivingObjects();
			Map<String, Integer> countMap = new HashMap<String, Integer>();
			for (LivingObject lo : los) {
				try {
					lo.heartbeat(System.currentTimeMillis(), 1, game);
				} catch (Exception e) {
					out.print("心跳出现异常生物:" + lo.getClass() + "," + e.getMessage() + "<br/>");
					lo.setAlive(false);
				}
				if (true) {
					String clazz = lo.getClass().toString();
					if (!countMap.containsKey(clazz)) {
						countMap.put(clazz, 0);
					}
					countMap.put(clazz, countMap.get(clazz) + 1);
					if (Monster.class.equals(lo.getClass())) {
						Monster monster = (Monster) lo;
						out.print(monster.getName() + ",(" + monster.getX() + "," + monster.getY() + ")<BR/>");
					} else if (FlopCaijiNpc.class.equals(lo.getClass())) {
						FlopCaijiNpc caijiNpc = (FlopCaijiNpc) lo;
						//game.removeSprite(caijiNpc);
						out.print("[FlopCaijiNpc: " + caijiNpc.getName() + "/team:" + caijiNpc.getTeam() + "]");
						try {
							caijiNpc.heartbeat(System.currentTimeMillis(), 1, game);
						} catch (Exception e) {
							out.print("..................." + caijiNpc.getName());
							caijiNpc.setAlive(false);
						}
					} else if (Player.class.equals(lo.getClass())) {
						Player player = (Player) lo;
						Team t = player.getTeam();
						if (t != null) {
							boolean hasNull = false;
							for (int i = 0; i < t.getMembers().size(); i++) {
								Player p = t.getMembers().get(i);
								if (p == null) {
									hasNull = true;
								}
							}
							out.print("<font color='red'>[含有NULL]" + player.getLogString() + "/" + t.getMembers().size() + "/" + t.getId() + "<BR/></font>");
						}
					}
				}
				for (Iterator<String> itor = countMap.keySet().iterator(); itor.hasNext();) {
					String clazz = itor.next();
					int num = countMap.get(clazz);
					out.print(clazz + ":" + num);
					out.print("<BR/>");
				}
			}
			if (false) {
				Field f = Game.class.getDeclaredField("templateMonsterAndNpcsInGame");
				f.setAccessible(true);
				Sprite templateMonsterAndNpcsInGame[] = (Sprite[]) f.get(game);
				out.print("templateMonsterAndNpcsInGame:" + templateMonsterAndNpcsInGame + "<BR/>");
				List<Sprite> hasList = new ArrayList<Sprite>();
				for (Sprite s : templateMonsterAndNpcsInGame) {
					if (s == null) {
						out.print("........................................................................<BR/>");
					} else {
						//hasList.add(s);
						out.print("has:" + s.getClass() + ">>" + (s instanceof Monster ? ((Monster) s).getSpriteCategoryId() : ((NPC) s).getnPCCategoryId()) + "<BR/>");
					}
				}
				//f.set(game,hasList.toArray(new Sprite[0]));
			}
		}
		{
			if (false) {
				File file = new File("/data/home/cp_mqq/resin_server/webapps/game_server/WEB-INF/game_init_config/bindata/bornpoints.xml");
				InputStream is = new FileInputStream(file);
				Document dom = XmlUtil.load(is);

				Element gameEles[] = XmlUtil.getChildrenByName(dom.getDocumentElement(), "game");
				for (int i = 0; i < gameEles.length; i++) {
					Element e = gameEles[i];
					String name = XmlUtil.getAttributeAsString(e, "name", TransferLanguage.getMap());
					name = MultiLanguageTranslateManager.languageTranslate(name);
					GameInfo gi = GameManager.getInstance().getGameInfo(name);
					if (gi != null) {
						Field f = GameInfo.class.getDeclaredField("monsterBornEle");
						f.setAccessible(true);
						//gi.monsterBornEle = e;
						f.set(gi, e);
					} else {
						throw new Exception("Game for Name [" + name + "] not exists!");
					}
				}
				game.init();
				out.print("OK");
			}
		}
		try {
			game.heartbeat();
		} catch (Exception e) {
			TaskManager.logger.error("地图异常", e);
		}
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>地图查看</title>
</head>
<body>
	<form action="" method="post">
		地图:<input name="mapName" type="text" value="<%=mapName%>"><BR />
		国家:<input name="country" type="text" value="<%=country%>"> <input
			type="submit">
	</form>
</body>
</html>