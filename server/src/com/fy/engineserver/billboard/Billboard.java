package com.fy.engineserver.billboard;

import java.util.ArrayList;

public class Billboard {
	public BillboardData[] data;
	
	String name;
	
	String[] titles;
	
	public Billboard(String name,String[] titles){
		this.data=new BillboardData[BillboardManager.MAX_LINES];
		this.name=name;
		this.titles=titles;
	}
	
	public void setData(String[] rankObject,long[] value,String[] discription,long[] id){
		for(int i=0;i<rankObject.length;i++){
			this.data[i]=new BillboardData(i+1);
			this.data[i].setRankingObject(rankObject[i]);
			this.data[i].setValue(value[i]);
			this.data[i].setDescription(discription[i]);
			this.data[i].setId(id[i]);
		}
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getTitlesString(){
		String titleS="";
		for(int i=0;i<this.titles.length;i++){
			titleS+=this.titles[i];
			if(i<this.titles.length-1){
				titleS+=":";
			}
		}
		return titleS;
	}
	
	public String[] getTitles(){
		return this.titles;
	}
	
	/**
	 * return the rank of the specified name
	 * @param name
	 * @return
	 */
	public int getRank(String name){
		int rank=0;
		if(this.data!=null){
			for(int i=0;i<this.data.length;i++){
				if(this.data[i]!=null){
					if(this.data[i].getRankingObject().equals(name)){
						rank=this.data[i].getRank();
						break;
					}
				}
			}
			return rank;
		}else{
			return -1;
		}
	}
	
	public int getRank(long id){
		int rank=0;
		if(this.data!=null){
			for(int i=0;i<this.data.length;i++){
				if(this.data[i]!=null){
					if(this.data[i].getId()==id){
						rank=this.data[i].getRank();
						break;
					}
				}
			}
			return rank;
		}else{
			return -1;
		}
	}
	
	public BillboardData[] getBillboardData(int lines){
		ArrayList<BillboardData> al=new ArrayList<BillboardData>();
		if(this.data!=null&&this.data.length>0){
			int num=0;
			if(lines<BillboardManager.MAX_LINES){
				num=lines;
			}else{
				num=BillboardManager.MAX_LINES;
			}
			if(num>this.data.length){
				num=this.data.length;
			}
			for(int i=0;i<num;i++){
				if(this.data[i]!=null){
					al.add(this.data[i]);
				}else{
					break;
				}
			}
		}
		BillboardData[] bd=new BillboardData[al.size()];
		for(int i=0;i<bd.length;i++){
			bd[i]=al.get(i);
		}
		return bd;
	}
}
