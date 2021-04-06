package com.fy.engineserver.activity.TransitRobbery;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fy.engineserver.activity.TransitRobbery.model.RobberyConstant;
import com.fy.engineserver.core.ExperienceManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.concrete.GamePlayerManager;

public class FeiShengExpThread extends Thread{
	/** key=playerId   value=开始观礼时间--超过观礼时间加经验*/
	public Map<Long, Long> addExpMap = new HashMap<Long, Long>();
	/** 删除的列表 */
	private Set<Long> removeList = new HashSet<Long>();
	/** 确定观礼后38s加经验 */
	public static int guanliTime = 25;
	
	public boolean isStart = true;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		long currentTime;
		while(isStart){
			synchronized (addExpMap) {
				for(Long id : removeList){
					addExpMap.remove(id);
				}
			}
			removeList.clear();
			if(addExpMap.size() > 0){
				currentTime = System.currentTimeMillis();
				Iterator<Long> ite = addExpMap.keySet().iterator();
				long pId = 0;
				while(ite.hasNext()){
					pId = ite.next();
					if(pId > 0 && currentTime >= (addExpMap.get(pId) + guanliTime * 1000)){
						removeList.add(pId);
						try {
							Player p = GamePlayerManager.getInstance().getPlayer(pId);
							long exp =  (long) (p.getNextLevelExp() * RobberyConstant.expAdd / 100);
							if(exp > RobberyConstant.MAXEXPAWARD) {
								exp = RobberyConstant.MAXEXPAWARD;
							}
							p.addExp(exp, ExperienceManager.观看飞升动画);
							if (TransitRobberyManager.logger.isInfoEnabled()) {
								TransitRobberyManager.logger.info("[确认观礼] [加经验] [成功] [" + p.getLogString() + "]");
							}
//							String msg = Translate.translateString(Translate.观礼飞升获得经验, new String[][] {{Translate.STRING_1, String.valueOf(exp)}});
//							p.sendError(msg);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							TransitRobberyManager.logger.error("[飞升观礼经验获取player出错][playerId = " + pId + "]", e);
						}
					} else if(pId <= 0){
						removeList.add(pId);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					TransitRobberyManager.logger.error("[飞升观礼线程出错]",e);
				}
			} else {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					TransitRobberyManager.logger.error("[飞升观礼线程出错]",e);
				}
			}
		}
	}

}
