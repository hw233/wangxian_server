package com.fy.engineserver.activity.fateActivity;

import java.util.Comparator;

import com.xuanzhi.tools.simplejpa.annotation.SimpleEmbeddable;


@SimpleEmbeddable
public class FinishRecord {

	
	public FinishRecord() {
		super();
	}
	public FinishRecord(long playerId, int num) {
		super();
		this.playerId = playerId;
		this.num = num;
	}

	private long playerId;
	private int num;
	
	public long getPlayerId() {
		return playerId;
	}
	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public void add(){
		setNum(this.num++);
	}
	
	public static class FinishRecordComparator implements Comparator<FinishRecord> {

		@Override
		public int compare(FinishRecord f1, FinishRecord f2) {
			
			int num1 = f1.getNum();
			int num2 = f2.getNum();

			if(num1 <= num2){
				return 1;
			}else{
				return 0;
			}
		}
		
	}
}
