package com.fy.engineserver.validate;

import java.util.Arrays;

import com.fy.engineserver.enterlimit.EnterLimitManager;

/**
 * 一致性题目
 * 
 *
 */
public class IdenticalValidateAsk extends ValidateAsk {

	private int askType;
	
	private String[] asks;

	/**
	 * 输入的答案是否正确
	 * @param input
	 * @return
	 */
	public boolean isRight(String input) {
		String[] indexs = input.split(",");
		try{
			int in0 = Integer.parseInt(indexs[0]);
			int in1 = Integer.parseInt(indexs[1]);
			if (in0<0 || in1<0) {
				return false;
			}
			if (asks[in0].equals(asks[in1])) {
				return true;
			}
		}catch(Exception e) {
			EnterLimitManager.logger.error("解析答案出错:", e);
		}
		return false;
	}

	@Override
	public String toString() {
		return "ValidateAsk [类型"+askType+"] ["+getAskFormType()+"] [size="+asks.length+"] [asks="+Arrays.toString(asks)+"]";
	}

	public void setAskType(int askType) {
		this.askType = askType;
	}

	public int getAskType() {
		return askType;
	}
	
	public String[] getAsks() {
		return asks;
	}

	public void setAsks(String[] asks) {
		this.asks = asks;
	}
}
