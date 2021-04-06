package com.fy.engineserver.trade.requestbuy;

//一级分类和其绑定的二级分类   原只有求购用，后拍卖也用
public class RequestOption {

	private String first;
	
	private String[] second;

	public RequestOption(){
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getFirst() {
		return first;
	}

	public void setSecond(String[] second) {
		this.second = second;
	}

	public String[] getSecond() {
		return second;
	}
	
}
