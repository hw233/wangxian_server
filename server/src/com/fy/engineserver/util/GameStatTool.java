package com.fy.engineserver.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntity;
import com.fy.engineserver.activity.TransitRobbery.TransitRobberyEntityManager;
import com.fy.engineserver.datasource.article.concrete.ReadEquipmentExcelManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.articles.QiLingArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.NewMagicWeaponEntity;
import com.fy.engineserver.datasource.article.data.entity.QiLingArticleEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.HorseEquipmentColumn;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeapon;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponConstant;
import com.fy.engineserver.datasource.article.data.magicweapon.MagicWeaponManager;
import com.fy.engineserver.datasource.article.data.magicweapon.huntLife.HuntLifeManager;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponAttrModel;
import com.fy.engineserver.datasource.article.data.magicweapon.model.MagicWeaponBaseModel;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.career.CareerManager;
import com.fy.engineserver.datasource.skill.Skill;
import com.fy.engineserver.datasource.skill.master.SkBean;
import com.fy.engineserver.datasource.skill.master.SkEnhanceManager;
import com.fy.engineserver.jiazu2.instance.JiazuMember2;
import com.fy.engineserver.jiazu2.instance.JiazuXiulian;
import com.fy.engineserver.jiazu2.manager.JiazuEntityManager2;
import com.fy.engineserver.jiazu2.manager.JiazuManager2;
import com.fy.engineserver.jiazu2.model.PracticeModel;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.Soul;
import com.fy.engineserver.sprite.horse.Horse;
import com.fy.engineserver.sprite.horse.HorseManager;
import com.fy.engineserver.sprite.horse2.manager.Horse2Manager;
import com.fy.engineserver.sprite.horse2.model.HorseRankModel;
import com.fy.engineserver.sprite.pet.Pet;
import com.fy.engineserver.sprite.pet.PetManager;

public class GameStatTool {

	public static Map<Long, RowData> map = new Hashtable<Long, RowData>();

	public static RowData getRowData(Player player) {
		RowData d = new RowData();
		d.角色等级 = player.getLevel();
		d.人数 = 1;
		TransitRobberyEntity transitRobberyEntity = TransitRobberyEntityManager.getInstance().getTransitRobberyEntity(player.getId());
		if (transitRobberyEntity != null) d.渡劫 = transitRobberyEntity.getCurrentLevel();
		SkBean findSkBean = SkEnhanceManager.getInst().findSkBean(player);
		int total = 0;
		if (findSkBean != null) {
			byte[] levels = findSkBean.getLevels();
			for (int i = 0; i < levels.length; i++) {
				if (levels[i] != -1) {
					total += levels[i];
				}
			}
		}
		d.大师技能等级 = total;
		d.充值金额 = player.getRMB() / 100;
		d.元神开启 = player.getUnusedSoul().size() > 0 ? 1 : 0;

		if (d.元神开启 == 0) d.元神等级 = 0;
		else d.元神等级 = (long) (player.getSoul(Soul.SOUL_TYPE_SOUL).getGrade());

		if (d.元神开启 == 0) d.元神加成比 = 0;
		else d.元神加成比 = (float) (0.2 + player.getUpgradeNums() * 0.01);

		EquipmentColumn ec = player.getSoul(Soul.SOUL_TYPE_BASE).getEc();
		EquipmentEntity ee = null;
		Equipment article = null;
		int colorType = 0;
		int endowments = 0;
		{
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.武器.数量 = ee == null ? 0 : 1;
				d.本尊装备.武器.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.武器.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.武器.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.武器.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.武器.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.武器.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.武器.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.武器.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.武器.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.武器.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.武器.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.武器.鉴定.put("未鉴定", 1L);
				} else if (endowments == 1) {
					d.本尊装备.武器.鉴定.put("普通", 1L);
				} else if (endowments == 2) {
					d.本尊装备.武器.鉴定.put("一般", 1L);
				} else if (endowments == 3) {
					d.本尊装备.武器.鉴定.put("优秀", 1L);
				} else if (endowments == 4) {
					d.本尊装备.武器.鉴定.put("卓越", 1L);
				} else if (endowments == 5) {
					d.本尊装备.武器.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.武器.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.武器.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.武器.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.武器.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				
				if(ee.isEnchant()){
					d.本尊附魔.武器附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.本尊附魔.武器附魔蓝色占比 = 1;
				}
			}
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_head);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.头部.数量 = ee == null ? 0 : 1;
				d.本尊装备.头部.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.头部.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.头部.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.头部.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.头部.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.头部.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.头部.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.头部.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.头部.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.头部.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.头部.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.头部.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.头部.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.头部.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.头部.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.头部.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.头部.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.头部.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.头部.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.头部.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				if(ee.isEnchant()){
					d.本尊附魔.头部附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.本尊附魔.头盔附魔蓝色占比 = 1;
				}
			}

			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.饰品.数量 = ee == null ? 0 : 1;
				d.本尊装备.饰品.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.饰品.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.饰品.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.饰品.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.饰品.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.饰品.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.饰品.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.饰品.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.饰品.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.饰品.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.饰品.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.饰品.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.饰品.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.饰品.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.饰品.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.饰品.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.饰品.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.饰品.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.饰品.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.饰品.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}

			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.腰带.数量 = ee == null ? 0 : 1;
				d.本尊装备.腰带.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.腰带.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.腰带.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.腰带.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.腰带.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.腰带.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.腰带.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.腰带.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.腰带.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.腰带.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.腰带.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.腰带.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.腰带.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.腰带.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.腰带.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.腰带.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.腰带.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.腰带.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.腰带.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.腰带.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.项链.数量 = ee == null ? 0 : 1;
				d.本尊装备.项链.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.项链.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.项链.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.项链.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.项链.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.项链.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.项链.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.项链.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.项链.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.项链.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.项链.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.项链.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.项链.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.项链.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.项链.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.项链.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.项链.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.项链.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.项链.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.项链.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}

			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.鞋子.数量 = ee == null ? 0 : 1;
				d.本尊装备.鞋子.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.鞋子.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.鞋子.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.鞋子.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.鞋子.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.鞋子.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.鞋子.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.鞋子.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.鞋子.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.鞋子.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.鞋子.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.鞋子.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.鞋子.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.鞋子.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.鞋子.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.鞋子.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.鞋子.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.鞋子.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.鞋子.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.鞋子.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.戒指.数量 = ee == null ? 0 : 1;
				d.本尊装备.戒指.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.戒指.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.戒指.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.戒指.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.戒指.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.戒指.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.戒指.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.戒指.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.戒指.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.戒指.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.戒指.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.戒指.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.戒指.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.戒指.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.戒指.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.戒指.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.戒指.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.戒指.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.戒指.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.戒指.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				
				if(ee.isEnchant()){
					d.本尊附魔.戒指附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.本尊附魔.戒指附魔蓝色占比 = 1;
				}
			}

			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.护腕.数量 = ee == null ? 0 : 1;
				d.本尊装备.护腕.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.护腕.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.护腕.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.护腕.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.护腕.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.护腕.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.护腕.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.护腕.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.护腕.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.护腕.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.护腕.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.护腕.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.护腕.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.护腕.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.护腕.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.护腕.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.护腕.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.护腕.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.护腕.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.护腕.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.肩部.数量 = ee == null ? 0 : 1;
				d.本尊装备.肩部.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.肩部.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.肩部.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.肩部.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.肩部.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.肩部.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.肩部.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.肩部.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.肩部.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.肩部.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.肩部.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.肩部.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.肩部.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.肩部.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.肩部.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.肩部.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.肩部.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.肩部.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.肩部.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.肩部.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_body);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.本尊装备.胸部.数量 = ee == null ? 0 : 1;
				d.本尊装备.胸部.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.本尊装备.胸部.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.本尊装备.胸部.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.本尊装备.胸部.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.本尊装备.胸部.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.本尊装备.胸部.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.本尊装备.胸部.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.本尊装备.胸部.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.本尊装备.胸部.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.本尊装备.胸部.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.本尊装备.胸部.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.本尊装备.胸部.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.本尊装备.胸部.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.本尊装备.胸部.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.本尊装备.胸部.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.本尊装备.胸部.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.本尊装备.胸部.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.本尊装备.胸部.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.本尊装备.胸部.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.本尊装备.胸部.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				if(ee.isEnchant()){
					d.本尊附魔.胸甲附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.本尊附魔.胸甲附魔蓝色占比 = 1;
				}
			}
			
		}

		//
		{
			for (ArticleEntity eec1 : ec.getEquipmentArrayByCopy()) {
				if (eec1 == null || eec1 instanceof NewMagicWeaponEntity) {
					continue;
				}
				EquipmentEntity eec = (EquipmentEntity) eec1;
				long[] inlayArticleIds = eec.getInlayArticleIds();
				int[] inlayArticleColors = eec.getInlayArticleColors();
				for (int i = 0; i < inlayArticleColors.length; i++) {
					switch (inlayArticleColors[i]) {
					// 0 绿色 1 橙色 2 黑色 3 蓝色 4 红色 5 白色 6 黄色 7 紫色
					case 0:
						d.本尊宝石.宝石竹清.孔数 += 1;
						break;
					case 1:
						d.本尊宝石.宝石幽橘.孔数 += 1;
						d.本尊宝石.宝石枳香.孔数 += 1;
						break;
					case 2:
						d.本尊宝石.宝石墨轮.孔数 += 1;
						break;
					case 3:
						d.本尊宝石.宝石湛天.孔数 += 1;
						break;
					case 4:
						d.本尊宝石.宝石炎焚.孔数 += 1;
						d.本尊宝石.宝石焚融.孔数 += 1;
						d.本尊宝石.宝石焚天.孔数 += 1;
						d.本尊宝石.宝石焚荒.孔数 += 1;
						d.本尊宝石.宝石焚焱.孔数 += 1;
						break;
					case 5:
						d.本尊宝石.宝石混沌.孔数 += 1;

						d.本尊宝石.宝石寒冰.孔数 += 1;
						d.本尊宝石.宝石寒霜.孔数 += 1;
						d.本尊宝石.宝石寒雨.孔数 += 1;
						d.本尊宝石.宝石寒颤.孔数 += 1;
						break;
					case 6:
						d.本尊宝石.宝石无相.孔数 += 1;

						d.本尊宝石.宝石离火.孔数 += 1;
						d.本尊宝石.宝石朔风.孔数 += 1;
						d.本尊宝石.宝石狂风.孔数 += 1;
						d.本尊宝石.宝石暴风.孔数 += 1;
						break;
					case 7:
						d.本尊宝石.宝石魔渊.孔数 += 1;

						d.本尊宝石.宝石雷霆.孔数 += 1;
						d.本尊宝石.宝石雷击.孔数 += 1;
						d.本尊宝石.宝石雷鸣.孔数 += 1;
						d.本尊宝石.宝石雷诸.孔数 += 1;
						break;
					default:
						break;
					}
				}
				for (long baoshiId : inlayArticleIds) {
					if (baoshiId > 0) {
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(baoshiId);
						if (ae != null) {
							InlayArticle a = (InlayArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
							int ii = a.getName_stat().indexOf("(");
							if (ii <= 0) {									//报错，神匣原因
								continue;
							}
							String baseName = a.getName_stat();
							if(a.getName_stat().contains("(")){
								baseName = a.getName_stat().substring(0, a.getName_stat().indexOf("("));
							}
							if (baseName.contains("宝石竹清")) {
								d.本尊宝石.宝石竹清.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石竹清.镶嵌 += 1;
							} else if (baseName.contains("宝石枳香")) {
								d.本尊宝石.宝石枳香.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石枳香.镶嵌 += 1;
							} else if (baseName.contains("宝石幽橘")) {
								d.本尊宝石.宝石幽橘.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石幽橘.镶嵌 += 1;
							} else if (baseName.contains("宝石墨轮")) {
								d.本尊宝石.宝石墨轮.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石墨轮.镶嵌 += 1;
							} else if (baseName.contains("宝石湛天")) {
								d.本尊宝石.宝石湛天.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石湛天.镶嵌 += 1;
							} else if (baseName.contains("宝石炎焚")) {
								d.本尊宝石.宝石炎焚.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石炎焚.镶嵌 += 1;
							} else if (baseName.contains("宝石焚融")) {
								d.本尊宝石.宝石焚融.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石焚融.镶嵌 += 1;
							} else if (baseName.contains("宝石焚天")) {
								d.本尊宝石.宝石焚天.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石焚天.镶嵌 += 1;
							} else if (baseName.contains("宝石焚荒")) {
								d.本尊宝石.宝石焚荒.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石焚荒.镶嵌 += 1;
							} else if (baseName.contains("宝石焚焱")) {
								d.本尊宝石.宝石焚焱.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石焚焱.镶嵌 += 1;
							} else if (baseName.contains("宝石混沌")) {
								d.本尊宝石.宝石混沌.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石混沌.镶嵌 += 1;
							} else if (baseName.contains("宝石寒冰")) {
								d.本尊宝石.宝石寒冰.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石寒冰.镶嵌 += 1;
							} else if (baseName.contains("宝石寒霜")) {
								d.本尊宝石.宝石寒霜.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石寒霜.镶嵌 += 1;
							} else if (baseName.contains("宝石寒雨")) {
								d.本尊宝石.宝石寒雨.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石寒雨.镶嵌 += 1;
							} else if (baseName.contains("宝石寒颤")) {
								d.本尊宝石.宝石寒颤.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石寒颤.镶嵌 += 1;
							} else if (baseName.contains("宝石无相")) {
								d.本尊宝石.宝石无相.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石无相.镶嵌 += 1;
							} else if (baseName.contains("宝石离火")) {
								d.本尊宝石.宝石离火.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石离火.镶嵌 += 1;
							} else if (baseName.contains("宝石朔风")) {
								d.本尊宝石.宝石朔风.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石朔风.镶嵌 += 1;
							} else if (baseName.contains("宝石狂风")) {
								d.本尊宝石.宝石狂风.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石狂风.镶嵌 += 1;
							} else if (baseName.contains("宝石暴风")) {
								d.本尊宝石.宝石暴风.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石暴风.镶嵌 += 1;
							} else if (baseName.contains("宝石魔渊")) {
								d.本尊宝石.宝石魔渊.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石魔渊.镶嵌 += 1;
							} else if (baseName.contains("宝石雷霆")) {
								d.本尊宝石.宝石雷霆.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石雷霆.镶嵌 += 1;
							} else if (baseName.contains("宝石雷击")) {
								d.本尊宝石.宝石雷击.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石雷击.镶嵌 += 1;
							} else if (baseName.contains("宝石雷鸣")) {
								d.本尊宝石.宝石雷鸣.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石雷鸣.镶嵌 += 1;
							} else if (baseName.contains("宝石雷诸")) {
								d.本尊宝石.宝石雷诸.等级 += (a.getArticleLevel() + 1);
								d.本尊宝石.宝石雷诸.镶嵌 += 1;
							}
						}
					}
				}

			}
			// 宝石
		}
		{
			long activePetId = player.getActivePetId();
			Pet pet = null;
			if (activePetId > 0) {
				pet = PetManager.getInstance().getPet(activePetId);
			}
			if (pet != null) {
				d.出战宠物.基本情况.数量 = 1L;
				d.出战宠物.基本情况.等级 = pet.getLevel();
				d.出战宠物.基本情况.携带等级 = pet.getTrainLevel();

				switch (pet.getRarity()) {
				case PetManager.随处可见:

					d.出战宠物.基本情况.稀有度.put("随处可见", 1L);
					break;
				case PetManager.百里挑一:
					d.出战宠物.基本情况.稀有度.put("百里挑一", 1L);

					break;
				case PetManager.千载难逢:
					d.出战宠物.基本情况.稀有度.put("千载难逢", 1L);

					break;
				case PetManager.万年不遇:
					d.出战宠物.基本情况.稀有度.put("万年不遇", 1L);

					break;

				default:
					break;
				}
				// Translate.宠物性格忠诚, Translate.宠物性格精明, Translate.宠物性格谨慎, Translate.宠物性格狡诈
				switch (pet.getCharacter()) {
				case 0:
					d.出战宠物.基本情况.性格倾向.put("忠诚", 1L);
					break;
				case 1:
					d.出战宠物.基本情况.性格倾向.put("精明", 1L);
					break;
				case 2:
					d.出战宠物.基本情况.性格倾向.put("谨慎", 1L);
					break;
				case 3:
					d.出战宠物.基本情况.性格倾向.put("狡猾", 1L);
					break;

				default:
					break;
				}

				byte generation = pet.getGeneration();
				if (generation == 0) {
					d.出战宠物.基本情况.几代.put("一代", 1L);
				} else {
					d.出战宠物.基本情况.几代.put("二代", 1L);
				}
				switch (pet.getVariation()) {
				case 0:

					d.出战宠物.基本情况.变异.put("未变异", 1L);
					break;
				case 1:

					d.出战宠物.基本情况.变异.put("一级变异", 1L);
					break;
				case 2:

					d.出战宠物.基本情况.变异.put("二级变异", 1L);
					break;
				case 3:

					d.出战宠物.基本情况.变异.put("三级变异", 1L);
					break;
				case 4:

					d.出战宠物.基本情况.变异.put("四级变异", 1L);
					break;
				case 5:

					d.出战宠物.基本情况.变异.put("五级变异", 1L);
					break;

				default:
					break;
				}

				float avg = 0;
				for (float value : pet.getRealRandom()) {
					avg += value;
				}
				avg /= pet.getRealRandom().length;

				d.出战宠物.宠物强化.炼妖 = avg;
				d.出战宠物.宠物强化.强化 = pet.getStarClass();
				d.出战宠物.宠物强化.进阶 = pet.grade;
				d.出战宠物.宠物强化.先天技能 = pet.talent1Skill > 0 ? 1 : 0;
				d.出战宠物.宠物强化.后天技能 = pet.talent2Skill > 0 ? 1 : 0;

				int[] skillIds = pet.getSkillIds();

				d.出战宠物.宠物强化.基础技能.put("1级", 0L);
				d.出战宠物.宠物强化.基础技能.put("2级", 0L);
				d.出战宠物.宠物强化.基础技能.put("3级", 0L);
				d.出战宠物.宠物强化.基础技能.put("4级", 0L);
				for (int skillId : skillIds) {
					Skill skill = CareerManager.getInstance().getSkillById(skillId);
					if (skill != null) {
						d.出战宠物.宠物强化.基础技能数量 += 1;
						if (skill.getSkillType() == Skill.SKILL_ACTIVE) {
							d.出战宠物.宠物技能.主动技数量 += 1;
						} else {
							d.出战宠物.宠物技能.被动技数量 += 1;
						}
						// 技能占比:
						int skillLevel = pet.getSkillLevelById(skillId);
						switch (skillLevel) {
						case 1:
							d.出战宠物.宠物强化.基础技能.put("1级", d.出战宠物.宠物强化.基础技能.get("1级") + 1);
							break;
						case 2:
							d.出战宠物.宠物强化.基础技能.put("2级", d.出战宠物.宠物强化.基础技能.get("2级") + 1);
							break;
						case 3:
							d.出战宠物.宠物强化.基础技能.put("3级", d.出战宠物.宠物强化.基础技能.get("3级") + 1);
							break;
						case 4:
							d.出战宠物.宠物强化.基础技能.put("4级", d.出战宠物.宠物强化.基础技能.get("4级") + 1);
							break;
						default:
							break;
						}
					}
				}
				d.出战宠物.宠物强化.基础技能.put("空", (20 - d.出战宠物.宠物强化.基础技能数量) > 0 ? (20 - d.出战宠物.宠物强化.基础技能数量) : 0);

				d.出战宠物.宠物强化.天赋技能.put("初级",0L);
				d.出战宠物.宠物强化.天赋技能.put("高级",0L);
				d.出战宠物.宠物强化.天赋技能.put("终级",0L);
				
				int[] tianfuSkill = pet.getTianFuSkIds();
				if(tianfuSkill != null) {
					for (int i = 0; i < tianfuSkill.length; i++) {
						int skillId = pet.getTianFuSkIds()[i];
						int skillLevel = pet.getTianFuSkIvs()[i];
						if (skillId > 0) {
							Skill skill = CareerManager.getInstance().getSkillById(skillId);
							if (skill != null) {
								d.出战宠物.宠物强化.天赋技能数量 += 1;
	
								switch (skillLevel) {
								case 1:
									d.出战宠物.宠物强化.天赋技能.put("初级", d.出战宠物.宠物强化.天赋技能.get("初级") + 1);
									break;
								case 2:
									d.出战宠物.宠物强化.天赋技能.put("高级", d.出战宠物.宠物强化.天赋技能.get("高级") + 1);
									break;
								case 3:
									d.出战宠物.宠物强化.天赋技能.put("终级", d.出战宠物.宠物强化.天赋技能.get("终级") + 1);
									break;
	
								default:
									break;
								}
							}
						}
					}
				}
				d.出战宠物.宠物强化.天赋技能.put("空", (20 - d.出战宠物.宠物强化.天赋技能数量) > 0 ? (20 - d.出战宠物.宠物强化.天赋技能数量) : 0);

				d.出战宠物.宠物技能.主动技能格 = skillIds.length;
				d.出战宠物.宠物技能.被动技能格 = skillIds.length;

				// TODO 这个要等宠物做完

				// rr = randomDistribute(10);
				// d.出战宠物.宠物技能.主动技能.put("空", (long) (rr[0] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("1级初级", (long) (rr[1] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("1级中级", (long) (rr[2] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("1级高级", (long) (rr[3] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("2级初级", (long) (rr[4] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("2级中级", (long) (rr[5] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("2级高级", (long) (rr[6] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("3级初级", (long) (rr[7] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("3级中级", (long) (rr[8] * d.出战宠物.宠物技能.主动技能格));
				// d.出战宠物.宠物技能.主动技能.put("3级高级", (long) (rr[9] * d.出战宠物.宠物技能.主动技能格));
				// rr = randomDistribute(4);
				// d.出战宠物.宠物技能.被动技能.put("空", (long) (rr[0] * d.出战宠物.宠物技能.被动技能格));
				// d.出战宠物.宠物技能.被动技能.put("1级", (long) (rr[1] * d.出战宠物.宠物技能.被动技能格));
				// d.出战宠物.宠物技能.被动技能.put("2级", (long) (rr[2] * d.出战宠物.宠物技能.被动技能格));
				// d.出战宠物.宠物技能.被动技能.put("3级", (long) (rr[3] * d.出战宠物.宠物技能.被动技能格));
			}
		}
		{
			// 角色技能
			byte[] skillOneLevels = player.getSoul(Soul.SOUL_TYPE_BASE).getSkillOneLevels();

			d.人物技能.本尊进阶技能._41级等级 = skillOneLevels[0];
			d.人物技能.本尊进阶技能._56级等级 = skillOneLevels[1];
			d.人物技能.本尊进阶技能._71级等级 = skillOneLevels[2];
			d.人物技能.本尊进阶技能._86级等级 = skillOneLevels[3];
			d.人物技能.本尊进阶技能._101级等级 = skillOneLevels[4];
			d.人物技能.本尊进阶技能._116级等级 = skillOneLevels[5];
			d.人物技能.本尊进阶技能._131级等级 = skillOneLevels[6];
			d.人物技能.本尊进阶技能._146级等级 = skillOneLevels[7];
			d.人物技能.本尊进阶技能._161级等级 = skillOneLevels[8];
			d.人物技能.本尊进阶技能._176级等级 = skillOneLevels[9];
			d.人物技能.本尊进阶技能._191级等级 = skillOneLevels[10];
			d.人物技能.本尊进阶技能._206级等级 = skillOneLevels[11];

			byte[] xinfaLevels = player.getXinfaLevels();

			d.人物技能.心法.血心法等级 = xinfaLevels[0];
			d.人物技能.心法.外攻心法等级 = xinfaLevels[1];
			d.人物技能.心法.外防心法等级 = xinfaLevels[2];
			d.人物技能.心法.内法心法等级 = xinfaLevels[3];
			d.人物技能.心法.内防心法等级 = xinfaLevels[4];

			// 庚金攻击 离火攻击 葵水攻击 乙木攻击 庚金抗性 离火抗性 葵水抗性 乙木抗性 庚金减抗 离火减抗 葵水减抗 乙木减抗
			d.人物技能.注魂.火攻等级 = xinfaLevels[5];
			d.人物技能.注魂.风攻等级 = xinfaLevels[6];
			d.人物技能.注魂.冰攻等级 = xinfaLevels[7];
			d.人物技能.注魂.雷攻等级 = xinfaLevels[8];

			d.人物技能.注魂.金防等级 = xinfaLevels[9];
			d.人物技能.注魂.火防等级 = xinfaLevels[10];
			d.人物技能.注魂.水防等级 = xinfaLevels[11];
			d.人物技能.注魂.木防等级 = xinfaLevels[12];

			d.人物技能.注魂.火减抗等级 = xinfaLevels[13];
			d.人物技能.注魂.风减抗等级 = xinfaLevels[14];
			d.人物技能.注魂.冰减抗等级 = xinfaLevels[15];
			d.人物技能.注魂.雷减抗等级 = xinfaLevels[16];
		}
		// //
		//
		for (ArticleEntity eec1 : ec.getEquipmentArrayByCopy()) {
			if (eec1 == null || eec1 instanceof NewMagicWeaponEntity) {
				continue;
			}
			EquipmentEntity eec = (EquipmentEntity) eec1;
			long[] ids = eec.getInlayQiLingArticleIds();
			int[] types = eec.getInlayQiLingArticleTypes();
			// Translate.气血器灵, Translate.外攻器灵, Translate.内攻器灵, Translate.外防器灵, Translate.内防器灵,
			// Translate.炎攻器灵, Translate.冰攻器灵, Translate.风攻器灵, Translate.雷攻器灵,
			// Translate.炎防器灵, Translate.水防器灵, Translate.火防器灵, Translate.木防器灵 };
			for (int i = 0; i < ids.length; i++) {
				if (ids[i] > 0) {
					QiLingArticleEntity entity = (QiLingArticleEntity) ArticleEntityManager.getInstance().getEntity(ids[i]);
					if (entity != null) {
						QiLingArticle qiLing = (QiLingArticle) ArticleManager.getInstance().getArticle(entity.getArticleName());
						int pingzhi = entity.getColorType();
						float tunshuzhi = entity.getPropertyValue() * 1.0f / qiLing.getMaxProperty(entity.getColorType());

						switch (types[i]) {
						case 0:
							d.本尊器灵.长生之灵_生命.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.长生之灵_生命.品质.put("白器灵", d.本尊器灵.长生之灵_生命.品质.get("白器灵") + 1L);
								d.本尊器灵.长生之灵_生命.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.长生之灵_生命.品质.put("绿器灵", d.本尊器灵.长生之灵_生命.品质.get("绿器灵") + 1L);
								d.本尊器灵.长生之灵_生命.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.长生之灵_生命.品质.put("蓝器灵", d.本尊器灵.长生之灵_生命.品质.get("蓝器灵") + 1L);
								d.本尊器灵.长生之灵_生命.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.长生之灵_生命.品质.put("紫器灵", d.本尊器灵.长生之灵_生命.品质.get("紫器灵") + 1L);
								d.本尊器灵.长生之灵_生命.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.长生之灵_生命.品质.put("橙器灵", d.本尊器灵.长生之灵_生命.品质.get("橙器灵") + 1L);
								d.本尊器灵.长生之灵_生命.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 1:
							d.本尊器灵.擎天之灵_外攻.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.擎天之灵_外攻.品质.put("白器灵", d.本尊器灵.擎天之灵_外攻.品质.get("白器灵") + 1L);
								d.本尊器灵.擎天之灵_外攻.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.擎天之灵_外攻.品质.put("绿器灵", d.本尊器灵.擎天之灵_外攻.品质.get("绿器灵") + 1L);
								d.本尊器灵.擎天之灵_外攻.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.擎天之灵_外攻.品质.put("蓝器灵", d.本尊器灵.擎天之灵_外攻.品质.get("蓝器灵") + 1L);
								d.本尊器灵.擎天之灵_外攻.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.擎天之灵_外攻.品质.put("紫器灵", d.本尊器灵.擎天之灵_外攻.品质.get("紫器灵") + 1L);
								d.本尊器灵.擎天之灵_外攻.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.擎天之灵_外攻.品质.put("橙器灵", d.本尊器灵.擎天之灵_外攻.品质.get("橙器灵") + 1L);
								d.本尊器灵.擎天之灵_外攻.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 2:
							d.本尊器灵.琼浆之灵_内法.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.琼浆之灵_内法.品质.put("白器灵", d.本尊器灵.琼浆之灵_内法.品质.get("白器灵") + 1L);
								d.本尊器灵.琼浆之灵_内法.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.琼浆之灵_内法.品质.put("绿器灵", d.本尊器灵.琼浆之灵_内法.品质.get("绿器灵") + 1L);
								d.本尊器灵.琼浆之灵_内法.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.琼浆之灵_内法.品质.put("蓝器灵", d.本尊器灵.琼浆之灵_内法.品质.get("蓝器灵") + 1L);
								d.本尊器灵.琼浆之灵_内法.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.琼浆之灵_内法.品质.put("紫器灵", d.本尊器灵.琼浆之灵_内法.品质.get("紫器灵") + 1L);
								d.本尊器灵.琼浆之灵_内法.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.琼浆之灵_内法.品质.put("橙器灵", d.本尊器灵.琼浆之灵_内法.品质.get("橙器灵") + 1L);
								d.本尊器灵.琼浆之灵_内法.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 3:
							d.本尊器灵.金汤之灵_外防.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.金汤之灵_外防.品质.put("白器灵", d.本尊器灵.金汤之灵_外防.品质.get("白器灵") + 1L);
								d.本尊器灵.金汤之灵_外防.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.金汤之灵_外防.品质.put("绿器灵", d.本尊器灵.金汤之灵_外防.品质.get("绿器灵") + 1L);
								d.本尊器灵.金汤之灵_外防.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.金汤之灵_外防.品质.put("蓝器灵", d.本尊器灵.金汤之灵_外防.品质.get("蓝器灵") + 1L);
								d.本尊器灵.金汤之灵_外防.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.金汤之灵_外防.品质.put("紫器灵", d.本尊器灵.金汤之灵_外防.品质.get("紫器灵") + 1L);
								d.本尊器灵.金汤之灵_外防.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.金汤之灵_外防.品质.put("橙器灵", d.本尊器灵.金汤之灵_外防.品质.get("橙器灵") + 1L);
								d.本尊器灵.金汤之灵_外防.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 4:
							d.本尊器灵.罡岚之灵_内防.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.罡岚之灵_内防.品质.put("白器灵", d.本尊器灵.罡岚之灵_内防.品质.get("白器灵") + 1L);
								d.本尊器灵.罡岚之灵_内防.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.罡岚之灵_内防.品质.put("绿器灵", d.本尊器灵.罡岚之灵_内防.品质.get("绿器灵") + 1L);
								d.本尊器灵.罡岚之灵_内防.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.罡岚之灵_内防.品质.put("蓝器灵", d.本尊器灵.罡岚之灵_内防.品质.get("蓝器灵") + 1L);
								d.本尊器灵.罡岚之灵_内防.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.罡岚之灵_内防.品质.put("紫器灵", d.本尊器灵.罡岚之灵_内防.品质.get("紫器灵") + 1L);
								d.本尊器灵.罡岚之灵_内防.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.罡岚之灵_内防.品质.put("橙器灵", d.本尊器灵.罡岚之灵_内防.品质.get("橙器灵") + 1L);
								d.本尊器灵.罡岚之灵_内防.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 5:
							d.本尊器灵.离火之灵_炎焚攻击.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.离火之灵_炎焚攻击.品质.put("白器灵", d.本尊器灵.离火之灵_炎焚攻击.品质.get("白器灵") + 1L);
								d.本尊器灵.离火之灵_炎焚攻击.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.离火之灵_炎焚攻击.品质.put("绿器灵", d.本尊器灵.离火之灵_炎焚攻击.品质.get("绿器灵") + 1L);
								d.本尊器灵.离火之灵_炎焚攻击.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.离火之灵_炎焚攻击.品质.put("蓝器灵", d.本尊器灵.离火之灵_炎焚攻击.品质.get("蓝器灵") + 1L);
								d.本尊器灵.离火之灵_炎焚攻击.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.离火之灵_炎焚攻击.品质.put("紫器灵", d.本尊器灵.离火之灵_炎焚攻击.品质.get("紫器灵") + 1L);
								d.本尊器灵.离火之灵_炎焚攻击.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.离火之灵_炎焚攻击.品质.put("橙器灵", d.本尊器灵.离火之灵_炎焚攻击.品质.get("橙器灵") + 1L);
								d.本尊器灵.离火之灵_炎焚攻击.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 6:
							d.本尊器灵.葵水之灵_葵水攻击.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.葵水之灵_葵水攻击.品质.put("白器灵", d.本尊器灵.葵水之灵_葵水攻击.品质.get("白器灵") + 1L);
								d.本尊器灵.葵水之灵_葵水攻击.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.葵水之灵_葵水攻击.品质.put("绿器灵", d.本尊器灵.葵水之灵_葵水攻击.品质.get("绿器灵") + 1L);
								d.本尊器灵.葵水之灵_葵水攻击.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.葵水之灵_葵水攻击.品质.put("蓝器灵", d.本尊器灵.葵水之灵_葵水攻击.品质.get("蓝器灵") + 1L);
								d.本尊器灵.葵水之灵_葵水攻击.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.葵水之灵_葵水攻击.品质.put("紫器灵", d.本尊器灵.葵水之灵_葵水攻击.品质.get("紫器灵") + 1L);
								d.本尊器灵.葵水之灵_葵水攻击.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.葵水之灵_葵水攻击.品质.put("橙器灵", d.本尊器灵.葵水之灵_葵水攻击.品质.get("橙器灵") + 1L);
								d.本尊器灵.葵水之灵_葵水攻击.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 7:
							d.本尊器灵.飓风之灵_离火攻击.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.飓风之灵_离火攻击.品质.put("白器灵", d.本尊器灵.飓风之灵_离火攻击.品质.get("白器灵") + 1L);
								d.本尊器灵.飓风之灵_离火攻击.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.飓风之灵_离火攻击.品质.put("绿器灵", d.本尊器灵.飓风之灵_离火攻击.品质.get("绿器灵") + 1L);
								d.本尊器灵.飓风之灵_离火攻击.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.飓风之灵_离火攻击.品质.put("蓝器灵", d.本尊器灵.飓风之灵_离火攻击.品质.get("蓝器灵") + 1L);
								d.本尊器灵.飓风之灵_离火攻击.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.飓风之灵_离火攻击.品质.put("紫器灵", d.本尊器灵.飓风之灵_离火攻击.品质.get("紫器灵") + 1L);
								d.本尊器灵.飓风之灵_离火攻击.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.飓风之灵_离火攻击.品质.put("橙器灵", d.本尊器灵.飓风之灵_离火攻击.品质.get("橙器灵") + 1L);
								d.本尊器灵.飓风之灵_离火攻击.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 8:
							d.本尊器灵.惊雷之灵_乙木攻击.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.惊雷之灵_乙木攻击.品质.put("白器灵", d.本尊器灵.惊雷之灵_乙木攻击.品质.get("白器灵") + 1L);
								d.本尊器灵.惊雷之灵_乙木攻击.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.惊雷之灵_乙木攻击.品质.put("绿器灵", d.本尊器灵.惊雷之灵_乙木攻击.品质.get("绿器灵") + 1L);
								d.本尊器灵.惊雷之灵_乙木攻击.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.惊雷之灵_乙木攻击.品质.put("蓝器灵", d.本尊器灵.惊雷之灵_乙木攻击.品质.get("蓝器灵") + 1L);
								d.本尊器灵.惊雷之灵_乙木攻击.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.惊雷之灵_乙木攻击.品质.put("紫器灵", d.本尊器灵.惊雷之灵_乙木攻击.品质.get("紫器灵") + 1L);
								d.本尊器灵.惊雷之灵_乙木攻击.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.惊雷之灵_乙木攻击.品质.put("橙器灵", d.本尊器灵.惊雷之灵_乙木攻击.品质.get("橙器灵") + 1L);
								d.本尊器灵.惊雷之灵_乙木攻击.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 9:
							d.本尊器灵.御火之灵_炎焚防御.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.御火之灵_炎焚防御.品质.put("白器灵", d.本尊器灵.御火之灵_炎焚防御.品质.get("白器灵") + 1L);
								d.本尊器灵.御火之灵_炎焚防御.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.御火之灵_炎焚防御.品质.put("绿器灵", d.本尊器灵.御火之灵_炎焚防御.品质.get("绿器灵") + 1L);
								d.本尊器灵.御火之灵_炎焚防御.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.御火之灵_炎焚防御.品质.put("蓝器灵", d.本尊器灵.御火之灵_炎焚防御.品质.get("蓝器灵") + 1L);
								d.本尊器灵.御火之灵_炎焚防御.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.御火之灵_炎焚防御.品质.put("紫器灵", d.本尊器灵.御火之灵_炎焚防御.品质.get("紫器灵") + 1L);
								d.本尊器灵.御火之灵_炎焚防御.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.御火之灵_炎焚防御.品质.put("橙器灵", d.本尊器灵.御火之灵_炎焚防御.品质.get("橙器灵") + 1L);
								d.本尊器灵.御火之灵_炎焚防御.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 10:
							d.本尊器灵.御冰之灵_葵水防御.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.御冰之灵_葵水防御.品质.put("白器灵", d.本尊器灵.御冰之灵_葵水防御.品质.get("白器灵") + 1L);
								d.本尊器灵.御冰之灵_葵水防御.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.御冰之灵_葵水防御.品质.put("绿器灵", d.本尊器灵.御冰之灵_葵水防御.品质.get("绿器灵") + 1L);
								d.本尊器灵.御冰之灵_葵水防御.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.御冰之灵_葵水防御.品质.put("蓝器灵", d.本尊器灵.御冰之灵_葵水防御.品质.get("蓝器灵") + 1L);
								d.本尊器灵.御冰之灵_葵水防御.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.御冰之灵_葵水防御.品质.put("紫器灵", d.本尊器灵.御冰之灵_葵水防御.品质.get("紫器灵") + 1L);
								d.本尊器灵.御冰之灵_葵水防御.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.御冰之灵_葵水防御.品质.put("橙器灵", d.本尊器灵.御冰之灵_葵水防御.品质.get("橙器灵") + 1L);
								d.本尊器灵.御冰之灵_葵水防御.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 11:
							d.本尊器灵.御风之灵_离金防御.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.御风之灵_离金防御.品质.put("白器灵", d.本尊器灵.御风之灵_离金防御.品质.get("白器灵") + 1L);
								d.本尊器灵.御风之灵_离金防御.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.御风之灵_离金防御.品质.put("绿器灵", d.本尊器灵.御风之灵_离金防御.品质.get("绿器灵") + 1L);
								d.本尊器灵.御风之灵_离金防御.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.御风之灵_离金防御.品质.put("蓝器灵", d.本尊器灵.御风之灵_离金防御.品质.get("蓝器灵") + 1L);
								d.本尊器灵.御风之灵_离金防御.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.御风之灵_离金防御.品质.put("紫器灵", d.本尊器灵.御风之灵_离金防御.品质.get("紫器灵") + 1L);
								d.本尊器灵.御风之灵_离金防御.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.御风之灵_离金防御.品质.put("橙器灵", d.本尊器灵.御风之灵_离金防御.品质.get("橙器灵") + 1L);
								d.本尊器灵.御风之灵_离金防御.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						case 12:
							d.本尊器灵.御雷之灵_乙木防御.镶嵌 += 1;
							switch (pingzhi) {
							case 0:
								d.本尊器灵.御雷之灵_乙木防御.品质.put("白器灵", d.本尊器灵.御雷之灵_乙木防御.品质.get("白器灵") + 1L);
								d.本尊器灵.御雷之灵_乙木防御.白器灵填充 += tunshuzhi;
								break;
							case 1:
								d.本尊器灵.御雷之灵_乙木防御.品质.put("绿器灵", d.本尊器灵.御雷之灵_乙木防御.品质.get("绿器灵") + 1L);
								d.本尊器灵.御雷之灵_乙木防御.绿器灵填充 += tunshuzhi;
								break;
							case 2:
								d.本尊器灵.御雷之灵_乙木防御.品质.put("蓝器灵", d.本尊器灵.御雷之灵_乙木防御.品质.get("蓝器灵") + 1L);
								d.本尊器灵.御雷之灵_乙木防御.蓝器灵填充 += tunshuzhi;
								break;
							case 3:
								d.本尊器灵.御雷之灵_乙木防御.品质.put("紫器灵", d.本尊器灵.御雷之灵_乙木防御.品质.get("紫器灵") + 1L);
								d.本尊器灵.御雷之灵_乙木防御.紫器灵填充 += tunshuzhi;
								break;
							case 4:
								d.本尊器灵.御雷之灵_乙木防御.品质.put("橙器灵", d.本尊器灵.御雷之灵_乙木防御.品质.get("橙器灵") + 1L);
								d.本尊器灵.御雷之灵_乙木防御.橙器灵填充 += tunshuzhi;
								break;
							}
							break;
						}
					}
				}
			}
			for (int i = 0; i < types.length; i++) {
				switch (types[i]) {
				case 0:
					d.本尊器灵.长生之灵_生命.槽数 += 1;
					break;
				case 1:
					d.本尊器灵.擎天之灵_外攻.槽数 += 1;
					break;
				case 2:
					d.本尊器灵.琼浆之灵_内法.槽数 += 1;
					break;
				case 3:
					d.本尊器灵.金汤之灵_外防.槽数 += 1;
					break;
				case 4:
					d.本尊器灵.罡岚之灵_内防.槽数 += 1;
					break;
				case 5:
					d.本尊器灵.离火之灵_炎焚攻击.槽数 += 1;
					break;
				case 6:
					d.本尊器灵.葵水之灵_葵水攻击.槽数 += 1;
					break;
				case 7:
					d.本尊器灵.飓风之灵_离火攻击.槽数 += 1;
					break;
				case 8:
					d.本尊器灵.惊雷之灵_乙木攻击.槽数 += 1;
					break;
				case 9:
					d.本尊器灵.御火之灵_炎焚防御.槽数 += 1;
					break;
				case 10:
					d.本尊器灵.御冰之灵_葵水防御.槽数 += 1;
					break;
				case 11:
					d.本尊器灵.御风之灵_离金防御.槽数 += 1;
					break;
				case 12:
					d.本尊器灵.御雷之灵_乙木防御.槽数 += 1;
					break;
				}
			}
		}
		{

			//本尊法宝
			ArticleEntity ae = ec.get(11);
			if(ae!=null){
				if(ae instanceof NewMagicWeaponEntity){
					NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
					Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
					if(a!=null){
						if(a instanceof MagicWeapon){
							MagicWeapon m = (MagicWeapon)a;
							d.本尊法宝.数量 = 1;
							d.本尊法宝.等级 = a.getArticleLevel();
							d.本尊法宝.品阶 = me.getMcolorlevel();
							d.本尊法宝.加持 = me.getMstar();
							switch (m.getCareertype()) {
								case 0:
									d.本尊法宝.职业类型.put("通用", 1l);
									break;
								case 1:
									d.本尊法宝.职业类型.put("修罗", 1l);							
									break;
								case 2:
									d.本尊法宝.职业类型.put("影魅", 1l);		
									break;
								case 3:
									d.本尊法宝.职业类型.put("仙心", 1l);	
									break;
								case 4:
									d.本尊法宝.职业类型.put("九黎", 1l);	
									break;
							}
							
							switch(me.getColorType()){
								case 0:
									d.本尊法宝.颜色.put("白", 1l);
									break;
								case 1:
									d.本尊法宝.颜色.put("绿", 1l);
									break;
								case 2:
									d.本尊法宝.颜色.put("蓝", 1l);
									break;
								case 3:
									d.本尊法宝.颜色.put("紫", 1l);
									break;
								case 4:
									d.本尊法宝.颜色.put("橙", 1l);
									break;
								case 5:
									d.本尊法宝.颜色.put("金", 1l);
									break;
							}
							
							d.本尊法宝.基础属性.put(me.getBasicpropertyname_stat(), 1l);
							MagicWeaponBaseModel mbm = MagicWeaponManager.instance.mwBaseModel.get(me.getColorType());
							
							if(me.getHideproterty()!=null){
								d.本尊法宝.隐藏属性开启比例 = me.getHideproterty().size();
								d.本尊法宝.可开启隐藏属性最大数 = mbm.getHiddenAttrNum();
								if(me.getHideproterty().size()>0){
									Map<Integer, Integer> map = new HashMap<Integer, Integer>();
									
									d.本尊法宝.属性颜色.put("白属性",0l);
									d.本尊法宝.属性颜色.put("绿属性", 0l);
									d.本尊法宝.属性颜色.put("蓝属性", 0l);
									d.本尊法宝.属性颜色.put("紫属性", 0l);
									d.本尊法宝.属性颜色.put("橙属性", 0l);
									d.本尊法宝.属性颜色.put("金属性", 0l);
									for(int i=0;i<me.getHideproterty().size();i++){
										MagicWeaponAttrModel mm = me.getHideproterty().get(i);
										if(mm!=null){
											if(map.get(mm.getId())==null){
												map.put(mm.getId(), 1);
											}else{
												map.put(mm.getId(), map.get(mm.getId()).intValue()+1);
											}
											
											switch(mm.getColorType()){
											case 0:
												d.本尊法宝.属性颜色.put("白属性", d.本尊法宝.属性颜色.get("白属性") +1);
												break;
											case 1:
												d.本尊法宝.属性颜色.put("绿属性", d.本尊法宝.属性颜色.get("绿属性") +1);
												break;
											case 2:
												d.本尊法宝.属性颜色.put("蓝属性", d.本尊法宝.属性颜色.get("蓝属性") +1);
												break;
											case 3:
												d.本尊法宝.属性颜色.put("紫属性", d.本尊法宝.属性颜色.get("紫属性") +1);
												break;
											case 4:
												d.本尊法宝.属性颜色.put("橙属性", d.本尊法宝.属性颜色.get("橙属性") +1);
												break;
											case 5:
												d.本尊法宝.属性颜色.put("金属性", d.本尊法宝.属性颜色.get("金属性") +1);
												break;
											}
										}
									}
									
									int 二 = 0;
									int 三 = 0;
									int 四 = 0;
									int 五 = 0;
									Iterator<Integer> it = map.keySet().iterator();
									while(it.hasNext()){
										int o = it.next();
										int value = map.get(o);
										if(value==2){
											二 ++;
										}else if(value==3){
											三++;
										}else if(value==4){
											四++;
										}else if(value==5){
											五++;
										}
									}
									
									d.本尊法宝.同属性占比.put("2个", 二*2);
									d.本尊法宝.同属性占比.put("3个", 三*3);
									d.本尊法宝.同属性占比.put("4个", 四*4);
									d.本尊法宝.同属性占比.put("5个", 五*5);
								}
							}
							
						}
					}
				}
			}
		
		}
		// ///
		ArrayList<Long> horseIDs = player.getSoul(Soul.SOUL_TYPE_BASE).getHorseArr();
		Horse maxLevelHorse = null;
		for (int i = 0; i < horseIDs.size(); i++) {
			Horse horse = HorseManager.getInstance().getHorseById(horseIDs.get(i), player);
			if(horse!=null && !horse.isFly()){
				//new add
				d.本尊坐骑.培养 += horse.getRank();
				d.本尊坐骑.血脉  += horse.getOtherData().getBloodStar();
				int color = horse.getColorType();
				switch (color) {
				case 0:
					d.本尊坐骑.品质.put("白", 1L);
					break;
				case 1:
					d.本尊坐骑.品质.put("绿", 1L);
					break;
				case 2:
					d.本尊坐骑.品质.put("蓝", 1L);
					break;
				case 3:
					d.本尊坐骑.品质.put("紫", 1L);
					break;
				case 4:
					d.本尊坐骑.品质.put("橙", 1L);
					break;
				default:
					break;
				}
				
				int[] skillList = horse.getOtherData().getSkillList();
				if(skillList!=null && skillList.length>0){
					for(int j=0;j<skillList.length;j++){
						int id = skillList[j];
						if(Horse2Manager.instance.getSkillType(id)==0){
							d.本尊坐骑.技能.中低级技能总数++;
							d.本尊坐骑.技能.低级技能占比++;
							if(horse.getOtherData().getSkillLevelById(id)==0){
								d.本尊坐骑.技能.一级低级技能占比++;
							}if(horse.getOtherData().getSkillLevelById(id)==1){
								d.本尊坐骑.技能.二级低级技能占比++;
							}if(horse.getOtherData().getSkillLevelById(id)==2){
								d.本尊坐骑.技能.三级低级技能占比++;
							}
						}else if(Horse2Manager.instance.getSkillType(id)==1){
							d.本尊坐骑.技能.中高级技能总数++;
							d.本尊坐骑.技能.高级技能占比++;
							if(horse.getOtherData().getSkillLevelById(id)==0){
								d.本尊坐骑.技能.一级高级技能占比++;
							}if(horse.getOtherData().getSkillLevelById(id)==1){
								d.本尊坐骑.技能.二级高级技能占比++;
							}if(horse.getOtherData().getSkillLevelById(id)==2){
								d.本尊坐骑.技能.三级高级技能占比++;
							}
						}
					}
				}
				
				HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(horse.getRank());
				if(hrm!=null){
					d.本尊坐骑.技能.本尊坐骑已开启技能格总数 = hrm.getOpenSkillNum();
				}
						
				///
			}
			if (horse != null && horse.isFly()) {
				if (horse.isLimitTime()) {
					d.本尊坐骑.临时飞行坐骑数量 += 1;
				} else {
					d.本尊坐骑.永久飞行坐骑数量 += 1;
					Article a = ArticleManager.getInstance().getArticle(horse.getHorseName());
					if (a.getName_stat().indexOf("爱的炫舞") >= 0) {
						d.本尊坐骑.飞行坐骑.put("爱的炫舞", 1L);
					} else if (a.getName_stat().indexOf("浪漫今生") >= 0) {
						d.本尊坐骑.飞行坐骑.put("浪漫今生", 1L);
					} else if (a.getName_stat().indexOf("碧虚青鸾") >= 0) {
						d.本尊坐骑.飞行坐骑.put("碧虚青鸾", 1L);
					} else if (a.getName_stat().indexOf("八卦仙蒲") >= 0) {
						d.本尊坐骑.飞行坐骑.put("八卦仙蒲", 1L);
					} else if (a.getName_stat().indexOf("梦瞳仙鹤") >= 0) {
						d.本尊坐骑.飞行坐骑.put("梦瞳仙鹤", 1L);
					} else if (a.getName_stat().indexOf("乾坤葫芦") >= 0) {
						d.本尊坐骑.飞行坐骑.put("乾坤葫芦", 1L);
					} else if (a.getName_stat().indexOf("金极龙皇") >= 0) {
						d.本尊坐骑.飞行坐骑.put("金极龙皇", 1L);
					} else if (a.getName_stat().indexOf("焚焰火扇") >= 0) {
						d.本尊坐骑.飞行坐骑.put("焚焰火扇", 1L);
					} else if (a.getName_stat().indexOf("深渊魔章") >= 0) {
						d.本尊坐骑.飞行坐骑.put("深渊魔章", 1L);
					}
				}
			} else {
				if(horse!=null){
					d.本尊坐骑.普通坐骑数量 += 1;
					if (maxLevelHorse == null) {
						maxLevelHorse = horse;
					} else {
						if (maxLevelHorse.getRank() < horse.getRank()) {
							maxLevelHorse = horse;
						}
					}
				}
			}
		}
		if (maxLevelHorse != null) {
			HorseEquipmentColumn hec = maxLevelHorse.getEquipmentColumn();
			EquipmentEntity[] hEquEntitys = hec.getEquipmentArrayByCopy();
			for (int j = 0; j < hEquEntitys.length; j++) {
				// Translate.金盔, Translate.剑翅, Translate.腿甲, Translate.配鞍, Translate.鳞甲
				EquipmentEntity hequE = hEquEntitys[j];
				int color = 0;
				if (hequE != null) {
					switch (j) {
					case 0:
						d.本尊坐骑.金盔.数量 = 1;
						d.本尊坐骑.金盔.强化 = hequE.getStar();
						color = hequE.getColorType();
						switch (color) {
						case ArticleManager.equipment_color_白:
							d.本尊坐骑.金盔.颜色.put("白", 1L);
							break;
						case ArticleManager.equipment_color_绿:
							d.本尊坐骑.金盔.颜色.put("绿", 1L);
							break;
						case ArticleManager.equipment_color_蓝:
							d.本尊坐骑.金盔.颜色.put("蓝", 1L);
							break;
						case ArticleManager.equipment_color_紫:
							d.本尊坐骑.金盔.颜色.put("紫", 1L);
							break;
						case ArticleManager.equipment_color_完美紫:
							d.本尊坐骑.金盔.颜色.put("完美紫", 1L);
							break;
						case ArticleManager.equipment_color_橙:
							d.本尊坐骑.金盔.颜色.put("橙", 1L);
							break;
						case ArticleManager.equipment_color_完美橙:
							d.本尊坐骑.金盔.颜色.put("完美橙", 1L);
							break;

						default:
							break;
						}
						break;
					case 1:
						d.本尊坐骑.剑翅.数量 = 1;
						d.本尊坐骑.剑翅.强化 = hequE.getStar();
						color = hequE.getColorType();
						switch (color) {
						case ArticleManager.equipment_color_白:
							d.本尊坐骑.剑翅.颜色.put("白", 1L);
							break;
						case ArticleManager.equipment_color_绿:
							d.本尊坐骑.剑翅.颜色.put("绿", 1L);
							break;
						case ArticleManager.equipment_color_蓝:
							d.本尊坐骑.剑翅.颜色.put("蓝", 1L);
							break;
						case ArticleManager.equipment_color_紫:
							d.本尊坐骑.剑翅.颜色.put("紫", 1L);
							break;
						case ArticleManager.equipment_color_完美紫:
							d.本尊坐骑.剑翅.颜色.put("完美紫", 1L);
							break;
						case ArticleManager.equipment_color_橙:
							d.本尊坐骑.剑翅.颜色.put("橙", 1L);
							break;
						case ArticleManager.equipment_color_完美橙:
							d.本尊坐骑.剑翅.颜色.put("完美橙", 1L);
							break;

						default:
							break;
						}
						break;
					case 2:
						d.本尊坐骑.腿甲.数量 = 1;
						d.本尊坐骑.腿甲.强化 = hequE.getStar();
						color = hequE.getColorType();
						switch (color) {
						case ArticleManager.equipment_color_白:
							d.本尊坐骑.腿甲.颜色.put("白", 1L);
							break;
						case ArticleManager.equipment_color_绿:
							d.本尊坐骑.腿甲.颜色.put("绿", 1L);
							break;
						case ArticleManager.equipment_color_蓝:
							d.本尊坐骑.腿甲.颜色.put("蓝", 1L);
							break;
						case ArticleManager.equipment_color_紫:
							d.本尊坐骑.腿甲.颜色.put("紫", 1L);
							break;
						case ArticleManager.equipment_color_完美紫:
							d.本尊坐骑.腿甲.颜色.put("完美紫", 1L);
							break;
						case ArticleManager.equipment_color_橙:
							d.本尊坐骑.腿甲.颜色.put("橙", 1L);
							break;
						case ArticleManager.equipment_color_完美橙:
							d.本尊坐骑.腿甲.颜色.put("完美橙", 1L);
							break;

						default:
							break;
						}
						break;
					case 3:
						d.本尊坐骑.配鞍.数量 = 1;
						d.本尊坐骑.配鞍.强化 = hequE.getStar();
						color = hequE.getColorType();
						switch (color) {
						case ArticleManager.equipment_color_白:
							d.本尊坐骑.配鞍.颜色.put("白", 1L);
							break;
						case ArticleManager.equipment_color_绿:
							d.本尊坐骑.配鞍.颜色.put("绿", 1L);
							break;
						case ArticleManager.equipment_color_蓝:
							d.本尊坐骑.配鞍.颜色.put("蓝", 1L);
							break;
						case ArticleManager.equipment_color_紫:
							d.本尊坐骑.配鞍.颜色.put("紫", 1L);
							break;
						case ArticleManager.equipment_color_完美紫:
							d.本尊坐骑.配鞍.颜色.put("完美紫", 1L);
							break;
						case ArticleManager.equipment_color_橙:
							d.本尊坐骑.配鞍.颜色.put("橙", 1L);
							break;
						case ArticleManager.equipment_color_完美橙:
							d.本尊坐骑.配鞍.颜色.put("完美橙", 1L);
							break;

						default:
							break;
						}
						break;
					case 4:
						d.本尊坐骑.鳞甲.数量 = 1;
						d.本尊坐骑.鳞甲.强化 = hequE.getStar();
						color = hequE.getColorType();
						switch (color) {
						case ArticleManager.equipment_color_白:
							d.本尊坐骑.鳞甲.颜色.put("白", 1L);
							break;
						case ArticleManager.equipment_color_绿:
							d.本尊坐骑.鳞甲.颜色.put("绿", 1L);
							break;
						case ArticleManager.equipment_color_蓝:
							d.本尊坐骑.鳞甲.颜色.put("蓝", 1L);
							break;
						case ArticleManager.equipment_color_紫:
							d.本尊坐骑.鳞甲.颜色.put("紫", 1L);
							break;
						case ArticleManager.equipment_color_完美紫:
							d.本尊坐骑.鳞甲.颜色.put("完美紫", 1L);
							break;
						case ArticleManager.equipment_color_橙:
							d.本尊坐骑.鳞甲.颜色.put("橙", 1L);
							break;
						case ArticleManager.equipment_color_完美橙:
							d.本尊坐骑.鳞甲.颜色.put("完美橙", 1L);
							break;

						default:
							break;
						}
						break;
					}
				}
			}
		}
		
		// //////////////////////////////////////////////////////////////////////
		Soul yuanshen = player.getSoul(Soul.SOUL_TYPE_SOUL);
		if (yuanshen != null) {
			// 元神
			ec = yuanshen.getEc();
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_weapon);
			if (ee != null) {

				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.武器.数量 = ee == null ? 0 : 1;
				d.元神装备.武器.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.武器.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.武器.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.武器.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.武器.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.武器.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.武器.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.武器.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.武器.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.武器.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.武器.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.武器.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.武器.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.武器.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.武器.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.武器.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.武器.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.武器.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.武器.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.武器.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				
				if(ee.isEnchant()){
					d.元神附魔.武器附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.元神附魔.武器附魔蓝色占比 = 1;
				}
			}

			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_head);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.头部.数量 = ee == null ? 0 : 1;
				d.元神装备.头部.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.头部.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.头部.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.头部.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.头部.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.头部.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.头部.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.头部.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.头部.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.头部.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.头部.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.头部.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.头部.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.头部.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.头部.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.头部.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.头部.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.头部.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.头部.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.头部.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				if(ee.isEnchant()){
					d.元神附魔.头部附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.元神附魔.头盔附魔蓝色占比 = 1;
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_fingerring);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.饰品.数量 = ee == null ? 0 : 1;
				d.元神装备.饰品.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.饰品.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.饰品.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.饰品.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.饰品.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.饰品.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.饰品.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.饰品.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.饰品.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.饰品.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.饰品.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.饰品.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.饰品.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.饰品.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.饰品.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.饰品.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.饰品.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.饰品.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.饰品.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.饰品.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_belt);
			if (ee != null) {

				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.腰带.数量 = ee == null ? 0 : 1;
				d.元神装备.腰带.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.腰带.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.腰带.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.腰带.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.腰带.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.腰带.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.腰带.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.腰带.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.腰带.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.腰带.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.腰带.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.腰带.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.腰带.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.腰带.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.腰带.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.腰带.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.腰带.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.腰带.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.腰带.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.腰带.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_necklace);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.项链.数量 = ee == null ? 0 : 1;
				d.元神装备.项链.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.项链.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.项链.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.项链.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.项链.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.项链.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.项链.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.项链.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.项链.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.项链.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.项链.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.项链.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.项链.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.项链.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.项链.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.项链.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.项链.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.项链.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.项链.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.项链.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_foot);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.鞋子.数量 = ee == null ? 0 : 1;
				d.元神装备.鞋子.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.鞋子.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.鞋子.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.鞋子.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.鞋子.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.鞋子.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.鞋子.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.鞋子.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.鞋子.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.鞋子.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.鞋子.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.鞋子.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.鞋子.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.鞋子.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.鞋子.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.鞋子.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.鞋子.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.鞋子.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.鞋子.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.鞋子.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}

			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_jewelry);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.戒指.数量 = ee == null ? 0 : 1;
				d.元神装备.戒指.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.戒指.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.戒指.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.戒指.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.戒指.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.戒指.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.戒指.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.戒指.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.戒指.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.戒指.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.戒指.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.戒指.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.戒指.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.戒指.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.戒指.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.戒指.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.戒指.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.戒指.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.戒指.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.戒指.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				
				if(ee.isEnchant()){
					d.元神附魔.戒指附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.元神附魔.戒指附魔蓝色占比 = 1;
				}
			}

			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_hand);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.护腕.数量 = ee == null ? 0 : 1;
				d.元神装备.护腕.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.护腕.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.护腕.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.护腕.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.护腕.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.护腕.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.护腕.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.护腕.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.护腕.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.护腕.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.护腕.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.护腕.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.护腕.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.护腕.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.护腕.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.护腕.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.护腕.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.护腕.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.护腕.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.护腕.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_shoulder);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.肩部.数量 = ee == null ? 0 : 1;
				d.元神装备.肩部.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.肩部.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.肩部.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.肩部.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.肩部.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.肩部.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.肩部.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.肩部.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.肩部.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.肩部.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.肩部.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.肩部.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.肩部.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.肩部.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.肩部.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.肩部.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.肩部.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.肩部.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.肩部.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.肩部.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
			}
			//
			ee = (EquipmentEntity) ec.get(EquipmentColumn.EQUIPMENT_TYPE_body);
			if (ee != null) {
				article = ee == null ? null : (Equipment) ArticleManager.getInstance().getArticle(ee.getArticleName());

				d.元神装备.胸部.数量 = ee == null ? 0 : 1;
				d.元神装备.胸部.等级 = ee == null ? 0 : article.getPlayerLevelLimit();
				d.元神装备.胸部.星级 = ee == null ? 0 : (ee.getStar() >= 20 ? 20 : ee.getStar());
				d.元神装备.胸部.羽化 = ee == null ? 0 : (ee.getStar() <= 20) ? 0 : ee.getStar() - 20;
				d.元神装备.胸部.铭刻 = ee == null ? 0 : (ee.isInscriptionFlag() ? 1 : 0);

				colorType = ee.getColorType();
				switch (colorType) {
				case ArticleManager.equipment_color_白:
					d.元神装备.胸部.颜色.put("白", 1L);
					break;
				case ArticleManager.equipment_color_绿:
					d.元神装备.胸部.颜色.put("绿", 1L);
					break;
				case ArticleManager.equipment_color_蓝:
					d.元神装备.胸部.颜色.put("蓝", 1L);
					break;
				case ArticleManager.equipment_color_紫:
					d.元神装备.胸部.颜色.put("紫", 1L);
					break;
				case ArticleManager.equipment_color_完美紫:
					d.元神装备.胸部.颜色.put("完美紫", 1L);
					break;
				case ArticleManager.equipment_color_橙:
					d.元神装备.胸部.颜色.put("橙", 1L);
					break;
				case ArticleManager.equipment_color_完美橙:
					d.元神装备.胸部.颜色.put("完美橙", 1L);
					break;
				default:
					break;
				}

				endowments = ee.getEndowments();
				if (endowments == 0) {
					d.元神装备.胸部.鉴定.put("普通", 1L);
				} else if (endowments == 1) {
					d.元神装备.胸部.鉴定.put("一般", 1L);
				} else if (endowments == 2) {
					d.元神装备.胸部.鉴定.put("优秀", 1L);
				} else if (endowments == 3) {
					d.元神装备.胸部.鉴定.put("卓越", 1L);
				} else if (endowments == 4) {
					d.元神装备.胸部.鉴定.put("完美", 1L);
				} else {
					for (int kk = 0; kk < ReadEquipmentExcelManager.endowmentsNewValues_int.length; kk++) {
						int value = ReadEquipmentExcelManager.endowmentsNewValues_int[kk];
						if (value >= endowments - ArticleManager.newEndowments) {
							if (kk == 1) {
								d.元神装备.胸部.鉴定.put("传奇", 1L);
							} else if (kk == 2) {
								d.元神装备.胸部.鉴定.put("传说", 1L);
							} else if (kk == 3) {
								d.元神装备.胸部.鉴定.put("神话", 1L);
							} else if (kk == 4) {
								d.元神装备.胸部.鉴定.put("永恒", 1L);
							}
							break;
						}
					}
				}
				if(ee.isEnchant()){
					d.元神附魔.胸甲附魔普及率 = 1;
				}
				if(ee.getEnchantColor() == 2){
					d.元神附魔.胸甲附魔蓝色占比 = 1;
				}

			}
		}
		//
		if (yuanshen != null) {
			// 元神宝石
			ec = yuanshen.getEc();

			for (ArticleEntity eec1 : ec.getEquipmentArrayByCopy()) {
				if (eec1 == null || eec1 instanceof NewMagicWeaponEntity) {
					continue;
				}
				EquipmentEntity eec = (EquipmentEntity) eec1;
				long[] inlayArticleIds = eec.getInlayArticleIds();
				int[] inlayArticleColors = eec.getInlayArticleColors();
				for (int i = 0; i < inlayArticleColors.length; i++) {
					switch (inlayArticleColors[i]) {
					// 0 绿色 1 橙色 2 黑色 3 蓝色 4 红色 5 白色 6 黄色 7 紫色
					case 0:
						d.元神宝石.宝石竹清.孔数 += 1;
						break;
					case 1:
						d.元神宝石.宝石幽橘.孔数 += 1;
						d.元神宝石.宝石枳香.孔数 += 1;
						break;
					case 2:
						d.元神宝石.宝石墨轮.孔数 += 1;
						break;
					case 3:
						d.元神宝石.宝石湛天.孔数 += 1;
						break;
					case 4:
						d.元神宝石.宝石炎焚.孔数 += 1;
						d.元神宝石.宝石焚融.孔数 += 1;
						d.元神宝石.宝石焚天.孔数 += 1;
						d.元神宝石.宝石焚荒.孔数 += 1;
						d.元神宝石.宝石焚焱.孔数 += 1;
						break;
					case 5:
						d.元神宝石.宝石混沌.孔数 += 1;

						d.元神宝石.宝石寒冰.孔数 += 1;
						d.元神宝石.宝石寒霜.孔数 += 1;
						d.元神宝石.宝石寒雨.孔数 += 1;
						d.元神宝石.宝石寒颤.孔数 += 1;
						break;
					case 6:
						d.元神宝石.宝石无相.孔数 += 1;

						d.元神宝石.宝石离火.孔数 += 1;
						d.元神宝石.宝石朔风.孔数 += 1;
						d.元神宝石.宝石狂风.孔数 += 1;
						d.元神宝石.宝石暴风.孔数 += 1;
						break;
					case 7:
						d.元神宝石.宝石魔渊.孔数 += 1;

						d.元神宝石.宝石雷霆.孔数 += 1;
						d.元神宝石.宝石雷击.孔数 += 1;
						d.元神宝石.宝石雷鸣.孔数 += 1;
						d.元神宝石.宝石雷诸.孔数 += 1;
						break;
					default:
						break;
					}
				}
				for (long baoshiId : inlayArticleIds) {
					if (baoshiId > 0) {
						ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(baoshiId);
						if (ae != null) {
							InlayArticle a = (InlayArticle) ArticleManager.getInstance().getArticle(ae.getArticleName());
							String baseName = a.getName_stat();
							if(a.getName_stat().contains("(")){
								baseName = a.getName_stat().substring(0, a.getName_stat().indexOf("("));
							}
							if (baseName.contains("宝石竹清")) {
								d.元神宝石.宝石竹清.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石竹清.镶嵌 += 1;
							} else if (baseName.contains("宝石枳香")) {
								d.元神宝石.宝石枳香.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石枳香.镶嵌 += 1;
							} else if (baseName.contains("宝石幽橘")) {
								d.元神宝石.宝石幽橘.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石幽橘.镶嵌 += 1;
							} else if (baseName.contains("宝石墨轮")) {
								d.元神宝石.宝石墨轮.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石墨轮.镶嵌 += 1;
							} else if (baseName.contains("宝石湛天")) {
								d.元神宝石.宝石湛天.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石湛天.镶嵌 += 1;
							} else if (baseName.contains("宝石炎焚")) {
								d.元神宝石.宝石炎焚.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石炎焚.镶嵌 += 1;
							} else if (baseName.contains("宝石焚融")) {
								d.元神宝石.宝石焚融.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石焚融.镶嵌 += 1;
							} else if (baseName.contains("宝石焚天")) {
								d.元神宝石.宝石焚天.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石焚天.镶嵌 += 1;
							} else if (baseName.contains("宝石焚荒")) {
								d.元神宝石.宝石焚荒.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石焚荒.镶嵌 += 1;
							} else if (baseName.contains("宝石焚焱")) {
								d.元神宝石.宝石焚焱.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石焚焱.镶嵌 += 1;
							} else if (baseName.contains("宝石混沌")) {
								d.元神宝石.宝石混沌.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石混沌.镶嵌 += 1;
							} else if (baseName.contains("宝石寒冰")) {
								d.元神宝石.宝石寒冰.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石寒冰.镶嵌 += 1;
							} else if (baseName.contains("宝石寒霜")) {
								d.元神宝石.宝石寒霜.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石寒霜.镶嵌 += 1;
							} else if (baseName.contains("宝石寒雨")) {
								d.元神宝石.宝石寒雨.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石寒雨.镶嵌 += 1;
							} else if (baseName.contains("宝石寒颤")) {
								d.元神宝石.宝石寒颤.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石寒颤.镶嵌 += 1;
							} else if (baseName.contains("宝石无相")) {
								d.元神宝石.宝石无相.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石无相.镶嵌 += 1;
							} else if (baseName.contains("宝石离火")) {
								d.元神宝石.宝石离火.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石离火.镶嵌 += 1;
							} else if (baseName.contains("宝石朔风")) {
								d.元神宝石.宝石朔风.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石朔风.镶嵌 += 1;
							} else if (baseName.contains("宝石狂风")) {
								d.元神宝石.宝石狂风.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石狂风.镶嵌 += 1;
							} else if (baseName.contains("宝石暴风")) {
								d.元神宝石.宝石暴风.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石暴风.镶嵌 += 1;
							} else if (baseName.contains("宝石魔渊")) {
								d.元神宝石.宝石魔渊.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石魔渊.镶嵌 += 1;
							} else if (baseName.contains("宝石雷霆")) {
								d.元神宝石.宝石雷霆.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石雷霆.镶嵌 += 1;
							} else if (baseName.contains("宝石雷击")) {
								d.元神宝石.宝石雷击.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石雷击.镶嵌 += 1;
							} else if (baseName.contains("宝石雷鸣")) {
								d.元神宝石.宝石雷鸣.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石雷鸣.镶嵌 += 1;
							} else if (baseName.contains("宝石雷诸")) {
								d.元神宝石.宝石雷诸.等级 += (a.getArticleLevel() + 1);
								d.元神宝石.宝石雷诸.镶嵌 += 1;
							}
						}
					}
				}

			}
			// 宝石

		}
		if (yuanshen != null) {
			// 元神器灵
			ec = yuanshen.getEc();

			for (ArticleEntity eec1 : ec.getEquipmentArrayByCopy()) {
				if (eec1 == null || eec1 instanceof NewMagicWeaponEntity) {
					continue;
				}
				EquipmentEntity eec = (EquipmentEntity) eec1;
				long[] ids = eec.getInlayQiLingArticleIds();
				int[] types = eec.getInlayQiLingArticleTypes();
				// Translate.气血器灵, Translate.外攻器灵, Translate.内攻器灵, Translate.外防器灵, Translate.内防器灵,
				// Translate.炎攻器灵, Translate.冰攻器灵, Translate.风攻器灵, Translate.雷攻器灵,
				// Translate.炎防器灵, Translate.水防器灵, Translate.火防器灵, Translate.木防器灵 };
				for (int i = 0; i < ids.length; i++) {
					if (ids[i] > 0) {
						QiLingArticleEntity entity = (QiLingArticleEntity) ArticleEntityManager.getInstance().getEntity(ids[i]);
						if (entity != null) {
							QiLingArticle qiLing = (QiLingArticle) ArticleManager.getInstance().getArticle(entity.getArticleName());
							int pingzhi = entity.getColorType();
							float tunshuzhi = entity.getPropertyValue() * 1.0f / qiLing.getMaxProperty(entity.getColorType());

							switch (types[i]) {
							case 0:
								d.元神器灵.长生之灵_生命.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.长生之灵_生命.品质.put("白器灵", d.元神器灵.长生之灵_生命.品质.get("白器灵") + 1L);
									d.元神器灵.长生之灵_生命.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.长生之灵_生命.品质.put("绿器灵", d.元神器灵.长生之灵_生命.品质.get("绿器灵") + 1L);
									d.元神器灵.长生之灵_生命.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.长生之灵_生命.品质.put("蓝器灵", d.元神器灵.长生之灵_生命.品质.get("蓝器灵") + 1L);
									d.元神器灵.长生之灵_生命.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.长生之灵_生命.品质.put("紫器灵", d.元神器灵.长生之灵_生命.品质.get("紫器灵") + 1L);
									d.元神器灵.长生之灵_生命.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.长生之灵_生命.品质.put("橙器灵", d.元神器灵.长生之灵_生命.品质.get("橙器灵") + 1L);
									d.元神器灵.长生之灵_生命.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 1:
								d.元神器灵.擎天之灵_外攻.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.擎天之灵_外攻.品质.put("白器灵", d.元神器灵.擎天之灵_外攻.品质.get("白器灵") + 1L);
									d.元神器灵.擎天之灵_外攻.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.擎天之灵_外攻.品质.put("绿器灵", d.元神器灵.擎天之灵_外攻.品质.get("绿器灵") + 1L);
									d.元神器灵.擎天之灵_外攻.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.擎天之灵_外攻.品质.put("蓝器灵", d.元神器灵.擎天之灵_外攻.品质.get("蓝器灵") + 1L);
									d.元神器灵.擎天之灵_外攻.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.擎天之灵_外攻.品质.put("紫器灵", d.元神器灵.擎天之灵_外攻.品质.get("紫器灵") + 1L);
									d.元神器灵.擎天之灵_外攻.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.擎天之灵_外攻.品质.put("橙器灵", d.元神器灵.擎天之灵_外攻.品质.get("橙器灵") + 1L);
									d.元神器灵.擎天之灵_外攻.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 2:
								d.元神器灵.琼浆之灵_内法.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.琼浆之灵_内法.品质.put("白器灵", d.元神器灵.琼浆之灵_内法.品质.get("白器灵") + 1L);
									d.元神器灵.琼浆之灵_内法.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.琼浆之灵_内法.品质.put("绿器灵", d.元神器灵.琼浆之灵_内法.品质.get("绿器灵") + 1L);
									d.元神器灵.琼浆之灵_内法.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.琼浆之灵_内法.品质.put("蓝器灵", d.元神器灵.琼浆之灵_内法.品质.get("蓝器灵") + 1L);
									d.元神器灵.琼浆之灵_内法.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.琼浆之灵_内法.品质.put("紫器灵", d.元神器灵.琼浆之灵_内法.品质.get("紫器灵") + 1L);
									d.元神器灵.琼浆之灵_内法.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.琼浆之灵_内法.品质.put("橙器灵", d.元神器灵.琼浆之灵_内法.品质.get("橙器灵") + 1L);
									d.元神器灵.琼浆之灵_内法.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 3:
								d.元神器灵.金汤之灵_外防.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.金汤之灵_外防.品质.put("白器灵", d.元神器灵.金汤之灵_外防.品质.get("白器灵") + 1L);
									d.元神器灵.金汤之灵_外防.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.金汤之灵_外防.品质.put("绿器灵", d.元神器灵.金汤之灵_外防.品质.get("绿器灵") + 1L);
									d.元神器灵.金汤之灵_外防.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.金汤之灵_外防.品质.put("蓝器灵", d.元神器灵.金汤之灵_外防.品质.get("蓝器灵") + 1L);
									d.元神器灵.金汤之灵_外防.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.金汤之灵_外防.品质.put("紫器灵", d.元神器灵.金汤之灵_外防.品质.get("紫器灵") + 1L);
									d.元神器灵.金汤之灵_外防.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.金汤之灵_外防.品质.put("橙器灵", d.元神器灵.金汤之灵_外防.品质.get("橙器灵") + 1L);
									d.元神器灵.金汤之灵_外防.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 4:
								d.元神器灵.罡岚之灵_内防.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.罡岚之灵_内防.品质.put("白器灵", d.元神器灵.罡岚之灵_内防.品质.get("白器灵") + 1L);
									d.元神器灵.罡岚之灵_内防.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.罡岚之灵_内防.品质.put("绿器灵", d.元神器灵.罡岚之灵_内防.品质.get("绿器灵") + 1L);
									d.元神器灵.罡岚之灵_内防.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.罡岚之灵_内防.品质.put("蓝器灵", d.元神器灵.罡岚之灵_内防.品质.get("蓝器灵") + 1L);
									d.元神器灵.罡岚之灵_内防.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.罡岚之灵_内防.品质.put("紫器灵", d.元神器灵.罡岚之灵_内防.品质.get("紫器灵") + 1L);
									d.元神器灵.罡岚之灵_内防.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.罡岚之灵_内防.品质.put("橙器灵", d.元神器灵.罡岚之灵_内防.品质.get("橙器灵") + 1L);
									d.元神器灵.罡岚之灵_内防.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 5:
								d.元神器灵.离火之灵_炎焚攻击.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.离火之灵_炎焚攻击.品质.put("白器灵", d.元神器灵.离火之灵_炎焚攻击.品质.get("白器灵") + 1L);
									d.元神器灵.离火之灵_炎焚攻击.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.离火之灵_炎焚攻击.品质.put("绿器灵", d.元神器灵.离火之灵_炎焚攻击.品质.get("绿器灵") + 1L);
									d.元神器灵.离火之灵_炎焚攻击.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.离火之灵_炎焚攻击.品质.put("蓝器灵", d.元神器灵.离火之灵_炎焚攻击.品质.get("蓝器灵") + 1L);
									d.元神器灵.离火之灵_炎焚攻击.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.离火之灵_炎焚攻击.品质.put("紫器灵", d.元神器灵.离火之灵_炎焚攻击.品质.get("紫器灵") + 1L);
									d.元神器灵.离火之灵_炎焚攻击.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.离火之灵_炎焚攻击.品质.put("橙器灵", d.元神器灵.离火之灵_炎焚攻击.品质.get("橙器灵") + 1L);
									d.元神器灵.离火之灵_炎焚攻击.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 6:
								d.元神器灵.葵水之灵_葵水攻击.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.葵水之灵_葵水攻击.品质.put("白器灵", d.元神器灵.葵水之灵_葵水攻击.品质.get("白器灵") + 1L);
									d.元神器灵.葵水之灵_葵水攻击.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.葵水之灵_葵水攻击.品质.put("绿器灵", d.元神器灵.葵水之灵_葵水攻击.品质.get("绿器灵") + 1L);
									d.元神器灵.葵水之灵_葵水攻击.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.葵水之灵_葵水攻击.品质.put("蓝器灵", d.元神器灵.葵水之灵_葵水攻击.品质.get("蓝器灵") + 1L);
									d.元神器灵.葵水之灵_葵水攻击.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.葵水之灵_葵水攻击.品质.put("紫器灵", d.元神器灵.葵水之灵_葵水攻击.品质.get("紫器灵") + 1L);
									d.元神器灵.葵水之灵_葵水攻击.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.葵水之灵_葵水攻击.品质.put("橙器灵", d.元神器灵.葵水之灵_葵水攻击.品质.get("橙器灵") + 1L);
									d.元神器灵.葵水之灵_葵水攻击.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 7:
								d.元神器灵.飓风之灵_离火攻击.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.飓风之灵_离火攻击.品质.put("白器灵", d.元神器灵.飓风之灵_离火攻击.品质.get("白器灵") + 1L);
									d.元神器灵.飓风之灵_离火攻击.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.飓风之灵_离火攻击.品质.put("绿器灵", d.元神器灵.飓风之灵_离火攻击.品质.get("绿器灵") + 1L);
									d.元神器灵.飓风之灵_离火攻击.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.飓风之灵_离火攻击.品质.put("蓝器灵", d.元神器灵.飓风之灵_离火攻击.品质.get("蓝器灵") + 1L);
									d.元神器灵.飓风之灵_离火攻击.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.飓风之灵_离火攻击.品质.put("紫器灵", d.元神器灵.飓风之灵_离火攻击.品质.get("紫器灵") + 1L);
									d.元神器灵.飓风之灵_离火攻击.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.飓风之灵_离火攻击.品质.put("橙器灵", d.元神器灵.飓风之灵_离火攻击.品质.get("橙器灵") + 1L);
									d.元神器灵.飓风之灵_离火攻击.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 8:
								d.元神器灵.惊雷之灵_乙木攻击.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.惊雷之灵_乙木攻击.品质.put("白器灵", d.元神器灵.惊雷之灵_乙木攻击.品质.get("白器灵") + 1L);
									d.元神器灵.惊雷之灵_乙木攻击.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.惊雷之灵_乙木攻击.品质.put("绿器灵", d.元神器灵.惊雷之灵_乙木攻击.品质.get("绿器灵") + 1L);
									d.元神器灵.惊雷之灵_乙木攻击.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.惊雷之灵_乙木攻击.品质.put("蓝器灵", d.元神器灵.惊雷之灵_乙木攻击.品质.get("蓝器灵") + 1L);
									d.元神器灵.惊雷之灵_乙木攻击.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.惊雷之灵_乙木攻击.品质.put("紫器灵", d.元神器灵.惊雷之灵_乙木攻击.品质.get("紫器灵") + 1L);
									d.元神器灵.惊雷之灵_乙木攻击.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.惊雷之灵_乙木攻击.品质.put("橙器灵", d.元神器灵.惊雷之灵_乙木攻击.品质.get("橙器灵") + 1L);
									d.元神器灵.惊雷之灵_乙木攻击.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 9:
								d.元神器灵.御火之灵_炎焚防御.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.御火之灵_炎焚防御.品质.put("白器灵", d.元神器灵.御火之灵_炎焚防御.品质.get("白器灵") + 1L);
									d.元神器灵.御火之灵_炎焚防御.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.御火之灵_炎焚防御.品质.put("绿器灵", d.元神器灵.御火之灵_炎焚防御.品质.get("绿器灵") + 1L);
									d.元神器灵.御火之灵_炎焚防御.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.御火之灵_炎焚防御.品质.put("蓝器灵", d.元神器灵.御火之灵_炎焚防御.品质.get("蓝器灵") + 1L);
									d.元神器灵.御火之灵_炎焚防御.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.御火之灵_炎焚防御.品质.put("紫器灵", d.元神器灵.御火之灵_炎焚防御.品质.get("紫器灵") + 1L);
									d.元神器灵.御火之灵_炎焚防御.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.御火之灵_炎焚防御.品质.put("橙器灵", d.元神器灵.御火之灵_炎焚防御.品质.get("橙器灵") + 1L);
									d.元神器灵.御火之灵_炎焚防御.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 10:
								d.元神器灵.御冰之灵_葵水防御.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.御冰之灵_葵水防御.品质.put("白器灵", d.元神器灵.御冰之灵_葵水防御.品质.get("白器灵") + 1L);
									d.元神器灵.御冰之灵_葵水防御.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.御冰之灵_葵水防御.品质.put("绿器灵", d.元神器灵.御冰之灵_葵水防御.品质.get("绿器灵") + 1L);
									d.元神器灵.御冰之灵_葵水防御.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.御冰之灵_葵水防御.品质.put("蓝器灵", d.元神器灵.御冰之灵_葵水防御.品质.get("蓝器灵") + 1L);
									d.元神器灵.御冰之灵_葵水防御.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.御冰之灵_葵水防御.品质.put("紫器灵", d.元神器灵.御冰之灵_葵水防御.品质.get("紫器灵") + 1L);
									d.元神器灵.御冰之灵_葵水防御.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.御冰之灵_葵水防御.品质.put("橙器灵", d.元神器灵.御冰之灵_葵水防御.品质.get("橙器灵") + 1L);
									d.元神器灵.御冰之灵_葵水防御.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 11:
								d.元神器灵.御风之灵_离金防御.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.御风之灵_离金防御.品质.put("白器灵", d.元神器灵.御风之灵_离金防御.品质.get("白器灵") + 1L);
									d.元神器灵.御风之灵_离金防御.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.御风之灵_离金防御.品质.put("绿器灵", d.元神器灵.御风之灵_离金防御.品质.get("绿器灵") + 1L);
									d.元神器灵.御风之灵_离金防御.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.御风之灵_离金防御.品质.put("蓝器灵", d.元神器灵.御风之灵_离金防御.品质.get("蓝器灵") + 1L);
									d.元神器灵.御风之灵_离金防御.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.御风之灵_离金防御.品质.put("紫器灵", d.元神器灵.御风之灵_离金防御.品质.get("紫器灵") + 1L);
									d.元神器灵.御风之灵_离金防御.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.御风之灵_离金防御.品质.put("橙器灵", d.元神器灵.御风之灵_离金防御.品质.get("橙器灵") + 1L);
									d.元神器灵.御风之灵_离金防御.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							case 12:
								d.元神器灵.御雷之灵_乙木防御.镶嵌 += 1;
								switch (pingzhi) {
								case 0:
									d.元神器灵.御雷之灵_乙木防御.品质.put("白器灵", d.元神器灵.御雷之灵_乙木防御.品质.get("白器灵") + 1L);
									d.元神器灵.御雷之灵_乙木防御.白器灵填充 += tunshuzhi;
									break;
								case 1:
									d.元神器灵.御雷之灵_乙木防御.品质.put("绿器灵", d.元神器灵.御雷之灵_乙木防御.品质.get("绿器灵") + 1L);
									d.元神器灵.御雷之灵_乙木防御.绿器灵填充 += tunshuzhi;
									break;
								case 2:
									d.元神器灵.御雷之灵_乙木防御.品质.put("蓝器灵", d.元神器灵.御雷之灵_乙木防御.品质.get("蓝器灵") + 1L);
									d.元神器灵.御雷之灵_乙木防御.蓝器灵填充 += tunshuzhi;
									break;
								case 3:
									d.元神器灵.御雷之灵_乙木防御.品质.put("紫器灵", d.元神器灵.御雷之灵_乙木防御.品质.get("紫器灵") + 1L);
									d.元神器灵.御雷之灵_乙木防御.紫器灵填充 += tunshuzhi;
									break;
								case 4:
									d.元神器灵.御雷之灵_乙木防御.品质.put("橙器灵", d.元神器灵.御雷之灵_乙木防御.品质.get("橙器灵") + 1L);
									d.元神器灵.御雷之灵_乙木防御.橙器灵填充 += tunshuzhi;
									break;
								}
								break;
							}
						}
					}
				}
				for (int i = 0; i < types.length; i++) {
					switch (types[i]) {
					case 0:
						d.元神器灵.长生之灵_生命.槽数 += 1;
						break;
					case 1:
						d.元神器灵.擎天之灵_外攻.槽数 += 1;
						break;
					case 2:
						d.元神器灵.琼浆之灵_内法.槽数 += 1;
						break;
					case 3:
						d.元神器灵.金汤之灵_外防.槽数 += 1;
						break;
					case 4:
						d.元神器灵.罡岚之灵_内防.槽数 += 1;
						break;
					case 5:
						d.元神器灵.离火之灵_炎焚攻击.槽数 += 1;
						break;
					case 6:
						d.元神器灵.葵水之灵_葵水攻击.槽数 += 1;
						break;
					case 7:
						d.元神器灵.飓风之灵_离火攻击.槽数 += 1;
						break;
					case 8:
						d.元神器灵.惊雷之灵_乙木攻击.槽数 += 1;
						break;
					case 9:
						d.元神器灵.御火之灵_炎焚防御.槽数 += 1;
						break;
					case 10:
						d.元神器灵.御冰之灵_葵水防御.槽数 += 1;
						break;
					case 11:
						d.元神器灵.御风之灵_离金防御.槽数 += 1;
						break;
					case 12:
						d.元神器灵.御雷之灵_乙木防御.槽数 += 1;
						break;
					}
				}
			}
		}
		//

		if (yuanshen != null) {
			horseIDs = player.getSoul(Soul.SOUL_TYPE_SOUL).getHorseArr();
			maxLevelHorse = null;
			for (int i = 0; i < horseIDs.size(); i++) {
				Horse horse = HorseManager.getInstance().getHorseById(horseIDs.get(i), player);
				if(horse!=null && !horse.isFly()){
					//new add
					d.元神坐骑.培养 += horse.getRank();
					d.元神坐骑.血脉  += horse.getOtherData().getBloodStar();
					int color = horse.getColorType();
					switch (color) {
					case 0:
						d.元神坐骑.品质.put("白", 1L);
						break;
					case 1:
						d.元神坐骑.品质.put("绿", 1L);
						break;
					case 2:
						d.元神坐骑.品质.put("蓝", 1L);
						break;
					case 3:
						d.元神坐骑.品质.put("紫", 1L);
						break;
					case 4:
						d.元神坐骑.品质.put("橙", 1L);
						break;
					default:
						break;
					}
					
					int[] skillList = horse.getOtherData().getSkillList();
					if(skillList!=null && skillList.length>0){
						for(int j=0;j<skillList.length;j++){
							int id = skillList[j];
							if(Horse2Manager.instance.getSkillType(id)==0){
								d.元神坐骑.技能.中低级技能总数++;
								d.元神坐骑.技能.低级技能占比++;
								if(horse.getOtherData().getSkillLevelById(id)==0){
									d.元神坐骑.技能.一级低级技能占比++;
								}if(horse.getOtherData().getSkillLevelById(id)==1){
									d.元神坐骑.技能.二级低级技能占比++;
								}if(horse.getOtherData().getSkillLevelById(id)==2){
									d.元神坐骑.技能.三级低级技能占比++;
								}
							}else if(Horse2Manager.instance.getSkillType(id)==1){
								d.元神坐骑.技能.中高级技能总数++;
								d.元神坐骑.技能.高级技能占比++;
								if(horse.getOtherData().getSkillLevelById(id)==0){
									d.元神坐骑.技能.一级高级技能占比++;
								}if(horse.getOtherData().getSkillLevelById(id)==1){
									d.元神坐骑.技能.二级高级技能占比++;
								}if(horse.getOtherData().getSkillLevelById(id)==2){
									d.元神坐骑.技能.三级高级技能占比++;
								}
							}
						}
					}
					
					HorseRankModel hrm = Horse2Manager.instance.rankModelMap.get(horse.getRank());
					if(hrm!=null){
						d.元神坐骑.技能.本尊坐骑已开启技能格总数 = hrm.getOpenSkillNum();
					}
					
					///
				}
				if (horse != null && horse.isFly()) {
					if (horse.isLimitTime()) {
						d.元神坐骑.临时飞行坐骑数量 += 1;
					} else {
						d.元神坐骑.永久飞行坐骑数量 += 1;
						Article a = ArticleManager.getInstance().getArticle(horse.getHorseName());
						if (a.getName_stat().indexOf("爱的炫舞") >= 0) {
							d.元神坐骑.飞行坐骑.put("爱的炫舞", 1L);
						} else if (a.getName_stat().indexOf("浪漫今生") >= 0) {
							d.元神坐骑.飞行坐骑.put("浪漫今生", 1L);
						} else if (a.getName_stat().indexOf("碧虚青鸾") >= 0) {
							d.元神坐骑.飞行坐骑.put("碧虚青鸾", 1L);
						} else if (a.getName_stat().indexOf("八卦仙蒲") >= 0) {
							d.元神坐骑.飞行坐骑.put("八卦仙蒲", 1L);
						} else if (a.getName_stat().indexOf("梦瞳仙鹤") >= 0) {
							d.元神坐骑.飞行坐骑.put("梦瞳仙鹤", 1L);
						} else if (a.getName_stat().indexOf("乾坤葫芦") >= 0) {
							d.元神坐骑.飞行坐骑.put("乾坤葫芦", 1L);
						} else if (a.getName_stat().indexOf("金极龙皇") >= 0) {
							d.元神坐骑.飞行坐骑.put("金极龙皇", 1L);
						} else if (a.getName_stat().indexOf("焚焰火扇") >= 0) {
							d.元神坐骑.飞行坐骑.put("焚焰火扇", 1L);
						} else if (a.getName_stat().indexOf("深渊魔章") >= 0) {
							d.元神坐骑.飞行坐骑.put("深渊魔章", 1L);
						}
					}
				} else {
					if(horse!=null){
						d.元神坐骑.普通坐骑数量 += 1;
						if (maxLevelHorse == null) {
							maxLevelHorse = horse;
						} else {
							if (maxLevelHorse.getRank() < horse.getRank()) {
								maxLevelHorse = horse;
							}
						}
					}
				}
			}
			if (maxLevelHorse != null) {
				HorseEquipmentColumn hec = maxLevelHorse.getEquipmentColumn();
				EquipmentEntity[] hEquEntitys = hec.getEquipmentArrayByCopy();
				hec = maxLevelHorse.getEquipmentColumn();
				hEquEntitys = hec.getEquipmentArrayByCopy();
				for (int j = 0; j < hEquEntitys.length; j++) {
					// Translate.金盔, Translate.剑翅, Translate.腿甲, Translate.配鞍, Translate.鳞甲
					EquipmentEntity hequE = hEquEntitys[j];
					int color = 0;
					if (hequE != null) {
						switch (j) {
						case 0:
							d.元神坐骑.金盔.数量 = 1;
							d.元神坐骑.金盔.强化 = hequE.getStar();
							color = hequE.getColorType();
							switch (color) {
							case ArticleManager.equipment_color_白:
								d.元神坐骑.金盔.颜色.put("白", 1L);
								break;
							case ArticleManager.equipment_color_绿:
								d.元神坐骑.金盔.颜色.put("绿", 1L);
								break;
							case ArticleManager.equipment_color_蓝:
								d.元神坐骑.金盔.颜色.put("蓝", 1L);
								break;
							case ArticleManager.equipment_color_紫:
								d.元神坐骑.金盔.颜色.put("紫", 1L);
								break;
							case ArticleManager.equipment_color_完美紫:
								d.元神坐骑.金盔.颜色.put("完美紫", 1L);
								break;
							case ArticleManager.equipment_color_橙:
								d.元神坐骑.金盔.颜色.put("橙", 1L);
								break;
							case ArticleManager.equipment_color_完美橙:
								d.元神坐骑.金盔.颜色.put("完美橙", 1L);
								break;

							default:
								break;
							}
							break;
						case 1:
							d.元神坐骑.剑翅.数量 = 1;
							d.元神坐骑.剑翅.强化 = hequE.getStar();
							color = hequE.getColorType();
							switch (color) {
							case ArticleManager.equipment_color_白:
								d.元神坐骑.剑翅.颜色.put("白", 1L);
								break;
							case ArticleManager.equipment_color_绿:
								d.元神坐骑.剑翅.颜色.put("绿", 1L);
								break;
							case ArticleManager.equipment_color_蓝:
								d.元神坐骑.剑翅.颜色.put("蓝", 1L);
								break;
							case ArticleManager.equipment_color_紫:
								d.元神坐骑.剑翅.颜色.put("紫", 1L);
								break;
							case ArticleManager.equipment_color_完美紫:
								d.元神坐骑.剑翅.颜色.put("完美紫", 1L);
								break;
							case ArticleManager.equipment_color_橙:
								d.元神坐骑.剑翅.颜色.put("橙", 1L);
								break;
							case ArticleManager.equipment_color_完美橙:
								d.元神坐骑.剑翅.颜色.put("完美橙", 1L);
								break;

							default:
								break;
							}
							break;
						case 2:
							d.元神坐骑.腿甲.数量 = 1;
							d.元神坐骑.腿甲.强化 = hequE.getStar();
							color = hequE.getColorType();
							switch (color) {
							case ArticleManager.equipment_color_白:
								d.元神坐骑.腿甲.颜色.put("白", 1L);
								break;
							case ArticleManager.equipment_color_绿:
								d.元神坐骑.腿甲.颜色.put("绿", 1L);
								break;
							case ArticleManager.equipment_color_蓝:
								d.元神坐骑.腿甲.颜色.put("蓝", 1L);
								break;
							case ArticleManager.equipment_color_紫:
								d.元神坐骑.腿甲.颜色.put("紫", 1L);
								break;
							case ArticleManager.equipment_color_完美紫:
								d.元神坐骑.腿甲.颜色.put("完美紫", 1L);
								break;
							case ArticleManager.equipment_color_橙:
								d.元神坐骑.腿甲.颜色.put("橙", 1L);
								break;
							case ArticleManager.equipment_color_完美橙:
								d.元神坐骑.腿甲.颜色.put("完美橙", 1L);
								break;

							default:
								break;
							}
							break;
						case 3:
							d.元神坐骑.配鞍.数量 = 1;
							d.元神坐骑.配鞍.强化 = hequE.getStar();
							color = hequE.getColorType();
							switch (color) {
							case ArticleManager.equipment_color_白:
								d.元神坐骑.配鞍.颜色.put("白", 1L);
								break;
							case ArticleManager.equipment_color_绿:
								d.元神坐骑.配鞍.颜色.put("绿", 1L);
								break;
							case ArticleManager.equipment_color_蓝:
								d.元神坐骑.配鞍.颜色.put("蓝", 1L);
								break;
							case ArticleManager.equipment_color_紫:
								d.元神坐骑.配鞍.颜色.put("紫", 1L);
								break;
							case ArticleManager.equipment_color_完美紫:
								d.元神坐骑.配鞍.颜色.put("完美紫", 1L);
								break;
							case ArticleManager.equipment_color_橙:
								d.元神坐骑.配鞍.颜色.put("橙", 1L);
								break;
							case ArticleManager.equipment_color_完美橙:
								d.元神坐骑.配鞍.颜色.put("完美橙", 1L);
								break;

							default:
								break;
							}
							break;
						case 4:
							d.元神坐骑.鳞甲.数量 = 1;
							d.元神坐骑.鳞甲.强化 = hequE.getStar();
							color = hequE.getColorType();
							switch (color) {
							case ArticleManager.equipment_color_白:
								d.元神坐骑.鳞甲.颜色.put("白", 1L);
								break;
							case ArticleManager.equipment_color_绿:
								d.元神坐骑.鳞甲.颜色.put("绿", 1L);
								break;
							case ArticleManager.equipment_color_蓝:
								d.元神坐骑.鳞甲.颜色.put("蓝", 1L);
								break;
							case ArticleManager.equipment_color_紫:
								d.元神坐骑.鳞甲.颜色.put("紫", 1L);
								break;
							case ArticleManager.equipment_color_完美紫:
								d.元神坐骑.鳞甲.颜色.put("完美紫", 1L);
								break;
							case ArticleManager.equipment_color_橙:
								d.元神坐骑.鳞甲.颜色.put("橙", 1L);
								break;
							case ArticleManager.equipment_color_完美橙:
								d.元神坐骑.鳞甲.颜色.put("完美橙", 1L);
								break;

							default:
								break;
							}
							break;
						}
					}
				}
			}
		}
		if (yuanshen != null) {
			byte[] skillOneLevels = yuanshen.getSkillOneLevels();

			d.元神进阶技能._41级等级 = skillOneLevels[0];
			d.元神进阶技能._56级等级 = skillOneLevels[1];
			d.元神进阶技能._71级等级 = skillOneLevels[2];
			d.元神进阶技能._86级等级 = skillOneLevels[3];
			d.元神进阶技能._101级等级 = skillOneLevels[4];
			d.元神进阶技能._116级等级 = skillOneLevels[5];
			d.元神进阶技能._131级等级 = skillOneLevels[6];
			d.元神进阶技能._146级等级 = skillOneLevels[7];
			d.元神进阶技能._161级等级 = skillOneLevels[8];
			d.元神进阶技能._176级等级 = skillOneLevels[9];
			d.元神进阶技能._191级等级 = skillOneLevels[10];
			d.元神进阶技能._206级等级 = skillOneLevels[11];
		}
		
		{
			//元神法宝
			if(yuanshen!=null){
				ec = yuanshen.getEc();
				ArticleEntity ae = ec.get(11);
				if(ae!=null){
					if(ae instanceof NewMagicWeaponEntity){
						NewMagicWeaponEntity me = (NewMagicWeaponEntity)ae;
						Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
						if(a!=null){
							if(a instanceof MagicWeapon){
								MagicWeapon m = (MagicWeapon)a;
								d.元神法宝.数量 = 1;
								d.元神法宝.等级 = a.getArticleLevel();
								d.元神法宝.品阶 = me.getMcolorlevel();
								d.元神法宝.加持 = me.getMstar();
								switch (m.getCareertype()) {
									case 0:
										d.元神法宝.职业类型.put("通用", 1l);
										break;
									case 1:
										d.元神法宝.职业类型.put("修罗", 1l);							
										break;
									case 2:
										d.元神法宝.职业类型.put("影魅", 1l);		
										break;
									case 3:
										d.元神法宝.职业类型.put("仙心", 1l);	
										break;
									case 4:
										d.元神法宝.职业类型.put("九黎", 1l);	
										break;
								}
								
								switch(me.getColorType()){
									case 0:
										d.元神法宝.颜色.put("白", 1l);
										break;
									case 1:
										d.元神法宝.颜色.put("绿", 1l);
										break;
									case 2:
										d.元神法宝.颜色.put("蓝", 1l);
										break;
									case 3:
										d.元神法宝.颜色.put("紫", 1l);
										break;
									case 4:
										d.元神法宝.颜色.put("橙", 1l);
										break;
									case 5:
										d.元神法宝.颜色.put("金", 1l);
										break;
								}
								
								d.元神法宝.基础属性.put(me.getBasicpropertyname(), 1l);
								MagicWeaponBaseModel mbm = MagicWeaponManager.instance.mwBaseModel.get(me.getColorType());
								
								if(me.getHideproterty()!=null){
									d.元神法宝.隐藏属性开启比例 = me.getHideproterty().size();
									d.元神法宝.可开启隐藏属性最大数 = mbm.getHiddenAttrNum();
									if(me.getHideproterty().size()>0){
										Map<Integer, Integer> map = new HashMap<Integer, Integer>();
										
										
										d.元神法宝.属性颜色.put("白属性",0l);
										d.元神法宝.属性颜色.put("绿属性", 0l);
										d.元神法宝.属性颜色.put("蓝属性", 0l);
										d.元神法宝.属性颜色.put("紫属性", 0l);
										d.元神法宝.属性颜色.put("橙属性", 0l);
										d.元神法宝.属性颜色.put("金属性", 0l);
										for(int i=0;i<me.getHideproterty().size();i++){
											MagicWeaponAttrModel mm = me.getHideproterty().get(i);
											if(mm!=null){
												if(map.get(mm.getId())==null){
													map.put(mm.getId(), 1);
												}else{
													map.put(mm.getId(), map.get(mm.getId()).intValue()+1);
												}
												
												switch(mm.getColorType()){
												case 0:
													d.元神法宝.属性颜色.put("白属性", d.元神法宝.属性颜色.get("白属性") +1);
													break;
												case 1:
													d.元神法宝.属性颜色.put("绿属性", d.元神法宝.属性颜色.get("绿属性") +1);
													break;
												case 2:
													d.元神法宝.属性颜色.put("蓝属性", d.元神法宝.属性颜色.get("蓝属性") +1);
													break;
												case 3:
													d.元神法宝.属性颜色.put("紫属性", d.元神法宝.属性颜色.get("紫属性") +1);
													break;
												case 4:
													d.元神法宝.属性颜色.put("橙属性", d.元神法宝.属性颜色.get("橙属性") +1);
													break;
												case 5:
													d.元神法宝.属性颜色.put("金属性", d.元神法宝.属性颜色.get("金属性") +1);
													break;
												}
											}
										}
										
										int 二 = 0;
										int 三 = 0;
										int 四 = 0;
										int 五 = 0;
										Iterator<Integer> it = map.keySet().iterator();
										while(it.hasNext()){
											int o = it.next();
											int value = map.get(o);
											if(value==2){
												二 ++;
											}else if(value==3){
												三++;
											}else if(value==4){
												四++;
											}else if(value==5){
												五++;
											}
										}
										
										d.元神法宝.同属性占比.put("2个", 二*2);
										d.元神法宝.同属性占比.put("3个", 三*3);
										d.元神法宝.同属性占比.put("4个", 四*4);
										d.元神法宝.同属性占比.put("5个", 五*5);
									}
									
								}
								
							}
						}
					}
				}
			}
		}
		
		{//家族修炼
			if(player.getJiazuId() > 0){
				d.家族修炼.有家族人数 = 1;
				JiazuMember2 jm = JiazuEntityManager2.instance.getEntity(player.getId());
				if(jm != null){
					List<JiazuXiulian> list = jm.getXiulian();
					for(JiazuXiulian xiulian : list){
						PracticeModel pm = JiazuManager2.instance.praticeMaps.get(xiulian.getSkillId());
						switch (pm.getAddAttrType()) {
						case MagicWeaponConstant.hpNum:
							d.家族修炼.气血 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.physicAttNum:
							d.家族修炼.物攻 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.magicAttNum:
							d.家族修炼.法功 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.physicDefanceNum:
							d.家族修炼.物防 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.magicDefanceNum:
							d.家族修炼.法防 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.dodgeNum:
							d.家族修炼.躲闪 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.cirtNum:
							d.家族修炼.暴击 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.hitNum:
							d.家族修炼.命中 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.破甲:
							d.家族修炼.破甲 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.精准:
							d.家族修炼.精准 = xiulian.getSkillLevel();
							break;
						case MagicWeaponConstant.免爆:
							d.家族修炼.免爆 = xiulian.getSkillLevel();
							break;
						default:
							break;
						}
					}
				}
			}else{
				d.家族修炼.无家族占比 = 1;
			}
		}
		{//玩家兽魂
			if (player.getHuntLifr() != null) {
				d.玩家兽魂.已开启兽魂人数 = 1;
				if(player.getHuntLifr().isOpenHunt()){
					d.玩家兽魂.镶嵌率 = 1;
				}
			}
			
			long[] huntDatas = player.getHuntLifr().getHuntDatas();
			//0气血  1攻击  2外防  3内防  4暴击   5命中   6闪避   7破甲   8精准   9免暴  
			if(huntDatas != null){
				for(int i=0;i<HuntLifeManager.对应兽魂道具名.length;i++){
					switch (i) {
					case 0:
						if(huntDatas[i] > 0){
							d.玩家兽魂.气血宝石个数 = 1;
							d.玩家兽魂.气血等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;
					case 1:
						if(huntDatas[i] > 0){
							d.玩家兽魂.攻击宝石个数 = 1;
							d.玩家兽魂.攻击等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;
					case 2:
						if(huntDatas[i] > 0){
							d.玩家兽魂.物防宝石个数 = 1;
							d.玩家兽魂.物防等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;
					case 3:
						if(huntDatas[i] > 0){
							d.玩家兽魂.法防宝石个数 = 1;
							d.玩家兽魂.法防等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;	
					case 4:
						if(huntDatas[i] > 0){
							d.玩家兽魂.暴击宝石个数 = 1;
							d.玩家兽魂.暴击等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;	
					case 5:
						if(huntDatas[i] > 0){
							d.玩家兽魂.命中宝石个数 = 1;
							d.玩家兽魂.命中等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;	
					case 6:
						if(huntDatas[i] > 0){
							d.玩家兽魂.躲闪宝石个数 = 1;
							d.玩家兽魂.躲闪等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;	
					case 7:
						if(huntDatas[i] > 0){
							d.玩家兽魂.破甲宝石个数 = 1;
							d.玩家兽魂.破甲等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;	
					case 8:
						if(huntDatas[i] > 0){
							d.玩家兽魂.精准宝石个数 = 1;
							d.玩家兽魂.精准等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;	
					case 9:
						if(huntDatas[i] > 0){
							d.玩家兽魂.免爆宝石个数 = 1;
							d.玩家兽魂.免爆等级 = player.getHuntLevel(huntDatas[i]);
						}
						break;	
					}
				}
			}
		}
		return d;
	}

	public static void update(Player player) {
		map.put(player.getId(), getRowData(player));
	}
	
	public static double get2Double(double a){  
	    DecimalFormat df=new DecimalFormat("0.00");  
	    return new Double(df.format(a).toString());  
	}  

}
