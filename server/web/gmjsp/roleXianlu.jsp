<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.wafen.model.FenMuModel"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.activity.wafen.instacne.model.FenDiModel"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.fy.engineserver.activity.wafen.manager.WaFenManager"%>
<%@page import="com.fy.engineserver.activity.wafen.instacne.WaFenInstance4Private"%>
<%@page import="com.fy.engineserver.event.EventRouter"%>
<%@page import="com.fy.engineserver.event.cate.EventWithObjParam"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerAim"%>
<%@page import="com.fy.engineserver.playerAims.model.PlayerAimModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimManager"%>
<%@page import="com.fy.engineserver.playerAims.model.ChapterModel"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerChapter"%>
<%@page import="com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerAimsEntity"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.seal.SealManager"%>
<%@page import="com.fy.engineserver.seal.data.Seal"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page import="java.util.Map"%><%@page import="com.fy.engineserver.mail.service.MailManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page import="com.fy.engineserver.sprite.Player"%><%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String type = request.getParameter("type");		//1查看章节领取状态   2检测并完成已完成目标   3完成某个目标  4完成章节所有目标  5周年活动产出  6查看封印
	String gmUser = URLDecoder.decode(request.getParameter("gmUserName"),"utf-8");
	String aimId = request.getParameter("aimId");
	String aimName = request.getParameter("aimName");
	Map<String, Object> result = new HashMap<String, Object>();
	try {
		int typeIndex = Integer.parseInt(type);
		if (typeIndex <= 5) {							//只有这几个需要获取角色信息
			Player player = null;
			PlayerAimsEntity entity = null;
			try {
				player = GamePlayerManager.getInstance().getPlayer(playerId);
				entity = PlayerAimeEntityManager.instance.getEntity(player.getId());
			} catch (Exception e) {
				
			}
			if (typeIndex == 1) {
				List<Object> tempList = new ArrayList<Object>();
				for (PlayerChapter pct : entity.getChapterList()) {
					ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(pct.getChapterName()) ;
					if (cm == null) {
						continue;
					}
					List<Integer> aimIds = new ArrayList<Integer>();
					for (PlayerAimModel pams : cm.getAimsList()) {
						for (PlayerAim pas : entity.getAimList()) {
							if (pams.getId() == pas.getAimId() && !aimIds.contains(pams.getId())) {
								aimIds.add(pams.getId());
							}
						}
					}
					Map<String,Object> tempM = new LinkedHashMap<String,Object>();
					tempM.put("name", pct.getChapterName());
					tempM.put("state", (pct.getReceiveType()<=0?"未领取":"已领取"));
					tempM.put("point", entity.updateChapterScore(cm, true));
					tempM.put("overTarget", aimIds);
					tempList.add(tempM);
				}
				result.put("xianlu", tempList);
			} else if (typeIndex == 2) {
				EventWithObjParam evt = new EventWithObjParam(com.fy.engineserver.event.Event.CHECK_PLAYER_AIMS, new Object[] { player.getId()});
				EventRouter.getInst().addEvent(evt);
				result.put("success", "true");
			} else if (typeIndex == 3) {
				boolean b = false;
				for (PlayerAim pa1 : entity.getAimList()) {
					if (pa1.getAimId() == Integer.parseInt(aimId)) {
						result.put("success", "false");
						result.put("message", "已经完成了此目标");
						b = true;
						break;
					}
				}
				if (!b) {
					PlayerAimModel pam = PlayerAimManager.instance.aimMaps.get(Integer.parseInt(aimId));
					long now = System.currentTimeMillis();
					PlayerAim aim = new PlayerAim();
					aim.setAimId(pam.getId());
					aim.setCompletTime(now);
					aim.setReveiveStatus((byte) 0);
					List<PlayerAim> aimList = entity.getAimList();
					aimList.add(aim);
					entity.setAimList(aimList);
					ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(pam.getChapterName());
					if(cm != null) {
						entity.updateChapterScore(cm, true);
					} else {
					}
					ArticleManager.logger.warn("[gm平台操作] [完成某条仙录] [修改者:"+gmUser+"] [" + player.getLogString() + "] [aimId:" + pam.getId() + "]");
					result.put("success", "true");
				}
			} else if  (typeIndex == 4) {
				boolean b = false;
				aimName = URLDecoder.decode(aimName,"utf-8");
				ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(aimName);
				if (cm == null) {
					result.put("success", "false");
					result.put("message", "章节不存在");
					b = true;
				}
				if (!b) {
					List<String> rr = new ArrayList<String>();
					for (int i=0; i<cm.getAimsList().size(); i++) {
						PlayerAimModel pam = PlayerAimManager.instance.aimMaps.get(cm.getAimsList().get(i).getId());
						long now = System.currentTimeMillis();
						PlayerAim aim = new PlayerAim();
						aim.setAimId(pam.getId());
						aim.setCompletTime(now);
						aim.setReveiveStatus((byte) 0);
						boolean has = false;
						List<PlayerAim> aimList = entity.getAimList();
						for (PlayerAim paa : aimList) {
							if (paa.getAimId() == aim.getAimId()) {
								has = true;
								break;
							}
						}
						if (!has) {
							aimList.add(aim);
							entity.setAimList(aimList);
							//ChapterModel cm = PlayerAimManager.instance.chapterMaps.get(pam.getChapterName());
							if(cm != null) {
								entity.updateChapterScore(cm, true);
							}
						}
					}
					ArticleManager.logger.warn("[gm平台操作] [完成某章节仙录] [修改者:"+gmUser+"] [" + player.getLogString() + "] [aimName:" + aimName + "]");
					result.put("success", "true");
				} 
			} else if (typeIndex == 5) {
				boolean bb = false;
				WaFenInstance4Private wp = WaFenManager.instance.privateMaps.get(player.getId());
				if (wp == null) {
					bb = true;
					result.put("success", "false");
					result.put("message", "没有此人寻宝记录");
				}
				if (!bb) {
					DecimalFormat df = new DecimalFormat( "0.00 ");
					Iterator<String> ite = wp.getOpenFendiMaps().keySet().iterator();
					while (ite.hasNext()) {
						String key = ite.next();
						Map<String, Integer> tempMap = new HashMap<String, Integer>();
						Map<String, Integer> tempMap2 = new HashMap<String, Integer>();
						List<FenDiModel> list = wp.getOpenFendiMaps().get(key);
						FenMuModel fmm = WaFenManager.instance.fenmuMap.get(key);
						int count1 = 0;
						int count2 = 0;
						if (list != null && list.size() > 0) {
							for (int i=0; i<list.size(); i++) {
								String articleName = fmm.getArticleMap().get(list.get(i).getArticleId()).getArticleCNNName();
								int colorType = fmm.getArticleMap().get(list.get(i).getArticleId()).getColorType();
								String key1 = articleName + "__" + colorType;
								if (!list.get(i).isBind()) {
									if (tempMap.get(key1) != null) {
										int tempp = tempMap.get(key1);
										tempMap.put(key1, tempp+1);
										count1++;
									} else {
										tempMap.put(key1, 1);
										count1++;
									}
								} else {
									if (tempMap2.get(key1) != null) {
										int tempp = tempMap2.get(key1);
										tempMap2.put(key1, tempp+1);
										count2++;
									} else {
										tempMap2.put(key1, 1);
										count2++;
									}
								}
							}
						}
						List<Object> tempList = new ArrayList<Object>();
						Iterator<String> ite2 = tempMap.keySet().iterator();
						while (ite2.hasNext()) {
							String key2 = ite2.next();
							int value2 = tempMap.get(key2);
							String[] ttt = key2.split("__");
							Map<String,Object> mm = new LinkedHashMap<String,Object>();
							mm.put("name", ttt[0]);
							mm.put("color", ttt[1]);
							mm.put("chance", df.format((((double)value2/(double)count1) * 100d))+"%");
							mm.put("isLock", "false");
							tempList.add(mm);
						}
						Iterator<String> ite3 = tempMap2.keySet().iterator();
						while (ite3.hasNext()) {
							String key3 = ite3.next();
							int value3 = tempMap2.get(key3);
							String[] ttt = key3.split("__");
							Map<String,Object> mm = new LinkedHashMap<String,Object>();
							mm.put("name", ttt[0]);
							mm.put("color", ttt[1]);
							mm.put("chance", df.format((((double)value3/(double)count1) * 100d))+"%");
							mm.put("isLock", "true");
							tempList.add(mm);
						}
				}
			}
		}
		}else if (typeIndex == 6) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Seal seal = SealManager.getInstance().getSeal();	
			Map<String,Object> temp = new LinkedHashMap<String,Object>();
			temp.put("level", seal.getSealLevel());
			temp.put("dateTime", sdf.format(seal.getSealCanOpenTime()));
			temp.put("haotian", seal.getFirstPlayerIdInCountry()[0]);
			temp.put("wuchen", seal.getFirstPlayerIdInCountry()[1]);
			temp.put("cangyue", seal.getFirstPlayerIdInCountry()[2]);
			result.put("sealInfo", temp);
		}
	} catch (Exception e) {
		result.put("result","页面出现异常" + e.getMessage());
		e.printStackTrace();
	}
	ArticleManager.logger.warn("[后台gm操作] [roleXianlu.jsp] []");
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString()); 
	out.flush();
	out.close();
%>