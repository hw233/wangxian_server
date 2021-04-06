<%@ page contentType="text/html;charset=utf-8"%>
<%@ page import="java.util.*,com.fy.engineserver.smith.*"%>
<%     
ArticleRelationShipManager msm = ArticleRelationShipManager.getInstance();
Integer ships[] = msm.getForbidShips().toArray(new Integer[0]);
int num = 0;
for(Integer id : ships) {
	ArticleRelationShip ship = msm.getRelationShipMap().get(id);
	if(ship != null) {
		int count = ship.getMaxDownCount();
		if(count < 8) {
			msm.releaseShip(ship);
			num++;
		}
	}
}
out.println("共解开" + num + "个物品工作室");

/*
MoneyRelationShipManager arm = MoneyRelationShipManager.getInstance();
ships = arm.getForbidShips();
for(Integer id : ships) {
	MoneyRelationShip ship = arm.getRelationShipMap().get(id);
	if(ship != null) {
		int count = ship.getMaxDownCount();
		if(count < 8) {
			arm.releaseShip(ship);
		}
	}
}
*/
%>