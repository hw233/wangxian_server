<%@page import="com.fy.engineserver.activity.across.AcrossServerManager"%>
<%@page import="com.fy.engineserver.activity.ActivityShowInfo"%>
<%@page import="com.fy.engineserver.activity.loginActivity.ActivityManagers"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Cell"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.chat.ChatMessage"%>
<%@page import="com.fy.engineserver.chat.ChatMessageService"%>
<%@page import="com.fy.engineserver.achievement.AchievementManager"%>
<%@page import="com.fy.engineserver.achievement.RecordAction"%>
<%@page import="com.fy.engineserver.util.TimeTool"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.fy.engineserver.activity.xianling.XianLingManager"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.xuanzhi.tools.simplejpa.annotation.SimpleVersion"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.DefaultSimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.wing.WingManager"%>
<%@page import="com.fy.engineserver.wing.WingPanel"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkEnhanceManager"%>
<%@page import="com.fy.engineserver.articleEnchant.EnchantManager"%>
<%@page import="com.fy.engineserver.datasource.skill.master.SkBean"%>
<%@page import="com.fy.engineserver.marriage.MarriageInfo"%>
<%@page import="com.fy.engineserver.marriage.manager.MarriageManager"%>
<%@page import="com.fy.engineserver.sprite.pet.PetFlyState"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="org.apache.poi.poifs.filesystem.POIFSFileSystem"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@page import="com.fy.engineserver.util.config.ConfigServiceManager"%>
<%@page import="java.io.File"%>
<%@page import="com.fy.engineserver.menu.activity.exchange.DuJieActivity"%>
<%@page import="com.fy.engineserver.menu.activity.exchange.ExchangeActivityManager"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplate"%>
<%@page import="com.fy.engineserver.datasource.buff.BuffTemplateManager"%>
<%@page import="com.fy.engineserver.sprite.SpriteSubSystem"%>
<%@page import="com.xuanzhi.tools.text.WordFilter"%>
<%@page import="com.xuanzhi.tools.cache.diskcache.DiskCache"%>
<%@page import="com.fy.engineserver.uniteserver.UnitedServerManager"%>
<%@page import="com.fy.engineserver.unite.UniteManager"%>
<%@page import="com.fy.engineserver.unite.Unite"%>
<%@page import="com.fy.engineserver.homestead.cave.Cave"%>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.fy.engineserver.core.client.AavilableTaskInfo"%>
<%@page import="com.fy.engineserver.vip.data.VipConf"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.Jiazu"%>
<%@page import="com.fy.engineserver.sprite.npc.OreNPC"%>
<%@page import="com.fy.engineserver.sprite.npc.MemoryNPCManager"%>
<%@page import="com.fy.engineserver.sprite.npc.NPCManager"%>
<%@page import="com.fy.engineserver.core.GameManager"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.activity.village.data.OreNPCData"%>
<%@page import="com.fy.engineserver.activity.village.manager.VillageFightManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.fy.engineserver.core.res.Constants"%>
<%@page import="com.fy.engineserver.sprite.horse.Horse"%>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.core.res.ResourceManager"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.activity.wolf.config.TimeRangeConfig"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="com.fy.engineserver.tournament.data.OneTournamentData"%>
<%@page import="com.fy.engineserver.tournament.data.TournamentRankDataClient"%>
<%@page
	import="com.fy.engineserver.tournament.manager.TournamentManager"%>
<%@page
	import="com.fy.engineserver.playerAims.manager.PlayerAimManager"%>
<%@page import="com.fy.engineserver.playerAims.model.ChapterModel"%>
<%@page import="com.fy.engineserver.playerAims.model.PlayerAimModel"%>
<%@page
	import="com.fy.engineserver.playerAims.manager.PlayerAimeEntityManager"%>
<%@page import="com.fy.engineserver.playerAims.instance.PlayerAim"%>
<%@page
	import="com.fy.engineserver.playerAims.instance.PlayerAimsEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.fy.engineserver.util.CompoundReturn"%>
<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.TaskProps"%>
<%@page import="com.fy.engineserver.sprite.PropsUseRecord"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.props.Props"%>
<%@page
	import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page
	import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.smith.ArticleMaker"%>
<%@page
	import="com.fy.engineserver.activity.base.TimesActivityManager"%>
<%@page import="com.fy.engineserver.activity.ActivityManager"%>
<%@page import="com.fy.engineserver.vip.VipManager"%>
<%@page
	import="com.fy.engineserver.masterAndPrentice.MasterPrentice"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page
	import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page
	import="com.fy.engineserver.gateway.MieshiGatewayClientService"%>
<%@page
	import="com.fy.engineserver.message.NEW_MIESHI_UPDATE_PLAYER_INFO"%>
<%@page import="com.fy.engineserver.homestead.cave.PetHookInfo"%>
<%@page import="com.fy.engineserver.homestead.faery.Faery"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<%
	/*WindowManager wm=WindowManager.getInstance();
	 MenuWindow  mw = wm.getWindowById(51165);
	 Option[] ops=mw.getOptions();
	 for(Option o:ops){
	 out.print(o.getText()+"<br>");
	 }*/

	//补发宠物灵性
	/* Player player = GamePlayerManager.getInstance().getPlayer(1388000000000042344l);
	Pet pet = PetManager.getInstance().getPet(1347000000003657994l);
	if (pet != null) {
		PetFlyState state = PetManager.getInstance().getPetFlyState(pet.getId(), player);
		if (state != null) {
	out.print(state + "<br>");
	long xiaohuaDate = state.getXiaoHuaDate();
	state.setLingXingPoint(state.getLingXingPoint() + 10);
	state.setHistoryLingXingPoint(state.getLingXingPoint());
	state.setQianNengPoint(state.getQianNengPoint() + 50);
	PetManager.getInstance().savePetFlyState(state, pet.getId(), player);
	out.print(state + "<br>");
	AchievementManager.getInstance().record(player, RecordAction.使用易筋丹使宠物灵性达到50);
		}
	} */
	/*Player player = GamePlayerManager.getInstance().getPlayer(1112000000000228757l);
	 List<PlayerTitle> list = player.getPlayerTitles();
	 if(list != null){
	
	 if(list.size() <= 0){
	 out.print("没有称号");
	 }else{
	
	 for(PlayerTitle pt : list){
	 if(pt.getTitleType()==1){
	 pt.setTitleName("花开半夏*三天王");
	 player.setPlayerTitles(list);
	 }
	 out.print("name:"+pt.getTitleName() + "  showName:" +pt.getTitleShowName() + "  type:" +pt.getTitleType() + "  color:"+PlayerTitlesManager.getInstance().getColorByTitleType(pt.getTitleType()) +"<br/>");
	 }
	 }
	 }else{
	 out.print(" list null");
	 }*/
	/*FaeryManager faeryManager = FaeryManager.getInstance();
	for (int index = 1; index < 4; index++) {

		List<Faery> countyList = faeryManager.getCountryMap().get(index);
		if (countyList == null) {
	out.print("国家不存在<br>");
	return;
		}
		//String[] faeryNames = new String[countyList.size()];
		//String[] mapNames = new String[countyList.size()];
		List<String> faeryNames = new ArrayList<String>();
		List<String> mapNames = new ArrayList<String>();
		Player player = GamePlayerManager.getInstance().getPlayer(1394000000000044110l);
		List<Long> caveWherePetin = player.getHookPetCaveId();
		for (int i = 0; i < countyList.size(); i++) {
	Faery faery = countyList.get(i);
	boolean hascave = false;
	for (Cave cave : faery.getCaves()) {
		if (cave != null && cave.getId() > 0) {
			hascave = true;
		}
	}
	if (!hascave) {
		continue;
	}
	//faeryNames[i] = faery.getName();
	String faeryName = faery.getName();
	if (player.getFaeryId() == faery.getId()) {
		faeryName += Translate.自己;
	}
	if (faery.getCountry() == player.getCountry()) {// 只能在本国的仙府挂机
		for (int in = 0; in < faery.getCaveIds().length; in++) {
			if (caveWherePetin.contains(faery.getCaveIds()[in])) {// 有挂机的宠物在这幅地图.
				faeryName += Translate.宠物挂机中;
				break;
			}
		}
	}
	faeryNames.add(faeryName);
	mapNames.add(faery.getMemoryMapName());
		}
		out.print(faeryNames.toString() + "<br>");
		out.print(mapNames.toString() + "<br>");
	}*/

	/*Hashtable<Long, Long> khatamMap=FaeryManager.getInstance().getKhatamMap();
	for(long id:khatamMap.keySet()){
		out.print(id+","+khatamMap.get(id)+"<br>");
	}*/

	/*Relation relation = SocialManager.getInstance().getRelationById(1108000000000251022l);
	if (relation.getUniteId() != -1) {

		relation.setUniteId(-1);
		out.print(relation.getUniteId());
	} else {
		out.print("无结义");
	}*/

	/*Player p = PlayerManager.getInstance().getPlayer("茗牌男子");
	out.print(p.getTradeState());*/
	/*ZongPai zp = ZongPaiManager.getInstance().getZongPaiById(1504000000000000001l);
	if (zp != null) {
		ChatMessageService.getInstance().sendMessageToZongPai(zp, "后台测试1");//主窗口
		ChatMessageService.getInstance().sendMessageToZongPai5(zp, "后台测试2");//系统
		ChatMessage cmsg = new ChatMessage();
		// 设置为宗派频道
		cmsg.setMessageText("后台测试3");
		ChatMessageService.getInstance().sendMessageToZong(zp, cmsg);
		cmsg.setSort(ChatChannelType.ZONG);
		cmsg.setMessageText("后台测试4");
		ChatMessageService.getInstance().sendMessageToZong(zp, cmsg);

	}*/
	/*HunshiManager hm = HunshiManager.getInstance();
	hm.loadFile();
	out.print(hm.jianDingCost.size()+"<br>");
	out.print(hm.jianDingCost2.size()+"<br>");
	out.print(hm.openHole.size()+"<br>");
	out.print(hm.openHole2.size()+"<br>");
	out.print(hm.hunshiPropIdMap.size()+"<br>");
	out.print(hm.hunshiMainValueMap.size()+"<br>");
	out.print(hm.hunshi2MainValueMap.size()+"<br>");
	out.print(hm.hunshiExtraValueMap.size()+"<br>");
	out.print(hm.hunshi2ExtraValueMap.size()+"<br>");
	out.print(hm.hunshiSuitMap.size()+"<br>");*/
	/*Player player = PlayerManager.getInstance().getPlayer("一见你就笑");
	BaoShiXiaZiData xiazidata = ArticleManager.getInstance().getxiLianData(player, 1421000000106103157l);
	if (xiazidata != null) {
		long[] ids = xiazidata.getIds();
		int[] colors = xiazidata.getColors();
		String[] names = xiazidata.getNames();
		for (int k = 0; k < ids.length; k++) {
	if (ids[k] > 0) {
		out.print(ids[k]);
		ArticleEntity inlayEntity2 = ArticleEntityManager.getInstance().getEntity(ids[k]);
		if (inlayEntity2 == null) { //装备上镶嵌的宝石神匣无实体
			out.print("[神匣内发现不存在的物品] [aeId:" + ids[k] + "][数量:1]<br>");
			ids[k] = -1;
			names[k] = "";
		}
	}
		}
		xiazidata.setIds(ids);
		xiazidata.setNames(names);
	}*/
	/*Player player = PlayerManager.getInstance().getPlayer("☯*无忌");
	Player other = PlayerManager.getInstance().getPlayer("♌️*无唁");
	MarriageManager.getInstance();
	String title0 = other.getName() + Translate.text_marriage_105;
	String title1 = player.getName() + Translate.text_marriage_105;
	PlayerTitlesManager.getInstance().addSpecialTitle(player, Translate.text_marriage_结婚, title0, true);
	PlayerTitlesManager.getInstance().addSpecialTitle(other, Translate.text_marriage_结婚, title1, true);
	out.print("ok");*/
	//HunshiManager hm = HunshiManager.getInstance();
	/* Map<String, HunshiPropId> hunshiPropIdMap = hm.hunshiPropIdMap;
	 out.print(hm.jianDingCost.size() + "<br>");
	 out.print(hm.jianDingCost2.size() + "<br>");
	 out.print(hm.openHole.size() + "<br>");
	 out.print(hm.openHole2.size() + "<br>");
	 out.print(hm.hunshiPropIdMap.size() + "<br>");
	 out.print(hm.hunshiMainValueMap.size() + "<br>");
	 out.print(hm.hunshi2MainValueMap.size() + "<br>");
	 out.print(hm.hunshiExtraValueMap.size() + "<br>");
	 out.print(hm.hunshi2ExtraValueMap.size() + "<br>");
	 out.print(hm.tempMap.size() + "<br>");
	 out.print(hm.suitSkillMap.size() + "<br>");
	 out.print(hm.suitSkillGroupMap.size() + "<br>");
	 for (Integer id : hm.suitSkillMap.keySet()) {
	 int index = hm.suitSkillMap.get(id).getType();
	 SkillType st = hm.getSkillType(index);
	 out.print("SkillType:"+st + "<br>");
	 hm.suitSkillMap.get(id).setSkillType(st);
	 }
	 for (HunshiSuit suit : hm.tempMap.values()) {
	 SuitSkill ss = hm.suitSkillMap.get(suit.getSkillId());
	 out.print("ss.getSkillType:"+ss.getSkillType() + "<br>");
	 }
	 */
	/*	Player p = PlayerManager.getInstance().getPlayer("hw");
	 Horse h = HorseManager.getInstance().getHorseById(p.getRidingHorseId(), p);*/
	/*if (h != null) {

		out.print("魂石：" + Arrays.toString(h.getHunshiArray()) + "<br>");
		out.print("魂石：" + Arrays.toString(h.getHunshiOpen()) + "<br>");
		out.print("套装魂石：" + Arrays.toString(h.getHunshi2Array()) + "<br>");
		out.print("套装魂石：" + Arrays.toString(h.getHunshi2Open()) + "<br>");

	} else {
		out.print("horse null<br>");
	}*/
	/*ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(1504000000000521066l);
	if (ae instanceof HunshiEntity) {
		HunshiEntity he = (HunshiEntity) ae;
		HunshiJianDing material = hm.getJianDingMaterials(he);
		if(material!=null){
		out.print("material:" + Arrays.toString(material.getMaterialNames()) + "<br>");
		}else{
		out.print("material:" + material + "<br>");
	
		}

	}*/
	/*ParticleData[] particleDatas = new ParticleData[1];
	particleDatas[0] = new ParticleData(p.getId(), "坐骑魂石/免疫盾", 8*1000, 1, 1, 1);
	NOTICE_CLIENT_PLAY_PARTICLE_RES resp = new NOTICE_CLIENT_PLAY_PARTICLE_RES(GameMessageFactory.nextSequnceNum(), particleDatas);
	p.addMessageToRightBag(resp);*/
	/*Player target = PlayerManager.getInstance().getPlayer("mc");
	int damage = 1;
	List<Integer> hunshiSkills = h.getHunshiSkills();
	for (Integer skillId : hunshiSkills) {
		SuitSkill ss = hm.suitSkillMap.get(skillId);
		if (ss != null) {
	int index = ss.getSkillType().getIndex();
	boolean cdTimeEnd = hm.cdTimeEnd(p, ss); // 无cd=cd结束
	SuitSkillGroup sst = hm.suitSkillGroupMap.get(ss.getType());
	switch (index) {
	case 7:
		out.print("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [是否单体：" + true + "] [cdTimeEnd:" + cdTimeEnd + "] [damage:" + damage + "]<br>");
		if (true && cdTimeEnd) {
			float rate7 = (float) target.getHp() / target.getMaxHP();
			if (rate7 * 100 < ss.getLifeRate()) {
				Random random = new Random();
				if (random.nextInt(100) < ss.getActiveRate()) {
					p.suitSkillCDTime.put(sst.getSkillGroup(), System.currentTimeMillis() + sst.getSkillCD());
					// 远程职业伤害减半
					if (p.getCareer() == 3 || p.getCareer() == 4) {
						damage = ss.getAffectValue() / 2;
					} else {
						damage = ss.getAffectValue();
					}
				}
			}
		}
		out.print("[getDamage2] [" + p.getLogString() + "] [skillName:" + ss.getName() + "] [计算后damage:" + damage + "]<br>");
		break;
	default:
		break;
	}
		}
	}*/

	/*ArrayList<WeekAndMonthChongZhiActivity> weekMonthDatas = NewChongZhiActivityManager.weekMonthDatas;
	for (int i = 0; i < weekMonthDatas.size(); i++) {
		WeekAndMonthChongZhiActivity ac = weekMonthDatas.get(i);
		if(ac.getDataID()==532){
	for(Long id:ac.player_gets){
		out.print(id+"<br>");
	}
		}
	}*/

	/*Player player = PlayerManager.getInstance().getPlayer(1378000000000057601l);
	// 仙府.从数据库中查出来,修改.再flush
	if (player.getCaveId() > 0) {
		try {
	Cave cave = FaeryManager.caveEm.find(player.getCaveId());
	if (cave != null) {
		cave.setOwnerName(player.getName());
		FaeryManager.caveEm.flush(cave);
		out.print(player.getLogString() + " [修改仙府名字] [完毕] [caveId:" + player.getCaveId() + "]");
	}
	cave.onLoadInitNpc();

		} catch (Exception e) {
	UnitedServerManager.logger.error(player.getLogString() + " [修改仙府名字] [异常] [caveId:" + player.getCaveId() + "]", e);
		}
	} else {
		UnitedServerManager.logger.warn(player.getLogString() + " [修改仙府名字] [没有仙府] [caveId:" + player.getCaveId() + "]");
	}*/
	/*Player p = PlayerManager.getInstance().getPlayer(1102000000000005420l);
	PetFlyState pstate = PetManager.getInstance().getPetFlyState(1102000000049433862l, p);
	long 剩余消化时间 = pstate.getXiaoHuaDate() - System.currentTimeMillis();
	out.print(剩余消化时间);*/
	/*Player player = PlayerManager.getInstance().getPlayer("愛归来");
	Faery faery = FaeryManager.getInstance().getFaery(1234000000000000001l);
	Cave caves[] = faery.getCaves();

	for (Cave cave : caves) {
		if (cave != null && cave.getId() == 1234000000001169432l) {
			long pid[] = cave.getPethouse().getStorePets();
			for (int i = 0; i < pid.length; i++) {
				if (pid[i] == 1234000000111886712l) {
					pid[i] = 0l;
					cave.getPethouse().setStorePets(pid);
					cave.setPethouse(cave.getPethouse());
					out.print("ok");
				}
			}
		}
	}*/
	/*Player player = PlayerManager.getInstance().getPlayer(1543000000000106159l);
	NEW_MIESHI_UPDATE_PLAYER_INFO info = new NEW_MIESHI_UPDATE_PLAYER_INFO(GameMessageFactory.nextSequnceNum(), GameConstants.getInstance().getServerName(), "BAIDUSDKUSER_583682139", player.getId(), "挚爱芹妹妹", player.getSoulLevel(), player.getCurrSoul().getCareer(), (int) player.getRMB(), player.getVipLevel(), 1);
	MieshiGatewayClientService.getInstance().sendMessageToGateway(info);*/
	/*Player player = PlayerManager.getInstance().getPlayer(1697000000000021377l);
	Relation r = SocialManager.getInstance().getRelationById(player.getId());
	ArrayList<Long> blackList = r.getBlacklist();
	ArrayList<Long> removeList = new ArrayList<Long>();
	for (Long id : blackList) {
		if (id != 1558000000000003860l) {
			removeList.add(id);
		}
	}
	for (Long id : removeList) {
		blackList.remove(id);
	}
	for (Long id : blackList) {
		out.print(id+"<br>");
	}
	out.print("个数:"+blackList.size()+"<hr>");
	List<Long> blackList2 = r.getBlackNameListOffline();
	blackList2.clear();
	r.setBlackNameListOffline(blackList2);*/
	/* GamePlayerManager gpm = ((GamePlayerManager) GamePlayerManager.getInstance());
	Player p = gpm.em.find(1684000000000345728l);
	out.print(p);
	 if (gpm.getNamePlayerMap().contains("✨冷妞✨")) {
		Player p2 = gpm.getNamePlayerMap().get("✨冷妞✨");
		out.print("name：" + p2.getUsername());
	 gpm.getNamePlayerMap().remove("✨冷妞✨");
	}
	if (gpm.getIdPlayerMap().contains(p.getId())) {
		Player p2 = gpm.getIdPlayerMap().get(p.getId());
		out.print("id：" + p2.getUsername());
	} 
	 if (p != null) {
		p.setName("✨冷妞✨1");
		gpm.em.flush(p);
		gpm.idPlayerMap.put(p.getId(), p);
		out.print("name：" + p.getName());

	}*/
	//Relation r = SocialManager.getInstance().getRelationById(1563000000000020608l);
	/*try{
		Relation relation = SocialManager.getInstance().em.find(1563000000000020608l);
		out.print(relation.getBlacklist().size()+"<br>");
		out.print(relation.getFriendlist().size()+"<br>");
	}catch(Exception e){
		out.print(e+"<br>");
	}*/
	/*TournamentManager tm = TournamentManager.getInstance();
	int lastWeek = tm.得到一年中的第几个星期_周日并到上星期(Timestamp.valueOf("2016-10-24 00:00:00").getTime() - 7 * 1l * 24 * 3600 * 1000);
	try {
		long count = tm.emPlayer.count();
		long[] ids =  tm.emPlayer.queryIds(OneTournamentData.class, "lastWeek=?", new Object[] { lastWeek }, "lastWeekTournamentPoint desc", 1, count + 1);
		List<OneTournamentData> datas = tm.emPlayer.query(OneTournamentData.class, "lastWeek=?", new Object[] { lastWeek }, "lastWeekTournamentPoint desc", 1, 1001);
		out.print(ids.length+"<br>");
		out.print(datas.size()+"<br>");
		out.print("ids"+ids[0]+"--"+ids[1]+"--"+ids[2]+"<br>");
		out.print("datas"+datas.get(0).getPlayerId()+"--"+datas.get(1).getPlayerId()+"--"+datas.get(2).getPlayerId()+"<br>");
	} catch (Exception ex) {
		out.print("[得到切换周的竞技的人的id] [异常]"+ex);
	}*/
	/*Player p = PlayerManager.getInstance().getPlayer(1100000000007681026l);
	for(Buff b:p.getBuffs()){
		b.setInvalidTime(0);
	}
	Player p2 = PlayerManager.getInstance().getPlayer(1100000000009392129l);
	for(Buff b:p2.getBuffs()){
		b.setInvalidTime(0);
	}*/
	/*try{
		out.print(URLEncoder.encode("幻%影", "utf-8"));
	out.print(URLDecoder.decode(URLEncoder.encode("幻%影", "utf-8")));
	}catch(Exception e){
		out.print(e);
	}*/
	
	/**玩家无法切换飞升形象*/
	/*Player player = PlayerManager.getInstance().getPlayer("暗夜℡最嚣");
	player.setAvataRace(Constants.race_human_new);
	ResourceManager.getInstance().getAvata(player);
	try {
		Soul soul = player.getCurrSoul();
		for (long horseId : soul.getHorseArr()) {
			Horse horse = HorseManager.getInstance().getHorseById(horseId, player);
			if (horse != null && !horse.isFly()) {
				ResourceManager.getInstance().getHorseAvataForPlayer(horse, player);
				horse.selfMarks[31] = true;
				horse.notifySelfChange();
			}
		}
	} catch (Exception e) {
		out.print(e);
	}*/
	
	/**灵矿不显示家族名字*/
	/*VillageFightManager vm = VillageFightManager.getInstance();
	ArrayList<OreNPC> oreNPCList = vm.oreNPCList;
	for(int i = 0; i < oreNPCList.size(); i++){
		OreNPC on = oreNPCList.get(i);
		if(on.jiazuId == 1650000000000000027l){
			try {
				Jiazu jiazu = JiazuManager.getInstance().getJiazu(on.jiazuId);
				if (jiazu != null) {
					on.setTitle(Translate.translateString(Translate.某某家族的矿, new String[][] { { Translate.STRING_1, jiazu.getName() } }));
				}
			} catch (Exception ex) {
				out.print(ex);
			}
		}
		out.print(on.getTitle()+"<br>");
	}*/
	/**查询屠魔帖使用次数
	Player player = PlayerManager.getInstance().getPlayer("低调メ蓝天");
	out.print(VipManager.getInstance().vip每日增加的道具使用次数(player, Translate.屠魔帖)+"<br>");
	int vipAdd = VipManager.getInstance().vip每日增加的道具使用次数(player, Translate.屠魔帖);
	Article a = ArticleManager.getInstance().getArticle("屠魔帖●天罡");
	int exchangeAdd = ActivityManager.getInstance().getDayExchangeAddNum(player, a.get物品二级分类());
	int specialDateAdd = ActivityManager.getInstance().specialActivityAddNum(player);
	int activityAdd = TimesActivityManager.instacen.getAddNum(player, TimesActivityManager.TUMOTIE_ACTIVITY);
	Props ps = (Props) a;
	int totalNum = ps.getMaxUsingTimes() + vipAdd + exchangeAdd + specialDateAdd + activityAdd;
	out.print("ps.getMaxUsingTimes():"+ps.getMaxUsingTimes()+"<br>");
	out.print("vipAdd:"+vipAdd+"<br>");
	out.print("exchangeAdd:"+exchangeAdd+"<br>");
	out.print("specialDateAdd:"+specialDateAdd+"<br>");
	out.print("activityAdd:"+activityAdd+"<br>");
	out.print(totalNum+"<br>");
	
	int[] nums = ArticleManager.getInstance().getUseNumsByArticleName(player, "屠魔帖●天罡");
	int maxNum = VipConf.conf[player.getVipLevel()].vip使用屠魔贴的次数 + nums[0] + TimesActivityManager.instacen.getAddNum(player, TimesActivityManager.TUMOTIE_ACTIVITY);
	out.print("VipConf.conf[player.getVipLevel()].vip使用屠魔贴的次数:"+VipConf.conf[player.getVipLevel()].vip使用屠魔贴的次数+"<br>");
	out.print("nums[0]:"+nums[0]+"<br>");
	out.print("TimesActivityManager.instacen.getAddNum(player, TimesActivityManager.TUMOTIE_ACTIVITY):"+TimesActivityManager.instacen.getAddNum(player, TimesActivityManager.TUMOTIE_ACTIVITY)+"<br>");
	out.print(maxNum);*/
	/*Player player = PlayerManager.getInstance().getPlayer("孤影");
	Task t = TaskManager.getInstance().getTask("新的灾难");
		if (!player.getNextCanAcceptTasks().contains(t.getId())) {
			CompoundReturn cr = player.takeOneTask(t, false, null);
			out.print("t.getName():"+t.getName()+",t.getGrade():"+t.getGrade()+",cr.getIntValue():"+cr.getIntValue()+"<br>");
			List<AavilableTaskInfo> addList = new ArrayList<AavilableTaskInfo>();// 本次增加的任务
				player.getNextCanAcceptTasks().add(t.getId());
				addList.add(new AavilableTaskInfo(t));
				player.setDirty(true, "nextCanAcceptTasks");
			// 发送给客户端变化
						if (addList.size() > 0) {
							player.noticeClientCanAcceptModify((byte) 0, addList);
							if (TaskManager.logger.isDebugEnabled()) {
								TaskManager.logger.debug(player.getLogString() + "[checknext] [当前可接任务列表] [{}]", new Object[] { Arrays.toString(player.getNextCanAcceptTasks().toArray(new Long[0])) });
							}
						}
		}*/
		/*MagicWeaponConstant.LASTWARNTIME=1;
		out.print(MagicWeaponConstant.LASTWARNTIME);*/
		  /* Relation relation = SocialManager.getInstance().getRelationById(1489000000000002175l);
			out.print(relation.getMarriageId()+"<br>");
			relation.setMarriageId(-1);
			out.print(relation.getMarriageId()+"<br>");
			MarriageInfo infoto = MarriageManager.getInstance().getMarriageInfoById(1489000000000002175l);
			out.print(infoto);  */
		/* if(relation.getUniteId() != -1){
			Unite u = UniteManager.getInstance().getUnite(relation.getUniteId());
			if(u != null){
				for(long id :u.getMemberIds()){
					out.print(id+"<br>");
				}
			}else{
				out.print("没有结义");
			}
		}else{
			out.print(relation.getUniteId());
		}  */
		//从合服改名目录中删除某玩家
		/* UnitedServerManager um = UnitedServerManager.getInstance();
		DiskCache sameNamePlayerCache = um.getSameNamePlayerCache();
		String playerName = (String) sameNamePlayerCache.get(1689000000000026844l);
		out.print(playerName+"<br>");
		Player[] ps = PlayerManager.getInstance().getPlayerByUser("✨_-点点");
		out.print(ps.length+"<br>");
		WordFilter filter = WordFilter.getInstance();
		String name = "✨_-点点";
		SpriteSubSystem ss = SpriteSubSystem.getInstance();
		boolean valid = filter.cvalid(name, 0) && filter.evalid(name, 1) && ss.prefixValid(name) && ss.tagValid(name) && ss.gmValid(name);
		out.print(valid+"<br>"); */
		/* Player p = PlayerManager.getInstance().getPlayer(1100000000009467905l);
		BuffTemplateManager btm = BuffTemplateManager.getInstance();
		BuffTemplate bt = btm.getBuffTemplateByName("减速");
		Buff buff = bt.createBuff(0);
		buff.setStartTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis());
		buff.setInvalidTime(com.fy.engineserver.gametime.SystemTime.currentTimeMillis()+10000);
		
		p.placeBuff(buff);
		out.print("[fireBuff] [成功]"+buff.getTemplateName()); */
		
		/* 腾讯刷渡劫奖励活动id
		ExchangeActivityManager eam = ExchangeActivityManager.getInstance();
		File file = new File(eam.getFilePath());
		file = new File(ConfigServiceManager.getInstance().getFilePath(file));
		HSSFWorkbook hssfWorkbook = null;
		InputStream is = new FileInputStream(file);
		POIFSFileSystem pss = new POIFSFileSystem(is);
		hssfWorkbook = new HSSFWorkbook(pss);
		HSSFSheet sheet = hssfWorkbook.getSheetAt(3);
		eam.dujieActivitys.clear();
		eam.loadDuJieConfig(sheet);
		out.print("ok"); */
		//玩家宠物易筋丹到时间未正常消化
		/*Player player = PlayerManager.getInstance().getPlayer(1443000000000003039l);
		PetFlyState pstate = PetManager.getInstance().getPetFlyState(1443000000002653865l, player);
		out.print(pstate.getXiaoHuaDate()+"<br>");
		Pet pet = PetManager.getInstance().getPet(1443000000002653865l);
		PetFlyState state = PetManager.getInstance().getPetFlyState(pet.getId(), player);
		if(state != null){
			int getQianNengValue = 0;
			if(state.getXiaoHuaDate() > 0 && state.getXiaoHuaDate() <= System.currentTimeMillis()){
				state.setLingXingPoint(state.getLingXingPoint()+10);
//				if(state.getHistoryLingXingPoint() < state.getLingXingPoint()){
					state.setHistoryLingXingPoint(state.getLingXingPoint());
					state.setQianNengPoint(state.getQianNengPoint()+50);
					getQianNengValue = 50;
//				}
				state.setXiaoHuaDate(0);
				PetManager.getInstance().savePetFlyState(state, pet.getId(), player);
				if(getQianNengValue > 0){
					player.sendError(Translate.translateString(Translate.消耗易筋丹成功, new String[][] { { Translate.COUNT_1, String.valueOf(10) }, { Translate.COUNT_2, String.valueOf(50) } }));
				}else{
					player.sendError(Translate.translateString(Translate.消耗易筋丹成功2, new String[][] { { Translate.COUNT_1, String.valueOf(10) } }));
				}
				PetManager.logger.warn("[玩家宠物道具易筋丹消化完毕] [{}] [{}]",new Object[]{state,player.getLogString()});
			}
		}
		out.print(pstate.toString()+"<br>"); */
		/* Player p = PlayerManager.getInstance().getPlayer("Cute❥昊昊");
		SkBean bean = SkEnhanceManager.getInst().findSkBean(p);
		if (bean == null) {
			out.print(null+"<br>");
		}else{
			bean.setPoint(80);
			out.print("ok<br>");
		} */
		/*玩家收集的翅膀丢失，wingpalel导旧数据复制到新数据
		Player p = PlayerManager.getInstance().getPlayer("☤、纱裙少女");
		WingPanel wp = WingManager.getInstance().getWingPanle(p);
		String emfPath1 = request.getRealPath("/") + "WEB-INF/spring_config/";
		DefaultSimpleEntityManagerFactory factory = new DefaultSimpleEntityManagerFactory(emfPath1 + "simpleEMF_hefu.xml");
		SimpleEntityManager<WingPanel> wingManager = factory.getSimpleEntityManager(WingPanel.class);
		Field versionField = UnitedServerManager.getFieldByAnnotation(ArticleEntity.class, SimpleVersion.class);
		versionField.setAccessible(true);
		WingPanel wp2 = wingManager.find(1669000000000008055l);
		if(wp2==null){
			out.print("原数据库无此数据！");
			return;
		}
		out.print(wp2.getWingMap()+"<BR>");
		wp.setWingMap(wp2.getWingMap());
		out.print(wp2.getWingId()+"<BR>");
		wp.setWingId(wp2.getWingId());
		out.print(wp2.getStar()+"<BR>");
		wp.setStar(wp2.getStar());
		out.print(wp2.getBrightInlayId()+"<BR>");
		wp.setBrightInlayId(wp2.getBrightInlayId());
		out.print(wp2.getOnceMaxStar()+"<BR>");
		wp.setOnceMaxStar(wp2.getOnceMaxStar());
		out.print(Arrays.toString(wp2.getInlayArticleIds())+"<BR>");
		wp.setInlayArticleIds(wp2.getInlayArticleIds());
		out.print("ok"); */
		/* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long now = sdf.parse("2017-02-20 00:00:29").getTime();
		long last = sdf.parse("2017-02-19 00:03:12").getTime();
		boolean withLastSameDay = TimeTool.instance.isSame(now, last, TimeTool.TimeDistance.DAY, 1);
		out.print(withLastSameDay); */
		/* Player p = ((GamePlayerManager) GamePlayerManager.getInstance()).em.find(1220000000000328515l);
		out.print(p.getCurrentExchangeSilkwormTimes());
		int num = 1;
		p.setCurrentExchangeSilkwormTimes(num);
		out.print(p.getCurrentExchangeSilkwormTimes());
		if (JiazuManager.logger.isWarnEnabled()) {
			JiazuManager.logger.warn(p.getJiazuLogString() + "后台设置已兑换次数为："+num);
		} */
		/* String des  = "ceshiyixia";
		try{
			ChatMessageService cms = ChatMessageService.getInstance();
			ChatMessage msg = new ChatMessage();
			msg.setMessageText("<f color='0x14ff00'>"+des+"</f>");
			cms.sendRoolMessageToSystem(msg);
		}catch(Exception e){
			out.print(e);
		} */
		/* Player player = PlayerManager.getInstance().getPlayer(1156000000000307235l);
		Knapsack knapsack = player.getKnapsacks_warehouse();
		if(knapsack != null && knapsack.getCells() != null){
			int cellCount = knapsack.getCells().length;
			for(int i = 0; i < cellCount; i++){
				Cell cell = knapsack.getCell(i);
				if(cell != null && cell.getEntityId() == 1253000000254735437l){
					cell.setEntityId(-1);
					player.setKnapsacks_warehouse(player.getKnapsacks_warehouse());
				}
			}
		} */
		//修改玩家已喝酒次数，因为玩家跨零点喝的次数记到昨天导致当天无法喝酒了
		/* Player player = PlayerManager.getInstance().getPlayer("看迩笑陪迩鬧");
		Map<String, PropsUseRecord> map = player.getPropsUseRecordMap();
		PropsUseRecord pr = map.get("酒");
		Class clazz = Class.forName("com.fy.engineserver.sprite.PropsUseRecord");
		Field field = clazz.getDeclaredField("useNum"); 
		field.setAccessible(true);//设置允许访问 
		field.setByte(pr, (byte)1);
		out.print(pr.getUseNum()+"<br>");
		out.print(pr.getPropsName()+"<br>");
		out.print(pr.getLastUseTime()+"<br>");
		player.setPropsUseRecordMap(map);
		player.setDirty(true, "propsUseRecordMap"); */
		/*  List<ActivityShowInfo> infos = ActivityManagers.getInstance().infos;
		for(ActivityShowInfo info:infos){
			if(info.activityId == 1815){
				info.activityContent = "<f color='0xff0000'>【活动时间】</f>\n<f color='0x00ff00'>02月24日00:00——03月02日23:59</f>\n<f color='0xff0000'>【活动描述】</f>\n活动期间，渡过相应天劫的玩家，可以在飘渺王城渡劫使者处领取对应天劫回馈礼包一个！海量酒、帖各类稀有道具全部免费送！助您快速飞升仙界！（活动期间奖励只可领取一次哦）>";
			}
		} */
%>
<body>
	<form action="">
		<input type="text" name="playerId"> <input type="submit"
			value="提交">
	</form>
</body>
</html>