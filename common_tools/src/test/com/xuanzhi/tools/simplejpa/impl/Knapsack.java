package com.xuanzhi.tools.simplejpa.impl;



import com.xuanzhi.tools.simplejpa.annotation.*;

@SimpleEmbeddable
public class Knapsack {

	Cell cells[];
	
	int length;
	
	@SimpleEmbeddable
	public static class Cell{
		
		long articleId;
		int count;
		public Cell(){
			
		}
		public Cell(long id,int c){
			
		}
		
		public long getArticleId() {
			return articleId;
		}
		public void setArticleId(long articleId) {
			this.articleId = articleId;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
	}



	public Cell[] getCells() {
		return cells;
	}



	public void setCells(Cell[] cells) {
		this.cells = cells;
	}



	public int getLength() {
		return length;
	}



	public void setLength(int length) {
		this.length = length;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("length="+length+"");
		if(cells == null) return sb.toString();
		for(int i = 0 ; i < cells.length ; i++){
			if(cells[i] != null){
				sb.append(",["+cells[i].articleId+","+cells[i].count+"]");
			}else{
				sb.append(",[-,-]");
			}
		}
		return sb.toString();
	}
}
