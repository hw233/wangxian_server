package com.fy.engineserver.datasource.article.data.props;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.core.PetSubSystem;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PetEggPropsEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.stat.ArticleStatManager;

public class VariationPetProps extends Props {

	private String petEggName;
	private String petEggName_stat;
	private byte generation;
	private byte variation;
	private int[] tianshenSkill;
	private int[] tianshenLvs;
	private int[] tianfuSkill;
//	private int[] tianfuLvs;
	private boolean isopenxiantian;
	private boolean isopenhoutian;

	public String getPetEggName() {
		return petEggName;
	}

	public void setPetEggName(String petEggName) {
		this.petEggName = petEggName;
	}

	public String getPetEggName_stat() {
		return petEggName_stat;
	}

	public void setPetEggName_stat(String petEggNameStat) {
		petEggName_stat = petEggNameStat;
	}

	public byte getGeneration() {
		return generation;
	}

	public void setGeneration(byte generation) {
		this.generation = generation;
	}

	public byte getVariation() {
		return variation;
	}

	public void setVariation(byte variation) {
		this.variation = variation;
	}

	@Override
	public String canUse(Player p) {

		return super.canUse(p);
	}

	@Override
	public boolean use(Game game, Player player, ArticleEntity ae) {

		if (!super.use(game, player, ae)) {
			return false;
		}
		long start = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
		long eggId = ae.getId();
		String articleName = this.petEggName;
		Article article = ArticleManager.getInstance().getArticle(articleName);
		ArticleManager.logger.warn("[使用道具获得变异宠] [" + articleName + "] [" + article + "] [" + (article instanceof PetEggProps) + "]]");
		if (article == null || !(article instanceof PetEggProps)) {
			PetSubSystem.logger.error("[使用道具获得变异宠] [错误：宠物蛋道具错误] [{}] [{}] [{}] [物品id:{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), eggId, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[使用道具获得变异宠] [错误：宠物蛋道具错误] [{}] [{}] [{}] [物品id:{}] [{}ms]", new Object[] { player.getId(), player.getName(), player.getUsername(), eggId, com.fy.engineserver.gametime.SystemTime.currentTimeMillis() - start });
			return false;
		}

		PetManager petManager = PetManager.getInstance();

		Pet pet = petManager.breedingVariationPet((PropsEntity) ae, this, player);
		ArticleManager.logger.warn("[使用道具获得变异宠] [孵化结束]");
		if (pet == null) {
			return false;
		}
		// if (this.generation == 0) {// 如果是一代
		ArticleManager.logger.warn("[使用道具获得变异宠] [成功] [" + player.getLogString() + "]");
		Article a = ArticleManager.getInstance().getArticle(petEggName);
		PetEggPropsEntity pep;
		try {
			pep = (PetEggPropsEntity) ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_PET_MATING, player, 0, 1, true);
			pep.setPetId(pet.getId());
			player.putToKnapsacks(pep, "使用道具获得变异宠");
			ArticleStatManager.addToArticleStat(player, null, pep, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "使用道具获得变异宠", null);
			return true;
		} catch (Exception e) {
			ArticleManager.logger.warn("[" + player.getLogString() + "] [使用道具获得变异宠出错]" + e);
			e.printStackTrace();
		}
		// } else if (this.generation == 1) {// 如果是二代
		// ArticleManager.logger.warn("[使用道具获得变异宠] [二代]");
		// Pet pet = petManager.creatChildPet(generation, variation);
		// int showTrainLevel = petA.getTrainLevel();
		// int trainLevel = petA.getRealTrainLevel();
		// int rarity = petA.getRarity();
		//
		// pet.setTrainLevel(showTrainLevel);
		// pet.setRealTrainLevel(trainLevel);
		// pet.setRarity((byte) rarity);
		//
		// pet.setMaxLevle(petA.getMaxLevle());
		//
		// pet.setCategory(petA.getCategory());
		// pet.setPetSort(petA.getPetSort());
		// pet.setTitle(petA.getTitle());
		//
		// pet.setCareer((byte) (petA.getCareer()));
		// pet.setSex(petA.getSex());
		// pet.setCharacter(petA.getCharacter());
		//
		// pet.setGrowthClass(petA.getGrowthClass());
		//
		// pet.setCategory(petA.getCategory());
		// pet.setPetSort(petA.getPetSort());
		// if (petA.petProps != null) {
		// pet.setAvataRace(petA.petProps.getAvataRace());
		// pet.setAvataSex(petA.petProps.getAvataSex());
		// } else {// 此处代码，繁殖后会复制进阶形象
		// pet.setAvataRace(petA.getAvataRace());
		// pet.setAvataSex(petA.getAvataSex());
		// }
		//
		// pet.setTalent2Skill(petA.talent2Skill);
		// pet.setWuXing(petA.getWuXing());
		//
		// pet.init();
		// pet.setHp(pet.getMaxHP());
		// PetEggPropsEntity pep;
		// Article a = ArticleManager.getInstance().getArticle(petEggName);
		// try {
		// pet = petManager.createPet(pet);
		// pep = (PetEggPropsEntity) ArticleEntityManager.getInstance().createEntity(a, false, ArticleEntityManager.CREATE_REASON_PET_MATING, player, 0, 1, true);
		// pep.setPetId(pet.getId());
		// pep.setColorType(pet.getQuality());
		// player.putToKnapsacks(pep, "使用道具获得变异宠");
		// ArticleStatManager.addToArticleStat(player, null, pep, ArticleStatManager.OPERATION_物品获得和消耗, 0, (byte) 0, 1, "使用道具获得变异宠", null);
		// return true;
		// } catch (Exception e) {
		// ArticleManager.logger.warn("[" + player.getLogString() + "] [使用道具获得变异宠出错]" + e);
		// e.printStackTrace();
		// }
		// }
		return false;
	}

	public int[] getTianshenSkill() {
		return tianshenSkill;
	}

	public void setTianshenSkill(int[] tianshenSkill) {
		this.tianshenSkill = tianshenSkill;
	}

	public int[] getTianfuSkill() {
		return tianfuSkill;
	}

	public void setTianfuSkill(int[] tianfuSkill) {
		this.tianfuSkill = tianfuSkill;
	}

	public boolean isIsopenxiantian() {
		return isopenxiantian;
	}

	public void setIsopenxiantian(boolean isopenxiantian) {
		this.isopenxiantian = isopenxiantian;
	}

	public boolean isIsopenhoutian() {
		return isopenhoutian;
	}

	public void setIsopenhoutian(boolean isopenhoutian) {
		this.isopenhoutian = isopenhoutian;
	}

//	public int[] getTianfuLvs() {
//		return tianfuLvs;
//	}
//
//	public void setTianfuLvs(int[] tianfuLvs) {
//		this.tianfuLvs = tianfuLvs;
//	}

	public int[] getTianshenLvs() {
		return tianshenLvs;
	}

	public void setTianshenLvs(int[] tianshenLvs) {
		this.tianshenLvs = tianshenLvs;
	}
	
	
}
