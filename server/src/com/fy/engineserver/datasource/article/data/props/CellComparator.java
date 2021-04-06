package com.fy.engineserver.datasource.article.data.props;

import java.util.Comparator;

import com.fy.engineserver.datasource.article.data.articles.AiguilleArticle;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.articles.MaterialArticle;
import com.fy.engineserver.datasource.article.data.articles.UpgradeArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.data.equipments.EquipmentColumn;
import com.fy.engineserver.datasource.article.data.equipments.Weapon;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;


/**
 * 衣服、头盔、肩膀、手套、鞋子、武器（刀、剑、弓、双刀、双剑）、饰品、项链、戒指
 * 缓回血、缓回蓝、瞬回血、瞬回蓝、卷轴、任务道具、坐骑、其他道具
 * 材料、镶嵌类宝石、强化类物品、打孔类物品
 * @author Administrator
 *
 */
public class CellComparator implements Comparator<Cell> {

	public int compare(Cell o1, Cell o2) {
		// TODO Auto-generated method stub
		ArticleEntity entity1 = ArticleEntityManager.getInstance().getEntity(o1.getEntityId());
		ArticleEntity entity2 = ArticleEntityManager.getInstance().getEntity(o2.getEntityId());

		if(entity1 != null && entity2 != null){
			ArticleManager am = ArticleManager.getInstance();
			Article article1 = am.getArticle(entity1.getArticleName());
			Article article2 = am.getArticle(entity2.getArticleName());
			if(article1 != null && article2 != null){
				//先按照颜色
				if(entity2.getColorType() == entity1.getColorType()){
					//按照二级分类分，然后根据不同的类型继续划分
					if(article1.get物品二级分类() != null && article2.get物品二级分类() != null){
						if(article1.get物品二级分类().equals(article2.get物品二级分类())){
							if(Translate.角色装备类.equals(article1.get物品一级分类())){
								if(entity1 instanceof EquipmentEntity && entity2 instanceof EquipmentEntity){
									Equipment a1 = (Equipment)am.getArticle(entity1.getArticleName());
									Equipment a2 = (Equipment)am.getArticle(entity2.getArticleName());
									//先比较比较颜色，颜色相同,装备类型，如果是武器先比较武器类型，如果类型相同，比较级别，级别相同，按照hashcode比较
									if(a1 != null && a2 != null){
										int a1equipType = a1.getEquipmentType();
										int a2equipType = a2.getEquipmentType();
										if(a1equipType == a2equipType){
											if(a1equipType == EquipmentColumn.EQUIPMENT_TYPE_weapon){
												if(((Weapon)a1).getWeaponType() == ((Weapon)a2).getWeaponType()){
													if(a1.getPlayerLevelLimit() == a2.getPlayerLevelLimit()){
														if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
															return o2.getCount() - o1.getCount();
														}else{
															return entity1.getShowName().compareTo(entity2.getShowName());
														}
													}else{
														return a2.getPlayerLevelLimit() - a1.getPlayerLevelLimit();
													}
												}else{
													return ((Weapon)a1).getWeaponType() - ((Weapon)a2).getWeaponType();
												}
											}else{
												if(a1.getPlayerLevelLimit() == a2.getPlayerLevelLimit()){
													if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
														return o2.getCount() - o1.getCount();
													}else{
														return entity1.getShowName().compareTo(entity2.getShowName());
													}
												}else{
													return a2.getPlayerLevelLimit() - a1.getPlayerLevelLimit();
												}
											}
										}else{
											return a1equipType - a2equipType;
										}
									}
								}
							}else if(Translate.马匹装备类.equals(article1.get物品一级分类())){
								if(entity1 instanceof EquipmentEntity && entity2 instanceof EquipmentEntity){
									Equipment a1 = (Equipment)am.getArticle(entity1.getArticleName());
									Equipment a2 = (Equipment)am.getArticle(entity2.getArticleName());
									//先比较装备类型，如果是武器先比较武器类型，如果类型相同，比较级别，级别相同，比较颜色，颜色相同按照hashcode比较
									if(a1 != null && a2 != null){
										if(a1.getEquipmentType() == a2.getEquipmentType()){
											if(a1.getPlayerLevelLimit() == a2.getPlayerLevelLimit()){
													if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
														return o2.getCount() - o1.getCount();
													}else{
														return entity1.getShowName().compareTo(entity2.getShowName());
													}
											}else{
												return a2.getPlayerLevelLimit() - a1.getPlayerLevelLimit();
											}
										}else{
											return a1.getEquipmentType() - a2.getEquipmentType();
										}
									}
								}
							}else if(Translate.宝石类.equals(article1.get物品一级分类())){
								if(article1 instanceof InlayArticle && article2 instanceof InlayArticle){
									InlayArticle a1 = (InlayArticle)article1;
									InlayArticle a2 = (InlayArticle)article2;
									if(a1.getArticleLevel() == a2.getArticleLevel()){
										if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
											return o2.getCount() - o1.getCount();
										}else{
											return entity1.getShowName().compareTo(entity2.getShowName());
										}
									}else{
										return a2.getArticleLevel() - a1.getArticleLevel();
									}
								}
							}else if(Translate.任务类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.人物消耗品类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.宠物消耗品.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.庄园消耗品.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.宠物蛋.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.古董类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.庄园果实类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.材料类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.角色药品.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.宠物药品.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.角色技能书类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.宠物技能书类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.包裹类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}else if(Translate.宝箱类.equals(article1.get物品一级分类())){
								Article a1 = article1;
								Article a2 = article2;
								if(a1.getArticleLevel() == a2.getArticleLevel()){
									if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
										return o2.getCount() - o1.getCount();
									}else{
										return entity1.getShowName().compareTo(entity2.getShowName());
									}
								}else{
									return a2.getArticleLevel() - a1.getArticleLevel();
								}
							}
						}else{
							return ArticleManager.根据物品二级分类名得到二级分类数值(article1.get物品二级分类()) - ArticleManager.根据物品二级分类名得到二级分类数值(article2.get物品二级分类());
						}
					}
					if(entity1 instanceof EquipmentEntity && entity2 instanceof EquipmentEntity){
						if(am != null){
							Equipment a1 = (Equipment)am.getArticle(entity1.getArticleName());
							Equipment a2 = (Equipment)am.getArticle(entity2.getArticleName());
							//先比较装备类型，如果是武器先比较武器类型，如果类型相同，比较级别，级别相同，比较颜色，颜色相同按照hashcode比较
							if(a1 != null && a2 != null){
								if(a1.getEquipmentType() == a2.getEquipmentType()){
									if(a1.getEquipmentType() == EquipmentColumn.EQUIPMENT_TYPE_weapon){
										if(((Weapon)a1).getWeaponType() == ((Weapon)a2).getWeaponType()){
											if(a1.getPlayerLevelLimit() == a2.getPlayerLevelLimit()){
												if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
													return o2.getCount() - o1.getCount();
												}else{
													return entity1.getShowName().compareTo(entity2.getShowName());
												}
											}else{
												return a2.getPlayerLevelLimit() - a1.getPlayerLevelLimit();
											}
										}else{
											return ((Weapon)a1).getWeaponType() - ((Weapon)a2).getWeaponType();
										}
									}else{
										if(a1.getPlayerLevelLimit() == a2.getPlayerLevelLimit()){
											if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
												return o2.getCount() - o1.getCount();
											}else{
												return entity1.getShowName().compareTo(entity2.getShowName());
											}
										}else{
											return a2.getPlayerLevelLimit() - a1.getPlayerLevelLimit();
										}
									}
								}else{
									return a1.getEquipmentType() - a2.getEquipmentType();
								}
							}
						}
						//缓回血、缓回蓝、瞬回血、瞬回蓝、卷轴、任务道具、坐骑、其他道具
					}else if(entity1 instanceof PropsEntity && entity2 instanceof PropsEntity){
						if(am != null){
							Props a1 = (Props)am.getArticle(entity1.getArticleName());
							Props a2 = (Props)am.getArticle(entity2.getArticleName());
							if(a1 != null && a2 != null){
								if(a1 instanceof LastingProps && !(a2 instanceof LastingProps)){
									return -1;
								}
								if(!(a1 instanceof LastingProps) && a2 instanceof LastingProps){
									return 1;
								}
								if(a1 instanceof LastingProps && a2 instanceof LastingProps){
									if(a1.getLevelLimit() == a2.getLevelLimit()){
										if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
											return o2.getCount() - o1.getCount();
										}else{
											return entity1.getShowName().compareTo(entity2.getShowName());
										}
									}else{
										return a2.getLevelLimit() - a1.getLevelLimit();
									}
								}
								if(!(a1 instanceof LastingProps) && !(a2 instanceof LastingProps)){
									if(a1 instanceof SingleProps && !(a2 instanceof SingleProps)){
										return -1;
									}
									if(!(a1 instanceof SingleProps) && a2 instanceof SingleProps){
										return 1;
									}
									if(a1 instanceof SingleProps && a2 instanceof SingleProps){
										if(a1.getLevelLimit() == a2.getLevelLimit()){
											if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
												return o2.getCount() - o1.getCount();
											}else{
												return entity1.getShowName().compareTo(entity2.getShowName());
											}
										}else{
											return a2.getLevelLimit() - a1.getLevelLimit();
										}
									}
									if(!(a1 instanceof SingleProps) && !(a2 instanceof SingleProps)){
										if(a1 instanceof FormulaProps && !(a2 instanceof FormulaProps)){
											return -1;
										}
										if(!(a1 instanceof FormulaProps) && a2 instanceof FormulaProps){
											return 1;
										}
										if(a1 instanceof FormulaProps && a2 instanceof FormulaProps){
											if(a1.getLevelLimit() == a2.getLevelLimit()){
												if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
													return o2.getCount() - o1.getCount();
												}else{
													return entity1.getShowName().compareTo(entity2.getShowName());
												}
											}else{
												return a2.getLevelLimit() - a1.getLevelLimit();
											}
										}
										if(!(a1 instanceof FormulaProps) && !(a2 instanceof FormulaProps)){
											if(a1 instanceof TaskProps && !(a2 instanceof TaskProps)){
												return -1;
											}
											if(!(a1 instanceof TaskProps) && a2 instanceof TaskProps){
												return 1;
											}
											if(a1 instanceof TaskProps && a2 instanceof TaskProps){
												if(a1.getLevelLimit() == a2.getLevelLimit()){
													if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
														return o2.getCount() - o1.getCount();
													}else{
														return entity1.getShowName().compareTo(entity2.getShowName());
													}
												}else{
													return a2.getLevelLimit() - a1.getLevelLimit();
												}
											}
											if(!(a1 instanceof TaskProps) && !(a2 instanceof TaskProps)){
												if(a1 instanceof MountProps && !(a2 instanceof MountProps)){
													return -1;
												}
												if(!(a1 instanceof MountProps) && a2 instanceof MountProps){
													return 1;
												}
												if(a1 instanceof MountProps && a2 instanceof MountProps){
													if(a1.getLevelLimit() == a2.getLevelLimit()){
														if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
															return o2.getCount() - o1.getCount();
														}else{
															return entity1.getShowName().compareTo(entity2.getShowName());
														}
													}else{
														return a2.getLevelLimit() - a1.getLevelLimit();
													}
												}
												if(!(a1 instanceof MountProps) && !(a2 instanceof MountProps)){
													if(a1.getLevelLimit() == a2.getLevelLimit()){
														if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
															return o2.getCount() - o1.getCount();
														}else{
															return entity1.getShowName().compareTo(entity2.getShowName());
														}
													}else{
														return a2.getLevelLimit() - a1.getLevelLimit();
													}
												}
											}
										}
									}
								}
							}
						}
						//材料、镶嵌类宝石、强化类物品、打孔类物品
					}else{
						if(am != null){
							Article a1 = am.getArticle(entity1.getArticleName());
							Article a2 = am.getArticle(entity2.getArticleName());
							if(a1 != null && a2 != null){
								if(a1 instanceof MaterialArticle && !(a2 instanceof MaterialArticle)){
									return -1;
								}
								if(!(a1 instanceof MaterialArticle) && a2 instanceof MaterialArticle){
									return 1;
								}
								if(a1 instanceof MaterialArticle && a2 instanceof MaterialArticle){
									if(((MaterialArticle)a1).getMaterialType() == ((MaterialArticle)a2).getMaterialType()){
										if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
											return o2.getCount() - o1.getCount();
										}else{
											return entity1.getShowName().compareTo(entity2.getShowName());
										}
									}else{
										return ((MaterialArticle)a1).getMaterialType() - ((MaterialArticle)a2).getMaterialType();
									}
								}
								if(!(a1 instanceof MaterialArticle) && !(a2 instanceof MaterialArticle)){
									if(a1 instanceof InlayArticle && !(a2 instanceof InlayArticle)){
										return -1;
									}
									if(!(a1 instanceof InlayArticle) && a2 instanceof InlayArticle){
										return 1;
									}
									if(a1 instanceof InlayArticle && a2 instanceof InlayArticle){
										if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
											return o2.getCount() - o1.getCount();
										}else{
											return entity1.getShowName().compareTo(entity2.getShowName());
										}
									}
									if(!(a1 instanceof InlayArticle) && !(a2 instanceof InlayArticle)){
										if(a1 instanceof UpgradeArticle && !(a2 instanceof UpgradeArticle)){
											return -1;
										}
										if(!(a1 instanceof UpgradeArticle) && a2 instanceof UpgradeArticle){
											return 1;
										}
										if(a1 instanceof UpgradeArticle && a2 instanceof UpgradeArticle){
											if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
												return o2.getCount() - o1.getCount();
											}else{
												return entity1.getShowName().compareTo(entity2.getShowName());
											}
										}
										if(!(a1 instanceof UpgradeArticle) && !(a2 instanceof UpgradeArticle)){
											if(a1 instanceof AiguilleArticle && !(a2 instanceof AiguilleArticle)){
												return -1;
											}
											if(!(a1 instanceof AiguilleArticle) && a2 instanceof AiguilleArticle){
												return 1;
											}
											if(a1 instanceof AiguilleArticle && a2 instanceof AiguilleArticle){
												if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
													return o2.getCount() - o1.getCount();
												}else{
													return entity1.getShowName().compareTo(entity2.getShowName());
												}
											}
											if(!(a1 instanceof AiguilleArticle) && !(a2 instanceof AiguilleArticle)){
												if(entity1.getShowName().compareTo(entity2.getShowName()) == 0){
													return o2.getCount() - o1.getCount();
												}else{
													return entity1.getShowName().compareTo(entity2.getShowName());
												}
											}
										}
									}
								}
							}
						}
					}
				}else{
					return entity2.getColorType() - entity1.getColorType();
				}
			}
		}
		return 0;
	}
	
}
