<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.career.*,java.io.*,java.util.ArrayList,com.jspsmart.upload.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.buff.*,com.fy.engineserver.datasource.props.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.sprite.npc.*,com.fy.engineserver.task.*,com.fy.engineserver.core.*,com.xuanzhi.tools.text.*,com.fy.engineserver.menu.WindowManager,com.fy.engineserver.datasource.repute.*,com.fy.engineserver.shop.*,org.apache.log4j.*,com.xuanzhi.boss.game.*,com.fy.engineserver.downcity.*,com.fy.engineserver.menu.question.*,java.sql.*,java.text.*"%>
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

		String filename = request.getParameter("filename");

		//日期
		Date date = new Date(System.currentTimeMillis());
		OutputStream os = null;
		ByteArrayInputStream in = null;
		if(filename.equals("careerandskills.xml")){
			try{
				CareerManager am = CareerManager.getInstance();
				if(am != null){
					byte data[] = (byte[])session.getAttribute(filename);
					if(data == null){
						return;
					}
					String careerData = new String(data, 0, data.length, "utf-8");
					am.loadFrom(careerData);
					java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
					if(!file.exists()){
						file.createNewFile();
					}
					os = new FileOutputStream(file);
					os.write(data);
					os.close();
					
					//备份
					date = new Date(System.currentTimeMillis());
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String dateStr = df.format(date);
					df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
					String minuteStr = df.format(date);
					file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
					FileUtils.chkFolder(file.getAbsolutePath());
					os = new FileOutputStream(file);
					os.write(data);
					os.close();
					out.println("职业文件上传成功，并已经在服务器上备份");
				}
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("articles.xml")){
			try{
				ArticleManager am = ArticleManager.getInstance();
				if(am != null){
					int articleVersion = am.getVersion();
					byte data[] = (byte[])session.getAttribute(filename);
					if(data == null){
						return;
					}
					in = new ByteArrayInputStream(data);
					am.loadFromStream(in);
					in.close();
					java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
					if(!file.exists()){
						file.createNewFile();
					}
					os = new FileOutputStream(file);
					os.write(data);
					os.close();

					//备份
					date = new Date(System.currentTimeMillis());
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String dateStr = df.format(date);
					df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
					String minuteStr = df.format(date);
					file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
					FileUtils.chkFolder(file.getAbsolutePath());
					os = new FileOutputStream(file);
					os.write(data);
					os.close();
					out.println("物品文件上传成功，并已经在服务器上备份");
				}
			}catch(Exception e){
				session.removeAttribute(filename);
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("suitEquipment.xml")){
			try{
				ArticleManager am = ArticleManager.getInstance();
				if(am == null){
					throw new Exception("物品管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.loadSuitEquipmentFromInputStream(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("套装文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				session.removeAttribute(filename);
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("buffs.xml")){
			try{
				BuffTemplateManager am = BuffTemplateManager.getInstance();
				if(am == null){
					throw new Exception("Buff管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.loadFromInputStream(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("Buff文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				session.removeAttribute(filename);
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("sprites.txt")){
			try{
				MonsterManager am = MemoryMonsterManager.getMonsterManager();
				if(am == null){
					throw new Exception("怪物管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				int allMonsterCount = am.getAmountOfMonsters();
				Monster[] monsters = am.getMonstersByPage(0,allMonsterCount);
				if(monsters != null){
					for(Monster m : monsters){
						if(m != null){
							m.setHp(0);
						}
					}
				}
				((MemoryMonsterManager)am).loadFrom(new String(data ,0,data.length));
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("怪物文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("bornpoints.xml")){
			try{
				GameManager am = GameManager.getInstance();
				if(am == null){
					throw new Exception("Game管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				am.reloadMonsterBornPointFile(file);

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("怪物出生点文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("NPC.ncm")){
			try{
				NPCManager am = MemoryNPCManager.getNPCManager();
				if(am == null){
					throw new Exception("NPC管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				int mountofnpcs = am.getAmountOfNPCs();
				NPC[] npcs = am.getNPCsByPage(0,mountofnpcs);
				if(npcs != null){
					for(NPC npc : npcs){
						if(npc != null){
							npc.setAlive(false);
						}
					}
				}
				((MemoryNPCManager)am).loadFrom(new String(data ,0,data.length));
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("NPC文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("mapnpcs.xml")){
			try{
				GameManager am = GameManager.getInstance();
				if(am == null){
					throw new Exception("Game管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				am.reloadNPCBornPointFile(file);

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("NPC出生点文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("tasks.xml")){
			try{
				TaskManager am = TaskManager.getInstance();
				if(am == null){
					throw new Exception("任务管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.load(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("任务文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("menu_windows.xml")){
			try{
				WindowManager am = WindowManager.getInstance();
				if(am == null){
					throw new Exception("菜单管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.loadFrom(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("菜单文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("reputes.xml")){
			try{
				ReputeManager am = ReputeManager.getInstance();
				if(am == null){
					throw new Exception("声望管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				am.loadFrom(new String(data, 0, data.length));
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("声望文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("shops.xml")){
			try{
				ShopManager am = ShopManager.getInstance();
				if(am == null){
					throw new Exception("商店管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.loadFromInputStream(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("商店文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("downcity.xml")){
			try{
				DownCityManager am = DownCityManager.getInstance();
				if(am == null){
					throw new Exception("副本管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.loadFromInputStream(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("副本文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("questions.xml")){
			try{
				QuestionManager am = QuestionManager.getInstance();
				if(am == null){
					throw new Exception("商店管理器为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.loadFromInputStream(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("问题集文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}
		if(filename.equals("discount.txt")){
			try{
				DiscountManager am = DiscountManager.getInstance();
				if(am == null){
					throw new Exception("商店打折管理为空");
				}
				byte data[] = (byte[])session.getAttribute(filename);
				if(data == null){
					return;
				}
				in = new ByteArrayInputStream(data);
				am.load(in);
				in.close();
				java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/" + filename);
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				os.write(data);
				os.close();

				//备份
				date = new Date(System.currentTimeMillis());
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				String dateStr = df.format(date);
				df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
				String minuteStr = df.format(date);
				file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_runtime_data/backup/"+dateStr+"/" + filename+minuteStr+".bak");
				FileUtils.chkFolder(file.getAbsolutePath());
				os = new FileOutputStream(file);
				os.write(data);
				os.close();
				out.println("商店打折文件上传成功，并已经在服务器上备份");
			}catch(Exception e){
				throw e;
			}finally{
				if(in != null){
					in.close();
				}
				if(os != null){
					os.close();
				}
				session.removeAttribute(filename);
			}
		}

	}catch(SecurityException e){
		e.printStackTrace();
		out.println(StringUtil.getStackTrace(e));
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
