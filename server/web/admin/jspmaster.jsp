<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.career.*,java.io.*,java.util.ArrayList,com.jspsmart.upload.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.buff.*,com.fy.engineserver.datasource.props.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.sprite.npc.*,com.fy.engineserver.task.*,com.fy.engineserver.core.*,com.xuanzhi.tools.text.*,com.fy.engineserver.menu.WindowManager,com.fy.engineserver.datasource.repute.*,com.fy.engineserver.shop.*,org.apache.log4j.*,com.xuanzhi.boss.game.*,com.fy.engineserver.downcity.*,com.fy.engineserver.menu.question.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>上传数据页面</title>
</head>
<body>
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){ %>
<%!
public Logger logger = Logger.getLogger("jsp.jspmaster");
%>
<%

//计算文件上传个数
	int count = 0;
	try {
		SmartUpload mySmartUpload = new SmartUpload();
		//SmartUpload的初始化，使用这个jspsmart一定要在一开始就这样声明
		mySmartUpload.initialize(pageContext);

		//限制每个上传附件的最大长度。 
		mySmartUpload.setMaxFileSize(Long.parseLong("500000000000"));

		//限制总上传数据的长度。 
		mySmartUpload.setTotalMaxFileSize(Long.parseLong("500000000000"));

		//设定允许上传的附件（通过扩展名限制）。 
//		mySmartUpload.setAllowedFilesList("txt,xml,ncm,mcm");

		//依据form的内容上传
		mySmartUpload.upload();
		
		String str1 = (String)mySmartUpload.getRequest().getParameter("text1");
		String str2 = (String)mySmartUpload.getRequest().getParameter("text2");
		String str3 = mySmartUpload.getRequest().getParameter("text3");
		String str6 = mySmartUpload.getRequest().getParameter("text6");
		String str7 = mySmartUpload.getRequest().getParameter("text7");
		String str8 = mySmartUpload.getRequest().getParameter("text8");
		String str9 = mySmartUpload.getRequest().getParameter("text9");
		String str10 = mySmartUpload.getRequest().getParameter("text10");
		String str11 = mySmartUpload.getRequest().getParameter("text11");
		String str12 = mySmartUpload.getRequest().getParameter("text12");
		String str13 = mySmartUpload.getRequest().getParameter("text13");
		String str14 = mySmartUpload.getRequest().getParameter("text14");
		String str15 = (String)mySmartUpload.getRequest().getParameter("text15");
		String str16 = (String)mySmartUpload.getRequest().getParameter("text16");
		ArrayList<String> existFile = new ArrayList<String>();
		if(str1 != null){
			existFile.add("careerandskills.xml");
		}
		if(str2 != null){
			existFile.add("articles.xml");
		}
		if(str3 != null){
			existFile.add("suitEquipment.xml");
		}
		if(str6 != null){
			existFile.add("buffs.xml");
		}
		if(str7 != null){
			existFile.add("sprites.txt");
		}
		if(str8 != null){
			existFile.add("bornpoints.xml");
		}
		if(str9 != null){
			existFile.add("NPC.ncm");
		}
		if(str10 != null){
			existFile.add("mapnpcs.xml");
		}
		if(str11 != null){
			existFile.add("tasks.xml");
		}
		if(str12 != null){
			existFile.add("menu_windows.xml");
		}
		if(str13 != null){
			existFile.add("reputes.xml");
		}
		if(str14 != null){
			existFile.add("shops.xml");
		}
		if(str15 != null){
			existFile.add("downcity.xml");
		}
		if(str16 != null){
			existFile.add("questions.xml");
		}
		out.println("<table cellspace='1' border='0' bgcolor='black'>");
		out.println("<tr bgcolor='white'><td>文件名称</td><td>文件大小(字节)</td><td>文件路径</td></tr>");
		//怪物数据需要进行特殊处理，所以要有个标示是否存在怪物数据文件
		boolean monsterFileFlag = false;
		boolean npcFileFlag = false;
		//将上传的文件一个一个取出来处理
		for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
			//取出一个文件
			com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);

			String filename = existFile.get(i);

			//如果文件存在，则做存档操作
			if (!myFile.isMissing()) {

				if(filename.equals("skills.txt")){
					try{
						
					//SkillFactory.getInstance().verifySkillData(myFile.getContentString());
					}catch(Exception e){
						throw new SecurityException("上传文件skills.txt内容出错");
					}
				}
				if(filename.equals("articles.xml")){
					try{
						ArticleManager am = ArticleManager.getInstance();
						if(am != null){
							int articleVersion = am.getVersion();
							ArticleManager amTemp = new ArticleManager();
							String content = myFile.getContentString();
							byte data[] = content.getBytes();
							ByteArrayInputStream in = new ByteArrayInputStream(data);
							//java.io.File file = new java.io.File(myFile.getFilePathName());
							//out.println(myFile.getFilePathName());
							//FileInputStream is = new FileInputStream(file);
							amTemp.loadFromStream(in);
							in.close();
							if(amTemp.getVersion() <= articleVersion){
								out.println("<script language='javascript'> alert('物品版本过低，当前版本"+amTemp.getVersion()+" 服务器版本"+articleVersion+"');</script>");
								return;
							}
							in = new ByteArrayInputStream(data);
							am.loadFromStream(in);
							in.close();
						}
					}catch(Exception e){
						throw new SecurityException("上传articles.xml文件内容出错");
					}
				}
				if(filename.equals("careerandskills.xml")){
					
						//CareerManager cm = CareerManager.getInstance();
						
						//java.io.File carFile = new java.io.File(myFile.getFilePathName());
						//FileInputStream fis = new FileInputStream(carFile);
						
						//ByteArrayOutputStream baos = new ByteArrayOutputStream();
						//int byteRead = -1;
						//while ((byteRead = fis.read()) != -1) {
						//	baos.write(byteRead);
						//}
						//fis.close();
						//baos.close();
						//byte[] data = baos.toByteArray();
						//String careerData = new String(data, 0, data.length, "GBK");
						//cm.loadFrom(careerData);
						//cm.verifyCareerData(myFile.getContentString());
					
				}
				if(filename.equals("sprites.txt")){
					monsterFileFlag = true;
				}
				if(filename.equals("NPC.ncm")){
					npcFileFlag = true;
				}
				//将文件存放于绝对路径的位置
				logger.info("上传文件时间："+System.currentTimeMillis()+"位置：/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				myFile.saveAs("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename,mySmartUpload.SAVE_PHYSICAL);
				
				//显示此上传文件的详细信息
				out.println("<tr bgcolor='white'><td>"+myFile.getFileName()+"</td><td>"+myFile.getSize()+"</td><td>"+myFile.getFilePathName()+"</td></tr>");
				count++;
			}
		}
		out.println("</table>");

		// 显示应该上传的文件数目
		out.println("<BR>" + mySmartUpload.getFiles().getCount() + " files could be uploaded.<BR>");
		
		// 显示成功上传的文件数目
		if(count <= 1){
        	out.println(count + " file is successfully uploaded.");
        }else{
        	out.println(count + " files are successfully uploaded.");
        }
		if(existFile.contains("careerandskills.xml")){
			CareerManager cm = CareerManager.getInstance();
			if(cm == null){
				cm = new CareerManager();
			}
			java.io.File careersFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/careerandskills.xml");
			if(careersFile != null && careersFile.isFile() && careersFile.exists()){
				
				FileInputStream fis = new FileInputStream(careersFile);
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int byteRead = -1;
				while ((byteRead = fis.read()) != -1) {
					baos.write(byteRead);
				}
				fis.close();
				baos.close();
				byte[] data = baos.toByteArray();
				String careerData = new String(data, 0, data.length, "GBK");
				cm.loadFrom(careerData);
			}
		}
		
		ArticleManager am = ArticleManager.getInstance();
		
//		if(existFile.contains("articles.xml")){
//			java.io.File articlesFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/articles.xml");
//			if(articlesFile != null && articlesFile.isFile() && articlesFile.exists()){
//				am.load(articlesFile);
//			}
//		}
		if(existFile.contains("suitEquipment.xml")){
			java.io.File suitEquipmentFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/suitEquipment.xml");
			if(suitEquipmentFile != null && suitEquipmentFile.isFile() && suitEquipmentFile.exists()){
				am.loadSuitEquipment(suitEquipmentFile);
			}
		}
		
		//SkillFactory sf = SkillFactory.getInstance();
		//sf.loadSkillFromFile("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/skills.txt");
		if(existFile.contains("buffs.xml")){
			BuffTemplateManager bm = BuffTemplateManager.getInstance();
			java.io.File buffsFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/buffs.xml");
			if(buffsFile != null && buffsFile.isFile() && buffsFile.exists()){
				bm.load(buffsFile);
			}
		}
		
		if(monsterFileFlag){
			MonsterManager sm = MemoryMonsterManager.getMonsterManager();
			java.io.File monsterFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/sprites.txt");
			if(sm != null && monsterFile != null && monsterFile.isFile() && monsterFile.exists()){
				FileInputStream fis = new FileInputStream(monsterFile);
				byte[] buffer = new byte[1024];
				int len = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while((len = fis.read(buffer, 0 , buffer.length)) != -1){
					baos.write(buffer , 0, len);
				}
				byte[] data = baos.toByteArray();
				fis.close();
				baos.close();
				int allMonsterCount = sm.getAmountOfMonsters();
				Monster[] monsters = sm.getMonstersByPage(0,allMonsterCount);
				if(monsters != null){
					for(Monster m : monsters){
						if(m != null){
							m.setHp(0);
						}
					}
				}
				((MemoryMonsterManager)sm).loadFrom(new String(data ,0,data.length));
				out.println("上传怪物数据成功");
			}
		}

		if(existFile.contains("tasks.xml")){
			TaskManager tm = TaskManager.getInstance();
			java.io.File tasksFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/tasks.xml");
			if(tm != null && tasksFile != null && tasksFile.isFile() && tasksFile.exists()){
				InputStream is = new FileInputStream(tasksFile);
				tm.load(is);
			}
		}
		
		if(npcFileFlag){
			NPCManager nm = MemoryNPCManager.getNPCManager();
			java.io.File npcFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/NPC.ncm");
			if(nm != null && npcFile != null && npcFile.isFile() && npcFile.exists()){
				FileInputStream fis = new FileInputStream(npcFile);
				byte[] buffer = new byte[1024];
				int len = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while((len = fis.read(buffer, 0 , buffer.length)) != -1){
					baos.write(buffer , 0, len);
				}
				byte[] data = baos.toByteArray();
				int mountofnpcs = nm.getAmountOfNPCs();
				NPC[] npcs = nm.getNPCsByPage(0,mountofnpcs);
				if(npcs != null){
					for(NPC npc : npcs){
						if(npc != null){
							npc.setAlive(false);
						}
					}
				}
				((MemoryNPCManager)nm).loadFrom(new String(data ,0,data.length));
				fis.close();
				baos.close();
			}
		}
		
		GameManager gm = GameManager.getInstance();
		if(existFile.contains("bornpoints.xml")){
			java.io.File bornpointsFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/bornpoints.xml");
			if(gm != null && bornpointsFile != null && bornpointsFile.isFile() && bornpointsFile.exists()){
				gm.reloadMonsterBornPointFile(bornpointsFile);
			}
		}
		
		if(existFile.contains("mapnpcs.xml")){
			java.io.File npcbornpointsFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/mapnpcs.xml");
			if(gm != null && npcbornpointsFile != null && npcbornpointsFile.isFile() && npcbornpointsFile.exists()){
				gm.reloadNPCBornPointFile(npcbornpointsFile);
			}
		}
		if(existFile.contains("menu_windows.xml")){
			WindowManager wm = WindowManager.getInstance();
			java.io.File menu_windowsFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/menu_windows.xml");
			if(wm != null && menu_windowsFile != null && menu_windowsFile.isFile() && menu_windowsFile.exists()){
				java.io.FileInputStream input = new java.io.FileInputStream(menu_windowsFile);
				wm.loadFrom(input);
				input.close();
			}
		}
		if(existFile.contains("reputes.xml")){
			ReputeManager rm = ReputeManager.getInstance();
			java.io.File reputesFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/reputes.xml");
			if(rm != null && reputesFile != null && reputesFile.isFile() && reputesFile.exists()){
				FileInputStream fis = new FileInputStream(reputesFile);
				byte[] buffer = new byte[1024];
				int len = -1;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				while ((len = fis.read(buffer, 0, buffer.length)) != -1) {
					baos.write(buffer, 0, len);
				}
				byte[] data = baos.toByteArray();
				rm.loadFrom(new String(data, 0, data.length));
				fis.close();
				baos.close();
			}
		}
		if(existFile.contains("shops.xml")){
			ShopManager sm = ShopManager.getInstance();
			java.io.File smFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/shops.xml");
			if(sm != null && smFile != null && smFile.isFile() && smFile.exists()){
				sm.loadFrom(smFile);
			}
		}
		if(existFile.contains("downcity.xml")){
			DownCityManager dcm = DownCityManager.getInstance();
			java.io.File downCityFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/downcity.xml");
			if(downCityFile != null && downCityFile.isFile() && downCityFile.exists()){
				dcm.load(downCityFile);
			}
		}
		if(existFile.contains("questions.xml")){
			QuestionManager qm = QuestionManager.getInstance();
			java.io.File questionFile = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/questions.xml");
			if(questionFile != null && questionFile.isFile() && questionFile.exists()){
				qm.loadFrom(questionFile);
			}
		}
	}catch(SecurityException e){
		e.printStackTrace();
		out.println(StringUtil.getStackTrace(e));
	} catch (SmartUploadException e) {
		e.printStackTrace();
		out.println(StringUtil.getStackTrace(e));
		e.getMessage();
	}catch(Exception e){
		out.println(StringUtil.getStackTrace(e));
		e.printStackTrace();
	}
}else{
	out.println("此服务器不能上传");
}
%>

</body>
</html>
