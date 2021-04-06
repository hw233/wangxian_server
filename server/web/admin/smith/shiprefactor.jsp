<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.*"%>
<%!
public class ShipRefactor implements Runnable {
	public void run() {
		while(ArticleRelationShipManager.getInstance().isOpenning()) {
			try {
				Thread.sleep(5000);
				
				HashSet<Long> set1 = new HashSet<Long>();
				
				ArticleRelationShipManager asm = ArticleRelationShipManager.getInstance();
				HashMap<Integer,ArticleRelationShip> shipsMap = asm.getRelationShipMap();
				ArticleRelationShip ships[] = shipsMap.values().toArray(new ArticleRelationShip[0]);
				for(ArticleRelationShip s : ships) {
					refactorArticleRelation(s, set1);
				}

				HashSet<Long> set2 = new HashSet<Long>();
				
				MoneyRelationShipManager asm2 = MoneyRelationShipManager.getInstance();
				HashMap<Integer,MoneyRelationShip> shipsMap2 = asm2.getRelationShipMap();
				MoneyRelationShip ships2[] = shipsMap2.values().toArray(new MoneyRelationShip[0]);
				for(MoneyRelationShip s : ships2) {
					refactorMoneyRelation(s, set2);
				}
			} catch(Throwable e) {
				ArticleRelationShipManager.logger.error("[页面refactor线程异常]", e);
			}
		}
	}
	
	public void refactorArticleRelation(ArticleRelationShip ship, HashSet<Long> idset) {
		HashMap<Long, ArticleMaker> makerMap = ArticleRelationShipManager.getInstance().getMakerMap();
		List<Long> list = ship.getTopLevelList();
		for(Long mmId : list) {
			ArticleMaker mm = makerMap.get(mmId);
			if(mm != null) {
				refactorArticleRelation(mm, ship.getId(), idset);
			}
		}
	}
	
	public void refactorArticleRelation(ArticleMaker maker, int newShipId, HashSet<Long> idset) {
		if(!idset.contains(maker.getId())) {
			if(ArticleRelationShipManager.getInstance().getPlayerRelationMap().get(maker.getId()) == null) {
				ArticleRelationShipManager.getInstance().getPlayerRelationMap().put(maker.getId(), newShipId);
				ArticleRelationShipManager.logger.info("[发现maker对应的relation为null, 重置maker的relation] ["+maker.getId()+"] ["+maker.getUsername()+"] [newShipId:"+newShipId+"]");
			} else if(ArticleRelationShipManager.getInstance().getPlayerRelationMap().get(maker.getId()) != newShipId) {
				int old = ArticleRelationShipManager.getInstance().getPlayerRelationMap().get(maker.getId());
				ArticleRelationShipManager.getInstance().getPlayerRelationMap().put(maker.getId(), newShipId);
				ArticleRelationShipManager.logger.info("[发现maker对应的relation不匹配, 重置maker的relation] ["+maker.getId()+"] ["+maker.getUsername()+"] [old:"+old+"] [newShipId:"+newShipId+"]");			
			}
		}
		idset.add(maker.getId());
		HashMap<Long, ArticleMaker> makerMap = ArticleRelationShipManager.getInstance().getMakerMap();
		List<Long> list = maker.getDownList();
		for(Long mmId : list) {
			ArticleMaker mm = makerMap.get(mmId);
			if(mm != null) {
				refactorArticleRelation(mm, newShipId, idset);
			}
		}
	}
	
	public void refactorMoneyRelation(MoneyRelationShip ship, HashSet<Long> idset) {
		HashMap<Long, MoneyMaker> makerMap = MoneyRelationShipManager.getInstance().getMakerMap();
		List<Long> list = ship.getTopLevelList();
		for(Long mmId : list) {
			MoneyMaker mm = makerMap.get(mmId);
			if(mm != null) {
				refactorMoneyRelation(mm, ship.getId(), idset);
			}
		}
	}
	
	public void refactorMoneyRelation(MoneyMaker maker, int newShipId, HashSet<Long> idset) {
		if(!idset.contains(maker.getId())) {
			if(MoneyRelationShipManager.getInstance().getPlayerRelationMap().get(maker.getId()) == null) {
				MoneyRelationShipManager.getInstance().getPlayerRelationMap().put(maker.getId(), newShipId);
				MoneyRelationShipManager.logger.info("[发现maker对应的relation为null, 重置maker的relation] ["+maker.getId()+"] ["+maker.getUsername()+"] [newShipId:"+newShipId+"]");
			} else if(MoneyRelationShipManager.getInstance().getPlayerRelationMap().get(maker.getId()) != newShipId) {
				int old = MoneyRelationShipManager.getInstance().getPlayerRelationMap().get(maker.getId());
				MoneyRelationShipManager.getInstance().getPlayerRelationMap().put(maker.getId(), newShipId);
				MoneyRelationShipManager.logger.info("[发现maker对应的relation不匹配, 重置maker的relation] ["+maker.getId()+"] ["+maker.getUsername()+"] [old:"+old+"] [newShipId:"+newShipId+"]");			
			}
		}
		idset.add(maker.getId());
		HashMap<Long, MoneyMaker> makerMap = MoneyRelationShipManager.getInstance().getMakerMap();
		List<Long> list = maker.getDownList();
		for(Long mmId : list) {
			MoneyMaker mm = makerMap.get(mmId);
			if(mm != null) {
				refactorMoneyRelation(mm, newShipId, idset);
			}
		}
	}
}
%>
<% 
Thread t = new Thread(new ShipRefactor(), "ShipRefactor");
t.start();
out.println("开启了检测线程.");
%>      