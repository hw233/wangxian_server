package com.fy.engineserver.datasource.article.data.props;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fy.engineserver.activity.dig.DigPropsEntity;
import com.fy.engineserver.activity.dig.DigTemplate;
import com.fy.engineserver.core.CoreSubSystem;
import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.articles.InlayArticle;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.EquipmentEntity;
import com.fy.engineserver.datasource.article.data.entity.HuntLifeArticleEntity;
import com.fy.engineserver.datasource.article.data.entity.PropsEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.message.GameMessageFactory;
import com.fy.engineserver.message.QUERY_ARTICLE_RES;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.stat.ArticleStatManager;
import com.fy.engineserver.time.Timer;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 背包，每个背包有固定的格子
 * 
 * 每个格子里面可以防止多个可叠加的物品
 * 
 */
@SimpleEmbeddable
public class Knapsack {

	public static final int MAX_CELL_NUM = 80;
	public static final byte NO_CHANGE = 0;
	public static final byte ADD_NEW_ARTICLE = 1;
	public static final byte REMOVE_ARTICLE = 2;
	public static final byte ADD_OLD_ARTICLE = 3;

	public static Logger logger = LoggerFactory.getLogger(Knapsack.class);

	public Knapsack() {
	}

	@Override
	protected void finalize() throws Throwable {

	}

	public Knapsack(Player owner) {
		this();
		this.owner = owner;
		this.setCells(new Cell[] { new Cell(), new Cell(), new Cell(), new Cell(), new Cell() });
		this.setCellChangeFlags(new byte[5]);
	}

	public Knapsack(Player owner, int numCell) {
		this();
		this.owner = owner;
		this.cells = new Cell[numCell];
		this.cellChangeFlags = new byte[numCell];
		for (int i = 0; i < numCell; i++) {
			this.cells[i] = new Cell();
		}
	}
	
	public transient String knapName = "";	//背包名，规范日志用

	public transient boolean loaded = false;
	transient Player owner;

	/**
	 * 扩展格子的数量
	 */
	transient int expandNum;

	/**
	 * 标记是否修改过，服务器会定期扫描，发现修改后，会存盘
	 */
	transient boolean modified = false;

	/**
	 * 格子变化的标记
	 * 0 标识没有变化
	 * 1 标识新添了物品
	 * 2 标识减少了物品数量
	 * 3 标记增加了物品数量
	 */
	public transient byte cellChangeFlags[];

	/**
	 * 格子的数组
	 */

	protected Cell cells[];

	public void setCells(Cell[] cells) {
		this.cells = cells;
		setModified(true);
	}

	public void addCells(int count) {
		int oldCellsNum = this.cells.length;
		int newCellsNum = oldCellsNum + count;
		Cell[] newCells = new Cell[newCellsNum];
		byte newCellChangeFlags[] = new byte[newCellsNum];
		for (int i = 0; i < cells.length; i++) {
			newCells[i] = cells[i];
			newCellChangeFlags[i] = cellChangeFlags[i];
		}
		for (int i = cells.length; i < newCellsNum; i++) {
			newCells[i] = new Cell();
		}
		cellChangeFlags = newCellChangeFlags;
		this.cells = newCells;
		setModified(true);
	}

	public int getExpandNum() {
		return expandNum;
	}

	public void setExpandNum(int expandNum) {
		this.expandNum = expandNum;
		setModified(true);
	}

	public void updateCell(int index, Cell cell) {
		cells[index] = cell;
		cellChangeFlags[index] = REMOVE_ARTICLE;
		setModified(true);
	}

	public void init(Player owner, int size) {
		this.owner = owner;
		if (cellChangeFlags == null || cellChangeFlags.length == 0) {
			cellChangeFlags = new byte[size];
		}
		if (cells == null || cells.length == 0) {
			cells = new Cell[size];
			for (int i = 0; i < size; i++) {
				cells[i] = new Cell();
			}
		}
	}

	public void reset(Player owner, int size) {
		this.owner = owner;
		cellChangeFlags = new byte[size];
		if (cells == null) {
			cells = new Cell[size];
			for (int i = 0; i < size; i++) {
				cells[i] = new Cell();
			}
		} else {
			Cell cs[] = new Cell[size];
			for (int i = 0; i < cs.length && i < cells.length; i++) {
				cs[i] = cells[i];
			}
			cells = cs;
		}
	}

	public byte[] getCellChangeFlags() {
		return cellChangeFlags;
	}

	public void clearChangeFlag() {
		for (int i = 0; i < cellChangeFlags.length; i++) {
			cellChangeFlags[i] = 0;
		}
	}

	/**
	 * 得到某个单元格上的物品
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public ArticleEntity getArticleEntityByCell(int index) {
		if (index < 0 || index >= cells.length) {
			return null;
		}
		Cell c = cells[index];
		if (c == null || c.count <= 0) return null;
		if (c.entityId == -1) return null;

		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		return aem.getEntity(c.entityId);
	}

	public Cell getCell(int index) {
		if (index < 0 || index >= cells.length) {
			return null;
		}
		return cells[index];
	}

	public Cell[] getCells() {
		return cells;
	}

	/**
	 * 自动整理背包
	 */
	public void autoArrange() {
		long autoStartTime1 = System.currentTimeMillis();
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
		if (owner == null) {
			return;
		}
		if (owner != null) {
			owner.loadAllKnapsack();
		}
		// 先把能叠放的进行叠放
		for (int i = cells.length - 1; i >= 0; i--) {
			if (cells[i] == null) {
				cells[i] = new Cell();
			}
			if (cells[i].getEntityId() >= 0 && cells[i].getCount() == 0) {
				cells[i].setEntityId(-1);
			}
			if (cells[i].getEntityId() < 0) {
				continue;
			}
			for (int j = 0; j < i; j++) {
				if (cells[j] == null) {
					cells[j] = new Cell();
				}
				if (cells[j].getEntityId() >= 0 && cells[j].getCount() == 0) {
					cells[j].setEntityId(-1);
				}
				if (cells[j].getEntityId() < 0) {
					continue;
				}
				if (cells[i].getEntityId() > 0) {
					ArticleEntity entity = aemanager.getEntity(cells[i].getEntityId());
					Article article = (entity == null ? null : am.getArticle(entity.getArticleName()));
					ArticleEntity entityj = aemanager.getEntity(cells[j].getEntityId());
					Article articlej = (entity == null ? null : am.getArticle(entityj.getArticleName()));
					if (cells[i].getEntityId() == cells[j].getEntityId()) {
						int jcount = cells[j].count;
						int icount = cells[i].count;
						if (article != null && jcount < article.getOverLapNum()) {
							if (jcount + icount > article.getOverLapNum()) {
								cells[j].count = article.getOverLapNum();
								cells[i].count = jcount + icount - article.getOverLapNum();
							} else {
								cells[j].count += cells[i].count;
								cells[i].count = 0;
								cells[i].entityId = -1;
								break;
							}
						}
					} else if (article.isOverlap() && entity.isBinded() == entityj.isBinded() && entity.getColorType() == entityj.getColorType() && !article.isHaveValidDays() && article.getName().equals(articlej.getName())) {
						// 名字相同，可堆叠，绑定相同，颜色相同，不是有期限的物品
						int jcount = cells[j].count;
						int icount = cells[i].count;
						if (article != null && jcount < article.getOverLapNum()) {
							if (jcount + icount > article.getOverLapNum()) {
								cells[j].count = article.getOverLapNum();
								cells[i].count = jcount + icount - article.getOverLapNum();
							} else {
								cells[j].count += cells[i].count;
								cells[i].count = 0;
								cells[i].entityId = -1;
								break;
							}
						}
					}
				}
			}
		}
		long autoStartTime2 = System.currentTimeMillis() - autoStartTime1;
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		// 合并背包物品可堆叠有时间限制
		for (int i = cells.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (cells[i].getEntityId() > 0 && cells[j].getEntityId() > 0 && (cells[i].getEntityId() != cells[j].getEntityId())) {
					ArticleEntity ae1 = aemanager.getEntity(cells[i].getEntityId());
					ArticleEntity ae2 = aemanager.getEntity(cells[j].getEntityId());

					if (ae1 != null && ae2 != null) {
						if (ae1.getArticleName().equals(ae2.getArticleName()) && ae1.getColorType() == ae2.getColorType() && ae1.isBinded() == ae2.isBinded()) {
							Article a = am.getArticle(ae1.getArticleName());
							if (a != null) {
								if (a.isOverlap() && a.isHaveValidDays()) {
									if (cells[i].count < a.getOverLapNum() && cells[j].count < a.getOverLapNum()) {
										// 有时间限制的可堆叠物品进行合并
										Timer timer1 = ae1.getTimer();
										Timer timer2 = ae2.getTimer();
										if (timer1 == null || timer2 == null) {
											continue;
										}
										int moveCount = cells[i].count;
										if (a.getOverLapNum() < cells[i].count + cells[j].count) {
											moveCount = a.getOverLapNum() - cells[j].count;
										}
										// 计算时间，时长为总时长除以总个数
										long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
										long zongshichang = (timer1.getEndTime() - now) * moveCount + (timer2.getEndTime() - now) * cells[j].count;
										long endTime = now + zongshichang / (moveCount + cells[j].count);

										// //////
										{
											// 新创建一个物品，来满足时间改变。
											boolean binded = true;
											if (!ae2.isBinded() && !ae1.isBinded()) {
												binded = false;
											}
											try {
												ArticleEntity newAe = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_merge_Article, owner, ae1.getColorType(), (cells[j].count + moveCount), true);
												if (newAe != null && newAe.getTimer() != null) {
													newAe.getTimer().setEndTime(endTime);
													newAe.setTimer(newAe.getTimer());
													newAe.setEndTime(endTime);
												}
												if (newAe != null) {
													cells[j].entityId = newAe.getId();
												}
											} catch (Exception ex) {

											}
										}

										if (cells[i].count > moveCount) {
											cells[i].count = cells[i].count - moveCount;
										} else {
											cells[i].entityId = -1;
											cells[i].count = 0;
										}

										// 设置原来两个物品的引用计数，因为从两个物品变成了一个新物品，所以原来两个物品都需要减去相应的引用计数
										ae1.setReferenceCount(ae1.getReferenceCount() - moveCount);
										ae2.setReferenceCount(ae2.getReferenceCount() - cells[j].count);

										cells[j].count = cells[j].count + moveCount;
										// //////
									}
								}
							}
						}
					}
				}
			}
		}
		long autoStartTime3 = System.currentTimeMillis() - autoStartTime1 - autoStartTime2;
		// 一级分类:"角色装备类", "马匹装备类", "宝石类", "任务类", "人物消耗品类", "宠物消耗品", "庄园消耗品", "宠物蛋", "古董类", "庄园果实类", "材料类", "角色药品", "宠物药品", "角色技能书类", "宠物技能书类", "包裹类", "宝箱类"
		// 放入不同类型的list
		List<Cell> 角色装备类_list = new ArrayList<Cell>();
//		List<Cell> 角色法宝类_list = new ArrayList<Cell>();
//		List<Cell> 角色翅膀类_list = new ArrayList<Cell>();
		List<Cell> 马匹装备类_list = new ArrayList<Cell>();
		List<Cell> 宝石类_list = new ArrayList<Cell>();
		List<Cell> 任务类_list = new ArrayList<Cell>();
		List<Cell> 人物消耗品类_list = new ArrayList<Cell>();
		List<Cell> 宠物消耗品_list = new ArrayList<Cell>();
		List<Cell> 庄园消耗品_list = new ArrayList<Cell>();
		List<Cell> 宠物蛋_list = new ArrayList<Cell>();
		List<Cell> 古董类_list = new ArrayList<Cell>();
		List<Cell> 庄园果实类_list = new ArrayList<Cell>();
		List<Cell> 材料类_list = new ArrayList<Cell>();
		List<Cell> 角色药品_list = new ArrayList<Cell>();
		List<Cell> 宠物药品_list = new ArrayList<Cell>();
		List<Cell> 角色技能书类_list = new ArrayList<Cell>();
		List<Cell> 宠物技能书类_list = new ArrayList<Cell>();
		List<Cell> 包裹类_list = new ArrayList<Cell>();
		List<Cell> 宝箱类_list = new ArrayList<Cell>();
		List<Cell> 其他类_list = new ArrayList<Cell>();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i].getEntityId() != -1 && cells[i].getCount() > 0) {
				ArticleEntity entity = aemanager.getEntity(cells[i].getEntityId());
				if (entity == null) {
					logger.error("[整理背包] [{}] [错误] [物品不存在:{}] [下标:{}] [owner:{}] [count:{}]", new Object[] { this.getClass().getSimpleName(), cells[i].getEntityId(), i, owner.getLogString(), cells[i].count });
					return;
				}
				Article a = am.getArticle(entity.getArticleName());
				if (a == null) {
					logger.error("[整理背包] [{}] [错误] [物品物种不存在:{}] [下标:{}] [owner:{}] [count:{}]", new Object[] { this.getClass().getSimpleName(), cells[i].getEntityId(), i, owner.getLogString(), cells[i].count });
					return;
				}

				if (Translate.角色装备类.equals(a.get物品一级分类())) {
					角色装备类_list.add(cells[i]);
				} else if (Translate.马匹装备类.equals(a.get物品一级分类())) {
					马匹装备类_list.add(cells[i]);
				} else if (Translate.宝石类.equals(a.get物品一级分类())) {
					宝石类_list.add(cells[i]);
				} else if (Translate.任务类.equals(a.get物品一级分类())) {
					任务类_list.add(cells[i]);
				} else if (Translate.人物消耗品类.equals(a.get物品一级分类())) {
					人物消耗品类_list.add(cells[i]);
				} else if (Translate.宠物消耗品.equals(a.get物品一级分类())) {
					宠物消耗品_list.add(cells[i]);
				} else if (Translate.庄园消耗品.equals(a.get物品一级分类())) {
					庄园消耗品_list.add(cells[i]);
				} else if (Translate.宠物蛋.equals(a.get物品一级分类())) {
					宠物蛋_list.add(cells[i]);
				} else if (Translate.古董类.equals(a.get物品一级分类())) {
					古董类_list.add(cells[i]);
				} else if (Translate.庄园果实类.equals(a.get物品一级分类())) {
					庄园果实类_list.add(cells[i]);
				} else if (Translate.材料类.equals(a.get物品一级分类())) {
					材料类_list.add(cells[i]);
				} else if (Translate.角色药品.equals(a.get物品一级分类())) {
					角色药品_list.add(cells[i]);
				} else if (Translate.宠物药品.equals(a.get物品一级分类())) {
					宠物药品_list.add(cells[i]);
				} else if (Translate.角色技能书类.equals(a.get物品一级分类())) {
					角色技能书类_list.add(cells[i]);
				} else if (Translate.宠物技能书类.equals(a.get物品一级分类())) {
					宠物技能书类_list.add(cells[i]);
				} else if (Translate.包裹类.equals(a.get物品一级分类())) {
					包裹类_list.add(cells[i]);
				} else if (Translate.宝箱类.equals(a.get物品一级分类())) {
					宝箱类_list.add(cells[i]);
				} else {
					其他类_list.add(cells[i]);
				}
			}
		}
		// 按物品显示名称的hashcode排序
		if (角色装备类_list.size() > 0) {
			Collections.sort(角色装备类_list, new CellComparator());
		}
		if (马匹装备类_list.size() > 0) {
			Collections.sort(马匹装备类_list, new CellComparator());
		}
		if (宝石类_list.size() > 0) {
			Collections.sort(宝石类_list, new CellComparator());
		}
		if (任务类_list.size() > 0) {
			Collections.sort(任务类_list, new CellComparator());
		}
		if (人物消耗品类_list.size() > 0) {
			Collections.sort(人物消耗品类_list, new CellComparator());
		}
		if (宠物消耗品_list.size() > 0) {
			Collections.sort(宠物消耗品_list, new CellComparator());
		}
		if (庄园消耗品_list.size() > 0) {
			Collections.sort(庄园消耗品_list, new CellComparator());
		}
		if (宠物蛋_list.size() > 0) {
			Collections.sort(宠物蛋_list, new CellComparator());
		}
		if (古董类_list.size() > 0) {
			Collections.sort(古董类_list, new CellComparator());
		}
		if (庄园果实类_list.size() > 0) {
			Collections.sort(庄园果实类_list, new CellComparator());
		}
		if (材料类_list.size() > 0) {
			Collections.sort(材料类_list, new CellComparator());
		}
		if (角色药品_list.size() > 0) {
			Collections.sort(角色药品_list, new CellComparator());
		}
		if (宠物药品_list.size() > 0) {
			Collections.sort(宠物药品_list, new CellComparator());
		}
		if (角色技能书类_list.size() > 0) {
			Collections.sort(角色技能书类_list, new CellComparator());
		}
		if (宠物技能书类_list.size() > 0) {
			Collections.sort(宠物技能书类_list, new CellComparator());
		}
		if (包裹类_list.size() > 0) {
			Collections.sort(包裹类_list, new CellComparator());
		}
		if (宝箱类_list.size() > 0) {
			Collections.sort(宝箱类_list, new CellComparator());
		}
		if (其他类_list.size() > 0) {
			Collections.sort(其他类_list, new CellComparator());
		}
		// 重置，堆放顺序,装备、道具、一般物品
		Cell newCells[] = new Cell[cells.length];
		int index = 0;
		for (int i = 0; i < 角色装备类_list.size(); i++) {
			newCells[index] = 角色装备类_list.get(i);
			index++;
		}
		for (int i = 0; i < 马匹装备类_list.size(); i++) {
			newCells[index] = 马匹装备类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宝石类_list.size(); i++) {
			newCells[index] = 宝石类_list.get(i);
			index++;
		}
		for (int i = 0; i < 任务类_list.size(); i++) {
			newCells[index] = 任务类_list.get(i);
			index++;
		}
		for (int i = 0; i < 人物消耗品类_list.size(); i++) {
			newCells[index] = 人物消耗品类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物消耗品_list.size(); i++) {
			newCells[index] = 宠物消耗品_list.get(i);
			index++;
		}
		for (int i = 0; i < 庄园消耗品_list.size(); i++) {
			newCells[index] = 庄园消耗品_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物蛋_list.size(); i++) {
			newCells[index] = 宠物蛋_list.get(i);
			index++;
		}
		for (int i = 0; i < 古董类_list.size(); i++) {
			newCells[index] = 古董类_list.get(i);
			index++;
		}
		for (int i = 0; i < 庄园果实类_list.size(); i++) {
			newCells[index] = 庄园果实类_list.get(i);
			index++;
		}
		for (int i = 0; i < 材料类_list.size(); i++) {
			newCells[index] = 材料类_list.get(i);
			index++;
		}
		for (int i = 0; i < 角色药品_list.size(); i++) {
			newCells[index] = 角色药品_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物药品_list.size(); i++) {
			newCells[index] = 宠物药品_list.get(i);
			index++;
		}
		for (int i = 0; i < 角色技能书类_list.size(); i++) {
			newCells[index] = 角色技能书类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物技能书类_list.size(); i++) {
			newCells[index] = 宠物技能书类_list.get(i);
			index++;
		}
		for (int i = 0; i < 包裹类_list.size(); i++) {
			newCells[index] = 包裹类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宝箱类_list.size(); i++) {
			newCells[index] = 宝箱类_list.get(i);
			index++;
		}
		for (int i = 0; i < 其他类_list.size(); i++) {
			newCells[index] = 其他类_list.get(i);
			index++;
		}
		for (int i = index; i < newCells.length; i++) {
			newCells[i] = new Cell();
		}
		this.cells = newCells;
		long autoStartTime4 = System.currentTimeMillis() - autoStartTime1 - autoStartTime2 - autoStartTime3;
		if (logger.isInfoEnabled()) {
			logger.info("[autoArrange] [{}] [success] [-] [owner:{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getName() });
		}
		setModified(true);
		logger.warn("[背包整理时间] 玩家ID[{}] 物品数目[{}] 第一个循环[{}] 第二次[{}] 排序[{}] 总[{}]", new Object[] { owner.getId() + "-" + owner.getName(), cells.length - getEmptyNum(), autoStartTime2, autoStartTime3, autoStartTime4, System.currentTimeMillis() - autoStartTime1 });
	}

	/**
	 * 自动整理背包,其他人别用
	 */
	public void autoArrange123() {
		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
		if (owner != null) {
			owner.loadAllKnapsack();
		}
		// 先把能叠放的进行叠放
		for (int i = cells.length - 1; i >= 0; i--) {
			if (cells[i] == null) {
				cells[i] = new Cell();
			}
			if (cells[i].getEntityId() >= 0 && cells[i].getCount() == 0) {
				cells[i].setEntityId(-1);
			}
			for (int j = 0; j < i; j++) {
				if (cells[j] == null) {
					cells[j] = new Cell();
				}
				if (cells[j].getEntityId() >= 0 && cells[j].getCount() == 0) {
					cells[j].setEntityId(-1);
				}
				if (cells[i].getEntityId() > 0 && (cells[i].getEntityId() == cells[j].getEntityId())) {
					ArticleEntity entity = aemanager.getEntity(cells[i].getEntityId());
					Article article = (entity == null ? null : am.getArticle(entity.getArticleName()));
					int jcount = cells[j].count;
					int icount = cells[i].count;
					if (article != null && jcount < article.getOverLapNum()) {
						if (jcount + icount > article.getOverLapNum()) {
							cells[j].count = article.getOverLapNum();
							cells[i].count = jcount + icount - article.getOverLapNum();
						} else {
							cells[j].count += cells[i].count;
							cells[i].count = 0;
							cells[i].entityId = -1;
							break;
						}
					}
				}
			}
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		// 合并背包物品可堆叠有时间限制
		for (int i = cells.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (cells[i].getEntityId() > 0 && cells[j].getEntityId() > 0 && (cells[i].getEntityId() != cells[j].getEntityId())) {
					ArticleEntity ae1 = aemanager.getEntity(cells[i].getEntityId());
					ArticleEntity ae2 = aemanager.getEntity(cells[j].getEntityId());

					if (ae1 != null && ae2 != null) {
						if (ae1.getArticleName().equals(ae2.getArticleName()) && ae1.getColorType() == ae2.getColorType() && ae1.isBinded() == ae2.isBinded()) {
							Article a = am.getArticle(ae1.getArticleName());
							if (a != null) {
								if (a.isOverlap() && a.isHaveValidDays()) {
									if (cells[i].count < a.getOverLapNum() && cells[j].count < a.getOverLapNum()) {
										// 有时间限制的可堆叠物品进行合并
										Timer timer1 = ae1.getTimer();
										Timer timer2 = ae2.getTimer();
										if (timer1 == null || timer2 == null) {
											continue;
										}
										int moveCount = cells[i].count;
										if (a.getOverLapNum() < cells[i].count + cells[j].count) {
											moveCount = a.getOverLapNum() - cells[j].count;
										}
										// 计算时间，时长为总时长除以总个数
										long now = com.fy.engineserver.gametime.SystemTime.currentTimeMillis();
										long zongshichang = (timer1.getEndTime() - now) * moveCount + (timer2.getEndTime() - now) * cells[j].count;
										long endTime = now + zongshichang / (moveCount + cells[j].count);

										// //////
										{
											// 新创建一个物品，来满足时间改变。
											boolean binded = true;
											if (!ae2.isBinded() && !ae1.isBinded()) {
												binded = false;
											}
											try {
												ArticleEntity newAe = aem.createEntity(a, binded, ArticleEntityManager.CREATE_REASON_merge_Article, owner, ae1.getColorType(), (cells[j].count + moveCount), true);
												if (newAe != null && newAe.getTimer() != null) {
													newAe.getTimer().setEndTime(endTime);
													newAe.setTimer(newAe.getTimer());
													newAe.setEndTime(endTime);
												}
												if (newAe != null) {
													cells[j].entityId = newAe.getId();
												}
											} catch (Exception ex) {

											}
										}

										if (cells[i].count > moveCount) {
											cells[i].count = cells[i].count - moveCount;
										} else {
											cells[i].entityId = -1;
											cells[i].count = 0;
										}

										// 设置原来两个物品的引用计数，因为从两个物品变成了一个新物品，所以原来两个物品都需要减去相应的引用计数
										ae1.setReferenceCount(ae1.getReferenceCount() - moveCount);
										ae2.setReferenceCount(ae2.getReferenceCount() - cells[j].count);

										cells[j].count = cells[j].count + moveCount;
										// //////
									}
								}
							}
						}
					}
				}
			}
		}

		// 一级分类:"角色装备类", "马匹装备类", "宝石类", "任务类", "人物消耗品类", "宠物消耗品", "庄园消耗品", "宠物蛋", "古董类", "庄园果实类", "材料类", "角色药品", "宠物药品", "角色技能书类", "宠物技能书类", "包裹类", "宝箱类"
		// 放入不同类型的list
		List<Cell> 角色装备类_list = new ArrayList<Cell>();
		List<Cell> 马匹装备类_list = new ArrayList<Cell>();
		List<Cell> 宝石类_list = new ArrayList<Cell>();
		List<Cell> 任务类_list = new ArrayList<Cell>();
		List<Cell> 人物消耗品类_list = new ArrayList<Cell>();
		List<Cell> 宠物消耗品_list = new ArrayList<Cell>();
		List<Cell> 庄园消耗品_list = new ArrayList<Cell>();
		List<Cell> 宠物蛋_list = new ArrayList<Cell>();
		List<Cell> 古董类_list = new ArrayList<Cell>();
		List<Cell> 庄园果实类_list = new ArrayList<Cell>();
		List<Cell> 材料类_list = new ArrayList<Cell>();
		List<Cell> 角色药品_list = new ArrayList<Cell>();
		List<Cell> 宠物药品_list = new ArrayList<Cell>();
		List<Cell> 角色技能书类_list = new ArrayList<Cell>();
		List<Cell> 宠物技能书类_list = new ArrayList<Cell>();
		List<Cell> 包裹类_list = new ArrayList<Cell>();
		List<Cell> 宝箱类_list = new ArrayList<Cell>();
		List<Cell> 其他类_list = new ArrayList<Cell>();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i].getEntityId() != -1 && cells[i].getCount() > 0) {
				ArticleEntity entity = aemanager.getEntity(cells[i].getEntityId());
				if (entity == null) {
					logger.error("[整理背包] [{}] [错误] [物品不存在:{}] [下标:{}]", new Object[] { this.getClass().getSimpleName(), cells[i].getEntityId(), i });
					continue;
				}
				Article a = am.getArticle(entity.getArticleName());
				if (a == null) {
					logger.error("[整理背包] [{}] [错误] [物品物种不存在:{}] [下标:{}]", new Object[] { this.getClass().getSimpleName(), cells[i].getEntityId(), i });
					continue;
				}

				if (Translate.角色装备类.equals(a.get物品一级分类())) {
					角色装备类_list.add(cells[i]);
				} else if (Translate.马匹装备类.equals(a.get物品一级分类())) {
					马匹装备类_list.add(cells[i]);
				} else if (Translate.宝石类.equals(a.get物品一级分类())) {
					宝石类_list.add(cells[i]);
				} else if (Translate.任务类.equals(a.get物品一级分类())) {
					任务类_list.add(cells[i]);
				} else if (Translate.人物消耗品类.equals(a.get物品一级分类())) {
					人物消耗品类_list.add(cells[i]);
				} else if (Translate.宠物消耗品.equals(a.get物品一级分类())) {
					宠物消耗品_list.add(cells[i]);
				} else if (Translate.庄园消耗品.equals(a.get物品一级分类())) {
					庄园消耗品_list.add(cells[i]);
				} else if (Translate.宠物蛋.equals(a.get物品一级分类())) {
					宠物蛋_list.add(cells[i]);
				} else if (Translate.古董类.equals(a.get物品一级分类())) {
					古董类_list.add(cells[i]);
				} else if (Translate.庄园果实类.equals(a.get物品一级分类())) {
					庄园果实类_list.add(cells[i]);
				} else if (Translate.材料类.equals(a.get物品一级分类())) {
					材料类_list.add(cells[i]);
				} else if (Translate.角色药品.equals(a.get物品一级分类())) {
					角色药品_list.add(cells[i]);
				} else if (Translate.宠物药品.equals(a.get物品一级分类())) {
					宠物药品_list.add(cells[i]);
				} else if (Translate.角色技能书类.equals(a.get物品一级分类())) {
					角色技能书类_list.add(cells[i]);
				} else if (Translate.宠物技能书类.equals(a.get物品一级分类())) {
					宠物技能书类_list.add(cells[i]);
				} else if (Translate.包裹类.equals(a.get物品一级分类())) {
					包裹类_list.add(cells[i]);
				} else if (Translate.宝箱类.equals(a.get物品一级分类())) {
					宝箱类_list.add(cells[i]);
				} else {
					其他类_list.add(cells[i]);
				}
			}
		}
		// 按物品显示名称的hashcode排序
		if (角色装备类_list.size() > 0) {
			Collections.sort(角色装备类_list, new CellComparator());
		}
		if (马匹装备类_list.size() > 0) {
			Collections.sort(马匹装备类_list, new CellComparator());
		}
		if (宝石类_list.size() > 0) {
			Collections.sort(宝石类_list, new CellComparator());
		}
		if (任务类_list.size() > 0) {
			Collections.sort(任务类_list, new CellComparator());
		}
		if (人物消耗品类_list.size() > 0) {
			Collections.sort(人物消耗品类_list, new CellComparator());
		}
		if (宠物消耗品_list.size() > 0) {
			Collections.sort(宠物消耗品_list, new CellComparator());
		}
		if (庄园消耗品_list.size() > 0) {
			Collections.sort(庄园消耗品_list, new CellComparator());
		}
		if (宠物蛋_list.size() > 0) {
			Collections.sort(宠物蛋_list, new CellComparator());
		}
		if (古董类_list.size() > 0) {
			Collections.sort(古董类_list, new CellComparator());
		}
		if (庄园果实类_list.size() > 0) {
			Collections.sort(庄园果实类_list, new CellComparator());
		}
		if (材料类_list.size() > 0) {
			Collections.sort(材料类_list, new CellComparator());
		}
		if (角色药品_list.size() > 0) {
			Collections.sort(角色药品_list, new CellComparator());
		}
		if (宠物药品_list.size() > 0) {
			Collections.sort(宠物药品_list, new CellComparator());
		}
		if (角色技能书类_list.size() > 0) {
			Collections.sort(角色技能书类_list, new CellComparator());
		}
		if (宠物技能书类_list.size() > 0) {
			Collections.sort(宠物技能书类_list, new CellComparator());
		}
		if (包裹类_list.size() > 0) {
			Collections.sort(包裹类_list, new CellComparator());
		}
		if (宝箱类_list.size() > 0) {
			Collections.sort(宝箱类_list, new CellComparator());
		}
		if (其他类_list.size() > 0) {
			Collections.sort(其他类_list, new CellComparator());
		}
		// 重置，堆放顺序,装备、道具、一般物品
		Cell newCells[] = new Cell[cells.length];
		int index = 0;
		for (int i = 0; i < 角色装备类_list.size(); i++) {
			newCells[index] = 角色装备类_list.get(i);
			index++;
		}
		for (int i = 0; i < 马匹装备类_list.size(); i++) {
			newCells[index] = 马匹装备类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宝石类_list.size(); i++) {
			newCells[index] = 宝石类_list.get(i);
			index++;
		}
		for (int i = 0; i < 任务类_list.size(); i++) {
			newCells[index] = 任务类_list.get(i);
			index++;
		}
		for (int i = 0; i < 人物消耗品类_list.size(); i++) {
			newCells[index] = 人物消耗品类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物消耗品_list.size(); i++) {
			newCells[index] = 宠物消耗品_list.get(i);
			index++;
		}
		for (int i = 0; i < 庄园消耗品_list.size(); i++) {
			newCells[index] = 庄园消耗品_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物蛋_list.size(); i++) {
			newCells[index] = 宠物蛋_list.get(i);
			index++;
		}
		for (int i = 0; i < 古董类_list.size(); i++) {
			newCells[index] = 古董类_list.get(i);
			index++;
		}
		for (int i = 0; i < 庄园果实类_list.size(); i++) {
			newCells[index] = 庄园果实类_list.get(i);
			index++;
		}
		for (int i = 0; i < 材料类_list.size(); i++) {
			newCells[index] = 材料类_list.get(i);
			index++;
		}
		for (int i = 0; i < 角色药品_list.size(); i++) {
			newCells[index] = 角色药品_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物药品_list.size(); i++) {
			newCells[index] = 宠物药品_list.get(i);
			index++;
		}
		for (int i = 0; i < 角色技能书类_list.size(); i++) {
			newCells[index] = 角色技能书类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宠物技能书类_list.size(); i++) {
			newCells[index] = 宠物技能书类_list.get(i);
			index++;
		}
		for (int i = 0; i < 包裹类_list.size(); i++) {
			newCells[index] = 包裹类_list.get(i);
			index++;
		}
		for (int i = 0; i < 宝箱类_list.size(); i++) {
			newCells[index] = 宝箱类_list.get(i);
			index++;
		}
		for (int i = 0; i < 其他类_list.size(); i++) {
			newCells[index] = 其他类_list.get(i);
			index++;
		}
		for (int i = index; i < newCells.length; i++) {
			newCells[i] = new Cell();
		}
		this.cells = newCells;
		if (logger.isInfoEnabled()) {
			logger.info("[autoArrange] [{}] [success] [-] [owner:{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getName() });
		}
		setModified(true);

	}

	/**
	 * 获取空格子的数量
	 * 
	 * @return
	 */
	public int getEmptyNum() {
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null || cells[i].entityId == -1 || cells[i].count <= 0) c++;
		}
		return c;
	}

	/**
	 * 判断背包是否已满
	 * 
	 * @return
	 */
	public boolean isFull() {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null || cells[i].entityId == -1 || cells[i].count <= 0) return false;
		}
		return true;
	}

	/**
	 * 判断背包中是否包含此物品
	 * 
	 * @param entity
	 * @return
	 */
	public boolean contains(ArticleEntity entity) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				if (cells[i].entityId == entity.getId()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断背包中是否包含此物品
	 * 
	 * @param entity
	 * @return
	 */
	public int indexOf(ArticleEntity entity) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				if (cells[i].entityId == entity.getId()) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 背包中含某个物品的个数
	 * @param articleName
	 * @return
	 */
	public int countArticle(String articleName) {
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				if (ae != null && ae.getArticleName().equals(articleName)) {
					c += cells[i].count;
				}
			}
		}
		return c;
	}
	public int countArticle(long articleId) {
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId > 0 && cells[i].entityId == articleId) {
				c += cells[i].count;
			}
		}
		return c;
	}

	/**
	 * 背包中含某个物品的个数
	 * @param articleName
	 * @return
	 */
	public int countArticle(String articleName, boolean flag) {
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				if (ae != null && ae.getArticleName().equals(articleName) && ae.isBinded() == flag) {
					c += cells[i].count;
				}
			}
		}
		return c;
	}

	/**
	 * 背包中含某个物品的个数
	 * @param articleName
	 * @return
	 */
	public int countArticle(String articleName, int colorType, boolean flag) {
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				if (ae != null && ae.getArticleName().equals(articleName) && ae.isBinded() == flag && ae.getColorType() == colorType) {
					c += cells[i].count;
				}
			}
		}
		return c;
	}

	/**
	 * 背包中含某个物品的个数
	 * @param articleName
	 * @return
	 */
	transient Article a = null;
	public int countArticle(String articleName, int colorType) {
		int c = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				if(ae != null && ae.getArticleName().contains("宝石")){
					a = ArticleManager.getInstance().getArticle(ae.getArticleName());
					if(a != null && (a instanceof InlayArticle)){
						if(((InlayArticle)a).getShowName().equals(articleName) && ae.getColorType() == colorType) {
							c += cells[i].count;
						}
					}
				}
				if (ae != null && ae.getArticleName().equals(articleName) && ae.getColorType() == colorType) {
					c += cells[i].count;
				}
			}
		}
		return c;
	}

	/**
	 * 背包中含某个物品的格子索引
	 * @param articleName
	 * @return
	 */
	public int indexOf(String articleName) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				if (ae != null && ae.getArticleName().equals(articleName)) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 背包中含某个物品的格子索引
	 * @param articleName
	 * @return
	 */
	public int indexOf(String articleName, boolean bindedFlag) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				if (ae != null && ae.getArticleName().equals(articleName) && ae.isBinded() == bindedFlag) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 背包中含某个物品的格子索引
	 * @param articleName
	 * @return
	 */
	public int indexOf(String articleName, int colorType, boolean bindedFlag) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				if (ae != null && ae.getArticleName().equals(articleName) && ae.isBinded() == bindedFlag && ae.getColorType() == colorType) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 背包中含某个物品的格子索引
	 * @param articleName
	 * @return
	 */
	public int indexOf(String articleName, int colorType) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].count > 0 && cells[i].entityId != -1) {
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(cells[i].entityId);
				
				if(ae != null && ae.getArticleName().contains("宝石")){
					Article a = ArticleManager.getInstance().getArticle(ae.getArticleName());
					if(a != null && (a instanceof InlayArticle)){
						if(((InlayArticle)a).getShowName().equals(articleName) && ae.getColorType() == colorType) {
							return i;
						}
					}
				}
				
				if (ae != null && ae.getArticleName().equals(articleName) && ae.getColorType() == colorType) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * 找一个空格子放置物品
	 * @param index
	 * @param ae
	 */
	public boolean put(ArticleEntity ae, String reason) {
		if (ae instanceof HuntLifeArticleEntity) {			//兽魂道具不放这儿，放兽魂单独的背包
			return owner.putArticle2ShouhunKnap((HuntLifeArticleEntity) ae);
		}
		
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ae.getArticleName());
		if (a == null) {
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isInfoEnabled()) logger.info("[put] [{}] [fail] [物种不存在] [owner:{}] [{}] [{}] [单元格：--] [id:{}] [{}] [-] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), ae.getId(), ae.getArticleName(), reason });
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[Get] [Fail] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(-1) + "] [" + this.getArticleInfo4Log(-1, -1) + "] [" + reason + "] [物种不存在]");
				}
			}
			return false;
		}
		if (a.isOverlap()) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null && cells[i].entityId == ae.getId() && cells[i].count < a.getOverLapNum()) {
					if (cells[i].count >= 0) cells[i].count++;
					else cells[i].count = 1;
					cellChangeFlags[i] = ADD_OLD_ARTICLE;
					setModified(true);

					// 通知玩家获得某个物品
					owner.obtainOneArticle(ae);
					if (!ArticleManager.使用物品日志规范) {
						if (logger.isInfoEnabled()) {
							logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [{}] [-] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), i, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
						}
					} else {
						if (logger.isInfoEnabled()) {
							logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(i) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
						}
					}
					return true;
				}
			}
		}
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null) {
				Cell c = new Cell();
				c.entityId = ae.getId();
				c.count = 1;
				cells[i] = c;
				bindWhenPickup(ae);
				cellChangeFlags[i] = ADD_NEW_ARTICLE;

				// 通知玩家获得某个物品
				owner.obtainOneArticle(ae);
				setModified(true);
				if (!ArticleManager.使用物品日志规范) {
					if (logger.isInfoEnabled()) {
						logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [单元格：{}] [id:{}] [{}] [{}] [-] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), i, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
					}
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(i) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
					}
				}
				return true;
			} else if (cells[i].entityId == -1 || cells[i].count <= 0) {
				cells[i].entityId = ae.getId();
				cells[i].count = 1;
				cellChangeFlags[i] = ADD_NEW_ARTICLE;
				bindWhenPickup(ae);

				// 通知玩家获得某个物品
				owner.obtainOneArticle(ae);
				setModified(true);
				if (!ArticleManager.使用物品日志规范) {
					if (logger.isInfoEnabled()) {
						logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] " + "[单元格：{}] [id:{}] [{}] [{}] [-] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), i, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
					}
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(i) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
					}
				}
				return true;
			}
		}
		if (!ArticleManager.使用物品日志规范) {
			if (logger.isWarnEnabled()) logger.warn("[put] [{}] [fail] [knapsack_full] [owner:{}] [{}] [{}] [-] [id:{}] [{}] [{}] [-] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), ae.getId(), ae.getArticleName(), ae.getColorType(), reason });
		} else {
			if (logger.isWarnEnabled()) {
				logger.warn("[Get] [Fail] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(-1) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "] [knapsack_full]");
			}
		}
		return false;
	}

	protected void bindWhenPickup(ArticleEntity ee) {
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ee.getArticleName());
		if (a != null && (a.getBindStyle() == Article.BINDTYPE_PICKUP || a.getBindStyle() == Article.BINDTYPE_NOHINT_PICKUP)) {

			if (ee.isBinded() == false) {
				ee.setBinded(true);

				QUERY_ARTICLE_RES res = null;
				if (ee instanceof PropsEntity) {
					res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[0], new com.fy.engineserver.datasource.article.entity.client.PropsEntity[] { CoreSubSystem.translate((PropsEntity) ee) }, new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[0]);
				} else if (ee instanceof EquipmentEntity) {
					res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[0], new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[] { CoreSubSystem.translate((EquipmentEntity) ee) });
				} else {
					res = new QUERY_ARTICLE_RES(GameMessageFactory.nextSequnceNum(), new com.fy.engineserver.datasource.article.entity.client.ArticleEntity[] { CoreSubSystem.translate(ee) }, new com.fy.engineserver.datasource.article.entity.client.PropsEntity[0], new com.fy.engineserver.datasource.article.entity.client.EquipmentEntity[0]);
				}
				owner.addMessageToRightBag(res);
			}
		}
	}

	/**
	 * 在某个单元格上放置某个物品，如果原单元格上有物品，
	 * 分几种情况：
	 * 可叠加的情况，返回null
	 * 空格子，返回null
	 * 原格子中有其他物品，且只有1件，返回原来的物品
	 * 原格子中有其他物品，多余1件，返回要放置的物品
	 * 
	 * 返回的物品标识没有放入到背包中
	 * 
	 * @param index
	 * @param ae
	 */
	public ArticleEntity put(int index, ArticleEntity ae, String reason) {
		if (index < 0 || index >= cells.length) {
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isWarnEnabled()) logger.warn("[put] [{}] [fail] [out_of_range] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [-] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), reason });
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[Get] [Fail] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(-1) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "] [失败原因:out_of_range]");
				}
			}
			return ae;
		}

		ArticleManager am = ArticleManager.getInstance();
		ArticleEntityManager aem = ArticleEntityManager.getInstance();
		Article article = am.getArticle(ae.getArticleName());

		Cell c = cells[index];
		if (c == null) {
			c = new Cell();
			c.entityId = ae.getId();
			c.count = 1;
			cells[index] = c;
			bindWhenPickup(ae);
			cellChangeFlags[index] = ADD_NEW_ARTICLE;
			setModified(true);

			// 通知玩家获得某个物品
			owner.obtainOneArticle(ae);
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isInfoEnabled()) logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [-] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
				}
			}
			return null;
		} else if (c.count <= 0 || c.entityId == -1) {
			c.entityId = ae.getId();
			c.count = 1;
			bindWhenPickup(ae);
			cellChangeFlags[index] = ADD_NEW_ARTICLE;
			setModified(true);

			// 通知玩家获得某个物品
			owner.obtainOneArticle(ae);
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isInfoEnabled()) logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [-] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
				}
			}
			return null;
		} else {
			if (article != null) {
				if (c.count > 1 && c.count < article.getOverLapNum()) {
					if (c.entityId == ae.getId()) {
						c.count++;
						cellChangeFlags[index] = ADD_OLD_ARTICLE;
						bindWhenPickup(ae);
						setModified(true);

						// 通知玩家获得某个物品
						owner.obtainOneArticle(ae);
						if (!ArticleManager.使用物品日志规范) {
							if (logger.isInfoEnabled()) logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [-] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
						} else {
							if (logger.isInfoEnabled()) {
								logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
							}
						}
						return null;
					} else {
						if (!ArticleManager.使用物品日志规范) {
							if (logger.isInfoEnabled()) logger.info("[put] [{}] [fail] [cell_has_article] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
						} else {
							if (logger.isInfoEnabled()) {
								logger.info("[Get] [Fail] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "] [cell_has_article]");
							}
						}
						return ae;
					}
				} else {
					if (c.entityId == ae.getId()) {
						if (article.isOverlap() && c.count < article.getOverLapNum()) {
							c.count++;
							cellChangeFlags[index] = ADD_OLD_ARTICLE;
							bindWhenPickup(ae);
							setModified(true);

							// 通知玩家获得某个物品
							owner.obtainOneArticle(ae);
							if (!ArticleManager.使用物品日志规范) {
								if (logger.isInfoEnabled()) logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [-] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
							} else {
								if (logger.isInfoEnabled()) {
									logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
								}
							}
							return null;
						} else {
							ArticleEntity ret = aem.getEntity(c.entityId);
							c.entityId = ae.getId();
							c.count = 1;
							cellChangeFlags[index] = ADD_NEW_ARTICLE;
							bindWhenPickup(ae);
							setModified(true);

							// 通知玩家获得某个物品
							owner.obtainOneArticle(ae);

							owner.discardOneArticle(ret);
							if (!ArticleManager.使用物品日志规范) {
								if (logger.isInfoEnabled()) logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), ret.getArticleName(), reason, ret.isBinded() });
							} else {
								if (logger.isInfoEnabled()) {
									logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
								}
							}
							return ret;
						}
					} else {
						ArticleEntity ret = aem.getEntity(c.entityId);
						c.entityId = ae.getId();
						c.count = 1;
						cellChangeFlags[index] = ADD_NEW_ARTICLE;
						bindWhenPickup(ae);
						setModified(true);

						// 通知玩家获得某个物品
						owner.obtainOneArticle(ae);

						owner.discardOneArticle(ret);
						if (!ArticleManager.使用物品日志规范) {
							if (logger.isInfoEnabled()) logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), ret.getArticleName(), reason, ae.isBinded() });
						} else {
							if (logger.isInfoEnabled()) {
								logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
							}
						}
						return ret;
					}
				}
			} else {
				if (!ArticleManager.使用物品日志规范) {
					if (logger.isWarnEnabled()) logger.warn("[put] [{}] [fail] [article_not_exists] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [{}] [{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), ae.getArticleName(), reason, ae.isBinded() });
				} else {
					if (logger.isWarnEnabled())  {
						logger.warn("[Get] [Fail] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [article_not_exists]");
					}
				}
				return ae;
			}
		}
	}

	/**
	 * 删除某个单元格上的一个物品
	 * 
	 * @param index
	 * @return
	 */
	public ArticleEntity remove(int index, String reason, boolean 从游戏中删除) {

		if (index < 0 || index >= cells.length) {
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isWarnEnabled()) logger.warn("[remove] [{}] [fail] [out_of_range] [owner:{}] [{}] [{}] [-] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getName(), index, reason });
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[Delete] [Fail] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(-1, -1) + "] [" + reason + "] [out_of_range]");
				}
			}
			return null;
		}
		ArticleEntityManager aem = ArticleEntityManager.getInstance();

		Cell c = cells[index];
		if (c == null || c.entityId == -1 || c.count <= 0) {
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isWarnEnabled()) logger.warn("[remove] [{}] [fail] [cell_is_empty] [owner:{}] [{}] [{}] [{}] [-] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, reason });
			} else {
				if (logger.isWarnEnabled()) {
					logger.warn("[Delete] [Fail] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(-1, -1) + "] [" + reason + "] [cell_is_empty]");
				}
			}
			return null;
		} else if (c.count > 1) {
			c.count--;

			cellChangeFlags[index] = REMOVE_ARTICLE;
			setModified(true);
			ArticleEntity ae = aem.getEntity(c.entityId);

			owner.discardOneArticle(ae);
			if (ae instanceof DigPropsEntity) {
				Map<Long, DigTemplate> map = owner.getDigInfo();
				if (map != null) {
					map.remove(ae.getId());
					owner.setDigInfo(map);
				}
			}
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isInfoEnabled() && ae != null) logger.info("[remove] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [color:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
			} else {
				if (logger.isInfoEnabled() && ae != null) {
					logger.info("[Delete] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
				}
			}
			if (ae != null && 从游戏中删除) {
				ae.setReferenceCount(ae.getReferenceCount());
			}
			return ae;
		} else {
			ArticleEntity ret = aem.getEntity(c.entityId);
			c.count = 0;
			c.entityId = -1;

			cellChangeFlags[index] = REMOVE_ARTICLE;
			setModified(true);

			owner.discardOneArticle(ret);
			if (ret instanceof DigPropsEntity) {
				Map<Long, DigTemplate> map = owner.getDigInfo();
				if (map != null) {
					map.remove(ret.getId());
					owner.setDigInfo(map);
				}
			}
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isInfoEnabled() && ret != null) logger.info("[remove] [{}] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [color:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ret.getId(), ret.getArticleName(), ret.getColorType(), reason, ret.isBinded() });
			} else {
				if (logger.isInfoEnabled() && ret != null) {
					logger.info("[Delete] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ret.getId(), 1) + "] [" + reason + "]");
				}
			}
			if (ret != null && 从游戏中删除) {
				ret.setReferenceCount(ret.getReferenceCount());
			}
			return ret;
		}
	}

	/**
	 * 清掉某个格子
	 * @param index
	 */
	public int clearCell(int index, String reason, boolean 从游戏中删除) {
		long entityId = cells[index].getEntityId();
		if (entityId == -1) return 0;
		ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
		int count = cells[index].getCount();
		cells[index].setEntityId(-1);
		cells[index].setCount(0);
		cellChangeFlags[index] = REMOVE_ARTICLE;
		setModified(true);
		if (entity != null) {
			owner.discardOneArticle(entity);
			if (从游戏中删除) {
				entity.setReferenceCount(entity.getReferenceCount() - count);
			}
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isInfoEnabled()) logger.info("[清空格子] [{}] [succ] [owner:{}] [{}] [{}] [{}] [id:{}] [物品:{}] [数量:{}] [color:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, entity.getId(), entity.getArticleName(), count, entity.getColorType(), reason, entity.isBinded() });
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[清空格子] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(entityId, count) + "] [" + reason + "]");
				}
			}
		} else {
			if (!ArticleManager.使用物品日志规范) {
				if (logger.isInfoEnabled()) logger.info("[清空格子] [{}] [--] [owner:{}] [{}] [{}] [{}] [id:--] [物品:--] [数量:{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, count, reason });
			} else {
				if (logger.isInfoEnabled()) {
					logger.info("[清空格子] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(entityId, count) + "] [" + reason + "] [原来为空格子,物品id:" + entityId + "]");
				}
			}
		}
		return count;
	}

	/**
	 * 减去某个格子,Count数量
	 * @param index
	 */
	public void clearCell(int index, int num, String reason, boolean 从游戏中删除) {
		long entityId = cells[index].getEntityId();
		if (entityId == -1) return;
		ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
		int count = cells[index].getCount();
		if (count <= num) {
			cells[index].setEntityId(-1);
			cells[index].setCount(0);
			cellChangeFlags[index] = REMOVE_ARTICLE;
			setModified(true);
			if (entity != null) {
				owner.discardOneArticle(entity);
				if (从游戏中删除) {
					entity.setReferenceCount(entity.getReferenceCount() - count);
				}
				if (!ArticleManager.使用物品日志规范) {
					if (logger.isInfoEnabled()) logger.info("[清空格子] [{}] [succ] [owner:{}] [{}] [{}] [{}] [id:{}] [物品:{}] [数量:{}] [color:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, entity.getId(), entity.getArticleName(), count, entity.getColorType(), reason, entity.isBinded() });
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("[清空格子] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(entity.getId(), count) + "] [" + reason + "]");
					}
				}
			} else {
				if (!ArticleManager.使用物品日志规范) {
					if (logger.isInfoEnabled()) logger.info("[清空格子] [{}] [--] [owner:{}] [{}] [{}] [{}] [id:--] [物品:丢失] [数量:{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, count, reason, "--" });
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("[清空格子] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(entityId, count) + "] [" + reason + "] [原来为空格子,物品id:" + entityId + "]");
					}
				}
			}
		} else {
			cells[index].setCount(count - num);
			cellChangeFlags[index] = ADD_OLD_ARTICLE;
			setModified(true);
			if (entity != null) {
				owner.discardOneArticle(entity);
				if (从游戏中删除) {
					entity.setReferenceCount(entity.getReferenceCount() - num);
				}
				if (!ArticleManager.使用物品日志规范) {
					if (logger.isInfoEnabled()) logger.info("[格子:数量减少] [{}] [succ] [owner:{}] [{}] [{}] [{}] [id:{}] [物品:{}] [数量:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, entity.getId(), entity.getArticleName(), num, reason, entity.isBinded() });
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("[清空格子] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(entity.getId(), num) + "] [" + reason + "]");
					}
				}
			} else {
				if (!ArticleManager.使用物品日志规范) {
					if (logger.isInfoEnabled()) logger.info("[格子:数量减少] [{}] [--] [owner:{}] [{}] [{}] [{}] [id:--] [物品:丢失] [数量:{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, num, reason, "--" });
				} else {
					if (logger.isInfoEnabled()) {
						logger.info("[清空格子] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(entityId, count) + "] [" + reason + "] [原来为空格子,物品id:" + entityId + "]");
					}
				}
			}
		}
	}

	public void updateCell(int index, long entityId, int count, String reason) {
		if (entityId == -1) return;
		ArticleEntity entity = ArticleEntityManager.getInstance().getEntity(entityId);
		long oldID = cells[index].getEntityId();
		long oldCount = cells[index].getCount();
		String oldName = "";
		if (oldID != -1) {
			ArticleEntity ae = ArticleEntityManager.getInstance().getEntity(oldID);
			if (ae != null) {
				oldName = ae.getArticleName();
			}
		}
		cells[index].setEntityId(entityId);
		cells[index].setCount(count);
		cellChangeFlags[index] = REMOVE_ARTICLE;
		if (entity != null) {
			owner.discardOneArticle(entity);
		}
		setModified(true);

		if (logger.isInfoEnabled()) {
			if (entity != null) logger.info("[更新格子] [{}] [succ] [owner:{}] [{}] [{}] [oldName:{}] [oldID:{}] [oldCount:{}] [新Id:{}] [物品:{}] [数量:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getName(), index, oldName, oldID, oldCount, entityId, entity.getArticleName(), count, reason, entity.isBinded() });
		} else {
			logger.info("[更新格子] [{}] [fail] [owner:{}] [{}] [{}] [oldName:{}] [oldID:{}] [oldCount:{}] [新Id:{}] [新物品:--] [数量:{}] [{}] [{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getName(), index, oldName, oldID, oldCount, entityId, count, reason, "--" });
		}

	}

	/**
	 * 测试找一个空格子放置物品
	 * @param index
	 * @param ae
	 */
	private boolean putTest(ArticleEntity ae) {
		ArticleManager am = ArticleManager.getInstance();
		Article a = am.getArticle(ae.getArticleName());
		if (a.isOverlap()) {
			for (int i = 0; i < cells.length; i++) {
				if (cells[i] != null && cells[i].entityId == ae.getId() && cells[i].count < a.getOverLapNum()) {
					if (cells[i].count >= 0) cells[i].count++;
					else cells[i].count = 1;
					return true;
				}
			}
		}
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null) {
				Cell c = new Cell();
				c.entityId = ae.getId();
				c.count = 1;
				cells[i] = c;
				return true;
			} else if (cells[i].entityId == -1 || cells[i].count <= 0) {
				cells[i].entityId = ae.getId();
				cells[i].count = 1;
				return true;
			}
		}
		return false;
	}

	/**
	 * 测试是否可以全部放下列表中的物品
	 * @param elist
	 * @return
	 */
	public boolean putAllOK(List<ArticleEntity> elist) {
		Knapsack testknap = new Knapsack(null);
		Cell testcells[] = new Cell[cells.length];
		for (int i = 0; i < testcells.length; i++) {
			testcells[i] = new Cell();
			if (cells[i] != null) {
				testcells[i].setEntityId(cells[i].getEntityId());
				testcells[i].setCount(cells[i].getCount());
			}
		}
		testknap.setCells(testcells);
		testknap.setCellChangeFlags(new byte[this.cellChangeFlags.length]);
		for (ArticleEntity entity : elist) {
			if (!testknap.putTest(entity)) {
				return false;
			}
		}
		return true;
	}

	public void putAll(List<ArticleEntity> elist, String reason) {
		for (ArticleEntity entity : elist) {
			put(entity, reason);
		}
	}

	/**
	 * 加入一个空格
	 * @param entityId
	 * @param count
	 * @return
	 */
	public boolean putToEmptyCell(long entityId, int count, String reason) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null || cells[i].getEntityId() == -1 || cells[i].getCount() == 0) {
				if (cells[i] == null) {
					cells[i] = new Cell();
				}
				cells[i].setEntityId(entityId);
				cells[i].setCount(count);
				cellChangeFlags[i] = ADD_NEW_ARTICLE;
				setModified(true);
				ArticleEntityManager aem = ArticleEntityManager.getInstance();
				ArticleEntity ae = aem.getEntity(entityId);
				if (ae != null) {
					for (int j = 0; aem != null && j < count; j++) {
						// 通知玩家获得某个物品
						owner.obtainOneArticle(ae);
					}
				}

				if (ae != null) {
					if (!ArticleManager.使用物品日志规范) {
						if (logger.isInfoEnabled()) logger.info("[put] [{}] [succ] [-] [owner:{}] [{}] [{}] [下标：{}] [id:{}] [{}] [{}] [-] [{}] [个数:{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), i, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, count, ae.isBinded() });
					} else {
						if (logger.isInfoEnabled()) {
							logger.info("[Get] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(i) + "] [" + this.getArticleInfo4Log(entityId, count) + "] [" + reason + "]");
						}
					}
				}

				return true;
			}
		}
		return false;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
		if (modified && owner != null) {
			owner.notifyKnapsackDirty();
		}
	}

	public void setCellChangeFlags(byte[] cellChangeFlags) {
		this.cellChangeFlags = cellChangeFlags;
	}

	/**
	 * 得到背包中某种物品的位置
	 * @param articleName
	 * @return
	 */
	public int getArticleCellPos(String articleName) {
		ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].getCount() > 0) {
				long entityId = cells[i].getEntityId();
				ArticleEntity entity = aemanager.getEntity(entityId);
				if (entity != null && entity.getArticleName().equals(articleName)) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getArticleCellCount(String articleName, boolean bind, int color) {
		ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
		int articleCount = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].getCount() > 0) {
				long entityId = cells[i].getEntityId();
				ArticleEntity entity = aemanager.getEntity(entityId);
				if (entity != null && entity.getArticleName().equals(articleName) && entity.getColorType() == color && entity.isBinded() == bind) {
					articleCount++;
				}
			}
		}
		return articleCount;
	}

	/**
	 * 得到背包中某种不重叠物品的个数
	 * @param articleName
	 * @return
	 */
	public int getArticleCellCount(String articleName) {
		ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
		int articleCount = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].getCount() > 0) {
				long entityId = cells[i].getEntityId();
				ArticleEntity entity = aemanager.getEntity(entityId);
				if (entity != null && entity.getArticleName().equals(articleName)) {
					articleCount++;
				}
			}
		}
		return articleCount;
	}
	/**
	 * 获得背包中某个物品数量
	 * @param aeId
	 * @return
	 */
	public int getArticleCellCount(long aeId) {
		int articleCount = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].getCount() > 0 && cells[i].getEntityId() == aeId) {
				articleCount += cells[i].getCount();
			}
		}
		return articleCount;
	}

	public int getArticleCellCount_重叠(String articleName) {
		ArticleEntityManager aemanager = ArticleEntityManager.getInstance();
		int articleCount = 0;
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] != null && cells[i].getCount() > 0) {
				long entityId = cells[i].getEntityId();
				ArticleEntity entity = aemanager.getEntity(entityId);
				if (entity != null && entity.getArticleName().equals(articleName)) {
					articleCount += cells[i].getCount();
				}
			}
		}
		return articleCount;
	}

	/**
	 * 获取背包的格子总数，包括有物品的格子和空格子
	 * 
	 * @return
	 */
	public int size() {
		return cells.length;
	}

	/**
	 * 根据物品id删除某个单元格上的一个物品
	 * 
	 * @param index
	 * @return
	 */
	public ArticleEntity removeByArticleId(long articleId, String reason, boolean 从游戏中删除) {

		ArticleEntityManager aem = ArticleEntityManager.getInstance();

		if (cells == null) {
			return null;
		}
		for (int index = 0; index < cells.length; index++) {
			Cell c = cells[index];
			if (c == null || c.entityId == -1 || c.count <= 0) {

			} else if (c.entityId == articleId && c.count > 0) {
				if (c.count > 1) {
					c.count--;

					cellChangeFlags[index] = REMOVE_ARTICLE;
					setModified(true);
					ArticleEntity ae = aem.getEntity(c.entityId);

					owner.discardOneArticle(ae);
					if (ae instanceof DigPropsEntity) {
						Map<Long, DigTemplate> map = owner.getDigInfo();
						if (map != null) {
							map.remove(ae.getId());
							owner.setDigInfo(map);
						}
					}
					if (!ArticleManager.使用物品日志规范) {
						if (logger.isInfoEnabled() && ae != null) logger.info("[remove] [{}] [叠加] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [color:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ae.getId(), ae.getArticleName(), ae.getColorType(), reason, ae.isBinded() });
					} else {
						if (logger.isInfoEnabled() && ae != null) {
							logger.info("[Delete] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ae.getId(), 1) + "] [" + reason + "]");
						}
					}

					if (ae != null && 从游戏中删除) {
						ae.setReferenceCount(ae.getReferenceCount() - 1);
					}
					return ae;
				} else {
					ArticleEntity ret = null;
					try {
						ret = aem.getEntity(c.entityId);
						// logger.info("[owner.getName():"+owner.getName()+"] [不重叠] ["+(ret==null)+"] [c.entityId:"+c.entityId+"] ");
						c.count = 0;
						c.entityId = -1;

						cellChangeFlags[index] = REMOVE_ARTICLE;
						setModified(true);

						owner.discardOneArticle(ret);

						if (ret != null && 从游戏中删除) {
							ret.setReferenceCount(ret.getReferenceCount() - 1);
						}
						if (ret instanceof DigPropsEntity) {
							Map<Long, DigTemplate> map = owner.getDigInfo();
							if (map != null) {
								map.remove(ret.getId());
								owner.setDigInfo(map);
							}
						}
					} catch (Exception e) {
						logger.error("[删除物品异常]", e);
					}
					if (!ArticleManager.使用物品日志规范) {
						if (logger.isInfoEnabled() && ret != null) logger.info("[remove] [{}] [c.entityId:" + c.entityId + "] [不叠加] [succ] [-] [owner:{}] [{}] [{}] [{}] [id:{}] [{}] [color:{}] [{}] [绑:{}]", new Object[] { this.getClass().getSimpleName(), owner.getId(), owner.getUsername(), owner.getName(), index, ret.getId(), ret.getArticleName(), ret.getColorType(), reason, ret.isBinded() });
					} else {
						if (logger.isInfoEnabled() && ret != null) {
							logger.info("[Delete] [Succ] [" + owner.getLogString4Knap() + "] [" + this.getKnapInfo4Log(index) + "] [" + this.getArticleInfo4Log(ret.getId(), 1) + "] [" + reason + "]");
						}
					}
					return ret;
				}
			}
		}
		return null;
	}

	/**
	 * 背包中是否有物品id的物品，如果有返回位置，如果没有返回-1
	 * @param articleId
	 * @return
	 */
	public int hasArticleEntityByArticleId(long articleId) {
		int index = -1;
		if (cells == null) {
			return index;
		}
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			if (c != null && c.entityId == articleId && c.count > 0) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * 删除多个id对应多数量的物品
	 * @param ids
	 * @param nums
	 */
	public void removeArticleEntity(Player p ,long ids[],int nums[],String reason){
		if(p == null || ids == null || nums == null){
			return;
		}
		if(ids.length != nums.length){
			return;
		}
		
		Map<Long, Integer> removeDatas = new HashMap<Long, Integer>();
		for(int i = 0;i < ids.length;i++){
			long id = ids[i];
			if(id <= 0){
				continue;
			}
			removeDatas.put(new Long(id), new Integer(nums[i]));
		}
		
		for(int i = 0;i < cells.length;i++){
			Cell cell = cells[i];
			if(cell == null || cell.entityId <= 0 || cell.count <= 0){
				continue;
			}
			int hasNums = cell.getCount();
			if(removeDatas.containsKey(new Long(cell.entityId))){
				int removeNums = removeDatas.get(new Long(cell.entityId));
				if(removeNums > 0){
					if(removeNums <= hasNums){
						removeDatas.put(new Long(cell.entityId), new Integer(0));
						this.clearCell(i, removeNums, reason, true);
						ArticleEntity raee = ArticleEntityManager.getInstance().getEntity(cell.entityId);
						if(raee!=null){
							ArticleStatManager.addToArticleStat(p, null, raee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, removeNums, "图纸合成删除", null);
						}
					}else{
						removeDatas.put(new Long(cell.entityId), new Integer(removeNums - hasNums));
						this.clearCell(i, hasNums, reason, true);
						ArticleEntity raee = ArticleEntityManager.getInstance().getEntity(cell.entityId);
						if(raee!=null){
							ArticleStatManager.addToArticleStat(p, null, raee, ArticleStatManager.OPERATION_物品获得和消耗, 0, ArticleStatManager.YINZI, hasNums, "图纸合成删除", null);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param index
	 * @return  背包名,物品格子索引,物品剩余数量
	 */
	public String getKnapInfo4Log(int index, String ft) {
		if (index < 0) {
			return ft + "PackageName:" + this.knapName + "] ["+ft+"PackageIndex:" + index + "] ["+ft+"CellLeftNum:-1";
		}
		Cell cl = this.getCell(index);
		if (cl == null) {
			return "";
		}
		return ft + "PackageName:" + this.knapName + "] ["+ft+"PackageIndex:" + index + "] ["+ft+"CellLeftNum:" + cl.count;
	}
	
	public String getKnapInfo4Log(int index) {
		return getKnapInfo4Log(index, "");
	}
	
	/**
	 * 规范物品日志
	 * @param entityId
	 * @param num
	 * @return
	 */
	public String getArticleInfo4Log(long entityId, int num, String ft) {
		if (entityId < 0) {
			return ft + "SimpleClassName:" + null + "] ["+ft+"ID:"+entityId+"] ["+ft+"ArticleName:] ["+ft+"Num:"+num+"] ["+ft+"Color:-1] ["+ft+"Bind:false] ["+ft+"Overlap:false]";
		}
		ArticleEntity ae = DefaultArticleEntityManager.getInstance().getEntity(entityId);
		if (ae != null) {
			return ft + "SimpleClassName:" + ae.getClass().getSimpleName() + "] ["+ft+"ID:" + ae.getId() + "] ["+ft+"ArticleName:"+ae.getArticleName()+"] ["+ft+"Num:"
					+ num + "] ["+ft+"Color:" + ae.getColorType() + "] ["+ft+"Bind:" + ae.isBinded() + "] ["+ft+"Overlap:" + ae.isOverlapFlag();
		}
		return ft + "SimpleClassName:" + null + "] ["+ft+"ID:"+entityId+"] ["+ft+"ArticleName:] ["+ft+"Num:"+num+"] ["+ft+"Color:-1] ["+ft+"Bind:false] ["+ft+"Overlap:false]";
	}
	
	public String getArticleInfo4Log(long entityId, int num) {
		return getArticleInfo4Log(entityId, num, "");
	}
	
	
}
