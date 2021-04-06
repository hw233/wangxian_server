<%@page import="com.fy.engineserver.newtask.service.TaskManager"%>
<%@page import="com.fy.engineserver.newtask.Task"%>
<%@page import="com.fy.engineserver.newtask.TaskEntity"%>
<%@page import="com.fy.engineserver.datasource.buff.Buff"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager"%>
<%@page import="com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity"%>
<%@page import="com.fy.engineserver.jiazu.service.JiazuManager"%>
<%@page import="com.fy.engineserver.jiazu.JiazuMember"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.gm.service.RoleInfo"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.core.Game"%>
<%@page import="com.fy.engineserver.gateway.MieshiGatewayClientService"%>
<%@page import="com.fy.engineserver.message.GM_ACTION_REQ"%>
<%@page import="com.xuanzhi.boss.game.GameConstants"%>
<%@page import="com.fy.gamegateway.gmaction.GmAction"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager"%>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo"%>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrentice"%>
<%@page import="com.fy.engineserver.society.SocialManager"%>
<%@page import="com.fy.engineserver.society.Relation"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.net.*" %>
<%@page import="net.sf.json.JSONObject" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="com.fy.engineserver.chat.ChatMessageService" %>
<%@page import="com.fy.engineserver.core.Game" %>
<%@page import="com.fy.engineserver.country.manager.CountryManager" %>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article" %>
<%@page import="com.fy.engineserver.datasource.article.data.entity.*" %>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn" %>
<%@page import="com.fy.engineserver.datasource.article.data.props.*" %>
<%@page import="com.fy.engineserver.datasource.article.manager.*" %>
<%@page import="com.fy.engineserver.gateway.MieshiGatewayClientService" %>
<%@page import="com.fy.engineserver.homestead.cave.Cave" %>
<%@page import="com.fy.engineserver.homestead.cave.Cave" %>
<%@page import="com.fy.engineserver.homestead.faery.service.FaeryManager" %>
<%@page import="com.fy.engineserver.masterAndPrentice.MasterPrentice" %>
<%@page import="com.fy.engineserver.message.GM_ACTION_REQ" %>
<%@page import="com.fy.engineserver.message.GameMessageFactory" %>
<%@page import="com.fy.engineserver.playerTitles.PlayerTitle" %>
<%@page import="com.fy.engineserver.society.Relation" %>
<%@page import="com.fy.engineserver.society.SocialManager" %>
<%@page import="com.fy.engineserver.sprite.Player" %>
<%@page import="com.fy.engineserver.sprite.PlayerManager" %>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfo" %>
<%@page import="com.fy.engineserver.sprite.PlayerSimpleInfoManager" %>
<%@page import="com.fy.engineserver.sprite.Soul" %>
<%@page import="com.fy.engineserver.sprite.horse.Horse" %>
<%@page import="com.fy.engineserver.sprite.horse.HorseManager" %>
<%@page import="com.fy.engineserver.sprite.pet.Pet" %>
<%@page import="com.fy.engineserver.sprite.pet.PetManager" %>
<%@page import="com.xuanzhi.boss.game.GameConstants" %>
<%@page import="com.fy.gamegateway.gmaction.GmAction" %>
<%!
String[] careerNames = { "通用", "斗罗", "鬼煞", "灵尊", "巫皇" ,"兽魁"};
public String getCareerName(int career)
  {
    return careerNames[career];
  }
  public String getShituRelation(Player p) {
    Relation relation = SocialManager.getInstance().getRelationById(p.getId());
    MasterPrentice mp = relation.getMasterPrentice();
    PlayerSimpleInfo info = null;
    if (mp != null) {
      long masterId = mp.getMasterId();
      if (masterId != -1L) {
        if (masterId != -1L)
          try {
            info = PlayerSimpleInfoManager.getInstance().getInfoById(masterId);
            return "师傅 - " + masterId + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>";
          } catch (Exception e) {
            e.printStackTrace();
          }
      }
      else {
        List prenticeIds = mp.getPrentices();
        StringBuffer sb = new StringBuffer();
        for (Iterator localIterator = prenticeIds.iterator(); localIterator.hasNext(); ) { long id = ((Long)localIterator.next()).longValue();
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
    return "";
  }

  public String getFriends(Player p) {
    Relation relation = SocialManager.getInstance().getRelationById(p.getId());
    if (relation != null) {
      List flist = relation.getFriendlist();
      StringBuffer sb = new StringBuffer();
      for (Iterator localIterator = flist.iterator(); localIterator.hasNext(); ) { long id = ((Long)localIterator.next()).longValue();
        try {
          PlayerSimpleInfo info = PlayerSimpleInfoManager.getInstance().getInfoById(id);
          sb.append("好友 - " + id + " - " + info.getName() + " - " + getCareerName(info.getCareer()) + " - " + info.getLevel() + "<br>");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return sb.toString();
    }
    return "";
  }

  public void sendActionLogToGateway(HttpSession session, Player p, int actionType, long amount, String articleInfo, String petInfo, String[] otherInfo, String reason)
  {
    GmAction ga = new GmAction();
    ga.setActionTime(System.currentTimeMillis());
    ga.setActionType(actionType);
    ga.setAmount(amount);
    ga.setArticleInfo(articleInfo);
    String sessionUser = (String)session.getAttribute("authorize.username");
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
		try{
			System.out.println("RoleInfo 接收到请求"+RoleInfo.class);
			String userName = request.getParameter("userName");
			String playerName = request.getParameter("playerName");
			if(playerName != null && !playerName.isEmpty()){
				playerName = URLDecoder.decode(playerName,"utf-8");
			}
			String playerId = request.getParameter("playerId");
			Player p = null;
// 			log.info("获取角色信息,userName:" + userName + ",playerName:" +playerName);
			System.out.println("RoleInfo 获取角色信息:userName="+userName);
			System.out.println("RoleInfo 获取角色信息:playerName="+playerName);
			
			if(userName != null && !userName.isEmpty() && playerName!= null && !playerName.isEmpty()) {
				p = PlayerManager.getInstance().getPlayer(playerName, userName);
			}
			if(p == null && playerId != null && !playerId.isEmpty()){
				p = PlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
			}
			if(p == null) {
// 				log.info("获取角色信息失败");
out.print("玩家不存在");
				System.out.println("RoleInfo 获取角色信息失败");
				out.write(JSONObject.fromObject(result).toString());
				return;
			}
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
			player.put("jiazuName",p.getJiazuName());
			player.put("silver", p.getSilver());
			player.put("bindSilver", p.getBindSilver());
			player.put("ambit",p.getClassLevel());
			CountryManager cm = CountryManager.getInstance();
			String countryLevel = CountryManager.得到官职名(cm.官职(p.getCountry(), p.getId()));
			if (countryLevel == null) {
				countryLevel = "没有官职";
			}
			
			player.put("office",countryLevel);		
			player.put("chargePoints",p.getChargePoints());
			player.put("zongPaiName",p.getZongPaiName());
			player.put("jiaZuName",p.getJiazuId());
			JiazuMember jm = JiazuManager.getInstance().getJiazuMember(p.getId(), p.getJiazuId());
			long jiazugongx = 0;
			if (jm != null) {
				jiazugongx = jm.getContributeMoney();
			}
			player.put("jiaZu",jiazugongx);			
			player.put("exp",p.getNextLevelExp());
			player.put("currentexp",p.getExp());
			player.put("lilian",p.getLilian());
			TransitRobberyEntity entity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(p.getId());
			int dujieLv = 0;
			if (entity != null) {
				dujieLv = entity.getCurrentLevel();
			}
			player.put("dujie",dujieLv);			
			player.put("gongxun",p.getGongxun());
			player.put("wencai",p.getCulture());
			player.put("yuanqi",p.getEnergy());
			player.put("eming",p.getEvil());
			player.put("xiulian",p.getClassLevel());		//什么鬼。  家族修炼的样子，
			player.put("qixue",p.getMaxHP());
			player.put("xianfa",p.getMaxMP());
			player.put("neigong",p.getMagicAttack());
			player.put("waigong",p.getPhyAttack());
			player.put("neifang",p.getMagicDefence() + "("+(Integer)p.getSelfValue(39)/10+"%)");		
			player.put("waifang",p.getPhyDefence() + "("+(Integer)p.getSelfValue(35)/10+"%)");		
			player.put("baoji",p.getCriticalHit() + "("+(Integer)p.getSelfValue(97)/10+"%)");			
			player.put("mingzhong",p.getHit() + "("+(Integer)p.getSelfValue(47)/10+"%)");				
			player.put("shanduo",p.getDodge()  + "("+(Integer)p.getSelfValue(50)/10+"%)");			
			player.put("pojia",p.getBreakDefence() + "("+(Integer)p.getSelfValue(44)/10+"%)");			
			player.put("jingzhun",p.getAccurate()  + "("+(Integer)p.getSelfValue(53)/10+"%)");			
			player.put("mianbao",p.getCriticalDefence()  + "("+(Integer)p.getSelfValue(96)/10+"%)");			
			player.put("bing","攻:" + p.getBlizzardAttack() + " 防:" + p.getBlizzardDefence() + " 减:" + p.getBlizzardIgnoreDefence());
			player.put("lei","攻:" + p.getThunderAttack() + " 防:" + p.getThunderDefence() + " 减:" + p.getThunderIgnoreDefence());
			player.put("huo","攻:" + p.getFireAttack() + " 防:" + p.getFireDefence() + " 减:" + p.getFireIgnoreDefence());
			player.put("feng","攻:" + p.getWindAttack() + " 防:" + p.getWindDefence() + " 减:" + p.getWindIgnoreDefence());
			long xianjingId = -1;
			int caveIndex = -1;
			if (cave != null && cave.getFaery() != null) {
				xianjingId = cave.getFaery().getId();
				caveIndex = cave.getIndex();
			}
			player.put("xianjingID",xianjingId);			
			player.put("xianfu",caveIndex);				
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
			long now = System.currentTimeMillis();
			List<Object> bf = new ArrayList<Object>();
			if (p.getBuffs() != null && p.getBuffs().size() > 0) {
				Map<String,Object> tm = new LinkedHashMap<String,Object>();
				Buff[] bons = p.getBuffs().toArray(new Buff[p.getBuffs().size()]);
				for (int i=0; i<bons.length; i++) {
					if (bons[i] != null) {
						tm.put("name", bons[i].getTemplateName() + "_" + bons[i].getLevel());
						tm.put("time", (bons[i].getInvalidTime() - now)/1000);
						bf.add(tm);
					}
				}
			}
			player.put("buff", bf);			
			List<Map<String,String>> taskMap = new ArrayList<Map<String,String>>();
			if (p.getAllTask() != null) {
				for (TaskEntity te : p.getAllTask()) {
					Task task = TaskManager.getInstance().getTask(te.getTaskId());
					Map<String,String> tm = new HashMap<String,String>();
					tm.put("taskTtype", te.getShowType()+"");
					tm.put("taskName", te.getTaskName());
					tm.put("taskDesc", task.getDes());
					tm.put("schedule", te.getCompleted()[0] + "/" + te.getTaskDemand()[0]);
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
			if(soul != null) {
				soulEC = soul.getEc();
			}
			if(soulEC == null) {
				soulEC = new EquipmentColumn();
			}
			//当前武器
			String name = "空";
			String color = "";
			String id = "";
			EquipmentColumn ec = new EquipmentColumn();
			EquipmentEntity ee =null;
			NewMagicWeaponEntity me = null;
			ArticleEntity ae = null;
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
			}
			
			
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("wuqi_name", name);
			equipment_dangqian.put("wuqi_id", id);
			//元神武器
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("wuqi_name", name);
			equipment_yuanshen.put("wuqi_id", id);
			//当前头盔
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
			}
			
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("toukui_name", name);
			equipment_dangqian.put("toukui_id", id);
			//元神头盔
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_head);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("toukui_name", name);
			equipment_yuanshen.put("toukui_id", id);
			//当前护肩
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("hujian_name", name);
			equipment_dangqian.put("hujian_id", id);
			//元神护肩
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("hujian_name", name);
			equipment_yuanshen.put("hujian_id", id);
			
			//当前 胸
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("xiong_name", name);
			equipment_dangqian.put("xiong_id", id);
			//元神 胸
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_body);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("xiong_name", name);
			equipment_yuanshen.put("xiong_id", id);
			//当前 护腕
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("huwan_name", name);
			equipment_dangqian.put("huwan_id", id);
			//元神 护腕
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("huwan_name", name);
			equipment_yuanshen.put("huwan_id", id);
			//当前腰带
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("yaodai_name", name);
			equipment_dangqian.put("yaodai_id", id);
			//元神腰带
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("yaodai_name", name);
			equipment_yuanshen.put("yaodai_id", id);
			
			//当前靴子
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("xuezi_name", name);
			equipment_dangqian.put("xuezi_id", id);
			//元神靴子
			
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("xuezi_name", name);
			equipment_yuanshen.put("xuezi_id", id);
			//当前首饰
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("shoushi_name", name);
			equipment_dangqian.put("shoushi_id", id);
			//元神首饰
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("shoushi_name", name);
			equipment_yuanshen.put("shoushi_id", id);
			
			//当前项链
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("xianglian_name", name);
			equipment_dangqian.put("xianglian_id", id);
			//元神项链
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("xianglian_name", name);
			equipment_yuanshen.put("xianglian_id", id);
			//当前戒指
			name = "空";
			color = "";
			id = "";
			try{				
				ae = currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
				if (ae instanceof EquipmentEntity){
					ee = (EquipmentEntity) ae;				
				}
			}catch(Exception e){
				ee = (EquipmentEntity)currentEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_dangqian.put("jiezhi_name", name);
			equipment_dangqian.put("jiezhi_id", id);
			//元神戒指
			name = "空";
			color = "";
			id = "";
			ae = soulEC.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
			if (ae instanceof EquipmentEntity) {
				ee = (EquipmentEntity) ae;				
			}
			if(ae != null && ee != null) {
				id = String.valueOf(ee.getId());
				name = ee.getArticleName();
				Article a = ArticleManager.getInstance().getArticle(name);
				if(a != null) {
					color = "(" + ArticleManager.getColorString(a, ee.getColorType()) + ")";
				}
			}
			name = name + color;
			equipment_yuanshen.put("jiezhi_name", name);
			equipment_yuanshen.put("jiezhi_id", id);
			//当前坐骑
			long horseId = p.getRidingHorseId();
			Horse rideHorse = null;
			if(horseId > 0) {
				rideHorse = HorseManager.getInstance().getHorseById(horseId, p);
				if(rideHorse != null){
					equipment_dangqian.put("horseName", rideHorse.getHorseName());
				}else{
					equipment_dangqian.put("horseName", "无");
				}
			}
			
			//元神坐骑
			if(soul != null) {
				horseId = soul.getRidingHorseId();
				if(horseId > 0) {
					rideHorse = HorseManager.getInstance().getHorseById(horseId, p);
					if(rideHorse != null){
						equipment_yuanshen.put("horseName", rideHorse.getHorseName());
					}else{
						equipment_yuanshen.put("horseName", "无");
					}
				}
			}
			result.put("equipment_yuanshen", equipment_yuanshen);
			result.put("equipment_dangqian", equipment_dangqian);
			
			//背包栏
			Knapsack knap = p.getKnapsack_common();
			Cell cells[] = knap.getCells();
			List<Map<String,Object>> knapsack = new ArrayList<Map<String, Object>>();
			for(int i=0; i<cells.length; i++) {
				name = "空";
				color = "";
				id = "";
				long eId = cells[i].getEntityId();
				int eNum = cells[i].getCount();
				ArticleEntity e = null;
				if(eId > 0 && eNum > 0) {
					e = ArticleEntityManager.getInstance().getEntity(eId);
					if(e != null) {
						id = String.valueOf(e.getId());
						name = e.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a != null) {
							color = "("+ArticleManager.getColorString(a, e.getColorType())+")";
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
			result.put("knapsack", knapsack);
				
			//宠物栏
			List<Map<String, Object>> pets = new ArrayList<Map<String,Object>>();
			knap = p.getPetKnapsack();
			cells = knap.getCells();
			id = "";
			for(int i=0; i<cells.length; i++) {
				long pEggId = cells[i].getEntityId();
				Pet pet = null;
				long petId = 0;
				if(pEggId > 0) {
					PetPropsEntity ppe = (PetPropsEntity) ArticleEntityManager.getInstance().getEntity(pEggId);
					if(ppe != null) {
						id = String.valueOf(ppe.getId());
						pet = PetManager.getInstance().getPet(ppe.getPetId());
						petId = ppe.getPetId();
					}
				}
				name = "空";
				if(pet != null) {
					name = pet.getName() + "("+PetManager.得到宠物稀有度名(pet.getRarity())+")";
				}
				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("id", id);
				temp.put("name", name);
				temp.put("petId", petId);
				pets.add(temp);
			}
			result.put("pets", pets);
			
			//防爆背包
			List<Map<String, Object>> fangbao = new ArrayList<Map<String,Object>>();
			knap = p.getKnapsack_fangbao();
			if(knap != null) {
				cells = knap.getCells();
			} else {
				cells = new Cell[0];
			}
			
			for(int i=0; i<cells.length; i++) {
				name = "空";
				color = "";
				id = "";
				long eId = cells[i].getEntityId();
				int eNum = cells[i].getCount();
				ArticleEntity e = null;
				if(eId > 0 && eNum > 0) {
					e = ArticleEntityManager.getInstance().getEntity(eId);
					if(e != null) {
						id = String.valueOf(e.getId());
						name = e.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a != null) {
							color = "("+ArticleManager.getColorString(a, e.getColorType())+")";
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
			List<Map<String, Object>> cangku = new ArrayList<Map<String,Object>>();
			knap = p.getKnapsacks_cangku();
			cells = knap.getCells();
			for(int i=0; i<cells.length; i++) {
				name = "空";
				color = "";
				id = "";
				long eId = cells[i].getEntityId();
				int eNum = cells[i].getCount();
				ArticleEntity e = null;
				if(eId > 0 && eNum > 0) {
					e = ArticleEntityManager.getInstance().getEntity(eId);
					if(e != null) {
						id = String.valueOf(e.getId());
						name = e.getArticleName();
						Article a = ArticleManager.getInstance().getArticle(name);
						if(a != null) {
							color = "("+ArticleManager.getColorString(a, e.getColorType())+")";
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
			
			result.put("cangku", cangku);
		}catch(Exception e){
			e.printStackTrace();
			out.print(e);
		}
		
		out.write(JSONObject.fromObject(result).toString());


%>