package com.fy.engineserver.trade.requestbuy;

public class RequestBuyInfo4Client {

	private long requestBuyId;
	private String releasePlayerName;
	private String articleName;
	private long entityId;
	private String icon;
	private int color;
	private int colorType = -1;			//物品品质颜色是否是单色，如果是单色传单色索引，否则传-1
	private int grade;
	private int grademax;
	private String leftTime;
	private int leftNum;
	private long perPrice;
	private String mainType;
	private String subType;

	public long getRequestBuyId() {
		return requestBuyId;
	}

	public void setRequestBuyId(long requestBuyId) {
		this.requestBuyId = requestBuyId;
	}

	public String getReleasePlayerName() {
		return releasePlayerName;
	}

	public void setReleasePlayerName(String releasePlayerName) {
		this.releasePlayerName = releasePlayerName;
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

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getLeftTime() {
		return leftTime;
	}

	public void setLeftTime(String leftTime) {
		this.leftTime = leftTime;
	}

	public int getLeftNum() {
		return leftNum;
	}

	public void setLeftNum(int leftNum) {
		this.leftNum = leftNum;
	}

	public long getPerPrice() {
		return perPrice;
	}

	public void setPerPrice(long perPrice) {
		this.perPrice = perPrice;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	public String getMainType() {
		return mainType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getSubType() {
		if(subType==null){
			return "";
		}
		return subType;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setGrademax(int grademax) {
		this.grademax = grademax;
	}

	public int getGrademax() {
		return grademax;
	}

	public void setColorType(int colorType) {
		this.colorType = colorType;
	}

	public int getColorType() {
		return colorType;
	}

}
