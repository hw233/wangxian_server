<%@page import="com.fy.engineserver.datasource.skill.Skill"%>
<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page
	import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkill"%>
<%@page import="java.util.Arrays"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.homestead.cave.PetHookInfo"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
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
	String petIds = request.getParameter("petId");
	long petId = 0;
	if (petIds != null && !petIds.isEmpty()) {
		petId = Long.valueOf(request.getParameter("petId"));
	}
	String type = request.getParameter("type");

	String gmUser = URLDecoder.decode(
			request.getParameter("gmUserName"), "utf-8");
	String skillId = request.getParameter("skillId"); //多个使用,分隔
	String tianfuSkillId = request.getParameter("tianfuSkillId"); //多个使用,分隔
	String entityId = request.getParameter("entityId"); //多个使用,分隔
	Map<String, String> result = new HashMap<String, String>();
	StringBuffer sb = new StringBuffer();
	try {
		Player player = null;
		Pet pet = null;
		try {
			player = GamePlayerManager.getInstance()
					.getPlayer(playerId);
			pet = PetManager.getInstance().getPet(petId);
		} catch (Exception e) {
			PetManager.logger.error("[后台修改宠物信息异常] [petId:" + petId
					+ "]", e);
		}
		if ("1".equals(type)) { //修改宠物技能
			if (pet == null) {
				result.put("success", "false");
				result.put("message", "对应id宠物不存在");
			} else {
				int[] oldBaseSkIds = null;
				int[] oldBaseLvs = null;
				int[] oldTfIds = null;
				int[] oldTfLvs = null;
				if (skillId != null && !skillId.isEmpty()) {
					String[] sks = null;
					if (skillId.contains(",")) {
						sks = skillId.split(",");
					} else {
						sks = new String[] { skillId };
					}
					int[] skIds = new int[sks.length];
					int[] skLv = new int[sks.length];
					CareerManager cm = CareerManager.getInstance();
					oldBaseSkIds = pet.getSkillIds();
					oldBaseLvs = pet.getActiveSkillLevels();
					for (int i = 0; i < skIds.length; i++) {
						skIds[i] = Integer.parseInt(sks[i]);
						boolean sameToOld = false;
						for (int ii = 0; ii < oldBaseSkIds.length; ii++) {
							if (oldBaseSkIds[ii] == skIds[i]) {
								skLv[i] = oldBaseLvs[ii];
								sameToOld = true;
							}
						}
						if(!sameToOld){
							skLv[i] = 1;
						}
						Skill skill = cm.getSkillById(skIds[i]);
						if (skill == null) {
							result.put("success", "false");
							result.put("message", "基础技能输入错误，不存在技能:"
									+ skIds[i]);
							PetManager.logger
									.error("[后台修改宠物信息] [petId:" + petId
											+ "] [基础技能输入错误，不存在技能:"
											+ skIds[i] + "]");
							JSONArray json = JSONArray
									.fromObject(result);
							out.print(json.toString());
							out.flush();
							out.close();
							return;
						}
					}
					//if (pet.getOwnerId() == player.getId()) {
					pet.setSkillIds(skIds);
					pet.setActiveSkillLevels(skLv);
					pet.init();
					sb.append(
							",oldBaseSkIds:"
									+ Arrays.toString(oldBaseSkIds))
							.append(",oldBaseLvs:"
									+ Arrays.toString(oldBaseLvs));
					sb.append(",newBaseSkIds:" + Arrays.toString(skIds))
							.append(",newBaseLvs:"
									+ Arrays.toString(skLv));
					PetManager.logger.error("[后台修改宠物信息] [petId:"
							+ petId + "] [" + sb.toString() + "]");
					//}
				}
				if (tianfuSkillId != null && !tianfuSkillId.isEmpty()) {
					String[] sks = null;
					if (tianfuSkillId.contains(",")) {
						sks = tianfuSkillId.split(",");
					} else {
						sks = new String[] { tianfuSkillId };
					}
					int[] skIds = new int[sks.length];
					int[] skLv = new int[sks.length];
					for (int i = 0; i < skIds.length; i++) {
						skIds[i] = Integer.parseInt(sks[i]);
						GenericSkill sk = GenericSkillManager.getInst().maps
								.get(skIds[i]);
						skLv[i] = 1;
						result.put("success", "false");
						result.put("message", "天赋技能输入错误，不存在技能:"
								+ skIds[i]);
						JSONArray json = JSONArray.fromObject(result);
						out.print(json.toString());
						out.flush();
						out.close();
						return;
					}
					//if (pet.getOwnerId() == player.getId()) {
					oldTfIds = pet.getTianFuSkIds();
					oldTfLvs = pet.getTianFuSkIvs();
					pet.setTianFuSkIds(skIds);
					pet.setTianFuSkIvs(skLv);
					pet.init();
					sb.append(",oldTfIds:" + Arrays.toString(oldTfIds))
							.append(",oldTfLvs:"
									+ Arrays.toString(oldTfLvs));
					sb.append(",newTfIds:" + Arrays.toString(skIds))
							.append(",newTfIds:"
									+ Arrays.toString(skLv));
					//}
				}
				result.put("success", "true");
				ArticleManager.logger.warn("[gm平台操作] [修改宠物技能] [修改者:"
						+ gmUser + "] [" + player.getLogString()
						+ "] [" + sb.toString() + "]");
			}
		} else if ("2".equals(type)) {
			Cave cave = FaeryManager.getInstance().getCave(player);

			if (cave != null && pet != null) {
				PetHookInfo[] infos = cave.getPethouse().getHookInfos();
				boolean caveHavePet = false;
				for (int i = 0; i < infos.length; i++) {
					PetHookInfo hi = infos[i];
					if (hi != null) {
						if (hi.getPetId() == petId) {
							caveHavePet = true;
							infos[i] = null;
							cave.notifyFieldChange("pethouse");

							pet.setHookInfo(null);
							PetManager.em.notifyFieldChange(pet,
									"hookInfo");
							result.put("success", "true");
							PetManager.logger.error("[后台删除挂机宠物] ["
									+ pet.getLogString() + "] [洞府id:"
									+ cave.getId() + "]");
							ArticleManager.logger
									.warn("[gm平台操作] [删除挂机宠物] [修改者:"
											+ gmUser + "] ["
											+ player.getLogString()
											+ "] [petId:" + petId + "]");
							/* out.print("后台删除挂机宠物<br/>");
							out.print("完成"); */
							break;
						}
					}
				}
				if (!caveHavePet) {
					PetHookInfo info = pet.getHookInfo();
					long infoCaveId = -1;
					if (info != null) {
						infoCaveId = info.getHouseOwnerId();
					}
					result.put("success", "false");
					result.put("message", "后台删除挂机宠物,宠物不在此仙府中,宠物存在于:"
							+ infoCaveId + " 的仙府中,输入的playerId为:"
							+ player.getId());
				}
			}
			if (cave != null && pet == null) {
				PetHookInfo[] infos = cave.getPethouse().getHookInfos();
				boolean caveHavePet = false;
				for (int i = 0; i < infos.length; i++) {
					PetHookInfo hi = infos[i];
					if (hi != null) {
						if (hi.getPetId() == petId) {
							caveHavePet = true;
							infos[i] = null;
							cave.notifyFieldChange("pethouse");
							result.put("success", "true");
							ArticleManager.logger
									.warn("[gm平台操作] [删除挂机宠物] [修改者:"
											+ gmUser + "] ["
											+ player.getLogString()
											+ "] [petId:" + petId + "]");
							break;
						}
					}
				}
				if (!caveHavePet) {
					result.put("success", "false");
					result.put("message", "对应id宠物不存在");
				}
			} else if (cave == null) {
				result.put("success", "false");
				result.put("message", "后台删除挂机宠物,仙府不存在或者该玩家仙府已被封印");
			} else if (pet == null) {
				result.put("success", "false");
				result.put("message", "对应id宠物不存在");
			}
		} else if ("3".equals(type)) { //删除无实体道具宠物
			long eId = Long.parseLong(entityId);
			//Player player = PlayerManager.getInstance().getPlayer(name);
			Knapsack k = player.getPetKnapsack();
			boolean has = false;
			Cell[] cells = k.getCells();
			for (Cell c : cells) {
				long id = c.getEntityId();

				if (id == eId) {
					has = true;
					c.entityId = -1;
					k.setModified(true);
					ArticleManager.logger
							.warn("[gm平台操作] [删除指定宠物道具] [修改者:" + gmUser
									+ "] [" + player.getLogString()
									+ "] [id:" + id + "]");
					result.put("success", "true");
					break;
				}
			}
			if (!has) {
				result.put("success", "false");
				result.put("message", "没有对应id物品");
			}
		} else if ("4".equals(type)) { //把某个宠物给某玩家
			long ownerIds = Long.valueOf(request
					.getParameter("ownerIds"));
			long ppIds = Long.valueOf(request.getParameter("ppIds"));
			if (pet != null) {

				ArticleManager.logger
						.error("[gm平台操作] [后台设置宠物道具id] [前] [ownerId:"
								+ pet.getOwnerId() + "] [propsId:"
								+ pet.getPetPropsId() + "]");
				pet.setPetPropsId(ppIds);
				pet.setOwnerId(ownerIds);
				ArticleManager.logger
						.error("[gm平台操作] [后台设置宠物道具id] [后] [ownerId:"
								+ pet.getOwnerId() + "] [propsId:"
								+ pet.getPetPropsId() + "]");
				result.put("success", "true");
			} else {
				result.put("success", "false");
				result.put("message", "没有这个宠物");
			}
		} else if ("5".equals(type)) {
			if (pet.isDelete()) {
				pet.setDelete(false);
				result.put("success", "true");
			}
		} else if ("6".equals(type)) { //生成新宠物
			String egg = URLDecoder.decode(request.getParameter("egg"),
					"utf-8");
			String pwd = request.getParameter("pwd");
			String generation = request.getParameter("generation");
			String variation = request.getParameter("variation");
			if (egg != null && !egg.equals("")) {
				if (!"buzhidaomimajiubiefa".equals(pwd)) {
					result.put("success", "false");
					result.put("message", "密码错误");
					JSONArray json = JSONArray.fromObject(result);
					out.print(json.toString());
					out.flush();
					out.close();
					return;
				}
				byte gt = Byte.valueOf(generation);
				byte vt = Byte.valueOf(variation);
				if (gt < 0 || gt > 1 || vt < 0 || vt > 5) {
					result.put("success", "false");
					result.put("message", "generation或variation错误，");
					JSONArray json = JSONArray.fromObject(result);
					out.print(json.toString());
					out.flush();
					out.close();
					return;
				}
				Article a = ArticleManager.getInstance()
						.getArticle(egg);
				if (a != null) {
					if (a instanceof PetEggProps) {
						PetEggProps pep = (PetEggProps) a;

						String petArticleS = pep.getPetArticleName();
						Article petArticle = ArticleManager
								.getInstance().getArticle(petArticleS);
						if (petArticle != null) {

							if (petArticle instanceof PetProps) {
								PetProps pp = (PetProps) petArticle;

								Pet petNew = PetManager.getInstance()
										.createPetByProps(pep, pp);
								if (petNew != null) {
									petNew.setVariation(vt);
									petNew.setGeneration(gt);
									PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager
											.getInstance()
											.createEntity(
													petArticle,
													false,
													ArticleEntityManager.CREATE_REASON_后台,
													null, 0, 1, true);
									ppe.setEggArticle(a.getName());
									ppe.setPetId(petNew.getId());
									petNew.setPetPropsId(ppe.getId());
									ArticleManager.logger
											.warn("[gm平台操作] [生产新宠物] [修改者:"
													+ gmUser
													+ "] ["
													+ player.getLogString()
													+ "] ["
													+ petNew.getLogString()
													+ "]");
									result.put("success", "true");
									result.put("message", "成功");
									result.put("eggId", ppe.getId()
											+ "");
								} else {
									result.put("success", "false");
									result.put("message", "异常");
								}
							} else {
								result.put("success", "false");
								result.put("message", "宠物蛋错误");
							}

						} else {
							result.put("success", "false");
							result.put("message", "宠物蛋道具不存在");
						}
					} else {
						result.put("success", "false");
						result.put("message", "输入道具名不是宠物蛋");
					}

				} else {
					result.put("success", "false");
					result.put("message", "不存在物品");
				}
			}
		} else {
			result.put("success", "false");
			result.put("message", "操作类型错 , type:" + type);
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