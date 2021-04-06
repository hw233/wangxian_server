
<%@page import="com.gm.servlet.GMServlet"%>
<%@page import="java.io.IOException"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="net.sf.json.JSON"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page
	import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.talent.TalentPageInfo"%>
<%@page import="com.fy.engineserver.talent.TalentSkillInfo"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.fy.engineserver.talent.FlyTalentManager"%>
<%@page import="com.fy.engineserver.talent.TalentData"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.BaoShiXiaZiData"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.InlayArticle"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.PetPropsEntity"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn"%>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitle"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page
	import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.country.manager.CountryManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page
	import="com.fy.engineserver.gateway.MieshiGatewayClientService"%>
<%@page import="com.fy.engineserver.message.GM_ACTION_REQ"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.gamegateway.gmaction.GmAction"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page
	import="com.fy.engineserver.masterAndPrentice.MasterPrentice"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="net.sf.json.JSONObject"%><%@page import="java.util.*"%><%@page
	import="com.xuanzhi.tools.text.JsonUtil"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.net.URLDecoder"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%!Map<String, Object> xiaZiData = new HashMap<String, Object>();
	Map<String, Object> talentData = new HashMap<String, Object>();

	public void handle_XiaZiData(EquipmentEntity ee, String key, Player p) {
		if (ee != null) {
			StringBuffer sb = new StringBuffer();
			long[] inlayArticleIds = ee.getInlayArticleIds();
			if (inlayArticleIds != null && inlayArticleIds.length > 0) {
				for (long id : inlayArticleIds) {
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(id);
					if (ae != null) {
						Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
						if (a != null && (a instanceof InlayArticle) && ((InlayArticle) a).getInlayType() > 1) {
							BaoShiXiaZiData data = ArticleManager.getInstance().getxiLianData(p, id);
							if (data != null) {
								long bids[] = data.getIds();
								int cellIndex = 0;
								sb.append("<br>").append("id:").append(id);
								sb.append("<br>").append(a.getName()).append("<br>");
								if (bids != null && bids.length > 0) {
									for (long bid : bids) {
										cellIndex++;
										if (bid > 0) {
											ArticleEntity bae = ArticleEntityManager.getInstance().getEntity(bid);
											if (bae != null) {
												Article baoshi = ArticleManager.getInstance().getArticle(bae.getArticleName());
												if (baoshi != null && baoshi instanceof InlayArticle) {
													int[] values = ((InlayArticle) baoshi).getPropertysValues();
													if (values != null) {
														sb.append("第").append(cellIndex).append("孔:");
														for (int i = 0; i < values.length; i++) {
															int value = values[i];
															if (value != 0) {
																switch (i) {
																case 0:
																	sb.append("<br>").append(Translate.最大气血值).append("+").append(value);
																	break;
																case 1:
																	sb.append("<br>").append(Translate.外功修为).append("+").append(value);
																	break;
																case 2:
																	sb.append("<br>").append(Translate.内法修为).append("+").append(value);
																	break;
																case 3:
																	sb.append("<br>").append(Translate.外功防御).append("+").append(value);
																	break;
																case 4:
																	sb.append("<br>").append(Translate.内法防御).append("+").append(value);
																	break;
																case 5:
																	sb.append("<br>").append(Translate.闪躲).append("+").append(value);
																	break;
																case 6:
																	sb.append("<br>").append(Translate.免暴).append("+").append(value);
																	break;
																case 7:
																	sb.append("<br>").append(Translate.命中).append("+").append(value);
																	break;
																case 8:
																	sb.append("<br>").append(Translate.暴击).append("+").append(value);
																	break;
																case 9:
																	sb.append("<br>").append(Translate.精准).append("+").append(value);
																	break;
																case 10:
																	sb.append("<br>").append(Translate.破甲).append("+").append(value);
																	break;
																case 11:
																	sb.append("<br>").append(Translate.阳炎攻击).append("+").append(value);
																	break;
																case 23:
																	sb.append("<br>").append(Translate.阳炎攻击).append("+").append(value);
																	break;
																case 26:
																	sb.append("<br>").append(Translate.阳炎攻击).append("+").append(value);
																	break;
																case 12:
																	sb.append("<br>").append(Translate.阳炎抗性).append("+").append(value);
																	break;
																case 24:
																	sb.append("<br>").append(Translate.阳炎抗性).append("+").append(value);
																	break;
																case 27:
																	sb.append("<br>").append(Translate.阳炎抗性).append("+").append(value);
																	break;
																case 13:
																	sb.append("<br>").append(Translate.阳炎减抗).append("+").append(value);
																	break;
																case 25:
																	sb.append("<br>").append(Translate.阳炎减抗).append("+").append(value);
																	break;
																case 28:
																	sb.append("<br>").append(Translate.阳炎减抗).append("+").append(value);
																	break;
																case 14:
																	sb.append("<br>").append(Translate.玄冰攻击).append("+").append(value);
																	break;
																case 29:
																	sb.append("<br>").append(Translate.玄冰攻击).append("+").append(value);
																	break;
																case 32:
																	sb.append("<br>").append(Translate.玄冰攻击).append("+").append(value);
																	break;
																case 15:
																	sb.append("<br>").append(Translate.玄冰抗性).append("+").append(value);
																	break;
																case 30:
																	sb.append("<br>").append(Translate.玄冰抗性).append("+").append(value);
																	break;
																case 33:
																	sb.append("<br>").append(Translate.玄冰抗性).append("+").append(value);
																	break;
																case 16:
																	sb.append("<br>").append(Translate.玄冰减抗).append("+").append(value);
																	break;
																case 31:
																	sb.append("<br>").append(Translate.玄冰减抗).append("+").append(value);
																	break;
																case 34:
																	sb.append("<br>").append(Translate.玄冰减抗).append("+").append(value);
																	break;
																case 17:
																	sb.append("<br>").append(Translate.疾风攻击).append("+").append(value);
																	break;
																case 41:
																	sb.append("<br>").append(Translate.疾风攻击).append("+").append(value);
																	break;
																case 44:
																	sb.append("<br>").append(Translate.疾风攻击).append("+").append(value);
																	break;
																case 18:
																	sb.append("<br>").append(Translate.疾风抗性).append("+").append(value);
																	break;
																case 42:
																	sb.append("<br>").append(Translate.疾风抗性).append("+").append(value);
																	break;
																case 45:
																	sb.append("<br>").append(Translate.疾风抗性).append("+").append(value);
																	break;
																case 19:
																	sb.append("<br>").append(Translate.疾风减抗).append("+").append(value);
																	break;
																case 43:
																	sb.append("<br>").append(Translate.疾风减抗).append("+").append(value);
																	break;
																case 46:
																	sb.append("<br>").append(Translate.疾风减抗).append("+").append(value);
																	break;
																case 20:
																	sb.append("<br>").append(Translate.狂雷攻击).append("+").append(value);
																	break;
																case 35:
																	sb.append("<br>").append(Translate.狂雷攻击).append("+").append(value);
																	break;
																case 38:
																	sb.append("<br>").append(Translate.狂雷攻击).append("+").append(value);
																	break;
																case 21:
																	sb.append("<br>").append(Translate.狂雷抗性).append("+").append(value);
																	break;
																case 36:
																	sb.append("<br>").append(Translate.狂雷抗性).append("+").append(value);
																	break;
																case 39:
																	sb.append("<br>").append(Translate.狂雷抗性).append("+").append(value);
																	break;
																case 22:
																	sb.append("<br>").append(Translate.狂雷减抗).append("+").append(value);
																	break;
																case 37:
																	sb.append("<br>").append(Translate.狂雷减抗).append("+").append(value);
																	break;
																case 40:
																	sb.append("<br>").append(Translate.狂雷减抗).append("+").append(value);
																	break;
																}
															}
														}
														sb.append("<br>");
													}
												}
											}
										}
									}
								}

							}
						}
					}
				}

			}
			xiaZiData.put(key, sb.toString());
		}
	}

	public void queryPlayerTalentMess(Player player) {
		TalentData data = FlyTalentManager.getInstance().getTalentData(player.getId());
		int xyLevel = 1;

		long currExp = 0;
		int levelA = 0;
		int 仙悟等级 = 0;
		if (data != null) {
			levelA = data.getXylevelA();
			currExp = data.getExp();
			xyLevel = data.getXylevel();
			talentData.put("TalentLevel", xyLevel);
			talentData.put("TalentCDTimes", ((FlyTalentManager.TALENT_SKILL_CD_TIME - data.getMinusCDTimes()) / 60000) + "分钟");
			HashMap<Integer, Integer> points = data.getPoints();
			if (points != null && points.size() > 0) {
				StringBuffer sbuff = new StringBuffer();
				Iterator<Map.Entry<Integer, Integer>> it = points.entrySet().iterator();
				while (it.hasNext()) {
					Entry<Integer, Integer> entry = it.next();
					if (entry != null) {
						Integer id = entry.getKey();
						Integer point = entry.getValue();
						if (point != null && point > 0 && id != null) {
							if (id != null && id == FlyTalentManager.仙悟) {
								仙悟等级 = point;
							}

							TalentSkillInfo sinfo = FlyTalentManager.getInstance().skillInfos.get(id);
							if (sinfo == null) {
								continue;
							}
							double levelValues[] = sinfo.getLevelValues();
							if (levelValues != null) {
								try {
									switch (id) {
									case FlyTalentManager.仙疗: //吃药恢复效果增加10%/20%/30%
									case FlyTalentManager.领悟: //提升10/20/30级仙婴等级 （最多提升至300级）
									case FlyTalentManager.灵巧: //增加1%/4%/7%移动速度
									case FlyTalentManager.闪避: //增加1%/2%/3%闪躲
									case FlyTalentManager.体质: //增加50000/100000/150000气血上限
									case FlyTalentManager.幸运: //增加1%/2%/3%免爆
									case FlyTalentManager.神赐: //增加20000/40000/60000双防
									case FlyTalentManager.猛兽: //宠物每次攻击时可额外造成20000/40000/60000伤害
									case FlyTalentManager.怒火: //增加1%/2%/3%暴击
									case FlyTalentManager.神力: //增加20000/40000/60000双攻
									case FlyTalentManager.碎甲: //增加1%/2%/3%破甲
									case FlyTalentManager.专注: //增加1%/5%/10%控制类技能持续时间
									case FlyTalentManager.仙悟: //增加2%/4%/6%仙婴全属性
									case FlyTalentManager.暴燃: //所有技能造成伤害增加0.5%/1%/2%
									case FlyTalentManager.宝石神悟: //提高自身宝石收益1%/2%/3%
									case FlyTalentManager.羽翼神悟: //翅膀基础属性收益提高0.5%/1%/1.5%
										if (point > levelValues.length) {
											point = levelValues.length;
										}
										if (sinfo.getPropType() == 1) {
											sbuff.append(String.format(sinfo.getMess3(), levelValues[point - 1] + "%").replace(".0", "")).append("<br>");
										} else {
											sbuff.append(String.format(sinfo.getMess3(), levelValues[point - 1]).replace(".0", "")).append("<br>");
										}
										break;
									case FlyTalentManager.强化神元: //增加3%气血
									case FlyTalentManager.强化神赐: //增加3%双防
									case FlyTalentManager.仙婴体质: //每2秒回复3%的气血
									case FlyTalentManager.仙婴固化: //额外获得15%减伤效果
									case FlyTalentManager.强化猛兽: //增加3%宠物伤害
									case FlyTalentManager.强化神力: //增加3%双攻
									case FlyTalentManager.仙婴兽化: //宠物攻击时，有几率额外造成30%伤害
									case FlyTalentManager.仙婴悟攻: //人物进行攻击时，有几率额外造成20%伤害
										if (point > levelValues.length) {
											point = levelValues.length;
										}
										sbuff.append(String.format(sinfo.getMess3(), levelValues[point - 1] + "%").replace(".0", "")).append("<br>");
										break;
									case FlyTalentManager.会心: //降低0.5%/1%/1.5%双防，提高宠物1%/2%/3%伤害
									case FlyTalentManager.破斧: //降低0.5%/1%/1.5%气血，提高1%/2%/3%自身双攻
									case FlyTalentManager.温和: //降低0.5%/1%/1.5%双攻，提高1%/2%/3%气血上限
									case FlyTalentManager.坚固: //降低0.5%/1%/1.5%双攻，提高1%/2%/3%双防
										if (point == 3) {
											sbuff.append(String.format(sinfo.getMess3(), "1.5%", "3%")).append("<br>");
										} else if (point == 2) {
											sbuff.append(String.format(sinfo.getMess3(), "1%", "2%")).append("<br>");
										} else if (point == 1) {
											sbuff.append(String.format(sinfo.getMess3(), "0.5%", "1%")).append("<br>");
										}
										break;
									default:
										break;
									}
								} catch (Exception e) {
									continue;
								}
							}
						}
					}
				}
				if (sbuff.length() > 0) {
					String mess = sbuff.toString();
					if (mess.contains("</f>")) {
						mess = mess.replace("</f>", "");
					}
					if (mess.contains("<f color='#f7e354' size='16'>")) {
						mess = mess.replace("<f color='#f7e354' size='16'>", "");
					}
					if (mess.contains("<f size='16'>")) {
						mess = mess.replace("<f size='16'>", "");
					}
					talentData.put("TalentAdd", mess);
				}
			}

			TalentPageInfo info = null;
			for (TalentPageInfo pageInfo : FlyTalentManager.getInstance().infos) {
				if (xyLevel + levelA > 300) {
					if (pageInfo != null && (pageInfo.getLevel() == 300)) {
						info = pageInfo;
					}
				} else {
					if (pageInfo != null && (pageInfo.getLevel() == xyLevel + levelA)) {
						info = pageInfo;
					}
				}
			}

			StringBuffer talentAddMess = new StringBuffer();
			if (info != null) {
				int phyAttact = info.getPhyAttact();
				int phyDefence = info.getPhyDefence();
				int magicAttact = info.getMagicAttact();
				int magicDefence = info.getMagicDefence();
				int hp = info.getHp();
				if (仙悟等级 == 1) {
					phyAttact = (int) (info.getPhyAttact() * 1.02);
					phyDefence = (int) (info.getPhyDefence() * 1.02);
					magicAttact = (int) (info.getMagicAttact() * 1.02);
					magicDefence = (int) (info.getMagicDefence() * 1.02);
					hp = (int) (info.getHp() * 1.02);
				} else if (仙悟等级 == 2) {
					phyAttact = (int) (info.getPhyAttact() * 1.04);
					phyDefence = (int) (info.getPhyDefence() * 1.04);
					magicAttact = (int) (info.getMagicAttact() * 1.04);
					magicDefence = (int) (info.getMagicDefence() * 1.04);
					hp = (int) (info.getHp() * 1.04);
				} else if (仙悟等级 == 3) {
					phyAttact = (int) (info.getPhyAttact() * 1.06);
					phyDefence = (int) (info.getPhyDefence() * 1.06);
					magicAttact = (int) (info.getMagicAttact() * 1.06);
					magicDefence = (int) (info.getMagicDefence() * 1.06);
					hp = (int) (info.getHp() * 1.06);
				}
				talentAddMess.append("外功:").append(phyAttact).append("<br>");
				talentAddMess.append("内功:").append(magicAttact).append("<br>");
				talentAddMess.append("外防:").append(phyDefence).append("<br>");
				talentAddMess.append("内防:").append(magicDefence).append("<br>");
				talentAddMess.append("气血:").append(hp).append("<br>");
				talentData.put("TalentProp", talentAddMess.toString());
			}
		}

	}%>

<%!String[] careerNames = new String[] { "通用", "斗罗", "鬼煞", "灵尊", "巫皇", "兽魁" };

	public String getCareerName(int career) {
		return careerNames[career];
	}

	public String getShituRelation(Player p) {
		Relation relation = SocialManager.getInstance().getRelationById(p.getId());
		if (relation != null) {
			MasterPrentice mp = relation.getMasterPrentice();
			PlayerSimpleInfo info = null;
			if (mp != null) {
				long masterId = mp.getMasterId();
				if (masterId != -1) {
					if (masterId != -1) {
						try {
							info = PlayerSimpleInfoManager.getInstance().getInfoById(masterId);
							return "师傅 - " + masterId + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>";
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else {
					List<Long> prenticeIds = mp.getPrentices();
					StringBuffer sb = new StringBuffer();
					for (long id : prenticeIds) {
						try {
							info = PlayerSimpleInfoManager.getInstance().getInfoById(id);
							sb.append("徒弟 - " + id + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					return sb.toString();
				}
			}
		}
		return "";
	}

	public String getFriends(Player p) {
		Relation relation = SocialManager.getInstance().getRelationById(p.getId());
		if (relation != null) {
			List<Long> flist = relation.getFriendlist();
			StringBuffer sb = new StringBuffer();
			for (long id : flist) {
				try {
					PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(id);
					if (info == null) {
						continue;
					}
					sb.append("好友 - " + id + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}
		return "";
	}

	public void sendActionLogToGateway(HttpSession session, Player p, int actionType, long amount, String articleInfo, String petInfo, String otherInfo[], String reason) {
		GmAction ga = new GmAction();
		ga.setActionTime(System.currentTimeMillis());
		ga.setActionType(actionType);
		ga.setAmount(amount);
		ga.setArticleInfo(articleInfo);
		String sessionUser = (String) session.getAttribute("authorize.username");
		if (sessionUser == null) {
			sessionUser = "";
		}
		ga.setOperator(sessionUser);
		ga.setOtherInfos(otherInfo);
		ga.setPetInfo(petInfo);
		ga.setPlayerId(p.getId());
		ga.setPlayerName(p.getName());
		ga.setReason(reason);
		ga.setServerName(GameConstants.getInstance().getServerName());
		ga.setUserName(p.getUsername());
		GM_ACTION_REQ req = new GM_ACTION_REQ(GameMessageFactory.nextSequnceNum(), ga);
		MieshiGatewayClientService.getInstance().sendMessageToGateway(req);
		Game.logger.error("[进行GM操作] [] [" + sessionUser + "] [" + p.getLogString() + "] [action:" + actionType + "] [amount:" + amount + "] [article:" + articleInfo + "] [pet:" + petInfo + "] [reason:" + reason + "]");
	}%>

<%
	Map<String, Object> result = new HashMap<String, Object>();
	try {
		String userName = request.getParameter("userName");
		String playerName = URLDecoder.decode(request.getParameter("playerName"), "utf-8");
		Player p = null;
		//System.out.println("RoleInfo 获取角色信息:userName="+userName);
		//System.out.println("RoleInfo 获取角色信息:playerName="+playerName);

		if (userName != null && !userName.isEmpty() && playerName != null && !playerName.isEmpty()) {
			p = PlayerManager.getInstance().getPlayer(playerName, userName);
		}
		if (p == null) {
			System.out.println("RoleInfo 获取角色信息失败");
			out.print(JSONObject.fromObject(result).toString());
		}
		queryPlayerTalentMess(p);
		/**Map<String, Object> player = new HashMap<String, Object>();
		player.put("id", p.getId());
		player.put("isOnline", PlayerManager.getInstance().isOnline(p.getId()));
		player.put("name", p.getName());
		player.put("username", p.getUsername());
		player.put("vipLevel", p.getVipLevel());
		player.put("career", getCareerName(p.getCareer()));
		player.put("country", CountryManager.得到国家名(p.getCountry()));
		Cave cave = FaeryManager.getInstance().getCave(p);
		if (cave != null) {
			player.put("cave", cave.getFaery().getName());
		}
		player.put("lastGame", p.getLastGame());
		player.put("spouse", p.getSpouse());
		player.put("sex", p.getSex());
		player.put("isPlayerBaned", ChatMessageService.getInstance().isPlayerBaned(p.getId()));
		player.put("level", p.getLevel());
		player.put("zongPaiName", p.getZongPaiName());
		player.put("jiazuName",p.getJiazuName());
		player.put("silver", p.getSilver());
		player.put("bindSilver", p.getBindSilver());
		String ptName ="";//chenghao
		if(p.getPlayerTitles()!= null){
			List<PlayerTitle> playerTitles = p.getPlayerTitles();
			Iterator<PlayerTitle> it = playerTitles.iterator();
			while (it.hasNext()) {
				PlayerTitle pt = it.next();
				ptName +=pt.getTitleName();
				if(it.hasNext()){
					ptName += ",";
				}
			}
		}
		player.put("title", ptName);
		player.put("shituRelation", getShituRelation(p));
		player.put("friends", getFriends(p));
		result.put("player", player);
		 */
		Map<String, Object> player = new HashMap<String, Object>();
		player.put("id", p.getId());
		player.put("isOnline", PlayerManager.getInstance().isOnline(p.getId()));
		player.put("name", p.getName());
		player.put("username", p.getUsername());
		player.put("vipLevel", p.getVipLevel());
		player.put("career", getCareerName(p.getCareer()));
		player.put("country", CountryManager.得到国家名(p.getCountry()));
		Cave cave = FaeryManager.getInstance().getCave(p);
		if (cave != null) {
			player.put("cave", cave.getFaery().getName());
		}
		player.put("lastGame", p.getLastGame());
		player.put("spouse", p.getSpouse());
		player.put("sex", p.getSex());
		player.put("isPlayerBaned", ChatMessageService.getInstance().isPlayerBaned(p.getId()));
		player.put("level", p.getLevel());
		player.put("zongPaiName", p.getZongPaiName());
		player.put("jiazuName", p.getJiazuName());
		player.put("silver", p.getSilver());
		player.put("bindSilver", p.getBindSilver());
		player.put("ambit", p.getClassLevel());
		CountryManager cm = CountryManager.getInstance();
		String countryLevel = CountryManager.得到官职名(cm.官职(p.getCountry(), p.getId()));
		if (countryLevel == null) {
			countryLevel = "没有官职";
		}
		player.put("speed", p.getSpeed());

		player.put("office", countryLevel);
		player.put("chargePoints", p.getChargePoints());
		player.put("zongPaiName", p.getZongPaiName());
		player.put("jiaZuName", p.getJiazuId());
		JiazuMember jm = JiazuManager.getInstance().getJiazuMember(p.getId(), p.getJiazuId());
		long jiazugongx = 0;
		if (jm != null) {
			jiazugongx = jm.getContributeMoney();
		}
		player.put("jiaZu", jiazugongx);
		player.put("exp", p.getNextLevelExp());
		player.put("currentexp", p.getExp());
		player.put("lilian", p.getLilian());
		TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
		int dujieLv = 0;
		if (entity != null) {
			dujieLv = entity.getCurrentLevel();
		}
		player.put("dujie", dujieLv);
		player.put("gongxun", p.getGongxun());
		player.put("wencai", p.getCulture());
		player.put("yuanqi", p.getEnergy());
		player.put("eming", p.getEvil());
		player.put("xiulian", p.getClassLevel()); //什么鬼。  家族修炼的样子，
		player.put("qixue", p.getMaxHP());
		player.put("xianfa", p.getMaxMP());
		player.put("neigong", p.getMagicAttack());
		player.put("waigong", p.getPhyAttack());
		player.put("neifang", p.getMagicDefence() + "(" + (Integer) p.getSpellDecrease() / 10 + "%)");
		player.put("waifang", p.getPhyDefence() + "(" + (Integer) p.getPhysicalDecrease() / 10 + "%)");
		player.put("baoji", p.getCriticalHit() + "(" + (Integer) p.getSelfValue(97) / 10 + "%)");
		player.put("mingzhong", p.getHit() + "(" + (Integer) p.getSelfValue(47) / 10 + "%)");
		player.put("shanduo", p.getDodge() + "(" + (Integer) p.getSelfValue(50) / 10 + "%)");
		player.put("pojia", p.getBreakDefence() + "(" + (Integer) p.getSelfValue(44) / 10 + "%)");
		player.put("jingzhun", p.getAccurate() + "(" + (Integer) p.getSelfValue(53) / 10 + "%)");
		player.put("mianbao", p.getCriticalDefence() + "(" + (Integer) p.getSelfValue(96) / 10 + "%)");
		player.put("bing", "攻:" + p.getBlizzardAttack() + " 防:" + p.getBlizzardDefence() + "(" + (Integer) p.getBlizzardDefenceRate() / 10 + "%)" + " 减:" + p.getBlizzardIgnoreDefence() + "(" + (Integer) p.getBlizzardIgnoreDefenceRate() / 10 + "%)");
		player.put("lei", "攻:" + p.getThunderAttack() + " 防:" + p.getThunderDefence() + "(" + (Integer) p.getThunderDefenceRate() / 10 + "%)" + " 减:" + p.getThunderIgnoreDefence() + "(" + (Integer) p.getThunderIgnoreDefenceRate() / 10 + "%)");
		player.put("huo", "攻:" + p.getFireAttack() + " 防:" + p.getFireDefence() + "(" + (Integer) p.getFireDefenceRate() / 10 + "%)" + " 减:" + p.getFireIgnoreDefence() + "(" + (Integer) p.getFireIgnoreDefenceRate() / 10 + "%)");
		player.put("feng", "攻:" + p.getWindAttack() + " 防:" + p.getWindDefence() + "(" + (Integer) p.getWindDefenceRate() / 10 + "%)" + " 减:" + p.getWindIgnoreDefence() + "(" + (Integer) p.getWindDefenceRate() / 10 + "%)");
		long xianjingId = -1;
		int caveIndex = -1;
		if (cave != null && cave.getFaery() != null) {
			xianjingId = cave.getFaery().getId();
			caveIndex = cave.getIndex();
		}
		player.put("xianjingID", xianjingId);
		player.put("xianfu", caveIndex);
		String ptName = "";//chenghao
		if (p.getPlayerTitles() != null) {
			List<PlayerTitle> playerTitles = p.getPlayerTitles();
			Iterator<PlayerTitle> it = playerTitles.iterator();
			while (it.hasNext()) {
				PlayerTitle pt = it.next();
				ptName += pt.getTitleName();
				if (it.hasNext()) {
					ptName += ",";
				}
			}
		}
		player.put("title", ptName);
		player.put("shituRelation", getShituRelation(p));
		player.put("friends", getFriends(p));
		long now = System.currentTimeMillis();
		List<Object> bf = new ArrayList<Object>();
		if (p.getBuffs() != null && p.getBuffs().size() > 0) {
			Map<String, Object> tm = new LinkedHashMap<String, Object>();
			Buff[] bons = p.getBuffs().toArray(new Buff[p.getBuffs().size()]);
			for (int i = 0; i < bons.length; i++) {
				if (bons[i] != null) {
					tm.put("name", bons[i].getTemplateName() + "_" + bons[i].getLevel());
					tm.put("time", (bons[i].getInvalidTime() - now) / 1000);
					bf.add(tm);
				}
			}
		}
		player.put("buff", bf);
		List<Map<String, String>> taskMap = new ArrayList<Map<String, String>>();
		if (p.getAllTask() != null) {
			for (TaskEntity te : p.getAllTask()) {
				Task task = TaskManager.getInstance().getTask(te.getTaskId());
				Map<String, String> tm = new HashMap<String, String>();
				tm.put("taskTtype", te.getShowType() + "");
				tm.put("taskName", te.getTaskName());
				if (task != null) {
					tm.put("taskDesc", task.getDes());
				}
				if (te.getCompleted() != null && te.getCompleted().length > 0 && te.getTaskDemand() != null && te.getTaskDemand().length > 0) {
					tm.put("schedule", te.getCompleted()[0] + "/" + te.getTaskDemand()[0]);
				} else {
					tm.put("schedule", "0/0");
				}
				taskMap.add(tm);
			}
		}
		player.put("task", taskMap);
		result.put("player", player);
		//装备栏
		Map<String, Object> equipment_dangqian = new HashMap<String, Object>();
		Map<String, Object> equipment_yuanshen = new HashMap<String, Object>();
		EquipmentColumn currentEC = p.getEquipmentColumns();
		EquipmentColumn soulEC = null;
		Soul soul = p.getSoul(Soul.SOUL_TYPE_SOUL);
		if (soul != null) {
			soulEC = soul.getEc();
		}
		if (soulEC == null) {
			soulEC = new EquipmentColumn();
		}
		if (currentEC == null) {
			currentEC = new EquipmentColumn();
		}
		//当前武器
		String name = "空";
		String color = "";
		String id = "";
		EquipmentColumn ec = new EquipmentColumn();
		EquipmentEntity ee = null;
		NewMagicWeaponEntity me = null;
		ArticleEntity ae = null;
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
		}

		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("wuqi_name", name);
		equipment_dangqian.put("wuqi_id", id);
		handle_XiaZiData(ee, "wuqi_id", p);
		//元神武器
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("wuqi_name", name);
		equipment_yuanshen.put("wuqi_id", id);
		handle_XiaZiData(ee, "wuqi_yuansheng_id", p);
		//当前头盔
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
		}

		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("toukui_name", name);
		equipment_dangqian.put("toukui_id", id);
		handle_XiaZiData(ee, "toukui_id", p);
		//元神头盔
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("toukui_name", name);
		equipment_yuanshen.put("toukui_id", id);
		handle_XiaZiData(ee, "toukui_yuansheng_id", p);
		//当前护肩
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("hujian_name", name);
		equipment_dangqian.put("hujian_id", id);
		handle_XiaZiData(ee, "hujian_id", p);
		//元神护肩
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("hujian_name", name);
		equipment_yuanshen.put("hujian_id", id);
		handle_XiaZiData(ee, "hujian_yuanshen_id", p);
		//当前 胸
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("xiong_name", name);
		equipment_dangqian.put("xiong_id", id);
		handle_XiaZiData(ee, "xiong_id", p);
		//元神 胸
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("xiong_name", name);
		equipment_yuanshen.put("xiong_id", id);
		handle_XiaZiData(ee, "xiong_yuanshen_id", p);
		//当前 护腕
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
			handle_XiaZiData(ee, "huwan_id", p);
		}
		name = name + color;
		equipment_dangqian.put("huwan_name", name);
		equipment_dangqian.put("huwan_id", id);
		//元神 护腕
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("huwan_name", name);
		equipment_yuanshen.put("huwan_id", id);
		handle_XiaZiData(ee, "huwan_yuansheng_id", p);
		//当前腰带
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("yaodai_name", name);
		equipment_dangqian.put("yaodai_id", id);
		handle_XiaZiData(ee, "yaodai_id", p);
		//元神腰带
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("yaodai_name", name);
		equipment_yuanshen.put("yaodai_id", id);
		handle_XiaZiData(ee, "yaodai_yuanshen_id", p);
		//当前靴子
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("xuezi_name", name);
		equipment_dangqian.put("xuezi_id", id);
		handle_XiaZiData(ee, "xuezi_id", p);
		//元神靴子

		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("xuezi_name", name);
		equipment_yuanshen.put("xuezi_id", id);
		handle_XiaZiData(ee, "xuezi_yuanshen_id", p);
		//当前首饰
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("shoushi_name", name);
		equipment_dangqian.put("shoushi_id", id);
		handle_XiaZiData(ee, "shoushi_id", p);
		//元神首饰
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("shoushi_name", name);
		equipment_yuanshen.put("shoushi_id", id);
		handle_XiaZiData(ee, "shoushi_yuanshen_id", p);
		//当前项链
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("xianglian_name", name);
		equipment_dangqian.put("xianglian_id", id);
		handle_XiaZiData(ee, "xianglian_id", p);
		//元神项链
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("xianglian_name", name);
		equipment_yuanshen.put("xianglian_id", id);
		handle_XiaZiData(ee, "xianglian_yuanshen_id", p);
		//当前戒指
		name = "空";
		color = "";
		id = "";
		try {
			ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
			if (ae != null && ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;
			}
		} catch (Exception e) {
			ee = (EquipmentEntity) currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_dangqian.put("jiezhi_name", name);
		equipment_dangqian.put("jiezhi_id", id);
		handle_XiaZiData(ee, "jiezhi_id", p);
		//元神戒指
		name = "空";
		color = "";
		id = "";
		ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
		if (ae != null && ae instanceof EquipmentEntity) {
			ee = (EquipmentEntity) ae;
		}
		if (ee != null && ae != null) {
			id = String.valueOf(ee.getId());
			name = ee.getArticleName();
			Article a = ArticleManager.getInstance().getArticle(name);
			if (a != null) {
				color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
			}
		}
		name = name + color;
		equipment_yuanshen.put("jiezhi_name", name);
		equipment_yuanshen.put("jiezhi_id", id);
		handle_XiaZiData(ee, "jiezhi_yuanshen_id", p);
		//当前坐骑
		long horseId = p.getRidingHorseId();
		Horse rideHorse = null;
		if (horseId > 0) {
			rideHorse = HorseManager.getInstance().getHorseById(horseId, p);
			if (rideHorse != null) {
				equipment_dangqian.put("horseName", rideHorse.getHorseName());
			} else {
				equipment_dangqian.put("horseName", "无");
			}
		}

		//元神坐骑
		if (soul != null) {
			horseId = soul.getRidingHorseId();
			if (horseId > 0) {
				rideHorse = HorseManager.getInstance().getHorseById(horseId, p);
				if (rideHorse != null) {
					equipment_yuanshen.put("horseName", rideHorse.getHorseName());
				} else {
					equipment_yuanshen.put("horseName", "无");
				}
			}
		}
		result.put("equipment_yuanshen", equipment_yuanshen);
		result.put("equipment_dangqian", equipment_dangqian);

		//背包栏
		Knapsack knap = p.getKnapsack_common();
		Cell cells[] = null;
		if (knap != null) {
			cells = knap.getCells();
		} else {
			cells = new Cell[0];
		}
		List<Map<String, Object>> knapsack = new ArrayList<Map<String, Object>>();
		if (cells != null && cells.length > 0) {
			for (int i = 0; i < cells.length; i++) {
				name = "空";
				color = "";
				id = "";
				long eId = cells[i].getEntityId();
				int eNum = cells[i].getCount();
				ArticleEntity e = null;
				if (eId > 0 && eNum > 0) {
					e = ArticleEntityManager.getInstance().getEntity(eId);
					if (e != null) {
						id = String.valueOf(e.getId());
						name = e.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if (a != null) {
							color = "(" + ArticleManager.getColorString(a, e.getColorType()) + ")";
							name = name + color;
						}
					}
				}
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("eId", eId);
				temp.put("name", name);
				temp.put("count", eNum);
				knapsack.add(temp);
			}
		}
		result.put("knapsack", knapsack);

		//宠物栏
		List<Map<String, Object>> pets = new ArrayList<Map<String, Object>>();
		knap = p.getPetKnapsack();
		if (knap != null) {
			cells = knap.getCells();
		} else {
			cells = new Cell[0];
		}
		id = "";
		for (int i = 0; i < cells.length; i++) {
			long pEggId = cells[i].getEntityId();
			Pet pet = null;
			long petId = 0;
			if (pEggId > 0) {
				PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().getEntity(pEggId);
				if (ppe != null) {
					id = String.valueOf(ppe.getId());
					pet = PetManager.getInstance().getPet(ppe.getPetId());
					petId = ppe.getPetId();
				}
			}
			name = "空";
			if (pet != null) {
				name = pet.getName() + "(" + PetManager.得到宠物稀有度名(pet.getRarity()) + ")";
			}
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("id", id);
			temp.put("name", name);
			temp.put("petId", petId);
			pets.add(temp);
		}
		result.put("pets", pets);

		//防爆背包
		List<Map<String, Object>> fangbao = new ArrayList<Map<String, Object>>();
		knap = p.getKnapsack_fangbao();
		if (knap != null) {
			cells = knap.getCells();
		} else {
			cells = new Cell[0];
		}

		for (int i = 0; i < cells.length; i++) {
			name = "空";
			color = "";
			id = "";
			long eId = cells[i].getEntityId();
			int eNum = cells[i].getCount();
			ArticleEntity e = null;
			if (eId > 0 && eNum > 0) {
				e = ArticleEntityManager.getInstance().getEntity(eId);
				if (e != null) {
					id = String.valueOf(e.getId());
					name = e.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if (a != null) {
						color = "(" + ArticleManager.getColorString(a, e.getColorType()) + ")";
						name = name + color;
					}
				}
			}
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("name", name);
			temp.put("eId", eId);
			temp.put("count", eNum);
			fangbao.add(temp);
		}
		result.put("fangbao", fangbao);

		//仓库
		List<Map<String, Object>> cangku = new ArrayList<Map<String, Object>>();
		knap = p.getKnapsacks_cangku();
		if (knap != null) {
			cells = knap.getCells();
		} else {
			cells = new Cell[0];
		}
		cells = knap.getCells();
		for (int i = 0; i < cells.length; i++) {
			name = "空";
			color = "";
			id = "";
			long eId = cells[i].getEntityId();
			int eNum = cells[i].getCount();
			ArticleEntity e = null;
			if (eId > 0 && eNum > 0) {
				e = ArticleEntityManager.getInstance().getEntity(eId);
				if (e != null) {
					id = String.valueOf(e.getId());
					name = e.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if (a != null) {
						color = "(" + ArticleManager.getColorString(a, e.getColorType()) + ")";
						name = name + color;
					}
				}
			}
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("eId", eId);
			temp.put("name", name);
			temp.put("count", eNum);
			cangku.add(temp);

		}
		//2号仓库
		List<Map<String, Object>> twocangku = new ArrayList<Map<String, Object>>();
		knap = p.getKnapsacks_warehouse();
		if (knap != null) {
			cells = knap.getCells();
		} else {
			cells = new Cell[0];
		}
		cells = knap.getCells();
		for (int i = 0; i < cells.length; i++) {
			name = "空";
			color = "";
			id = "";
			long eId = cells[i].getEntityId();
			int eNum = cells[i].getCount();
			ArticleEntity e = null;
			if (eId > 0 && eNum > 0) {
				e = ArticleEntityManager.getInstance().getEntity(eId);
				if (e != null) {
					id = String.valueOf(e.getId());
					name = e.getArticleName();
					Article a = ArticleManager.getInstance().getArticle(name);
					if (a != null) {
						color = "(" + ArticleManager.getColorString(a, e.getColorType()) + ")";
						name = name + color;
					}
				}
			}
			Map<String, Object> temp = new HashMap<String, Object>();
			temp.put("eId", eId);
			temp.put("name", name);
			temp.put("count", eNum);
			twocangku.add(temp);

		}

		result.put("twocangku", twocangku);
		result.put("cangku", cangku);
		result.put("xiazi", xiaZiData);
		result.put("talent", talentData);
	} catch (Exception e) {
		GMServlet.log.error("", e);
		result.put("result", "fail");
		e.printStackTrace();
		Game.logger.warn("[GM平台查询玩家信息] [异常]", e);
	}
	//out.print(JSONObject.fromObject(result).toString());
	JSONObject json = JSONObject.fromObject(result);
	//out.print(json.toString());
	//out.flush();
	//out.close();

	ServletOutputStream out1 = null;
	try {
		response.setCharacterEncoding("utf-8");
		out1 = response.getOutputStream();
		if (out != null) {
			out1.write(json.toString().getBytes("utf-8"));
			out1.flush();
		}
	} catch (Exception e) {
		ArticleManager.logger.error(e.getMessage(), e);
	} finally {
		try {
			if (out != null) {
				out1.close();
			}
		} catch (IOException e) {
			ArticleManager.logger.error(e.getMessage(), e);
		}
	}
%>
