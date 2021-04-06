package com.fy.engineserver.smith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 *
 * 
 * @version 创建时间：Mar 27, 2013 6:43:06 PM
 * 
 */
public class MoneyRelationShip implements Serializable {
	
	public static final long serialVersionUID = 457948060695060596L;
	
	private int id;
	
	/**
	 * 顶层的工作室成员，有可能是最终的买金者
	 */
	private List<Long> topLevelList = new ArrayList<Long>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<Long> getTopLevelList() {
		return topLevelList;
	}

	public void setTopLevelList(List<Long> topLevelList) {
		this.topLevelList = topLevelList;
	}
	
	/**
	 * 获得最底层的向上汇集总数
	 * @return
	 */
	public long getBottomLevelTotalUp() {
		long total[] = new long[]{0};
		for(Long mmId : topLevelList) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null) {
				recursiveGetTotalUp(mm, total);
			}
		}
		return total[0];
	}
	
	/**
	 * 只累计最低层的汇集
	 * @param upMaker
	 * @param total
	 * @return
	 */
	public long recursiveGetTotalUp(MoneyMaker upMaker, long total[]) {
		if(!MoneyRelationShipManager.getInstance().isOpenning()) {
			return 0;
		}
		if(upMaker.getDownList().size() == 0) {
			total[0] += upMaker.getTotalUp();
		} else {
			for(Long mmId : upMaker.getDownList()) {
				MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
				if(mm != null) {
					recursiveGetTotalUp(mm, total);
				}
			}
		}
		return total[0];
	}

	public MoneyMaker findMoneyMaker(String playerName) {
		for(int i=0; i<topLevelList.size(); i++) {
			MoneyMaker maker = MoneyRelationShipManager.getInstance().getMoneyMaker(topLevelList.get(i));
			if(maker.getName().equals(playerName)) {
				return maker;
			}
			MoneyMaker mm = searchMoneyMaker(maker, playerName);
			if(mm != null) {
				return mm;
			}
		}
		return null;
	}
	
	/**
	 * 从一个上线寻找在他的下线内的maker
	 * @param upMaker
	 * @param playerName
	 * @return
	 */
	public MoneyMaker searchMoneyMaker(MoneyMaker upMaker, String playerName) {
		if(!MoneyRelationShipManager.getInstance().isOpenning()) {
			return null;
		}
		List<Long> downList = upMaker.getDownList();
		if(downList.size() > 0) {
			for(int i=0; i<downList.size(); i++) {
				MoneyMaker down = MoneyRelationShipManager.getInstance().getMoneyMaker(downList.get(i));
				if(down != null && down.getName().equals(playerName)) {
					return down;
				} else {
					MoneyMaker mm = searchMoneyMaker(down, playerName);
					if(mm != null) {
						return mm;
					}
				}
			}
		}
		return null;
	}
	
	public boolean hasMinDownCount(int min) {
		for(Long mmId : topLevelList) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null && hasMinDownCount(mm, min, new HashSet<Long>())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否包含大于min的单层下线成员数量
	 * @param min
	 * @return
	 */
	public boolean hasMinDownCount(MoneyMaker upMaker, int min, HashSet<Long> idset) {
		if(idset.contains(upMaker.getId())) {
			return false;
		}
		idset.add(upMaker.getId());
		List<Long> downs = upMaker.getDownList();
		if(downs.size() >= min) {
			return true;
		}
		for(Long mmId : downs) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null && hasMinDownCount(mm, min, idset)) {
				return true;
			}
		}
		idset.remove(upMaker.getId());
		return false;
	}
	
	public boolean isDeadLooped() {
		HashSet<Long> rset = new HashSet<Long>();
		try {
			for(Long mmId : topLevelList) {
				MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
				if(mm == null) {
					rset.add(mmId);
					continue;
				}
				boolean dead = isDeadLooped(mm, new HashSet<Long>());
				if(dead) {
					return true;
				}
			}
			return false;
		} finally {
			Iterator<Long> ite = topLevelList.iterator();
			while(ite.hasNext()) {
				long id = ite.next();
				if(rset.contains(id)) {
					ite.remove();
				}
			}
		}
	}
	
	public boolean isDeadLooped(MoneyMaker am, HashSet<Long> ids) {
		if(ids.contains(am.getId())) {
			return true;
		}
		ids.add(am.getId());
		HashSet<Long> rset = new HashSet<Long>();
		List<Long> downList = am.getDownList();
		try {
			for(Long mmId : downList) {
				MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
				if(mm == null) {
					rset.add(mmId);
					continue;
				}
				boolean dead = isDeadLooped(mm, ids);
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
	
	/**
	 * 得到单层最大的下线数量
	 * @return
	 */
	public int getMaxDownCount() {
		int max[] = new int[1];
		for(Long mmId : topLevelList) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null) {
				recursiveGetMaxDownCount(mm, max);
			}
		}
		return max[0];
	}
	
	public int recursiveGetMaxDownCount(MoneyMaker upMaker, int max[]) {
		if(!MoneyRelationShipManager.getInstance().isOpenning()) {
			return 0;
		}
		if(upMaker.getDownList().size() > max[0]) {
			max[0] = upMaker.getDownList().size();
		}
		for(Long mmId : upMaker.getDownList()) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null) {
				recursiveGetMaxDownCount(mm, max);
			}
		}
		return max[0];
	}
	
	/**
	 * 层次关系是否大于min值
	 * @param min
	 * @return
	 */
	public boolean hasMinLayerCount(int min) {
		List<Long> makers = this.topLevelList;
		for(Long mmId : makers) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null && mm.getDownLayerCount() >= min) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 得到最大的层级
	 * @return
	 */
	public int getMaxLayerCount() {
		int max = 0;
		for(Long mmId : topLevelList) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null) {
				int count = mm.getDownLayerCount();
				if(count > max) {
					max = count;
				}
			}
		}
		return max;
	}
	
	public MoneyRelationShip4Client getClient() {
		MoneyRelationShip4Client c = new MoneyRelationShip4Client();
		c.setForbid(MoneyRelationShipManager.getInstance().isForbid(this));
		c.setId(this.id);
		for(Long mmId : topLevelList) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			MoneyMaker4Client mc = mm.getClient();
			c.getTopMakers().add(mc);
		}
		return c;
	}
	
	public HashMap<String,Integer> getUniqueIPAddress() {
		HashMap<String, Integer> ipset = new HashMap<String, Integer>();
		for(Long mmId : topLevelList) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null) {
				recursiveGetUniqueIPAddress(mm, ipset);
			}
		}
		return ipset;
	}
	
	public void recursiveGetUniqueIPAddress(MoneyMaker maker, HashMap<String, Integer> ipset) {
		if(maker.getIp() != null && maker.getIp().length() > 0) {
			String idstr = maker.getIp().substring(0, maker.getIp().lastIndexOf("."));
			Integer v = ipset.get(idstr);
			if(v == null) {
				v = 0;
			}
			ipset.put(idstr, v+1);
		}
		for(Long mmId : maker.getDownList()) {
			MoneyMaker mm = MoneyRelationShipManager.getInstance().getMoneyMaker(mmId);
			if(mm != null) {
				recursiveGetUniqueIPAddress(mm, ipset);
			}
		}
	}
}
