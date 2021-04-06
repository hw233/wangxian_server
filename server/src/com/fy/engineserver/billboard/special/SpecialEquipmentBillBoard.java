package com.fy.engineserver.billboard.special;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.equipments.Equipment;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.xuanzhi.tools.cache.CacheListener;
import com.xuanzhi.tools.cache.Cacheable;
import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEntity;
import com.xuanzhi.tools.simplejpa.annotation.SimpleId;
import com.xuanzhi.tools.simplejpa.annotation.SimpleVersion;

@SimpleEntity(name = "specialeq")
public class SpecialEquipmentBillBoard implements Serializable, Cacheable, CacheListener {

	private static final long serialVersionUID = 3807691619906590878L;
	public static SpecialEquipmentCompare specialComparator = new SpecialEquipmentBillBoard.SpecialEquipmentCompare();

	public SpecialEquipmentBillBoard() {

	}

	public SpecialEquipmentBillBoard(long id) {
		this.id = id;
	}

	@SimpleId
	private long id = 1l;
	@SimpleVersion
	private int version;

	// 特殊装备1 装备名 装备id
	@SimpleColumn(length = 16000)
	private LinkedHashMap<String, List<Long>> special1Map = new LinkedHashMap<String, List<Long>>();
	@SimpleColumn(length = 80000)
	private LinkedHashMap<String, List<Long>> special2Map = new LinkedHashMap<String, List<Long>>();

	public String removeSpecial1(String equipmentName, long eqId) {
		synchronized (special1Map) {
			List<Long> list = special1Map.get(equipmentName);
			if (list != null && list.size() > 0) {
				int max = list.size();
				for (int i = 0; i < max; i++) {
					if (list.get(i) != null && list.get(i) == eqId) {
						list.remove(i);
						List<Entry<String, List<Long>>> lists = this.sortSpecial(special1Map);
						special1Map.clear();
						for (Entry<String, List<Long>> en : lists) {
							special1Map.put(en.getKey(), en.getValue());
						}
						setSpecial1Map(special1Map);

						SpecialEquipmentManager.logger.warn("[删除装备1] [" + equipmentName + "] [" + eqId + "]");
						return equipmentName;
					}
				}
				SpecialEquipmentManager.logger.error("[删除装备1错误] [" + equipmentName + "] [" + eqId + "]");
				// Long values = list.get(0);
				// if(values != null && values == eqId){
				// list.remove(0);
				//
				// List<Entry<String, List<Long>>> lists = this.sortSpecial(special1Map);
				// special1Map.clear();
				// for(Entry<String, List<Long>> en : lists){
				// special1Map.put(en.getKey(), en.getValue());
				// }
				// setSpecial1Map(special1Map);
				//
				// SpecialEquipmentManager.logger.warn("[删除装备1] ["+equipmentName+"] ["+eqId+"]");
				// return equipmentName;
				// }else{
				// SpecialEquipmentManager.logger.error("[删除装备1错误] [values:"+(values != null ? values:"null")+"] ["+equipmentName+"] ["+eqId+"]");
				// }
			} else {
				SpecialEquipmentManager.logger.error("[删除装备1错误] [没有这个装备] [" + equipmentName + "] [" + eqId + "]");
			}
			return null;
		}
	}

	public String removeSpecial2(String equipmentName, long eqId) {
		synchronized (special2Map) {
			List<Long> list = special2Map.get(equipmentName);
			if (list != null && list.size() > 0) {
				int max = list.size();
				for (int i = 0; i < max; i++) {
					if (list.get(i) != null && list.get(i) == eqId) {
						list.remove(i);
						List<Entry<String, List<Long>>> lists = this.sortSpecial(special2Map);
						special2Map.clear();
						for (Entry<String, List<Long>> en : lists) {
							special2Map.put(en.getKey(), en.getValue());
						}
						SpecialEquipmentManager.logger.warn("[删除装备2] [" + equipmentName + "] [" + eqId + "]");
						setSpecial2Map(special2Map);
						return equipmentName;
					}
				}
				SpecialEquipmentManager.logger.error("[删除装备2错误] [" + equipmentName + "] [" + eqId + "]");
			} else {
				SpecialEquipmentManager.logger.error("[删除装备2错误] [没有这个装备] [" + equipmentName + "] [" + eqId + "]");
			}
			return null;
		}
	}

	public String putSpecial1(String equipmentName, long eqId) {
		synchronized (special1Map) {
			try {
				List<Long> list = special1Map.get(equipmentName);
				if (list != null && list.size() == 0) {
					list.add(eqId);

					List<Entry<String, List<Long>>> lists = this.sortSpecial(special1Map);
					special1Map.clear();
					for (Entry<String, List<Long>> en : lists) {
						special1Map.put(en.getKey(), en.getValue());
					}
					setSpecial1Map(special1Map);
					SpecialEquipmentManager.logger.warn("[放入装备1] [" + equipmentName + "] [" + eqId + "]");
					return equipmentName;
				}
			} catch (Exception e) {
				SpecialEquipmentManager.logger.error("[放特殊1错误] [" + equipmentName + "]", e);
			}
			return null;
		}
	}

	public synchronized String putSpecial2(String equipmentName, long eqId) {
		synchronized (special2Map) {
			try {
				List<Long> list = special2Map.get(equipmentName);
				if (list != null) {

					for (long idd : list) {
						if (idd == eqId) {
							SpecialEquipmentManager.logger.warn("[重复放入装备2] [" + equipmentName + "] [" + eqId + "]");
							return null;
						}
					}
					list.add(eqId);
					List<Entry<String, List<Long>>> lists = this.sortSpecial(special2Map);
					special2Map.clear();
					for (Entry<String, List<Long>> en : lists) {
						special2Map.put(en.getKey(), en.getValue());
					}
					SpecialEquipmentManager.logger.warn("[放入装备2] [" + equipmentName + "] [" + eqId + "]");
					setSpecial2Map(special2Map);
					return equipmentName;
				}
			} catch (Exception e) {
				SpecialEquipmentManager.logger.error("[放特殊2错误] [" + equipmentName + "]", e);
			}
			return null;
		}
	}

	public synchronized List<Long> getSpeicail1(String equipmentName) {
		return this.special1Map.get(equipmentName);
	}

	public synchronized List<Long> getSpeicail2(String equipmentName) {
		return this.special2Map.get(equipmentName);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void remove(int type) {
		try {
			SpecialEquipmentManager.em.save(this);
		} catch (Exception e) {
			SpecialEquipmentManager.logger.error("[缓存中删除混沌万灵榜保存数据错误]", e);
		}
	}

	@Override
	public int getSize() {
		return 0;
	}

	public List<Entry<String, List<Long>>> sortSpecial(Map<String, List<Long>> map) {

		Set<Entry<String, List<Long>>> set = map.entrySet();
		List<Entry<String, List<Long>>> list = new ArrayList<Entry<String, List<Long>>>();
		for (Entry<String, List<Long>> e : set) {
			list.add(e);
		}
		Collections.sort(list, this.specialComparator);

		return list;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	static class SpecialEquipmentCompare implements Comparator<Entry<String, List<Long>>> {

		@Override
		public int compare(Entry<String, List<Long>> o1, Entry<String, List<Long>> o2) {

			ArticleManager am = ArticleManager.getInstance();
			String eq1Name = o1.getKey();
			String eq2Name = o2.getKey();

			Article a1 = am.getArticle(eq1Name);
			Article a2 = am.getArticle(eq2Name);

			if (a1 != null && a2 != null && a1 instanceof Equipment && a2 instanceof Equipment) {
				Equipment e1 = (Equipment) a1;
				Equipment e2 = (Equipment) a2;

				if (e2.getArticleLevel() > e1.getArticleLevel()) {
					return 1;
				}
				if (e2.getArticleLevel() == e1.getArticleLevel()) {
					List<Long> list1 = o1.getValue();
					List<Long> list2 = o2.getValue();

					if (list1 != null && list1.size() > 0 && list2 != null && list2.size() > 0) {
						ArticleEntityManager aem = ArticleEntityManager.getInstance();
						EquipmentEntity ee1 = (EquipmentEntity) aem.getEntity(list1.get(0));
						EquipmentEntity ee2 = (EquipmentEntity) aem.getEntity(list2.get(0));
						if (ee1 != null && ee2 != null) {

							long time1 = ee1.getCreateTime();
							long time2 = ee2.getCreateTime();
							if (time2 > time1) {
								return 1;
							}
						} else {
							ArticleManager.logger.error("[万灵榜装备不存在] [list1.get(0):" + list1.get(0) + "/" + ee1 + "] [list2.get(0):" + list2.get(0) + "/" + ee2 + "]");
							return 0;
						}
					} else {
						if (list1 != null && list1.size() > 0) {
							return 1;
						} else if (list2 != null && list2.size() > 0) {
							return -1;
						}
					}
				}
			} else {
				SpecialEquipmentManager.logger.error("[混沌万灵榜装备比较错误] [" + eq1Name + "] [" + eq2Name + "]");
			}
			return -1;
		}
	}

	public LinkedHashMap<String, List<Long>> getSpecial1Map() {
		return special1Map;
	}

	public void setSpecial1Map(LinkedHashMap<String, List<Long>> special1Map) {
		this.special1Map = special1Map;
		SpecialEquipmentManager.em.notifyFieldChange(this, "special1Map");
	}

	public LinkedHashMap<String, List<Long>> getSpecial2Map() {
		return special2Map;
	}

	public void setSpecial2Map(LinkedHashMap<String, List<Long>> special2Map) {
		this.special2Map = special2Map;
		SpecialEquipmentManager.em.notifyFieldChange(this, "special2Map");
	}

}
