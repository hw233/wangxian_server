package com.fy.engineserver.datasource.article.data.articles;



//采花得到的物品，可以兑换经验，元气等
public class PickFlowerArticle extends Article {

	//分类,兑换经验的，还是元气的
	private int flowerType;
	private String flowerTypeDes;
	
	//白色得分(根据颜色取不同比例);
	private int score;
	
	
	
	public int getFlowerType() {
		return flowerType;
	}
	public void setFlowerType(int flowerType) {
		this.flowerType = flowerType;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public String getFlowerTypeDes() {
		return flowerTypeDes;
	}
	public void setFlowerTypeDes(String flowerTypeDes) {
		this.flowerTypeDes = flowerTypeDes;
	}
	
	
}
