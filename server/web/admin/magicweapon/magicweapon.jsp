<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="java.util.List"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.model.BasicAttrRanModel"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon"%>
<%@page import="com.fy.engineserver.carbon.devilSquare.DevilSquareManager"%>
<%@page import="com.fy.engineserver.util.server.TestServerConfigManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.model.HiddenAttrModel"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.model.BasicDataModel"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.model.AdditiveAttrModel"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponAttrModel"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant"%>
<%@page import="com.fy.engineserver.sprite.Soul"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity"%>
<%@page import="com.fy.engineserver.sprite.concrete.GamePlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponBaseModel"%>
<%@page import="com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<head>
</head>
<body>
	<%
		String action = request.getParameter("action");
		String playerId = request.getParameter("playerId");
		String magicweaponid = request.getParameter("magicweaponid");
		String startLevel = request.getParameter("startLevel");
		String aaName = request.getParameter("aaName");
		String aaTitle = request.getParameter("aaTitle");
		String password = request.getParameter("password");
		action = action == null ? "" : action;
		aaTitle = aaTitle == null ? "" : aaTitle;
		aaName = aaName == null ? "" : aaName;
		password = password == null ? "" : password;
		playerId = playerId == null ? "" :playerId;
		magicweaponid = magicweaponid == null ? "" :magicweaponid;
		startLevel = startLevel == null ? "" :startLevel;
	%>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="reloaddata" /> 
		<input type="submit" value="重新加载静态文件1" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="lookupdata" /> 
		玩家ID:<input type="text" name="playerId" value="<%=playerId%>" /> 
		物品id:<input type="text" name="magicweaponid" value="<%=magicweaponid%>" /> 
		<input type="submit" value="鉴定法宝" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="setmstar" /> 
		玩家ID:<input type="text" name="playerId" value="<%=playerId%>" /> 
		物品id:<input type="text" name="magicweaponid" value="<%=magicweaponid%>" /> 
		星:<input type="text" name="startLevel" value="<%=startLevel%>" /> 
		<input type="submit" value="设置法宝强化星级" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="getmagicweaponinplayer" /> 
		玩家ID:<input type="text" name="playerId" value="<%=playerId%>" /> 
		本尊0元神1:<input type="text" name="magicweaponid" value="<%=magicweaponid%>" />
		<input type="submit" value="查看玩家身上法宝" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="setmagicweaponlevel" /> 
		法宝id:<input type="text" name="magicweaponid" value="<%=magicweaponid%>" />
		等级:<input type="text" name="startLevel" value="<%=startLevel%>"  />
		<input type="submit" value="法宝等级设置" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="testhiddenattr" /> 
		任务id:<input type="text" name="magicweaponid" value="<%=magicweaponid%>" />
		等级:<input type="text" name="startLevel" value="<%=startLevel%>"  />
		<input type="submit" value="测试隐藏属性" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="setduration" /> 
		法宝id:<input type="text" name="magicweaponid" value="<%=magicweaponid%>" />
		耐久:<input type="text" name="startLevel" value="<%=startLevel%>"  />
		<input type="submit" value="设置法宝耐久度" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="createSpecialMaigcweapon" /> 
		角色名:<input type="text" name="playerId" value="<%=playerId%>" />
		法宝名:<input type="text" name="aaName" value="<%=aaName%>"  />
		前缀:<input type="text" name="aaTitle" value="<%=aaTitle%>"  />
		密码:<input type="password" name="password" value="<%=password%>"  />
		<input type="submit" value="创建特定法宝" />
	</form>
	<form action="magicweapon.jsp" method="post">
		<input type="hidden" name="action" value="sendAetoBag" /> 
		角色名:<input type="text" name="playerId" value="<%=playerId%>" />
		物品id:<input type="text" name="aaName" value="<%=aaName%>"  />
		<input type="submit" value="发送指定物品到背包" />
	</form>
	
	<%
	if("reloaddata".equals(action)) {
		MagicWeaponManager.instance.init();
		out.println("***********ok***************");
	} else if ("sendAetoBag".equals(action)) {
		if (!TestServerConfigManager.isTestServer()) {
			out.println("只有测试服能用！");
			return ;	
		}
		Player player = GamePlayerManager.getInstance().getPlayer(playerId);
		ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(Long.parseLong(aaName));
		if (ae != null) {
			boolean b = player.putToKnapsacks(ae, "后台测试！");
			out.println("发送结果:" + b );
		}
	} else if ("createSpecialMaigcweapon".equals(action)) {
		boolean b = TestServerConfigManager.isTestServer();
		if(!b && !"createMagicweapon".equals(password)) {
			out.println("不是测试服切密码不对，不允许修改");
			return;
		} 
		Player p = GamePlayerManager.getInstance().getPlayer(playerId);
		Article a = ArticleManager.getInstance().getArticle(aaName);
		String title = aaTitle;
		if (a instanceof MagicWeapon) {
			MagicWeapon article = (MagicWeapon)a;
			BasicAttrRanModel bm = MagicWeaponManager.instance.basicAttrMap.get(title);
			if (bm == null) {
				out.println("前缀名输入错误:" + title + "<br>");
				return ;
			}
			NewMagicWeaponEntity entity = (NewMagicWeaponEntity)ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_后台, 
					null, a.getColorType(), 1, true);
			MagicWeaponAttrModel[] basicAttrs = new MagicWeaponAttrModel[bm.getAttrList().size()];
			for(int i=0; i<basicAttrs.length; i++) {
				int attrType = bm.getAttrList().get(i);
				int[] resultNum = MagicWeaponManager.instance.getAttrNumByLevelAndType(p, article.getDataLevel(), MagicWeaponConstant.basiceAttr, attrType, entity.getColorType());
				basicAttrs[i] = new MagicWeaponAttrModel(attrType, resultNum[1], resultNum[0], "基础属性值");
			}
			entity.setBasicpropertyname(title);
			entity.setBasicpropertyname_stat(MagicWeaponManager.nameStats.get(title));
			entity.setBasiAttr(basicAttrs);
			//鉴定获得隐藏属性，有可能一个都不获得
			MagicWeaponBaseModel mm = MagicWeaponManager.instance.mwBaseModel.get(entity.getColorType());	
			if(mm == null) {
				throw new Exception("[鉴定法宝出错,没有获取到隐藏属性model] [" + p.getLogString() + "] [color:" + entity.getColorType() + "]");
			}
			if(mm.getHiddenAttrNum() > 0 && mm.getAppraisalNum() > 0) {			//判断是否有且可以在鉴定时获取到隐藏属性才做处理
				int hiddenNum = p.random.nextInt(mm.getAppraisalNum());
				if(hiddenNum > 0) {
					for(int i=0; i<hiddenNum; i++) {
						if(entity.getHideproterty().size() < mm.getHiddenAttrNum()) {		//隐藏属性上限
							MagicWeaponAttrModel mwa = MagicWeaponManager.instance.getHiddenAttr(p, article.getDataLevel(), entity.getColorType());
							List<MagicWeaponAttrModel> tempH = entity.getHideproterty();
							tempH.add(mwa);
							entity.setHideproterty(tempH);
						}
					}
				}
			}
			entity.initAttrNum();
			out.println("[创建成功] [物品Id:" + entity.getId() + "] <br>");
		} else {
			out.println("此页面只能创建法宝！！ 输入的物品名:" + aaName + "<br>");
		}
	} else if("setduration".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		NewMagicWeaponEntity me = (NewMagicWeaponEntity)DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(magicweaponid));
		int num = Integer.parseInt(startLevel);
		if(num < 0) {
			out.println("耐久度最低为0!");
			return ;
		}
		MagicWeapon mw = (MagicWeapon)ArticleManager.getInstance().getArticle(me.getArticleName());
		if(num > mw.getNaijiudu()) {
			out.println("[输入耐久度超过上限!] [上限值为:" + mw.getNaijiudu() + "]");
			return ;
		}
		me.setMdurability(num);
		out.println("[耐久度设置成功！！]");
	} else if("lookupdata".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		MagicWeaponManager inst = MagicWeaponManager.instance;
		Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		NewMagicWeaponEntity ee = (NewMagicWeaponEntity)DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(magicweaponid));
		String result = inst.appraisal(p, ee,false);
		out.println("***********" + result + "*****************'");
	} else if("setmstar".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		NewMagicWeaponEntity ee = (NewMagicWeaponEntity)DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(magicweaponid));
		ee.setMstar(Integer.parseInt(startLevel));
		ee.initAttrNum();
		out.println("*********************1111***********");
	} else if ("getmagicweaponinplayer".equals(action)) {
		Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(playerId));
		Soul soul = p.getSoul(Integer.parseInt(magicweaponid));
		long mgid = soul.getEc().getEquipmentIds()[11];
		if(mgid > 0) {
			NewMagicWeaponEntity mw = (NewMagicWeaponEntity)DefaultArticleEntityManager.getInstance().getEntity(mgid);
			out.println("name:"+mw.getArticleName()+"<br>");
			out.println("star:"+mw.getMstar()+"<br>");
		}else {
			out.println("**************木有装备法宝******************");
		}
	} else if("setmagicweaponlevel".equals(action)) {
		if(!TestServerConfigManager.isTestServer()) {
			out.println("不是测试服，不允许修改");
			return;
		}
		NewMagicWeaponEntity ee = (NewMagicWeaponEntity)DefaultArticleEntityManager.getInstance().getEntity(Long.parseLong(magicweaponid));
		if(ee == null || ee.getBasiAttr() == null ) {
			out.println("法宝不存在或者没神识！");
			return;
		}
		int oldL = ee.getMcolorlevel();
		ee.setMcolorlevel(Integer.parseInt(startLevel));
		for(int i=0; i<ee.getBasiAttr().length; i++) {
			if(ee.getBasiAttr()[i] != null) {
				float sss = MagicWeaponManager.instance.getInitAttrByAttrNum(ee.getBasiAttr()[i].getAttrNum(), ee.getColorType(), oldL);
				int sss1 = getInitAttrDevelNum(sss, ee.getColorType(), ee.getMcolorlevel());
				ee.getBasiAttr()[i].setAttrNum(sss1);
			} 
		}
		ee.saveData("basiAttr");
		ee.initAttrNum();
		out.println("2222222222222222");
	} else if("testhiddenattr".equals(action)) {
		Player p = GamePlayerManager.getInstance().getPlayer(Long.parseLong(magicweaponid));
		MagicWeaponAttrModel wm = MagicWeaponManager.instance.getHiddenAttr(p, 110, 4);
		out.println(wm + "<BR>");
		HiddenAttrModel hm = MagicWeaponManager.instance.hiddenDataMap.get(110);
		int[] aa = hm.getAttrNumByType(wm.getId());
		out.println("[1]===" + aa[0]);
		out.println("[2]===" + aa[1]);
		int rr = 0;
		if(MagicWeaponManager.instance.getAddTypeByType(MagicWeaponConstant.hiddenAttr, wm.getId()) == MagicWeaponConstant.add_typeNum) {
			rr = MagicWeaponManager.instance.getHiddenNum(p,aa[1],4);
		} else {
			rr = MagicWeaponManager.instance.getHiddenP(p,aa[1], 1);
		}
		out.println("****" + rr);
	}
	%>
	<%!
	private int getInitAttrDevelNum(float baseNum, int colorType, int level) throws Exception {
		if(colorType > MagicWeaponConstant.newColorWeight.length) {
			throw new Exception("[计算法宝数值错误] [配置颜色不正确:" + colorType + "]");
		}
		int result = 0;
		float ff = level;
		int tl = MagicWeaponManager.getLbyLevel(level);
		if(tl >= MagicWeaponConstant.newLevelWeight.length) {
			tl = MagicWeaponConstant.newLevelWeight.length - 1;
		}
		float numerator = baseNum * (1f + MagicWeaponConstant.newColorWeight[colorType] / 100 + MagicWeaponConstant.newLevelWeight[tl] / 100f + ff / 32f);
		result = (int) numerator;
		return result;
	}
	%>>
</body>
</html>
