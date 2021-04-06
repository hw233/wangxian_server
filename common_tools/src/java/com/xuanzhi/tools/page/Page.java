package com.xuanzhi.tools.page;


public class Page {

	private int totalRecord = 0;

	private int curPage = 1;

	private int totalPage = 1;

	private int prevPage = 0;

	private int nextPage = 0;
	
	private int sizeOfPage = 10;
	
	private boolean isFirstPage = false;

	private boolean isLastPage = false;

	private Page() {

	}

	public Page(int pageNum, int sizeOfPage, int totalRecord) {
		
		int curPage = (pageNum < 1) ? 1: pageNum;
		
		int totalPage = (((totalRecord % sizeOfPage) == 0)) ? (totalRecord / sizeOfPage)
				: ((totalRecord / sizeOfPage) + 1);
		curPage = (curPage > totalPage) ? totalPage : curPage;
		setSizeOfPage(sizeOfPage);
		setCurPage(curPage);
		setTotalRecord(totalRecord);
		setTotalPage(totalPage);
		setFirstPage((curPage <= 1) ? true : false);
		setLastPage((curPage >= totalPage) ? true : false);
		setPrevPage((!isFirstPage()) ? (curPage - 1) : 0);
		setNextPage((!isLastPage()) ? (curPage + 1) : 0);
	}

	public int getSizeOfPage() {
		return sizeOfPage;
	}

	public void setSizeOfPage(int sizeOfPage) {
		this.sizeOfPage = sizeOfPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(int prevPage) {
		this.prevPage = prevPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public boolean isFirstPage() {
		return isFirstPage;
	}

	public void setFirstPage(boolean isFirstPage) {
		this.isFirstPage = isFirstPage;
	}

	public boolean isLastPage() {
		return isLastPage;
	}

	public void setLastPage(boolean isLastPage) {
		this.isLastPage = isLastPage;
	}
	
	public int getStartIndex() {
		return (curPage - 1) * sizeOfPage;
	}
}
