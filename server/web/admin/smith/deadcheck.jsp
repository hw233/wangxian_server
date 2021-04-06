<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.*"%>
<%!
public boolean isDeadLooped(ArticleRelationShip ship) {
	HashSet<Long> rset = new HashSet<Long>();
	try {
		for(Long mmId : ship.getTopLevelList()) {
			ArticleMaker mm = ArticleRelationShipManager.getInstance().getArticleMaker(mmId);
			if(mm == null) {
				rset.add(mmId);
				continue;
			}
			int counter[] = new int[]{0};
			boolean dead = isDeadLooped(mm, new HashSet<Long>(), counter);
			if(dead || counter[0] > 500) {
				if(counter[0] > 500) {
					ArticleRelationShipManager.logger.warn("[递归关系超过500的网络] ["+ship.getId()+"] ["+counter[0]+"]");
				}
				return true;
			}
		}
		return false;
	} finally {
		Iterator<Long> ite = ship.getTopLevelList().iterator();
		while(ite.hasNext()) {
			long id = ite.next();
			if(rset.contains(id)) {
				ite.remove();
			}
		}
	}
}

public boolean isDeadLooped(ArticleMaker am, HashSet<Long> ids, int counter[]) {
	if(ids.contains(am.getId())) {
		return true;
	}
	counter[0]++;
	HashSet<Long> rset = new HashSet<Long>();
	List<Long> downList = am.getDownList();
	ids.add(am.getId());
	try {
		for(Long mmId : downList) {
			ArticleMaker mm = ArticleRelationShipManager.getInstance().getArticleMaker(mmId);
			if(mm == null) {
				rset.add(mmId);
				continue;
			}
			boolean dead = isDeadLooped(mm, ids, counter);
			if(dead) {
				return true;
			}
		}
		return false;
	} finally {
		ids.remove(am.getId());
		Iterator<Long> ite = downList.iterator();
		while(ite.hasNext()) {
			long id = ite.next();
			if(rset.contains(id)) {
				ite.remove();
			}
		}
	}
}

public boolean isDeadLooped(MoneyRelationShip ship) {
	HashSet<Long> rset = new HashSet<Long>();
	try {
		for(Long mmId : ship.getTopLevelList()) {
			ArticleMaker mm = ArticleRelationShipManager.getInstance().getArticleMaker(mmId);
			if(mm == null) {
				rset.add(mmId);
				continue;
			}
			int counter[] = new int[]{0};
			boolean dead = isDeadLooped(mm, new HashSet<Long>(), counter);
			if(dead || counter[0] > 500) {
				if(counter[0] > 500) {
					ArticleRelationShipManager.logger.warn("[递归关系超过500的网络] ["+ship.getId()+"] ["+counter[0]+"]");
				}
				return true;
			}
		}
		return false;
	} finally {
		Iterator<Long> ite = ship.getTopLevelList().iterator();
		while(ite.hasNext()) {
			long id = ite.next();
			if(rset.contains(id)) {
				ite.remove();
			}
		}
	}
}

public boolean isDeadLooped(MoneyMaker am, HashSet<Long> ids, int counter[]) {
	if(ids.contains(am.getId())) {
		return true;
	}
	counter[0]++;
	HashSet<Long> rset = new HashSet<Long>();
	List<Long> downList = am.getDownList();
	ids.add(am.getId());
	try {
		for(Long mmId : downList) {
			ArticleMaker mm = ArticleRelationShipManager.getInstance().getArticleMaker(mmId);
			if(mm == null) {
				rset.add(mmId);
				continue;
			}
			boolean dead = isDeadLooped(mm, ids, counter);
			if(dead) {
				return true;
			}
		}
		return false;
	} finally {
		ids.remove(am.getId());
		Iterator<Long> ite = downList.iterator();
		while(ite.hasNext()) {
			long id = ite.next();
			if(rset.contains(id)) {
				ite.remove();
			}
		}
	}
}

public class CheckThread extends Thread {
	public void run() {
		while(ArticleRelationShipManager.getInstance().isOpenning()) {
			try {
				Thread.sleep(5000);
				ArticleRelationShipManager arm = ArticleRelationShipManager.getInstance();
				ArticleRelationShip ships[] = arm.getRelationShipMap().values().toArray(new ArticleRelationShip[0]);
				for(ArticleRelationShip ship : ships) {
					if(!arm.isForbid(ship)) {
						boolean dead = isDeadLooped(ship);
						if(dead) {
							arm.removeShip(ship);
							ArticleRelationShipManager.logger.warn("[死循环的物品工作室:已删除] [id:"+ship.getId()+"]<br>");
						}
					}
				}
				
				MoneyRelationShipManager mrs = MoneyRelationShipManager.getInstance();
				MoneyRelationShip ships2[] = mrs.getRelationShipMap().values().toArray(new MoneyRelationShip[0]);
				for(MoneyRelationShip ship : ships2) {
					if(!mrs.isForbid(ship)) {
						boolean dead = isDeadLooped(ship);
						if(dead) {
							mrs.removeShip(ship);
							MoneyRelationShipManager.logger.warn("[死循环的金钱工作室:已删除] [id:"+ship.getId()+"]<br>");
						}
					}
				}
			} catch(Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
%>
<%
Thread t = new CheckThread();
t.setName("DeadLoop-CheckThread");
t.start();
out.println("完成了线程启动!");
%>