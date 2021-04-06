package com.fy.engineserver.newtask.prizes;

import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.datasource.article.concrete.DefaultArticleEntityManager;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.newtask.Task;
import com.fy.engineserver.newtask.service.TaskConfig;
import com.fy.engineserver.newtask.service.TaskManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;
import com.fy.engineserver.util.CompoundReturn;

public class TaskPrizeOfRandomArticle extends TaskPrize implements TaskConfig {
	// 颜色,物品,数量,无额外条件获得概率,有额外条件获得概率@0~X个物品分别的概率|另一个|另2个|另3个
	// 1,酒,33,22%,33%,1
	/** 奖励的颜色 */
	private int[] prizeColor = new int[0];
	/** 无额外条件获得的概率 */
	private double[] baseRate = new double[0];
	/** 满足额外条件获得的概率 */
	private double[] highRate = new double[0];
	/** 每个物品获得个数的随机 */
	private double[][] everyNumRate = new double[0][];

	/** 无额外条件获得的概率 */
	private double[] modifyBaseRate = new double[0];
	/** 满足额外条件获得的概率 */
	private double[] modifyHighRate = new double[0];
	/** 每个物品获得个数的随机 */
	private double[][] modifyeveryNumRate = new double[0][];

	public TaskPrizeOfRandomArticle() {
		setPrizeType(PrizeType.RANDOM_ARTICLE);
		setPrizeByteType(getPrizeType().getIndex());
	}

	public static TaskPrize createTaskPrize(int[] prizeColor, String[] prizeName, long[] prizeNum, double[] baseRate, double[] highRate, double[][] everyNumRate) {
		TaskPrizeOfRandomArticle prize = new TaskPrizeOfRandomArticle();
		prize.setPrizeColor(prizeColor);
		prize.setPrizeName(prizeName);
		prize.setPrizeNum(prizeNum);
		prize.setBaseRate(baseRate);
		prize.setHighRate(highRate);
		prize.setEveryNumRate(everyNumRate);
		prize.modifyRate();
		return prize;
	}

	@Override
	@Deprecated
	public void doPrize(Player player, int[] index, Task task) {
		// TODO Auto-generated method stub
	}

	public CompoundReturn doPrize(Player player, boolean excess) {
		if (prizeColor.length == 0) {
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		// 先计算获得哪个物品
		int prizeIndex = -1;
		double randow4Article = TaskManager.RANDOM.nextDouble();
		if (excess) {
			for (int i = 0, j = modifyHighRate.length; i < j; i++) {
				if (modifyHighRate[i] <= randow4Article) {
					prizeIndex = i;
					break;
				}
			}
		} else {
			for (int i = 0, j = modifyBaseRate.length; i < j; i++) {
				if (modifyBaseRate[i] <= randow4Article) {
					prizeIndex = i;
					break;
				}
			}
		}
		if (prizeIndex == -1) {
			TaskSubSystem.logger.error("[随机奖励没随机到][角色:{}]", new Object[] { player.getName() });
			return CompoundReturn.createCompoundReturn().setBooleanValue(false);
		}
		// 先计算获得物品个数
		if (modifyeveryNumRate[prizeIndex] == null || modifyeveryNumRate[prizeIndex].length == 0) {
			// 直接发货
			Article article = ArticleManager.getInstance().getArticle(getPrizeName()[prizeIndex]);
			if (article == null) {
				TaskSubSystem.logger.error("[随机奖励没随机到][物品名称{}] [数量 {}][颜色{}][角色:{}]", new Object[] { getPrizeName()[prizeIndex], getPrizeNum()[prizeIndex], getPrizeColor()[prizeIndex], player.getName() });
				return CompoundReturn.createCompoundReturn().setBooleanValue(false);
			}
			CompoundReturn re = CompoundReturn.createCompoundReturn();
			List<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
			for (int i = 0; i < getPrizeNum()[prizeIndex]; i++) {
				try{
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_TASK_REWARD, player, getPrizeColor()[prizeIndex], 1, true);
				// player.putToKnapsacks(ae);
				aeList.add(ae);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			return re.setBooleanValue(true).setObjValue(aeList);

		} else {
			// 随机数量
			double randow4Num = TaskManager.RANDOM.nextDouble();
			int num = 0;
			for (int i = 0; i < modifyeveryNumRate[prizeIndex].length; i++) {
				if (modifyeveryNumRate[prizeIndex][i] >= randow4Num) {
					num = i;
					break;
				}
			}
			if (num == 0) {
				TaskSubSystem.logger.error("[随机奖励没随机到][物品名称{}] [数量 {}][颜色{}][角色:{}]", new Object[] { getPrizeName()[prizeIndex], num, getPrizeColor()[prizeIndex], player.getName() });
				return CompoundReturn.createCompoundReturn().setBooleanValue(false);
			}
			Article article = ArticleManager.getInstance().getArticle(getPrizeName()[prizeIndex]);
			if (article == null) {
				TaskSubSystem.logger.error("[随机奖励没随机到][物品名称{}] [数量 {}][颜色{}][角色:{}]", new Object[] { getPrizeName()[prizeIndex], getPrizeNum()[prizeIndex], getPrizeColor()[prizeIndex], player.getName() });
				return CompoundReturn.createCompoundReturn().setBooleanValue(false);
			}
			CompoundReturn re = CompoundReturn.createCompoundReturn();
			List<ArticleEntity> aeList = new ArrayList<ArticleEntity>();
			for (int i = 0; i < num; i++) {
				try{
				ArticleEntity ae = DefaultArticleEntityManager.getInstance().createEntity(article, true, ArticleEntityManager.CREATE_REASON_TASK_REWARD, player, getPrizeColor()[prizeIndex], 1, true);
				// player.putToKnapsacks(ae);
				aeList.add(ae);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			return re.setBooleanValue(true).setObjValue(aeList);
		}

	}

	/**
	 * 修正几率，便于计算
	 */
	public void modifyRate() {
		if (baseRate.length > 0) {
			modifyBaseRate = new double[baseRate.length];
			modifyHighRate = new double[baseRate.length];
			modifyeveryNumRate = new double[baseRate.length][];

			modifyBaseRate[0] = baseRate[0];
			for (int i = 1, j = baseRate.length; i < j; i++) {
				modifyBaseRate[i] = baseRate[i] + modifyBaseRate[i - 1];
			}
			modifyHighRate[0] = highRate[0];
			for (int i = 1, j = baseRate.length; i < j; i++) {
				modifyHighRate[i] = highRate[i] + modifyHighRate[i - 1];
			}

			for (int i = 0, j = everyNumRate.length; i < j; i++) {
				if (everyNumRate[i] == null) {
					continue;
				}
				modifyeveryNumRate[i] = new double[everyNumRate[i].length];

				modifyeveryNumRate[i][0] = everyNumRate[i][0];
				for (int x = 1, y = everyNumRate[i].length; x < y; x++) {
					modifyeveryNumRate[i][x] = everyNumRate[i][x] + modifyeveryNumRate[i][x - 1];
				}
			}

		}
	}

	public int[] getPrizeColor() {
		return prizeColor;
	}

	public void setPrizeColor(int[] prizeColor) {
		this.prizeColor = prizeColor;
	}

	public double[] getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(double[] baseRate) {
		this.baseRate = baseRate;
	}

	public double[] getHighRate() {
		return highRate;
	}

	public void setHighRate(double[] highRate) {
		this.highRate = highRate;
	}

	public double[][] getEveryNumRate() {
		return everyNumRate;
	}

	public void setEveryNumRate(double[][] everyNumRate) {
		this.everyNumRate = everyNumRate;
	}

	public double[] getModifyBaseRate() {
		return modifyBaseRate;
	}

	public void setModifyBaseRate(double[] modifyBaseRate) {
		this.modifyBaseRate = modifyBaseRate;
	}

	public double[] getModifyHighRate() {
		return modifyHighRate;
	}

	public void setModifyHighRate(double[] modifyHighRate) {
		this.modifyHighRate = modifyHighRate;
	}

	public double[][] getModifyeveryNumRate() {
		return modifyeveryNumRate;
	}

	public void setModifyeveryNumRate(double[][] modifyeveryNumRate) {
		this.modifyeveryNumRate = modifyeveryNumRate;
	}

	@Override
	public String toHtmlString(String cssClass) {
		StringBuffer sbf = new StringBuffer("<table class='");
		sbf.append(cssClass).append("'>");
		sbf.append("<tr>");
		sbf.append("<td>");
		sbf.append(getPrizeType().getName());
		sbf.append("</td>");
		sbf.append("</tr>");

		sbf.append("<tr><td>名字</td><td>颜色</td><td>基本概率</td><td>有额外目标概率</td><td>各个数量随机概率</td></tr>");

		for (int i = 0; i < getPrizeName().length; i++) {
			if (getEveryNumRate()[i] == null) {
				continue;
			}
			sbf.append("<tr><td>").append(getPrizeName()[i]).append("</td><td>").append(getPrizeColor()[i]).append("</td><td>").append(getBaseRate()[i]).append("</td><td>").append(getHighRate()[i]).append("</td><td>");
			for (int j = 0; j < getPrizeNum()[i]; j++) {
				sbf.append(getEveryNumRate()[i][j]).append(j < getPrizeNum()[i] - 1 ? "," : "");
			}
			sbf.append("</tr>");
		}
		sbf.append("</table>");
		return sbf.toString();
	}

	public static void main(String[] args) {
		double[][] everyNumRate = new double[2][];
		everyNumRate[0] = new double[] { 1 };
		everyNumRate[1] = new double[] { 0.1, 0.2, 0.7 };
		TaskPrize prize = createTaskPrize(new int[] { 1, 2 }, new String[] { "A1", "A2" }, new long[] { 1, 3 }, new double[] { 0.2, 0.3 }, new double[] { 0.1, 0.9 }, everyNumRate);
//		System.out.println(prize.toHtmlString("GG"));
	}
}
