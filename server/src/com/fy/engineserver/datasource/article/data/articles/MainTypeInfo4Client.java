package com.fy.engineserver.datasource.article.data.articles;

public class MainTypeInfo4Client implements Comparable<MainTypeInfo4Client> {
	/** 显示的顺序 */
	private int index;
	/** 一级类型 */
	private String mainTypeName;
	/** 子类型 */
	private String[] subTypes;

	public MainTypeInfo4Client(int index, String mainTypeName, String[] subTypes) {
		this.index = index;
		this.mainTypeName = mainTypeName;
		this.subTypes = subTypes;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getMainTypeName() {
		return mainTypeName;
	}

	public void setMainTypeName(String mainTypeName) {
		this.mainTypeName = mainTypeName;
	}

	public String[] getSubTypes() {
		return subTypes;
	}

	public void setSubTypes(String[] subTypes) {
		this.subTypes = subTypes;
	}

	@Override
	public int compareTo(MainTypeInfo4Client o) {
		return o.getIndex() - getIndex();
	}
}
