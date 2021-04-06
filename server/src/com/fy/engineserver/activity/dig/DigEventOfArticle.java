package com.fy.engineserver.activity.dig;

import java.util.List;
import java.util.Random;

import com.fy.engineserver.core.Game;
import com.fy.engineserver.datasource.article.data.articles.Article;
import com.fy.engineserver.datasource.article.data.entity.ArticleEntity;
import com.fy.engineserver.datasource.article.manager.ArticleEntityManager;
import com.fy.engineserver.datasource.article.manager.ArticleManager;
import com.fy.engineserver.datasource.language.Translate;
import com.fy.engineserver.mail.service.MailManager;
import com.fy.engineserver.newtask.service.TaskSubSystem;
import com.fy.engineserver.sprite.Player;

public class DigEventOfArticle extends DigEvent {

	private List<DigRateArticle> list;

	/** 随机道具名 */
	private String[] rangeArticleNameArr;
	/** 颜色 */
	private int[] colorArr;
	/** 数量 */
	private int[] numArr;
	/** 几率 */
	private int[] rateArr;
	/** 是否绑定 */
	private boolean[] bindArr;

	public DigEventOfArticle(String useArticleName, String useArticleNameStat, int eventType) {
		super(useArticleName, useArticleNameStat, eventType);
	}

	public DigEventOfArticle(String useArticleName, String useArticleNameStat, List<DigRateArticle> list) {
		this(useArticleName, useArticleNameStat, DigManager.EVENT_ARTICLE);
		this.list = list;
	}

	@Override
	public void execute(Player player, Game game) {
		init();
		// TODO Auto-generated method stub
		int index = getRandomResult();
		String name = rangeArticleNameArr[index];
		Article a = ArticleManager.getInstance().getArticle(name);
		if (a == null) {
			TaskSubSystem.logger.error(player.getLogString() + "[物品不存在] [物品名：" + name + "]");
		} else {
			try {
				ArticleEntity ae = ArticleEntityManager.getInstance().createEntity(a, bindArr[index], ArticleEntityManager.挖宝, player, colorArr[index], numArr[index], true);
				if (ae == null) {
					TaskSubSystem.logger.error(player.getLogString() + "[创建物品异常] [物品名：" + name + "]");
				} else if (!player.getKnapsack_common().isFull()) {
					player.putToKnapsacks(ae, numArr[index], "挖宝随机道具");
					player.sendError(Translate.translateString(Translate.恭喜您获得了, new String[][]{{Translate.STRING_1,name},{Translate.COUNT_1,String.valueOf(numArr[index])}}));
				} else {
					MailManager.getInstance().sendMail(player.getId(), new ArticleEntity[] { ae }, new int[] { numArr[index] }, Translate.系统, Translate.由于您的背包已满您得到的部分物品通过邮件发送, 0, 0, 0, "挖宝");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void init() {
		rangeArticleNameArr = new String[list.size()];
		colorArr = new int[list.size()];
		numArr = new int[list.size()];
		rateArr = new int[list.size()];
		bindArr = new boolean[list.size()];
		for (int i = 0; i < list.size(); i++) {
			rangeArticleNameArr[i] = list.get(i).getRateName();
			colorArr[i] = list.get(i).getColor();
			numArr[i] = list.get(i).getNum();
			rateArr[i] = list.get(i).getRate();
			bindArr[i] = list.get(i).getBind();
		}
	}

	public int getRandomResult() {

		Random random = new Random();
//		int[] rate = new int[rateArr.length];

		int total = 0;
		for (int everyRate : rateArr) {
			total += everyRate;
		}
		int result = random.nextInt(total);
		int low = 0;
		int max = 0;
		for (int i = 0; i < rateArr.length; i++) {
			max += rateArr[i];
			if (low <= result && result < max) {
				return i;
			}
			low += rateArr[i];
		}
		return 0;
	}

	public String[] getRangeArticleNameArr() {
		return rangeArticleNameArr;
	}

	public void setRangeArticleNameArr(String[] rangeArticleNameArr) {
		this.rangeArticleNameArr = rangeArticleNameArr;
	}

	public int[] getColorArr() {
		return colorArr;
	}

	public void setColorArr(int[] colorArr) {
		this.colorArr = colorArr;
	}

	public int[] getNumArr() {
		return numArr;
	}

	public void setNumArr(int[] numArr) {
		this.numArr = numArr;
	}

	public int[] getRateArr() {
		return rateArr;
	}

	public void setRateArr(int[] rateArr) {
		this.rateArr = rateArr;
	}

	public boolean[] getBindArr() {
		return bindArr;
	}

	public void setBindArr(boolean[] bindArr) {
		this.bindArr = bindArr;
	}

	public List<DigRateArticle> getList() {
		return list;
	}

	public void setList(List<DigRateArticle> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "DigEventOfArticle [list=" + list + "]";
	}

}
