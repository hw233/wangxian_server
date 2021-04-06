<%@page import="com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.PropsEntity"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.EquipmentEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.impl.SimpleEntityManagerMysqlImpl"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleEntityManager"%>
<%@page import="com.fy.engineserver.datasource.article.data.entity.ArticleEntity"%>
<%@page import="com.xuanzhi.tools.simplejpa.SimpleEntityManager"%>
<%@page import="com.fy.engineserver.uniteserver.DeleteArticleEntity"%>
<%@page import="com.fy.engineserver.sprite.TeamSubSystem"%>
<%@page import="com.fy.engineserver.datasource.article.data.equipments.Equipment"%>
<%@page import="com.fy.engineserver.datasource.article.data.articles.Article"%>
<%@page import="com.fy.engineserver.datasource.article.manager.ArticleManager"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8">
		<title>修改组队锁bug</title>
		<link rel="stylesheet" href="gm/style.css" />
		<script type="text/javascript">
	
</script>
	</head>
	<body>
		<br> 
		<%
			String action = request.getParameter("action");
			if (action != null) {
				if (action.equals("dele")) {
					DeleteArticleEntity.isBreak = false;
					try {
						if (DeleteArticleEntity.isCheckUse) {
							DeleteArticleEntity.logger.warn("开始拍卖中articleEntity数据获取");
							DeleteArticleEntity.getAuctionArticleEntitysToServer();
							DeleteArticleEntity.logger.warn("结束拍卖中articleEntity数据获取");
							DeleteArticleEntity.logger.warn("开始坐骑中articleEntity数据获取");
							DeleteArticleEntity.getHorseArticleEntitysTOServer();
							DeleteArticleEntity.logger.warn("结束坐骑中articleEntity数据获取");
							DeleteArticleEntity.logger.warn("开始宠物articleEntity数据获取");
							DeleteArticleEntity.getPetArticleEntitysToServer();
							DeleteArticleEntity.logger.warn("结束宠物articleEntity数据获取");
							DeleteArticleEntity.logger.warn("开始家族仓库articleEntity数据获取");
							DeleteArticleEntity.getJiaZuCangKuArticleEntitysToServer();
							DeleteArticleEntity.logger.warn("结束家族仓库articleEntity数据获取");
							DeleteArticleEntity.logger.warn("开始千层塔 articleEntity数据获取");
							DeleteArticleEntity.getQianCengTaArticleEntitysToServer();
							DeleteArticleEntity.logger.warn("结束千层塔 articleEntity数据获取");
							DeleteArticleEntity.logger.warn("开始Player articleEntity数据获取");
							DeleteArticleEntity.getPlayerArticleEntitysToServer();
							DeleteArticleEntity.logger.warn("结束Player articleEntity数据获取");
							DeleteArticleEntity.logger.warn("开始mail articleEntity数据获取");
							DeleteArticleEntity.getMailArticleEntitysToServer();
							DeleteArticleEntity.logger.warn("结束mail articleEntity数据获取");
							DeleteArticleEntity.logger.warn("开始装备中宝石器灵 articleEntity数据获取");
							DeleteArticleEntity.getEquipmentEntitysArticleEntity();
							DeleteArticleEntity.logger.warn("结束装备中宝石器灵 articleEntity数据获取" + " 总数:" + DeleteArticleEntity.useIDs.size());
							
							DeleteArticleEntity.maxID = ArticleEntityManager.getInstance().em.queryIds(ArticleEntity.class, "", "id desc", 1,2)[0];
						}
						DeleteArticleEntity.logger.warn("开始删除物品");
						long deleteNum = 0;
						SimpleEntityManager<ArticleEntity> articleEntityEm = ArticleEntityManager.getInstance().em;
						long allCount = articleEntityEm.count();
						int pagIndex = 0;
						int pagNum = 5000;
						long starttt = System.currentTimeMillis();
						
						if (false) {
							long gg = System.currentTimeMillis();
							DeleteArticleEntity.logger.warn("删除索引开始");
							articleEntityEm.dropEntityIndex(ArticleEntity.class);
							DeleteArticleEntity.logger.warn("删除索引结束 ["+(System.currentTimeMillis()-gg)+"]");
						}
						long lastID = articleEntityEm.queryIds(ArticleEntity.class, "", "", 1,2)[0];
						int zeroNum = 0;
						while(true) {
							if (DeleteArticleEntity.isBreak) {
								break;
							}
							try {
								long[] ids = articleEntityEm.queryIds(ArticleEntity.class, "id > ? and id <= ?", new Object[]{lastID, lastID+10000}, "id", pagNum*pagIndex+1, pagNum*pagIndex+pagNum+1);
								long oldDeleteNum = deleteNum;
								long tt = System.currentTimeMillis();
								if (ids.length > 0) {
									for (long ii : ids) {
										if ((!DeleteArticleEntity.useIDs.contains(ii)) && ii < DeleteArticleEntity.maxID) {
											ArticleEntity entity = articleEntityEm.find(ii);
											if (entity instanceof EquipmentEntity || entity instanceof PropsEntity || entity instanceof QiLingArticleEntity) {
												if (DeleteArticleEntity.isRealDelete) {
													articleEntityEm.remove(entity);
												}
												deleteNum++;
												DeleteArticleEntity.logger.warn("[删除物品] ["+entity.getId()+"] ["+entity.getArticleName()+"]");
											}
										}
									}
								}
								DeleteArticleEntity.logger.warn("[查询ID"+lastID+"] [maxID:"+DeleteArticleEntity.maxID+"] 删除物品 数目["+(deleteNum - oldDeleteNum)+"] [总删除数目:"+deleteNum+"] ["+allCount+"] [删除万分之:"+((deleteNum*10000L)/allCount)+"] [花费:"+(System.currentTimeMillis()-tt)+"ms]");
								if (pagIndex * pagNum >= allCount) {
									break;
								}
								if (ids.length > 0) {
									lastID = ids[ids.length - 1];
								}
								if (deleteNum - oldDeleteNum == 0) {
									lastID += 500000;
									zeroNum++;
								}else {
									zeroNum = 0;
								}
								if (zeroNum >= 10) {
									lastID += 5000000;
								}
								if (zeroNum >= 100) {
									lastID += 1000000000000000L;
									lastID = lastID/10000000000000L*10000000000000L;
									zeroNum = 0;
								}
								if (lastID >= DeleteArticleEntity.maxID) {
									break;
								}
							} catch(Exception e) {
								DeleteArticleEntity.logger.error("----------", e);
							}
						}
						DeleteArticleEntity.logger.warn("删除物品结束， 删除物品数目:" + deleteNum + "  总数:" + allCount + "  有用数目:" + DeleteArticleEntity.useIDs.size() + "   共花费:" + (System.currentTimeMillis()-starttt));
					} catch(Exception e) {
						DeleteArticleEntity.logger.error("", e);
					}
				}else if (action.equals("stop")) {
					DeleteArticleEntity.isBreak = true;
				}else if (action.equals("checkBoolean")) {
					DeleteArticleEntity.isCheckUse = !DeleteArticleEntity.isCheckUse;
				}
			}
		%>
		<br>
		<br>
		<form>
			<input type="hidden" name="action" value="checkBoolean">
			是否检查ID<input type="text" name="isCheckUse" value="<%=DeleteArticleEntity.isCheckUse %>">
			<input type="submit" value="修改状态">
		</form>
		<br>
	<form>
		<input type="hidden" name="action" value="dele">
		<input type="submit" value="开始删除">
	</form>
		<br>
		<br>
		<form>
		<input type="hidden" name="action" value="stop">
		<input type="submit" value="停止删除">
		</form>
	</body>
</html>
