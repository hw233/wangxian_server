package com.fy.engineserver.datasource.article.data.entity;

import com.fy.engineserver.articleProtect.ArticleProtectData;
import com.fy.engineserver.articleProtect.ArticleProtectDataValues;
import com.fy.engineserver.articleProtect.ArticleProtectManager;
import com.fy.engineserver.articleProtect.PlayerArticleProtectData;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.props.Props;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill2.GenericSkillManager;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetFlySkillInfo;
import com.fy.engineserver.sprite.pet.PetFlyState;
import com.fy.engineserver.sprite.pet.PetManager;
import com.fy.engineserver.sprite.pet2.Pet2Manager;
import com.xuanzhi.tools.cache.CacheSizes;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

/**
 * 宠物实体
 * 
 * 
 */
@SimpleEntity
public class PetPropsEntity extends PropsEntity {

	public static final long serialVersionUID = 23545547344354552L;

	private String eggArticle;

	public PetPropsEntity() {
	}

	public PetPropsEntity(long id) {
		super(id);
	}

	private long petId;

	public long getPetId() {
		// if(props != null) {
		// // return Long.parseLong(props.getProperty(PET_ID_KEY));
		//			
		// }
		return this.petId;
		// return -1;
	}

	public void setPetId(long petId) {
		// if(props == null) {
		// props = new Properties();
		// }
		// props.put(PET_ID_KEY, String.valueOf(petId));
		this.petId = petId;
		saveData("petId");
	}

	/**
	 * 重写玩家获得物品描述
	 */
	public String getInfoShow(Player p) {

		StringBuffer sb = new StringBuffer();
		ArticleManager am = ArticleManager.getInstance();
		Props e = (Props) am.getArticle(this.getArticleName());
		if (e == null) {
			// ArticleManager.logger.warn("[查询物品出错][道具]["+this.getArticleName()+"][实体存在物种为空]");
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[查询物品出错][道具][{}][实体存在物种为空]", new Object[] { this.getArticleName() });
			return "";
		}
		PetManager mwm = PetManager.getInstance();
		Pet mw = mwm.getPet(getPetId());
		if (mw == null) {
			Pet2Manager.log.error("PetPropsEntity.getInfoShow: 宠物是null {}", getPetId());
			return "";
		}
		Pet pet = mw;
		for (int i = pet.getStarClass() / 2; --i >= 0;) {
			if (pet.getStarClass() <= 10) {
				// 星星
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='643' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>");
			} else if (pet.getStarClass() <= 16) {
				// 月亮
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='715' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>");
			} else {
				// 太阳
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='787' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>");
			}
			// sb.append("★");
		}
		if (pet.getStarClass() % 2 == 1) {
			if (pet.getStarClass() <= 10) {
				// 星星
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='607' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>\n");
			} else if (pet.getStarClass() <= 16) {
				// 月亮
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='679' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>\n");
			} else {
				// 太阳
				sb.append("<i imagePath='/ui/texture_map1n2.png' rectX='751' rectY='1' rectW='36' rectH='36' width='20' height='20'>").append("</i>\n");
			}
			// sb.append("☆");
		}
		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.等级).append("：").append("</f>");
		if (pet.getLevel() <= 220) {
			sb.append("<f color='0xFFFFFF' size='28'>").append(pet.getLevel()).append("</f>");
		} else {
			sb.append("<f color='0xFFFFFF' size='28'>").append(Translate.仙).append((pet.getLevel() - 220)).append("</f>");
		}
		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.可携带等级).append("：").append("</f>");
		PetFlyState stat = PetManager.getInstance().getPetFlyState(pet.getId(), "宠物实体");

		boolean isxian = false;
		if (stat != null) {
			if (stat.getFlyState() == 1) {
				sb.append("<f color='0xFFFFFF' size='28'>").append(Translate.仙).append("</f>");
				isxian = true;
			}
		}
		if (!isxian) {
			sb.append("<f color='0xFFFFFF' size='28'>").append(pet.getTrainLevel()).append("</f>");
		}
		{
			String colorStrings[] = { "#FFFFFF", "#00FF00", "#0000FF", "#A000FF", "#FFA000" };
			int grade = pet.grade;
			sb.append("\n<f color='");
			if (grade == 1) {
				sb.append(colorStrings[0]);
			} else if (grade == 2) {
				sb.append(colorStrings[1]);
			} else if (grade == 3 || grade == 4) {
				sb.append(colorStrings[2]);
			} else if (grade == 5 || grade == 6) {
				sb.append(colorStrings[3]);
			} else if (grade > 6) {
				sb.append(colorStrings[4]);
			} else {
				sb.append(colorStrings[0]);
			}
			sb.append("' size='28'>");
			sb.append(PetManager.宠物品阶[grade - 1]);
			sb.append(Pet2Manager.getInst().getConfStr("wuXingStr"));
			sb.append(pet.getWuXing());
			sb.append("</f>");
		}

		PlayerArticleProtectData data = ArticleProtectManager.instance.getPlayerData(p);
		if (data != null) {
			ArticleProtectData da = data.getProtectData(this);
			if (da != null) {
				String daString = null;
				if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_Common) {
					daString = Translate.以锁魂;
				} else if (da.getState() == ArticleProtectDataValues.ArticleProtect_State_High) {
					daString = Translate.以高级锁魂;
				}
				if (da.getRemoveTime() > 0) {
					daString = Translate.正在解魂;
					long haveTime = da.getRemoveTime() - System.currentTimeMillis();
					if (haveTime < 0) {
						haveTime = 0;
					}
					daString += ":" + (haveTime / 1000 / 60 / 60) + Translate.小时 + (haveTime / 1000 / 60 % 60) + Translate.分钟;
				}
				if (daString != null) {
					sb.append("\n<f color='0xFFFF00' size='28'>").append(daString).append("</f>");
				}
			}
		}

		if (pet.getGeneration() == 0) {
			sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.一代);
		} else if (pet.getGeneration() == 1) {
			sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.二代);
		}
		if (pet.getVariation() > 0) {
			sb.append(pet.getVariation()).append(Translate.级变异);
		}
		sb.append(Translate.宠物).append("(").append(PetManager.得到宠物性别名(pet.getSex())).append(")").append("</f>");
		sb.append("\n");

		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.繁殖次数).append("：").append("</f>");
		sb.append("<f color='0xFFFFFF' size='28'>").append(pet.getBreedTimes()).append("</f>");

		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.稀有度).append("：").append("</f>");
		sb.append("<f color='").append(PetManager.得到宠物稀有度颜色值(pet.getRarity())).append("'>").append(PetManager.得到宠物稀有度名(pet.getRarity())).append("</f>");

		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.成长率).append("：").append("</f>");
		sb.append("<f color='").append(PetManager.得到宠物成长品质颜色值(pet.isIdentity(), pet.getGrowthClass())).append("'>").append(PetManager.得到宠物成长品质名(pet.isIdentity(), pet.getGrowthClass())).append("</f>");

		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.品质).append("：").append("</f>");
		sb.append("<f color='").append(PetManager.得到宠物品质颜色值(pet.isIdentity(), pet.getQuality())).append("'>").append(PetManager.得到宠物品质名(pet.isIdentity(), pet.getQuality())).append("</f>");
		sb.append("\n");
		if (pet.getOrnaments()[0] > 0) {
			ArticleEntity aee = ArticleEntityManager.getInstance().getEntity(pet.getOrnaments()[0]);
			if (aee != null) {
				Article  aaa = ArticleManager.getInstance().getArticle(aee.getArticleName());
				int colorStr = 0x00FF00;
				if (aaa != null) {
					colorStr = ArticleManager.getColorValue(aaa, aee.getColorType());
				}
				sb.append("\n<f color='").append(colorStr).append("' size='28'>").append(Translate.宠物饰品).append(aee.getArticleName()).append("</f>");
				sb.append("\n");
			}
		}
		
		for (int i = 0; i < 5; i++) {
			switch (i) {
			case 0:
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.力量).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getStrengthA());
				if (pet.getStrengthS() > 0) {
					sb.append(" + ").append(pet.getStrengthS());
				}
				sb.append("</f>");
				break;
			case 1:
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.身法).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getDexterityA());
				if (pet.getDexterityS() > 0) {
					sb.append(" + ").append(pet.getDexterityS());
				}
				sb.append("</f>");
				break;
			case 2:
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.灵力).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getSpellpowerA());
				if (pet.getSpellpowerS() > 0) {
					sb.append(" + ").append(pet.getSpellpowerS());
				}
				sb.append("</f>");
				break;
			case 3:
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.耐力).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getConstitutionA());
				if (pet.getConstitutionS() > 0) {
					sb.append(" + ").append(pet.getConstitutionS());
				}
				sb.append("</f>");
				break;
			case 4:
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.定力).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getDingliA());
				if (pet.getDingliS() > 0) {
					sb.append(" + ").append(pet.getDingliS());
				}
				sb.append("</f>");
				break;
			}
		}
		sb.append("\n");
		for (int i = 0; i < 5; i++) {
			switch (i) {
			case 0:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.力量).append(Translate.资质).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getShowStrengthQuality()).append("</f>");
				break;
			case 1:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.身法).append(Translate.资质).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getShowDexterityQuality()).append("</f>");
				break;
			case 2:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.灵力).append(Translate.资质).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getShowSpellpowerQuality()).append("</f>");
				break;
			case 3:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.耐力).append(Translate.资质).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getShowConstitutionQuality()).append("</f>");
				break;
			case 4:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.定力).append(Translate.资质).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getShowDingliQuality()).append("</f>");
				break;
			}
		}
		for (int i = 0; i < 7; i++) {
			switch (i) {
			case 0:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.物理攻击).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getPhyAttack()).append("</f>");
				break;
			case 1:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.法术攻击).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getMagicAttack()).append("</f>");
				break;
			case 2:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.物理防御).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getPhyDefence()).append("</f>");
				break;
			case 3:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.法术防御).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getMagicDefence()).append("</f>");
				break;
			case 4:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.命中).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getHit()).append("(").append(pet.getHitRate()).append("%)").append("</f>");
				break;
			case 5:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.闪躲).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getDodge()).append("(").append(pet.getDodgeRate()).append("%)").append("</f>");
				break;
			case 6:
				sb.append("\n<f color='0x00e4ff' size='28'>").append(Translate.暴击).append("：").append("</f>").append("<f color='0xffffff'>").append(pet.getCriticalHit()).append("(").append(pet.getCriticalHitRate()).append("%)").append("</f>");
				break;
			}
		}
		sb.append("\n");
		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.性格).append("：").append("</f>").append("<f color='0xFFFFFF' size='28'>").append(PetManager.得到宠物性格名(pet.getCharacter())).append("</f>");
		sb.append("\n");

		CareerManager cm = CareerManager.getInstance();
		int skId = pet.talent1Skill;
		boolean has = false;
		if (skId > 0) {
			Skill skill = GenericSkillManager.getInst().maps.get(skId);
			if (skill != null) {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.先天技能).append("</f>");
				sb.append(skill.getName());
				has = true;
			} else {
				PetManager.logger.error("PetEggPropsEntity.getInfoShow: miss id {}", skId);
			}
		}
		skId = pet.talent2Skill;
		if (skId > 0) {
			Skill skill = GenericSkillManager.getInst().maps.get(skId);
			if (skill != null) {
				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.后天技能).append("</f>");
				sb.append(skill.getName());
				has = true;
			} else {
				PetManager.logger.error("PetEggPropsEntity.getInfoShow: miss id {}", skId);
			}
		}
//		int len = pet.tianFuSkIds == null ? 0 : pet.tianFuSkIds.length;
//		for (int i = 0; i < len; i++) {
//			skId = pet.tianFuSkIds[i];
//			Skill skill = GenericSkillManager.getInst().maps.get(skId);
//			if (skill != null) {
//				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.天赋技能).append("</f>");
//				sb.append(skill.getName());
//				has = true;
//			} else {
//				PetManager.logger.error("PetEggPropsEntity.getInfoShow: miss id {}", skId);
//			}
//		}
		if (pet.getSkillIds() != null && pet.getSkillIds().length > 0) {

			int i = 0;
			if (cm != null) {
				int[] activeSkillLevels = pet.getActiveSkillLevels();
				for (int skillId : pet.getSkillIds()) {
					Skill skill = cm.getSkillById(skillId);
					if (skill != null) {
						sb.append("\n<f color='0x00FF00' size='28'>").append("基础" + PetEggPropsEntity.skillSt[i]).append("</f>");
						sb.append(skill.getName());
						sb.append(" Lv" + ((activeSkillLevels == null || i >= activeSkillLevels.length) ? 1 : activeSkillLevels[i]));
						has = true;
						++i;
					}
				}
			}

		}
		if (!has) {
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.无技能).append("</f>");
		}
		sb.append("\n<f color='0xFFFFFF' size='28'>").append("</f>");
		return sb.toString();
	}

	public String getInfoShowExtra(Player p) {
		StringBuffer sb = new StringBuffer();
		ArticleManager am = ArticleManager.getInstance();
		Props e = (Props) am.getArticle(this.getArticleName());
		if (e == null) {
			if (ArticleManager.logger.isWarnEnabled()) ArticleManager.logger.warn("[查询物品出错][道具][{}][实体存在物种为空]", new Object[] { this.getArticleName() });
			return "";
		}
		PetManager mwm = PetManager.getInstance();
		Pet pet = mwm.getPet(getPetId());
		if (pet == null) {
			Pet2Manager.log.error("PetPropsEntity.getInfoShow: 宠物是null {}", getPetId());
			return "";
		}
		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.宠物官方名称).append("：").append(articleName).append("</f>");
		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.血量).append("：").append(pet.getHp()).append("</f>");
		sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.幻化等级).append("：").append(pet.getStarClass()).append("</f>");
		PetFlyState state = PetManager.getInstance().getPetFlyState(getPetId(), "飞升技能");
		if (state != null) {
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.灵性).append("：").append(state.getLingXingPoint()).append("</f>");
			sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.飞升技能).append("：").append("</f>");
			if (state.getSkillId() > 0) {
				PetFlySkillInfo skillInfo = PetManager.petFlySkills.get(state.getSkillId());
				if (skillInfo != null) {
					sb.append(skillInfo.getSkillDesc());
				}
			}
		}
		return sb.toString();
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		int size = super.getSize();
		size += CacheSizes.sizeOfInt(); // leftUsingTimes
		return size;
	}

	public String getEggArticle() {
		return eggArticle;
	}

	public void setEggArticle(String eggArticle) {
		this.eggArticle = eggArticle;
		saveData("eggArticle");
	}

}
