<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.career.*,java.io.*,java.util.ArrayList,com.jspsmart.upload.*,com.fy.engineserver.datasource.skill.*,com.fy.engineserver.datasource.buff.*,com.fy.engineserver.datasource.props.*,com.fy.engineserver.sprite.monster.*,com.fy.engineserver.sprite.npc.*,com.fy.engineserver.task.*,com.fy.engineserver.core.*,com.xuanzhi.tools.text.*,com.fy.engineserver.menu.WindowManager,com.fy.engineserver.datasource.repute.*,com.fy.engineserver.shop.*,org.apache.log4j.*,com.xuanzhi.boss.game.*,com.fy.engineserver.downcity.*,com.fy.engineserver.menu.question.*,java.util.*,
com.fy.engineserver.menu.*,com.fy.engineserver.menu.question.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>上传数据页面</title>
<link rel="stylesheet" type="text/css" href="../css/common.css">
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
<!--
.titlecolor{
background-color:#C2CAF5;
text-align:center;
}
.tdnoborder{
border:0px;
}
-->
</style>
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
		String submitFileName = null;
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
		//增加的内容和删除的内容
		ArrayList<String> addContentList = new ArrayList<String>();
		ArrayList<String> deleteContentList = new ArrayList<String>();
		//将上传的文件一个一个取出来处理
		for (int i = 0; i < mySmartUpload.getFiles().getCount(); i++) {
			//取出一个文件
			com.jspsmart.upload.File myFile = mySmartUpload.getFiles().getFile(i);

			String filename = existFile.get(i);

			//如果文件存在，则做存档操作
			if (!myFile.isMissing()) {

				if(filename.equals("careerandskills.xml")){
					submitFileName = "careerandskills.xml";
					try{
						CareerManager cm = CareerManager.getInstance();
						if(cm != null){
							CareerManager cmTemp = new CareerManager();
							String content = myFile.getContentString();
							byte data[] = content.getBytes();
							String careerData = new String(data, 0, data.length, "utf-8");
							cmTemp.loadFrom(careerData);
							//给session赋值，这样可以在下个页面用
							session.setAttribute(submitFileName,data);
							Skill[] skills = cm.getSkills();
							Skill[] skillsTemp = cmTemp.getSkills();
							if(skillsTemp != null){
								for(Skill at : skillsTemp){
									if(at != null){
										boolean exist = false;
										if(skills != null){
											for(Skill a : skills){
												if(a != null && a.getName() != null && a.getName().equals(at.getName())){
													exist = true;
													break;
												}
											}
										}
										if(!exist){
											addContentList.add(at.getName());
										}
									}
								}
							}
							if(skills != null){
								for(Skill a : skills){
									if(a != null){
										boolean exist = false;
										if(skillsTemp != null){
											for(Skill at : skillsTemp){
												if(at != null && at.getName() != null && at.getName().equals(a.getName())){
													exist = true;
													break;
												}
											}
										}
										if(!exist){
											deleteContentList.add(a.getName());
										}
									}
								}
							}
						}else{
							throw new Exception("职业技能管理器为空");
						}
					}catch(Exception e){
						throw e;
					}
				}

				if(filename.equals("articles.xml")){
					submitFileName = "articles.xml";
					try{
						ArticleManager am = ArticleManager.getInstance();
						if(am != null){
							int articleVersion = am.getVersion();
							ArticleManager amTemp = new ArticleManager();
							String content = myFile.getContentString();
							byte data[] = content.getBytes();
							ByteArrayInputStream in = new ByteArrayInputStream(data);
							amTemp.loadFromStream(in);
							in.close();
							if(amTemp.getVersion() < articleVersion){
								//删除session中的值
								session.removeAttribute(submitFileName);
								out.println("<script language='javascript'> alert('物品版本过低，当前版本"+amTemp.getVersion()+" 服务器版本"+articleVersion+"');</script>");
								return;
							}
							//给session赋值，这样可以在下个页面用
							session.setAttribute(submitFileName,data);
							Article[] articlesTemp = amTemp.getAllArticles();
							Article[] articles = am.getAllArticles();
							if(articlesTemp != null){
								for(Article at : articlesTemp){
									if(at != null){
										boolean exist = false;
										if(articles != null){
											for(Article a : articles){
												if(a != null && a.getName() != null && a.getName().equals(at.getName())){
													exist = true;
													break;
												}
											}
										}
										if(!exist){
											addContentList.add(at.getName());
										}
									}
								}
							}
							if(articles != null){
								for(Article a : articles){
									if(a != null){
										boolean exist = false;
										if(articlesTemp != null){
											for(Article at : articlesTemp){
												if(at != null && at.getName() != null && at.getName().equals(a.getName())){
													exist = true;
													break;
												}
											}
										}
										if(!exist){
											deleteContentList.add(a.getName());
										}
									}
								}
							}

						}
					}catch(Exception e){
						throw e;
					}
				}

				if(filename.equals("suitEquipment.xml")){
					submitFileName = "suitEquipment.xml";
					try{
						ArticleManager am = ArticleManager.getInstance();
						if(am == null){
							throw new Exception("物品管理器为空");
						}
						ArticleManager amTemp = new ArticleManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						ByteArrayInputStream in = new ByteArrayInputStream(data);
						amTemp.loadSuitEquipmentFromInputStream(in);
						in.close();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						SuitEquipment[] articlesTemp = amTemp.getAllSuitEquipments();
						SuitEquipment[] articles = am.getAllSuitEquipments();
						if(articlesTemp != null){
							for(SuitEquipment at : articlesTemp){
								if(at != null){
									boolean exist = false;
									if(articles != null){
										for(SuitEquipment a : articles){
											if(a != null && a.getSuitEquipmentName() != null && a.getSuitEquipmentName().equals(at.getSuitEquipmentName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getSuitEquipmentName());
									}
								}
							}
						}
						if(articles != null){
							for(SuitEquipment a : articles){
								if(a != null){
									boolean exist = false;
									if(articlesTemp != null){
										for(SuitEquipment at : articlesTemp){
											if(at != null && at.getSuitEquipmentName() != null && at.getSuitEquipmentName().equals(a.getSuitEquipmentName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getSuitEquipmentName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("buffs.xml")){
					submitFileName = "buffs.xml";
					try{
						BuffTemplateManager am = BuffTemplateManager.getInstance();
						if(am == null){
							throw new Exception("Buff管理器为空");
						}
						BuffTemplateManager amTemp = new BuffTemplateManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						ByteArrayInputStream in = new ByteArrayInputStream(data);
						amTemp.loadFromInputStream(in);
						in.close();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						BuffTemplate[] objTemp = amTemp.getAllBuffTemplates();
						BuffTemplate[] obj = am.getAllBuffTemplates();
						if(objTemp != null){
							for(BuffTemplate at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(BuffTemplate a : obj){
											if(a != null && a.getName() != null && a.getName().equals(at.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getName());
									}
								}
							}
						}
						if(obj != null){
							for(BuffTemplate a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(BuffTemplate at : objTemp){
											if(at != null && at.getName() != null && at.getName().equals(a.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("sprites.txt")){
					submitFileName = "sprites.txt";
					try{
						MonsterManager am = MemoryMonsterManager.getMonsterManager();
						if(am == null){
							throw new Exception("怪物管理器为空");
						}
						MonsterManager amTemp = new MemoryMonsterManager();
						String content = myFile.getContentString();
						byte datas[] = content.getBytes();
						InputStream fis = new ByteArrayInputStream(datas);
						byte[] buffer = new byte[1024];
						int len = -1;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						while((len = fis.read(buffer, 0 , buffer.length)) != -1){
							baos.write(buffer , 0, len);
						}
						byte[] data = baos.toByteArray();
						fis.close();
						baos.close();
						((MemoryMonsterManager)amTemp).setArticleManager(ArticleManager.getInstance());
						((MemoryMonsterManager)amTemp).loadFrom(new String(data ,0,data.length));
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						MemoryMonsterManager.MonsterTempalte[] objTemp = ((MemoryMonsterManager)amTemp).getMonsterTemaplates();
						MemoryMonsterManager.MonsterTempalte[] obj = ((MemoryMonsterManager)am).getMonsterTemaplates();
						if(objTemp != null){
							for(MemoryMonsterManager.MonsterTempalte at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(MemoryMonsterManager.MonsterTempalte a : obj){
											if(a != null && a.monster != null && a.monster.getName() != null && at.monster != null && a.monster.getName().equals(at.monster.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist && at.monster != null){
										addContentList.add(at.monster.getName());
									}
								}
							}
						}
						if(obj != null){
							for(MemoryMonsterManager.MonsterTempalte a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(MemoryMonsterManager.MonsterTempalte at : objTemp){
											if(at != null && at.monster != null && at.monster.getName() != null && a.monster != null && at.monster.getName().equals(a.monster.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist && a.monster != null){
										deleteContentList.add(a.monster.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("bornpoints.xml")){
					submitFileName = "bornpoints.xml";
					try{
						GameManager am = GameManager.getInstance();
						if(am == null){
							throw new Exception("Game管理器为空");
						}
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						out.println("怪物出生点暂时不比较数据");
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("NPC.ncm")){
					submitFileName = "NPC.ncm";
					try{
						NPCManager am = MemoryNPCManager.getNPCManager();
						if(am == null){
							throw new Exception("NPC管理器为空");
						}
						MemoryNPCManager amTemp = new MemoryNPCManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						amTemp.setTaskManager(TaskManager.getInstance());
						amTemp.loadFrom(new String(data ,0,data.length));
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						MemoryNPCManager.NPCTempalte[] objTemp = amTemp.getNPCTemaplates();
						MemoryNPCManager.NPCTempalte[] obj = ((MemoryNPCManager)am).getNPCTemaplates();
						if(objTemp != null){
							for(MemoryNPCManager.NPCTempalte at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(MemoryNPCManager.NPCTempalte a : obj){
											if(a != null && a.npc != null && a.npc.getName() != null && at.npc != null && a.npc.getName().equals(at.npc.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.npc.getName());
									}
								}
							}
						}
						if(obj != null){
							for(MemoryNPCManager.NPCTempalte a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(MemoryNPCManager.NPCTempalte at : objTemp){
											if(at != null && at.npc != null && at.npc.getName() != null && a.npc != null && at.npc.getName().equals(a.npc.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.npc.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("mapnpcs.xml")){
					submitFileName = "mapnpcs.xml";
					try{
						GameManager am = GameManager.getInstance();
						if(am == null){
							throw new Exception("Game管理器为空");
						}
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						out.println("NPC出生点暂时不比较数据");
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("tasks.xml")){
					submitFileName = "tasks.xml";
					try{
						TaskManager am = TaskManager.getInstance();
						if(am == null){
							throw new Exception("任务管理器为空");
						}
						TaskManager amTemp = new TaskManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						ByteArrayInputStream in = new ByteArrayInputStream(data);
						amTemp.setArticleEntityManager(ArticleEntityManager.getInstance());
						amTemp.setArticleManager(ArticleManager.getInstance());
						amTemp.setQuestionManager(QuestionManager.getInstance());
						amTemp.load(in);
						in.close();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						Task[] objTemp = amTemp.getAllTasks();
						Task[] obj = am.getAllTasks();
						if(objTemp != null){
							for(Task at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(Task a : obj){
											if(a != null && a.getName() != null && a.getName().equals(at.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getName());
									}
								}
							}
						}
						if(obj != null){
							for(Task a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(Task at : objTemp){
											if(at != null && at.getName() != null && at.getName().equals(a.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("menu_windows.xml")){
					submitFileName = "menu_windows.xml";
					try{
						WindowManager am = WindowManager.getInstance();
						if(am == null){
							throw new Exception("菜单管理器为空");
						}
						WindowManager amTemp = new WindowManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						ByteArrayInputStream in = new ByteArrayInputStream(data);
						amTemp.loadFrom(in);
						in.close();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						MenuWindow[] objTemp = amTemp.getWindows();
						MenuWindow[] obj = am.getWindows();
						if(objTemp != null){
							for(MenuWindow at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(MenuWindow a : obj){
											if(a != null && a.getTitle() != null && a.getTitle().equals(at.getTitle())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getTitle());
									}
								}
							}
						}
						if(obj != null){
							for(MenuWindow a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(MenuWindow at : objTemp){
											if(at != null && at.getTitle() != null && at.getTitle().equals(a.getTitle())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getTitle());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("reputes.xml")){
					submitFileName = "reputes.xml";
					try{
						ReputeManager am = ReputeManager.getInstance();
						if(am == null){
							throw new Exception("声望管理器为空");
						}
						ReputeManager amTemp = new ReputeManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						amTemp.loadFrom(new String(data, 0, data.length));
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						ReputeDefine[] objTemp = amTemp.getAllReputeDefines();
						ReputeDefine[] obj = am.getAllReputeDefines();
						if(objTemp != null){
							for(ReputeDefine at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(ReputeDefine a : obj){
											if(a != null && a.getName() != null && a.getName().equals(at.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getName());
									}
								}
							}
						}
						if(obj != null){
							for(ReputeDefine a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(ReputeDefine at : objTemp){
											if(at != null && at.getName() != null && at.getName().equals(a.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("shops.xml")){
					submitFileName = "shops.xml";
					try{
						ShopManager am = ShopManager.getInstance();
						if(am == null){
							throw new Exception("商店管理器为空");
						}
						ShopManager amTemp = new ShopManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						InputStream is = new ByteArrayInputStream(data);
						amTemp.setGoodsDiskCache(am.getGoodsDiskCache());
						amTemp.loadFromInputStream(is);
						is.close();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						Shop[] objTemp = amTemp.getShops().values().toArray(new Shop[0]);
						Shop[] obj = am.getShops().values().toArray(new Shop[0]);
						if(objTemp != null){
							for(Shop at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(Shop a : obj){
											if(a != null && a.getName() != null && a.getName().equals(at.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getName());
									}
								}
							}
						}
						if(obj != null){
							for(Shop a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(Shop at : objTemp){
											if(at != null && at.getName() != null && at.getName().equals(a.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("downcity.xml")){
					submitFileName = "downcity.xml";
					try{
						DownCityManager am = DownCityManager.getInstance();
						if(am == null){
							throw new Exception("副本管理器为空");
						}
						DownCityManager amTemp = new DownCityManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						InputStream is = new ByteArrayInputStream(data);
						amTemp.loadFromInputStream(is);
						is.close();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						DownCityInfo[] objTemp = amTemp.getDciList().toArray(new DownCityInfo[0]);
						DownCityInfo[] obj = am.getDciList().toArray(new DownCityInfo[0]);
						if(objTemp != null){
							for(DownCityInfo at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(DownCityInfo a : obj){
											if(a != null && a.getName() != null && a.getName().equals(at.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getName());
									}
								}
							}
						}
						if(obj != null){
							for(DownCityInfo a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(DownCityInfo at : objTemp){
											if(at != null && at.getName() != null && at.getName().equals(a.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
				if(filename.equals("questions.xml")){
					submitFileName = "questions.xml";
					try{
						QuestionManager am = QuestionManager.getInstance();
						if(am == null){
							throw new Exception("问题管理器为空");
						}
						QuestionManager amTemp = new QuestionManager();
						String content = myFile.getContentString();
						byte data[] = content.getBytes();
						InputStream is = new ByteArrayInputStream(data);
						amTemp.loadFromInputStream(is);
						is.close();
						//给session赋值，这样可以在下个页面用
						session.setAttribute(submitFileName,data);
						Question[] objTemp = amTemp.getAllQuestion().toArray(new Question[0]);
						Question[] obj = am.getAllQuestion().toArray(new Question[0]);
						if(objTemp != null){
							for(Question at : objTemp){
								if(at != null){
									boolean exist = false;
									if(obj != null){
										for(Question a : obj){
											if(a != null && a.getName() != null && a.getName().equals(at.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										addContentList.add(at.getName());
									}
								}
							}
						}
						if(obj != null){
							for(Question a : obj){
								if(a != null){
									boolean exist = false;
									if(objTemp != null){
										for(Question at : objTemp){
											if(at != null && at.getName() != null && at.getName().equals(a.getName())){
												exist = true;
												break;
											}
										}
									}
									if(!exist){
										deleteContentList.add(a.getName());
									}
								}
							}
						}
					}catch(Exception e){
						throw e;
					}
				}
			}
		}
		out.println("</table>");
%>
<table>
<tr class="titlecolor">
<td colspan="6">增加(<%=addContentList.size() %>)</td>
</tr>
<%
if(addContentList.size() != 0){
	int row = addContentList.size()/6;
	if(addContentList.size() % 6 != 0){
		row += 1;
	}
	for(int i = 0; i < row;i++){
		%>
		<tr>
		<%for(int j = 0; j < 6; j++){
			if((i*6+j) >= addContentList.size()){
				%><td class="tdnoborder"></td><%
			}else{
			%>
			<td class="tdnoborder"><%=addContentList.get(i*6+j) %></td>
			<%
			}
		} %>
		</tr>
		<%
	}
}

%>
<tr class="titlecolor">
<td colspan="6">删除(<%=deleteContentList.size() %>)</td>
</tr>
<%
if(deleteContentList.size() != 0){
	int row = deleteContentList.size()/6;
	if(deleteContentList.size() % 6 != 0){
		row += 1;
	}
	for(int i = 0; i < row;i++){
		%>
		<tr>
		<%for(int j = 0; j < 6; j++){
			if((i*6+j) >= deleteContentList.size()){
				%><td class="tdnoborder"></td><%
			}else{
			%>
			<td class="tdnoborder"><%=deleteContentList.get(i*6+j) %></td>
			<%
			}
		} %>
		</tr>
		<%
	}
}
OutputStream os = response.getOutputStream();
java.io.File file = new java.io.File("/home/game/resin-3.0.19/webapps/game_server/WEB-INF/game_init_config/buffs.xml");
InputStream is = new FileInputStream(file);
byte[] bs = new byte[is.available()];
is.read(bs);

os.write(bs);
%>
</table>
<form action="upLoadsFileOnServer.jsp" method="post">
	<input type="hidden" name="filename" value="<%=submitFileName %>">
	<input type="submit" value="确定提交修改">
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="button" value="返回" onclick="javascript:history.back();">
</form>
<%
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
