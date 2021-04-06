package com.fy.engineserver.datasource.article.manager;



/**
 * 种子NPC管理器，保存服务器中，
 * 所有的种子NPC
 * 
 */
public class SeedNPCManager {
//
//	public static Logger logger = Logger.getLogger(SeedNPCManager.class);
//
//	protected static SeedNPCManager self;
//
//	static Random rd = new Random(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
//
//	HashMap<Long,SeedNpc> npcMap = new HashMap<Long,SeedNpc>();
//
//	public HashMap<Long, SeedNpc> getNpcMap() {
//		return npcMap;
//	}
//	public void setNpcMap(HashMap<Long, SeedNpc> npcMap) {
//		this.npcMap = npcMap;
//	}
//	public File getConfigFile() {
//		return configFile;
//	}
//	public void setConfigFile(File configFile) {
//		this.configFile = configFile;
//	}
//	/**
//	 * 得到物品管理器本身
//	 * @return
//	 */
//	public static SeedNPCManager getInstance(){
//		return self;
//	}
//	private File configFile;
//	public void init() throws Exception{
//		
//		long startTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		
//		if(configFile != null && configFile.isFile() && configFile.exists())
//			load(configFile);
//		else
//			logger.warn("[配置文件不错在] ["+configFile.getPath()+"]");
//		
//		System.out.println("[系统初始化] [种子NPC管理器] [初始化完成] ["+getClass().getName()+"] [耗时："+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - startTime)+"毫秒]");
//		
//		self = this;
//	}
//	public void load(File file)throws Exception{
//		if(file == null || !file.isFile() || !file.exists()){
//			return;
//		}
//		FileInputStream is = new FileInputStream(file);
//		Document dom = null;
////		if(TransferLanguage.characterTransferormFlag){
//			dom = XmlUtil.load(is,"utf-8");
////		}else{
////			dom = XmlUtil.load(is);
////		}
//		Element root = dom.getDocumentElement();
//		Element eles[] = XmlUtil.getChildrenByName(root, "npc");
//		HashMap<Long,SeedNpc> maps = new HashMap<Long,SeedNpc>();
//		NPCManager nm = MemoryNPCManager.getNPCManager();
//		if(nm == null){
//			return;
//		}
//		GameManager gm = GameManager.getInstance();
//		if(gm == null){
//			return;
//		}
//		for(int j = 0 ; j < eles.length ; j++){
//			try{
//				int seedNpcTemplateId = XmlUtil.getAttributeAsInteger(eles[j],"seedNpcTemplateId",0);
//				int playerLevel = XmlUtil.getAttributeAsInteger(eles[j],"plantPlayerLevel",0);
//				int x = XmlUtil.getAttributeAsInteger(eles[j],"x",0);
//				int y = XmlUtil.getAttributeAsInteger(eles[j],"y",0);
//				int plantId = XmlUtil.getAttributeAsInteger(eles[j],"plantId",0);
//				String mapName = XmlUtil.getAttributeAsString(eles[j],"mapName",null, null);
//				String fruitName = XmlUtil.getAttributeAsString(eles[j],"fruitName",null, null);
//				String name = "";
//				long plantTime = XmlUtil.getAttributeAsLong(eles[j],"plantTime",0);
//				long ripeTime = XmlUtil.getAttributeAsLong(eles[j],"ripeTime",0);
//				long personalPickTime = XmlUtil.getAttributeAsLong(eles[j],"personalPickTime",0);
//				int afterRipeImage = XmlUtil.getAttributeAsInteger(eles[j],"afterRipeImage",0);
//				int type = XmlUtil.getAttributeAsInteger(eles[j],"type",0);
//				NPC npc = ((MemoryNPCManager)nm).createNPC(seedNpcTemplateId);
//				Element nameeles[] = XmlUtil.getChildrenByName(eles[j], "name");
//				if(nameeles != null){
//					for(Element el : nameeles){
//						name = XmlUtil.getValueAsString(el, null);
//					}
//				}
//				if(npc instanceof SeedNpc){
//					npc.setX(x);
//					npc.setY(y);
//					npc.setAlive(true);
//					npc.setGameName(mapName);
//					Game game = gm.getGameByName(mapName);
//					if(game == null){
//						continue;
//					}
//					((SeedNpc)npc).setPlantPlayerId(plantId);
//					((SeedNpc)npc).setPlantTime(plantTime);
//					((SeedNpc)npc).setRipeTime(ripeTime);
//					((SeedNpc)npc).setPersonalPickupLastingTime(personalPickTime);
//					((SeedNpc)npc).setAfterRipeImage(afterRipeImage);
//					((SeedNpc)npc).setFruitName(fruitName);
//					((SeedNpc)npc).setPlantPlayerLevel(playerLevel);
//					npc.setType((byte)type);
//					npc.setName(name);
//					game.addSprite(npc);
//					maps.put(npc.getId(), (SeedNpc)npc);
//				}
//			}catch(Exception ex){
//				logger.warn("[种子NPC管理器加载出现异常]");
//			}
//		}
//		npcMap = maps;
//	}
//	public synchronized void write() throws IOException{
//		long nowTime = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
//		StringBuffer sb = new StringBuffer();
//		if(npcMap == null || npcMap.keySet() == null){
//			return;
//		}
//		sb.append("<?xml version='1.0' encoding='gbk' ?>\n");
//		sb.append("<npcmanager>\n");
//		for(Long intKey : npcMap.keySet()){
//			if(intKey != null && npcMap.get(intKey) != null){
//				SeedNpc seedNpc = npcMap.get(intKey);
//				sb.append("<npc id='"+seedNpc.getId()+"' mapName='"+seedNpc.getGameName()+"' x='"+seedNpc.getX()+"' y='"+seedNpc.getY()+"' plantId='"+seedNpc.getPlantPlayerId()+"' plantTime='"+seedNpc.getPlantTime()+"' ripeTime='"+seedNpc.getRipeTime()+"' personalPickTime='"+seedNpc.getPersonalPickupLastingTime()+"' seedNpcTemplateId='"+seedNpc.getNPCCategoryId()+"' afterRipeImage='"+seedNpc.getAfterRipeImage()+"' type='"+seedNpc.getType()+"' fruitName='"+(seedNpc.getFruitName() == null ? "" : seedNpc.getFruitName())+"' plantPlayerLevel='"+seedNpc.getPlantPlayerLevel()+"'>\n");
//				sb.append("<name><![CDATA["+seedNpc.getName()+"]]></name>\n");
//				sb.append("</npc>\n\n");
//			}
//		}
//		sb.append("</npcmanager>");
//		if(configFile != null){
//			java.io.FileOutputStream os = new java.io.FileOutputStream(configFile);
////			if(TransferLanguage.characterTransferormFlag){
//				os.write(sb.toString().getBytes("utf-8"));
////			}else{
////				os.write(sb.toString().getBytes());
////			}
//			
//			os.close();
//		}
//		ArticleManager.logger.warn("[种植园存盘成功:时长"+(com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - nowTime)+"毫秒]");
//	}
}
