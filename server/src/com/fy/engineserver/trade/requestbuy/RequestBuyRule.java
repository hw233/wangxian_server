package com.fy.engineserver.trade.requestbuy;


import com.xuanzhi.tools.simplejpa.annotation.SimpleColumn;
import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;

/**
 * 求购规则
 * 
 * 
 */
@SimpleEmbeddable
public class RequestBuyRule {

	/** 主类型名称*/
	@SimpleColumn(length=100)
	private String mainType;
	/** 子类型索引 */
	@SimpleColumn(length=100)
	private String subType;
	/** 显示的物品名(一般是物品名,装备则有特殊规则) */
	@SimpleColumn(length=100)
	private String outshowName;
	/** 物品名 */
	@SimpleColumn(length=100)
	private String articleName;
	/** 颜色 */
	private int color;
	/** 使用等级 */
	private int grade;
	/** 装备等级上限*/
	private int maxGrade;
	
	public RequestBuyRule(){}
	
	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getOutshowName() {
		return outshowName;
	}

	public void setOutshowName(String outshowName) {
		this.outshowName = outshowName;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setMaxGrade(int maxGrade) {
		this.maxGrade = maxGrade;
	}

	public int getMaxGrade() {
		return maxGrade;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		if(obj instanceof RequestBuyRule){
			RequestBuyRule buyRule = (RequestBuyRule)obj;
			if(buyRule.getMainType().equals(mainType)
					&&buyRule.getArticleName().equals(articleName)
					&&buyRule.getColor()==color){
				if(buyRule.getSubType()==null&&subType==null){
					return true;
				}else if(buyRule.getSubType()!=null&&buyRule.getSubType().equals(subType)){
					return true;
				}
			}
		}
		return false;
	}
}
