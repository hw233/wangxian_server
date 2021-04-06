<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.fy.engineserver.datasource.article.data.props.Knapsack"%>
<%@page import="com.fy.engineserver.datasource.language.Translate"%>
<%@page import="com.fy.engineserver.sprite.pet.PetManager"%>
<%@page import="com.xuanzhi.tools.transport.ResponseMessage"%>
<%@page import="com.xuanzhi.tools.transport.RequestMessage"%>
<%@page import="com.xuanzhi.tools.transport.ConnectionException"%>
<%@page import="com.xuanzhi.tools.transport.Connection"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="com.fy.engineserver.gateway.SubSystemAdapter"%>
<%@page import="java.util.ConcurrentModificationException"%>
<%@page import="com.fy.engineserver.gateway.GameSubSystem"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fy.engineserver.gateway.GameNetworkFramework"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="java.lang.reflect.Field"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetSkillProp"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkillManager"%>
<%@page import="com.fy.engineserver.newBillboard.date.pet.PetRankData"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetEggProps"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.props.PetProps"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.fy.engineserver.newBillboard.date.pet.PetALLBillboard"%>
<%@page import="com.fy.engineserver.datasource.skill2.GenericSkill"%>
<%@page import="com.fy.engineserver.sprite.PlayerManager"%>
<%@page import="com.fy.engineserver.sprite.Player"%>
<%@page import="com.fy.engineserver.message.GameMessageFactory"%>
<%@page import="com.fy.engineserver.message.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%@page import="com.fy.engineserver.sprite.pet2.*"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManagerFactory"%>
<%@page import="com.fy.engineserver.sprite.pet.Pet"%>
<%@page import="java.util.List"%>
<%@page import="com.xuanzhi.tools.page.PageUtil"%><html>
<head>
</head>
<body>
<%!
class TakeSkill2 extends SubSystemAdapter {
	Logger log = Pet2Manager.log;
	public String[] getInterestedMessageTypes(){
		return new String[]{"PET2_LIAN_DAN_REQ"};
	}
	
	@Override
	public ResponseMessage handleReqeustMessage(Connection conn,
			RequestMessage message) throws ConnectionException, Exception {
		Player p = check(conn, message, log);
		log.debug("{} req {}", p.getName(), message.getTypeDescription());
		if(message instanceof PET2_LIAN_DAN_REQ){
			return lianDan(p, (PET2_LIAN_DAN_REQ)message);
		}
		return null;
	}
	
	public ResponseMessage lianDan(Player p, PET2_LIAN_DAN_REQ message) {
		long petId = message.getPetId();
		if(petId == p.getActivePetId()){
			p.sendError(Translate.pet_fight);
			return null;
		}
		Pet pet = PetManager.getInstance().getPet(petId);
		if(pet == null){
			log.error("lian dan pet not find {}", petId);
			return null;
		}
		if(pet.isDelete()){
			log.error("宠物已删除", petId);
			return null;
		}
		if(pet.getRarity()<PetManager.千载难逢){
			p.sendError(getConfStr("xiYouDuTaiDi"));
			return null;
		}
		int takeLv = pet.getTrainLevel();
		LianHunConf conf = PetGrade.lianHunMap.get(takeLv);
		if(conf == null){
			log.error("lianDan conf not find for take lv {}", takeLv);
		}
		Knapsack bag = p.getKnapsack_common();
		int gridLeft = bag.getEmptyNum();
		if(gridLeft<1){
			String msg = getConfStr("bagFull");
			p.sendError(msg);
			return null;
		}
		String artName = conf.articleName;
		int reason = ArticleEntityManager.PET2_PET_TO_DanYao;
		Article art = ArticleManager.getInstance().getArticle(artName);
		if(art == null){
			log.error("lianDan article not find {}", artName);
			return null;
		}
		int color = art.getColorType();
		ArticleEntity ae = null;
		try {
			ae = ArticleEntityManager.getInstance().createEntity(art, false, reason, p, color, 1, true);
		} catch (Exception e) {
			log.error("lianDan create entity fail", e);
			return null;
		}
		ArticleEntity petEntityInBag = p.getPetKnapsack().removeByArticleId(pet.getPetPropsId(), "宠物炼做丹药", true);
		PetManager.getInstance().deletePet(p, pet);
		log.warn("宠物炼做丹药，移除宠物 {}", petEntityInBag);
		log.warn("宠物炼做丹药，移除宠物 {} 玩家 {}", petId, p.getName());
		bag.put(ae, "宠物转为魂魄丹药");
		p.sendError(String.format(getConfStr("UI_DESC_lianhuachenggong"),ae.getArticleName()));
		//
		PET2_LIAN_DAN_RES res = new PET2_LIAN_DAN_RES(message.getSequnceNum(), petId);
		log.warn("新的处理器");
		return res;
	}
	
	public String getConfStr(String str){
		return Pet2Manager.getInst().getConfStr(str);
	}
	
	@Override
	public boolean handleResponseMessage(Connection conn,
			RequestMessage request, ResponseMessage response)
			throws ConnectionException, Exception {
		return false;
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}
}
%>
<%
Field mf = GameNetworkFramework.class.getDeclaredField("message2SubSysMap");
mf.setAccessible(true);
HashMap<String, GameSubSystem[]> message2SubSysMap = (HashMap<String, GameSubSystem[]>)mf.get(GameNetworkFramework.getInstance());
GameSubSystem[] sys = message2SubSysMap.get("PET2_LIAN_DAN_REQ");
if(sys[0].getClass() == TakeSkill2.class){
	out.println("已经是新的处理器:"+sys[0].getClass().getSimpleName());
}else{
	sys[0] = new TakeSkill2();
}
%>
<br/>完成-防止手速快的刷宠物。
</BODY></html>
