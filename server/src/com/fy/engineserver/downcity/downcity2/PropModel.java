package com.fy.engineserver.downcity.downcity2;

public class PropModel {
	/** 物品统计名 */
	private String articleCNNName;
	/** 物品颜色 */
	private int colorType;
	/** 物品数量 */
	private int num;
	/** 显示概率 */
	private int probabbly4Show;
	/** 抽中概率 */
	private int probabbly4Get;
	/** 临时物品id */
	private long tempArticleEntityId;
	/** 是否绑定 */
	private boolean bind;
	/** 加入线程时间 */
	private long startTime;
	
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj instanceof PropModel) {
			PropModel pm = (PropModel) obj;
			if (this.articleCNNName.equals(pm.getArticleCNNName()) && this.num == pm.getNum() && this.probabbly4Show == pm.getProbabbly4Show() && this.probabbly4Get == pm.getProbabbly4Get()) {
				return true;
			}
		}
		return false;
	}

	public PropModel(String articleCNNName, int colorType, int num, int probabbly4Show, int probabbly4Get, long tempArticleEntityId) {
		super();
		this.articleCNNName = articleCNNName;
		this.colorType = colorType;
		this.num = num;
		this.probabbly4Show = probabbly4Show;
		this.probabbly4Get = probabbly4Get;
		this.tempArticleEntityId = tempArticleEntityId;
		this.bind = false;
	}

	@Override
	public String toString() {
		return "PropModel [articleCNNName=" + articleCNNName + ", colorType=" + colorType + ", num=" + num + ", probabbly4Show=" + probabbly4Show + ", probabbly4Get=" + probabbly4Get + "]";
	}

	public String getArticleCNNName() {
		return articleCNNName;
	}
	public void setArticleCNNName(String articleCNNName) {
		this.articleCNNName = articleCNNName;
	}
	public int getColorType() {
		return colorType;
	}
	public void setColorType(int colorType) {
		this.colorType = colorType;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}


	public int getProbabbly4Get() {
		return probabbly4Get;
	}


	public void setProbabbly4Get(int probabbly4Get) {
		this.probabbly4Get = probabbly4Get;
	}
	public int getProbabbly4Show() {
		return probabbly4Show;
	}
	public void setProbabbly4Show(int probabbly4Show) {
		this.probabbly4Show = probabbly4Show;
	}

	public long getTempArticleEntityId() {
		return tempArticleEntityId;
	}

	public void setTempArticleEntityId(long tempArticleEntityId) {
		this.tempArticleEntityId = tempArticleEntityId;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isBind() {
		return bind;
	}

	public void setBind(boolean bind) {
		this.bind = bind;
	}
	
}
