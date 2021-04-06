<%@ page contentType="text/html;charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">


<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.core.LivingObject"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.core.NPCFlushAgent"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte"%>
<%@page import="com.fy.engineserver.core.NPCFlushAgent.BornPoint"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager.NPCTempalte"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.Iterator"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>刷页面删除npc</title>
</head>
<body>

<%
	GameManager gm = GameManager.getInstance();
	{
		for (int i = 1; i <= 3; i++) {
			Game game = gm.getGameByName("piaomiaowangcheng", i);
			NPCFlushAgent flushAgent = game.getNpcFlushAgent();
			LivingObject[] lObject = game.getLivingObjects();
			for (LivingObject lo : lObject) {
				if (lo instanceof NPC) {
					if (((NPC) lo).getName().equals("元气大仙")) {
						game.removeSprite((NPC) lo);
						removeNpcFormNPCFlushAgent(flushAgent, ((NPC) lo), out);
					}
				}
			}
		}
	}
	{
		Game game = gm.getGameByName("yongancheng", 0);
		NPCFlushAgent flushAgent = game.getNpcFlushAgent();
		LivingObject[] lObject = game.getLivingObjects();
		for (LivingObject lo : lObject) {
			if (lo instanceof NPC) {
				if (((NPC) lo).getName().equals("炼妖师")) {
					game.removeSprite((NPC) lo);
					removeNpcFormNPCFlushAgent(flushAgent, ((NPC) lo), out);
				} else if (((NPC) lo).getName().equals("九宫道境")) {
					game.removeSprite((NPC) lo);
					removeNpcFormNPCFlushAgent(flushAgent, ((NPC) lo), out);
				}

			}
		}
	}
%>
<%!public void removeNpcFormNPCFlushAgent(NPCFlushAgent npcFlushAgent, NPC npc, JspWriter out) throws Exception {
		Class<?> clazz = NPCFlushAgent.class;
		Field f = clazz.getDeclaredField("templateMap");
		f.setAccessible(true);
		HashMap<BornPoint, Integer> templateMap = (HashMap<BornPoint, Integer>) f.get(npcFlushAgent);

		Iterator<BornPoint> itor = templateMap.keySet().iterator();
		while (itor.hasNext()) {
			BornPoint bp = itor.next();
			int cid = templateMap.get(bp);
			if (cid == npc.getnPCCategoryId()) {
				itor.remove();
				out.print("在NPCFlushAgent里移除了NPC：" + npc.getName() + "<BR/>");
			}
		}

	}%>

</body>

</html>
