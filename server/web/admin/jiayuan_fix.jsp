<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,
com.fy.engineserver.jiayuan.*,
 org.w3c.dom.*,
 java.io.*,
  com.fy.engineserver.core.event.*,
com.fy.engineserver.sprite.*,com.fy.engineserver.sprite.concrete.*,com.fy.engineserver.core.*,com.fy.engineserver.datasource.buff.*,
com.fy.engineserver.datasource.skill.*,com.google.gson.*,java.lang.reflect.*,com.fy.engineserver.sprite.npc.*"%>
<%
String filename = "/home/game/resin/webapps/game_server/WEB-INF/game_runtime_data/jiayuan/2837.dcp";
JiaYuanManager jm = JiaYuanManager.getInstance();
GameManager gameManager = GameManager.getInstance();
NPCManager nm = MemoryNPCManager.getNPCManager();
Document dom = null;
try{
	FileInputStream input = new FileInputStream(filename);
	dom = XmlUtil.load(input,"UTF-8");
	input.close();
}catch(Exception e){
	System.out.println("[加载家园进度出错] ["+filename+"] [XML文件解析出错]");
}
if(dom != null){
	try {
		Element root = dom.getDocumentElement();
		
		long id = XmlUtil.getAttributeAsLong(root, "id");
		long createTime = XmlUtil.getAttributeAsLong(root, "createTime");
		int level = XmlUtil.getAttributeAsInteger(root, "level");
		
		String name = XmlUtil.getValueAsString(XmlUtil.getChildByName(root, "name"));

		
		JiaYuan dc = new JiaYuan();
		dc.gangId = id;
		dc.gangName = name;
		dc.level = level;
		
		
		String mapName = jm.getMapNameByJiaYuanLevel(dc.level);
		GameInfo gi = gameManager.getGameInfo(mapName);
		if(gi == null){
			throw new Exception("家园["+name+"]配置对应的地图["+mapName+"]不存在，丢弃此副本进度");
		}
		Game newGame = new Game(gameManager,gi);
		if(gameManager.getPlayerManager() instanceof PlayerInOutGameListener){
			newGame.addPlayerInOutGameListener((PlayerInOutGameListener)gameManager.getPlayerManager());
		}
		newGame.init();

		Element eles[] = XmlUtil.getChildrenByName(root, "npc");
		for(int j = 0 ; j < eles.length ; j++){
			try{
				int seedNpcTemplateId = XmlUtil.getAttributeAsInteger(eles[j],"seedNpcTemplateId",0);
				int playerLevel = XmlUtil.getAttributeAsInteger(eles[j],"plantPlayerLevel",0);
				int x = XmlUtil.getAttributeAsInteger(eles[j],"x",0);
				int y = XmlUtil.getAttributeAsInteger(eles[j],"y",0);
				int plantId = XmlUtil.getAttributeAsInteger(eles[j],"plantId",0);
				String fruitName = XmlUtil.getAttributeAsString(eles[j],"fruitName",null);
				long plantTime = XmlUtil.getAttributeAsLong(eles[j],"plantTime",0);
				long ripeTime = XmlUtil.getAttributeAsLong(eles[j],"ripeTime",0);
				long personalPickTime = XmlUtil.getAttributeAsLong(eles[j],"personalPickTime",0);
				int afterRipeImage = XmlUtil.getAttributeAsInteger(eles[j],"afterRipeImage",0);
				int type = XmlUtil.getAttributeAsInteger(eles[j],"type",0);
				NPC npc = nm.createNPC(seedNpcTemplateId);
				Element nameEle = XmlUtil.getChildByName(eles[j], "name");
				String npcName = XmlUtil.getValueAsString(nameEle);
				if(npc instanceof SeedNpc){
					npc.setX(x);
					npc.setY(y);
					npc.setAlive(true);
					npc.setGameName(mapName);
					
					((SeedNpc)npc).setPlantPlayerId(plantId);
					((SeedNpc)npc).setPlantTime(plantTime);
					((SeedNpc)npc).setRipeTime(ripeTime);
					((SeedNpc)npc).setPersonalPickupLastingTime(personalPickTime);
					((SeedNpc)npc).setAfterRipeImage(afterRipeImage);
					((SeedNpc)npc).setFruitName(fruitName);
					((SeedNpc)npc).setPlantPlayerLevel(playerLevel);
					npc.setType((byte)type);
					npc.setName(npcName);
					
					newGame.addSprite(npc);
					
				}
			}catch(Exception e){
				System.out.println("[家园初始化] [加载SeedNpc出错]");
			}
		}
		
		
		

		dc.setGame(newGame);
		dc.createTime = createTime;
		
		int index = 0;
		int min = 1000;
		for(int j = 0 ; j < jm.threads.length ; j++){
			int n = threads[j].getGames().length;
			if(n < min){
				min = n;
				index = j;
			}
		}
		threads[index].addGame(newGame);
		dc.threadIndex = index;
		dc.init();
		
		jiaYuanMap.put(dc.gangId, dc);
		
		logger.info("[加载家园进度] [成功] ["+dc.gangId+"] ["+dc.gangName+"] ["+filename.getAbsolutePath()+"]");
		
	} catch (Exception e) {
		logger.warn("[加载副本进度] [失败] [出现异常] ["+filename.getAbsolutePath()+"]",e);
	}
}
%>
