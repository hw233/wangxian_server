package com.fy.engineserver.homestead.cave;

import com.fy.engineserver.core.PetExperienceManager;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetPropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.homestead.faery.service.CaveSubSystem;
import com.fy.engineserver.homestead.faery.service.FaeryConfig;
import com.fy.engineserver.homestead.faery.service.FaeryManager;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

@SimpleEmbeddable
public class CavePethouse extends CaveBuilding implements FaeryConfig {

	/** 宠物仓库 */
	private long[] storePets = new long[FaeryManager.getInstance().getPethouseCfg()[FaeryManager.getInstance().getPethouseCfg().length - 1].getStoreNum()];

	private PetHookInfo[] hookInfos = new PetHookInfo[FaeryManager.getInstance().getPethouseCfg()[FaeryManager.getInstance().getPethouseCfg().length - 1].getHookNum()];

	public CavePethouse() {
		setType(CAVE_BUILDING_TYPE_PETHOUSE);
	}

	/**
	 * 挂机位是否满了
	 * @return
	 */
	public boolean hookFull() {
		int maxNum = FaeryManager.getInstance().getPethouseCfg()[getGrade() - 1].getHookNum();
		int hasNum = 0;
		for (PetHookInfo info : this.getHookInfos()) {
			if (info != null) {
				hasNum++;
			}
		}
		return hasNum >= maxNum;
	}

	/**
	 * 存储位是否满了
	 * @return
	 */
	public boolean storeFull() {
		int maxNum = FaeryManager.getInstance().getPethouseCfg()[getGrade() - 1].getStoreNum();
		int hasNum = 0;
		for (long info : this.getStorePets()) {
			if (info > 0) {
				hasNum++;
			}
		}
		return hasNum >= maxNum;
	}

	/**
	 * 封印的时候返还挂机宠物
	 */
	public synchronized void rebackHookPetBykhatam() {
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn("[封印仙府] [开始退还挂机宠物]");
		}
		if (getHookInfos() != null) {
			for (int i = 0; i < getHookInfos().length; i++) {
				PetHookInfo hookInfo = getHookInfos()[i];
				if (hookInfo != null) {
					long articleId = hookInfo.getArticleId();
					ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(articleId);
					if (ae != null && ae instanceof PetPropsEntity) {
						long petId = ((PetPropsEntity) ae).getPetId();
						Pet pet = PetManager.getInstance().getPet(petId);
						if (pet != null) {
							int hookExp = getCave().getHookExp(hookInfo);
							pet.addExp(hookExp, PetExperienceManager.ADDEXP_REASON_HOOK);
							pet.setHookInfo(null);
							getHookInfos()[i] = null;
							FaeryManager.caveEm.notifyFieldChange(getCave(), "pethouse");
							if (CaveSubSystem.logger.isWarnEnabled()) {
								CaveSubSystem.logger.warn(getCave().getOwnerName() + "[仙府封印] [退还挂机宠物成功] [petId:{}]", new Object[] { petId });
							}
						} else {
							if (CaveSubSystem.logger.isWarnEnabled()) {
								CaveSubSystem.logger.warn(getCave().getOwnerName() + "[仙府封印] [退还挂机宠物失败] [宠物不存在] [petId:{}]", new Object[] { petId });
							}
						}
					} else {
						if (CaveSubSystem.logger.isWarnEnabled()) {
							CaveSubSystem.logger.warn(getCave().getOwnerName() + "[仙府封印] [退还挂机宠物失败] [不是宠物蛋] [articleId:{}]", new Object[] { articleId });
						}
					}
				}
			}
		}
		if (CaveSubSystem.logger.isWarnEnabled()) {
			CaveSubSystem.logger.warn("[封印仙府] [退还挂机宠物] [完毕]");
		}
	}

	public long[] getStorePets() {
		return storePets;
	}

	public void setStorePets(long[] storePets) {
		this.storePets = storePets;
	}

	public PetHookInfo[] getHookInfos() {
		return hookInfos;
	}

	public void setHookInfos(PetHookInfo[] hookInfos) {
		this.hookInfos = hookInfos;
	}

	@Override
	public void modifyName() {
		super.modifyName();
	}
}
