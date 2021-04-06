package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.economic.BillingCenter;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 资源集合
 * 
 * 
 */
@SimpleEmbeddable
public class ResourceCollection implements Comparable<ResourceCollection>, FaeryConfig {

	private int food;
	private int wood;
	private int stone;

	public ResourceCollection() {

	}

	public ResourceCollection(int food, int wood, int stone) {
		this.food = food;
		this.wood = wood;
		this.stone = stone;
		if (!isValid()) {
			try {
				throw new Exception("[资源数配置错误: " + toString() + "]");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ResourceCollection(int resType, int num) {
		if (resType == FRUIT_RES_FOOD) {
			this.food = num;
		} else if (resType == FRUIT_RES_WOOD) {
			this.wood = num;
		} else if (resType == FRUIT_RES_STONE) {
			this.stone = num;
		} else {
			throw new IllegalStateException();
		}
		if (!isValid()) {
			try {
				throw new Exception("[资源数配置错误: " + toString() + "]");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public int getFood() {
		return food;
	}

	public void setFood(int food) {
		this.food = food;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public int getStone() {
		return stone;
	}

	public void setStone(int stone) {
		this.stone = stone;
	}

	@Override
	public String toString() {
		return "[" + Translate.粮食 + ":" + getFood() + "][" + Translate.木材 + ":" + getWood() + "][" + Translate.石料 + ":" + getStone() + "]";
	}

	/**
	 * 资源是否多于 o
	 * @param o
	 * @return
	 */
	public boolean moreThan(ResourceCollection o) {
		return compareTo(o) > 0;
	}

	/**
	 * 是否都多于目标
	 * @param o
	 * @return
	 */
	public boolean isEnough(ResourceCollection o) {
		return getWood() >= o.getWood() && getFood() >= o.getFood() && getStone() >= o.getStone();
	}

	/**
	 * 减少资源
	 * @param o
	 * @return
	 */
	public synchronized boolean deduct(ResourceCollection o) {
		// TODO 对象锁
		if (isEnough(o)) {
			this.setFood(this.getFood() - o.getFood());
			this.setStone(this.getStone() - o.getStone());
			this.setWood(this.getWood() - o.getWood());
			if (!isValid()) {
				unite(o);
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 是否有效
	 * @return
	 */
	public boolean isValid() {
		return getFood() >= 0 || getWood() >= 0 || getStone() >= 0;
	}

	/**
	 * 增加资源(合并)
	 * @param o
	 */
	public ResourceCollection unite(ResourceCollection o) {
		if (o.isValid()) {
			this.setFood(getFood() + o.getFood());
			this.setWood(getWood() + o.getWood());
			this.setStone(getStone() + o.getStone());
		} else {
			if (FaeryManager.logger.isErrorEnabled()) FaeryManager.logger.error("[合并资源异常][源:{}][客:{}]", new Object[] { toString(), o.toString() });
		}
		return this;
	}

	public String getNoticleString() {
		StringBuffer sbf = new StringBuffer();
		if (getFood() > 0) {
			sbf.append(Translate.粮食 + ":").append(getFood());
		}
		if (getWood() > 0) {
			sbf.append(Translate.木材 + ":").append(getWood());
		}
		if (getStone() > 0) {
			sbf.append(Translate.石料 + ":").append(getStone());
		}
		return sbf.toString();
	}

	@Override
	public int compareTo(ResourceCollection o) {
		if (getWood() > o.getWood() || getFood() > o.getFood() || getStone() > o.getStone()) {
			return 1;
		}
		return -1;
	}

	@Override
	protected Object clone() {
		ResourceCollection collection = new ResourceCollection(this.getFood(), this.getWood(), this.getStone());
		return collection;
	}

	/**
	 * 当前资源与所需资源相比是否足够的加色显示
	 * @param compare
	 * @return
	 */
	public String getCompareShowColor(ResourceCollection cost) {

		StringBuffer sbf = new StringBuffer();
		String color = "#FFFFFF";
		if (this.getFood() < cost.getFood()) {
			color = "#FF0000";
		} else {
			color = "#FFFFFF";
		}
		sbf.append("<f color='" + color + "'>").append(Translate.粮食).append(":").append(cost.getFood()).append("</f>").append("\n");
		if (this.getWood() < cost.getWood()) {
			color = "#FF0000";
		} else {
			color = "#FFFFFF";
		}
		sbf.append("<f color='" + color + "'>").append(Translate.木材).append(":").append(cost.getWood()).append("</f>").append("\n");
		if (this.getStone() < cost.getStone()) {
			color = "#FF0000";
		} else {
			color = "#FFFFFF";
		}
		sbf.append("<f color='" + color + "'>").append(Translate.石料).append(":").append(cost.getStone()).append("</f>").append("\n");

		return sbf.toString();
	}

	public static void main(String[] args) {
		ResourceCollection c1 = new ResourceCollection(1, 3, 3);
		ResourceCollection c2 = new ResourceCollection(1, 5, 3);
	}
}
