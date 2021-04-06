package com.fy.engineserver.articleEnchant;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.articleEnchant.model.EnchantModel;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity
public class EnchantData {
	@SimpleId
	private long id;			//装备id
	@SimpleVersion
	private int version;
	/** 装备上的附魔 */
	private List<EnchantAttr> enchants = new ArrayList<EnchantAttr>();
	/** 附魔是否处于锁定状态  (锁定状态不掉耐久，无效果)*/
	private boolean caging = false;
	/** 删除标记为，在物品合成或丢弃 */
	private boolean canDelete = false;
	/**
	 * 掉耐久
	 * @return true 扣除成功切附魔还有耐久   false扣除并删除附魔  需要再扣除时移除附魔效果
	 */
	public boolean lostDurable(Player player, EquipmentEntity ee, int lostNum) {
		if (caging) {				//锁定状态不掉耐久
			return true;
		}
		List<EnchantAttr> needRemove = new ArrayList<EnchantAttr>();
		for (EnchantAttr ea : enchants) {
			if (!ea.lostDuable(lostNum)) {
				needRemove.add(ea);
			}
		}
		for (EnchantAttr ea : needRemove) {
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[附魔消失前]" + player.getPlayerPropsString());
			}
			EnchantEntityManager.instance.unLoadEnchantAttr(player, ee);
			boolean result = enchants.remove(ea);
			EnchantManager.logger.warn("["+player.getLogString4Knap()+"] [附魔耐久消耗完] [删除] [id:"+id+"] [result:" + result + "] [" + ea + "]");
			if (ArticleManager.logger.isWarnEnabled()) {
				ArticleManager.logger.warn("[附魔消失后]" + player.getPlayerPropsString());
			}
			try {
				EnchantModel tempModel = EnchantManager.instance.modelMap.get(ea.getEnchantId());
				EnchantEntityManager.instance.sendEnchantDataStat(player, tempModel.getEnchatName(), ee.getId(), 2, tempModel.getEquiptmentType());
			} catch (Exception e) {
				EnchantEntityManager.logger.warn("[发送统计] [异常]", e);
			}
			return false;
		}
		EnchantEntityManager.em.notifyFieldChange(this, "enchants");
		return true;
	}
	
	public String getLogString() {
		if (enchants != null && enchants.size() > 0) {
			StringBuffer sb = new StringBuffer();
			for (EnchantAttr ea : enchants) {
				sb.append(ea);
			}
			return sb.toString();
		}
		return "";
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public List<EnchantAttr> getEnchants() {
		return enchants;
	}
	public void setEnchants(List<EnchantAttr> enchants) {
		this.enchants = enchants;
	}

	public boolean isLock() {
		return caging;
	}

	public void setLock(boolean lock) {
		this.caging = lock;
		EnchantEntityManager.em.notifyFieldChange(this, "caging");
	}

	public boolean isDelete() {
		return canDelete;
	}

	public void setDelete(boolean delete) {
		this.canDelete = delete;
		EnchantEntityManager.em.notifyFieldChange(this, "canDelete");
	}
	
	
}
