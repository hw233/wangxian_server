<%@ page contentType="text/html;charset=utf-8" import="java.util.*,com.xuanzhi.tools.text.*,com.fy.engineserver.core.*,
com.fy.engineserver.sprite.*,com.xuanzhi.tools.transport.*,com.xuanzhi.tools.queue.*,
com.xuanzhi.tools.cache.diskcache.*,com.xuanzhi.tools.cache.diskcache.concrete.*,
com.fy.engineserver.downcity.*,com.fy.engineserver.downcity.stat.*"%><%


DownCityScheduleManager mmmm = DownCityScheduleManager.getInstance();
DefaultDiskCache ddc = mmmm.getDdc();
String dcsId = request.getParameter("id");
DownCitySchedule dcs = null;
HashMap<String,Integer> hm = new HashMap<String,Integer>();
ShopManager sm = ShopManager.getInstance();
if(dcsId != null){
	dcs = (DownCitySchedule)ddc.get(dcsId);
}

%>
<%@page import="com.fy.engineserver.gateway.GameNetworkFramework"%>

<%@page import="com.fy.engineserver.datasource.career.CareerManager"%>
<%@page import="com.fy.engineserver.shop.ShopManager"%>
<%@page import="com.fy.engineserver.shop.Shop"%>
<%@page import="com.fy.engineserver.shop.Goods"%><%@include file="../IPManager.jsp" %><html><head>
<link rel="stylesheet" type="text/css" href="../../css/common.css">
<link rel="stylesheet" type="text/css" href="../../css/table.css">
<style type="text/css">
.titlecolor{
background-color:#C2CAF5;
}
</style>
</HEAD>
<BODY>
<input type="button" value="返回" onclick="javascript:history.back();">
<h2>副本统计情况</h2>
<table>
<tr class="titlecolor"><td colspan="26">副本中人的信息</td></tr>
<tr class="titlecolor"><td>玩家</td><td>id</td><td>用户名</td><td>职业</td><td>级别</td><td>血量</td><td>蓝量</td><td>物攻减伤</td><td>法攻减伤</td><td>物理攻击强度</td><td>法术攻击强度</td><td>暴击</td><td>闪避</td><td>韧性</td><td>伤害输出</td><td>治疗输出</td><td>受到伤害</td><td>受到治疗</td><td>死亡次数</td><td>缓回血量</td><td>缓回蓝量</td><td>瞬回血量</td><td>瞬回蓝量</td><td>在副本中的时长</td><td>耐久损耗金钱</td></tr>
<%
if(dcs != null){
	HashMap<Long,DownCityPlayerInfo> playerhm = dcs.getDownCityPlayerInfos();
	if(playerhm != null && playerhm.keySet() != null){
		for(Long playerid : playerhm.keySet()){
			DownCityPlayerInfo dcpi = playerhm.get(playerid);
			if(dcpi != null){
				out.println("<tr>");
				out.println("<td>"+dcpi.getName()+"</td>");
				out.println("<td>"+dcpi.getId()+"</td>");
				out.println("<td>"+dcpi.getUsername()+"</td>");
				String career = "";
				try{
					if(dcpi.getCareer() == null || dcpi.getCareer().trim().equals("")){
						Player p = PlayerManager.getInstance().getPlayer(dcpi.getId());
					    career = CareerManager.careerNames[p.getCareer()];
					}else{
						career = dcpi.getCareer();
					}
				}catch(Exception ex){
					
				}
				out.println("<td>"+career+"</td>");
				out.println("<td>"+dcpi.getLevel()+"</td>");
				out.println("<td>"+dcpi.getTotalHp()+"</td>");
				out.println("<td>"+dcpi.getTotalMp()+"</td>");
				out.println("<td>"+dcpi.getDefence()+"</td>");
				out.println("<td>"+dcpi.getResistance()+"</td>");
				out.println("<td>"+dcpi.getMeleeAttackIntensity()+"</td>");
				out.println("<td>"+dcpi.getSpellAttackIntensity()+"</td>");
				out.println("<td>"+dcpi.getCriticalHit()+"</td>");
				out.println("<td>"+dcpi.getDodge()+"</td>");
				out.println("<td>"+dcpi.getToughness()+"</td>");
				out.println("<td>"+dcpi.getShanghaiShuChu()+"</td>");
				out.println("<td>"+dcpi.getZhiliaoShuChu()+"</td>");
				out.println("<td>"+dcpi.getReceiveDamage()+"</td>");
				out.println("<td>"+dcpi.getReceiveZhiliao()+"</td>");
				out.println("<td>"+dcpi.getDeadCount()+"</td>");
				out.println("<td>"+dcpi.getHuanhuiHp()+"</td>");
				out.println("<td>"+dcpi.getHuanhuiMp()+"</td>");
				out.println("<td>"+dcpi.getShunhuiHp()+"</td>");
				out.println("<td>"+dcpi.getShunhuiMp()+"</td>");
				out.println("<td>"+(dcpi.getLastingTime()/60000)+"分</td>");
				out.println("<td>"+dcpi.getMoneyForNaijiu()+"</td>");
				out.println("</tr>");
			}
		}
	}
}
%>
</table>

<table>
<tr class="titlecolor"><td colspan="26">副本产出信息</td></tr>
<tr class="titlecolor"><td>怪物名</td><td>掉落金币</td><td>掉落物品</td><td>掉落时间</td></tr>
<%
if(dcs != null){
	List<DownCityOutputInfo> dcoiList = dcs.getDownCityOutputInfos();
	if(dcoiList != null){
		for(DownCityOutputInfo dcoi : dcoiList){
			if(dcoi != null){
				out.println("<tr>");
				out.println("<td>"+dcoi.getMonsterName()+"</td>");
				out.println("<td>"+dcoi.getFlopMoney()+"</td>");
				out.println("<td>"+dcoi.getPropName()+"</td>");
				out.println("<td>"+DateUtil.formatDate(new Date(dcoi.getOutputPoint()),"yyyy-MM-dd HH:mm:ss")+"</td>");
				out.println("</tr>");
			}
		}
	}
}
%>
</table>

<table>
<tr class="titlecolor"><td colspan="26">副本消耗信息</td></tr>
<tr class="titlecolor"><td>玩家</td><td>id</td><td>用户名</td><td>使用道具名</td><td>价格金币</td></tr>
<%
if(dcs != null){
	List<DownCityConsumeInfo> dcciList = dcs.getDownCityConsumeInfos();
	if(dcciList != null){
		int totalMoney = 0;
		for(DownCityConsumeInfo dcci : dcciList){
			if(dcci != null){
				out.println("<tr>");
				out.println("<td>"+dcci.getPlayerName()+"</td>");
				out.println("<td>"+dcci.getId()+"</td>");
				out.println("<td>"+dcci.getUserName()+"</td>");
				out.println("<td>"+dcci.getPropName()+"</td>");
				if(hm.get(dcci.getPropName()) == null){
					if(sm != null){
						Map<String,Shop> map = sm.getShops();
						if(map != null && map.keySet() != null){
							boolean exist = false;
							for(String key : map.keySet()){
								if(key != null && map.get(key) != null){
									Shop shop = map.get(key);
									if(shop.getName().indexOf("奇珍商店") >= 0 || shop.getName().indexOf("打折抢购") >= 0){
										continue;
									}
									Goods[] goods = shop.getGoods();
									if(goods != null){
										for(Goods good : goods){
											if(good != null && good.getArticleName().equals(dcci.getPropName())){
												if(good != null && good.getMoneyPrice() > 0){
													hm.put(good.getArticleName(),good.getMoneyPrice()/good.getBundleNum());
												}else{
													hm.put(good.getArticleName(),0);
												}
												exist = true;
												break;
											}
										}
									}
									if(exist){
										break;
									}
								}
							}
						}
					}
				}
				if(hm.get(dcci.getPropName()) == null){
					out.println("<td>"+0+"</td>");
				}else{
					totalMoney += hm.get(dcci.getPropName());
					out.println("<td>"+hm.get(dcci.getPropName())+"</td>");
				}
				
				out.println("</tr>");
			}
		}
		out.println("<tr><td colspan='6'>使用道具消耗金币:"+totalMoney+"</td></tr>");
	}
}
%>
</table>

</BODY></html>
