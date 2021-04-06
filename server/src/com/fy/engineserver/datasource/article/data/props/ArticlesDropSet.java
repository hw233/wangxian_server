package com.fy.engineserver.datasource.article.data.props;


/**
 * 物品掉落集合
 * @author Administrator
 *
 */
public class ArticlesDropSet {

	/**
	 * 物品掉落集合名称
	 */
	private String name;
	
	/**
	 * 掉落内容，数组中是掉落物品的物品名和掉落几率
	 */
	private ArticleProperty dsps[];

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArticleProperty[] getDsps() {
		return dsps;
	}

	public void setDsps(ArticleProperty[] dsps) {
		this.dsps = dsps;
	}

}
