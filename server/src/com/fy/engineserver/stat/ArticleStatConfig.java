package com.fy.engineserver.stat;

import com.fy.engineserver.datasource.article.data.articles.Article;

public class ArticleStatConfig {

	private String 一级分类;
	private String[] 二级分类;

	public ArticleStatConfig(String 一级分类, String[] 二级分类) {
		super();
		this.一级分类 = 一级分类;
		this.二级分类 = 二级分类;
	}

	public String get一级分类() {
		return 一级分类;
	}

	public void set一级分类(String 一级分类) {
		this.一级分类 = 一级分类;
	}

	public String[] get二级分类() {
		return 二级分类;
	}

	public void set二级分类(String[] 二级分类) {
		this.二级分类 = 二级分类;
	}

	public boolean inConfig(Article article) {
		String article一级分类 = article.get物品一级分类();
		String article二级分类 = article.get物品二级分类();
		if (!一级分类.equals(article一级分类)) {
			return false;
		}
		if (二级分类 == null) {// 一级分类符合，二级分类不录入表示所有的二级分类都统计
			return true;
		}
		for (String 分类2 : 二级分类) {
			if (分类2.equals(article二级分类)) {
				return true;
			}
		}
		return false;
	}
}
