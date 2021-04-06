
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%><%@page
	import="com.fy.engineserver.homestead.cave.PetHookInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.achievement.RecordAction"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.newtask.actions.TaskActionOfCaveBuild"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="com.fy.engineserver.sprite.npc.CaveNPC"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveSchedule"%>
<%@page
	import="com.fy.engineserver.homestead.cave.CaveFieldBombData"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveField"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveBuilding"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveFence"%>
<%@page import="com.fy.engineserver.homestead.cave.CavePethouse"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveStorehouse"%>
<%@page import="com.fy.engineserver.sprite.npc.NPC"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryConfig"%>
<%@page import="com.fy.engineserver.homestead.cave.CaveMainBuilding"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="java.net.URLDecoder"%><%@page
	import="com.fy.boss.gm.gmpagestat.GmEventManager"%><%@page
	import="net.sf.json.JSONArray"%><%@page import="java.util.HashMap"%><%@page
	import="java.util.Map"%><%@page
	import="com.fy.engineserver.mail.service.MailManager"%><%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%><%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%><%@page
	import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%><%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%><%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%><%@page
	import="com.fy.engineserver.sprite.Player"%><%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%><%@ page
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	long playerId = Long.valueOf(request.getParameter("playerId"));
	String type = request.getParameter("type");
	String gmUser = URLDecoder.decode(request.getParameter("gmUserName"), "utf-8");
	Map<String, Object> result = new HashMap<String, Object>();
	try {
		Player player = null;
		Cave cave = null;
		try {
			player = GamePlayerManager.getInstance().getPlayer(playerId);
			cave = FaeryManager.getInstance().getCave(player);
		} catch (Exception e) {

		}
		if (cave != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if ("1".equals(type)) {
				List<String> resource = new ArrayList<String>();
				resource.add("粮食:" + cave.getCurrRes().getFood());
				resource.add("木头:" + cave.getCurrRes().getWood());
				resource.add("石料:" + cave.getCurrRes().getStone());
				result.put("resources", resource);
				result.put("food", cave.getStorehouse().getFoodLevel());
				result.put("wood", cave.getStorehouse().getWoodLevel());
				result.put("stone", cave.getStorehouse().getStoneLevel());
				result.put("lastTime", sdf.format((cave.getOwnerLastVisitTime())));

				List<Map<String, Object>> assets = new ArrayList<Map<String, Object>>();
				Map<String, Object> xiaoyaoju = new LinkedHashMap<String, Object>();
				CaveMainBuilding mainB = cave.getMainBuilding();
				NPC n = mainB.getNpc();
				xiaoyaoju.put("name", n.getName());
				xiaoyaoju.put("level", mainB.getGrade());
				xiaoyaoju.put("state", FaeryConfig.CAVE_BUILDING_STATUS_ARR[mainB.getStatus()]);
				xiaoyaoju.put("position", new int[] { n.getX(), n.getY() });
				xiaoyaoju.put("dateTime", sdf.format((cave.getMainBuilding().getLvUpStartTime())));
				assets.add(xiaoyaoju);

				Map<String, Object> wanbaoku = new LinkedHashMap<String, Object>();
				CaveStorehouse cs = cave.getStorehouse();
				n = cs.getNpc();
				wanbaoku.put("name", n.getName());
				wanbaoku.put("level", cs.getGrade());
				wanbaoku.put("state", FaeryConfig.CAVE_BUILDING_STATUS_ARR[cs.getStatus()]);
				wanbaoku.put("position", new int[] { n.getX(), n.getY() });
				wanbaoku.put("dateTime", sdf.format((cs.getLvUpStartTime())));
				assets.add(wanbaoku);

				Map<String, Object> yushouzhai = new LinkedHashMap<String, Object>();
				CavePethouse ps = cave.getPethouse();
				n = ps.getNpc();
				yushouzhai.put("name", n.getName());
				yushouzhai.put("level", ps.getGrade());
				yushouzhai.put("state", FaeryConfig.CAVE_BUILDING_STATUS_ARR[ps.getStatus()]);
				yushouzhai.put("position", new int[] { n.getX(), n.getY() });
				yushouzhai.put("dateTime", sdf.format((ps.getLvUpStartTime())));
				assets.add(yushouzhai);

				Map<String, Object> men = new LinkedHashMap<String, Object>();
				CaveFence cf = cave.getFence();
				n = cf.getNpc();
				men.put("name", n.getName());
				men.put("level", cf.getGrade());
				men.put("state", FaeryConfig.CAVE_BUILDING_STATUS_ARR[cf.getStatus()]);
				men.put("position", new int[] { n.getX(), n.getY() });
				men.put("dateTime", sdf.format((cf.getLvUpStartTime())));
				assets.add(men);
				result.put("assets", assets);

				List<Map<String, Object>> hookInfo = new ArrayList<Map<String, Object>>();
				{
					for (PetHookInfo pi : cave.getPethouse().getHookInfos()) {
						if (pi != null) {
							Map<String, Object> petInfos = new LinkedHashMap<String, Object>();
							petInfos.put("petOwnerId", pi.getPetOwnerId());
							petInfos.put("petOwnerName", pi.getPetOwnerName());
							petInfos.put("petId", pi.getPetId());
							petInfos.put("petGrade", pi.getPetGrade());
							petInfos.put("petName", pi.getPetName());
							petInfos.put("petName", pi.getPetName());
							petInfos.put("caveId", pi.getCaveId());
							petInfos.put("houseOwnerId", pi.getHouseOwnerId());
							petInfos.put("houseOwnerName", pi.getHouseOwnerName());
							petInfos.put("hookTime", pi.getHookTime());
							petInfos.put("articleId", pi.getArticleId());
							hookInfo.add(petInfos);
						}
					}
					result.put("PetHookInfo", hookInfo);
				}

				List<Map<String, Object>> personalPet = new ArrayList<Map<String, Object>>();
				{
					for (long petPropsId : cave.getPethouse().getStorePets()) {
						if (petPropsId > 0) {
							Map<String, Object> petInfos = new LinkedHashMap<String, Object>();
							ArticleEntityManager aem = ArticleEntityManager.getInstance();
							ArticleEntity ae =  aem.getEntity(petPropsId);
							long petId = 0;
							if(ae != null){
								if(ae instanceof PetPropsEntity){
									PetPropsEntity ppe = (PetPropsEntity)ae;
									petId = ppe.getPetId();
								}else if(ae instanceof PetEggPropsEntity){
									PetEggPropsEntity ppp = (PetEggPropsEntity)ae;
									petId = ppp.getPetId();
								}else{
									
								}
							}else{
								out.print("没有这个实体");
							}
							if(petId > 0){
								Pet pet = PetManager.getInstance().getPet(petId);
								if (pet != null) {
									petInfos.put("petId", petId);
									petInfos.put("petName", pet.getName());
									petInfos.put("articleId", pet.getPetPropsId());
									hookInfo.add(petInfos);
								}else{
									petInfos.put("petId", petId);
									petInfos.put("petGrade", "这只宠不存在了！！");
								}
							}
							personalPet.add(petInfos);
						}
					}
					result.put("personalPet", personalPet);
				} 

				List<Map<String, Object>> assetsInfo = new ArrayList<Map<String, Object>>();
				for (Iterator<Long> itor = cave.getBuildings().keySet().iterator(); itor.hasNext();) {
					Map<String, Object> tm = new LinkedHashMap<String, Object>();
					long id = itor.next();
					CaveBuilding cb = cave.getBuildings().get(id);
					if (cb != null) {
						String spString = "";
						if (cb instanceof CaveField) {
							CaveField cff = (CaveField) cb;
							if (cff.getCaveFieldBombData() != null) {
								CaveFieldBombData cfb = cff.getCaveFieldBombData();
								spString = cfb.toString();
							}
						}
						tm.put("name", cb.getNpc().getName() + (spString == null ? "" : "[" + spString + "]"));
						if (cb.getSchedules() != null && cb.getSchedules().size() > 0) {
							for (CaveSchedule css : cb.getSchedules()) {
								if (css != null) {
									tm.put("state", css.toString());
								} else {
									tm.put("state", "无");
								}
							}
						} else {
							tm.put("state", "空闲");
						}
					}
					assetsInfo.add(tm);
				}
				result.put("success", "true");
				result.put("assetsInfo", assetsInfo);
			} else if ("2".equals(type)) { //仙府无选项
				long caveId = player.getCaveId();
				if (caveId > 0) {
					Cave cave2 = FaeryManager.caveEm.find(caveId);
					if (cave2 == null) {
						player.setCaveId(0);
						result.put("success", "true");
					} else {
						result.put("success", "false");
						result.put("message", "玩家仙府存在，不能清除");
					}

				} else {
					if (cave != null && cave.getOwnerId() == player.getId()) {
						player.setCaveId(cave.getId());
						result.put("success", "true");
					} else {
						result.put("success", "false");
						result.put("message", "玩家本来就木有仙府");
					}
				}
			} else if ("3".equals(type)) { //修复仙府进度异常
				if (cave == null) {
					result.put("success", "false");
					result.put("message", "玩家本来就木有仙府");
				} else {
					for (Iterator<Long> it = cave.getBuildings().keySet().iterator(); it.hasNext();) {
						Long id = it.next();
						CaveBuilding cb = cave.getBuildings().get(id);
						if (cb != null) {
							List<CaveSchedule> csList = cb.getSchedules();
							List<CaveSchedule> remove = new ArrayList<CaveSchedule>();
							if (csList != null) {
								for (CaveSchedule cs : csList) {
									if (cs.hasDone()) {
										remove.add(cs);
									}
								}
								if (remove.size() > 0) {
									for (CaveSchedule rm : remove) {
										if (rm != null) {
											cb.removeScheduleForDone(rm.getOptionType());
											cave.notifyFieldChange(cb.getType());
										}
									}
									result.put("success", "true");
								} else {
									result.put("success", "false");
									result.put("message", "没有异常数据");
								}
							}
						}
					}
				}
			} else if ("4".equals(type)) { //修复资源存储等级
				boolean changed = false;
				if (cave.getStorehouse().getFoodLevel() > 20) {
					cave.getStorehouse().setFoodLevel(20);
					changed = true;
				}
				if (cave.getStorehouse().getWoodLevel() > 20) {
					cave.getStorehouse().setWoodLevel(20);
					changed = true;
				}
				if (cave.getStorehouse().getStoneLevel() > 20) {
					cave.getStorehouse().setStoneLevel(20);
					changed = true;
				}
				if (changed) {
					cave.notifyFieldChange(cave.getStorehouse().getType());
				}
				result.put("success", "true");
			} else if ("5".equals(type)) { //修复建筑一直升级
				String modify = request.getParameter("modify");
				if ("xiaoyaoju".equals(modify)) {
					CaveMainBuilding cmb = cave.getMainBuilding();
					if (cmb.inBuilding()) {
						CaveSchedule caveSchedule = cmb.getScheduleByOptType(Cave.OPTION_LEVEL_UP);
						if (caveSchedule != null) {
						}
						cmb.setGrade(cmb.getGrade() + 1);
						((CaveNPC) cmb.getNpc()).setGrade(cmb.getGrade());
						((CaveNPC) cmb.getNpc()).setInBuilding(false);
						ResourceManager.getInstance().setAvata(((CaveNPC) cmb.getNpc()));
						cmb.removeScheduleForDone(cave.OPTION_LEVEL_UP);
						cmb.setStatus(cave.CAVE_BUILDING_STATUS_SERVICE);

						Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
						owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(cmb.getType(), cmb.getGrade()));
						if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
							owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, cmb.getNpc().getName() }, { Translate.STRING_2, String.valueOf(cmb.getGrade()) } }));
						}
						if (cave.logger.isWarnEnabled()) {
							cave.logger.warn("[建筑升级完成] [{}] [升级后等级:{}] [主人:{}] [建筑ID:{}]", new Object[] { cmb.getNpc().getName(), cmb.getGrade(), cave.getOwnerId(), cmb.getNpc().getId() });
						}

						cave.notifyFieldChange(cmb.getType());
						{
							// 主建筑级别统计
							AchievementManager.getInstance().record(owner, RecordAction.仙府主建筑达到等级, cmb.getGrade());
						}
					}
				}
				if ("wanbaoku".equals(modify)) {
					CaveStorehouse csh = cave.getStorehouse();
					if (cave.getStorehouse().inBuilding()) {
						CaveSchedule caveSchedule = cave.getStorehouse().getScheduleByOptType(Cave.OPTION_LEVEL_UP);
						if (caveSchedule != null) {
						}
					}
					csh.setGrade(csh.getGrade() + 1);
					((CaveNPC) csh.getNpc()).setGrade(csh.getGrade());
					((CaveNPC) csh.getNpc()).setInBuilding(false);
					ResourceManager.getInstance().setAvata(((CaveNPC) csh.getNpc()));
					csh.removeScheduleForDone(cave.OPTION_LEVEL_UP);
					csh.setStatus(cave.CAVE_BUILDING_STATUS_SERVICE);

					Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
					owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(csh.getType(), csh.getGrade()));
					if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
						owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, csh.getNpc().getName() }, { Translate.STRING_2, String.valueOf(csh.getGrade()) } }));
					}
					if (cave.logger.isWarnEnabled()) {
						cave.logger.warn("[建筑升级完成] [{}] [升级后等级:{}] [主人:{}] [建筑ID:{}]", new Object[] { csh.getNpc().getName(), csh.getGrade(), cave.getOwnerId() }, csh.getNpc().getId());
					}
					cave.notifyFieldChange(csh.getType());
				}
				if ("yushouzhai".equals(modify)) {
					CavePethouse cph = cave.getPethouse();
					if (cave.getPethouse().inBuilding()) {
						CaveSchedule caveSchedule = cave.getPethouse().getScheduleByOptType(Cave.OPTION_LEVEL_UP);
						if (caveSchedule != null) {
						}
						cph.setGrade(cph.getGrade() + 1);
						((CaveNPC) cph.getNpc()).setGrade(cph.getGrade());
						cph.removeScheduleForDone(cave.OPTION_LEVEL_UP);

						((CaveNPC) cph.getNpc()).setInBuilding(false);
						ResourceManager.getInstance().setAvata(((CaveNPC) cph.getNpc()));
						cph.setStatus(cave.CAVE_BUILDING_STATUS_SERVICE);

						Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
						owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(cph.getType(), cph.getGrade()));
						if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
							owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, cph.getNpc().getName() }, { Translate.STRING_2, String.valueOf(cph.getGrade()) } }));
						}
						if (cave.logger.isWarnEnabled()) {
							cave.logger.warn("[建筑升级完成] [{}] [升级后:{}] [主人:{}] [建筑ID:{}]", new Object[] { cph.getNpc().getName(), cph.getGrade(), cave.getOwnerId() }, cph.getNpc().getId());
						}
						cave.notifyFieldChange(cph.getType());
					}
				}
				if ("tiandi".equals(modify)) {
					for (CaveField caveField : cave.getFields()) {
						if (caveField.inBuilding()) {
							CaveSchedule caveSchedule = caveField.getScheduleByOptType(Cave.OPTION_LEVEL_UP);
							if (caveSchedule != null) {
							}
							caveField.setGrade(caveField.getGrade() + 1);
							((CaveNPC) caveField.getNpc()).setGrade(caveField.getGrade());
							((CaveNPC) caveField.getNpc()).setInBuilding(false);
							ResourceManager.getInstance().setAvata(((CaveNPC) caveField.getNpc()));
							caveField.removeScheduleForDone(Cave.OPTION_LEVEL_UP);
							caveField.setStatus(Cave.CAVE_BUILDING_STATUS_SERVICE);

							Player owner = GamePlayerManager.getInstance().getPlayer(cave.getOwnerId());
							owner.dealWithTaskAction(TaskActionOfCaveBuild.createTaskAction(caveField.getType(), caveField.getGrade()));
							if (GamePlayerManager.getInstance().isOnline(cave.getOwnerId())) {
								owner.sendNotice(Translate.translateString(Translate.text_cave_064, new String[][] { { Translate.STRING_1, caveField.getNpc().getName() }, { Translate.STRING_2, String.valueOf(caveField.getGrade()) } }));
							}
							cave.notifyFieldChange(caveField.getType());
							if (cave.logger.isWarnEnabled()) {
								cave.logger.warn(cave.getOwnerId() + "[建筑升级完成] [" + caveField.getNpc().getName() + "] [升级后:" + caveField.getGrade() + "] [主人:" + cave.getOwnerName() + "] [建筑ID:" + caveField.getNpc().getId() + "]");
							}

						}
					}
				}
				result.put("success", "true");
			}
		} else {
			result.put("success", false);
			result.put("message", "仙府不存在");
		}
	} catch (Exception e) {
		result.put("result", "页面出现异常" + e.getMessage());
		e.printStackTrace();
	}
	JSONArray json = JSONArray.fromObject(result);
	out.print(json.toString());
	out.flush();
	out.close();
%>