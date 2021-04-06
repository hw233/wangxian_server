package com.fy.engineserver.smith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 定义一个打金成员，它具有的特点是：
 * 	自己向上一级打金号汇聚银子，从下一级获取银子
 * 
 * @version 创建时间：Mar 27, 2013 6:32:00 PM
 * 
 */
public class ArticleMaker implements Serializable {
	
	public static final long serialVersionUID = 5473946543575439482L;
	
	private long id;
	
	/**
	 * 玩家的名字
	 */
	private String name;
	
	/**
	 * 玩家的用户名
	 */
	private String username;
	
	/**
	 * 下线的成员列表
	 */
	private List<Long> downList = new ArrayList<Long>();
	
	/**
	 * 上线的成员列表
	 */
	private List<Long> upList = new ArrayList<Long>();
	
	/**
	 * 上贡的时候给上贡者对应的数量
	 */
	private HashMap<Long, Long> upAmountMap = new HashMap<Long,Long>();
	
	/**
	 * 向上线汇聚的物品总数
	 */
	private int totalUp;
	
	/**
	 * 总共获得的数量
	 */
	private int totalSilver;
	
	/**
	 * 向上汇聚的次数
	 */
	private int upTimes;
	
	/**
	 * 平均每次上交的数量
	 */
	private int avgUp;
	
	/**
	 * 这个人真正的充值额度，用来判定是买金的，还是工作室上线，单位是分
	 */
	private long rmb;
	
	/**
	 * 当前拥有的银两
	 */
	private long currentSilver;
	
	/**
	 * IP地址
	 */
	private String ip;
	
	/**
	 * 机型
	 */
	private String mt;
	
	/**
	 * 等级
	 */
	private int level;
	
	private transient int layer;
	
	public void addUp(long upId, int amount) {
		totalUp += amount;
		upTimes++;
		avgUp = totalUp/upTimes;
		if(upAmountMap == null) {
			upAmountMap = new HashMap<Long,Long>();
		}
		Long mtotal = upAmountMap.get(upId);
		if(mtotal == null) {
			upAmountMap.put(upId, new Long(amount));
		} else {
			upAmountMap.put(upId, mtotal+amount);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Long> getDownList() {
		return downList;
	}

	public void setDownList(List<Long> downList) {
		this.downList = downList;
	}

	public List<Long> getUpList() {
		return upList;
	}

	public void setUpList(List<Long> upList) {
		this.upList = upList;
	}

	public int getTotalUp() {
		return totalUp;
	}

	public void setTotalUp(int totalUp) {
		this.totalUp = totalUp;
	}

	public int getAvgUp() {
		return avgUp;
	}

	public void setAvgUp(int avgUp) {
		this.avgUp = avgUp;
	}

	public long getRmb() {
		return rmb;
	}

	public void setRmb(long rmb) {
		this.rmb = rmb;
	}

	public int getUpTimes() {
		return upTimes;
	}

	public void setUpTimes(int upTimes) {
		this.upTimes = upTimes;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public int getTotalSilver() {
		return totalSilver;
	}

	public void setTotalSilver(int totalSilver) {
		this.totalSilver = totalSilver;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMt() {
		return mt;
	}

	public void setMt(String mt) {
		this.mt = mt;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getCurrentSilver() {
		return currentSilver;
	}

	public void setCurrentSilver(long currentSilver) {
		this.currentSilver = currentSilver;
	}
	
	public long getUpAmount(long upId) {
		if(upAmountMap == null) {
			upAmountMap = new HashMap<Long,Long>();
		}
		Long count = upAmountMap.get(upId);
		if(count == null) {
			return 0;
		}
		return count;
	}

	public int getDownLayerCount() {
		int max = 0;
		for(Long mmId : downList) {
			ArticleMaker mm = ArticleRelationShipManager.getInstance().getArticleMaker(mmId);
			if(mm != null) {
				mm.setLayer(2);
				int layer = recursiveGetDownLayerCount(mm, new int[]{2});
				if(layer > max) {
					max = layer;
				}
			}
		}
		return max;
	}
	
	public int recursiveGetDownLayerCount(ArticleMaker maker, int[] layer) {
		if(maker.getLayer() > layer[0]) {
			layer[0] = maker.getLayer();
		}
		if(maker.getDownList().size() > 0) {
			for(Long mmId : maker.getDownList()) {
				ArticleMaker mm = ArticleRelationShipManager.getInstance().getArticleMaker(mmId);
				if(mm != null) {
					mm.setLayer(maker.getLayer()+1);
					recursiveGetDownLayerCount(mm, layer);
				}
			}
		}
		return layer[0];
	}
	
	public ArticleMaker4Client getClient() {
		ArticleMaker4Client mc = new ArticleMaker4Client();
		mc.setCurrentSilver(currentSilver);
		mc.setForbid(ArticleRelationShipManager.getInstance().isForbid(this.id));
		mc.setId(id);
		mc.setIp(ip);
		mc.setLevel(level);
		mc.setName(name);
		mc.setRmb(rmb);
		mc.setTotalUp(totalUp);
		mc.setUsername(username);
		if(this.downList.size() > 0) {
			for(Long mmId : this.downList) {
				ArticleMaker mm = ArticleRelationShipManager.getInstance().getArticleMaker(mmId); 
				mc.getSubMakers().add(mm.getClient());
			}
		}
		return mc;
	}
}
