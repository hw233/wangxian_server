package com.fy.engineserver.activity.monsterDrop;

import java.util.ArrayList;

import com.fy.engineserver.datasource.article.manager.ArticleManager;

public class MonsterDropInfo {

	private String erjiName;				//二级分类名字
	
	private String propName;				//物品名字
	
	private int colorType;					//颜色
	
	private boolean isBind;					//是否绑定
	
	private int num;						//掉落个数
	
	private int dropRandom;					//掉落几率
	
	private int dropLevelMin;				//掉落最小等级
	
	private int dropLevelMax;				//掉落最大等级
	
	private ArrayList<String> dropArticleNames;
	
	public void createDropArticleNames(){
		dropArticleNames = new ArrayList<String>();
		if (propName == null || "".equals(propName)) {
			
		}else {
			dropArticleNames.add(propName);
		}
	}
	
	public void setErjiName(String erjiName) {
		this.erjiName = erjiName;
	}

	public String getErjiName() {
		return erjiName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getPropName() {
		return propName;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int getColorType() {
		return colorType;
	}

	public void setBind(boolean isBind) {
		this.isBind = isBind;
	}

	public boolean isBind() {
		return isBind;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public void setDropRandom(int dropRandom) {
		this.dropRandom = dropRandom;
	}

	public int getDropRandom() {
		return dropRandom;
	}

	public void setDropLevelMin(int dropLevelMin) {
		this.dropLevelMin = dropLevelMin;
	}

	public int getDropLevelMin() {
		return dropLevelMin;
	}

	public void setDropLevelMax(int dropLevelMax) {
		this.dropLevelMax = dropLevelMax;
	}

	public int getDropLevelMax() {
		return dropLevelMax;
	}

	public void setDropArticleNames(ArrayList<String> dropArticleNames) {
		this.dropArticleNames = dropArticleNames;
	}

	public ArrayList<String> getDropArticleNames() {
		return dropArticleNames;
	}
	
	public String getLogString() {
		StringBuffer sb = new StringBuffer("");
		sb.append(" [分类:").append(erjiName).append("]");
		sb.append(" [名字:").append(propName).append("]");
		sb.append(" [color:").append(colorType).append("]");
		sb.append(" [绑定:").append(isBind).append("]");
		sb.append(" [数量:").append(num).append("]");
		sb.append(" [几率:").append(dropRandom).append("]");
		sb.append(" [min:").append(dropLevelMin).append("]");
		sb.append(" [max:").append(dropLevelMax).append("]");
		
		return sb.toString();
	}
}
