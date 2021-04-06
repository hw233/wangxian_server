<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.fy.engineserver.datasource.props.*,com.google.gson.*,java.util.*,com.fy.engineserver.sprite.*,
com.fy.engineserver.sprite.concrete.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.xuanzhi.boss.game.GameConstants"%><%@include file="IPManager.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"> 
<title>物品</title>
<%
//服务器类型为0代表为可修改的开发服务器
if(GameConstants.getInstance().getServerType() == 0){
long playerId = -1;
String action = request.getParameter("action");
String level = request.getParameter("level");
String drillCount = request.getParameter("drillCount");
String errorMessage = null;
PlayerManager pm = PlayerManager.getInstance();
Player player = null;
ArticleManager am = ArticleManager.getInstance();
ArticleEntityManager aem = ArticleEntityManager.getInstance();
PropsCategory pcs[] = am.getAllPropsCategory();
HashMap<String, Article> hm = am.getArticles();
if(action != null && action.equals("strongArticle") && level != null){

	String cellIndex = request.getParameter("cellIndex1");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		if(hm == null){
			errorMessage = "没有物品";
		}
		
		Knapsack knapsack = player.getKnapsack();
		EquipmentUpgradeMamager eum = EquipmentUpgradeMamager.getInstnace();
		ArticleEntity ae = knapsack.getArticleEntityByCell(new Integer(cellIndex).intValue());
		int strongCount = 0;
		if(ae instanceof EquipmentEntity){
			Equipment e = (Equipment)am.getArticle(ae.getArticleName());
			if(e.getMaxLevel() >= Integer.parseInt(level) && e.isUpgrandFlag()){
				((EquipmentEntity)ae).setLevel(Integer.parseInt(level));
				out.println("ok"+((EquipmentEntity)ae).getArticleName()+"强化到了"+Integer.parseInt(level));
				ArticleManager.logger.warn("[通过页面强化] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+e.getName()+"] [level:"+level+"]");
				return;
			}
		}else if(ae instanceof PropsEntity){
			
		
		}else if(ae instanceof ArticleEntity){
			
		
		}

	}
	return;
}
if(action != null && action.equals("strongArticle")){

	String cellIndex = request.getParameter("cellIndex1");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
		if(hm == null){
			errorMessage = "没有物品";
		}
		Article ee = am.getArticle("圣光之珠");
		Article eex = am.getArticle("幸运符");
		Knapsack knapsack = player.getKnapsack();
		EquipmentUpgradeMamager eum = EquipmentUpgradeMamager.getInstnace();
		ArticleEntity ae = knapsack.getArticleEntityByCell(new Integer(cellIndex).intValue());
		int strongCount = 0;
		if(ae instanceof EquipmentEntity){
			Equipment e = (Equipment)am.getArticle(ae.getArticleName());
			
			while(((EquipmentEntity)ae).getLevel() != 16 && player.getMoney() > 60000 && e.isUpgrandFlag()){
				int count = Integer.parseInt("4");
				ArticleEntity aeex = null;
				int index = knapsack.getArticleCellPos("幸运符");
				if(index == -1){
					aeex = aem.createEntity(eex,false,ArticleEntityManager.CREATE_REASON_OTHER,player);
					if(!knapsack.put(aeex)){
						errorMessage = "没有放入物品";
						throw new Exception();
					}
					index = knapsack.getArticleCellPos("幸运符");
				}else{
					aeex = knapsack.getArticleEntityByCell(index);
				}
			
				for(int i = 0; i < count; i++){
					ArticleEntity aee = null;
						aee = aem.createEntity(ee,false,ArticleEntityManager.CREATE_REASON_OTHER,player);
					if(!knapsack.put(aee)){
						errorMessage = "没有放入物品";
						throw new Exception("哥们背包满啦");
					}
				}
				eum.strengthen(player,e,(EquipmentEntity)ae,aeex.getId(),index);
				strongCount++;
			}
			
			out.println("升级次数"+strongCount+"\n");
			out.println("停止升级原因可能已经到16，可能金钱不够60000，可能装备不能升级。");
		}else if(ae instanceof PropsEntity){
			
		
		}else if(ae instanceof ArticleEntity){
			
		
		}

	}
}
if(action != null && action.equals("drill") && drillCount != null){
	String cellIndex = request.getParameter("cellIndex1");
	try{
		playerId = Long.parseLong(request.getParameter("playerid"));
		if(cellIndex == null || cellIndex.trim().length() == 0){
			errorMessage = "要使用的物品不能为空";
		}
	}catch(Exception e){
		errorMessage = "玩家ID必须是数字，不能含有非数字的字符";
	}
	if(errorMessage == null){
		player = pm.getPlayer(playerId);
		if(player == null){
			errorMessage = "ID为"+playerId+"的玩家不存在！";
		}
	}
	if(errorMessage == null){
	Knapsack sack = player.getKnapsack();
	ArticleEntity ae = sack.getArticleEntityByCell(new Integer(cellIndex).intValue());
	if(ae != null && ae instanceof EquipmentEntity){
		int index = sack.getArticleCellPos(ae.getArticleName());
		if(index == -1){
			EquipmentDrillManager.logger.warn("[装备打孔][失败][背包中没有"+ae.getArticleName()+"的物品][playerId:"+player.getId()+"][player:"+player.getName()+"]");
			return;
		}
		EquipmentEntity ee = (EquipmentEntity)ae;
		Equipment a = (Equipment)am.getArticle(ee.getArticleName());
		if(a != null && a.getAiguilleMax() >= Integer.parseInt(drillCount)){
			ArticleEntity[] aes = ee.getInlayEntites();
			int count = 0;
			if(aes == null){
				count = Integer.parseInt(drillCount);
			}else{
				count = Integer.parseInt(drillCount) - aes.length;
			}
			if(count <= 0){
				return;
			}
			if(aes == null){
				aes = new ArticleEntity[count];
				ee.setInlayEntites(aes);
			}else{
				ArticleEntity[] aesTemp = new ArticleEntity[aes.length + count];
				for(int i = 0; i < aes.length; i++){
					aesTemp[i] = aes[i];
				}
				ee.setInlayEntites(aesTemp);
			}
			ee.setDrillCount(a.getAiguilleMax());
			out.println("打了"+count+"个孔");
			ArticleManager.logger.warn("[通过页面打孔] ["+player.getUsername()+"] ["+player.getId()+"] ["+player.getName()+"] ["+a.getName()+"] [count:"+count+"]");
		}
	}
	}
}


if(errorMessage != null){
	out.println(errorMessage);
}
}else{
	out.println("服务器不能改数据");
}
%>
</head>
<body>
</body>
</html>
