package com.fy.engineserver.datasource.article.data.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.hunshi.HunshiManager;
import com.fy.engineserver.hunshi.HunshiPropValue;
import com.fy.engineserver.hunshi.HunshiSuit;
import com.fy.engineserver.hunshi.SuitSkill;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.sprite.horse.Horse;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;

@SimpleEntity
public class HunshiEntity extends PropsEntity {

	private static final long serialVersionUID = 1L;

	private int[] extraPropValue = new int[25]; // 副属性数组
	private int[] mainPropValue = new int[25]; // 主属性数组

	private int rongHeZhi; // 融合值

	private boolean jianding; // 是否鉴定过

	private int type;// 0-魂石；1-套装魂石

	private int holeIndex;// 带窍魂石的序号，如果是不带窍的魂石则为-1

	public HunshiEntity() {
	}

	public HunshiEntity(long id) {
		setId(id);
	}

	@Override
	public String getInfoShow(Player p) {
		HunshiManager hm = HunshiManager.getInstance();
		hm.logger.debug("[查看魂石泡泡] [" + p.getLogString() + "]");
		StringBuffer sb = new StringBuffer();
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return Translate.物品 + ":" + articleName;
		}
		Article a = am.getArticle(articleName);
		if (hm != null) {
			if (isJianding()) {
				sb.append("\n<f color='0xfffcff00' size='28'>").append(Translate.已鉴定).append("</f>");
				sb.append("\n");
				if (hm.hunshiPropIdMap.keySet().contains(a.getName_stat())) {
					// 主属性
					if (this.getColorType() >= 2 && getType() == 0) { // 蓝色以上才有主属性
						sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.基础属性).append("</f>");
						int[] propIds = hm.hunshiPropIdMap.get(a.getName_stat()).getMainPropId();
						if (propIds != null && propIds.length > 0) {
							for (Integer id : propIds) {
								if (id >= 0) {
									if (mainPropValue[id] > 0) {
										sb.append("\n<f color='0xffffffff' size='28'>").append(HunshiManager.propertyInfo[id]).append(":").append("</f><f size='28'>");
										sb.append(mainPropValue[id]).append("</f>");
									}
								}
							}
						}
						sb.append("\n");
					}
					// 附加属性
					sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.附加属性).append("</f>");
					int[] extraPropIds = hm.hunshiPropIdMap.get(a.getName_stat()).getExtraPropId();
					if (extraPropIds != null && extraPropIds.length > 0) {
						for (Integer id : extraPropIds) {
							if (extraPropValue[id] > 0) {
								sb.append("\n<f color='0xff009cff' size='28'>").append(HunshiManager.propertyInfo[id]).append(":").append("</f><f size='28'>");
								sb.append(extraPropValue[id]).append("</f>");
							}
						}
					}
				}
				if (extraPropValue[extraPropValue.length - 1] > 0) {
					sb.append("\n<f color='0xffff8400' size='28'>").append(Translate.附加融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(extraPropValue[extraPropValue.length - 1])).append("</f>");
				}
				sb.append("\n<f color='0xff1dcd00' size='28'>").append(Translate.基础融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(getRongHeZhi())).append("</f>");
				// 按区段分颜色显示基础融合率
				// int temprhg = getRongHeZhi();
				// if (temprhg <= 800) {
				// sb.append("\n<f color='0xffffffff' size='28'>").append(Translate.基础融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(getRongHeZhi())).append("</f>");
				// } else if (temprhg > 800 && temprhg <= 1200) {
				// sb.append("\n<f color='0xff1dcd00' size='28'>").append(Translate.基础融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(getRongHeZhi())).append("</f>");
				// } else if (temprhg > 1200 && temprhg <= 1500) {
				// sb.append("\n<f color='0xff009cff' size='28'>").append(Translate.基础融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(getRongHeZhi())).append("</f>");
				// } else if (temprhg > 1500 && temprhg <= 1800) {
				// sb.append("\n<f color='0xffe6028d' size='28'>").append(Translate.基础融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(getRongHeZhi())).append("</f>");
				// } else if (temprhg > 1800 && temprhg <= 2000) {
				// sb.append("\n<f color='0xffff8400' size='28'>").append(Translate.基础融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(getRongHeZhi())).append("</f>");
				// }

			} else {
				sb.append("\n<f size='28'>").append(Translate.未鉴定).append("</f>");
				sb.append("\n");
				if (this.getColorType() >= 2) {
					sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.基础属性).append("</f>");
					if (hm.hunshiPropIdMap.keySet().contains(a.getName_stat())) {
						int[] propIds = hm.hunshiPropIdMap.get(a.getName_stat()).getMainPropId();
						if (propIds != null && propIds.length > 0) {
							for (Integer id : propIds) {
								if (id >= 0) { // 蓝色以上才有主属性
									sb.append("\n<f color='0x00FF00' size='28'>").append(HunshiManager.propertyInfo[id]).append(":").append("</f><f size='28'>");
									Map<Integer, HunshiPropValue> hunshiMainPropMap = null;
									if (type == 0) {
										hunshiMainPropMap = hm.hunshiMainValueMap;
									} else if (type == 1) {
										hunshiMainPropMap = hm.hunshi2MainValueMap;
									}
									if (hunshiMainPropMap.get(id) != null) {
										HunshiPropValue propValue = hunshiMainPropMap.get(id);
										int[] propRange = propValue.getPropValue(getColorType());
										sb.append(propRange[0]).append("</f>");
									}
								}
							}
						}
					}
				}

				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.附加属性).append("</f>");
				if (getColorType() < 2) {
					sb.append("\n<f color='0xff009cff' size='28'>").append(Translate.translateString(Translate.随机属性, new String[][] { { Translate.COUNT_1, (getColorType() + 1) + "" } })).append("</f>");
				} else {
					sb.append("\n<f color='0xff009cff' size='28'>").append(Translate.translateString(Translate.随机属性, new String[][] { { Translate.COUNT_1, "3" } })).append("</f>");
				}
			}
			if (type == 1) {

				HunshiSuit suit = hm.getHunshiSuit(this);
				if (suit != null) {
					String[] hunshi2Names = suit.getHunshi2Names();
					for (String name : hunshi2Names) {
						if (name.equals(articleName)) {
							sb.append("\n<f color='0xffffffff' size='28'>").append(name).append("</f>");
						} else {
							sb.append("\n<f color='0xa6a6a6' size='28'>").append(name).append("</f>");
						}
					}
					int[] propId = suit.getPropId();
					for (int i = 0; i < propId.length; i++) {
						sb.append("\n<f color='0xa6a6a6' size='28'>").append(HunshiManager.propertyInfo[propId[i]]).append(":").append("</f><f color='0xa6a6a6' size='28'>").append(suit.getPropValue()[i]).append("</f>");

					}
					// 加技能描述
					SuitSkill skill = hm.suitSkillMap.get(suit.getSkillId());
					sb.append("\n<f color='0xa6a6a6' size='28'>").append(skill.getName()).append("</f>");
					sb.append("\n<f color='0xa6a6a6' size='28'>").append(skill.getDes()).append("</f>");

				}
			}
		}

		return super.getInfoShow(p) + sb.toString();
	}

	/**
	 * 查看某个玩家镶嵌在套装石面板的套装石
	 * @param p
	 * @param h
	 * @return
	 */
	public String getInfoShow(Player p, Horse h) {
		HunshiManager hm = HunshiManager.getInstance();
		hm.logger.debug("[查看镶嵌的套装石泡泡] [" + p.getLogString() + "]");
		StringBuffer sb = new StringBuffer();
		ArticleManager am = ArticleManager.getInstance();
		if (am == null) {
			return Translate.物品 + ":" + articleName;
		}
		Article a = am.getArticle(articleName);
		if (hm != null) {
			if (isJianding()) {
				sb.append("\n<f color='0xfffcff00' size='28'>").append(Translate.已鉴定).append("</f>");
				sb.append("\n");
				if (hm.hunshiPropIdMap.keySet().contains(a.getName_stat())) {
					// 主属性
					if (this.getColorType() >= 2 && getType() == 0) { // 蓝色以上才有主属性
						sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.基础属性).append("</f>");
						int[] propIds = hm.hunshiPropIdMap.get(a.getName_stat()).getMainPropId();
						if (propIds != null && propIds.length > 0) {
							for (Integer id : propIds) {
								if (id >= 0) {
									if (mainPropValue[id] > 0) {
										sb.append("\n<f color='0xffffffff' size='28'>").append(HunshiManager.propertyInfo[id]).append(":").append("</f><f size='28'>");
										sb.append(mainPropValue[id]).append("</f>");
									}
								}
							}
						}
						sb.append("\n");
					}
					// 附加属性
					sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.附加属性).append("</f>");
					int[] extraPropIds = hm.hunshiPropIdMap.get(a.getName_stat()).getExtraPropId();
					if (extraPropIds != null && extraPropIds.length > 0) {
						for (Integer id : extraPropIds) {
							if (extraPropValue[id] > 0) {
								sb.append("\n<f color='0xff009cff' size='28'>").append(HunshiManager.propertyInfo[id]).append(":").append("</f><f size='28'>");
								sb.append(extraPropValue[id]).append("</f>");
							}
						}
					}
				}
				if (extraPropValue[extraPropValue.length - 1] > 0) {
					sb.append("\n<f color='0xffff8400' size='28'>").append(Translate.附加融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(extraPropValue[extraPropValue.length - 1])).append("</f>");
				}
				sb.append("\n<f color='0xff1dcd00' size='28'>").append(Translate.基础融合率).append(":").append("</f><f size='28'>").append(hm.getPercent(getRongHeZhi())).append("</f>");

			} else {
				sb.append("\n<f size='28'>").append(Translate.未鉴定).append("</f>");
				sb.append("\n");
				if (this.getColorType() >= 2 && getType() == 0) {
					sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.基础属性).append("</f>");
					if (hm.hunshiPropIdMap.keySet().contains(a.getName_stat())) {
						int[] propIds = hm.hunshiPropIdMap.get(a.getName_stat()).getMainPropId();
						if (propIds != null && propIds.length > 0) {
							for (Integer id : propIds) {
								if (id >= 0) { // 蓝色以上才有主属性
									sb.append("\n<f color='0x00FF00' size='28'>").append(HunshiManager.propertyInfo[id]).append(":").append("</f><f size='28'>");
									Map<Integer, HunshiPropValue> hunshiMainPropMap = null;
									if (type == 0) {
										hunshiMainPropMap = hm.hunshiMainValueMap;
									} else if (type == 1) {
										hunshiMainPropMap = hm.hunshi2MainValueMap;
									}
									if (hunshiMainPropMap.get(id) != null) {
										HunshiPropValue propValue = hunshiMainPropMap.get(id);
										int[] propRange = propValue.getPropValue(getColorType());
										sb.append(propRange[0]).append("</f>");
									}
								}
							}
						}
					}
				}

				sb.append("\n<f color='0x00FF00' size='28'>").append(Translate.附加属性).append("</f>");
				if (getColorType() < 2) {
					sb.append("\n<f color='0xff009cff' size='28'>").append(Translate.translateString(Translate.随机属性, new String[][] { { Translate.COUNT_1, (getColorType() + 1) + "" } })).append("</f>");
				} else {
					sb.append("\n<f color='0xff009cff' size='28'>").append(Translate.translateString(Translate.随机属性, new String[][] { { Translate.COUNT_1, "3" } })).append("</f>");
				}
			}
			if (type == 1) {

				HunshiSuit suit = hm.getHunshiSuit(this);
				if (suit != null) {
					List<ArticleEntity> ll = new ArrayList<ArticleEntity>();
					long[] hunshi2Array = h.getHunshi2Array();
					for (int i = 0; i < hunshi2Array.length; i++) {
						if (hunshi2Array[i] > 0) {
							ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(hunshi2Array[i]);
							ll.add(ae);
						}
					}
					String[] hunshi2Names = suit.getHunshi2Names();
					int[] propId = suit.getPropId();
					SuitSkill skill = hm.suitSkillMap.get(suit.getSkillId());
					for (String name : hunshi2Names) {
						boolean has = false;
						for (ArticleEntity ae : ll) {
							if (ae.getArticleName().equals(name)) {
								has = true;
							} else {
								continue;
							}
						}
						if (has) {
							sb.append("\n<f color='0xffffffff' size='28'>").append(name).append("</f>");
						} else {
							sb.append("\n<f color='0xa6a6a6' size='28'>").append(name).append("</f>");
						}
					}
					if (hm.isAvailibale(suit, ll)) {
						for (int i = 0; i < propId.length; i++) {
							sb.append("\n<f color='0xffffffff' size='28'>").append(HunshiManager.propertyInfo[propId[i]]).append(":").append(suit.getPropValue()[i]).append("</f>");

						}
						// 加技能描述
						sb.append("\n\n<f color='0xfffcff00' size='28'>").append(skill.getName()).append("</f>");
						sb.append("\n<f color='0xfffcff00' size='28'>").append(skill.getDes()).append("</f>");

					} else {
						for (int i = 0; i < propId.length; i++) {
							sb.append("\n<f color='0xa6a6a6' size='28'>").append(HunshiManager.propertyInfo[propId[i]]).append(":").append("</f><f color='0xa6a6a6' size='28'>").append(suit.getPropValue()[i]).append("</f>");

						}
						// 加技能描述
						sb.append("\n<f color='0xa6a6a6' size='28'>").append(skill.getName()).append("</f>");
						sb.append("\n<f color='0xa6a6a6' size='28'>").append(skill.getDes()).append("</f>");
					}

				}
			}
		}

		return super.getInfoShow(p) + sb.toString();
	}

	public int[] getExtraPropValue() {
		return extraPropValue;
	}

	public void setExtraPropValue(int[] extraPropValue) {
		this.extraPropValue = extraPropValue;
		saveData("extraPropValue");
	}

	public int[] getMainPropValue() {
		return mainPropValue;
	}

	public void setMainPropValue(int[] mainPropValue) {
		this.mainPropValue = mainPropValue;
		saveData("mainPropValue");
	}

	public int getRongHeZhi() {
		return rongHeZhi;
	}

	public void setRongHeZhi(int rongHeZhi) {
		this.rongHeZhi = rongHeZhi;
		saveData("rongHeZhi");
	}

	public boolean isJianding() {
		return jianding;
	}

	public void setJianding(boolean jianding) {
		this.jianding = jianding;
		saveData("jianding");
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		saveData("type");
	}

	public int getHoleIndex() {
		return holeIndex;
	}

	public void setHoleIndex(int holeIndex) {
		this.holeIndex = holeIndex;
	}

}
