package com.fy.engineserver.activity.wafen.instacne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.wafen.instacne.model.FenDiModel;
import com.fy.engineserver.activity.wafen.manager.WaFenManager;
import com.fy.engineserver.activity.wafen.model.FenMuModel;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.sprite.Player;

/**
 * 个人拥有坑位
 * @author Administrator
 *
 */
public class WaFenInstance4Private implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 玩家id */
	private long playerId;
	/** 挖过的次数 */
	private int times;
	/** 挖开的坟地 (key=坟墓name)*/
	private Map<String, List<FenDiModel>> openFendiMaps = new HashMap<String, List<FenDiModel>>();
	/** 剩余银铲子数量 */
	private int leftYinChanzi;
	/** 剩余金铲子数量 */
	private int leftJinChanzi;
	/** 剩余琉璃铲子 */
	private int leftLiuLiChanzi;
	
	public int saveChanZi(byte type, int num) {
		if (type == WaFenManager.yinChanZi) {
			leftYinChanzi = leftYinChanzi + num;
			return leftYinChanzi;
		} else if (type == WaFenManager.jinChanZi) {
			leftJinChanzi = leftJinChanzi + num;
			return leftYinChanzi;
		} else if (type == WaFenManager.liuliChanZi) {
			leftLiuLiChanzi = leftLiuLiChanzi + num;
			return leftYinChanzi;
		}
		return 0;
	}
	public boolean expenseChanZi(byte type, int num) {
		if (type == WaFenManager.yinChanZi) {
			leftYinChanzi = leftYinChanzi - num;
			return leftYinChanzi >=0;
		} else if (type == WaFenManager.jinChanZi) {
			leftJinChanzi = leftJinChanzi - num;
			return leftYinChanzi>=0;
		} else if (type == WaFenManager.liuliChanZi) {
			leftLiuLiChanzi = leftLiuLiChanzi - num;
			return leftYinChanzi >= 0;
		}
		return false;
	}
	
	public int getChanziNum(byte type) {
		if (type == WaFenManager.yinChanZi) {
			return leftYinChanzi;
		} else if (type == WaFenManager.jinChanZi) {
			return leftJinChanzi;
		} else if (type == WaFenManager.liuliChanZi) {
			return leftLiuLiChanzi;
		}
		return 0;
	}
	/**
	 * 十连挖坟地index
	 * @param fenmuName
	 * @return
	 */
	public List<Integer> getFendiIndex4Ten(String fenmuName, Player player){
		List<Integer> result = new ArrayList<Integer>();
		List<FenDiModel> list = openFendiMaps.get(fenmuName);
		FenMuModel fm = WaFenManager.instance.fenmuMap.get(fenmuName);
		if (/*list != null && */fm != null) {
			List<Integer> tempList = new ArrayList<Integer>();
			for (int i=0; i<fm.getTotalNum(); i++) {
				boolean exist = false;
				if (list != null) {
					for (FenDiModel fdm : list) {
						if (i == fdm.getIndex()) {
							exist = true;
							break;
						}
					}
				}
				if (!exist) {
					tempList.add(i);
				}
			}
			if (tempList.size() < 10) {
				player.sendError(Translate.剩余格子不足10个);
				return null;
			}
			int count = 0;
			while(result.size() < 10 && count < 50) {
				int temp = player.random.nextInt(tempList.size());
				if (!result.contains(tempList.get(temp))) {
					result.add(tempList.get(temp));
				}
			}
			if (result.size() < 10) {
				for (int i=0; i<tempList.size(); i++) {
					if (!result.contains(tempList.get(i))) {
						result.add(tempList.get(i));
					}
					if (result.size() >= 10) {
						return result;
					}
				}
			}
			return result;
		}
		return null;
	}
	/**
	 * 获取单个坟墓挖取的次数
	 * @param fenmuName
	 * @return
	 */
	public int getDigTimes(String fenmuName) {
		List<FenDiModel> list = openFendiMaps.get(fenmuName);
		if (list == null) {
			return 0;
		}
		return list.size();
	}
	/**
	 * 是否可以挖某个坟墓 (不分共有私有)
	 * @param fenmuName
	 * @return
	 */
	public boolean canDig(String fenmuName) {
		FenMuModel fm = WaFenManager.instance.fenmuMap.get(fenmuName);
		if (fm.getId() == 1) {			//第一个坟墓默认可挖
			return true;
		}
		if (fm != null) {
			for (FenMuModel fmm : WaFenManager.instance.fenmuMap.values()) {
				if (fmm.getId() == (fm.getId() - 1)) {			//之前一个坟墓,检查是否挖完
					List<FenDiModel> list = openFendiMaps.get(fmm.getName());
					if (list != null && list.size() >= fmm.getTotalNum()) {		//全部挖完才可以挖下一个坟墓
						return true;
					}
					break;
				}
			}
		}
		return false;
	}
	
	/**
	 * 是否挖开所有私有坟墓
	 * @return
	 */
	public boolean isAllOpened() {
		Iterator<String> ite = WaFenManager.instance.fenmuMap.keySet().iterator();
		int totalTimes = 0;
		while (ite.hasNext()) {
			String key = ite.next();
			FenMuModel fm = WaFenManager.instance.fenmuMap.get(key);
			if (fm.getShareType() == 0) {
				totalTimes += fm.getTotalNum();
			}
		}
		if (times >= totalTimes) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取特定坟墓特定物品已经获取过的次数
	 * @param fenmuId
	 * @param fenmuArticleId
	 * @return
	 */
	public int getOpenTimesById(String fenmuId, int fenmuArticleId) {
		List<FenDiModel> list = getOpenFendiMaps().get(fenmuId);
		if (list != null && list.size() > 0) {
			int count = 0;
			for (FenDiModel fdm : list) {
				if (fdm.getArticleId() == fenmuArticleId) {
					count++;
				}
			}
			return count;
		}
		return 0;
	}
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getLeftYinChanzi() {
		return leftYinChanzi;
	}
	public void setLeftYinChanzi(int leftYinChanzi) {
		this.leftYinChanzi = leftYinChanzi;
	}
	public int getLeftJinChanzi() {
		return leftJinChanzi;
	}
	public void setLeftJinChanzi(int leftJinChanzi) {
		this.leftJinChanzi = leftJinChanzi;
	}
	public int getLeftLiuLiChanzi() {
		return leftLiuLiChanzi;
	}
	public void setLeftLiuLiChanzi(int leftLiuLiChanzi) {
		this.leftLiuLiChanzi = leftLiuLiChanzi;
	}

	public Map<String, List<FenDiModel>> getOpenFendiMaps() {
		return openFendiMaps;
	}

	public void setOpenFendiMaps(Map<String, List<FenDiModel>> openFendiMaps) {
		this.openFendiMaps = openFendiMaps;
	}
	
	
}
